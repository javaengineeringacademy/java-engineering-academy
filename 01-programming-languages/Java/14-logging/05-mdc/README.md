# MDC: Mapped Diagnostic Context

## Overview

Mapped Diagnostic Context (MDC) is a mechanism for enriching log messages with **request-scoped context data**. It stores key-value pairs in thread-local storage, allowing every log message within a thread to automatically include contextual information like request IDs, user IDs, and session data.

## Why MDC?

Without MDC:
```java
logger.info("Request 12345 for user john: Processing order");
logger.info("Request 12345 for user john: Validation passed");
logger.info("Request 12345 for user john: Order completed");
```

With MDC:
```java
MDC.put("requestId", "12345");
MDC.put("userId", "john");
logger.info("Processing order");
logger.info("Validation passed");
logger.info("Order completed");
// Output: [12345] [john] Processing order
```

## SLF4J MDC API

```java
import org.slf4j.MDC;

// Store context
MDC.put("requestId", UUID.randomUUID().toString());
MDC.put("userId", currentUser.getId());
MDC.put("sessionId", session.getId());

// Retrieve context
String requestId = MDC.get("requestId");

// Remove single entry
MDC.remove("requestId");

// Remove all entries
MDC.clear();

// Get all keys
Set<String> keys = MDC.getCopyOfContextMap().keySet();
```

## Log4j 2 ThreadContext

```java
import org.apache.logging.log4j.ThreadContext;

// Log4j 2 equivalent of MDC
ThreadContext.put("requestId", UUID.randomUUID().toString());
ThreadContext.put("userId", currentUser.getId());

// Same operations as SLF4J MDC
String requestId = ThreadContext.get("requestId");
ThreadContext.remove("requestId");
ThreadContext.clearAll();
```

## Pattern Integration

### Logback

```xml
<pattern>%d{HH:mm:ss.SSS} [%thread] [%X{requestId}] [%X{userId}] %-5level %logger - %msg%n</pattern>
```

### Log4j 2

```xml
<pattern>%d{HH:mm:ss.SSS} [%t] [%X{requestId}] [%X{userId}] %-5level %logger - %msg%n</pattern>
```

## Common MDC Keys

| Key | Purpose | Example Value |
|-----|---------|---------------|
| `requestId` | Unique request identifier | `550e8400-e29b-41d4-a716-446655440000` |
| `userId` | Authenticated user | `user-123` |
| `sessionId` | HTTP session | `sess-456` |
| `traceId` | Distributed trace ID | `abc123def456` |
| `spanId` | Current span ID | `span-789` |
| `service` | Microservice name | `order-service` |
| `version` | Application version | `1.2.3` |
| `environment` | Deployment environment | `production` |

## Thread Pool Handling

```java
// CRITICAL: MDC is thread-local!
// Values do NOT propagate to child threads automatically

ExecutorService executor = Executors.newFixedThreadPool(5);

// WRONG: MDC not visible in child thread
executor.submit(() -> {
    String requestId = MDC.get("requestId"); // null!
});

// CORRECT: Copy MDC before submitting
Map<String, String> contextMap = MDC.getCopyOfContextMap();
executor.submit(() -> {
    MDC.setContextMap(contextMap); // Restore context
    try {
        String requestId = MDC.get("requestId"); // works!
        processRequest();
    } finally {
        MDC.clear();
    }
});
```

## Web Framework Integration

### Spring Boot (Automatic)

Spring Boot automatically adds MDC values via `RequestLoggingFilter`:

```yaml
logging:
  pattern:
    "[requestId]": "%X{requestId}"
```

### Servlet Filter

```java
public class MdcFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletRequest request = (HttpServletRequest) req;
        String requestId = UUID.randomUUID().toString();
        
        MDC.put("requestId", requestId);
        MDC.put("userId", request.getRemoteUser());
        
        try {
            chain.doFilter(req, res);
        } finally {
            MDC.clear();
        }
    }
}
```

### Spring Handler Interceptor

```java
public class MdcInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put("requestId", UUID.randomUUID().toString());
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                  Object handler, Exception ex) {
        MDC.clear();
    }
}
```

## MDC and Async

```java
// For CompletableFuture
CompletableFuture.supplyAsync(() -> {
    // MDC is NOT inherited
    MDC.setContextMap(originalContext);
    try {
        return processData();
    } finally {
        MDC.clear();
    }
}, executor);

// For Spring @Async
@Async
public void asyncMethod() {
    // MDC is NOT inherited
    // Need explicit context passing
}
```

## Best Practices

1. **Always clean up MDC** in `finally` blocks
2. **Use meaningful key names** consistent across services
3. **Don't store large objects** in MDC (they persist per-thread)
4. **Handle thread pools** explicitly (copy/restore MDC)
5. **Limit MDC keys** to what's needed for logging
6. **Use MDC for request-scoped data only** (not global state)
7. **Document MDC keys** in your logging convention
