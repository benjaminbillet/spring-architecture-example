# Spring profiles

[Spring profiles](https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/html/boot-features-profiles.html) enables you to create [external or internal](https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/html/boot-features-external-config.html) configurations available only in certain environment. For example, it can be used to differentiate your development and production configurations.

As an example, we define two properties file in `src/main/resources`, a global one `application.properties` and a specific one for the `dev` profile (the naming convention is `application-${profile}.properties`).

These properties, as well as environment variables are exposed by spring through the `Environment` class. Here we use an environment variable to set the active profile (you can set more than one, separated by a comma, e.g., `dev,local`):
```
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```

In `build.gradle`, we add a configuration to define a default profile when no other profile is set. With the code we defined in `App.java`, the name of the application will change depending on the profile you pick.

# @ConfigurationProperties
Spring boot provides a binder for application properties that can be used easily with the `@ConfigurationProperties` annotation to create [type-safe configurations](https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config-typesafe-configuration-properties).

We define an `ApplicationProperties` class with this annotation. Everything that starts with `myapp.` in the properties file will be mapped on the attributes of the class.
