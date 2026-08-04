# RabbitMQ Exchanges

## Direct, Fanout, Topic, and Headers Exchanges

---

## Table of Contents

- [Overview](#overview)
- [Exchange Types](#exchange-types)
- [Direct Exchange](#direct-exchange)
- [Fanout Exchange](#fanout-exchange)
- [Topic Exchange](#topic-exchange)
- [Headers Exchange](#headers-exchange)
- [Default Exchange](#default-exchange)
- [Exchange Operations](#exchange-operations)
- [Best Practices](#best-practices)

---

## Overview

Exchanges are message routing logic in RabbitMQ. Producers send messages to exchanges, which then route them to queues based on bindings and routing keys.

### Exchange Concept

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│ Producer │────▶│ Exchange │────▶│  Queue   │
└──────────┘     └──────────┘     └──────────┘
                       │
                       │ Binding
                       │ (routing key)
                       └──────────▶ ┌──────────┐
                                    │  Queue   │
                                    └──────────┘
```

### Exchange Properties

| Property | Description |
|----------|-------------|
| Name | Exchange identifier |
| Type | Exchange type (direct, fanout, topic, headers) |
| Durable | Survives broker restart |
| Auto-delete | Deleted when last queue unbound |
| Arguments | Additional properties |

---

## Exchange Types

### Exchange Type Comparison

| Type | Routing Logic | Use Case |
|------|--------------|----------|
| Direct | Exact match on routing key | Work distribution |
| Fanout | Broadcast to all queues | Notifications |
| Topic | Pattern matching | Log routing |
| Headers | Header attribute matching | Complex routing |

---

## Direct Exchange

### Direct Exchange Behavior

```
Exchange: orders (type: direct)

Bindings:
orders → order-queue (routing key: "order")
orders → payment-queue (routing key: "payment")
orders → notification-queue (routing key: "notification")

Message routing:
(routing key: "order") → order-queue
(routing key: "payment") → payment-queue
(routing key: "notification") → notification-queue
(routing key: "unknown") → discarded
```

### Direct Exchange Configuration

```javascript
// Declare exchange
await channel.assertExchange('orders', 'direct', {
  durable: true,
  autoDelete: false
});

// Bind queue
await channel.bindQueue('order-queue', 'orders', 'order');
await channel.bindQueue('payment-queue', 'orders', 'payment');

// Publish message
channel.publish('orders', 'order', Buffer.from('Order data'));
channel.publish('orders', 'payment', Buffer.from('Payment data'));
```

### Direct Exchange Use Cases

- Work distribution among workers
- Task routing to specific handlers
- Priority-based routing
- Service-specific message routing

---

## Fanout Exchange

### Fanout Exchange Behavior

```
Exchange: notifications (type: fanout)

Bindings:
notifications → email-queue
notifications → sms-queue
notifications → push-queue

Message routing:
Message → email-queue, sms-queue, push-queue (all bound queues)
```

### Fanout Exchange Configuration

```javascript
// Declare exchange
await channel.assertExchange('notifications', 'fanout', {
  durable: true
});

// Bind queues
await channel.bindQueue('email-queue', 'notifications', '');
await channel.bindQueue('sms-queue', 'notifications', '');
await channel.bindQueue('push-queue', 'notifications', '');

// Publish message (routing key ignored)
channel.publish('notifications', '', Buffer.from('Notification data'));
```

### Fanout Exchange Use Cases

- Broadcasting events to multiple consumers
- Real-time notifications
- Log distribution
- Cache invalidation

---

## Topic Exchange

### Topic Exchange Behavior

```
Exchange: logs (type: topic)

Bindings:
logs → error-queue (routing key: "*.error")
logs → info-queue (routing key: "*.info")
logs → user-queue (routing key: "user.*")
logs → all-queue (routing key: "#")

Message routing:
(user.error) → error-queue, all-queue
(order.info) → info-queue, all-queue
(payment.error) → error-queue, all-queue
(user.login.info) → info-queue, user-queue, all-queue
```

### Pattern Matching

| Pattern | Description | Example |
|---------|-------------|---------|
| `*` | Match one word | `*.error` matches `user.error` |
| `#` | Match zero or more words | `user.#` matches `user.login.error` |
| Exact | Match exactly | `user.error` matches only `user.error` |

### Topic Exchange Configuration

```javascript
// Declare exchange
await channel.assertExchange('logs', 'topic', {
  durable: true
});

// Bind queues with patterns
await channel.bindQueue('error-queue', 'logs', '*.error');
await channel.bindQueue('info-queue', 'logs', '*.info');
await channel.bindQueue('user-queue', 'logs', 'user.*');
await channel.bindQueue('all-queue', 'logs', '#');

// Publish messages
channel.publish('logs', 'user.error', Buffer.from('User error'));
channel.publish('logs', 'order.info', Buffer.from('Order info'));
channel.publish('logs', 'payment.error', Buffer.from('Payment error'));
```

### Topic Exchange Use Cases

- Log routing by severity and component
- Event routing by category and type
- Multi-level routing hierarchies
- Flexible message filtering

---

## Headers Exchange

### Headers Exchange Behavior

```
Exchange: events (type: headers)

Bindings:
events → queue1 (headers: {format: json, type: user})
events → queue2 (headers: {format: xml})
events → queue3 (headers: {x-match: all, type: payment})

Message routing:
(headers: {format: json, type: user}) → queue1
(headers: {format: xml}) → queue2
(headers: {type: payment, format: json}) → queue3 (if x-match: all)
```

### Headers Matching

| Match Type | Description |
|------------|-------------|
| `x-match: all` | All headers must match |
| `x-match: any` | Any header can match |

### Headers Exchange Configuration

```javascript
// Declare exchange
await channel.assertExchange('events', 'headers', {
  durable: true
});

// Bind queues with header matching
await channel.bindQueue('json-user-queue', 'events', '', {
  'x-match': 'all',
  'format': 'json',
  'type': 'user'
});

await channel.bindQueue('xml-queue', 'events', '', {
  'x-match': 'all',
  'format': 'xml'
});

await channel.bindQueue('payment-queue', 'events', '', {
  'x-match': 'any',
  'type': 'payment'
});

// Publish messages
channel.publish('events', '', Buffer.from('Data'), {
  headers: {
    'format': 'json',
    'type': 'user'
  }
});
```

### Headers Exchange Use Cases

- Complex routing based on multiple attributes
- Content-based routing
- Priority-based routing
- Metadata-driven routing

---

## Default Exchange

### Default Exchange Behavior

```
Default Exchange (nameless):
- Every queue is automatically bound with queue name as routing key
- Direct exchange behavior

Example:
Queue: "order-queue"
Binding: "" → "order-queue" (routing key: "order-queue")

Message (routing key: "order-queue") → "order-queue"
```

### Default Exchange Usage

```javascript
// No exchange declaration needed
await channel.assertQueue('order-queue', { durable: true });

// Publish to default exchange with queue name as routing key
channel.publish('', 'order-queue', Buffer.from('Order data'));
```

---

## Exchange Operations

### Declare Exchange

```javascript
await channel.assertExchange('orders', 'direct', {
  durable: true,
  autoDelete: false,
  arguments: {
    'x-delayed-type': 'topic'  // For delayed message exchange plugin
  }
});
```

### Delete Exchange

```javascript
await channel.deleteExchange('orders');
```

### List Exchanges

```javascript
const exchanges = await channel.getExchange('orders');
console.log(exchanges);
```

### Exchange Properties

```javascript
await channel.checkExchange('orders');
```

---

## Best Practices

### Exchange Selection

1. **Use direct exchange** for work distribution
2. **Use fanout exchange** for broadcasting
3. **Use topic exchange** for flexible routing
4. **Use headers exchange** for complex routing

### Exchange Configuration

1. **Make exchanges durable** - Survive broker restarts
2. **Use meaningful names** - Follow naming conventions
3. **Document routing logic** - Maintain routing documentation
4. **Test routing patterns** - Verify routing behavior

### Performance

1. **Minimize exchange count** - Reduce overhead
2. **Use appropriate exchange type** - Match routing needs
3. **Monitor exchange metrics** - Track message rates
4. **Balance routing complexity** - Avoid over-engineering

### Reliability

1. **Use publisher confirms** - Ensure message published
2. **Handle routing failures** - Implement error handling
3. **Use dead letter exchanges** - Capture unroutable messages
4. **Monitor unroutable messages** - Track routing failures

---

## Further Reading

- [RabbitMQ Exchanges](https://www.rabbitmq.com/tutorials/amqp-concepts.html#exchanges)
- [Exchange Types](https://www.rabbitmq.com/tutorials/tutorial-three-python.html)
- [Topic Exchange](https://www.rabbitmq.com/tutorials/tutorial-five-python.html)
