# RabbitMQ Fundamentals

## AMQP Protocol, Exchanges, Queues, and Bindings

---

## Table of Contents

- [Overview](#overview)
- [AMQP Protocol](#amqp-protocol)
- [RabbitMQ Architecture](#rabbitmq-architecture)
- [Exchanges](#exchanges)
- [Queues](#queues)
- [Bindings](#bindings)
- [Message Flow](#message-flow)
- [Getting Started](#getting-started)
- [Best Practices](#best-practices)

---

## Overview

RabbitMQ is an open-source message broker implementing the Advanced Message Queuing Protocol (AMQP). It provides flexible routing, reliable delivery, and multiple messaging patterns.

### Key Features

- **AMQP 0-9-1**: Full protocol implementation
- **Flexible Routing**: Multiple exchange types
- **Reliability**: Publisher confirms, consumer acknowledgments
- **Clustering**: High availability and scaling
- **Management UI**: Built-in web interface
- **Plugins**: Extensible functionality

---

## AMQP Protocol

### AMQP Model

```
┌─────────────────────────────────────────────────────────────┐
│                    AMQP Model                                │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐            │
│  │ Producer │────▶│ Exchange │────▶│  Queue   │            │
│  └──────────┘     └──────────┘     └──────────┘            │
│                           │             │                    │
│                           │ Binding     │                    │
│                           └─────────────┘                    │
│                                         │                    │
│                                         ▼                    │
│                                    ┌──────────┐            │
│                                    │ Consumer │            │
│                                    └──────────┘            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### AMQP Concepts

| Concept | Description |
|---------|-------------|
| Connection | TCP connection between client and broker |
| Channel | Virtual connection within a connection |
| Exchange | Message routing logic |
| Queue | Message storage |
| Binding | Links exchange to queue |
| Routing Key | Message routing criteria |

---

## RabbitMQ Architecture

### Cluster Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    RabbitMQ Cluster                           │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Node 1     │  │   Node 2     │  │   Node 3     │      │
│  │  (Disc)      │◀─▶│  (Disc)      │◀─▶│  (RAM)       │      │
│  │              │  │              │  │              │      │
│  │ ┌──────────┐ │  │ ┌──────────┐ │  │ ┌──────────┐ │      │
│  │ │ Queue A  │ │  │ │ Queue A  │ │  │ │ Queue A  │ │      │
│  │ │ (Master) │ │  │ │ (Mirror) │ │  │ │ (Mirror) │ │      │
│  │ └──────────┘ │  │ └──────────┘ │  │ └──────────┘ │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Component Roles

| Component | Responsibility |
|-----------|---------------|
| Node | Single RabbitMQ instance |
| Erlang Cookie | Cluster authentication |
| Mnesia | Database for metadata |
| Queue Master | Handles queue operations |
| Queue Mirror | Replica for HA |

---

## Exchanges

### Exchange Types

```
┌─────────────────────────────────────────────────────────────┐
│                    Exchange Types                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Direct: Match routing key exactly                           │
│  Fanout: Broadcast to all bound queues                       │
│  Topic: Pattern matching on routing key                      │
│  Headers: Match message headers                              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Direct Exchange

```
Exchange: orders (direct)
Binding: orders → order-queue (routing key: order)
Binding: orders → payment-queue (routing key: payment)

Message (routing key: order) → order-queue
Message (routing key: payment) → payment-queue
Message (routing key: unknown) → discarded
```

### Fanout Exchange

```
Exchange: notifications (fanout)
Binding: notifications → email-queue
Binding: notifications → sms-queue
Binding: notifications → push-queue

Message → email-queue, sms-queue, push-queue (all bound queues)
```

### Topic Exchange

```
Exchange: logs (topic)
Binding: logs → error-queue (routing key: *.error)
Binding: logs → info-queue (routing key: *.info)
Binding: logs → all-queue (routing key: #)

Message (routing key: user.error) → error-queue
Message (routing key: order.info) → info-queue
Message (routing key: payment.error) → error-queue
```

### Headers Exchange

```
Exchange: events (headers)
Binding: events → queue1 (headers: {format: json, type: user})
Binding: events → queue2 (headers: {format: xml})

Message (headers: {format: json, type: user}) → queue1
Message (headers: {format: xml}) → queue2
```

---

## Queues

### Queue Properties

```
Queue Properties:
├── name: queue name
├── durable: survives broker restart
├── exclusive: used by single connection, deleted on close
├── auto-delete: deleted when last consumer unsubscribes
└── arguments: additional properties (TTL, max-length, etc.)
```

### Queue Types

| Type | Description |
|------|-------------|
| Classic | Traditional queue, mirrored for HA |
| Quorum | Raft-based replicated queue |
| Stream | Append-only log, non-destructive reads |

### Queue Configuration

```javascript
// Node.js (amqplib)
channel.assertQueue('orders', {
  durable: true,
  exclusive: false,
  autoDelete: false,
  arguments: {
    'x-message-ttl': 86400000,      // 24 hours
    'x-max-length': 1000000,         // Max messages
    'x-dead-letter-exchange': 'dlx', // DLX exchange
    'x-dead-letter-routing-key': 'dead-letters'
  }
});
```

### Message Properties

```javascript
channel.sendToQueue('orders', Buffer.from(JSON.stringify({
  id: 'order-123',
  amount: 99.99,
  currency: 'USD'
})), {
  persistent: true,                    // Survive broker restart
  messageId: 'msg-001',
  timestamp: Date.now(),
  contentType: 'application/json',
  headers: {
    'x-retry-count': 0,
    'x-priority': 1
  }
});
```

---

## Bindings

### Binding Concepts

```
Exchange ←── Binding ──▶ Queue

Binding Properties:
├── exchange: source exchange
├── queue: destination queue
├── routingKey: routing criteria
└── arguments: additional binding arguments
```

### Binding Patterns

```
Direct Exchange:
Binding key: "order"
Message routing key: "order" → Match
Message routing key: "orders" → No match

Topic Exchange:
Binding key: "*.error"
Message routing key: "user.error" → Match
Message routing key: "order.info" → No match

Binding key: "#.error"
Message routing key: "user.login.error" → Match
```

### Binding Operations

```javascript
// Create binding
await channel.bindQueue('order-queue', 'orders', 'order');

// Delete binding
await channel.unbindQueue('order-queue', 'orders', 'order');

// List bindings
const bindings = await channel.getBindings('orders');
```

---

## Message Flow

### Publish Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Message Publish Flow                       │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1. Producer connects to broker                              │
│     └── Opens channel                                        │
│                                                              │
│  2. Producer declares exchange (if needed)                   │
│     └── Exchange type, durable, etc.                        │
│                                                              │
│  3. Producer declares queue (if needed)                      │
│     └── Queue name, durable, etc.                           │
│                                                              │
│  4. Producer creates binding                                 │
│     └── Exchange → Queue with routing key                   │
│                                                              │
│  5. Producer publishes message                               │
│     └── Message + routing key to exchange                   │
│                                                              │
│  6. Exchange routes message                                  │
│     └── Based on type and routing key                       │
│                                                              │
│  7. Queue stores message                                     │
│     └── If persistent, writes to disk                       │
│                                                              │
│  8. Consumer receives message                                │
│     └── Delivered via push or pull                          │
│                                                              │
│  9. Consumer acknowledges message                            │
│     └── Removes from queue                                  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Consumer Flow

```
Consumer Operations:
1. Connect to broker
2. Open channel
3. Set prefetch count (QoS)
4. Consume from queue
5. Process message
6. Acknowledge or reject message
7. Handle errors
8. Close connection
```

---

## Getting Started

### Docker Setup

```yaml
# docker-compose.yml
version: '3'
services:
  rabbitmq:
    image: rabbitmq:3.12-management
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
```

### Basic Producer (Node.js)

```javascript
const amqplib = require('amqplib');

async function produce() {
  const connection = await amqplib.connect('amqp://localhost');
  const channel = await connection.createChannel();
  
  await channel.assertExchange('orders', 'direct', { durable: true });
  
  const message = { id: 'order-123', amount: 99.99 };
  channel.publish('orders', 'order', Buffer.from(JSON.stringify(message)), {
    persistent: true
  });
  
  console.log('Message sent');
  await connection.close();
}

produce();
```

### Basic Consumer (Node.js)

```javascript
const amqplib = require('amqplib');

async function consume() {
  const connection = await amqplib.connect('amqp://localhost');
  const channel = await connection.createChannel();
  
  await channel.assertExchange('orders', 'direct', { durable: true });
  await channel.assertQueue('order-queue', { durable: true });
  await channel.bindQueue('order-queue', 'orders', 'order');
  
  channel.prefetch(1);
  
  channel.consume('order-queue', async (msg) => {
    const order = JSON.parse(msg.content.toString());
    console.log('Received order:', order);
    
    // Process order
    await processOrder(order);
    
    channel.ack(msg);
  });
  
  console.log('Waiting for messages...');
}

consume();
```

---

## Best Practices

### Connection Management

1. **Use connection pooling** - Reuse connections
2. **Handle connection failures** - Implement reconnection logic
3. **Limit connections** - Set appropriate limits
4. **Use channels** - Multiplex connections

### Queue Design

1. **Use durable queues** - Survive broker restarts
2. **Set message TTL** - Prevent queue buildup
3. **Use dead letter queues** - Handle failed messages
4. **Monitor queue length** - Prevent memory issues

### Message Design

1. **Keep messages small** - Reduce network overhead
2. **Use persistent messages** - For reliable delivery
3. **Include message IDs** - Enable deduplication
4. **Set appropriate headers** - Add metadata

### Reliability

1. **Use publisher confirms** - Ensure message published
2. **Use consumer acknowledgments** - Ensure message processed
3. **Implement retry logic** - Handle transient failures
4. **Monitor consumer lag** - Track processing progress

### Performance

1. **Batch messages** - Reduce overhead
2. **Use appropriate prefetch** - Balance throughput vs latency
3. **Monitor memory usage** - Prevent OOM
4. **Use multiple channels** - Parallel processing

---

## Further Reading

- [RabbitMQ Documentation](https://www.rabbitmq.com/docs)
- [AMQP 0-9-1 Specification](https://www.rabbitmq.com/resources/specifications/amqp0-9-1.pdf)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
