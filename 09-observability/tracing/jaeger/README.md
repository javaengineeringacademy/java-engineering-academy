# Jaeger Distributed Tracing

## Overview

Jaeger is an open-source distributed tracing system for monitoring and troubleshooting microservices-based applications.

## Core Concepts

### Spans
A span represents a single unit of work in a trace.

```java
import io.opentracing.Tracer;

@Autowired
Tracer tracer;

public Order createOrder(OrderRequest request) {
    Span span = tracer.buildSpan("create-order")
        .withTag("order.type", request.getType())
        .start();
    
    try (Scope scope = tracer.activateSpan(span)) {
        Order order = processOrder(request);
        span.setTag("order.id", order.getId());
        return order;
    } catch (Exception e) {
        span.setTag("error", true);
        span.log(Map.of("error.message", e.getMessage()));
        throw e;
    } finally {
        span.finish();
    }
}
```

### Traces
A trace represents the end-to-end journey of a request through the system.

## Configuration

### Spring Boot Application
```yaml
jaeger:
  service: order-service
  sampler-type: const
  sampler-param: 1
  agent:
    host: localhost
    port: 6831
```

### OpenTelemetry SDK
```java
JaegerExporter jaegerExporter = JaegerExporter.builder()
    .setEndpoint("http://localhost:14268/api/traces")
    .setServiceName("order-service")
    .build();

SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
    .addSpanProcessor(BatchSpanProcessor.builder(jaegerExporter).build())
    .build();
```

## Sampling Strategies

| Strategy | Description |
|----------|-------------|
| Const | Always/never sample |
| Probabilistic | Sample percentage |
| Rate Limiting | Limit samples per second |
| Remote | Fetch from Jaeger backend |

## Best Practices

1. Use meaningful span names
2. Add relevant tags for filtering
3. Propagate trace context across services
4. Use appropriate sampling rates
5. Instrument critical code paths
6. Monitor trace storage usage
7. Use Jaeger UI for analysis
8. Integrate with Prometheus metrics
