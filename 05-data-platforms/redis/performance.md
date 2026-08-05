# Redis Performance Optimization

## Pipeline Batching

### Basic Pipeline

```javascript
// Without pipeline: N round trips
for (let i = 0; i < 1000; i++) {
  await redis.set(`key:${i}`, `value:${i}`);
}

// With pipeline: 1 round trip
const pipeline = redis.pipeline();
for (let i = 0; i < 1000; i++) {
  pipeline.set(`key:${i}`, `value:${i}`);
}
await pipeline.exec();
```

### Batch Size Tuning

- Too small: Multiple round trips
- Too large: Memory pressure, blocking server
- Sweet spot: 100-1000 commands per pipeline
- Monitor with `INFO commandstats`

## Lua Scripting

### Atomic Operations

```lua
-- Rate limiting script
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local window = tonumber(ARGV[2])

local current = tonumber(redis.call('GET', key) or '0')
if current >= limit then
  return 0
end

redis.call('INCR', key)
redis.call('EXPIRE', key, window)
return 1
```

### Performance Tips for Scripts

- Keep scripts short and simple
- Avoid slow operations (sorting large sets)
- Use `redis.pcall` for non-critical commands
- Cache scripts with `SCRIPT LOAD`

## Connection Pooling

### Configuration

```javascript
const Redis = require('ioredis');

const pool = new Redis.Cluster(nodes, {
  redisOptions: {
    maxRetriesPerRequest: 3,
    retryDelayOnFailover: 300,
    enableReadyCheck: true,
  },
  clusterRetryStrategy: function (times) {
    return Math.min(times * 100, 2000);
  },
});
```

### Pool Sizing

- Default: 10 connections per node
- High throughput: 20-50 connections
- Monitor with `INFO clients` (connected_clients)
- Too many connections: File descriptor exhaustion

## Memory Optimization

### Data Structure Selection

```bash
# Small integers: Use intset encoding
redis.conf: set-max-intset-entries 512

# Small hashes: Use ziplist encoding
redis.conf: hash-max-ziplist-entries 128
redis.conf: hash-max-ziplist-value 64

# Small lists: Use quicklist encoding
redis.conf: list-max-ziplist-size -2
```

### Memory Efficiency

```bash
# Check memory usage
redis-cli INFO memory

# Check per-key memory
redis-cli MEMORY USAGE key

# Check encoding
redis-cli OBJECT ENCODING key

# Active defragmentation
redis-cli CONFIG SET activedefrag yes
```

### String Optimization

```bash
# Use integers for numeric values (8 bytes vs 20+ bytes)
SET counter 0           # 8 bytes
SET counter "0"         # 20 bytes (in-memory)

# Use embstr for short strings (< 44 bytes)
# Use raw for long strings (> 44 bytes)
```

## Query Optimization

### Avoid KEYS Command

```bash
# BAD: Blocks server
KEYS user:*

# GOOD: Use SCAN
SCAN 0 MATCH user:* COUNT 100
```

### Use EXISTS over GET

```bash
# Slower: Returns value
EXISTS key
GET key

# Faster: Just check existence
EXISTS key
```

### Efficient Data Retrieval

```bash
# BAD: Get all fields
HGETALL user:1234

# GOOD: Get specific fields
HMGET user:1234 name email

# BAD: Get all members
SMEMBERS tags:large-set

# GOOD: Use SSCAN
SSCAN tags:large-set 0 COUNT 100
```

## Network Optimization

### TCP Tuning

```bash
# Increase TCP backlog
redis-cli CONFIG SET tcp-backlog 1024

# Enable TCP keepalive
redis-cli CONFIG SET tcp-keepalive 60
```

### Client-Side Optimization

```javascript
// Enable TCP_NODELAY for low latency
const redis = new Redis({
  host: 'localhost',
  port: 6379,
  enableOfflineQueue: true,
  reconnectOnError: function (err) {
    return err.message.includes('READONLY');
  },
});
```

## Monitoring Performance

### Key Metrics

```bash
# Throughput
redis-cli INFO stats | grep instantaneous_ops_per_sec

# Latency
redis-cli --latency

# Memory
redis-cli INFO memory | grep used_memory_human

# Hit ratio
redis-cli INFO stats | grep -E "(keyspace_hits|keyspace_misses)"
```

### Latency Diagnostics

```bash
# Enable latency monitoring
redis-cli CONFIG SET latency-monitor-threshold 10

# Check latency events
redis-cli LATENCY LATEST

# Latency history
redis-cli LATENCY HISTORY command
```

## Benchmarking

### redis-benchmark

```bash
# Basic benchmark
redis-benchmark -t set,get -n 100000 -c 50

# Pipeline benchmark
redis-benchmark -t set,get -n 100000 -c 50 -P 10

# Cluster benchmark
redis-benchmark -h node1 -p 7000 -t set,get -n 100000

# With data size
redis-benchmark -t set,get -n 100000 -d 256
```

## Performance Best Practices

1. Use pipeline for bulk operations
2. Keep keys short and descriptive
3. Use appropriate data structures
4. Set TTL on all cache keys
5. Avoid large collections (10K+ members)
6. Use SCAN instead of KEYS
7. Enable connection pooling
8. Monitor slow log regularly
9. Use Lua scripts for atomic operations
10. Tune maxmemory and eviction policy
