# Redpanda Compatibility

## Kafka API Compatibility, Migration, and Client Support

---

## Table of Contents

- [Overview](#overview)
- [API Compatibility](#api-compatibility)
- [Client Support](#client-support)
- [Migration from Kafka](#migration-from-kafka)
- [Configuration Mapping](#configuration-mapping)
- [Limitations](#limitations)
- [Best Practices](#best-practices)

---

## Overview

Redpanda is 100% Kafka API compatible, allowing seamless migration from Kafka. This guide covers compatibility details, migration strategies, and client support.

### Compatibility Matrix

| Feature | Compatibility |
|---------|--------------|
| Kafka APIs | 100% |
| Java Client | Full |
| librdkafka | Full |
| Confluent Platform | Full |
| Schema Registry | Full |
| Kafka Connect | Full |
| Kafka Streams | Full |

---

## API Compatibility

### Supported APIs

```
Kafka APIs:
├── Produce API (v0-v9)
├── Fetch API (v0-v12)
├── Metadata API
├── Offsets API
├── Consumer Group Protocol
├── Admin API
├── Transactions API
└── Delegation Tokens

Versions Supported:
├── Kafka 0.8.x to 3.x
├── All protocol versions
└── All message formats
```

### API Behavior

```java
// Same code works with both Kafka and Redpanda
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

// Works with Kafka
Producer<String, String> kafkaProducer = new KafkaProducer<>(props);
kafkaProducer.send(new ProducerRecord<>("topic", "key", "value"));

// Works with Redpanda (same code)
Producer<String, String> redpandaProducer = new KafkaProducer<>(props);
redpandaProducer.send(new ProducerRecord<>("topic", "key", "value"));
```

---

## Client Support

### Java Client

```java
// Maven dependency (same as Kafka)
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.5.1</version>
</dependency>

// Code (same as Kafka)
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

Producer<String, String> producer = new KafkaProducer<>(props);
```

### librdkafka

```c
// C/C++ client (same as Kafka)
#include <librdkafka/rdkafka.h>

rd_kafka_conf_t *conf = rd_kafka_conf_new();
rd_kafka_conf_set(conf, "bootstrap.servers", "localhost:9092", NULL, 0);
```

### Confluent Kafka Go

```go
// Go client (same as Kafka)
import "github.com/confluentinc/confluent-kafka-go/kafka"

producer, _ := kafka.NewProducer(&kafka.ConfigMap{
    "bootstrap.servers": "localhost:9092",
})
```

### Confluent Kafka Python

```python
# Python client (same as Kafka)
from confluent_kafka import Producer

producer = Producer({'bootstrap.servers': 'localhost:9092'})
producer.produce('topic', key='key', value='value')
```

---

## Migration from Kafka

### Migration Strategy

```
Migration Steps:
1. Deploy Redpanda cluster
2. Mirror topics from Kafka to Redpanda
3. Redirect producers to Redpanda
4. Redirect consumers to Redpanda
5. Decommission Kafka cluster

Tools:
├── MirrorMaker 2
├── Redpanda Connect
└── rpk topic mirror
```

### MirrorMaker 2 Setup

```properties
# mm2.properties
clusters = kafka, redpanda

kafka.bootstrap.servers = kafka:9092
redpanda.bootstrap.servers = redpanda:9092

kafka->redpanda.enabled = true
kafka->redpanda.topics = .*

replication.policy.class = org.apache.kafka.connect.mirror.IdentityReplicationPolicy
```

### rpk Mirror

```bash
# Mirror topic from Kafka to Redpanda
rpk topic mirror \
  --from kafka:9092 \
  --to redpanda:9092 \
  --topic orders

# Mirror all topics
rpk topic mirror \
  --from kafka:9092 \
  --to redpanda:9092 \
  --all-topics
```

### Migration Checklist

```
Pre-Migration:
├── Deploy Redpanda cluster
├── Verify Kafka compatibility
├── Set up monitoring
└── Plan cutover window

During Migration:
├── Start topic mirroring
├── Verify data consistency
├── Redirect producers
├── Redirect consumers
└── Monitor metrics

Post-Migration:
├── Verify all data migrated
├── Monitor performance
├── Decommission Kafka
└── Update documentation
```

---

## Configuration Mapping

### Kafka to Redpanda Config

```properties
# Kafka (server.properties)
broker.id=1
listeners=PLAINTEXT://localhost:9092
log.dirs=/var/lib/kafka/data
num.partitions=3
default.replication.factor=3
min.insync.replicas=2

# Redpanda (redpanda.yaml)
redpanda:
  node_id: 1
  kafka_api:
    - address: 0.0.0.0
      port: 9092
  data_directory: /var/lib/redpanda/data
  default_topic_partitions: 3
  default_topic_replication: 3
```

### Common Settings

| Kafka Setting | Redpanda Setting |
|---------------|------------------|
| `broker.id` | `redpanda.node_id` |
| `listeners` | `redpanda.kafka_api` |
| `log.dirs` | `redpanda.data_directory` |
| `num.partitions` | `redpanda.default_topic_partitions` |
| `default.replication.factor` | `redpanda.default_topic_replication` |
| `min.insync.replicas` | `redpanda.min.insync.replicas` |

---

## Limitations

### Not Supported

```
Not Supported:
├── Kafka Connect (JVM-based)
├── Kafka Streams (JVM-based)
├── Some Kafka admin operations
├── Some protocol versions
└── Some configuration options

Note:
- Kafka Connect can be used with Redpanda via Kafka Connect
- Kafka Streams works with Redpanda via Kafka client
```

### Known Differences

| Feature | Kafka | Redpanda |
|---------|-------|----------|
| Consensus | ZooKeeper | Raft |
| Language | Java | C++ |
| Threading | Thread pool | Thread-per-core |
| GC | Yes | No |
| Configuration | Properties | YAML |

---

## Best Practices

### Migration

1. **Test compatibility** - Verify all features work
2. **Use MirrorMaker 2** - For reliable mirroring
3. **Plan cutover** - Minimize downtime
4. **Monitor metrics** - Track performance

### Configuration

1. **Map settings** - Translate Kafka configs
2. **Tune for Redpanda** - Optimize for thread-per-core
3. **Use appropriate partition count** - Match to CPU cores
4. **Monitor resources** - CPU, memory, disk

### Operations

1. **Use rpk CLI** - Redpanda management
2. **Monitor health** - Track cluster status
3. **Plan capacity** - Scale as needed
4. **Test failover** - Verify recovery

---

## Further Reading

- [Redpanda Kafka Compatibility](https://docs.redpanda.com/docs/faq/)
- [Redpanda Migration Guide](https://docs.redpanda.com/docs/migration/)
- [Redpanda Configuration](https://docs.redpanda.com/docs/reference/rpk/)
