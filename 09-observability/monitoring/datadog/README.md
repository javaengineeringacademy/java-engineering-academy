# Datadog

## Overview

Datadog is a cloud-scale monitoring and security platform that provides unified observability across infrastructure, applications, logs, and security. It offers SaaS-based metrics, traces, and log management.

## Core Concepts

### Agent Architecture
The Datadog Agent collects metrics, traces, and logs from hosts, containers, and applications.

### Tags
Key-value labels applied to metrics for filtering, grouping, and aggregation.

### Monitors
Alert definitions that trigger notifications based on metric conditions.

## Architecture

```
Applications -> Datadog Agent -> Datadog SaaS -> Dashboards/Alerts
                   |
            +------+------+
            |      |      |
         Metrics  Traces  Logs
```

### Agent Types
- **Infrastructure Agent** - Host and container metrics
- **APM Agent** - Application traces
- **Log Agent** - Log collection
- **Security Agent** - Runtime security monitoring

## Configuration

### Agent Installation
```bash
# Linux
DD_API_KEY=YOUR_KEY DD_SITE="datadoghq.com" \
  bash -c "$(curl -L https://install.datadoghq.com/scripts/install_script.sh)"

# Docker
docker run -d --name datadog-agent \
  -e DD_API_KEY=YOUR_KEY \
  -e DD_SITE="datadoghq.com" \
  -v /var/run/docker.sock:/var/run/docker.sock:ro \
  gcr.io/datadoghq/agent:latest
```

### Application Configuration
```python
from ddtrace import tracer

tracer.configure(
    hostname="my-service",
    service="order-service",
    env="production",
    version="1.0.0"
)

# Custom span
with tracer.trace("process.order", service="order-service"):
    process_order(order_id)
```

## Key Features

### Infrastructure Monitoring
- Host and container metrics
- Cloud provider integrations
- Network performance monitoring
- Database monitoring

### APM
- Distributed tracing
- Code-level profiling
- Service map visualization
- Database query analysis

### Log Management
- Log collection and parsing
- Index filtering and retention
- Live tail for debugging
- Log-based metrics

## Monitors and Alerts

### Metric Monitor
```json
{
  "name": "High Error Rate",
  "type": "metric alert",
  "query": "sum:rate(http.requests{status:5xx}.as_count()) / sum:rate(http.requests.as_count()) > 0.05",
  "message": "Error rate exceeded 5% threshold",
  "options": {
    "thresholds": {
      "critical": 0.05,
      "warning": 0.02
    },
    "notify_no_data": true
  }
}
```

### Composite Monitor
```json
{
  "name": "Service Degraded",
  "type": "composite",
  "query": "high_error_rate && high_latency"
}
```

## Dashboards

### Dashboard Templates
- Infrastructure Overview
- APM Service Overview
- Log Analytics
- Container Monitoring

### Custom Widgets
```json
{
  "definition": {
    "type": "timeseries",
    "requests": [{
      "q": "avg:http.request.duration{service:order-service}",
      "display_type": "line"
    }]
  }
}
```

## Best Practices

1. Use consistent tagging across all metrics
2. Create service-level dashboards for each team
3. Configure monitors with clear escalation policies
4. Use Log Management to correlate logs with metrics
5. Leverage APM for distributed tracing across services
6. Set up synthetic monitoring for user-facing endpoints
7. Use database monitoring for query performance
8. Enable runtime security monitoring for threat detection
