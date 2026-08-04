# Load Balancer Health Checks

## Overview

Load balancers use health checks to route traffic only to healthy instances, improving application availability and user experience.

## Health Check Types

### HTTP Health Check
```nginx
# NGINX upstream health check
upstream backend {
    server 10.0.0.1:8080;
    server 10.0.0.2:8080;
    
    health_check interval=10 fails=3 passes=2;
}
```

### TCP Health Check
```yaml
# AWS ALB Target Group
health_check:
  protocol: TCP
  port: 8080
  healthy_threshold: 2
  unhealthy_threshold: 3
  interval: 10
```

### Application Health Endpoint
```java
@RestController
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/health/ready")
    public ResponseEntity<Map<String, String>> ready() {
        // Check dependencies
        if (isDatabaseUp() && isCacheUp()) {
            return ResponseEntity.ok(Map.of("status", "READY"));
        }
        return ResponseEntity.status(503).body(Map.of("status", "NOT_READY"));
    }
}
```

## Cloud Provider Configuration

### AWS ALB
```json
{
  "HealthCheckPath": "/actuator/health",
  "HealthCheckIntervalSeconds": 30,
  "HealthCheckTimeoutSeconds": 5,
  "HealthyThresholdCount": 2,
  "UnhealthyThresholdCount": 3
}
```

### GCP Load Balancer
```yaml
healthCheck:
  type: HTTP
  requestPath: /health
  port: 8080
  checkIntervalSec: 10
  timeoutSec: 5
  healthyThreshold: 2
  unhealthyThreshold: 3
```

## Health Check Response

```json
{
  "status": "UP",
  "components": {
    "database": {"status": "UP"},
    "cache": {"status": "UP"},
    "diskSpace": {"status": "UP", "total": "10GB", "free": "5GB"}
  }
}
```

## Best Practices

1. Return appropriate HTTP status codes
2. Include dependency health status
3. Implement separate liveness/readiness endpoints
4. Set reasonable timeouts
5. Avoid expensive checks in health endpoints
6. Cache health status
7. Monitor health check metrics
8. Implement graceful degradation
