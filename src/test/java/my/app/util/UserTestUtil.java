package my.app.util;

import java.util.Collections;

import javax.annotation.Nullable;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import my.app.domain.Authority;
import my.app.domain.User;

public final class UserTestUtil {
  public static final String JOHNDOE_LOGIN = "johndoe";

  public static final String JOHNDOE_PASSWORD = "password";

  public static final String JOHNDOE_EMAIL = "johndoe@localhost";

  public static final String JOHNDOE_FIRSTNAME = "John";

  public static final String JOHNDOE_LASTNAME = "Doe";

  public static User createJohnDoe(@Nullable PasswordEncoder encoder) {
    String encodedPasword = null;
    if (encoder == null) {
      encodedPasword = RandomStringUtils.random(60);
    } else {
      encodedPasword = encoder.encode(JOHNDOE_PASSWORD);
    }

    User user = new User();
    user.setLogin(JOHNDOE_LOGIN);
    user.setPassword(encodedPasword);
    user.setActivated(true);
    user.setEmail(JOHNDOE_EMAIL);
    user.setFirstName(JOHNDOE_FIRSTNAME);
    user.setLastName(JOHNDOE_LASTNAME);
    user.setAuthorities(Collections.singleton(Authority.of(AuthUtil.USER)));
    return user;
  }

  private UserTestUtil() {
  }
}
