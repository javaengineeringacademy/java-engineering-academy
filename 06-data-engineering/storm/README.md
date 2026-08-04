# Apache Storm

Apache Storm is a distributed real-time computation system for processing unbounded streams of data. It provides guaranteed message processing with exactly-once semantics.

## Table of Contents

1. [Architecture](#architecture)
2. [Spouts and Bolts](#spouts-and-bolts)
3. [Topologies](#topologies)
4. [Tuples](#tuples)
5. [Stream Grouping](#stream-grouping)
6. [Reliability and Acking](#reliability-and-acking)
7. [Trident](#trident)
8. [Storm vs Spark Streaming](#storm-vs-spark-streaming)
9. [Deployment on YARN](#deployment-on-yarn)

---

## Architecture

Storm follows a master-worker architecture:

### Nimbus (Master)

- Receives topology submissions
- Distributes code to workers
- Monitors topology health
- Reassigns tasks on failure

### Supervisor (Worker)

- Runs worker processes
- Manages task execution
- Reports status to Nimbus
- Handles worker lifecycle

### ZooKeeper

- Coordinates distributed processes
- Stores topology configuration
- Manages leader election
- Provides reliable coordination

```
┌─────────────────────────────────────┐
│            Nimbus                   │
│  ┌─────────────────────────────┐   │
│  │  Topology Management        │   │
│  │  Task Assignment            │   │
│  │  Monitoring                 │   │
│  └─────────────────────────────┘   │
└─────────────────────────────────────┘
                    │
                    ▼
            ┌──────────────┐
            │  ZooKeeper   │
            │  Cluster     │
            └──────────────┘
                    │
       ┌────────────┼────────────┐
       ▼            ▼            ▼
┌──────────┐  ┌──────────┐  ┌──────────┐
│Supervisor│  │Supervisor│  │Supervisor│
│ Worker 1 │  │ Worker 1 │  │ Worker 1 │
│ Worker 2 │  │ Worker 2 │  │ Worker 2 │
└──────────┘  └──────────┘  └──────────┘
```

### Worker Processes

- JVM processes running on Supervisor nodes
- Execute tasks assigned by Nimbus
- Communicate through Netty
- Each worker runs one topology

---

## Spouts and Bolts

### Spouts

Spouts are data sources that emit tuples into the topology:

```java
public class KafkaSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private KafkaConsumer<String, String> consumer;
    
    @Override
    public void open(Map<String, Object> conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        this.collector = collector;
        // Initialize Kafka consumer
    }
    
    @Override
    public void nextTuple() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
            collector.emit(new Values(record.key(), record.value()), 
                         record.offset());
        }
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("key", "message"));
    }
}
```

### Bolts

Bolts process tuples and optionally emit new tuples:

```java
public class WordCountBolt extends BaseRichBolt {
    private OutputCollector collector;
    private Map<String, Long> counts;
    
    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context,
                       OutputCollector collector) {
        this.collector = collector;
        this.counts = new HashMap<>();
    }
    
    @Override
    public void execute(Tuple input) {
        String word = input.getStringByField("word");
        Long count = counts.getOrDefault(word, 0L) + 1;
        counts.put(word, count);
        collector.emit(new Values(word, count));
        collector.ack(input);
    }
    
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }
}
```

### Bolt Lifecycle

1. **prepare()**: Called when bolt is initialized
2. **execute()**: Called for each incoming tuple
3. **cleanup()**: Called when bolt is shut down

### Common Base Classes

- **BaseRichBolt**: Full-featured bolt with acking support
- **BaseBasicBolt**: Simplified bolt (auto-ack)
- **BaseStatefulBolt**: Bolt with managed state

---

## Topologies

A topology is a network of spouts and bolts:

### Topology Definition

```java
TopologyBuilder builder = new TopologyBuilder();

// Set spout
builder.setSpout("kafka-spout", new KafkaSpout(), 2);

// Set bolts
builder.setBolt("split", new SplitBolt(), 4)
    .shuffleGrouping("kafka-spout");

builder.setBolt("count", new WordCountBolt(), 4)
    .fieldsGrouping("split", new Fields("word"));

// Build topology
StormTopology topology = builder.createTopology();
```

### Topology Lifecycle

1. **Submit**: Topology submitted to Nimbus
2. **Deploy**: Nimbus assigns tasks to workers
3. **Run**: Topology processes tuples
4. **Kill**: Topology stops and resources released

### Topology Configuration

```java
Config conf = new Config();
conf.setNumWorkers(2);
conf.setMaxSpoutPending(1000);
conf.setMessageTimeoutSecs(30);
conf.setStatsSampleRate(0.05);

// Submit topology
StormSubmitter.submitTopology("my-topology", conf, topology);
```

### Topology Metrics

Storm provides built-in metrics:
- Tuple processing rates
- Latency measurements
- Capacity metrics
- Worker/JVM metrics

---

## Tuples

Tuples are immutable data structures carrying data through the topology:

### Tuple Fields

```java
// Define fields
Fields fields = new Fields("word", "count", "timestamp");

// Create values
Values values = new Values("hello", 42, System.currentTimeMillis());
```

### Tuple Operations

```java
// Get field by index
String word = tuple.getString(0);
Long count = tuple.getLong(1);

// Get field by name
String word = tuple.getStringByField("word");
Long count = tuple.getLongByField("count");

// Get all fields
List<Object> values = tuple.getValues();
```

### Tuple Types

- **Regular Tuples**: Standard data tuples
- **Anchor Tuples**: Used for reliability tracking
- **Heartbeat Tuples**: System tuples for monitoring

### Tuple Acking

```java
// Anchor tuple for reliability
collector.emit(anchor, new Values(data));

// Ack tuple
collector.ack(tuple);

// Fail tuple
collector.fail(tuple);
```

---

## Stream Grouping

Stream groupings determine how tuples are distributed between components:

### Shuffle Grouping

Distributes tuples randomly across tasks:

```java
builder.setBolt("processor", new ProcessorBolt(), 4)
    .shuffleGrouping("source");
```

### Fields Grouping

Distributes tuples based on field values:

```java
builder.setBolt("count", new WordCountBolt(), 4)
    .fieldsGrouping("split", new Fields("word"));
```

Tuples with same field values always go to same task.

### All Grouping

Broadcasts tuples to all tasks:

```java
builder.setBolt("broadcaster", new BroadcasterBolt(), 4)
    .allGrouping("source");
```

### Global Grouping

Sends all tuples to single task (highest task ID):

```java
builder.setBolt("aggregator", new AggregatorBolt(), 1)
    .globalGrouping("source");
```

### None Grouping

Equivalent to shuffle grouping (no guarantees):

```java
builder.setBolt("processor", new ProcessorBolt(), 4)
    .noneGrouping("source");
```

### Direct Grouping

Producer decides which task receives tuple:

```java
builder.setBolt("direct", new DirectBolt(), 4)
    .directGrouping("source");
```

### Custom Grouping

Implement IRichSpout interface for custom distribution:

```java
public class CustomGrouping implements CustomStreamGrouping {
    @Override
    public void prepare(WorkerTopologyContext context, 
                       GlobalStreamId stream, List<Integer> targetTasks) {
        // Initialization
    }
    
    @Override
    public List<Integer> chooseTasks(int taskId, List<Object> values) {
        // Custom routing logic
        return targetTasks;
    }
}
```

---

## Reliability and Acking

Storm guarantees every tuple is processed:

### Tuple Tree

When a tuple is anchored, it creates a tuple tree:

```java
// Anchor tuple
collector.emit(tuple, new Values(data));

// Process downstream
// ...
```

### Acking Mechanism

Storm uses ackers to track tuple trees:

1. **Spout**: Generates message ID
2. **Bolt**: Anchors tuples and acks input
3. **Acker**: Tracks tuple tree completion
4. **Timeout**: Fails tuple if not completed

### Configuration

```java
Config conf = new Config();
conf.setMessageTimeoutSecs(30);  // Tuple timeout
conf.setMaxSpoutPending(1000);   // Max pending tuples
conf.setNumAckers(1);            // Number of acker tasks
```

### Failure Handling

```java
@Override
public void execute(Tuple input) {
    try {
        // Process tuple
        collector.emit(input, new Values(result));
        collector.ack(input);
    } catch (Exception e) {
        collector.fail(input);  // Trigger replay
    }
}
```

### Guaranteed Processing

- Each tuple must be acked or failed
- Timeout triggers tuple failure
- Failed tuples are replayed
- Exactly-once semantics with proper design

---

## Trident

Trident is a high-level abstraction for Storm:

### Trident Topology

```java
TridentTopology topology = new TridentTopology();

TridentState wordCounts = topology.newStream("kafka-spout", kafkaSpout)
    .each(new Fields("message"), new SplitFunction(), new Fields("word"))
    .groupBy(new Fields("word"))
    .aggregate(new Count(), new Fields("count"))
    .persistentMapState(new Fields("word"))
    .newValuesStream()
    .each(new Fields("word", "count"), new PrintFunction(), new Fields());
```

### Trident Operations

**Batch Operations**
- Each batch processes a group of tuples
- Batches are processed atomically

**Aggregations**
```java
// Count aggregation
stream.aggregate(new Count(), new Fields("count"));

// Sum aggregation
stream.aggregate(new Fields("value"), new Sum(), new Fields("sum"));
```

**Grouping**
```java
// Group by fields
stream.groupBy(new Fields("key"))
    .aggregate(new Count(), new Fields("count"));
```

**Joins**
```java
// Join two streams
TridentTopology topology = new TridentTopology();
TridentState left = topology.newStream("left", leftSpout)
    .persistentMapState(new Fields("id"));
TridentState right = topology.newStream("right", rightSpout)
    .persistentMapState(new Fields("id"));

topology.newStream("merged", mergedSpout)
    .join(left, new Fields("id"), right, new Fields("id"),
          new Fields("left_data", "right_data"));
```

### Trident State

```java
// Define state factory
MapState.Factory factory = new MemoryMapState.Factory();

// Use state in topology
TridentState state = topology.newStream("input", spout)
    .groupBy(new Fields("key"))
    .persistentState(factory)
    .newValuesStream()
    .each(new Fields("key", "value"), new UpdateFunction() {
        @Override
        public void update(TridentTuple tuple, TridentState state) {
            state.update(tuple.getStringByField("key"),
                       tuple.getValueByField("value"));
        }
    });
```

---

## Storm vs Spark Streaming

| Feature | Storm | Spark Streaming |
|---------|-------|-----------------|
| Processing Model | Tuple-at-a-time | Micro-batch |
| Latency | Milliseconds | Seconds |
| State Management | Manual | Built-in (DStream) |
| Exactly-Once | Via Trident | Micro-batch level |
| Backpressure | Built-in | Limited |
| API Complexity | Low-level | High-level |
| Ecosystem | Growing | Mature |
| Fault Tolerance | Tuple replay | RDD lineage |

### When to Use Storm

- Ultra-low latency requirements
- Complex event processing
- Real-time monitoring
- Financial fraud detection
- IoT data processing

### When to Use Spark Streaming

- High throughput requirements
- Existing Spark ecosystem
- Batch and stream unification
- Machine learning integration

---

## Deployment on YARN

### Storm-YARN Setup

```bash
# Configure storm-yarn.yaml
storm.yarn.master.queue: "default"
storm.yarn.workers.queue: "default"
storm.yarn.workers.count: 5
storm.yarn.workers.memory.mb: 8192
storm.yarn.workers.vcores: 4
```

### Submit Topology

```bash
# Submit topology to YARN
storm-yarn submit \
    --name "my-topology" \
    --application-master-args "nimbus.host=nimbus.example.com" \
    topology.jar
```

### Management Commands

```bash
# List running topologies
storm-yarn list

# Kill topology
storm-yarn kill <topology-name>

# Get application status
storm-yarn status <application-id>

# Show topology details
storm-yarn get topology-id <topology-name>
```

### YARN Integration

```
┌─────────────────────────────────────────┐
│              YARN ResourceManager       │
└─────────────────────────────────────────┘
                    │
       ┌────────────┼────────────┐
       ▼            ▼            ▼
┌──────────┐  ┌──────────┐  ┌──────────┐
│   Node   │  │   Node   │  │   Node   │
│ Manager  │  │ Manager  │  │ Manager  │
│  ┌────┐  │  │  ┌────┐  │  │  ┌────┐  │
│  │AM  │  │  │  │NM  │  │  │  │NM  │  │
│  └────┘  │  │  └────┘  │  │  └────┘  │
│  ┌────┐  │  │  ┌────┐  │  │  ┌────┐  │
│  │W1  │  │  │  │W2  │  │  │  │W3  │  │
│  └────┘  │  │  └────┘  │  │  └────┘  │
└──────────┘  └──────────┘  └──────────┘
```

### Configuration

Key configuration for YARN deployment:

```yaml
# storm.yaml
storm.zookeeper.servers:
  - "zk1.example.com"
  - "zk2.example.com"
  - "zk3.example.com"

nimbus.seeds: ["nimbus1.example.com"]
nimbus.thrift.port: 6627

supervisor.slots.ports:
  - 6700
  - 6701
  - 6702
  - 6703

# YARN-specific settings
storm.yarn.master.heap: 1024
storm.yarn.workers.heap: 4096
storm.yarn.workers.java.opts: "-Xmx3g -Dstorm.log.dir=/var/log/storm"
```

---

## Best Practices

### Performance Optimization

1. Use appropriate parallelism
2. Batch small tuples
3. Minimize serialization overhead
4. Use local mode for development
5. Monitor bolt capacity

### Reliability

1. Always anchor important tuples
2. Set appropriate timeouts
3. Handle failures gracefully
4. Use Trident for exactly-once

### Resource Management

1. Right-size worker processes
2. Monitor JVM heap usage
3. Configure appropriate slot counts
4. Use resource isolation

---

## Further Reading

- [Storm Documentation](https://storm.apache.org/)
- [Trident Tutorial](https://storm.apache.org/releases/current/Trident-tutorial.html)
- [Storm-YARN](https://github.com/yahoo/storm-yarn)
