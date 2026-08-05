# Event Sourcing Pattern

## Overview

Event Sourcing persists the state of an entity as a sequence of immutable events. Instead of storing current state, every change is recorded as an event. The current state is derived by replaying events. This provides a complete audit trail, enables temporal queries, and supports event-driven architectures with replay capabilities.

## When to Use

- Systems requiring complete audit trails
- Applications needing temporal queries (state at any point in time)
- Event-driven architectures with event consumers
- Systems requiring replay and reprocessing capabilities
- Domain-driven designs with complex state transitions
- Applications needing to debug state changes in production

## Implementation

### AWS
- DynamoDB for event storage with streams
- EventBridge for event distribution
- Lambda for event processing
- Kinesis for high-throughput event streaming

### Azure
- Azure Cosmos DB for event storage
- Azure Event Hubs for event streaming
- Azure Functions for event processing
- Azure Event Grid for event distribution

### Google Cloud
- Firestore for event storage
- Pub/Sub for event distribution
- Cloud Functions for event processing
- Dataflow for event stream processing

### Event Store Solutions
- EventStoreDB - Purpose-built event store
- Apache Kafka - Event streaming platform
- Axon Server - Event store for Axon Framework
- Marten (.NET) - Event store on PostgreSQL

## Best Practices

1. Design events as immutable, self-contained facts
2. Use event versioning and upcasting for schema evolution
3. Implement snapshotting to reduce replay time
4. Separate write model (events) from read model (projections)
5. Implement idempotent event handlers
6. Consider event store partitioning for scalability
7. Plan for event store archival and retention policies

## Interview Questions

1. How do you handle event schema evolution in event sourcing?
2. What is snapshotting and when should you implement it?
3. How does event sourcing differ from traditional CRUD?
4. Describe strategies for querying current state from event history.
5. How do you handle eventual consistency in event-sourced systems?

## References

- Event Sourcing - Martin Fowler
- EventStoreDB Documentation
- Axon Framework Documentation
- Implementing Domain-Driven Design - Vaughn Vernon
- Designing Data-Intensive Applications - Martin Kleppmann
- Microservices Patterns - Chris Richardson
