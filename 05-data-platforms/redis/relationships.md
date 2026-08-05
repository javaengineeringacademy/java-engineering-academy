# Redis Relationships

## Works With

### MySQL

Redis complements MySQL as a caching layer. Cache frequently read queries in Redis to reduce database load. Use cache-aside pattern: read from cache first, fall back to MySQL, then populate cache.

Invalidate cache on writes to prevent stale data. Use Redis TTL to expire entries automatically. Consider write-through for strong consistency.

### MongoDB

Redis can cache MongoDB query results for frequently accessed documents. MongoDB change streams can trigger Redis cache invalidation. Redis provides fast reads for data that MongoDB serves as the primary store.

Use Redis for session data and real-time counters. Use MongoDB for document storage and complex queries.

### Elasticsearch

Redis can serve as a fast lookup layer for Elasticsearch results. Cache search results in Redis to reduce query load. Use Redis pub/sub to invalidate caches when indices update.

### Kafka

Redis can buffer Kafka consumer processing. Use Redis to deduplicate Kafka messages or track consumer offsets. Redis Streams provide a lightweight alternative to Kafka for simpler use cases.

Redis can act as a sidecar cache for Kafka consumer state.

## Alternative

### Memcached

Memcached is a simpler in-memory key-value store. It supports only string values and has no persistence. It uses multithreading for better CPU utilization.

Choose Memcached for simple caching with minimal overhead. Choose Redis for data structures, persistence, pub/sub, and scripting.

Memcached is faster for pure string caching due to simpler architecture. Redis is more versatile.

### KeyDB

KeyDB is a fork of Redis with multithreading and better performance. It is fully compatible with Redis protocols and modules. KeyDB supports active replication and FLASH storage.

Consider KeyDB if you need Redis compatibility with higher throughput. It is a drop-in replacement for most Redis workloads.

## Competitor

### Hazelcast

Hazelcast is an in-memory data grid supporting distributed data structures, caching, and computation. It offers Java-native APIs and embedded mode.

Choose Hazelcast for distributed computing and Java-heavy environments. Choose Redis for simpler operations, broader language support, and richer data structures.

Hazelcast provides distributed locks and queues natively. Redis provides these through modules or Lua scripts.

## Migration Notes

Migrating from Redis to alternatives requires consideration of:
- Data structure support (strings, hashes, lists, sets, sorted sets)
- Persistence options (RDB, AOF)
- Pub/sub and Streams
- Lua scripting and transactions
- Cluster mode and sharding

Migrating to Redis from alternatives requires:
- Data structure mapping (Memcached strings to Redis hashes)
- Persistence configuration
- Memory management and eviction policies
- Client library changes
