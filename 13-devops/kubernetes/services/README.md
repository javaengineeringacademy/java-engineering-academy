# Kubernetes Services

## Overview

Services provide stable network endpoints for Pods. They enable communication between different parts of your application.

## Service Types

### ClusterIP
```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-service
spec:
  type: ClusterIP
  selector:
    app: my-app
  ports:
  - protocol: TCP
    port: 80
    targetPort: 3000
```

### NodePort
```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-nodeport
spec:
  type: NodePort
  selector:
    app: my-app
  ports:
  - port: 80
    targetPort: 3000
    nodePort: 30080
```

### LoadBalancer
```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-loadbalancer
  annotations:
    service.beta.kubernetes.io/aws-load-balancer-type: nlb
spec:
  type: LoadBalancer
  selector:
    app: my-app
  ports:
  - port: 80
    targetPort: 3000
```

### ExternalName
```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-external
spec:
  type: ExternalName
  externalName: external-service.example.com
```

## Headless Services

```yaml
apiVersion: v1
kind: Service
metadata:
  name: my-headless
spec:
  clusterIP: None
  selector:
    app: my-app
  ports:
  - port: 80
```

## Service Discovery

```yaml
# Using environment variables
apiVersion: v1
kind: Pod
metadata:
  name: my-pod
spec:
  containers:
  - name: my-app
    env:
    - name: SERVICE_HOST
      value: "my-service"
    - name: SERVICE_PORT
      value: "80"

# Using DNS
# my-service.my-namespace.svc.cluster.local
```

## Best Practices

1. **Use ClusterIP** - Default for internal services
2. **Use NodePort** - For development/testing only
3. **Use LoadBalancer** - For external access
4. **Implement health checks** - Ensure service availability
5. **Use selectors** - Target specific Pods
6. **Monitor services** - Track endpoints and health
7. **Use annotations** - Configure cloud providers
8. **Implement network policies** - Control traffic flow
9. **Document services** - Add descriptions for complex configs
10. **Test service discovery** - Ensure proper DNS resolution
