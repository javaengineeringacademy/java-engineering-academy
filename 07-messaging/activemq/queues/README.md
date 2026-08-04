# ActiveMQ Queues

## Queue Messaging, Point-to-Point, and Queue Configuration

---

## Table of Contents

- [Overview](#overview)
- [Queue Concepts](#queue-concepts)
- [Point-to-Point Messaging](#point-to-point-messaging)
- [Queue Configuration](#queue-configuration)
- [Queue Operations](#queue-operations)
- [Best Practices](#best-practices)

---

## Overview

Queues in ActiveMQ provide point-to-point messaging where each message is consumed by exactly one consumer. This guide covers queue concepts, configuration, and operations.

### Queue Characteristics

- **FIFO Ordering**: Messages consumed in order
- **Single Consumer**: One consumer per message
- **Message Removal**: Messages removed after consumption
- **Load Balancing**: Distribution across consumers
- **Persistence**: Configurable message persistence

---

## Queue Concepts

### Queue Structure

```
Queue: orders
┌─────────────────────────────────────────────────────────────┐
│                    Queue: orders                              │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐            │
│  │ Msg1 │ │ Msg2 │ │ Msg3 │ │ Msg4 │ │ Msg5 │            │
│  └──────┘ └──────┘ └──────┘ └──────┘ └──────┘            │
│       │        │        │        │        │                 │
│       ▼        ▼        ▼        ▼        ▼                 │
│    First-in  ──────────────────────────────▶ Last-out      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Queue Consumers

```
Multiple Consumers:
Consumer 1 ──┐
             ▼
Consumer 2 ──┼──▶ ┌──────────┐
             ▼    │  Queue   │
Consumer 3 ──┘    └──────────┘

Load Balancing:
Message 1 → Consumer 1
Message 2 → Consumer 2
Message 3 → Consumer 3
Message 4 → Consumer 1 (round-robin)
```

---

## Point-to-Point Messaging

### Point-to-Point Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Point-to-Point Flow                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. Producer sends message to queue                          │
│     └── Message stored in queue                             │
│                                                              │
│  2. Queue dispatches message to consumer                     │
│     └── Only one consumer receives message                  │
│                                                              │
│  3. Consumer processes message                               │
│     └── Acknowledges when done                             │
│                                                              │
│  4. Message removed from queue                              │
│     └── Available for next message                          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Point-to-Point Example

```java
// Producer
Connection connection = factory.createConnection();
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
Queue queue = session.createQueue("orders");

MessageProducer producer = session.createProducer(queue);
TextMessage message = session.createTextMessage("Order 123");
producer.send(message);

// Consumer 1
MessageConsumer consumer1 = session.createConsumer(queue);
Message msg1 = consumer1.receive(); // Gets Order 123

// Consumer 2 (not used for this message)
MessageConsumer consumer2 = session.createConsumer(queue);
// msg1 already consumed, consumer2 waits for next message
```

---

## Queue Configuration

### Queue Properties

```java
// Create queue with properties
ActiveMQDestination queue = ActiveMQDestination.createQueue("orders?consumer.prefetchSize=10");

// Or using URL
ConnectionFactory factory = new ActiveMQConnectionFactory(
    "tcp://localhost:61616?jms.prefetchSize=10"
);
```

### Queue Configuration Options

| Option | Description |
|--------|-------------|
| `consumer.prefetchSize` | Messages to prefetch |
| `consumer.dispatchAsync` | Async message dispatch |
| `consumer.exclusive` | Exclusive consumer |
| `consumer.retroactive` | Retroactive dispatch |
| `consumer.priority` | Consumer priority |
| `queue.flowControl` | Enable flow control |
| `queue.flowControlLimit` | Flow control threshold |

### Broker Configuration

```xml
<!-- activemq.xml -->
<destinationPolicy>
    <destinationEntry>
        <destination queue="orders">
            <pendingQueuePolicy>
                <storeCursor/>
            </pendingQueuePolicy>
            <deadLetterStrategy>
                <individualDeadLetterStrategy queuePrefix="DLQ."/>
            </deadLetterStrategy>
        </destination>
    </destinationEntry>
</destinationPolicy>
```

---

## Queue Operations

### Create Queue

```java
// Programmatic
Queue queue = session.createQueue("orders");

// JNDI
// activemq.xml configuration
<destination>
    <queue name="orders"/>
</destination>
```

### Send Message

```java
MessageProducer producer = session.createProducer(queue);
TextMessage message = session.createTextMessage("Order data");
producer.send(message);
```

### Receive Message

```java
// Synchronous
MessageConsumer consumer = session.createConsumer(queue);
Message message = consumer.receive();

// Asynchronous
consumer.setMessageListener(message -> {
    if (message instanceof TextMessage) {
        String text = ((TextMessage) message).getText();
        System.out.println("Received: " + text);
    }
});
```

### Browse Queue

```java
// Create browser
QueueBrowser browser = session.createBrowser(queue);
Enumeration<Message> messages = browser.getEnumeration();

while (messages.hasMoreElements()) {
    Message message = messages.nextElement();
    // Process message without consuming
}
```

### Purge Queue

```java
// Via JMX
MBeanServer server = ManagementFactory.getPlatformMBeanServer();
ObjectName queueView = new ObjectName("org.apache.activemq:type=Broker,brokerName=localhost,destinationType=Queue,destinationName=orders");
server.invoke(queueView, "purge", new Object[]{}, new String[]{});
```

---

## Best Practices

### Queue Design

1. **Use meaningful names** - Follow naming conventions
2. **Set appropriate prefetch** - Balance throughput vs latency
3. **Configure dead letter queue** - Handle failed messages
4. **Monitor queue length** - Prevent memory issues

### Message Handling

1. **Use transactions** - For reliability
2. **Acknowledge properly** - Don't lose messages
3. **Handle redeliveries** - Implement retry logic
4. **Set message TTL** - Prevent queue buildup

### Performance

1. **Tune prefetch size** - Match to workload
2. **Use batch processing** - Process multiple messages
3. **Use async sends** - For higher throughput
4. **Monitor consumer lag** - Track processing progress

### Reliability

1. **Use persistent messages** - For critical data
2. **Use transactions** - For atomic operations
3. **Implement dead letter queues** - Capture failed messages
4. **Monitor metrics** - Track performance

---

## Further Reading

- [ActiveMQ Queues](https://activemq.apache.org/queues.html)
- [JMS Queue](https://jakarta.ee/specifications/messaging/)
- [ActiveMQ Configuration](https://activemq.apache.org/xml-reference.html)
