package my.app.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.core.task.AsyncTaskExecutor;

public class AuditedExecutor implements AsyncTaskExecutor, InitializingBean, DisposableBean {

  private final Logger log = LoggerFactory.getLogger(AuditedExecutor.class);

  private final AsyncTaskExecutor executor;

  private final AuditEventRepository auditer;

  public AuditedExecutor(AsyncTaskExecutor executor, AuditEventRepository auditer) {
    this.executor = executor;
    this.auditer = auditer;
  }

  @Override
  public void execute(Runnable task) {
    executor.execute(wrapTask(task));
  }

  @Override
  public void execute(Runnable task, long startTimeout) {
    executor.execute(wrapTask(task), startTimeout);
  }

  @Override
  public Future<?> submit(Runnable task) {
    return executor.submit(wrapTask(task));
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    return executor.submit(wrapTask(task));
  }

  @Override
  public void destroy() throws Exception {
    if (executor instanceof DisposableBean) {
      DisposableBean bean = (DisposableBean) executor;
      bean.destroy();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (executor instanceof InitializingBean) {
      InitializingBean bean = (InitializingBean) executor;
      bean.afterPropertiesSet();
    }
  }

  private <T> Callable<T> wrapTask(Callable<T> task) {
    return () -> {
      try {
        return task.call();
      } catch (Exception e) {
        handleError(e);
        throw e;
      }
    };
  }

  private Runnable wrapTask(Runnable task) {
    return () -> {
      try {
        task.run();
      } catch (Exception e) {
        handleError(e);
      }
    };
  }

  private void handleError(Exception e) {
    log.error("Async pool executor failed", e);
    auditer.add(new AuditEvent(AuthUtil.SYSTEM_ACCOUNT, "async-pool-error", MiscUtil.toMap("exception", e)));
  }
}
