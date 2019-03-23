package my.app.api.errors;

import my.app.config.ApplicationProperties;

public class LoginAlreadyUsedException extends BadRequestException {

  private static final long serialVersionUID = -7447715303139803692L;

  public LoginAlreadyUsedException(ApplicationProperties config) {
    super(config.getRfc7807().getLoginAlreadyUsedTypeUri(), "Login name already used!", "user", "user-exists");
  }
}
