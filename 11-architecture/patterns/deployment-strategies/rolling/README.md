# Rolling Update

## Overview

Incrementally replace instances with new version.

## Configuration (Kubernetes)

```yaml
apiVersion: apps/v1
kind: Deployment
spec:
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 25%
      maxUnavailable: 25%
```

## Process

1. Update one instance
2. Verify health
3. Update next instance
4. Repeat until complete

## Benefits

- Zero downtime
- Resource efficient
- Gradual rollout
