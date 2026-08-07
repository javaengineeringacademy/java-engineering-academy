# Logging Module

> **Difficulty:** ⭐⭐ Easy  
> **Reading:** 20 min | **Practice:** 30 min | **Total:** 50 min

## Overview
Without logging, production debugging becomes guesswork. Logging frameworks provide structured, leveled output that helps you trace issues, audit user actions, and monitor system health. This module covers SLF4J, Logback, and Log4j2 — from basic configuration to performance-optimized async logging and structured output for log aggregation.

## Key Concepts

### 1. SLF4J (Simple Logging Facade for Java)
- Logging abstraction layer
- Backend-agnostic logging API
- Common methods: `trace()`, `debug()`, `info()`, `warn()`, `error()`

### 2. Logback
- Native SLF4J implementation
- High performance
- Advanced configuration options

### 3. Log4j2
- High-performance logging framework
- Asynchronous logging support
- Plugin architecture

### 4. Logging Best Practices
- Use appropriate log levels
- Include context in log messages
- Avoid logging sensitive information
- Use structured logging

## Module Structure
- `Slf4jBasics.java` - SLF4J API usage
- `LogbackConfiguration.java` - Logback setup and configuration
- `StructuredLogging.java` - Structured logging patterns
- `PerformanceLogging.java` - Performance-optimized logging
- `LoggingBestPractices.java` - Best practices and patterns

## Performance

| Aspect | Impact | Recommendation |
|--------|--------|----------------|
| String concatenation in log statements | High — always evaluated | Use parameterized logging |
| Disabled log level check | Low — typically O(1) | SLF4J checks level before formatting |
| Async logging | Significantly reduces I/O blocking | Use for high-throughput applications |
| Log file rotation | Minimal | Configure SizeAndTimeBasedRollingPolicy |
| JSON/structured logging | Moderate | Worth it for log aggregation systems |

**Benchmarks (approximate):**
- `logger.debug("msg")` when disabled: ~1-5ns (JIT eliminates)
- `logger.debug("msg " + var)` when disabled: ~50-200ns (string still built)
- `logger.debug("msg {}", var)` when disabled: ~5-10ns (parameterized)
- Async appender throughput: ~100k-1M events/sec (vs 10k-50k sync)

**Optimization tips:**
1. Always use parameterized logging: `logger.debug("user={}", user)` NOT `logger.debug("user=" + user)`
2. Use `isDebugEnabled()` guard for expensive operations: `if (logger.isDebugEnabled()) logger.debug("expensive: {}", compute())`
3. Use async appenders for high-throughput logging
4. Configure appropriate log levels per package
5. Use structured logging (JSON) for machine-parseable output
6. Avoid logging in tight loops — sample instead
7. Use MDC (Mapped Diagnostic Context) for request-scoped context instead of concatenating thread info

## Alternatives

| Framework | Performance | Async | Features | Use When |
|-----------|-------------|-------|----------|----------|
| System.out | Fastest | No | None | Quick debugging only |
| java.util.logging | Moderate | Limited | Basic | JDK-only environments |
| Log4j 1.x | Moderate | No | Basic | Legacy (avoid) |
| Logback | Fast | Yes | Rich | Default SLF4J implementation |
| Log4j2 | Fastest | Yes | Richest | High-performance needs |
| SLF4J | Facade | N/A | N/A | Always use as facade |

## Examples

```java
// SLF4J — standard logging facade
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User createUser(String name) {
        logger.info("Creating user: name={}", name);
        try {
            User user = repository.save(new User(name));
            logger.info("User created: id={}", user.getId());
            return user;
        } catch (Exception e) {
            logger.error("Failed to create user: name={}", name, e);
            throw e;
        }
    }
}

// Structured Logging (JSON output for log aggregation)
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public void processOrder(Order order) {
        // Structured context via MDC
        MDC.put("orderId", order.getId());
        MDC.put("customerId", order.getCustomerId());

        logger.info("Processing order");
        logger.info("Order validated");
        logger.info("Order completed");

        MDC.clear();
    }
}

// Performance-aware logging
public class DataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DataProcessor.class);

    public void processLargeDataset(List<Data> data) {
        int processed = 0;
        for (Data item : data) {
            process(item);
            processed++;
            if (processed % 10000 == 0) {
                logger.info("Progress: {}/{} items processed", processed, data.size());
            }
        }
        logger.info("Dataset processing complete: {} items", processed);
    }
}
```

## Internal Working

**How logging frameworks work under the hood:**

1. **SLF4J (Facade)**: SLF4J is a thin API layer. It delegates to a backend (Logback, Log4j2) at runtime via static binding. The `LoggerFactory.getLogger()` call finds the appropriate implementation on the classpath.

2. **Logback architecture**:
   - `Logger` → `Appender` → `Layout` pipeline
   - Logger hierarchy mirrors package hierarchy
   - Level inheritance: child loggers inherit parent level if not set
   - `Appender` writes to destination (console, file, network)
   - `Layout` formats the log event into text/JSON

3. **Log4j2 architecture**:
   - Uses `Logger` → `Appender` → `Layout` similar to Logback
   - Asynchronous loggers use LMAX Disruptor ring buffer for high throughput
   - Plugin architecture for custom appenders and layouts
   - Garbage-free logging for zero GC in steady state

4. **Parameterized logging**: `logger.debug("user={}", name)` defers string formatting until the message is actually needed. If the level is disabled, no string concatenation occurs.

5. **MDC (Mapped Diagnostic Context)**: Thread-local map that stores context (request ID, user ID). Appenders can include MDC values in output. Automatically cleared when thread returns to pool.

6. **Log rotation**: Rolling file appenders monitor file size/time and rotate to new files. `SizeAndTimeBasedRollingPolicy` combines both. Old files are compressed and optionally deleted.

## Trade-offs

Logging gives you visibility but costs:
- Performance: Even disabled log checks have overhead (branch prediction)
- Disk space: Logs consume storage rapidly in production
- Security: Logs can leak sensitive data (PII, tokens)
- Complexity: Async logging adds thread management overhead

Use logging when:
- You need to trace production issues
- You're building audit trails
- You need performance metrics
- Debugging distributed systems

Avoid excessive logging when:
- You're in a hot path (inner loops)
- Data is sensitive (PII, passwords)
- The operation is trivial and expected to succeed

## Why This Concept Exists

Logging exists because:

1. **Observability** — You cannot fix what you cannot see. Logs provide visibility into runtime behavior
2. **Debugging** — Reproducing issues in development is often impossible; logs capture the state at the time of failure
3. **Monitoring** — Logs feed into alerting systems, dashboards, and anomaly detection
4. **Audit trail** — Security, compliance, and business requirements demand action logs
5. **Distributed tracing** — In microservices, logs with correlation IDs enable request tracking across services
6. **Post-mortem analysis** — When production incidents occur, logs are the primary source of forensic data

Without proper logging, production debugging becomes guesswork, and system behavior becomes opaque.

## Production Checklist

### ✅ Before using logging in production:

☐ I'm using SLF4J facade (not System.out)
☐ I log at the right level (ERROR for failures, WARN for issues, INFO for milestones, DEBUG for diagnostics)
☐ I don't log sensitive data (passwords, tokens, PII)
☐ I use parameterized logging (not string concatenation)
☐ I have log rotation configured
☐ I know my logging framework's async options
☐ I've tested logging doesn't impact performance

## Common Mistakes
1. Using wrong log level for messages
2. Not including context in log messages
3. Logging sensitive information
4. Not configuring log rotation

## Engineering Maturity Levels

### Level 1: Can Use
- Knows System.out.println basics
- Can use logger.info() and logger.error()

### Level 2: Understands
- Knows log levels (TRACE, DEBUG, INFO, WARN, ERROR)
- Understands structured logging

### Level 3: Deep Knowledge
- Knows SLF4J facade pattern
- Understands MDC (Mapped Diagnostic Context)

### Level 4: Expert
- Configures log rotation and retention
- Knows async logging for performance

### Level 5: Master
- Designs logging architecture for distributed systems
- Knows ELK stack, Fluentd, log aggregation

## Common Myths

### ❌ Myth 1: System.out.println is fine for production
**Reality:** No log levels, no rotation, no timestamps, no structured output. Use SLF4J.

### ❌ Myth 2: More logging is always better
**Reality:** Excessive logging wastes I/O and disk. Log at the right level for the right situation.

### ❌ Myth 3: String concatenation is fine for logging
**Reality:** `"User: " + name` evaluates even if debug is disabled. Use `logger.debug("User: {}", name)`.

### ❌ Myth 4: Log4j 1.x is still fine
**Reality:** End of life, known vulnerabilities. Use Logback or Log4j2.

### ❌ Myth 5: Logging frameworks are interchangeable
**Reality:** SLF4J is a facade, not an implementation. You need Logback or Log4j2 behind it.

## Interview Questions
1. What is the difference between SLF4J and Log4j?
2. How do you configure log levels?
3. What is structured logging?
4. How do you handle log rotation?
5. What are the performance implications of logging?

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)

## Prerequisites

- [Fundamentals](../01-fundamentals/README.md)

## Related Topics

- [Senior](../15-senior/README.md)

## Next

- [Senior](../15-senior/README.md)

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Application monitoring |
| Complexity | O(1) for logging |
| Thread Safe | Yes (SLF4J) |
| Ordered | Yes (log order) |
| Allows Null | Yes |
| Best Alternative | SLF4J with Logback/Log4j2 |
| When to Use | Production monitoring |
| When to Avoid | Debug code |
