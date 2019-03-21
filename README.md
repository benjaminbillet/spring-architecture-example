# JPA and Spring Data

[Java Persistence API](https://docs.oracle.com/javaee/7/tutorial/partpersist.htm#BNBPY) is a specification that describes the management of relational data:
- describe entities (`my.app.domain` package), i.e., persistence domain objects representing tables.
- create queries using [Java Persistence Query Language](https://docs.oracle.com/javaee/7/tutorial/persistence-querylanguage.htm#BNBTG)] and [Critera API](https://docs.oracle.com/javaee/7/tutorial/persistence-criteria.htm#GJITV)

JPA is only a specification, the implementation provided by the `spring-boot-starter-data-jpa` starter is [Hibernate](http://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html).

[Spring data JPA](https://docs.spring.io/spring-data/data-jpa/docs/2.1.5.RELEASE/reference/html) provides repository support for JPA. [Repositories](https://docs.spring.io/spring-data/data-jpa/docs/2.1.5.RELEASE/reference/html/#repositories) are interfaces that are automatically implemented by Spring at runtime, based on the name of the repository methods. See `my.app.repository` for an example.

Before running your application, make sure to create the `mydatabase` database (see `application-dev.properties` for configuration):

```
create database mydatabase character set utf8mb4 collate utf8mb4_bin;
```