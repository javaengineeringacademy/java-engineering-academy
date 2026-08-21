# Memory Considerations in Logging Performance

## String Object Pool

```java
// Java 9+ Compact Strings: ASCII strings use 1 byte/char
// Unicode strings use 2 bytes/char

// Log messages often contain ASCII (timestamps, levels, logger names)
// Compact Strings reduce memory by ~50% for typical logs
```

## Log Event Memory

| Component | Size | Notes |
|-----------|------|-------|
| LogEvent object | ~100-200 bytes | Header fields |
| Message String | ~50-500 bytes | Depends on message length |
| Parameter array | ~16-64 bytes | Object references |
| MDC copy | ~200-500 bytes | Map + entries |
| Exception | ~1-5 KB | Stack trace strings |

## Async Queue Memory

```xml
<!-- Queue memory = queueSize × eventSize -->
<queueSize>256</queueSize>
<!-- 256 × 500 bytes = ~128 KB -->

<queueSize>1024</queueSize>
<!-- 1024 × 500 bytes = ~512 KB -->
```

**Memory vs throughput tradeoff:**
- Larger queue = fewer blocks/drops, more memory
- Smaller queue = more blocks/drops, less memory

## I/O Buffer Memory

```xml
<!-- Encoder buffer: held per appender -->
<outputBufferSize>8192</outputBufferSize>  <!-- 8 KB per appender -->

<!-- Rollback buffer: held per rolling policy -->
<!-- Size depends on max file size -->
```

## GC Pressure Mitigation

```java
// 1. Use parameterized logging (fewer String objects)
// 2. Use async appenders (queue batching)
// 3. Use compact mode (smaller JSON output)
// 4. Use object pooling (Log4j 2 MutableLogEvent)
// 5. Reduce MDC keys (smaller context maps)
```

## Best Practices Summary

| Practice | Memory Impact |
|----------|--------------|
| Parameterized logging | 50-70% fewer objects |
| Async with batching | Fewer I/O buffers |
| Compact JSON | 30% smaller output |
| Object pooling | Eliminates allocation |
| Limit MDC keys | Smaller per-thread maps |
| Buffer before flush | Reduces syscall overhead |
