# Spark Streaming

Spark Streaming enables processing of real-time data streams with high throughput and fault tolerance. It provides two main APIs: DStreams (legacy) and Structured Streaming (modern), both built on Spark's core engine for unified batch and stream processing.

## Table of Contents

1. [Streaming Overview](#streaming-overview)
2. [DStreams (Legacy)](#dstreams-legacy)
3. [Structured Streaming](#structured-streaming)
4. [Window Operations](#window-operations)
5. [State Management](#state-management)
6. [Fault Tolerance](#fault-tolerance)
7. [Performance Optimization](#performance-optimization)
8. [Best Practices](#best-practices)
9. [Common Patterns](#common-patterns)

---

## Streaming Overview

### Streaming Paradigms

```
Streaming Approaches:
┌─────────────────────────────────────────────────────────────┐
│                      Micro-batch Processing                  │
│         (Structured Streaming, DStreams)                    │
│         - Processes data in small batches                   │
│         - High throughput                                    │
│         - Latency: seconds                                  │
├─────────────────────────────────────────────────────────────┤
│                      Continuous Processing                   │
│         (Spark 3.x Structured Streaming)                   │
│         - True streaming                                    │
│         - Low latency                                       │
│         - Exactly-once guarantees                           │
└─────────────────────────────────────────────────────────────┘
```

### DStreams vs Structured Streaming

| Feature | DStreams | Structured Streaming |
|---------|----------|---------------------|
| **API** | RDD-based | DataFrame/Dataset |
| **Optimization** | None | Catalyst optimizer |
| **Fault Tolerance** | WAL + Checkpoints | WAL + Checkpoints |
| **State Management** | Manual | Built-in |
| **Late Data Handling** | Manual | Built-in |
| **Event Time** | Manual | Built-in |
| **Recommended** | No | Yes |

---

## DStreams (Legacy)

### Creating DStreams

```python
from pyspark.streaming import StreamingContext

# Create StreamingContext
ssc = StreamingContext(sparkContext, batchInterval=5)

# From socket
socket_dstream = ssc.socketTextStream("localhost", 9999)

# From file system
file_dstream = ssc.textFileStream("hdfs://path/to/directory")

# From Kafka
from pyspark.streaming.kafka import KafkaUtils

kafka_params = {
    "bootstrap.servers": "localhost:9092",
    "key.deserializer": StringDeserializer,
    "value.deserializer": StringDeserializer,
    "group.id": "streaming_group"
}

kafka_dstream = KafkaUtils.createDirectStream(
    ssc, ["topic_name"], kafka_params
)
```

### DStream Operations

```python
# Transformations (narrow)
mapped_dstream = socket_dstream.map(lambda line: line.split(" "))
filtered_dstream = socket_dstream.filter(lambda line: "error" in line)
flat_mapped_dstream = socket_dstream.flatMap(lambda line: line.split(" "))

# Transformations (wide)
reduced_dstream = mapped_dstream.reduceByKey(lambda a, b: a + b)
grouped_dstream = mapped_dstream.groupByKey()

# Window operations
windowed_dstream = socket_dstream.window(windowDuration=30, slideDuration=10)
count_by_window = socket_dstream.countByWindow()
count_by_value = socket_dstream.countByValueAndWindow(windowDuration=30, slideDuration=10)

# Join operations
joined_dstream = stream1.join(stream2)
left_joined = stream1.leftOuterJoin(stream2)

# Output operations
socket_dstream.pprint()
socket_dstream.foreachRDD(process_rdd)
reduced_dstream.saveAsTextFiles("output_path")
```

### DStream with Kafka

```python
from pyspark.streaming.kafka import KafkaUtils
from kafka import KafkaConsumer, KafkaProducer

# Consumer
consumer = KafkaConsumer(
    'topic_name',
    bootstrap_servers=['localhost:9092'],
    value_deserializer=lambda m: json.loads(m.decode('utf-8'))
)

# Producer
producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

# DStream from Kafka
kafka_stream = KafkaUtils.createDirectStream(
    ssc,
    ["topic_name"],
    {
        "metadata.broker.list": "localhost:9092",
        "group.id": "streaming_group",
        "auto.offset.reset": "largest"
    }
)

# Process messages
def process_message(message):
    key, value = message
    data = json.loads(value)
    # Process data
    return processed_data

processed_stream = kafka_stream.map(process_message)
```

---

## Structured Streaming

### Creating Streams

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder \
    .appName("StructuredStreaming") \
    .getOrCreate()

# From socket
socket_df = spark.readStream \
    .format("socket") \
    .option("host", "localhost") \
    .option("port", 9999) \
    .load()

# From Kafka
kafka_df = spark.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "topic_name") \
    .option("startingOffsets", "earliest") \
    .load()

# From file system
file_df = spark.readStream \
    .format("text") \
    .path("hdfs://path/to/directory") \
    .load()

# From rate (for testing)
rate_df = spark.readStream \
    .format("rate") \
    .option("rowsPerSecond", 100) \
    .load()
```

### Streaming Queries

```python
# Simple transformation
words = socket_df.select(
    explode(split(socket_df.value, " ")).alias("word")
)

# Windowed aggregation
windowed_counts = words \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window(words.timestamp, "10 minutes", "5 minutes"),
        words.word
    ) \
    .count()

# Write stream
query = windowed_counts.writeStream \
    .outputMode("complete") \
    .format("console") \
    .option("checkpointLocation", "hdfs://path/to/checkpoint") \
    .trigger(processingTime="10 seconds") \
    .start()

# Wait for termination
query.awaitTermination()
```

### Output Modes

```python
# Append mode: Only new rows
query = df.writeStream \
    .outputMode("append") \
    .format("console") \
    .start()

# Complete mode: Entire result table
query = df.writeStream \
    .outputMode("complete") \
    .format("console") \
    .start()

# Update mode: Only updated rows
query = df.writeStream \
    .outputMode("update") \
    .format("console") \
    .start()
```

### Streaming Sinks

```python
# Console sink (for debugging)
query = df.writeStream \
    .format("console") \
    .start()

# File sink
query = df.writeStream \
    .format("parquet") \
    .path("hdfs://path/to/output") \
    .start()

# Kafka sink
query = df.writeStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("topic", "output_topic") \
    .start()

# Memory sink (for testing)
query = df.writeStream \
    .format("memory") \
    .queryName("output_table") \
    .start()

# Foreach sink
def process_row(row):
    # Process each row
    pass

query = df.writeStream \
    .foreach(process_row) \
    .start()

# ForeachBatch sink
def process_batch(batch_df, batch_id):
    # Process each batch
    batch_df.write.mode("append").parquet("output_path")

query = df.writeStream \
    .foreachBatch(process_batch) \
    .start()
```

---

## Window Operations

### Tumbling Windows

```python
# Fixed-size non-overlapping windows
windowed_df = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window(df.timestamp, "10 minutes"),
        df.event_type
    ) \
    .count()
```

### Sliding Windows

```python
# Fixed-size overlapping windows
windowed_df = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window(df.timestamp, "10 minutes", "5 minutes"),
        df.event_type
    ) \
    .count()
```

### Session Windows

```python
# Dynamic windows based on activity gaps
session_df = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        session_window(df.timestamp, "10 minutes"),
        df.user_id
    ) \
    .count()
```

### Window Functions

```python
# Aggregations over windows
windowed_df = df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "1 hour"),
        "user_id"
    ) \
    .agg(
        count("*").alias("event_count"),
        sum("amount").alias("total_amount"),
        avg("amount").alias("avg_amount")
    )

# Window with watermark
windowed_df = df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "1 hour"),
        "user_id"
    ) \
    .agg(
        count("*").alias("event_count")
    )
```

---

## State Management

### Stateful Operations

```python
# Group state
stateful_df = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        df.user_id,
        window(df.timestamp, "1 hour")
    ) \
    .agg(
        collect_list("event").alias("events"),
        count("*").alias("event_count")
    )

# Flat map groupsWithState
from pyspark.sql.streaming import GroupStateTimeout, GroupState

def update_state(user_id, events, state: GroupState):
    if state.hasTimedOut:
        # State timed out
        yield (user_id, state.get)
        state.remove()
    elif state.exists:
        # Update existing state
        current_events = state.get
        updated_events = current_events + events
        state.update(updated_events)
        state.setTimeoutDuration("10 minutes")
        yield (user_id, updated_events)
    else:
        # New state
        state.update(events)
        state.setTimeoutDuration("10 minutes")
        yield (user_id, events)

stateful_df = df \
    .groupByKey(lambda row: row.user_id) \
    .mapGroupsWithState(update_state, timeout=GroupStateTimeout.ProcessingTimeTimeout)
```

### State Store

```python
# RocksDB state store
spark.conf.set("spark.sql.streaming.stateStore.providerClass",
    "org.apache.spark.sql.execution.streaming.state.RocksDBStateStoreProvider")

# HDFS state store
spark.conf.set("spark.sql.streaming.stateStore.providerClass",
    "org.apache.spark.sql.execution.streaming.state.HDFSStateStoreProvider")
```

### State Expiration

```python
# Timeout-based expiration
stateful_df = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        df.user_id,
        window(df.timestamp, "1 hour")
    ) \
    .mapGroupsWithState(
        update_state,
        timeout=GroupStateTimeout.ProcessingTimeTimeout
    )

# Watermark-based expiration
windowed_df = df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "1 hour"),
        "user_id"
    ) \
    .count()
```

---

## Fault Tolerance

### Checkpointing

```python
# Enable checkpointing
query = df.writeStream \
    .outputMode("append") \
    .format("parquet") \
    .path("hdfs://path/to/output") \
    .option("checkpointLocation", "hdfs://path/to/checkpoint") \
    .start()

# Checkpoint contents:
# - Offset logs
# - Commit logs
# - State snapshots
```

### Write-Ahead Logs (WAL)

```python
# Enable WAL for DStreams
ssc.checkpoint("hdfs://path/to/checkpoint")

# WAL ensures data is not lost on failure
# Data is written to persistent storage before processing
```

### Exactly-Once Semantics

```python
# Structured Streaming guarantees exactly-once with:
# 1. WAL for input sources
# 2. Idempotent sinks
# 3. Offset tracking

# Idempotent sink example
def process_batch(batch_df, batch_id):
    # Upsert operation ensures idempotency
    batch_df.write \
        .format("jdbc") \
        .option("url", "jdbc:mysql://host:3306/db") \
        .option("dbtable", "output_table") \
        .mode("append") \
        .option("batchsize", 1000) \
        .save()

query = df.writeStream \
    .foreachBatch(process_batch) \
    .option("checkpointLocation", "hdfs://path/to/checkpoint") \
    .start()
```

### Failure Recovery

```python
# On failure, streaming query restarts from last checkpoint
# 1. Reads checkpoint metadata
# 2. Replays data from last committed offset
# 3. Resumes processing

# Manual restart
query = spark.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "topic_name") \
    .option("startingOffsets", "earliest") \
    .load() \
    .writeStream \
    .format("parquet") \
    .path("output_path") \
    .option("checkpointLocation", "hdfs://path/to/checkpoint") \
    .start()

# Query will automatically resume from last checkpoint
```

---

## Performance Optimization

### Parallelism

```python
# Configure parallelism
spark.conf.set("spark.sql.shuffle.partitions", "200")
spark.conf.set("spark.default.parallelism", "200")

# Repartition for parallelism
repartitioned_df = df.repartition(100)

# Coalesce to reduce partitions
coalesced_df = df.coalesce(10)
```

### Batch Size

```python
# Optimize batch interval
query = df.writeStream \
    .trigger(processingTime="10 seconds") \
    .start()

# Smaller batches = lower latency, higher overhead
# Larger batches = higher latency, better throughput
```

### Memory Management

```python
# Configure memory
spark.conf.set("spark.streaming.backpressure.enabled", "true")
spark.conf.set("spark.streaming.receiver.maxRate", "1000")
spark.conf.set("spark.streaming.kafka.maxRatePerPartition", "1000")

# Off-heap memory
spark.conf.set("spark.memory.offHeap.enabled", "true")
spark.conf.set("spark.memory.offHeap.size", "1g")
```

### Watermark Optimization

```python
# Use appropriate watermark
watermark_df = df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "1 hour"),
        "user_id"
    ) \
    .count()

# Watermark helps manage state size
# by discarding old data
```

### State Store Optimization

```python
# Use RocksDB for better performance
spark.conf.set(
    "spark.sql.streaming.stateStore.providerClass",
    "org.apache.spark.sql.execution.streaming.state.RocksDBStateStoreProvider"
)

# Configure state store
spark.conf.set("spark.sql.streaming.stateStore.rocksDB.blockSize", "16kb")
spark.conf.set("spark.sql.streaming.stateStore.rocksDB.writeBufferSize", "4mb")
```

---

## Best Practices

### 1. Use Structured Streaming

```python
# Prefer Structured Streaming over DStreams
# Benefits:
# - Catalyst optimization
# - Better fault tolerance
# - Built-in state management
# - Event time processing

query = df.writeStream \
    .outputMode("update") \
    .format("console") \
    .start()
```

### 2. Implement Watermarks

```python
# Use watermarks to handle late data
watermark_df = df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "1 hour"),
        "user_id"
    ) \
    .count()
```

### 3. Use Idempotent Sinks

```python
# Ensure exactly-once with idempotent operations
def process_batch(batch_df, batch_id):
    # Use MERGE or INSERT OVERWRITE for idempotency
    batch_df.write \
        .format("delta") \
        .mode("overwrite") \
        .save("output_path")

query = df.writeStream \
    .foreachBatch(process_batch) \
    .start()
```

### 4. Monitor Streaming Queries

```python
# Monitor query status
for query in spark.streams.active:
    print(f"Query ID: {query.id}")
    print(f"Status: {query.status}")
    print(f"Recent Progress: {query.recentProgress}")
```

### 5. Handle Late Data

```python
# Use watermarks for late data
late_data_df = df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "1 hour"),
        "user_id"
    ) \
    .count()

# Late data beyond watermark is dropped
```

---

## Common Patterns

### Pattern 1: Real-time Aggregation

```python
# Real-time counting
windowed_counts = df \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "5 minutes"),
        "event_type"
    ) \
    .count() \
    .writeStream \
    .outputMode("update") \
    .format("console") \
    .start()
```

### Pattern 2: Session Analysis

```python
# User session tracking
session_df = df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        session_window("event_time", "30 minutes"),
        "user_id"
    ) \
    .agg(
        collect_list("event").alias("events"),
        count("*").alias("event_count")
    ) \
    .writeStream \
    .outputMode("append") \
    .format("parquet") \
    .path("session_output") \
    .option("checkpointLocation", "session_checkpoint") \
    .start()
```

### Pattern 3: Anomaly Detection

```python
# Detect anomalies in real-time
from pyspark.sql.functions import avg, stddev

# Calculate rolling statistics
stats_df = df \
    .withWatermark("timestamp", "5 minutes") \
    .groupBy(
        window("timestamp", "1 minute"),
        "metric_name"
    ) \
    .agg(
        avg("value").alias("avg_value"),
        stddev("value").alias("stddev_value")
    )

# Detect anomalies (values outside 3 standard deviations)
anomalies_df = df.join(stats_df, 
    (df.timestamp >= stats_df.window.start) &
    (df.timestamp < stats_df.window.end) &
    (df.metric_name == stats_df.metric_name)
) \
.withColumn("is_anomaly",
    (df.value > stats_df.avg_value + 3 * stats_df.stddev_value) |
    (df.value < stats_df.avg_value - 3 * stats_df.stddev_value)
) \
.filter("is_anomaly = true")
```

### Pattern 4: Stream-Stream Join

```python
# Join two streams
stream1 = spark.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "topic1") \
    .load()

stream2 = spark.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "topic2") \
    .load()

# Join streams with watermarks
joined_df = stream1 \
    .withWatermark("event_time", "10 minutes") \
    .join(stream2 \
        .withWatermark("event_time", "10 minutes"),
        stream1.user_id == stream2.user_id
    )

query = joined_df.writeStream \
    .outputMode("append") \
    .format("console") \
    .start()
```

### Pattern 5: Multi-hop Processing

```python
# Multi-stage streaming pipeline
# Stage 1: Ingest and clean
raw_df = spark.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "raw_topic") \
    .load()

cleaned_df = raw_df \
    .select(
        from_json(raw_df.value, schema).alias("data")
    ) \
    .select("data.*") \
    .filter(col("data.is_valid") == True)

# Stage 2: Enrich
enriched_df = cleaned_df \
    .join(broadcast(lookup_df), "user_id")

# Stage 3: Aggregate
aggregated_df = enriched_df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "1 hour"),
        "category"
    ) \
    .agg(sum("amount").alias("total_amount"))

# Write to sink
query = aggregated_df.writeStream \
    .outputMode("update") \
    .format("parquet") \
    .path("output_path") \
    .option("checkpointLocation", "checkpoint_path") \
    .start()
```

---

## Conclusion

Spark Streaming provides:

- **Unified API** for batch and stream processing
- **Structured Streaming** for modern streaming workloads
- **Exactly-once semantics** with proper configuration
- **Event time processing** with watermarks
- **State management** for complex streaming logic

Key takeaways:

1. **Use Structured Streaming** over DStreams
2. **Implement watermarks** for late data handling
3. **Use idempotent sinks** for exactly-once semantics
4. **Monitor streaming queries** for performance
5. **Optimize batch intervals** for your use case

Streaming is essential for real-time analytics, event processing, and building responsive data pipelines.