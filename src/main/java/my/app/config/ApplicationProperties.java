package my.app.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.context.properties.ConfigurationProperties;

// every key that starts with "myapp" in the application properties and matches the attributes of this class will be injected automatically
@ConfigurationProperties(prefix = "myapp", ignoreUnknownFields = true)
public class ApplicationProperties {
  private Rfc7807 rfc7807;
  private Jwt jwt;

  public Rfc7807 getRfc7807() {
    return this.rfc7807;
  }

  public void setRfc7807(Rfc7807 rfc7807) {
    this.rfc7807 = rfc7807;
  }

  public Jwt getJwt() {
    return this.jwt;
  }

  public void setJwt(Jwt jwt) {
    this.jwt = jwt;
  }


  public static class Rfc7807 {
    private String problemBaseUrl;
    private String defaultType;
    private String entityNotFoundType;
    private String constraintViolationType;
    private String loginAlreadyUsedType;
    private String emailAlreadyUsedType;
    private String invalidPasswordType;

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

    public String getLoginAlreadyUsedType() {
      return this.loginAlreadyUsedType;
    }

    public URI getLoginAlreadyUsedTypeUri() {
      try {
        return new URI(getProblemBaseUrl() + "/" + getLoginAlreadyUsedType());
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }

    public void setLoginAlreadyUsedType(String loginAlreadyUsedType) {
      this.loginAlreadyUsedType = loginAlreadyUsedType;
    }

    public String getEmailAlreadyUsedType() {
      return this.emailAlreadyUsedType;
    }

    public URI getEmailAlreadyUsedTypeUri() {
      try {
        return new URI(getProblemBaseUrl() + "/" + getEmailAlreadyUsedType());
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }

    public void setEmailAlreadyUsedType(String emailAlreadyUsedType) {
      this.emailAlreadyUsedType = emailAlreadyUsedType;
    }

    public String getInvalidPasswordType() {
      return this.invalidPasswordType;
    }

    public URI getInvalidPasswordTypeUri() {
      try {
        return new URI(getProblemBaseUrl() + "/" + getInvalidPasswordType());
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }

    public void setInvalidPasswordType(String invalidPasswordType) {
      this.invalidPasswordType = invalidPasswordType;
    }
  }

  public static class Jwt {
    private String secretBase64;
    private Integer tokenValiditySeconds;
    private Integer tokenValidityRememberMeSeconds;

    public String getSecretBase64() {
      return this.secretBase64;
    }
  
    public void setSecretBase64(String secretBase64) {
      this.secretBase64 = secretBase64;
    }
  
    public Integer getTokenValiditySeconds() {
      return this.tokenValiditySeconds;
    }
  
    public void setTokenValiditySeconds(Integer tokenValiditySeconds) {
      this.tokenValiditySeconds = tokenValiditySeconds;
    }
  
    public Integer getTokenValidityRememberMeSeconds() {
      return this.tokenValidityRememberMeSeconds;
    }
  
    public void setTokenValidityRememberMeSeconds(Integer tokenValidityRememberMeSeconds) {
      this.tokenValidityRememberMeSeconds = tokenValidityRememberMeSeconds;
    }
  }
}
