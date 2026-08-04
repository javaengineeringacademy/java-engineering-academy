# Kafka Partitions

## Partitioning Strategies, Ordering Guarantees, and Partition Management

---

## Table of Contents

- [Overview](#overview)
- [Partition Concepts](#partition-concepts)
- [Partitioning Strategies](#partitioning-strategies)
- [Ordering Guarantees](#ordering-guarantees)
- [Partition Reassignment](#partition-reassignment)
- [Partition Leadership](#partition-leadership)
- [Partition Monitoring](#partition-monitoring)
- [Best Practices](#best-practices)

---

## Overview

Partitions are the fundamental unit of parallelism in Kafka. They enable horizontal scaling, parallel processing, and ordered message delivery within a partition.

### Key Concepts

- **Partition**: Ordered, immutable sequence of messages
- **Offset**: Unique sequential identifier for each message
- **Leader**: Partition replica that handles all reads/writes
- **Follower**: Replica that replicates from leader
- **ISR**: In-Sync Replicas that are fully caught up

---

## Partition Concepts

### Partition Structure

```
Partition: orders-0 (Leader on Broker 1)

┌─────────────────────────────────────────────────────────────┐
│                    Partition 0                               │
├─────────────────────────────────────────────────────────────┤
│  Offset 0 │ key: order-1  │ value: {...} │ timestamp: t0   │
│  Offset 1 │ key: order-2  │ value: {...} │ timestamp: t1   │
│  Offset 2 │ key: order-3  │ value: {...} │ timestamp: t2   │
│  Offset 3 │ key: order-4  │ value: {...} │ timestamp: t3   │
│  Offset 4 │ key: order-5  │ value: {...} │ timestamp: t4   │
└─────────────────────────────────────────────────────────────┘
     ▲
     │
  Messages are immutable once written
  Offset never reused
```

### Partition Files

```
Partition Directory: /var/lib/kafka/data/orders-0/
│
├── 00000000000000000000.index      # Offset → position index
├── 00000000000000000000.log        # Message data
├── 00000000000000000000.timeindex  # Timestamp → offset index
│
├── 00000000000000000100.index
├── 00000000000000000100.log
├── 00000000000000000100.timeindex
│
└── leader-epoch-checkpoint
```

### Partition Replication

```
Topic: orders (3 partitions, replication factor 3)

Partition 0:
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Broker 1  │     │   Broker 2  │     │   Broker 3  │
│   (Leader)  │────▶│  (Follower) │     │  (Follower) │
│             │     │             │     │             │
│  ISR: [1,2,3]     │  ISR: [1,2,3]     │  ISR: [1,2,3] │
└─────────────┘     └─────────────┘     └─────────────┘

Partition 1:
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Broker 1  │     │   Broker 2  │     │   Broker 3  │
│  (Follower) │     │   (Leader)  │────▶│  (Follower) │
│             │     │             │     │             │
│  ISR: [1,2,3]     │  ISR: [1,2,3]     │  ISR: [1,2,3] │
└─────────────┘     └─────────────┘     └─────────────┘

Partition 2:
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Broker 1  │     │   Broker 2  │     │   Broker 3  │
│  (Follower) │     │  (Follower) │     │   (Leader)  │
│             │     │             │     │             │
│  ISR: [1,2,3]     │  ISR: [1,2,3]     │  ISR: [1,2,3] │
└─────────────┘     └─────────────┘     └─────────────┘
```

---

## Partitioning Strategies

### Default Partitioner

```java
// Default behavior
if (key == null) {
    // Round-robin across partitions
    return nextPartition();
} else {
    // Hash key and mod by partition count
    return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
}
```

### Round-Robin Partitioning

```
Topic: orders (3 partitions)
Messages with null keys:

Message 1 → Partition 0
Message 2 → Partition 1
Message 3 → Partition 2
Message 4 → Partition 0  (wraps around)
Message 5 → Partition 1
Message 6 → Partition 2
```

### Key-Based Partitioning

```
Topic: orders (3 partitions)
Messages with keys:

Key: user-123 → hash(user-123) % 3 = Partition 0
Key: user-456 → hash(user-456) % 3 = Partition 1
Key: user-789 → hash(user-789) % 3 = Partition 2
Key: user-123 → hash(user-123) % 3 = Partition 0  (same key = same partition)
```

### Custom Partitioner

```java
public class RegionPartitioner implements Partitioner {
    
    private Map<String, Integer> regionPartitionMap;
    
    @Override
    public void configure(Map<String, ?> configs) {
        regionPartitionMap = new HashMap<>();
        regionPartitionMap.put("us-east", 0);
        regionPartitionMap.put("us-west", 1);
        regionPartitionMap.put("eu-west", 2);
    }
    
    @Override
    public int partition(String topic, Object key, byte[] keyBytes,
                        Object value, byte[] valueBytes, Cluster cluster) {
        String region = (String) key;
        return regionPartitionMap.getOrDefault(region, 0);
    }
    
    @Override
    public void close() {}
}

// Use custom partitioner
props.put("partitioner.class", "com.example.RegionPartitioner");

ProducerRecord<String, String> record = 
    new ProducerRecord<>("topic", "us-east", "value");
```

### Sticky Partitioning

Kafka 2.4+ introduced sticky partitioning for null keys:

```
Without sticky partitioning (round-robin):
Message 1 → Partition 0
Message 2 → Partition 1
Message 3 → Partition 2
Message 4 → Partition 0

With sticky partitioning:
Message 1 → Partition 0 (batch full or linger.ms reached)
Message 2 → Partition 0
Message 3 → Partition 0
Message 4 → Partition 1 (switch partition)
```

Benefits:
- Reduces number of in-flight requests
- Improves batching efficiency
- Lower latency for null-key messages

---

## Ordering Guarantees

### Per-Partition Ordering

```
Partition 0:
┌─────────────────────────────────────────────────────────────┐
│  Offset 0 (t1) ──▶ Offset 1 (t2) ──▶ Offset 2 (t3)      │
│       │                   │                   │             │
│       ▼                   ▼                   ▼             │
│   Strict ordering within partition                          │
└─────────────────────────────────────────────────────────────┘

Partition 1:
┌─────────────────────────────────────────────────────────────┐
│  Offset 0 (t2) ──▶ Offset 1 (t3) ──▶ Offset 2 (t4)      │
│       │                   │                   │             │
│       ▼                   ▼                   ▼             │
│   Strict ordering within partition                          │
└─────────────────────────────────────────────────────────────┘

NO ordering guarantee between Partition 0 and Partition 1
```

### Key-Based Ordering

```java
// Same key → same partition → ordered processing
ProducerRecord<String, String> record1 = 
    new ProducerRecord<>("topic", "user-123", "event-1");
ProducerRecord<String, String> record2 = 
    new ProducerRecord<>("topic", "user-123", "event-2");
ProducerRecord<String, String> record3 = 
    new ProducerRecord<>("topic", "user-123", "event-3");

// All three messages go to same partition
// Consumer processes them in order: event-1, event-2, event-3
```

### Global Ordering

```
Kafka does NOT provide global ordering by default.

For global ordering:
1. Use single partition topic (limits parallelism)
2. Use key-based partitioning (ordering per key)
3. Use application-level ordering (timestamp-based)
```

### Ordering with Idempotent Producer

```java
// Enable idempotent producer
props.put("enable.idempotence", true);

// Guarantees:
// - Exactly-once delivery per partition
// - Ordering maintained within partition
// - No duplicates within partition
```

### Ordering with Transactions

```java
// Transactional producer
props.put("transactional.id", "my-transactional-id");

producer.beginTransaction();
producer.send(record1);
producer.send(record2);
producer.send(record3);
producer.commitTransaction();

// Guarantees:
// - Atomic write of all messages
// - Ordering maintained
// - Exactly-once delivery
```

---

## Partition Reassignment

### When to Reassign

- Broker decommissioning
- Load balancing
- Hardware upgrade
- Disk failure recovery

### Reassignment Process

```
Before Reassignment:
Broker 1: Partition 0 (Leader), Partition 1 (Follower)
Broker 2: Partition 0 (Follower), Partition 1 (Leader)
Broker 3: (Empty)

After Reassignment:
Broker 1: Partition 0 (Leader)
Broker 2: Partition 1 (Leader)
Broker 3: Partition 0 (Follower), Partition 1 (Follower)
```

### Reassignment Commands

```bash
# Generate reassignment plan
kafka-reassign-partitions.sh --bootstrap-server localhost:9092 \
  --topics-to-move-json-file topics-to-move.json \
  --broker-list "1,2,3" \
  --generate

# Execute reassignment
kafka-reassign-partitions.sh --bootstrap-server localhost:9092 \
  --reassignment-json-file reassignment.json \
  --execute

# Verify reassignment
kafka-reassign-partitions.sh --bootstrap-server localhost:9092 \
  --reassignment-json-file reassignment.json \
  --verify
```

### Reassignment JSON Format

```json
{
  "partitions": [
    {
      "topic": "orders",
      "partition": 0,
      "replicas": [1, 2, 3]
    },
    {
      "topic": "orders",
      "partition": 1,
      "replicas": [2, 3, 1]
    }
  ]
}
```

### Reassignment Throttling

```bash
# Limit reassignment bandwidth
kafka-configs.sh --bootstrap-server localhost:9092 \
  --entity-type brokers \
  --entity-name 1 \
  --add-config follower.replication.throttled.rate=10000000

# Or use JSON with throttle
{
  "partitions": [...],
  "version": 1
}
```

---

## Partition Leadership

### Leader Election

```
Normal Operation:
Partition 0: Leader=Broker1, ISR=[1,2,3]
Partition 1: Leader=Broker2, ISR=[1,2,3]
Partition 2: Leader=Broker3, ISR=[1,2,3]

Broker 1 fails:
Partition 0: Leader=Broker2, ISR=[2,3]  ← Broker2 elected leader
Partition 1: Leader=Broker2, ISR=[2,3]
Partition 2: Leader=Broker3, ISR=[2,3]
```

### Leader Distribution

```
Balanced Leadership:
Broker 1: Partition 0 (Leader), Partition 3 (Leader)
Broker 2: Partition 1 (Leader), Partition 4 (Leader)
Broker 3: Partition 2 (Leader), Partition 5 (Leader)

Unbalanced Leadership:
Broker 1: Partition 0, 1, 2, 3, 4, 5 (All leaders)  ← Bad!
Broker 2: (No leaders)
Broker 3: (No leaders)
```

### Leader Rebalancing

```bash
# Trigger leader rebalance
kafka-preferred-replica-election.sh --bootstrap-server localhost:9092 \
  --topic orders

# Or use JSON file
kafka-preferred-replica-election.sh --bootstrap-server localhost:9092 \
  --election-type preferred \
  --election-json-file election.json
```

### Unclean Leader Election

```properties
# Allow non-ISR replica to become leader?
unclean.leader.election.enable=false

# false: Safer, but may cause availability issues
# true: More available, but risks data loss
```

---

## Partition Monitoring

### Key Metrics

| Metric | Description |
|--------|-------------|
| `UnderReplicatedPartitions` | Partitions with ISR < replication factor |
| `IsrShrinkPerSec` | Rate of ISR shrink events |
| `IsrExpandPerSec` | Rate of ISR expand events |
| `ActiveControllerCount` | Number of active controllers |
| `OfflinePartitionsCount` | Partitions without leader |
| `LeaderElectionRateAndTimeMs` | Leader election rate and latency |

### Monitoring Commands

```bash
# Check partition status
kafka-topics.sh --describe \
  --topic orders \
  --bootstrap-server localhost:9092

# Check under-replicated partitions
kafka-topics.sh --describe \
  --bootstrap-server localhost:9092 | grep "Isr:"

# Check partition count per broker
kafka-topics.sh --describe \
  --bootstrap-server localhost:9092 | grep "Leader:"
```

### Prometheus Metrics

```yaml
# Kafka Exporter metrics
kafka_topic_partition_current_offset
kafka_topic_partition_replica_count
kafka_topic_partition_in_sync_replica_count
kafka_topic_partition_under_replicated_partition
kafka_brokers
kafka_topic_partitions
```

### Alerting Rules

```yaml
# Alert on under-replicated partitions
groups:
  - name: kafka
    rules:
      - alert: KafkaUnderReplicatedPartitions
        expr: kafka_topic_partition_under_replicated_partition > 0
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Under-replicated partitions detected"
          
      - alert: KafkaNoLeader
        expr: kafka_topic_partition_leader == -1
        for: 1m
        labels:
          severity: critical
        annotations:
          summary: "Partition without leader"
```

---

## Best Practices

### Partition Design

1. **Plan partition count** before production
2. **Use key-based partitioning** for ordering
3. **Avoid hot partitions** with good key design
4. **Consider future growth** when setting partition count

### Partition Management

1. **Monitor partition distribution** across brokers
2. **Balance leader distribution** for load balancing
3. **Use preferred replica election** for leader balancing
4. **Plan reassignment carefully** to minimize impact

### Ordering Requirements

1. **Use key-based partitioning** for per-key ordering
2. **Use single partition** for global ordering (limits parallelism)
3. **Use idempotent producer** for exactly-once ordering
4. **Use transactions** for atomic multi-partition writes

### Monitoring

1. **Track under-replicated partitions**
2. **Monitor ISR shrink/expand rates**
3. **Alert on offline partitions**
4. **Monitor partition leadership distribution**

### Common Pitfalls

1. **Over-partitioning** - Too many partitions increase overhead
2. **Hot partitions** - Uneven key distribution causes load imbalance
3. **Reassignment storms** - Too many concurrent reassignments
4. **Leader imbalance** - Uneven leader distribution causes hotspots

---

## Further Reading

- [Kafka Partition Documentation](https://kafka.apache.org/documentation/#configuration)
- [Kafka Partition Reassignment](https://kafka.apache.org/documentation/#basic_ops_cluster_expansion)
- [Kafka Leader Election](https://kafka.apache.org/documentation/#basic_ops_leader_election)
