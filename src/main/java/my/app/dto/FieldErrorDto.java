package my.app.dto;

import java.io.Serializable;

public class FieldErrorDto implements Serializable {

  private static final long serialVersionUID = 1083503963432754705L;

  private final String objectName;

  private final String field;

  private final String code;

  public FieldErrorDto(String objectName, String field, String code) {
    this.objectName = objectName;
    this.field = field;
    this.code = code;
  }

  public String getObjectName() {
    return objectName;
  }

  public String getField() {
    return field;
  }

  public String getCode() {
    return code;
  }

}
