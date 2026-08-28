# Health Check Patterns

## What Are They?

Health checks are endpoints that report the status of an application and its dependencies. They enable orchestrators (Kubernetes, ECS) to manage traffic routing.

## Two Types

### Liveness Probe

- Is the application running?
- Restarts the container if it fails
- Use for: infinite loops, deadlocks, unrecoverable states
- **Do NOT** check dependencies — restart won't fix a database outage

### Readiness Probe

- Is the application ready to serve traffic?
- Removes from load balancer if it fails
- Use for: dependency checks, warm-up, initialization
- Should reflect actual ability to handle requests

## Implementation

```java
// Spring Boot Actuator
management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: always

// Custom health indicator
@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Health.up().withDetail("database", "connected").build();
        } catch (Exception e) {
            return Health.down().withDetail("database", e.getMessage()).build();
        }
    }
}
```

## HTTP Endpoints

| Endpoint | Purpose | Expected Code |
|----------|---------|---------------|
| `/health/liveness` | Is app alive? | 200 = alive |
| `/health/readiness` | Ready for traffic? | 200 = ready, 503 = not ready |

## Best Practices

1. Liveness should be lightweight — no external calls
2. Readiness should check all critical dependencies
3. Include response time in health output
4. Use consistent JSON format across services
5. Set appropriate probe intervals and thresholds
6. Log health check failures for monitoring

## Common Mistakes

- Checking database in liveness probe (causes unnecessary restarts)
- Making health checks too expensive
- Not accounting for startup time (readiness should fail during init)
- Hardcoding dependency URLs in health checks

## Interview Questions

1. **What is the difference between a liveness probe and a readiness probe?**
   Liveness probe answers "Is the application alive?" If it fails, Kubernetes restarts the container. Use for unrecoverable states like deadlocks, infinite loops, or memory leaks. Readiness probe answers "Is the application ready to serve traffic?" If it fails, Kubernetes removes the pod from the load balancer. Use for dependency checks, warm-up, and initialization.

2. **Why should you never check external dependencies in a liveness probe?**
   If the database is temporarily unavailable, a liveness probe checking it will fail, causing Kubernetes to restart the container. The restart won't fix the database. Instead, the readiness probe should fail (removing the pod from traffic), and the pod should wait for the database to recover. Liveness = internal state. Readiness = external dependency.

3. **How do you design a health check that doesn't impact performance?**
   Liveness: check internal state only (thread count, heap usage, deadlocks). No external calls. Should complete in <1ms. Readiness: check critical dependencies (database, cache) with short timeouts (1-2s). Use cached health status (refresh every 10s, not per-request). Limit health check frequency (every 10-30s).

4. **What are the Kubernetes probe configuration best practices?**
   Liveness: `initialDelaySeconds=30`, `periodSeconds=10`, `failureThreshold=3`. Readiness: `initialDelaySeconds=5`, `periodSeconds=5`, `failureThreshold=2`. Startup: `initialDelaySeconds=0`, `periodSeconds=5`, `failureThreshold=30`. Set timeouts (`timeoutSeconds=3`) to prevent slow health checks from blocking.

5. **How do you handle health checks during graceful shutdown?**
   During shutdown, liveness should return UP (don't restart during shutdown). Readiness should return DOWN (stop sending traffic). Implement by setting a `shuttingDown` flag in the shutdown hook. The readiness probe checks this flag and returns 503 during shutdown.

## Pitfalls

**Checking database in liveness probe:**
```java
// BAD: Database check in liveness
@Component
public class LivenessHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        jdbcTemplate.queryForObject("SELECT 1", Integer.class); // External call!
        return Health.up().build();
    }
}
// If database is down, Kubernetes restarts pod — won't fix database

// GOOD: Liveness checks internal state only
@Component
public class LivenessHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        // Check thread count, heap usage, deadlocks — internal state
        if (threadCount > MAX_THREADS) {
            return Health.down().withDetail("reason", "Thread count exceeded").build();
        }
        return Health.up().build();
    }
}
```

**Health check too expensive:**
```java
// BAD: Full database scan in health check
public Health health() {
    List<User> users = userRepository.findAll(); // 10,000 rows!
    return Health.up().withDetail("users", users.size()).build();
}

// GOOD: Lightweight check
public Health health() {
    jdbcTemplate.queryForObject("SELECT 1", Integer.class); // Fast
    return Health.up().build();
}
```

**Not accounting for startup time:**
```java
// BAD: Readiness probe fails during startup
// Pod never becomes ready, Kubernetes keeps restarting

// GOOD: Use startup probe for slow-starting applications
// readinessProbe: only after startupProbe succeeds
// startupProbe: allows 30 attempts × 5s = 150s for startup
```

## Performance

**Health Check Performance:**
```
Liveness check: <1ms (no external calls)
Readiness check: 1-5ms (database ping + cache check)
Full health check: 5-10ms (all dependencies)
Health check frequency: every 10-30s
Health check overhead: <0.1% of CPU
```

**Impact of Health Check Failures:**
```
Liveness failure: Pod restart (10-30s downtime)
Readiness failure: Pod removed from load balancer (no downtime for users)
Startup failure: Pod never becomes ready (100% request failure)

Restart cost: 10-30s (JVM startup + warm-up)
Readiness recovery: 1-5s (once dependency recovers)
```

## Internal Working

**Kubernetes Probe Execution:**
1. kubelet periodically executes probe command or HTTP request
2. Probe result: Success (200-399), Failure (other), Unknown (timeout)
3. Liveness failure → kubelet kills container → restarts
4. Readiness failure → kubelet removes pod from Service endpoints
5. Startup failure → kubelet kills container → restarts

**Spring Boot Actuator Health Check:**
```
1. HTTP request to /health endpoint
2. Actuator invokes all HealthIndicator beans
3. Each indicator checks a component (database, cache, etc.)
4. Aggregate status: UP if all UP, DOWN if any DOWN
5. Response: JSON with status and details
```

## Why This Concept Exists

Health checks exist because:

1. **Container orchestration**: Kubernetes needs to know which pods can receive traffic
2. **Automatic recovery**: Liveness probes enable automatic restart of failed containers
3. **Traffic management**: Readiness probes prevent sending traffic to unready pods
4. **Dependency monitoring**: Health checks verify critical dependencies are available
5. **User experience**: Users should never reach a pod that can't serve requests
6. **Debugging**: Health check responses provide diagnostic information

Without health checks, Kubernetes can't distinguish between a running process and a functioning application, leading to user-facing errors.

## Overview

Health check patterns for Java applications include liveness probes (is the app alive?), readiness probes (is the app ready for traffic?), and startup probes (has the app finished starting?). These enable Kubernetes to manage traffic routing, automatic recovery, and dependency monitoring. Implementation uses Spring Boot Actuator with custom HealthIndicator beans.

## References

- Kubernetes Probes: https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/
- Spring Boot Actuator: https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html
- HealthIndicator interface: https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/health/HealthIndicator.html
- Kubernetes Startup Probes: https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/#define-startup-probes
- "Kubernetes in Action" by Marko Lukša — Health check patterns
