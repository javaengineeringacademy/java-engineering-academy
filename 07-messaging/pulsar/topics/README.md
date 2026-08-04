# Pulsar Topics

## Persistent/Non-Persistent Topics, Partitions, and Subscriptions

---

## Table of Contents

- [Overview](#overview)
- [Topic Types](#topic-types)
- [Topic Partitions](#topic-partitions)
- [Subscriptions](#subscriptions)
- [Topic Configuration](#topic-configuration)
- [Topic Operations](#topic-operations)
- [Best Practices](#best-practices)

---

## Overview

Topics in Pulsar are the fundamental unit of message organization. They support persistent and non-persistent storage, partitioning for parallelism, and multiple subscription types.

### Topic Structure

```
Topic Name:
persistent://tenant/namespace/topic

Components:
├── persistence: persistent or non-persistent
├── tenant: Tenant name
├── namespace: Namespace name
└── topic: Topic name

Example:
persistent://my-tenant/my-namespace/orders
```

---

## Topic Types

### Persistent Topics

```
Persistent Topic:
┌─────────────────────────────────────────────────────────────┐
│                    Persistent Topic                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐            │
│  │ Producer │────▶│  Broker  │────▶│BookKeeper│            │
│  └──────────┘     └──────────┘     └──────────┘            │
│                          │             │                     │
│                          │             ▼                     │
│                          │     ┌──────────────┐            │
│                          │     │  Ledger      │            │
│                          │     │  (Durable)   │            │
│                          │     └──────────────┘            │
│                          │                                  │
│                          ▼                                  │
│                    ┌──────────┐                            │
│                    │ Consumer │                            │
│                    └──────────┘                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘

Properties:
- Messages stored in BookKeeper
- Survive broker restart
- Configurable retention
- Support for deduplication
```

### Non-Persistent Topics

```
Non-Persistent Topic:
┌─────────────────────────────────────────────────────────────┐
│                    Non-Persistent Topic                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐            │
│  │ Producer │────▶│  Broker  │────▶│ Consumer │            │
│  └──────────┘     └──────────┘     └──────────┘            │
│                          │                                  │
│                          │ No storage                       │
│                          │                                  │
│                    ┌──────────┐                            │
│                    │  Memory  │                            │
│                    └──────────┘                            │
│                                                              │
└─────────────────────────────────────────────────────────────┘

Properties:
- Messages not stored
- Lost on broker restart
- Higher throughput
- Lower latency
```

### Topic Type Comparison

| Feature | Persistent | Non-Persistent |
|---------|------------|----------------|
| Storage | BookKeeper | Memory only |
| Durability | Yes | No |
| Retention | Configurable | None |
| Throughput | High | Very High |
| Latency | Low | Very Low |

---

## Topic Partitions

### Partitioned Topics

```
Partitioned Topic (3 partitions):
┌─────────────────────────────────────────────────────────────┐
│                    Topic: orders                             │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Partition 0  │  │ Partition 1  │  │ Partition 2  │      │
│  │              │  │              │  │              │      │
│  │ ┌──────────┐ │  │ ┌──────────┐ │  │ ┌──────────┐ │      │
│  │ │Messages  │ │  │ │Messages  │ │  │ │Messages  │ │      │
│  │ └──────────┘ │  │ └──────────┘ │  │ └──────────┘ │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                              │
└─────────────────────────────────────────────────────────────┘

Properties:
- Each partition is an independent topic
- Messages routed by key or round-robin
- Parallel consumption possible
- Ordering per partition
```

### Partition Assignment

```
Producer with Key Routing:
Key: "user-123" → Partition 0
Key: "user-456" → Partition 1
Key: "user-789" → Partition 2
Key: "user-123" → Partition 0 (same key = same partition)

Round-Robin (no key):
Message 1 → Partition 0
Message 2 → Partition 1
Message 3 → Partition 2
Message 4 → Partition 0
```

### Partition Configuration

```bash
# Create partitioned topic
pulsar-admin topics create-partitioned-topic \
  persistent://my-tenant/my-namespace/orders \
  --partitions 3

# Get partition metadata
pulsar-admin topics get-partitioned-topic-metadata \
  persistent://my-tenant/my-namespace/orders

# Update partition count
pulsar-admin topics update-partitioned-topic \
  persistent://my-tenant/my-namespace/orders \
  --partitions 6
```

---

## Subscriptions

### Subscription Types

#### Exclusive Subscription

```
Exclusive:
Only one consumer can subscribe at a time

Consumer 1 (Active) ──┐
                      ▼
              ┌──────────────┐
              │  Subscription │
              └──────────────┘
                      │
                      ▼
              ┌──────────────┐
              │     Topic    │
              └──────────────┘

Properties:
- Single consumer
- Ordering guaranteed
- Failover to another consumer on failure
```

#### Shared Subscription

```
Shared:
Messages distributed across consumers

Consumer 1 ──┐
             ▼
Consumer 2 ──┼──▶ ┌──────────────┐
             ▼    │  Subscription │
Consumer 3 ──┘    └──────────────┘
                       │
                       ▼
                 ┌──────────────┐
                 │     Topic    │
                 └──────────────┘

Properties:
- Multiple consumers
- Load balancing
- No ordering guarantee
- At-least-once delivery
```

#### Failover Subscription

```
Failover:
Active/standby consumer setup

Consumer 1 (Active) ──┐
                      ▼
Consumer 2 (Standby) ─┼──▶ ┌──────────────┐
                      ▼    │  Subscription │
Consumer 3 (Standby) ─┘    └──────────────┘
                                  │
                                  ▼
                            ┌──────────────┐
                            │     Topic    │
                            └──────────────┘

Properties:
- Active/standby setup
- Failover on consumer failure
- Ordering guaranteed
- No load balancing
```

#### Key_Shared Subscription

```
Key_Shared:
Messages with same key go to same consumer

Key: user-123 ──┐
                ▼
Key: user-123 ──┼──▶ Consumer 1

Key: user-456 ──┐
                ▼
Key: user-456 ──┼──▶ Consumer 2

Key: user-789 ──┐
                ▼
Key: user-789 ──┼──▶ Consumer 3

Properties:
- Key-based routing
- Ordering per key
- Load balancing across keys
```

### Subscription Configuration

```bash
# Create subscription
pulsar-admin topics create-subscription \
  persistent://my-tenant/my-namespace/orders \
  --subscription my-sub

# Subscribe with type
pulsar-admin topics subscribe \
  persistent://my-tenant/my-namespace/orders \
  --subscription my-sub \
  --subscription-type Shared

# Get subscription stats
pulsar-admin topics stats-internal \
  persistent://my-tenant/my-namespace/orders
```

---

## Topic Configuration

### Retention Settings

```bash
# Set retention
pulsar-admin topics set-retention \
  persistent://my-tenant/my-namespace/orders \
  --size 1G \
  --time 24h

# Get retention
pulsar-admin topics get-retention \
  persistent://my-tenant/my-namespace/orders
```

### Backlog Settings

```bash
# Set backlog quota
pulsar-admin topics set-backlog-quota \
  persistent://my-tenant/my-namespace/orders \
  --limit 10G \
  --retention-time 24h

# Get backlog quota
pulsar-admin topics get-backlog-quota \
  persistent://my-tenant/my-namespace/orders
```

### Deduplication

```bash
# Enable deduplication
pulsar-admin topics enable-deduplication \
  persistent://my-tenant/my-namespace/orders

# Get deduplication status
pulsar-admin topics get-deduplication \
  persistent://my-tenant/my-namespace/orders
```

---

## Topic Operations

### Create Topic

```bash
# Create persistent topic
pulsar-admin topics create persistent://my-tenant/my-namespace/orders

# Create non-persistent topic
pulsar-admin topics create non-persistent://my-tenant/my-namespace/orders

# Create partitioned topic
pulsar-admin topics create-partitioned-topic \
  persistent://my-tenant/my-namespace/orders \
  --partitions 3
```

### Delete Topic

```bash
# Delete topic
pulsar-admin topics delete persistent://my-tenant/my-namespace/orders

# Delete partitioned topic
pulsar-admin topics delete-partitioned-topic \
  persistent://my-tenant/my-namespace/orders
```

### Get Topic Info

```bash
# Get topic stats
pulsar-admin topics stats persistent://my-tenant/my-namespace/orders

# Get topic info
pulsar-admin topics info persistent://my-tenant/my-namespace/orders

# Get internal stats
pulsar-admin topics stats-internal persistent://my-tenant/my-namespace/orders
```

### Manage Messages

```bash
# Peek messages
pulsar-admin topics peek-messages \
  persistent://my-tenant/my-namespace/orders \
  --subscription my-sub \
  --count 10

# Skip messages
pulsar-admin topics skip \
  persistent://my-tenant/my-namespace/orders \
  --subscription my-sub \
  --count 10

# Reset cursor
pulsar-admin topics reset-cursor \
  persistent://my-tenant/my-namespace/orders \
  --subscription my-sub \
  --time 1h
```

---

## Best Practices

### Topic Design

1. **Use namespaces** - Organize topics logically
2. **Plan partitioning** - Use appropriate partition count
3. **Set retention** - Configure based on requirements
4. **Use deduplication** - Prevent duplicate messages

### Partition Strategy

1. **Use key_shared** - For key-based ordering
2. **Balance partitions** - Distribute load evenly
3. **Plan for growth** - Add partitions as needed
4. **Monitor partition health** - Track lag and throughput

### Subscription Management

1. **Choose appropriate type** - Match to use case
2. **Monitor consumer lag** - Track processing progress
3. **Handle failover** - Test failover scenarios
4. **Use exclusive for ordering** - When ordering required

### Performance

1. **Use batching** - Batch messages for throughput
2. **Tune prefetch** - Balance throughput vs latency
3. **Use compression** - Reduce network overhead
4. **Monitor BookKeeper** - Track ledger health

---

## Further Reading

- [Pulsar Topics](https://pulsar.apache.org/docs/concepts-topics/)
- [Pulsar Subscriptions](https://pulsar.apache.org/docs/concepts-messaging/)
- [Pulsar Administration](https://pulsar.apache.org/admin-api/)
