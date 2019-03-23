package my.app.api.errors;

import org.zalando.problem.Status;

import my.app.config.ApplicationProperties;

public class InvalidPasswordException extends AbstractException {

  private static final long serialVersionUID = 7702230986148770842L;

  public InvalidPasswordException(ApplicationProperties config) {
    super(config.getRfc7807().getInvalidPasswordTypeUri(), "Incorrect password", Status.UNAUTHORIZED, "password",
        "invalid-password");
  }
}
