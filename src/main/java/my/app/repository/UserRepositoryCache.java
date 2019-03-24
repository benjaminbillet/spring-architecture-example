package my.app.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.NoRepositoryBean;

import my.app.domain.User;

// custom caches for user repository
@NoRepositoryBean
public interface UserRepositoryCache {
  String USERS_BY_LOGIN_CACHE = "usersByLogin";
  String USERS_BY_EMAIL_CACHE = "usersByEmail";

  @EntityGraph(attributePaths = "authorities")
  @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
  Optional<User> findOneWithAuthoritiesByLogin(String login);

  @EntityGraph(attributePaths = "authorities")
  @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
  Optional<User> findOneWithAuthoritiesByEmail(String email);
}
