package my.app.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import my.app.config.ApplicationProperties;
import my.app.util.AuthUtil;

public class JwtFilterTest {

  private ApplicationProperties config;
  private JwtTokenProvider tokenProvider;
  private JwtFilter jwtFilter;

  @Before
  public void setup() {
    config = new ApplicationProperties();
    config.setJwt(new ApplicationProperties.Jwt());

    config.getJwt().setTokenValiditySeconds(60);
    config.getJwt().setSecretBase64("VGhlIEpXVCBKV0EgU3BlY2lmaWNhdGlvbiAoUkZDIDc1MTgsIFNlY3Rpb24gMy4yKSBzdGF0ZXMgdGhhdCBrZXlzIHVzZWQgd2l0aCBITUFDLVNIQSBhbGdvcml0aG1zIE1VU1QgaGF2ZSBhIHNpemUgPj0gMjU2IGJpdHM=");

    tokenProvider = new JwtTokenProvider(config);
    tokenProvider.init();

    jwtFilter = new JwtFilter(tokenProvider);
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  @Test
  public void testJwtFilter() throws Exception {
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
      "principal",
      "credentials",
      Collections.singletonList(new SimpleGrantedAuthority(AuthUtil.USER))
    );

    String token = tokenProvider.createToken(authentication, false);
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    request.setRequestURI("/api/time");

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("principal");
    assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()).isEqualTo(token);
  }

  @Test
  public void testJwtFilterInvalidToken() throws Exception {
    String token = "invalid token";
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    request.setRequestURI("/api/time");

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  public void testJwtFilterMissingToken() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/time");

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  public void testJwtFilterEmptyHeader() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ");
    request.setRequestURI("/api/time");

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  public void testJwtFilterWrongScheme() throws Exception {
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
      "principal",
      "credentials",
      Collections.singletonList(new SimpleGrantedAuthority(AuthUtil.USER))
    );

    String token = tokenProvider.createToken(authentication, false);
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + token);
    request.setRequestURI("/api/time");

    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();
    jwtFilter.doFilter(request, response, filterChain);

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }
}
