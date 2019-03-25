package my.app.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
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

  @Autowired
  private CacheManager cacheManager;

  @Autowired
  private AuditingHandler auditingHandler;

  @Mock
  DateTimeProvider dateTimeProvider;


  @Test
  @Transactional
  public void testFindNotActivatedUsers() {
    when(dateTimeProvider.getNow()).thenReturn(Optional.of(Instant.now().minus(30, ChronoUnit.DAYS)));
    auditingHandler.setDateTimeProvider(dateTimeProvider);

    Instant now = Instant.now();

    User user = UserTestUtil.createJohnDoe(null);
    user.setActivated(false);
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
    when(dateTimeProvider.getNow()).thenReturn(Optional.of(Instant.now().minus(30, ChronoUnit.DAYS)));
    auditingHandler.setDateTimeProvider(dateTimeProvider);

    User user = UserTestUtil.createJohnDoe(null);
    user.setActivated(false);
    userRepository.saveAndFlush(user);

    assertThat(userRepository.findOneByLogin(UserTestUtil.JOHNDOE_LOGIN)).isPresent();
    userService.removeNotActivatedUsers();
    assertThat(userRepository.findOneByLogin(UserTestUtil.JOHNDOE_LOGIN)).isNotPresent();
  }

  @Test
  @Transactional
  public void testCustomCaches() throws Exception {
    User user = UserTestUtil.createJohnDoe(null);
    userRepository.saveAndFlush(UserTestUtil.createJohnDoe(null));

    userService.getUserWithAuthoritiesByLogin(user.getLogin());
    userService.getUserWithAuthoritiesByEmail(user.getEmail());

    assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(user.getLogin())).isNotNull();
    assertThat(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).get(user.getEmail())).isNotNull();
  }
}
