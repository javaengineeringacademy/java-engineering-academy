# Spark Streaming

## Table of Contents

- [Overview](#overview)
- [DStreams (Discretized Streams)](#dstreams-discretized-streams)
- [Input Sources](#input-sources)
- [Output Operations](#output-operations)
- [Window Operations](#window-operations)
- [State Management](#state-management)
- [Fault Tolerance](#fault-tolerance)
- [Performance Tuning](#performance-tuning)
- [Spark Structured Streaming](#spark-structured-streaming)
- [Best Practices](#best-practices)
- [Examples](#examples)
- [References](#references)

---

## Overview

Spark Streaming is an extension of the core Spark API that enables scalable,
high-throughput, fault-tolerant stream processing of live data streams.
It uses a micro-batch architecture where incoming data is divided into small
batches processed by Spark.

### Key Characteristics

- **Micro-batch processing**: Data processed in small time intervals
- **Unified engine**: Same API as batch processing
- **Fault tolerant**: Automatic recovery from failures
- **Scalable**: Horizontal scaling across clusters
- **Exactly-once semantics**: With checkpointing and WAL

### When to Use Spark Streaming

- Real-time analytics and dashboards
- Continuous ETL pipelines
- Event processing and log aggregation
- Fraud detection and anomaly detection
- Real-time recommendation engines

### Spark Streaming vs Structured Streaming

| Feature | DStream (Legacy) | Structured Streaming |
|---------|------------------|---------------------|
| API | RDD-based | DataFrame/Dataset-based |
| Processing | Micro-batch only | Micro-batch + Continuous |
| Latency | Seconds | Milliseconds (continuous) |
| Fault Tolerance | Manual | Automatic |
| Event Time | Manual handling | Built-in support |
| State Management | Manual | Built-in operators |

---

## DStreams (Discretized Streams)

### Creating DStreams

```python
from pyspark.streaming import StreamingContext
from pyspark import SparkContext

# Create SparkContext
sc = SparkContext("local[*]", "StreamingApp")

# Create StreamingContext with batch interval
ssc = StreamingContext(sc, batchInterval=5)  # 5 seconds

# Create DStream from socket
socket_stream = ssc.socketTextStream("localhost", 9999)

# Create DStream from Kafka
from pyspark.streaming.kafka import KafkaUtils

kafka_stream = KafkaUtils.createStream(
    ssc,
    "zookeeper:2181",
    "consumer-group",
    {"topic": "events"}
)

# Create DStream from file system
file_stream = ssc.textFileStream("hdfs://path/to/directory")

# Create DStream from queue
rdd_queue = [sc.parallelize([1, 2, 3]), sc.parallelize([4, 5, 6])]
queue_stream = ssc.queueStream(rdd_queue)
```

### DStream Transformations

```python
# Map
mapped = socket_stream.map(lambda line: line.upper())

# FlatMap
words = socket_stream.flatMap(lambda line: line.split(" "))

# Filter
filtered = socket_stream.filter(lambda line: "error" in line.lower())

# Count
word_counts = words.map(lambda word: (word, 1)).reduceByKey(lambda a, b: a + b)

# Union
union_stream = stream1.union(stream2)

# Reduce by key
word_counts = words.map(lambda word: (word, 1)).reduceByKey(lambda a, b: a + b)

# Join
joined = stream1.join(stream2)

# Cogroup
cogrouped = stream1.cogroup(stream2)

# Transform
transformed = socket_stream.transform(lambda rdd: rdd.filter(lambda x: len(x) > 0))

# Update state by key
def update_function(new_values, running_count):
    return (running_count or 0) + sum(new_values)

running_counts = words.map(lambda word: (word, 1)).updateStateByKey(update_function)
```

### Output Operations

```python
# Print to console
socket_stream.pprint()

# Save as text files
socket_stream.saveAsTextFiles("hdfs://output/text/")

# Save as object files
socket_stream.saveAsObjectFiles("hdfs://output/object/")

# Save as sequence files
socket_stream.saveAsSequenceFiles("hdfs://output/sequence/")

# For each RDD
def process_rdd(rdd):
    if not rdd.isEmpty():
        # Process RDD
        pass

socket_stream.foreachRDD(process_rdd)

# Save to external systems
def save_to_db(rdd):
    if not rdd.isEmpty():
        # Connect to database and save
        pass

socket_stream.foreachRDD(save_to_db)

# Start and stop
ssc.start()
ssc.awaitTermination()
ssc.stop()
```

---

## Input Sources

### Socket Source

```python
# Create socket stream
socket_stream = ssc.socketTextStream("localhost", 9999)

# Process stream
words = socket_stream.flatMap(lambda line: line.split(" "))
word_counts = words.map(lambda word: (word, 1)).reduceByKey(lambda a, b: a + b)
word_counts.pprint()
```

### File Source

```python
# Monitor directory for new files
file_stream = ssc.textFileStream("hdfs://path/to/directory")

# Process files
file_stream.foreachRDD(lambda rdd: process_file_rdd(rdd))

# Filter by file pattern
filtered = file_stream.filter(lambda file: file.endswith(".csv"))
```

### Kafka Source

```python
from pyspark.streaming.kafka import KafkaUtils

# Direct approach (recommended)
kafka_stream = KafkaUtils.createDirectStream(
    ssc,
    ["topic1", "topic2"],
    {
        "metadata.broker.list": "broker1:9092,broker2:9092",
        "group.id": "streaming-group",
        "auto.offset.reset": "largest"
    }
)

# Get messages
kafka_stream.foreachRDD(lambda rdd: process_kafka_rdd(rdd))

# With key-value
key_value_stream = kafka_stream.map(lambda message: (message.key, message.value))
```

### Kinesis Source

```python
from pyspark.streaming.kinesis import KinesisUtils, InitialPositionInStream

kinesis_stream = KinesisUtils.createStream(
    ssc,
    "myStream",                    # Stream name
    "kinesis-stream-name",         # Kinesis stream name
    "https://kinesis.us-east-1.amazonaws.com",  # Endpoint
    "myApp",                       # App name
    "initial-position",            # Initial position
    InitialPositionInStream.LATEST,
    5,                             # Checkpoint interval
    "aws_access_key_id",           # AWS access key
    "aws_secret_access_key"        # AWS secret key
)
```

### Queue Source

```python
# Create queue stream
rdd_queue = [
    sc.parallelize([1, 2, 3]),
    sc.parallelize([4, 5, 6]),
    sc.parallelize([7, 8, 9])
]

queue_stream = ssc.queueStream(rdd_queue, oneAtATime=True)

# Process queue
queue_stream.pprint()
```

---

## Output Operations

### Console Output

```python
# Print to console
stream.pprint()

# Print with limit
stream.pprint(num=10)
```

### File Output

```python
# Save as text files
stream.saveAsTextFiles("hdfs://output/text/", "txt")

# Save as object files
stream.saveAsObjectFiles("hdfs://output/object/")

# Save as sequence files
stream.saveAsSequenceFiles("hdfs://output/sequence/")
```

### foreachRDD

```python
# Process each RDD
def process_rdd(rdd):
    if not rdd.isEmpty():
        # Get SparkContext
        sc = rdd.context

        # Process data
        results = rdd.collect()

        # Save to external system
        for result in results:
            save_to_database(result)

stream.foreachRDD(process_rdd)

# With partition-level processing
def process_partition(partition_index, rdd):
    if not rdd.isEmpty():
        # Process partition
        for record in rdd:
            process_record(record)

stream.foreachRDD(process_partition)
```

### External System Integration

```python
# Save to Cassandra
def save_to_cassandra(rdd):
    if not rdd.isEmpty():
        rdd.foreachPartition(save_partition_to_cassandra)

def save_partition_to_cassandra(partition):
    from cassandra.cluster import Cluster
    cluster = Cluster(['cassandra-node'])
    session = cluster.connect('keyspace')

    for record in partition:
        session.execute(
            "INSERT INTO table (key, value) VALUES (%s, %s)",
            (record[0], record[1])
        )

stream.foreachRDD(save_to_cassandra)

# Save to Redis
def save_to_redis(rdd):
    if not rdd.isEmpty():
        rdd.foreachPartition(save_partition_to_redis)

def save_partition_to_redis(partition):
    import redis
    r = redis.Redis(host='redis-host', port=6379)

    for record in partition:
        r.set(record[0], record[1])

stream.foreachRDD(save_to_redis)
```

---

## Window Operations

### Basic Window Operations

```python
from pyspark.streaming import Window

# Tumbling window (non-overlapping)
tumbling_window = window(stream, windowDuration=30, slideDuration=30)

# Sliding window (overlapping)
sliding_window = window(stream, windowDuration=60, slideDuration=10)

# Window with reduce
windowed_counts = words.map(lambda word: (word, 1)) \
    .reduceByKeyAndWindow(
        lambda a, b: a + b,  # Add
        lambda a, b: a - b,  # Subtract (optional, for efficiency)
        windowDuration=30,
        slideDuration=10
    )
```

### Window Transformations

```python
# Count per window
windowed_counts = words.map(lambda word: (word, 1)) \
    .reduceByKeyAndWindow(lambda a, b: a + b, 30, 10)

# Join windows
windowed_join = window1.join(window2)

# Window with inverse reduce (more efficient)
windowed_counts = words.map(lambda word: (word, 1)) \
    .reduceByKeyAndWindow(
        lambda a, b: a + b,  # Add function
        lambda a, b: a - b,  # Subtract function
        30,  # Window duration
        10   # Slide duration
    )

# Count window
count_window = stream.countByWindow(30, 10)

# Count by value and window
count_by_value_window = words.countByValueAndWindow(30, 10)
```

### Window Duration and Slide Duration

```python
# Window duration: Time range of the window
# Slide duration: How often the window moves

# Tumbling window: windowDuration == slideDuration
tumbling = window(stream, 30, 30)  # 30-second tumbling windows

# Sliding window: slideDuration < windowDuration
sliding = window(stream, 60, 10)  # 60-second windows, updated every 10 seconds

# Session window: Not directly supported in DStreams
# Use Structured Streaming for session windows
```

---

## State Management

### updateStateByKey

```python
# Maintain state across batches
def update_function(new_values, running_count):
    return (running_count or 0) + sum(new_values)

running_counts = words.map(lambda word: (word, 1)) \
    .updateStateByKey(update_function)

# With checkpointing
ssc.checkpoint("hdfs://checkpoint/path")
running_counts = words.map(lambda word: (word, 1)) \
    .updateStateByKey(update_function)
```

### Stateful Operations

```python
# Track user sessions
def update_session(new_events, existing_session):
    if existing_session is None:
        return new_events
    return existing_session + new_events

session_stream = events.map(lambda e: (e.user_id, e)) \
    .updateStateByKey(update_session)

# Maintain sliding window state
def update_sliding_state(new_values, current_state):
    if current_state is None:
        current_state = []
    current_state.extend(new_values)
    # Keep only recent values
    return current_state[-100:]

stateful_stream = stream.map(lambda x: (x.key, x)) \
    .updateStateByKey(update_sliding_state)
```

### Checkpointing

```python
# Enable checkpointing
ssc.checkpoint("hdfs://checkpoint/path")

# Checkpoint interval
ssc.checkpoint("hdfs://checkpoint/path")

# Recover from checkpoint
ssc = StreamingContext.getOrCreate("hdfs://checkpoint/path", create_context)
```

---

## Fault Tolerance

### Write-Ahead Logs (WAL)

```python
# Enable WAL for reliable recovery
sparkConf.set("spark.streaming.receiver.writeAheadLog.enable", True)

# Receiver reliability
class ReliableReceiver(Receiver):
    def onStart(self):
        self.thread = Thread(target=self.receive)
        self.thread.start()

    def receive(self):
        try:
            while True:
                data = receive_data()
                self.store(data)
                # Acknowledge data
                acknowledge(data)
        except Exception as e:
            self.restart("Error receiving data", e)

    def onStop(self):
        self.thread.stop()
```

### Exactly-Once Semantics

```python
# Use direct Kafka approach for exactly-once
kafka_stream = KafkaUtils.createDirectStream(
    ssc,
    ["topic"],
    {"metadata.broker.list": "broker:9092"}
)

# Process with transaction
def process_rdd(rdd):
    if not rdd.isEmpty():
        # Start transaction
        transaction = start_transaction()

        try:
            # Process data
            results = process_data(rdd)

            # Save to external system
            save_results(results)

            # Commit transaction
            transaction.commit()
        except Exception as e:
            # Rollback transaction
            transaction.rollback()
            raise e

kafka_stream.foreachRDD(process_rdd)
```

### Checkpointing for Fault Tolerance

```python
# Enable checkpointing
ssc.checkpoint("hdfs://checkpoint/path")

# Checkpoint interval
ssc.checkpoint("hdfs://checkpoint/path")

# Stateful operations require checkpointing
running_counts = words.map(lambda word: (word, 1)) \
    .updateStateByKey(update_function)

# Recover from checkpoint
def create_context():
    sc = SparkContext("local[*]", "StreamingApp")
    ssc = StreamingContext(sc, 5)
    # Setup stream
    return ssc

ssc = StreamingContext.getOrCreate("hdfs://checkpoint/path", create_context)
```

---

## Performance Tuning

### Batch Interval

```python
# Optimal batch interval depends on processing time
# Target: Processing time < Batch interval

# Test different batch intervals
for interval in [1, 2, 5, 10]:
    ssc = StreamingContext(sc, interval)
    # Setup stream
    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
```

### Parallelism

```python
# Set number of receiver threads
sparkConf.set("spark.streaming.concurrentReceivers", 2)

# Set number of executor cores
sparkConf.set("spark.executor.cores", 4)

# Set number of executors
sparkConf.set("spark.executor.instances", 10)
```

### Backpressure

```python
# Enable backpressure
sparkConf.set("spark.streaming.backpressure.enabled", True)

# Set initial rate
sparkConf.set("spark.streaming.backpressure.initialRate", 1000)

# Set rate limit
sparkConf.set("spark.streaming.receiver.maxRate", 1000)
sparkConf.set("spark.streaming.kafka.maxRatePerPartition", 1000)
```

### Memory Management

```python
# Set receiver memory
sparkConf.set("spark.streaming.receiver.maxRate", 1000)

# Set block interval
sparkConf.set("spark.streaming.blockInterval", 200)

# Set receiver buffer size
sparkConf.set("spark.streaming.receiver.writeAheadLog.rollingIntervalSecs", 60)

# Set compression
sparkConf.set("spark.rdd.compress", True)
sparkConf.set("spark.shuffle.compress", True)
```

### Monitoring

```python
# Enable metrics
sparkConf.set("spark.metrics.conf", "metrics.properties")

# Monitor batch processing time
streamingMetrics = ssc.sparkContext._jsc.sc().getStreamingMetrics()

# Track batch processing time
def process_batch(time, rdd):
    processing_time = System.currentTimeMillis() - time
    print(f"Processing time: {processing_time}ms")

stream.foreachRDD(process_batch)
```

---

## Spark Structured Streaming

### Overview

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder \
    .appName("StructuredStreaming") \
    .getOrCreate()

# Read from Kafka
df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "events") \
    .load()

# Parse JSON
schema = StructType() \
    .add("user_id", StringType()) \
    .add("event_type", StringType()) \
    .add("timestamp", TimestampType())

events = df \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*")

# Windowed aggregation
windowed_counts = events \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "10 minutes", "5 minutes"),
        "event_type"
    ) \
    .count()

# Write to console
query = windowed_counts \
    .writeStream \
    .outputMode("update") \
    .format("console") \
    .start()

query.awaitTermination()
```

### Output Modes

```python
# Append mode (new rows only)
query = df.writeStream \
    .outputMode("append") \
    .format("console") \
    .start()

# Complete mode (complete result table)
query = df.writeStream \
    .outputMode("complete") \
    .format("console") \
    .start()

# Update mode (changed rows only)
query = df.writeStream \
    .outputMode("update") \
    .format("console") \
    .start()
```

### Watermarking

```python
# Handle late data
events_with_watermark = events \
    .withWatermark("timestamp", "10 minutes") \
    .groupBy(
        window("timestamp", "10 minutes"),
        "event_type"
    ) \
    .count()

# Late data handling
events_with_watermark = events \
    .withWatermark("event_time", "30 seconds") \
    .groupBy(
        window("event_time", "10 seconds"),
        "user"
    ) \
    .count()
```

### State Management

```python
# MapGroupsWithState (complex state)
from pyspark.sql.streaming import GroupState, GroupStateTimeout

def update_state(user_id, events, state: GroupState):
    if state.hasTimedOut:
        # State timeout handling
        state.remove()
        return (user_id, "timeout", None)
    elif state.exists:
        # Update existing state
        current_state = state.get
        new_state = update(current_state, events)
        state.update(new_state)
        state.setTimeoutDuration("30 seconds")
        return (user_id, "updated", new_state)
    else:
        # New state
        state.update(events)
        state.setTimeoutDuration("30 seconds")
        return (user_id, "new", events)

result = events \
    .groupByKey(lambda x: x.user_id) \
    .mapGroupsWithState(update_state)
```

---

## Best Practices

### Design Principles

1. **Idempotent operations**: Handle duplicate processing gracefully
2. **Exactly-once semantics**: Use checkpointing and WAL
3. **Backpressure**: Prevent overwhelming downstream systems
4. **Monitoring**: Track batch processing times and lag

### Performance Optimization

1. **Tune batch interval**: Balance latency and throughput
2. **Optimize serialization**: Use Kryo serializer
3. **Manage state**: Regularly clean up old state
4. **Use direct Kafka approach**: Better fault tolerance

### Common Patterns

```python
# Exactly-once processing with Kafka
def process_batch(rdd):
    if not rdd.isEmpty():
        # Start transaction
        transaction = start_transaction()

        try:
            # Process data
            results = rdd.map(process_record)

            # Save to sink
            save_results(results)

            # Commit transaction
            transaction.commit()
        except Exception as e:
            transaction.rollback()
            raise e

kafka_stream.foreachRDD(process_batch)

# Deduplication
def deduplicate(rdd):
    if not rdd.isEmpty():
        # Use window for deduplication
        pass

stream.foreachRDD(deduplicate)
```

---

## Examples

### Real-Time Log Processing

```python
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

# Create streaming context
ssc = StreamingContext(sc, 5)
ssc.checkpoint("hdfs://checkpoint/logs")

# Read from Kafka
kafka_stream = KafkaUtils.createDirectStream(
    ssc,
    ["logs"],
    {"metadata.broker.list": "broker:9092"}
)

# Parse logs
logs = kafka_stream.map(lambda x: x[1])

# Filter errors
errors = logs.filter(lambda line: "ERROR" in line)

# Count errors by type
error_counts = errors.map(
    lambda line: (line.split(" ")[2], 1)
).reduceByKey(lambda a, b: a + b)

# Window aggregation
windowed_errors = error_counts.reduceByKeyAndWindow(
    lambda a, b: a + b,
    lambda a, b: a - b,
    300,  # 5 minutes
    60    # 1 minute
)

# Save to database
def save_to_db(rdd):
    if not rdd.isEmpty():
        for error_type, count in rdd.collect():
            save_error_count(error_type, count)

windowed_errors.foreachRDD(save_to_db)

# Start streaming
ssc.start()
ssc.awaitTermination()
```

### Real-Time Analytics

```python
# Read from Kafka
events = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "events") \
    .load()

# Parse events
parsed_events = events \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*")

# Real-time aggregation
real_time_metrics = parsed_events \
    .withWatermark("timestamp", "1 minute") \
    .groupBy(
        window("timestamp", "1 minute"),
        "event_type"
    ) \
    .agg(
        count("*").alias("event_count"),
        countDistinct("user_id").alias("unique_users")
    )

# Write to dashboard
query = real_time_metrics \
    .writeStream \
    .outputMode("update") \
    .format("memory") \
    .queryName("real_time_metrics") \
    .start()

# Query results
spark.sql("SELECT * FROM real_time_metrics").show()
```

---

## References

- [Spark Streaming Programming Guide](https://spark.apache.org/docs/latest/streaming-programming-guide.html)
- [Structured Streaming Guide](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html)
- [Spark Streaming Kafka Integration](https://spark.apache.org/docs/latest/streaming-kafka-0-10-integration.html)
- [Spark Streaming Examples](https://spark.apache.org/examples.html)
- [High Performance Spark](http://shop.oreilly.com/product/0636920028512.do)
