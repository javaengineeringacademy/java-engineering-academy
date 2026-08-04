# Structured Streaming

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Input Sources](#input-sources)
- [Output Modes](#output-modes)
- [Window Operations](#window-operations)
- [Watermarking](#watermarking)
- [State Management](#state-management)
- [Streaming Joins](#streaming-joins)
- [Fault Tolerance](#fault-tolerance)
- [Performance Optimization](#performance-optimization)
- [Monitoring](#monitoring)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

Structured Streaming is a scalable, fault-tolerant stream processing engine
built on Spark SQL. It allows you to express streaming computations as
standard batch-like queries on static data, enabling the same DataFrame/Dataset
API for both batch and streaming.

### Key Characteristics

- **Unified API**: Same code for batch and streaming
- **Event-time processing**: Built-in support for event-time semantics
- **Exactly-once semantics**: Fault tolerance with checkpointing
- **Low latency**: Millisecond-level processing with continuous mode
- **End-to-end exactly-once**: With idempotent sinks

### When to Use Structured Streaming

- Real-time analytics and dashboards
- Continuous ETL pipelines
- Event-driven applications
- Real-time machine learning
- IoT data processing

### Structured vs DStream

| Feature | Structured Streaming | DStream |
|---------|---------------------|---------|
| API | DataFrame/Dataset | RDD |
| Processing | Micro-batch + Continuous | Micro-batch only |
| Latency | Milliseconds (continuous) | Seconds |
| Event Time | Built-in | Manual |
| State Management | Built-in operators | Manual |
| Fault Tolerance | Automatic | Manual checkpointing |

---

## Core Concepts

### Input Tables and Output Tables

```
┌──────────────────────────────────────────────────────────────┐
│                  Structured Streaming                        │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  Input Table ──► Query ──► Output Table                     │
│  (Unbounded)     (Processing)   (Result)                    │
│                                                              │
│  ┌─────────┐     ┌─────────┐     ┌─────────┐              │
│  │ Input   │ ──► │  Query  │ ──► │ Output  │              │
│  │ Table   │     │         │     │ Table   │              │
│  │         │     │ Select  │     │         │              │
│  │ (Append)│     │ Filter  │     │ (Append)│              │
│  │ (Update)│     │ Aggregate│    │ (Update)│              │
│  │ (Complete)   │ Join    │     │ (Complete)             │
│  └─────────┘     └─────────┘     └─────────┘              │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### Processing Model

```python
# Conceptual model
# 1. New data arrives in input table
# 2. Query processes the data
# 3. Results written to output table
# 4. Repeat

# Actual implementation
# Data processed in micro-batches or continuous mode
```

### Output Modes

```python
# Append mode: Only new rows added to output table
# - Suitable for queries without aggregations
# - Only new rows are written to sink

# Complete mode: Entire result table is written to sink
# - Suitable for aggregation queries
# - Complete result table is written to sink

# Update mode: Only rows that were updated in the result table
# - Suitable for aggregation queries
# - Only changed rows are written to sink
```

---

## Input Sources

### File Source

```python
# Read from files
df = spark \
    .readStream \
    .format("csv") \
    .option("path", "/data/input") \
    .option("maxFilesPerTrigger", 100) \
    .schema(schema) \
    .load()

# Monitor directory for new files
df = spark \
    .readStream \
    .text("/data/input")

# JSON files
df = spark \
    .readStream \
    .json("/data/input")
```

### Kafka Source

```python
# Read from Kafka
df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "topic1,topic2") \
    .option("startingOffsets", "earliest") \
    .option("endingOffsets", "latest") \
    .option("failOnDataLoss", "false") \
    .load()

# Parse Kafka messages
parsed = df \
    .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*")
```

### Socket Source (Testing Only)

```python
# Read from socket (testing only)
df = spark \
    .readStream \
    .format("socket") \
    .option("host", "localhost") \
    .option("port", 9999) \
    .load()
```

### Rate Source (Testing)

```python
# Generate test data
df = spark \
    .readStream \
    .format("rate") \
    .option("rowsPerSecond", 1000) \
    .option("rampUpTime", 0) \
    .load()
```

### Custom Sources

```python
# Create custom source
class CustomSource:
    def __init__(self, options):
        self.options = options

    def get_batch(self, start, end):
        # Return batch DataFrame for given offset range
        pass

# Register custom source
spark.sparkContext._jsc.sparkSession().catalog().registerSource(
    "custom", "com.example.CustomSourceProvider"
)
```

---

## Output Modes

### Append Mode

```python
# Only new rows are written
query = df.writeStream \
    .outputMode("append") \
    .format("console") \
    .start()

# Suitable for:
# - Queries without aggregations
# - Filter, map, flatMap operations
# - Window operations with watermarking
```

### Complete Mode

```python
# Entire result table is written
query = df.writeStream \
    .outputMode("complete") \
    .format("console") \
    .start()

# Suitable for:
# - Aggregation queries
# - GROUP BY operations
# - Window aggregations
```

### Update Mode

```python
# Only changed rows are written
query = df.writeStream \
    .outputMode("update") \
    .format("console") \
    .start()

# Suitable for:
# - Aggregation queries
# - Queries with filtering after aggregation
# - Most aggregation scenarios
```

### Output Mode Selection

```python
# Append mode
df.writeStream.outputMode("append")  # New rows only

# Complete mode
df.writeStream.outputMode("complete")  # Full result table

# Update mode
df.writeStream.outputMode("update")  # Changed rows only

# Selection guide:
# - No aggregation → append
# - Aggregation with no filter → complete
# - Aggregation with filter → update
```

---

## Window Operations

### Tumbling Windows

```python
from pyspark.sql.functions import window, col

# Tumbling window (non-overlapping)
windowed_counts = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "10 minutes"),  # 10-minute tumbling window
        "event_type"
    ) \
    .count()

# Output
# ┌─────────────────────────────────┐
# │ Window                          │
# ├─────────────────────────────────┤
# │ [2024-01-01 00:00, 2024-01-01  │
# │  00:10]                         │
# │ [2024-01-01 00:10, 2024-01-01  │
# │  00:20]                         │
# └─────────────────────────────────┘
```

### Sliding Windows

```python
# Sliding window (overlapping)
windowed_counts = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "10 minutes", "5 minutes"),  # 10-min window, 5-min slide
        "event_type"
    ) \
    .count()

# Output
# ┌─────────────────────────────────┐
# │ Window                          │
# ├─────────────────────────────────┤
# │ [2024-01-01 00:00, 2024-01-01  │
# │  00:10]                         │
# │ [2024-01-01 00:05, 2024-01-01  │
# │  00:15]                         │
# │ [2024-01-01 00:10, 2024-01-01  │
# │  00:20]                         │
# └─────────────────────────────────┘
```

### Session Windows

```python
# Session windows (variable length)
# Custom implementation using flatMapGroupsWithState

from pyspark.sql.streaming import GroupState, GroupStateTimeout

def session_window_fn(user_id, events, state: GroupState):
    if state.hasTimedOut:
        # Emit final session
        session = state.get
        state.remove()
        return [session]
    elif state.exists:
        # Update existing session
        session = state.get
        session.extend(events)
        state.update(session)
        state.setTimeoutDuration("30 minutes")
        return []
    else:
        # New session
        state.update(events)
        state.setTimeoutDuration("30 minutes")
        return []

result = df \
    .groupByKey(lambda x: x.user_id) \
    .flatMapGroupsWithState(
        session_window_fn,
        GroupStateTimeout.ProcessingTimeTimeout
    )
```

### Window Aggregation Patterns

```python
# Running aggregation
running_agg = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "10 minutes"),
        "user_id"
    ) \
    .agg(
        count("*").alias("event_count"),
        sum("amount").alias("total_amount"),
        avg("amount").alias("avg_amount")
    )

# Multiple windows
multi_window = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "5 minutes", "1 minute"),
        window("timestamp", "1 hour", "5 minutes"),
        "event_type"
    ) \
    .count()
```

---

## Watermarking

### Basic Watermarking

```python
# Define watermark on event time
watermarked_df = df \
    .withWatermark("timestamp", "10 minutes")

# Watermark defines maximum lateness
# Events arriving more than 10 minutes late are dropped
```

### Watermark with Aggregation

```python
# Aggregate with watermark
result = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "10 minutes"),
        "event_type"
    ) \
    .count()

# Late events are dropped after watermark
# State is cleaned up after watermark passes
```

### Watermark for Joins

```python
# Stream-stream join with watermark
left = df_left.withWatermark("timestamp", "5 minutes")
right = df_right.withWatermark("timestamp", "10 minutes")

joined = left.join(
    right,
    (left.key == right.key) &
    (left.timestamp >= right.timestamp) &
    (left.timestamp <= right.timestamp + expr("INTERVAL 10 MINUTES"))
)
```

### Watermark Configuration

```python
# Configure watermark
watermarked_df = df \
    .withWatermark("event_time", "10 minutes")

# Multiple watermarks
watermarked_df = df \
    .withWatermark("event_time", "10 minutes") \
    .withWatermark("processing_time", "5 minutes")
```

---

## State Management

### MapGroupsWithState

```python
from pyspark.sql.streaming import GroupState, GroupStateTimeout

def update_state(user_id, events, state: GroupState):
    if state.hasTimedOut:
        # Handle timeout
        session = state.get
        state.remove()
        return [(user_id, "timeout", session)]
    elif state.exists:
        # Update existing state
        current = state.get
        current.extend(events)
        state.update(current)
        state.setTimeoutDuration("30 minutes")
        return []
    else:
        # New state
        state.update(events)
        state.setTimeoutDuration("30 minutes")
        return []

result = df \
    .groupByKey(lambda x: x.user_id) \
    .mapGroupsWithState(update_state)
```

### FlatMapGroupsWithState

```python
# Multiple output rows per group
def process_events(user_id, events, state: GroupState):
    if state.hasTimedOut:
        # Emit final result
        session = state.get
        state.remove()
        return [SessionResult(user_id, session, "complete")]
    elif state.exists:
        # Update and optionally emit
        current = state.get
        current.extend(events)
        state.update(current)
        state.setTimeoutDuration("30 minutes")
        return []
    else:
        # New state
        state.update(events)
        state.setTimeoutDuration("30 minutes")
        return []

result = df \
    .groupByKey(lambda x: x.user_id) \
    .flatMapGroupsWithState(process_events)
```

### State Cleanup

```python
# State is cleaned up based on watermark
# Configure timeout for state cleanup
def update_state_with_timeout(user_id, events, state: GroupState):
    if state.hasTimedOut:
        state.remove()
        return []
    elif state.exists:
        state.update(state.get + events)
        state.setTimeoutDuration("1 hour")
        return []
    else:
        state.update(events)
        state.setTimeoutDuration("1 hour")
        return []

# Cleanup old state
df \
    .withWatermark("timestamp", "10 minutes") \
    .groupByKey(lambda x: x.user_id) \
    .mapGroupsWithState(update_state_with_timeout)
```

---

## Streaming Joins

### Stream-Stream Joins

```python
# Inner join
joined = left_stream.join(right_stream, "key")

# Outer join
joined = left_stream.join(
    right_stream,
    left_stream.key == right_stream.key,
    "outer"
)

# Left outer join
joined = left_stream.join(
    right_stream,
    left_stream.key == right_stream.key,
    "left_outer"
)

# Right outer join
joined = left_stream.join(
    right_stream,
    left_stream.key == right_stream.key,
    "right_outer"
)

# Full outer join
joined = left_stream.join(
    right_stream,
    left_stream.key == right_stream.key,
    "full_outer"
)
```

### Join with Watermark

```python
# Stream-stream join with watermark
left = left_stream \
    .withWatermark("timestamp", "10 minutes")

right = right_stream \
    .withWatermark("timestamp", "5 minutes")

joined = left.join(
    right,
    (left.key == right.key) &
    (left.timestamp >= right.timestamp) &
    (left.timestamp <= right.timestamp + expr("INTERVAL 5 MINUTES"))
)
```

### Join Conditions

```python
# Equality join
joined = left.join(right, left.key == right.key)

# Range join
joined = left.join(
    right,
    (left.timestamp >= right.timestamp) &
    (left.timestamp <= right.timestamp + expr("INTERVAL 1 HOUR"))
)

# Multi-condition join
joined = left.join(
    right,
    (left.key1 == right.key1) &
    (left.key2 == right.key2) &
    (left.timestamp >= right.timestamp)
)
```

### Join Types

```python
# Inner join (default)
joined = left.join(right, "key")

# Left outer join
joined = left.join(right, "key", "left_outer")

# Right outer join
joined = left.join(right, "key", "right_outer")

# Full outer join
joined = left.join(right, "key", "full_outer")

# Left semi join
joined = left.join(right, "key", "left_semi")

# Left anti join
joined = left.join(right, "key", "left_anti")
```

---

## Fault Tolerance

### Checkpointing

```python
# Enable checkpointing
query = df.writeStream \
    .option("checkpointLocation", "hdfs://checkpoint/path") \
    .outputMode("append") \
    .format("console") \
    .start()

# Checkpoint stores:
# - Offsets of consumed data
# - Metadata of the query
# - Aggregation state (for stateful operations)
```

### Exactly-Once Semantics

```python
# End-to-end exactly-once
# 1. Source provides exactly-once consumption
# 2. Processing is deterministic
# 3. Sink provides exactly-once writing

# Kafka example
df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "topic") \
    .option("startingOffsets", "earliest") \
    .option("maxOffsetsPerTrigger", 10000) \
    .load()

# Write to Kafka (exactly-once)
query = df.writeStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("topic", "output-topic") \
    .option("checkpointLocation", "hdfs://checkpoint") \
    .start()
```

### Fault Recovery

```python
# Automatic recovery from failures
# 1. Query stops due to failure
# 2. Restart query with same checkpoint location
# 3. Spark reads checkpoint to recover state
# 4. Query resumes from last checkpoint

# Recovery example
def create_query():
    return df.writeStream \
        .option("checkpointLocation", "hdfs://checkpoint") \
        .outputMode("append") \
        .format("console") \
        .start()

# Start or recover
query = spark \
    .readStream \
    .load() \
    .writeStream \
    .option("checkpointLocation", "hdfs://checkpoint") \
    .start()
```

---

## Performance Optimization

### Micro-Batch Tuning

```python
# Trigger interval
query = df.writeStream \
    .trigger(processingTime="10 seconds") \
    .start()

# Trigger once
query = df.writeStream \
    .trigger(once=True) \
    .start()

# Trigger now
query = df.writeStream \
    .trigger(now=True) \
    .start()

# Trigger continuously (experimental)
query = df.writeStream \
    .trigger(continuous="1 second") \
    .start()
```

### Partitioning

```python
# Repartition before aggregation
repartitioned = df.repartition(100, "key")

# Partition by output column
query = df.writeStream \
    .partitionBy("event_type") \
    .start()

# Coalesce for reducing partitions
coalesced = df.coalesce(10)
```

### Memory Management

```python
# Configure memory
spark.conf.set("spark.sql.shuffle.partitions", 200)
spark.conf.set("spark.sql.streaming.stateStore.providerClass",
    "org.apache.spark.sql.execution.streaming.state.HDFSBackedStateStoreProvider")

# State store configuration
spark.conf.set("spark.sql.streaming.stateStore.compression", True)
spark.conf.set("spark.sql.streaming.stateStore.minPartitions", 4)
```

### Batch Size

```python
# Control batch size
spark.conf.set("spark.sql.streaming.maxBatchesToRetainInMemory", 100)
spark.conf.set("spark.sql.streaming.minBatchesToRetain", 100)

# Max offsets per trigger
spark.conf.set("spark.sql.streaming.maxOffsetsPerTrigger", 10000)
```

### Rate Limiting

```python
# Limit processing rate
spark.conf.set("spark.sql.streaming.rateLimit", 10000)
spark.conf.set("spark.sql.streaming.maxRate", 10000)

# Kafka rate limiting
spark.conf.set("spark.streaming.kafka.maxRatePerPartition", 1000)
```

---

## Monitoring

### Streaming Metrics

```python
# Get streaming query metrics
query.lastProgress
query.recentProgress

# Access metrics programmatically
metrics = query.lastProgress
print(f"Input rows: {metrics['inputRowsPerSecond']}")
print(f"Processing time: {metrics['batchDuration']}")
print(f"State rows: {metrics['stateOperator']['numRowsTotal']}")
```

### Spark UI

```
# Streaming tab in Spark UI
http://<driver>:4040/StreamingQuery/

# Key metrics:
# - Input rate
# - Processing rate
# - Batch duration
# - Input rows per second
# - Processed rows per second
# - State rows
# - Number of completed batches
# - Number of active batches
```

### Query Status

```python
# Check query status
print(query.status)

# Check if query is active
print(query.isActive)

# Get query id
print(query.id)

# Get recent progress
for progress in query.recentProgress:
    print(progress)
```

### Alerting

```python
# Monitor query health
def monitor_query(query):
    while query.isActive:
        # Check for issues
        if query.lastProgress:
            input_rate = query.lastProgress['inputRowsPerSecond']
            if input_rate > threshold:
                send_alert("High input rate")

        time.sleep(60)

# Custom metrics
spark.streams.awaitAnyTermination()
```

---

## Best Practices

### Design Principles

1. **Idempotent operations**: Handle duplicate processing gracefully
2. **Exactly-once semantics**: Use checkpointing and idempotent sinks
3. **Backpressure**: Prevent overwhelming downstream systems
4. **Monitoring**: Track processing times and lag

### Performance Optimization

1. **Tune batch interval**: Balance latency and throughput
2. **Optimize serialization**: Use Kryo serializer
3. **Manage state**: Regularly clean up old state
4. **Partition appropriately**: Match partition count to cluster size

### Common Patterns

```python
# Deduplication
deduplicated = df.dropDuplicates(["event_id"])

# Windowed aggregation
windowed_agg = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(window("timestamp", "10 minutes"), "event_type") \
    .count()

# Stream-static join
joined = df.join(static_df, "key")

# Multiple output sinks
query1 = df.writeStream.format("console").start()
query2 = df.writeStream.format("kafka").start()
```

### Error Handling

```python
# Handle bad records
def process_record(record):
    try:
        # Process record
        return process_valid_record(record)
    except Exception as e:
        # Log error and return None
        log_error(record, e)
        return None

# Filter null records
cleaned_df = df.na.drop()

# Handle schema evolution
df = spark.readStream \
    .schema(schema) \
    .option("mergeSchema", True) \
    .json("/data/input")
```

---

## Examples

### Real-Time Analytics Dashboard

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder \
    .appName("RealTimeDashboard") \
    .getOrCreate()

# Read from Kafka
events = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "events") \
    .load()

# Parse events
schema = StructType() \
    .add("event_id", StringType()) \
    .add("user_id", StringType()) \
    .add("event_type", StringType()) \
    .add("amount", DoubleType()) \
    .add("timestamp", TimestampType())

parsed = events \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*")

# Real-time aggregation
metrics = parsed \
    .withWatermark("timestamp", "1 minute") \
    .groupBy(
        window("timestamp", "5 minutes"),
        "event_type"
    ) \
    .agg(
        count("*").alias("event_count"),
        sum("amount").alias("total_amount"),
        countDistinct("user_id").alias("unique_users")
    )

# Write to console (for dashboard)
query = metrics.writeStream \
    .outputMode("update") \
    .format("console") \
    .option("checkpointLocation", "hdfs://checkpoint/dashboard") \
    .start()

query.awaitTermination()
```

### Real-Time Fraud Detection

```python
# Read transactions
transactions = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "transactions") \
    .load()

# Parse transactions
parsed = transactions \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*")

# Detect anomalies
anomalies = parsed \
    .withWatermark("timestamp", "5 minutes") \
    .groupBy(
        window("timestamp", "5 minutes"),
        "user_id"
    ) \
    .agg(
        count("*").alias("transaction_count"),
        sum("amount").alias("total_amount"),
        avg("amount").alias("avg_amount")
    ) \
    .filter(
        (col("transaction_count") > 10) |
        (col("total_amount") > 10000)
    )

# Write alerts
alerts = anomalies.writeStream \
    .outputMode("update") \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("topic", "alerts") \
    .option("checkpointLocation", "hdfs://checkpoint/fraud") \
    .start()

alerts.awaitTermination()
```

---

## References

- [Structured Streaming Programming Guide](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html)
- [Structured Streaming API](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql.html)
- [Kafka Integration](https://spark.apache.org/docs/latest/structured-streaming-kafka-integration.html)
- [Streaming Dataset Operations](https://spark.apache.org/docs/latest/api/python/reference/pyspark.sql.html#dataset-operations)
- [High Performance Spark](http://shop.oreilly.com/product/0636920028512.do)
