# Redis Pitfalls and Anti-Patterns

## Big Keys

### Problem

Keys with excessively large values or member counts (10K+ members).

```bash
# Detect large keys
redis-cli --bigkeys
redis-cli MEMORY USAGE large:key

# Examples of big keys
- List with 1M elements
- Set with 500K members
- Hash with 100K fields
- String value > 1 MB
```

### Impact

- Memory imbalance in cluster
- Slow commands (SORT, KEYS, LRANGE)
- Network congestion
- Latency spikes

### Solutions

- Split into multiple smaller keys
- Use SCAN instead of KEYS
- Set TTL to auto-expire
- Monitor with `redis-cli --bigkeys`

## Hot Keys

### Problem

Single key receiving disproportionate traffic.

```bash
# Detect hot keys
redis-cli --hotkeys
redis-cli INFO commandstats

# Examples
- Popular product ID during flash sale
- Viral social media post
- Rate limit key
```

### Impact

- Node overload in cluster
- Uneven memory distribution
- CPU bottleneck
- Network saturation

### Solutions

- Use local caching (in-memory)
- Shard across multiple keys
- Use read replicas
- Implement rate limiting

## Blocking Commands

### Problem

Commands that block the server.

```bash
# Blocking commands
KEYS *              # Scan entire keyspace
SORT large:list     # Sort large collections
LRANGE big:list 0 -1  # Get all elements
SAVE                # Synchronous save
FLUSHALL            # Delete all keys
DEBUG SLEEP 10      # Debug command
```

### Impact

- All clients blocked
- Increased latency
- Potential timeout errors
- Service degradation

### Solutions

- Use SCAN instead of KEYS
- Use pipeline for bulk operations
- Use non-blocking alternatives
- Set client timeouts

## Memory Issues

### Problem

Running out of memory or excessive memory usage.

```bash
# Check memory
INFO memory
MEMORY USAGE key

# Common issues
- No maxmemory set
- Wrong eviction policy
- Large values in memory
- Memory fragmentation
```

### Impact

- Eviction of data
- OOM errors
- Performance degradation
- Service crashes

### Solutions

- Set `maxmemory` limit
- Choose appropriate eviction policy
- Monitor memory usage
- Use `activedefrag yes`

## Unbounded Collections

### Problem

Collections that grow without limits.

```bash
# Dangerous patterns
LPUSH queue:*       # Unbounded list
SADD set:* item     # Unbounded set
ZADD zset:* score   # Unbounded sorted set
```

### Impact

- Memory exhaustion
- Slow operations
- Performance degradation

### Solutions

- Use `LTRIM` for lists
- Use `ZREMRANGEBYRANK` for sorted sets
- Set TTL on keys
- Monitor collection sizes

## Cache Stampede

### Problem

Many clients request same expired key simultaneously.

```
Client A ──→ Cache Miss ──→ DB Query ──→ Cache Update
Client B ──→ Cache Miss ──→ DB Query ──→ Cache Update
Client C ──→ Cache Miss ──→ DB Query ──→ Cache Update
```

### Impact

- Database overload
- Increased latency
- Potential database crash

### Solutions

- Use distributed locks
- Implement stale-while-revalidate
- Use probabilistic early expiration
- Batch cache updates

## No TTL on Keys

### Problem

Keys without expiration accumulate forever.

```bash
# Problematic
SET cache:api:response '{"data":"..."}'

# Better
SETEX cache:api:response 3600 '{"data":"..."}'
```

### Impact

- Memory exhaustion
- Stale data served
- No automatic cleanup

### Solutions

- Always set TTL on cache keys
- Use `EXPIRE` or `SETEX`
- Monitor key counts
- Implement cleanup jobs

## Using wrong data type

### Problem

Using wrong data structure for the use case.

```bash
# Bad: Store object as string
SET user:1234 '{"name":"John","age":30}'

# Good: Store as hash
HSET user:1234 name "John" age 30

# Bad: Use LIST for unique items
LPUSH tags:post "redis" "cache" "redis"

# Good: Use SET
SADD tags:post "redis" "cache"
```

### Impact

- Increased memory usage
- Slower operations
- Unnecessary data transfer

### Solutions

- Use appropriate data types
- Hash for objects
- Set for unique collections
- List for ordered data

## Not Using Pipelines

### Problem

Sending commands one at a time.

```javascript
// Bad: 1000 round trips
for (let i = 0; i < 1000; i++) {
  await redis.set(`key:${i}`, `value:${i}`);
}

// Good: 1 round trip
const pipeline = redis.pipeline();
for (let i = 0; i < 1000; i++) {
  pipeline.set(`key:${i}`, `value:${i}`);
}
await pipeline.exec();
```

### Impact

- High network latency
- Low throughput
- Poor performance

### Solutions

- Use pipeline for bulk operations
- Batch related commands
- Monitor round trips

## Ignoring Replication Lag

### Problem

Reading from replicas that are behind primary.

```bash
# Check replication lag
INFO replication

# Monitor lag
redis-cli -p 26379 SENTINEL replicas mymaster
```

### Impact

- Stale reads
- Inconsistent data
- User confusion

### Solutions

- Monitor replication lag
- Use `WAIT` command for consistency
- Set `min-replicas-max-lag`
- Accept eventual consistency

## Not Monitoring

### Problem

Running Redis without monitoring.

```bash
# Enable monitoring
redis-cli INFO
redis-cli SLOWLOG GET
redis-cli --latency
```

### Impact

- Undetected issues
- Performance problems
- Security vulnerabilities

### Solutions

- Set up monitoring
- Configure alerts
- Regular health checks
- Review slow log
