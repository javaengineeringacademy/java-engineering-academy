# Message Queue Pattern

## Point-to-Point, Competing Consumers, and Work Queues

---

## Table of Contents

- [Overview](#overview)
- [Point-to-Point Architecture](#point-to-point-architecture)
- [Competing Consumers](#competing-consumers)
- [Work Queues](#work-queues)
- [Implementation](#implementation)
- [Best Practices](#best-practices)

---

## Overview

Message Queue pattern (Point-to-Point) provides reliable, asynchronous communication between producers and consumers. Each message is processed by exactly one consumer, enabling load balancing and fault tolerance.

### Key Characteristics

- **One-to-One**: One message to one consumer
- **Load Balancing**: Distribution across consumers
- **Fault Tolerance**: Automatic failover
- **Decoupling**: Producer and consumer independent

---

## Point-to-Point Architecture

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    Point-to-Point Pattern                     │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐                            │
│  │ Producer │────▶│  Queue   │                            │
│  └──────────┘     └──────────┘                            │
│                         │                                   │
│                         │ Message dispatched                │
│                         │ to one consumer                   │
│                         ▼                                   │
│                    ┌──────────┐                            │
│                    │Consumer 1│ ←── Receives message       │
│                    └──────────┘                            │
│                         │                                   │
│                    ┌──────────┐                            │
│                    │Consumer 2│ ←── Waits for next         │
│                    └──────────┘                            │
│                         │                                   │
│                    ┌──────────┐                            │
│                    │Consumer 3│ ←── Waits for next         │
│                    └──────────┘                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Message Flow

```
Normal Flow:
1. Producer sends message to queue
2. Queue dispatches message to available consumer
3. Consumer processes message
4. Consumer acknowledges completion
5. Queue removes message

Failover Flow:
1. Consumer 1 receives message
2. Consumer 1 crashes
3. Queue redelivers message to Consumer 2
4. Consumer 2 processes message
```

---

## Competing Consumers

### Competing Consumers Concept

```
Competing Consumers:
Multiple consumers compete for messages

Consumer 1 ──┐
             ▼
Consumer 2 ──┼──▶ ┌──────────┐
             ▼    │  Queue   │
Consumer 3 ──┘    └──────────┘

Load Balancing:
Message 1 → Consumer 1 (Processing)
Message 2 → Consumer 2 (Processing)
Message 3 → Consumer 3 (Processing)
Message 4 → Consumer 1 (Ready)
```

### Competing Consumers Example

```java
// Producer
Connection connection = factory.createConnection();
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
Queue queue = session.createQueue("tasks");

MessageProducer producer = session.createProducer(queue);
for (int i = 0; i < 10; i++) {
    TextMessage message = session.createTextMessage("Task " + i);
    producer.send(message);
}

// Consumer 1
MessageConsumer consumer1 = session.createConsumer(queue);
consumer1.setMessageListener(message -> {
    System.out.println("Consumer 1: " + ((TextMessage) message).getText());
    // Process task
});

// Consumer 2
MessageConsumer consumer2 = session.createConsumer(queue);
consumer2.setMessageListener(message -> {
    System.out.println("Consumer 2: " + ((TextMessage) message).getText());
    // Process task
});

// Consumer 3
MessageConsumer consumer3 = session.createConsumer(queue);
consumer3.setMessageListener(message -> {
    System.out.println("Consumer 3: " + ((TextMessage) message).getText());
    // Process task
});
```

---

## Work Queues

### Work Queue Concept

```
Work Queue:
Distribute work across workers

┌──────────┐     ┌──────────┐     ┌──────────┐
│  Master  │────▶│  Queue   │────▶│ Worker 1 │
└──────────┘     └──────────┘     └──────────┘
                     │            ┌──────────┐
                     └───────────▶│ Worker 2 │
                                  └──────────┘
                                  ┌──────────┐
                                  │ Worker 3 │
                                  └──────────┘
```

### Work Queue Implementation

```java
// Worker 1
MessageConsumer worker1 = session.createConsumer(queue);
worker1.setMessageListener(message -> {
    try {
        String task = ((TextMessage) message).getText();
        processTask(task);
        session.commit(); // Acknowledge
    } catch (Exception e) {
        session.rollback(); // Redeliver
    }
});

// Worker 2
MessageConsumer worker2 = session.createConsumer(queue);
worker2.setMessageListener(message -> {
    try {
        String task = ((TextMessage) message).getText();
        processTask(task);
        session.commit();
    } catch (Exception e) {
        session.rollback();
    }
});
```

---

## Implementation

### Kafka Implementation

```java
// Producer
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

Producer<String, String> producer = new KafkaProducer<>(props);
for (int i = 0; i < 10; i++) {
    producer.send(new ProducerRecord<>("tasks", "task-" + i, "Task data " + i));
}

// Workers (same consumer group)
Properties consumerProps = new Properties();
consumerProps.put("bootstrap.servers", "localhost:9092");
consumerProps.put("group.id", "task-workers");
consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

// Worker 1
KafkaConsumer<String, String> worker1 = new KafkaConsumer<>(consumerProps);
worker1.subscribe(Arrays.asList("tasks"));

// Worker 2
KafkaConsumer<String, String> worker2 = new KafkaConsumer<>(consumerProps);
worker2.subscribe(Arrays.asList("tasks"));
```

### RabbitMQ Implementation

```java
// Producer
Channel channel = connection.createChannel();
channel.queueDeclare("tasks", true, false, false, null);

for (int i = 0; i < 10; i++) {
    String message = "Task " + i;
    channel.basicPublish("", "tasks", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
}

// Worker 1
Channel workerChannel1 = connection.createChannel();
workerChannel1.queueDeclare("tasks", true, false, false, null);
workerChannel1.basicQos(1); // Prefetch 1

workerChannel1.basicConsume("tasks", false, new DefaultConsumer(workerChannel1) {
    @Override
    public void handleDelivery(String tag, Envelope envelope, AMQP.BasicProperties props, byte[] body) {
        String message = new String(body);
        processTask(message);
        workerChannel1.basicAck(envelope.getDeliveryTag(), false);
    }
});

// Worker 2
Channel workerChannel2 = connection.createChannel();
workerChannel2.queueDeclare("tasks", true, false, false, null);
workerChannel2.basicQos(1);

workerChannel2.basicConsume("tasks", false, new DefaultConsumer(workerChannel2) {
    @Override
    public void handleDelivery(String tag, Envelope envelope, AMQP.BasicProperties props, byte[] body) {
        String message = new String(body);
        processTask(message);
        workerChannel2.basicAck(envelope.getDeliveryTag(), false);
    }
});
```

---

## Best Practices

### Design

1. **Use meaningful queue names** - Follow naming conventions
2. **Set appropriate prefetch** - Balance throughput vs latency
3. **Implement acknowledgment** - Ensure message processing
4. **Monitor queue length** - Prevent buildup

### Performance

1. **Use multiple workers** - Parallel processing
2. **Tune prefetch size** - Match to workload
3. **Use async processing** - For higher throughput
4. **Batch messages** - Reduce overhead

### Reliability

1. **Use persistent messages** - For critical data
2. **Implement transactions** - For atomic operations
3. **Handle failures** - Redeliver on failure
4. **Use dead letter queues** - Capture failed messages

### Operations

1. **Monitor worker performance** - Track processing
2. **Scale workers** - Add workers as needed
3. **Track consumer lag** - Monitor backlog
4. **Document procedures** - Maintain runbooks

---

## Further Reading

- [Message Queue Pattern](https://www.enterpriseintegrationpatterns.com/patterns/messaging/MessageChannel.html)
- [Competing Consumers](https://www.enterpriseintegrationpatterns.com/patterns/messaging/CompetingConsumers.html)
- [Work Queues](https://www.rabbitmq.com/tutorials/tutorial-two-python.html)
