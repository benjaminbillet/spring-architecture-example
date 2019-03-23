package my.app.auth;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import my.app.domain.User;
import my.app.repository.UserRepository;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String login) {
    log.debug("Authenticating: {}", login);

    // allow to authenticate using email address
    if (new EmailValidator().isValid(login, null)) {
      return userRepository.findOneWithAuthoritiesByEmail(login).map(user -> createSpringSecurityUser(login, user))
          .orElseThrow(() -> new UsernameNotFoundException("Could not find user email: " + login));
    }

    // allow to authenticate with login name
    String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
    return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
        .map(user -> createSpringSecurityUser(lowercaseLogin, user))
        .orElseThrow(() -> new UsernameNotFoundException("Could not find user login: " + lowercaseLogin));

  }

  private org.springframework.security.core.userdetails.User createSpringSecurityUser(String login,
      User user) {
    if (!user.getActivated()) {
      throw new UserNotActivatedException("User not activated: " + login);
    }
    List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
    return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
        grantedAuthorities);
  }
}
