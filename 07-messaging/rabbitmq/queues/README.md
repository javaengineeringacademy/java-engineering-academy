# RabbitMQ Queues

## Queue Properties, Durable, Exclusive, Auto-Delete, and Queue Types

---

## Table of Contents

- [Overview](#overview)
- [Queue Properties](#queue-properties)
- [Queue Types](#queue-types)
- [Queue Configuration](#queue-configuration)
- [Message Properties](#message-properties)
- [Queue Management](#queue-management)
- [Best Practices](#best-practices)

---

## Overview

Queues are message storage in RabbitMQ. They hold messages until consumers are ready to process them. Understanding queue properties and types is essential for reliable messaging.

### Queue Concept

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│ Producer │────▶│ Exchange │────▶│  Queue   │
└──────────┘     └──────────┘     └──────────┘
                                         │
                                         ▼
                                    ┌──────────┐
                                    │ Consumer │
                                    └──────────┘
```

---

## Queue Properties

### Core Properties

| Property | Description |
|----------|-------------|
| name | Queue identifier |
| durable | Survives broker restart |
| exclusive | Used by single connection, deleted on close |
| auto-delete | Deleted when last consumer unsubscribes |
| arguments | Additional properties (TTL, max-length, etc.) |

### Property Combinations

| durable | exclusive | auto-delete | Use Case |
|---------|-----------|-------------|----------|
| true | false | false | Persistent work queue |
| true | false | true | Temporary queue with persistence |
| false | false | false | Non-persistent queue |
| false | true | false | Exclusive temporary queue |
| false | false | true | Auto-cleanup queue |

---

## Queue Types

### Classic Queues

```
Classic Queue:
┌─────────────────────────────────────────────────────────────┐
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Queue Master                       │   │
│  │  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐              │   │
│  │  │ Msg1 │ │ Msg2 │ │ Msg3 │ │ Msg4 │              │   │
│  │  └──────┘ └──────┘ └──────┘ └──────┘              │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  Mirrors (for HA):                                           │
│  ┌────────────────┐  ┌────────────────┐                     │
│  │ Mirror Node 2  │  │ Mirror Node 3  │                     │
│  └────────────────┘  └────────────────┘                     │
└─────────────────────────────────────────────────────────────┘
```

### Quorum Queues

```
Quorum Queue (Raft-based):
┌─────────────────────────────────────────────────────────────┐
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Leader                             │   │
│  │  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐              │   │
│  │  │ Msg1 │ │ Msg2 │ │ Msg3 │ │ Msg4 │              │   │
│  │  └──────┘ └──────┘ └──────┘ └──────┘              │   │
│  └──────────────────────────────────────────────────────┘   │
│                         │                                    │
│                         ▼                                    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Followers                          │   │
│  │  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐              │   │
│  │  │ Msg1 │ │ Msg2 │ │ Msg3 │ │ Msg4 │              │   │
│  │  └──────┘ └──────┘ └──────┘ └──────┘              │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Stream Queues

```
Stream Queue (Append-only log):
┌─────────────────────────────────────────────────────────────┐
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Stream                             │   │
│  │  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐     │   │
│  │  │ Msg1 │ │ Msg2 │ │ Msg3 │ │ Msg4 │ │ Msg5 │     │   │
│  │  └──────┘ └──────┘ └──────┘ └──────┘ └──────┘     │   │
│  │      ▲          ▲          ▲          ▲          ▲   │   │
│  │      │          │          │          │          │   │   │
│  │   Offset 0   Offset 1   Offset 2   Offset 3   Offset 4 │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Queue Type Comparison

| Feature | Classic | Quorum | Stream |
|---------|---------|--------|--------|
| Replication | Mirror | Raft | Replicated |
| Ordering | FIFO | FIFO | FIFO |
| Multiple Consumers | Yes | Yes | Yes |
| Message Acknowledgment | Yes | Yes | No |
| Message Deletion | Yes | Yes | No |
| Performance | Good | High | Very High |

---

## Queue Configuration

### Declare Queue

```javascript
// Declare queue
await channel.assertQueue('orders', {
  durable: true,
  exclusive: false,
  autoDelete: false,
  arguments: {
    'x-message-ttl': 86400000,              // 24 hours
    'x-max-length': 1000000,                // Max messages
    'x-max-length-bytes': 1073741824,       // Max size (1GB)
    'x-dead-letter-exchange': 'dlx',        // DLX exchange
    'x-dead-letter-routing-key': 'dead',    // DLX routing key
    'x-max-priority': 10                    // Priority queue
  }
});
```

### Queue Arguments

| Argument | Description |
|----------|-------------|
| `x-message-ttl` | Message time-to-live (ms) |
| `x-max-length` | Maximum number of messages |
| `x-max-length-bytes` | Maximum queue size (bytes) |
| `x-dead-letter-exchange` | Dead letter exchange |
| `x-dead-letter-routing-key` | Dead letter routing key |
| `x-max-priority` | Maximum priority level |
| `x-queue-type` | Queue type (classic, quorum, stream) |
| `x-quorum-initial-group-size` | Quorum queue replication factor |

---

## Message Properties

### Message Attributes

```javascript
channel.sendToQueue('orders', Buffer.from('Order data'), {
  persistent: true,                        // Survive broker restart
  messageId: 'msg-001',                    // Unique message ID
  correlationId: 'corr-001',              // Correlation ID
  timestamp: Date.now(),                   // Timestamp
  expiration: '86400000',                  // TTL (ms)
  type: 'order.created',                   // Message type
  contentType: 'application/json',         // Content type
  contentEncoding: 'utf-8',                // Encoding
  priority: 1,                             // Priority (0-9)
  replyTo: 'reply-queue',                  // Reply queue
  headers: {
    'x-retry-count': 0,                    // Custom header
    'x-source': 'order-service'
  }
});
```

### Message States

```
Message Lifecycle:
┌──────────┐     ┌──────────┐     ┌──────────┐
│  Ready   │────▶│Unacked   │────▶│ Ack/Rej  │
└──────────┘     └──────────┘     └──────────┘
     │               │               │
     │               │               ▼
     │               │         ┌──────────┐
     │               │         │ Deleted  │
     │               │         └──────────┘
     │               │
     │               ▼
     │         ┌──────────┐
     │         │ Requeued │
     │         └──────────┘
     │               │
     ▼               ▼
┌──────────┐     ┌──────────┐
│  Ready   │◀────│ Requeued │
└──────────┘     └──────────┘
```

---

## Queue Management

### List Queues

```bash
# RabbitMQ management API
curl -u guest:guest http://localhost:15672/api/queues

# Or using rabbitmqctl
rabbitmqctl list_queues name messages consumers memory
```

### Purge Queue

```javascript
// Purge all messages from queue
await channel.purgeQueue('orders');
```

### Delete Queue

```javascript
// Delete queue
await channel.deleteQueue('orders');
```

### Check Queue

```javascript
// Get queue info
const queue = await channel.checkQueue('orders');
console.log('Messages:', queue.messageCount);
console.log('Consumers:', queue.consumerCount);
```

---

## Best Practices

### Queue Design

1. **Use durable queues** - For reliable messaging
2. **Set message TTL** - Prevent queue buildup
3. **Use dead letter queues** - Handle failed messages
4. **Monitor queue length** - Prevent memory issues

### Queue Types

1. **Use quorum queues** - For better reliability
2. **Use streams** - For high throughput
3. **Use classic queues** - For simple use cases
4. **Consider queue type** - Based on requirements

### Configuration

1. **Set appropriate limits** - Max length, TTL
2. **Use dead letter exchanges** - Capture failed messages
3. **Configure priorities** - When needed
4. **Document queue purpose** - Maintain clarity

### Performance

1. **Balance queue count** - Too many queues overhead
2. **Monitor queue metrics** - Track performance
3. **Use prefetch wisely** - Balance throughput vs latency
4. **Consider queue type** - Match to workload

### Reliability

1. **Use persistent messages** - For critical data
2. **Implement acknowledgments** - Ensure processing
3. **Use publisher confirms** - Ensure delivery
4. **Monitor consumer lag** - Track progress

---

## Further Reading

- [RabbitMQ Queues](https://www.rabbitmq.com/tutorials/amqp-concepts.html#queues)
- [Quorum Queues](https://www.rabbitmq.com/quorum-queues.html)
- [Streams](https://www.rabbitmq.com/streams.html)
