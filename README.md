# Caching
Spring provides [caching capabilities](https://docs.spring.io/spring-boot/docs/2.1.3.RELEASE/reference/htmlsingle/#boot-features-caching) that stores method results in-memory. This is especially useful for speeding up database queries.

Spring supports several caching backends, but here we use [Ehcache](https://www.ehcache.org) that implements (now) the JEE specification for caching (JCache).

In practice, by enabling caching (see `CacheConfiguration`), we can express what entities can be cached. We also demonstrate custom caches for some repositories (see `UserRepository`).
