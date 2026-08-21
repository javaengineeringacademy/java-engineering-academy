# Memory Considerations in Logging

## String Creation Overhead

Every log message creates objects in memory:

```java
// BAD: Creates concatenated String regardless of log level
logger.debug("Processing item " + itemId + " for user " + userId);

// Object allocation:
// 1. StringBuilder (implicit from concatenation)
// 2. Multiple String objects for each "+"
// 3. Final concatenated String
// TOTAL: ~4-5 objects allocated and immediately garbage collected
```

```java
// GOOD: No allocation if DEBUG is disabled
logger.debug("Processing item {} for user {}", itemId, userId);

// Object allocation when DEBUG enabled:
// 1. Object[] array for varargs
// 2. FormattingTuple (MessageFormatter result)
// TOTAL: 2 objects (or 0 if level is disabled)
```

## Exception Logging and Stack Traces

```java
// EXPENSIVE: toString() on exception generates full stack trace string
logger.error("Failed: " + exception.toString());

// CHEAP: Exception passed as last argument, logged only if level enabled
logger.error("Failed to process request", exception);
```

**Why this matters:**
- `Exception.toString()` builds a String with all stack frames
- `logger.error(message, exception)` passes the exception object directly
- The stack trace is only serialized if the ERROR level is active

## Logger Instance Memory

```java
// Each logger consumes ~100-200 bytes of heap
// With hierarchical naming, loggers share parent references

// MEMORY WASTE: Creating loggers in loops
for (int i = 0; i < 10000; i++) {
    Logger logger = LoggerFactory.getLogger("module." + i); // LEAK!
}

// CORRECT: Use class reference or static logger
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
```

## MDC ThreadLocal Overhead

```java
// MDC uses ThreadLocal storage
// Each thread gets its own copy of the MDC map
// Typical overhead: ~200-500 bytes per thread

MDC.put("userId", user.getId());      // Added to ThreadLocal Map
MDC.put("requestId", requestId);      // Each put is an entry

// CLEANUP IS CRITICAL for thread pools
// Without cleanup, values persist and leak memory
try {
    MDC.put("userId", user.getId());
    processRequest();
} finally {
    MDC.clear();  // Always clear after use
}
```

## Log File Size Management

```xml
<!-- Rolling policies prevent unbounded disk usage -->
<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
    <fileNamePattern>app.%d{yyyy-MM-dd}.log</fileNamePattern>
    <maxHistory>30</maxHistory>           <!-- Keep 30 days -->
    <totalSizeCap>10GB</totalSizeCap>     <!-- Total disk limit -->
</rollingPolicy>
```

## Best Practices Summary

| Practice | Memory Impact |
|----------|--------------|
| Use parameterized logging | Avoids string concatenation objects |
| Guard expensive operations | No allocation if level disabled |
| Log exceptions as last argument | Avoids toString() allocation |
| Clean up MDC in finally blocks | Prevents ThreadLocal leaks |
| Configure rolling policies | Controls disk usage |
| Don't create loggers in loops | Prevents logger object proliferation |
