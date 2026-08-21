# Production: Decision Guide

## When to Apply Production Patterns

### Resilience Pattern Selection

| Failure Mode | Pattern | Implementation |
|-------------|---------|----------------|
| Service unavailable | Circuit breaker | Hystrix, Resilience4j |
| Slow service | Bulkhead + timeout | Thread pool isolation |
| Transient errors | Retry with backoff | Exponential backoff + jitter |
| Traffic spike | Rate limiting | Token bucket, sliding window |
| Resource exhaustion | Circuit breaker + fallback | Degraded responses |
| Cascading failure | All patterns together | Defense in depth |

### Health Check Strategy

| Check Type | What to Verify | Interval |
|-----------|---------------|----------|
| Liveness | Process is running | 10s |
| Readiness | Service can handle traffic | 30s |
| Startup | Service initialized | 5s (during startup) |
| Deep check | Database, cache, queue connectivity | 60s |
| Shallow check | HTTP endpoint responds | 10s |

**Health check endpoint should:**
- Return quickly (<100ms)
- Check dependencies selectively
- Not perform heavy operations
- Include version/build info
- Return structured JSON

### Rate Limiting Strategies

| Strategy | Behavior | Best For |
|----------|----------|----------|
| Fixed window | Count per fixed time period | Simple use cases |
| Sliding window | Smooth rate over rolling window | API rate limiting |
| Token bucket | Allows bursts, smooth average | User-facing APIs |
| Leaky bucket | Fixed outflow, burst handling | Queue-based systems |

### Circuit Breaker Configuration

| Parameter | Typical Value | Rationale |
|-----------|--------------|-----------|
| Failure threshold | 50% or 5 failures | Balance sensitivity vs noise |
| Slow call threshold | 50% slow calls | Detect performance degradation |
| Open duration | 30-60 seconds | Allow service recovery |
| Half-open max calls | 3-5 | Test recovery without overwhelming |

### Graceful Shutdown Sequence

```
1. Receive SIGTERM
2. Deregister from load balancer
3. Stop accepting new requests
4. Wait for in-flight requests (timeout: 30s)
5. Close consumer connections (Kafka, RabbitMQ)
6. Flush pending writes
7. Close database connections
8. Close cache connections
9. Flush logs
10. Release file handles
11. Exit JVM
```

### JVM Production Configuration

```bash
# Memory
-XX:MaxRAMPercentage=75.0
-XX:InitialRAMPercentage=50.0
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp/heapdump.hprof

# GC
-XX:+UseZGC
-XX:+ZGenerational

# Diagnostics
-Xlog:gc*:file=/var/log/gc.log:time,uptime,level,tags
-XX:StartFlightRecording=duration=60s,filename=/tmp/flight.jfr
-XX:+UnlockDiagnosticVMOptions
-XX:+DebugNonSafepoints

# Thread
-XX:+UseBiasedLocking
-XX:CICompilerCount=4
```

## Further Reading

- *Release It!* by Michael Nygard
- *Site Reliability Engineering* by Google
- *The Site Reliability Workbook* by Google
- [12-Factor App](https://12factor.net/)
- [Circuit Breaker Pattern](https://martinfowler.com/bliki/CircuitBreaker.html)
