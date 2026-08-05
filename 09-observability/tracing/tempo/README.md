# Grafana Tempo

## Overview

Grafana Tempo is a high-scale distributed tracing backend that stores and queries trace data. It is designed for cost-effective trace storage with minimal indexing, making it suitable for high-volume tracing workloads.

## Core Concepts

### Trace Storage
Tempo stores traces as complete trace objects without indexing individual fields, reducing storage costs.

### TraceQL
Tempo's query language for searching and analyzing trace data.

### Search by ID
Direct trace lookup using trace IDs for fast retrieval of specific traces.

## Architecture

```
Applications -> OTel Collector -> Tempo -> Grafana
                       |            |
               Ingest/Process   Store/Query
                       |            |
                    Object Storage (S3/GCS/Azure)
```

### Components
- **Distributor** - Receives and validates incoming traces
- **Ingester** - Builds and flushes trace blocks
- **Querier** - Handles search and trace retrieval
- **Compactor** - Merges and deduplicates trace blocks

## Configuration

### Tempo Configuration
```yaml
server:
  http_listen_port: 3200

distributor:
  receivers:
    otlp:
      protocols:
        grpc:
          endpoint: "0.0.0.0:4317"
        http:
          endpoint: "0.0.0.0:4318"
    jaeger:
      protocols:
        thrift_http:
          endpoint: "0.0.0.0:14268"
        grpc:
          endpoint: "0.0.0.0:14250"

ingester:
  lifecycler:
    ring:
      replication_factor: 1

storage:
  trace:
    backend: local
    local:
      path: /var/tempo/traces
    block:
      bloom_filter_false_positive: 0.05
      index_downsample_bytes: 1000

compactor:
  compaction:
    block_retention: 48h
```

### Docker Configuration
```yaml
version: '3.8'
services:
  tempo:
    image: grafana/tempo:latest
    command: ["-config.file=/etc/tempo/tempo.yaml"]
    volumes:
      - ./tempo.yaml:/etc/tempo/tempo.yaml
      - tempo-data:/var/tempo
    ports:
      - "3200:3200"
      - "4317:4317"
      - "4318:4318"

volumes:
  tempo-data:
```

### OTel Collector Configuration
```yaml
receivers:
  otlp:
    protocols:
      grpc:
        endpoint: "0.0.0.0:4317"
      http:
        endpoint: "0.0.0.0:4318"

processors:
  batch:
    timeout: 1s
    send_batch_size: 1024

exporters:
  otlp:
    endpoint: "tempo:4317"
    tls:
      insecure: true

service:
  pipelines:
    traces:
      receivers: [otlp]
      processors: [batch]
      exporters: [otlp]
```

## Key Features

### Cost-Effective Storage
- No field indexing reduces storage costs
- Object storage backend for scalability
- Configurable retention policies

### Search Capabilities
- TraceQL for complex queries
- Search by service, duration, and tags
- Direct trace ID lookup

### Integration
- Native Grafana integration
- OTLP support for vendor-neutral ingestion
- Correlation with logs and metrics

## TraceQL Queries

### Basic Queries
```traceql
# Search by service
{ service.name = "order-service" }

# Search by operation
{ service.name = "order-service" && name = "process-order" }

# Search by duration
{ service.name = "order-service" && duration > 1s }

# Search by status
{ service.name = "order-service" && status = error }
```

### Advanced Queries
```traceql
# Complex conditions
{ service.name = "order-service" && 
  duration > 1s && 
  status = error &&
  resource.os.type = "linux" }

# Span attributes
{ span.db.system = "postgresql" && 
  span.db.statement =~ "SELECT.*orders" }

# Aggregate queries
{ service.name = "order-service" } | avg(duration)
{ service.name = "order-service" } | p99(duration)
```

### Metrics from Traces
```traceql
# Request rate
rate({ service.name = "order-service" } [1m])

# Error rate
rate({ service.name = "order-service" && status = error } [1m])

# Latency distribution
histogram_quantile(0.99, 
  rate({ service.name = "order-service" } | duration [1m])
)
```

## Grafana Integration

### Data Source Configuration
```json
{
  "name": "Tempo",
  "type": "tempo",
  "url": "http://tempo:3200",
  "access": "proxy",
  "jsonData": {
    "httpMethod": "GET",
    "nodeGraph": { "enabled": true },
    "traceQuery": {
      "timeShiftEnabled": true,
      "spanStartTimeShift": "-30m",
      "spanEndTimeShift": "30m"
    }
  }
}
```

### Service Graph
```json
{
  "datasource": "Tempo",
  "type": "nodeGraph",
  "options": {
    "edges": {
      "color": {
        "mode": "palette"
      }
    },
    "nodes": {
      "color": {
        "mode": "palette"
      }
    }
  }
}
```

## Best Practices

1. Use OTLP for vendor-neutral trace ingestion
2. Configure appropriate retention policies
3. Use object storage for production deployments
4. Implement sampling strategies to control volume
5. Use TraceQL for efficient trace searching
6. Correlate traces with logs using trace IDs
7. Monitor Tempo's resource usage and query performance
8. Use Grafana Tempo's service graph for dependency visualization
