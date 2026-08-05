## .NET Microservices

Building microservice architectures with .NET using patterns like Clean Architecture, DDD, CQRS, and event-driven communication.

## Overview

Microservices architecture structures applications as a collection of small, independent services that communicate over well-defined APIs. .NET provides excellent support for building microservices.

## Why It Matters

- Independent deployment and scaling
- Technology flexibility per service
- Fault isolation between services
- Team autonomy and ownership
- Better suited for large, complex systems

## Key Concepts

- **Service Boundary**: Domain-driven service boundaries
- **API Gateway**: Single entry point for clients
- **Service Discovery**: Finding service endpoints
- **Circuit Breaker**: Fault tolerance pattern
- **Saga Pattern**: Distributed transaction management
- **Event Sourcing**: Storing state changes as events

## Core Topics

- Service decomposition strategies
- Inter-service communication (HTTP, gRPC, messaging)
- Data management per service
- Service discovery and load balancing
- Distributed tracing and monitoring
- Resilience patterns (retry, circuit breaker, bulkhead)
- Container orchestration with Kubernetes

## Best Practices

- Design services around business domains
- Use async communication where possible
- Implement resilience patterns
- Use API Gateway for external communication
- Keep databases per service
- Implement distributed tracing

## Hands-on Labs

- Build a microservices solution with .NET
- Implement inter-service communication
- Add resilience with Polly
- Deploy to Kubernetes

## Interview Questions

1. When should you use microservices over a monolith?
2. How do you handle distributed transactions?
3. What is the saga pattern?
4. How do you implement service-to-service communication?

## References

- https://learn.microsoft.com/dotnet/architecture/microservices/
- https://learn.microsoft.com/dotnet/architecture/
- https://github.com/dotnet/eShop
