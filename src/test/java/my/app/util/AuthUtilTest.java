package my.app.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

// a test for various authentication utilities
public class AuthUtilTest {

  @Test
  public void testGetCurrentUserLogin() {
    setBogusSecurityContext(null);
    Optional<String> login = AuthUtil.getCurrentUserLogin();
    assertThat(login).contains("principal");
  }

  @Test
  public void testGetCurrentUserJwt() {
    setBogusSecurityContext(null);
    Optional<String> jwt = AuthUtil.getCurrentUserJwt();
    assertThat(jwt).contains("credentials");
  }

  @Test
  public void testIsAuthenticated() {
    setBogusSecurityContext(null);
    assertThat(AuthUtil.isAuthenticated()).isTrue();
  }

  @Test
  public void testAnonymousIsNotAuthenticated() {
    setBogusSecurityContext(AuthUtil.ANONYMOUS);
    assertThat(AuthUtil.isAuthenticated()).isFalse();
  }

  @Test
  public void testIsCurrentUserHasAuthority() {
    setBogusSecurityContext(AuthUtil.USER);
    assertThat(AuthUtil.isCurrentUserHasAuthority(AuthUtil.USER)).isTrue();
    assertThat(AuthUtil.isCurrentUserHasAuthority(AuthUtil.ADMIN)).isFalse();
  }

  private void setBogusSecurityContext(String authority) {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    if (authority != null) {
      Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(authority));
      securityContext
          .setAuthentication(new UsernamePasswordAuthenticationToken("principal", "credentials", authorities));
    } else {
      securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("principal", "credentials"));
    }
    SecurityContextHolder.setContext(securityContext);
  }
}
