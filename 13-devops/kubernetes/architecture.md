# Kubernetes Architecture

## Overview

Kubernetes (K8s) is an open-source container orchestration platform that automates deployment, scaling, and management of containerized applications. It follows a master-worker (control plane - node) architecture.

## Control Plane Components

### API Server (kube-apiserver)

The front-end for the Kubernetes control plane. It exposes the Kubernetes API and processes RESTful requests. All communication between components goes through the API server. It validates and configures data for API objects including pods, services, and deployments.

### etcd

A consistent and highly available key-value store used as Kubernetes' backing store for all cluster data. It stores the desired and actual state of the cluster. etcd is the single source of truth for the cluster state. Always run an odd number of etcd instances (3 or 5) for high availability.

### Scheduler (kube-scheduler)

Watches for newly created Pods with no assigned node and selects a node for them to run on. Considers resource requirements, hardware/software/policy constraints, data locality, and inter-workload interference. The scheduling decision includes node selection and pod binding.

### Controller Manager (kube-controller-manager)

Runs controller processes that regulate the state of the system. Controllers watch the shared state of the cluster and make changes to move the current state toward the desired state. Key controllers include: Node Controller, Replication Controller, Endpoints Controller, and Service Account & Token Controllers.

### Cloud Controller Manager

Embeds cloud-specific control logic. It allows the cloud provider to link code with the cluster, separating Kubernetes core from cloud-specific components. AWS, GCP, and Azure provide their own implementations.

## Worker Node Components

### kubelet

An agent running on each node that ensures containers are running in a Pod. It watches the API server for Pods assigned to its node and ensures the described containers are running and healthy. It does not manage containers not created by Kubernetes.

### kube-proxy

A network proxy running on each node that maintains network rules on nodes. These network rules allow network communication to Pods from inside or outside the cluster. Supports iptables, IPVS, and userspace proxy modes.

### Container Runtime

The software responsible for running containers. Supports multiple runtimes: containerd (default), CRI-O, Docker (via dockershim), and gVisor. Kubernetes uses the Container Runtime Interface (CRI) to interact with runtimes.

## Cluster Architecture

### Pod Scheduling Flow

1. User submits manifest to API Server
2. API Server validates and persists to etcd
3. Scheduler watches for unscheduled Pods
4. Scheduler selects optimal node based on constraints
5. Scheduler binds Pod to selected node
6. kubelet on selected node starts the container

### Communication Patterns

- **Control Plane to Node**: API server to kubelet (HTTPS)
- **Node to Control Plane**: kubelet and kube-proxy to API server
- **Pod to Pod**: Via cluster network (CNI plugins)
- **Service to Pod**: Via kube-proxy rules and kube-proxy

### High Availability

- Multiple API server replicas behind load balancer
- etcd cluster with 3 or 5 members
- Multiple scheduler and controller manager instances with leader election
- Control plane nodes dedicated and isolated from worker nodes

## CNI Plugins

Container Network Interface (CNI) plugins provide pod networking:
- **Calico**: Network policy and BGP routing
- **Flannel**: Simple overlay network
- **Cilium**: eBPF-based networking and security
- **Weave**: Mesh networking with encryption
- **AWS VPC CNI**: Native VPC networking

## Storage Architecture

- **Persistent Volumes (PV)**: Cluster-level storage resources
- **Persistent Volume Claims (PVC)**: Requests for storage by Pods
- **Storage Classes**: Dynamic provisioning of PVs
- **CSI Drivers**: Container Storage Interface for vendor plugins

## Design Principles

- **Declarative Configuration**: Desired state over imperative commands
- **Immutable Infrastructure**: Containers and images are immutable
- **Loose Coupling**: Services and microservice architecture
- **Self-healing**: Automatic restart, rescheduling, and replication
- **Horizontal Scaling**: Scale out rather than scale up

## Best Practices

1. Separate control plane from worker nodes
2. Use odd number of etcd nodes for quorum
3. Implement network policies for pod isolation
4. Use persistent storage for stateful applications
5. Monitor all control plane components
6. Regularly back up etcd data
7. Use RBAC for access control
8. Implement Pod Security Standards
