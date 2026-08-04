# Pulsar Geo-Replication

## Geo-Replication, Multi-Datacenter, and Cross-Cluster Replication

---

## Table of Contents

- [Overview](#overview)
- [Geo-Replication Architecture](#geo-replication-architecture)
- [Replication Configuration](#replication-configuration)
- [Multi-Datacenter Setup](#multi-datacenter-setup)
- [Replication Policies](#replication-policies)
- [Best Practices](#best-practices)

---

## Overview

Pulsar provides built-in geo-replication for replicating messages across multiple datacenters. This enables disaster recovery, low-latency access, and regulatory compliance.

### Key Features

- **Built-in Replication**: No external tools needed
- **Asynchronous Replication**: Non-blocking replication
- **Selective Replication**: Choose topics to replicate
- **Multi-Datacenter**: Support for multiple clusters
- **Conflict Resolution**: Configurable conflict handling

### Use Cases

- Disaster recovery
- Low-latency access in multiple regions
- Regulatory compliance (data residency)
- Global event distribution

---

## Geo-Replication Architecture

### Replication Topology

```
┌─────────────────────────────────────────────────────────────┐
│                    Geo-Replication Topology                   │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Cluster 1    │  │ Cluster 2    │  │ Cluster 3    │      │
│  │ (US-East)    │◀─▶│ (EU-West)   │◀─▶│ (AP-South)  │      │
│  │              │  │              │  │              │      │
│  │ ┌──────────┐ │  │ ┌──────────┐ │  │ ┌──────────┐ │      │
│  │ │ Topic A  │ │  │ │ Topic A  │ │  │ │ Topic A  │ │      │
│  │ │ (Replic- │◀─▶│ │ │ (Replic- │◀─▶│ │ │ (Replic- │ │      │
│  │ │  ated)   │ │  │ │  ated)   │ │  │ │  ated)   │ │      │
│  │ └──────────┘ │  │ └──────────┘ │  │ └──────────┘ │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Replication Flow

```
Producer (Cluster 1) ──▶ Topic (Cluster 1) ──▶ Replicator ──▶ Cluster 2
                                                        │
                                                        ▼
                                                  Cluster 3

Components:
├── Producer: Publishes to local cluster
├── Replicator: Copies messages to other clusters
└── Consumer: Reads from any cluster
```

---

## Replication Configuration

### Cluster Configuration

```properties
# cluster.conf
clusterName=us-east
webServicePort=8080
brokerServicePort=6650

# Replication
replicationClusters=us-east,eu-west,ap-south
```

### Topic Configuration

```bash
# Enable replication for topic
pulsar-admin topics set-replicated-clusters \
  persistent://tenant/namespace/topic \
  --clusters us-east,eu-west,ap-south

# Get replication clusters
pulsar-admin topics get-replicated-clusters \
  persistent://tenant/namespace/topic
```

### Namespace Configuration

```bash
# Set namespace replication clusters
pulsar-admin namespaces set-clusters \
  my-tenant/my-namespace \
  --clusters us-east,eu-west,ap-south

# Get namespace replication clusters
pulsar-admin namespaces get-clusters \
  my-tenant/my-namespace
```

---

## Multi-Datacenter Setup

### Cluster Setup

```properties
# Cluster 1 (US-East)
clusterName=us-east
zkServers=zk-us-east:2181
configurationStore=zk-us-east:2181

# Cluster 2 (EU-West)
clusterName=eu-west
zkServers=zk-eu-west:2181
configurationStore=zk-eu-west:2181

# Cluster 3 (AP-South)
clusterName=ap-south
zkServers=zk-ap-south:2181
configurationStore=zk-ap-south:2181
```

### Cluster Configuration

```bash
# Create cluster
pulsar-admin clusters create us-east \
  --url http://us-east:8080 \
  --url-secure https://us-east:8443

# Update cluster
pulsar-admin clusters update us-east \
  --url http://us-east:8080

# List clusters
pulsar-admin clusters list

# Get cluster info
pulsar-admin clusters get us-east

# Delete cluster
pulsar-admin clusters delete us-east
```

### Geo-Replication Setup

```bash
# Configure replication for tenant
pulsar-admin tenants update my-tenant \
  --allowed-clusters us-east,eu-west,ap-south

# Configure replication for namespace
pulsar-admin namespaces set-clusters my-tenant/my-namespace \
  --clusters us-east,eu-west,ap-south

# Configure replication for topic
pulsar-admin topics set-replicated-clusters \
  persistent://my-tenant/my-namespace/orders \
  --clusters us-east,eu-west,ap-south
```

---

## Replication Policies

### Replication Modes

| Mode | Description |
|------|-------------|
| Async | Non-blocking replication |
| Sync | Blocking replication (not recommended) |

### Conflict Resolution

```java
// Conflict resolution strategies
ConflictResolutionStrategy strategy = ConflictResolutionStrategy.LatestTimestamp;

// Or use producer's cluster preference
ConflictResolutionStrategy strategy = ConflictResolutionStrategy.ProducerCluster;
```

### Replication Settings

```bash
# Set replication settings
pulsar-admin namespaces set-retention \
  my-tenant/my-namespace \
  --size 1G \
  --time 24h

# Set replication deduplication
pulsar-admin namespaces enable-deduplication \
  my-tenant/my-namespace
```

### Replication Monitoring

```bash
# Get replication stats
pulsar-admin topics stats \
  persistent://my-tenant/my-namespace/orders

# Get replication cursors
pulsar-admin topics get-replication-cursor \
  persistent://my-tenant/my-namespace/orders
```

---

## Best Practices

### Cluster Design

1. **Use odd number of clusters** - For quorum-based replication
2. **Plan capacity** - Size each cluster appropriately
3. **Use dedicated brokers** - Separate replication from serving
4. **Monitor replication lag** - Track replication progress

### Network

1. **Use dedicated network** - Separate replication traffic
2. **Optimize latency** - Choose regions with low latency
3. **Monitor bandwidth** - Track replication bandwidth
4. **Use compression** - Reduce network overhead

### Operations

1. **Test failover** - Verify disaster recovery
2. **Monitor health** - Track cluster health
3. **Plan capacity** - Scale clusters as needed
4. **Document procedures** - Maintain runbooks

### Security

1. **Use TLS** - Encrypt replication traffic
2. **Authenticate clusters** - Use TLS certificates
3. **Authorize access** - Control cluster access
4. **Audit changes** - Log configuration changes

---

## Further Reading

- [Pulsar Geo-Replication](https://pulsar.apache.org/docs/concepts-replication/)
- [Pulsar Multi-Datacenter](https://pulsar.apache.org/docs/admin-api-clusters/)
- [Pulsar Replication](https://pulsar.apache.org/docs/admin-geo-replication/)
