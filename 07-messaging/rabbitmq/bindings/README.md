# RabbitMQ Bindings

## Binding Patterns, Routing Keys, and Message Routing

---

## Table of Contents

- [Overview](#overview)
- [Binding Concepts](#binding-concepts)
- [Binding Patterns](#binding-patterns)
- [Routing Keys](#routing-keys)
- [Binding Operations](#binding-operations)
- [Advanced Routing](#advanced-routing)
- [Best Practices](#best-practices)

---

## Overview

Bindings link exchanges to queues, defining how messages are routed. Understanding binding patterns is essential for designing effective message routing.

### Binding Concept

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│ Exchange │────▶│ Binding  │────▶│  Queue   │
└──────────┘     └──────────┘     └──────────┘
                       │
                       │ Routing Key
                       │ Pattern
                       └─────────────▶ ┌──────────┐
                                       │  Queue   │
                                       └──────────┘
```

---

## Binding Concepts

### Binding Properties

| Property | Description |
|----------|-------------|
| Exchange | Source exchange name |
| Queue | Destination queue name |
| Routing Key | Routing criteria |
| Arguments | Additional binding arguments |

### Binding Types

| Type | Description |
|------|-------------|
| Direct | Exact routing key match |
| Topic | Pattern matching on routing key |
| Fanout | No routing key required |
| Headers | Header-based routing |

---

## Binding Patterns

### Direct Exchange Bindings

```
Exchange: orders (direct)

Bindings:
orders → order-queue (routing key: "order")
orders → payment-queue (routing key: "payment")
orders → shipping-queue (routing key: "shipping")

Message routing:
(routing key: "order") → order-queue
(routing key: "payment") → payment-queue
(routing key: "shipping") → shipping-queue
(routing key: "unknown") → discarded
```

### Topic Exchange Bindings

```
Exchange: logs (topic)

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

### Fanout Exchange Bindings

```
Exchange: notifications (fanout)

Bindings:
notifications → email-queue
notifications → sms-queue
notifications → push-queue

Message routing:
Message → email-queue, sms-queue, push-queue (all bound queues)
```

### Headers Exchange Bindings

```
Exchange: events (headers)

Bindings:
events → queue1 (headers: {format: json, type: user})
events → queue2 (headers: {format: xml})
events → queue3 (headers: {x-match: all, type: payment})

Message routing:
(headers: {format: json, type: user}) → queue1
(headers: {format: xml}) → queue2
(headers: {type: payment, format: json}) → queue3 (if x-match: all)
```

---

## Routing Keys

### Routing Key Format

```
Direct Exchange:
- Exact match: "order"
- Case sensitive: "Order" ≠ "order"

Topic Exchange:
- Words separated by dots: "user.error"
- * matches one word: "*.error"
- # matches zero or more words: "user.#"

Headers Exchange:
- Not used for routing
- Headers used instead
```

### Routing Key Examples

```javascript
// Direct exchange
channel.publish('orders', 'order', Buffer.from('Order data'));
channel.publish('orders', 'payment', Buffer.from('Payment data'));

// Topic exchange
channel.publish('logs', 'user.error', Buffer.from('Error data'));
channel.publish('logs', 'order.info', Buffer.from('Info data'));
channel.publish('logs', 'payment.error', Buffer.from('Error data'));
```

### Routing Key Patterns

| Pattern | Matches | Example |
|---------|---------|---------|
| `order` | Exactly "order" | "order" ✓, "orders" ✗ |
| `*.error` | Any one word + ".error" | "user.error" ✓, "order.info" ✗ |
| `user.*` | "user." + any one word | "user.login" ✓, "user.logout" ✓ |
| `#` | Zero or more words | "user" ✓, "user.login" ✓, "a.b.c" ✓ |
| `user.#` | "user." + any words | "user" ✓, "user.login" ✓, "user.a.b" ✓ |

---

## Binding Operations

### Create Binding

```javascript
// Direct exchange binding
await channel.bindQueue('order-queue', 'orders', 'order');

// Topic exchange binding
await channel.bindQueue('error-queue', 'logs', '*.error');

// Headers exchange binding
await channel.bindQueue('json-queue', 'events', '', {
  'x-match': 'all',
  'format': 'json'
});
```

### Delete Binding

```javascript
// Delete binding
await channel.unbindQueue('order-queue', 'orders', 'order');
```

### List Bindings

```javascript
// List bindings for exchange
const bindings = await channel.getBindings('orders');
console.log(bindings);
```

### Binding Arguments

```javascript
// Binding with arguments
await channel.bindQueue('priority-queue', 'events', '', {
  'x-priority': 10,
  'x-argument': 'value'
});
```

---

## Advanced Routing

### Multi-Bindings

```
Exchange: events (topic)

Multiple bindings to same queue:
events → user-queue (routing key: "user.*")
events → user-queue (routing key: "admin.*")

Result:
(user.login) → user-queue
(admin.login) → user-queue
```

### Dead Letter Bindings

```
Exchange: orders (direct)
Binding: orders → order-queue (routing key: "order")

Dead Letter Exchange: dlx (direct)
Binding: dlx → dead-letter-queue (routing key: "dead")

When message expires or is rejected:
order-queue → dlx → dead-letter-queue
```

### Priority Bindings

```
Exchange: tasks (direct)

Bindings:
tasks → high-priority-queue (routing key: "high")
tasks → medium-priority-queue (routing key: "medium")
tasks → low-priority-queue (routing key: "low")

Message routing based on priority:
(routing key: "high") → high-priority-queue
(routing key: "medium") → medium-priority-queue
(routing key: "low") → low-priority-queue
```

---

## Best Practices

### Binding Design

1. **Use appropriate exchange type** - Match routing needs
2. **Keep routing keys simple** - Easy to understand
3. **Document binding logic** - Maintain clarity
4. **Test routing patterns** - Verify behavior

### Routing Keys

1. **Use consistent naming** - Follow conventions
2. **Avoid special characters** - Keep simple
3. **Use hierarchical naming** - For topic exchanges
4. **Document patterns** - Maintain documentation

### Performance

1. **Minimize binding count** - Reduce overhead
2. **Use direct exchanges** - When possible
3. **Monitor binding metrics** - Track performance
4. **Balance routing complexity** - Avoid over-engineering

### Reliability

1. **Handle unroutable messages** - Use alternate exchanges
2. **Implement dead letter queues** - Capture failed messages
3. **Monitor routing failures** - Track issues
4. **Test failover scenarios** - Verify recovery

---

## Further Reading

- [RabbitMQ Bindings](https://www.rabbitmq.com/tutorials/amqp-concepts.html#bindings)
- [Exchange Types](https://www.rabbitmq.com/tutorials/tutorial-three-python.html)
- [Topic Exchange](https://www.rabbitmq.com/tutorials/tutorial-five-python.html)
