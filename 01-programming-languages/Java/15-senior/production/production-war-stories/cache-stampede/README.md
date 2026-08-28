# War Story: Cache Stampede Overwhelmed Database

## What Happened

Our product catalog service experienced a complete cache invalidation event when 100,000+ cache entries expired simultaneously. The resulting database load spiked to 50x normal, causing cascading failures across dependent services. The incident lasted 35 minutes and affected all users browsing products.

## Timeline

| Time | Event |
|------|-------|
| 09:00 | Daily catalog refresh job runs, sets cache TTL to 24h |
| 09:00 | All 100K cache entries get identical TTL (expires tomorrow 09:00) |
| Next day, 09:00 | All cache entries expire simultaneously |
| 09:00:01 | First cache miss triggers database query |
| 09:00:05 | 10,000 concurrent database queries hitting product table |
| 09:00:10 | Database CPU at 100%, query latency > 5s |
| 09:00:15 | Dependent services timeout, circuit breakers open |
| 09:00:20 | Product catalog service health check fails |
| 09:00:25 | Load balancer routes traffic to remaining instances |
| 09:00:35 | Manual intervention: cache warming script deployed |
| 09:01:00 | Service stabilizes as cache repopulates |

## Root Cause

The catalog refresh job set all cache entries with a fixed 24-hour TTL from the same timestamp:

```java
// Problematic code — identical TTL for all entries
public void refreshCatalog() {
    List<Product> products = productRepository.findAll();
    for (Product product : products) {
        cache.put("product:" + product.getId(),
            product,
            Duration.ofHours(24)); // ALL expire at same time!
    }
}
```

When all entries expired simultaneously, every request triggered a cache miss and database query. The database couldn't handle 100K concurrent queries, causing timeouts that cascaded through the service mesh.

## Detection

### Database Metrics
- `pg_stat_activity`: Active connections spiked from 50 to 500
- Query latency: p99 went from 10ms to 5,000ms
- CPU utilization: 100% sustained

### Cache Metrics
- Cache hit rate: dropped from 99.5% to 0%
- Cache miss rate: spiked to 100%
- Cache operations/second: spiked from 1K to 50K

### Application Metrics
- Response time p99: 8,000ms (from 100ms)
- Error rate: 45%
- Dependent service circuit breakers: 8 of 12 open

### What We Missed
- No monitoring on cache expiration patterns
- No alerting on cache hit rate drops
- No database load shedding for cache misses

## Fix

### Immediate (During Incident)
1. Deployed cache warming script to pre-populate cache
2. Added rate limiting on product catalog endpoints
3. Increased database read replicas temporarily

### Short-Term (Within 1 Week)
1. Added random jitter to cache TTLs:

```java
// Fixed — jittered TTL
public void refreshCatalog() {
    List<Product> products = productRepository.findAll();
    for (Product product : products) {
        Duration ttl = Duration.ofHours(24)
            .plus(Duration.ofMinutes(ThreadLocalRandom.current().nextInt(120)));
        cache.put("product:" + product.getId(), product, ttl);
    }
}
```

2. Implemented request coalescing (singleflight pattern)
3. Added cache hit rate alerting: `cache_hit_rate < 0.95`

### Long-Term (Within 1 Month)
1. Implemented cache warming on startup and after invalidation
2. Added staggered refresh to prevent simultaneous expiration
3. Implemented database load shedding for cache-miss scenarios
4. Added cache stampede detection and circuit breaking

## Prevention

### Cache Strategy
- **Always use jittered TTLs**: Add random offset (±10-20%) to prevent synchronized expiration
- **Request coalescing**: Only one concurrent request per cache key hits database
- **Cache warming**: Pre-populate cache before expiration using background refresh
- **Staggered refresh**: Don't refresh all keys at once

### Monitoring
- Alert on cache hit rate < 95%
- Alert on cache miss rate > 5%
- Monitor cache expiration patterns
- Dashboard for cache operations and database load correlation

### Testing
- Load test cache expiration scenarios
- Validate cache behavior under cache-miss storms
- Test database resilience during cache failures
- Chaos engineering: simulate cache invalidation events

## Lessons Learned

1. **Identical TTLs are a ticking time bomb** — always add jitter
2. **Cache stampede is a known pattern** — prevention is straightforward
3. **Request coalescing is essential** — don't let N requests trigger N database queries
4. **Cache warming is not optional** — critical for large caches
5. **Monitor cache hit rate** — it's the most important cache metric

## Interview Questions

1. **What is cache stampede and what causes it?**
   Cache stampede (thundering herd) occurs when many concurrent requests miss the cache simultaneously and hit the database. Caused by: synchronized TTL expiration, hot key expiry, cache failure, or deployment. When 100K cache entries expire at the same instant, 100K database queries fire simultaneously, overwhelming the database.

2. **What is request coalescing (singleflight) and how does it prevent stampede?**
   Request coalescing ensures only one concurrent request per cache key queries the database. Others wait for the result. Implementation: `ConcurrentHashMap<Long, CompletableFuture<Product>> inflight`. `computeIfAbsent` ensures only one future per key. All requests join the same future. Result: 1 database query instead of 10K.

3. **How do you add jitter to cache TTLs and why?**
   Add random offset (±10-20%) to prevent synchronized expiration: `ttl = baseTtl.plus(Duration.ofMinutes(ThreadLocalRandom.current().nextInt(120)))`. Without jitter, all entries expire at the same time (cache stampede). With jitter, entries expire gradually, spreading database load across time.

4. **What is cache warming and when should you implement it?**
   Cache warming pre-populates cache before expiration using background refresh. Implement when: cache hit rate drops below 95%, cache has >10K entries, or TTL-based expiry causes predictable load spikes. Two approaches: proactive (refresh before expiry) and reactive (refresh on first miss after expiry).

5. **What monitoring prevents cache stampede?**
   (1) Cache hit rate alert: <95% = warning, <90% = critical. (2) Cache miss rate alert: >5% = warning, >10% = critical. (3) Database query rate alert: >2x normal = warning. (4) Cache expiration pattern monitoring. (5) Dashboard for cache operations vs database load correlation.

## Pitfalls

**Identical TTLs for all cache entries:**
```java
// BAD: All entries expire at the same time
for (Product product : products) {
    cache.put("product:" + product.getId(), product, Duration.ofHours(24));
}
// 100K entries all expire tomorrow at 09:00

// GOOD: Jittered TTLs
for (Product product : products) {
    Duration ttl = Duration.ofHours(24)
        .plus(Duration.ofMinutes(ThreadLocalRandom.current().nextInt(120)));
    cache.put("product:" + product.getId(), product, ttl);
}
// Entries expire gradually over 2 hours
```

**No request coalescing:**
```java
// BAD: 10K concurrent requests all hit database
Product cached = cache.get("product:" + id);
if (cached == null) {
    Product product = productRepository.findById(id).orElseThrow(); // 10K queries!
    cache.put("product:" + id, product, Duration.ofMinutes(5));
    return product;
}

// GOOD: Singleflight
private final ConcurrentHashMap<Long, CompletableFuture<Product>> inflight = new ConcurrentHashMap<>();

public Product getProduct(long id) {
    Product cached = cache.get("product:" + id);
    if (cached != null) return cached;

    CompletableFuture<Product> future = inflight.computeIfAbsent(id,
        key -> CompletableFuture.supplyAsync(() -> {
            try {
                Product product = productRepository.findById(key).orElseThrow();
                cache.put("product:" + key, product, Duration.ofMinutes(5));
                return product;
            } finally {
                inflight.remove(key);
            }
        })
    );
    return future.join();
}
```

**No cache warming strategy:**
```java
// BAD: Cache cold after invalidation
cache.invalidateAll(); // All entries gone
// Users experience slow responses until cache repopulates

// GOOD: Cache warming on startup and after invalidation
@EventListener(ApplicationReadyEvent.class)
public void warmCache() {
    List<Product> products = productRepository.findAll();
    for (Product product : products) {
        Duration ttl = Duration.ofHours(24)
            .plus(Duration.ofMinutes(ThreadLocalRandom.current().nextInt(120)));
        cache.put("product:" + product.getId(), product, ttl);
    }
    log.info("Cache warmed with {} products", products.size());
}
```

## Performance

**Cache Stampede Impact:**
```
Normal operation:
- Cache hit rate: 99.5%
- Database queries: 50/sec (0.5% of 10K req/s)
- Response time: 5ms (cached), 50ms (database)

Cache stampede:
- Cache hit rate: 0%
- Database queries: 10,000/sec (200x normal)
- Response time: 5,000ms (database overloaded)
- Database CPU: 100%
- Service availability: 0%

Duration: 35 minutes
Lost revenue: $200K
Engineering cost: $50K (incident response)
```

**Request Coalescing Performance:**
```
Without coalescing:
- 10K concurrent requests
- 10K database queries
- Database latency: 5,000ms (overloaded)
- Total: 10K × 5,000ms = 50,000 seconds of DB time

With coalescing:
- 10K concurrent requests
- 1 database query
- Database latency: 50ms (normal)
- Total: 50ms of DB time
- Improvement: 1,000,000x
```

## Internal Working

**Cache Stampede Mechanism:**
```
1. Cache entries created at t=0 with TTL=24h
2. All entries expire at t=24h (simultaneous)
3. First request at t=24h+1s: cache miss → database query
4. Concurrent requests at t=24h+1s: all miss cache → all hit database
5. Database overwhelmed: CPU 100%, connections exhausted
6. Service becomes unresponsive
7. Cache repopulates gradually as database recovers
8. Total downtime: 35 minutes
```

**Request Coalescing Implementation:**
```
1. Request arrives for product:123
2. Check cache: miss
3. Check inflight map: no entry for product:123
4. Create CompletableFuture, put in inflight map
5. Start async database query
6. Other requests for product:123 arrive
7. Check cache: still miss
8. Check inflight map: entry exists
9. Join existing CompletableFuture (no new query)
10. Database query completes, result returned to all waiters
11. Remove from inflight map, put in cache
```

## Why This Concept Exists

Cache stampede prevention exists because:

1. **Cache invalidation is hard**: The "two hard things in CS" — cache invalidation and naming things
2. **Synchronized expiration is dangerous**: All entries expiring at once creates massive load spikes
3. **Database is the bottleneck**: Databases can't handle 100x normal query load
4. **User experience degrades**: Slow responses cause timeouts and user frustration
5. **Cascading failures**: Database overload causes dependent services to fail
6. **Prevention is cheap**: Jitter + coalescing costs $5K to implement, prevents $200K incidents

The pattern exists because cache stampede is a predictable, preventable failure mode that many teams learn the hard way.

## Overview

Cache stampede (thundering herd) occurs when many concurrent requests miss the cache simultaneously, overwhelming the database. Prevention: jittered TTLs (random offset ±10-20%), request coalescing (singleflight pattern), cache warming (background refresh), and monitoring (cache hit rate alerts). This war story demonstrates a real incident where 100K cache entries expired simultaneously, causing 35 minutes of downtime.

## References

- Redis documentation — Cache stampede: https://redis.io/docs/manual/patterns/
- "High Performance MySQL" by Baron Schwartz — Cache stampede prevention
- Singleflight pattern: https://pkg.go.dev/golang.org/x/sync/singleflight
- Cache invalidation patterns: https://docs.microsoft.com/en-us/azure/architecture/patterns/cache-aside
- "Release It!" by Michael Nygard — Cascading failure prevention
