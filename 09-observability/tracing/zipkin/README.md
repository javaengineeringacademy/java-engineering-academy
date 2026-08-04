# Zipkin Distributed Tracing

## Overview

Zipkin is a distributed tracing system for gathering timing data needed to troubleshoot latency problems in microservices architectures.

## Architecture

```
Instrumented Apps -> Collector -> Storage -> UI/API
```

### Components
- **Collector** - Receives trace data
- **Storage** - Persists trace data (Cassandra, Elasticsearch, MySQL)
- **API** - Query interface for traces
- **UI** - Web interface for visualization

## Configuration

### Spring Boot
```yaml
spring:
  zipkin:
    base-url: http://localhost:9411
  sleuth:
    sampler:
      probability: 1.0
```

### Brave Tracer
```java
 brave.http.HttpTracing tracing = brave.http.HttpTracing.newBuilder(tracer).build();

OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(new BraveOkHttp3Interceptor(tracing))
    .build();
```

## Collector Types

| Collector | Protocol | Use Case |
|-----------|----------|----------|
| HTTP | REST API | Standard deployment |
| Kafka | Message queue | High throughput |
| gRPC | Streaming | Low latency |

## Dependencies View

```json
{
  "services": [
    {"serviceName": "gateway", "callCount": 1000},
    {"serviceName": "order-service", "callCount": 800},
    {"serviceName": "payment-service", "callCount": 600}
  ],
  "dependencies": [
    {"parent": "gateway", "child": "order-service", "callCount": 800},
    {"parent": "order-service", "child": "payment-service", "callCount": 600}
  ]
}
```

## Best Practices

1. Instrument all HTTP clients and servers
2. Use consistent service naming
3. Propagate trace headers
4. Set appropriate sampling rates
5. Use storage with sufficient capacity
6. Monitor collector health
7. Use dependencies view for architecture analysis
8. Integrate with alerting systems
