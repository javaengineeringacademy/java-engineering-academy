# Change Data Capture (CDC)

## Table of Contents

- [Overview](#overview)
- [CDC Concepts](#cdc-concepts)
- [CDC Methods](#cdc-methods)
- [CDC Tools](#cdc-tools)
- [CDC Patterns](#cdc-patterns)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

Change Data Capture (CDC) is a design pattern that identifies and captures
changes made to data in a database, then delivers those changes in real-time
to downstream systems. CDC enables real-time data synchronization and
event-driven architectures.

### Key Characteristics

- **Real-time capture**: Capture changes as they occur
- **Minimal impact**: Low overhead on source systems
- **Reliable delivery**: Ensure all changes are captured
- **Order preservation**: Maintain change order
- **Exactly-once semantics**: Prevent duplicate processing

### When to Use CDC

- Real-time data synchronization
- Event-driven architectures
- Data lake ingestion
- Cache invalidation
- Search index updates

### CDC vs Batch Extract

| Feature | CDC | Batch Extract |
|---------|-----|---------------|
| Latency | Real-time | Minutes to hours |
| Source Impact | Minimal | High |
| Data Freshness | Current | Delayed |
| Complexity | Higher | Lower |
| Cost | Higher | Lower |

---

## CDC Concepts

### Change Events

```python
# Change event structure
change_event = {
    "op": "u",  # Operation: c=create, u=update, d=delete, r=read (snapshot)
    "ts_ms": 1704067200000,  # Timestamp in milliseconds
    "before": {  # Previous state (for updates and deletes)
        "id": 1,
        "name": "Alice",
        "email": "alice@example.com"
    },
    "after": {  # New state (for creates and updates)
        "id": 1,
        "name": "Alice Smith",
        "email": "alice.smith@example.com"
    },
    "source": {
        "db": "mydb",
        "table": "users",
        "lsn": 12345678,  # Log sequence number
        "txId": 123  # Transaction ID
    }
}
```

### Operation Types

```python
# Create (insert)
create_event = {
    "op": "c",
    "before": None,
    "after": {"id": 1, "name": "Alice"}
}

# Update
update_event = {
    "op": "u",
    "before": {"id": 1, "name": "Alice"},
    "after": {"id": 1, "name": "Alice Smith"}
}

# Delete
delete_event = {
    "op": "d",
    "before": {"id": 1, "name": "Alice Smith"},
    "after": None
}

# Snapshot (initial read)
snapshot_event = {
    "op": "r",
    "before": None,
    "after": {"id": 1, "name": "Alice Smith"}
}
```

### Log-Based CDC

```sql
-- PostgreSQL WAL (Write-Ahead Log)
-- Changes recorded in WAL before being applied to database

-- MySQL binlog
-- Changes recorded in binary log

-- SQL Server Change Data Capture
-- Uses transaction log to capture changes
EXEC sys.sp_cdc_enable_table
    @source_schema = N'dbo',
    @source_name = N'users',
    @role_name = NULL,
    @supports_net_changes = 1;
```

---

## CDC Methods

### Log-Based CDC

```python
# Read from database transaction log
# Lowest impact on source system
# Captures all changes including deletes

# Debezium example
{
    "name": "users-connector",
    "config": {
        "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
        "database.hostname": "localhost",
        "database.port": "5432",
        "database.user": "user",
        "database.password": "password",
        "database.dbname": "mydb",
        "database.server.name": "myserver",
        "table.include.list": "public.users",
        "plugin.name": "pgoutput",
        "slot.name": "debezium_slot"
    }
}
```

### Query-Based CDC

```python
# Query database for changes
# Higher impact on source system
# May miss deletes

# Using timestamp column
def query_based_cdc(last_watermark):
    query = f"""
        SELECT * FROM users
        WHERE modified_at > '{last_watermark}'
        ORDER BY modified_at
    """
    return execute_query(query)

# Using version column
def query_based_cdc_version(last_version):
    query = f"""
        SELECT * FROM users
        WHERE version > {last_version}
        ORDER BY version
    """
    return execute_query(query)
```

### Trigger-Based CDC

```sql
-- Create trigger to capture changes
CREATE TABLE users_audit (
    id INT,
    name VARCHAR(100),
    email VARCHAR(100),
    operation VARCHAR(10),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
);

CREATE OR REPLACE FUNCTION capture_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO users_audit (id, name, email, operation)
        VALUES (NEW.id, NEW.name, NEW.email, 'INSERT');
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO users_audit (id, name, email, operation)
        VALUES (NEW.id, NEW.name, NEW.email, 'UPDATE');
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO users_audit (id, name, email, operation)
        VALUES (OLD.id, OLD.name, OLD.email, 'DELETE');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER users_changes
AFTER INSERT OR UPDATE OR DELETE ON users
FOR EACH ROW EXECUTE FUNCTION capture_changes();
```

### Timestamp-Based CDC

```python
# Using modified_at timestamp
def timestamp_cdc(spark, source_path, target_path, watermark_path):
    """Timestamp-based CDC"""
    # Get last watermark
    last_watermark = get_watermark(watermark_path)

    # Query for changes
    changes = spark.read.format("jdbc") \
        .option("url", source_url) \
        .option("dbtable", f"(SELECT * FROM users WHERE modified_at > '{last_watermark}') as changes") \
        .load()

    # Process changes
    if changes.count() > 0:
        # Apply changes to target
        apply_changes(changes, target_path)

        # Update watermark
        new_watermark = changes.agg(max("modified_at")).collect()[0][0]
        save_watermark(watermark_path, new_watermark)
```

---

## CDC Tools

### Debezium

```json
{
    "name": "users-connector",
    "config": {
        "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
        "database.hostname": "localhost",
        "database.port": "5432",
        "database.user": "user",
        "database.password": "password",
        "database.dbname": "mydb",
        "database.server.name": "myserver",
        "table.include.list": "public.users",
        "plugin.name": "pgoutput",
        "slot.name": "debezium_slot",
        "publication.name": "dbz_publication",
        "snapshot.mode": "initial"
    }
}
```

### AWS DMS (Database Migration Service)

```python
# AWS DMS configuration
dms_config = {
    "SourceEndpoint": {
        "EngineName": "postgres",
        "ServerName": "source-db.example.com",
        "Port": 5432,
        "DatabaseName": "mydb",
        "Username": "user",
        "Password": "password"
    },
    "TargetEndpoint": {
        "EngineName": "kinesis",
        "KinesisSettings": {
            "StreamArn": "arn:aws:kinesis:us-east-1:123456789012:stream/my-stream",
            "ServiceAccessRoleArn": "arn:aws:iam::123456789012:role/dms-role"
        }
    },
    "ReplicationTask": {
        "MigrationType": "full-load-and-cdc",
        "TableMappings": {
            "rules": [
                {
                    "rule-type": "selection",
                    "rule-id": "1",
                    "rule-name": "users",
                    "object-locator": {
                        "schema-name": "public",
                        "table-name": "users"
                    },
                    "rule-action": "include"
                }
            ]
        }
    }
}
```

### GoldenGate

```sql
-- GoldenGate extract
EXTRACT extusers
USERIDALIAS ogguser
EXTTRAIL ./dirdat/et
TABLE public.users;

-- GoldenGate replicate
REPLICAT repusers
USERIDALIAS ogguser
MAP public.users, TARGET public.users;
```

### Maxwell's Daemon

```bash
# Start Maxwell
bin/maxwell --user=user --password=password --host=localhost \
    --producer=kinesis --kinesis_stream=cdc-stream \
    --filter="include: mydb.users"
```

---

## CDC Patterns

### Real-Time Sync Pattern

```python
# Real-time synchronization using CDC
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.appName("CDCSync").getOrCreate()

# Read CDC stream from Kafka
cdc_stream = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "dbserver1.public.users") \
    .load()

# Parse CDC events
parsed_stream = cdc_stream \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), cdc_schema).alias("data")) \
    .select("data.*")

# Apply changes to target
def apply_cdc_changes(batch_df, batch_id):
    """Apply CDC changes to target table"""
    from delta import DeltaTable

    # Load target table
    target_table = DeltaTable.forPath(spark, "s3://target/users/")

    # Apply changes based on operation type
    creates = batch_df.filter(col("op") == "c").select("after.*")
    updates = batch_df.filter(col("op") == "u").select("after.*")
    deletes = batch_df.filter(col("op") == "d").select("before.*")

    # Insert creates
    if creates.count() > 0:
        target_table.alias("target").merge(
            creates.alias("source"),
            "target.id = source.id"
        ).whenNotMatchedInsertAll().execute()

    # Update updates
    if updates.count() > 0:
        target_table.alias("target").merge(
            updates.alias("source"),
            "target.id = source.id"
        ).whenMatchedUpdateAll().execute()

    # Delete deletes
    if deletes.count() > 0:
        target_table.alias("target").merge(
            deletes.alias("source"),
            "target.id = source.id"
        ).whenMatchedDelete().execute()

# Write stream
query = parsed_stream.writeStream \
    .foreachBatch(apply_cdc_changes) \
    .outputMode("update") \
    .option("checkpointLocation", "s3://checkpoints/cdc-sync") \
    .start()

query.awaitTermination()
```

### Event Sourcing Pattern

```python
# Event sourcing with CDC
class EventStore:
    def __init__(self):
        self.events = []

    def append(self, event):
        """Append event to store"""
        self.events.append(event)

    def get_events(self, aggregate_id):
        """Get events for aggregate"""
        return [e for e in self.events if e.aggregate_id == aggregate_id]

    def get_events_since(self, timestamp):
        """Get events since timestamp"""
        return [e for e in self.events if e.timestamp >= timestamp]

# CDC as event source
def cdc_to_event_store(cdc_stream, event_store):
    """Convert CDC events to event store"""
    for event in cdc_stream:
        event_store.append(event)
```

### Data Lake Ingestion Pattern

```python
# CDC to data lake
def cdc_to_data_lake(spark, cdc_stream, target_path):
    """Ingest CDC events to data lake"""

    def process_batch(batch_df, batch_id):
        """Process batch of CDC events"""
        # Parse events
        events = batch_df.select(
            col("op").alias("operation"),
            col("ts_ms").alias("timestamp"),
            col("before").alias("previous_state"),
            col("after").alias("current_state"),
            col("source").alias("source_info")
        )

        # Write to data lake
        events.write.mode("append") \
            .partitionBy("operation") \
            .parquet(target_path)

    # Write stream
    query = cdc_stream.writeStream \
        .foreachBatch(process_batch) \
        .outputMode("append") \
        .option("checkpointLocation", f"{target_path}_checkpoint") \
        .start()

    return query
```

---

## Best Practices

### Event Design

1. **Include operation type**: Clearly indicate create, update, delete
2. **Include timestamps**: Event time and processing time
3. **Include before/after states**: For updates and deletes
4. **Include source metadata**: Database, table, transaction ID

### Processing

1. **Handle duplicates**: Use idempotent processing
2. **Maintain order**: Process events in order
3. **Handle late events**: Use watermarks for late data
4. **Implement dead letter queues**: Capture failed events

### Monitoring

1. **Track CDC lag**: Monitor delay between source and target
2. **Monitor event rates**: Track events processed per second
3. **Alert on anomalies**: Set up alerts for unusual patterns
4. **Log processing details**: For debugging and auditing

### Performance

1. **Batch processing**: Process events in batches
2. **Parallel processing**: Use multiple consumers
3. **Optimize serialization**: Use efficient formats
4. **Monitor resource usage**: Track CPU, memory, storage

---

## References

- [Debezium Documentation](https://debezium.io/documentation/)
- [AWS DMS Documentation](https://docs.aws.amazon.com/dms/)
- [Change Data Capture Patterns](https://www.oreilly.com/library/view/change-data-capture/9781492028161/)
- [CDC in Practice](https://www.oreilly.com/library/view/cdc-in-practice/9781492028161/)
- [Real-Time Data Integration](https://www.oreilly.com/library/view/real-time-data-integration/9781492028161/)
