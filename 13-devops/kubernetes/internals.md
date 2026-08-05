# Kubernetes Internals

## Control Plane Architecture

The Kubernetes control plane consists of several components that collectively manage cluster state. The API server is the central hub; all communication flows through it. The scheduler assigns pods to nodes based on resource requirements and constraints. The controller manager runs reconciliation loops to maintain desired state. In managed clusters, these components are distributed across multiple nodes for high availability.

The control plane maintains the desired state of all resources (Deployments, Services, ConfigMaps) in etcd. The API server validates and persists objects to etcd. Controllers watch the API server for changes and take action to converge the actual state toward the desired state. This reconciliation loop is the core of Kubernetes' declarative model.

The control plane runs as static pods on control plane nodes. The kubelet on control plane nodes manages these static pods. Managed Kubernetes services (EKS, GKE, AKS) run the control plane as a managed service. Self-managed clusters require manual installation and maintenance of control plane components.

The control plane components communicate via the API server. Controllers use watches to detect changes. The scheduler uses a cache of cluster state derived from API server watches. The controller manager uses informers to watch resources. The kubelet watches the API server for pod assignments.

## etcd Key-Value Store

etcd is a distributed, consistent key-value store using the Raft consensus algorithm. Kubernetes stores all cluster state in etcd, including node registrations, pod definitions, secrets, and configuration. Raft ensures linearizability through leader election and log replication. A quorum (majority of nodes) is required for writes, providing fault tolerance.

etcd watches enable efficient change notification. The API server uses etcd watches to detect state changes and propagate them to controllers and kubelets. etcd compaction removes historical revisions to manage disk usage. Defragmentation reclaims space after compaction. Backup strategies typically use `etcdctl snapshot save` to capture point-in-time cluster state.

etcd performance depends on disk I/O latency. SSDs are recommended for etcd nodes. The `--quota-backend-bytes` flag limits etcd storage (default 8GB). The `--auto-compaction-retention` flag automates compaction. Cluster scaling follows the 3 or 5 node pattern for fault tolerance. The `etcdctl endpoint health` command monitors etcd health.

etcd uses a write-ahead log for durability. Each write is first appended to the log, then applied to the in-memory store. The log is truncated after snapshots. The `--snapshot-count` flag controls snapshot frequency. The `--wal-dir` flag specifies the write-ahead log directory. The `--data-dir` flag specifies the data directory.

## API Server

The kube-apiserver implements the Kubernetes API. It handles RESTful requests, validates objects against OpenAPI schemas, and persists them to etcd. The API server supports multiple versions simultaneously, enabling rolling upgrades of clients. Admission controllers intercept requests after authentication and authorization.

Mutating admission controllers modify objects (e.g., injecting sidecars via Istio). Validating admission controllers enforce policies (e.g., PodSecurityPolicy). The API server uses watches and bookmarks to efficiently stream changes to clients. API priority and fairness (APF) throttles requests to prevent overload, using a queue-based system with priority levels.

The API server exposes several APIs: core API, apps API, batch API, and custom resources. The `/healthz` and `/readyz` endpoints indicate API server health. The API server uses TLS for client authentication. RBAC (Role-Based Access Control) authorizes API requests based on roles and bindings.

The API server supports watch bookmarks for efficient change tracking. Watch bookmarks notify clients of the current resource version. The API server uses pagination for large list operations. The `--max-requests-inflight` flag limits concurrent non-mutating requests. The `--max-mutating-requests-inflight` flag limits concurrent mutating requests.

## Scheduler

The kube-scheduler assigns pods to nodes through a two-phase process. Filtering eliminates nodes that cannot host the pod (resource constraints, taints, affinity rules). Scoring ranks the remaining nodes by preference (resource balance, affinity, topology spread). The highest-scoring node is selected.

The scheduler uses a cache of cluster state derived from API server watches. Preemption allows high-priority pods to evict lower-priority pods when resources are insufficient. The scheduler respects pod disruption budgets, topology constraints, and inter-pod affinity/anti-affinity rules. Scheduler profiles allow customization of filtering and scoring plugins per workload type.

The scheduler runs as a single replica; leader election ensures only one scheduler is active. Scheduler extenders allow custom filtering and scoring logic. The `kube-scheduler --profile` flag configures scheduler profiles. The `kubectl describe pod` output shows scheduling decisions and preemption events.

The scheduler uses a plugin architecture for extensibility. Plugins are registered for each extension point: queue sort, pre-filter, filter, post-filter, scoring, reserve, permit, pre-bind, bind, post-bind, and unreserve. The scheduler profile configuration enables/disables plugins per extension point.

## Kubelet Agent

The kubelet runs on every node and manages pod lifecycle. It watches the API server for pod assignments and ensures containers run and remain healthy. The kubelet communicates with the container runtime via the Container Runtime Interface (CRI). It pulls images, creates containers, and manages volumes.

The kubelet reports node status including resource availability, conditions, and runtime information. Liveness and readiness probes are executed by the kubelet; failed liveness probes trigger container restarts. The kubelet manages pod sandboxes, configures networking via the Container Network Interface (CNI), and handles pod eviction when node resources are exhausted.

The kubelet exposes a summary API for node metrics. The `kubectl top nodes` command queries kubelet metrics. The kubelet manages image garbage collection based on disk usage thresholds. The kubelet registers the node with the API server using a bootstrap token. The `--register-schedulable` flag controls whether the node is schedulable.

The kubelet manages the pod lifecycle: pulling images, creating containers, starting/stopping containers, and reporting status. The kubelet executes liveness, readiness, and startup probes. The kubelet manages container restarts based on restart policies (Always, OnFailure, Never). The kubelet handles pod eviction when node resources are exhausted.

## Controller Manager

The controller manager runs multiple controllers, each implementing a reconciliation loop. The Deployment controller manages ReplicaSets and rolling updates. The Node controller monitors node health and evicts pods from failed nodes. The Service controller provisions load balancers. The EndpointSlice controller maintains service-to-pod mappings.

Controllers use informers to watch resources and maintain a local cache. When a discrepancy is detected between desired and actual state, the controller takes corrective action. Work queues decouple watch events from reconciliation, providing retry and rate limiting. Leader election ensures only one instance of each controller is active in HA configurations.

The controller manager runs multiple controllers: deployment, replicaset, statefulset, daemonset, job, cronjob, service, endpoint, endpoint-slice, node, namespace, and garbage collection. Each controller has its own reconciliation logic. The `--concurrent-syncs` flag controls the number of workers per controller.

Controllers use the controller-runtime library for common patterns. The reconciler interface defines the reconciliation logic. The work queue provides rate-limited retry. The event recorder emits events for debugging. The `kubectl get events` command shows controller-generated events.

## Network Model

Kubernetes networking requires that every pod receives a unique IP address. Pods can communicate directly without NAT. The CNI plugin (Calico, Cilium, Flannel) configures networking. Service discovery uses DNS (CoreDNS) to resolve service names to cluster IPs. kube-proxy maintains iptables or IPVS rules for service load balancing.

Network policies control pod-to-pod traffic. Calico implements network policies using BGP for routing and eBPF for packet filtering. Cilium uses eBPF to replace kube-proxy entirely, providing kernel-level load balancing and observability. Ingress controllers route external HTTP traffic to services based on hostnames and paths.

The Container Network Interface (CNI) defines how network plugins configure pod networking. CNI plugins are invoked during pod creation. Each CNI plugin assigns an IP address to the pod and configures routing. Network policies are enforced by CNI plugins or external policy engines. The `kubectl describe networkpolicy` command shows applied network policies.

Kubernetes services provide stable endpoints for pod groups. ClusterIP services are accessible within the cluster. NodePort services expose ports on all nodes. LoadBalancer services provision cloud load balancers. ExternalName services map to external DNS names. The `kubectl get endpoints` command shows service endpoints.

## Storage Architecture

Kubernetes manages persistent storage through PersistentVolumes (PV) and PersistentVolumeClaims (PVC). The StorageClass dynamic provisioning creates PVs on demand from cloud providers or local storage. The CSI (Container Storage Interface) driver handles volume operations: attach, detach, mount, unmount.

Volume plugins handle various storage backends. Ephemeral volumes exist for pod lifetime. CSI snapshot volumes enable point-in-time backups. The kubelet manages volume mounting via the Node Stage and Node Publish operations. Storage capacity tracking informs the scheduler of available storage when scheduling pods with PVCs.

The CSI specification defines the interface between Kubernetes and storage drivers. CSI drivers are deployed as DaemonSets on nodes and as Deployments in the control plane. Volume snapshots enable point-in-time backups. Volume cloning creates copies of existing volumes. The `kubectl get pv` and `kubectl get pvc` commands show storage resources.

Kubernetes supports various volume types: emptyDir (ephemeral), hostPath (node filesystem), persistentVolumeClaim (dynamic storage), configMap, secret, and projected. The `--volume` flag or `volumes` field in pod spec configures volumes. The `volumeMounts` field mounts volumes into containers. The `subPath` field mounts a subdirectory of a volume.
