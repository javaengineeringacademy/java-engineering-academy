# Redis Common Misconceptions

## 1. Redis is Only a Cache

**Myth**: Redis is just a caching solution like Memcached.

**Reality**: Redis is a multi-purpose data structure server:
- Caching (most common use case)
- Session storage
- Message brokering (Pub/Sub, Streams)
- Rate limiting
- Leaderboards and counters
- Geospatial indexing
- Time series data
- Distributed locks

**Why People Believe It**: Caching is Redis' most visible use case. Early Redis adoption focused on caching.

**Evidence**: 
- Redis supports multiple data structures (strings, lists, sets, hashes, sorted sets)
- Redis Streams provide Kafka-like messaging
- Redis Cluster enables horizontal scaling
- Redis modules extend functionality (RedisJSON, RedisSearch)

**Interview Relevance**: Discuss Redis' full feature set. Explain when to use each capability. Mention alternatives for specific use cases.

---

## 2. Redis is Single-Threaded So It Can't Handle Load

**Myth**: Single-threaded architecture limits Redis performance.

**Reality**: Single-threading is a deliberate design choice:
- No context switching overhead
- No locking contention
- Deterministic execution order
- Pipeline/batch operations amortize overhead
- Redis 6+ supports I/O threading for network operations

**Why People Believe It**: Single-threaded seems like a bottleneck. Multi-core CPUs seem underutilized.

**Evidence**: 
- Redis handles 100k+ operations per second
- Memory operations are nanoseconds
- Network I/O is the bottleneck, not CPU
- Redis Cluster distributes across multiple instances

**Interview Relevance**: Explain single-threaded benefits. Discuss when multi-threading is needed. Mention Redis Cluster for scaling.

---

## 3. Redis Can't Persist Data

**Myth**: Redis loses all data on restart (purely in-memory).

**Reality**: Redis supports persistence:
- **RDB snapshots**: Point-in-time saves
- **AOF (Append-Only File)**: Every write operation
- **Hybrid persistence**: RDB + AOF combined
- **Redis 7.0**: Multi-part AOF for better performance

**Why People Believe It**: Redis is memory-first. Early versions had limited persistence options.

**Evidence**: 
- RDB provides compact backups
- AOF offers durability guarantees
- `appendfsync` controls fsync behavior
- Persistence can be disabled intentionally

**Interview Relevance**: Discuss persistence tradeoffs. Explain RDB vs. AOF. Mention durability guarantees and recovery scenarios.

---

## 4. Redis is Not Suitable for Large Datasets

**Myth**: Redis can only handle small datasets that fit in memory.

**Reality**: Redis scales horizontally:
- Redis Cluster shards data across nodes
- Hash tags distribute keys
- 64-bit systems support large memory
- Eviction policies manage memory pressure
- Redis on Flash (Enterprise) uses SSD for warm data

**Why People Believe It**: Redis is memory-bound. Memory is more expensive than disk.

**Evidence**: 
- Redis Cluster supports thousands of nodes
- Enterprise versions support tiered storage
- Eviction policies (LRU, LFU) handle memory pressure
- Datasets can exceed single-node memory

**Interview Relevance**: Discuss scaling strategies. Explain cluster architecture. Mention memory management and eviction.

---

## 5. All Data Must Fit in Memory

**Myth**: Redis requires entire dataset in memory.

**Reality**: This is partially true but nuanced:
- Primary node: Data must fit in memory
- Replicas: Can use disk-backed storage (Enterprise)
- Redis on Flash: SSD for cold data
- Eviction removes least-recently-used keys
- External storage integration possible

**Why People Believe It**: Redis' speed advantage comes from memory access. Disk access negates performance benefits.

**Evidence**: 
- Memory is 10-100x faster than disk
- Eviction policies prevent OOM
- Redis Enterprise supports Flash storage
- Design for memory-first is fundamental

**Interview Relevance**: Explain memory requirements. Discuss eviction policies. Mention when Redis isn't appropriate (very large datasets).

---

## 6. Redis Transactions are ACID

**Myth**: Redis transactions provide ACID guarantees like databases.

**Reality**: Redis transactions are different:
- **Atomicity**: Commands execute atomically (no interleaving)
- **Consistency**: No constraint checking
- **Isolation**: No isolation levels (single-threaded)
- **Durability**: Depends on persistence configuration

**Why People Believe It**: Redis transactions use MULTI/EXEC. The term "transaction" implies ACID.

**Evidence**: 
- No rollback on command failure (WATCH for optimistic locking)
- No constraints or validation
- Durability requires AOF with fsync
- Lua scripts provide atomicity but not transactions

**Interview Relevance**: Explain Redis transaction semantics. Compare to database ACID. Discuss when to use transactions vs. Lua scripts.
