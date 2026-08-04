# ActiveMQ Topics

## Topic Messaging, Publish/Subscribe, and Topic Configuration

---

## Table of Contents

- [Overview](#overview)
- [Topic Concepts](#topic-concepts)
- [Publish/Subscribe](#publishsubscribe)
- [Durable Subscriptions](#durable-subscriptions)
- [Topic Configuration](#topic-configuration)
- [Best Practices](#best-practices)

---

## Overview

Topics in ActiveMQ provide publish/subscribe messaging where each message is broadcast to all subscribers. This guide covers topic concepts, durable subscriptions, and configuration.

### Topic Characteristics

- **Broadcast**: Message sent to all subscribers
- **Fan-Out**: One message to multiple consumers
- **No Persistence**: Messages lost if no subscribers
- **Durable Subscriptions**: Optional message persistence

---

## Topic Concepts

### Topic Structure

```
Topic: notifications
┌─────────────────────────────────────────────────────────────┐
│                    Topic: notifications                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐                            │
│  │ Producer │────▶│   Topic  │                            │
│  └──────────┘     └──────────┘                            │
│                         │                                   │
│                         ▼                                   │
│                    ┌──────────┐                            │
│                    │Consumer 1│                            │
│                    └──────────┘                            │
│                         │                                   │
│                         ▼                                   │
│                    ┌──────────┐                            │
│                    │Consumer 2│                            │
│                    └──────────┘                            │
│                         │                                   │
│                         ▼                                   │
│                    ┌──────────┐                            │
│                    │Consumer 3│                            │
│                    └──────────┘                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Publish/Subscribe

### Pub/Sub Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Publish/Subscribe Flow                     │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. Producer publishes message to topic                      │
│     └── Message broadcast to all subscribers                │
│                                                              │
│  2. Each subscriber receives copy of message                 │
│     └── Independent processing                              │
│                                                              │
│  3. Subscribers acknowledge independently                    │
│     └── No interaction between subscribers                  │
│                                                              │
│  4. Message discarded after broadcast                        │
│     └── No persistence (unless durable)                     │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Pub/Sub Example

```java
// Producer
Connection connection = factory.createConnection();
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
Topic topic = session.createTopic("notifications");

MessageProducer producer = session.createProducer(topic);
TextMessage message = session.createTextMessage("Alert: System down");
producer.send(message);

// Consumer 1 (receives message)
MessageConsumer consumer1 = session.createConsumer(topic);
consumer1.setMessageListener(msg -> {
    System.out.println("Consumer 1: " + ((TextMessage) msg).getText());
});

// Consumer 2 (receives same message)
MessageConsumer consumer2 = session.createConsumer(topic);
consumer2.setMessageListener(msg -> {
    System.out.println("Consumer 2: " + ((TextMessage) msg).getText());
});
```

---

## Durable Subscriptions

### Durable Subscription Concept

```
Non-Durable Subscription:
Subscriber disconnects → Misses messages

Durable Subscription:
Subscriber disconnects → Messages stored → Delivered on reconnect

┌─────────────────────────────────────────────────────────────┐
│                    Durable Subscription                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. Subscriber connects and creates durable subscription     │
│     └── Broker stores messages for this subscriber          │
│                                                              │
│  2. Subscriber disconnects                                   │
│     └── Messages continue to be stored                      │
│                                                              │
│  3. Subscriber reconnects                                    │
│     └── Receives stored messages                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Durable Subscription Example

```java
// Create durable subscription
String clientId = "subscriber-1";
String subscriptionName = "alerts-sub";

connection.setClientID(clientId);
connection.start();

Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
Topic topic = session.createTopic("alerts");

MessageConsumer consumer = session.createDurableConsumer(
    topic, 
    subscriptionName
);

consumer.setMessageListener(message -> {
    System.out.println("Received: " + ((TextMessage) message).getText());
});

// Later: Unsubscribe
// session.unsubscribe(subscriptionName);
```

### Durable vs Non-Durable

| Feature | Non-Durable | Durable |
|---------|-------------|---------|
| Message Persistence | No | Yes |
| Missed Messages | Yes | No |
| Resource Usage | Low | Higher |
| Use Case | Real-time only | Critical data |

---

## Topic Configuration

### Topic Properties

```java
// Create topic with properties
ActiveMQDestination topic = ActiveMQDestination.createTopic("notifications?retroactive=true");

// Or using URL
ConnectionFactory factory = new ActiveMQConnectionFactory(
    "tcp://localhost:61616?jms.retroactive=true"
);
```

### Topic Configuration Options

| Option | Description |
|--------|-------------|
| `retroactive` | Dispatch messages from before subscription |
| `consumer.prefetchSize` | Messages to prefetch |
| `consumer.dispatchAsync` | Async message dispatch |
| `topic.flowControl` | Enable flow control |
| `topic.flowControlLimit` | Flow control threshold |

### Broker Configuration

```xml
<!-- activemq.xml -->
<destinationPolicy>
    <destinationEntry>
        <destination topic="notifications">
            <pendingDurableTopicPolicy>
                <storeCursor/>
            </pendingDurableTopicPolicy>
        </destination>
    </destinationEntry>
</destinationPolicy>
```

---

## Best Practices

### Topic Design

1. **Use meaningful names** - Follow naming conventions
2. **Set appropriate prefetch** - Match to workload
3. **Use durable subscriptions** - For critical data
4. **Monitor subscriber count** - Track active subscribers

### Message Handling

1. **Use async listeners** - For better performance
2. **Handle reconnections** - Reconnect durable subscriptions
3. **Implement message filtering** - Use message selectors
4. **Set message TTL** - Prevent message buildup

### Performance

1. **Tune prefetch size** - Balance throughput vs latency
2. **Use non-durable when possible** - Less overhead
3. **Batch messages** - Reduce network overhead
4. **Monitor consumer lag** - Track processing progress

### Reliability

1. **Use durable subscriptions** - For critical data
2. **Implement acknowledgment** - Ensure message processing
3. **Handle failures** - Reconnect on failure
4. **Monitor metrics** - Track performance

---

## Further Reading

- [ActiveMQ Topics](https://activemq.apache.org/topics.html)
- [JMS Topics](https://jakarta.ee/specifications/messaging/)
- [Durable Subscriptions](https://activemq.apache.org/jms-tutorial.html)
