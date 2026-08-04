# Kubernetes Fundamentals

## Overview

Kubernetes (K8s) is an open-source container orchestration platform that automates deployment, scaling, and management of containerized applications.

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Control Plane                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ API      │  │ etcd     │  │ Scheduler│  │ Controller│   │
│  │ Server   │  │          │  │          │  │ Manager  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────────┐
│                    Worker Nodes                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ kubelet  │  │ kube-    │  │ Container│  │ Pods     │   │
│  │          │  │ proxy    │  │ Runtime  │  │          │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Pods

### Basic Pod
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app
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

### Multi-Container Pod
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app-with-sidecar
spec:
  containers:
  - name: app
    image: my-app:1.0
  - name: sidecar
    image: fluentd:latest
    volumeMounts:
    - name: logs
      mountPath: /var/log
  volumes:
  - name: logs
    emptyDir: {}
```

## Services

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
  - port: 80
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

## Namespaces

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: my-namespace
  labels:
    name: my-namespace
```

## ConfigMaps

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  APP_ENV: production
  config.json: |
    {
      "database": {
        "host": "postgres-service",
        "port": 5432
      }
    }
```

## Secrets

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: my-secret
type: Opaque
data:
  username: YWRtaW4=  # base64 encoded
  password: cGFzc3dvcmQ=  # base64 encoded
```

## kubectl Commands

```bash
# Get resources
kubectl get pods
kubectl get services
kubectl get deployments

# Describe resources
kubectl describe pod my-pod

# Logs
kubectl logs my-pod
kubectl logs -f my-pod

# Execute command
kubectl exec -it my-pod -- /bin/sh

# Apply configuration
kubectl apply -f deployment.yaml

# Delete resources
kubectl delete pod my-pod
```

## Best Practices

1. **Use Deployments** - Don't create pods directly
2. **Set resource requests/limits** - Ensure proper scheduling
3. **Use namespaces** - Organize resources logically
4. **Implement health checks** - Use liveness and readiness probes
5. **Use ConfigMaps/Secrets** - Don't hardcode configuration
6. **Implement RBAC** - Control access to resources
7. **Use labels** - Organize and select resources
8. **Monitor resources** - Track CPU and memory usage
9. **Implement network policies** - Control traffic flow
10. **Use Helm** - Manage complex deployments
