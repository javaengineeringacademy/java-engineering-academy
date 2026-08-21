# Memory Considerations in Log4j 2

## Garbage-Free Logging

Log4j 2 is designed to minimize GC pressure:

```java
// Traditional logging creates objects:
logger.info("User " + userId + " logged in");  // 3 String objects + StringBuilder

// Log4j 2 parameterized (garbage-free):
logger.info("User {} logged in", userId);
// Creates: Object[1] array (1 allocation)
// Reuses: LogEvent object (from pool)
```

## LogEvent Pool

```java
// Log4j 2 uses MutableLogEvent for reusability
// Events are created once and reused
MutableLogEvent event = new MutableLogEvent();
event.setTimeInMillis(System.currentTimeMillis());
event.setLevel(Level.INFO);
event.setMessage("Processing item {}", itemId);
// Event is reset and reused for next log call
```

**Pool configuration:**
```xml
<Configuration>
    <Appenders>
        <Async name="Async">
            <AppenderRef ref="File"/>
        </Async>
    </Appenders>
</Configuration>
<!-- Async uses Disruptor ring buffer with reusable events -->
```

## String Deduplication

```xml
<!-- Log4j 2 can deduplicate identical strings -->
<Configuration>
    <Appenders>
        <File name="File" fileName="app.log">
            <PatternLayout>
                <Pattern>%d %-5p [%t] %c - %msg%n</Pattern>
            </PatternLayout>
        </File>
    </Appenders>
</Configuration>
```

**How it works:**
- Identical logger names share String references
- Repeated message patterns share String references
- Reduces heap memory for applications with many similar loggers

## Async Memory Footprint

```xml
<AsyncLogger name="com.myapp" level="INFO"/>
<!-- Uses Disruptor ring buffer -->
<!-- Default: 256 events × ~300 bytes = ~75KB -->
<!-- Configurable: -Dlog4j2.asyncLoggerConfigBufferSize=1024 -->
```

## Layout Memory

```xml
<!-- PatternLayout: Creates StringBuilder per event -->
<PatternLayout pattern="%d %-5p [%t] %c - %msg%n"/>

<!-- JsonLayout: More objects but structured output -->
<JsonLayout compact="true"/>

<!-- MinimalLayout: Smallest footprint -->
<PatternLayout pattern="%msg%n"/>
```

## Best Practices Summary

| Practice | Memory Impact |
|----------|--------------|
| Use garbage-free mode | Eliminates per-call allocations |
| AsyncLogger (Disruptor) | Reuses event objects |
| Compact JsonLayout | Smaller event size |
| Set ring buffer size | Bounds async memory usage |
| Avoid excessive MDC | MDC values stored per-thread |
| Use parameterized logging | Avoids string concatenation |
