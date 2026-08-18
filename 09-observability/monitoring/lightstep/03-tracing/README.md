# Distributed Tracing with LightStep

## Overview

Distributed tracing tracks the flow of requests across multiple services, providing end-to-end visibility into your application's behavior.

---

## Trace Structure

### Trace Components

```
Trace ID: abc123def456
├── Span 1: API Gateway (450ms)
│   ├── Span 2: Auth Service (50ms)
│   └── Span 3: User Service (380ms)
│       ├── Span 4: Database Query (120ms)
│       └── Span 5: Cache Lookup (10ms)
└── Span 6: Response Assembly (20ms)
```

### Span Data Model

```json
{
  "traceId": "abc123def456",
  "spanId": "span789",
  "parentSpanId": "parent123",
  "operationName": "HTTP GET /api/users",
  "serviceName": "user-service",
  "startTime": "2024-01-15T10:30:00Z",
  "duration": 125.5,
  "tags": {
    "http.method": "GET",
    "http.status_code": 200,
    "http.url": "/api/users",
    "component": "web"
  },
  "logs": [
    {
      "timestamp": "2024-01-15T10:30:00.1Z",
      "fields": {
        "message": "Database query executed",
        "db.statement": "SELECT * FROM users WHERE id = ?"
      }
    }
  ],
  "status": {
    "code": "OK"
  }
}
```

---

## Creating Spans

### Manual Instrumentation

```java
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;

Tracer tracer = GlobalOpenTelemetry.getTracer("my-service");

// Create a span
Span span = tracer.spanBuilder("HTTP GET /api/users")
    .setAttribute("http.method", "GET")
    .setAttribute("http.url", "/api/users")
    .startSpan();

try {
    // Your business logic here
    List<User> users = userService.getAllUsers();
    
    span.setAttribute("user.count", users.size());
    span.setStatus(StatusCode.OK);
    
    return users;
} catch (Exception e) {
    span.setStatus(StatusCode.ERROR, e.getMessage());
    span.recordException(e);
    throw e;
} finally {
    span.end();
}
```

### Auto-instrumentation with Agent

```bash
# Just add the agent - no code changes needed
java -javaagent:lightstep-agent.jar \
     -Dlightstep.access.token=YOUR_TOKEN \
     -jar my-application.jar
```

---

## Span Attributes

### Standard Attributes

```java
// HTTP attributes
span.setAttribute("http.method", "GET");
span.setAttribute("http.url", "/api/users");
span.setAttribute("http.status_code", 200);
span.setAttribute("http.request_content_length", 1024);
span.setAttribute("http.response_content_length", 2048);

// Database attributes
span.setAttribute("db.system", "postgresql");
span.setAttribute("db.statement", "SELECT * FROM users");
span.setAttribute("db.user", "admin");

// Messaging attributes
span.setAttribute("messaging.system", "kafka");
span.setAttribute("messaging.destination", "user-events");
span.setAttribute("messaging.operation", "publish");

// Custom attributes
span.setAttribute("myapp.user.id", userId);
span.setAttribute("myapp.request.id", requestId);
```

### Semantic Conventions

```java
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

span.setAttribute(SemanticAttributes.HTTP_METHOD, "GET");
span.setAttribute(SemanticAttributes.HTTP_URL, "/api/users");
span.setAttribute(SemanticAttributes.HTTP_STATUS_CODE, 200);
span.setAttribute(SemanticAttributes.DB_SYSTEM, "postgresql");
span.setAttribute(SemanticAttributes.DB_STATEMENT, "SELECT * FROM users");
```

---

## Span Events

### Adding Events

```java
Span span = tracer.spanBuilder("process-order").startSpan();

// Add event with timestamp
span.addEvent("order.received", 
    Instant.now(),
    Attributes.of(
        AttributeKey.stringKey("order.id"), orderId,
        AttributeKey.doubleKey("order.total"), 99.99
    ));

// Add event with current time
span.addEvent("payment.processed",
    Attributes.of(
        AttributeKey.stringKey("payment.method"), "credit_card",
        AttributeKey.stringKey("payment.status"), "success"
    ));

span.end();
```

### Exception Events

```java
try {
    // Your code
} catch (Exception e) {
    span.addEvent("exception",
        Attributes.of(
            AttributeKey.stringKey("exception.type"), e.getClass().getName(),
            AttributeKey.stringKey("exception.message"), e.getMessage()
        ));
    span.recordException(e);
    span.setStatus(StatusCode.ERROR, e.getMessage());
}
```

---

## Context Propagation

### W3C TraceContext

```java
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;

// Inject context into outgoing request
TextMapPropagator propagator = W3CTraceContextPropagator.getInstance();
Context context = Context.current().with(span);
propagator.inject(context, carrier, setter);

// Extract context from incoming request
Context extractedContext = propagator.extract(Context.current(), carrier, getter);
Span span = tracer.spanBuilder("operation")
    .setParent(extractedContext)
    .startSpan();
```

### B3 Propagation (Zipkin)

```java
import io.opentelemetry.api.trace.propagation.B3Propagator;

TextMapPropagator b3Propagator = B3Propagator.injectingSingleHeader();
// or
TextMapPropagator b3Propagator = B3Propagator.injectingMultiHeaders();
```

---

## Trace Analysis

### Querying Traces

```sql
-- LightStep Query Language
service.name = "user-service" AND duration > 100ms

-- Find errors
status.code = "ERROR"

-- Find slow traces
duration > 500ms AND service.name = "api-gateway"

-- Find traces with specific attribute
http.status_code = 500
```

### Trace Visualization

```
Trace: abc123def456
Duration: 450ms
Status: OK

├── API Gateway (450ms) ✓
│   ├── Auth Service (50ms) ✓
│   │   └── Token Validation (30ms) ✓
│   └── User Service (380ms) ✓
│       ├── Cache Lookup (10ms) ✓
│       ├── Database Query (120ms) ⚠️ SLOW
│       └── Response Assembly (50ms) ✓
└── Response Encoding (20ms) ✓
```

---

## Best Practices

### Span Naming

```java
// Good - consistent, low cardinality
spanBuilder("HTTP GET /api/users/{id}")
spanBuilder("DB SELECT users")
spanBuilder("KAFKA PUBLISH user-events")

// Bad - high cardinality
spanBuilder("GET /api/users/12345")
spanBuilder("SELECT * FROM users WHERE id = 12345")
```

### Attributes

```java
// Good - useful for filtering
span.setAttribute("http.method", "GET");
span.setAttribute("http.status_code", 200);
span.setAttribute("user.id", userId);

// Bad - high cardinality
span.setAttribute("http.url", "/api/users/" + userId);
span.setAttribute("request.id", UUID.randomUUID().toString());
```

### Error Handling

```java
try {
    // Your code
} catch (BusinessException e) {
    span.setStatus(StatusCode.ERROR, "Business logic failed");
    span.recordException(e);
    throw e;
} catch (Exception e) {
    span.setStatus(StatusCode.ERROR, "Unexpected error");
    span.recordException(e);
    throw e;
} finally {
    span.end();
}
```

---

## Performance Considerations

### Batch Processing

```java
BatchSpanProcessor processor = BatchSpanProcessor.builder(exporter)
    .setScheduleDelay(1000)  // Export every 1 second
    .setMaxQueueSize(1024)   // Max spans in queue
    .setMaxExportBatchSize(512)  // Spans per batch
    .build();
```

### Sampling

```java
// Sample 10% of traces
TraceIdRatioBasedSampler sampler = TraceIdRatioBasedSampler.create(0.1);

// Use parent-based sampling for consistency
ParentBasedSampler parentBasedSampler = ParentBasedSampler.builder()
    .setRoot(TraceIdRatioBasedSampler.create(0.1))
    .build();
```

---

## Common Patterns

### Request-Reply Pattern

```java
public Response processRequest(Request request) {
    Span span = tracer.spanBuilder("process-request")
        .setAttribute("request.type", request.getType())
        .startSpan();
    
    try (Scope scope = span.makeCurrent()) {
        Response response = doProcessing(request);
        span.setAttribute("response.status", "success");
        return response;
    } catch (Exception e) {
        span.setAttribute("response.status", "error");
        span.recordException(e);
        throw e;
    } finally {
        span.end();
    }
}
```

### Async Processing

```java
public CompletableFuture<Result> processAsync(Request request) {
    Span span = tracer.spanBuilder("process-async")
        .startSpan();
    
    return CompletableFuture.supplyAsync(() -> {
        try (Scope scope = span.makeCurrent()) {
            Result result = doProcessing(request);
            span.setStatus(StatusCode.OK);
            return result;
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR, e.getMessage());
            span.recordException(e);
            throw new CompletionException(e);
        } finally {
            span.end();
        }
    });
}
```

---

## Next Steps

- [Metrics](../04-metrics/) - Metrics collection
- [Sampling](../05-sampling/) - Sampling strategies
- [Context Propagation](../06-context-propagation/) - Cross-service context
