# Apache Flink

Apache Flink is a distributed stream processing framework for stateful computations over unbounded and bounded data streams. It provides exactly-once semantics, high throughput, and low latency.

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Flink API](#flink-api)
- [State Management](#state-management)
- [Windowing](#windowing)
- [CEP (Complex Event Processing)](#cep)
- [Examples](#examples)
- [Best Practices](#best-practices)

## Overview

Flink is designed for stream processing as its core paradigm, with batch processing as a special case of stream processing (bounded streams).

### Key Features

- **True streaming**: Event-by-event processing
- **Exactly-once semantics**: Fault tolerance with checkpointing
- **State management**: Built-in state backends
- **Windowing**: Flexible window types
- **Event time processing**: Handle out-of-order events
- **High availability**: Fault-tolerant cluster

### Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    FLINK ARCHITECTURE                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │                    Flink Cluster                        │  │
│   │  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  │  │
│   │  │ Job     │  │ Task    │  │ Task    │  │ Task    │  │  │
│   │  │Manager  │  │Manager  │  │Manager  │  │Manager  │  │  │
│   │  └─────────┘  └─────────┘  └─────────┘  └─────────┘  │  │
│   │       │            │            │            │          │  │
│   │       └────────────┴────────────┴────────────┘          │  │
│   │                         │                               │  │
│   │                    Checkpoint                           │  │
│   │                    Coordinator                          │  │
│   └─────────────────────────────────────────────────────────┘  │
│                                                                 │
│   Sources ──> Operators ──> Sinks                             │
└─────────────────────────────────────────────────────────────────┘
```

## Core Concepts

### Execution Environment

```java
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.api.common.RuntimeExecutionMode;

public class FlinkExample {
    public static void main(String[] args) throws Exception {
        // Create execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Configure
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        env.setParallelism(4);
        env.enableCheckpointing(60000); // Checkpoint every 60 seconds

        // Source
        DataStream<String> text = env.socketTextStream("localhost", 9999);

        // Processing
        DataStream<WordWithCount> counts = text
            .flatMap(new Tokenizer())
            .keyBy(value -> value.word)
            .window(TumblingEventTimeWindows.of(Time.seconds(5)))
            .sum("count");

        // Sink
        counts.print();

        // Execute
        env.execute("Word Count");
    }
}
```

### DataStream API

```java
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class DataStreamOperations {
    public void transformations(StreamExecutionEnvironment env) {
        // Source
        DataStream<String> source = env.fromCollection(Arrays.asList("a", "b", "c"));

        // Map
        DataStream<Integer> mapped = source.map(String::length);

        // FlatMap
        DataStream<String> flatMapped = source.flatMap(
            (String value, Collector<String> out) -> {
                for (String word : value.split(" ")) {
                    out.collect(word);
                }
            }
        );

        // Filter
        DataStream<String> filtered = source.filter(value -> !value.isEmpty());

        // KeyBy
        DataStream<Tuple2<String, Integer>> keyed = source
            .map(value -> Tuple2.of(value, 1))
            .keyBy(value -> value.f0);

        // Reduce
        DataStream<Tuple2<String, Integer>> reduced = keyed
            .reduce((v1, v2) -> Tuple2.of(v1.f0, v1.f1 + v2.f1));
    }
}
```

## Flink API

### Table API

```java
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class TableAPIExample {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // Create table from stream
        Table table = tableEnv.from("input-stream");

        // Query using SQL
        Table result = tableEnv.sqlQuery(
            "SELECT word, COUNT(*) as cnt " +
            "FROM input_table " +
            "GROUP BY word " +
            "HAVING COUNT(*) > 1"
        );

        // Convert back to stream
        DataStream<Row> outputStream = tableEnv.toAppendStream(result, Row.class);
    }
}
```

### SQL Client

```sql
-- Flink SQL examples
CREATE TABLE orders (
    order_id STRING,
    customer_id STRING,
    amount DECIMAL(10, 2),
    order_time TIMESTAMP(3),
    WATERMARK FOR order_time AS order_time - INTERVAL '5' SECOND
) WITH (
    'connector' = 'kafka',
    'topic' = 'orders',
    'properties.bootstrap.servers' = 'localhost:9092',
    'format' = 'json'
);

-- Windowed aggregation
SELECT
    TUMBLE_START(order_time, INTERVAL '1' MINUTE) as window_start,
    COUNT(*) as order_count,
    SUM(amount) as total_amount
FROM orders
GROUP BY TUMBLE(order_time, INTERVAL '1' MINUTE);

-- Session window
SELECT
    customer_id,
    SESSION_START(order_time, INTERVAL '30' MINUTE) as session_start,
    COUNT(*) as order_count
FROM orders
GROUP BY customer_id, SESSION(order_time, INTERVAL '30' MINUTE);
```

## State Management

### ValueState

```java
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class StatefulProcessor extends KeyedProcessFunction<String, Event, Alert> {
    private ValueState<Long> eventCount;

    @Override
    public void open(Configuration parameters) {
        ValueStateDescriptor<Long> descriptor = new ValueStateDescriptor<>(
            "event-count",
            Types.LONG
        );
        eventCount = getRuntimeContext().getState(descriptor);
    }

    @Override
    public void processElement(Event event, Context ctx, Collector<Alert> out) throws Exception {
        Long currentCount = eventCount.value();
        if (currentCount == null) {
            currentCount = 0L;
        }

        currentCount++;
        eventCount.update(currentCount);

        if (currentCount > 10) {
            out.collect(new Alert(event.getUserId(), "Too many events"));
        }
    }
}
```

### ListState

```java
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;

public class ListStateExample extends KeyedProcessFunction<String, Event, List<Event>> {
    private ListState<Event> eventBuffer;

    @Override
    public void open(Configuration parameters) {
        ListStateDescriptor<Event> descriptor = new ListStateDescriptor<>(
            "event-buffer",
            Event.class
        );
        eventBuffer = getRuntimeContext().getListState(descriptor);
    }

    @Override
    public void processElement(Event event, Context ctx, Collector<List<Event>> out) throws Exception {
        eventBuffer.add(event);

        // Process every 100 events
        Iterable<Event> events = eventBuffer.get();
        List<Event> eventList = new ArrayList<>();
        events.forEach(eventList::add);

        if (eventList.size() >= 100) {
            out.collect(eventList);
            eventBuffer.clear();
        }
    }
}
```

### MapState

```java
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;

public class MapStateExample extends KeyedProcessFunction<String, Event, UserStats> {
    private MapState<String, Long> metrics;

    @Override
    public void open(Configuration parameters) {
        MapStateDescriptor<String, Long> descriptor = new MapStateDescriptor<>(
            "user-metrics",
            Types.STRING,
            Types.LONG
        );
        metrics = getRuntimeContext().getMapState(descriptor);
    }

    @Override
    public void processElement(Event event, Context ctx, Collector<UserStats> out) throws Exception {
        // Update metrics
        Long count = metrics.get("event_count");
        if (count == null) count = 0L;
        metrics.put("event_count", count + 1);

        // Update total value
        Long totalValue = metrics.get("total_value");
        if (totalValue == null) totalValue = 0L;
        metrics.put("total_value", totalValue + event.getValue());

        out.collect(new UserStats(event.getUserId(), count + 1, totalValue + event.getValue()));
    }
}
```

## Windowing

### Tumbling Windows

```java
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class TumblingWindowExample {
    public void process(DataStream<Event> stream) {
        DataStream<WindowResult> result = stream
            .keyBy(Event::getUserId)
            .window(TumblingEventTimeWindows.of(Time.minutes(5)))
            .aggregate(new EventAggregator());
    }

    static class EventAggregator implements AggregateFunction<Event, EventAccumulator, WindowResult> {
        @Override
        public EventAccumulator createAccumulator() {
            return new EventAccumulator();
        }

        @Override
        public EventAccumulator add(Event value, EventAccumulator accumulator) {
            accumulator.addEvent(value);
            return accumulator;
        }

        @Override
        public WindowResult getResult(EventAccumulator accumulator) {
            return new WindowResult(accumulator.getCount(), accumulator.getTotalValue());
        }

        @Override
        public EventAccumulator merge(EventAccumulator a, EventAccumulator b) {
            return a.merge(b);
        }
    }
}
```

### Session Windows

```java
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class SessionWindowExample {
    public void process(DataStream<Event> stream) {
        DataStream<SessionResult> result = stream
            .keyBy(Event::getUserId)
            .window(EventTimeSessionWindows.withGap(Time.minutes(30)))
            .process(new SessionProcessor());
    }

    static class SessionProcessor extends ProcessWindowFunction<Event, SessionResult, String, TimeWindow> {
        @Override
        public void process(String userId, Context context, Iterable<Event> events, Collector<SessionResult> out) {
            List<Event> eventList = new ArrayList<>();
            events.forEach(eventList::add);

            SessionResult result = new SessionResult(
                userId,
                context.window().getStart(),
                context.window().getEnd(),
                eventList.size()
            );
            out.collect(result);
        }
    }
}
```

## CEP (Complex Event Processing)

### Pattern Detection

```java
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;

import java.util.List;
import java.util.Map;

public class CEPExample {
    public void detectPatterns(DataStream<Event> stream) {
        // Define pattern: Login followed by 3 failed attempts within 5 minutes
        Pattern<Event, ?> pattern = Pattern.<Event>begin("start")
            .where(evt -> evt.getType().equals("LOGIN"))
            .followedBy("failures")
            .where(evt -> evt.getType().equals("FAILED_LOGIN"))
            .times(3)
            .within(Time.minutes(5));

        // Apply pattern
        PatternStream<Event> patternStream = CEP.pattern(stream, pattern);

        // Select matches
        DataStream<Alert> alerts = patternStream.select(
            new PatternSelectFunction<Event, Alert>() {
                @Override
                public Alert select(Map<String, List<Event>> pattern) {
                    Event start = pattern.get("start").get(0);
                    List<Event> failures = pattern.get("failures");

                    return new Alert(
                        start.getUserId(),
                        "Suspicious login activity detected",
                        failures.size()
                    );
                }
            }
        );
    }
}
```

### Advanced Patterns

```java
public class AdvancedCEPExample {
    public void detectComplexPatterns(DataStream<Event> stream) {
        // Pattern with condition and iteration
        Pattern<Event, ?> pattern = Pattern.<Event>begin("first")
            .where(evt -> evt.getValue() > 100)
            .followedBy("second")
            .where(evt -> evt.getValue() > 200)
            .optional()
            .followedBy("third")
            .where(evt -> evt.getValue() > 300)
            .times(1, 3)
            .within(Time.hours(1));

        // Negation pattern
        Pattern<Event, ?> securityPattern = Pattern.<Event>begin("login")
            .where(evt -> evt.getType().equals("LOGIN"))
            .notFollowedBy("logout")
            .where(evt -> evt.getType().equals("LOGOUT"))
            .within(Time.hours(8));

        // Overlapping pattern
        Pattern<Event, ?> overlappingPattern = Pattern.<Event>begin("start")
            .where(evt -> evt.isImportant())
            .timesOrMore(2)
            .consecutive()
            .within(Time.minutes(10));
    }
}
```

## Examples

### Real-time Analytics Pipeline

```java
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class RealTimeAnalytics {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
        env.enableCheckpointing(30000);

        // Read from Kafka
        DataStream<Event> events = env
            .fromSource(
                KafkaSource.<Event>builder()
                    .setBootstrapServers("localhost:9092")
                    .setTopics("events")
                    .setGroupId("analytics-group")
                    .setValueOnlyDeserializer(new EventDeserializer())
                    .build(),
                WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(20)),
                "Kafka Source"
            );

        // Process events
        DataStream<AnalyticsResult> results = events
            .keyBy(Event::getCategory)
            .window(TumblingEventTimeWindows.of(Time.minutes(1)))
            .aggregate(new EventAggregator());

        // Write to sink
        results.sinkTo(
            KafkaSink.<AnalyticsResult>builder()
                .setBootstrapServers("localhost:9092")
                .setRecordSerializer(new AnalyticsResultSerializer())
                .build()
        );

        env.execute("Real-time Analytics");
    }
}
```

### Fraud Detection

```java
public class FraudDetection {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Transaction> transactions = env
            .addSource(new TransactionSource())
            .assignTimestampsAndWatermarks(
                WatermarkStrategy
                    .<Transaction>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                    .withTimestampAssigner((event, timestamp) -> event.getTimestamp())
            );

        // Detect high-value transactions
        DataStream<FraudAlert> highValueAlerts = transactions
            .filter(t -> t.getAmount() > 10000)
            .map(t -> new FraudAlert(t, "HIGH_AMOUNT"));

        // Detect velocity attacks
        DataStream<FraudAlert> velocityAlerts = transactions
            .keyBy(Transaction::getUserId)
            .window(TumblingEventTimeWindows.of(Time.minutes(5)))
            .process(new VelocityCheckProcessor());

        // Merge alerts
        highValueAlerts.union(velocityAlerts)
            .addSink(new AlertSink());

        env.execute("Fraud Detection");
    }
}
```

## Best Practices

### 1. State Management

```java
// Use appropriate state type
public class StateBestPractices {
    // Use ValueState for single values
    private ValueState<Long> count;

    // Use ListState for collections
    private ListState<Event> eventBuffer;

    // Use MapState for key-value pairs
    private MapState<String, Long> metrics;
}
```

### 2. Watermark Strategy

```java
// Define proper watermark strategy
WatermarkStrategy<Event> strategy = WatermarkStrategy
    .<Event>forBoundedOutOfOrderness(Duration.ofSeconds(20))
    .withTimestampAssigner((event, timestamp) -> event.getEventTime())
    .withIdleness(Duration.ofMinutes(1));
```

### 3. Checkpointing

```java
// Configure checkpointing
env.enableCheckpointing(60000); // 60 seconds
env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30000);
env.getCheckpointConfig().setCheckpointTimeout(120000);
env.getCheckpointConfig().setTolerableCheckpointFailureNumber(3);
```

### 4. Performance Tuning

```java
// Optimize for performance
env.setParallelism(4);
env.setBufferTimeout(1); // Low latency mode

// Use async I/O for external lookups
AsyncDataStream.unorderedWait(
    stream,
    new AsyncDatabaseLookup(),
    30, TimeUnit.SECONDS,
    100 // Max concurrent requests
);
```

## Further Reading

- [Flink Documentation](https://flink.apache.org/docs/)
- [Flink DataStream API](https://flink.apache.org/docs/dev/datastream_api.html)
- [Flink SQL](https://flink.apache.org/docs/dev/table/sql/)
- [Kafka Streams](../kafka-streams/) - Kafka-native alternative
- [Apache Storm](../storm/) - Another stream processor
