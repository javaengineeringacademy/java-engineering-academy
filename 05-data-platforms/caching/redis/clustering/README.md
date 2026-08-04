# Redis Clustering

## Comprehensive Guide to Redis Cluster and Replication

Redis provides clustering for scalability and replication for high availability. This guide covers Redis Cluster, Sentinel, and replication.

---

## Table of Contents

1. [Redis Cluster](#redis-cluster)
2. [Redis Sentinel](#redis-sentinel)
3. [Replication](#replication)
4. [Best Practices](#best-practices)

---

## Redis Cluster

### Create Cluster

```bash
# Start cluster mode
redis-server --cluster-enabled yes --cluster-config-file nodes.conf --cluster-node-timeout 5000

# Create cluster
redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 --cluster-replicas 1
```

### Cluster Operations

```bash
# Check cluster info
redis-cli cluster info

# Check cluster nodes
redis-cli cluster nodes

# Add node
redis-cli --cluster add-node 127.0.0.1:7006 127.0.0.1:7000

# Remove node
redis-cli --cluster del-node 127.0.0.1:7000 <node-id>

# Reshard
redis-cli --cluster reshard 127.0.0.1:7000

# Check cluster
redis-cli --cluster check 127.0.0.1:7000
```

### Hash Slots

```
Redis Cluster uses 16384 hash slots
Each key is mapped to a slot: CRC16(key) % 16384
Each node is responsible for a subset of slots
```

### Cluster Commands

```bash
# Get slot for key
CLUSTER KEYSLOT mykey

# Get cluster info
CLUSTER INFO

# Get cluster nodes
CLUSTER NODES

# Get cluster slots
CLUSTER SLOTS
```

---

## Redis Sentinel

### Sentinel Configuration

```conf
# sentinel.conf
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 5000
sentinel failover-timeout mymaster 10000
sentinel parallel-syncs mymaster 1
```

### Start Sentinel

```bash
redis-sentinel sentinel.conf
```

### Sentinel Commands

```bash
# Check sentinel status
redis-cli -p 26379 SENTINEL masters

# Check master
redis-cli -p 26379 SENTINEL get-master-addr-by-name mymaster

# Check replicas
redis-cli -p 26379 SENTINEL replicas mymaster

# Check sentinels
redis-cli -p 26379 SENTINEL sentinels mymaster
```

### Sentinel Operations

```bash
# Failover
redis-cli -p 26379 SENTINEL failover mymaster

# Reset
redis-cli -p 26379 SENTINEL reset mymaster

# Flush configs
redis-cli -p 26379 SENTINEL flushconfig
```

---

## Replication

### Setup Replication

```bash
# On replica
REPLICAOF 127.0.0.1 6379

# Check replication info
INFO replication

# Check replication status
redis-cli INFO replication
```

### Replication Commands

```bash
# Set replica
REPLICAOF <master-ip> <master-port>

# Make replica read-only
CONFIG SET slave-read-only yes

# Check replication
INFO replication

# Sync
BGSAVE
```

### Replication States

```
- WAITING_FOR_BULK_START: Waiting for BGSAVE to start
- SEND_BULK: Sending RDB to replica
- WAIT_BGSAVE_END: Waiting for BGSAVE to finish
- SEND_CACHE: Sending cached data
- CONNECTED: Connected and syncing
- SYNCED: Fully synced
```

---

## Best Practices

### 1. Use Cluster for Scalability

```bash
# Good - Use cluster for large datasets
redis-cli --cluster create node1:6379 node2:6379 node3:6379

# Good - Distribute data across nodes
redis-cli --cluster reshard node1:6379
```

### 2. Use Sentinel for HA

```bash
# Good - Use sentinel for failover
redis-sentinel sentinel.conf

# Good - Monitor replication
redis-cli -p 26379 SENTINEL masters
```

### 3. Use Replication for Read Scaling

```bash
# Good - Read from replicas
REPLICAOF 127.0.0.1 6379

# Good - Monitor replication lag
INFO replication
```

### 4. Use Proper Configuration

```conf
# Good - Cluster configuration
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000

# Good - Sentinel configuration
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 5000
```

### 5. Monitor Cluster Health

```bash
# Good - Check cluster status
redis-cli cluster info

# Good - Check cluster nodes
redis-cli cluster nodes

# Good - Check cluster slots
redis-cli cluster slots
```

---

## Further Reading

- [Redis Cluster](https://redis.io/docs/management/scaling/)
- [Redis Sentinel](https://redis.io/docs/management/sentinel/)
- [Replication](https://redis.io/docs/management/replication/)
