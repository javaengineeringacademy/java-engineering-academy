# Memory Considerations in Structured Logging

## JSON Object Creation

```java
// Each structured log event creates:
// 1. JSONObject/Map for the event
// 2. String keys for each field
// 3. Value wrappers (for numbers, booleans)
// 4. Serialized byte array

// Typical memory per event:
// Text log: ~200-300 bytes
// JSON log: ~500-800 bytes (1.5-2x larger)
```

## Field Storage

```java
// MDC fields are included in JSON output
// Each MDC entry = 1 JSON field = ~100-200 bytes in output

// With 5 MDC keys:
// request + userId + sessionId + service + version
// = ~500-1000 bytes added to each event

// Memory impact in async queue:
// 1024 events × 800 bytes = ~800KB queue memory
```

## String Deduplication

```json
// Same logger name repeated thousands of times:
{"logger":"com.myapp.service.UserService"}
{"logger":"com.myapp.service.UserService"}
{"logger":"com.myapp.service.UserService"}

// JSON libraries can intern strings
// Logstash encoder: uses String.intern() for logger names
// Reduces memory for repetitive fields
```

## Buffer Management

```xml
<!-- Encoder buffer size affects memory usage -->
<encoder class="net.logstash.logback.encoder.LogstashEncoder">
    <!-- Default: 8KB buffer -->
    <outputBufferSize>16384</outputBufferSize>
</encoder>
```

**Buffer strategy:**
- Larger buffers = fewer allocations, more memory per encoder
- Smaller buffers = more allocations, less memory per encoder
- For high-throughput: larger buffers are better

## Compression

```xml
<!-- Compress log files to reduce disk usage -->
<rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
    <fileNamePattern>app.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
    <maxFileSize>100MB</maxFileSize>
</rollingPolicy>
```

**Compression impact:**
- JSON compresses well (30-60% reduction)
- gzip adds CPU overhead
- Reduces disk usage and network transfer

## Best Practices Summary

| Practice | Memory Impact |
|----------|--------------|
| Use compact JSON mode | ~30% smaller output |
| Limit MDC fields | Reduces per-event size |
| Use async appenders | Bounds queue memory |
| Configure buffer size | Optimizes allocation |
| Compress old files | Reduces disk usage |
| Deduplicate strings | Reduces heap usage |
