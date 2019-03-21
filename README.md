# Spring Boot

[Spring Boot[(https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/htmlsingle)] enables you to create stand-alone Spring-based applications embedding their own application server.

Look at what changed in build.gradle. By adding the [spring boot plugin](https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/gradle-plugin/reference/html), a new task (`./gradlew tasks`) is available: now, you can use `./gradlew bootRun` to start the application.

Look at the source code of `src/main/java/my/app/App.java`, which is the main entry point of your application.

When your run the application using `./gradlew bootRun`, you should see the following line:
```
2018-01-21 07:53:56.362  INFO 14427 --- [main] my.app.App : My application is running!
```
