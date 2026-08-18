# LightStep Monitoring & Observability

> **Package**: `academy.observability.lightstep`

## What is LightStep?

LightStep is a distributed tracing and observability platform that provides real-time insights into microservices architecture. It enables teams to track requests across services, identify performance bottlenecks, and debug complex distributed systems.

### Key Capabilities

- **Distributed Tracing**: End-to-end request tracking across services
- **Metrics Collection**: Performance metrics and SLAs
- **Service Maps**: Visual dependency graphs
- **Alerting**: Proactive issue detection
- **OpenTelemetry Support**: Vendor-neutral instrumentation

---

## LightStep Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        LightStep Platform                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │  Collector   │  │   Storage    │  │   Analysis   │          │
│  │  (OTLP)     │  │   Engine     │  │   Engine     │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│         │                  │                  │                 │
│         └──────────────────┼──────────────────┘                 │
│                            │                                    │
│  ┌──────────────────────────────────────────────────────┐      │
│  │              Dashboard & API                         │      │
│  └──────────────────────────────────────────────────────┘      │
└─────────────────────────────────────────────────────────────────┘
                               │
                               │ OTLP/gRPC
                               │
┌──────────────────────────────┴──────────────────────────────┐
│                    Application Services                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │  Service │  │  Service │  │  Service │  │  Service │   │
│  │    A     │──│    B     │──│    C     │──│    D     │   │
│  │ (Agent)  │  │ (Agent)  │  │ (Agent)  │  │ (Agent)  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Component Overview

| Component | Purpose |
|-----------|---------|
| **Collector** | Receives telemetry via OTLP protocol |
| **Storage Engine** | Stores traces and metrics |
| **Analysis Engine** | Processes and analyzes data |
| **Dashboard** | Visualization and query interface |
| **SDK/Agent** | Application instrumentation |

---

## OpenTelemetry Integration

LightStep is built on OpenTelemetry standards:

```yaml
# otel-config.yaml
exporters:
  otlp:
    endpoint: ingest.lightstep.com:443
    headers:
      lightstep-access-token: ${LIGHTSTEP_ACCESS_TOKEN}

processors:
  batch:
    timeout: 1s
    send_batch_size: 512

service:
  pipelines:
    traces:
      receivers: [otlp]
      processors: [batch]
      exporters: [otlp]
    metrics:
      receivers: [otlp]
      processors: [batch]
      exporters: [otlp]
```

---

## Tracing with LightStep

### Span Structure

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
    "http.url": "/api/users"
  }
}
```

### Trace Visualization

```
Trace: abc123def456
├── API Gateway (450ms)
│   ├── Auth Service (50ms)
│   └── User Service (380ms)
│       ├── Database Query (120ms)
│       └── Cache Lookup (10ms)
└── Response Assembly (20ms)
```

---

## Metrics with LightStep

### Available Metrics

- **Latency**: Request duration percentiles
- **Throughput**: Requests per second
- **Error Rate**: Failed requests percentage
- **Saturation**: Resource utilization

### Custom Metrics

```java
Meter meter = globalTracer.getMeter("my-service");
LongCounter requestCounter = meter.counterBuilder("http.requests")
    .setDescription("Total HTTP requests")
    .build();

requestCounter.add(1, Attributes.of(
    AttributeKey.stringKey("method"), "GET",
    AttributeKey.stringKey("path"), "/api/users"
));
```

---

## Service Mesh Observability

LightStep integrates with service meshes:

| Mesh | Integration |
|------|-------------|
| Istio | Native support via Envoy |
| Linkerd | OpenTelemetry adapter |
| Consul Connect | Custom exporter |

---

## LightStep vs Other Tools

| Feature | LightStep | Jaeger | Zipkin | Datadog |
|---------|-----------|--------|--------|---------|
| Managed Service | ✅ | ❌ | ❌ | ✅ |
| OpenTelemetry | ✅ Native | ✅ | ⚠️ Partial | ✅ |
| Real-time Analysis | ✅ | ❌ | ❌ | ✅ |
| Pricing | Enterprise | Free | Free | Per-host |
| SLA Monitoring | ✅ | ❌ | ❌ | ✅ |
| Service Maps | ✅ | ❌ | ❌ | ✅ |

---

## Java SDK Integration

### Maven Dependency

```xml
<dependency>
    <groupId>com.lightstep.opentelemetry</groupId>
    <artifactId>opentelemetry-exporter-otlp-trace</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Configuration

```java
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

OtlpGrpcSpanExporter exporter = OtlpGrpcSpanExporter.builder()
    .setEndpoint("ingest.lightstep.com:443")
    .addHeader("lightstep-access-token", accessToken)
    .build();

SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
    .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
    .setResource(Resource.getDefault().merge(
        Resource.create(Attributes.of(
            ResourceAttributes.SERVICE_NAME, "my-service"
        ))
    ))
    .build();
```

---

## Configuration and Setup

### Environment Variables

```bash
export LIGHTSTEP_ACCESS_TOKEN="your-access-token"
export LIGHTSTEP_SERVICE_NAME="my-service"
export LIGHTSTEP_COLLECTOR_HOST="ingest.lightstep.com"
export LIGHTSTEP_COLLECTOR_PORT=443
```

### Configuration File

```json
{
  "service_name": "my-service",
  "access_token": "${LIGHTSTEP_ACCESS_TOKEN}",
  "collector": {
    "host": "ingest.lightstep.com",
    "port": 443,
    "protocol": "grpc"
  },
  "reporting": {
    "period_ms": 10000,
    "max_buffered_spans": 1000
  }
}
```

---

## Sampling Strategies

### Probability Sampling

```java
ProbabilitySampler sampler = ProbabilitySampler.create(0.1); // 10% sampling
```

### Rate Limiting

```java
RateLimitingSampler sampler = RateLimitingSampler.create(100); // 100 traces/sec
```

### Parent-based Sampling

```java
ParentBasedSampler sampler = ParentBasedSampler.builder()
    .setRoot(TraceIdRatioBasedSampler.create(0.1))
    .setRemoteParent(TraceIdRatioBasedSampler.create(0.2))
    .setLocalParent(AlwaysOnSampler.getInstance())
    .build();
```

---

## Context Propagation

### W3C TraceContext

```java
TextMapPropagator propagator = W3CTraceContextPropagator.getInstance();
Context context = propagator.extract(Context.current(), carrier, getter);
```

### B3 Propagation (Zipkin)

```java
TextMapPropagator b3Propagator = B3Propagator.injectingMultiHeaders();
```

### LightStep Propagation

```java
TextMapPropagator lightstepPropagator = LightStepPropagator.getInstance();
```

---

## Alerting and Dashboards

### Alert Conditions

- Error rate exceeds threshold
- Latency percentile violations
- Service dependency failures
- SLA breaches

### Dashboard Metrics

- Request rate over time
- Response time distribution
- Error rate trends
- Service dependency map

---

## Distributed Tracing Concepts

### Trace

A complete journey of a request through the system

### Span

A single operation within a trace

### Span Context

Information propagated across service boundaries

### baggage

Key-value pairs passed through the trace

---

## Performance Monitoring

### Key Indicators

- **P50/P95/P99 Latency**: Response time percentiles
- **Apdex Score**: User satisfaction metric
- **Throughput**: Requests per second
- **Error Budget**: Allowed failure rate

---

## Quick Start

```bash
# Install LightStep Java agent
curl -L https://github.com/lightstep/opentelemetry-java/releases/latest/download/lightstep-opentelemetry-java-agent.jar -o lightstep-agent.jar

# Run application with agent
java -javaagent:lightstep-agent.jar \
     -Dlightstep.access.token=YOUR_TOKEN \
     -jar my-application.jar
```

---

## Next Steps

1. [Overview](01-overview/) - Deep dive into LightStep concepts
2. [Setup & Configuration](02-setup-configuration/) - Installation guide
3. [Tracing](03-tracing/) - Distributed tracing implementation
4. [Metrics](04-metrics/) - Metrics collection
5. [Sampling](05-sampling/) - Sampling strategies
6. [Context Propagation](06-context-propagation/) - Cross-service context
7. [Java SDK](07-java-sdk/) - Java implementation
8. [Alerting & Dashboards](08-alerting-dashboards/) - Monitoring setup
