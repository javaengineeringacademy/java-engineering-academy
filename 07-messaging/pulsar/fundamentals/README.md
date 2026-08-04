# Pulsar Fundamentals

## Apache Pulsar Architecture, Brokers, Bookies, and Core Concepts

---

## Table of Contents

- [Overview](#overview)
- [Pulsar Architecture](#pulsar-architecture)
- [Brokers](#brokers)
- [Bookies (BookKeeper)](#bookies-bookkeeper)
- [Topics](#topics)
- [Subscriptions](#subscriptions)
- [Getting Started](#getting-started)
- [Best Practices](#best-practices)

---

## Overview

Apache Pulsar is a cloud-native distributed messaging and streaming platform. It separates serving (brokers) from storage (BookKeeper), enabling independent scaling and multi-tenancy.

### Key Features

- **Separation of Concerns**: Brokers for serving, BookKeeper for storage
- **Multi-Tenancy**: Built-in tenant/namespace isolation
- **Geo-Replication**: Built-in multi-datacenter support
- **Unified Messaging**: Queuing and streaming in one platform
- **Schema Evolution**: Built-in schema registry

---

## Pulsar Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Apache Pulsar Cluster                     │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Broker Layer                        │   │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │   │
│  │  │   Broker 1   │  │   Broker 2   │  │   Broker 3   │ │   │
│  │  │   (Active)   │  │   (Active)   │  │   (Active)   │ │   │
│  │  └──────────────┘  └──────────────┘  └──────────────┘ │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Storage Layer                       │   │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │   │
│  │  │   Bookie 1   │  │   Bookie 2   │  │   Bookie 3   │ │   │
│  │  │   (BookKeeper│  │   (BookKeeper│  │   (BookKeeper│ │   │
│  │  └──────────────┘  └──────────────┘  └──────────────┘ │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Metadata Store                      │   │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │   │
│  │  │   ZK/ETCD 1  │  │   ZK/ETCD 2  │  │   ZK/ETCD 3  │ │   │
│  │  └──────────────┘  └──────────────┘  └──────────────┘ │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Component Roles

| Component | Responsibility |
|-----------|---------------|
| Broker | Handles produce/consume requests, topic ownership |
| Bookie | Stores entry logs and indexes |
| Metadata Store | Stores topic metadata, configuration |
| ZooKeeper/ETCD | Distributed coordination |

---

## Brokers

### Broker Responsibilities

```
Broker:
├── Topic Ownership
│   ├── Topic assignment
│   ├── Producer management
│   └── Consumer management
├── Request Handling
│   ├── Produce requests
│   ├── Consume requests
│   └── Admin requests
├── Load Balancing
│   ├── Bundle ownership
│   ├── Topic migration
│   └── Traffic distribution
└── Schema Management
    ├── Schema storage
    ├── Schema validation
    └── Schema evolution
```

### Broker Configuration

```properties
# broker.conf
brokerServicePort=6650
webServicePort=8080
clusterName=pulsar-cluster
zookeeperServers=zk1:2181,zk2:2181,zk3:2181
bookkeeperLedgerRoot=/ledgers
numBookies=3
```

### Broker Scaling

```
Horizontal Scaling:
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Broker 1   │     │   Broker 2   │     │   Broker 3   │
│   Bundle A   │     │   Bundle B   │     │   Bundle C   │
└──────────────┘     └──────────────┘     └──────────────┘

Add Broker 4:
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Broker 1   │     │   Broker 2   │     │   Broker 3   │     │   Broker 4   │
│   Bundle A   │     │   Bundle B   │     │   Bundle C   │     │   Bundle D   │
└──────────────┘     └──────────────┘     └──────────────┘     └──────────────┘
```

---

## Bookies (BookKeeper)

### BookKeeper Architecture

```
BookKeeper:
├── Entry Log
│   ├── Append-only log
│   ├── Sequential writes
│   └── No random access
├── Index
│   ├── Ledger → entry mapping
│   └── Fast lookups
└── Journal
    ├── Write-ahead log
    └── Durability guarantee
```

### Ledger Concept

```
Ledger:
┌─────────────────────────────────────────────────────────────┐
│                    Ledger Entries                            │
│  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐            │
│  │ E1   │ │ E2   │ │ E3   │ │ E4   │ │ E5   │            │
│  └──────┘ └──────┘ └──────┘ └──────┘ └──────┘            │
│       │        │        │        │        │                 │
│       ▼        ▼        ▼        ▼        ▼                 │
│   Entry 0  Entry 1  Entry 2  Entry 3  Entry 4              │
└─────────────────────────────────────────────────────────────┘

Properties:
- Append-only
- Sequential read
- One writer at a time
- Multiple readers
```

### Bookie Configuration

```properties
# bookie.conf
bookiePort=3181
journalDir=/var/lib/bookkeeper/journal
ledgerDir=/var/lib/bookkeeper/ledgers
zkServers=zk1:2181,zk2:2181,zk3:2181
```

---

## Topics

### Topic Format

```
Topic Name:
persistent://tenant/namespace/topic

Example:
persistent://my-tenant/my-namespace/orders

Components:
├── persistent: Persistence (persistent/non-persistent)
├── my-tenant: Tenant name
├── my-namespace: Namespace name
└── orders: Topic name
```

### Topic Types

| Type | Description |
|------|-------------|
| Persistent | Messages stored in BookKeeper |
| Non-persistent | Messages not stored, memory only |

### Topic Configuration

```bash
# Create topic
pulsar-admin topics create persistent://my-tenant/my-namespace/orders

# Get topic stats
pulsar-admin topics stats persistent://my-tenant/my-namespace/orders

# Get topic info
pulsar-admin topics info persistent://my-tenant/my-namespace/orders
```

---

## Subscriptions

### Subscription Types

```
Exclusive:
┌──────────────┐
│   Consumer   │
│   (Exclusive)│
└──────────────┘
       │
       ▼
┌──────────────┐
│   Subscription│
└──────────────┘
       │
       ▼
┌──────────────┐
│     Topic    │
└──────────────┘

Shared:
┌──────────────┐
│   Consumer 1 │
└──────────────┘
       │
       ▼
┌──────────────┐
│   Consumer 2 │
└──────────────┘
       │
       ▼
┌──────────────┐
│   Subscription│
└──────────────┘
       │
       ▼
┌──────────────┐
│     Topic    │
└──────────────┘

Failover:
┌──────────────┐
│   Consumer 1 │←── Active
└──────────────┘
       │
       ▼
┌──────────────┐
│   Consumer 2 │←── Standby
└──────────────┘
       │
       ▼
┌──────────────┐
│   Subscription│
└──────────────┘
       │
       ▼
┌──────────────┐
│     Topic    │
└──────────────┘
```

### Subscription Configuration

```bash
# Create subscription
pulsar-admin topics create-subscription \
  persistent://my-tenant/my-namespace/orders \
  --subscription my-sub

# Consume from subscription
pulsar-client consume persistent://my-tenant/my-namespace/orders \
  --subscription my-sub \
  --subscription-type Shared
```

---

## Getting Started

### Docker Setup

```yaml
# docker-compose.yml
version: '3'
services:
  pulsar:
    image: apachepulsar/pulsar:3.1.0
    command: bin/pulsar standalone
    ports:
      - "6650:6650"
      - "8080:8080"
```

### Basic Producer

```java
PulsarClient client = PulsarClient.builder()
    .serviceUrl("pulsar://localhost:6650")
    .build();

Producer<String> producer = client.newProducer(Schema.STRING)
    .topic("persistent://my-tenant/my-namespace/orders")
    .create();

producer.send("Order data");
```

### Basic Consumer

```java
Consumer<String> consumer = client.newConsumer(Schema.STRING)
    .topic("persistent://my-tenant/my-namespace/orders")
    .subscriptionName("my-subscription")
    .subscribe();

Message<String> message = consumer.receive();
String payload = message.getValue();
consumer.acknowledge(message);
```

---

## Best Practices

### Topic Design

1. **Use namespaces** - Organize topics by tenant/namespace
2. **Plan partitioning** - Use key_shared for ordering
3. **Set retention** - Configure appropriate retention
4. **Use schema registry** - Enforce schema evolution

### Performance

1. **Use batching** - Batch messages for throughput
2. **Tune prefetch** - Balance throughput vs latency
3. **Use compression** - Reduce network overhead
4. **Monitor BookKeeper** - Track ledger health

### Operations

1. **Monitor broker metrics** - Track throughput and latency
2. **Monitor BookKeeper** - Track ledger health
3. **Plan capacity** - Size cluster appropriately
4. **Test failover** - Verify recovery

---

## Further Reading

- [Pulsar Documentation](https://pulsar.apache.org/docs/)
- [Pulsar Architecture](https://pulsar.apache.org/docs/concepts-architecture/)
- [Pulsar Administration](https://pulsar.apache.org/admin-api/)
