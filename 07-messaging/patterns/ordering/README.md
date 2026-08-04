# Message Ordering Pattern

## Message Ordering, Partition Ordering, and Ordering Guarantees

---

## Table of Contents

- [Overview](#overview)
- [Ordering Concepts](#ordering-concepts)
- [Partition Ordering](#partition-ordering)
- [Key-Based Ordering](#key-based-ordering)
- [Implementation](#implementation)
- [Best Practices](#best-practices)

---

## Overview

Message ordering ensures messages are processed in a specific sequence. This guide covers ordering guarantees, partition-based ordering, and implementation strategies.

### Ordering Levels

| Level | Description |
|-------|-------------|
| Global | All messages in order |
| Partition | Messages within partition in order |
| Key | Messages with same key in order |

---

## Ordering Concepts

### Ordering Concept

```
Ordering Guarantees:
┌─────────────────────────────────────────────────────────────┐
│                    Message Ordering                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Global Ordering (difficult to achieve):                    │
│  Msg1 → Msg2 → Msg3 → Msg4 → Msg5                         │
│                                                              │
│  Partition Ordering (achievable):                           │
│  Partition 0: Msg1 → Msg3 → Msg5                           │
│  Partition 1: Msg2 → Msg4 → Msg6                           │
│                                                              │
│  Key-Based Ordering (common approach):                      │
│  Key "user-1": Msg1 → Msg4 → Msg7                          │
│  Key "user-2": Msg2 → Msg5 → Msg8                          │
│  Key "user-3": Msg3 → Msg6 → Msg9                          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Partition Ordering

### Partition Ordering Concept

```
Partition Ordering:
Messages within a partition are ordered

Partition 0:
┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐
│ Msg1 │→│ Msg3 │→│ Msg5 │→│ Msg7 │
└──────┘ └──────┘ └──────┘ └──────┘
Offset 0  Offset 1  Offset 2  Offset 3

Order guaranteed within partition
No ordering between partitions
```

### Partition Ordering Example

```java
// Producer sends to specific partition
Producer<String, String> producer = new KafkaProducer<>(props);

// All messages with same key go to same partition
producer.send(new ProducerRecord<>("events", "user-1", "Event 1"));
producer.send(new ProducerRecord<>("events", "user-1", "Event 2"));
producer.send(new ProducerRecord<>("events", "user-1", "Event 3"));

// Consumer processes in order
KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
consumer.subscribe(Arrays.asList("events"));

while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        // Events for "user-1" processed in order: Event 1, Event 2, Event 3
        processEvent(record);
    }
}
```

---

## Key-Based Ordering

### Key-Based Ordering Concept

```
Key-Based Ordering:
Same key always goes to same partition

Key "user-1" → hash("user-1") % 4 = Partition 2
Key "user-2" → hash("user-2") % 4 = Partition 0
Key "user-3" → hash("user-3") % 4 = Partition 1
Key "user-1" → hash("user-1") % 4 = Partition 2 (same partition)

Result:
Partition 2: user-1 events in order
Partition 0: user-2 events in order
Partition 1: user-3 events in order
```

### Key-Based Ordering Example

```java
// Producer with key-based partitioning
Producer<String, String> producer = new KafkaProducer<>(props);

// Events for same user go to same partition
producer.send(new ProducerRecord<>("user-events", "user-1", "login"));
producer.send(new ProducerRecord<>("user-events", "user-1", "click"));
producer.send(new ProducerRecord<>("user-events", "user-1", "purchase"));

producer.send(new ProducerRecord<>("user-events", "user-2", "signup"));
producer.send(new ProducerRecord<>("user-events", "user-2", "login"));

// Consumer processes in order per user
KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
consumer.subscribe(Arrays.asList("user-events"));

while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        // user-1 events: login → click → purchase (ordered)
        // user-2 events: signup → login (ordered)
        processUserEvent(record);
    }
}
```

---

## Implementation

### Kafka Ordering

```java
// Producer with idempotent writes for ordering
Properties producerProps = new Properties();
producerProps.put("bootstrap.servers", "localhost:9092");
producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
producerProps.put("enable.idempotence", true);
producerProps.put("acks", "all");
producerProps.put("max.in.flight.requests.per.connection", 5);

Producer<String, String> producer = new KafkaProducer<>(producerProps);

// Send with key for ordering
producer.send(new ProducerRecord<>("orders", "order-123", "created"));
producer.send(new ProducerRecord<>("orders", "order-123", "paid"));
producer.send(new ProducerRecord<>("orders", "order-123", "shipped"));

// Consumer with manual commit for ordering
Properties consumerProps = new Properties();
consumerProps.put("bootstrap.servers", "localhost:9092");
consumerProps.put("group.id", "order-processor");
consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
consumerProps.put("enable.auto.commit", false);

KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
consumer.subscribe(Arrays.asList("orders"));

while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
    for (ConsumerRecord<String, String> record : records) {
        // Process in order: created → paid → shipped
        processOrderEvent(record);
    }
    consumer.commitSync();
}
```

### RabbitMQ Ordering

```java
// Producer with message ordering
Channel channel = connection.createChannel();
channel.queueDeclare("orders", true, false, false, null);

// Send messages in order
channel.basicPublish("", "orders", null, "created".getBytes());
channel.basicPublish("", "orders", null, "paid".getBytes());
channel.basicPublish("", "orders", null, "shipped".getBytes());

// Single consumer for ordering
Channel consumerChannel = connection.createChannel();
consumerChannel.queueDeclare("orders", true, false, false, null);
consumerChannel.basicQos(1); // Process one at a time

consumerChannel.basicConsume("orders", false, new DefaultConsumer(consumerChannel) {
    @Override
    public void handleDelivery(String tag, Envelope envelope, AMQP.BasicProperties props, byte[] body) {
        String message = new String(body);
        // Process in order: created → paid → shipped
        processOrderEvent(message);
        consumerChannel.basicAck(envelope.getDeliveryTag(), false);
    }
});
```

---

## Best Practices

### Design

1. **Use key-based partitioning** - For per-key ordering
2. **Design keys for even distribution** - Avoid hot partitions
3. **Use idempotent producers** - Prevent duplicates
4. **Implement idempotent consumers** - Handle retries

### Performance

1. **Balance partition count** - Match to throughput needs
2. **Use appropriate prefetch** - Balance throughput vs ordering
3. **Avoid ordering when possible** - For better scalability
4. **Monitor consumer lag** - Track processing progress

### Reliability

1. **Use manual offset commit** - Ensure processing
2. **Handle rebalances gracefully** - Use listeners
3. **Implement dead letter queues** - Capture failed messages
4. **Test ordering scenarios** - Verify guarantees

### Operations

1. **Monitor partition assignment** - Ensure balance
2. **Track consumer lag** - Monitor backlog
3. **Document ordering requirements** - Maintain clarity
4. **Test failover scenarios** - Verify ordering preserved

---

## Further Reading

- [Kafka Ordering](https://kafka.apache.org/documentation/#design_gap Ordering guarantees)
- [Message Ordering](https://www.enterpriseintegrationpatterns.com/patterns/messaging/MessageOrder.html)
- [Consumer Groups](https://kafka.apache.org/documentation/#consumerconfigs)
