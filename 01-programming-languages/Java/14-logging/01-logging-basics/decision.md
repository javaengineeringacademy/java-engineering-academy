# Decision Framework: Java Logging

## When to Use Which Log Level

### TRACE

```java
// Use for extremely detailed information, typically only during development
if (logger.isTraceEnabled()) {
    logger.trace("Entering method: processOrder(orderId={}, items={})", 
        order.getId(), order.getItems().size());
}
```

**Use when:**
- Tracing method entry/exit in complex algorithms
- Recording variable values at specific points
- Debugging production issues with temporary TRACE-level logging

**Avoid when:**
- Logging in hot paths without level checks
- Capturing potentially large collections without size checks

### DEBUG

```java
// Development-time diagnostic information
logger.debug("Cache hit for key={}, returning cached value", cacheKey);
logger.debug("Query executed in {}ms, returned {} rows", duration, results.size());
```

**Use when:**
- Recording state changes during processing
- Tracking conditional logic branches
- Measuring performance of specific operations
- Documenting business rule decisions

### INFO

```java
// Normal, expected operations
logger.info("Application started on port {}", serverPort);
logger.info("User {} authenticated successfully", username);
logger.info("Order {} completed, total={}", orderId, total);
```

**Use when:**
- Application lifecycle events (start, stop, config load)
- Business transactions completing successfully
- Significant state changes that are expected
- Periodic status reports

### WARN

```java
// Something unexpected but not necessarily wrong
logger.warn("User {} failed login attempt {} of {}", username, attempt, maxAttempts);
logger.warn("Configuration key {} not found, using default: {}", key, defaultValue);
logger.warn("Connection pool at {}% capacity, consider increasing", utilization);
```

**Use when:**
- Fallback values are being used
- Retry attempts are occurring
- Deprecated APIs are being called
- Resource thresholds are being approached
- Data quality issues (missing optional fields)

### ERROR

```java
// Something failed and needs attention
logger.error("Failed to process order {}: {}", orderId, e.getMessage(), e);
logger.error("Database connection failed after {} attempts", retryCount, e);
```

**Use when:**
- Operations that should have succeeded failed
- System resources are exhausted
- Data integrity issues are detected
- External service calls fail

**Always include the exception** as the last argument for stack traces.

## Decision Matrix: Logging vs Not Logging

| Scenario | Log Level | Include Stack Trace |
|----------|-----------|-------------------|
| Method entry in debug | TRACE/DEBUG | No |
| Successful CRUD operation | DEBUG | No |
| Application startup config | INFO | No |
| Business transaction complete | INFO | No |
| Retry attempt | WARN | No |
| Fallback to default value | WARN | No |
| Operation failed | ERROR | Yes |
| Exception caught and rethrown | ERROR | Yes |
| Exception caught and handled | WARN or ERROR | Depends on severity |
| External service timeout | WARN or ERROR | Yes |

## Performance Considerations

```java
// BAD: Always evaluates string concatenation
if (logger.isDebugEnabled()) {
    logger.debug("Processing: " + expensiveToString(data));
}

// GOOD: Parameterized logging (SLF4J/Log4j2)
logger.debug("Processing: {}", expensiveToString(data));

// BETTER: If evaluation is expensive, guard it
if (logger.isTraceEnabled()) {
    logger.trace("Detailed state: {}", computeDetailedState(data));
}
```

## When NOT to Log

- **Inside tight loops** without level checks
- **Sensitive data**: passwords, tokens, SSNs, credit cards
- **Redundant information** already captured elsewhere
- **Development-only debugging** that wasn't removed
- **Exception stack traces** for expected/flow-control exceptions
