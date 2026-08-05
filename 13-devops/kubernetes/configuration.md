# Kubernetes Configuration

## kubeconfig

The kubeconfig file contains cluster connection information, user credentials, and context settings. Default location is ~/.kube/config. KUBECONFIG environment variable can override this path.

```yaml
apiVersion: v1
kind: Config
current-context: my-cluster
clusters:
- cluster:
    server: https://k8s.example.com
    certificate-authority: ca.crt
  name: my-cluster
contexts:
- context:
    cluster: my-cluster
    user: admin
    namespace: production
  name: my-cluster
users:
- name: admin
  user:
    client-certificate: admin.crt
    client-key: admin.key
```

### kubeconfig Management

- Use kubectl config view to inspect configuration
- Set context with kubectl config use-context
- Merge multiple kubeconfig files with KUBECONFIG environment variable
- Use kubectx and kubens for quick context/namespace switching
- Never commit kubeconfig files with credentials to version control

## Resource Requests and Limits

Define compute resources for containers. Requests guarantee minimum resources; limits enforce maximum. Always set both in production to prevent resource starvation and OOM kills.

```yaml
resources:
  requests:
    memory: "256Mi"
    cpu: "500m"
  limits:
    memory: "512Mi"
    cpu: "1000m"
```

### Resource Units

- CPU: Measured in cores (1 CPU = 1000m). Can use decimals (0.5) or millicores (500m)
- Memory: Measured in bytes. Suffixes: Ki, Mi, Gi, Ti for binary; K, M, G, T for decimal
- Ephemeral Storage: Measured in bytes with same suffixes as memory

## Labels and Annotations

Labels are key-value pairs for identifying and selecting objects. Annotations are key-value pairs for storing non-identifying metadata. Both use RFC 6902 JSON Patch format.

### Labels

```yaml
metadata:
  labels:
    app: web-server
    tier: frontend
    environment: production
    version: v1.2.3
```

### Annotations

```yaml
metadata:
  annotations:
    description: "Main web server for the application"
    prometheus.io/scrape: "true"
    prometheus.io/port: "9090"
```

## Horizontal Pod Autoscaler (HPA)

Automatically scales the number of Pods based on observed CPU utilization or custom metrics. Requires metrics-server to be installed. Supports target CPU, memory, or custom metrics.

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: my-app-hpa
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
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
```

## Init Containers

Run before the main application container in a Pod. Used for setup tasks, configuration, and dependencies. Init containers run to completion before the main container starts.

```yaml
initContainers:
- name: init-db
  image: busybox
  command: ['sh', '-c', 'until nslookup db-service; do sleep 2; done']
```

## Environment Variables

Inject configuration into containers via environment variables. Can reference ConfigMaps, Secrets, or field metadata from the Pod spec.

```yaml
env:
- name: DATABASE_HOST
  value: "mysql-service"
- name: DATABASE_PASSWORD
  valueFrom:
    secretKeyRef:
      name: db-secret
      key: password
- name: POD_NAME
  valueFrom:
    fieldRef:
      fieldPath: metadata.name
```

## ConfigMaps and Secrets as Volumes

Mount ConfigMaps and Secrets as files inside containers. Changes to ConfigMaps/Secrets are automatically reflected in mounted volumes.

```yaml
volumes:
- name: config-volume
  configMap:
    name: app-config
- name: secret-volume
  secret:
    secretName: app-secrets
containers:
- volumeMounts:
  - name: config-volume
    mountPath: /etc/config
    readOnly: true
  - name: secret-volume
    mountPath: /etc/secrets
    readOnly: true
```

## Resource Quotas

Limit total resource consumption per namespace. Prevents single teams from consuming all cluster resources. Can limit CPU, memory, storage, and object counts.

```yaml
apiVersion: v1
kind: ResourceQuota
metadata:
  name: compute-quota
  namespace: production
spec:
  hard:
    requests.cpu: "10"
    requests.memory: 20Gi
    limits.cpu: "20"
    limits.memory: 40Gi
    pods: "50"
```

## LimitRanges

Set default and maximum resource limits for containers in a namespace. Ensures all containers have resource constraints even if not specified.

```yaml
apiVersion: v1
kind: LimitRange
metadata:
  name: default-limits
  namespace: production
spec:
  limits:
  - default:
      cpu: "1"
      memory: 512Mi
    defaultRequest:
      cpu: 100m
      memory: 128Mi
    type: Container
```
