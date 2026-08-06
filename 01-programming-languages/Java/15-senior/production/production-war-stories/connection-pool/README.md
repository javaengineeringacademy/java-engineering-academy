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

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
