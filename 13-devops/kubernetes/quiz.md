# Kubernetes Quiz

## Question 1
What is a Pod in Kubernetes?
- A) A virtual machine
- B) The smallest deployable unit, wrapping one or more containers with shared storage and network
- C) A cluster node
- D) A load balancer

**Answer: B**
**Explanation:** A Pod is the basic building block in Kubernetes. It groups one or more containers that share network namespace, storage volumes, and a lifecycle. Pods are scheduled onto nodes and are ephemeral by design.

## Question 2
What is the purpose of a Kubernetes Service?
- A) To run container processes
- B) To provide a stable network endpoint for accessing a set of Pods, with built-in load balancing
- C) To store Docker images
- D) To manage node health

**Answer: B**
- **Answer: B**
**Explanation:** A Service provides a stable IP address and DNS name for a group of Pods. Since Pods are ephemeral and can be destroyed/recreated, Services ensure reliable communication with a consistent endpoint.

## Question 3
What is the difference between a Deployment and a StatefulSet?
- A) They are the same
- B) Deployments are for stateless apps with no guaranteed ordering; StatefulSets are for stateful apps with stable identities and ordered deployment
- C) StatefulSets are faster than Deployments
- D) Deployments don't support scaling

**Answer: B**
**Explanation:** Deployments manage stateless applications with pod names that change. StatefulSets provide stable, unique pod identities (pod-0, pod-1), ordered scaling, and stable network names, making them suitable for databases and caches.

## Question 4
What is the role of etcd in a Kubernetes cluster?
- A) It runs application containers
- B) It is a distributed key-value store that holds all cluster state and configuration data
- C) It balances network traffic
- D) It builds Docker images

**Answer: B**
**Explanation:** etcd is the backing store for all cluster data in Kubernetes. It stores configuration, secrets, and the desired state of all resources. It's the single source of truth for the cluster and must be highly available.

## Question 5
What does a ConfigMap provide in Kubernetes?
- A) Container runtime configuration
- B) Non-sensitive configuration data to containers, decoupled from image content
- C) CPU and memory limits
- D) Network policies

**Answer: B**
**Explanation:** ConfigMaps store key-value pairs of non-sensitive configuration (files, properties, command arguments). They can be injected as environment variables or mounted as files, allowing you to change configuration without rebuilding images.