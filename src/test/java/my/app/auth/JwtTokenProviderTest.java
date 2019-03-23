package my.app.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import my.app.config.ApplicationProperties;
import my.app.util.AuthUtil;

public class JwtTokenProviderTest {
  private ApplicationProperties config;
  private JwtTokenProvider tokenProvider;

  @Before
  public void setup() {
    config = new ApplicationProperties();
    config.setJwt(new ApplicationProperties.Jwt());

    config.getJwt().setTokenValiditySeconds(60);
    config.getJwt().setSecretBase64("VGhlIEpXVCBKV0EgU3BlY2lmaWNhdGlvbiAoUkZDIDc1MTgsIFNlY3Rpb24gMy4yKSBzdGF0ZXMgdGhhdCBrZXlzIHVzZWQgd2l0aCBITUFDLVNIQSBhbGdvcml0aG1zIE1VU1QgaGF2ZSBhIHNpemUgPj0gMjU2IGJpdHM=");
  
    tokenProvider = new JwtTokenProvider(config);
    tokenProvider.init();
  }

  @Test
  public void testInvalidSignature() {
    Key otherKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(config.getJwt().getSecretBase64().substring(10)));

    String invalidToken = Jwts.builder()
      .signWith(otherKey, SignatureAlgorithm.HS512)
      .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)))
      .compact();

    Authentication auth = tokenProvider.verifyToken(invalidToken);
    assertThat(auth).isNull();
  }

  @Test
  public void testJwtMalformed() {
    Authentication authentication = createAuthentication();
    String token = tokenProvider.createToken(authentication, false);
    String invalidToken = token.substring(1);
    Authentication auth = tokenProvider.verifyToken(invalidToken);
    assertThat(auth).isNull();
  }

  @Test
  public void testJwtTokenExpired() {
    config.getJwt().setTokenValiditySeconds(-60);

    Authentication authentication = createAuthentication();
    String token = tokenProvider.createToken(authentication, false);

    Authentication auth = tokenProvider.verifyToken(token);
    assertThat(auth).isNull();
  }

  @Test
  public void testJwtUnsupportedToken() {
    byte[] keyBytes = Decoders.BASE64.decode(config.getJwt().getSecretBase64());
    Key key = Keys.hmacShaKeyFor(keyBytes);

    String unsupportedToken = Jwts.builder()
      .setPayload("payload")
      .signWith(key, SignatureAlgorithm.HS512)
      .compact();

    Authentication auth = tokenProvider.verifyToken(unsupportedToken);
    assertThat(auth).isNull();
  }

  @Test
  public void testJwtEmptyToken() {
    Authentication auth = tokenProvider.verifyToken("");
    assertThat(auth).isNull();
  }

  private Authentication createAuthentication() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthUtil.USER));
    return new UsernamePasswordAuthenticationToken("principal", "credentials", authorities);
  }
}
