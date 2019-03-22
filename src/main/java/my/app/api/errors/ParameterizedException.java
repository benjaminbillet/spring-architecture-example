package my.app.api.errors;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import my.app.config.ApplicationProperties;

public class ParameterizedException extends AbstractThrowableProblem {

  private static final long serialVersionUID = 3231847082381413519L;

  public ParameterizedException(ApplicationProperties config, Status status, String message, String... params) {
    this(config.getRfc7807().getDefaultTypeUri(), status, message, params);
  }

  public ParameterizedException(URI type, Status status, String message, String... params) {
    super(type, message, status, null, null, null, MapUtils.putAll(new HashMap<>(), params));
  }

  public ParameterizedException(ApplicationProperties config, Status status, String message, Map<String, Object> params) {
    this(config.getRfc7807().getDefaultTypeUri(), status, message, params);
  }

  public ParameterizedException(URI type, Status status, String message, Map<String, Object> params) {
    super(type, message, status, null, null, null, toProblemParameters(message, params));
  }

  public static Map<String, Object> toProblemParameters(String message, Map<String, Object> paramMap) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("message", message);
    parameters.put("params", paramMap);
    return parameters;
  }
}
