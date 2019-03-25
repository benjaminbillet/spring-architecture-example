package my.app.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import my.app.util.AuditedExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer, SchedulingConfigurer {

  private final ApplicationProperties config;

  private final AuditEventRepository auditer;

  public AsyncConfiguration(ApplicationProperties config, AuditEventRepository auditer) {
    this.config = config;
    this.auditer = auditer;
  }
  
  @Override
  @Bean(name = "taskExecutor")
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(config.getAsyncPool().getCoreSize());
    executor.setMaxPoolSize(config.getAsyncPool().getMaxSize());
    executor.setQueueCapacity(config.getAsyncPool().getQueueSize());
    executor.setThreadNamePrefix("test-app-executor-");
    return new AuditedExecutor(executor, auditer);
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    taskRegistrar.setScheduler(scheduledTaskExecutor());
  }

  @Bean
  public Executor scheduledTaskExecutor() {
    return Executors.newScheduledThreadPool(config.getAsyncPool().getCoreSize());
  }
}
