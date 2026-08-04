# Kubernetes Probes

## Overview

Kubernetes probes determine container health through liveness, readiness, and startup checks.

## Probe Types

### Liveness Probe
Detects if container is running and needs restart.

```yaml
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: app
    livenessProbe:
      httpGet:
        path: /actuator/health/liveness
        port: 8080
      initialDelaySeconds: 30
      periodSeconds: 10
      timeoutSeconds: 5
      failureThreshold: 3
```

### Readiness Probe
Detects if container is ready to accept traffic.

```yaml
    readinessProbe:
      httpGet:
        path: /actuator/health/readiness
        port: 8080
      initialDelaySeconds: 10
      periodSeconds: 5
      timeoutSeconds: 3
      successThreshold: 1
      failureThreshold: 3
```

### Startup Probe
Handles slow-starting containers.

```yaml
    startupProbe:
      httpGet:
        path: /actuator/health
        port: 8080
      failureThreshold: 30
      periodSeconds: 10
```

## Spring Boot Configuration

```yaml
management:
  health:
    probes:
      enabled: true
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
  endpoint:
    health:
      probes:
        enabled: true
```

## Custom Health Indicators

```java
@Component
@ConditionalOnProperty(name = "management.health.probes.enabled", matchIfMissing = true)
public class DatabaseHealthIndicator implements HealthIndicator {
    private final DataSource dataSource;
    
    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            return Health.up().build();
        } catch (SQLException e) {
            return Health.down().withDetail("error", e.getMessage()).build();
        }
    }
}
```

## Probe Combinations

| Container Type | Liveness | Readiness | Startup |
|---------------|----------|-----------|---------|
| Fast startup | Yes | Yes | No |
| Slow startup | Yes | Yes | Yes |
| Batch job | No | No | Yes |
| Stateless | Yes | Yes | Optional |
| Stateful | Yes | Yes | Yes |

## Best Practices

1. Always configure liveness probe
2. Use readiness probe for traffic routing
3. Use startup probe for slow applications
4. Set appropriate timeouts
5. Implement graceful shutdown
6. Monitor probe failures
7. Use different endpoints for each probe
8. Test probe behavior under load
