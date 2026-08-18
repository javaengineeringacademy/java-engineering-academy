# Context Propagation

## Overview

Context propagation ensures trace context is correctly passed between services, maintaining the trace's integrity across distributed systems.

---

## Propagation Formats

### W3C TraceContext (Recommended)

```java
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;

// Inject context into outgoing request
TextMapPropagator propagator = W3CTraceContextPropagator.getInstance();
Context context = Context.current().with(span);

Map<String, String> carrier = new HashMap<>();
propagator.inject(context, carrier, Map::put);

// Headers:
// traceparent: 00-abc123def456-span789-01
// tracestate: vendor1=value1,vendor2=value2
```

### B3 Propagation (Zipkin)

```java
import io.opentelemetry.api.trace.propagation.B3Propagator;

// Single header format
TextMapPropagator b3Single = B3Propagator.injectingSingleHeader();
// Header: b3: abc123def456-span789-01

// Multi-header format
TextMapPropagator b3Multi = B3Propagator.injectingMultiHeaders();
// Headers:
// X-B3-TraceId: abc123def456
// X-B3-SpanId: span789
// X-B3-Sampled: 1
```

### LightStep Propagation

```java
import io.opentelemetry.api.trace.propagation.LightStepPropagator;

TextMapPropagator lightstepPropagator = LightStepPropagator.getInstance();
// Headers:
// ot-tracer-traceid: abc123def456
// ot-tracer-spanid: span789
// ot-tracer-sampled: true
```

---

## Injection (Outgoing Requests)

### HTTP Headers

```java
public void makeHttpRequest(String url) {
    Span span = tracer.spanBuilder("HTTP GET").startSpan();
    
    try (Scope scope = span.makeCurrent()) {
        Map<String, String> headers = new HashMap<>();
        
        // Inject context into headers
        W3CTraceContextPropagator.getInstance()
            .inject(Context.current(), headers, Map::put);
        
        // Make HTTP request with headers
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("traceparent", headers.get("traceparent"))
            .header("tracestate", headers.get("tracestate"))
            .GET()
            .build();
        
        httpClient.send(request, BodyHandlers.ofString());
    } finally {
        span.end();
    }
}
```

### Kafka Messages

```java
public void publishMessage(String topic, String message) {
    Span span = tracer.spanBuilder("KAFKA PUBLISH").startSpan();
    
    try (Scope scope = span.makeCurrent()) {
        Map<String, byte[]> headers = new HashMap<>();
        
        // Inject context into message headers
        W3CTraceContextPropagator.getInstance()
            .inject(Context.current(), headers, 
                (carrier, key, value) -> 
                    carrier.put(key, value.getBytes()));
        
        ProducerRecord<String, String> record = 
            new ProducerRecord<>(topic, message);
        
        // Add trace headers
        headers.forEach((key, value) -> 
            record.headers().add(key, value));
        
        producer.send(record);
    } finally {
        span.end();
    }
}
```

---

## Extraction (Incoming Requests)

### HTTP Headers

```java
public Response handleHttpRequest(HttpRequest request) {
    // Extract context from headers
    Context extractedContext = W3CTraceContextPropagator.getInstance()
        .extract(Context.current(), 
            request.headers(), 
            (carrier, key) -> carrier.get(key));
    
    // Create span with parent context
    Span span = tracer.spanBuilder("HTTP GET /api/users")
        .setParent(extractedContext)
        .startSpan();
    
    try (Scope scope = span.makeCurrent()) {
        // Process request
        return processRequest(request);
    } finally {
        span.end();
    }
}
```

### Kafka Messages

```java
public void consumeMessage(ConsumerRecord<String, String> record) {
    // Extract context from message headers
    Map<String, String> headers = new HashMap<>();
    record.headers().forEach(header -> 
        headers.put(header.key(), new String(header.value())));
    
    Context extractedContext = W3CTraceContextPropagator.getInstance()
        .extract(Context.current(), headers, Map::get);
    
    // Create span with parent context
    Span span = tracer.spanBuilder("KAFKA CONSUME")
        .setParent(extractedContext)
        .startSpan();
    
    try (Scope scope = span.makeCurrent()) {
        // Process message
        processMessage(record.value());
    } finally {
        span.end();
    }
}
```

---

## Multi-format Support

### Configure Multiple Propagators

```java
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;

ContextPropagators propagators = ContextPropagators.create(
    TextMapPropagator.composite(
        W3CTraceContextPropagator.getInstance(),
        B3Propagator.injectingMultiHeaders(),
        LightStepPropagator.getInstance()
    )
);

// Configure with SDK
SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
    .setPropagators(propagators)
    .build();
```

---

## Baggage Propagation

### Setting Baggage

```java
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;

// Add baggage
Baggage baggage = Baggage.empty()
    .toBuilder()
    .put("user.id", "12345")
    .put("user.tier", "premium")
    .put("request.id", UUID.randomUUID().toString())
    .build();

Context context = Context.current().with(baggage);
```

### Extracting Baggage

```java
// Extract baggage from context
Baggage baggage = Baggage.fromContext(context);

// Get specific baggage value
String userId = baggage.getEntryValue("user.id");
String userTier = baggage.getEntryValue("user.tier");
```

### Baggage Best Practices

```java
// Good - useful for cross-service context
Baggage baggage = Baggage.empty()
    .toBuilder()
    .put("user.id", userId)  // Low cardinality, useful
    .put("user.tier", tier)  // Low cardinality, useful
    .put("tenant.id", tenantId)  // Low cardinality, useful
    .build();

// Bad - high cardinality or sensitive data
Baggage badBaggage = Baggage.empty()
    .toBuilder()
    .put("request.id", UUID.randomUUID().toString())  // High cardinality
    .put("user.email", email)  // Sensitive data
    .put("session.id", sessionId)  // High cardinality
    .build();
```

---

## Async Propagation

### CompletableFuture

```java
public CompletableFuture<Result> processAsync(Request request) {
    Span span = tracer.spanBuilder("process-async").startSpan();
    
    // Capture context for async processing
    Context context = Context.current().with(span);
    
    return CompletableFuture.supplyAsync(() -> {
        // Restore context in async thread
        try (Scope scope = context.makeCurrent()) {
            return doProcessing(request);
        } finally {
            span.end();
        }
    });
}
```

### Thread Pools

```java
// Wrap executor to propagate context
ExecutorService executor = ContextPropagatingExecutorService.wrap(
    Executors.newFixedThreadPool(10)
);

// Submit task with context propagation
executor.submit(() -> {
    // Context is automatically propagated
    processRequest(request);
});
```

---

## Spring Integration

### RestTemplate

```java
@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .additionalInterceptors(new TracingInterceptor())
            .build();
    }
}

@Component
public class TracingInterceptor implements ClientHttpRequestInterceptor {
    
    private final TextMapPropagator propagator = 
        W3CTraceContextPropagator.getInstance();
    
    @Override
    public ClientHttpResponse intercept(
        HttpRequest request, byte[] body,
        ClientHttpRequestExecution execution) throws IOException {
        
        Map<String, String> headers = new HashMap<>();
        propagator.inject(Context.current(), headers, Map::put);
        
        headers.forEach(request.getHeaders()::add);
        
        return execution.execute(request, body);
    }
}
```

### WebClient

```java
@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .filter(new TracingFilter())
            .build();
    }
}

@Component
public class TracingFilter implements ExchangeFilterFunction {
    
    private final TextMapPropagator propagator = 
        W3CTraceContextPropagator.getInstance();
    
    @Override
    public Mono<ClientResponse> filter(
        ClientRequest request, ExchangeFunction next) {
        
        Map<String, String> headers = new HashMap<>();
        propagator.inject(Context.current(), headers, Map::put);
        
        ClientRequest filteredRequest = ClientRequest.from(request)
            .headers(httpHeaders -> 
                headers.forEach(httpHeaders::add))
            .build();
        
        return next.exchange(filteredRequest);
    }
}
```

---

## Best Practices

### Propagation Format

- Use W3C TraceContext as default
- Support B3 for Zipkin compatibility
- Use LightStep format for LightStep services

### Baggage

- Keep baggage small (< 1KB)
- Use low-cardinality values
- Avoid sensitive data
- Set expiration policies

### Async Processing

- Capture context before async operations
- Restore context in async threads
- Use context-propagating executors

### Testing

- Test propagation across services
- Verify context is correctly extracted
- Test with multiple propagation formats

---

## Troubleshooting

### Missing Spans

- Check propagation format configuration
- Verify headers are correctly passed
- Check for context restoration in async code

### Broken Traces

- Ensure all services use same propagation format
- Verify context is correctly extracted
- Check for context loss in middleware

### Performance Issues

- Reduce baggage size
- Use efficient propagation formats
- Batch context operations

---

## Next Steps

- [Java SDK](../07-java-sdk/) - Java implementation
- [Alerting & Dashboards](../08-alerting-dashboards/) - Monitoring setup
