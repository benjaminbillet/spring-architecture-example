package my.app.api.errors;

import java.net.URI;

import org.zalando.problem.Status;

import my.app.config.ApplicationProperties;

public class BadRequestException extends AbstractException {

  private static final long serialVersionUID = -8659688443627971341L;

  public BadRequestException(ApplicationProperties config, String title, String entityName, String errorKey) {
    this(config.getRfc7807().getDefaultTypeUri(), title, entityName, errorKey);
  }

  public BadRequestException(URI type, String title, String entityName, String errorKey) {
    super(type, title, Status.BAD_REQUEST, entityName, errorKey);
  }
}
