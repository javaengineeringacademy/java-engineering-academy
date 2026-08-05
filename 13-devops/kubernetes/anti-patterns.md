# Kubernetes Anti-Patterns

## 1. No Resource Limits
**Description:** Not setting resource requests and limits for pods.

**Why it's bad:** Pods can consume all cluster resources, cause OOM kills, affect other pods.

**Example (bad code):**
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: myapp
spec:
  containers:
  - name: myapp
    image: myapp:latest
    # No resources defined
```

**Better approach:** Set resource limits:
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: myapp
spec:
  containers:
  - name: myapp
    image: myapp:latest
    resources:
      requests:
        memory: "256Mi"
        cpu: "250m"
      limits:
        memory: "512Mi"
        cpu: "500m"
```

**Impact:** Resource management, cluster stability.

---

## 2. Host Networking
**Description:** Using host network for pods.

**Why it's bad:** Security risk, port conflicts, breaks network policies.

**Example (bad code):**
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: myapp
spec:
  hostNetwork: true
  containers:
  - name: myapp
    image: myapp:latest
```

**Better approach:** Use pod networking:
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: myapp
spec:
  containers:
  - name: myapp
    image: myapp:latest
    ports:
    - containerPort: 8080
```

**Impact:** Better isolation, network policy support.

---

## 3. Running as Root
**Description:** Running containers as root user.

**Why it's bad:** Security risk, container escape vulnerabilities.

**Example (bad code):**
```yaml
apiVersion: v1
kind: Pod
spec:
  securityContext: {}
  containers:
  - name: myapp
    image: myapp:latest
```

**Better approach:** Run as non-root:
```yaml
apiVersion: v1
kind: Pod
spec:
  securityContext:
    runAsNonRoot: true
    runAsUser: 1000
  containers:
  - name: myapp
    image: myapp:latest
    securityContext:
      allowPrivilegeEscalation: false
      readOnlyRootFilesystem: true
```

**Impact:** Improved security, reduced attack surface.

---

## 4. Using Latest Tag
**Description:** Using `latest` tag for container images.

**Why it's bad:** Unpredictable deployments, rollback difficulties.

**Example (bad code):**
```yaml
containers:
- name: myapp
  image: myapp:latest
```

**Better approach:** Pin specific versions:
```yaml
containers:
- name: myapp
  image: myapp:1.2.3
```

**Impact:** Reproducible deployments, easier rollbacks.

---

## 5. No Liveness/Readiness Probes
**Description:** Not defining health checks for pods.

**Why it's bad:** Kubernetes cannot detect unhealthy pods, traffic sent to unready pods.

**Example (bad code):**
```yaml
containers:
- name: myapp
  image: myapp:latest
  # No probes defined
```

**Better approach:** Add probes:
```yaml
containers:
- name: myapp
  image: myapp:latest
  livenessProbe:
    httpGet:
      path: /healthz
      port: 8080
    initialDelaySeconds: 30
    periodSeconds: 10
  readinessProbe:
    httpGet:
      path: /ready
      port: 8080
    initialDelaySeconds: 5
    periodSeconds: 5
```

**Impact:** Automatic recovery, proper traffic routing.

---

## 6. Using kubectl apply Without Review
**description:** Applying manifests without reviewing changes.

**Why it's bad:** Unintended changes, security risks.

**Example (bad code):**
```bash
kubectl apply -f manifests/
# No review of changes
```

**Better approach:** Review changes:
```bash
kubectl diff -f manifests/
kubectl apply -f manifests/
```

**Impact:** Controlled deployments, fewer accidents.

---

## 7. Not Using Namespaces
**Description:** Deploying everything in default namespace.

**Why it's bad:** Resource contention, no isolation, hard to manage.

**Example (bad code):**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: myapp
  # No namespace
```

**Better approach:** Use namespaces:
```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: production
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: myapp
  namespace: production
```

**Impact:** Better isolation, resource management.

---

## 8. Storing Secrets in ConfigMaps
**Description:** Storing sensitive data in ConfigMaps.

**Why it's bad:** Secrets exposed in plain text, security risk.

**Example (bad code):**
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: myapp-config
data:
  DB_PASSWORD: "secret123"
```

**Better approach:** Use Secrets:
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: myapp-secret
type: Opaque
data:
  DB_PASSWORD: c2VjcmV0MTIz  # base64 encoded
```

**Impact:** Better security, secret management.

---

## 9. Not Using Pod Disruption Budgets
**Description:** Not protecting pods during voluntary disruptions.

**Why it's bad:** Can cause downtime during node drains or cluster upgrades.

**Example (bad code):**
```yaml
# No PDB defined
# Node drain can remove all pods
```

**Better approach:** Define PDB:
```yaml
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: myapp-pdb
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: myapp
```

**Impact:** High availability during maintenance.

---

## 10. Ignoring Pod Topology Constraints
**Description:** Not specifying pod placement constraints.

**Why it's bad:** Pods may not spread across failure domains.

**Example (bad code):**
```yaml
# Pods may all land on same node
```

**Better approach:** Use topology spread constraints:
```yaml
topologySpreadConstraints:
- maxSkew: 1
  topologyKey: kubernetes.io/hostname
  whenUnsatisfiable: DoNotSchedule
  labelSelector:
    matchLabels:
      app: myapp
```

**Impact:** Better availability, failure domain distribution.

---

## 11. Using kubectl exec for Debugging
**Description:** Using exec to debug production pods.

**Why it's bad:** Security risk, ephemeral debugging, not reproducible.

**Example (bad code):**
```bash
kubectl exec -it mypod -- /bin/sh
# Interactive debugging in production
```

**Better approach:** Use ephemeral containers or debug pods:
```bash
kubectl debug -it mypod --image=busybox
```

**Impact:** Safer debugging, reproducible.

---

## 12. Not Using Network Policies
**Description:** Not restricting pod-to-pod communication.

**Why it's bad:** No network isolation, security risk.

**Example (bad code):**
```yaml
# All pods can communicate with each other
```

**Better approach:** Define network policies:
```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: myapp-netpol
spec:
  podSelector:
    matchLabels:
      app: myapp
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - podSelector:
        matchLabels:
          app: frontend
```

**Impact:** Network isolation, improved security.