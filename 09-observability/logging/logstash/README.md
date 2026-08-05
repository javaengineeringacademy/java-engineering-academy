# Logstash

## Overview

Logstash is a server-side data processing pipeline that ingests data from multiple sources, transforms it, and sends it to desired output destinations. It is part of the ELK Stack and provides flexible log processing capabilities.

## Core Concepts

### Inputs
Plugins that ingest data from various sources like files, beats, Kafka, and syslog.

### Filters
Plugins that parse, enrich, and transform log events.

### Outputs
Plugins that send processed data to destinations like Elasticsearch, S3, and Kafka.

### Codecs
Plugins that encode or decode data at input/output boundaries.

## Architecture

```
Sources -> Inputs -> Filters -> Outputs -> Destinations
            |
    Codec (Decode/Encode)
```

### Plugin Types
- **Input** - Data ingestion from sources
- **Filter** - Data transformation and enrichment
- **Output** - Data routing to destinations
- **Codec** - Data encoding/decoding

## Configuration

### Basic Pipeline
```ruby
# logstash.conf
input {
  beats {
    port => 5044
  }
  
  tcp {
    port => 5000
    codec => json
  }
}

filter {
  if [type] == "apache" {
    grok {
      match => { "message" => "%{COMBINEDAPACHELOG}" }
    }
    date {
      match => [ "timestamp", "dd/MMM/yyyy:HH:mm:ss Z" ]
    }
    geoip {
      source => "clientip"
    }
  }
  
  if [type] == "app" {
    json {
      source => "message"
    }
    date {
      match => [ "timestamp", "ISO8601" ]
    }
  }
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "%{[@metadata][beat]}-%{+YYYY.MM.dd}"
  }
  
  if [level] == "ERROR" {
    slack {
      url => "https://hooks.slack.com/..."
      message => "Error in %{[service]}: %{[message]}"
    }
  }
}
```

### Advanced Pipeline
```ruby
input {
  kafka {
    bootstrap_servers => "kafka1:9092,kafka2:9092"
    topics => ["app-logs"]
    group_id => "logstash-consumers"
    codec => json
  }
}

filter {
  # Parse nested JSON
  ruby {
    code => "
      event.get('request').each do |k, v|
        event.set('request_' + k, v)
      end
    "
  }
  
  # Enrich with GeoIP
  geoip {
    source => "[request][ip]"
    target => "geoip"
  }
  
  # Add metadata
  ruby {
    code => "
      event.set('processed_at', Time.now.utc.iso8601)
      event.set('pipeline_version', '1.0.0')
    "
  }
  
  # Drop debug logs in production
  if [level] == "DEBUG" and [environment] == "production" {
    drop { }
  }
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "app-logs-%{+YYYY.MM.dd}"
    template_name => "app-logs"
  }
}
```

## Key Features

### Processing Capabilities
- Grok pattern matching for unstructured data
- JSON parsing and field extraction
- GeoIP enrichment
- User agent parsing

### Reliability
- Persistent queues for fault tolerance
- Dead letter queues for failed events
- Automatic retry mechanisms

### Performance
- Pipeline workers for parallel processing
- Persistent batch mode
- Memory queue optimization

## Filter Examples

### Grok Patterns
```ruby
filter {
  grok {
    match => {
      "message" => "%{TIMESTAMP_ISO8601:timestamp} %{LOGLEVEL:level} %{GREEDYDATA:message}"
    }
  }
}
```

### Mutate Operations
```ruby
filter {
  mutate {
    rename => { "hostname" => "host" }
    remove_field => [ "debug", "trace" ]
    add_field => { "environment" => "production" }
    gsub => [ "message", "\n", " " ]
    lowercase => [ "level" ]
    strip => [ "message" ]
  }
}
```

### Date Parsing
```ruby
filter {
  date {
    match => [ "timestamp", "ISO8601", "yyyy-MM-dd HH:mm:ss", "dd/MMM/yyyy:HH:mm:ss Z" ]
    target => "@timestamp"
    timezone => "UTC"
  }
}
```

## Output Examples

### Elasticsearch
```ruby
output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "logs-%{+YYYY.MM.dd}"
    user => "elastic"
    password => "changeme"
    ssl => true
  }
}
```

### Kafka
```ruby
output {
  kafka {
    bootstrap_servers => "kafka1:9092,kafka2:9092"
    topic_id => "processed-logs"
    codec => json
    compression_type => "snappy"
  }
}
```

### S3
```ruby
output {
  s3 {
    bucket => "my-log-bucket"
    region => "us-east-1"
    prefix => "logs/%{+YYYY/MM/dd}/"
    codec => json_lines
  }
}
```

## Best Practices

1. Use persistent queues for reliability
2. Implement proper error handling with dead letter queues
3. Use grok patterns for structured parsing
4. Optimize filter order for performance
5. Monitor pipeline performance with API
6. Use multiple pipelines for different data types
7. Implement log rotation for Logstash logs
8. Use environment variables for configuration flexibility
