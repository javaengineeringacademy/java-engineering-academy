# Rate Limiting

## What Is It?

Rate limiting controls the number of requests a client can make to an API within a given time window. It protects services from overload and abuse.

## Algorithms

### Token Bucket

- Tokens added at a fixed rate up to a max capacity
- Each request consumes one token
- Allows bursts up to bucket capacity
- **Best for**: APIs needing burst tolerance

### Fixed Window

- Counts requests in a fixed time interval
- Simple to implement
- Boundary problem: up to 2x burst at window edges
- **Best for**: Simple use cases

### Sliding Window Log

- Tracks exact timestamps of each request
- Most accurate but memory-intensive
- No boundary issues
- **Best for**: Strict rate limiting needs

### Sliding Window Counter

- Weighted average of current and previous window
- Good balance of accuracy and efficiency
- **Best for**: Production systems

## Comparison

| Algorithm | Accuracy | Memory | Burst Handling |
|-----------|----------|--------|----------------|
| Token Bucket | High | Low | Allows bursts |
| Fixed Window | Medium | Low | Boundary issues |
| Sliding Log | Highest | High | No bursts |
| Sliding Counter | High | Low | Smooth |

## When to Use

- **API Gateway**: Protect backend services
- **User Limits**: Prevent abuse per user
- **Downstream Protection**: Avoid overwhelming third-party APIs
- **Resource Management**: Control access to shared resources
- **Cost Control**: Limit expensive operations

## Implementation Options

- **Resilience4j RateLimiter**: Production-ready
- **Bucket4j**: Distributed rate limiting
- **Guava RateLimiter**: Single JVM token bucket
- **Redis + Lua**: Distributed sliding window

## Best Practices

1. Return meaningful HTTP 429 responses
2. Include rate limit headers (X-RateLimit-*)
3. Allow configuration per endpoint
4. Consider distributed rate limiting for multi-instance deployments
5. Monitor and alert on limit breaches

## Interview Questions

1. **What is the difference between token bucket and sliding window algorithms?**
   Token bucket: tokens added at fixed rate up to max capacity, each request consumes one token, allows bursts up to bucket capacity. Best for APIs needing burst tolerance. Sliding window: counts requests in a rolling time window, no boundary issues, but more memory-intensive. Token bucket is O(1) memory, sliding window is O(n) where n is requests in window.

2. **How do you implement distributed rate limiting across multiple instances?**
   Use Redis with Lua scripts for atomic operations. Lua script checks token count, decrements if available, returns result. All instances check the same Redis counter. Latency: 1-2ms per request. Alternative: use Redis + sliding window for more accurate limiting. Avoid per-instance rate limiting (allows N× rate with N instances).

3. **When should you rate limit at the API gateway vs application level?**
   API gateway: rate limit by IP, API key, or user. Protects backend from abuse. Application level: rate limit by business logic (e.g., 100 orders/minute per user). Use both: gateway prevents abuse, application enforces business rules. Gateway is coarser, application is finer.

4. **How do you handle rate limiting during cascading failures?**
   Rate limiting is a circuit breaker complement: circuit breaker stops calls to failing services, rate limiting prevents overwhelming recovering services. When a service recovers, gradually increase rate limit (adaptive rate limiting). Use token bucket with burst capacity to handle sudden traffic spikes.

5. **What are the HTTP headers for rate limiting responses?**
   `X-RateLimit-Limit`: maximum requests per window. `X-RateLimit-Remaining`: requests remaining. `X-RateLimit-Reset`: when window resets (epoch timestamp). `Retry-After`: seconds to wait (on 429). Clients should respect Retry-After and back off exponentially.

## Pitfalls

**Per-instance rate limiting:**
```java
// BAD: Rate limit per instance (allows N× total rate)
// 3 instances × 100 req/s = 300 req/s total
RateLimiter limiter = RateLimiter.create(100.0); // Per instance

// GOOD: Distributed rate limiting with Redis
String luaScript = """
    local key = KEYS[1]
    local limit = tonumber(ARGV[1])
    local window = tonumber(ARGV[2])
    local current = redis.call('INCR', key)
    if current == 1 then
        redis.call('EXPIRE', key, window)
    end
    return current <= limit
    """;
Boolean allowed = redisTemplate.execute(script, List.of("rate:" + userId), 100, 60);
```

**Not returning Retry-After header:**
```java
// BAD: Return 429 without guidance
return ResponseEntity.status(429).body("Rate limit exceeded");

// GOOD: Return 429 with Retry-After
return ResponseEntity.status(429)
    .header("Retry-After", "30")
    .header("X-RateLimit-Limit", "100")
    .header("X-RateLimit-Remaining", "0")
    .header("X-RateLimit-Reset", String.valueOf(resetTime))
    .body("Rate limit exceeded. Retry after 30 seconds.");
```

**Using fixed window instead of sliding window:**
```java
// BAD: Fixed window has boundary issues
// Window: [10:00:00 - 10:00:01]
// At 10:00:00.5: 100 requests
// At 10:00:01.0: 100 requests (new window)
// Total in 1 second: 200 requests (2× limit!)

// GOOD: Sliding window counter
// Weighted average of current and previous window
// At 10:00:00.5: 100 requests (50% of window)
// At 10:00:01.0: 100 requests (0.5 × 100 + 0.5 × 100 = 100)
// Total: 100 requests (within limit)
```

## Performance

**Rate Limiter Performance:**
```
Guava RateLimiter (single JVM):
- Throughput: 50M+ operations/second
- Latency: 50ns per acquire
- Memory: 100 bytes per limiter
- Limitation: Not distributed

Redis + Lua (distributed):
- Throughput: 100K+ operations/second
- Latency: 1-2ms per operation
- Memory: 50 bytes per key
- Limitation: Network overhead

Bucket4j (distributed):
- Throughput: 50K+ operations/second
- Latency: 2-5ms per operation
- Memory: 1KB per bucket
- Limitation: Proxy manager overhead
```

**Rate Limiting Overhead:**
```
Per-request overhead:
- Token bucket: 100ns (single JVM)
- Redis: 1-2ms (distributed)
- API gateway: 0.5-1ms (Kong, Envoy)

Impact on throughput:
- 100K req/s with local limiter: 99.99% throughput
- 100K req/s with Redis: 95% throughput (5% overhead)
```

## Internal Working

**Token Bucket Algorithm:**
```
1. Initialize bucket with capacity C tokens
2. Refill tokens at rate R per second
3. On request: check if tokens >= 1
4. If yes: consume 1 token, allow request
5. If no: reject or wait
6. Never exceed capacity C

Time complexity: O(1) per request
Space complexity: O(1) per bucket
```

**Sliding Window Counter:**
```
1. Divide time into fixed windows (e.g., 1 second)
2. Count requests in current window and previous window
3. Weight by time elapsed: current_weight = time_elapsed / window_size
4. total = (previous_count × (1 - weight)) + current_count
5. If total > limit: reject

Time complexity: O(1) per request
Space complexity: O(1) per key (two counters)
```

**Redis Rate Limiting:**
```
1. Client sends request
2. Application calls Redis Lua script
3. Lua script atomically: INCR key, check count, EXPIRE key
4. Redis returns allowed/rejected
5. Application returns response to client
6. Redis operations are atomic (no race conditions)
```

## Why This Concept Exists

Rate limiting exists because:

1. **Resource protection**: Prevents single users from exhausting shared resources
2. **Cost control**: Limits expensive operations (API calls, database queries)
3. **Fairness**: Ensures all users get fair access to resources
4. **Abuse prevention**: Stops DDoS attacks and scraping
5. **Downstream protection**: Prevents overwhelming third-party APIs
6. **SLA enforcement**: Ensures per-user or per-tenant limits

Without rate limiting, a single client can consume 100% of resources, degrading service for all users.

## Overview

Rate limiting controls the number of requests a client can make within a given time window. Algorithms include token bucket (burst tolerance), fixed window (simple), sliding window log (accurate), and sliding window counter (balanced). Implementation options: Guava RateLimiter (single JVM), Resilience4j RateLimiter, Bucket4j (distributed), Redis + Lua (distributed). Essential for API protection, cost control, and fairness.

## References

- Resilience4j RateLimiter: https://resilience4j.readme.io/docs/ratelimiter
- Bucket4j: https://github.com/bucket4j/bucket4j
- Guava RateLimiter: https://guava.dev/
- Redis Rate Limiting with Lua: https://redis.io/commands/eval/
- "System Design Interview" by Alex Xu — Rate limiting patterns
- Cloudflare Rate Limiting: https://developers.cloudflare.com/waf/rate-limiting-rules/
