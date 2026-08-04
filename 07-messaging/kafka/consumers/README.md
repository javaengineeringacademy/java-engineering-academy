# Kafka Consumers

## Consumer Groups, Offsets, Rebalancing, and Consumer Configuration

---

## Table of Contents

- [Overview](#overview)
- [Consumer Architecture](#consumer-architecture)
- [Consumer Groups](#consumer-groups)
- [Offset Management](#offset-management)
- [Rebalancing](#rebalancing)
- [Consumer Configuration](#consumer-configuration)
- [Consumer Protocols](#consumer-protocols)
- [Advanced Patterns](#advanced-patterns)
- [Troubleshooting](#troubleshooting)
- [Best Practices](#best-practices)

---

## Overview

Kafka consumers read messages from Kafka topics. Consumers are organized into consumer groups, which enable parallel processing and load balancing across multiple instances.

### Key Concepts

- **Consumer**: Application that reads messages from Kafka
- **Consumer Group**: Logical grouping of consumers for load balancing
- **Partition Assignment**: Each partition assigned to one consumer in a group
- **Offset**: Position marker for consumer's progress
- **Rebalancing**: Redistribution of partitions when consumers join/leave

---

## Consumer Architecture

### Consumer Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Kafka Consumer                            │
│                                                              │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐              │
│  │  poll()  │───▶│  Fetch   │───▶│  Record  │              │
│  │          │    │  Manager │    │  Buffer  │              │
│  └──────────┘    └──────────┘    └────┬─────┘              │
│                                       │                     │
│                                       ▼                     │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐              │
│  │  commit  │◀───│  User    │◀───│  Deser-  │              │
│  │  Offsets │    │  Code    │    │  ializer │              │
│  └──────────┘    └──────────┘    └──────────┘              │
│                                                              │
│  ┌──────────┐    ┌──────────┐                               │
│  │  Group   │◀──▶│  Coord-  │                               │
│  │  Coord-  │    │  inator  │                               │
│  │  inator  │    └──────────┘                               │
│  └──────────┘                                               │
└─────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────┐
│                    Kafka Broker Cluster                      │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐              │
│  │ Broker 1 │    │ Broker 2 │    │ Broker 3 │              │
│  │ Partition│    │ Partition│    │ Partition│              │
│  │ 0        │    │ 1        │    │ 2        │              │
│  └──────────┘    └──────────┘    └──────────┘              │
└─────────────────────────────────────────────────────────────┘
```

### Consumer Components

| Component | Responsibility |
|-----------|---------------|
| Consumer API | Application interface for consuming messages |
| Fetch Manager | Manages fetch requests to brokers |
| Record Buffer | Buffers deserialized records |
| Group Coordinator | Manages consumer group membership |
| Partition Assignor | Determines partition assignment |
| Offset Manager | Tracks and commits offsets |

---

## Consumer Groups

### Group Membership

```
Consumer Group: order-processor

┌─────────────────────────────────────────────────────────────┐
│                                                              │
│  Consumer 1          Consumer 2          Consumer 3          │
│  ┌──────────┐        ┌──────────┐        ┌──────────┐      │
│  │ Group ID │        │ Group ID │        │ Group ID │      │
│  │ Member ID│        │ Member ID│        │ Member ID│      │
│  └──────────┘        └──────────┘        └──────────┘      │
│       │                  │                  │                │
│       └──────────────────┼──────────────────┘                │
│                          │                                   │
│                          ▼                                   │
│              ┌──────────────────────┐                       │
│              │   Group Coordinator  │                       │
│              │   (Broker)           │                       │
│              └──────────────────────┘                       │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Partition Assignment

```
Topic: orders (6 partitions)
Consumer Group: order-processor

Consumer 1: Partitions [0, 1]
Consumer 2: Partitions [2, 3]
Consumer 3: Partitions [4, 5]

┌──────────────┐
│  Partition 0 │ → Consumer 1
│  Partition 1 │ → Consumer 1
│  Partition 2 │ → Consumer 2
│  Partition 3 │ → Consumer 2
│  Partition 4 │ → Consumer 3
│  Partition 5 │ → Consumer 3
└──────────────┘
```

### Consumer Scaling

```
Scenario 1: 3 consumers, 6 partitions
Consumer 1: [0, 1]
Consumer 2: [2, 3]
Consumer 3: [4, 5]

Scenario 2: 6 consumers, 6 partitions
Consumer 1: [0]
Consumer 2: [1]
Consumer 3: [2]
Consumer 4: [3]
Consumer 5: [4]
Consumer 6: [5]

Scenario 3: 7 consumers, 6 partitions
Consumer 1: [0]
Consumer 2: [1]
Consumer 3: [2]
Consumer 4: [3]
Consumer 5: [4]
Consumer 6: [5]
Consumer 7: [idle]  ← No partition assigned
```

---

## Offset Management

### Offset Storage

```
__consumer_offsets topic (internal)

Key: {group_id, topic, partition}
Value: {offset, metadata, timestamp}

Example:
Group: order-processor
Topic: orders
Partition 0: offset=152
Partition 1: offset=234
Partition 2: offset=189
```

### Offset Commit Strategies

#### Automatic Commit

```java
props.put("enable.auto.commit", true);
props.put("auto.commit.interval.ms", 5000);  // Commit every 5 seconds

// Consumer automatically commits offsets
ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
for (ConsumerRecord<String, String> record : records) {
    processRecord(record);
}
// Offsets committed automatically
```

#### Manual Commit - Synchronous

```java
props.put("enable.auto.commit", false);

ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
for (ConsumerRecord<String, String> record : records) {
    processRecord(record);
}
consumer.commitSync();  // Blocks until commit succeeds
```

#### Manual Commit - Asynchronous

```java
props.put("enable.auto.commit", false);

ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
for (ConsumerRecord<String, String> record : records) {
    processRecord(record);
}
consumer.commitAsync((offsets, exception) -> {
    if (exception != null) {
        log.error("Commit failed: {}", exception.getMessage());
    }
});
```

#### Per-Partition Commit

```java
ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
for (TopicPartition partition : records.partitions()) {
    for (ConsumerRecord<String, String> record : records.records(partition)) {
        processRecord(record);
    }
    // Commit offset for this partition
    long lastOffset = records.records(partition).get(
        records.records(partition).size() - 1).offset();
    consumer.commitSync(Collections.singletonMap(
        partition, new OffsetAndMetadata(lastOffset + 1)));
}
```

### Offset Reset Policies

```java
props.put("auto.offset.reset", "earliest");  // Start from beginning
props.put("auto.offset.reset", "latest");    // Start from end (default)
props.put("auto.offset.reset", "none");      // Throw exception
```

### Offset Operations

```bash
# Reset offsets to earliest
kafka-consumer-groups.sh --reset-offsets \
  --group order-processor \
  --topic orders \
  --to-earliest \
  --execute

# Reset offsets to specific datetime
kafka-consumer-groups.sh --reset-offsets \
  --group order-processor \
  --topic orders \
  --to-datetime 2024-01-01T00:00:00.000

# Shift offsets by N
kafka-consumer-groups.sh --reset-offsets \
  --group order-processor \
  --topic orders \
  --shift-by -100 \
  --execute
```

---

## Rebalancing

### Rebalance Trigger

```
┌─────────────────────────────────────────────────────────────┐
│              Rebalance Triggers                              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. New consumer joins group                                │
│     └── Consumer 4 joins group                              │
│     └── Partitions redistributed                            │
│                                                             │
│  2. Consumer leaves group                                   │
│     └── Consumer 2 crashes                                  │
│     └── Partitions reassigned                               │
│                                                             │
│  3. Consumer stops heartbeating                             │
│     └── Consumer 1 unresponsive                             │
│     └── Group coordinator removes it                        │
│                                                             │
│  4. Topic partitions change                                 │
│     └── New partitions added to topic                       │
│     └── Partitions reassigned                               │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Rebalance Flow

```
Phase 1: Join Group
─────────────────────
Consumer 1 ──── JoinGroup ───▶ Coordinator
Consumer 2 ──── JoinGroup ───▶ Coordinator
Consumer 3 ──── JoinGroup ───▶ Coordinator

Phase 2: Synchronize Group
────────────────────────────
Coordinator ──── SyncGroup ───▶ Consumer 1 (assignments)
Coordinator ──── SyncGroup ───▶ Consumer 2 (assignments)
Coordinator ──── SyncGroup ───▶ Consumer 3 (assignments)

Phase 3: Rebalance Complete
────────────────────────────
Consumer 1: [Partition 0, Partition 1]
Consumer 2: [Partition 2, Partition 3]
Consumer 3: [Partition 4, Partition 5]
```

### Rebalance Protocols

#### Eager Rebalance (Default)

```java
// All consumers stop processing
// All partitions revoked
// Partitions reassigned
// Consumers resume processing

// During rebalance:
// - Consumer 1 processing partition 0, 1
// - Consumer 1 stops processing
// - Consumer 2 stops processing
// - Consumer 3 stops processing
// - Partitions reassigned
// - All consumers resume
```

#### Cooperative Rebalance (Incremental)

```java
props.put("partition.assignment.strategy", 
    "org.apache.kafka.clients.consumer.CooperativeStickyAssignor");

// Only affected partitions are revoked
// Consumer 1 continues processing partition 0, 1
// Consumer 2 only revokes partition 2, 3
// Consumer 3 continues processing partition 4, 5
```

### Rebalance Configuration

```java
// Session timeout (how long before consumer is considered dead)
props.put("session.timeout.ms", 10000);

// Heartbeat interval (how often consumer sends heartbeat)
props.put("heartbeat.interval.ms", 3000);

// Max poll interval (max time between polls)
props.put("max.poll.interval.ms", 300000);

// Max poll records (max records per poll)
props.put("max.poll.records", 500);
```

### Rebalance Listeners

```java
consumer.subscribe(Arrays.asList("topic"), new ConsumerRebalanceListener() {
    
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        // Commit final offsets
        consumer.commitSync();
        // Release resources
        releaseResources();
    }
    
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        // Initialize resources for new partitions
        initializeResources(partitions);
    }
});
```

---

## Consumer Configuration

### Essential Configuration

```java
Properties props = new Properties();

// Bootstrap servers
props.put("bootstrap.servers", "broker1:9092,broker2:9092");

// Consumer group
props.put("group.id", "order-processor");

// Deserializers
props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

// Offset management
props.put("enable.auto.commit", false);
props.put("auto.offset.reset", "earliest");

// Isolation level
props.put("isolation.level", "read_committed");
```

### Configuration Reference

#### Core Settings

| Property | Default | Description |
|----------|---------|-------------|
| `bootstrap.servers` | - | List of broker addresses |
| `group.id` | - | Consumer group identifier |
| `key.deserializer` | - | Deserializer for message keys |
| `value.deserializer` | - | Deserializer for message values |
| `auto.offset.reset` | latest | Offset reset policy |

#### Performance Settings

| Property | Default | Description |
|----------|---------|-------------|
| `max.poll.records` | 500 | Maximum records returned by poll() |
| `max.poll.interval.ms` | 300000 | Maximum time between polls |
| `fetch.min.bytes` | 1 | Minimum bytes per fetch |
| `fetch.max.wait.ms` | 500 | Maximum wait time for fetch |
| `fetch.max.bytes` | 52428800 | Maximum bytes per fetch |

#### Reliability Settings

| Property | Default | Description |
|----------|---------|-------------|
| `enable.auto.commit` | true | Enable automatic offset commits |
| `auto.commit.interval.ms` | 5000 | Auto-commit interval |
| `session.timeout.ms` | 10000 | Session timeout |
| `heartbeat.interval.ms` | 3000 | Heartbeat interval |
| `max.session.timeout.ms` | 1800000 | Maximum session timeout |

#### Assignment Settings

| Property | Default | Description |
|----------|---------|-------------|
| `partition.assignment.strategy` | RangeAssignor | Partition assignment strategy |
| `exclude.internal.topics` | true | Exclude internal topics |
| `isolation.level` | read_uncommitted | Transaction isolation level |

---

## Consumer Protocols

### Consumer Rebalance Protocol

```
┌─────────────────────────────────────────────────────────────┐
│              Consumer Rebalance Protocol                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  State 1: Empty                                             │
│  └── No members in group                                    │
│                                                             │
│  State 2: Preparing Rebalance                               │
│  └── Members joining/leaving                                │
│  └── Waiting for all members to join                        │
│                                                             │
│  State 3: Completing Rebalance                              │
│  └── Assigning partitions to members                        │
│  └── Waiting for SyncGroup responses                        │
│                                                             │
│  State 4: Stable                                            │
│  └── Partitions assigned                                    │
│  └── Members processing                                     │
│                                                             │
│  State 5: Dead                                              │
│  └── Group coordinator failed                               │
│  └── New coordinator elected                                │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Heartbeat Protocol

```
┌─────────────────────────────────────────────────────────────┐
│              Heartbeat Protocol                              │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Consumer                           Coordinator            │
│     │                                   │                  │
│     │──── Heartbeat(groupGeneration) ──▶│                  │
│     │                                   │                  │
│     │◀─── HeartbeatResponse ───────────│                  │
│     │                                   │                  │
│  (If no heartbeat for session.timeout.ms:                   │
│   Consumer removed from group)                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Advanced Patterns

### Consumer with Manual Partition Assignment

```java
// Manually assign partitions (no consumer group coordination)
TopicPartition partition0 = new TopicPartition("orders", 0);
TopicPartition partition1 = new TopicPartition("orders", 1);

consumer.assign(Arrays.asList(partition0, partition1));

// Seek to specific offset
consumer.seek(partition0, 0);
consumer.seek(partition1, 100);

// Poll and process
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        processRecord(record);
    }
    consumer.commitSync();
}
```

### Consumer with Timestamp-Based Seeking

```java
// Get offsets by timestamp
Map<TopicPartition, Long> timestamps = new HashMap<>();
timestamps.put(new TopicPartition("orders", 0), Instant.now()
    .minus(Duration.ofHours(24)).toEpochMilli());

Map<TopicPartition, OffsetAndTimestamp> offsets = 
    consumer.offsetsForTimes(timestamps);

// Seek to found offsets
for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : offsets.entrySet()) {
    if (entry.getValue() != null) {
        consumer.seek(entry.getKey(), entry.getValue().offset());
    }
}
```

### Consumer with Transactional Consumption

```java
// Consume only committed transactions
props.put("isolation.level", "read_committed");

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
consumer.subscribe(Arrays.asList("orders"));

while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        // Only see records from committed transactions
        processRecord(record);
    }
    consumer.commitSync();
}
```

---

## Troubleshooting

### Common Issues

| Issue | Symptom | Solution |
|-------|---------|----------|
| High consumer lag | Consumer falling behind | Increase consumers, optimize processing |
| Frequent rebalances | Frequent pauses | Increase session.timeout.ms, reduce max.poll.interval.ms |
| Duplicate processing | Same message processed twice | Use idempotent consumers, manual commit |
| Consumer crashes | Consumer repeatedly restarts | Check max.poll.interval.ms, increase session.timeout.ms |

### Consumer Lag Monitoring

```bash
# Check consumer lag
kafka-consumer-groups.sh --describe \
  --group order-processor \
  --bootstrap-server localhost:9092

# Output:
# GROUP           TOPIC  PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG
# order-processor orders 0          152             234             82
```

### Rebalance Debugging

```java
// Add rebalance listener for debugging
consumer.subscribe(Arrays.asList("topic"), new ConsumerRebalanceListener() {
    
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        log.info("Partitions revoked: {}", partitions);
    }
    
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        log.info("Partitions assigned: {}", partitions);
    }
});
```

---

## Best Practices

### Configuration

1. **Use manual offset commits** for reliable processing
2. **Set appropriate session.timeout.ms** based on processing time
3. **Use cooperative rebalancing** to minimize processing disruption
4. **Configure max.poll.records** based on processing capacity

### Processing

1. **Process messages idempotently** to handle duplicates
2. **Implement dead letter queues** for unprocessable messages
3. **Use batch processing** when possible for efficiency
4. **Monitor consumer lag** and set up alerts

### Rebalancing

1. **Keep processing time within max.poll.interval.ms**
2. **Use rebalance listeners** to properly manage resources
3. **Consider cooperative rebalancing** for minimal disruption
4. **Test rebalance scenarios** in staging environment

### Monitoring

1. **Track consumer lag** per partition
2. **Monitor rebalance frequency**
3. **Alert on consumer crashes**
4. **Track processing latency**

---

## Further Reading

- [Kafka Consumer Configuration](https://kafka.apache.org/documentation/#consumerconfigs)
- [Consumer Group Protocol](https://kafka.apache.org/documentation/#consumerconfigs)
- [Rebalancing in Apache Kafka](https://kafka.apache.org/documentation/#rebalance)
