## Event-Driven Architecture

Building decoupled systems using events for communication between services and components.

## Overview

Event-driven architecture uses events as the primary communication mechanism between services. Producers emit events, and consumers react to them asynchronously.

## Why It Matters

- Loose coupling between producers and consumers
- Scalable through parallel processing
- Natural fit for domain events
- Enables eventual consistency
- Supports audit trails and replay

## Key Concepts

- **Event**: Immutable record of something that happened
- **Event Producer**: Component that raises events
- **Event Consumer**: Component that handles events
- **Event Bus**: Channel for event distribution
- **Event Store**: Persistent storage for events
- **Dead Letter Queue**: Failed event handling

## Core Topics

- Event publishing patterns
- Message brokers (Azure Service Bus, RabbitMQ)
- Event sourcing implementation
- Saga/orchestration for distributed transactions
- Idempotent event handling
- Dead letter queues and error handling
- Event schema evolution and versioning

## Best Practices

- Make events immutable
- Implement idempotent consumers
- Use dead letter queues for failed events
- Version event schemas carefully
- Keep events small and focused

## Hands-on Labs

- Implement event publishing with Azure Service Bus
- Build event consumers with idempotency
- Implement a saga with event-driven coordination
- Set up dead letter queue handling

## Interview Questions

1. What is the difference between event-driven and message-driven?
2. How do you handle event schema changes?
3. What is idempotency and why is it important?
4. How do you implement saga patterns?

## References

- https://learn.microsoft.com/azure/architecture/patterns/
- https://learn.microsoft.com/azure/service-bus-messaging/
- https://microservices.io/patterns/data/event-sourcing.html
