# Grafana Dashboards

## Overview

Grafana is an open-source analytics and visualization platform that connects to multiple data sources to create dashboards, alerts, and reports.

## Data Sources

| Type | Use Case |
|------|----------|
| Prometheus | Metrics |
| Elasticsearch | Logs |
| Jaeger/Zipkin | Traces |
| Loki | Log aggregation |
| InfluxDB | Time-series |
| SQL | Relational data |

## Dashboard Structure

### Panel Types
- **Time Series** - Line/area charts over time
- **Stat** - Single value with trend
- **Gauge** - Value within range
- **Table** - Tabular data
- **Heatmap** - Distribution visualization
- **Logs** - Log stream display

### Variables
```json
{
  "name": "service",
  "type": "query",
  "query": "label_values(http_requests_total, service)",
  "refresh": 2
}
```

### Query Variables
```promql
# Variable query
label_values(http_requests_total, service)

# Using variable
rate(http_requests_total{service="$service"}[5m])
```

## Dashboard JSON

```json
{
  "dashboard": {
    "title": "Service Overview",
    "panels": [
      {
        "title": "Request Rate",
        "type": "timeseries",
        "targets": [
          {
            "expr": "rate(http_requests_total{service=\"$service\"}[5m])",
            "legendFormat": "{{status}}"
          }
        ]
      }
    ],
    "templating": {
      "list": [
        {
          "name": "service",
          "type": "query",
          "query": "label_values(http_requests_total, service)"
        }
      ]
    }
  }
}
```

## Alerting

### Contact Points
- Email
- Slack
- PagerDuty
- Webhook
- Microsoft Teams

### Alert Rules
```json
{
  "condition": "A",
  "frequency": "5m",
  "notifications": [
    {"uid": "slack-channel"}
  ],
  "data": [
    {
      "model": {
        "expr": "rate(http_requests_total{status=~\"5..\"}[5m]) > 0.1"
      }
    }
  ]
}
```

## Best Practices

1. Organize dashboards by service/layer
2. Use consistent color schemes
3. Set appropriate time ranges
4. Create linked dashboards
5. Use annotations for deployments
6. Implement dashboard provisioning
7. Version control dashboard JSON
8. Set up alert escalation
