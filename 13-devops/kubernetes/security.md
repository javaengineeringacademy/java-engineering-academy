# Kubernetes Security

## RBAC (Role-Based Access Control)

### ClusterRoles and Roles

ClusterRoles define permissions cluster-wide; Roles are namespace-scoped. Use the principle of least privilege.

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: production
  name: pod-manager
rules:
- apiGroups: [""]
  resources: ["pods", "pods/log", "pods/exec"]
  verbs: ["get", "list", "watch", "create", "delete"]
- apiGroups: ["apps"]
  resources: ["deployments"]
  verbs: ["get", "list", "watch"]
```

### RoleBindings

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: dev-binding
  namespace: production
subjects:
- kind: User
  name: dev-user
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: Role
  name: pod-manager
  apiGroup: rbac.authorization.k8s.io
```

### Best Practices

- Never use cluster-admin unless absolutely necessary
- Create roles per namespace and per team
- Use ServiceAccounts for workloads, not user accounts
- Audit RBAC policies regularly
- Avoid wildcard permissions

## Pod Security Standards

Replace deprecated PodSecurityPolicy with Pod Security Standards (PSS) and Pod Security Admission (PSA).

### Security Levels

- **Privileged**: Unrestricted, allows everything
- **Baseline**: Prevents known privilege escalations
- **Restricted**: Hardened, follows pod security best practices

### Pod Security Admission

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: production
  labels:
    pod-security.kubernetes.io/enforce: restricted
    pod-security.kubernetes.io/audit: restricted
    pod-security.kubernetes.io/warn: restricted
```

### Security Context

```yaml
securityContext:
  runAsNonRoot: true
  runAsUser: 1000
  runAsGroup: 3000
  fsGroup: 2000
  seccompProfile:
    type: RuntimeDefault
containers:
- name: app
  securityContext:
    allowPrivilegeEscalation: false
    readOnlyRootFilesystem: true
    capabilities:
      drop:
        - ALL
```

## Network Policies

Control traffic flow between Pods, namespaces, and external endpoints. Default deny all ingress and egress, then allow specific traffic.

### Default Deny All

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: default-deny-all
  namespace: production
spec:
  podSelector: {}
  policyTypes:
  - Ingress
  - Egress
```

### Allow Specific Traffic

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-frontend-to-backend
  namespace: production
spec:
  podSelector:
    matchLabels:
      app: backend
  policyTypes:
  - Ingress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          app: frontend
    ports:
    - protocol: TCP
      port: 8080
```

### Egress Rules

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: allow-dns-and-db
  namespace: production
spec:
  podSelector:
    matchLabels:
      app: backend
  policyTypes:
  - Egress
  egress:
  - to:
    - namespaceSelector: {}
    ports:
    - protocol: UDP
      port: 53
  - to:
    - podSelector:
        matchLabels:
          app: database
    ports:
    - protocol: TCP
      port: 5432
```

## Secrets Encryption

### Encryption at Rest

Enable encryption for secrets stored in etcd using EncryptionConfiguration.

```yaml
apiVersion: apiserver.config.k8s.io/v1
kind: EncryptionConfiguration
resources:
  - resources:
    - secrets
    providers:
    - aescbc:
        keys:
        - name: key1
          secret: <base64-encoded-secret>
    - identity: {}
```

### External Secret Stores

Use external secret managers instead of Kubernetes secrets for production:

- **HashiCorp Vault**: Enterprise-grade secret management
- **AWS Secrets Manager**: Native AWS integration
- **Azure Key Vault**: Azure native integration
- **Sealed Secrets**: Encrypt secrets for Git storage

### Sealed Secrets Example

```yaml
apiVersion: bitnami.com/v1alpha1
kind: SealedSecret
metadata:
  name: my-secret
  namespace: production
spec:
  encryptedData:
    password: AgBy3i4OJSWK+PiTySYZZA9rO...
```

## Image Security

### Image Scanning

- Scan images for vulnerabilities before deployment
- Use Trivy, Clair, or Snyk for vulnerability scanning
- Block images with critical vulnerabilities

### Image Policies

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: image-policy
data:
  image-policy.yaml: |
    apiVersion: imagepolicy.k8s.io/v1alpha1
    kind: ImagePolicy
    name: image-policy
    spec:
      rejectionRules:
      - name: block-critical-vulns
        imageRegex: ".*"
        vulnerabilityThreshold: Critical
```

### Signed Images

Use Cosign or Notary for image signing and verification.

## Runtime Security

### AppArmor Profiles

```yaml
securityContext:
  appArmorProfile:
    type: Localhost
    localhostProfile: k8s-apparmor-example
```

### Seccomp Profiles

```yaml
securityContext:
  seccompProfile:
    type: RuntimeDefault
```

## Audit Logging

Enable Kubernetes audit logging to track API access and changes.

```yaml
apiVersion: audit.k8s.io/v1
kind: Policy
rules:
- level: RequestResponse
  resources:
  - group: ""
    resources: ["secrets", "configmaps"]
- level: Metadata
  resources:
  - group: ""
    resources: ["pods", "services"]
```

## CIS Benchmarks

Run kube-bench to check cluster against CIS Kubernetes Benchmark:

```bash
# Run kube-bench
kubectl run kube-bench --image=aquasec/kube-bench --restart=Never

# Or run on node
kube-bench run --targets master,node
```
