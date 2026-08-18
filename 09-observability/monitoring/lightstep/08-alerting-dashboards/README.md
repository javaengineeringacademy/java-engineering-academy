# Alerting & Dashboards

## Overview

LightStep provides comprehensive alerting and dashboard capabilities for monitoring application health and performance.

---

## Alerting

### Alert Conditions

#### Error Rate Alert

```yaml
alert:
  name: high-error-rate
  description: Alert when error rate exceeds threshold
  condition: |
    rate(http.server.requests{status="error"}[5m]) / 
    rate(http.server.requests[5m]) > 0.05
  severity: critical
  message: "Error rate exceeds 5%"
  actions:
    - type: slack
      channel: "#alerts"
      message: "High error rate detected: {{value}}%"
    - type: pagerduty
      severity: critical
```

#### Latency Alert

```yaml
alert:
  name: high-latency
  description: Alert when P99 latency exceeds threshold
  condition: |
    histogram_quantile(0.99, rate(http.server.duration_bucket[5m])) > 1000
  severity: warning
  message: "P99 latency exceeds 1000ms"
  actions:
    - type: slack
      channel: "#performance"
      message: "High latency detected: {{value}}ms"
```

#### SLA Breach Alert

```yaml
alert:
  name: sla-breach
  description: Alert when SLA is breached
  condition: |
    1 - (rate(http.server.requests{status="success"}[5m]) / 
    rate(http.server.requests[5m])) > 0.001
  severity: critical
  message: "SLA breach: Availability below 99.9%"
  actions:
    - type: pagerduty
      severity: critical
    - type: email
      to: "team@company.com"
```

---

## Alert Types

### Availability Alerts

```yaml
# Service down
alert:
  name: service-down
  condition: |
    up{service="my-service"} == 0
  severity: critical
  message: "Service is down"

# High error rate
alert:
  name: high-error-rate
  condition: |
    rate(http.server.requests{status="error"}[5m]) > 0.1
  severity: critical
  message: "Error rate exceeds 10%"
```

### Performance Alerts

```yaml
# High latency
alert:
  name: high-latency
  condition: |
    histogram_quantile(0.95, rate(http.server.duration_bucket[5m])) > 500
  severity: warning
  message: "P95 latency exceeds 500ms"

# Slow database queries
alert:
  name: slow-queries
  condition: |
    histogram_quantile(0.99, rate(db.query.duration_bucket[5m])) > 1000
  severity: warning
  message: "Database queries are slow"
```

### Resource Alerts

```yaml
# High memory usage
alert:
  name: high-memory
  condition: |
    jvm.memory.used / jvm.memory.max > 0.8
  severity: warning
  message: "Memory usage exceeds 80%"

# High CPU usage
alert:
  name: high-cpu
  condition: |
    rate(process_cpu_seconds_total[5m]) > 0.8
  severity: warning
  message: "CPU usage exceeds 80%"
```

---

## Dashboard Creation

### Request Overview Dashboard

```json
{
  "title": "Request Overview",
  "panels": [
    {
      "title": "Request Rate",
      "type": "timeseries",
      "query": "rate(http.server.requests[5m])",
      "unit": "req/s"
    },
    {
      "title": "Error Rate",
      "type": "timeseries",
      "query": "rate(http.server.requests{status=\"error\"}[5m]) / rate(http.server.requests[5m])",
      "unit": "percent"
    },
    {
      "title": "P50 Latency",
      "type": "timeseries",
      "query": "histogram_quantile(0.50, rate(http.server.duration_bucket[5m]))",
      "unit": "ms"
    },
    {
      "title": "P95 Latency",
      "type": "timeseries",
      "query": "histogram_quantile(0.95, rate(http.server.duration_bucket[5m]))",
      "unit": "ms"
    },
    {
      "title": "P99 Latency",
      "type": "timeseries",
      "query": "histogram_quantile(0.99, rate(http.server.duration_bucket[5m]))",
      "unit": "ms"
    }
  ]
}
```

### Service Map Dashboard

```json
{
  "title": "Service Map",
  "panels": [
    {
      "title": "Service Dependencies",
      "type": "service-map",
      "query": "service.name != \"\""
    },
    {
      "title": "Service Request Rate",
      "type": "timeseries",
      "query": "sum by (service.name) (rate(http.server.requests[5m]))",
      "unit": "req/s"
    },
    {
      "title": "Service Error Rate",
      "type": "timeseries",
      "query": "sum by (service.name) (rate(http.server.requests{status=\"error\"}[5m])) / sum by (service.name) (rate(http.server.requests[5m]))",
      "unit": "percent"
    }
  ]
}
```

---

## Dashboard Types

### Overview Dashboard

- Request rate over time
- Error rate over time
- Latency percentiles (P50, P95, P99)
- Active traces
- Service health status

### Service Dashboard

- Service-specific request rate
- Service-specific error rate
- Service-specific latency
- Database query performance
- External dependency performance

### SLA Dashboard

- Availability percentage
- Error budget remaining
- SLA breach history
- Compliance trends

---

## Alert Actions

### Slack Integration

```yaml
actions:
  - type: slack
    channel: "#alerts"
    message: |
      Alert: {{alert.name}}
      Severity: {{alert.severity}}
      Value: {{value}}
      Timestamp: {{timestamp}}
```

### PagerDuty Integration

```yaml
actions:
  - type: pagerduty
    severity: critical
    description: "{{alert.message}}"
```

### Email Integration

```yaml
actions:
  - type: email
    to: "team@company.com"
    subject: "Alert: {{alert.name}}"
    body: |
      Alert: {{alert.name}}
      Severity: {{alert.severity}}
      Value: {{value}}
```

---

## Advanced Alerting

### Composite Alerts

```yaml
alert:
  name: cascading-failure
  description: Multiple services experiencing issues
  condition: |
    rate(http.server.requests{service="service-a",status="error"}[5m]) > 0.1
    and
    rate(http.server.requests{service="service-b",status="error"}[5m]) > 0.1
    and
    rate(http.server.requests{service="service-c",status="error"}[5m]) > 0.1
  severity: critical
  message: "Cascading failure detected across multiple services"
```

### Prediction Alerts

```yaml
alert:
  name: sla-breach-prediction
  description: Predict SLA breach based on current trends
  condition: |
    predict_linear(http.server.availability[1h], 3600) < 0.999
  severity: warning
  message: "SLA breach predicted within 1 hour"
```

---

## Best Practices

### Alert Design

1. **Clear Messages**: Include context and value
2. **Appropriate Severity**: Critical, warning, info
3. **Actionable**: Include next steps
4. **Avoid Noise**: Use thresholds and windows

### Dashboard Design

1. **Logical Grouping**: Related metrics together
2. **Consistent Time Ranges**: Use same time windows
3. **Clear Titles**: Describe what each panel shows
4. **Appropriate Units**: ms, req/s, percent

---

## Troubleshooting

### Missing Alerts

- Check alert conditions
- Verify data collection
- Check notification channels

### False Positives

- Adjust thresholds
- Increase time windows
- Add additional conditions

### Dashboard Issues

- Check data sources
- Verify query syntax
- Check panel configurations

---

## Next Steps

- [Examples](../examples/) - Code examples
- [Practices](../practices/) - Hands-on exercises
- [Solutions](../solutions/) - Complete solutions
