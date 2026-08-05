# Redis Interview Questions

## Basic Questions

### 1. What is Redis and why is it fast?

Redis is an in-memory data structure server that supports多种数据结构. It is fast because:
- Data stored in RAM (no disk I/O)
- Single-threaded (no context switching)
- Efficient data structures (ziplist, quicklist)
- Non-blocking I/O multiplexing

### 2. What data types does Redis support?

- **Strings**: Binary-safe, up to 512 MB
- **Lists**: Doubly-linked list, queue/stack
- **Sets**: Unordered unique strings
- **Sorted Sets**: Ordered by score
- **Hashes**: Field-value pairs
- **Streams**: Append-only log
- **HyperLogLog**: Cardinality estimation
- **Bitmaps**: Bit-array operations

### 3. What is the difference between RDB and AOF?

| Feature | RDB | AOF |
|---------|-----|-----|
| Method | Point-in-time snapshots | Append-only log |
| Durability | Less (data loss possible) | More (every write) |
| Performance | Better (background save) | Worse (fsync overhead) |
| File Size | Smaller | Larger |
| Recovery | Faster | Slower |

### 4. What is Redis persistence?

Persistence writes in-memory data to disk:
- **RDB**: Periodic snapshots
- **AOF**: Logs every write
- **Hybrid**: RDB + AOF (Redis 4.0+)

### 5. What is the difference between KEYS and SCAN?

- **KEYS**: Blocks server, scans entire keyspace
- **SCAN**: Cursor-based, non-blocking, incremental

## Intermediate Questions

### 6. What is Redis clustering?

Redis Cluster distributes data across multiple nodes:
- 16384 hash slots
- Automatic sharding
- No single point of failure
- Client-side routing

### 7. What is Sentinel?

Sentinel provides:
- High availability
- Automatic failover
- Monitoring
- Service discovery

### 8. What is pipeline in Redis?

Pipeline batches multiple commands:
- Reduces network round trips
- No atomicity guarantee
- Client-side batching
- Server executes commands sequentially

### 9. What are Lua scripts in Redis?

- Execute atomic operations
- No client-server interaction during execution
- Cache with `SCRIPT LOAD`
- Execute with `EVAL`/`EVALSHA`

### 10. What is pub/sub in Redis?

- Fire-and-forget messaging
- No message persistence
- Pattern matching with PSUBSCRIBE
- Use streams for persistent messaging

## Advanced Questions

### 11. How does Redis handle memory?

- Uses jemalloc allocator
- Active defragmentation
- Eviction policies (LRU, LFU, random)
- Memory limits with `maxmemory`

### 12. What is the difference between LRU and LFU?

- **LRU**: Least Recently Used (evicts old data)
- **LFU**: Least Frequently Used (evicts rarely accessed data)

### 13. How does Redis replication work?

- Asynchronous replication
- Full resync: RDB + buffer
- Partial resync: Replication backlog
- Replicas serve stale reads

### 14. What is Redis Cluster failover?

- Automatic detection of failed nodes
- Replica promotion
- Slot reassignment
- Client notification

### 15. What is the difference between MULTI and Lua?

| Feature | MULTI/EXEC | Lua Scripts |
|---------|------------|-------------|
| Atomicity | Queue-based | Execute-based |
| Conditional Logic | No | Yes |
| Access to Keys | During EXEC | During execution |
| Blocking | Briefly | Yes |

## System Design Questions

### 16. Design a caching layer with Redis

- Use appropriate eviction policy (allkeys-lru)
- Set TTL on all cache keys
- Use pipeline for bulk operations
- Implement cache invalidation

### 17. Design a session store with Redis

- Use Hashes for session data
- Set TTL for session expiration
- Use replication for read scaling
- Implement session fixation prevention

### 18. Design a rate limiter with Redis

- Use Lua scripts for atomicity
- Sliding window or fixed window
- Use sorted sets for time-based limiting
- Implement graceful degradation

### 19. Design a distributed lock with Redis

- Use `SET NX EX` for acquire
- Use Lua for release (check-and-delete)
- Implement lock renewal
- Handle clock drift

### 20. Design a real-time analytics system with Redis

- Use HyperLogLog for unique counts
- Use Bitmaps for feature flags
- Use Sorted Sets for leaderboards
- Use Streams for event processing

## Coding Questions

### 21. Implement distributed lock

```lua
-- Lock acquire
if redis.call('SET', KEYS[1], ARGV[1], 'NX', 'PX', ARGV[2]) then
  return 1
end
return 0

-- Lock release
if redis.call('GET', KEYS[1]) == ARGV[1] then
  return redis.call('DEL', KEYS[1])
end
return 0
```

### 22. Implement rate limiter

```lua
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

### 23. Implement cache with TTL

```javascript
class Cache {
  constructor(redis) {
    this.redis = redis;
  }

  async get(key) {
    const data = await this.redis.get(key);
    return data ? JSON.parse(data) : null;
  }

  async set(key, value, ttl = 3600) {
    await this.redis.setex(key, ttl, JSON.stringify(value));
  }

  async del(key) {
    await this.redis.del(key);
  }
}
```

## Performance Questions

### 24. How to optimize Redis performance?

- Use pipeline for bulk operations
- Use SCAN instead of KEYS
- Set maxmemory and eviction policy
- Enable connection pooling
- Use appropriate data structures

### 25. How to monitor Redis performance?

```bash
# Key metrics
INFO memory
INFO stats
INFO clients

# Slow log
SLOWLOG GET 10

# Latency
redis-cli --latency
```
