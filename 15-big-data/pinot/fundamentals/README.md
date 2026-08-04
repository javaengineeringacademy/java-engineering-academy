# Apache Pinot Fundamentals

## Overview
Pinot is a real-time distributed OLAP datastore designed for serving high-concurrency, low-latency analytics.

## Architecture
- **Broker**: Routes queries to servers
- **Server**: Stores and serves segments
- **Controller**: Manages cluster and schema
- **Minion**: Background processing tasks

## Data Model
- **Dimension**: Categorical columns
- **Metric**: Numeric columns for aggregation
- **Time**: Timestamp column
- **Star-join**: Pre-computed joins

## Table Types
```json
{
  "tableName": "events",
  "tableType": "REALTIME",
  "segmentsConfig": {
    "replication": "2",
    "timeColumnName": "timestamp",
    "schemaName": "events"
  },
  "ingestionConfig": {
    "streamConfigs": {
      "stream.kafka.topic.name": "events",
      "stream.kafka.broker.list": "kafka:9092",
      "stream.kafka.consumer.type": "highLevel"
    }
  }
}
```

## Schema
```json
{
  "schemaName": "events",
  "dimensionFieldSpecs": [
    {"name": "user_id", "dataType": "STRING"},
    {"name": "event_type", "dataType": "STRING"},
    {"name": "page", "dataType": "STRING"}
  ],
  "metricFieldSpecs": [
    {"name": "revenue", "dataType": "DOUBLE"},
    {"name": "count", "dataType": "LONG"}
  ],
  "dateTimeFieldSpecs": [
    {"name": "timestamp", "dataType": "TIMESTAMP", "format": "yyyy-MM-dd HH:mm:ss", "granularity": "DAY"}
  ]
}
```

## Best Practices
1. Design star schemas for join performance
2. Use appropriate indexing (inverted, sorted, bloom)
3. Partition tables by time
4. Monitor query latency and throughput
5. Use realtime tables for streaming data
