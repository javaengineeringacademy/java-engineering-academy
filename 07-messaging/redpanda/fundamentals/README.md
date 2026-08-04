# Redpanda Fundamentals

## Redpanda Architecture, Kafka Compatibility, and Thread-per-Core Model

---

## Table of Contents

- [Overview](#overview)
- [Redpanda Architecture](#redpanda-architecture)
- [Kafka Compatibility](#kafka-compatibility)
- [Thread-per-Core Model](#thread-per-core-model)
- [Getting Started](#getting-started)
- [Best Practices](#best-practices)

---

## Overview

Redpanda is a Kafka-compatible streaming platform written in C++ with a thread-per-core architecture. It provides better performance, lower latency, and simpler operations than Kafka.

### Key Features

- **Kafka Compatible**: 100% Kafka API compatible
- **No JVM**: Written in C++, no garbage collection
- **Thread-per-Core**: Efficient CPU utilization
- **No ZooKeeper**: Raft consensus for metadata
- **Simple Operations**: Single binary, no dependencies

### When to Use Redpanda

- Migrating from Kafka
- Need better performance
- Lower latency requirements
- Simpler operations

---

## Redpanda Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Redpanda Cluster                           │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  Redpanda 1  │  │  Redpanda 2  │  │  Redpanda 3  │      │
│  │   (Leader)   │  │  (Follower)  │  │  (Follower)  │      │
│  │              │  │              │  │              │      │
│  │ ┌──────────┐ │  │ ┌──────────┐ │  │ ┌──────────┐ │      │
│  │ │  Raft    │ │  │ │  Raft    │ │  │ │  Raft    │ │      │
│  │ │  Group   │◀─▶│ │ │  Group   │◀─▶│ │ │  Group   │ │      │
│  │ └──────────┘ │  │ └──────────┘ │  │ └──────────┘ │      │
│  │              │  │              │  │              │      │
│  │ ┌──────────┐ │  │ ┌──────────┐ │  │ ┌──────────┐ │      │
│  │ │  Kafka   │ │  │ │  Kafka   │ │  │ │  Kafka   │ │      │
│  │ │  API     │ │  │ │  API     │ │  │ │  API     │ │      │
│  │ └──────────┘ │  │ └──────────┘ │  │ └──────────┘ │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Component Roles

| Component | Responsibility |
|-----------|---------------|
| Redpanda | Handles produce/consume requests |
| Raft | Metadata consensus |
| Kafka API | Kafka-compatible interface |

---

## Kafka Compatibility

### API Compatibility

```
Kafka APIs Supported:
├── Produce API (v0-v9)
├── Fetch API (v0-v12)
├── Metadata API
├── Offsets API
├── Consumer Group API
├── Admin API
└── Transactions API

Client Compatibility:
├── Java Client (KafkaProducer, KafkaConsumer)
├── librdkafka
├── confluent-kafka-go
├── confluent-kafka-python
└── All Kafka-compatible clients
```

### Configuration Compatibility

```properties
# Redpanda configuration (redpanda.yaml)
redpanda:
  kafka_api:
    - address: 0.0.0.0
      port: 9092
  rpc_server:
    address: 0.0.0.0
    port: 33145
  seed_servers:
    - address: redpanda-1
      port: 33145
    - address: redpanda-2
      port: 33145
    - address: redpanda-3
      port: 33145
```

### Client Usage

```java
// Java client (same as Kafka)
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

Producer<String, String> producer = new KafkaProducer<>(props);
producer.send(new ProducerRecord<>("topic", "key", "value"));

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
consumer.subscribe(Arrays.asList("topic"));
```

---

## Thread-per-Core Model

### Threading Model

```
Thread-per-Core Architecture:
┌─────────────────────────────────────────────────────────────┐
│                    CPU Cores                                  │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Core 0: ┌──────────────────────────────────────────────┐   │
│          │ Redpanda Thread 0                             │   │
│          │ ├─ Request handling                           │   │
│          │ ├─ Message processing                         │   │
│          │ └─ Network I/O                                │   │
│          └──────────────────────────────────────────────┘   │
│                                                              │
│  Core 1: ┌──────────────────────────────────────────────┐   │
│          │ Redpanda Thread 1                             │   │
│          │ ├─ Request handling                           │   │
│          │ ├─ Message processing                         │   │
│          │ └─ Network I/O                                │   │
│          └──────────────────────────────────────────────┘   │
│                                                              │
│  Core 2: ┌──────────────────────────────────────────────┐   │
│          │ Redpanda Thread 2                             │   │
│          │ ├─ Request handling                           │   │
│          │ ├─ Message processing                         │   │
│          │ └─ Network I/O                                │   │
│          └──────────────────────────────────────────────┘   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Benefits

| Benefit | Description |
|---------|-------------|
| No Context Switching | Each thread runs on dedicated core |
| No GC Pauses | C++ memory management |
| Better Cache Utilization | Data stays in CPU cache |
| Lower Latency | No JVM overhead |

---

## Getting Started

### Docker Setup

```yaml
# docker-compose.yml
version: '3'
services:
  redpanda:
    image: vectorized/redpanda:latest
    command: redpanda start
    ports:
      - "9092:9092"
      - "9644:9644"
    environment:
      REDPANDA_REDPODANDA_MEMORY: 1G
      REDPANDA_REDPODANDA_CPUS: 1
```

### Basic Producer

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

Producer<String, String> producer = new KafkaProducer<>(props);
producer.send(new ProducerRecord<>("topic", "key", "value")).get();
```

### Basic Consumer

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("group.id", "my-group");
props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
consumer.subscribe(Arrays.asList("topic"));

while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        System.out.println(record.key() + ": " + record.value());
    }
}
```

---

## Best Practices

### Deployment

1. **Use odd number of nodes** - For quorum consensus
2. **Dedicate resources** - Separate Redpanda from other services
3. **Use fast storage** - NVMe SSDs recommended
4. **Monitor metrics** - Use Prometheus/Grafana

### Performance

1. **Tune batch size** - Balance throughput vs latency
2. **Use compression** - Reduce network overhead
3. **Tune partition count** - Match to CPU cores
4. **Monitor CPU usage** - Ensure efficient utilization

### Operations

1. **Use rpk CLI** - Redpanda management tool
2. **Monitor health** - Track cluster status
3. **Plan capacity** - Scale as needed
4. **Test failover** - Verify recovery

---

## Further Reading

- [Redpanda Documentation](https://docs.redpanda.com/)
- [Redpanda Architecture](https://docs.redpanda.com/docs/concepts/architecture/)
- [Kafka Compatibility](https://docs.redpanda.com/docs/faq/)
