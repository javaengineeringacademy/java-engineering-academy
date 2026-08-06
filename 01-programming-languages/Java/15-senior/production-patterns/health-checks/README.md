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
