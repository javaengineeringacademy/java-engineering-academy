# 09 - Observability

## Overview

Observability is the ability to understand the internal state of a system by examining its external outputs. In modern distributed systems, observability is built on three pillars: **logs**, **metrics**, and **traces**.

## The Three Pillars

### 1. Logs
Logs are discrete events that record what happened in the system. They provide detailed context for debugging and auditing.

- **SLF4J** - Logging facade for Java
- **Logback** - Default SLF4J implementation
- **Log4j2** - High-performance logging framework
- **Structured Logging** - Machine-readable log formats
- **ELK Stack** - Elasticsearch, Logstash, Kibana for log aggregation
- **Centralized Logging** - Aggregating logs from distributed services

### 2. Metrics
Metrics are numerical measurements collected over time. They enable monitoring system health and performance trends.

- **Prometheus** - Time-series database and monitoring
- **Grafana** - Visualization and dashboarding
- **JMX** - Java Management Extensions monitoring
- **Micrometer** - Application metrics facade

### 3. Traces
Traces track requests as they flow through distributed systems, showing the path and latency of each operation.

- **Jaeger** - End-to-end distributed tracing
- **Zipkin** - Distributed tracing system
- **OpenTelemetry** - Vendor-neutral observability framework
- **Context Propagation** - Passing trace context across service boundaries

## Additional Topics

### Alerting
Notifications triggered when metrics or health checks indicate problems.

- **AlertManager** - Prometheus alert routing and deduplication
- **On-Call Management** - Rotation schedules and escalation policies

### Profiling
Deep analysis of application performance at the code level.

- **AsyncProfiler** - Low-overhead CPU and allocation profiling
- **JMH** - Java Microbenchmark Harness
- **Heap Dumps** - Memory analysis
- **Flame Graphs** - Visual representation of stack traces

### Health Checks
Mechanisms to verify system components are functioning correctly.

- **Spring Boot Actuator** - Production-ready health endpoints
- **Probes** - Kubernetes liveness, readiness, startup checks
- **Load Balancer Health Checks** - Infrastructure-level health verification

## Directory Structure

```
09-observability/
├── logging/
│   ├── slf4j/
│   ├── logback/
│   ├── log4j2/
│   ├── elk/
│   ├── structured/
│   └── centralized/
├── metrics/
│   ├── prometheus/
│   ├── grafana/
│   ├── jmx/
│   └── micrometer/
├── tracing/
│   ├── jaeger/
│   ├── zipkin/
│   ├── opentelemetry/
│   └── context-propagation/
├── alerting/
│   ├── alertmanager/
│   └── oncall/
├── profiling/
│   ├── async-profiler/
│   ├── jmh/
│   ├── heap-dumps/
│   └── flame-graphs/
└── health-checks/
    ├── actuator/
    ├── probes/
    └── load-balancers/
```

## Observability Maturity Model

| Level | Description | Capabilities |
|-------|-------------|--------------|
| 0 - Reactive | No observability | Manual debugging, SSH access |
| 1 - Basic | Logging and basic metrics | Centralized logs, uptime monitoring |
| 2 - Intermediate | Distributed tracing, dashboards | End-to-end visibility, basic alerting |
| 3 - Advanced | Full observability | Correlated logs/metrics/traces, SLO-based alerting |
| 4 - Intelligent | AIOps, predictive | Anomaly detection, auto-remediation |

## Key Principles

1. **Correlation** - Link logs, metrics, and traces using trace IDs
2. **Context** - Enrich all signals with service, instance, and version metadata
3. **Actionability** - Every alert should have a clear response procedure
4. **Cost Awareness** - Sampling and retention policies control storage costs
5. **Security** - Redact sensitive data from all observability signals

## Technology Stack

| Category | Recommended Tools |
|----------|-------------------|
| Logging | SLF4J + Logback, ELK Stack |
| Metrics | Prometheus + Grafana, Micrometer |
| Tracing | OpenTelemetry, Jaeger |
| Alerting | AlertManager, PagerDuty |
| Profiling | AsyncProfiler, JMH |
| Health Checks | Spring Boot Actuator |

## Getting Started

1. Add SLF4J + Logback for structured logging
2. Add Micrometer + Prometheus for metrics collection
3. Add Spring Boot Actuator for health checks
4. Configure OpenTelemetry for distributed tracing
5. Set up Grafana dashboards for visualization
6. Configure AlertManager for notifications

## References

- [OpenTelemetry Documentation](https://opentelemetry.io/docs/)
- [Prometheus Best Practices](https://prometheus.io/docs/practices/)
- [The Three Pillars of Observability](https://www.oreilly.com/library/view/distributed-systems-observability/9781492033431/)
- [SLF4J Manual](http://www.slf4j.org/manual.html)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
