# Spring Boot Actuator

## Overview

Spring Boot Actuator provides production-ready features for monitoring and managing applications through HTTP endpoints and JMX.

## Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## Configuration

```yaml
management:
  endpoints:
    web:
      base-path: /actuator
      exposure:
        include: health,info,metrics,prometheus,env,loggers
  endpoint:
    health:
      show-details: when_authorized
    shutdown:
      enabled: true
  health:
    db:
      enabled: true
    diskspace:
      enabled: true
```

## Health Endpoints

### Default Health Indicator
```java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        try {
            checkService();
            return Health.up()
                .withDetail("service", "available")
                .withDetail("version", "1.0.0")
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

### Composite Health
```yaml
management:
  health:
    roles: admin
    probes:
      enabled: true
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
```

## Custom Endpoints

```java
@Component
@Endpoint(id = "cache")
public class CacheEndpoint {
    private final CacheManager cacheManager;
    
    @ReadOperation
    public Map<String, Object> getCacheStats() {
        return cacheManager.getCacheNames().stream()
            .collect(Collectors.toMap(
                name -> name,
                name -> getStats(cacheManager.getCache(name))
            ));
    }
    
    @WriteOperation
    public void clearCache(@Selector String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }
}
```

## Metrics Endpoints

```bash
# Application metrics
GET /actuator/metrics/jvm.memory.used
GET /actuator/metrics/http.server.requests

# Prometheus format
GET /actuator/prometheus

# Environment
GET /actuator/env

# Loggers
GET /actuator/loggers
PUT /actuator/loggers/com.example
```

## Security

```java
@Configuration
public class ActuatorSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeRequests()
            .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
            .anyRequest().hasRole("ADMIN");
    }
}
```

## Best Practices

1. Enable only necessary endpoints
2. Secure actuator endpoints
3. Use separate management port
4. Customize health indicators
5. Expose Prometheus metrics
6. Monitor actuator performance
7. Use management context for isolation
8. Document available endpoints
