# Ambassador Pattern

## Overview

The Ambassador pattern creates helper services that act as proxies or intermediaries for the main application. Unlike sidecars that run in the same pod, ambassadors are deployed as separate services that handle network communication, security, and monitoring for the application. They provide a consistent interface to external services regardless of the application's location.

## When to Use

- Providing consistent connectivity to external services
- Implementing service discovery and load balancing
- Handling TLS termination and certificate management
- Offloading network monitoring and logging
- Implementing retry and circuit breaker logic
- Managing connections to legacy systems

## Implementation

### AWS
- Application Load Balancer as ambassador
- API Gateway for external service mediation
- App Mesh for service-to-service communication
- Cloud Map for service discovery

### Azure
- Azure API Management as ambassador service
- Azure Front Door for global traffic management
- Azure Service Mesh for inter-service communication
- Azure Application Gateway as traffic ambassador

### Google Cloud
- Cloud Endpoints as managed ambassador
- Anthos Service Mesh for service communication
- Cloud Load Balancing for traffic management
- Apigee for API mediation

### Kubernetes
- Ambassador API Gateway (dedicated project)
- Custom ambassador deployments per service
- Istio ingress gateway as ambassador
- HAProxy as network ambassador

## Best Practices

1. Keep ambassador services focused on specific responsibilities
2. Implement health checks for ambassador availability
3. Monitor ambassador performance as critical infrastructure
4. Use ambassadors for protocol translation needs
5. Implement connection pooling in ambassadors
6. Consider ambassador placement (same namespace vs. shared)
7. Test ambassador failover scenarios thoroughly

## Interview Questions

1. How does the Ambassador pattern differ from the Sidecar pattern?
2. When would you choose Ambassador over Service Mesh?
3. How do you handle ambassador service failures?
4. Describe strategies for scaling ambassador services.
5. What are the security considerations for ambassador proxies?

## References

- Ambassador Pattern - Microsoft Azure Architecture Center
- Ambassador API Gateway Documentation
- Istio Documentation
- Envoy Proxy Documentation
- Building Microservices - Sam Newman
- Cloud Design Patterns - Microsoft Azure Architecture Center
