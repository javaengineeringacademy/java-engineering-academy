# Internals: Structured Logging Serialization

## JSON Serialization Process

```
LogEvent object
    ↓
Layout.toSerializable(event)
    ↓
Field mapping:
  - timestamp → event.getTimeMillis()
  - level → event.getLevel().toString()
  - logger → event.getLoggerName()
  - message → event.getMessage().getFormattedMessage()
  - thread → event.getThreadName()
  - MDC → event.getContextMap()
  - exception → event.getThrownProxy()
    ↓
JSON Object construction
    ↓
String serialization
    ↓
Write to output
```

## Field Extraction

```java
// Logstash encoder extracts fields automatically
public class LogstashEncoder extends EncoderBase<ILoggingEvent> {
    
    public byte[] encode(ILoggingEvent event) {
        // 1. Create base JSON object
        // 2. Add standard fields (timestamp, level, logger, etc.)
        // 3. Add MDC fields (configurable which to include)
        // 4. Add exception fields (if present)
        // 5. Add custom fields (from configuration)
        // 6. Serialize to JSON bytes
    }
}
```

## Performance Optimizations

### Compact Mode

```xml
<!-- Reduces output size by ~30% -->
<JsonLayout compact="true"/>
```

### Pre-built Strings

```java
// Avoids string concatenation in hot path
private static final String LEVEL_FIELD = "\"level\":\"";
private static final String LOGGER_FIELD = "\"logger\":\"";
// Pre-concatenated field names reduce allocation
```

### Buffer Reuse

```java
// Encoders can reuse byte buffers
private ByteArrayOutputStream buffer = new ByteArrayOutputStream(4096);

public byte[] encode(ILoggingEvent event) {
    buffer.reset(); // Reuse buffer
    writeJson(event, buffer);
    return buffer.toByteArray();
}
```
