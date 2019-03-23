package my.app.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtToken {

  private String token;

  public JwtToken(String token) {
    this.token = token;
  }

  @JsonProperty("id_token")
  String getToken() {
    return token;
  }

  void setToken(String token) {
    this.token = token;
  }
}
