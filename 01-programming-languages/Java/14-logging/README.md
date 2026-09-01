# Module 14: Logging

> **Difficulty:** ⭐⭐ Intermediate  
> **Reading:** 30 min | **Practice:** 45 min | **Total:** 75 min

## Overview

Logging is essential for debugging, monitoring, auditing, and security. This module covers Java logging frameworks (SLF4J, Logback, Log4j2), structured logging, MDC (Mapped Diagnostic Context), performance optimization, and production logging best practices. Learn to write logs that provide actionable insights without impacting performance.

## Learning Objectives

- [ ] Configure SLF4J with Logback or Log4j2
- [ ] Use MDC for request context tracking
- [ ] Implement structured logging (JSON format)
- [ ] Optimize logging performance in production
- [ ] Apply log levels appropriately
- [ ] Design logging strategies for microservices

## Prerequisites

- Java fundamentals
- Basic understanding of configuration files
- Familiarity with Maven/Gradle dependencies

## History

- **1996** — Java 1.0 introduced `java.util.logging` (JUL) as built-in logging
- **2001** — Log4j 1.x became the de facto logging standard
- **2004** — SLF4J created as a facade API for logging frameworks
- **2005** — Logback created as native SLF4J implementation
- **2012** — Log4j2 released with async logging and performance improvements
- **2017** — Java 9 added `System.Logger` for platform logging
- **2021** — Structured logging became standard for cloud-native applications

## Production Notes

- **Where is it used?** In every Java application for debugging, monitoring, and auditing
- **Why is it useful?** Provides visibility into application behavior, errors, and performance
- **When should it be avoided?** Not applicable; logging is essential
- **Alternative?** No logging (unacceptable), `System.out.println` (unstructured, slow)

## Why This Concept Exists

Without logging:
- Cannot debug production issues
- Cannot monitor application health
- Cannot audit user actions
- Cannot detect security incidents
- Cannot measure performance

## Core Concepts

### Logging Architecture

```
┌─────────────────────────────────────┐
│       Java Logging Architecture     │
├─────────────────────────────────────┤
│  Application Code                   │
│  ┌─────────────────────────────┐    │
│  │ logger.info("message")      │    │
│  └─────────────────────────────┘    │
│           ↓                         │
│  Logging Facade (SLF4J)            │
│  ┌─────────────────────────────┐    │
│  │ Unified API                 │    │
│  └─────────────────────────────┘    │
│           ↓                         │
│  Implementation                     │
│  ┌─────────────────────────────┐    │
│  │ Logback │ Log4j2 │ JUL     │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

### Log Levels

| Level | Purpose | When to Use |
|-------|---------|-------------|
| ERROR | Unexpected errors | Exceptions, failures |
| WARN | Potential issues | Degraded functionality |
| INFO | Business events | Request/response, state changes |
| DEBUG | Development details | Variable values, flow |
| TRACE | Fine-grained details | Method entry/exit |

### SLF4J + Logback Configuration

```xml
<!-- logback.xml -->
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>app.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

## Internal Working

### MDC (Mapped Diagnostic Context)

```java
// Add context
MDC.put("userId", "12345");
MDC.put("requestId", "abc-def");

// Log with context
logger.info("Processing request"); // Includes userId and requestId

// Remove context
MDC.clear();
```

### Structured Logging (JSON)

```java
// Instead of:
logger.info("User {} logged in from {}", userId, ip);

// Use structured:
logger.info(jsonify(Map.of(
    "event", "user_login",
    "userId", userId,
    "ip", ip,
    "timestamp", Instant.now()
)));
```

## Syntax

```java
// SLF4J logging
Logger logger = LoggerFactory.getLogger(MyClass.class);
logger.debug("Value: {}", value);
logger.info("Processing: {}", item);
logger.warn("Degraded: {}", reason);
logger.error("Failed: {}", exception, exception);

// MDC
MDC.put("key", "value");
MDC.remove("key");
MDC.clear();

// Performance measurement
long start = System.currentTimeMillis();
// ... operation
logger.info("Operation took {}ms", System.currentTimeMillis() - start);
```

## Examples

### Easy: Basic Logging
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    public User findUser(Long id) {
        logger.debug("Finding user with id: {}", id);
        try {
            User user = repository.findById(id);
            logger.info("Found user: {}", user.getName());
            return user;
        } catch (Exception e) {
            logger.error("Failed to find user: {}", id, e);
            throw e;
        }
    }
}
```

### Medium: MDC for Request Tracking
```java
import org.slf4j.MDC;
import java.util.UUID;

public class RequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("clientIp", request.getRemoteAddr());
        
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}

// Logback pattern: %d{HH:mm:ss.SSS} [%thread] [%X{requestId}] %-5level %logger{36} - %msg%n
// Output: 14:30:15.123 [http-nio-8080-exec-1] [abc-123] INFO  UserService - Found user: Alice
```

### Hard: Structured JSON Logging
```java
import com.fasterxml.jackson.databind.ObjectMapper;

public class StructuredLogger {
    private static final Logger logger = LoggerFactory.getLogger(StructuredLogger.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public void logEvent(String event, Map<String, Object> data) {
        try {
            Map<String, Object> logEntry = new LinkedHashMap<>();
            logEntry.put("timestamp", Instant.now().toString());
            logEntry.put("event", event);
            logEntry.putAll(data);
            
            logger.info(mapper.writeValueAsString(logEntry));
        } catch (Exception e) {
            logger.error("Failed to create structured log", e);
        }
    }
}

// Usage
structuredLogger.logEvent("user_login", Map.of(
    "userId", userId,
    "ip", clientIp,
    "userAgent", userAgent
));
```

### Enterprise: Performance Logging
```java
public class PerformanceLogger {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceLogger.class);
    
    public static <T> T logExecutionTime(Supplier<T> operation, String operationName) {
        long start = System.nanoTime();
        try {
            T result = operation.get();
            long duration = (System.nanoTime() - start) / 1_000_000;
            logger.info("{} completed in {}ms", operationName, duration);
            return result;
        } catch (Exception e) {
            long duration = (System.nanoTime() - start) / 1_000_000;
            logger.error("{} failed after {}ms", operationName, duration, e);
            throw e;
        }
    }
}

// Usage
User user = PerformanceLogger.logExecutionTime(
    () -> userService.findUser(userId),
    "findUser"
);
```

## Performance Considerations

| Operation | Cost | Notes |
|-----------|------|-------|
| String concatenation | O(n) | Use parameterized logging |
| Parameterized logging | ~1μs | Use `{}` placeholders |
| String concatenation in log | ~10μs | Avoid with lazy evaluation |
| JSON serialization | ~100μs | Cache when possible |

## Best Practices

**Do's:**
- Use SLF4J facade with Logback/Log4j2 implementation
- Use parameterized logging: `logger.info("Value: {}", value)`
- Use MDC for request context
- Use appropriate log levels
- Include exception stack traces
- Use structured logging for production

**Don'ts:**
- Don't use `System.out.println` in production
- Don't concatenate strings in log statements
- Don't log sensitive data (passwords, tokens)
- Don't use DEBUG/TRACE in production
- Don't create logger per method (use static final)

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| String concatenation in log | Performance penalty | Use parameterized logging |
| Logging sensitive data | Security breach | Sanitize sensitive fields |
| No MDC context | Cannot trace requests | Add requestId, userId to MDC |
| Wrong log level | Noise or missing info | Use appropriate levels |
| Missing exception in log | Lost debugging info | Always log exception object |

## Interview Questions

### Q1: What is the difference between SLF4J and Logback?
**Answer:** SLF4J is a facade API (interface). Logback is an implementation. Use SLF4J API in code, Logback for configuration and execution. This allows switching implementations without changing code.

### Q2: What is MDC and why is it useful?
**Answer:** MDC (Mapped Diagnostic Context) adds thread-local context to logs. Use for request IDs, user IDs, correlation IDs. Enables tracing requests across components.

### Q3: What is structured logging?
**Answer:** Logging in JSON format with fields (timestamp, event, userId, etc.). Enables log aggregation (ELK, Splunk), searching, and analysis. Essential for microservices.

### Q4: What is the difference between `toString()` and parameterized logging?
**Answer:** Parameterized logging (`logger.info("Value: {}", value)`) is lazy — string is only created if log level is enabled. `toString()` is always called, wasting CPU.

### Q5: How do you handle logging in async code?
**Answer:** Use MDC in thread pools: `MDC.getCopyOfContextMap()` before, `MDC.setContextMap()` after. Use `ScheduledExecutorService` with MDC-aware wrappers.

### Q6: What is the performance impact of logging?
**Answer:** Logging is I/O-bound. Console logging is ~1μs. File logging is ~10μs. Network logging is ~100μs. Use async logging and appropriate levels to minimize impact.

### Q7: What is the difference between `log.error("msg", e)` and `log.error("msg" + e)`?
**Answer:** The first logs the full stack trace. The second logs only the exception message. Always pass the exception as the last argument for full stack traces.

### Q8: How do you configure logging for microservices?
**Answer:** Use structured JSON logging, include correlation IDs via MDC, configure log aggregation (ELK/Datadog), use appropriate levels per service, and implement health check logging.

### Q9: What is async logging and when to use it?
**Answer:** Async logging buffers log events and writes them in a separate thread. Use for high-throughput applications to reduce logging latency. Logback and Log4j2 both support async appenders.

### Q10: What is the difference between Logback and Log4j2?
**Answer:** Logback is simpler, native SLF4J implementation. Log4j2 has more features (async logging, lambda support, plugin system). Both are performant; choose based on needs.

## Cross-References

- **Previous Module:** [13 - Reflection & Annotations](../13-reflection-annotations/)
- **Next Module:** [15 - Senior](../15-senior/)
- **Related:** [09 - Multithreading](../09-multithreading-&-concurrency/) — MDC in thread pools
- **Related:** [03 - Exceptions](../03-exceptions/) — logging exceptions properly
- **Related:** [15 - Senior](../15-senior/) — production logging patterns

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Missing logs | Check log level | Verify level isn't filtering desired logs |
| No MDC context | Check thread propagation | Use `MDC.getCopyOfContextMap()` in thread pools |
| Log file not created | Check permissions | Verify log directory exists and is writable |
| Performance impact | Async logging | Use async appenders |
| Structured log parsing | Log aggregation | Use ELK/Splunk for JSON logs |

## Code Review Checklist

- [ ] SLF4J used (not `System.out.println`)
- [ ] Parameterized logging (not string concatenation)
- [ ] MDC used for request context
- [ ] Appropriate log levels
- [ ] Exception stack traces logged
- [ ] No sensitive data logged
- [ ] Structured logging for production

## Architecture Considerations

Logging is a cross-cutting concern that affects every layer of an application. At scale, logging strategy determines debuggability, monitoring, and compliance. For microservices, structured logging with correlation IDs enables distributed tracing. For event-driven systems, logging provides audit trails.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Facade pattern (SLF4J) | Framework abstraction | Pros: Flexible implementation; Cons: Extra layer |
| Async logging | High throughput | Pros: Low latency; Cons: Complexity, potential data loss |
| Structured logging | Log aggregation | Pros: Searchable, analyzable; Cons: Larger log files |
| MDC | Request tracing | Pros: Context propagation; Cons: Thread-local overhead |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Logging sensitive data | Information exposure | Sanitize PII, passwords, tokens |
| Log injection | Code injection | Validate log inputs |
| Log file access | Unauthorized access | Restrict file permissions |
| Log forging | False audit trails | Use structured logging |
| Missing audit logs | Compliance failure | Ensure critical events logged |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | `java.util.logging` | Use SLF4J facade |
| Log4j 1.x | De facto standard | Migrate to Logback or Log4j2 |
| SLF4J | Facade pattern | Use SLF4J API |
| Log4j 2 | Async logging, JSON | Use for high-performance needs |
| Java 9 | `System.Logger` | Use SLF4J for applications |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| SLF4J 2.x | Java 8+ | Stable |
| Logback 1.4 | Java 11+ | Stable |
| Log4j2 2.x | Java 8+ | Stable |
| Structured logging | Any | Best practice |

## Production Incidents

### Incident 1: Logging Sensitive Data

**Problem:** A logging system exposed user passwords and credit card numbers in log files.
**Cause:** Developer logged full request objects including sensitive fields.
**Impact:** Security breach; compliance violation; customer notification required.
**Detection:** Security audit identified sensitive data in logs.
**Solution:** Implemented log sanitization; removed sensitive fields; added PII masking.
**Prevention:** Use log sanitization library; never log sensitive data; implement log review process.

### Incident 2: Logging Causing Performance Degradation

**Problem:** A high-throughput API slowed from 10ms to 100ms after adding DEBUG logging.
**Cause:** DEBUG logs were enabled in production; string concatenation in log statements.
**Impact:** 10x latency increase; SLA violations; customer complaints.
**Detection:** Performance monitoring showed latency spike; profiling revealed logging overhead.
**Solution:** Disabled DEBUG logs; used parameterized logging; implemented async logging.
**Prevention:** Use appropriate log levels; parameterize logs; benchmark logging impact.

### Incident 3: Missing MDC Context

**Problem:** A microservice couldn't trace requests across components; logs were uncorrelated.
**Cause:** MDC context wasn't propagated to async thread pools.
**Impact:** Debugging took hours; mean time to resolution increased 5x.
**Detection:** Support team couldn't correlate logs; investigation revealed missing context.
**Solution:** Implemented MDC propagation in thread pools; added correlation IDs.
**Prevention:** Always propagate MDC in async code; use correlation IDs in microservices.

## Production Checklist

- [ ] SLF4J used (not `System.out.println`)
- [ ] Parameterized logging (not string concatenation)
- [ ] MDC used for request context
- [ ] Appropriate log levels configured
- [ ] Exception stack traces logged
- [ ] No sensitive data logged
- [ ] Structured logging for production
- [ ] Async logging for high throughput
- [ ] Log rotation configured
- [ ] Log aggregation configured

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses `System.out.println`; doesn't use MDC; logs everything |
| Intermediate | Uses SLF4J; parameterized logging; appropriate levels |
| Advanced | Uses MDC; structured logging; async logging |
| Expert | Designs logging strategy; implements log aggregation; mentors on logging |

## Common Myths

1. **Myth**: More logging is always better
   **Truth**: Excessive logging creates noise and performance overhead. Log meaningful events at appropriate levels.

2. **Myth**: `System.out.println` is fine for small apps
   **Truth**: `System.out.println` is unstructured, unbuffered, and cannot be configured. Use SLF4J even for small apps.

3. **Myth**: Logging performance doesn't matter
   **Truth**: Logging is I/O-bound and can impact throughput. Use async logging and appropriate levels.

4. **Myth**: Structured logging is only for microservices
   **Truth**: Structured logging benefits all applications by enabling search, analysis, and monitoring.

5. **Myth**: Log files don't need rotation
   **Truth**: Unbounded log files fill disk. Always configure rotation and retention.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Debugging, monitoring, auditing |
| Architecture | Facade (SLF4J) + Implementation (Logback/Log4j2) |
| Levels | ERROR, WARN, INFO, DEBUG, TRACE |
| MDC | Thread-local context for request tracking |
| Structured | JSON format for log aggregation |
| Best practice | Parameterized logging, MDC, appropriate levels |
| Common mistake | String concatenation in log statements |
| When to use | All Java applications |
| When to avoid | Never — logging is essential |
