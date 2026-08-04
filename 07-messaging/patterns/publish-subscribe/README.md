# Publish-Subscribe Pattern

## Pub/Sub Pattern, Fan-Out, and Event Broadcasting

---

## Table of Contents

- [Overview](#overview)
- [Pub/Sub Architecture](#pubsub-architecture)
- [Fan-Out Pattern](#fan-out-pattern)
- [Implementation](#implementation)
- [Use Cases](#use-cases)
- [Best Practices](#best-practices)

---

## Overview

Publish-Subscribe (Pub/Sub) is a messaging pattern where message senders (publishers) do not send messages directly to receivers (subscribers). Instead, messages are published to a topic, and all subscribers receive a copy.

### Key Characteristics

- **Decoupling**: Publishers and subscribers independent
- **Fan-Out**: One message to multiple subscribers
- **No State**: Subscribers don't affect each other
- **Async**: Non-blocking message delivery

---

## Pub/Sub Architecture

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    Publish/Subscribe Pattern                  │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐                            │
│  │ Publisher │────▶│  Topic   │                            │
│  │    1     │     │          │                            │
│  └──────────┘     │          │                            │
│                   │          │────▶ ┌──────────┐           │
│  ┌──────────┐     │          │     │Subscriber │           │
│  │ Publisher │────▶│          │     │    1     │           │
│  │    2     │     │          │     └──────────┘           │
│  └──────────┘     │          │                            │
│                   │          │────▶ ┌──────────┐           │
│                   │          │     │Subscriber │           │
│                   │          │     │    2     │           │
│                   │          │     └──────────┘           │
│                   │          │                            │
│                   │          │────▶ ┌──────────┐           │
│                   │          │     │Subscriber │           │
│                   └──────────┘     │    3     │           │
│                                    └──────────┘           │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Component Roles

| Component | Responsibility |
|-----------|---------------|
| Publisher | Sends messages to topic |
| Topic | Channels messages to subscribers |
| Subscriber | Receives messages from topic |

---

## Fan-Out Pattern

### Fan-Out Concept

```
Fan-Out:
One message → Multiple destinations

┌──────────┐     ┌──────────┐     ┌──────────┐
│          │────▶│  Email   │    │  Email   │
│ Publisher│     │ Service  │    │ Consumer │
│          │     └──────────┘    └──────────┘
│          │                            ▲
│          │     ┌──────────┐          │
│          │────▶│   SMS    │──────────┘
│          │     │ Service  │
│          │     └──────────┘
│          │                            ▲
│          │     ┌──────────┐          │
│          │────▶│  Push    │──────────┘
│          │     │ Service  │
└──────────┘     └──────────┘
```

### Fan-Out Implementation

```java
// Kafka Fan-Out
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

Producer<String, String> producer = new KafkaProducer<>(props);

// Publish to topic
ProducerRecord<String, String> record = 
    new ProducerRecord<>("notifications", "user-123", "Alert: System down");
producer.send(record);

// Consumer 1 (different consumer group)
KafkaConsumer<String, String> consumer1 = new KafkaConsumer<>(props);
consumer1.subscribe(Arrays.asList("notifications"));

// Consumer 2 (different consumer group)
KafkaConsumer<String, String> consumer2 = new KafkaConsumer<>(props);
consumer2.subscribe(Arrays.asList("notifications"));
```

---

## Implementation

### Kafka Implementation

```java
// Publisher
Producer<String, String> producer = new KafkaProducer<>(props);
producer.send(new ProducerRecord<>("events", "order-created", orderData));

// Subscriber 1 (Analytics)
KafkaConsumer<String, String> analyticsConsumer = new KafkaConsumer<>(props);
analyticsConsumer.subscribe(Arrays.asList("events"));

// Subscriber 2 (Notifications)
KafkaConsumer<String, String> notificationConsumer = new KafkaConsumer<>(props);
notificationConsumer.subscribe(Arrays.asList("events"));

// Subscriber 3 (Audit)
KafkaConsumer<String, String> auditConsumer = new KafkaConsumer<>(props);
auditConsumer.subscribe(Arrays.asList("events"));
```

### RabbitMQ Implementation

```java
// Publisher
Channel channel = connection.createChannel();
channel.exchangeDeclare("events", "fanout");

String message = "Order created";
channel.basicPublish("events", "", null, message.getBytes());

// Subscriber 1
Channel channel1 = connection.createChannel();
channel1.exchangeDeclare("events", "fanout");
String queueName1 = channel1.queueDeclare().getQueue();
channel1.queueBind(queueName1, "events", "");

// Subscriber 2
Channel channel2 = connection.createChannel();
channel2.exchangeDeclare("events", "fanout");
String queueName2 = channel2.queueDeclare().getQueue();
channel2.queueBind(queueName2, "events", "");
```

---

## Use Cases

| Use Case | Description |
|----------|-------------|
| Event Broadcasting | Broadcast events to multiple services |
| Log Distribution | Distribute logs to multiple processors |
| Cache Invalidation | Notify multiple caches of changes |
| Real-time Notifications | Send notifications to multiple clients |

---

## Best Practices

### Design

1. **Use appropriate topic** - Match to use case
2. **Keep messages small** - Reduce network overhead
3. **Use meaningful keys** - For partitioning
4. **Document topics** - Maintain clarity

### Performance

1. **Use async publishing** - For higher throughput
2. **Tune prefetch** - Balance throughput vs latency
3. **Use compression** - Reduce network overhead
4. **Monitor subscriber lag** - Track processing progress

### Reliability

1. **Use durable subscriptions** - For critical data
2. **Implement acknowledgments** - Ensure delivery
3. **Use dead letter queues** - Capture failed messages
4. **Monitor metrics** - Track performance

---

## Further Reading

- [Publish-Subscribe Pattern](https://www.enterpriseintegrationpatterns.com/patterns/messaging/PublishSubscribeChannel.html)
- [Kafka Topics](https://kafka.apache.org/documentation/#topics)
- [RabbitMQ Fanout](https://www.rabbitmq.com/tutorials/tutorial-four-python.html)
