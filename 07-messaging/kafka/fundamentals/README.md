# Kafka Fundamentals

## Apache Kafka Architecture, Brokers, Topics, and Partitions

---

## Table of Contents

- [Overview](#overview)
- [Core Concepts](#core-concepts)
- [Kafka Architecture](#kafka-architecture)
- [Brokers](#brokers)
- [Topics](#topics)
- [Partitions](#partitions)
- [Replication](#replication)
- [Consumer Groups](#consumer-groups)
- [Getting Started](#getting-started)
- [Best Practices](#best-practices)

---

## Overview

Apache Kafka is a distributed event streaming platform capable of handling trillions of events per day. Originally developed at LinkedIn, it has become the de facto standard for building real-time data pipelines and streaming applications.

### Key Characteristics

- **High Throughput**: Millions of messages per second
- **Low Latency**: Millisecond-level message delivery
- **Durability**: Persistent, replicated commit log
- **Scalability**: Horizontal scaling across machines
- **Fault Tolerance**: Automatic failover and recovery

---

## Core Concepts

### Message

A message (also called a record or event) is the fundamental unit of data in Kafka.

```
┌─────────────────────────────────────────┐
│              Kafka Message              │
├─────────────────────────────────────────┤
│  Key:       Optional binary key         │
│  Value:     Message payload             │
│  Headers:   Key-value metadata          │
│  Timestamp: When the message was created│
│  Offset:    Unique position in partition│
│  Partition: Target partition number     │
└─────────────────────────────────────────┘
```

### Topic

A topic is a logical channel to which messages are published. Topics are similar to tables in a database.

### Partition

A topic is divided into partitions, which are ordered, immutable sequences of messages. Partitions enable parallelism and scalability.

### Offset

An offset is a unique identifier for each message within a partition. Offsets are sequential and never reused.

### Broker

A broker is a Kafka server that stores data and serves clients (producers and consumers).

### Cluster

A cluster is a group of brokers working together.

---

## Kafka Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Kafka Cluster                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Broker 1  │  │   Broker 2  │  │   Broker 3  │        │
│  │   (Leader)  │  │  (Follower) │  │  (Follower) │        │
│  ├─────────────┤  ├─────────────┤  ├─────────────┤        │
│  │ Topic A     │  │ Topic A     │  │ Topic A     │        │
│  │  P0 (L)     │  │  P0 (F)     │  │  P0 (F)     │        │
│  │  P1 (F)     │  │  P1 (L)     │  │  P1 (F)     │        │
│  │  P2 (F)     │  │  P2 (F)     │  │  P2 (L)     │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│                                                             │
│  L = Leader, F = Follower (Replica)                         │
└─────────────────────────────────────────────────────────────┘
         │                    │                    │
         ▼                    ▼                    ▼
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│  Producer 1 │       │  Producer 2 │       │  Consumer 1 │
└─────────────┘       └─────────────┘       └─────────────┘
```

### Request Flow

```
1. Producer connects to any broker (bootstrap server)
2. Broker returns metadata (leader for each partition)
3. Producer sends message to partition leader
4. Leader writes to local log
5. Followers replicate from leader
6. Once ISR acknowledges, write is committed
```

---

## Brokers

### Broker Role

Each broker in a Kafka cluster:

- Accepts connections from producers and consumers
- Stores and serves messages
- Manages partition replicas
- Handles leader election

### Broker Configuration

```properties
# Server configuration (server.properties)
broker.id=1
listeners=PLAINTEXT://localhost:9092
advertised.listeners=PLAINTEXT://localhost:9092
log.dirs=/var/lib/kafka/data
num.partitions=3
default.replication.factor=3
min.insync.replicas=2
```

### Broker Identification

Each broker has a unique integer ID (`broker.id`). This ID is used for:
- Replica assignment
- Controller election
- Log directory assignment

### Bootstrap Servers

Producers and consumers connect to any broker in the cluster (bootstrap server) to obtain cluster metadata. The client then connects directly to the appropriate broker.

```
Client → Bootstrap Server (any broker)
       ← Metadata (all brokers and partitions)
       → Direct connection to partition leader
```

### Broker Roles

| Role | Description |
|------|-------------|
| Leader | Handles all read/write operations for a partition |
| Follower | Replicates data from the leader |
| Controller | Manages cluster operations, topic creation, partition reassignment |

### Controller Election

- One broker is elected as controller
- Controller manages partition leaders
- If controller fails, new controller is elected from remaining brokers
- Controller election is handled by ZooKeeper or KRaft

---

## Topics

### Topic Structure

```
Topic: user-events
├── Partition 0
│   ├── Offset 0: {key: "user1", value: "login"}
│   ├── Offset 1: {key: "user2", value: "click"}
│   └── Offset 2: {key: "user1", value: "purchase"}
├── Partition 1
│   ├── Offset 0: {key: "user3", value: "signup"}
│   └── Offset 1: {key: "user4", value: "logout"}
└── Partition 2
    ├── Offset 0: {key: "user5", value: "login"}
    ├── Offset 1: {key: "user2", value: "logout"}
    └── Offset 2: {key: "user3", value: "click"}
```

### Topic Configuration

```properties
# Topic-level configuration
num.partitions=3
retention.ms=604800000  # 7 days
retention.bytes=-1     # No size limit
cleanup.policy=delete  # or compact
compression.type=producer
max.message.bytes=1048576  # 1MB
```

### Topic Naming Conventions

```
# Hierarchical naming
<domain>.<entity>.<event-type>

# Examples
user.events.login
order.events.created
payment.events.completed
system.logs.audit
```

### Topic Operations

```bash
# Create topic
kafka-topics.sh --create \
  --topic user-events \
  --bootstrap-server localhost:9092 \
  --partitions 3 \
  --replication-factor 3

# List topics
kafka-topics.sh --list --bootstrap-server localhost:9092

# Describe topic
kafka-topics.sh --describe \
  --topic user-events \
  --bootstrap-server localhost:9092

# Delete topic
kafka-topics.sh --delete \
  --topic user-events \
  --bootstrap-server localhost:9092

# Modify topic
kafka-topics.sh --alter \
  --topic user-events \
  --partitions 6 \
  --bootstrap-server localhost:9092
```

### Topic Types

| Type | Description |
|------|-------------|
| Regular | Standard topic with configurable retention |
| Compact | Retains last value per key |
| Internal | System topics (e.g., __consumer_offsets) |

---

## Partitions

### Partition Structure

A partition is an ordered, immutable sequence of messages stored as a set of segment files.

```
Partition Directory
├── 00000000000000000000.index
├── 00000000000000000000.log
├── 00000000000000000000.timeindex
├── 00000000000000000100.index
├── 00000000000000000100.log
└── 00000000000000000100.timeindex
```

### Partition Replication

```
Partition 0 across 3 brokers:

Broker 1 (Leader)    Broker 2 (Follower)  Broker 3 (Follower)
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Offset 0     │────▶│ Offset 0     │     │ Offset 0     │
│ Offset 1     │────▶│ Offset 1     │     │ Offset 1     │
│ Offset 2     │────▶│ Offset 2     │     │ Offset 2     │
│ Offset 3     │────▶│ Offset 3     │     │ Offset 3     │
└──────────────┘     └──────────────┘     └──────────────┘
     ▲                                          │
     └──────────────────────────────────────────┘
              Replication (async or sync)
```

### Partition Assignment

```
Topic: orders (3 partitions, replication factor 3)

Partition 0: Broker 1 (Leader), Broker 2, Broker 3
Partition 1: Broker 2 (Leader), Broker 3, Broker 1
Partition 2: Broker 3 (Leader), Broker 1, Broker 2

Leader distribution ensures load balancing across brokers.
```

### Partition Count Considerations

| Factor | Consideration |
|--------|---------------|
| Throughput | More partitions = higher throughput |
| Consumer Parallelism | Max parallelism = partition count |
| File Handles | Each partition uses file handles |
| Memory | Each partition needs memory for buffers |
| Leader Election | More partitions = longer election |

### Partition Reassignment

```bash
# Reassign partitions
kafka-reassign-partitions.sh --bootstrap-server localhost:9092 \
  --reassignment-json-file reassignment.json \
  --execute

# Reassignment JSON
{
  "partitions": [
    {"topic": "orders", "partition": 0, "replicas": [2, 3, 1]}
  ]
}
```

---

## Replication

### Replication Factors

The replication factor determines how many copies of each partition exist.

```
Replication Factor = 3

Broker 1: Partition 0 (Leader), Partition 1 (Follower), Partition 2 (Follower)
Broker 2: Partition 0 (Follower), Partition 1 (Leader), Partition 2 (Follower)
Broker 3: Partition 0 (Follower), Partition 1 (Follower), Partition 2 (Leader)
```

### In-Sync Replicas (ISR)

ISR is the set of replicas that are fully caught up with the leader.

```
Partition 0:
Leader: Broker 1
ISR: [Broker 1, Broker 2, Broker 3]  # All in sync
Non-ISR: []                           # None

If Broker 3 falls behind:
ISR: [Broker 1, Broker 2]  # Only these are in sync
Non-ISR: [Broker 3]        # Replicating but not in sync
```

### min.insync.replicas

```properties
min.insync.replicas=2
```

- Minimum replicas that must acknowledge a write
- If ISR falls below this, writes are rejected
- Protects against data loss

### Unclean Leader Election

```properties
unclean.leader.election.enable=false
```

- Allow non-ISR replica to become leader?
- `false`: Safer, but may cause availability issues
- `true`: More available, but risks data loss

### Replication Flow

```
1. Producer sends message to partition leader
2. Leader writes to local log
3. Leader sends message to followers
4. Followers write to their local logs
5. Followers send acknowledgment to leader
6. Once min.insync.replicas acknowledge, leader commits
7. Leader sends acknowledgment to producer
```

---

## Consumer Groups

### Consumer Group Concept

```
Consumer Group: order-processor
├── Consumer 1: Processes Partition 0
├── Consumer 2: Processes Partition 1
└── Consumer 3: Processes Partition 2

Topic: orders (3 partitions)
├── Partition 0 → Consumer 1
├── Partition 1 → Consumer 2
└── Partition 2 → Consumer 3
```

### Consumer Group Behavior

| Scenario | Behavior |
|----------|----------|
| Consumers < Partitions | Some consumers process multiple partitions |
| Consumers = Partitions | Each consumer processes one partition |
| Consumers > Partitions | Some consumers are idle |
| Consumer fails | Partitions reassigned to remaining consumers |

### Consumer Offset Management

```
Consumer Group: order-processor
Topic: orders

Partition 0: Committed Offset = 152
Partition 1: Committed Offset = 234
Partition 2: Committed Offset = 189

Consumer processes messages until offset 152,
then commits offset 153 for partition 0.
```

### Offset Storage

```
__consumer_offsets topic (internal)
├── Key: group_id + topic + partition
└── Value: committed offset + metadata
```

### Consumer Group Operations

```bash
# List consumer groups
kafka-consumer-groups.sh --list --bootstrap-server localhost:9092

# Describe consumer group
kafka-consumer-groups.sh --describe \
  --group order-processor \
  --bootstrap-server localhost:9092

# Reset offsets
kafka-consumer-groups.sh --reset-offsets \
  --group order-processor \
  --topic orders \
  --to-earliest \
  --execute \
  --bootstrap-server localhost:9092
```

---

## Getting Started

### Prerequisites

- Java 8+ or Docker
- Kafka binaries or Docker images

### Quick Start with Docker

```yaml
# docker-compose.yml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: confluentinc/cp-kafka:7.4.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

### Basic Producer

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

Producer<String, String> producer = new KafkaProducer<>(props);

ProducerRecord<String, String> record = 
    new ProducerRecord<>("user-events", "user123", "login successful");

producer.send(record, (metadata, exception) -> {
    if (exception == null) {
        System.out.printf("Sent to partition %d, offset %d%n", 
            metadata.partition(), metadata.offset());
    }
});

producer.close();
```

### Basic Consumer

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("group.id", "user-event-processor");
props.put("enable.auto.commit", "false");
props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
consumer.subscribe(Arrays.asList("user-events"));

while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        System.out.printf("Key: %s, Value: %s, Partition: %d, Offset: %d%n",
            record.key(), record.value(), record.partition(), record.offset());
    }
    consumer.commitSync();
}
```

---

## Best Practices

### Topic Design

- Use descriptive topic names
- Plan partition count carefully (increasing is easier than decreasing)
- Use retention policies appropriate for your use case
- Consider compaction for state topics

### Broker Configuration

- Set `min.insync.replicas` to at least 2
- Disable unclean leader election in production
- Use dedicated disk for log directories
- Monitor disk usage and set alerts

### Producer Best Practices

- Use async sending with callbacks
- Implement proper error handling
- Use idempotent producers for exactly-once
- Batch messages for better throughput

### Consumer Best Practices

- Use manual offset commit
- Handle rebalances gracefully
- Process messages idempotently
- Monitor consumer lag

### Monitoring

- Track broker metrics (request rate, ISR shrink rate)
- Monitor consumer lag per partition
- Set up alerts for broker availability
- Track partition leader distribution

---

## Further Reading

- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [Kafka Configuration Reference](https://kafka.apache.org/documentation/#configuration)
- [Kafka Protocol Guide](https://kafka.apache.org/protocol)
