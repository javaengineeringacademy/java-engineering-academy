# ELK Stack - Elasticsearch, Logstash, Kibana

## Overview

The ELK Stack combines Elasticsearch, Logstash, and Kibana for centralized log management, providing collection, processing, storage, and visualization capabilities.

## Architecture

```
Sources -> Logstash/Filebeat -> Elasticsearch -> Kibana
  (Apps)    (Process/Ship)      (Store/Search)   (Visualize)
```

## Logstash Configuration

### Pipeline
```ruby
input {
  beats { port => 5044 }
  tcp { port => 5000; codec => json }
  kafka { bootstrap_servers => "localhost:9092"; topics => ["logs"] }
}

filter {
  json { source => "message" }
  date { match => ["timestamp", "ISO8601"] }
  grok { match => { "message" => "%{LOGLEVEL:level} %{GREEDYDATA:msg}" } }
  geoip { source => "client_ip" }
}

output {
  elasticsearch {
    hosts => ["http://localhost:9200"]
    index => "logs-%{+YYYY.MM.dd}"
  }
}
```

## Filebeat Configuration

```yaml
filebeat.inputs:
- type: log
  paths:
    - /var/log/app/*.log
  json.keys_under_root: true
  fields:
    service: my-service

output.elasticsearch:
  hosts: ["localhost:9200"]
  index: "filebeat-%{+yyyy.MM.dd}"

setup.kibana:
  host: "localhost:5601"
```

## Index Lifecycle Management

```json
PUT _ilm/policy/logs-policy
{
  "policy": {
    "phases": {
      "hot": { "actions": { "rollover": { "max_size": "50gb", "max_age": "1d" } } },
      "warm": { "min_age": "7d", "actions": { "shrink": { "number_of_shards": 1 } } },
      "delete": { "min_age": "90d", "actions": { "delete": {} } }
    }
  }
}
```

## Index Templates

```json
PUT _index_template/logs-template
{
  "index_patterns": ["logs-*"],
  "template": {
    "settings": { "number_of_shards": 3, "index.lifecycle.name": "logs-policy" },
    "mappings": {
      "properties": {
        "@timestamp": { "type": "date" },
        "level": { "type": "keyword" },
        "service": { "type": "keyword" },
        "message": { "type": "text" },
        "trace_id": { "type": "keyword" }
      }
    }
  }
}
```

## Kibana Queries

```
service: my-service AND level: ERROR
trace_id: abc123
@timestamp >= now-1h AND service: my-service
```

## Best Practices

1. Use structured JSON logging for easier parsing
2. Implement ILM to manage index lifecycle
3. Create index templates with proper mappings
4. Use Filebeat for lightweight log shipping
5. Set up Kibana dashboards for monitoring
6. Configure alerts for critical patterns
7. Use ingest pipelines for preprocessing
8. Monitor Elasticsearch cluster health
