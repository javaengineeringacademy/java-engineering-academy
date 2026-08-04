# Kafka Streams

Kafka Streams is a client library for building stream processing applications on top of Apache Kafka. It provides a simple yet powerful API for real-time data processing with exactly-once semantics.

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Kafka Streams API](#kafka-streams-api)
- [State Stores](#state-stores)
- [Windowing](#windowing)
- [Examples](#examples)
- [Best Practices](#best-practices)

## Overview

Kafka Streams is designed for building streaming applications that consume from and produce to Kafka topics. It runs as a library within your application, not as a separate cluster.

### Key Features

- **Lightweight**: No separate cluster required
- **Exactly-once semantics**: Built-in fault tolerance
- **Stateful processing**: Local state stores with changelog topics
- **Windowing**: Tumbling, hopping, sliding, and session windows
- **Interactive queries**: Query state stores directly

### Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    KAFKA STREAMS APPLICATION                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   Input Topic ──> Stream Processor ──> Output Topic            │
│                                                                 │
│   ┌─────────────────────────────────────────────────────┐      │
│   │                  Stream Threads                     │      │
│   │  ┌─────────┐  ┌─────────┐  ┌─────────┐            │      │
│   │  │Source   │─>│Process  │─>│Sink     │            │      │
│   │  │Processor│  │Processor│  │Processor│            │      │
│   │  └─────────┘  └─────────┘  └─────────┘            │      │
│   │       │            │            │                   │      │
│   │       └────────────┴────────────┘                   │      │
│   │                    │                                │      │
│   │              State Store                            │      │
│   └─────────────────────────────────────────────────────┘      │
└─────────────────────────────────────────────────────────────────┘
```

## Core Concepts

### Topology

```java
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class WordCountExample {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        // Read from input topic
        KStream<String, String> textLines = builder.stream("input-topic");

        // Process stream
        KTable<String, Long> wordCounts = textLines
            .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
            .groupBy((key, word) -> word)
            .count(Materialized.as("word-counts-store"));

        // Write to output topic
        wordCounts.toStream().to("output-topic", Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();
    }
}
```

### Stream Processing

```java
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Reducer;

public class StreamProcessingExamples {
    public void filterExample(KStream<String, String> stream) {
        // Filter events
        KStream<String, String> filtered = stream
            .filter((key, value) -> value != null && !value.isEmpty());
    }

    public void mapExample(KStream<String, String> stream) {
        // Transform values
        KStream<String, String> mapped = stream
            .mapValues(value -> value.toUpperCase());
    }

    public void flatMapExample(KStream<String, String> stream) {
        // One-to-many transformation
        KStream<String, String> flatMapped = stream
            .flatMapValues(value -> Arrays.asList(value.split(" ")));
    }

    public void aggregateExample(KStream<String, Integer> stream) {
        // Aggregate values
        KTable<String, Integer> aggregated = stream
            .groupByKey()
            .reduce(
                (value1, value2) -> value1 + value2,
                Materialized.as("sum-store")
            );
    }
}
```

## State Stores

### Local State

```java
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

public class StateStoreExample {
    public void createStateStore(StreamsBuilder builder) {
        // Create state store
        StoreBuilder<KeyValueStore<String, String>> storeBuilder =
            Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("my-store"),
                Serdes.String(),
                Serdes.String()
            );

        builder.addStateStore(storeBuilder);
    }

    public void useStateStore(KStream<String, String> stream) {
        // Use state store in processing
        stream.process(() -> new Processor<String, String, String, String>() {
            private ProcessorContext<String, String> context;
            private KeyValueStore<String, String> store;

            @Override
            public void init(ProcessorContext<String, String> context) {
                this.context = context;
                this.store = context.getStateStore("my-store");
            }

            @Override
            public void process(String key, String value) {
                // Read from store
                String existing = store.get(key);

                // Write to store
                store.put(key, value);

                // Forward downstream
                context.forward(key, value);
            }
        }, "my-store");
    }
}
```

### Changelog Topics

```java
// Changelog topics provide fault tolerance
// State changes are logged to changelog topics
// On failure, state is rebuilt from changelog

Properties config = new Properties();
config.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 3);
config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 3);

// State store with changelog
KTable<String, Long> table = stream
    .groupByKey()
    .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as(
        "count-store"
    ).withLoggingDisabled()); // Disable changelog for performance
```

### Interactive Queries

```java
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;

public class InteractiveQueryExample {
    private KafkaStreams streams;

    public Long getCount(String key) {
        // Query state store
        ReadOnlyKeyValueStore<String, Long> store =
            streams.store(
                StoreQueryParameters.fromNameAndType("count-store", QueryableStoreTypes.keyValueStore())
            );
        return store.get(key);
    }

    public Map<String, Long> getAllCounts() {
        ReadOnlyKeyValueStore<String, Long> store =
            streams.store(
                StoreQueryParameters.fromNameAndType("count-store", QueryableStoreTypes.keyValueStore())
            );

        Map<String, Long> counts = new HashMap<>();
        store.all().forEachRemaining(keyValue -> counts.put(keyValue.key, keyValue.value));
        return counts;
    }

    public Map<String, Long> getCountsForWindow(long from, long to) {
        ReadOnlyWindowStore<String, Long> store =
            streams.store(
                StoreQueryParameters.fromNameAndType("window-store", QueryableStoreTypes.windowStore())
            );

        Map<String, Long> windowCounts = new HashMap<>();
        store.fetchAll(from, to).forEachRemaining(keyValue -> {
            windowCounts.put(keyValue.key, keyValue.value);
        });
        return windowCounts;
    }
}
```

## Windowing

### Tumbling Windows

```java
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windows;

public class TumblingWindowExample {
    public void tumblingWindow(KStream<String, Integer> stream) {
        // 5-minute tumbling windows
        KTable<Windowed<String>, Long> windowedCounts = stream
            .groupByKey()
            .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
            .count(Materialized.as("tumbling-window-store"));
    }
}
```

### Hopping Windows

```java
public class HoppingWindowExample {
    public void hoppingWindow(KStream<String, Integer> stream) {
        // 10-minute window advancing every 5 minutes
        KTable<Windowed<String>, Long> windowedCounts = stream
            .groupByKey()
            .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(10))
                .advanceBy(Duration.ofMinutes(5)))
            .count(Materialized.as("hopping-window-store"));
    }
}
```

### Sliding Windows

```java
public class SlidingWindowExample {
    public void slidingWindow(KStream<String, Integer> stream) {
        // Sliding window of 5 minutes
        KTable<Windowed<String>, Long> windowedCounts = stream
            .groupByKey()
            .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
            .count(Materialized.as("sliding-window-store"));
    }
}
```

### Session Windows

```java
import org.apache.kafka.streams.kstream.SessionWindows;

public class SessionWindowExample {
    public void sessionWindow(KStream<String, Integer> stream) {
        // Session window with 30-minute gap
        KTable<Windowed<String>, Long> sessionCounts = stream
            .groupByKey()
            .windowedBy(SessionWindows.ofInactivityGapWithNoGrace(Duration.ofMinutes(30)))
            .count(Materialized.as("session-window-store"));
    }
}
```

## Examples

### Real-time Analytics

```java
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Joined;

public class RealTimeAnalytics {
    public void processEvents(StreamsBuilder builder) {
        // Read events
        KStream<String, Event> events = builder.stream(
            "events-topic",
            Consumed.with(Serdes.String(), eventSerde)
        );

        // Enrich with user data
        KTable<String, User> users = builder.table(
            "users-topic",
            Consumed.with(Serdes.String(), userSerde)
        );

        KStream<String, EnrichedEvent> enriched = events.join(
            users,
            (event, user) -> new EnrichedEvent(event, user),
            Joined.with(Serdes.String(), eventSerde, userSerde)
        );

        // Aggregate by user and window
        KTable<Windowed<String>, Long> userEventCounts = enriched
            .groupByKey()
            .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
            .count(Materialized.as("user-event-counts"));

        // Output results
        userEventCounts.toStream()
            .map((windowedKey, count) -> KeyValue.pair(
                windowedKey.key(),
                new UserEventCount(windowedKey.key(), count, windowedKey.window().start())
            ))
            .to("analytics-topic", Produced.with(Serdes.String(), userEventCountSerde));
    }
}
```

### Fraud Detection

```java
public class FraudDetection {
    public void detectFraud(StreamsBuilder builder) {
        KStream<String, Transaction> transactions = builder.stream(
            "transactions-topic",
            Consumed.with(Serdes.String(), transactionSerde)
        );

        // Detect suspicious patterns
        KStream<String, FraudAlert> alerts = transactions
            .filter((key, transaction) -> transaction.getAmount() > 10000)
            .mapValues(transaction -> {
                FraudAlert alert = new FraudAlert();
                alert.setTransactionId(transaction.getId());
                alert.setUserId(transaction.getUserId());
                alert.setAmount(transaction.getAmount());
                alert.setReason("HIGH_AMOUNT");
                return alert;
            });

        // Write alerts
        alerts.to("fraud-alerts-topic", Produced.with(Serdes.String(), fraudAlertSerde));

        // Aggregate fraud stats by user
        KTable<String, Long> userFraudCounts = transactions
            .filter((key, transaction) -> transaction.getAmount() > 10000)
            .groupByKey()
            .count(Materialized.as("user-fraud-counts"));
    }
}
```

## Best Practices

### 1. Configuration

```java
Properties getStreamConfig() {
    Properties config = new Properties();

    // Application ID
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-app");

    // Bootstrap servers
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

    // State directory
    config.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");

    // Processing guarantees
    config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, "exactly_once_v2");

    // Number of stream threads
    config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 4);

    // Replication factor for changelog topics
    config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 3);

    // Cache max bytes buffering
    config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 10 * 1024 * 1024L);

    // Commit interval
    config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

    return config;
}
```

### 2. Error Handling

```java
public class RobustStreamProcessor {
    public void processWithErrorHandling(KStream<String, String> stream) {
        stream
            .mapValues(value -> {
                try {
                    return transform(value);
                } catch (Exception e) {
                    // Log error and send to dead letter topic
                    log.error("Failed to transform: {}", e.getMessage());
                    return null;
                }
            })
            .filter((key, value) -> value != null)
            .to("output-topic");
    }

    public void processWithDeadLetterTopic(KStream<String, String> stream) {
        KStream<String, String> validStream = stream
            .filter((key, value) -> {
                try {
                    validate(value);
                    return true;
                } catch (Exception e) {
                    // Send to dead letter topic
                    stream.to("dead-letter-topic");
                    return false;
                }
            });

        validStream.to("output-topic");
    }
}
```

### 3. Monitoring

```java
public class StreamMonitoring {
    private KafkaStreams streams;

    public void setupMonitoring() {
        streams.setStateListener((newState, oldState) -> {
            log.info("State changed from {} to {}", oldState, newState);

            if (newState == KafkaStreams.State.ERROR) {
                alertOps("Stream application in ERROR state");
            }
        });

        streams.setUncaughtExceptionHandler((thread, throwable) -> {
            log.error("Uncaught exception: {}", throwable.getMessage(), throwable);
            alertOps("Uncaught exception in stream application");
        });
    }

    public void monitorMetrics() {
        // Get metrics
        Metrics metrics = streams.metrics();

        // Key metrics to monitor
        // - stream-processor-node-process-rate
        // - stream-task-commit-rate
        // - record-error-rate
    }
}
```

### 4. Testing

```java
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

public class StreamTest {
    @Test
    public void testStreamProcessing() {
        // Create test topology
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> input = builder.stream("input-topic");
        KTable<String, Long> counts = input
            .flatMapValues(value -> Arrays.asList(value.split(" ")))
            .groupByKey()
            .count();

        // Create test driver
        TopologyTestDriver testDriver = new TopologyTestDriver(builder.build());

        // Test input
        TestInputTopic<String, String> inputTopic =
            testDriver.createInputTopic("input-topic", new StringSerializer(), new StringSerializer());
        inputTopic.pipeInput("key", "hello world hello");

        // Verify output
        TestOutputTopic<String, Long> outputTopic =
            testDriver.createOutputTopic("output-topic", new StringDeserializer(), new LongDeserializer());
        assertEquals(2L, outputTopic.readKeyValue().value); // "hello" appears twice
    }
}
```

## Further Reading

- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [Kafka Streams DSL](https://kafka.apache.org/documentation/streams/developer-guide/dsl-api.html)
- [Kafka Streams Processor API](https://kafka.apache.org/documentation/streams/developer-guide/processor-api.html)
- [Apache Flink](../flink/) - Alternative stream processor
- [Apache Storm](../storm/) - Another stream processing framework
