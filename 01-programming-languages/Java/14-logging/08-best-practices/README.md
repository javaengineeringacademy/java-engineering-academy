# Logging Best Practices

## Overview

This module consolidates all logging best practices into actionable guidelines. Following these practices ensures consistent, maintainable, and effective logging across your Java applications.

## 1. Logger Declaration

```java
// CORRECT: private static final with class reference
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

// WRONG: Instance field
private Logger logger = LoggerFactory.getLogger(MyClass.class);

// WRONG: Public or non-final
public static Logger logger = LoggerFactory.getLogger(MyClass.class);

// WRONG: String name (less type-safe)
private static final Logger logger = LoggerFactory.getLogger("myapp.module");
```

## 2. Log Levels

| Level | Use For | Example |
|-------|---------|---------|
| TRACE | Method entry/exit, variable values | `Entering processOrder(id={})` |
| DEBUG | Development diagnostics | `Cache hit for key={}` |
| INFO | Normal business operations | `Order {} completed` |
| WARN | Unexpected but recoverable | `Retry attempt {} of {}` |
| ERROR | Failures requiring attention | `Failed to process order` |

## 3. Parameterized Logging

```java
// CORRECT: SLF4J/Log4j 2 parameterized
logger.debug("User {} logged in from {}", username, ipAddress);
logger.error("Failed to process order {}: {}", orderId, e.getMessage(), e);

// WRONG: String concatenation
logger.debug("User " + username + " logged in from " + ipAddress);

// WRONG: String.format
logger.debug(String.format("User %s logged in from %s", username, ipAddress));
```

## 4. Exception Logging

```java
// CORRECT: Exception as last argument (full stack trace)
logger.error("Failed to process order", exception);

// CORRECT: Include context with exception
logger.error("Failed to process order {} for user {}", orderId, userId, exception);

// WRONG: Exception in message (loses stack trace)
logger.error("Failed: " + exception.getMessage());

// WRONG: Just toString() (may lose stack trace)
logger.error(exception.toString());
```

## 5. Message Content

```java
// CORRECT: Include context, be specific
logger.info("Order {} processed: items={}, total={}, userId={}",
        orderId, itemCount, total, userId);

// WRONG: Vague, unhelpful
logger.info("Done");
logger.info("Processing...");
logger.info("Error occurred");
```

## 6. Sensitive Data

```java
// WRONG: Logging sensitive data
logger.info("User {} logged in with password {}", username, password);
logger.debug("Credit card: {}", cardNumber);
logger.info("SSN: {}", ssn);

// CORRECT: Log only non-sensitive identifiers
logger.info("User {} authenticated", username);
logger.debug("Payment method: {}", maskCardNumber(cardNumber));
```

## 7. MDC Usage

```java
// CORRECT: Always clean up in finally
MDC.put("requestId", UUID.randomUUID().toString());
MDC.put("userId", userId);
try {
    processRequest();
} finally {
    MDC.clear(); // ALWAYS clean up
}

// WRONG: MDC not cleaned
MDC.put("requestId", requestId);
processRequest();
// MDC values persist in thread pool
```

## 8. Logger in Loops

```java
// CORRECT: Guard expensive operations
for (String item : items) {
    if (logger.isDebugEnabled()) {
        logger.debug("Processing item: {}", item.toString());
    }
    processItem(item);
}

// WRONG: Always creates objects
for (String item : items) {
    logger.debug("Processing item: {}", item.toString());
}
```

## 9. Performance

```java
// CORRECT: Use parameterized logging (default)
logger.debug("Processing item {} for user {}", itemId, userId);

// CORRECT: Guard expensive operations only
if (logger.isTraceEnabled()) {
    String state = expensiveStateDump();
    logger.trace("State: {}", state);
}

// WRONG: Guard everything (unnecessary boilerplate)
if (logger.isDebugEnabled()) {
    logger.debug("Simple message");
}
```

## 10. Configuration

```xml
<!-- CORRECT: Environment-specific levels -->
<logger name="com.myapp.dao" level="DEBUG"/>
<logger name="org.springframework" level="WARN"/>
<logger name="org.hibernate" level="ERROR"/>

<!-- CORRECT: Async for I/O-heavy appenders -->
<appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="FILE"/>
</appender>
```

## Quick Reference Checklist

- [ ] Logger is `private static final` with class reference
- [ ] Using parameterized logging (`{}` placeholders)
- [ ] Exception passed as last argument for stack traces
- [ ] Log messages include relevant context (IDs, counts)
- [ ] No sensitive data in log messages
- [ ] MDC cleaned up in `finally` blocks
- [ ] Expensive operations guarded with level checks
- [ ] Appropriate log levels used
- [ ] Async appenders for file/network output
- [ ] No `System.out.println()` for application logging
