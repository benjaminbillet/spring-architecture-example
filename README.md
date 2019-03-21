# Service
A service is an annotated classes defined as “an operation offered as an interface that stands alone in the model, with no encapsulated state.”
Here, we use them to create an intermediary layer between the REST layer (`my.app.api`) and the data access layer (`my.app.repository` and `my.app.domain`).

We also usually define the transactions at service level using [spring transactions](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/data-access.html#transaction-declarative), based on the [Java Transaction API](https://docs.oracle.com/javaee/7/tutorial/transactions.htm#BNCIH). See also this small introduction: https://dzone.com/articles/how-does-spring-transactional


# Data transfer objects
DTOs are data objects (no business logic) used for transferring data between layers. Here, the DTO we built (`my.app.dto`) is quite straightforward (one to one mapping with an entity), but they can be used to flatten complex entity relations into a single object that can be used by the REST layer.

# A small API
In `my.app.api`, we designed a small Create-Retrieve-Update-Delete API:

```
curl -v -X POST 'http://localhost:8080/api/public/resources' -H 'Content-type: application/json' -d '{"name":"Resource1","description":"Description of Resource1"}'
```

```
curl -v -X GET 'http://localhost:8080/api/public/resources/1'
```

```
curl -v -X GET 'http://localhost:8080/api/public/resources'
```

Thanks to the use of a `Pageable`, the `GET api/public/resources` endpoint works with pagination and sorting for free using query parameters:
- you can have several `sort` parameters, of the form `sort={attribute},{direction}` with direction (asc or desc) being optional (e.g., `?sort=name,desc&sort=id` )
- by default a page is 20 elements, but you can set the page size explicitly: `?size={page size}&page={page number}`. The page number starts at 0.

