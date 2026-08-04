# Kubernetes Deployments

## Overview

Deployments provide declarative updates for Pods and ReplicaSets. They manage the desired state of your application.

## Basic Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
  labels:
    app: my-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-app
        image: my-app:1.0
        ports:
        - containerPort: 3000
        resources:
          requests:
            memory: "128Mi"
            cpu: "250m"
          limits:
            memory: "256Mi"
            cpu: "500m"
```

## Rollout Strategies

### Rolling Update
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-app
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 1
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-app
        image: my-app:2.0
```

### Recreate Strategy
```yaml
spec:
  strategy:
    type: Recreate
```

## Rollout Commands

```bash
# Check rollout status
kubectl rollout status deployment/my-app

# View rollout history
kubectl rollout history deployment/my-app

# Rollback to previous version
kubectl rollout undo deployment/my-app

# Rollback to specific revision
kubectl rollout undo deployment/my-app --to-revision=2

# Pause rollout
kubectl rollout pause deployment/my-app

# Resume rollout
kubectl rollout resume deployment/my-app
```

## ReplicaSets

```yaml
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: my-rs
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-app
  template:
    metadata:
      labels:
        app: my-app
    spec:
      containers:
      - name: my-app
        image: my-app:1.0
```

## Advanced Configurations

### Pod Disruption Budget
```yaml
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: my-pdb
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: my-app
```

### Horizontal Pod Autoscaler
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: my-hpa
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

## Best Practices

1. **Use rolling updates** - Minimize downtime
2. **Implement PDBs** - Protect against voluntary disruptions
3. **Set resource limits** - Prevent resource exhaustion
4. **Use probes** - Ensure application health
5. **Implement scaling** - Use HPA for automatic scaling
6. **Monitor rollouts** - Track deployment progress
7. **Use annotations** - Add metadata for tools
8. **Implement labels** - Organize resources logically
9. **Test deployments** - Use staging environments
10. **Document deployments** - Add descriptions for complex configs
