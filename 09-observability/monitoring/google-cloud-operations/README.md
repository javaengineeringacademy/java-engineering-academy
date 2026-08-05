# Google Cloud Operations Suite

## Overview

Google Cloud Operations Suite (formerly Stackdriver) provides monitoring, logging, tracing, and debugging tools for applications running on Google Cloud, AWS, and on-premises environments.

## Core Components

### Cloud Monitoring
Collects metrics, events, and metadata from Google Cloud and external sources.

### Cloud Logging
Centralized logging service for storing, searching, and analyzing log data.

### Cloud Trace
Distributed tracing backend for analyzing application latency.

### Cloud Profiler
Continuous profiling tool for analyzing application performance.

## Architecture

```
Applications -> Ops Agent -> Google Cloud Operations Suite
                    |
            Metrics/Logs/Traces
                    |
        Monitoring/Logging/Trace APIs
                    |
            Dashboards/Alerts
```

### Agent Types
- **Ops Agent** - Unified agent for metrics and logs
- **OpenTelemetry Collector** - Vendor-neutral telemetry collection
- **Cloud Profiler Agent** - Continuous profiling agent

## Configuration

### Ops Agent Installation
```bash
# Linux
curl -sSO https://dl.google.com/cloudagents/add-google-cloud-ops-agent-repo.sh
sudo bash add-google-cloud-ops-agent-repo.sh --also-install

# Configuration file
cat > /etc/google-cloud-ops-agent/config.yaml << EOF
metrics:
  receivers:
    hostmetrics:
      type: hostmetrics
      collection_interval: 60s
  service:
    pipelines:
      default_pipeline:
        receivers: [hostmetrics]

logging:
  receivers:
    syslog:
      type: syslog
    app_logs:
      type: files
      include_paths:
        - /var/log/app/*.log
  service:
    pipelines:
      default_pipeline:
        receivers: [syslog, app_logs]
EOF
```

### Custom Metrics
```python
from google.cloud import monitoring_v3

client = monitoring_v3.MetricServiceClient()
project_name = f"projects/my-project"

series = monitoring_v3.TimeSeries()
series.metric.type = "custom.googleapis.com/order/count"
series.resource.type = "global"
series.resource.labels["project_id"] = "my-project"

point = monitoring_v3.Point()
point.value.int64_value = 1
point.interval.end_time.seconds = int(time.time())
series.points = [point]

client.create_time_series(
    name=project_name,
    time_series=[series]
)
```

## Key Features

### Cloud Monitoring
- **Dashboards** - Custom metric visualization
- **Uptime Checks** - External endpoint monitoring
- **Alerting Policies** - Metric and log-based alerts
- **SLO Monitoring** - Service level objective tracking

### Cloud Logging
- **Log Explorer** - Query and analyze logs
- **Log-based Alerts** - Trigger notifications from logs
- **Log Sinks** - Export logs to other destinations
- **Log Analytics** - SQL-based log analysis

### Cloud Trace
- **Request Tracing** - Automatic HTTP request tracing
- **Trace Analysis** - Latency breakdown and comparison
- **Trace Logs** - Correlated trace and log data

## Log-Based Alerts

```json
{
  "displayName": "Error Log Alert",
  "condition": {
    "displayName": "Error logs detected",
    "conditionThreshold": {
      "filter": "resource.type=\"k8s_container\" AND severity=ERROR",
      "comparison": "COMPARISON_GT",
      "thresholdValue": 0,
      "duration": "300s"
    }
  },
  "alertStrategy": {
    "autoClose": "1800s"
  }
}
```

## Cloud Trace Configuration

```python
from opentelemetry import trace
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor
from opentelemetry.exporter.cloud_trace import CloudTraceSpanExporter

exporter = CloudTraceSpanExporter()
provider = TracerProvider()
processor = BatchSpanProcessor(exporter)
provider.add_span_processor(processor)
trace.set_tracer_provider(provider)

tracer = trace.get_tracer(__name__)

with tracer.start_as_current_span("process-order") as span:
    span.set_attribute("order.id", order_id)
    process_order(order_id)
```

## Best Practices

1. Use Ops Agent for unified metrics and log collection
2. Configure log-based alerts for critical error patterns
3. Use OpenTelemetry for vendor-neutral instrumentation
4. Create dashboards for different operational perspectives
5. Implement SLO-based alerting for user-facing services
6. Use Cloud Trace for distributed tracing across services
7. Enable Cloud Profiler for continuous performance analysis
8. Set up log sinks for long-term retention and analysis
