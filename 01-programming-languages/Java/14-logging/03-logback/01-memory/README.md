# Memory Considerations in Logback

## Event Object Creation

```java
// Each log call creates a LoggingEvent
// LoggingEvent contains: timestamp, thread, level, logger, message, marker, MDC, throwable

// Lightweight events (no exception):
// ~200-300 bytes per event

// Events with full stack trace:
// ~1-5 KB per event (depends on stack depth)

// Logback reuses some objects internally
// But each event is a new object (necessary for async)
```

## AsyncAppender Queue Memory

```xml
<appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
    <queueSize>1024</queueSize>  <!-- 1024 × ~300 bytes = ~300KB -->
</appender>
```

**Memory implications:**
- Queue holds references to LoggingEvent objects
- If appender is slow, queue fills up
- Old events may be garbage collected before writing (if `neverBlock=true`)
- Total queue memory: `queueSize × avgEventSize`

## Rolling File Appender

```xml
<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
    <fileNamePattern>app.%d{yyyy-MM-dd}.log</fileNamePattern>
    <maxHistory>30</maxHistory>
    <totalSizeCap>10GB</totalSizeCap>
</rollingPolicy>
```

**Disk usage control:**
- `maxHistory`: Deletes files older than N periods
- `totalSizeCap`: Enforces total disk usage limit
- Cleanup runs at rollover time
- Files are compressed (`.gz`) if `compression` enabled

## Encoder Buffer

```xml
<encoder>
    <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    <immediateFlush>false</immediateFlush>  <!-- Buffer before flushing -->
</encoder>
```

**Buffer strategy:**
- Encoder buffers output before flushing to OutputStream
- Reduces I/O syscalls (batching)
- `immediateFlush=false` reduces I/O but delays output
- Buffer size configurable via `outputBufferSize`

## Filter State

```xml
<!-- Filters can maintain state -->
<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
    <level>INFO</level>
</filter>
```

**Memory per filter:**
- Simple filters (Threshold): ~16 bytes
- Evaluator filters (GEventEvaluator): ~1KB (compiled expression)
- Each appender with filters adds to per-logger memory

## Best Practices Summary

| Practice | Memory Impact |
|----------|--------------|
| Use AsyncAppender | Bounded queue, predictable memory |
| Set `maxHistory` | Limits disk usage |
| Set `totalSizeCap` | Enforces total disk limit |
| Use `immediateFlush=false` | Reduces I/O buffer churn |
| Avoid verbose patterns | Smaller event objects |
| Clean MDC in finally | Prevents ThreadLocal leaks |
