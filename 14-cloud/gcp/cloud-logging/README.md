# Google Cloud Logging

## Overview

Cloud Logging is a fully managed service for storing, monitoring, and analyzing log data.

## Core Concepts

```
┌─────────────────────────────────────────────────────────┐
│                  Cloud Logging                           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │  Logs    │  │  Sinks   │  │  Alerts  │             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │  Analytics    │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## Writing Logs

### gcloud CLI
```bash
# Write log entry
gcloud logging write my-log "Hello World" \
  --payload-type=text \
  --severity=INFO

# Write JSON log
gcloud logging write my-log '{"message": "Hello World", "trace": "my-trace"}' \
  --payload-type=json
```

### SDK
```python
from google.cloud import logging

client = logging.Client()
logger = client.logger('my-log')

# Write log entry
logger.log_struct({
    'message': 'Hello World',
    'severity': 'INFO',
    'httpRequest': {
        'requestMethod': 'GET',
        'requestUrl': '/api/resource',
    }
})
```

## Querying Logs

### Log Explorer
```sql
-- Query logs
resource.type="gce_instance"
logName="projects/my-project/logs/my-log"
severity>=ERROR

-- Filter by time
timestamp>="2024-01-15T00:00:00Z"
timestamp<="2024-01-15T23:59:59Z"
```

### gcloud CLI
```bash
# Query logs
gcloud logging read 'resource.type="gce_instance" AND severity>=ERROR' \
  --limit=100 \
  --format=json
```

## Log Sinks

```bash
# Create sink to BigQuery
gcloud logging sinks create my-bq-sink \
  bigquery.googleapis.com/projects/my-project/datasets/my_dataset \
  --log-filter='resource.type="gce_instance"'

# Create sink to GCS
gcloud logging sinks create my-gcs-sink \
  storage.googleapis.com/my-bucket \
  --log-filter='severity>=ERROR'

# Create sink to Pub/Sub
gcloud logging sinks create my-pubsub-sink \
  pubsub.googleapis.com/projects/my-project/topics/my-topic \
  --log-filter='resource.type="gce_instance"'
```

## Log-Based Metrics

```bash
# Create log-based metric
gcloud logging metrics create my-metric \
  --log-filter='textPayload:"error"' \
  --description="Count of error logs"

# Get metric
gcloud logging metrics describe my-metric
```

## Exclusion Filters

```bash
# Create exclusion
gcloud logging sinks update my-sink \
  --exclusions='[{"filter":"textPayload:\"debug\"", "disabled":false}]'
```

## Log Storage

```bash
# Set bucket config
gcloud logging buckets create my-bucket \
  --location=us-central1 \
  --retention-days=365

# Update bucket
gcloud logging buckets update _Default \
  --location=global \
  --retention-days=90
```

## Advanced Logs Filters

```sql
-- Text search
textPayload:"error"

-- JSON field
jsonPayload.event_type="order.created"

-- Multiple conditions
resource.type="gce_instance" AND severity>=ERROR AND textPayload:"timeout"

-- Exclusion
NOT textPayload:"debug"
```

## Alerting

```bash
# Create alert policy
gcloud alpha monitoring policies create \
  --display-name="Error Log Alert" \
  --condition-filter='log_metric="my-error-metric"' \
  --condition-threshold-value=10 \
  --condition-threshold-duration=60s
```

## BigQuery Integration

```sql
-- Query logs in BigQuery
SELECT
  timestamp,
  textPayload,
  severity,
  resource.type
FROM
  `my-project.my_dataset.cloudlogging_sink_*`
WHERE
  _TABLE_SUFFIX BETWEEN '2024_01_15' AND '2024_01_16'
  AND severity = 'ERROR'
```

## Audit Logging

```bash
# Enable admin activity audit logs
gcloud projects get-iam-policy my-project

# View audit logs
gcloud logging read 'protoPayload.serviceName="cloudresourcemanager.googleapis.com"' \
  --limit=100
```

## Cost Optimization

- **Use exclusion filters** to reduce volume
- **Set retention policies** appropriately
- **Use log-based metrics** instead of custom metrics
- **Archive old logs** to Coldline storage
- **Monitor log volume**

## Best Practices

1. **Use structured logging** (JSON)
2. **Include trace IDs** for correlation
3. **Set appropriate log levels**
4. **Implement log sinks** for export
5. **Use log-based metrics** for monitoring
6. **Set retention policies**
7. **Use exclusion filters** for noise reduction
8. **Enable audit logging** for compliance
9. **Monitor with alerts**
10. **Regular log review**
