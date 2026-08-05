# Kubernetes Pitfalls

## 1. No Resource Limits

Without resource limits, a single container can consume all node resources, starving other Pods. This causes OOM kills and cluster instability.

```yaml
# BAD: No limits
containers:
- name: app
  image: my-app

# GOOD: With limits
containers:
- name: app
  image: my-app
  resources:
    requests:
      cpu: "250m"
      memory: "256Mi"
    limits:
      cpu: "500m"
      memory: "512Mi"
```

## 2. Host Networking

Using hostNetwork gives Pods access to the node's network namespace, bypassing network policies and isolation. This is a security risk.

```yaml
# BAD: Host networking
spec:
  hostNetwork: true

# GOOD: Use Services for networking
spec:
  containers:
  - name: app
    ports:
    - containerPort: 8080
```

## 3. Running as Root

Containers running as root have elevated privileges and can escape the container if compromised.

```yaml
# BAD: Running as root
containers:
- name: app
  image: my-app

# GOOD: Run as non-root
securityContext:
  runAsNonRoot: true
  runAsUser: 1000
  runAsGroup: 3000
containers:
- name: app
  image: my-app
  securityContext:
    allowPrivilegeEscalation: false
    readOnlyRootFilesystem: true
```

## 4. Missing Health Checks

Without health checks, Kubernetes cannot detect and recover from application failures. Deadlocks and crashes go unnoticed.

```yaml
# BAD: No health checks
containers:
- name: app
  image: my-app

# GOOD: With health checks
containers:
- name: app
  image: my-app
  readinessProbe:
    httpGet:
      path: /healthz
      port: 8080
    initialDelaySeconds: 5
    periodSeconds: 10
  livenessProbe:
    httpGet:
      path: /healthz
      port: 8080
    initialDelaySeconds: 15
    periodSeconds: 20
```

## 5. Using latest Tag

Using the latest tag makes deployments non-deterministic. You cannot rollback to a known good state.

```yaml
# BAD: Using latest
containers:
- name: app
  image: my-app:latest

# GOOD: Use specific version
containers:
- name: app
  image: my-app:v1.2.3
```

## 6. Secrets in Git

Storing secrets in Git repositories exposes credentials. Use external secret managers or sealed secrets.

```yaml
# BAD: Secret in Git
apiVersion: v1
kind: Secret
metadata:
  name: db-secret
data:
  password: cGFzc3dvcmQ=  # base64, not encrypted

# GOOD: Use external secret store
apiVersion: external-secrets.io/v1beta1
kind: ExternalSecret
metadata:
  name: db-secret
spec:
  secretStoreRef:
    name: aws-secrets-manager
    kind: ClusterSecretStore
  target:
    name: db-secret
  data:
  - secretKey: password
    remoteRef:
      key: prod/db/password
```

## 7. No Network Policies

Without network policies, all Pods can communicate with each other. This increases the attack surface.

```yaml
# Default deny all
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: default-deny
spec:
  podSelector: {}
  policyTypes:
  - Ingress
  - Egress
```

## 8. Ignoring Pod Disruption Budgets

Without PDBs, voluntary disruptions (node drains, upgrades) can cause downtime.

```yaml
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: my-app-pdb
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: my-app
```

## 9. Not Using Namespaces

Running everything in the default namespace makes organization and access control difficult.

## 10. Hardcoded Configuration

Hardcoding values in manifests prevents environment-specific configurations. Use ConfigMaps and environment variables.

## 11. Missing Graceful Shutdown

Applications that don't handle SIGTERM cause request drops during deployments.

```yaml
lifecycle:
  preStop:
    exec:
      command: ["/bin/sh", "-c", "sleep 15"]
terminationGracePeriodSeconds: 30
```

## 12. Over-Scaling

Setting too many replicas wastes resources. Start small and use HPA for dynamic scaling.

## 13. Ignoring Node Affinity

Not using node affinity can cause Pods to be scheduled on inappropriate nodes.

## 14. Not Monitoring

Without monitoring, you cannot detect issues before they cause outages. Deploy Prometheus and Grafana.

## 15. Manual Deployments

Making manual changes to production bypasses change control. Always use GitOps and CI/CD pipelines.
