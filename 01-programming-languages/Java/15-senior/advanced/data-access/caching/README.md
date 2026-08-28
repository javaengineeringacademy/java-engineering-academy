# Caching Concepts

## Caching Strategies
- **Cache-Aside**: App manages cache manually
- **Read-Through**: Cache loads data automatically
- **Write-Through**: Writes to cache and DB together
- **Write-Behind**: Async write to DB

## When to Cache
- Frequently accessed data
- Expensive computations
- API responses
- Database query results
- Session data

## Cache Invalidation
- Time-based expiration (TTL)
- Event-based invalidation
- Version-based invalidation
- Manual invalidation

## Common Patterns
- **Cache-Aside**: Most common
- **Read-Through**: Transparent loading
- **Refresh-Ahead**: Pre-emptive refresh
- **Write-Through**: Consistent writes

## Eviction Policies
- **LRU**: Least Recently Used
- **LFU**: Least Frequently Used
- **FIFO**: First In First Out
- **TTL**: Time To Live
- **Size-based**: Maximum entries

## Caffeine Features
- High-performance cache
- Size-based eviction
- Time-based expiration
- Asynchronous loading
- Statistics collection
- Removal listeners

## Common Issues
- Cache stampede (thundering herd)
- Cache penetration
- Cache avalanche
- Data consistency
- Memory pressure

## Best Practices
- Set appropriate cache sizes
- Use TTL for all entries
- Monitor hit/miss rates
- Implement cache warming
- Handle cache failures gracefully

## Interview Questions

1. **Explain cache-aside vs read-through caching. When would you choose each?**
   Cache-aside gives the application explicit control: check cache, on miss query DB, populate cache. Read-through delegates cache loading to the cache itself via a CacheLoader. Choose cache-aside when you need custom loading logic (e.g., combining multiple data sources). Choose read-through when you want simpler application code and the cache library supports it natively (Caffeine LoadingCache).

2. **How do you prevent cache stampede (thundering herd)?**
   When a popular cache key expires, thousands of threads hit the database simultaneously. Solutions: (1) Use `synchronized` or `ReentrantLock` so only one thread loads from DB; (2) Use Caffeine's `AsyncLoadingCache` with `refreshAfterWrite` which refreshes in background before expiry; (3) Implement probabilistic early expiration; (4) Use a mutex lock per key with `Lock striping`.

3. **What happens when you have 10 million entries but only 1GB of heap for the cache?**
   You must configure eviction. With Caffeine: set `maximumSize(1_000_000)` and use `recordStats()` to monitor hit rate. For LRU, Caffeine uses a W-TinyLFU algorithm that approximates LFU with O(1) operations. Set appropriate TTL to prevent stale data. Consider off-heap caching (MapDB, Ehcache with off-heap) if heap is too constrained.

4. **How do you handle cache invalidation in a distributed system?**
   Options: (1) TTL-based expiration — simplest but eventual consistency; (2) Pub/Sub invalidation via Redis Pub/Sub or Kafka to broadcast invalidation events; (3) Versioned keys — append version number to keys; (4) Write-through to DB + cache simultaneously; (5) Event Sourcing — invalidate on DB change events from CDC (Change Data Capture).

5. **When does caching actually hurt performance?**
   When: (1) Hit rate is below ~60% — cache lookup overhead exceeds DB query savings; (2) Data changes frequently — high invalidation churn wastes memory; (3) Cache key design causes collisions; (4) Serialization/deserialization is expensive (e.g., complex objects with deep graphs); (5) The working set exceeds cache size causing constant eviction (thrashing).

6. **Compare Caffeine vs Guava Cache vs Redis for local caching.**
   Caffeine: fastest (W-TinyLFU), ~98% hit rate, Java 8+, async support. Guava Cache: older, LRU-only, ~90% hit rate under scan resistance. Redis: network overhead (1-5ms), shared across instances, persistent. Use Caffeine for in-process caching, Redis for distributed caching, Guava only for legacy codebases.

## Performance

### Cache Hit Rate Impact
```
No cache:   100 queries × 5ms = 500ms total
80% hit:     20 queries × 5ms + 80 × 0.1ms = 108ms (78% improvement)
95% hit:      5 queries × 5ms + 95 × 0.1ms = 34.5ms (93% improvement)
```

### Caffeine Benchmark (ops/sec)
| Operation | Caffeine | Guava | ConcurrentHashMap |
|-----------|----------|-------|-------------------|
| get() | ~250M | ~120M | ~200M |
| put() | ~180M | ~90M | ~180M |
| Mixed (80/20 get/put) | ~220M | ~100M | ~195M |

### Memory Overhead per Entry
| Cache | Overhead |
|-------|----------|
| Caffeine | ~48 bytes |
| Guava | ~64 bytes |
| ConcurrentHashMap | ~32 bytes |
| Redis | ~60 bytes + network |

## Examples

```java
// Caffeine cache with TTL, size limit, and stats
Cache<String, User> cache = Caffeine.newBuilder()
    .maximumSize(10_000)
    .expireAfterWrite(Duration.ofMinutes(5))
    .recordStats()
    .build();

// Loading cache with async refresh
LoadingCache<String, User> loadingCache = Caffeine.newBuilder()
    .maximumSize(10_000)
    .refreshAfterWrite(Duration.ofMinutes(1))
    .expireAfterWrite(Duration.ofMinutes(5))
    .build(key -> userDetailsService.loadUser(key));

// Cache-aside pattern
public User getUser(String id) {
    User cached = cache.getIfPresent(id);
    if (cached != null) return cached;
    User user = userRepository.findById(id);
    cache.put(id, user);
    return user;
}

// Preventing stampede with synchronized loading
private final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

public User getUserSafe(String id) {
    User cached = cache.getIfPresent(id);
    if (cached != null) return cached;
    Object lock = locks.computeIfAbsent(id, k -> new Object());
    synchronized (lock) {
        cached = cache.getIfPresent(id); // double-check
        if (cached != null) return cached;
        User user = userRepository.findById(id);
        cache.put(id, user);
        locks.remove(id);
        return user;
    }
}

// Redis distributed cache
@Component
public class RedisCacheService {
    @Autowired
    private RedisTemplate<String, Object> redis;

    public User getUser(String id) {
        User cached = (User) redis.opsForValue().get("user:" + id);
        if (cached != null) return cached;
        User user = userRepository.findById(id);
        redis.opsForValue().set("user:" + id, user, Duration.ofMinutes(10));
        return user;
    }
}
```

## Internal Working

### Caffeine W-TinyLFU Algorithm
1. **Admission window**: New entries enter a small LRU window (1% of capacity)
2. **Probation segment**: Entries promoted from window after surviving a frequency count
3. **Protected segment**: 80% of capacity, holds entries with highest frequency
4. **TinyLFU sketch**: Count-Min Sketch tracks frequency with ~8 bits per counter
5. **Admission filter**: New entry must have higher frequency than oldest probation entry

### Cache Loading Flow
```
get(key) → key in cache?
  → YES: return value, increment access count
  → NO:  is another thread loading?
            → YES: block/wait for result
            → NO:  acquire loading lock, load from DB, store in cache, release lock
```

### Eviction Process
1. Access order maintained via doubly-linked list
2. W-TinyLFU frequency sketch updated on every access
3. When size exceeds `maximumSize`, eviction candidate selected
4. Entry with lowest frequency-score is evicted
5. Expired entries cleaned up lazily on access and periodically via `Scheduler`

## Why This Concept Exists

Caching solves the fundamental mismatch between computation/data access cost and application performance requirements. Database queries cost 1-100ms, network calls 1-50ms, but users expect sub-100ms responses. Caching stores frequently accessed data in fast-access memory (RAM) to eliminate redundant expensive operations. The concept exists because: (1) disk/network I/O is 100,000x slower than RAM access; (2) most read workloads follow the 80/20 rule (80% of requests hit 20% of data); (3) computing the same result repeatedly wastes CPU cycles.

## Overview

Caching is a performance optimization technique that stores computed or retrieved data in a fast-access storage layer (typically RAM) so subsequent requests can be served without re-executing the expensive operation. In Java, caching exists at multiple levels: CPU L1/L2/L3 caches, JVM JIT code cache, OS page cache, and application-level caches (Caffeine, Guava, Redis). The key challenge is cache invalidation — keeping cached data consistent with the source of truth.

## Pitfalls

```java
// PITFALL 1: Cache without TTL — stale data forever
Cache<String, User> cache = Caffeine.newBuilder()
    .maximumSize(10_000)
    // No .expireAfterWrite() — stale data possible
    .build();

// PITFALL 2: Using mutable objects as cache keys
class UserKey {
    String name; // mutable!
}
cache.put(new UserKey("John"), user); // Key changes → cache miss forever

// PITFALL 3: Cache-aside race condition (no synchronization)
// Thread A: cache miss → query DB (takes 100ms)
// Thread B: cache miss → query DB (takes 100ms) — redundant query

// PITFALL 4: Forgetting to invalidate on write
userRepository.save(user);
// Missing: cache.invalidate(userId);

// PITFALL 5: Large objects in cache causing GC pressure
// Each cached object adds to heap → longer GC pauses
// Solution: Set maximumSize, use off-heap, or compress
```

## References

- [Caffeine GitHub](https://github.com/ben-manes/caffeine)
- [Caffeine Wiki](https://github.com/ben-manes/caffeine/wiki)
- [JSR-107 (JCache)](https://jcp.org/en/jsr/detail?id=107)
- [Redis Documentation](https://redis.io/documentation)
- "Effective Java" by Joshua Bloch — Item 83: Use lazy initialization judiciously
