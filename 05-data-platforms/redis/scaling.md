# Redis Scaling

## Vertical Scaling

### Increase Resources

```bash
# Check current resources
redis-cli INFO memory
redis-cli INFO server

# Increase maxmemory
CONFIG SET maxmemory 16gb

# Add CPU cores (requires restart)
# Edit redis.conf: io-threads 4
```

### Limitations

- Single machine limits (CPU, memory, network)
- No horizontal distribution
- Single point of failure
- Network bandwidth bottleneck

## Horizontal Scaling: Replication

### Primary-Replica Setup

```conf
# Replica config
replicaof primary-host primary-port
masterauth primary-password
replica-read-only yes
```

### Benefits

- Read scaling (distribute read traffic)
- Data redundancy
- Automatic failover with Sentinel

### Limitations

- All writes go to primary
- Writes not scaled horizontally
- Replication lag possible

## Cluster Mode

### Architecture

```
Client ──→ Node A (slots 0-5460)
      ──→ Node B (slots 5461-10922)
      ──→ Node C (slots 10923-16383)
```

### Hash Slot Distribution

- 16384 hash slots total
- Each key hashed to a slot: `CRC16(key) % 16384`
- Each node owns a subset of slots
- Client routes commands to correct node

### Setup

```bash
# Create cluster with 6 nodes (3 masters, 3 replicas)
redis-cli --cluster create \
  127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 \
  127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 \
  --cluster-replicas 1

# Check cluster status
redis-cli --cluster check 127.0.0.1:7000
```

### Resharding

```bash
# Add new node
redis-cli --cluster add-node 127.0.0.1:7006 127.0.0.1:7000

# Reshard slots
redis-cli --cluster reshard 127.0.0.1:7000 \
  --cluster-from <source-node-id> \
  --cluster-to <target-node-id> \
  --cluster-slots <number-of-slots>

# Automatic rebalancing
redis-cli --cluster rebalance 127.0.0.1:7000
```

## Sentinel

### Architecture

```
Sentinel 1 ──┐
Sentinel 2 ──┤──→ Monitor Primary ──→ Replica 1
Sentinel 3 ──┘                       ──→ Replica 2
```

### Benefits

- Automatic failover
- Service discovery
- Monitoring and alerting

### Configuration

```conf
# sentinel.conf
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 5000
sentinel failover-timeout mymaster 60000
sentinel parallel-syncs mymaster 1
```

### Failover Process

1. Sentinel detects primary down
2. Sentinels agree on failover (quorum)
3. Sentinel selects best replica
4. Replica promoted to primary
5. Other replicas reconfigure
6. Clients notified of new primary

## Client-Side Sharding

### Manual Sharding

```javascript
// Simple hash-based sharding
const nodes = [
  { host: '127.0.0.1', port: 7000 },
  { host: '127.0.0.1', port: 7001 },
  { host: '127.0.0.1', port: 7002 },
];

function getNode(key) {
  const hash = crc16(key) % nodes.length;
  return nodes[hash];
}
```

### Limitations

- Manual rebalancing required
- No automatic failover
- Client complexity

## Read Replicas

### Scaling Reads

```bash
# Create multiple replicas
replicaof primary-host primary-port

# Load balance reads at application level
# Use different replicas for different clients
```

### Read Replicas in Cloud

- AWS ElastiCache: Up to 5 read replicas
- Azure Cache for Redis: Up to 10 read replicas
- Google Cloud: Up to 5 read replicas

## Sharding Strategies

### Hash-Based Sharding

```javascript
// Consistent hashing
function getShard(key, nodes) {
  const hash = crc16(key);
  const slot = hash % 16384;
  return nodes.find(n => n.slots.includes(slot));
}
```

### Range-Based Sharding

```javascript
// Shard by key prefix
const shardingMap = {
  'user:*': 'shard-1',
  'order:*': 'shard-2',
  'product:*': 'shard-3',
};
```

## Performance at Scale

### Connection Pooling

```javascript
// Use connection pool per node
const pool = new Redis.Cluster(nodes, {
  redisOptions: {
    maxRetriesPerRequest: 3,
  },
  clusterRetryStrategy: function (times) {
    return Math.min(times * 100, 2000);
  },
});
```

### Pipeline Optimization

```javascript
// Pipeline across multiple nodes
const pipeline = redis.pipeline();
nodes.forEach(node => {
  for (let i = 0; i < 1000; i++) {
    pipeline.set(`key:${i}`, `value:${i}`);
  }
});
await pipeline.exec();
```

## Scaling Decision Matrix

| Need | Solution |
|------|----------|
| Read scaling | Replication + read replicas |
| Write scaling | Cluster mode |
| Data size > RAM | Cluster mode (distribute data) |
| High availability | Sentinel or Cluster |
| Geographic distribution | Redis Enterprise Active-Active |

## Scaling Best Practices

1. Start with replication for read scaling
2. Move to cluster mode when write throughput exceeds single node
3. Use Sentinel for automatic failover
4. Monitor cluster health with `CLUSTER INFO`
5. Plan slot distribution before deployment
6. Use hash tags for related keys (`{user}:1234:profile`)
7. Test failover scenarios regularly
8. Use client libraries with cluster support
9. Monitor replication lag
10. Scale up before scaling out when possible
