# 11.11 Test Containers

## 1. Introduction

Testcontainers is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.

## 2. Learning Objectives

- Use Testcontainers for integration testing
- Configure database containers
- Set up message broker containers
- Manage container lifecycle
- Write tests with real dependencies

## 3. Prerequisites

- JUnit 5 basics
- Docker fundamentals
- Integration testing concepts

## 4. Why This Concept Exists

Testcontainers solves:
- Flaky integration tests with shared databases
- Complex test environment setup
- Dependency on external services
- Environment consistency issues
- Cleanup problems after tests

## 5. Problem Statement

How do we test code that depends on databases, message brokers, or other services without relying on shared environments?

## 6. Theory

### Container Types

| Container | Purpose |
|-----------|---------|
| PostgreSQLContainer | PostgreSQL database |
| MySQLContainer | MySQL database |
| KafkaContainer | Kafka broker |
| RabbitMQContainer | RabbitMQ |
| GenericContainer | Any Docker image |

### Container Lifecycle

```
Start → Use → Stop
  ↓       ↓      ↓
Pull   Execute  Cleanup
image  tests    container
```

### Configuration Options

- **Image**: Docker image to use
- **Ports**: Port mappings
- **Volumes**: Mount host directories
- **Network**: Custom networks
- **Environment**: Environment variables
- **Startup strategies**: Wait conditions

## 7. Internal Working

### Container Management

1. Pull Docker image if not cached
2. Create container with configuration
3. Start container
4. Wait for readiness condition
5. Execute tests
6. Stop and remove container

### Database Container Flow

```
PostgreSQLContainer
    ↓
Start PostgreSQL image
    ↓
Wait for port 5432
    ↓
Provide JDBC URL
    ↓
Execute tests
    ↓
Stop and remove
```

## 8. JVM Perspective

- Testcontainers runs in test JVM
- Docker API calls via TCP
- Containers run in Docker daemon
- Port mappings via Docker networking

## 9. Memory Representation

```
Testcontainers Memory Model:
┌─────────────────────────────────────┐
│           Test JVM                  │
│  - Testcontainers client            │
│  - Container connection info        │
│  - JDBC/Docker clients              │
├─────────────────────────────────────┤
│        Docker Daemon                │
│  - Container instances              │
│  - Container images                 │
│  - Network configurations           │
└─────────────────────────────────────┘
```

## 10. Easy Example

```java
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class BasicPostgresTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @Test
    void shouldConnectToDatabase() {
        assertNotNull(postgres.getJdbcUrl());
        assertEquals("testdb", postgres.getDatabaseName());
    }
}
```

## 11. Medium Example

```java
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class DatabaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(
            postgres.getJdbcUrl(),
            postgres.getUsername(),
            postgres.getPassword()
        );
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void shouldInsertAndQueryData() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, name VARCHAR(100))");
        stmt.execute("INSERT INTO users (name) VALUES ('Alice')");

        ResultSet rs = stmt.executeQuery("SELECT name FROM users");
        assertTrue(rs.next());
        assertEquals("Alice", rs.getString("name"));
    }
}
```

## 12. Hard Example

```java
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class KafkaIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    @Test
    void shouldStartKafkaBroker() {
        assertTrue(kafka.isRunning());
        assertNotNull(kafka.getBootstrapServers());
    }
}
```

## Interview Questions

1. **What is Testcontainers?**
   Testcontainers is a Java library for integration testing with Docker containers.

2. **Why use Testcontainers over embedded databases?**
   Real database behavior, production-like testing, supports any Docker image.

3. **How do you configure container startup?**
   Use withImage(), withPortBindings(), withEnv(), and wait strategies.

4. **What is a wait strategy?**
   A condition that determines when a container is ready to receive requests.

5. **How do you clean up containers?**
   Testcontainers automatically stops and removes containers after tests.
