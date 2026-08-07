# Redis

## Why Redis Exists

Every Python developer building scalable applications hits a bottleneck: databases are too slow for frequently accessed data. Reading from PostgreSQL for every page view or API call creates unacceptable latency. Redis was created to solve this by providing an in-memory data store that's thousands of times faster than disk-based databases. It serves as a cache, session store, and message broker — all in one lightweight package.

## What You'll Learn

By the end of this section, you'll be able to:

- Store and retrieve data using Redis strings, hashes, lists, and sets
- Implement caching patterns with TTL and eviction strategies
- Use pub/sub for real-time messaging between application components

## When to Use Redis

| Use Case | Why Redis | Alternative |
|----------|----------|-------------|
| Session storage | In-memory, fast reads/writes | Database |
| Caching | Sub-millisecond response times | Memcached |
| Rate limiting | Atomic counters with TTL | Database |
| Message queuing | Pub/sub for real-time events | RabbitMQ |
| Leaderboards | Sorted sets with scores | Database |
| Geospatial data | Built-in geo commands | PostGIS |

## How Redis Works Internally

Redis stores all data in memory, which is why it's so fast. Data is organized into different data structures: strings (simple key-value), hashes (field-value pairs), lists (ordered sequences), sets (unordered unique values), and sorted sets (ordered by score). Each structure has optimized commands for common operations.

Redis uses a single-threaded event loop (since version 6, it supports I/O threading for network operations). This means commands are executed atomically — no race conditions. When you `SET key value EX 3600`, Redis stores the key with a 3600-second expiration. The key is automatically deleted when the TTL expires, which is how caching works.

```python
import redis

r = redis.Redis(host='localhost', port=6379, db=0)

# String operations
r.set('user:1:name', 'Alice', ex=3600)  # Expire in 1 hour
name = r.get('user:1:name')

# Hash operations
r.hset('user:1', mapping={'name': 'Alice', 'age': '30'})
user = r.hgetall('user:1')

# List operations
r.lpush('queue', 'task1', 'task2')
task = r.rpop('queue')

# Pub/Sub
pubsub = r.pubsub()
pubsub.subscribe('channel')
for message in pubsub.listen():
    print(message['data'])
```

## Production Checklist

### ✅ Before using Redis in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Redis is just a cache
**Reality:** Redis is a full data structure server. It can be used as a database, cache, message broker, session store, rate limiter, and more. Its versatility makes it indispensable.

### ❌ Myth 2: Redis data is lost on restart
**Reality:** Redis supports persistence through RDB snapshots and AOF (Append-Only File). You can configure both for durability while maintaining performance.

### ❌ Myth 3: Redis is single-threaded, so it's slow
**Reality:** Redis's single-threaded architecture eliminates contention and locking overhead. It can handle 100,000+ operations per second on modest hardware.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | In-memory data structure store |
| Complexity | O(1) for most operations |
| Thread Safe | Yes (single-threaded) |
| Best Alternative | Memcached for simple caching |
| When to Use | Caching, sessions, real-time data |
| When to Avoid | Large datasets exceeding RAM |

## Related Topics

- [11-celery](../11-celery/) - Task queue with Redis backend
- [05-django](../05-django/) - Django cache framework
- [04-flask](../04-flask/) - Flask caching integration
