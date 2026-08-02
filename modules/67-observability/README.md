# Module 67: Observability

## Overview
Observability is the ability to understand system state from external outputs. It encompasses logging, metrics, and tracing for monitoring and debugging distributed systems.

## Learning Objectives
- Implement structured logging
- Collect and export metrics
- Use distributed tracing
- Create dashboards and alerts
- Apply observability best practices

## Prerequisites
- Logging concepts
- Monitoring basics
- Distributed systems

## Why This Concept Exists
Distributed systems are complex. Observability provides:
- System understanding
- Issue detection
- Performance optimization
- Root cause analysis

## Problem Statement
How do you understand and monitor complex distributed systems?

## Theory

### Three Pillars

| Pillar | Purpose | Tools |
|--------|---------|-------|
| Logs | Event records | ELK, Loki |
| Metrics | Numeric measurements | Prometheus, Datadog |
| Traces | Request flow | Jaeger, Zipkin |

### Metrics Types

| Type | Use Case |
|------|----------|
| Counter | Incrementing values |
| Gauge | Current values |
| Histogram | Distribution |
| Summary | Percentiles |

## Enterprise Example

```java
import io.micrometer.core.instrument.*;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

@RestController
public class ObservabilityController {
    private static final Logger logger = LoggerFactory.getLogger(ObservabilityController.class);
    
    private final MeterRegistry registry;
    private final Timer.Sample sample;
    
    public ObservabilityController(MeterRegistry registry) {
        this.registry = registry;
        this.sample = Timer.start(registry);
    }
    
    @GetMapping("/api/data")
    public String getData() {
        // Structured logging
        logger.atInfo()
            .addKeyValue("endpoint", "/api/data")
            .addKeyValue("userId", "123")
            .log("Processing request");
        
        // Counter
        registry.counter("api.requests", "endpoint", "/api/data").increment();
        
        // Timer
        Timer timer = Timer.builder("api.duration")
            .tag("endpoint", "/api/data")
            .register(registry);
        
        return timer.record(() -> {
            // Process request
            return "data";
        });
    }
    
    @GetMapping("/api/metrics")
    public String getMetrics() {
        // Gauge
        Gauge.builder("queue.size", queue, Queue::size)
            .tag("queue", "orders")
            .register(registry);
        
        // Distribution summary
        DistributionSummary summary = DistributionSummary.builder("order.amount")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        
        summary.record(100.0);
        
        return "Metrics exported";
    }
}
```

## Performance Considerations
- Use asynchronous logging
- Sample traces in production
- Aggregate metrics efficiently
- Store logs centrally

## Best Practices
1. Use structured logging
2. Implement health checks
3. Create meaningful alerts
4. Monitor business metrics
5. Correlate logs, metrics, traces

## Interview Questions

### Q1: What are the three pillars of observability?
**Answer:** Logs, metrics, and traces.

### Q2: What is structured logging?
**Answer:** Logging in a machine-readable format like JSON.

### Q3: What is distributed tracing?
**Answer:** Tracking requests across multiple services.

### Q4: What is the difference between logging and metrics?
**Answer:** Logs are events, metrics are numeric measurements.

### Q5: What is a health check?
**Answer:** Endpoint indicating service readiness and liveness.

## Summary
Observability is essential for understanding and operating distributed systems effectively.

## References
- OpenTelemetry Documentation
- Micrometer Documentation
- Observability Engineering
