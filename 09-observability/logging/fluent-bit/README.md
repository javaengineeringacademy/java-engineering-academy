# Fluent Bit

## Overview

Fluent Bit is a lightweight and high-performance log processor and forwarder. It is part of the Fluentd ecosystem, designed for resource-constrained environments like edge computing, containers, and embedded systems.

## Core Concepts

### Input Plugins
Collect logs and metrics from various sources with minimal resource usage.

### Filter Plugins
Process and modify data in-flight with parsers and transformers.

### Output Plugins
Send processed data to multiple destinations with buffering and retry.

### Parsers
Define how to parse unstructured log data into structured fields.

## Architecture

```
Data Sources -> Fluent Bit -> Destinations
                  |
         Input -> Parser -> Filter -> Buffer -> Output
```

### Key Differences from Fluentd
| Feature | Fluent Bit | Fluentd |
|---------|------------|---------|
| Language | C/C++ | Ruby/C |
| Memory | Low (~1MB) | Higher (~40MB) |
| Plugins | 100+ | 500+ |
| Use Case | Edge/Containers | Central aggregation |

## Configuration

### Basic Configuration
```yaml
# fluent-bit.conf
service:
    flush: 1
    log_level: info
    parsers_file: parsers.conf

input:
    - name: tail
      path: /var/log/app/*.log
      parser: json
      tag: app.logs

filter:
    - name: record_modifier
      match: "app.*"
      record:
        hostname: "${HOSTNAME}"

output:
    - name: es
      match: "app.*"
      host: elasticsearch
      port: 9200
      index: app-logs
```

### Docker Configuration
```yaml
# docker-compose.yml
version: '3.8'
services:
  fluent-bit:
    image: fluent/fluent-bit:latest
    volumes:
      - ./fluent-bit.conf:/fluent-bit/etc/fluent-bit.conf
      - ./parsers.conf:/fluent-bit/etc/parsers.conf
      - /var/log:/var/log:ro
    ports:
      - "24224:24224"
    environment:
      - FLUSH_INTERVAL=1
```

### Kubernetes Configuration
```yaml
# DaemonSet
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: fluent-bit
spec:
  selector:
    matchLabels:
      app: fluent-bit
  template:
    metadata:
      labels:
        app: fluent-bit
    spec:
      containers:
      - name: fluent-bit
        image: fluent/fluent-bit:latest
        volumeMounts:
        - name: varlog
          mountPath: /var/log
        - name: config
          mountPath: /fluent-bit/etc/
      volumes:
      - name: varlog
        hostPath:
          path: /var/log
      - name: config
        configMap:
          name: fluent-bit-config
```

## Key Features

### Performance
- Written in C for minimal resource usage
- Asynchronous I/O for high throughput
- Memory-efficient buffering

### Reliability
- Backup output for failover
- Automatic retries with exponential backoff
- Chunk-based buffering for durability

### Flexibility
- Hot reload of configuration
- Custom plugins in C or Go
- Embedded scripting with Lua

## Parser Examples

### JSON Parser
```ini
[PARSER]
    Name        json
    Format      json
    Time_Key    timestamp
    Time_Format %Y-%m-%dT%H:%M:%S.%L%z
```

### Multiline Parser
```ini
[PARSER]
    Name        multiline
    Format      regex
    Regex       ^(?<time>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}) \[(?<level>\w+)\](?<message>(?s).*)
    Time_Key    time
    Time_Format %Y-%m-%d %H:%M:%S
```

## Output Examples

### Elasticsearch Output
```yaml
output:
    - name: es
      match: "app.*"
      host: elasticsearch
      port: 9200
      index: app-logs-%Y.%m.%d
      type: _doc
      logstash_format: true
      logstash_prefix: app-logs
```

### Kafka Output
```yaml
output:
    - name: kafka
      match: "app.*"
      brokers: kafka1:9092,kafka2:9092
      topics: app-logs
      message_key: "${HOSTNAME}"
```

### S3 Output
```yaml
output:
    - name: s3
      match: "app.*"
      bucket: my-log-bucket
      region: us-east-1
      s3_key_format: /logs/$TAG/%Y/%m/%d/$UUID.gz
```

## Best Practices

1. Use parsers to structure unstructured logs
2. Configure buffering for reliability
3. Use tags for flexible log routing
4. Implement filters for log enrichment
5. Monitor Fluent Bit metrics for health
6. Use Kubernetes DaemonSet for container log collection
7. Configure multiple outputs for different destinations
8. Use Lua filters for custom processing logic
