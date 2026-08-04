# OpenTelemetry

## Overview

OpenTelemetry is a vendor-neutral open-source framework for collecting, processing, and exporting telemetry data (traces, metrics, logs).

## Core Components

### SDK Components
- **Tracer Provider** - Creates and configures tracers
- **Meter Provider** - Creates and configures meters
- **Logger Provider** - Creates and configures loggers
- **Span Processor** - Processes spans before export
- **Exporter** - Sends telemetry to backends

## Configuration

### Java Agent
```bash
java -javaagent:opentelemetry-javaagent.jar \
     -Dotel.service.name=order-service \
     -Dotel.exporter.otlp.endpoint=http://localhost:4317 \
     -Dotel.traces.exporter=otlp \
     -Dotel.metrics.exporter=otlp \
     -jar application.jar
```

### SDK Manual Setup
```java
SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
    .addSpanProcessor(BatchSpanProcessor.builder(otlpExporter).build())
    .setResource(Resource.getDefault().merge(
        Resource.builder()
            .put("service.name", "order-service")
            .put("service.version", "1.0.0")
            .build()))
    .build();

SdkMeterProvider meterProvider = SdkMeterProvider.builder()
    .registerMetricReader(PeriodicMetricReader.builder(otlpExporter).build())
    .build();
```

## Auto-Instrumentation

### Supported Libraries
| Library | Version |
|---------|---------|
| Spring Web MVC | 5.x, 6.x |
| Spring WebFlux | 5.x, 6.x |
| gRPC | 1.x |
| JDBC | All |
| Redis (Lettuce) | All |
| Kafka | All |
| RabbitMQ | All |

### Custom Instrumentation
```java
Tracer tracer = GlobalOpenTelemetry.getTracer("order-service");

Span span = tracer.spanBuilder("process-order")
    .setAttribute("order.id", orderId)
    .startSpan();

try (Scope scope = span.makeCurrent()) {
    // Business logic
    span.setStatus(StatusCode.OK);
} catch (Exception e) {
    span.setStatus(StatusCode.ERROR, e.getMessage());
    span.recordException(e);
} finally {
    span.end();
}
```

## Context Propagation

### W3C Trace Context
```
traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01
```

### B3 Propagation
```
X-B3-TraceId: 4bf92f3577b34da6a3ce929d0e0e4736
X-B3-SpanId: 00f067aa0ba902b7
```

## Best Practices

1. Use the Java agent for auto-instrumentation
2. Configure appropriate sampling strategies
3. Use OTLP exporter for vendor-neutral export
4. Include service metadata in resources
5. Use baggage for cross-cutting context
6. Monitor SDK performance overhead
7. Use exporters for multiple backends
8. Implement health checks for SDK components
