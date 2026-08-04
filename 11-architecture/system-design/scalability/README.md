# Scalability Patterns

## Horizontal Scaling

```yaml
# Kubernetes HPA
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: my-app
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

## Database Sharding

- Hash-based sharding
- Range-based sharding
- Directory-based sharding

## Caching Strategies

- Cache-aside
- Write-through
- Write-behind
- Read-through
