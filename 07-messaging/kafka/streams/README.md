# Kafka Streams

## Kafka Streams API, State Stores, Windowing, and Stream Processing

---

## Table of Contents

- [Overview](#overview)
- [Kafka Streams Architecture](#kafka-streams-architecture)
- [Stream Processing](#stream-processing)
- [State Stores](#state-stores)
- [Windowing](#windowing)
- [Topology](#topology)
- [Exactly-Once Semantics](#exactly-once-semantics)
- [Performance Tuning](#performance-tuning)
- [Best Practices](#best-practices)

---

## Overview

Kafka Streams is a client library for building streaming applications that process data stored in Kafka. It provides a high-level DSL and low-level Processor API for stream processing.

### Key Features

- **Client Library**: No separate cluster needed
- **Exactly-Once**: Built-in exactly-once semantics
- **Stateful Processing**: Local state stores with changelog topics
- **Windowing**: Multiple windowing strategies
- **High-Level DSL**: Simple API for common operations
- **Low-Level API**: Fine-grained control for complex processing

### When to Use Kafka Streams

- Real-time data transformations
- Event-driven microservices
- Stream-table joins
- Anomaly detection
- Real-time analytics

---

## Kafka Streams Architecture

### Processing Topology

```
┌─────────────────────────────────────────────────────────────┐
│                   Kafka Streams Application                   │
│                                                              │
│  Source ──▶ Processor ──▶ Processor ──▶ Sink                │
│                                                              │
│  Source: Reads from Kafka topic                              │
│  Processor: Transforms data                                  │
│  Sink: Writes to Kafka topic                                 │
└─────────────────────────────────────────────────────────────┘
         │                                    │
         ▼                                    ▼
┌──────────────┐                    ┌──────────────┐
│  Kafka Topic │                    │  Kafka Topic │
│  (Input)     │                    │  (Output)    │
└──────────────┘                    └──────────────┘
```

### Application Instances

```
Multiple instances processing different partitions:

Instance 1: Partitions [0, 1]
┌─────────────────────────────────────────────────────────────┐
│  Source ──▶ Process ──▶ Sink                                │
└─────────────────────────────────────────────────────────────┘
         │                                    │
         ▼                                    ▼
┌──────────────┐                    ┌──────────────┐
│  Topic Part  │                    │  Topic Part  │
│  0, 1        │                    │  0, 1        │
└──────────────┘                    └──────────────┘

Instance 2: Partitions [2, 3]
┌─────────────────────────────────────────────────────────────┐
│  Source ──▶ Process ──▶ Sink                                │
└─────────────────────────────────────────────────────────────┘
         │                                    │
         ▼                                    ▼
┌──────────────┐                    ┌──────────────┐
│  Topic Part  │                    │  Topic Part  │
│  2, 3        │                    │  2, 3        │
└──────────────┘                    └──────────────┘
```

---

## Stream Processing

### High-Level DSL

```java
StreamsBuilder builder = new StreamsBuilder();

// Simple transformation
KStream<String, String> stream = builder.stream("input-topic");

KStream<String, String> transformed = stream
    .filter((key, value) -> value != null)
    .mapValues(value -> value.toUpperCase())
    .selectKey((key, value) -> key + "-processed");

transformed.to("output-topic");

// Build and start
KafkaStreams streams = new KafkaStreams(builder.build(), config);
streams.start();
```

### Common Operations

```java
// Filter
KStream<String, String> filtered = stream
    .filter((key, value) -> value.contains("important"));

// Map
KStream<String, String> mapped = stream
    .map((key, value) -> KeyValue.pair(key.toUpperCase(), value));

// FlatMap
KStream<String, String> flatMapped = stream
    .flatMap((key, value) -> {
        List<KeyValue<String, String>> result = new ArrayList<>();
        for (String word : value.split(" ")) {
            result.add(KeyValue.pair(key, word));
        }
        return result;
    });

// Group By Key
KGroupedStream<String, String> grouped = stream.groupByKey();

// Reduce (aggregation)
KTable<String, String> reduced = grouped.reduce(
    (value1, value2) -> value1 + "," + value2
);

// Count
KTable<String, Long> counted = grouped.count();
```

### Table Operations

```java
// Create table from stream
KTable<String, String> table = builder.table("table-topic");

// Join stream with table
KStream<String, String> joined = stream.join(
    table,
    (streamValue, tableValue) -> streamValue + "-" + tableValue,
    Joined.with(Serdes.String(), Serdes.String(), Serdes.String())
);

// Left join
KStream<String, String> leftJoined = stream.leftJoin(
    table,
    (streamValue, tableValue) -> {
        if (tableValue == null) {
            return streamValue;
        }
        return streamValue + "-" + tableValue;
    }
);
```

---

## State Stores

### Key-Value Store

```java
// Create state store
StoreBuilder<KeyValueStore<String, String>> storeBuilder = 
    Stores.keyValueStoreBuilder(
        Stores.persistentKeyValueStore("my-store"),
        Serdes.String(),
        Serdes.String()
    );

// Add to topology
builder.addStateStore(storeBuilder);

// Use in processor
KStream<String, String> stream = builder.stream("input-topic");

stream.transformValues(
    () -> new ValueTransformer<String, String>() {
        private KeyValueStore<String, String> store;
        
        @Override
        public void init(ProcessorContext context) {
            this.store = context.getStateStore("my-store");
        }
        
        @Override
        public String transform(String value) {
            String existing = store.get(value);
            store.put(value, value);
            return existing;
        }
        
        @Override
        public void close() {}
    },
    "my-store"
);
```

### Window Store

```java
// Create window store
StoreBuilder<WindowStore<String, Long>> windowStoreBuilder = 
    Stores.windowStoreBuilder(
        Stores.persistentWindowStore("window-store",
            Duration.ofMinutes(5),    // retention
            Duration.ofMinutes(1),    // size
            false                     // retain duplicates
        ),
        Serdes.String(),
        Serdes.Long()
    );

// Use in processor
stream.process(
    () -> new Processor<String, String, Void>() {
        private WindowStore<String, Long> windowStore;
        
        @Override
        public void init(ProcessorContext<Void> context) {
            this.windowStore = context.getStateStore("window-store");
        }
        
        @Override
        public void process(Record<String, String> record) {
            windowStore.put(record.key(), 1L, record.timestamp());
        }
    },
    "window-store"
);
```

### Session Store

```java
// Create session store
StoreBuilder<SessionStore<String, Long>> sessionStoreBuilder = 
    Stores.sessionStoreBuilder(
        Stores.persistentSessionStore("session-store",
            Duration.ofMinutes(5)  // retention
        ),
        Serdes.String(),
        Serdes.Long()
    );

// Use for session windowing
stream.groupByKey()
    .windowedBy(SessionWindows.ofNoGapWithGrace(Duration.ofMinutes(5)))
    .aggregate(
        () -> 0L,
        (key, value, current) -> current + 1,
        (key, value, current) -> current - 1,
        Materialized.as("session-store")
            .withKeySerde(Serdes.String())
            .withValueSerde(Serdes.Long())
    );
```

### Changelog Topics

```
State Store Changelog:

State Store: "my-store"
Changelog Topic: "my-store-changelog"

When state store updated:
1. Update local state store
2. Write to changelog topic
3. On failure, replay changelog topic to restore state

┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Processor  │────▶│  State      │────▶│  Changelog  │
│             │     │  Store      │     │  Topic      │
└─────────────┘     └─────────────┘     └─────────────┘
       │
       ▼
┌─────────────┐
│  Kafka      │
│  (Output)   │
└─────────────┘
```

---

## Windowing

### Tumbling Window

```java
// Fixed-size, non-overlapping windows
stream.groupByKey()
    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
    .count()
    .toStream()
    .map((windowedKey, count) -> 
        KeyValue.pair(
            windowedKey.key() + "-" + windowedKey.window().startTime(),
            count
        )
    )
    .to("output-topic");

// Window 1: [00:00 - 00:05)
// Window 2: [00:05 - 00:10)
// Window 3: [00:10 - 00:15)
```

### Hopping Window

```java
// Fixed-size, overlapping windows
stream.groupByKey()
    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5))
        .advanceBy(Duration.ofMinutes(1)))
    .count()
    .toStream()
    .map((windowedKey, count) -> 
        KeyValue.pair(
            windowedKey.key() + "-" + windowedKey.window().startTime(),
            count
        )
    )
    .to("output-topic");

// Window 1: [00:00 - 00:05)
// Window 2: [00:01 - 00:06)
// Window 3: [00:02 - 00:07)
```

### Sliding Window

```java
// Dynamic-size windows based on event time
stream.groupByKey()
    .windowedBy(SlidingWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5)))
    .count()
    .toStream()
    .map((windowedKey, count) -> 
        KeyValue.pair(
            windowedKey.key() + "-" + windowedKey.window().startTime(),
            count
        )
    )
    .to("output-topic");
```

### Session Window

```java
// Activity sessions with gap-based merging
stream.groupByKey()
    .windowedBy(SessionWindows.ofNoGapWithGrace(Duration.ofMinutes(5)))
    .count()
    .toStream()
    .map((windowedKey, count) -> 
        KeyValue.pair(
            windowedKey.key() + "-" + windowedKey.window().startTime(),
            count
        )
    )
    .to("output-topic");

// Sessions merged when gap < threshold
// Session 1: [00:00 - 00:02]
// Session 2: [00:02 - 00:04]  ← Merged with Session 1
// Session 3: [00:10 - 00:12]  ← New session (gap > threshold)
```

### Window Comparison

| Window Type | Overlap | Use Case |
|-------------|---------|----------|
| Tumbling | No | Fixed intervals, no overlap |
| Hopping | Yes | Fixed intervals, overlap needed |
| Sliding | Yes | Event-based, dynamic |
| Session | Yes | Activity tracking, gaps |

---

## Topology

### Topology Description

```java
StreamsBuilder builder = new StreamsBuilder();
KafkaStreams streams = new KafkaStreams(builder.build(), config);

// Print topology
System.out.println(streams.toString());
```

### Topology Visualization

```
Sub-topologies:
  Sub-topology 0:
    SOURCE: KSTREAM-SOURCE-0000000000
      (topics: [input-topic])
      --> KSTREAM-FILTER-0000000001
    
    KSTREAM-FILTER-0000000001
      (predicate: (key, value) -> value != null)
      --> KSTREAM-MAPVALUES-0000000002
    
    KSTREAM-MAPVALUES-0000000002
      (valueMapper: value -> value.toUpperCase())
      --> KSTREAM-SELECTKEY-0000000003
    
    KSTREAM-SELECTKEY-0000000003
      (keySelector: (key, value) -> key + "-processed")
      --> KSTREAM-SINK-0000000004
    
    SINK: KSTREAM-SINK-0000000004
      (topic: output-topic)
```

### Processor API

```java
Topology topology = new Topology();

topology.addSource("Source", "input-topic")
    .addProcessor("Process", 
        () -> new Processor<String, String, String>() {
            private ProcessorContext<String> context;
            
            @Override
            public void init(ProcessorContext<String> context) {
                this.context = context;
            }
            
            @Override
            public void process(Record<String, String> record) {
                String value = record.value().toUpperCase();
                context.forward(new Record<>(
                    record.key(), 
                    value, 
                    record.timestamp()
                ));
            }
        },
        "Source"
    )
    .addSink("Sink", "output-topic", "Process");
```

---

## Exactly-Once Semantics

### Configuration

```java
Properties props = new Properties();
props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-app");
props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, 
    StreamsConfig.EXACTLY_ONCE_V2);
props.put(StreamsConfig.producerPrefix(ProducerConfig.TRANSACTIONAL_ID_CONFIG),
    "my-stream-app-transactional");
```

### Exactly-Once Flow

```
┌─────────────────────────────────────────────────────────────┐
│              Exactly-Once Processing                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. Read from input topic (consume with EOS)                │
│  2. Process records                                         │
│  3. Write to output topic (produce with EOS)                │
│  4. Commit consumer offsets (transactional)                 │
│                                                             │
│  All operations in single transaction:                      │
│  ┌────────────────────────────────────────────────────┐    │
│  │  Read → Process → Write → Commit (atomic)          │    │
│  └────────────────────────────────────────────────────┘    │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### EOS Versions

| Version | Description |
|---------|-------------|
| `exactly_once_beta` | Deprecated, use v2 |
| `exactly_once_v2` | Improved EOS, better performance |
| `exactly_once` | Legacy, use v2 |

---

## Performance Tuning

### Configuration

```java
Properties props = new Properties();

// Processing guarantee
props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, 
    StreamsConfig.AT_LEAST_ONCE);

// Commit interval
props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

// Cache max bytes
props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 10 * 1024 * 1024);

// Number of threads
props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 4);

// State directory
props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
```

### Parallelism

```
Thread Distribution:

4 threads, 12 partitions:
Thread 1: Partitions [0, 1, 2]
Thread 2: Partitions [3, 4, 5]
Thread 3: Partitions [6, 7, 8]
Thread 4: Partitions [9, 10, 11]

Each thread processes partitions sequentially
```

### Monitoring

```java
// Get metrics
Metrics metrics = streams.metrics();

// Key metrics:
// - stream-task-count: Active tasks
// - process-rate: Records processed per second
// - commit-rate: Commits per second
// - record-lateness: Event time vs processing time
```

---

## Best Practices

### Design

1. **Use appropriate windowing** - Match window type to use case
2. **Design for parallelism** - Partition data appropriately
3. **Handle late events** - Use grace periods
4. **Use state stores wisely** - Consider size and access patterns

### Performance

1. **Tune cache settings** - Reduce downstream writes
2. **Adjust commit interval** - Balance latency vs throughput
3. **Use multiple threads** - Parallelize processing
4. **Monitor state store size** - Prevent out-of-memory

### Reliability

1. **Enable exactly-once** - For critical applications
2. **Handle rebalances gracefully** - Use CooperativeStickyAssignor
3. **Monitor consumer lag** - Track processing progress
4. **Test failure scenarios** - Verify state recovery

### Operations

1. **Use state.dir** - Dedicated directory for state stores
2. **Monitor changelog topics** - Track state recovery
3. **Plan capacity** - Size for state store growth
4. **Use interactive queries** - Query state stores at runtime

---

## Further Reading

- [Kafka Streams Documentation](https://kafka.apache.org/documentation/streams/)
- [Kafka Streams DSL](https://kafka.apache.org/documentation/streams/developer-guide/dsp-api.html)
- [Kafka Streams Processor API](https://kafka.apache.org/documentation/streams/developer-guide/processor-api.html)
