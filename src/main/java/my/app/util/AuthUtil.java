package my.app.util;

import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class AuthUtil {

  public static final int KEY_SIZE = 16;

  public static final int PASSWORD_MIN_LENGTH = 4;
  public static final int PASSWORD_MAX_LENGTH = 100;

  public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

  public static final String ADMIN = "ROLE_ADMIN";
  public static final String USER = "ROLE_USER";
  public static final String ANONYMOUS = "ROLE_ANONYMOUS";

  public static final String SYSTEM_ACCOUNT = "system";

  public static String generatePassword() {
    return RandomStringUtils.randomAlphanumeric(KEY_SIZE);
  }

  public static String generateActivationKey() {
    return RandomStringUtils.randomNumeric(KEY_SIZE);
  }

  public static boolean checkPasswordLength(String password) {
    return !StringUtils.isEmpty(password) && password.length() >= PASSWORD_MIN_LENGTH
        && password.length() <= PASSWORD_MAX_LENGTH;
  }

  public static Optional<String> getCurrentUserLogin() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
      if (authentication.getPrincipal() instanceof UserDetails) {
        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
        return springSecurityUser.getUsername();
      }
      return null;
    });
  }

  public static Optional<String> getCurrentUserJwt() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication())
        .filter(authentication -> authentication.getCredentials() instanceof String)
        .map(authentication -> (String) authentication.getCredentials());
  }

  public static boolean isAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> authentication
        .getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ANONYMOUS)))
        .orElse(false);
  }

  public static boolean isCurrentUserHasAuthority(String authority) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> authentication
        .getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
        .orElse(false);
  }

  private AuthUtil() {
  }
}
