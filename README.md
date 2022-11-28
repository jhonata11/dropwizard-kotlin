
# Dropwizard Kotlin 
![Build status](https://github.com/jhonata11/dropwizard-kotlin/actions/workflows/build.yml/badge.svg)

[Dropwizard](https://www.dropwizard.io/en/latest/) template using [Kotlin](https://kotlinlang.org/).

## Usage
This project is build around the [gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html). To start it locally you can simply run the command:
```console
$ ./gradlew run
```
This will basically start the application using the settings from the [`dev.yml`](config/dev.yml) file. 

## Features
Here are some features this template repository has:
- Dependency injection with [Guice](https://github.com/google/guice).
- Unit and integration tests with [Wiremock](https://wiremock.org/)
 
