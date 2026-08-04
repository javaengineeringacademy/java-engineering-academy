# Spark Structured Streaming

## Overview

Spark Structured Streaming is a scalable, fault-tolerant stream processing engine built on Apache Spark. It treats a live data stream as a continuously appended unbounded table, enabling users to query it using the same DataFrame/SQL API used for batch processing.

## Table of Contents

- [Architecture](#architecture)
- [Core Concepts](#core-concepts)
- [Input Sources](#input-sources)
- [Output Modes](#output-modes)
- [Window Operations](#window-operations)
- [State Management](#state-management)
- [Fault Tolerance](#fault-tolerance)
- [Performance Tuning](#performance-tuning)
- [Best Practices](#best-practices)

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│              SPARK STRUCTURED STREAMING                      │
├─────────────────────────────────────────────────────────────┤
│  Input Sources                                              │
│  Kafka │ File │ Socket │ Rate │ Custom                      │
├─────────────────────────────────────────────────────────────┤
│  Processing Engine                                           │
│  DataFrame API │ SQL │ Structured Streaming                 │
├─────────────────────────────────────────────────────────────┤
│  Output Sinks                                               │
│  Kafka │ File │ Console │ Memory │ Foreach │ Custom         │
├─────────────────────────────────────────────────────────────┤
│  State Store                                                │
│  HDFS │ S3 │ Local │ Custom                                 │
└─────────────────────────────────────────────────────────────┘
```

## Core Concepts

### Basic Streaming Query

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder \
    .appName("StructuredStreaming") \
    .getOrCreate()

# Read from Kafka
raw_stream = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "events") \
    .load()

# Parse JSON
events = raw_stream.select(
    col("key").cast("string").alias("key"),
    from_json(col("value").cast("string"), schema).alias("data")
).select("data.*")

# Process
processed = events \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "5 minutes"),
        "event_type"
    ).count()

# Write output
query = processed.writeStream \
    .outputMode("update") \
    .format("console") \
    .option("checkpointLocation", "/tmp/checkpoint") \
    .start()
```

## Input Sources

### Kafka Source

```python
# Kafka with all options
df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "broker1:9092,broker2:9092") \
    .option("subscribe", "topic1,topic2") \
    .option("startingOffsets", "earliest") \
    .option("endingOffsets", "latest") \
    .option("kafka.group.id", "spark-consumer-group") \
    .option("maxOffsetsPerTrigger", 100000) \
    .option("failOnDataLoss", "false") \
    .load()
```

### File Source

```python
# Read from cloud storage
df = spark \
    .readStream \
    .format("json") \
    .schema(custom_schema) \
    .option("maxFilesPerTrigger", 10) \
    .option("path", "s3://bucket/events/") \
    .option("cleanSource", "delete") \
    .load()
```

### Rate Source (Testing)

```python
# Generate test data
df = spark \
    .readStream \
    .format("rate") \
    .option("rowsPerSecond", 1000) \
    .option("numPartitions", 10) \
    .load()
```

### Custom Source

```python
class CustomDataSource(DataSourceV2):
    def createReader(self, schema):
        return CustomReader(schema)

class CustomReader(DataSourceReader):
    def read(self, partitionId):
        # Custom data reading logic
        return data_iterator
```

## Output Modes

### Append Mode

```python
# Only new rows added since last trigger
query = df.writeStream \
    .outputMode("append") \
    .format("console") \
    .start()

# Best for: Stateless operations, filters, projections
```

### Complete Mode

```python
# Entire result table output every trigger
query = df.writeStream \
    .outputMode("complete") \
    .format("console") \
    .start()

# Best for: Aggregations, windowed operations
```

### Update Mode

```python
# Only rows that were updated since last trigger
query = df.writeStream \
    .outputMode("update") \
    .format("console") \
    .start()

# Best for: Aggregations with watermark, non-blocking
```

| Mode | Description | Use Case |
|------|-------------|----------|
| Append | New rows only | Stateless transformations |
| Complete | Full result table | Aggregations |
| Update | Updated rows only | Stateful aggregations |

## Window Operations

### Tumbling Window

```python
# Fixed-size, non-overlapping windows
windowed_count = events \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "5 minutes"),  # 5-min tumbling window
        "event_type"
    ).agg(
        count("*").alias("event_count"),
        avg("value").alias("avg_value")
    )
```

### Sliding Window

```python
# Overlapping windows
windowed_count = events \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "10 minutes", "5 minutes"),  # 10-min window, 5-min slide
        "event_type"
    ).count()
```

### Session Window

```python
# Dynamic windows based on activity gaps
from pyspark.sql.functions import session_window

sessionized = events \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        session_window("event_time", "5 minutes"),  # 5-min gap threshold
        "user_id"
    ).count()
```

### Global Window

```python
# All events in a single window (requires custom logic)
from pyspark.sql.functions import lit

global_window = events \
    .withColumn("window_key", lit("global")) \
    .groupBy("window_key") \
    .count()
```

## State Management

### Stateful Aggregations

```python
# Running aggregation with watermark
stateful_agg = events \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        col("user_id"),
        window("event_time", "1 hour")
    ).agg(
        sum("amount").alias("total_amount"),
        count("*").alias("event_count"),
        approx_count_distinct("session_id").alias("unique_sessions")
    )
```

### MapGroupsWithState (Custom State)

```python
from pyspark.sql.functions import map_groupsWith_state, GroupState

def update_user_state(user_id, events_iter, state: GroupState):
    # Get current state or initialize
    if state.exists:
        current_state = state.get
    else:
        current_state = {"total_amount": 0, "event_count": 0}

    # Process events
    for event in events_iter:
        current_state["total_amount"] += event["amount"]
        current_state["event_count"] += 1

    # Update state
    state.update(current_state)

    # Set timeout for inactive users
    state.timeout(3600)  # 1 hour

    # Yield result
    yield (user_id, current_state)

# Usage
result = events \
    .groupByKey("user_id") \
    .mapGroupsWithState(update_user_state)
```

### FlatMapGroupsWithState

```python
def process_session(user_id, events_iter, state: GroupState):
    if state.exists:
        session = state.get
    else:
        session = {"start_time": None, "events": []}

    for event in events_iter:
        if session["start_time"] is None:
            session["start_time"] = event["event_time"]
        
        session["events"].append(event)
        
        # Check if session should end
        if len(session["events"]) > 100:
            yield (user_id, session)
            session = {"start_time": None, "events": []}
    
    state.update(session)
```

## Fault Tolerance

### Checkpoint Configuration

```python
query = df.writeStream \
    .outputMode("update") \
    .format("kafka") \
    .option("checkpointLocation", "s3://bucket/checkpoints/") \
    .option("kafka.bootstrap.servers", "broker:9092") \
    .option("topic", "output") \
    .start()
```

### Exactly-Once Semantics

```python
# Spark provides exactly-once via:
# 1. Write-ahead log for input offsets
# 2. Idempotent writes to sink
# 3. Checkpoint recovery

# Configuration for exactly-once
spark.conf.set("spark.sql.streaming.metricsEnabled", "true")
spark.conf.set("spark.sql.streaming.stateStore.providerClass",
               "org.apache.spark.sql.execution.streaming.state.HDFSBackedStateStoreProvider")
```

### Failure Recovery

```python
# Automatic recovery from checkpoint
query = spark.readStream \
    .format("kafka") \
    .load() \
    .writeStream \
    .option("checkpointLocation", "/checkpoint/path") \
    .start()

# Spark will automatically recover from last checkpoint
```

## Performance Tuning

### Partitioning

```python
# Optimize shuffle partitions
spark.conf.set("spark.sql.shuffle.partitions", "200")

# Repartition for parallelism
events = events.repartition(100)

# Coalesce for output
events.coalesce(10)
```

### Trigger Configuration

```python
# Processing time trigger
query = df.writeStream \
    .trigger(processingTime="10 seconds") \
    .start()

# Once trigger (for testing)
query = df.writeStream \
    .trigger(once=True) \
    .start()

# Available now trigger
query = df.writeStream \
    .trigger(availableNow=True) \
    .start()
```

### Memory Management

```python
# Tune shuffle memory
spark.conf.set("spark.sql.shuffle.partitions", "200")
spark.conf.set("spark.sql.autoBroadcastJoinThreshold", "10m")

# State store configuration
spark.conf.set("spark.sql.streaming.stateStore.minDeltasForSnapshot", "2")
```

### Monitoring

```python
# Enable streaming metrics
spark.conf.set("spark.sql.streaming.metricsEnabled", "true")

# Query status
query.lastProgress  # Latest progress
query.status        # Current status
query.recentProgress  # Recent progress history
```

## Best Practices

### 1. Use Watermarks for Late Data

```python
# Handle late-arriving data
events \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "5 minutes"),
        "user_id"
    ).count()
```

### 2. Optimize File Output

```python
# Control file sizes
events.writeStream \
    .trigger(processingTime="1 minute") \
    .option("path", "s3://output/") \
    .option("checkpointLocation", "/checkpoint/") \
    .option("maxFilesPerTrigger", 100) \
    .start()
```

### 3. Use Efficient Serialization

```python
# Enable Kryo serialization
spark.conf.set("spark.serializer", 
               "org.apache.spark.serializer.KryoSerializer")
spark.conf.set("spark.kryo.registrationRequired", "true")
```

### 4. Monitor State Size

```python
# Track state size
events.writeStream \
    .queryName("stateful-query") \
    .start()

# Monitor via Spark UI
# Streaming tab shows state size and processing rates
```

### 5. Handle Data Skew

```python
# Salting for skewed joins
from pyspark.sql.functions import concat, rand

salted_events = events.withColumn(
    "salted_key",
    concat("user_id", (rand() * 10).cast("int").cast("string"))
)
```

## Further Reading

- [Spark Structured Streaming Guide](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html)
- [Spark Streaming + Kafka Integration](https://spark.apache.org/docs/latest/structured-streaming-kafka-integration.html)
- [Spark State Management](https://spark.apache.org/docs/latest/structured-streaming-stateful-operations.html)
