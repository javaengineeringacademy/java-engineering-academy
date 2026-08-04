# Kafka Streams

## Overview

Kafka Streams is a client library for building stream processing applications on top of Apache Kafka. It provides a simple yet powerful API for performing real-time data processing with exactly-once semantics.

## Key Concepts

### Topology

A topology defines the computation graph of your stream processing application.

```java
StreamsBuilder builder = new StreamsBuilder();

KStream<String, String> source = builder.stream("input-topic");

KStream<String, String> transformed = source
    .filter((key, value) -> value != null)
    .mapValues(value -> value.toUpperCase())
    .peek((key, value) -> System.out.println(key + ": " + value));

transformed.to("output-topology");

Properties props = new Properties();
props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-app");
props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();
```

### Processors

| Processor | Description |
|-----------|-------------|
| `filter` | Keep records matching predicate |
| `mapValues` | Transform values |
| `flatMapValues` | One-to-many value transformation |
| `groupByKey` | Repartition by key |
| `join` | Join two streams |
| `windowedBy` | Apply windowing |

### State Stores

State stores provide local state for stream processing operations.

```java
// Create a state store
StoreBuilder<KeyValueStore<String, String>> storeBuilder =
    Stores.keyValueStoreBuilder(
        Stores.persistentKeyValueStore("my-store"),
        Serdes.String(),
        Serdes.String()
    );

builder.addStateStore(storeBuilder);

// Use the state store in a processor
KStream<String, String> stream = builder.stream("input-topic");
stream.process(() -> new Processor<String, String, Void>() {
    private KeyValueStore<String, String> store;

    @Override
    public void init(ProcessorContext<String, Void> context) {
        this.store = context.getStateStore("my-store");
    }

    @Override
    public void process(Record<String, String> record) {
        store.put(record.key(), record.value());
        context.forward(record);
    }
}, "my-store");
```

## Windowing

### Tumbling Windows

Fixed-size, non-overlapping windows.

```java
stream
    .groupByKey()
    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
    .count()
    .toStream();
```

### Hopping Windows

Fixed-size, overlapping windows.

```java
stream
    .groupByKey()
    .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(10))
        .advanceBy(Duration.ofMinutes(5)))
    .count()
    .toStream();
```

### Session Windows

Dynamic-size windows based on activity.

```java
stream
    .groupByKey()
    .windowedBy(SessionWindows.ofInactivityGapWithNoGrace(Duration.ofMinutes(5)))
    .count()
    .toStream();
```

### Sliding Windows

Fixed-size windows that slide based on record timestamps.

```java
stream
    .groupByKey()
    .windowedBy(SlidingWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5)))
    .count()
    .toStream();
```

## Table-Stream Joins

### KTable-KTable Join

```java
KTable<String, String> table1 = builder.table("topic1");
KTable<String, String> table2 = builder.table("topic2");

KTable<String, String> joined = table1.join(
    table2,
    (value1, value2) -> value1 + ":" + value2
);
```

### KStream-KTable Join

```java
KStream<String, String> stream = builder.stream("stream-topic");
KTable<String, String> table = builder.table("table-topic");

KStream<String, String> joined = stream.join(
    table,
    (streamValue, tableValue) -> streamValue + ":" + tableValue,
    Joined.with(Serdes.String(), Serdes.String(), Serdes.String())
);
```

### KStream-KStream Join

```java
KStream<String, String> stream1 = builder.stream("topic1");
KStream<String, String> stream2 = builder.stream("topic2");

KStream<String, String> joined = stream1.join(
    stream2,
    (value1, value2) -> value1 + ":" + value2,
    JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(5)),
    Joined.with(Serdes.String(), Serdes.String(), Serdes.String())
);
```

## Exactly-Once Semantics

```java
Properties props = new Properties();
props.put(StreamsConfig.APPLICATION_ID_CONFIG, "exactly-once-app");
props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, 
    StreamsConfig.EXACTLY_ONCE_V2_CONFIG);
props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 3);
```

## Interactive Queries

```java
KafkaStreams streams = new KafkaStreams(builder.build(), props);
streams.start();

// Query local state
KTable<String, Long> table = builder.table("counts");
ReadOnlyKeyValueStore<String, Long> store = 
    streams.store(table.queryableStoreName(),
        QueryableStoreTypes.keyValueStore());

// Get value by key
Long count = store.get("my-key");

// Get all values
try (KeyValueIterator<String, Long> iter = store.all()) {
    while (iter.hasNext()) {
        KeyValue<String, Long> entry = iter.next();
        System.out.println(entry.key + ": " + entry.value);
    }
}
```

## Configuration

| Property | Description | Default |
|----------|-------------|---------|
| `application.id` | Application identifier | Required |
| `bootstrap.servers` | Kafka broker list | Required |
| `state.dir` | State store directory | `/tmp/kafka-streams` |
| `num.stream.threads` | Number of processing threads | 1 |
| `replication.factor` | Replication factor for topics | 1 |
| `processing.guarantee` | Processing guarantee | `at_least_once` |

## Best Practices

1. **Idempotent processors** - Design processors to handle duplicate records
2. **RocksDB tuning** - Configure block cache and write buffer for performance
3. **Serde optimization** - Use Avro/Protobuf instead of JSON for better performance
4. **Grace periods** - Define appropriate grace periods for windowed operations
5. **Consumer lag monitoring** - Monitor consumer lag for performance issues
6. **State store cleanup** - Configure cleanup policies for old state stores

## Key Takeaways

- Kafka Streams is a lightweight library for stream processing
- It provides exactly-once semantics with proper configuration
- Windowing supports tumbling, hopping, session, and sliding windows
- State stores enable local state management
- Interactive queries allow querying state from external applications
