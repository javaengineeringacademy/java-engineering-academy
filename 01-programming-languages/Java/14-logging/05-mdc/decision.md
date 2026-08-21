# Decision Framework: MDC Usage

## When to Use MDC

### Use MDC When:

- **Web applications** - Every request needs requestId, userId
- **Microservices** - Distributed tracing requires trace/span IDs
- **Batch processing** - Track batch IDs and item progress
- **Message queues** - Propagate message metadata through consumers
- **Multi-tenant systems** - Log tenant context automatically
- **Debugging production issues** - Correlate logs across components

### Avoid MDC When:

- **Global/static information** - Use logger configuration instead
- **Very large data** - MDC stores per-thread, can cause memory issues
- **Sensitive data** - PII, passwords (unless masked in pattern)
- **High-frequency updates** - MDC is per-thread, not for per-call data

## MDC Key Strategy

| Category | Keys | Example |
|----------|------|---------|
| Request tracing | requestId, traceId, spanId | UUIDs |
| User context | userId, sessionId, tenantId | Authenticated values |
| Application context | service, version, environment | Deployment info |
| Business context | orderId, batchId, correlationId | Domain identifiers |

## MDC vs Logger Parameters

| Aspect | MDC | Logger Parameters |
|--------|-----|-------------------|
| Scope | Thread-wide | Per log call |
| Persistence | Until cleared | Single message |
| Pattern access | `%X{key}` | Direct in message |
| Best for | Request context | Event-specific data |
| Overhead | ThreadLocal access | Method arguments |

## MDC vs Distributed Tracing

| Aspect | MDC | Distributed Tracing |
|--------|-----|-------------------|
| Scope | Single service | Across services |
| Tools | SLF4J MDC | Zipkin, Jaeger, OTel |
| Context | Thread-local | Network propagation |
| Overhead | Minimal | HTTP header overhead |
| Use case | Log correlation | Request flow visualization |

**Recommendation:** Use MDC for local log correlation, distributed tracing for cross-service visibility. They complement each other.

## Thread Pool Strategy

| Approach | Pros | Cons |
|----------|------|------|
| Copy/Restore MDC | Explicit, safe | More code |
| MDC-aware Executor | Automatic propagation | Requires custom executor |
| Decorate Runnable | Transparent wrapping | Classpath dependency |
| Context Propagation (Spring) | Framework-managed | Spring-specific |

## Spring Boot MDC Configuration

```yaml
# application.yml
logging:
  pattern:
    "[requestId]": "%X{requestId:-}"
    "[userId]": "%X{userId:-}"
    "[traceId]": "%X{traceId:-}"
```

```java
// application.yml alternative
spring:
  mdc:
    enabled: true
```
