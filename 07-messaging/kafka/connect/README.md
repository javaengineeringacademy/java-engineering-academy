# Kafka Connect

## Kafka Connect, Source/Sink Connectors, and Data Integration

---

## Table of Contents

- [Overview](#overview)
- [Kafka Connect Architecture](#kafka-connect-architecture)
- [Source Connectors](#source-connectors)
- [Sink Connectors](#sink-connectors)
- [Connector Configuration](#connector-configuration)
- [Transforms (SMTs)](#transforms-smts)
- [Error Handling](#error-handling)
- [Performance Tuning](#performance-tuning)
- [Custom Connectors](#custom-connectors)
- [Best Practices](#best-practices)

---

## Overview

Kafka Connect is a framework for streaming data between Kafka and external systems. It provides a scalable, reliable way to move large amounts of data without writing custom integration code.

### Key Features

- **Scalable**: Distributed mode for horizontal scaling
- **Reliable**: Automatic offset management and fault tolerance
- **Extensible**: Rich ecosystem of pre-built connectors
- **Transformation**: Single Message Transforms (SMTs) for data manipulation
- **Monitoring**: Built-in metrics and monitoring

### Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Kafka Connect Cluster                     │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Worker 1   │  │   Worker 2   │  │   Worker 3   │      │
│  │              │  │              │  │              │      │
│  │ ┌──────────┐ │  │ ┌──────────┐ │  │ ┌──────────┐ │      │
│  │ │Connector │ │  │ │Connector │ │  │ │Connector │ │      │
│  │ │  Task    │ │  │ │  Task    │ │  │ │  Task    │ │      │
│  │ └──────────┘ │  │ └──────────┘ │  │ └──────────┘ │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
         │                    │                    │
         ▼                    ▼                    ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│  PostgreSQL  │    │    MySQL     │    │    S3        │
│  Database    │    │   Database   │    │   Bucket     │
└──────────────┘    └──────────────┘    └──────────────┘
```

---

## Kafka Connect Architecture

### Worker Nodes

```
Worker Node:
┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Connector API                      │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                   Task Manager                        │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐          │   │
│  │  │  Task 1  │  │  Task 2  │  │  Task 3  │          │   │
│  │  └──────────┘  └──────────┘  └──────────┘          │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                  Offset Store                        │   │
│  │            (Kafka or Memory)                         │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Component Roles

| Component | Responsibility |
|-----------|---------------|
| Worker | Hosts connectors and tasks, manages offset storage |
| Connector | Defines data source/sink configuration |
| Task | Executes actual data transfer |
| Converter | Handles serialization/deserialization |
| Transformation | Modifies messages in-flight |

---

## Source Connectors

### JDBC Source Connector

```json
{
  "name": "postgres-source",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "connection.url": "jdbc:postgresql://localhost:5432/mydb",
    "connection.user": "postgres",
    "connection.password": "password",
    "table.whitelist": "users,orders",
    "mode": "timestamp",
    "timestamp.column.name": "updated_at",
    "topic.prefix": "db-"
  }
}
```

### File Source Connector

```json
{
  "name": "file-source",
  "config": {
    "connector.class": "org.apache.kafka.connect.file.FileStreamSourceConnector",
    "file": "/var/log/application.log",
    "topic": "application-logs",
    "tasks.max": 1
  }
}
```

### Debezium CDC Connector

```json
{
  "name": "postgres-cdc",
  "config": {
    "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
    "database.hostname": "localhost",
    "database.port": "5432",
    "database.user": "postgres",
    "database.password": "password",
    "database.dbname": "mydb",
    "database.server.name": "postgres",
    "schema.include.list": "public",
    "table.include.list": "public.users,public.orders",
    "plugin.name": "pgoutput",
    "slot.name": "debezium_slot"
  }
}
```

### Source Connector Types

| Type | Use Case |
|------|----------|
| JDBC | Database capture |
| Debezium | CDC from databases |
| File | Log file ingestion |
| Kafka | Topic replication |
| MQTT | IoT data ingestion |
| Twitter | Social media streaming |

---

## Sink Connectors

### JDBC Sink Connector

```json
{
  "name": "postgres-sink",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url": "jdbc:postgresql://localhost:5432/mydb",
    "connection.user": "postgres",
    "connection.password": "password",
    "topics": "user-events",
    "auto.create": true,
    "auto.evolve": true,
    "insert.mode": "upsert",
    "pk.mode": "record_key",
    "pk.fields": "id"
  }
}
```

### S3 Sink Connector

```json
{
  "name": "s3-sink",
  "config": {
    "connector.class": "io.confluent.connect.s3.S3SinkConnector",
    "s3.bucket.name": "my-kafka-data",
    "s3.region": "us-east-1",
    "topics": "user-events",
    "flush.size": 1000,
    "rotate.interval.ms": 3600000,
    "storage.class": "io.confluent.connect.s3.storage.S3Storage",
    "format.class": "io.confluent.connect.s3.format.json.JsonFormat",
    "partition.duration.ms": 86400000,
    "path.format": "'year'=YYYY/'month'=MM/'day'=dd",
    "locale": "en-US",
    "timezone": "UTC"
  }
}
```

### Elasticsearch Sink Connector

```json
{
  "name": "elasticsearch-sink",
  "config": {
    "connector.class": "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector",
    "connection.url": "http://localhost:9200",
    "topics": "user-events",
    "type.name": "_doc",
    "key.ignore": false,
    "schema.ignore": true
  }
}
```

### Sink Connector Types

| Type | Use Case |
|------|----------|
| JDBC | Database loading |
| S3 | Cloud storage |
| Elasticsearch | Search indexing |
| HDFS | Data lake storage |
| MongoDB | Document storage |
| Redis | Cache loading |

---

## Connector Configuration

### Connector Config Format

```json
{
  "name": "my-connector",
  "config": {
    "connector.class": "org.apache.kafka.connect.file.FileStreamSourceConnector",
    "tasks.max": 1,
    "file": "/var/log/application.log",
    "topic": "application-logs"
  }
}
```

### Task Configuration

```json
{
  "tasks": [
    {
      "id": "0",
      "state": "RUNNING",
      "worker_id": "worker1:8083"
    }
  ]
}
```

### Common Configuration

| Property | Description |
|----------|-------------|
| `connector.class` | Fully qualified class name |
| `tasks.max` | Maximum number of tasks |
| `key.converter` | Converter for message keys |
| `value.converter` | Converter for message values |
| `transforms` | List of SMTs to apply |

### Converter Configuration

```json
{
  "key.converter": "org.apache.kafka.connect.storage.StringConverter",
  "value.converter": "org.apache.kafka.connect.json.JsonConverter",
  "value.converter.schemas.enable": true
}
```

### Converter Types

| Converter | Format |
|-----------|--------|
| StringConverter | Plain strings |
| JsonConverter | JSON with optional schema |
| AvroConverter | Avro with Schema Registry |
| ProtobufConverter | Protocol Buffers |

---

## Transforms (SMTs)

### SMT Configuration

```json
{
  "transforms": "route,extractField",
  "transforms.route.type": "org.apache.kafka.connect.transforms.RegexRouter",
  "transforms.route.regex": "([^.]+)\\.([^.]+)\\.([^.]+)",
  "transforms.route.replacement": "$2-$3",
  "transforms.extractField.type": "org.apache.kafka.connect.transforms.ExtractField$Key",
  "transforms.extractField.field": "id"
}
```

### Common SMTs

| SMT | Purpose |
|-----|---------|
| `RegexRouter` | Route messages based on regex |
| `ExtractField` | Extract field from key/value |
| `InsertField` | Insert field into key/value |
| `ReplaceField` | Replace field names |
| `MaskField` | Mask sensitive fields |
| `TimestampRouter` | Add timestamp to topic |
| `ValueToKey` | Convert value to key |
| `Cast` | Type casting |
| `Flatten` | Flatten nested structures |
| `Unflatten` | Unflatten structures |

### RegexRouter Example

```json
{
  "transforms": "route",
  "transforms.route.type": "org.apache.kafka.connect.transforms.RegexRouter",
  "transforms.route.regex": "(.*)\\.(.*)",
  "transforms.route.replacement": "prefix-$2"
}
```

### InsertField Example

```json
{
  "transforms": "addTimestamp",
  "transforms.addTimestamp.type": "org.apache.kafka.connect.transforms.InsertField$Value",
  "transforms.addTimestamp.timestamp.field": "processed_at"
}
```

### MaskField Example

```json
{
  "transforms": "maskSensitive",
  "transforms.maskSensitive.type": "org.apache.kafka.connect.transforms.MaskField$Value",
  "transforms.maskSensitive.fields": "password,ssn,credit_card"
}
```

---

## Error Handling

### Dead Letter Queue

```json
{
  "errors.tolerance": "all",
  "errors.deadletterqueue.topic.name": "dlq-connector",
  "errors.deadletterqueue.topic.replication.factor": 3,
  "errors.deadletterqueue.context.headers.enable": true
}
```

### Error Context Headers

```
Headers added to DLQ messages:
├── connect.errors.tolerance: all
├── connect.errors.stage: key.converter
├── connect.errors.class: org.apache.kafka.connect.errors.DataException
├── connect.errors.message: Unable to convert value
└── connect.errors.stacktrace: [full stack trace]
```

### Retry Configuration

```json
{
  "errors.retry.timeout": 60000,
  "errors.retry.delay.max.ms": 60000,
  "errors.tolerance": "all",
  "errors.deadletterqueue.topic.name": "dlq-connector"
}
```

### Error Handling Strategies

| Strategy | Description |
|----------|-------------|
| `none` | Fail on first error |
| `all` | Tolerate all errors |
| `record` | Tolerate per-record errors |

---

## Performance Tuning

### Task Configuration

```json
{
  "tasks.max": 4,
  "batch.size": 1000,
  "max.batch.size": 1000000,
  "poll.interval.ms": 100
}
```

### Connector Parallelism

```
Task Distribution:

Connector with tasks.max=4:
┌─────────────────────────────────────────────────────────────┐
│  Worker 1: Task 0, Task 1                                   │
│  Worker 2: Task 2, Task 3                                   │
└─────────────────────────────────────────────────────────────┘

Task to Partition Mapping:
Task 0 → Partitions [0, 1]
Task 1 → Partitions [2, 3]
Task 2 → Partitions [4, 5]
Task 3 → Partitions [6, 7]
```

### Performance Settings

| Property | Description |
|----------|-------------|
| `tasks.max` | Number of parallel tasks |
| `batch.size` | Records per batch |
| `max.batch.size` | Bytes per batch |
| `poll.interval.ms` | Time between polls |
| `max.poll.records` | Max records per poll |

### Monitoring

```bash
# Check connector status
curl -s http://localhost:8083/connectors/my-connector/status | jq

# List connectors
curl -s http://localhost:8083/connectors | jq

# Pause connector
curl -X PUT http://localhost:8083/connectors/my-connector/pause

# Resume connector
curl -X PUT http://localhost:8083/connectors/my-connector/resume
```

---

## Custom Connectors

### Connector Interface

```java
public class CustomSourceConnector extends SourceConnector {
    
    private Map<String, String> config;
    
    @Override
    public void start(Map<String, String> props) {
        this.config = props;
    }
    
    @Override
    public Class<? extends Task> taskClass() {
        return CustomSourceTask.class;
    }
    
    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        // Return task configurations
        return Arrays.asList(config);
    }
    
    @Override
    public void stop() {
        // Cleanup
    }
    
    @Override
    public ConfigDef config() {
        return new ConfigDef()
            .define("source.url", Type.STRING, Importance.HIGH, "Source URL")
            .define("topic", Type.STRING, Importance.HIGH, "Target topic");
    }
}
```

### Task Interface

```java
public class CustomSourceTask extends SourceTask {
    
    private String sourceUrl;
    private String topic;
    
    @Override
    public void start(Map<String, String> props) {
        this.sourceUrl = props.get("source.url");
        this.topic = props.get("topic");
    }
    
    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        // Fetch data and create records
        List<SourceRecord> records = new ArrayList<>();
        
        Data data = fetchData();
        SourceRecord record = new SourceRecord(
            null,           // sourcePartition
            null,           // sourceOffset
            topic,          // topic
            null,           // partition
            null,           // keySchema
            data.getId(),   // key
            null,           // valueSchema
            data.getValue() // value
        );
        records.add(record);
        
        return records;
    }
    
    @Override
    public void stop() {
        // Cleanup
    }
}
```

---

## Best Practices

### Configuration

1. **Set tasks.max appropriately** - Match to source parallelism
2. **Use appropriate converters** - Match data format needs
3. **Configure error handling** - Use DLQ for failed messages
4. **Set batch sizes** - Balance throughput vs latency

### Performance

1. **Increase parallelism** - Use more tasks for higher throughput
2. **Batch operations** - Reduce per-record overhead
3. **Use compression** - Reduce network transfer
4. **Monitor metrics** - Track throughput and latency

### Reliability

1. **Use idempotent connectors** - Handle retries safely
2. **Configure dead letter queues** - Capture failed messages
3. **Enable monitoring** - Set up alerts for failures
4. **Test failover** - Verify connector recovery

### Operations

1. **Use REST API** - Automate connector management
2. **Version connectors** - Track configuration changes
3. **Monitor consumer lag** - Track source connector progress
4. **Plan capacity** - Size cluster appropriately

---

## Further Reading

- [Kafka Connect Documentation](https://kafka.apache.org/documentation/#connect)
- [Kafka Connect API](https://kafka.apache.org/30/javadoc/index.html?org/apache/kafka/connect/package-summary.html)
- [Confluent Hub Connectors](https://www.confluent.io/hub/)
