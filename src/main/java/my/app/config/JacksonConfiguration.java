package my.app.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@Configuration
public class JacksonConfiguration {

  // Jackson support for the new Java time API
  @Bean
  public JavaTimeModule javaTimeModule() {
    return new JavaTimeModule();
  }
  @Bean
  public Jdk8Module jdk8TimeModule() {
    return new Jdk8Module();
  }

  // Jackson support for hibernate types
  @Bean
  public Hibernate5Module hibernate5Module() {
    return new Hibernate5Module();
  }

  // https://tools.ietf.org/html/rfc7807 (Problem Details for HTTP APIs) serialization/deserialization
  @Bean
  public ProblemModule problemModule() {
    return new ProblemModule();
  }
  @Bean
  public ConstraintViolationProblemModule constraintViolationProblemModule() {
    return new ConstraintViolationProblemModule();
  }

  // Jackson Afterburner module (improve serialization/deserialization performances)
  @Bean
  public AfterburnerModule afterburnerModule() {
    return new AfterburnerModule();
  }
}
