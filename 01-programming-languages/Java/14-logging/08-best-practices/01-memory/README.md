# Memory Considerations: Best Practices Impact

## Practice Impact Summary

| Practice | Memory Impact | Priority |
|----------|--------------|----------|
| Private static final logger | Prevents logger object proliferation | Critical |
| Parameterized logging | 50-70% fewer String objects | High |
| Exception as last argument | Avoids toString() allocation | High |
| MDC cleanup in finally | Prevents ThreadLocal leaks | Critical |
| Guard expensive operations | Zero allocation if level disabled | Medium |
| Async appenders | Bounds queue memory | Medium |

## Logger Declaration Memory

```java
// WRONG: Creates logger per instance
public class MyClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    // Each instance: ~200-400 bytes for logger
    // 1000 instances: ~200-400 KB wasted
}

// CORRECT: Shared static logger
public class MyClass {
    private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
    // All instances share one logger: ~200-400 bytes total
}
```

## Parameterized Logging Memory

```java
// String concatenation: Always creates objects
logger.debug("User " + userId + " action " + action);
// Per call: StringBuilder + 3 Strings + final String = ~4 objects

// Parameterized: Creates objects only if level enabled
logger.debug("User {} action {}", userId, action);
// Per call when enabled: Object[2] + FormattingTuple = ~2 objects
// Per call when disabled: 0 objects
```

## MDC Leak Impact

```java
// Thread pool with 100 threads
// Each thread retains MDC until cleared
// If not cleaned after 1000 requests:
// 1000 entries × 100 bytes = 100 KB leaked
// Over hours: GB of leaked memory

// With proper cleanup:
// Only current thread's MDC retained
// 100 threads × 5 entries × 100 bytes = 50 KB total
```

## Best Practices ROI

| Practice | Effort | Memory Savings | Debugging Improvement |
|----------|--------|---------------|----------------------|
| Static final logger | Trivial | High | Low |
| Parameterized logging | Low | High | Medium |
| Exception as last arg | Low | Medium | High |
| MDC cleanup | Low | High | Medium |
| Guard operations | Low | Medium | Low |
| Async appenders | Medium | Low | Low |

## Measurement

```java
// Monitor memory usage in production
Runtime runtime = Runtime.getRuntime();
long usedMemory = runtime.totalMemory() - runtime.freeMemory();
logger.info("Memory usage: {}MB", usedMemory / (1024 * 1024));

// Track GC activity
// If GC pauses correlate with logging, optimize logging
```
