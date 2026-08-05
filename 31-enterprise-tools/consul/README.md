# Consul - Service Discovery and Service Mesh

## Overview

Consul is a service mesh solution for discovering and configuring services in dynamic infrastructure. It provides service discovery, health checking, KV store, and secure service-to-service communication with built-in support for multi-datacenter deployments.

## Why It Matters

- Enables dynamic service discovery in microservices architectures
- Provides health checking to ensure traffic routes to healthy instances
- Supports service mesh with mTLS for secure inter-service communication
- Offers multi-datacenter support for global service architectures
- Integrates with Kubernetes, VMs, and bare metal environments

## Key Concepts

- **Service**: A logical unit of application functionality registered with Consul
- **Agent**: Core process running on every node providing health checks and services
- **Datacenter**: Logical grouping of nodes and services
- **Service Mesh**: Infrastructure layer providing secure, observable service communication
- **Connect**: Consul's service mesh implementation with mTLS and intentions
- **Intention**: Security policy defining which services can communicate

## Core Topics

### Service Discovery
- Service registration and deregistration
- DNS and HTTP-based service lookup
- Health checks: HTTP, TCP, script, TTL-based

### Service Mesh and Connect
- mTLS certificate management for service identity
- Proxies (Envoy) for transparent traffic management
- Intentions for service-to-service access control

### Key-Value Store
- Distributed configuration storage
- Session management for distributed locking
- Variables for service configuration

### Multi-Datacenter Support
- WAN gossip for cross-datacenter communication
- Global services and prepared queries
- Network area segmentation

## Best Practices

1. Use health checks to ensure only healthy services receive traffic
2. Implement intentions to enforce zero-trust networking between services
3. Deploy Consul agents on every node for full cluster visibility
4. Use KV store for distributed configuration management
5. Segment services into separate namespaces for multi-tenancy
6. Monitor Consul cluster health and raft leader status

## Hands-on Labs

1. **Consul Setup**: Deploy a 3-node Consul cluster using Docker
2. **Service Registration**: Register a sample service with health checks
3. **Service Discovery**: Query services using DNS and HTTP interfaces
4. **Connect Mesh**: Enable Connect and configure mTLS between services
5. **Intentions**: Create intentions to allow and deny service communication
6. **KV Store**: Store and retrieve configuration using the KV store

## Interview Questions

1. What is the difference between Consul DNS and HTTP interfaces for service discovery?
2. How does Consul Connect provide mutual TLS between services?
3. Explain the role of intentions in a service mesh
4. How does Consul handle health checking for registered services?
5. What is the purpose of the KV store in Consul?
6. Describe how Consul supports multi-datacenter deployments
7. How would you use Consul for distributed locking?

## References

- Consul Documentation: https://developer.hashicorp.com/consul/docs
- Consul Connect: https://developer.hashicorp.com/consul/docs/connect
- Consul Tutorials: https://developer.hashicorp.com/consul/tutorials
- Consul API: https://developer.hashicorp.com/consul/api-docs
