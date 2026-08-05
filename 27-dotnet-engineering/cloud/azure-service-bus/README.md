## Azure Service Bus

Enterprise message broker for reliable messaging between applications and services.

## Overview

Azure Service Bus provides reliable message queuing and publish-subscribe messaging with features like sessions, transactions, and dead lettering.

## Why It Matters

- Enterprise-grade reliability and durability
- Supports both queue and topic patterns
- Sessions for ordered processing
- Transactions for atomic operations
- Dead letter queues for error handling

## Key Concepts

- **Queue**: Point-to-point messaging
- **Topic**: Publish-subscribe messaging
- **Subscription**: Filtered view of a topic
- **Session**: Ordered message processing
- **Dead Letter Queue**: Failed message storage
- **Peek-Lock**: Reliable message processing
- **Auto-Delete**: Automatic message removal

## Core Topics

- Queue and topic creation
- Message sending and receiving
- Sessions for ordered processing
- Scheduled and deferred messages
- Dead letter queue handling
- Azure Service Bus SDK for .NET
- Scaling and partitioning

## Best Practices

- Use sessions for ordered processing
- Implement peek-lock for reliable handling
- Handle dead letter queues for failed messages
- Use managed identity for authentication
- Monitor queue depth and message age

## Hands-on Labs

- Send and receive messages with Service Bus
- Implement session-based processing
- Handle dead letter queue messages
- Build a publish-subscribe pattern

## Interview Questions

1. What is the difference between queues and topics?
2. How do sessions work in Service Bus?
3. What is the dead letter queue and when is it used?

## References

- https://learn.microsoft.com/azure/service-bus-messaging/
- https://learn.microsoft.com/azure/service-bus-messaging/sbasic-how-to-use-queues-basic
- https://learn.microsoft.com/azure/service-bus-messaging/topics-subscriptions
