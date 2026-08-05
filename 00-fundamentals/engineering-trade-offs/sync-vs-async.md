# Sync vs Async

## Problem Statement

Should a function call wait for a result or proceed immediately? Synchronous code is simple to reason about. Asynchronous code is more efficient but harder to understand. The choice affects performance, reliability, and code complexity.

## The Core Tension

Synchronous: Caller waits for completion. Code reads top-to-bottom. Simple mental model. Blocks resources while waiting.

Asynchronous: Caller proceeds immediately. Callbacks, promises, or events handle results later. More efficient. Harder to follow control flow.

## When to Choose Synchronous

**Simple operations**: When the operation completes in microseconds and there is nothing else to do while waiting.

**Sequential dependencies**: When step B requires the result of step A. Making B async provides no benefit.

**Debugging and testing**: Synchronous code is easier to trace through a debugger. Stack traces are clear.

**User-facing responses**: When the user needs the result before proceeding (saving a document, processing a payment).

**Small-scale systems**: When throughput requirements are low, the complexity of async is not justified.

## When to Choose Asynchronous

**I/O-bound operations**: Network requests, file operations, database queries. The thread would be idle waiting.

**Long-running tasks**: Report generation, video processing, batch jobs. Blocking the caller wastes resources.

**High throughput**: When you need to handle thousands of concurrent operations, async avoids thread exhaustion.

**Fire-and-forget**: When the caller does not need the result (sending an email, logging an event, updating a cache).

**Decoupling**: When the caller should not depend on the callee's availability or performance.

## Async Patterns

**Callbacks**: Original async pattern. Leads to callback hell when nested.

**Promises/Futures**: Chainable operations. Cleaner than callbacks but still complex.

**Async/Await**: Syntactic sugar over promises. Reads like synchronous code. Best ergonomics.

**Message queues**: Decouple systems entirely. Producer sends message, consumer processes later. Best for cross-service communication.

**Event-driven**: System emits events. Multiple consumers react independently. Best for one-to-many communication.

## Message Queues

Queues are the most robust async pattern:

- **Buffering**: Absorb traffic spikes
- **Retry**: Failed messages can be retried
- **Ordering**: Messages can be processed in order
- **Fan-out**: Multiple consumers can process the same message
- **Dead letter**: Failed messages go to a dead letter queue for investigation

Common queues: RabbitMQ, Kafka, SQS, Redis Streams.

## Event-Driven Architecture

Events represent things that happened, not commands:

```
// Command: "Send email to user" (imperative)
// Event: "Order was placed" (declarative)

OrderPlaced {
  orderId: "123"
  userId: "456"
  total: 99.99
  timestamp: "2024-01-15T10:30:00Z"
}
```

Multiple services react to OrderPlaced: send confirmation email, update inventory, process payment, update analytics. The order service does not need to know about these downstream effects.

## Real-World Examples

**Web request handling**: Synchronous within the request. The user expects a response. But downstream operations (logging, analytics, notifications) should be async.

**Email sending**: Always async. The user does not need to wait for the email to be delivered. If the email service is down, the user should not see an error.

**Payment processing**: Synchronous for the critical path (charge the card). Async for receipt generation, inventory updates, and analytics.

**Log aggregation**: Async by nature. Logs are sent to a queue and processed by a separate system. No user waits for log processing.

## Decision Matrix

| Factor | Choose Sync | Choose Async |
|--------|------------|--------------|
| Response time required | Low | High acceptable |
| Operation duration | Fast (< 100ms) | Slow (> 500ms) |
| Resource utilization | Low concern | High concern |
| Code complexity budget | Low | High |
| Reliability requirements | Immediate feedback | Eventual consistency OK |
| Coupling | Tight is acceptable | Loose preferred |

## Interview Relevance

**Common questions**:
- "Design a notification system"
- "How would you handle processing millions of events?"
- "What happens when a downstream service is slow?"

**What interviewers want**:
- You understand when to introduce async without over-engineering
- You know message queue patterns and trade-offs
- You can reason about backpressure and retry strategies
- You understand the difference between sync within a service and async between services

**Red flags**:
- Making everything async "for performance" without justification
- Not mentioning retry and dead letter strategies
- Not considering what happens when the queue is full
- Ignoring ordering requirements in async systems

## Key Takeaway

Use synchronous code for the critical path where the caller needs a result. Use asynchronous code for everything else. The boundary between sync and async is where most system design decisions live.
