package my.app.config;

import java.time.Duration;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import my.app.domain.Authority;
import my.app.domain.PublicResource;
import my.app.domain.User;
import my.app.repository.UserRepository;

@Configuration
@EnableCaching
public class CacheConfiguration {
  private final javax.cache.configuration.Configuration<Object, Object> cacheConfiguration;

  public CacheConfiguration() {
    // TODO could be improved with configuration in application.properties
    this.cacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(CacheConfigurationBuilder
        .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(1000))
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(3600))).build());
  }

  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cm -> {
      // cache for simple entities
      cm.createCache(User.class.getName(), cacheConfiguration);
      cm.createCache(Authority.class.getName(), cacheConfiguration);
      cm.createCache(PublicResource.class.getName(), cacheConfiguration);

      // cache for join tables (user-authority)
      cm.createCache(User.class.getName() + ".authorities", cacheConfiguration);

      // custom caches
      cm.createCache(UserRepository.USERS_BY_LOGIN_CACHE, cacheConfiguration);
      cm.createCache(UserRepository.USERS_BY_EMAIL_CACHE, cacheConfiguration);
    };
  }
}
