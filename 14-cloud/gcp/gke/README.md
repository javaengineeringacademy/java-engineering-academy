# Google Kubernetes Engine (GKE)

## Overview

GKE is a managed Kubernetes service for deploying, managing, and scaling containerized applications.

## Cluster Types

| Type           | Description                          | Use Case              |
|----------------|--------------------------------------|-----------------------|
| Standard       | Full control over cluster            | Production workloads  |
| Autopilot      | Fully managed nodes                  | Simple workloads      |
| Edge           | On-premises clusters                 | Hybrid deployments    |

## Creating Clusters

### Standard Cluster
```bash
# Create standard cluster
gcloud container clusters create my-cluster \
  --zone us-central1-a \
  --num-nodes 3 \
  --machine-type e2-standard-4 \
  --enable-autoscaling --min-nodes 1 --max-nodes 10 \
  --enable-autoupgrade \
  --enable-autorepair

# Get credentials
gcloud container clusters get-credentials my-cluster --zone us-central1-a
```

### Autopilot Cluster
```bash
# Create autopilot cluster
gcloud container clusters create-auto my-autopilot-cluster \
  --region us-central1

# Benefits:
# - No node management
# - Automatic right-sizing
# - Built-in security
```

## Node Pools

```bash
# Create node pool
gcloud container node-pools create my-pool \
  --cluster my-cluster \
  --zone us-central1-a \
  --num-nodes 3 \
  --machine-type e2-standard-4 \
  --enable-autoscaling --min-nodes 1 --max-nodes 10

# Update node pool
gcloud container node-pools update my-pool \
  --cluster my-cluster \
  --zone us-central1-a \
  --machine-type e2-standard-8

# Delete node pool
gcloud container node-pools delete my-pool \
  --cluster my-cluster \
  --zone us-central1-a
```

### Node Pool Configuration
```yaml
apiVersion: container/v1
kind: NodePool
metadata:
  name: my-pool
spec:
  machineType: e2-standard-4
  initialNodeCount: 3
  autoscaling:
    minNodeCount: 1
    maxNodeCount: 10
  management:
    autoRepair: true
    autoUpgrade: true
```

## Workload Identity

```bash
# Enable Workload Identity
gcloud container clusters update my-cluster \
  --zone us-central1-a \
  --workload-pool=my-project.svc.id.goog

# Create Kubernetes service account
kubectl create serviceaccount my-sa --namespace default

# Create IAM service account
gcloud iam service-accounts create my-gsa

# Bind them
gcloud iam service-accounts add-iam-policy-binding my-gsa \
  --role roles/iam.workloadIdentityUser \
  --member "serviceAccount:my-project.svc.id.goog[default/my-sa]"

# Annotate Kubernetes service account
kubectl annotate serviceaccount my-sa \
  iam.gke.io/gcp-service-account=my-gsa@my-project.iam.gserviceaccount.com
```

## Pod Security

### Pod Security Standards
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: production
  labels:
    pod-security.kubernetes.io/enforce: restricted
```

### Network Policies
```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-frontend
spec:
  podSelector:
    matchLabels:
      role: frontend
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          role: backend
```

## Ingress

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: my-ingress
  annotations:
    kubernetes.io/ingress.class: gce
    kubernetes.io/ingress.global-static-ip-name: my-static-ip
spec:
  rules:
  - host: myapp.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: my-service
            port:
              number: 80
```

## Managed Certificates

```yaml
apiVersion: networking.gke.io/v1
kind: ManagedCertificate
metadata:
  name: my-cert
spec:
  domains:
  - myapp.example.com
```

## ConfigMaps & Secrets

### ConfigMap
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config
data:
  DB_HOST: "mydb"
  DB_PORT: "3306"
```

### Secret
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: my-secret
type: Opaque
data:
  DB_PASSWORD: cGFzc3dvcmQ=
```

## Horizontal Pod Autoscaling

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: my-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: my-deployment
  minReplicas: 1
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

## Cluster Autoscaler

```bash
# Enable cluster autoscaler
gcloud container clusters update my-cluster \
  --zone us-central1-a \
  --enable-autoscaling \
  --min-nodes 1 \
  --max-nodes 10
```

## Maintenance Windows

```bash
# Set maintenance window
gcloud container clusters update my-cluster \
  --zone us-central1-a \
  --maintenance-window-start="2024-01-15T02:00:00Z" \
  --maintenance-window-end="2024-01-15T06:00:00Z" \
  --maintenance-window-recurrence="FREQ=WEEKLY;BYDAY=MO,TU,WE,TH,FR"
```

## Monitoring

```bash
# Enable monitoring
gcloud container clusters update my-cluster \
  --zone us-central1-a \
  --enable-stackdriver-kubernetes

# Get cluster metrics
gcloud monitoring metrics list \
  --filter='metric.type="kubernetes.io/container/restart_count"'
```

## Cost Optimization

- **Use Autopilot** for simple workloads
- **Use Spot VMs** for node pools
- **Right-size nodes** based on workload
- **Implement cluster autoscaling**
- **Use Preemptible VMs** for dev/test

## Best Practices

1. **Use Autopilot** for managed experience
2. **Implement Workload Identity** for security
3. **Use network policies** for isolation
4. **Enable monitoring** and logging
5. **Implement proper RBAC**
6. **Use ConfigMaps/Secrets** for configuration
7. **Enable auto-upgrade** for nodes
8. **Use node pools** for workload isolation
9. **Implement proper scaling** policies
10. **Regular cluster upgrades**
