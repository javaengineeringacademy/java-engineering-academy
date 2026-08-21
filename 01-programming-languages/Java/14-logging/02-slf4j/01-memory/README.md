# Memory Considerations in SLF4J

## Logger Instance Management

```java
// Each logger consumes ~200-400 bytes of heap
// Logger hierarchy shares parent references

// LEAK: Creating loggers dynamically
public void process(String category) {
    // BAD: Creates new logger every call
    Logger logger = LoggerFactory.getLogger("dynamic." + category);
    logger.info("Processing");
}

// CORRECT: Use pre-defined loggers
private static final Logger PROCESSOR_LOGGER = 
    LoggerFactory.getLogger("myapp.processor");
```

## Parameterized Message Memory

```java
// SLF4J creates objects ONLY when level is enabled
logger.debug("Processing {}", expensiveOperation());

// What happens internally:
// 1. Check: isDebugEnabled() → false → STOP (no objects created)
// 2. If enabled: 
//    - Object[] args = new Object[]{expensiveOperation()}  (1 allocation)
//    - FormattingTuple tuple = MessageFormatter.arrayFormat(msg, args) (1 allocation)
//    - Message sent to appender
//    - Total: 2 objects created

// BAD: Always creates objects
if (logger.isDebugEnabled()) {
    logger.debug("Processing: " + expensiveToString());
}
// Creates: StringBuilder, multiple String fragments, final String
```

## MDC ThreadLocal Impact

```java
// Each MDC map entry is stored in ThreadLocal
// Thread pools retain threads → MDC values persist

// LEAK: Not clearing MDC in thread pools
ExecutorService executor = Executors.newFixedThreadPool(10);
executor.submit(() -> {
    MDC.put("requestId", "123");
    processRequest();
    // MDC NOT cleared → requestId stays in ThreadLocal
});

// CORRECT: Always clean in finally
executor.submit(() -> {
    MDC.put("requestId", "123");
    try {
        processRequest();
    } finally {
        MDC.clear();  // Removes all entries
    }
});
```

## String Formatting Overhead

```java
// SLF4J uses MessageFormatter internally
// It avoids regex and uses simple {} replacement

// Performance comparison (approximate):
// String concatenation: ~50-100ns per character
// SLF4J parameterized: ~20-50ns total (fixed overhead)
// String.format: ~500-1000ns (regex-based)

// Use parameterized logging - it's both correct and performant
```

## Logback Async Appender Memory

```xml
<!-- AsyncAppender uses a bounded queue -->
<appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
    <queueSize>1024</queueSize>  <!-- Fixed array of 1024 event references -->
    <!-- If queue full: either blocks or discards based on config -->
</appender>
```
