# Amazon ElastiCache

## Overview

Amazon ElastiCache is a fully managed in-memory caching service supporting Redis and Memcached.

## Engine Comparison

| Feature         | Redis                              | Memcached                  |
|-----------------|------------------------------------|----------------------------|
| Data Structures | Lists, Sets, Hashes, Sorted Sets  | Simple key-value           |
| Persistence     | Yes (snapshots, AOF)              | No                         |
| Replication     | Yes (multi-AZ)                    | No                         |
| Pub/Sub         | Yes                                | No                         |
| Lua Scripting   | Yes                                | No                         |
| Use Case        | Session store, leaderboards       | Simple caching             |

## Redis vs Memcached

### When to Use Redis
- Complex data structures
- Data persistence required
- Pub/Sub messaging
- Lua scripting
- Cluster mode

### When to Use Memcached
- Simple key-value caching
- Multi-threaded performance
- Horizontal scaling
- No persistence needed

## Cluster Mode (Redis)

```bash
# Create Redis cluster
aws elasticache create-cache-cluster \
  --cache-cluster-id my-redis-cluster \
  --engine redis \
  --cache-node-type cache.r6g.large \
  --num-cache-nodes 3 \
  --replication-group-id my-replication-group

# Create replication group
aws elasticache create-replication-group \
  --replication-group-id my-replication-group \
  --replication-group-description "My Redis cluster" \
  --num-cache-clusters 3 \
  --cache-node-type cache.r6g.large \
  --engine redis \
  --automatic-failover-enabled
```

### Cluster Mode Enabled
```bash
# Create cluster-mode enabled
aws elasticache create-replication-group \
  --replication-group-id my-cluster \
  --replication-group-description "Cluster mode" \
  --num-node-groups 3 \
  --replicas-per-node-group 2 \
  --cache-node-type cache.r6g.large \
  --engine redis \
  --cluster-mode enabled
```

## Replication Groups

### Architecture
```
Primary Node ──→ Replica 1 (AZ-1)
    │
    ├──→ Replica 2 (AZ-2)
    │
    └──→ Replica 3 (AZ-3)
```

### Features
- **Automatic failover**
- **Multi-AZ replication**
- **Read replica scaling**
- **Backup and restore**

## Parameter Groups

```bash
# Create parameter group
aws elasticache create-cache-parameter-group \
  --cache-parameter-group-family redis7 \
  --cache-parameter-group-name my-params \
  --description "Custom Redis parameters"

# Modify parameters
aws elasticache modify-cache-parameter-group \
  --cache-parameter-group-name my-params \
  --parameter-name-values '[
    {"name": "maxmemory-policy", "value": "volatile-lru"}
  ]'
```

## Security

### Encryption at Rest
```bash
# Enable encryption at rest
aws elasticache create-cache-cluster \
  --cache-cluster-id my-cluster \
  --at-rest-encryption-enabled \
  --kms-key-id arn:aws:kms:us-east-1:123456789012:key/my-key
```

### Encryption in Transit
```bash
# Enable encryption in transit
aws elasticache create-cache-cluster \
  --cache-cluster-id my-cluster \
  --transit-encryption-enabled \
  --auth-token my-secret-token
```

### VPC Security Groups
```bash
aws ec2 authorize-security-group-ingress \
  --group-id sg-12345678 \
  --protocol tcp \
  --port 6379 \
  --cidr 10.0.0.0/16
```

## Subnet Groups

```bash
# Create subnet group
aws elasticache create-cache-subnet-group \
  --cache-subnet-group-name my-subnet-group \
  --cache-subnet-group-description "My subnet group" \
  --subnet-ids subnet-12345678 subnet-87654321
```

## Backup & Restore

```bash
# Create snapshot
aws elasticache create-snapshot \
  --cache-cluster-id my-cluster \
  --snapshot-name my-snapshot

# Restore from snapshot
aws elasticache restore-cache-cluster-from-snapshot \
  --cache-cluster-id my-restored-cluster \
  --snapshot-name my-snapshot

# Enable automated backups
aws elasticache modify-cache-cluster \
  --cache-cluster-id my-cluster \
  --snapshot-retention-limit 7 \
  --snapshot-window 05:00-09:00
```

## Global Datastore (Redis)

```bash
# Create global datastore
aws elasticache create-replication-group \
  --replication-group-id my-global \
  --global-replication-group-description "Global replication" \
  --global-replication-group-id-suffix my-suffix \
  --replication-group-description "Primary region"
```

### Features
- **Cross-region replication**
- **Sub-second latency**
- **Disaster recovery**
- **Consistency**

## Redis Auth

```bash
# Enable Redis AUTH
aws elasticache create-cache-cluster \
  --cache-cluster-id my-cluster \
  --auth-token my-secret-token \
  --engine redis

# Token rotation
aws elasticache modify-cache-cluster \
  --cache-cluster-id my-cluster \
  --auth-token new-secret-token
```

## User Groups & ACLs (Redis)

```bash
# Create user group
aws elasticache create-user-group \
  --user-group-id my-user-group \
  --user-group-description "My user group"

# Create user
aws elasticache create-user \
  --user-id my-user \
  --user-description "My user" \
  --passwords my-password \
  --access-string "on ~* +@all"
```

## Monitoring

```bash
# Get cache metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/ElastiCache \
  --metric-name CacheHits \
  --dimensions Name=CacheClusterId,Value=my-cluster \
  --start-time 2024-01-01T00:00:00Z \
  --end-time 2024-01-01T23:59:59Z \
  --period 300 \
  --statistics Sum
```

### Key Metrics
| Metric           | Description                    |
|------------------|--------------------------------|
| CacheHitRate     | Percentage of cache hits       |
| Evictions        | Items removed due to memory    |
| CurrentConnections| Active connections             |
| EngineCPUUtilization | CPU usage                 |

## Cost Optimization

- **Use Reserved Nodes** for steady-state workloads
- **Right-size cache nodes** based on usage
- **Use cluster mode** for horizontal scaling
- **Implement eviction policies** appropriately
- **Monitor memory usage** to avoid over-provisioning

## Best Practices

1. **Choose appropriate engine** (Redis vs Memcached)
2. **Use cluster mode** for large datasets
3. **Implement encryption** at rest and in transit
4. **Enable automatic failover** for Redis
5. **Use parameter groups** for tuning
6. **Implement backup** strategy
7. **Monitor cache hit rate**
8. **Use VPC security groups**
9. **Implement authentication**
10. **Regular performance testing**
