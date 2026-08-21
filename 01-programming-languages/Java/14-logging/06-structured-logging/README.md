# Structured Logging

## Overview

Structured logging produces **machine-parseable log entries** with consistent format, enabling efficient searching, filtering, and analysis. Unlike traditional text logs, structured logs use formats like JSON where each field is explicitly defined.

## Why Structured Logging?

### Traditional Logs
```
2024-01-15 10:30:45 INFO [http-nio-8080-exec-1] c.m.a.s.UserService - User john_doe logged in from 192.168.1.100
```

### Structured Logs (JSON)
```json
{
  "timestamp": "2024-01-15T10:30:45.123Z",
  "level": "INFO",
  "thread": "http-nio-8080-exec-1",
  "logger": "com.myapp.service.UserService",
  "message": "User logged in",
  "userId": "john_doe",
  "ipAddress": "192.168.1.100",
  "requestId": "550e8400-e29b-41d4-a716-446655440000"
}
```

**Benefits:**
- **Queryable** - Search by any field
- **Aggregatable** - Count, group, analyze
- **Correlatable** - Link events across services
- **Parseable** - Machine-readable, no regex needed
- **Indexable** - ELK, Splunk, Datadog can index fields

## JSON Layouts

### Logback JSON

```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

```xml
<appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
    <encoder class="net.logstash.logback.encoder.LogstashEncoder">
        <includeMdcKeyName>requestId</includeMdcKeyName>
        <includeMdcKeyName>userId</includeMdcKeyName>
        <fieldNames>
            <timestamp>[ignore]</timestamp>
            <message>message</message>
        </fieldNames>
    </encoder>
</appender>
```

### Log4j 2 JSON

```xml
<JsonLayout compact="true" eventEol="true">
    <KeyValuePair key="service" value="myapp"/>
    <KeyValuePair key="environment" value="production"/>
</JsonLayout>
```

## Field Naming Conventions

### Standard Fields

| Field | Description | Example |
|-------|-------------|---------|
| `timestamp` | ISO 8601 time | `2024-01-15T10:30:45.123Z` |
| `level` | Log level | `INFO` |
| `logger` | Logger name | `com.myapp.UserService` |
| `thread` | Thread name | `http-nio-8080-exec-1` |
| `message` | Log message | `User logged in` |
| `stack_trace` | Exception stack | Full trace string |

### Context Fields (via MDC)

| Field | Description | Example |
|-------|-------------|---------|
| `requestId` | Request trace ID | `550e8400-...` |
| `userId` | Authenticated user | `john_doe` |
| `sessionId` | HTTP session | `sess-456` |
| `traceId` | Distributed trace | `abc123def456` |
| `service` | Service name | `order-service` |

### Custom Fields

```java
// Logstash encoder allows adding custom fields
logger.info("Order processed");

// Via MDC (included automatically)
MDC.put("orderId", "ORD-123");
MDC.put("orderTotal", "99.99");
```

## Log Levels in Structured Format

```json
{"level": "INFO", "message": "User logged in", "userId": "john_doe"}
{"level": "WARN", "message": "Retry attempt", "attempt": 2, "maxAttempts": 3}
{"level": "ERROR", "message": "Payment failed", "orderId": "ORD-123", "error": "Card declined"}
```

## Exception Handling

```java
// Logback/Log4j 2 automatically include exception details
try {
    processOrder(orderId);
} catch (Exception e) {
    logger.error("Order processing failed", e);
}

// Output includes:
// "stack_trace": "java.lang.RuntimeException: Order processing failed\n\tat..."
// "exception_class": "java.lang.RuntimeException"
// "exception_message": "Order processing failed"
```

## Search and Analysis

### Kibana/ELK Queries

```json
// Find all errors for a user
level:ERROR AND userId:"john_doe"

// Find slow requests
message:"Request completed" AND duration:>1000

// Find all events in a trace
requestId:"550e8400-e29b-41d4-a716-446655440000"
```

### Splunk Queries

```spl
index=app level=ERROR userId="john_doe"
| stats count by logger
| sort -count
```

## Best Practices

1. **Use consistent field names** across all services
2. **Include MDC context** in structured output
3. **Keep message field human-readable** (add details as separate fields)
4. **Use ISO 8601 timestamps** for cross-system compatibility
5. **Include exception details** as structured fields
6. **Avoid nested JSON in message** (use separate fields instead)
7. **Test with log analysis tools** before production
