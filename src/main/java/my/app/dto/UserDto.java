package my.app.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import my.app.util.AuthUtil;

public class UserDto {

  public static final String ENTITY_NAME = "user";

  private Long id;

  @NotBlank
  @Pattern(regexp = AuthUtil.LOGIN_REGEX)
  @Size(min = 1, max = 50)
  private String login;

  @Size(max = 50)
  private String firstName;

  @Size(max = 50)
  private String lastName;

  @Email
  @Size(min = 5, max = 254)
  private String email;

  private boolean activated = false;

  private Set<String> authorities;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public Set<String> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<String> authorities) {
    this.authorities = authorities;
  }

  @Override
  public String toString() {
    return "{" + " id='" + getId() + "'" + ", login='" + getLogin() + "'" + ", firstName='" + getFirstName() + "'"
        + ", lastName='" + getLastName() + "'" + ", email='" + getEmail() + "'" + ", activated='" + isActivated() + "'"
        + ", authorities='" + getAuthorities() + "'" + "}";
  }
}
