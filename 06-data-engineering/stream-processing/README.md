# Stream Processing

## Table of Contents

- [Overview](#overview)
- [Stream Processing Concepts](#stream-processing-concepts)
- [Stream Processing Architecture](#stream-processing-architecture)
- [Stream Processing Tools](#stream-processing-tools)
- [Stream Processing Patterns](#stream-processing-patterns)
- [Performance Optimization](#performance-optimization)
- [Best Practices](#best-practices)
- [References](#references)

---

## Overview

Stream processing is a data processing approach that processes data in real-time
as it arrives, enabling immediate insights and actions. It is designed for
applications requiring low-latency processing of continuous data streams.

### Key Characteristics

- **Real-time processing**: Milliseconds to seconds latency
- **Continuous processing**: Process data as it arrives
- **Event-driven**: React to events as they occur
- **Stateful processing**: Maintain state across events
- **Fault tolerant**: Recover from failures without data loss

### When to Use Stream Processing

- Real-time monitoring and alerting
- Fraud detection and anomaly detection
- Real-time analytics and dashboards
- IoT data processing
- Log processing and analysis

### Stream vs Batch Processing

| Feature | Stream Processing | Batch Processing |
|---------|------------------|------------------|
| Latency | Milliseconds to seconds | Minutes to hours |
| Data Volume | Variable | High (TB-PB) |
| Processing | Continuous | Scheduled |
| Complexity | Higher | Lower |
| Cost | Higher | Lower |
| Use Case | Real-time monitoring | Reporting, analytics |

---

## Stream Processing Concepts

### Event Time vs Processing Time

```python
# Event time: When the event occurred
# Processing time: When the event is processed

# Example
event = {
    "event_id": "123",
    "user_id": "user1",
    "event_type": "click",
    "event_time": "2024-01-01T10:00:00Z",  # Event time
    "processing_time": "2024-01-01T10:00:05Z"  # Processing time
}

# Handling late events
from pyspark.sql.functions import window, col

# Window based on event time
windowed_counts = df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "10 minutes", "5 minutes"),
        "event_type"
    ) \
    .count()
```

### Windows

```python
# Tumbling window (non-overlapping)
tumbling_window = window("event_time", "10 minutes")

# Sliding window (overlapping)
sliding_window = window("event_time", "10 minutes", "5 minutes")

# Session window (variable length)
# Custom implementation using flatMapGroupsWithState

# Window aggregation
windowed_counts = df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "10 minutes", "5 minutes"),
        "event_type"
    ) \
    .count()
```

### Watermarking

```python
# Define watermark for late data handling
watermarked_df = df \
    .withWatermark("event_time", "10 minutes")

# Late events are dropped after watermark
# State is cleaned up after watermark passes
```

### State Management

```python
from pyspark.sql.streaming import GroupState, GroupStateTimeout

# Maintain state across events
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

---

## Stream Processing Architecture

### Architecture Patterns

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Stream Processing Architecture                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  Data Sources                                                        │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐              │
│  │  Kafka   │ │  Kinesis │ │   Files  │ │   APIs   │              │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘              │
│                           │                                          │
│  Message Broker           │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐                                                  │  │
│  │  │  Kafka   │                                                  │  │
│  │  └──────────┘                                                  │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Stream Processing        │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │  Flink   │ │  Spark   │ │  Kafka   │ │  Storm   │       │  │
│  │  │          │ │Streaming │ │ Streams  │ │          │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Serving Layer            │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐       │  │
│  │  │   HBase  │ │ Cassandra│ │   Redis  │ │  Elastic │       │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └──────────┘       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                           │                                          │
│  Monitoring Layer        │                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  Metrics │ Logs │ Alerts │ Dashboards                       │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Stream Processing Models

```python
# Micro-batch processing (Spark Streaming)
# Data processed in small batches
# Latency: Seconds
# Throughput: High

# Continuous processing (Flink)
# Data processed continuously
# Latency: Milliseconds
# Throughput: Lower than micro-batch

# Hybrid processing (Structured Streaming)
# Micro-batch with continuous mode option
# Latency: Milliseconds to seconds
# Throughput: High
```

---

## Stream Processing Tools

### Apache Flink

```java
// Flink stream processing
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

// Read from Kafka
DataStream<String> input = env.addSource(
    new FlinkKafkaConsumer<>("topic", new SimpleStringSchema(), properties)
);

// Transform
DataStream<Event> events = input
    .map(line -> parseEvent(line))
    .filter(event -> event.isValid());

// Window aggregation
DataStream<AggregatedEvent> aggregated = events
    .keyBy(event -> event.getType())
    .window(TumblingEventTimeWindows.of(Time.minutes(5)))
    .aggregate(new EventAggregator());

// Write to sink
aggregated.addSink(new FlinkKafkaProducer<>("output-topic", new EventSchema(), properties));

env.execute("Stream Processing Job");
```

### Apache Spark Structured Streaming

```python
from pyspark.sql import SparkSession
from pyspark.sql.functions import *

spark = SparkSession.builder.appName("StreamProcessing").getOrCreate()

# Read from Kafka
df = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "events") \
    .load()

# Parse events
parsed_df = df \
    .selectExpr("CAST(value AS STRING)") \
    .select(from_json(col("value"), schema).alias("data")) \
    .select("data.*")

# Windowed aggregation
windowed_counts = parsed_df \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "10 minutes", "5 minutes"),
        "event_type"
    ) \
    .count()

# Write to sink
query = windowed_counts.writeStream \
    .outputMode("update") \
    .format("console") \
    .start()

query.awaitTermination()
```

### Apache Kafka Streams

```java
// Kafka Streams processing
StreamsBuilder builder = new StreamsBuilder();

// Read from topic
KStream<String, String> source = builder.stream("input-topic");

// Transform
KStream<String, Event> events = source
    .mapValues(value -> parseEvent(value))
    .filter((key, event) -> event.isValid());

// Window aggregation
KTable<Windowed<String>, Long> aggregated = events
    .groupByKey()
    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
    .count();

// Write to output topic
aggregated.toStream().to("output-topic");

// Start streams
KafkaStreams streams = new KafkaStreams(builder.build(), config);
streams.start();
```

### Apache Storm

```java
// Storm topology
TopologyBuilder builder = new TopologyBuilder();

// Spout (data source)
builder.setSpout("kafka-spout", new KafkaSpout(), 1);

// Bolts (processing)
builder.setBolt("parse-bolt", new ParseBolt(), 2)
    .shuffleGrouping("kafka-spout");

builder.setBolt("aggregate-bolt", new AggregateBolt(), 2)
    .fieldsGrouping("parse-bolt", new Fields("event_type"));

builder.setBolt("output-bolt", new OutputBolt(), 1)
    .shuffleGrouping("aggregate-bolt");

// Submit topology
StormSubmitter.submitTopology("stream-processing", config, builder.createTopology());
```

---

## Stream Processing Patterns

### Event Processing Pattern

```python
# Process individual events
def process_event(event):
    """Process single event"""
    # Validate event
    if not validate_event(event):
        return None

    # Transform event
    transformed = transform_event(event)

    # Enrich event
    enriched = enrich_event(transformed)

    return enriched

# Apply to stream
processed = events.map(process_event)
```

### Windowed Aggregation Pattern

```python
# Aggregate events within windows
windowed_counts = events \
    .withWatermark("event_time", "10 minutes") \
    .groupBy(
        window("event_time", "10 minutes", "5 minutes"),
        "event_type"
    ) \
    .count()
```

### Stateful Processing Pattern

```python
# Maintain state across events
def update_state(user_id, events, state: GroupState):
    """Update state with new events"""
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

result = events \
    .groupByKey(lambda x: x.user_id) \
    .mapGroupsWithState(update_state)
```

### Pattern Detection Pattern

```python
# Detect patterns in event sequences
def detect_pattern(event_sequence):
    """Detect pattern in event sequence"""
    # Check for specific pattern
    if len(event_sequence) >= 3:
        if (event_sequence[-3].type == "login" and
            event_sequence[-2].type == "view" and
            event_sequence[-1].type == "purchase"):
            return "purchase_funnel"

    return None

# Apply pattern detection
patterns = events \
    .groupByKey(lambda x: x.user_id) \
    .mapGroupsWithState(detect_pattern)
```

---

## Performance Optimization

### Throughput Optimization

```python
# Increase parallelism
spark.conf.set("spark.sql.shuffle.partitions", 200)

# Use micro-batch processing
query = df.writeStream \
    .trigger(processingTime="10 seconds") \
    .start()

# Optimize serialization
spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
```

### Latency Optimization

```python
# Use continuous processing (experimental)
query = df.writeStream \
    .trigger(continuous="1 second") \
    .start()

# Reduce batch interval
query = df.writeStream \
    .trigger(processingTime="1 second") \
    .start()

# Optimize state management
spark.conf.set("spark.sql.streaming.stateStore.providerClass",
    "org.apache.spark.sql.execution.streaming.state.HDFSBackedStateStoreProvider")
```

### Backpressure

```python
# Enable backpressure
spark.conf.set("spark.streaming.backpressure.enabled", True)

# Set rate limit
spark.conf.set("spark.streaming.receiver.maxRate", 1000)
spark.conf.set("spark.streaming.kafka.maxRatePerPartition", 1000)
```

### State Management

```python
# Optimize state storage
spark.conf.set("spark.sql.streaming.stateStore.compression", True)
spark.conf.set("spark.sql.streaming.stateStore.minPartitions", 4)

# Configure state timeout
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
```

---

## Best Practices

### Event Design

1. **Use immutable events**: Events should not be modified after creation
2. **Include event time**: Always include timestamp when event occurred
3. **Use meaningful event types**: Clear, descriptive event names
4. **Include correlation IDs**: For tracing events across systems

### State Management

1. **Use appropriate state backend**: Choose based on use case
2. **Implement state cleanup**: Remove old state to prevent memory issues
3. **Handle state timeouts**: Define appropriate timeout durations
4. **Monitor state size**: Track state growth over time

### Error Handling

1. **Implement dead letter queues**: Capture failed events
2. **Use idempotent processing**: Handle duplicate events gracefully
3. **Implement retry mechanisms**: For transient failures
4. **Monitor error rates**: Track and alert on errors

### Monitoring

1. **Track processing latency**: Monitor end-to-end latency
2. **Monitor throughput**: Track events processed per second
3. **Alert on anomalies**: Set up alerts for unusual patterns
4. **Log processing details**: For debugging and auditing

---

## References

- [Apache Flink Documentation](https://flink.apache.org/docs/)
- [Spark Structured Streaming](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html)
- [Kafka Streams Documentation](https://kafka.apache.org/documentation/streams/)
- [Apache Storm Documentation](https://storm.apache.org/releases/)
- [Stream Processing Patterns](https://www.oreilly.com/library/view/stream-processing-patterns/9781492028161/)
- [Real-Time Stream Processing](https://www.oreilly.com/library/view/real-time-stream-processing/9781492028161/)
