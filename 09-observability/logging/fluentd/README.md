# Fluentd

## Overview

Fluentd is a unified logging layer that collects, processes, and forwards log data from various sources to multiple destinations. It provides a plugin-based architecture for flexible log routing and transformation.

## Core Concepts

### Input Plugins
Collect log data from sources like files, syslog, HTTP, and message queues.

### Filter Plugins
Transform, enrich, or route log events based on conditions.

### Output Plugins
Forward processed logs to destinations like Elasticsearch, S3, or Kafka.

### Buffer Plugins
Handle temporary storage for output plugins to manage backpressure.

## Architecture

```
Sources -> Fluentd -> Destinations
            |
    Input -> Filter -> Buffer -> Output
```

### Plugin Types
- **Input** - Collect data from sources
- **Filter** - Process and transform events
- **Output** - Send data to destinations
- **Buffer** - Temporary storage for outputs
- **Formatter** - Serialize events for output

## Configuration

### Basic Configuration
```xml
# fluent.conf
<source>
  @type forward
  port 24224
  bind 0.0.0.0
</source>

<filter app.**>
  @type record_transformer
  <record>
    hostname "#{Socket.gethostname}"
    service ${record["service"]}
  </record>
</filter>

<match app.**>
  @type elasticsearch
  host elasticsearch
  port 9200
  index_name app-logs
  type_name _doc
  <buffer>
    flush_mode interval
    flush_interval 5s
    chunk_limit_size 2M
    retry_max_interval 30s
    retry_forever true
  </buffer>
</match>
```

### Advanced Configuration
```xml
# Multiple outputs with different routing
<source>
  @type tail
  path /var/log/app/*.log
  pos_file /var/log/fluentd/app.log.pos
  tag app.logs
  <parse>
    @type json
    time_key timestamp
    time_format %Y-%m-%dT%H:%M:%S.%NZ
  </parse>
</source>

<filter app.logs>
  @type grep
  <regexp>
    key level
    pattern /^ERROR|WARN$/
  </regexp>
</filter>

<match app.logs>
  @type copy

  <store>
    @type elasticsearch
    host elasticsearch
    index_name app-logs
  </store>

  <store>
    @type s3
    bucket my-log-bucket
    path logs/
    <buffer>
      flush_interval 1h
    </buffer>
  </store>
</match>
```

## Key Features

### Reliability
- Buffered output with automatic retries
- Backup for failed outputs
- Chunk-based buffering for durability

### Performance
- Multi-threaded processing
- Memory and file-based buffering
- Configurable flush intervals

### Flexibility
- 500+ plugins available
- Custom plugin development in Ruby
- Dynamic configuration reloading

## Plugin Examples

### Tail Plugin
```xml
<source>
  @type tail
  path /var/log/app/error.log
  pos_file /var/log/fluentd/error.log.pos
  tag app.error
  <parse>
    @type multiline
    format /^(?<time>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}) \[(?<level>\w+)\] (?<message>.*)/
    time_format %Y-%m-%d %H:%M:%S
  </parse>
</source>
```

### Record Transformer
```xml
<filter app.**>
  @type record_transformer
  enable_ruby true
  <record>
    hostname "#{Socket.gethostname}"
    environment "#{ENV['DEPLOY_ENV']}"
    version "#{`git rev-parse --short HEAD`.strip}"
  </record>
</filter>
```

### Elasticsearch Output
```xml
<match app.**>
  @type elasticsearch
  host elasticsearch
  port 9200
  index_name app-logs-%Y.%m.%d
  type_name _doc
  include_tag_key true
  tag_key @log_name
  <buffer>
    @type file
    path /var/log/fluentd/buffers/elasticsearch
    flush_mode interval
    flush_interval 10s
    retry_type exponential_backoff
    retry_max_interval 30s
    chunk_limit_size 5M
    total_limit_size 1G
  </buffer>
</match>
```

## Best Practices

1. Use tags for logical log routing
2. Implement buffering for reliability and performance
3. Configure retry policies for fault tolerance
4. Use filters for log enrichment before output
5. Monitor Fluentd metrics for health
6. Use Kubernetes DaemonSet for container log collection
7. Implement log validation with grep filters
8. Use multiple outputs for redundancy and different use cases
