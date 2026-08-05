# Saga Pattern

## Overview

The Saga pattern manages distributed transactions across multiple services by defining a sequence of local transactions. If a step fails, compensating transactions are executed to undo previous work. Sagas maintain eventual consistency without distributed locks, using either choreography (events-driven) or orchestration (central coordinator) approaches.

## When to Use

- Managing transactions across multiple microservices
- Maintaining data consistency without distributed locks
- Implementing long-running business processes
- Coordinating multi-step workflows with compensation logic
- Building event-driven distributed systems
- Handling partial failures in business operations

## Implementation

### AWS
- Step Functions for saga orchestration
- SQS/SNS for choreography-based sagas
- Lambda for saga step execution
- DynamoDB for saga state management

### Azure
- Durable Functions for saga orchestration
- Service Bus for choreography-based sagas
- Azure Logic Apps for workflow sagas
- Cosmos DB for saga state persistence

### Google Cloud
- Cloud Workflows for saga orchestration
- Pub/Sub for choreography-based sagas
- Cloud Tasks for step execution
- Firestore for saga state storage

### Libraries
- Axon Framework (Java) - Saga support
- MassTransit (.NET) - Saga state machines
- Temporal - Workflow orchestration
- Camunda - Business process management

## Best Practices

1. Design compensating transactions for every step
2. Make saga steps idempotent for safe retries
3. Implement saga monitoring and observability
4. Use orchestration for complex workflows with many steps
5. Use choreography for simple, loosely-coupled workflows
6. Persist saga state for recovery from failures
7. Test compensation logic thoroughly in staging

## Interview Questions

1. Compare choreography and orchestration approaches in Saga pattern.
2. How do you handle a failure in a compensating transaction?
3. What makes saga steps different from regular transactions?
4. How would you implement saga monitoring and debugging?
5. Describe strategies for testing saga compensation logic.

## References

- Saga Pattern - Chris Richardson
- Microservices Patterns - Chris Richardson
- Durable Functions Documentation
- Temporal Documentation
- Axon Framework Saga Documentation
- Designing Data-Intensive Applications - Martin Kleppmann
