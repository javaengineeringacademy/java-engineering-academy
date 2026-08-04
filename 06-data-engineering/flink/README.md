# Apache Flink

Apache Flink is a distributed processing engine for stateful computations over unbounded and bounded data streams. It provides exactly-once semantics, event-time processing, and highly iterative computation capabilities.

## Table of Contents

1. [Architecture](#architecture)
2. [Dataflow Model](#dataflow-model)
3. [Windowing](#windowing)
4. [State Management](#state-management)
5. [Exactly-Once Semantics](#exactly-once-semantics)
6. [Complex Event Processing (CEP)](#complex-event-processing-cep)
7. [Table API and SQL](#table-api-and-sql)
8. [Flink vs Spark Streaming](#flink-vs-spark-streaming)
9. [Deployment](#deployment)

---

## Architecture

Flink follows a master-worker architecture with two main components:

### JobManager (Master)

The JobManager coordinates the distributed execution of Flink jobs.

- **ResourceManager**: Manages TaskManager slots and allocation
- **Dispatcher**: Provides REST interface for job submission
- **JobMaster**: Manages a single job graph execution

Key responsibilities:
- Receives programs and creates ExecutionGraphs
- Schedules tasks across TaskManagers
- Monitors progress and handles failures
- Checkpoints coordination

### TaskManager (Worker)

TaskManagers execute the actual data processing tasks.

- Executes individual operators and subtasks
- Manages task buffers and data exchange
- Reports task status to JobManager
- Participates in checkpointing

```
┌─────────────────────────────────────────────┐
│              JobManager                      │
│  ┌───────────┐ ┌──────────┐ ┌────────────┐ │
│  │Dispatcher │ │JobMaster │ │ResourceManager│
│  └───────────┘ └──────────┘ └────────────┘ │
└─────────────────────────────────────────────┘
                     │
        ┌────────────┼────────────┐
        ▼            ▼            ▼
┌──────────┐  ┌──────────┐  ┌──────────┐
│TaskManager│  │TaskManager│  │TaskManager│
│  Slot 1   │  │  Slot 1   │  │  Slot 1   │
│  Slot 2   │  │  Slot 2   │  │  Slot 2   │
└──────────┘  └──────────┘  └──────────┘
```

### Task Slots

Each TaskManager contains one or more task slots:
- Slots represent fixed resources (CPU, memory)
- Tasks are allocated to slots for execution
- Slots enable resource isolation within a TaskManager

---

## Dataflow Model

Flink programs model computation as dataflows:

### Core Concepts

- **DataStream**: Immutable, potentially unbounded collection of elements
- **Transformation**: Operations that produce new DataStreams from existing ones
- **Sink**: Destination for DataStreams (files, databases, message queues)
- **Source**: Origin of DataStreams (Kafka, files, sockets)

### Common Transformations

```java
// Map transformation
DataStream<String> mapped = input.map(x -> x.toUpperCase());

// FlatMap transformation
DataStream<String> flatMapped = input.flatMap(
    (String value, Collector<String> out) -> {
        for (String word : value.split(" ")) {
            out.collect(word);
        }
    }
);

// KeyBy transformation
KeyedStream<Event, String> keyed = input
    .keyBy(event -> event.getUserId());

// Reduce transformation
DataStream<Long> reduced = keyed
    .map(event -> event.getValue())
    .reduce((a, b) -> a + b);
```

### Execution Graph

Flink converts programs into Execution Graphs:
- **Program**: User-defined transformations
- **StreamGraph**: Logical representation
- **JobGraph**: Optimized representation
- **ExecutionGraph**: Physical execution plan

---

## Windowing

Windowing divides streams into finite segments for processing:

### Time Windows

**Tumbling Windows**
- Fixed-size, non-overlapping windows
- Each event belongs to exactly one window

```java
input.keyBy(...)
    .window(TumblingEventTimeWindows.of(Time.seconds(5)))
    .reduce(...);
```

**Sliding Windows**
- Fixed-size, overlapping windows
- Each event can belong to multiple windows

```java
input.keyBy(...)
    .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
    .reduce(...);
```

**Session Windows**
- Groups events by activity periods
- Windows close after inactivity gap

```java
input.keyBy(...)
    .window(EventTimeSessionWindows.withGap(Time.minutes(5)))
    .reduce(...);
```

### Count Windows

**Tumbling Count Windows**
- Fixed number of events per window

```java
input.keyBy(...)
    .countWindow(100)
    .reduce(...);
```

**Sliding Count Windows**
- Overlapping count-based windows

```java
input.keyBy(...)
    .countWindow(100, 10)
    .reduce(...);
```

### Window Functions

- **ReduceFunction**: Incremental aggregation
- **FoldFunction**: Incremental aggregation with accumulator
- **ProcessWindowFunction**: Full window access with context

---

## State Management

Flink provides robust state management for fault-tolerant processing:

### State Types

**Value State**
```java
ValueState<Long> countState = getRuntimeContext()
    .getState(new ValueStateDescriptor<>("count", Long.class));
```

**List State**
```java
ListState<Event> listState = getRuntimeContext()
    .getListState(new ListStateDescriptor<>("events", Event.class));
```

**Map State**
```java
MapState<String, Long> mapState = getRuntimeContext()
    .getMapState(new MapStateDescriptor<>("counts", String.class, Long.class));
```

### State Backends

**MemoryStateBackend**
- Stores state on TaskManager heap
- Suitable for small state
- Used in development/testing

**FsStateBackend**
- Stores state on filesystem
- Logs state checkpoints to durable storage
- Good for large state with moderate access speed

**RocksDBStateBackend**
- Stores state in RocksDB database
- Supports very large state (terabytes)
- Incremental checkpoints

### Checkpointing

Flink periodically creates consistent snapshots:

```java
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

// Enable checkpointing every 60 seconds
env.enableCheckpointing(60000);

// Set checkpointing mode (exactly-once)
env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

// Set minimum pause between checkpoints
env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30000);

// Set checkpoint timeout
env.getCheckpointConfig().setCheckpointTimeout(120000);

// Allow only one checkpoint at a time
env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
```

---

## Exactly-Once Semantics

Flink achieves exactly-once processing through:

### End-to-End Exactly-Once

**Two-Phase Commit (2PC) Protocol**
- Pre-commit: Buffer output and prepare transaction
- Commit: Actually write output and commit transaction
- Abort: Rollback in case of failure

**Kafka Integration**
```java
FlinkKafkaProducer<String> sink = new FlinkKafkaProducer<>(
    "output-topic",
    new SimpleStringSchema(),
    properties,
    FlinkKafkaProducer.Semantic.EXACTLY_ONCE
);
```

### State Consistency

- Consistent checkpointing ensures exactly-once state
- State is restored from last successful checkpoint
- In-flight records may be replayed after failure

---

## Complex Event Processing (CEP)

Flink CEP library detects patterns in event streams:

### Pattern Definition

```java
Pattern<Event, ?> pattern = Pattern.<Event>begin("start")
    .where(new SimpleCondition<Event>() {
        @Override
        public boolean filter(Event event) {
            return event.getType().equals("click");
        }
    })
    .times(3)
    .within(Time.seconds(5));
```

### Pattern Operations

- **begin**: Start pattern sequence
- **next**: Strict continuation (no gaps)
- **followedBy**: Relaxed continuation (gaps allowed)
- **followedByAny**: Non-strict continuation
- **or**: Alternative patterns
- **not**: Exclusion patterns

### Pattern Application

```java
PatternStream<Event> patternStream = CEP.pattern(input, pattern);

DataStream<Alert> alerts = patternStream.select(
    new PatternSelectFunction<Event, Alert>() {
        @Override
        public Alert select(Map<String, List<Event>> pattern) {
            Event start = pattern.get("start").get(0);
            return new Alert("Pattern detected: " + start.getId());
        }
    }
);
```

---

## Table API and SQL

Flink provides SQL-like interface for data processing:

### Table API

```java
// Create table environment
TableEnvironment tableEnv = TableEnvironment.create(EnvironmentSettings.newInstance()
    .useBlinkPlanner()
    .build());

// Create table from data stream
Table table = tableEnv.fromDataStream(input);

// Query using Table API
Table result = table
    .filter($("type").isEqual("click"))
    .groupBy($("userId"))
    .select($("userId"), $("value").sum().as("total"));
```

### Flink SQL

```sql
-- Create source table
CREATE TABLE kafka_source (
    user_id STRING,
    event_type STRING,
    value BIGINT,
    event_time TIMESTAMP(3),
    WATERMARK FOR event_time AS event_time - INTERVAL '5' SECOND
) WITH (
    'connector' = 'kafka',
    'topic' = 'events',
    'properties.bootstrap.servers' = 'localhost:9092',
    'format' = 'json'
);

-- Create sink table
CREATE TABLE result_sink (
    user_id STRING,
    total BIGINT,
    window_start TIMESTAMP(3)
) WITH (
    'connector' = 'print'
);

-- Insert query
INSERT INTO result_sink
SELECT
    user_id,
    SUM(value) AS total,
    TUMBLE_START(event_time, INTERVAL '1' MINUTE) AS window_start
FROM kafka_source
WHERE event_type = 'click'
GROUP BY
    user_id,
    TUMBLE(event_time, INTERVAL '1' MINUTE);
```

### Temporal Joins

```sql
-- Temporal table join for dimension lookup
SELECT
    o.order_id,
    p.product_name,
    o.amount
FROM orders o
JOIN product_dimensions FOR SYSTEM_TIME AS OF o.proc_time AS p
ON o.product_id = p.product_id;
```

---

## Flink vs Spark Streaming

| Feature | Flink | Spark Streaming |
|---------|-------|-----------------|
| Processing Model | True streaming (event-by-event) | Micro-batch |
| Latency | Milliseconds | Seconds |
| State Management | Built-in, robust | Limited |
| Windowing | Event-time, session windows | Event-time, processing-time |
| Exactly-Once | End-to-end | Micro-batch level |
| Backpressure | Built-in | Limited |
| Iterative Processing | Native support | Limited |
| API | DataStream, Table, SQL | RDD, DataFrame, SQL |
| Ecosystem | Growing | Mature |

### When to Use Flink

- Low-latency requirements
- Complex event processing
- Stateful stream processing
- Event-time processing
- Exactly-once semantics

---

## Deployment

### Standalone Cluster

```bash
# Start JobManager
./bin/jobmanager.sh start

# Start TaskManager
./bin/taskmanager.sh start

# Submit job
./bin/flink run examples/streaming/WordCount.jar
```

### YARN Deployment

```bash
# Run on YARN
./bin/flink run -m yarn-cluster \
    -c org.example.WordCount \
    examples/streaming/WordCount.jar
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: flink-jobmanager
spec:
  replicas: 1
  selector:
    matchLabels:
      app: flink
      component: jobmanager
  template:
    metadata:
      labels:
        app: flink
        component: jobmanager
    spec:
      containers:
        - name: jobmanager
          image: flink:1.14
          command: ["/docker-entrypoint.sh"]
          args: ["jobmanager"]
          ports:
            - containerPort: 6123
              name: rpc
```

### Docker

```bash
# Build Flink image
docker build -t flink:latest .

# Run Flink cluster
docker run -d --name flink-jobmanager \
    flink:latest jobmanager

docker run -d --name flink-taskmanager \
    --link flink-jobmanager:jobmanager \
    flink:latest taskmanager
```

### Configuration

Key configuration parameters:

```yaml
# flink-conf.yaml
jobmanager.rpc.address: localhost
jobmanager.rpc.port: 6123
taskmanager.numberOfTaskSlots: 4
parallelism.default: 4
state.backend: rocksdb
state.checkpoints.dir: hdfs:///flink/checkpoints
state.savepoints.dir: hdfs:///flink/savepoints
```

---

## Best Practices

### Performance Optimization

1. Use appropriate parallelism
2. Configure state backend based on state size
3. Use asynchronous I/O for external systems
4. Avoid data skew in keyBy operations
5. Use project and filter early to reduce data

### Fault Tolerance

1. Enable checkpointing with appropriate intervals
2. Use exactly-once mode for critical applications
3. Configure checkpoint timeout appropriately
4. Monitor checkpoint progress

### Resource Management

1. Right-size TaskManager slots
2. Use standalone sessions for multiple jobs
3. Monitor memory and CPU usage
4. Configure network buffers appropriately

---

## Further Reading

- [Flink Documentation](https://flink.apache.org/)
- [Flink Training](https://training.ververica.com/)
- [Flink Examples](https://github.com/apache/flink/tree/master/flink-examples)
