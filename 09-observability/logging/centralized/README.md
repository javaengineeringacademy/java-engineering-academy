# Centralized Logging

## Overview

Centralized logging aggregates logs from multiple services and instances into a single system, enabling unified search, analysis, and monitoring across distributed systems.

## Architecture Patterns

### Agent-Based Collection
```
Service -> Agent (Fluentd/Filebeat) -> Aggregator (Logstash) -> Storage (Elasticsearch) -> UI (Kibana)
```

### Direct Shipping
```
Service -> Message Queue (Kafka) -> Processor -> Storage -> UI
```

### Sidecar Pattern (Kubernetes)
```
Pod: [Service Container] [Log Collector Sidecar]
```

## Components

### Fluentd
```xml
<source>
  @type tail
  path /var/log/app/*.log
  pos_file /var/log/fluentd/app.log.pos
  tag app.logs
  <parse>
    @type json
  </parse>
</source>

<match app.logs>
  @type elasticsearch
  host localhost
  port 9200
  index_name app-logs
  <buffer>
    @type file
    path /var/log/fluentd/buffer
    flush_interval 5s
  </buffer>
</match>
```

### Vector
```toml
[sources.app_logs]
  type = "file"
  include = ["/var/log/app/*.log"]

[transforms.parse]
  type = "remap"
  inputs = ["app_logs"]
  source = '''
    . = parse_json!(.message)
  '''

[sinks.elasticsearch]
  type = "elasticsearch"
  inputs = ["parse"]
  endpoints = ["http://localhost:9200"]
  index = "logs-%Y-%m-%d"
```

## Correlation

### Trace ID Propagation
```java
// Include trace ID in all logs
MDC.put("traceId", traceContext.traceId());
MDC.put("spanId", traceContext.spanId());

// Log pattern
[%X{traceId}] [%X{spanId}] %level %logger - %msg
```

### Request Context
```java
// Add context at entry point
FilterContext ctx = FilterContext.builder()
    .requestId(UUID.randomUUID().toString())
    .userId(getUserId(request))
    .service("order-service")
    .build();

MDC.putMap(ctx.toMap());
```

## Log Retention

| Hot | Warm | Cold | Delete |
|-----|------|------|--------|
| SSD, full data | Compressed, reduced replicas | Read-only, archive | Purge |
| 0-7 days | 7-30 days | 30-90 days | 90+ days |

## Best Practices

1. Use structured JSON logging
2. Include trace IDs for correlation
3. Implement log levels per service
4. Set up log retention policies
5. Monitor log ingestion rates
6. Use dead letter queues for failures
7. Implement log validation
8. Secure log transport (TLS)
