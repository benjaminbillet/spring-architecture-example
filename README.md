# Gradle

Look at the commented build.gradle for the base concepts.
- Learn the Gradle basics: https://docs.gradle.org/5.3/userguide/tutorial_using_tasks.html#learning-the-basics
- Learn the specifics of Java compilation with Gradle: https://docs.gradle.org/5.3/userguide/building_java_projects.html#java-projects


## Few commands to start:

Display the available tasks:

```gradle tasks```


Run the build task:

```gradle build```

Look in the build folder, a jar with a minimalistic `MANIFEST.MF` has been generated.


# Gradlew
The [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) is a script that invokes a declared version of Gradle, downloading it beforehand if necessary.

Generate the wrapper

```gradle wrapper --gradle-version 5.3 --distribution-type all```

Several files are generated:
- `gradlew`, `gradlew.bat` are scripts for running gradle; instead of `gradle build`, try `./gradlew build`
- `gradle/wrapper` folder contains the gradle jar and a `gradle-wrapper.properties` file (look at it, the url of the downloaded gradle is specified there)