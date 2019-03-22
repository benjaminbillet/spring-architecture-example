package my.app.api.errors;

import org.zalando.problem.Status;

import my.app.config.ApplicationProperties;

import java.net.URI;

public class NotFoundException extends AbstractException {

  private static final long serialVersionUID = -8659688443627971341L;

  public NotFoundException(ApplicationProperties config, String title, String entityName, String errorKey) {
    this(config.getRfc7807().getEntityNotFoundTypeUri(), title, entityName, errorKey);
  }

  public NotFoundException(URI type, String title, String entityName, String errorKey) {
    super(type, title, Status.NOT_FOUND, entityName, errorKey);
  }
}
