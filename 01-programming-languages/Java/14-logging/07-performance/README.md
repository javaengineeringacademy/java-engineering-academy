# Logging Performance

## Overview

Logging performance is critical in high-throughput applications. Poor logging can become a bottleneck, causing latency spikes and reducing throughput. This module covers optimization strategies, benchmarking, and monitoring logging performance.

## Performance Impact Sources

### 1. String Construction

```java
// EXPENSIVE: Always creates objects
logger.debug("User " + userId + " performed " + action + " on " + resource);

// CHEAP: Only creates objects if level enabled
logger.debug("User {} performed {} on {}", userId, action, resource);
```

### 2. Level Check Cost

```java
// O(1) integer comparison
if (logger.isDebugEnabled()) {
    logger.debug("Expensive: {}", computeExpensiveData());
}
```

### 3. I/O Operations

```
Synchronous:  logger → encoder → file/console (blocking)
Asynchronous: logger → queue → writer thread (non-blocking)
```

### 4. Exception Stack Traces

```java
// Full stack trace: ~1-5 KB, ~10-100 microseconds
logger.error("Failed", exception);

// Just message: ~100 bytes, ~1 microsecond
logger.error("Failed: {}", exception.getMessage());
```

## Optimization Strategies

### 1. Use Parameterized Logging

```java
// BAD: ~50-100ns per character
logger.debug("Processing item " + itemId + " for user " + userId);

// GOOD: ~20-50ns total
logger.debug("Processing item {} for user {}", itemId, userId);
```

### 2. Guard Expensive Operations

```java
// Check before expensive toString()
if (logger.isTraceEnabled()) {
    String state = dumpState();  // Expensive
    logger.trace("State: {}", state);
}

// Parameterized handles this automatically
logger.debug("Data: {}", computeData());
```

### 3. Async Logging

```xml
<!-- Logback AsyncAppender -->
<appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
    <queueSize>1024</queueSize>
    <discardingThreshold>0</discardingThreshold>
    <appender-ref ref="FILE"/>
</appender>

<!-- Log4j 2 AsyncLogger (Disruptor) -->
<AsyncLogger name="com.myapp" level="INFO"/>
```

### 4. Batch Writing

```xml
<!-- Buffer before flushing -->
<encoder>
    <pattern>%d %-5level %logger - %msg%n</pattern>
    <immediateFlush>false</immediateFlush>
</encoder>
```

### 5. Reduce Log Volume

```xml
<!-- Suppress noisy loggers -->
<logger name="org.hibernate" level="WARN"/>
<logger name="org.springframework" level="WARN"/>
<logger name="org.apache" level="ERROR"/>
```

## Benchmarking

### JMH Benchmark

```java
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class LoggingBenchmark {

    private Logger logger;

    @Setup
    public void setup() {
        logger = LoggerFactory.getLogger(LoggingBenchmark.class);
    }

    @Benchmark
    public void testParameterized() {
        logger.debug("Item {} processed", 42);
    }

    @Benchmark
    public void testConcatenation() {
        logger.debug("Item " + 42 + " processed");
    }

    @Benchmark
    public void testGuarded() {
        if (logger.isDebugEnabled()) {
            logger.debug("Item " + 42 + " processed");
        }
    }
}
```

### Expected Results

| Method | Throughput (ops/sec) | Latency (ns/op) |
|--------|---------------------|-----------------|
| Parameterized | ~1,000,000 | ~1,000 |
| Guarded concatenation | ~900,000 | ~1,100 |
| Unguarded concatenation | ~200,000 | ~5,000 |

## Monitoring

### Logback Metrics

```xml
<!-- Enable logback metrics -->
<configuration>
    <appender name="METRICS" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="FILE"/>
    </appender>
</configuration>
```

### Key Metrics to Monitor

| Metric | Warning Threshold | Critical Threshold |
|--------|------------------|-------------------|
| Log events/sec | > 10,000 | > 50,000 |
| Async queue utilization | > 80% | > 95% |
| Log file size/day | > 1 GB | > 10 GB |
| I/O wait time | > 10ms | > 100ms |
| GC pauses from logging | > 5ms | > 50ms |

## Best Practices Summary

| Practice | Impact | Priority |
|----------|--------|----------|
| Use parameterized logging | High | Critical |
| Async for I/O-heavy appenders | High | High |
| Guard expensive operations | Medium | High |
| Reduce noisy logger levels | Medium | Medium |
| Buffer before flushing | Low | Medium |
| Use appropriate log levels | Medium | High |
| Monitor log volume | Low | Low |
