# Kubernetes Patterns

## 1. Sidecar Pattern

**Problem:** Cross-cutting concerns (logging, monitoring, proxying) are tightly coupled to the application.

**Solution:** Deploy a helper container in the same pod that shares network and filesystem with the main container.

**Implementation:**
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: app-with-sidecar
spec:
  containers:
  - name: app
    image: myapp:1.0
    ports:
    - containerPort: 8080
  - name: log-collector
    image: fluentd:latest
    volumeMounts:
    - name: shared-logs
      mountPath: /var/log/app
  volumes:
  - name: shared-logs
    emptyDir: {}
```

**When to Use:** When you need to add observability, TLS termination, or configuration sync without modifying the application.

**When NOT to Use:** When the sidecar adds significant resource overhead or when the concern can be handled at the pod level.

---

## 2. Ambassador Pattern

**Problem:** Applications need to connect to external services with retry, circuit breaking, or protocol translation.

**Solution:** Deploy an ambassador container that proxies all outbound connections through a local endpoint.

**Implementation:**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: app-with-ambassador
spec:
  template:
    spec:
      containers:
      - name: app
        image: myapp:1.0
        env:
        - name: DB_HOST
          value: "localhost"
        - name: DB_PORT
          value: "5432"
      - name: ambassador
        image: envoyproxy/envoy:latest
        ports:
        - containerPort: 5432
        volumeMounts:
        - name: envoy-config
          mountPath: /etc/envoy
      volumes:
      - name: envoy-config
        configMap:
          name: envoy-config
```

**When to Use:** When applications need protocol-aware proxies for service mesh integration or legacy service connectivity.

**When NOT to Use:** When the application already implements its own retry and circuit breaker logic.

---

## 3. Init Container Pattern

**Problem:** Applications depend on external resources that may not be ready at startup, or require pre-processing.

**Solution:** Use init containers that run to completion before the main application containers start.

**Implementation:**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: app-with-init
spec:
  template:
    spec:
      initContainers:
      - name: wait-for-db
        image: busybox:1.36
        command: ['sh', '-c', 'until nc -z postgres-service 5432; do sleep 2; done']
      - name: migrate
        image: myapp-migrations:1.0
        command: ['npm', 'run', 'migrate']
      containers:
      - name: app
        image: myapp:1.0
```

**When to Use:** When you need dependency ordering, configuration downloads, or database migrations before application start.

**When NOT to Use:** When init containers run indefinitely or when the application can handle delayed readiness itself.

---

## 4. Operator Pattern

**Problem:** Complex stateful applications (databases, queues) require domain-specific operational knowledge beyond generic Kubernetes primitives.

**Solution:** Build a custom controller (operator) that encodes operational logic as code, managing custom resources.

**Implementation:**
```yaml
apiVersion: apiextensions.k8s.io/v1
kind: CustomResourceDefinition
metadata:
  name: postgresclusters.postgres.example.com
spec:
  group: postgres.example.com
  names:
    kind: PostgresCluster
    plural: postgresclusters
  scope: Namespaced
  versions:
  - name: v1
    served: true
    storage: true
---
apiVersion: postgres.example.com/v1
kind: PostgresCluster
metadata:
  name: my-db
spec:
  replicas: 3
  postgresVersion: "15"
  storage:
    size: 50Gi
```

**When to Use:** When managing stateful applications that need automated backup, failover, scaling, or upgrades.

**When NOT to Use:** When simple Deployments and Services suffice. Operators add significant development and maintenance cost.

---

## 5. DaemonSet Pattern

**Problem:** Infrastructure services (log collectors, node exporters) must run on every node in the cluster.

**Solution:** Use DaemonSet to guarantee one pod per node, automatically scheduling on new nodes.

**Implementation:**
```yaml
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: fluentd
  namespace: logging
spec:
  selector:
    matchLabels:
      app: fluentd
  template:
    metadata:
      labels:
        app: fluentd
    spec:
      containers:
      - name: fluentd
        image: fluentd:latest
        volumeMounts:
        - name: varlog
          mountPath: /var/log
          readOnly: true
      volumes:
      - name: varlog
        hostPath:
          path: /var/log
```

**When to Use:** When every node needs a consistent service: monitoring, logging, networking, or storage.

**When NOT to Use:** When services only need to run on a subset of nodes or when per-node footprint is too resource-heavy.

---

## 6. StatefulSet Pattern

**Problem:** Stateful applications need stable network identities, persistent storage, and ordered deployment.

**Solution:** StatefulSet provides stable pod names, ordered rolling updates, and volume claim templates.

**Implementation:**
```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: kafka
spec:
  serviceName: kafka-headless
  replicas: 3
  selector:
    matchLabels:
      app: kafka
  template:
    metadata:
      labels:
        app: kafka
    spec:
      containers:
      - name: kafka
        image: confluentinc/cp-kafka:7.5.0
        ports:
        - containerPort: 9092
        volumeMounts:
        - name: data
          mountPath: /var/lib/kafka/data
  volumeClaimTemplates:
  - metadata:
      name: data
    spec:
      accessModes: ["ReadWriteOnce"]
      resources:
        requests:
          storage: 100Gi
```

**When to Use:** Databases, message queues, and any workload requiring stable identity and persistent storage.

**When NOT to Use:** Stateless workloads or applications where pods are truly interchangeable.

---

## 7. Job and CronJob Pattern

**Problem:** Batch workloads and scheduled tasks must run to completion without leaving orphan pods.

**Solution:** Job runs pods to completion. CronJob triggers Job creation on a schedule.

**Implementation:**
```yaml
apiVersion: batch/v1
kind: CronJob
metadata:
  name: data-export
spec:
  schedule: "0 2 * * *"
  jobTemplate:
    spec:
      backoffLimit: 2
      template:
        spec:
          containers:
          - name: exporter
            image: data-exporter:1.0
            command: ["python", "export.py"]
          restartPolicy: Never
```

**When to Use:** Scheduled reports, data ETL, cleanup tasks, and any batch processing that runs on a schedule.

**When NOT to Use:** Long-running services or workloads that must be perpetually available (use Deployment instead).

---

## 8. Leader Election Pattern

**Problem:** Multiple replicas of a controller must have only one actively processing to avoid conflicts.

**Solution:** Use Kubernetes leader election via leases so one replica becomes the leader while others stand by.

**Implementation:**
```go
leaderelection.RunOrDie(ctx, leaderelection.LeaderElectionConfig{
    Lock:          &resourcelock.LeaseLock{...},
    LeaseDuration: 15 * time.Second,
    RenewDeadline: 10 * time.Second,
    RetryPeriod:   2 * time.Second,
    Callbacks: leaderelection.LeaderCallbacks{
        OnStartedLeading: func(ctx context.Context) {
            // Run controller logic
        },
        OnStoppedLeading: func() {
            // Re-elect or exit
        },
    },
})
```

**When to Use:** Controllers, schedulers, and any singleton process in a replicated deployment.

**When NOT to Use:** When all replicas can safely process independently without coordination.

---

## Best Practices

- Use namespace isolation to separate environments and teams.
- Set resource requests and limits on every container to enable scheduling and prevent OOM.
- Use PodDisruptionBudgets to maintain availability during voluntary disruptions.
- Prefer Deployment over ReplicaSet for declarative updates and rollbacks.
- Use network policies to restrict pod-to-pod communication by default.
- Monitor with Prometheus and alert on pod restarts, OOMKills, and pending pods.
- Tag pods with labels consistently for service discovery and monitoring.
