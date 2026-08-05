# Kubernetes Interview Questions

## 1. What is Kubernetes and why is it used?

Kubernetes is an open-source container orchestration platform that automates deployment, scaling, and management of containerized applications. It provides self-healing, horizontal scaling, automated rollouts and rollbacks, service discovery, and load balancing.

## 2. Explain the Kubernetes architecture

Kubernetes follows a master-worker architecture. The control plane (master) includes the API server, etcd, scheduler, and controller manager. Worker nodes run kubelet, kube-proxy, and container runtime. Pods are the smallest deployable units.

## 3. What is a Pod?

A Pod is the smallest deployable unit in Kubernetes. It represents one or more containers that share storage, network, and a specification for how to run. Pods are ephemeral and can be terminated and replaced at any time.

## 4. What is the difference between a Deployment and a ReplicaSet?

A Deployment provides declarative updates for Pods and ReplicaSets. It manages ReplicaSets and provides rollout capabilities. A ReplicaSet ensures a specified number of Pod replicas are running at any time.

## 5. What are Services in Kubernetes?

Services provide stable network endpoints for Pods. Types include ClusterIP (internal only), NodePort (external via node port), LoadBalancer (cloud provider load balancer), and ExternalName (DNS alias).

## 6. What are ConfigMaps and Secrets?

ConfigMaps store non-confidential configuration data as key-value pairs. Secrets store sensitive information like passwords and keys. Both can be mounted as files or environment variables in Pods.

## 7. What is a Namespace?

A Namespace is a mechanism for isolating groups of resources within a single cluster. It provides scope for names, resource quotas, and access control. Common namespaces include default, kube-system, and kube-public.

## 8. What is RBAC?

Role-Based Access Control regulates access to resources based on user roles. It consists of Roles (namespace-scoped), ClusterRoles (cluster-wide), RoleBindings, and ClusterRoleBindings.

## 9. What are Init Containers?

Init Containers run before the main application container in a Pod. They run to completion and are used for setup tasks, configuration, and dependency checks.

## 10. What is a DaemonSet?

A DaemonSet ensures a copy of a Pod runs on all or specific nodes. It's used for logging agents, monitoring agents, and storage daemons that need to run on every node.

## 11. What is the difference between a StatefulSet and a Deployment?

StatefulSets manage stateful applications with stable network identities and persistent storage. Deployments are for stateless applications. StatefulSets guarantee ordering and uniqueness of Pods.

## 12. How does Kubernetes handle networking?

Kubernetes assigns each Pod a unique IP address. Containers within a Pod share the network namespace. Services provide stable IPs and DNS names. CNI plugins handle pod networking across nodes.

## 13. What is an Ingress?

An Ingress manages external access to Services, typically HTTP/HTTPS. It provides load balancing, SSL termination, and name-based virtual hosting. It requires an Ingress Controller (NGINX, Traefik).

## 14. What is the difference between liveness and readiness probes?

Liveness probes check if a container is running. If it fails, the container is restarted. Readiness probes check if a container is ready to serve traffic. If it fails, the Pod is removed from Service endpoints.

## 15. What is Horizontal Pod Autoscaler (HPA)?

HPA automatically scales the number of Pod replicas based on observed CPU utilization or custom metrics. It requires metrics-server to be installed and monitors resource usage to make scaling decisions.

## 16. What are Resource Requests and Limits?

Resource Requests guarantee minimum CPU and memory for a container. Limits enforce maximum usage. Requests help the scheduler place Pods; limits prevent resource starvation and OOM kills.

## 17. What is a Persistent Volume?

A Persistent Volume (PV) is a piece of storage in the cluster. Persistent Volume Claims (PVC) request storage. StorageClasses enable dynamic provisioning. CSI drivers provide vendor-specific storage.

## 18. How do you troubleshoot a Pod that won't start?

1. Check Pod status with kubectl get pods
2. Describe the Pod for events: kubectl describe pod
3. View logs: kubectl logs pod-name
4. Check previous container logs: kubectl logs pod-name --previous
5. Verify image exists and is accessible
6. Check resource availability and quotas

## 19. What is Helm?

Helm is a package manager for Kubernetes. It uses Charts (packages of templates), Values (configuration), and Releases (deployed instances). It simplifies complex deployments and enables version management.

## 20. What are Network Policies?

Network Policies control traffic flow between Pods, namespaces, and external endpoints. They implement zero-trust networking by default deny all traffic and allow specific communication patterns.

## 21. What is the difference between kubectl apply and kubectl create?

kubectl create creates a resource from a file or command. kubectl apply applies a configuration to a resource, creating it if it doesn't exist or updating it if it does. Apply is idempotent.

## 22. How do you perform rolling updates in Kubernetes?

Deployments automatically perform rolling updates. Configure maxSurge and maxUnavailable to control the update process. Use kubectl rollout status to monitor and kubectl rollout undo to rollback.

## 23. What is the kubelet?

Kubelet is an agent running on each node that ensures containers are running in a Pod. It watches the API server for Pods assigned to its node and manages container lifecycle.

## 24. What is etcd?

etcd is a consistent and highly available key-value store used as Kubernetes' backing store for all cluster data. It stores the desired and actual state of the cluster.

## 25. What is the difference between ClusterIP, NodePort, and LoadBalancer?

ClusterIP provides internal-only access within the cluster. NodePort exposes the Service on each node's IP at a static port. LoadBalancer provisions an external load balancer (cloud provider).

## 26. How do you secure a Kubernetes cluster?

1. Enable RBAC and use least-privilege access
2. Implement Pod Security Standards
3. Use network policies for traffic control
4. Encrypt secrets at rest and in transit
5. Scan images for vulnerabilities
6. Enable audit logging
7. Regular updates and patches

## 27. What is a Pod Disruption Budget?

A PDB limits the number of Pods that can be voluntarily disrupted. It ensures application availability during node drains, upgrades, and other voluntary disruptions.

## 28. How does Kubernetes handle configuration management?

Kubernetes uses ConfigMaps for non-sensitive configuration and Secrets for sensitive data. Both can be mounted as files or injected as environment variables. External secret stores provide enhanced security.

## 29. What is the difference between StatefulSet and Deployment?

StatefulSets provide stable network identities, persistent storage, and ordered deployment/scaling for stateful applications. Deployments are for stateless applications with random Pod names.

## 30. What are tolerations and taints?

Taints are applied to nodes to repel Pods. Tolerations are applied to Pods to allow scheduling on tainted nodes. They work together to control Pod placement on specific nodes.
