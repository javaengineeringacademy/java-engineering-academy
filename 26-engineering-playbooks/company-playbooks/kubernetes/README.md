# Kubernetes Architecture Playbook

## Overview

Kubernetes is an open-source container orchestration platform that automates deployment, scaling, and management of containerized applications. Originally developed at Google based on Borg, Kubernetes has become the standard for container orchestration.

## Control Plane

### API Server

The API server is the central management entity, exposing the Kubernetes API. All communication between components flows through the API server, which validates and processes requests.

The API server stores cluster state in etcd, a distributed key-value store. All cluster operations are represented as API objects, enabling declarative configuration and reconciliation.

### etcd

etcd stores all cluster state, including node status, pod definitions, and configuration. It uses the Raft consensus algorithm for consistency, ensuring all API servers see the same state.

etcd is critical to cluster health. A compromised etcd cluster can lead to data loss or inconsistency. Regular backups and proper sizing are essential for production clusters.

### Scheduler

The scheduler assigns pods to nodes based on resource requirements, constraints, and policies. It considers factors like resource availability, affinity rules, taints and tolerations, and data locality.

The scheduling algorithm runs in two phases: filtering and scoring. Filtering eliminates unsuitable nodes, and scoring ranks the remaining nodes to select the best match.

### Controller Manager

Controller managers run control loops that reconcile desired state with actual state. Each controller manages a specific resource type, such as deployments, replicasets, or nodes.

When the desired state changes, controllers detect the drift and take action to bring the actual state into alignment. This reconciliation loop is the core of Kubernetes' self-healing capability.

## Node Components

### Kubelet

Kubelet runs on each node, managing pods and containers. It ensures containers specified in pod definitions are running and healthy. Kubelet communicates with the API server and container runtime.

### Kube Proxy

Kube proxy maintains network rules on nodes, enabling service discovery and load balancing. It implements the Service abstraction, routing traffic to appropriate pods based on labels and selectors.

### Container Runtime

The container runtime pulls images, starts containers, and manages their lifecycle. Kubernetes supports multiple runtimes through the Container Runtime Interface, including containerd and CRI-O.

## Workload Resources

### Deployments

Deployments manage replica sets and pods, providing declarative updates and rollback capabilities. They support rolling updates, ensuring zero-downtime deployments.

### StatefulSets

StatefulSets manage stateful applications, providing stable network identities and persistent storage. Each pod in a StatefulSet gets a unique, persistent hostname and ordered deployment.

### DaemonSets

DaemonSets ensure a pod runs on every node (or a subset). They are useful for logging, monitoring, and node-level services that must run on every machine.

## Networking

### Service Abstraction

Services provide stable network endpoints for pods. Service types include ClusterIP (internal), NodePort (external on node port), and LoadBalancer (cloud load balancer).

### Ingress

Ingress manages external HTTP/HTTPS access to services. It provides routing rules, SSL termination, and name-based virtual hosting. Ingress controllers implement the actual routing logic.

### Network Policies

Network Policies control traffic flow between pods. They enable microsegmentation, restricting which pods can communicate with each other, enhancing security.
