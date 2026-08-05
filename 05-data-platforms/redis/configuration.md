# Redis Configuration

## Configuration File

Redis configuration is defined in `redis.conf`. On startup, load with:

```bash
redis-server /path/to/redis.conf
```

Override any setting via command line:

```bash
redis-server --maxmemory 2gb --maxmemory-policy allkeys-lru
```

## Memory Settings

```conf
# Maximum memory limit
maxmemory 4gb

# Eviction policy when maxmemory is reached
maxmemory-policy allkeys-lru

# LRU sampling precision (higher = more accurate, more CPU)
maxmemory-samples 10

# Active defragmentation
activedefrag yes
active-defrag-threshold-lower 10
active-defrag-threshold-upper 100
```

### Eviction Policies

| Policy | Scope | Behavior |
|--------|-------|----------|
| `noeviction` | - | Returns errors when memory limit reached |
| `allkeys-lru` | All keys | Evict least recently used |
| `allkeys-lfu` | All keys | Evict least frequently used |
| `allkeys-random` | All keys | Evict random key |
| `volatile-lru` | Keys with TTL | Evict LRU among keys with expiry |
| `volatile-lfu` | Keys with TTL | Evict LFU among keys with expiry |
| `volatile-random` | Keys with TTL | Evict random key with expiry |
| `volatile-ttl` | Keys with TTL | Evict key with shortest TTL |

## Network Settings

```conf
# Bind address (use 127.0.0.1 for local only)
bind 127.0.0.1 -::1

# Listening port
port 6379

# TCP backlog (increase on high-traffic servers)
tcp-backlog 511

# Client timeout (0 = no timeout)
timeout 300

# TCP keepalive (detect dead connections)
tcp-keepalive 300

# Maximum simultaneous clients
maxclients 10000
```

## Persistence Settings

```conf
# RDB snapshots
save 900 1       # Save if at least 1 key changed in 900 seconds
save 300 10      # Save if at least 10 keys changed in 300 seconds
save 60 10000    # Save if at least 10000 keys changed in 60 seconds

# RDB filename and directory
dbfilename dump.rdb
dir /var/lib/redis

# Stop writes on background save errors
stop-writes-on-bgsave-error yes

# Compress RDB with LZF
rdbcompression yes

# Enable/disable RDB
save ""          # Disable RDB persistence

# AOF (Append-Only File)
appendonly yes
appendfilename "appendonly.aof"

# AOF fsync policy
appendfsync everysec    # always | everysec | no

# AOF rewrite settings
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb

# Disable AOF
appendonly no
```

## Security Settings

```conf
# Require password authentication
requirepass your_strong_password_here

# ACL file (Redis 6.0+)
aclfile /etc/redis/users.acl

# Disable危险 commands
rename-command FLUSHALL ""
rename-command FLUSHDB ""
rename-command DEBUG ""

# Protected mode (auto-protects if no bind/password)
protected-mode yes
```

## Slow Log

```conf
# Slow log threshold (in microseconds)
slowlog-log-slower-than 10000

# Maximum slow log entries
slowlog-max-len 128
```

## Latency Monitoring

```conf
# Latency monitor threshold (in milliseconds)
latency-monitor-threshold 100
```

## Threading (Redis 6.0+)

```conf
# Number of I/O threads (1 = disabled, auto = detect)
io-threads 4

# Enable multi-threaded reads
io-threads-do-reads yes
```

## Replication Settings

```conf
# Replicate from primary
replicaof <masterip> <masterport>

# Primary password (if primary requires auth)
masterauth <master-password>

# Replica read-only
replica-read-only yes

# Replication backlog size
repl-backlog-size 256mb

# Replication backlog TTL (seconds, 0 = never release)
repl-backlog-ttl 3600

# Replica serves stale data during sync
replica-serve-stale-data yes

# Min replicas for writes
min-replicas-to-write 1
min-replicas-max-lag 10
```

## Cluster Settings

```conf
# Enable cluster mode
cluster-enabled yes

# Cluster configuration file
cluster-config-file nodes-6379.conf

# Cluster node timeout (milliseconds)
cluster-node-timeout 15000

# Require full coverage (all slots assigned)
cluster-require-full-coverage yes

# Allow reads from replicas in cluster
cluster-replica-validity-factor 10
```

## Logging

```conf
# Log level: debug, verbose, notice, warning
loglevel notice

# Log file (empty = stdout)
logfile /var/log/redis/redis.log

# syslog
# syslog-enabled yes
# syslog-ident redis
# syslog-facility local0
```

## Client Output Buffer Limits

```conf
# Normal clients (0 0 = unlimited)
client-output-buffer-limit normal 0 0 0

# Replica clients
client-output-buffer-limit replica 256mb 64mb 60

# Pub/Sub clients
client-output-buffer-limit pubsub 32mb 8mb 60
```

## Database Settings

```conf
# Number of databases
databases 16

# Max memory samples for key eviction
maxmemory-samples 5
```

## Dynamic Configuration

Many settings can be changed at runtime without restart:

```bash
# Set maxmemory
CONFIG SET maxmemory 8gb

# Change eviction policy
CONFIG SET maxmemory-policy allkeys-lfu

# Enable slow log
CONFIG SET slowlog-log-slower-than 5000

# Reload ACL file
ACL LOAD

# Save configuration
CONFIG REWRITE
```

## Configuration Best Practices

1. Set `maxmemory` to 70-80% of available RAM
2. Use `appendfsync everysec` for most workloads
3. Set `tcp-backlog` to at least 511 for high traffic
4. Enable `active-defrag` for long-running instances
5. Use `rename-command` to disable dangerous commands in production
6. Always set `requirepass` in production
7. Use `CONFIG REWRITE` to persist runtime changes
