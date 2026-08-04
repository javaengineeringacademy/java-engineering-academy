# Integration Protocols - JMS

## Overview

JMS (Java Message Service) is a messaging standard for sending messages between two or more clients. It supports point-to-point (queues) and publish-subscribe (topics) models.

## Table of Contents

1. [JMS Basics](#jms-basics)
2. [Queues](#queues)
3. [Topics](#topics)
4. [Message Types](#message-types)
5. [Transactions](#transactions)
6. [Configuration](#configuration)

## JMS Basics

### JMS Architecture

```
┌──────────┐      ┌──────────┐      ┌──────────┐
│ Producer │ ───> │   JMS    │ ───> │ Consumer │
└──────────┘      │ Provider │      └──────────┘
                  └──────────┘
```

### JMS Components

| Component | Description |
|-----------|-------------|
| ConnectionFactory | Creates connections |
| Connection | Active connection |
| Session | Single-threaded context |
| Producer | Sends messages |
| Consumer | Receives messages |
| Message | Data payload |

## Queues

### Queue Producer

```java
ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
Connection connection = factory.createConnection();
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

Queue queue = session.createQueue("orders-queue");
MessageProducer producer = session.createProducer(queue);

TextMessage message = session.createTextMessage("Order data");
producer.send(message);
```

### Queue Consumer

```java
ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
Connection connection = factory.createConnection();
Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

Queue queue = session.createQueue("orders-queue");
MessageConsumer consumer = session.createConsumer(queue);

connection.start();
Message message = consumer.receive();
if (message instanceof TextMessage) {
    String text = ((TextMessage) message).getText();
    System.out.println("Received: " + text);
}
```

### Request-Reply Queue

```java
// Producer
Queue requestQueue = session.createQueue("request-queue");
Queue replyQueue = session.createQueue("reply-queue");

MessageProducer producer = session.createProducer(requestQueue);
TextMessage request = session.createTextMessage("Request data");
request.setJMSReplyTo(replyQueue);
producer.send(request);

// Consumer
MessageConsumer consumer = session.createConsumer(requestQueue);
Message received = consumer.receive();
// Process and send reply
TextMessage reply = session.createTextMessage("Reply data");
MessageProducer replyProducer = session.createProducer(replyQueue);
replyProducer.send(reply);
```

## Topics

### Topic Publisher

```java
Topic topic = session.createTopic("events-topic");
MessagePublisher publisher = session.createPublisher(topic);

TextMessage message = session.createTextMessage("Event data");
publisher.publish(message);
```

### Topic Subscriber

```java
Topic topic = session.createTopic("events-topic");
MessageSubscriber subscriber = session.createSubscriber(topic);

connection.start();
Message message = subscriber.receive();
```

### Durable Subscriber

```java
Topic topic = session.createTopic("events-topic");
MessageSubscriber subscriber = session.createDurableSubscriber(topic, "subscriber-1");

connection.start();
// Receive messages even when not connected
```

## Message Types

### TextMessage

```java
TextMessage message = session.createTextMessage("Text content");
message.setStringProperty("type", "ORDER");
```

### ObjectMessage

```java
Order order = new Order("123", "Product", 10.0);
ObjectMessage message = session.createObjectMessage(order);
```

### MapMessage

```java
MapMessage message = session.createMapMessage();
message.setString("orderId", "123");
message.setDouble("total", 99.99);
```

### BytesMessage

```java
BytesMessage message = session.createBytesMessage();
message.writeBytes(data);
```

## Transactions

### Transactional Producer

```java
Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
// Send messages
producer.send(message);
// Commit
session.commit();
```

### Transactional Consumer

```java
Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
MessageConsumer consumer = session.createConsumer(queue);
connection.start();
Message message = consumer.receive();
// Process message
session.commit(); // or session.rollback()
```

## Configuration

### ActiveMQ Configuration

```java
ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
factory.setBrokerURL("tcp://localhost:61616");
factory.setUser("admin");
factory.setPassword("admin");
```

### Connection Pooling

```java
PooledConnectionFactory pooledFactory = new PooledConnectionFactory();
pooledFactory.setConnectionFactory(factory);
pooledFactory.setMaxConnections(10);
```

## Best Practices

1. **Use transactions**: Ensure reliable delivery
2. **Handle acknowledgments**: Configure acknowledgment modes
3. **Use durable subscribers**: For reliable topic consumption
4. **Connection pooling**: Pool connections for performance
5. **Error handling**: Handle JMS exceptions properly
6. **Message expiration**: Set message TTL
7. **Priority**: Use message priority for ordering
8. **Monitoring**: Monitor queue depths and throughput

## References

- [JMS Specification](https://jakarta.ee/specifications/messaging/3.1/apidocs/)
- [ActiveMQ](https://activemq.apache.org/)
