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
