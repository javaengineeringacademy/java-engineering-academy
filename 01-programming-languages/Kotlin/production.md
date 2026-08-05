# Kotlin Production Deployment

## Docker

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy Gradle wrapper
COPY gradle/ gradle/
COPY gradlew .
RUN chmod +x gradlew

# Cache dependencies
COPY build.gradle.kts settings.gradle.kts ./
RUN ./gradlew dependencies --no-daemon

# Build application
COPY src/ src/
RUN ./gradlew bootJar --no-daemon

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build/libs/app.jar"]
```

## Spring Boot Integration

```kotlin
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@RestController
class UserController(private val userService: UserService) {
    @GetMapping("/users/{id}")
    suspend fun getUser(@PathVariable id: Long): User {
        return userService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}
```

## Docker Compose

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_HOST=postgres
      - REDIS_HOST=redis
    depends_on:
      - postgres
      - redis

  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: myapp
      POSTGRES_PASSWORD: secret
    volumes:
      - pgdata:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine
```

## Health Checks

```kotlin
@RestController
class HealthController {
    @GetMapping("/health")
    fun health(): Map<String, String> {
        return mapOf("status" to "UP")
    }

    @GetMapping("/ready")
    fun ready(): Map<String, String> {
        // Check dependencies
        return mapOf("status" to "READY")
    }
}
```

## Graceful Shutdown

```kotlin
@Bean
fun gracefulShutdown(): DisposableBean {
    return DisposableBean {
        println("Shutting down gracefully...")
        // Close connections
        // Drain queues
        // Complete in-flight requests
    }
}
```

## Environment Configuration

```yaml
# application.yml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:5432/myapp
    username: ${DB_USER:postgres}
    password: ${DB_PASS:secret}
  redis:
    host: ${REDIS_HOST:localhost}
```

## JVM Tuning

```bash
java -Xms512m -Xmx1024m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar app.jar
```
