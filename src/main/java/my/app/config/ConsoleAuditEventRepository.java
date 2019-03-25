package my.app.config;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.stereotype.Component;

@Component
public class ConsoleAuditEventRepository implements AuditEventRepository {

  private final Logger log = LoggerFactory.getLogger(ConsoleAuditEventRepository.class);

  private final AuditEventRepository eventRepository = new InMemoryAuditEventRepository();

  @Override
  public void add(AuditEvent event) {
    log.info("On audit application event: timestamp: {}, principal: {}, type: {}, data: {}", event.getTimestamp(),
        event.getPrincipal(), event.getType(), event.getData());

    eventRepository.add(event);
  }

  @Override
  public List<AuditEvent> find(String principal, Instant after, String type) {
    return eventRepository.find(principal, after, type);
  }
}
