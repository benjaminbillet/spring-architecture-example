package my.app.vdo;

import javax.validation.constraints.Size;

import my.app.dto.UserDto;
import my.app.util.AuthUtil;

public class UserVdo extends UserDto {
  @Size(min = AuthUtil.PASSWORD_MIN_LENGTH, max = AuthUtil.PASSWORD_MAX_LENGTH)
  private String password;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
