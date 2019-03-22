package my.app.api.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public abstract class AbstractException extends AbstractThrowableProblem {

  private static final long serialVersionUID = -2883323595600513869L;

  private final String entityName;
  private final String errorKey;

  public AbstractException(URI type, String title, Status status, String entityName, String errorKey) {
    super(type, title, status, null, null, null, toProblemParameters(entityName, errorKey));
    this.entityName = entityName;
    this.errorKey = errorKey;
  }

  public String getEntityName() {
    return entityName;
  }

  public String getErrorKey() {
    return errorKey;
  }

  private static Map<String, Object> toProblemParameters(String entityName, String errorKey) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("message", "error." + errorKey);
    parameters.put("params", entityName);
    return parameters;
  }
}
