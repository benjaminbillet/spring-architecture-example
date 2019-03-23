package my.app.config;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import my.app.util.AuthUtil;
import my.app.repository.AuthorityRepository;

@Configuration
@EnableJpaRepositories("my.app.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {
  private final AuthorityRepository authorityRepository;

  public DatabaseConfiguration(AuthorityRepository authorityRepository) {
    this.authorityRepository = authorityRepository;
  }

  @PostConstruct
  public void init() {
    // check that base roles exists
    Objects.nonNull(authorityRepository.getOne(AuthUtil.ADMIN));
    Objects.nonNull(authorityRepository.getOne(AuthUtil.USER));
    Objects.nonNull(authorityRepository.getOne(AuthUtil.ANONYMOUS));
  }
}
