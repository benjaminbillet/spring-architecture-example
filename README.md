# Spring Data JPA Auditing
Spring Data provides support to keep track of who created/changed entities and when using specific annotations (see `my.app.domain.AuditedEntity`): https://docs.spring.io/spring-data/jpa/docs/2.1.5.RELEASE/reference/html/#auditing

Creation/modification dates as well as creator and last editor credentials are transparently stored in the entity (the identity being retrieved using `my.app.config.SpringSecurityAuditorAware`).

# Spring Actuator Auditing
Spring Boot Actuator introduces a flexible audit framework that publishes events (by default, "authentication success", "failure" and "access denied" exceptions): https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/html/production-ready-auditing.html

By default, audit events are kept in memory and accessible through `http://localhost:8080/actuator/auditevents`. By implementing a different `AuditEventRepository`, you can create a persistence mechanism for all the audit events. Here we give an example that dumps events to stdin (`my.app.config.ConsoleAuditEventRepository`).
