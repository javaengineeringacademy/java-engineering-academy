# Kafka Topics

## Topic Design, Partitions, Replication, and Management

---

## Table of Contents

- [Overview](#overview)
- [Topic Structure](#topic-structure)
- [Topic Configuration](#topic-configuration)
- [Partition Design](#partition-design)
- [Replication Strategy](#replication-strategy)
- [Topic Operations](#topic-operations)
- [Topic Naming Conventions](#topic-naming-conventions)
- [Topic Cleanup Policies](#topic-cleanup-policies)
- [Best Practices](#best-practices)

---

## Overview

Topics are the fundamental organizational unit in Kafka. They represent logical channels to which producers write and consumers read. Proper topic design is crucial for system performance, scalability, and maintainability.

### Key Concepts

- **Topic**: Logical category or feed name
- **Partition**: Ordered, immutable sequence of messages
- **Replica**: Copy of a partition for fault tolerance
- **Offset**: Unique identifier for each message in a partition

---

## Topic Structure

### Topic Hierarchy

```
Topic: user-events
│
├── Partition 0
│   ├── Segment 00000000000000000000
│   │   ├── Offset 0
│   │   ├── Offset 1
│   │   └── Offset 2
│   └── Segment 00000000000000000003
│       ├── Offset 3
│       └── Offset 4
│
├── Partition 1
│   ├── Segment 00000000000000000000
│   │   ├── Offset 0
│   │   └── Offset 1
│   └── Segment 00000000000000000002
│       ├── Offset 2
│       └── Offset 3
│
└── Partition 2
    ├── Segment 00000000000000000000
    │   └── Offset 0
    └── Segment 00000000000000000001
        ├── Offset 1
        └── Offset 2
```

### Partition Structure

```
Partition Directory: /var/lib/kafka/data/user-events-0/
│
├── 00000000000000000000.index      # Offset index
├── 00000000000000000000.log        # Message log
├── 00000000000000000000.timeindex  # Timestamp index
├── 00000000000000000100.index
├── 00000000000000000100.log
├── 00000000000000000100.timeindex
└── leader-epoch-checkpoint
```

---

## Topic Configuration

### Essential Configuration

```bash
# Create topic with configuration
kafka-topics.sh --create \
  --topic user-events \
  --bootstrap-server localhost:9092 \
  --partitions 6 \
  --replication-factor 3 \
  --config retention.ms=604800000 \
  --config retention.bytes=-1 \
  --config cleanup.policy=delete \
  --config compression.type=producer
```

### Configuration Reference

#### Retention Settings

| Property | Default | Description |
|----------|---------|-------------|
| `retention.ms` | 604800000 | Retention time in milliseconds (7 days) |
| `retention.bytes` | -1 | Maximum topic size (-1 = unlimited) |
| `retention.bytes.per.partition` | -1 | Maximum partition size |

#### Cleanup Settings

| Property | Default | Description |
|----------|---------|-------------|
| `cleanup.policy` | delete | Cleanup strategy: delete, compact, delete,compact |
| `min.cleanable.dirty.ratio` | 0.5 | Minimum ratio of dirty log for compaction |
| `delete.retention.ms` | 86400000 | Time to retain delete tombstone markers |

#### Performance Settings

| Property | Default | Description |
|----------|---------|-------------|
| `compression.type` | producer | Compression: none, gzip, snappy, lz4, zstd |
| `segment.bytes` | 1073741824 | Segment file size (1GB) |
| `segment.ms` | 604800000 | Segment time (7 days) |
| `index.interval.bytes` | 4096 | Index interval |
| `flush.messages` | - | Flush after N messages |
| `flush.ms` | - | Flush after N milliseconds |

#### Message Settings

| Property | Default | Description |
|----------|---------|-------------|
| `max.message.bytes` | 1000012 | Maximum message size |
| `message.format.version` | 2.8 | Message format version |
| `message.timestamp.type` | CreateTime | Timestamp type: CreateTime, LogAppendTime |

#### Compaction Settings

| Property | Default | Description |
|----------|---------|-------------|
| `min.cleanable.dirty.ratio` | 0.5 | Minimum dirty log ratio for compaction |
| `cleaner.enable` | true | Enable log cleaner |
| `cleaner.threads` | 1 | Number of cleaner threads |
| `cleaner.dedupe.buffer.size` | 134217728 | Deduplication buffer size |

---

## Partition Design

### Partition Count Strategy

```
Determining Partition Count:

Throughput Requirement:
  Target: 10,000 messages/second
  Single partition throughput: ~1,000 messages/second
  Required partitions: 10

Consumer Parallelism:
  Maximum consumers: 10
  Required partitions: 10

Future Growth:
  Add 20-30% buffer
  Recommended: 12-15 partitions
```

### Partition Distribution

```
Topic: orders (6 partitions, 3 brokers)

Broker 1: Partition 0, 3
Broker 2: Partition 1, 4
Broker 3: Partition 2, 5

Replication:
Partition 0: Leader=Broker1, Follower=Broker2, Follower=Broker3
Partition 1: Leader=Broker2, Follower=Broker3, Follower=Broker1
Partition 2: Leader=Broker3, Follower=Broker1, Follower=Broker2
Partition 3: Leader=Broker1, Follower=Broker3, Follower=Broker2
Partition 4: Leader=Broker2, Follower=Broker1, Follower=Broker3
Partition 5: Leader=Broker3, Follower=Broker2, Follower=Broker1
```

### Partition Count Guidelines

| Factor | Recommendation |
|--------|---------------|
| Throughput | 1 partition per 10MB/s of throughput |
| Consumers | 1 partition per consumer instance |
| Broker count | Partitions should be multiple of broker count |
| Future growth | Add 20-30% buffer |
| File handles | Each partition uses ~2 file handles |

### When to Increase Partitions

- Consumer lag consistently high
- Throughput approaching partition limit
- Need more parallelism
- Single partition is a bottleneck

### When NOT to Increase Partitions

- Ordering requirements across partitions
- Key-based routing with many keys
- Compacted topics with many keys
- Already at reasonable partition count

---

## Replication Strategy

### Replication Factor

```
Replication Factor = 1 (No redundancy)
┌─────────────┐
│   Broker 1  │ ← Only copy
│ Partition 0 │
└─────────────┘

Replication Factor = 2 (Basic redundancy)
┌─────────────┐     ┌─────────────┐
│   Broker 1  │     │   Broker 2  │
│ Partition 0 │ ←─▶ │ Partition 0 │
│   (Leader)  │     │ (Follower)  │
└─────────────┘     └─────────────┘

Replication Factor = 3 (High availability)
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Broker 1  │     │   Broker 2  │     │   Broker 3  │
│ Partition 0 │ ←─▶ │ Partition 0 │ ←─▶ │ Partition 0 │
│   (Leader)  │     │ (Follower)  │     │ (Follower)  │
└─────────────┘     └─────────────┘     └─────────────┘
```

### ISR (In-Sync Replicas)

```
Partition 0:
Leader: Broker 1
ISR: [Broker 1, Broker 2, Broker 3]  ← All in sync

If Broker 3 falls behind:
ISR: [Broker 1, Broker 2]  ← Only these are in sync

min.insync.replicas = 2:
- Writes require 2 ISR acknowledgments
- If ISR < 2, writes are rejected
```

### Replication Configuration

```properties
# Broker configuration
default.replication.factor=3
min.insync.replicas=2
unclean.leader.election.enable=false
replica.lag.time.max.ms=10000
```

---

## Topic Operations

### Create Topic

```bash
# Basic create
kafka-topics.sh --create \
  --topic user-events \
  --bootstrap-server localhost:9092 \
  --partitions 6 \
  --replication-factor 3

# With configuration
kafka-topics.sh --create \
  --topic user-events \
  --bootstrap-server localhost:9092 \
  --partitions 6 \
  --replication-factor 3 \
  --config retention.ms=259200000 \
  --config cleanup.policy=compact
```

### List Topics

```bash
# List all topics
kafka-topics.sh --list --bootstrap-server localhost:9092

# List internal topics
kafka-topics.sh --list --bootstrap-server localhost:9092 \
  --include-cluster-metadata-internal

# Filter topics
kafka-topics.sh --list --bootstrap-server localhost:9092 | grep user
```

### Describe Topic

```bash
# Describe topic
kafka-topics.sh --describe \
  --topic user-events \
  --bootstrap-server localhost:9092

# Output:
# Topic: user-events  TopicId: abc123  PartitionCount: 6  ReplicationFactor: 3
#   Partition: 0  Leader: 1  Replicas: 1,2,3  Isr: 1,2,3
#   Partition: 1  Leader: 2  Replicas: 2,3,1  Isr: 2,3,1
#   Partition: 2  Leader: 3  Replicas: 3,1,2  Isr: 3,1,2
#   Partition: 3  Leader: 1  Replicas: 1,3,2  Isr: 1,3,2
#   Partition: 4  Leader: 2  Replicas: 2,1,3  Isr: 2,1,3
#   Partition: 5  Leader: 3  Replicas: 3,2,1  Isr: 3,2,1

# Describe with config
kafka-topics.sh --describe \
  --topic user-events \
  --bootstrap-server localhost:9092 \
  --include-configs
```

### Alter Topic

```bash
# Alter topic configuration
kafka-topics.sh --alter \
  --topic user-events \
  --bootstrap-server localhost:9092 \
  --config retention.ms=86400000

# Note: Cannot change partition count for compacted topics
```

### Delete Topic

```bash
# Delete topic
kafka-topics.sh --delete \
  --topic user-events \
  --bootstrap-server localhost:9092

# Check if topic is marked for deletion
kafka-topics.sh --list --bootstrap-server localhost:9092 | grep user-events
```

### Reassign Partitions

```bash
# Create reassignment JSON
cat > reassignment.json << EOF
{
  "partitions": [
    {"topic": "user-events", "partition": 0, "replicas": [2, 3, 1]},
    {"topic": "user-events", "partition": 1, "replicas": [3, 1, 2]}
  ]
}
EOF

# Execute reassignment
kafka-reassign-partitions.sh --bootstrap-server localhost:9092 \
  --reassignment-json-file reassignment.json \
  --execute

# Verify reassignment
kafka-reassign-partitions.sh --bootstrap-server localhost:9092 \
  --reassignment-json-file reassignment.json \
  --verify
```

---

## Topic Naming Conventions

### Naming Patterns

```
# Hierarchical naming
<domain>.<entity>.<event-type>

# Examples
user.events.login
user.events.logout
order.events.created
order.events.shipped
payment.events.completed
payment.events.failed
```

### Naming Best Practices

| Practice | Example |
|----------|---------|
| Use lowercase | `user.events` |
| Use dots as separators | `user.events.login` |
| Keep consistent | Don't mix `user.events` and `user-events` |
| Be descriptive | `order.events.created` not `orders` |
| Avoid special characters | Use alphanumeric and dots only |

### Topic Categories

```
Events:
├── user.events.*
├── order.events.*
└── payment.events.*

Commands:
├── user.commands.*
├── order.commands.*
└── payment.commands.*

State:
├── user.state.*
├── order.state.*
└── payment.state.*

Audit:
├── audit.user.*
├── audit.order.*
└── audit.payment.*
```

---

## Topic Cleanup Policies

### Delete Policy

```properties
cleanup.policy=delete
retention.ms=604800000  # 7 days
retention.bytes=-1      # No size limit
```

Messages deleted after retention period. Default policy.

### Compact Policy

```properties
cleanup.policy=compact
min.cleanable.dirty.ratio=0.5
delete.retention.ms=86400000
```

Retains last value per key. Useful for state topics.

```
Before compaction:
Offset 0: key=a, value=1
Offset 1: key=b, value=2
Offset 2: key=a, value=3
Offset 3: key=c, value=4
Offset 4: key=b, value=5

After compaction:
Offset 0: key=a, value=3
Offset 2: key=c, value=4
Offset 4: key=b, value=5

Key "a" retained value=3 (latest)
Key "b" retained value=5 (latest)
Key "c" retained value=4 (latest)
```

### Delete+Compact Policy

```properties
cleanup.policy=delete,compact
retention.ms=604800000
min.cleanable.dirty.ratio=0.5
```

Combines both strategies. Compact first, then delete old records.

---

## Best Practices

### Topic Design

1. **Plan partition count carefully** - Increasing is easy, decreasing is hard
2. **Use descriptive names** - Follow naming conventions
3. **Consider future growth** - Add buffer to partition count
4. **Use appropriate cleanup policy** - Delete for events, compact for state

### Partition Strategy

1. **Balance load across partitions** - Use key-based routing for even distribution
2. **Avoid hot partitions** - Design keys for even distribution
3. **Consider ordering requirements** - Partitions provide ordering only within partition
4. **Plan for replication** - Set appropriate replication factor

### Configuration

1. **Set retention appropriately** - Balance storage cost with data availability
2. **Configure compression** - Use producer or topic-level compression
3. **Set max message size** - Consider producer and consumer limits
4. **Enable compaction for state topics** - Use compact cleanup policy

### Operations

1. **Monitor partition distribution** - Ensure balanced leadership
2. **Track partition count** - Don't over-partition
3. **Plan for growth** - Add partitions before hitting limits
4. **Test changes in staging** - Verify configuration changes

### Monitoring

1. **Track topic metrics** - Bytes in/out, message rate
2. **Monitor partition health** - ISR size, leader distribution
3. **Alert on anomalies** - ISR shrink, under-replicated partitions
4. **Review retention settings** - Ensure adequate data retention

---

## Further Reading

- [Kafka Topic Configuration](https://kafka.apache.org/documentation/#topicconfigs)
- [Kafka Topic Management](https://kafka.apache.org/documentation/#operations)
- [Kafka Compaction](https://kafka.apache.org/documentation/#compaction)
