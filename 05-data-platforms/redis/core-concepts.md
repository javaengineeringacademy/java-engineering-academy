# Redis Core Concepts

## Data Types

### Strings

- Binary-safe up to 512 MB
- Integers stored as long (no overhead)
- Commands: `GET`, `SET`, `INCR`, `DECR`, `APPEND`, `GETRANGE`, `SETRANGE`
- Atomic counters: `INCRBY`, `DECRBY`, `INCRBYFLOAT`
- Multi-value: `MSET`, `MGET`, `MSETNX`

### Lists

- Doubly-linked list or quicklist (ziplist + linked list)
- O(1) push/pop from both ends
- Bounded or unbounded
- Commands: `LPUSH`, `RPUSH`, `LPOP`, `RPOP`, `LRANGE`, `LINDEX`
- Blocking: `BLPOP`, `BRPOP`, `BLMPOP`
- Queue pattern: `LPUSH` + `RPOP`
- Stack pattern: `LPUSH` + `LPOP`

### Sets

- Unordered collection of unique strings
- Intset for small integer sets (memory efficient)
- Commands: `SADD`, `SREM`, `SISMEMBER`, `SMEMBERS`, `SCARD`
- Set operations: `SINTER`, `SUNION`, `SDIFF`, `SINTERSTORE`
- Random access: `SRANDMEMBER`, `SPOP`

### Sorted Sets (ZSets)

- Ordered by score (double precision float)
- O(log N) for insert, update, rank queries
- Commands: `ZADD`, `ZREM`, `ZRANK`, `ZREVRANK`, `ZRANGE`, `ZREVRANGE`
- Score operations: `ZINCRBY`, `ZRANGEBYSCORE`, `ZRANGEBYLEX`
- Use cases: Leaderboards, priority queues, time-series indexing

### Hashes

- Field-value pairs (like a mini object)
- Ziplist encoding for small hashes (compact)
- Commands: `HSET`, `HGET`, `HDEL`, `HGETALL`, `HKEYS`, `HVALS`
- Bulk operations: `HMSET`, `HMGET`
- Increment: `HINCRBY`, `HINCRBYFLOAT`
- Field-level TTL: `HEXPIRE` (Redis 7.4+)

### Streams

- Append-only log data structure
- Consumer groups for distributed processing
- Commands: `XADD`, `XREAD`, `XREADGROUP`, `XRANGE`, `XLEN`
- Consumer management: `XGROUP CREATE`, `XACK`, `XPENDING`
- Entry IDs: `<millisecondsTime>-<sequenceNumber>`
- Use cases: Event sourcing, message queues, audit logs

### HyperLogLog

- Probabilistic cardinality estimation
- Uses only 12 KB memory regardless of cardinality
- 0.81% standard error
- Commands: `PFADD`, `PFCOUNT`, `PFMERGE`
- Use cases: Unique visitors, cardinality analytics

### Bitmaps

- Bit-array operations on strings
- Commands: `SETBIT`, `GETBIT`, `BITCOUNT`, `BITOP`, `BITPOS`
- Use cases: Feature flags, user activity tracking, daily sign-ins

## Key Operations

### TTL (Time-To-Live)

- Set expiration on any key type
- Commands: `EXPIRE`, `PEXPIRE`, `EXPIREAT`, `PEXPIREAT`
- Remove expiration: `PERSIST`
- Check: `TTL`, `PTTL`
- Lazy deletion on access + periodic sampling (10 keys/sec)

### Key Space

- Global namespace (no databases within databases)
- 16 logical databases (0-15), default is 0
- `SELECT <db>` to switch databases
- `KEYS` (blocking) vs `SCAN` (cursor-based, non-blocking)
- `RANDOMKEY` for random key selection

### Transactions

- `MULTI` / `EXEC` / `WATCH` / `DISCARD`
- Commands queued during `MULTI`, executed atomically
- No rollback on command failure (unlike RDBMS)
- `WATCH` provides optimistic locking (CAS semantics)
- Lua scripts offer true atomicity

## Pub/Sub

### Channels

- Fire-and-forget messaging
- No message persistence or acknowledgment
- Commands: `PUBLISH`, `SUBSCRIBE`, `PSUBSCRIBE`, `UNSUBSCRIBE`
- Pattern matching: `PSUBSCRIBE news.*`
- Limited replay: No history, messages lost if no subscriber

### Sharded Pub/Sub (Redis 7.0+)

- Messages routed to the node owning the shard
- Better scalability for pub/sub workloads
- Commands: `SPUBLISH`, `SSUBSCRIBE`

### Consumer Groups vs Pub/Sub

| Feature | Pub/Sub | Consumer Groups |
|---------|---------|-----------------|
| Persistence | None | Built-in |
| Replay | No | Yes |
| Backpressure | None | Via `MAXLEN`/`MINID` |
| Load balancing | Fan-out | Round-robin |

## Pipelining

- Send multiple commands without waiting for responses
- Reduces network round trips
- Server-side: Commands buffered, executed together
- Client-side: Batch commands, read all responses

```
// Without pipeline: 4 round trips
SET a 1 → OK
SET b 2 → OK
SET c 3 → OK
GET a   → "1"

// With pipeline: 1 round trip
SET a 1, SET b 2, SET c 3, GET a → OK, OK, OK, "1"
```

## Lua Scripting

- Scripts run atomically on the server
- No client-server interaction during execution
- `EVAL` / `EVALSHA` for script execution
- Cache scripts with `SCRIPT LOAD`
- Use cases: Atomic multi-step operations, rate limiting, distributed locks

## Transactions vs Lua vs Pipelines

| Feature | Transactions | Lua Scripts | Pipelines |
|---------|-------------|-------------|-----------|
| Atomicity | Yes (queue) | Yes (execute) | No |
| Conditional Logic | No | Yes | No |
| Network Efficiency | No | Yes | Yes |
| Blocking | No | Yes (briefly) | No |
