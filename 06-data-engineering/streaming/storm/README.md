# Apache Storm

## Overview

Apache Storm is a free and open-source distributed real-time computation system. Storm provides guaranteed processing of messages, with very high throughput and low latency. It was originally created by Nathan Marz at BackType before being acquired by Twitter.

## Table of Contents

- [Architecture](#architecture)
- [Core Concepts](#core-concepts)
- [Spouts and Bolts](#spouts-and-bolts)
- [Topologies](#topologies)
- [Grouping Strategies](#grouping-strategies)
- [State Management](#state-management)
- [Fault Tolerance](#fault-tolerance)
- [Storm vs Other Systems](#storm-vs-other-systems)
- [Best Practices](#best-practices)

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    STORM CLUSTER                             │
├─────────────────────────────────────────────────────────────┤
│  Nimbus (Master)                                            │
│  • Distributes code across workers                          │
│  • Monitors for failures                                    │
│  • Reassigns tasks on failure                               │
├─────────────────────────────────────────────────────────────┤
│  Supervisor (Worker Nodes)                                  │
│  • Runs worker processes                                    │
│  • Manages local resources                                  │
│  • Reports status to Nimbus                                 │
├─────────────────────────────────────────────────────────────┤
│  ZooKeeper Coordination                                     │
│  • Cluster state management                                 │
│  • Nimbus/Supervisor coordination                           │
│  • Task assignment tracking                                 │
└─────────────────────────────────────────────────────────────┘
```

## Core Concepts

### Tuples

Tuples are Storm's core data structure - an ordered list of elements:

```java
// Tuple structure
public class OrderTuple {
    String orderId;
    String customerId;
    double amount;
    long timestamp;
}
```

### Streams

A stream is an unbounded sequence of tuples processed in parallel.

### Topologies

A topology is a network of spouts and bolts connected by stream groupings.

## Spouts and Bolts

### Spouts (Data Sources)

```java
public class KafkaSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private KafkaConsumer<String, String> consumer;

    @Override
    public void open(Map conf, TopologyContext context, 
                     SpoutOutputCollector collector) {
        this.collector = collector;
        this.consumer = new KafkaConsumer<>(kafkaProps);
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
        declarer.declare(new Fields("key", "value"));
    }
}
```

### Bolts (Processing Units)

```java
public class OrderProcessingBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String orderId = input.getStringByField("orderId");
        double amount = input.getDoubleByField("amount");

        // Process order
        OrderResult result = processOrder(orderId, amount);

        // Emit downstream
        collector.emit(new Values(orderId, result.getStatus(), 
                                  result.getDetails()));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("orderId", "status", "details"));
    }
}
```

### BaseRichBolt vs BaseBasicBolt

| Feature | BaseRichBolt | BaseBasicBolt |
|---------|--------------|---------------|
| Tuple acknowledgment | Manual | Automatic |
| Error handling | Manual ack/fail | Automatic ack |
| State management | More control | Simpler API |
| Use case | Complex operations | Simple transformations |

## Topologies

### Building a Topology

```java
public class OrderTopology {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        // Spout: reads from Kafka
        builder.setSpout("kafka-spout", new KafkaSpout(), 4);

        // Bolt: validates orders
        builder.setBolt("validator", new OrderValidatorBolt(), 8)
               .shuffleGrouping("kafka-spout");

        // Bolt: enriches with customer data
        builder.setBolt("enricher", new CustomerEnrichmentBolt(), 4)
               .fieldsGrouping("validator", new Fields("customerId"));

        // Bolt: persists to database
        builder.setBolt("persister", new DatabasePersisterBolt(), 4)
               .fieldsGrouping("enricher", new Fields("orderId"));

        // Bolt: sends to notification topic
        builder.setBolt("notifier", new NotificationBolt(), 2)
               .globalGrouping("persister");

        // Submit topology
        Config conf = new Config();
        conf.setNumWorkers(4);
        conf.setMaxSpoutPending(1000);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("order-processing", conf, 
                              builder.createTopology());
    }
}
```

### Topology Lifecycle

```
Submit → Activate → Run → Deactivate → Kill
  │         │        │         │           │
  ▼         ▼        ▼         ▼           ▼
Prepare   Start   Process   Pause      Cleanup
```

## Grouping Strategies

### Shuffle Grouping

Random distribution of tuples across bolt tasks:

```java
builder.setBolt("processor", new ProcessorBolt(), 4)
       .shuffleGrouping("source");
```

### Fields Grouping

Consistent hashing ensures same field values go to same task:

```java
builder.setBolt("aggregator", new AggregatorBolt(), 4)
       .fieldsGrouping("source", new Fields("userId"));
```

### Global Grouping

All tuples go to a single task:

```java
builder.setBolt("counter", new CounterBolt(), 1)
       .globalGrouping("source");
```

### All Grouping

Tuples replicated to all tasks:

```java
builder.setBolt("broadcaster", new BroadcasterBolt(), 4)
       .allGrouping("source");
```

### Direct Grouping

Tuple source determines target task:

```java
builder.setBolt("target", new TargetBolt(), 4)
       .directGrouping("source");
```

### Custom Grouping

```java
public class ConsistentHashGrouping implements CustomStreamGrouping {
    private List<Integer> targetTasks;

    @Override
    public void prepare(WorkerTopologyContext context, 
                       GlobalStreamId stream, List<Integer> targetTasks) {
        this.targetTasks = targetTasks;
    }

    @Override
    public List<Integer> chooseTasks(int taskId, List<Object> values) {
        int hash = Math.abs(values.get(0).hashCode());
        int index = hash % targetTasks.size();
        return Collections.singletonList(targetTasks.get(index));
    }
}
```

## State Management

### Storm State Management

```java
// Using Trident for stateful processing
TridentTopology topology = new TridentTopology();

TridentState wordCounts = topology.newStream("spout", spout)
    .each(new Fields("sentence"), new SplitFunction(), new Fields("word"))
    .groupBy(new Fields("word"))
    .persistentAggregate(new MemoryMapState.Factory(), new Count(), 
                         new Fields("count"))
    .newValuesStream();
```

### Windowing

```java
public class SlidingWindowBolt extends BaseWindowedBolt {
    @Override
    public void execute(TupleWindow inputWindow) {
        List<Tuple> tuples = inputWindow.get();
        
        // Process all tuples in window
        double sum = tuples.stream()
            .mapToDouble(t -> t.getDoubleByField("amount"))
            .sum();
        
        collector.emit(new Values(sum));
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context,
                       OutputCollector collector) {
        this.collector = collector;
    }
}

// Usage
builder.setBolt("windowed", new SlidingWindowBolt()
    .withWindow(Time.seconds(60), Time.seconds(10)), 4)
    .fieldsGrouping("source", new Fields("key"));
```

## Fault Tolerance

### Message Acknowledgment

```java
// Spout ack mechanism
public void ack(Object msgId) {
    // Message fully processed
    System.out.println("Message " + msgId + " processed successfully");
}

public void fail(Object msgId) {
    // Message failed - replay
    System.out.println("Message " + msgId + " failed - will replay");
}
```

### Reliability Configuration

```java
Config conf = new Config();
conf.setNumAckers(1);           // Number of acker tasks
conf.setMaxSpoutPending(1000);  // Max unacked tuples
conf.setMessageTimeoutSecs(30); // Timeout for tuple trees
```

### Trident (Exactly-Once)

```java
TridentTopology topology = new TristentTopology();

topology.newStream("spout", spout)
    .each(new Fields("data"), new ProcessFunction(), new Fields("result"))
    .partitionPersist(new BaseStateFactory() {
        @Override
        public State makeState(Map conf, TridentCollector collector, 
                              int partitionIndex, int numPartitions) {
            return new TransactionalState();
        }
    }, new Fields("result"), new StateUpdater() {
        @Override
        public void updateState(State state, TridentTuple[] tuples,
                               TridentCollector collector) {
            for (TridentTuple tuple : tuples) {
                state.put(tuple.getString(0), tuple.getValue(1));
            }
        }
    });
```

## Storm vs Other Systems

| Feature | Storm | Kafka Streams | Flink | Spark Streaming |
|---------|-------|---------------|-------|-----------------|
| Processing | Tuple-at-a-time | Micro-batch | Micro-batch/event | Micro-batch |
| Latency | Milliseconds | Milliseconds | Milliseconds | Seconds |
| Throughput | High | Very High | Very High | Very High |
| State Management | External | Built-in | Built-in | Built-in |
| Exactly-once | Via Trident | Built-in | Built-in | Built-in |
| Ease of Use | Moderate | High | High | High |
| Maturity | High | Medium | High | High |

## Best Practices

### 1. Bolt Parallelism

```java
// Right-size your bolts based on processing time
long avgProcessingTime = 50; // ms
long targetLatency = 100; // ms
int requiredTasks = (int) Math.ceil(avgProcessingTime / targetLatency);
builder.setBolt("processor", new ProcessorBolt(), requiredTasks);
```

### 2. Data Serialization

```java
// Use efficient serialization
public class OrderSerializer implements Serializable {
    private byte[] serialize(Order order) {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        buffer.putLong(order.getId());
        buffer.putDouble(order.getAmount());
        buffer.put(order.getCustomerName().getBytes());
        return buffer.array();
    }
}
```

### 3. Resource Management

```yaml
# Storm topology configuration
topology.workers: 4
topology.message.timeout.secs: 30
topology.max.spout.pending: 1000
topology.acker.executors: 1
topology.tasks: 20
```

### 4. Monitoring

```java
// Add metrics to bolts
public class MonitoredBolt extends BaseRichBolt {
    private transient Counter counter;
    private transient Meter meter;

    @Override
    public void prepare(Map conf, TopologyContext context,
                       OutputCollector collector) {
        this.counter = context.registerMetric("processed", 
                                             new Count(), 60);
        this.meter = context.registerMetric("latency",
                                           new Meter(), 60);
    }

    @Override
    public void execute(Tuple input) {
        long start = System.currentTimeMillis();
        // Process tuple
        counter.inc();
        meter.mark();
        long latency = System.currentTimeMillis() - start;
    }
}
```

## Further Reading

- [Apache Storm Documentation](https://storm.apache.org/)
- [Storm Trident](https://storm.apache.org/releases/current/Trident-tutorial.html)
- [Storm SQL](https://storm.apache.org/releases/current/Storm-SQL.html)
