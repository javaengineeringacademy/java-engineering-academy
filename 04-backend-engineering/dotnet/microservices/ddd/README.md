## Domain-Driven Design in .NET

Applying DDD principles to .NET applications with rich domain models, bounded contexts, and strategic design.

## Overview

Domain-Driven Design is an approach to software development that centers the development on the core business domain. It emphasizes collaboration between technical and domain experts.

## Why It Matters

- Aligns software with business domain
- Reduces complexity through bounded contexts
- Improves communication between developers and domain experts
- Enables rich, expressive domain models
- Foundation for microservice boundaries

## Key Concepts

- **Entities**: Objects with identity and lifecycle
- **Value Objects**: Immutable objects defined by attributes
- **Aggregates**: Consistency boundaries around entities
- **Domain Events**: Notifications of state changes
- **Bounded Contexts**: Logical boundaries for model consistency
- **Ubiquitous Language**: Shared vocabulary between team and domain

## Core Topics

- Entities vs Value Objects
- Aggregate design and consistency boundaries
- Domain services and domain events
- Repository pattern per aggregate
- Bounded context mapping
- Strategic design patterns
- Anti-corruption layers

## Best Practices

- Keep aggregates small
- Reference other aggregates by identity only
- Use value objects to enforce invariants
- Publish domain events for cross-aggregate coordination
- Design bounded contexts around business capabilities

## Hands-on Labs

- Model an e-commerce domain with DDD
- Implement aggregates with invariants
- Create domain events and handlers
- Design bounded contexts for microservices

## Interview Questions

1. What is the difference between entities and value objects?
2. How do you design aggregates?
3. What is a bounded context?
4. How do domain events work?

## References

- https://learn.microsoft.com/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/
- https://github.com/ddd-crew/ddd-starter-modelling-process
- https://www.domainlanguage.com/ddd/
