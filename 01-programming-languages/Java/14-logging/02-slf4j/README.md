# SLF4J: The Simple Logging Facade for Java

## Overview

SLF4J (Simple Logging Facade for Java) provides a **unified API** for logging in Java applications. It decouples your application code from the underlying logging implementation, allowing you to switch implementations without code changes.

## Why SLF4J Exists

Before SLF4J, Java developers faced the "library logging problem":

```java
// Your app uses Log4j 1.x
// Spring uses Jakarta Commons Logging (JCL)
// Hibernate uses java.util.logging (JUL)
// Result: 3 different logging APIs, 3 different configurations
```

SLF4J solves this by providing **one API** that delegates to whichever implementation you choose at deployment time.

## Architecture

```
Your Code → SLF4J API (slf4j-api.jar)
                  ↓
          Binding (logback-classic, log4j-slf4j2-impl, etc.)
                  ↓
          Implementation (Logback, Log4j 2, java.util.logging)
```

## Core API

### Logger Creation

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Recommended: Use class reference
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

// Alternative: Named logger
private static final Logger logger = LoggerFactory.getLogger("myapp.module");
```

### Parameterized Messages

```java
// Basic placeholders
logger.debug("User {} logged in from {}", username, ipAddress);

// Multiple parameters
logger.info("Order {} processed: items={}, total={}", orderId, itemCount, total);

// Exception as last parameter (does not consume a placeholder)
logger.error("Failed to process request {}: {}", requestId, e.getMessage(), e);

// Array parameter
String[] roles = {"admin", "user"};
logger.debug("User {} has roles: {}", username, roles);
```

### Logger Name Hierarchy

```
root
├── academy
│   └── javaengineering
│       └── logging
│           ├── service
│           └── dao
└── org
    └── slf4j
```

**Level inheritance:**
- `academy.javaengineering.logging.service` inherits from `academy.javaengineering.logging`
- If no level is set, inherits from parent, eventually from root

## MDC Integration

SLF4J provides access to Mapped Diagnostic Context:

```java
import org.slf4j.MDC;

MDC.put("requestId", UUID.randomUUID().toString());
MDC.put("userId", currentUser.getId());

try {
    // All log messages in this thread include requestId and userId
    logger.info("Processing request");
} finally {
    MDC.clear();  // Always clean up
}
```

## Fluent API (SLF4J 2.0+)

```java
// New fluent API in SLF4J 2.0
logger.atDebug()
    .setMessage("Processing item")
    .addKeyValue("itemId", itemId)
    .addKeyValue("userId", userId)
    .log();

// With exception
logger.atError()
    .setCause(exception)
    .setMessage("Failed to process")
    .log();
```

## Marker Support

```java
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

// Create markers for filtering
Marker auditMarker = MarkerFactory.getMarker("AUDIT");
Marker securityMarker = MarkerFactory.getMarker("SECURITY");

// Use markers in logging
logger.info(auditMarker, "User {} accessed resource {}", userId, resourceId);
logger.warn(securityMarker, "Failed login attempt from {}", ipAddress);
```

## Configuration Binding

SLF4J 2.x uses `ServiceLoader` to find implementations:

1. Looks for `META-INF/services/org.slf4j.spi.SLF4JServiceProvider`
2. First found provider is used
3. Warnings printed for multiple providers

**Common bindings:**

| Artifact | Implementation |
|----------|---------------|
| `logback-classic` | Logback (native SLF4J) |
| `log4j-slf4j2-impl` | Log4j 2 with SLF4J binding |
| `slf4j-simple` | Simple console output |
| `slf4j-jdk14` | java.util.logging adapter |
| `slf4j-nop` | No operation (disables logging) |

## Common Bridges

| Bridge | Purpose |
|--------|---------|
| `jcl-over-slf4j` | Redirects JCL calls to SLF4J |
| `log4j-over-slf4j` | Redirects Log4j 1.x calls to SLF4J |
| `jul-to-slf4j` | Redirects java.util.logging to SLF4J |

## Best Practices

1. **Depend only on `slf4j-api`** in your library code
2. **Choose implementation at deployment** (Logback or Log4j 2)
3. **Use class reference** for logger naming
4. **Prefer parameterized messages** over string concatenation
5. **Include exceptions as last argument** for stack traces
6. **Use MDC** for request-scoped context
7. **Check levels** before expensive operations
