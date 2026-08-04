# Micrometer Metrics

## Overview

Micrometer is an application metrics facade for JVM-based applications, providing a vendor-neutral API for metrics collection with Spring Boot auto-configuration.

## Core Concepts

### Meter Types
- **Counter** - Monotonically increasing value
- **Gauge** - Value that can go up or down
- **Timer** - Duration and count of events
- **DistributionSummary** - Distribution of values

## Usage Examples

### Counter
```java
Counter ordersCounter = Counter.builder("orders.created")
    .tag("type", "standard")
    .description("Total orders created")
    .register(meterRegistry);

ordersCounter.increment();
```

### Timer
```java
Timer.builder("order.processing.time")
    .publishPercentiles(0.5, 0.95, 0.99)
    .register(meterRegistry)
    .record(() -> processOrder(order));
```

### Gauge
```java
Gauge.builder("queue.size", queue, Queue::size)
    .description("Current queue size")
    .register(meterRegistry);
```

### DistributionSummary
```java
DistributionSummary.builder("order.amount")
    .publishPercentiles(0.5, 0.95, 0.99)
    .register(meterRegistry)
    .record(orderAmount);
```

## Tags and Dimensions

```java
// Tags for dimensional metrics
Counter.builder("http.requests")
    .tag("method", "GET")
    .tag("status", "200")
    .tag("service", "user-service")
    .register(meterRegistry)
    .increment();
```

## Common Registries

| Registry | Use Case |
|----------|----------|
| PrometheusMeterRegistry | Prometheus |
| SimpleMeterRegistry | Testing/Development |
| CompositeMeterRegistry | Multiple backends |
| StatsdMeterRegistry | StatsD |
| DatadogMeterRegistry | Datadog |
| CloudWatchMeterRegistry | AWS CloudWatch |

## Spring Boot Configuration

```yaml
management:
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: ${spring.application.name}
    distribution:
      percentiles-histogram:
        http.server.requests: true
      percentiles:
        http.server.requests: 0.5, 0.95, 0.99
```

## Custom Metrics

```java
@Component
public class BusinessMetrics {
    private final Counter orderCounter;
    private final Timer processingTimer;
    
    public BusinessMetrics(MeterRegistry registry) {
        this.orderCounter = Counter.builder("business.orders.total")
            .register(registry);
        this.processingTimer = Timer.builder("business.processing.time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
    }
    
    public void recordOrder() {
        orderCounter.increment();
    }
    
    public void recordProcessing(Runnable operation) {
        processingTimer.record(operation);
    }
}
```

## Best Practices

1. Use meaningful meter names with units
2. Apply consistent tag conventions
3. Avoid high-cardinality tag values
4. Configure percentile histograms for latency
5. Use common tags for service identification
6. Monitor registry health metrics
7. Use timers for duration measurements
8. Test metrics in integration tests
