# Async emailing
Spring provides [task scheduling and asynchronous tasks](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/integration.html#scheduling) capabilities. We use it here to send activation emails (`my.app.service.MailService`). See `my.app.config.AsyncConfiguration` for the configuration details.

# Internationalization 
Using message sources, we build static internationalization (see `resources/i18n` folder). A `messages-{locale code}.properties` defines a set of internationalized strings for a given language. See https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/core.html#context-functionality-messagesource for details.

# Spring template
We add [Thymeleaf](https://www.thymeleaf.org), a template engine, to the project. See Spring support of template engines: https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/htmlsingle/#boot-features-spring-mvc-template-engines