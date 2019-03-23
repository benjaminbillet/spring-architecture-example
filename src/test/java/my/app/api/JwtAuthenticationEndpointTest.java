package my.app.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import my.app.App;
import my.app.auth.JwtTokenProvider;
import my.app.config.ExceptionTranslator;
import my.app.domain.User;
import my.app.repository.UserRepository;
import my.app.util.JsonUtil;
import my.app.vdo.LoginVdo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class JwtAuthenticationEndpointTest {

  @Autowired
  private JwtTokenProvider tokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Test
  @Transactional
  public void testAuthenticate() throws Exception {
    User user = new User();
    user.setLogin("test-user");
    user.setEmail("test-user@example.com");
    user.setActivated(true);
    user.setPassword(passwordEncoder.encode("password"));

    userRepository.saveAndFlush(user);

    LoginVdo login = new LoginVdo();
    login.setUsername("test-user");
    login.setPassword("password");
    getMockEndpoint()
        .perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(JsonUtil.toJsonBytes(login)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id_token").isString())
        .andExpect(jsonPath("$.id_token").isNotEmpty())
        .andExpect(header().string(HttpHeaders.AUTHORIZATION, not(nullValue())))
        .andExpect(header().string(HttpHeaders.AUTHORIZATION, not(isEmptyString())));
  }

  @Test
  @Transactional
  public void testAuthenticateRememberMe() throws Exception {
    User user = new User();
    user.setLogin("test-user-remember");
    user.setEmail("test-user-remember@example.com");
    user.setActivated(true);
    user.setPassword(passwordEncoder.encode("password"));

    userRepository.saveAndFlush(user);

    LoginVdo login = new LoginVdo();
    login.setUsername("test-user-remember");
    login.setPassword("password");
    login.setRememberMe(true);
    getMockEndpoint()
        .perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(JsonUtil.toJsonBytes(login)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id_token").isString())
        .andExpect(jsonPath("$.id_token").isNotEmpty())
        .andExpect(header().string(HttpHeaders.AUTHORIZATION, not(nullValue())))
        .andExpect(header().string(HttpHeaders.AUTHORIZATION, not(isEmptyString())));
  }

  @Test
  @Transactional
  public void testAuthenticateWrongPassword() throws Exception {
    LoginVdo login = new LoginVdo();
    login.setUsername("wrong-test-user");
    login.setPassword("wrongpassword");
    getMockEndpoint()
        .perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(JsonUtil.toJsonBytes(login)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.id_token").doesNotExist())
        .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION));
  }

  private MockMvc getMockEndpoint() {
    JwtAuthenticationEndpoint endpoint = new JwtAuthenticationEndpoint(tokenProvider, authenticationManager);
    return MockMvcBuilders.standaloneSetup(endpoint).setControllerAdvice(exceptionTranslator).build();
  }
}
