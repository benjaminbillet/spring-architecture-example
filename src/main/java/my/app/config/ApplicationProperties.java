package my.app.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;

// every key that starts with "myapp" in the application properties and matches the attributes of this class will be injected automatically
@ConfigurationProperties(prefix = "myapp", ignoreUnknownFields = true)
public class ApplicationProperties {
  private Rfc7807 rfc7807;

  public Rfc7807 getRfc7807() {
    return this.rfc7807;
  }

  public void setRfc7807(Rfc7807 rfc7807) {
    this.rfc7807 = rfc7807;
  }

  public static class Rfc7807 {
    private String problemBaseUrl;
    private String defaultType;
    private String entityNotFoundType;
    private String constraintViolationType;

    public String getProblemBaseUrl() {
      return problemBaseUrl;
    }

    public void setProblemBaseUrl(String problemBaseUrl) {
      this.problemBaseUrl = problemBaseUrl;
    }

    public String getDefaultType() {
      return this.defaultType;
    }

    public URI getDefaultTypeUri() {
      try {
        return new URI(getProblemBaseUrl() + "/" + getDefaultType());
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }

    public void setDefaultType(String defaultType) {
      this.defaultType = defaultType;
    }

    public String getEntityNotFoundType() {
      return this.entityNotFoundType;
    }

    public URI getEntityNotFoundTypeUri() {
      try {
        return new URI(getProblemBaseUrl() + "/" + getEntityNotFoundType());
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }

    public void setEntityNotFoundType(String entityNotFoundType) {
      this.entityNotFoundType = entityNotFoundType;
    }

    public String getConstraintViolationType() {
      return this.constraintViolationType;
    }

    public URI getConstraintViolationTypeUri() {
      try {
        return new URI(getProblemBaseUrl() + "/" + getConstraintViolationType());
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }

    public void setConstraintViolationType(String constraintViolationType) {
      this.constraintViolationType = constraintViolationType;
    }
  }
}
