# RabbitMQ Clustering

## Clustering, Mirroring, Quorum Queues, and High Availability

---

## Table of Contents

- [Overview](#overview)
- [Cluster Architecture](#cluster-architecture)
- [Node Types](#node-types)
- [Mirrored Queues](#mirrored-queues)
- [Quorum Queues](#quorum-queues)
- [Cluster Management](#cluster-management)
- [Best Practices](#best-practices)

---

## Overview

RabbitMQ clustering provides high availability, fault tolerance, and horizontal scaling. Clustering enables message replication across multiple nodes.

### Key Features

- **High Availability**: Failover to other nodes
- **Message Replication**: Mirror messages across nodes
- **Load Balancing**: Distribute load across nodes
- **Fault Tolerance**: Continue operating during failures

---

## Cluster Architecture

### Cluster Topology

```
┌─────────────────────────────────────────────────────────────┐
│                    RabbitMQ Cluster                           │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Node 1     │  │   Node 2     │  │   Node 3     │      │
│  │   (Disc)     │◀─▶│   (Disc)     │◀─▶│   (RAM)      │      │
│  │              │  │              │  │              │      │
│  │ ┌──────────┐ │  │ ┌──────────┐ │  │ ┌──────────┐ │      │
│  │ │ Queue A  │ │  │ │ Queue A  │ │  │ │ Queue A  │ │      │
│  │ │ (Master) │ │  │ │ (Mirror) │ │  │ │ (Mirror) │ │      │
│  │ └──────────┘ │  │ └──────────┘ │  │ └──────────┘ │      │
│  │              │  │              │  │              │      │
│  │ ┌──────────┐ │  │ ┌──────────┐ │  │ ┌──────────┐ │      │
│  │ │ Queue B  │ │  │ │ Queue B  │ │  │ │ Queue B  │ │      │
│  │ │ (Mirror) │ │  │ │ (Master) │ │  │ │ (Mirror) │ │      │
│  │ └──────────┘ │  │ └──────────┘ │  │ └──────────┘ │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Cluster Communication

```
Erlang Distribution Protocol:
- Port 25672 (inter-node communication)
- Uses Erlang cookie for authentication
- Gossip protocol for cluster state

Cluster Formation:
1. Node starts and contacts seed nodes
2. Joins existing cluster or forms new one
3. Synchronizes metadata (queues, exchanges, bindings)
4. Begins serving clients
```

---

## Node Types

### Disc Nodes

```
Disc Node:
- Stores metadata on disk
- Survives broker restarts
- Recommended for queue masters
- Default node type

Use Cases:
- Production clusters
- Persistent queues
- Critical data
```

### RAM Nodes

```
RAM Node:
- Stores metadata in memory
- Faster startup
- Used for temporary data
- Not recommended for queue masters

Use Cases:
- Development clusters
- Temporary queues
- Non-critical data
```

### Node Configuration

```ini
# rabbitmq.conf
# Disc node (default)
# No special configuration needed

# RAM node
# Note: Not recommended for production
```

---

## Mirrored Queues

### Mirrored Queue Concept

```
Mirrored Queue:
┌─────────────────────────────────────────────────────────────┐
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Queue Master                       │   │
│  │  ┌──────┐ ┌──────┐ ┌──────┐ ┌──────┐              │   │
│  │  │ Msg1 │ │ Msg2 │ │ Msg3 │ │ Msg4 │              │   │
│  │  └──────┘ └──────┘ └──────┘ └──────┘              │   │
│  └──────────────────────────────────────────────────────┘   │
│                         │                                    │
│                         ▼                                    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │                    Mirrors                            │   │
│  │  ┌────────────────┐  ┌────────────────┐             │   │
│  │  │ Mirror Node 2  │  │ Mirror Node 3  │             │   │
│  │  │ ┌────┐┌────┐   │  │ ┌────┐┌────┐   │             │   │
│  │  │ │M1  ││M2  │   │  │ │M1  ││M2  │   │             │   │
│  │  │ └────┘└────┘   │  │ └────┘└────┘   │             │   │
│  │  └────────────────┘  └────────────────┘             │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Mirrored Queue Configuration

```javascript
// Declare mirrored queue
await channel.assertQueue('orders', {
  durable: true,
  arguments: {
    'x-ha-mode': 'all',           // Mirror to all nodes
    'x-ha-queues': 'all',         // All queues mirrored
    'x-ha-sync-mode': 'automatic' // Automatic sync
  }
});
```

### Ha Mode Options

| Mode | Description |
|------|-------------|
| `all` | Mirror to all nodes |
| `exactly` | Mirror to specific number of nodes |
| `nodes` | Mirror to specific nodes |

### Ha Sync Mode

| Mode | Description |
|------|-------------|
| `automatic` | Automatically sync new mirrors |
| `manual` | Manually sync mirrors |

### Mirrored Queue Failover

```
Normal Operation:
Node 1: Queue Master (active)
Node 2: Queue Mirror (synced)
Node 3: Queue Mirror (synced)

Node 1 fails:
Node 2: Queue Master (active) ← Promoted
Node 3: Queue Mirror (synced)

When Node 1 recovers:
Node 1: Queue Mirror (syncing)
Node 2: Queue Master (active)
Node 3: Queue Mirror (synced)
```

---

## Quorum Queues

### Quorum Queue Concept

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
│  │  ┌────────────────┐  ┌────────────────┐             │   │
│  │  │ Follower 1     │  │ Follower 2     │             │   │
│  │  │ ┌────┐┌────┐   │  │ ┌────┐┌────┐   │             │   │
│  │  │ │M1  ││M2  │   │  │ │M1  ││M2  │   │             │   │
│  │  │ └────┘└────┘   │  │ └────┘└────┘   │             │   │
│  │  └────────────────┘  └────────────────┘             │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Quorum Queue Configuration

```javascript
// Declare quorum queue
await channel.assertQueue('orders', {
  durable: true,
  arguments: {
    'x-queue-type': 'quorum',
    'x-quorum-initial-group-size': 3  // Replication factor
  }
});
```

### Quorum Queue Features

| Feature | Description |
|---------|-------------|
| Raft Consensus | Strong consistency |
| Automatic Failover | Leader election |
| Data Safety | Majority writes required |
| Performance | High throughput |

### Quorum Queue Comparison

| Feature | Mirrored | Quorum |
|---------|----------|--------|
| Consensus | None | Raft |
| Failover | Manual | Automatic |
| Data Safety | Best effort | Majority |
| Performance | Good | High |
| Resource Usage | High | Low |

---

## Cluster Management

### Cluster Commands

```bash
# Join cluster
rabbitmqctl join_cluster rabbit@node1

# Leave cluster
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl start_app

# Cluster status
rabbitmqctl cluster_status

# Force join cluster
rabbitmqctl force_boot
```

### Cluster Formation

```bash
# Start first node
rabbitmq-server -detached

# Join second node
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl join_cluster rabbit@node1
rabbitmqctl start_app

# Join third node
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl join_cluster rabbit@node2
rabbitmqctl start_app
```

### Cluster Configuration

```ini
# rabbitmq.conf
cluster_formation.peer_discovery_backend = classic_config
cluster_formation.classic_config.nodes.1 = rabbit@node1
cluster_formation.classic_config.nodes.2 = rabbit@node2
cluster_formation.classic_config.nodes.3 = rabbit@node3
```

---

## Best Practices

### Cluster Design

1. **Use odd number of nodes** - For quorum queues
2. **Mix disc and RAM nodes** - Balance storage and performance
3. **Use quorum queues** - For better reliability
4. **Plan for capacity** - Size cluster appropriately

### High Availability

1. **Use mirrored or quorum queues** - For data replication
2. **Set appropriate replication factor** - Based on requirements
3. **Test failover scenarios** - Verify recovery
4. **Monitor cluster health** - Track node status

### Performance

1. **Balance queue masters** - Distribute load
2. **Monitor resource usage** - CPU, memory, disk
3. **Use appropriate node types** - Match workload
4. **Optimize network** - Low latency between nodes

### Operations

1. **Regular backups** - Backup cluster state
2. **Monitor metrics** - Track performance
3. **Plan upgrades** - Rolling upgrades
4. **Document procedures** - Maintain runbooks

---

## Further Reading

- [RabbitMQ Clustering](https://www.rabbitmq.com/clustering.html)
- [Quorum Queues](https://www.rabbitmq.com/quorum-queues.html)
- [Mirrored Queues](https://www.rabbitmq.com/ha.html)
