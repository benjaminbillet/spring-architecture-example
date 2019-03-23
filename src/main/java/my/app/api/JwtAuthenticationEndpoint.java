package my.app.api;

import javax.validation.Valid;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import my.app.auth.JwtToken;
import my.app.auth.JwtTokenProvider;
import my.app.vdo.LoginVdo;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthenticationEndpoint {

  private final JwtTokenProvider tokenProvider;

  private final AuthenticationManager authenticationManager;

  public JwtAuthenticationEndpoint(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
    this.tokenProvider = tokenProvider;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/authenticate")
  public ResponseEntity<JwtToken> authorize(@Valid @RequestBody LoginVdo vdo) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        vdo.getUsername(), vdo.getPassword());

    Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    boolean rememberMe = BooleanUtils.toBoolean(vdo.isRememberMe());
    String token = tokenProvider.createToken(authentication, rememberMe);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(token);
    return ResponseEntity.ok().headers(httpHeaders).body(new JwtToken(token));    
  }
}
