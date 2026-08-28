# War Story: Connection Pool Exhaustion

## What Happened

Our order service crashed repeatedly during a flash sale event. The service started returning HTTP 503 errors after 5 minutes of peak traffic. Each restart bought another 5–10 minutes before crashing again. The incident lasted 2 hours and lost an estimated $45,000 in orders.

## Timeline

| Time | Event |
|------|-------|
| 10:00 | Flash sale begins, traffic ramps up 10x |
| 10:05 | First 503 errors appear, error rate hits 15% |
| 10:08 | Service auto-restarts (health check failure) |
| 10:12 | Second crash, same pattern |
| 10:15 | On-call engineer paged |
| 10:18 | Thread dump shows all threads waiting on HikariCP |
| 10:20 | Database shows 10 connections from the service |
| 10:25 | HikariCP config: maxPoolSize=10 identified as root cause |
| 10:30 | Hotfix deployed: maxPoolSize=50, connectionTimeout=30000 |
| 10:35 | Service stabilizes, no more crashes |
| 12:00 | Flash sale ends, post-mortem begins |

## Root Cause

The HikariCP connection pool was configured with `maximumPoolSize=10` and no `connectionTimeout`. Under load:

1. All 10 connections were checked out by concurrent requests
2. New requests waited indefinitely for a connection
3. Tomcat thread pool filled up with waiting threads
4. Health check endpoint couldn't get a connection
5. Load balancer marked instance as unhealthy
6. Instance restarted, repeating the cycle

The pool size was set during initial development when the service handled 50 concurrent requests. The flash sale generated 500+ concurrent requests.

## Detection

### Thread Dump Analysis
```
"http-nio-8080-exec-42" #42 daemon prio=5 os_prio=0 tid=0x00007f8b3c0a8000
   java.lang.Thread.State: WAITING (parking)
    at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:133)
    at com.zaxxer.hikari.HikariDataSource.getConnection(HikariDataSource.java:97)
    at com.example.service.OrderRepository.findById(OrderRepository.java:45)
```
All 200 Tomcat threads showed the same stack trace — waiting for a HikariCP connection.

### Database Monitoring
- PostgreSQL `pg_stat_activity`: Only 10 connections from the service
- Connection count remained constant even as request queue grew

### Application Metrics
- `hikaricp_connections_active`: 10 (at maximum)
- `hikaricp_connections_pending`: 190 (all Tomcat threads waiting)
- `hikaricp_connections_timeout_total`: Rapidly increasing

## Fix

### Immediate (Hotfix)
```yaml
# Updated HikariCP configuration
spring:
  datasource:
    hikari:
      maximum-pool-size: 50
      connection-timeout: 30000
      minimum-idle: 10
      idle-timeout: 600000
      max-lifetime: 1800000
```

### Short-Term (Within 1 Week)
1. Added connection pool metrics to Grafana dashboard
2. Set alerts for `hikaricp_connections_pending > 0`
3. Set alerts for `hikaricp_connections_active / hikaricp_connections_max > 0.8`
4. Load tested with 500 concurrent connections to validate new pool size

### Long-Term (Within 1 Month)
1. Implemented connection pool right-sizing tool based on traffic patterns
2. Added circuit breaker for database calls (Resilience4j)
3. Implemented request queuing with backpressure before hitting pool limits
4. Created connection pool sizing runbook

## Prevention

### Configuration Standards
- `connectionTimeout` must always be set (never infinite wait)
- Pool size must be validated against expected peak load
- `minimumIdle` should match expected steady-state connections
- `maxLifetime` should be less than database `wait_timeout`

### Monitoring
- Alert on pending connections > 0 (early warning)
- Alert on pool utilization > 80%
- Monitor connection creation and destruction rates
- Track connection wait time histogram

### Load Testing
- Every service must be load tested at 2x expected peak
- Connection pool behavior must be validated under load
- Include connection pool metrics in load test reports

### Process
- Pool size changes require peer review
- Connection pool configuration must be documented per service
- Quarterly review of pool sizing against actual traffic patterns

## Interview Questions

1. **What is the connection pool sizing formula and why does it matter?**
   Formula: `pool_size = (2 × number_of_cores) + effective_spindle_count`. For SSDs: `2 × cores + 1`. Why: too few connections = request queuing and timeouts. Too many = database overload and context switching overhead. The formula accounts for CPU-bound and I/O-bound work. Monitor `hikaricp_connections_pending` to validate.

2. **How do you detect connection pool exhaustion in production?**
   Signals: (1) `hikaricp_connections_pending > 0`, (2) `hikaricp_connections_active = maxPoolSize`, (3) `Connection is not available, request timed out` errors, (4) thread dump showing all threads waiting on HikariCP, (5) health check failures. Alert on: pending connections > 0, pool utilization > 80%.

3. **What is the difference between HikariCP, DBCP2, and Tomcat JDBC?**
   HikariCP: fastest (150K ops/s), lowest memory (15MB), smallest codebase (4K LOC). DBCP2: slower (80K ops/s), more memory (25MB), more configuration options. Tomcat JDBC: middle ground (100K ops/s), 20MB memory, Tomcat integration. HikariCP is the default for Spring Boot because it's the fastest and most reliable.

4. **How do you prevent connection leaks?**
   (1) Use try-with-resources for all JDBC connections, (2) enable `leakDetectionThreshold` in HikariCP (5-10s), (3) use static analysis (SpotBugs, ErrorProne), (4) monitor `hikaricp_connections_timeout_total`, (5) load test with realistic connection patterns. Leaked connections are returned when the borrowing thread GCs, but that may take minutes.

5. **What happens when connection pool timeout is not set?**
   Without `connectionTimeout`, threads wait indefinitely for a connection. If all connections are checked out, new threads queue forever. This causes: health check failures (can't get connection), load balancer removal, cascading failures. Always set `connectionTimeout=5000` (5 seconds) to fail fast.

## Pitfalls

**Setting pool size too low:**
```java
// BAD: Default pool size (10) for high-traffic service
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(10); // Default — too low for 200 threads
// 190 threads waiting for connections → timeouts

// GOOD: Size based on actual concurrency
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(50); // Match expected peak concurrency
config.setMinimumIdle(10); // Keep connections warm
```

**Not setting connectionTimeout:**
```java
// BAD: No connectionTimeout — threads wait forever
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(10);
// If all connections checked out, threads queue indefinitely

// GOOD: Fail fast
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(10);
config.setConnectionTimeout(5000); // 5 second timeout
```

**Not using try-with-resources:**
```java
// BAD: Connection not closed in all code paths
public Order getOrder(long id) {
    Connection conn = dataSource.getConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders WHERE id = ?");
    ps.setLong(1, id);
    ResultSet rs = ps.executeQuery();
    // If exception thrown here, conn never closed!
    return mapToOrder(rs);
}

// GOOD: Try-with-resources
public Order getOrder(long id) {
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders WHERE id = ?")) {
        ps.setLong(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            return mapToOrder(rs);
        }
    }
}
```

## Performance

**Connection Pool Performance:**
```
HikariCP:
- Connection acquisition: <1ms (when pool not exhausted)
- Connection creation: 50-100ms (TCP + auth)
- Connection validation: 1-5ms (SELECT 1)
- Max throughput: 150K acquisitions/second
- Memory: 15MB for 50 connections

DBCP2:
- Connection acquisition: <1ms
- Connection creation: 50-100ms
- Connection validation: 1-5ms
- Max throughput: 80K acquisitions/second
- Memory: 25MB for 50 connections
```

**Pool Sizing Impact:**
```
Pool size 10: 200 req/s (bottleneck)
Pool size 25: 500 req/s
Pool size 50: 1000 req/s (optimal)
Pool size 100: 1050 req/s (diminishing returns)
Pool size 200: 1000 req/s (database overloaded)

Optimal: pool_size = (2 × cores) + 1
For 8-core server: 17 connections
For 16-core server: 33 connections
```

## Internal Working

**HikariCP Connection Lifecycle:**
```
1. Application calls getConnection()
2. Pool checks idle connections (ready to use)
3. If idle exists: validate (SELECT 1), assign (<1ms)
4. If no idle: create new (TCP + auth, 50-100ms)
5. If at max: wait for connection (connectionTimeout)
6. Application uses connection for queries
7. Application calls close() (returns to pool, not closed)
8. Pool validates connection for next use
9. Connection idle: closed after idleTimeout
10. Connection old: closed after maxLifetime
```

**Connection Leak Detection:**
```
HikariCP leak detection:
1. Thread acquires connection, starts timer
2. If connection not returned within leakDetectionThreshold
3. HikariCP logs warning with stack trace
4. Connection still leaked? After maxLifetime, forcibly reclaimed
5. Warning log includes: connection age, thread name, stack trace

Default leakDetectionThreshold: 0 (disabled)
Recommended: 5000-10000ms
```

## Why This Concept Exists

Connection pool exhaustion happens because:

1. **Database connections are expensive**: Each connection = TCP socket + auth + memory (~10MB for PostgreSQL)
2. **Databases have connection limits**: PostgreSQL default: 100 connections. Exceeding = connection refused
3. **Threads need connections**: Each request typically needs 1-3 database connections
4. **Load spikes exceed pool size**: Flash sales, viral events create 10x traffic spikes
5. **Connections leak**: Unclosed connections exhaust the pool over time
6. **Health checks need connections**: If health check can't get a connection, pod is killed

The formula `pool_size = (2 × cores) + 1` exists because each core can execute one thread, and connections are I/O-bound (waiting for database), so we need enough connections to keep all cores busy.

## Overview

Connection pool exhaustion occurs when all connections in the pool are checked out, causing new requests to timeout. Root cause: undersized pool for actual concurrency. Detection: monitor `hikaricp_connections_pending` and `hikaricp_connections_active`. Fix: size pool based on formula `(2 × cores) + 1`, set `connectionTimeout=5000`, enable leak detection. This war story demonstrates a flash sale incident where `maximumPoolSize=10` caused 503 errors.

## References

- HikariCP documentation: https://github.com/brettwooldridge/HikariCP
- HikariCP pool sizing: https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing
- PostgreSQL connection limits: https://www.postgresql.org/docs/current/runtime-config-connection.html
- "High Performance MySQL" by Baron Schwartz — Connection pooling
- HikariCP leak detection: https://github.com/brettwooldridge/HikariCP/wiki/Leak-Detection
