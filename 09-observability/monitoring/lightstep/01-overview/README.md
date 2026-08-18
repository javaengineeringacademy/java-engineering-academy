# LightStep Overview

## What is LightStep?

LightStep is a distributed tracing and observability platform that provides real-time insights into microservices architecture. It helps teams track requests across services, identify performance bottlenecks, and debug complex distributed systems.

---

## Core Concepts

### Distributed Tracing

Distributed tracing tracks the flow of requests across multiple services:

```
User Request → API Gateway → Auth Service → User Service → Database
     ↓              ↓              ↓              ↓           ↓
   Trace         Span 1         Span 2         Span 3     Span 4
```

### Key Terminology

| Term | Definition |
|------|------------|
| **Trace** | Complete journey of a request through the system |
| **Span** | Single operation within a trace |
| **Span Context** | Information propagated across service boundaries |
| **Baggage** | Key-value pairs passed through the trace |
| **Collector** | Receives and processes telemetry data |
| **Sampling** | Strategy for selecting which traces to collect |

---

## LightStep Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        LightStep Platform                       │
├─────────────────────────────────────────────────────────────────┤
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

---

## OpenTelemetry Integration

LightStep is built on OpenTelemetry standards:

### Benefits of OpenTelemetry

1. **Vendor-Neutral**: Switch backends without changing code
2. **Standard APIs**: Consistent instrumentation across languages
3. **Community-Driven**: Active open-source community
4. **Comprehensive**: Traces, metrics, and logs in one SDK

### OTLP Protocol

```
Application → OTLP Exporter → LightStep Collector → Storage → Analysis
```

---

## Use Cases

### 1. Microservices Debugging

```
Problem: User reports slow API response
Solution: Trace shows bottleneck in Service C database query

Trace Visualization:
├── API Gateway (50ms)
│   ├── Auth Service (10ms)
│   └── User Service (40ms)
│       ├── Cache Lookup (2ms) ✓
│       └── Database Query (35ms) ⚠️ SLOW
└── Response Assembly (5ms)
```

### 2. Performance Optimization

```
Before Optimization:
- P99 Latency: 2000ms
- Error Rate: 5%

After Optimization (with LightStep insights):
- P99 Latency: 500ms
- Error Rate: 1%
```

### 3. SLA Monitoring

```
SLA Requirements:
- Availability: 99.9%
- P99 Latency: < 1000ms

LightStep Monitoring:
- Real-time SLA tracking
- Alert on SLA breaches
- Error budget tracking
```

---

## Key Features

### Real-time Analysis
- Live trace streaming
- Instant query results
- Real-time service maps

### Advanced Sampling
- Probability-based sampling
- Rate limiting
- Parent-based consistency
- Adaptive sampling

### Service Mesh Integration
- Istio native support
- Linkerd integration
- Envoy metrics

### Alerting & Dashboards
- Custom alert rules
- SLA monitoring
- Performance dashboards
- Error budget tracking

---

## Comparison with Other Tools

### LightStep vs Jaeger

| Aspect | LightStep | Jaeger |
|--------|-----------|--------|
| Deployment | Managed SaaS | Self-hosted |
| Setup | Simple | Complex |
| Analysis | Advanced | Basic |
| Support | Enterprise | Community |
| Cost | Paid | Free |

### LightStep vs Zipkin

| Aspect | LightStep | Zipkin |
|--------|-----------|--------|
| Architecture | Cloud-native | Traditional |
| OpenTelemetry | Native | Adapter |
| Real-time | Full | Limited |
| Scalability | High | Medium |

### LightStep vs Datadog

| Aspect | LightStep | Datadog |
|--------|-----------|---------|
| Focus | Tracing-first | Full-stack |
| OpenTelemetry | Native | Supported |
| Pricing | Per-host | Per-host |
| Features | Specialized | Broad |

---

## Getting Started

### Prerequisites

- LightStep account
- Application with OpenTelemetry SDK
- Network connectivity to LightStep collector

### Basic Setup

1. **Install SDK**
```bash
# Maven
<dependency>
    <groupId>io.opentelemetry</groupId>
    <artifactId>opentelemetry-api</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. **Configure Exporter**
```java
OtlpGrpcSpanExporter exporter = OtlpGrpcSpanExporter.builder()
    .setEndpoint("ingest.lightstep.com:443")
    .addHeader("lightstep-access-token", accessToken)
    .build();
```

3. **Instrument Code**
```java
Tracer tracer = globalTracer.get();
Span span = tracer.spanBuilder("my-operation").startSpan();
// ... your code ...
span.end();
```

---

## Next Steps

- [Setup & Configuration](../02-setup-configuration/) - Installation guide
- [Tracing](../03-tracing/) - Distributed tracing implementation
- [Metrics](../04-metrics/) - Metrics collection
