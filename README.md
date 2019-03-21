# Spring web

[Spring web mvc](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/web.html#mvc-controller) enables you to create REST endpoints. Look at the class `UtilsEndpoint` for a set of very simple services.

- `App.java` updated to log useful informations at server startups
- Jackson dependencies added to `build.gradle`
- Configuration for new [date and time API](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html) for Jackson (see `my.app.config` package).
