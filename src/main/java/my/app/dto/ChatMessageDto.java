package my.app.dto;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;

public class ChatMessageDto {

  private String sessionId;

  private String userLogin;

  private String message;

  private Instant time;

  public String getSessionId() {
      return sessionId;
  }

  public void setSessionId(String sessionId) {
      this.sessionId = sessionId;
  }

  public String getUserLogin() {
      return userLogin;
  }

  public void setUserLogin(String userLogin) {
      this.userLogin = userLogin;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Instant getTime() {
      return time;
  }

  public void setTime(Instant time) {
      this.time = time;
  }


  @Override
  public String toString() {
    return "{" +
      " sessionId='" + getSessionId() + "'" +
      ", userLogin='" + getUserLogin() + "'" +
      ", message='" + StringUtils.abbreviate(getMessage(), 100) + "'" +
      ", time='" + getTime() + "'" +
      "}";
  }
}
