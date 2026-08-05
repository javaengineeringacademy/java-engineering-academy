# Service Registry Pattern

## Overview

The Service Registry pattern provides a central database where services register themselves and discover other services. It enables dynamic service discovery in distributed systems where instances may scale, fail, or move. Clients query the registry to find available service instances and select one for communication.

## When to Use

- Managing dynamic service instances in microservices
- Supporting auto-scaling and instance discovery
- Enabling load balancing across service instances
- Facilitating service-to-service communication
- Supporting blue-green and canary deployments
- Managing service health and availability

## Implementation

### AWS
- AWS Cloud Map for service discovery
- Route 53 for DNS-based service discovery
- ECS service discovery with Cloud Map
- ALB target groups for instance registration

### Azure
- Azure Container Registry with service discovery
- Azure DNS for service resolution
- Azure Service Fabric naming service
- Azure Kubernetes Service with kube-dns

### Google Cloud
- Cloud DNS for service discovery
- GKE with built-in DNS discovery
- Anthos Service Mesh for advanced discovery
- Cloud Endpoints for managed service registry

### Dedicated Registries
- Netflix Eureka - REST-based service registry
- HashiCorp Consul - Multi-datacenter service mesh
- Apache ZooKeeper - Distributed coordination
- etcd - Distributed key-value store
- Nacos - Dynamic service discovery and configuration

## Best Practices

1. Implement health checks for registered service instances
2. Use client-side load balancing with registry data
3. Cache registry data locally to reduce lookup latency
4. Implement eventual consistency for registry updates
5. Monitor registry health as critical infrastructure
6. Use DNS-based discovery for language-agnostic resolution
7. Implement graceful handling of registry unavailability

## Interview Questions

1. Compare client-side and server-side service discovery.
2. How does Consul handle multi-datacenter service discovery?
3. What happens when the service registry becomes unavailable?
4. Describe the CAP theorem implications for service registries.
5. How do you handle service instance health monitoring?

## References

- Service Registry Pattern - Microsoft Azure Architecture Center
- Netflix Eureka Documentation
- HashiCorp Consul Documentation
- Apache ZooKeeper Documentation
- Building Microservices - Sam Newman
- Microservices Patterns - Chris Richardson
