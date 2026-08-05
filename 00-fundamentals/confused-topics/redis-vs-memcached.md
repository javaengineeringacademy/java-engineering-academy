# Redis vs Memcached

## What They Are

### Redis (Remote Dictionary Server)
An in-memory data structure store supporting multiple data types (strings, hashes, lists, sets, sorted sets). Provides persistence, replication, and advanced features like pub/sub and Lua scripting.

### Memcached
A high-performance, distributed memory caching system designed for simplicity. Stores key-value pairs with string values only. Optimized for speed and ease of use.

## Key Difference Table

| Feature | Redis | Memcached |
|---------|-------|-----------|
| Data Structures | Strings, hashes, lists, sets, sorted sets | Strings only |
| Persistence | Yes (RDB, AOF) | No |
| Replication | Yes (master-slave) | No (client-side) |
| Clustering | Built-in | Client-side |
| Memory Management | Various eviction policies | LRU only |
| Threading | Single-threaded (6.0+ multi-threaded I/O) | Multi-threaded |
| Pub/Sub | Yes | No |
| Lua Scripting | Yes | No |
| Transactions | Yes (MULTI/EXEC) | No |
| Use Case | Feature-rich caching | Simple caching |

## When to Use Which

### Use Redis When
- Complex data structures needed
- Data persistence required
- Pub/sub messaging needed
- Lua scripting for atomic operations
- Multiple data types in one store
- Geographic distribution (Redis Cluster)
- Leaderboards, counters, rate limiting

### Use Memcached When
- Simple key-value caching
- Maximum throughput needed
- Multi-threaded performance required
- Minimal memory overhead
- Simple caching layer
- Large object caching

## Interview Trap

**Trap**: "Redis is always better than Memcached."

**Reality**: Redis offers more features, but Memcached can be faster for simple caching due to multi-threading and simpler architecture. Choose based on requirements, not feature count.

**Follow-up Trap**: "Redis and Memcached are interchangeable."

**Reality**: They have different strengths. Redis is a data structure store; Memcached is a pure cache. Using Redis when you only need simple caching adds unnecessary complexity.

## Visual Diagram

```
Redis Data Structures:
┌─────────────────────────────────────────────────────┐
│                    Redis                             │
│                                                     │
│  String: "user:123" → "{name: 'John'}"            │
│                                                     │
│  Hash: "user:123" → {                              │
│    name: "John",                                    │
│    email: "john@example.com",                       │
│    age: 30                                          │
│  }                                                  │
│                                                     │
│  List: "queue" → [msg1, msg2, msg3]                │
│                                                     │
│  Set: "tags" → {java, python, go}                  │
│                                                     │
│  Sorted Set: "leaderboard" → {                     │
│    player1: 1000,                                   │
│    player2: 900,                                    │
│    player3: 800                                     │
│  }                                                  │
└─────────────────────────────────────────────────────┘

Memcached:
┌─────────────────────────────────────────────────────┐
│                  Memcached                          │
│                                                     │
│  "user:123" → "{name: 'John', email: '...'}"      │
│                                                     │
│  "session:abc" → "session data..."                 │
│                                                     │
│  "product:456" → "{name: 'Laptop', price: 999}"   │
│                                                     │
│  (All values are strings)                           │
│  (No complex data structures)                       │
└─────────────────────────────────────────────────────┘
```

## Performance Comparison

| Metric | Redis | Memcached |
|--------|-------|-----------|
| Throughput | 100K+ ops/sec | 200K+ ops/sec |
| Latency | <1ms | <1ms |
| Memory Efficiency | Good (structures) | Better (simple) |
| CPU Usage | Higher (features) | Lower (simple) |

## Clustering Approach

**Redis Clustering:**
- Built-in cluster mode
- Automatic sharding
- Master-slave replication
- Automatic failover

**Memcached Clustering:**
- Client-side sharding
- Consistent hashing
- No replication
- Manual failover handling

## Key Insight

Redis and Memcached solve different problems:

**Redis**: Feature-rich data structure store with persistence
**Memcached**: Simple, fast, multi-threaded cache

Use Redis when you need more than simple caching. Use Memcached when you need maximum speed for simple key-value caching.

Many architectures use both:
- Redis for complex caching and data structures
- Memcached for simple, high-throughput caching
- Example: Redis for user sessions with expiry, Memcached for HTML fragment caching
