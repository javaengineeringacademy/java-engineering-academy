# Consumer Groups Pattern

## Consumer Groups, Parallel Processing, and Load Balancing

---

## Table of Contents

- [Overview](#overview)
- [Consumer Group Architecture](#consumer-group-architecture)
- [Parallel Processing](#parallel-processing)
- [Load Balancing](#load-balancing)
- [Implementation](#implementation)
- [Best Practices](#best-practices)

---

## Overview

Consumer Groups enable parallel processing of messages by distributing partitions across multiple consumers. Each partition is consumed by exactly one consumer in a group, enabling horizontal scaling.

### Key Characteristics

- **Parallel Processing**: Multiple consumers process simultaneously
- **Partition Assignment**: Each partition assigned to one consumer
- **Load Balancing**: Distribution across consumers
- **Fault Tolerance**: Automatic failover on consumer failure

---

## Consumer Group Architecture

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    Consumer Group Pattern                     │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐                            │
│  │ Producer │────▶│  Topic   │                            │
│  └──────────┘     │          │                            │
│                   │ Part 0   │────▶ ┌──────────┐           │
│                   │ Part 1   │     │Consumer 1 │           │
│                   │ Part 2   │     └──────────┘           │
│                   │ Part 3   │                            │
│                   │          │────▶ ┌──────────┐           │
│                   │          │     │Consumer 2 │           │
│                   └──────────┘     └──────────┘           │
│                                                              │
│  Consumer Group: order-processor                             │
│  Partitions: 0, 1, 2, 3                                    │
│  Consumers: 2                                               │
│  Assignment: Consumer 1 → [0, 1], Consumer 2 → [2, 3]     │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Partition Assignment

```
Consumer Group: order-processor
Topic: orders (4 partitions)

Consumer 1: Partitions [0, 1]
Consumer 2: Partitions [2, 3]

If Consumer 1 fails:
Consumer 1: Partitions [] (failed)
Consumer 2: Partitions [0, 1, 2, 3] (reassigned)
```

---

## Parallel Processing

### Parallel Processing Concept

```
Parallel Processing:
Multiple consumers process different partitions concurrently

Partition 0 ──▶ Consumer 1 (Processing)
Partition 1 ──▶ Consumer 1 (Processing)
Partition 2 ──▶ Consumer 2 (Processing)
Partition 3 ──▶ Consumer 2 (Processing)

Throughput = Sum of all consumer throughputs
```

### Parallel Processing Example

```java
// Consumer 1 (processes partitions 0, 1)
KafkaConsumer<String, String> consumer1 = new KafkaConsumer<>(props);
consumer1.subscribe(Arrays.asList("orders"));
while (true) {
    ConsumerRecords<String, String> records = consumer1.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        processRecord(record); // Parallel processing
    }
}

// Consumer 2 (processes partitions 2, 3)
KafkaConsumer<String, String> consumer2 = new KafkaConsumer<>(props);
consumer2.subscribe(Arrays.asList("orders"));
while (true) {
    ConsumerRecords<String, String> records = consumer2.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        processRecord(record); // Parallel processing
    }
}
```

---

## Load Balancing

### Load Balancing Concept

```
Load Balancing:
Distribute work evenly across consumers

Before:
Consumer 1: 100 messages
Consumer 2: 50 messages
Consumer 3: 25 messages

After Load Balancing:
Consumer 1: 58 messages
Consumer 2: 59 messages
Consumer 3: 58 messages
```

### Load Balancing Strategies

| Strategy | Description |
|----------|-------------|
| Round Robin | Distribute sequentially |
| Key-Based | Same key to same consumer |
| Sticky | Minimize partition movement |
| Cooperative | Incremental rebalancing |

---

## Implementation

### Kafka Consumer Group

```java
// Producer
Properties producerProps = new Properties();
producerProps.put("bootstrap.servers", "localhost:9092");
producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

Producer<String, String> producer = new KafkaProducer<>(producerProps);

// Publish messages with keys for ordering
for (int i = 0; i < 100; i++) {
    String key = "user-" + (i % 10); // 10 unique keys
    String value = "Event " + i;
    producer.send(new ProducerRecord<>("events", key, value));
}

// Consumer Group
Properties consumerProps = new Properties();
consumerProps.put("bootstrap.servers", "localhost:9092");
consumerProps.put("group.id", "event-processor");
consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
consumerProps.put("enable.auto.commit", false);

// Consumer 1
KafkaConsumer<String, String> consumer1 = new KafkaConsumer<>(consumerProps);
consumer1.subscribe(Arrays.asList("events"));

// Consumer 2
KafkaConsumer<String, String> consumer2 = new KafkaConsumer<>(consumerProps);
consumer2.subscribe(Arrays.asList("events"));
```

### RabbitMQ Consumer Group

```java
// Producer
Channel channel = connection.createChannel();
channel.queueDeclare("events", true, false, false, null);

for (int i = 0; i < 100; i++) {
    channel.basicPublish("", "events", null, ("Event " + i).getBytes());
}

// Consumer 1 (same queue, competing consumers)
Channel channel1 = connection.createChannel();
channel1.queueDeclare("events", true, false, false, null);
channel1.basicQos(1);

channel1.basicConsume("events", false, new DefaultConsumer(channel1) {
    @Override
    public void handleDelivery(String tag, Envelope envelope, AMQP.BasicProperties props, byte[] body) {
        String message = new String(body);
        processMessage(message);
        channel1.basicAck(envelope.getDeliveryTag(), false);
    }
});

// Consumer 2 (same queue, competing consumers)
Channel channel2 = connection.createChannel();
channel2.queueDeclare("events", true, false, false, null);
channel2.basicQos(1);

channel2.basicConsume("events", false, new DefaultConsumer(channel2) {
    @Override
    public void handleDelivery(String tag, Envelope envelope, AMQP.BasicProperties props, byte[] body) {
        String message = new String(body);
        processMessage(message);
        channel2.basicAck(envelope.getDeliveryTag(), false);
    }
});
```

---

## Best Practices

### Design

1. **Use appropriate partition count** - Match to consumer count
2. **Use meaningful group IDs** - Follow naming conventions
3. **Implement idempotent processing** - Handle duplicates
4. **Monitor consumer lag** - Track processing progress

### Performance

1. **Scale consumers horizontally** - Add consumers as needed
2. **Tune prefetch size** - Balance throughput vs latency
3. **Use async processing** - For higher throughput
4. **Batch messages** - Reduce overhead

### Reliability

1. **Use manual offset commit** - Ensure processing
2. **Implement dead letter queues** - Capture failed messages
3. **Handle rebalances gracefully** - Use listeners
4. **Monitor consumer health** - Track failures

### Operations

1. **Monitor consumer lag** - Track backlog
2. **Scale consumers** - Add workers as needed
3. **Track partition assignment** - Ensure balance
4. **Document procedures** - Maintain runbooks

---

## Further Reading

- [Consumer Groups](https://kafka.apache.org/documentation/#consumerconfigs)
- [Load Balancing](https://www.enterpriseintegrationpatterns.com/patterns/messaging/CompetingConsumers.html)
- [Parallel Processing](https://docs.confluent.io/platform/current/kafka/streams.html)
