# Criteria API and Metamodel API
The [Criteria API](https://docs.oracle.com/javaee/7/tutorial/persistence-criteria001.htm#GJRIJ) enables you to create query-defining objects based on the entities we previously defined.

The [Metamodel API](https://docs.oracle.com/javaee/7/tutorial/persistence-criteria002.htm#GJIUP) enables you to create a metamodel of the entities
The metamodel associated to an entity X is a class X_ that must be created in the same package of X, each attribute of X_ corresponding to the persistent fields of the entity class. Combined to the Criteria API, metamodels enable to create [complex typesafe queries](https://docs.oracle.com/javaee/7/tutorial/persistence-criteria003.htm#GJIVM).

Instead of writing this metamodel classes ourselves, we use the [Hibernate JPA Static Metamodel Generator](http://docs.jboss.org/hibernate/orm/5.4/topical/html_single/metamodelgen/MetamodelGenerator.html) that automatically generates the code of these classes in `build/generated`.

Look also:
- [QueryDSL](http://www.querydsl.com), a promising library to write queries as Java code


# REST filters based on the Criteria and Metamodel APIs
In `my.app.vdo.filter` we define a set of generic filters (`Filter`, `StringFilter`, `ComparableFilter`) that enable us to represents constraints (e.g., `equalsTo` or `greaterThan`). The `FilterMapper` class can convert these filters into [spring specification classes](https://docs.spring.io/spring-data/jpa/docs/2.1.5.RELEASE/reference/html/#specifications) that can then be passed to the repositories.

These filters can be passed to our REST API (see changes in `PublicResourceEndpoint`), enabling us to create complex services mixing filtering, sorting and pagination.

```
curl -v -X GET 'localhost:8080/api/public/resources?name.empty=false&name.in=Resource1,Resource2'
```

```
curl -v -X GET 'localhost:8080/api/public/resources?name.contains=Resource&sort=id,desc'
```
