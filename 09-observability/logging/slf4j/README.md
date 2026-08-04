# SLF4J - Simple Logging Facade for Java

## Overview

SLF4J (Simple Logging Facade for Java) is a logging abstraction layer that provides a unified API for various logging frameworks. It allows applications to plug in different logging implementations at deployment time without code changes.

## Core Concepts

### Logger Hierarchy
SLF4J uses a hierarchical logger namespace similar to Java packages.

```java
// Get loggers at different levels
Logger rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
Logger classLogger = LoggerFactory.getLogger(MyClass.class);
Logger namedLogger = LoggerFactory.getLogger("com.example.service");
```

### Log Levels
SLF4J defines six log levels in order of severity:

| Level | Purpose |
|-------|---------|
| TRACE | Fine-grained diagnostic information |
| DEBUG | Diagnostic information for debugging |
| INFO | Confirmation of expected behavior |
| WARN | Potentially harmful situations |
| ERROR | Error events that might allow继续 |
| FATAL | Critical failures (not in SLF4J 2.x) |

### Basic Logging

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public User findById(Long id) {
        logger.trace("Looking up user with id: {}", id);
        
        try {
            User user = userRepository.findById(id);
            logger.debug("Found user: {}", user);
            return user;
        } catch (Exception e) {
            logger.error("Failed to find user with id: {}", id, e);
            throw e;
        }
    }
}
```

### Parameterized Messages

```java
// SLF4J supports parameterized messages for performance
logger.debug("User {} logged in from IP {}", username, ipAddress);
logger.info("Processing order {} with {} items", orderId, itemCount);
logger.error("Failed to connect to database at {}:{}", host, port);
```

### Performance Benefits of Parameterized Logging

```java
// BAD - String concatenation always evaluated
logger.debug("User " + user.getName() + " has " + user.getOrders().size() + " orders");

// GOOD - Parameters only evaluated if log level enabled
logger.debug("User {} has {} orders", user.getName(), user.getOrders().size());

// BETTER - Check level before expensive operations
if (logger.isDebugEnabled()) {
    logger.debug("User details: {}", expensiveJsonSerialization(user));
}
```

## MDC (Mapped Diagnostic Context)

MDC provides a way to enrich log messages with contextual data that persists across threads.

### Basic MDC Usage

```java
import org.slf4j.MDC;

public class RequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) {
        try {
            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("userId", getUserId(request));
            MDC.put("remoteAddr", request.getRemoteAddr());
            
            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // Always clear to prevent memory leaks
        }
    }
}
```

### Logback Configuration for MDC

```xml
<pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] [%X{requestId}] [%X{userId}] 
%-5level %logger{36} - %msg%n</pattern>
```

### MDC in Async Context

```java
// Copy MDC for async operations
Map<String, String> contextMap = MDC.getCopyOfContextMap();

CompletableFuture.supplyAsync(() -> {
    if (contextMap != null) {
        MDC.setContextMap(contextMap);
    }
    try {
        return processAsync();
    } finally {
        MDC.clear();
    }
});
```

## Markers

Markers are named objects used to classify log events for filtering and routing.

### Using Markers

```java
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SecurityService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private static final Marker SECURITY = MarkerFactory.getMarker("SECURITY");
    private static final Marker AUDIT = MarkerFactory.getMarker("AUDIT");
    private static final Marker LOGIN_FAILURE = MarkerFactory.getMarker("LOGIN_FAILURE");
    
    public boolean authenticate(String username, String password) {
        boolean success = validateCredentials(username, password);
        
        if (success) {
            logger.info(SECURITY, "User {} authenticated successfully", username);
        } else {
            logger.warn(LOGIN_FAILURE, SECURITY, 
                       "Failed login attempt for user {}", username);
        }
        
        return success;
    }
}
```

### Marker Relationships

```java
// Create marker with cause relationship
Marker authError = MarkerFactory.getMarker("AUTH_ERROR");
Marker parentMarker = MarkerFactory.getMarker("SECURITY_ERROR");
authError.add(parentMarker);

// Log with parent marker
logger.error(authError, "Authentication failed: {}", reason);
```

## Best Practices

### 1. Use Correct Log Levels
```java
// DEBUG: Variable values, method entry/exit
logger.debug("Processing order: {}", order);

// INFO: Significant business events
logger.info("Order {} placed by user {}", orderId, userId);

// WARN: Recoverable issues
logger.warn("Retry attempt {} for service {}", attemptCount, serviceName);

// ERROR: Unrecoverable issues requiring attention
logger.error("Payment processing failed for order {}", orderId, exception);
```

### 2. Include Contextual Information
```java
// GOOD: Includes context for debugging
logger.error("Order {} failed: status={}, amount={}, userId={}", 
             orderId, status, amount, userId);

// BAD: Vague message
logger.error("Order failed");
```

### 3. Avoid Sensitive Data
```java
// BAD: Logs sensitive information
logger.info("User login: username={}, password={}", username, password);

// GOOD: Logs only necessary information
logger.info("Login attempt for user {}", username);
```

### 4. Use Appropriate Logger Scope
```java
// GOOD: Class-level logger
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

// BAD: Package-level logger (too broad)
private static final Logger logger = LoggerFactory.getLogger("com.example");
```

### 5. Exception Logging
```java
try {
    riskyOperation();
} catch (Exception e) {
    // GOOD: Includes exception stack trace
    logger.error("Operation failed: {}", e.getMessage(), e);
    
    // BAD: Loses stack trace
    logger.error("Operation failed: " + e.getMessage());
}
```

## SLF4J Bindings

| Binding | Implementation | Use Case |
|---------|----------------|----------|
| slf4j-simple | Simple output | Development |
| slf4j-jdk14 | java.util.logging | Legacy systems |
| slf4j-log4j12 | Log4J 1.x | Legacy systems |
| logback-classic | Logback | Production (recommended) |
| log4j-slf4j-impl | Log4j2 | Production |

## Configuration Example

### Maven Dependencies
```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.11</version>
</dependency>
```

### Gradle Dependencies
```groovy
implementation 'org.slf4j:slf4j-api:2.0.9'
implementation 'ch.qos.logback:logback-classic:1.4.11'
```

## Common Anti-Patterns

1. **String concatenation in log statements**
2. **Logging without context**
3. **Using System.out.println instead of logger**
4. **Not clearing MDC in finally blocks**
5. **Excessive logging in production**
6. **Logging sensitive information**
7. **Using wrong log levels**
8. **Not checking log level before expensive operations**
