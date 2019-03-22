package my.app.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;

// this class parameterize various behaviors of rest controllers (exception handling, security, validation, etc.)
@ControllerAdvice
public class ExceptionTranslator implements ProblemHandling {

  private ApplicationProperties config;

  public ExceptionTranslator(ApplicationProperties config) {
    this.config = config;
  }  

  // process the RFC7807 problem payload
  @Override
  public ResponseEntity<Problem> process(ResponseEntity<Problem> entity, NativeWebRequest request) {
    if (entity == null) {
      return entity;
    }

    Problem problem = entity.getBody();
    if ((problem instanceof DefaultProblem) == false) {
      return entity;
    }

    ProblemBuilder builder = Problem.builder()
        .withStatus(problem.getStatus()).withTitle(problem.getTitle())
        .with("path", request.getNativeRequest(HttpServletRequest.class).getRequestURI());
    if (Problem.DEFAULT_TYPE.equals(problem.getType())) {
      builder.withType(config.getRfc7807().getDefaultTypeUri());
    } else {
      builder.withType(problem.getType());
    }

    return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
  }

  @ExceptionHandler
  public ResponseEntity<Problem> handleNoSuchElementException(NoSuchElementException ex, NativeWebRequest request) {
    Problem problem = Problem.builder().withStatus(Status.NOT_FOUND)
        .with("message", config.getRfc7807().getEntityNotFoundTypeUri()).build();
    return create(ex, problem, request);
  }
}
