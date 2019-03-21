package my.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// every key that starts with "myapp" in the application properties and matches the attributes of this class will be injected automatically
@ConfigurationProperties(prefix = "myapp", ignoreUnknownFields = true)
public class ApplicationProperties {
  private String myStringAttribute;
  private Boolean myBooleanAttribute;
  private MyComplexAttribute myComplexAttribute;

  public String getMyStringAttribute() {
    return this.myStringAttribute;
  }

  public void setMyStringAttribute(String myStringAttribute) {
    this.myStringAttribute = myStringAttribute;
  }

  public Boolean isMyBooleanAttribute() {
    return this.myBooleanAttribute;
  }

  public Boolean getMyBooleanAttribute() {
    return this.myBooleanAttribute;
  }

  public void setMyBooleanAttribute(Boolean myBooleanAttribute) {
    this.myBooleanAttribute = myBooleanAttribute;
  }

  public MyComplexAttribute getMyComplexAttribute() {
    return this.myComplexAttribute;
  }

  public void setMyComplexAttribute(MyComplexAttribute myComplexAttribute) {
    this.myComplexAttribute = myComplexAttribute;
  }


  @Override
  public String toString() {
    return "{" +
      " myStringAttribute='" + getMyStringAttribute() + "'" +
      ", myBooleanAttribute='" + isMyBooleanAttribute() + "'" +
      ", myComplexAttribute='" + getMyComplexAttribute() + "'" +
      "}";
  }


  public static class MyComplexAttribute {
    private Long myLongAttribute;

    public Long getMyLongAttribute() {
      return this.myLongAttribute;
    }
  
    public void setMyLongAttribute(Long myLongAttribute) {
      this.myLongAttribute = myLongAttribute;
    }

    @Override
    public String toString() {
      return "{" +
        " myLongAttribute='" + getMyLongAttribute() + "'" +
        "}";
    }
  }
}
