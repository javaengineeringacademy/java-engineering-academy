# Kotlin Project Structure

## Standard Layout

```
project/
  build.gradle.kts
  settings.gradle.kts
  gradle.properties
  gradle/
    wrapper/
      gradle-wrapper.jar
      gradle-wrapper.properties
  src/
    main/
      kotlin/
        com/example/app/
          Application.kt
          models/
            User.kt
          repositories/
            UserRepository.kt
          services/
            UserService.kt
          api/
            UserRoutes.kt
      resources/
        application.conf
        logback.xml
    test/
      kotlin/
        com/example/app/
          UserServiceTest.kt
          UserRoutesTest.kt
      resources/
        test-application.conf
```

## settings.gradle.kts

```kotlin
rootProject.name = "my-kotlin-app"

include(":module-one")
include(":module-two")
```

## build.gradle.kts

```kotlin
plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    application
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.ktor:ktor-server-core:2.3.3")
    implementation("io.ktor:ktor-server-netty:2.3.3")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-tests:2.3.3")
}

application {
    mainClass.set("com.example.app.ApplicationKt")
}
```

## Package Structure

```
com.example.app/
  Application.kt          # Entry point
  config/
    AppConfig.kt          # Configuration
    Database.kt           # Database setup
  models/
    User.kt               # Data classes
    Response.kt
  repositories/
    UserRepository.kt     # Data access
  services/
    UserService.kt        # Business logic
  api/
    routes/
      UserRoutes.kt       # HTTP endpoints
    middleware/
      Auth.kt             # Authentication
  utils/
    Extensions.kt         # Extension functions
```

## Main Entry Point

```kotlin
package com.example.app

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
```
