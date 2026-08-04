# Structured Logging

## Overview

Structured logging outputs log events in a machine-readable format (typically JSON), enabling efficient parsing, searching, and analysis by log management systems.

## Unstructured vs Structured

### Unstructured (Traditional)
```
2024-01-15 10:30:00 ERROR [http-nio-8080-exec-1] c.e.UserService - Failed to find user 12345: Connection timeout
```

### Structured (JSON)
```json
{
  "timestamp": "2024-01-15T10:30:00.000Z",
  "level": "ERROR",
  "thread": "http-nio-8080-exec-1",
  "logger": "com.example.UserService",
  "message": "Failed to find user",
  "userId": 12345,
  "error": "Connection timeout",
  "traceId": "abc-def-123",
  "service": "user-service"
}
```

## Benefits

| Benefit | Description |
|---------|-------------|
| Queryability | Filter by any field |
| Aggregation | Group by service, level, traceId |
| Correlation | Link logs across services |
| Analytics | Statistical analysis of log data |
| Alerting | Field-based alert rules |

## Implementation

### Logstash Encoder (Logback)
```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

```xml
<encoder class="net.logstash.logback.encoder.LogstashEncoder">
    <includeMdcKeyName>requestId</includeMdcKeyName>
    <includeMdcKeyName>userId</includeMdcKeyName>
    <fieldNames>
        <timestamp>[ignore]</timestamp>
        <message>message</message>
        <logger>logger</logger>
        <thread>thread</thread>
        <level>level</level>
    </fieldNames>
    <customFields>{"service":"user-service","version":"1.0.0"}</customFields>
</encoder>
```

### JSON Layout (Log4j2)
```xml
<JsonLayout compact="true" eventEol="true">
    <KeyValuePair key="service" value="user-service"/>
    <KeyValuePair key="environment" value="production"/>
</JsonLayout>
```

### Logbook (HTTP Logging)
```java
@Bean
public Logbook logbook() {
    return Logbook.builder()
        .sink(new Slf4jSink())
        .build();
}
```

## Custom Fields

```java
// MDC for thread-local context
MDC.put("userId", user.getId());
MDC.put("requestId", requestId);
MDC.put("service", "user-service");

// Structured argument (Key-Value)
logger.info("Order created orderId={} amount={} currency={}", 
            orderId, amount, currency);

// Logstash structured argument
logger.info("{}", kv("orderId", orderId), kv("amount", amount));
```

## Log Schema Standards

### Common Fields
```json
{
  "@timestamp": "ISO8601",
  "level": "INFO|WARN|ERROR|DEBUG|TRACE",
  "service": "service-name",
  "traceId": "distributed-trace-id",
  "spanId": "span-id",
  "userId": "user-identifier",
  "message": "human-readable message",
  "error": {
    "type": "exception-class",
    "message": "error message",
    "stacktrace": "full stack trace"
  }
}
```

### ECS (Elastic Common Schema)
```json
{
  "@timestamp": "2024-01-15T10:30:00.000Z",
  "log.level": "ERROR",
  "service.name": "user-service",
  "service.version": "1.0.0",
  "trace.id": "abc-def-123",
  "error.message": "Connection timeout",
  "error.type": "java.sql.SQLException"
}
```

## Best Practices

1. Use JSON format for all production logs
2. Include trace IDs for distributed correlation
3. Use consistent field naming conventions
4. Avoid logging sensitive PII data
5. Include service name and version
6. Use structured arguments instead of string concatenation
7. Configure proper log levels per field
8. Implement log validation before output
