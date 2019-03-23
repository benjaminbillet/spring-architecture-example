package my.app.api.errors;

import java.net.URI;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import my.app.config.ApplicationProperties;

public class InternalServerErrorException extends AbstractThrowableProblem {
  private static final long serialVersionUID = -129159507295156763L;

  public InternalServerErrorException(ApplicationProperties config, String message) {
    this(config.getRfc7807().getDefaultTypeUri(), message);
  }

  public InternalServerErrorException(URI type, String message) {
    super(type, message, Status.INTERNAL_SERVER_ERROR);
  }
}
