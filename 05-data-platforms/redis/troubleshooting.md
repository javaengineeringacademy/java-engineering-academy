# Redis Troubleshooting

## Connection Issues

### Cannot Connect

```bash
# Check if Redis is running
systemctl status redis

# Check port
netstat -tlnp | grep 6379

# Check bind address
CONFIG GET bind

# Check protected mode
CONFIG GET protected-mode

# Check firewall
iptables -L
```

### Connection Refused

```bash
# Verify Redis is listening
redis-cli PING

# Check port
CONFIG GET port

# Check if protected mode is enabled
CONFIG GET protected-mode

# Test with telnet
telnet localhost 6379
```

### Authentication Failed

```bash
# Check if password is set
CONFIG GET requirepass

# Authenticate
AUTH your_password

# Check ACL
ACL LIST
ACL WHOAMI
```

## Memory Issues

### Out of Memory

```bash
# Check memory usage
INFO memory

# Check maxmemory setting
CONFIG GET maxmemory

# Check eviction policy
CONFIG GET maxmemory-policy

# Check which keys use most memory
redis-cli --bigkeys

# Check per-key memory
MEMORY USAGE key
```

### High Memory Usage

```bash
# Check fragmentation ratio
INFO memory | grep mem_fragmentation_ratio

# Enable active defrag
CONFIG SET activedefrag yes

# Check key encoding
OBJECT ENCODING key

# Monitor memory over time
watch -n 1 'redis-cli INFO memory | grep used_memory_human'
```

## Performance Issues

### Slow Commands

```bash
# Check slow log
SLOWLOG GET 10

# Enable slow log
CONFIG SET slowlog-log-slower-than 10000

# Check command stats
INFO commandstats

# Profile specific command
redis-cli --latency
```

### High Latency

```bash
# Check latency
redis-cli --latency

# Enable latency monitor
CONFIG SET latency-monitor-threshold 10

# Check network
redis-cli --latency-dist

# Check I/O threads (Redis 6.0+)
CONFIG GET io-threads
```

### Low Hit Rate

```bash
# Check hit/miss ratio
INFO stats | grep keyspace

# Calculate ratio
hits / (hits + misses)

# Check TTL distribution
TTL key

# Monitor evictions
INFO stats | grep evicted_keys
```

## Replication Issues

### Replication Lag

```bash
# Check replication status
INFO replication

# Check replication offset
INFO replication | grep master_repl_offset

# Check replica offset
INFO replication | grep slave_repl_offset

# Monitor lag
redis-cli -p 26379 SENTINEL replicas mymaster
```

### Replication Broken

```bash
# Check replication status
INFO replication

# Force full resync
CONFIG SET replica-full-resync yes

# Check network between nodes
redis-cli -h replica-host PING

# Check master auth
CONFIG GET masterauth
```

## Cluster Issues

### Cluster State Not OK

```bash
# Check cluster info
CLUSTER INFO

# Check cluster nodes
CLUSTER NODES

# Check slot coverage
CLUSTER SLOTS

# Reshard if needed
redis-cli --cluster reshard host:port
```

### Node Failure

```bash
# Check cluster health
CLUSTER INFO | grep cluster_state

# Check node status
CLUSTER NODES | grep failing

# Fix cluster
redis-cli --cluster fix host:port

# Replace failed node
redis-cli --cluster add-node new-host:port existing-host:port
```

### Slot Migration Issues

```bash
# Check slot status
CLUSTER SLOTS

# Check migration
CLUSTER NODES | grep migrating

# Fix stuck migration
redis-cli --cluster fix host:port
```

## Persistence Issues

### RDB Not Saving

```bash
# Check save settings
CONFIG GET save

# Check BGSAVE status
INFO persistence | grep rdb_last_bgsave_status

# Trigger manual save
BGSAVE

# Check disk space
df -h /var/lib/redis
```

### AOF Issues

```bash
# Check AOF status
INFO persistence | grep aof_enabled

# Check AOF size
INFO persistence | grep aof_current_size

# Rewrite AOF
BGREWRITEAOF

# Check AOF errors
INFO persistence | grep aof_last_bgrewrite_status
```

## High CPU Usage

```bash
# Check which commands are slow
SLOWLOG GET 10

# Check connected clients
INFO clients | grep connected_clients

# Check for blocking commands
CLIENT LIST

# Profile CPU
redis-cli --stat
```

## Data Issues

### Keys Disappearing

```bash
# Check TTL
TTL key

# Check eviction policy
CONFIG GET maxmemory-policy

# Check if key is expired
TTL key

# Monitor key count
INFO keyspace
```

### Data Corruption

```bash
# Check RDB file
redis-check-rdb dump.rdb

# Check AOF file
redis-check-aof appendonly.aof

# Restore from backup
redis-cli DEBUG RELOAD
```

## Network Issues

### Timeout Errors

```bash
# Check client timeout
CONFIG GET timeout

# Check TCP keepalive
CONFIG GET tcp-keepalive

# Check maxclients
CONFIG GET maxclients

# Monitor connections
INFO clients
```

### Connection Limits

```bash
# Check maxclients
CONFIG GET maxclients

# Check connected clients
INFO clients | grep connected_clients

# Kill idle clients
CLIENT KILL id <client-id>
```

## Quick Fixes

```bash
# Restart Redis
systemctl restart redis

# Clear all data (DANGER!)
FLUSHALL

# Reset slow log
SLOWLOG RESET

# Rewrite config
CONFIG REWRITE

# Reload ACL
ACL LOAD
```

## Debugging Checklist

1. Check Redis process status
2. Verify network connectivity
3. Check memory usage and limits
4. Review slow log for bottlenecks
5. Check replication status
6. Verify cluster health
7. Review client connections
8. Check persistence status
9. Monitor CPU usage
10. Review error logs
