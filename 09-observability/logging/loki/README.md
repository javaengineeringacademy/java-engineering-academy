# Grafana Loki

## Overview

Grafana Loki is a horizontally scalable, multi-tenant log aggregation system. Unlike full-text indexing systems, Loki indexes only labels, making it cost-effective and efficient for storing large volumes of log data.

## Core Concepts

### Labels
Key-value pairs used to index log streams. Labels enable efficient querying without full-text indexing.

### Streams
A unique combination of labels that identifies a log stream. Each stream has its own set of log entries.

### LogQL
Loki's query language inspired by PromQL for querying and filtering log data.

## Architecture

```
Applications -> Promtail/Fluentd -> Loki -> Grafana
                    |                 |
              Parse/Label        Store/Index
                    |                 |
                 Index            Chunks (S3/GCS)
```

### Components
- **Distributor** - Receives and validates incoming logs
- **Ingester** - Builds chunks and indexes for log streams
- **Querier** - Handles LogQL queries
- **Store Gateway** - Accesses historical data from object storage

## Configuration

### Loki Configuration
```yaml
auth_enabled: false

server:
  http_listen_port: 3100

common:
  path_prefix: /loki
  storage:
    filesystem:
      chunks_directory: /loki/chunks
      rules_directory: /loki/rules
  replication_factor: 1
  ring:
    kvstore:
      store: inmemory

schema_config:
  configs:
    - from: "2024-01-01"
      store: tsdb
      object_store: filesystem
      schema: v13
      index:
        prefix: index_
        period: 24h
```

### Promtail Configuration
```yaml
server:
  http_listen_port: 9080

positions:
  filename: /tmp/positions.yaml

clients:
  - url: http://loki:3100/loki/api/v1/push

scrape_configs:
  - job_name: app-logs
    static_configs:
      - targets:
          - localhost
        labels:
          app: order-service
          environment: production
          __path__: /var/log/app/*.log
    pipeline_stages:
      - json:
          expressions:
            level: level
            message: message
      - labels:
          level:
      - timestamp:
          source: timestamp
          format: RFC3339Nano
```

## Key Features

### Cost Efficiency
- No full-text indexing reduces storage costs
- Label-based indexing for efficient queries
- Object storage backend for scalability

### Multi-Tenancy
- Tenant-based isolation
- Per-tenant limits and quotas
- Federated querying across tenants

### LogQL
- PromQL-inspired query language
- Label filtering and parsing
- Log aggregation and metrics

## LogQL Queries

### Basic Log Queries
```logql
# Filter by label
{app="order-service"}

# Filter by multiple labels
{app="order-service", environment="production"}

# Filter by level
{app="order-service"} |~ "ERROR"

# Exclude pattern
{app="order-service"} !~ "DEBUG"
```

### Log Parsing
```logql
# JSON parsing
{app="order-service"} | json | level="ERROR"

# Logfmt parsing
{app="order-service"} | logfmt | status >= 500

# Regex parsing
{app="order-service"} | regexp `(?P<level>\w+): (?P<message>.*)`
```

### Metrics from Logs
```logql
# Rate of error logs
rate({app="order-service"} |~ "ERROR" [5m])

# Count by level
count_over_time({app="order-service"} [5m]) by (level)

# Top error messages
topk(10, 
  sum by (message) (
    rate({app="order-service"} | json | level="ERROR" [5m])
  )
)
```

## Grafana Integration

### Data Source Configuration
```json
{
  "name": "Loki",
  "type": "loki",
  "url": "http://loki:3100",
  "access": "proxy",
  "jsonData": {
    "maxLines": 1000,
    "timeout": 60
  }
}
```

### Dashboard Queries
```logql
# Error rate panel
rate({app="$app"} |~ "ERROR" [$__interval])

# Log volume panel
sum(count_over_time({app="$app"} [$__interval])) by (level)
```

## Best Practices

1. Use consistent label naming conventions
2. Avoid high cardinality labels
3. Use pipeline stages for efficient parsing
4. Configure retention policies for cost management
5. Use recording rules for frequently run queries
6. Implement tenant isolation for multi-team environments
7. Monitor Loki's resource usage and query performance
8. Use object storage for production deployments
