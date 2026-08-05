# OpenSearch

## Overview

OpenSearch is a community-driven, open-source search and analytics suite derived from Elasticsearch. It provides distributed search, log analytics, and observability capabilities with full API compatibility.

## Core Concepts

### Indices
Collections of documents with defined mappings that determine how fields are stored and indexed.

### Clusters and Nodes
A cluster is a collection of nodes that together hold data and provide search capabilities. Nodes are individual servers in the cluster.

### Shards and Replicas
Shards partition data across nodes. Replicas provide redundancy and improve search throughput.

## Architecture

```
Applications -> OpenSearch Dashboards -> OpenSearch Cluster
                    |                       |
              Query/Visualize        Nodes/Shards/Replicas
```

### Components
- **OpenSearch** - Search and analytics engine
- **OpenSearch Dashboards** - Visualization and management UI
- **OpenSearch SQL** - SQL interface for querying data
- **OpenSearch Alerting** - Monitor and alert on data changes

## Configuration

### Cluster Setup
```yaml
# opensearch.yml
cluster.name: my-cluster
node.name: node-1
network.host: 0.0.0.0
discovery.seed_hosts: ["node-1", "node-2"]
cluster.initial_master_nodes: ["node-1", "node-2"]
```

### Index Template
```json
PUT _index_template/logs-template
{
  "index_patterns": ["logs-*"],
  "template": {
    "settings": {
      "number_of_shards": 3,
      "number_of_replicas": 1,
      "index.lifecycle.name": "logs-policy"
    },
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

### Index Lifecycle Management
```json
PUT _ilm/policy/logs-policy
{
  "policy": {
    "phases": {
      "hot": {
        "actions": {
          "rollover": {
            "max_size": "50gb",
            "max_age": "1d"
          }
        }
      },
      "warm": {
        "min_age": "7d",
        "actions": {
          "shrink": { "number_of_shards": 1 },
          "forcemerge": { "max_num_segments": 1 }
        }
      },
      "delete": {
        "min_age": "90d",
        "actions": { "delete": {} }
      }
    }
  }
}
```

## Key Features

### Search and Analytics
- Full-text search with relevance scoring
- Aggregations for data analysis
- SQL and PPL query support
- Real-time analytics dashboards

### Log Analytics
- Log ingestion and parsing
- Field-level security
- Anomaly detection
- Alerting on log patterns

### Observability
- Trace analytics for distributed tracing
- Metric analytics for infrastructure monitoring
- Alerting on anomalies and thresholds

## Query Examples

### Log Query
```json
GET logs-*/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "service": "order-service" } },
        { "match": { "level": "ERROR" } }
      ],
      "filter": [
        { "range": { "@timestamp": { "gte": "now-1h" } } }
      ]
    }
  }
}
```

### Aggregation Query
```json
GET logs-*/_search
{
  "size": 0,
  "aggs": {
    "errors_by_service": {
      "terms": { "field": "service" },
      "aggs": {
        "error_rate": {
          "filter": { "term": { "level": "ERROR" } }
        }
      }
    }
  }
}
```

## Best Practices

1. Use index templates for consistent field mappings
2. Implement ILM for automated index lifecycle management
3. Use field-level security for sensitive data
4. Configure appropriate shard and replica counts
5. Use aliases for seamless index reindexing
6. Monitor cluster health and performance
7. Use snapshot and restore for disaster recovery
8. Optimize mappings for search and storage efficiency
