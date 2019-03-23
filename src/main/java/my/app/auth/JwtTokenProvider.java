package my.app.auth;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import my.app.config.ApplicationProperties;

@Component
public class JwtTokenProvider {

  public static final String AUTHORITIES_KEY = "auth";

  private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

  private final ApplicationProperties config;

  private Key key;

  public JwtTokenProvider(ApplicationProperties config) {
    this.config = config;
  }

  @PostConstruct
  public void init() {
    log.debug("Using a Base64-encoded JWT secret key");
    byte[] keyBytes = Decoders.BASE64.decode(config.getJwt().getSecretBase64());
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(Authentication authentication, boolean rememberMe) {
    String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    Instant validity = Instant.now();
    if (rememberMe) {
      validity = validity.plus(config.getJwt().getTokenValidityRememberMeSeconds(), ChronoUnit.SECONDS);
    } else {
      validity = validity.plus(config.getJwt().getTokenValiditySeconds(), ChronoUnit.SECONDS);
    }

    return Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
        .signWith(key, SignatureAlgorithm.HS512).setExpiration(Date.from(validity)).compact();
  }

  public Authentication verifyToken(String token) {
    if (StringUtils.isBlank(token)) {
      return null;
    }
    try {
      Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

      Collection<? extends GrantedAuthority> authorities = Arrays
          .stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());

      User principal = new User(claims.getSubject(), "", authorities);
      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT signature: {}", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token: {}", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token: {}", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT token compact of handler are invalid: {}", e);
    }

    return null;
  }
}
