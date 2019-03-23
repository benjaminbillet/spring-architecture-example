## TODO

Insert database roles:
```
insert into authority values ('ROLE_ADMIN');
insert into authority values ('ROLE_USER');
insert into authority values ('ROLE_ANONYMOUS');

```

Register a new account:
```
curl -v -X POST -H 'Content-type: application/json' -d '{"login": "bbillet","firstName": "Benjamin","lastName": "Billet","email": "hello@benjaminbillet.fr","password": "password"}' 'http://localhost:8080/api/auth/register'
```

Look for the activation key in the database:
```
select login,activation_key from user;
```

Activate the account:
```
curl -v -X GET 'http://localhost:8080/api/auth/activate?key=<the activation key>'
```

Authenticate yourself:
```
curl -v -X POST -H 'Content-type: application/json' -d '{"username": "bbillet","password": "password"}' 'http://localhost:8080/api/auth/authenticate'
```

Use the id_token as bearer authentication: 
```
curl -v -X GET -H 'Authorization: Bearer <the id_token>' 'http://localhost:8080/api/auth/account'
```















