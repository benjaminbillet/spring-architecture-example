package my.app.util;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthUtil {

  static final int KEY_SIZE = 16;

  static final int PASSWORD_MIN_LENGTH = 4;
  static final int PASSWORD_MAX_LENGTH = 100;

  static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

  static final String ADMIN = "ROLE_ADMIN";
  static final String USER = "ROLE_USER";
  static final String ANONYMOUS = "ROLE_ANONYMOUS";

  static String generatePassword() {
    return RandomStringUtils.randomAlphanumeric(KEY_SIZE);
  }

  static String generateActivationKey() {
    return RandomStringUtils.randomNumeric(KEY_SIZE);
  }

  static boolean checkPasswordLength(String password) {
    return !StringUtils.isEmpty(password) && password.length() >= PASSWORD_MIN_LENGTH
        && password.length() <= PASSWORD_MAX_LENGTH;
  }

  static Optional<String> getCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
      if (authentication.getPrincipal() instanceof UserDetails) {
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        return springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
        return (String) authentication.getPrincipal();
      }
      return null;
    });
  }

  static Optional<String> getCurrentUserJwt() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication())
        .filter(authentication -> authentication.getCredentials() instanceof String)
        .map(authentication -> (String) authentication.getCredentials());
  }

  static boolean isAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> authentication
        .getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ANONYMOUS)))
        .orElse(false);
  }

  static boolean isCurrentUserHasAuthority(String authority) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> authentication
        .getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
        .orElse(false);
  }
}
