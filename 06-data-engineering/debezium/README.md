# Debezium

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Connectors](#connectors)
- [Configuration](#configuration)
- [Data Formats](#data-formats)
- [Transformations](#transformations)
- [Monitoring](#monitoring)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

Debezium is a distributed platform for change data capture (CDC) built on
Apache Kafka. It captures row-level changes in databases and streams them
to Kafka topics, enabling real-time data synchronization and event-driven
architectures.

### Key Characteristics

- **Log-based CDC**: Reads database transaction logs
- **Low impact**: Minimal overhead on source systems
- **Reliable**: Guarantees exactly-once delivery
- **Scalable**: Distributed processing with Kafka
- **Multi-database**: Supports MySQL, PostgreSQL, MongoDB, and more

### When to Use Debezium

- Real-time data synchronization
- Event-driven architectures
- Data lake ingestion
- Cache invalidation
- Search index updates

### Debezium vs Other CDC Tools

| Feature | Debezium | AWS DMS | GoldenGate |
|---------|----------|---------|------------|
| Open Source | Yes | No | No |
| Log-Based | Yes | Yes | Yes |
| Multi-DB | Yes | Yes | Yes |
| Kafka Native | Yes | No | No |
| Cost | Free | Paid | Paid |

---

## Architecture

### Debezium Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Debezium Architecture                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Source Databases                                                    │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │  MySQL   │ │PostgreSQL│ │ MongoDB  │ │ SQL Server│              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
│                           │                                          │
│  Debezium Connectors      │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │  MySQL   │ │PostgreSQL│ │ MongoDB  │ │ SQL Server│       │  │
│  │  │Connector │ │Connector │ │Connector │ │Connector │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Kafka Connect            │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐                                                  │  │
│  │  │  Kafka   │                                                  │  │
│  │  │ Connect  │                                                  │  │
│  │  └──────────┘                                                  │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Kafka Broker            │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐                                                  │  │
│  │  │  Kafka   │                                                  │  │
│  │  │ Broker   │                                                  │  │
│  │  └──────────┘                                                  │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Consumers               │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │  Spark   │ │  Flink   │ │  Custom  │ │  KSQL    │       │  │
│  │  │          │ │          │ │Consumer  │ │          │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Components

1. **Source Connector**: Captures changes from source database
2. **Kafka Connect**: Distributed platform for connectors
3. **Kafka Broker**: Message broker for event streaming
4. **Schema Registry**: Manages event schemas
5. **Consumer**: Processes captured events

---

## Connectors

### PostgreSQL Connector

```json
{
    "name": "postgresql-connector",
    "config": {
        "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
        "database.hostname": "localhost",
        "database.port": "5432",
        "database.user": "debezium",
        "database.password": "password",
        "database.dbname": "mydb",
        "database.server.name": "myserver",
        "plugin.name": "pgoutput",
        "slot.name": "debezium_slot",
        "publication.name": "dbz_publication",
        "table.include.list": "public.users,public.orders",
        "snapshot.mode": "initial",
        "heartbeat.interval.ms": "10000"
    }
}
```

### MySQL Connector

```json
{
    "name": "mysql-connector",
    "config": {
        "connector.class": "io.debezium.connector.mysql.MySqlConnector",
        "database.hostname": "localhost",
        "database.port": "3306",
        "database.user": "debezium",
        "database.password": "password",
        "database.server.id": "184054",
        "database.server.name": "myserver",
        "database.include.list": "mydb",
        "table.include.list": "mydb.users,mydb.orders",
        "database.history.kafka.bootstrap.servers": "localhost:9092",
        "database.history.kafka.topic": "schema-changes.mydb"
    }
}
```

### MongoDB Connector

```json
{
    "name": "mongodb-connector",
    "config": {
        "connector.class": "io.debezium.connector.mongodb.MongoDbConnector",
        "mongodb.connection.mode": "replica_set",
        "mongodb.connectionstring": "mongodb://localhost:27017",
        "database.include.list": "mydb",
        "collection.include.list": "mydb.users,mydb.orders",
        "mongodb.name": "myserver"
    }
}
```

### SQL Server Connector

```json
{
    "name": "sqlserver-connector",
    "config": {
        "connector.class": "io.debezium.connector.sqlserver.SqlServerConnector",
        "database.hostname": "localhost",
        "database.port": "1433",
        "database.user": "sa",
        "database.password": "password",
        "database.names": "mydb",
        "database.server.name": "myserver",
        "table.include.list": "dbo.users,dbo.orders"
    }
}
```

---

## Configuration

### Connector Configuration

```properties
# PostgreSQL connector configuration
connector.class=io.debezium.connector.postgresql.PostgresConnector
database.hostname=localhost
database.port=5432
database.user=debezium
database.password=password
database.dbname=mydb
database.server.name=myserver
plugin.name=pgoutput
slot.name=debezium_slot
publication.name=dbz_publication
table.include.list=public.users
snapshot.mode=initial
heartbeat.interval.ms=10000
```

### Kafka Connect Configuration

```properties
# Kafka Connect configuration
bootstrap.servers=localhost:9092
group.id=debezium-connect
key.converter=io.confluent.connect.avro.AvroConverter
value.converter=io.confluent.connect.avro.AvroConverter
key.converter.schema.registry.url=http://localhost:8081
value.converter.schema.registry.url=http://localhost:8081
```

### Transform Configuration

```json
{
    "name": "users-connector",
    "config": {
        "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
        "transforms": "unwrap,route",
        "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState",
        "transforms.route.type": "org.apache.kafka.connect.transforms.RegexRouter",
        "transforms.route.regex": "([^.]+)\\.([^.]+)\\.([^.]+)",
        "transforms.route.replacement": "cdc-$3"
    }
}
```

---

## Data Formats

### JSON Format

```json
{
    "op": "u",
    "ts_ms": 1704067200000,
    "before": {
        "id": 1,
        "name": "Alice",
        "email": "alice@example.com"
    },
    "after": {
        "id": 1,
        "name": "Alice Smith",
        "email": "alice.smith@example.com"
    },
    "source": {
        "version": "2.4.0.Final",
        "connector": "postgresql",
        "name": "myserver",
        "ts_ms": 1704067200000,
        "snapshot": "false",
        "db": "mydb",
        "sequence": "[\"12345678\",\"12345679\"]",
        "schema": "public",
        "table": "users",
        "txId": 123,
        "lsn": 12345678
    },
    "transaction": null
}
```

### Avro Format

```json
{
    "type": "record",
    "name": "Envelope",
    "namespace": "myserver.public.users",
    "fields": [
        {
            "name": "op",
            "type": "string"
        },
        {
            "name": "ts_ms",
            "type": "long"
        },
        {
            "name": "before",
            "type": ["null", {
                "type": "record",
                "name": "Value",
                "fields": [
                    {"name": "id", "type": "int"},
                    {"name": "name", "type": "string"},
                    {"name": "email", "type": "string"}
                ]
            }]
        },
        {
            "name": "after",
            "type": ["null", "Value"]
        }
    ]
}
```

### CloudEvents Format

```json
{
    "specversion": "1.0",
    "type": "io.debezium.postgresql.change",
    "source": "myserver",
    "id": "12345678",
    "time": "2024-01-01T00:00:00Z",
    "datacontenttype": "application/json",
    "data": {
        "op": "u",
        "before": {"id": 1, "name": "Alice"},
        "after": {"id": 1, "name": "Alice Smith"}
    }
}
```

---

## Transformations

### Single Message Transforms (SMTs)

```json
{
    "transforms": "unwrap,route,mask",
    "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState",
    "transforms.route.type": "org.apache.kafka.connect.transforms.RegexRouter",
    "transforms.route.regex": "([^.]+)\\.([^.]+)\\.([^.]+)",
    "transforms.route.replacement": "cdc-$3",
    "transforms.mask.type": "org.apache.kafka.connect.transforms.MaskField$Value",
    "transforms.mask.fields": "email,phone"
}
```

### Custom Transformations

```java
// Custom SMT
public class CustomTransform implements Transformation<SinkRecord> {

    @Override
    public SinkRecord apply(SinkRecord record) {
        // Custom transformation logic
        Struct value = (Struct) record.value();

        // Modify value
        value.put("custom_field", "custom_value");

        return new SinkRecord(
            record.topic(),
            record.kafkaPartition(),
            record.keySchema(),
            record.key(),
            record.valueSchema(),
            value,
            record.timestamp()
        );
    }
}
```

### Message Transformations

```python
# Transform Debezium events
def transform_event(event):
    """Transform Debezium event"""
    # Extract operation
    op = event.get("op")

    # Extract data
    if op in ["c", "u"]:
        data = event.get("after")
    elif op == "d":
        data = event.get("before")
    else:
        data = None

    # Add metadata
    transformed = {
        "operation": op,
        "timestamp": event.get("ts_ms"),
        "data": data,
        "source": event.get("source", {}).get("table")
    }

    return transformed
```

---

## Monitoring

### Metrics

```python
# Debezium metrics
metrics = {
    "connector_status": "RUNNING",
    "replication_slot_lag": 1000,
    "events_processed": 1000000,
    "events_filtered": 10000,
    "batch_size": 1024,
    "connection_pool_active": 5,
    "connection_pool_idle": 5
}

# Kafka Connect metrics
kafka_metrics = {
    "connector_active_tasks": 1,
    "connector_failed_tasks": 0,
    "connector_paused_tasks": 0,
    "connector_restart_count": 0
}
```

### Monitoring Setup

```python
# Monitor Debezium with Prometheus
from prometheus_client import Counter, Gauge, start_http_server

# Define metrics
events_processed = Counter('debezium_events_processed_total', 'Total events processed')
replication_lag = Gauge('debezium_replication_lag', 'Replication lag in ms')

# Update metrics
def update_metrics(event):
    events_processed.inc()
    replication_lag.set(event.get("ts_ms") - event.get("source", {}).get("ts_ms"))

# Start metrics server
start_http_server(8000)
```

### Alerting

```python
# Alert on replication lag
def check_replication_lag(lag_ms, threshold_ms=60000):
    """Check replication lag and alert if exceeds threshold"""
    if lag_ms > threshold_ms:
        send_alert(f"Replication lag exceeded threshold: {lag_ms}ms")
        return True
    return False

# Alert on connector failure
def check_connector_status(status):
    """Check connector status and alert on failure"""
    if status != "RUNNING":
        send_alert(f"Connector status: {status}")
        return True
    return False
```

---

## Best Practices

### Configuration

1. **Use snapshot.mode=initial**: For initial data load
2. **Enable heartbeats**: For monitoring replication lag
3. **Configure table.include.list**: Only capture needed tables
4. **Use appropriate plugin.name**: For PostgreSQL (pgoutput recommended)

### Performance

1. **Batch processing**: Process events in batches
2. **Parallel consumers**: Use multiple consumer instances
3. **Optimize serialization**: Use Avro with schema registry
4. **Monitor lag**: Track replication lag and adjust

### Reliability

1. **Use exactly-once semantics**: Configure offset tracking
2. **Implement dead letter queues**: Capture failed events
3. **Monitor connector health**: Set up alerts for failures
4. **Test failover**: Ensure high availability

### Security

1. **Use SSL/TLS**: Encrypt connections
2. **Implement authentication**: Use strong credentials
3. **Restrict access**: Use least privilege principle
4. **Encrypt sensitive data**: Use SMTs to mask PII

---

## Examples

### Complete Debezium Setup

```bash
# Start Kafka Connect
docker run -d --name kafka-connect \
    -p 8083:8083 \
    -e GROUP_ID=1 \
    -e CONFIG_STORAGE_TOPIC=my_connect_configs \
    -e OFFSET_STORAGE_TOPIC=my_connect_offsets \
    -e STATUS_STORAGE_TOPIC=my_connect_statuses \
    debezium/connect:2.4

# Register PostgreSQL connector
curl -X POST http://localhost:8083/connectors \
    -H "Content-Type: application/json" \
    -d '{
        "name": "users-connector",
        "config": {
            "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
            "database.hostname": "postgres",
            "database.port": "5432",
            "database.user": "debezium",
            "database.password": "password",
            "database.dbname": "mydb",
            "database.server.name": "myserver",
            "plugin.name": "pgoutput",
            "table.include.list": "public.users"
        }
    }'

# Check connector status
curl http://localhost:8083/connectors/users-connector/status
```

### Processing Debezium Events

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.appName("DebeziumProcessing").getOrCreate()

# Read from Kafka
df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "myserver.public.users") \
    .load()

# Parse Debezium events
parsed = df \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*")

# Process events
def process_batch(batch_df, batch_id):
    """Process batch of Debezium events"""
    # Filter by operation type
    creates = batch_df.filter(col("op") == "c")
    updates = batch_df.filter(col("op") == "u")
    deletes = batch_df.filter(col("op") == "d")

    # Process each operation type
    if creates.count() > 0:
        process_creates(creates)
    if updates.count() > 0:
        process_updates(updates)
    if deletes.count() > 0:
        process_deletes(deletes)

# Write stream
query = parsed.writeStream \
    .foreachBatch(process_batch) \
    .outputMode("update") \
    .start()

query.awaitTermination()
```

---

## References

- [Debezium Documentation](https://debezium.io/documentation/)
- [Debezium PostgreSQL Connector](https://debezium.io/documentation/connectors/postgresql/)
- [Debezium MySQL Connector](https://debezium.io/documentation/connectors/mysql/)
- [Debezium MongoDB Connector](https://debezium.io/documentation/connectors/mongodb/)
- [Kafka Connect Documentation](https://kafka.apache.org/documentation/connect/)
- [Change Data Capture with Debezium](https://www.oreilly.com/library/view/change-data-capture/9781492028161/)
