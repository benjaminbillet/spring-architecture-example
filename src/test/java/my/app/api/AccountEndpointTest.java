package my.app.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import my.app.App;
import my.app.config.ApplicationProperties;
import my.app.config.ExceptionTranslator;
import my.app.domain.Authority;
import my.app.domain.User;
import my.app.dto.UserDto;
import my.app.dto.UserMapper;
import my.app.repository.UserRepository;
import my.app.service.UserService;
import my.app.util.AuthUtil;
import my.app.util.JsonUtil;
import my.app.util.UserTestUtil;
import my.app.vdo.UserVdo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class AccountEndpointTest {

  @Autowired
  private ApplicationProperties config;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private ExceptionTranslator exceptionTranslator;

  @Autowired
  private CacheManager cacheManager;

  @Before
  public void setup() {
    // clear all caches
    cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
  }

  @Test
  @Transactional
  public void testRegister() throws Exception {
    int nbUsersBefore = userRepository.findAll().size();

    UserVdo vdo = new UserVdo();
    vdo.setLogin(UserTestUtil.JOHNDOE_LOGIN);
    vdo.setPassword(UserTestUtil.JOHNDOE_PASSWORD);
    vdo.setFirstName(UserTestUtil.JOHNDOE_FIRSTNAME);
    vdo.setLastName(UserTestUtil.JOHNDOE_LASTNAME);
    vdo.setEmail(UserTestUtil.JOHNDOE_EMAIL);

    // these fields will be ignored
    vdo.setActivated(true);
    vdo.setAuthorities(Collections.singleton(AuthUtil.ADMIN));

    getMockEndpoint().perform(post("/api/auth/register")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(JsonUtil.toJsonBytes(vdo)))
      .andExpect(status().isCreated());

    List<User> users = userRepository.findAll();
    assertThat(users).hasSize(nbUsersBefore + 1);

    User user = userRepository.findOneByEmailIgnoreCase(UserTestUtil.JOHNDOE_EMAIL).get();
    assertThat(user.getLogin()).isEqualTo(UserTestUtil.JOHNDOE_LOGIN);
    assertThat(user.getFirstName()).isEqualTo(UserTestUtil.JOHNDOE_FIRSTNAME);
    assertThat(user.getLastName()).isEqualTo(UserTestUtil.JOHNDOE_LASTNAME);
    assertThat(user.getEmail()).isEqualTo(UserTestUtil.JOHNDOE_EMAIL);
    assertThat(user.getActivated()).isFalse();
    assertThat(user.getAuthorities()).containsExactly(Authority.of(AuthUtil.USER));
  }

  @Test
  @Transactional
  public void testRegisterWithId() throws Exception {
    int nbUsersBefore = userRepository.findAll().size();

    UserVdo vdo = new UserVdo();
    vdo.setId(1L);
    vdo.setLogin(UserTestUtil.JOHNDOE_LOGIN);
    vdo.setPassword(UserTestUtil.JOHNDOE_PASSWORD);
    vdo.setFirstName(UserTestUtil.JOHNDOE_FIRSTNAME);
    vdo.setLastName(UserTestUtil.JOHNDOE_LASTNAME);
    vdo.setEmail(UserTestUtil.JOHNDOE_EMAIL);

    // this API call must fail
    getMockEndpoint().perform(post("/api/auth/register")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(JsonUtil.toJsonBytes(vdo)))
      .andExpect(status().isBadRequest());

    List<User> users = userRepository.findAll();
    assertThat(users).hasSize(nbUsersBefore);
  }

  @Test
  @Transactional
  public void restRegisterWithExistingUser() throws Exception {
    userRepository.saveAndFlush(UserTestUtil.createJohnDoe(null));
    int nbUsersBefore = userRepository.findAll().size();

    UserVdo vdo = new UserVdo();
    vdo.setLogin(UserTestUtil.JOHNDOE_LOGIN); // this login is already be used
    vdo.setPassword(UserTestUtil.JOHNDOE_PASSWORD);
    vdo.setFirstName(UserTestUtil.JOHNDOE_FIRSTNAME);
    vdo.setLastName(UserTestUtil.JOHNDOE_LASTNAME);
    vdo.setEmail("someoneelse@localhost");

    getMockEndpoint().perform(post("/api/auth/register")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(JsonUtil.toJsonBytes(vdo)))
      .andExpect(status().isBadRequest());

    List<User> users = userRepository.findAll();
    assertThat(users).hasSize(nbUsersBefore);
  }

  @Test
  @Transactional
  public void testRegisterWithExistingMail() throws Exception {
    userRepository.saveAndFlush(UserTestUtil.createJohnDoe(null));
    int nbUsersBefore = userRepository.findAll().size();

    UserVdo vdo = new UserVdo();
    vdo.setLogin("someoneelse");
    vdo.setPassword(UserTestUtil.JOHNDOE_PASSWORD);
    vdo.setFirstName(UserTestUtil.JOHNDOE_FIRSTNAME);
    vdo.setLastName(UserTestUtil.JOHNDOE_LASTNAME);
    vdo.setEmail(UserTestUtil.JOHNDOE_EMAIL);// this email is already be used
    vdo.setActivated(true);
 
    getMockEndpoint().perform(post("/api/auth/register")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(JsonUtil.toJsonBytes(vdo)))
      .andExpect(status().isBadRequest());

    List<User> users = userRepository.findAll();
    assertThat(users).hasSize(nbUsersBefore);
  }
  

  @Test
  @Transactional
  public void testActivate() throws Exception {
    User user = UserTestUtil.createJohnDoe(null);
    user.setActivated(false);
    user.setActivationKey(RandomStringUtils.random(20));
    userRepository.saveAndFlush(user);

    getMockEndpoint().perform(get("/api/auth/activate?key=" + user.getActivationKey()))
      .andExpect(status().isOk());

    user = userRepository.findOneByEmailIgnoreCase(UserTestUtil.JOHNDOE_EMAIL).get();
    assertThat(user.getActivated()).isTrue();
  }

  @Test
  @Transactional
  public void testActivateWrongKey() throws Exception {
    User user = UserTestUtil.createJohnDoe(null);
    user.setActivated(false);
    user.setActivationKey(RandomStringUtils.random(20));
    userRepository.saveAndFlush(user);

    getMockEndpoint().perform(get("/api/auth/activate?key=wrongkey"))
      .andExpect(status().isInternalServerError());

    user = userRepository.findOneByEmailIgnoreCase(UserTestUtil.JOHNDOE_EMAIL).get();
    assertThat(user.getActivated()).isFalse();
  }

  @Test
  public void testUserDtoToEntity() {
    UserDto dto = new UserDto();
    dto.setId(1L);
    dto.setLogin(UserTestUtil.JOHNDOE_LOGIN);
    dto.setFirstName(UserTestUtil.JOHNDOE_FIRSTNAME);
    dto.setLastName(UserTestUtil.JOHNDOE_LASTNAME);
    dto.setEmail(UserTestUtil.JOHNDOE_EMAIL);
    dto.setActivated(true);
    dto.setAuthorities(Collections.singleton(AuthUtil.USER));

    User user = userMapper.toEntity(dto);
    assertThat(user.getLogin()).isEqualTo(UserTestUtil.JOHNDOE_LOGIN);
    assertThat(user.getFirstName()).isEqualTo(UserTestUtil.JOHNDOE_FIRSTNAME);
    assertThat(user.getLastName()).isEqualTo(UserTestUtil.JOHNDOE_LASTNAME);
    assertThat(user.getEmail()).isEqualTo(UserTestUtil.JOHNDOE_EMAIL);
    assertThat(user.getCreatedDate()).isNotNull();
    assertThat(user.getId()).isEqualTo(null); // the mapper ignore activated
    assertThat(user.getActivated()).isEqualTo(false); // the mapper ignore activated
    assertThat(user.getAuthorities()).isEmpty(); // the mapper ignore authorities
  }

  @Test
  public void testUserEntityToDto() {
    User user = UserTestUtil.createJohnDoe(null);
    user.setId(1L);
    user.setCreatedDate(Instant.now());

    UserDto dto = userMapper.toDto(user);
    assertThat(dto.getId()).isEqualTo(1L);
    assertThat(dto.getLogin()).isEqualTo(UserTestUtil.JOHNDOE_LOGIN);
    assertThat(dto.getFirstName()).isEqualTo(UserTestUtil.JOHNDOE_FIRSTNAME);
    assertThat(dto.getLastName()).isEqualTo(UserTestUtil.JOHNDOE_LASTNAME);
    assertThat(dto.getEmail()).isEqualTo(UserTestUtil.JOHNDOE_EMAIL);
    assertThat(dto.isActivated()).isTrue();
    assertThat(dto.getAuthorities()).containsExactly(AuthUtil.USER);
    assertThat(dto.toString()).isNotNull();
  }

  private MockMvc getMockEndpoint() {
    AccountEndpoint endpoint = new AccountEndpoint(config, userService);
    return MockMvcBuilders.standaloneSetup(endpoint)
      .setCustomArgumentResolvers(pageableArgumentResolver)
      .setControllerAdvice(exceptionTranslator)
      .setMessageConverters(jacksonMessageConverter)
      .build();
  }
}
