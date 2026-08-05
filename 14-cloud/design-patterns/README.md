# Cloud-Native Design Patterns

## Overview

Cloud design patterns are reusable solutions to common problems encountered when building applications for cloud environments. These patterns address challenges such as scalability, resilience, security, and performance in distributed systems. They provide a shared vocabulary and proven approaches for architects and developers working with cloud-native applications.

## Core Categories

### Resilience Patterns
- Circuit Breaker, Retry with Backoff, Bulkhead
- Handle failures gracefully without cascading effects

### Data Management Patterns
- CQRS, Event Sourcing, Saga
- Manage distributed data consistency and eventual consistency

### Messaging Patterns
- Competing Consumers, Queue-Based Load Leveling, Claim Check
- Decouple components through asynchronous communication

### Service Communication Patterns
- API Gateway, Service Registry, Backends for Frontends
- Manage service-to-service and client-to-service communication

### Migration and Deployment Patterns
- Strangler Fig, Sidecar, Ambassador
- Support incremental migration and runtime management

### Security Patterns
- Valet Key, Throttling
- Control access and protect resources from abuse

## When to Use

- Building microservices architectures
- Designing for high availability and fault tolerance
- Migrating monolithic applications to the cloud
- Implementing event-driven architectures
- Managing distributed transactions
- Optimizing for cloud cost and performance

## Best Practices

1. Combine multiple patterns to address complex requirements
2. Understand trade-offs before applying any pattern
3. Test failure scenarios thoroughly in staging environments
4. Monitor pattern implementations with observability tools
5. Start with the simplest pattern that solves the problem
6. Document pattern decisions for team alignment

## Interview Questions

1. How does the Circuit Breaker pattern prevent cascading failures?
2. Explain the difference between Choreography and Orchestration in Saga pattern.
3. When would you choose CQRS over traditional CRUD architecture?
4. How does the Strangler Fig pattern help with legacy migration?
5. What are the trade-offs of using Event Sourcing?

## References

- Cloud Design Patterns - Microsoft Azure Architecture Center
- The Twelve-Factor App - https://12factor.net
- Building Microservices - Sam Newman
- Designing Data-Intensive Applications - Martin Kleppmann
- Cloud Native Patterns - Cornelia Davis
- AWS Well-Architected Framework
- Google Cloud Architecture Framework
