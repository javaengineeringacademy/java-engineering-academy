# ActiveMQ Fundamentals

## ActiveMQ, JMS, Destinations, and Message Types

---

## Table of Contents

- [Overview](#overview)
- [JMS Concepts](#jms-concepts)
- [ActiveMQ Architecture](#activemq-architecture)
- [Destinations](#destinations)
- [Message Types](#message-types)
- [Getting Started](#getting-started)
- [Best Practices](#best-practices)

---

## Overview

Apache ActiveMQ is an open-source message broker implementing the Java Message Service (JMS) specification. It provides reliable messaging with support for multiple protocols and messaging patterns.

### Key Features

- **JMS Compliance**: Full JMS 1.1/2.0 implementation
- **Multiple Protocols**: OpenWire, AMQP, MQTT, STOMP
- **Persistence**: Configurable message persistence
- **Clustering**: High availability and scaling
- **Management**: Web console and JMX monitoring

### ActiveMQ Variants

| Variant | Description |
|---------|-------------|
| ActiveMQ Classic | Traditional broker, JVM-based |
| ActiveMQ Artemis | Next-gen broker, better performance |

---

## JMS Concepts

### JMS Model

```
┌─────────────────────────────────────────────────────────────┐
│                    JMS Model                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐            │
│  │ Producer │────▶│    JMS   │────▶│ Consumer │            │
│  │ (Sender) │     │  Broker  │     │(Receiver)│            │
│  └──────────┘     └──────────┘     └──────────┘            │
│                          │                                   │
│                          │                                   │
│                    ┌──────────┐                            │
│                    │Destination│                            │
│                    │(Queue/    │                            │
│                    │ Topic)    │                            │
│                    └──────────┘                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### JMS Components

| Component | Description |
|-----------|-------------|
| Connection | Connection to broker |
| Session | Context for message operations |
| Producer | Sends messages |
| Consumer | Receives messages |
| Destination | Queue or Topic |
| Message | Data being sent |

---

## ActiveMQ Architecture

### Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    ActiveMQ Broker                           │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Transport Layer                     │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐          │   │
│  │  │ OpenWire │  │  AMQP    │  │  MQTT    │          │   │
│  │  └──────────┘  └──────────┘  └──────────┘          │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Broker Core                        │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐          │   │
│  │  │ Destina- │  │ Message  │  │  Store   │          │   │
│  │  │ tions    │  │ Dispatch │  │          │          │   │
│  │  └──────────┘  └──────────┘  └──────────┘          │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Persistence Layer                   │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐          │   │
│  │  │ JDBC     │  │  KahaDB  │  │ LevelDB  │          │   │
│  │  └──────────┘  └──────────┘  └──────────┘          │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Destinations

### Queue (Point-to-Point)

```
Queue:
┌──────────┐     ┌──────────┐     ┌──────────┐
│ Producer │────▶│  Queue   │────▶│ Consumer │
└──────────┘     └──────────┘     └──────────┘

Properties:
- FIFO ordering
- One consumer per message
- Message removed after consumption
- Load balancing across consumers
```

### Topic (Publish/Subscribe)

```
Topic:
┌──────────┐     ┌──────────┐     ┌──────────┐
│ Producer │────▶│   Topic  │────▶│Consumer 1│
└──────────┘     └──────────┘     └──────────┘
                       │           ┌──────────┐
                       └──────────▶│Consumer 2│
                                   └──────────┘
                                   ┌──────────┐
                                   │Consumer 3│
                                   └──────────┘

Properties:
- Broadcast to all subscribers
- Each consumer receives copy
- Messages expire if no subscribers
- Durable subscriptions available
```

### Destination Configuration

```java
// Queue
Destination queue = session.createQueue("orders");

// Topic
Destination topic = session.createTopic("notifications");

// Temporary queue
TemporaryQueue tempQueue = session.createTemporaryQueue();
```

---

## Message Types

### Message Types

| Type | Description |
|------|-------------|
| TextMessage | String payload |
| ObjectMessage | Java object payload |
| BytesMessage | Raw byte array |
| MapMessage | Key-value pairs |
| StreamMessage | Stream of primitives |

### TextMessage

```java
// Create
TextMessage message = session.createTextMessage("Hello World");

// Read
String text = message.getText();
```

### ObjectMessage

```java
// Create
Order order = new Order("123", 99.99);
ObjectMessage message = session.createObjectMessage(order);

// Read
Order order = (Order) message.getObject();
```

### MapMessage

```java
// Create
MapMessage message = session.createMapMessage();
message.setString("orderId", "123");
message.setDouble("amount", 99.99);

// Read
String orderId = message.getString("orderId");
double amount = message.getDouble("amount");
```

### BytesMessage

```java
// Create
BytesMessage message = session.createBytesMessage();
message.writeBytes(data);

// Read
byte[] data = new byte[(int) message.getBodyLength()];
message.readBytes(data);
```

---

## Getting Started

### Maven Dependency

```xml
<!-- ActiveMQ Classic -->
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>activemq-client</artifactId>
    <version>5.17.3</version>
</dependency>

<!-- ActiveMQ Artemis -->
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>artemis-jms-client</artifactId>
    <version>2.31.2</version>
</dependency>
```

### Basic Producer

```java
ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
Connection connection = factory.createConnection();
connection.start();

Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
Destination queue = session.createQueue("orders");

MessageProducer producer = session.createProducer(queue);
TextMessage message = session.createTextMessage("Order data");
producer.send(message);

connection.close();
```

### Basic Consumer

```java
ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
Connection connection = factory.createConnection();
connection.start();

Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
Destination queue = session.createQueue("orders");

MessageConsumer consumer = session.createConsumer(queue);
Message message = consumer.receive();

if (message instanceof TextMessage) {
    String text = ((TextMessage) message).getText();
    System.out.println("Received: " + text);
}

connection.close();
```

---

## Best Practices

### Connection Management

1. **Use connection pooling** - Reuse connections
2. **Handle failures** - Implement reconnection
3. **Close resources** - Properly close connections
4. **Use transactions** - For reliability

### Destination Design

1. **Use meaningful names** - Follow naming conventions
2. **Set message TTL** - Prevent queue buildup
3. **Use dead letter queues** - Handle failed messages
4. **Monitor queue length** - Prevent memory issues

### Message Design

1. **Keep messages small** - Reduce network overhead
2. **Use appropriate type** - Match data format
3. **Include message ID** - Enable deduplication
4. **Set message properties** - Add metadata

### Performance

1. **Use batching** - Batch messages
2. **Tune prefetch** - Balance throughput vs latency
3. **Use async sends** - For higher throughput
4. **Monitor metrics** - Track performance

---

## Further Reading

- [ActiveMQ Documentation](https://activemq.apache.org/docs)
- [JMS Specification](https://jakarta.ee/specifications/messaging/)
- [ActiveMQ Artemis](https://activemq.apache.org/components/artemis/)
