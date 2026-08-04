# Prometheus Monitoring

## Overview

Prometheus is an open-source monitoring and alerting toolkit designed for reliability and scalability. It collects metrics via pull-based model, stores time-series data, and supports PromQL for querying.

## Core Concepts

### Metrics Types
- **Counter** - Monotonically increasing value (requests, errors)
- **Gauge** - Value that can go up or down (temperature, connections)
- **Histogram** - Distribution of values (request duration)
- **Summary** - Similar to histogram, computed client-side

### Labels
```java
Counter.builder("http_requests_total")
    .tag("method", "GET")
    .tag("status", "200")
    .register(meterRegistry)
    .increment();
```

## Configuration

### prometheus.yml
```yaml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

rule_files:
  - "alert_rules.yml"

alerting:
  alertmanagers:
    - static_configs:
        - targets: ['localhost:9093']

scrape_configs:
  - job_name: 'spring-boot'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
```

## PromQL Queries

### Rate and Increase
```promql
# Requests per second
rate(http_requests_total[5m])

# Total increase in last hour
increase(http_requests_total[1h])

# Error rate
rate(http_requests_total{status=~"5.."}[5m]) / rate(http_requests_total[5m])
```

### Histogram Quantiles
```promql
# 95th percentile latency
histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m]))

# 99th percentile
histogram_quantile(0.99, rate(http_request_duration_seconds_bucket[5m]))
```

### Aggregations
```promql
# Top 10 services by request rate
topk(10, rate(http_requests_total[5m]))

# Average by service
avg by (service) (http_request_duration_seconds)
```

## Alert Rules

```yaml
groups:
  - name: application
    rules:
      - alert: HighErrorRate
        expr: rate(http_requests_total{status=~"5.."}[5m]) > 0.1
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High error rate detected"
          
      - alert: HighLatency
        expr: histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m])) > 1
        for: 5m
        labels:
          severity: warning
```

## Micrometer Integration

```java
@Configuration
public class MetricsConfig {
    @Bean
    public MeterRegistryCustomizer<PrometheusMeterRegistry> metricsCustomizer() {
        return registry -> registry.config()
            .commonTags("application", "my-service");
    }
}
```

## Best Practices

1. Use appropriate metric types
2. Choose meaningful label names
3. Avoid high cardinality labels
4. Set scrape intervals appropriately
5. Use recording rules for complex queries
6. Implement alert routing
7. Monitor Prometheus itself
8. Use federation for multi-cluster
