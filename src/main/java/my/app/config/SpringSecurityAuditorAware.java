package my.app.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import my.app.util.AuthUtil;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(AuthUtil.getCurrentUserLogin().orElse(AuthUtil.SYSTEM_ACCOUNT));
  }
}
