# Redis Production Deployment

## Persistence Tuning

### RDB Configuration

```conf
# Production RDB settings
save 900 1
save 300 10
save 60 10000

# Compress RDB
rdbcompression yes

# Enable checksums
rdbchecksum yes

# Stop writes on BGSAVE error
stop-writes-on-bgsave-error yes
```

### AOF Configuration

```conf
# Enable AOF for durability
appendonly yes

# fsync policy
appendfsync everysec  # Best balance of performance and safety

# AOF rewrite settings
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb

# AOF rewrite during BGSAVE
aof-rewrite-incremental-fsync yes
```

### Hybrid Persistence

```conf
# Use RDB preamble in AOF (Redis 4.0+)
aof-use-rdb-preamble yes
```

## Replication Setup

### Primary Configuration

```conf
# Replication settings
min-replicas-to-write 1
min-replicas-max-lag 10

# Replication backlog
repl-backlog-size 256mb
repl-backlog-ttl 3600
```

### Replica Configuration

```conf
# Replica settings
replicaof primary-host primary-port
masterauth primary-password

# Read-only replicas
replica-read-only yes

# Serve stale data during sync
replica-serve-stale-data yes

# Accept writes during sync (not recommended)
replica-serve-stale-data no
```

## Sentinel Configuration

### sentinel.conf

```conf
# Sentinel port
port 26379

# Monitor primary
sentinel monitor mymaster 127.0.0.1 6379 2

# Failover timeout
sentinel failover-timeout mymaster 60000

# Parallel syncs
sentinel parallel-syncs mymaster 1

# Down-after-milliseconds
sentinel down-after-milliseconds mymaster 5000

# Auth
sentinel auth-pass mymaster primary-password
sentinel auth-user mymaster default
```

### Sentinel Deployment

```bash
# Run 3 sentinels on separate machines
redis-sentinel /etc/redis/sentinel.conf

# Check sentinel status
redis-cli -p 26379 SENTINEL masters
redis-cli -p 26379 SENTINEL get-master-addr-by-name mymaster
```

## Cluster Mode

### Cluster Configuration

```conf
# Enable cluster
cluster-enabled yes
cluster-config-file nodes-7000.conf
cluster-node-timeout 15000
cluster-require-full-coverage yes
```

### Cluster Setup

```bash
# Create cluster
redis-cli --cluster create \
  127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 \
  127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 \
  --cluster-replicas 1

# Check cluster status
redis-cli -c -h 127.0.0.1 -p 7000 CLUSTER INFO
redis-cli -c -h 127.0.0.1 -p 7000 CLUSTER NODES
```

## Resource Limits

### File Descriptors

```bash
# Check current limit
ulimit -n

# Increase in /etc/security/limits.conf
redis soft nofile 65535
redis hard nofile 65535

# Or in systemd service
[Service]
LimitNOFILE=65535
```

### Memory

```conf
# Set memory limit
maxmemory 8gb

# Eviction policy
maxmemory-policy allkeys-lru
```

## Network Configuration

```conf
# TCP backlog
tcp-backlog 511

# Client timeout
timeout 300

# TCP keepalive
tcp-keepalive 300

# Max clients
maxclients 10000
```

## Security Hardening

```conf
# Require password
requirepass strong_password_here

# ACL file
aclfile /etc/redis/users.acl

# Disable dangerous commands
rename-command FLUSHALL ""
rename-command FLUSHDB ""
rename-command DEBUG ""

# Protected mode
protected-mode yes

# Bind to specific interface
bind 127.0.0.1 10.0.0.1
```

## Backup Strategy

### Automated Backups

```bash
#!/bin/bash
# redis-backup.sh

REDIS_CLI="/usr/bin/redis-cli"
BACKUP_DIR="/var/backups/redis"
DATE=$(date +%Y%m%d_%H%M%S)

# Create backup directory
mkdir -p $BACKUP_DIR

# Trigger BGSAVE
$REDIS_CLI BGSAVE

# Wait for completion
while [ "$($REDIS_CLI LASTSAVE)" == "$LAST_SAVE" ]; do
  sleep 1
done

# Copy RDB file
cp /var/lib/redis/dump.rdb $BACKUP_DIR/dump_$DATE.rdb

# Compress
gzip $BACKUP_DIR/dump_$DATE.rdb

# Keep only last 7 days
find $BACKUP_DIR -name "*.rdb.gz" -mtime +7 -delete
```

### Cron Job

```bash
# Run backup daily at 2 AM
0 2 * * * /opt/scripts/redis-backup.sh
```

## Container Deployment

### Docker Production

```yaml
version: '3.8'
services:
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
      - ./redis.conf:/usr/local/etc/redis/redis.conf
    command: redis-server /usr/local/etc/redis/redis.conf
    deploy:
      resources:
        limits:
          memory: 4G
          cpus: '2'
    restart: unless-stopped

volumes:
  redis-data:
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: redis
spec:
  serviceName: redis
  replicas: 3
  selector:
    matchLabels:
      app: redis
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
        - name: redis
          image: redis:7-alpine
          ports:
            - containerPort: 6379
          volumeMounts:
            - name: redis-data
              mountPath: /data
  volumeClaimTemplates:
    - metadata:
        name: redis-data
      spec:
        accessModes: ["ReadWriteOnce"]
        resources:
          requests:
            storage: 10Gi
```

## Monitoring Setup

```bash
# Exporter for Prometheus
docker run -d --name redis-exporter \
  -p 9121:9121 \
  oliver006/redis_exporter \
  --redis.addr redis://redis:6379

# Grafana dashboard
# Import dashboard ID: 763
```

## Production Checklist

1. Set `maxmemory` to 70-80% of available RAM
2. Enable AOF with `appendfsync everysec`
3. Set up replication with automatic failover
4. Configure Sentinel for high availability
5. Use cluster mode for horizontal scaling
6. Enable TLS for encrypted connections
7. Set up monitoring and alerting
8. Automate backups with retention policy
9. Use systemd for process management
10. Set file descriptor limits
11. Configure firewall rules
12. Use non-root user to run Redis
13. Regular security audits
14. Performance testing before deployment
15. Document runbooks for common issues
