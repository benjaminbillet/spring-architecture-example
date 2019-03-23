package my.app.util;

import org.apache.commons.lang3.RandomStringUtils;

import my.app.domain.User;

public final class UserTestUtil {
  public static final String JOHNDOE_LOGIN = "johndoe";

  public static final String JOHNDOE_PASSWORD = "password";

  public static final String JOHNDOE_EMAIL = "johndoe@localhost";

  public static final String JOHNDOE_FIRSTNAME = "John";

  public static final String JOHNDOE_LASTNAME = "Doe";

  public static User createJohnDoe() {
    User user = new User();
    user.setLogin(JOHNDOE_LOGIN);
    user.setPassword(RandomStringUtils.random(60));
    user.setActivated(true);
    user.setEmail(JOHNDOE_EMAIL);
    user.setFirstName(JOHNDOE_FIRSTNAME);
    user.setLastName(JOHNDOE_LASTNAME);
    return user;
  }

  private UserTestUtil() {
  }
}
