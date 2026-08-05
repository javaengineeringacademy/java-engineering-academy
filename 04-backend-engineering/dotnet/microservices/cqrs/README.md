## CQRS Pattern

Command Query Responsibility Segregation separates read and write operations for optimized data models.

## Overview

CQRS separates the read (query) and write (command) models of an application, allowing each to be optimized independently. Often combined with Event Sourcing for complete audit trails.

## Why It Matters

- Independent scaling of reads and writes
- Optimized data models for each operation
- Clear separation of concerns
- Enables event sourcing
- Better performance for complex systems

## Key Concepts

- **Command**: Represents a write operation
- **Query**: Represents a read operation
- **Command Handler**: Processes commands
- **Query Handler**: Processes queries
- **Event Store**: Append-only log of domain events
- **Read Model**: Optimized projection for queries

## Core Topics

- CQRS implementation with MediatR
- Separating read and write databases
- Event sourcing with event stores
- Read model projections
- Optimistic concurrency
- eventual consistency handling

## Best Practices

- Start with simple CQRS, add event sourcing if needed
- Use different data stores for read/write if beneficial
- Handle eventual consistency explicitly
- Use MediatR for command/query dispatching
- Implement idempotent command handlers

## Hands-on Labs

- Implement CQRS with MediatR
- Separate read and write databases
- Build event sourcing with Marten
- Create read model projections

## Interview Questions

1. What is the difference between CQRS and traditional CRUD?
2. How does CQRS relate to Event Sourcing?
3. What are the tradeoffs of CQRS?
4. How do you handle eventual consistency?

## References

- https://learn.microsoft.com/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/
- https://cqrs.files.wordpress.com/2010/11/cqrs_documents.pdf
- https://github.com/jbogard/MediatR
