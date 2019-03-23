package my.app.api.errors;

import my.app.config.ApplicationProperties;

public class EmailAlreadyUsedException extends BadRequestException {

  private static final long serialVersionUID = -918427152102076172L;

  public EmailAlreadyUsedException(ApplicationProperties config) {
    super(config.getRfc7807().getEmailAlreadyUsedTypeUri(), "Email is already in use!", "user", "email-exists");
  }
}
