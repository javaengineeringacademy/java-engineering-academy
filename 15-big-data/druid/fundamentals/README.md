# Apache Druid Fundamentals

## Overview
Druid is a real-time analytics database designed for fast slice-and-dice queries on large datasets.

## Architecture
- **Historical**: Stores and serves historical data
- **MiddleManager**: Ingests real-time data
- **Broker**: Routes queries
- **Coordinator**: Manages data retention
- **Overlord**: Manages ingestion tasks
- **Router**: HTTP load balancer

## Data Model
- **Dimension**: String columns for grouping/filtering
- **Metric**: Numeric columns for aggregation
- **Timestamp**: Primary time column
- **Segment**: Data partitioned by time

## Ingestion
```json
{
  "type": "kafka",
  "dataSchema": {
    "dataSource": "events",
    "timestampSpec": {"column": "timestamp", "format": "iso"},
    "dimensionsSpec": {
      "dimensions": ["user_id", "event_type", "page", "country"]
    },
    "metricsSpec": [
      {"type": "count", "name": "count"},
      {"type": "doubleSum", "name": "revenue", "fieldName": "revenue"}
    ]
  },
  "ioConfig": {
    "topic": "events-topic",
    "consumerProperties": {"bootstrap.servers": "kafka:9092"}
  }
}
```

## Best Practices
1. Design schemas for query patterns
2. Use appropriate rollup granularity
3. Configure retention based on data age
4. Monitor segment count and size
5. Use lookup tables for dimension enrichment
