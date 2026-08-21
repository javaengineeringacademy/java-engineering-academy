# Logging Basics in Java

## Overview

Logging is the process of recording application events, errors, and state changes to files, consoles, or external systems. In Java, logging serves as the primary diagnostic tool for understanding application behavior in development and production environments.

## Core Concepts

### Why Log?

- **Debugging** - Trace issues through application flow
- **Auditing** - Record who did what and when
- **Monitoring** - Track application health and performance
- **Forensics** - Reconstruct events after failures
- **Compliance** - Meet regulatory requirements for data tracking

### Log Levels (Severity Order)

| Level | Purpose | Typical Use |
|-------|---------|-------------|
| `TRACE` | Fine-grained diagnostic | Method entry/exit, variable values |
| `DEBUG` | Development information | State transitions, algorithm decisions |
| `INFO` | Normal operations | Startup, shutdown, major lifecycle events |
| `WARN` | Potential problems | Deprecated usage, retry attempts, fallbacks |
| `ERROR` | Failures requiring attention | Exceptions, failed operations, system errors |
| `FATAL` | Critical system failure | Data corruption, unrecoverable states (rare in Java) |

### The Java Logging Ecosystem

Java logging evolved through several generations:

1. **`java.util.logging` (JUL)** - Built into JDK since 1.4
2. **Log4j 1.x** - Third-party, dominated before JDK logging
3. **Jakarta Commons Logging (JCL)** - Abstraction layer
4. **SLF4J** - Modern abstraction, created to solve JCL problems
5. **Log4j 2** - Complete rewrite of Log4j 1.x
6. **Logback** - SLF4J native implementation

### The Bridge Problem

Different libraries use different logging APIs. A typical application might have:
- Spring using JCL
- Hibernate using JCL
- Your code using SLF4J
- Tomcat using JUL

**Bridges** redirect one API to another implementation:

```
JCL calls → jcl-over-slf4j → Logback
JUL calls → jul-to-slf4j → SLF4J → Logback
Log4j 1 calls → log4j-over-slf4j → Logback
```

## SLF4J: The Facade Pattern

SLF4J provides a **facade** - a thin interface that delegates to the actual implementation:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public User findUser(String id) {
        logger.debug("Looking up user with id={}", id);
        User user = repository.get(id);
        if (user == null) {
            logger.warn("User not found: id={}", id);
        }
        return user;
    }
}
```

### Parameterized Logging (SLF4J)

SLF4J uses `{}` placeholders instead of string concatenation:

```java
// BAD: String concatenation creates garbage even if logging is disabled
logger.debug("Processing item " + itemId + " for user " + userId);

// GOOD: SLF4J parameterized logging
logger.debug("Processing item {} for user {}", itemId, userId);
```

### Logger Naming

```java
// Convention: Use the fully qualified class name
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

// Or for non-class contexts
private static final Logger logger = LoggerFactory.getLogger("myapp.processor");
```

## Logger Levels in Configuration

```xml
<!-- Logback configuration example -->
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
    
    <!-- Override specific packages -->
    <logger name="com.myapp.dao" level="DEBUG" />
    <logger name="org.springframework" level="WARN" />
</configuration>
```

## Best Practices for Basics

1. **Use SLF4J API** in your code, configure implementations at deployment
2. **Logger is always `static final`** and in a private field
3. **Never use `System.out.println()`** for application logging
4. **Log exceptions with stack trace**: `logger.error("Failed to process", e)`
5. **Use parameterized messages** to avoid unnecessary string operations
6. **Include context** in log messages: IDs, state, relevant parameters
7. **Avoid logging sensitive data**: passwords, tokens, PII
8. **Set appropriate levels** per package in configuration
