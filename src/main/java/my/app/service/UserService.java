package my.app.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import my.app.api.errors.EmailAlreadyUsedException;
import my.app.api.errors.LoginAlreadyUsedException;
import my.app.config.ApplicationProperties;
import my.app.domain.Authority;
import my.app.domain.User;
import my.app.dto.UserDto;
import my.app.dto.UserMapper;
import my.app.repository.AuthorityRepository;
import my.app.repository.UserRepository;
import my.app.util.AuthUtil;
import my.app.util.MiscUtil;

@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthorityRepository authorityRepository;

  private final UserMapper userMapper;

  private final ApplicationProperties config;

  private final CacheManager cacheManager;

  private final AuditEventRepository auditer;


  public UserService(ApplicationProperties config, UserRepository userRepository, UserMapper userMapper,
      AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder, CacheManager cacheManager,
      AuditEventRepository auditer) {
    this.config = config;
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.authorityRepository = authorityRepository;
    this.cacheManager = cacheManager;
    this.auditer = auditer;
  }

  public boolean activateRegistration(String key) {
    log.debug("Activating: {}", key);
    Optional<User> user = userRepository.findOneByActivationKey(key);
    if (user.isPresent()) {
      user.get().setActivated(true);
      user.get().setActivationKey(null);
      this.clearUserCaches(user.get());
      log.debug("Activated user: {}", user);
      return true;
    }
    return false;
  }

  public User registerUser(UserDto dto, String password) {
    userRepository.findOneByLogin(dto.getLogin().toLowerCase()).ifPresent(existingUser -> {
      boolean removed = removeNonActivatedUser(existingUser);
      if (!removed) {
        throw new LoginAlreadyUsedException(config);
      }
    });
    userRepository.findOneByEmailIgnoreCase(dto.getEmail()).ifPresent(existingUser -> {
      boolean removed = removeNonActivatedUser(existingUser);
      if (!removed) {
        throw new EmailAlreadyUsedException(config);
      }
    });
    User newUser = new User();
    newUser.setLogin(dto.getLogin().toLowerCase());
    newUser.setPassword(passwordEncoder.encode(password));
    newUser.setFirstName(dto.getFirstName());
    newUser.setLastName(dto.getLastName());
    newUser.setEmail(dto.getEmail().toLowerCase());
    newUser.setActivated(false); // new user is not active
    newUser.setActivationKey(AuthUtil.generateActivationKey()); // we create a registration key for new users
   
    // user role by default
    Set<Authority> authorities = new HashSet<>();
    authorityRepository.findById(AuthUtil.USER).ifPresent(authorities::add);
    newUser.setAuthorities(authorities);

    newUser = userRepository.save(newUser);
    this.clearUserCaches(newUser);

    auditer.add(new AuditEvent(AuthUtil.getCurrentUserLogin().orElse(AuthUtil.SYSTEM_ACCOUNT), "user-created",
        MiscUtil.toMap("item", dto.toString()))); // example of custom audit event
    log.debug("Created new user: {}", newUser);
    return newUser;
  }

  private boolean removeNonActivatedUser(User existingUser) {
    if (existingUser.getActivated()) {
      return false;
    }
    userRepository.delete(existingUser);
    userRepository.flush();
    this.clearUserCaches(existingUser);
    return true;
  }

  public void deleteUser(String login) {
    userRepository.findOneByLogin(login).ifPresent(user -> {
      userRepository.delete(user);
      this.clearUserCaches(user);
      log.debug("Deleted User: {}", user);
    });
  }

  @Transactional(readOnly = true)
  public Optional<UserDto> getUserWithAuthoritiesByLogin(String login) {
    return userRepository.findOneWithAuthoritiesByLogin(login).map(userMapper::toDto);
  }

  @Transactional(readOnly = true)
  public Optional<UserDto> getUserWithAuthoritiesByEmail(String email) {
    return userRepository.findOneWithAuthoritiesByEmail(email).map(userMapper::toDto);
  }

  @Transactional(readOnly = true)
  public Optional<UserDto> getUserWithAuthorities(Long id) {
    return userRepository.findOneWithAuthoritiesById(id).map(userMapper::toDto);
  }

  @Transactional(readOnly = true)
  public Optional<UserDto> getUserWithAuthorities() {
    return AuthUtil.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin).map(userMapper::toDto);
  }

  // delete non-activated user after 5 days
  // this process is scheduled every day at 01:00AM
  @Scheduled(cron = "0 0 1 * * ?")
  public void removeNotActivatedUsers() {
    userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(5, ChronoUnit.DAYS))
        .forEach(user -> {
          log.debug("Deleting not activated user {}", user.getLogin());
          userRepository.delete(user);
          this.clearUserCaches(user);
        });
  }

  private void clearUserCaches(User user) {
    Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
    Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
  }
}
