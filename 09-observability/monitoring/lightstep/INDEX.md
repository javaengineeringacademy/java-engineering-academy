# LightStep Monitoring Index

> **Package**: `academy.observability.lightstep`

## Quick Navigation

### Core Documentation
- [README.md](README.md) - Comprehensive guide
- [decision.md](decision.md) - When to use LightStep
- [references.md](references.md) - Official docs and resources
- [quiz.md](quiz.md) - Test your knowledge

### Learning Modules

#### 1. [Overview](01-overview/README.md)
- What is LightStep
- Core concepts
- Architecture
- OpenTelemetry integration

#### 2. [Setup & Configuration](02-setup-configuration/README.md)
- Installation methods
- Environment variables
- Configuration files
- Docker & Kubernetes

#### 3. [Tracing](03-tracing/README.md)
- Trace structure
- Creating spans
- Span attributes
- Context propagation

#### 4. [Metrics](04-metrics/README.md)
- Metric types
- Standard metrics
- Custom metrics
- Metric aggregation

#### 5. [Sampling](05-sampling/README.md)
- Probability sampling
- Rate limiting
- Parent-based sampling
- Adaptive sampling

#### 6. [Context Propagation](06-context-propagation/README.md)
- W3C TraceContext
- B3 propagation
- Baggage propagation
- Async propagation

#### 7. [Java SDK](07-java-sdk/README.md)
- Dependencies
- SDK initialization
- Tracer usage
- Spring Boot integration

#### 8. [Alerting & Dashboards](08-alerting-dashboards/README.md)
- Alert conditions
- Dashboard creation
- Alert actions
- Best practices

### Practice & Examples

#### [Examples](examples/README.md)
- Basic Spring Boot application
- Microservices architecture
- Kafka integration
- Database integration
- Async processing

#### [Practices](practices/README.md)
- Basic tracing exercise
- Context propagation exercise
- Metrics collection exercise
- Sampling strategies exercise
- Alerting configuration exercise
- Dashboard creation exercise

#### [Solutions](solutions/README.md)
- Complete solutions for all practices
- Challenge project solutions
- Verification steps

### Additional Resources
- [SUMMARY.md](SUMMARY.md) - Overview and summary
- [INDEX.md](INDEX.md) - This index file

## Learning Path

```
Start Here
    │
    ▼
[README.md] ────► [01-overview]
    │                  │
    │                  ▼
    │            [02-setup-configuration]
    │                  │
    │                  ▼
    │            [03-tracing]
    │                  │
    │                  ▼
    │            [04-metrics]
    │                  │
    │                  ▼
    │            [05-sampling]
    │                  │
    │                  ▼
    │            [06-context-propagation]
    │                  │
    │                  ▼
    │            [07-java-sdk]
    │                  │
    │                  ▼
    │            [08-alerting-dashboards]
    │                  │
    │                  ▼
    │            [examples] ──► [practices] ──► [solutions]
    │
    ▼
[quiz.md] ────► Test Your Knowledge
```

## Key Features

### LightStep Capabilities
- ✅ Distributed tracing
- ✅ Metrics collection
- ✅ OpenTelemetry support
- ✅ Real-time analysis
- ✅ Service maps
- ✅ Alerting
- ✅ Dashboards

### Java SDK Features
- ✅ Auto-instrumentation (Agent)
- ✅ Manual instrumentation
- ✅ Spring Boot integration
- ✅ Context propagation
- ✅ Custom metrics
- ✅ Sampling strategies

## Quick Start

### 1. Add Dependencies

```xml
<dependency>
    <groupId>io.opentelemetry</groupId>
    <artifactId>opentelemetry-api</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Configure

```bash
export LIGHTSTEP_ACCESS_TOKEN=your-token
export LIGHTSTEP_SERVICE_NAME=my-service
```

### 3. Instrument

```java
Tracer tracer = GlobalOpenTelemetry.getTracer("my-service");
Span span = tracer.spanBuilder("my-operation").startSpan();
try (Scope scope = span.makeCurrent()) {
    // Your code
} finally {
    span.end();
}
```

## Support

- [Official Documentation](https://docs.lightstep.com/)
- [OpenTelemetry](https://opentelemetry.io/)
- [GitHub](https://github.com/lightstep)
