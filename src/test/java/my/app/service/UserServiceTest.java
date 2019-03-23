package my.app.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import my.app.App;
import my.app.domain.User;
import my.app.repository.UserRepository;
import my.app.util.UserTestUtil;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@Transactional
public class UserServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Test
  @Transactional
  public void testFindNotActivatedUsers() {
    Instant now = Instant.now();

    User user = UserTestUtil.createJohnDoe();
    user.setActivated(false);
    user.setCreatedDate(now.minus(6, ChronoUnit.DAYS));
    user = userRepository.saveAndFlush(user);

    List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
    assertThat(users).isNotEmpty();

    userService.removeNotActivatedUsers();
    users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minus(3, ChronoUnit.DAYS));
    assertThat(users).isEmpty();
  }

  @Test
  @Transactional
  public void testRemoveNotActivatedUsers() {
    User user = UserTestUtil.createJohnDoe();
    user.setActivated(false);
    user.setCreatedDate(Instant.now().minus(30, ChronoUnit.DAYS));
    userRepository.saveAndFlush(user);

    assertThat(userRepository.findOneByLogin(UserTestUtil.JOHNDOE_LOGIN)).isPresent();
    userService.removeNotActivatedUsers();
    assertThat(userRepository.findOneByLogin(UserTestUtil.JOHNDOE_LOGIN)).isNotPresent();
  }
}
