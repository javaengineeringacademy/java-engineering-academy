# Kafka Producers

## Producer Configuration, Partitioning Strategies, and Acknowledgments

---

## Table of Contents

- [Overview](#overview)
- [Producer Architecture](#producer-architecture)
- [Configuration](#configuration)
- [Partitioning Strategies](#partitioning-strategies)
- [Acknowledgments](#acknowledgments)
- [Idempotent Producers](#idempotent-producers)
- [Transactional Producers](#transactional-producers)
- [Performance Tuning](#performance-tuning)
- [Error Handling](#error-handling)
- [Best Practices](#best-practices)

---

## Overview

The Kafka producer is responsible for publishing messages to Kafka topics. Producers handle partitioning, batching, compression, and retries to ensure reliable message delivery.

### Key Features

- **Partitioning**: Deterministic routing to partitions
- **Batching**: Accumulates messages for efficient sending
- **Compression**: Reduces network overhead
- **Retries**: Automatic retry on transient failures
- **Idempotency**: Guarantees exactly-once delivery per partition
- **Transactions**: Atomic writes across multiple partitions

---

## Producer Architecture

### Producer Flow

```
┌─────────────────────────────────────────────────────────────┐
│                     Kafka Producer                           │
│                                                              │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐              │
│  │  Send()  │───▶│  Record  │───▶│ Partition│              │
│  │          │    │  Accumu- │    │ Selector │              │
│  └──────────┘    │  lator   │    └────┬─────┘              │
│                  └──────────┘         │                     │
│                                       ▼                     │
│                  ┌──────────┐    ┌──────────┐              │
│                  │  Sender  │◀───│  Batch   │              │
│                  │  Thread  │    │  Builder │              │
│                  └────┬─────┘    └──────────┘              │
│                       │                                     │
│                       ▼                                     │
│              ┌────────────────┐                            │
│              │ Network I/O    │                            │
│              │ (Connection    │                            │
│              │  Pool)         │                            │
│              └────────────────┘                            │
└─────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────┐
│                    Kafka Broker Cluster                      │
└─────────────────────────────────────────────────────────────┘
```

### Component Details

| Component | Responsibility |
|-----------|---------------|
| Producer API | Application interface for sending messages |
| Record Accumulator | Buffers messages in memory |
| Partitioner | Determines target partition |
| Sender Thread | Sends batches to brokers asynchronously |
| Metadata Refresh | Maintains cluster topology |

---

## Configuration

### Essential Configuration

```java
Properties props = new Properties();

// Bootstrap servers
props.put("bootstrap.servers", "broker1:9092,broker2:9092");

// Serializers
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

// Acknowledgments
props.put("acks", "all");

// Retry configuration
props.put("retries", 3);
props.put("retry.backoff.ms", 1000);

// Batch configuration
props.put("batch.size", 16384);  // 16KB
props.put("linger.ms", 5);       // Wait 5ms to fill batch

// Compression
props.put("compression.type", "snappy");

// Idempotent producer
props.put("enable.idempotence", true);
```

### Configuration Reference

#### Core Settings

| Property | Default | Description |
|----------|---------|-------------|
| `bootstrap.servers` | - | List of broker addresses |
| `key.serializer` | - | Serializer for message keys |
| `value.serializer` | - | Serializer for message values |
| `acks` | 1 | Number of acknowledgments required |
| `buffer.memory` | 33554432 | Total memory for buffering |
| `compression.type` | none | Compression algorithm |
| `batch.size` | 16384 | Maximum batch size in bytes |
| `linger.ms` | 0 | Time to wait for more messages |
| `client.id` | - | Client identifier |

#### Reliability Settings

| Property | Default | Description |
|----------|---------|-------------|
| `retries` | 2147483647 | Number of retry attempts |
| `retry.backoff.ms` | 100 | Time between retries |
| `delivery.timeout.ms` | 120000 | Total time for delivery |
| `enable.idempotence` | false | Enable exactly-once semantics |
| `max.in.flight.requests.per.connection` | 5 | Max unacknowledged requests |

#### Performance Settings

| Property | Default | Description |
|----------|---------|-------------|
| `batch.size` | 16384 | Maximum batch size (bytes) |
| `linger.ms` | 0 | Time to wait for batch filling |
| `buffer.memory` | 33554432 | Total producer buffer memory |
| `compression.type` | none | Compression: none, gzip, snappy, lz4, zstd |
| `max.request.size` | 1048576 | Maximum request size (bytes) |

---

## Partitioning Strategies

### Default Partitioner

The default partitioner uses the following logic:

```
┌─────────────────────────────────────────────────────┐
│              Default Partitioning Logic               │
├─────────────────────────────────────────────────────┤
│                                                     │
│  1. If key is null:                                 │
│     → Round-robin across partitions                  │
│                                                     │
│  2. If key is provided:                             │
│     → hash(key) % num_partitions                     │
│     → Same key always goes to same partition         │
│                                                     │
└─────────────────────────────────────────────────────┘
```

### Round-Robin Partitioning

```java
// Key is null → round-robin
ProducerRecord<String, String> record1 = 
    new ProducerRecord<>("topic", null, "value1");
ProducerRecord<String, String> record2 = 
    new ProducerRecord<>("topic", null, "value2");
ProducerRecord<String, String> record3 = 
    new ProducerRecord<>("topic", null, "value3");

// Messages distributed evenly across partitions
// record1 → Partition 0
// record2 → Partition 1
// record3 → Partition 2
```

### Key-Based Partitioning

```java
// Key provided → hash(key) % partitions
ProducerRecord<String, String> record1 = 
    new ProducerRecord<>("topic", "user123", "value1");
ProducerRecord<String, String> record2 = 
    new ProducerRecord<>("topic", "user123", "value2");
ProducerRecord<String, String> record3 = 
    new ProducerRecord<>("topic", "user456", "value3");

// Same key always goes to same partition
// record1 → Partition X
// record2 → Partition X  (same as record1)
// record3 → Partition Y  (different key)
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

Kafka 2.4+ introduced sticky partitioning to reduce latency:

- Instead of round-robin for null keys, batches messages to same partition
- Switches partition when batch is full or linger time exceeded
- Reduces number of in-flight requests

### Partitioner Configuration

```java
// Disable default partitioner
props.put("partitioner.class", 
    "org.apache.kafka.clients.producer.internals.DefaultPartitioner");

// Custom partitioner
props.put("partitioner.class", "com.example.CustomPartitioner");
```

### Partitioning Best Practices

| Strategy | Use Case |
|----------|----------|
| Round-Robin | No ordering requirement |
| Key-Based | Need ordering per key |
| Custom | Complex routing logic |

---

## Acknowledgments

### Acks Configuration

```
┌─────────────────────────────────────────────────────────────┐
│                   Acknowledgment Modes                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  acks=0: No acknowledgment                                  │
│  ┌──────────┐    ┌──────────┐                               │
│  │ Producer │───▶│ Broker   │                               │
│  └──────────┘    └──────────┘                               │
│  (Fire and forget)                                          │
│                                                             │
│  acks=1: Leader acknowledgment                              │
│  ┌──────────┐    ┌──────────┐                               │
│  │ Producer │───▶│ Leader   │                               │
│  └──────────┘    └────┬─────┘                               │
│                       │                                     │
│                       ▼                                     │
│                  ┌──────────┐                               │
│                  │ Follower │                               │
│                  └──────────┘                               │
│                                                             │
│  acks=all: All ISR acknowledgment                           │
│  ┌──────────┐    ┌──────────┐                               │
│  │ Producer │◀──▶│ Leader   │◀──▶ Follower 1               │
│  └──────────┘    └──────────┘◀──▶ Follower 2               │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Comparison

| Acks | Durability | Throughput | Latency | Use Case |
|------|-----------|------------|---------|----------|
| 0 | None | Highest | Lowest | Non-critical events |
| 1 | Leader only | Medium | Medium | Default choice |
| all | All ISR | Lowest | Highest | Critical data |

### Acks Implementation

```java
// acks=0 (fire and forget)
props.put("acks", "0");
producer.send(record);  // No callback, no guarantee

// acks=1 (leader acknowledgment)
props.put("acks", "1");
producer.send(record, (metadata, exception) -> {
    // Leader wrote, but may not be replicated
});

// acks=all (all ISR acknowledge)
props.put("acks", "all");
producer.send(record, (metadata, exception) => {
    // Confirmed by all in-sync replicas
});
```

### min.insync.replicas

```properties
# On broker
min.insync.replicas=2
```

- Minimum replicas that must acknowledge for `acks=all`
- If ISR < min.insync.replicas, write is rejected
- Provides additional durability guarantee

---

## Idempotent Producers

### Problem: Duplicate Messages

Without idempotency, retries can cause duplicates:

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│ Producer │────▶│  Broker  │────▶│  Topic   │
└──────────┘     └──────────┘     └──────────┘
      │                                  │
      │  Send Message (offset 100)       │
      │─────────────────────────────────▶│
      │                                  │
      │  Retry (network timeout)         │
      │─────────────────────────────────▶│  Duplicate!
      │                                  │
```

### Solution: Idempotent Producer

```java
// Enable idempotent producer
props.put("enable.idempotence", true);

// Or explicitly set:
props.put("acks", "all");
props.put("retries", Integer.MAX_VALUE);
props.put("max.in.flight.requests.per.connection", 5);
```

### How It Works

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│ Producer │────▶│  Broker  │────▶│  Topic   │
└──────────┘     └──────────┘     └──────────┘
      │                                  │
      │  Send (seq=1, offset 100)        │
      │─────────────────────────────────▶│
      │                                  │
      │  Retry (seq=1)                   │
      │─────────────────────────────────▶│  Rejected! (duplicate)
      │                                  │
      │  Send (seq=2, offset 101)        │
      │─────────────────────────────────▶│
      │                                  │
```

Each producer gets a Producer ID (PID) and sequence numbers per partition. Broker tracks last committed sequence number per PID-partition combination.

### Idempotency Guarantees

- Exactly-once per partition
- Producer restart resets PID and sequence numbers
- Not transactional across partitions

---

## Transactional Producers

### Use Case: Atomic Multi-Partition Writes

```java
// Initialize transactional producer
props.put("transactional.id", "my-transactional-id");

Producer<String, String> producer = new KafkaProducer<>(props);
producer.initTransactions();

try {
    producer.beginTransaction();
    
    producer.send(new ProducerRecord<>("topic-a", "key1", "value1"));
    producer.send(new ProducerRecord<>("topic-b", "key2", "value2"));
    producer.send(new ProducerRecord<>("topic-c", "key3", "value3"));
    
    // Atomic commit
    producer.commitTransaction();
} catch (ProducerFencedException e) {
    // Another producer with same transactional.id started
    producer.close();
} catch (KafkaException e) {
    // Transaction failed
    producer.abortTransaction();
}
```

### Transaction Flow

```
┌─────────────────────────────────────────────────────────────┐
│                  Transaction Flow                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. initTransactions()                                      │
│     └── Register transactional.id with coordinator          │
│                                                             │
│  2. beginTransaction()                                      │
│     └── Start new transaction                               │
│                                                             │
│  3. send() messages                                         │
│     └── Buffer messages for transaction                     │
│                                                             │
│  4. commitTransaction() or abortTransaction()               │
│     └── Write transaction markers to partitions             │
│     └── Commit or discard all messages                      │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Transactional Configuration

```java
props.put("transactional.id", "unique-transactional-id");
props.put("enable.idempotence", true);
props.put("acks", "all");
props.put("retries", Integer.MAX_VALUE);
props.put("max.in.flight.requests.per.connection", 5);
```

### Transactional Consumer

```java
Properties props = new Properties();
props.put("group.id", "transactional-consumer");
props.put("isolation.level", "read_committed");  // Key setting

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
```

---

## Performance Tuning

### Batching

```java
// Increase batch size
props.put("batch.size", 65536);  // 64KB (default 16KB)

// Wait to fill batch
props.put("linger.ms", 5);  // Wait 5ms

// Increase buffer
props.put("buffer.memory", 67108864);  // 64MB (default 32MB)
```

### Compression

```java
// Snappy (fast compression, good ratio)
props.put("compression.type", "snappy");

// LZ4 (fastest compression)
props.put("compression.type", "lz4");

// GZIP (best compression, slower)
props.put("compression.type", "gzip");

// Zstandard (Kafka 2.1+, best balance)
props.put("compression.type", "zstd");
```

### Network Optimization

```java
// Increase max request size
props.put("max.request.size", 10485760);  // 10MB

// Increase max in-flight requests
props.put("max.in.flight.requests.per.connection", 10);

// Reduce linger for low latency
props.put("linger.ms", 1);

// Increase batch size for high throughput
props.put("batch.size", 131072);  // 128KB
```

### Compression Comparison

| Algorithm | Compression Ratio | CPU Usage | Throughput | Latency |
|-----------|-------------------|-----------|------------|---------|
| None | 1:1 | None | Highest | Lowest |
| Snappy | 2:1 | Low | High | Low |
| LZ4 | 2:1 | Low | High | Low |
| GZIP | 3:1 | High | Medium | Medium |
| ZSTD | 3:1 | Medium | Medium | Medium |

### Memory Management

```
Buffer Memory Distribution:

Total: 33554432 bytes (32MB)
├── Partition 0 buffer: 10MB
├── Partition 1 buffer: 8MB
└── Partition 2 buffer: 14MB

Each partition buffer:
├── Batch queue
└── In-flight requests
```

---

## Error Handling

### Common Errors

| Error | Cause | Solution |
|-------|-------|----------|
| `NotEnoughReplicasException` | ISR < min.insync.replicas | Increase min.insync.replicas |
| `RecordTooLargeException` | Message > max.request.size | Increase max.request.size |
| `TimeoutException` | Delivery timeout exceeded | Increase delivery.timeout.ms |
| `SerializationException` | Serializer error | Check serializer configuration |
| `NetworkException` | Network connectivity issue | Check broker availability |

### Error Handling Best Practices

```java
// Async send with error handling
producer.send(record, (metadata, exception) -> {
    if (exception != null) {
        if (exception instanceof TimeoutException) {
            // Retry logic
            retrySend(record);
        } else if (exception instanceof SerializationException) {
            // Log and discard
            log.error("Serialization error: {}", exception.getMessage());
        } else {
            // Store for later processing
            storeFailedRecord(record);
        }
    } else {
        log.info("Message sent to partition {}, offset {}", 
            metadata.partition(), metadata.offset());
    }
});
```

### Dead Letter Queue Pattern

```java
// Send to DLQ after max retries
private void sendWithRetry(ProducerRecord<String, String> record, int maxRetries) {
    int attempt = 0;
    while (attempt < maxRetries) {
        try {
            producer.send(record).get(5, TimeUnit.SECONDS);
            return;
        } catch (Exception e) {
            attempt++;
            if (attempt >= maxRetries) {
                sendToDLQ(record, e);
            }
        }
    }
}

private void sendToDLQ(ProducerRecord<String, String> record, Exception e) {
    ProducerRecord<String, String> dlqRecord = 
        new ProducerRecord<>("dlq-topic", record.key(), record.value());
    dlqRecord.headers().add("error", e.getMessage().getBytes());
    producer.send(dlqRecord);
}
```

---

## Best Practices

### Configuration

1. **Use async sending with callbacks** for reliability
2. **Enable idempotent producer** for exactly-once semantics
3. **Tune batch.size and linger.ms** based on throughput/latency needs
4. **Use compression** for high-volume topics
5. **Set appropriate timeouts** based on network conditions

### Partitioning

1. **Use key-based partitioning** for ordering guarantees
2. **Design keys for even distribution** to avoid hot partitions
3. **Consider sticky partitioning** for null keys
4. **Plan partition count** considering future scalability

### Error Handling

1. **Implement callbacks** for all sends
2. **Use dead letter queues** for failed messages
3. **Monitor error rates** and set alerts
4. **Log enough context** for debugging

### Monitoring

1. **Track send latency** and throughput
2. **Monitor batch fill rate** and size
3. **Alert on error rates** and retries
4. **Track record error rate** metric

---

## Further Reading

- [Kafka Producer Configuration](https://kafka.apache.org/documentation/#producerconfigs)
- [Kafka Producer API](https://kafka.apache.org/30/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html)
- [Idempotent Producer](https://kafka.apache.org/documentation/#idempotent_producer)
