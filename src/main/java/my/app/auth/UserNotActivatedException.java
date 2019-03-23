package my.app.auth;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {

  private static final long serialVersionUID = -6517221600385886530L;

  public UserNotActivatedException(String message) {
    super(message);
  }

  public UserNotActivatedException(String message, Throwable t) {
    super(message, t);
  }
}
