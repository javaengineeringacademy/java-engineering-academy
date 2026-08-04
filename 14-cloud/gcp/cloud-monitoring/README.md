# Google Cloud Monitoring

## Overview

Cloud Monitoring provides observability into your Google Cloud and third-party applications.

## Core Concepts

```
┌─────────────────────────────────────────────────────────┐
│                 Cloud Monitoring                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │Uptime    │  │ Dashboards│  │  Alerts  │             │
│  │Checks    │  │          │  │          │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │   Metrics     │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Metrics

### GCP Metrics
```bash
# List metrics
gcloud monitoring metrics list \
  --filter='metric.type="compute.googleapis.com/instance/cpu/utilization"'

# Get metric data
gcloud monitoring time-series list \
  --filter='metric.type="compute.googleapis.com/instance/cpu/utilization"' \
  --interval-start-time=2024-01-15T00:00:00Z \
  --interval-end-time=2024-01-15T23:59:59Z
```

### Custom Metrics
```python
from google.cloud import monitoring_v3
import time

client = monitoring_v3.MetricServiceClient()
project_name = "projects/my-project"

series = monitoring_v3.TimeSeries()
series.metric.type = "custom.googleapis.com/my_metric"
series.resource.type = "global"
series.resource.labels["project_id"] = "my-project"

now = time.time()
interval = monitoring_v3.TimeInterval(
    {"end_time": {"seconds": int(now)}}
)
point = monitoring_v3.Point(
    {"interval": interval, "value": {"double_value": 42.0}}
)
series.points = [point]

client.create_time_series(
    request={"name": project_name, "time_series": [series]}
)
```

## Dashboards

```bash
# Create dashboard
gcloud monitoring dashboards create --config-from-file=dashboard.json
```

### Dashboard JSON
```json
{
  "displayName": "My Dashboard",
  "gridLayout": {
    "widgets": [
      {
        "title": "CPU Utilization",
        "xyChart": {
          "dataSets": [
            {
              "timeSeriesQuery": {
                "timeSeriesFilter": {
                  "filter": "metric.type=\"compute.googleapis.com/instance/cpu/utilization\"",
                  "aggregation": {
                    "alignmentPeriod": "60s",
                    "perSeriesAligner": "ALIGN_MEAN"
                  }
                }
              }
            }
          ]
        }
      }
    ]
  }
}
```

## Alert Policies

```bash
# Create alert policy
gcloud alpha monitoring policies create \
  --display-name="High CPU Alert" \
  --condition-filter='metric.type="compute.googleapis.com/instance/cpu/utilization"' \
  --condition-threshold-value=0.8 \
  --condition-threshold-duration=300s \
  --notification-channels="my-email-channel"
```

### Alert Policy JSON
```json
{
  "displayName": "High CPU Alert",
  "conditions": [
    {
      "displayName": "CPU > 80%",
      "conditionThreshold": {
        "filter": "metric.type=\"compute.googleapis.com/instance/cpu/utilization\"",
        "comparison": "COMPARISON_GT",
        "thresholdValue": 0.8,
        "duration": "300s"
      }
    }
  ],
  "notificationChannels": ["my-email-channel"],
  "alertStrategy": {
    "autoClose": "1800s"
  }
}
```

## Uptime Checks

```bash
# Create uptime check
gcloud monitoring uptime checks create http my-uptime-check \
  --display-name="My Website" \
  --uri="https://my-website.com" \
  --timeout=10s \
  --period=60s
```

## Notification Channels

```bash
# Create email notification channel
gcloud alpha monitoring channels create \
  --display-name="My Email" \
  --type=email \
  --channel-labels=email_address=admin@example.com

# Create Slack notification channel
gcloud alpha monitoring channels create \
  --display-name="My Slack" \
  --type=slack \
  --channel-labels=channel_name=my-channel,auth_token=my-token
```

## Service Monitoring

```bash
# Create service
gcloud monitoring services create my-service \
  --display-name="My Service"

# Create SLO
gcloud monitoring slos create my-slo \
  --service=my-service \
  --display-name="Availability SLO" \
  --goal=0.999 \
  --calendar-period=DAY \
  --rolling-period=30d
```

## SLO (Service Level Objectives)

### Error Budget
```python
# Calculate error budget
total_requests = 1000000
allowed_errors = total_requests * (1 - 0.999)  # 1000 errors
actual_errors = 500
error_budget_remaining = (allowed_errors - actual_errors) / allowed_errors
```

## Monitoring Groups

```bash
# Create group
gcloud monitoring groups create my-group \
  --display-name="My Group" \
  --filter='resource.type="gce_instance" AND labels.env="prod"'
```

## Log-Based Metrics

```bash
# Create log-based metric
gcloud logging metrics create my-log-metric \
  --log-filter='textPayload:"error"' \
  --description="Count of error logs"
```

## Cost Optimization

- **Use appropriate aggregation** periods
- **Implement alerting** for cost anomalies
- **Monitor with dashboards**
- **Use log-based metrics** when possible
- **Set retention policies**

## Best Practices

1. **Create dashboards** for visibility
2. **Set up alerting** for critical metrics
3. **Implement uptime checks**
4. **Use service monitoring** for SLOs
5. **Monitor with custom metrics**
6. **Implement proper notification** channels
7. **Use monitoring groups** for organization
8. **Regular metric review**
9. **Implement proper aggregation**
10. **Use log-based metrics** when possible
