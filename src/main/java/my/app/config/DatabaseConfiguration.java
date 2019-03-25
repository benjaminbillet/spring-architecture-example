package my.app.config;

import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import my.app.util.AuthUtil;
import my.app.domain.Authority;
import my.app.repository.AuthorityRepository;

@Configuration
@EnableJpaRepositories("my.app.repository")
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration {
  private final AuthorityRepository authorityRepository;

  public DatabaseConfiguration(AuthorityRepository authorityRepository) {
    this.authorityRepository = authorityRepository;
  }

  @PostConstruct
  public void init() {
    // create base roles if not found
    Stream.of(AuthUtil.ADMIN, AuthUtil.USER, AuthUtil.ANONYMOUS)
        .forEach(authority -> authorityRepository.saveAndFlush(Authority.of(authority)));
    Objects.requireNonNull(authorityRepository.getOne(AuthUtil.ADMIN));
    Objects.requireNonNull(authorityRepository.getOne(AuthUtil.USER));
    Objects.requireNonNull(authorityRepository.getOne(AuthUtil.ANONYMOUS));
  }
}
