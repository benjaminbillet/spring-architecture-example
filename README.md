# Testing and code coverage
The `src/test` folder contains some test examples.

For running the tests, we use the [H2 database](http://www.h2database.com/html/main.html). This database is embedded in-memory and does not require to setup anything external to run the tests.

```
./gradlew clean test
```

Test reports are generated in `build/reports`. They are standard HTML files that can be opened in your browser.

Code coverage is measured by [Jacoco](https://www.eclemma.org/jacoco). Code coverage reports can be sent to SonarQube (currently ~57% code coverage):

```
./gradlew clean test sonarqube
```
