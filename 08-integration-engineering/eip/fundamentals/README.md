# Enterprise Integration Patterns - Fundamentals

## Overview

Enterprise Integration Patterns (EIP) provide a standardized vocabulary and set of patterns for designing and implementing enterprise integration solutions. These patterns address common challenges in connecting disparate systems, applications, and data sources.

## Table of Contents

1. [Messaging Concepts](#messaging-concepts)
2. [Message Channels](#message-channels)
3. [Message Construction](#message-construction)
4. [Message Routing](#message-routing)
5. [Message Transformation](#message-transformation)
6. [Messaging Endpoints](#messaging-endpoints)
7. [System Management](#system-management)
8. [Messaging Styles](#messaging-styles)
9. [Implementation Considerations](#implementation-considerations)

## Messaging Concepts

### What is a Message?

A message is a data structure that encapsulates information to be communicated between systems. Messages carry:

- **Header**: Metadata about the message (ID, timestamp, correlation ID, priority)
- **Body**: The actual payload (XML, JSON, text, binary)
- **Properties**: Application-specific metadata

```
┌─────────────────────────────────────┐
│            MESSAGE                  │
├─────────────────────────────────────┤
│  Headers                            │
│  - MessageID: msg-123               │
│  - Timestamp: 2024-01-15T10:30:00  │
│  - CorrelationID: corr-456          │
│  - Priority: HIGH                   │
├─────────────────────────────────────┤
│  Properties                         │
│  - Source: order-service            │
│  - Version: 2.0                     │
├─────────────────────────────────────┤
│  Body                               │
│  - { "orderId": 123, ... }          │
└─────────────────────────────────────┘
```

### Messaging Models

#### Point-to-Point

```
Producer ──────── Queue ──────── Consumer
```

- Single consumer processes each message
- Guaranteed delivery
- Load balancing across consumers

#### Publish-Subscribe

```
                    ┌──── Subscriber 1
Publisher ──── Topic ───── Subscriber 2
                    └──── Subscriber 3
```

- Multiple consumers receive each message
- Event broadcasting
- Loose coupling between producer and consumers

### Messaging Styles

#### Document Messaging

```json
{
  "messageType": "OrderCreated",
  "payload": {
    "orderId": "12345",
    "items": [...],
    "total": 99.99
  }
}
```

- Complete, self-contained messages
- Receiver processes entire document
- Ideal for batch processing

#### Command Messaging

```xml
<command type="ProcessOrder">
  <orderId>12345</orderId>
  <action>APPROVE</action>
</command>
```

- Instructions to perform specific actions
- Request-response pattern
- Tight coupling between sender and receiver

#### Event Messaging

```json
{
  "eventType": "OrderShipped",
  "timestamp": "2024-01-15T10:30:00Z",
  "data": {
    "orderId": "12345",
    "trackingNumber": "TRACK-789"
  }
}
```

- Notification of state changes
- Loose coupling
- Event-driven architecture

## Message Channels

### Channel Types

#### Point-to-Point Channel

```
┌──────────┐      ┌──────────┐      ┌──────────┐
│ Producer │ ───> │ Channel  │ ───> │ Consumer │
└──────────┘      └──────────┘      └──────────┘
```

- Single consumer per message
- Load balancing possible
- Guaranteed processing

#### Publish-Subscribe Channel

```
                ┌──────────┐ ───> Consumer 1
┌──────────┐   │          │
│ Producer │ ─>│ Channel  │ ───> Consumer 2
└──────────┘   │          │
                └──────────┘ ───> Consumer 3
```

- Broadcast to all subscribers
- Event notification
- Multiple consumers

#### Channel Types Summary

| Channel Type | Description | Use Case |
|-------------|-------------|----------|
| Point-to-Point | One consumer per message | Command processing |
| Publish-Subscribe | All subscribers receive | Event broadcasting |
| Executor | Thread pool processing | Parallel processing |
| Queue | Persistent message store | Reliable delivery |
| Topic | Non-persistent broadcast | Real-time updates |

### Channel Configuration

```java
// Point-to-Point Channel
@Bean
public MessageChannel orderChannel() {
    return new QueueChannel(100); // capacity of 100
}

// Publish-Subscribe Channel
@Bean
public MessageChannel eventChannel() {
    return new PublishSubscribeChannel();
}

// Executor Channel (Thread Pool)
@Bean
public MessageChannel asyncChannel() {
    return new ExecutorChannel(executorService());
}
```

## Message Construction

### Message Structure

```java
public interface Message<T> {
    MessageHeaders getHeaders();
    T getPayload();
    
    // Factory methods
    static <T> Message<T> withPayload(T payload);
    Message<T> setHeader(String key, Object value);
    Message<T> copyHeaders(Map<String, Object> headers);
}
```

### Message Headers

| Header | Description | Example |
|--------|-------------|---------|
| ID | Unique message identifier | UUID |
| TIMESTAMP | Creation time | Epoch millis |
| CORRELATION_ID | Links request/response | String |
| REPLY_TO | Response channel | Channel name |
| EXPIRATION | Message TTL | Timestamp |
| PRIORITY | Processing priority | Integer |
| SEQUENCE_NUMBER | Order in sequence | Integer |
| CONTENT_TYPE | Payload format | application/json |

### Message Types

#### Command Message

```java
Message<ProcessOrderCommand> command = MessageBuilder
    .withPayload(new ProcessOrderCommand(orderId, items))
    .setHeader("commandType", "PROCESS_ORDER")
    .setHeader("priority", "HIGH")
    .build();
```

#### Event Message

```java
Message<OrderCreatedEvent> event = MessageBuilder
    .withPayload(new OrderCreatedEvent(orderId, timestamp))
    .setHeader("eventType", "ORDER_CREATED")
    .setHeader("source", "order-service")
    .build();
```

#### Document Message

```java
Message<OrderDocument> doc = MessageBuilder
    .withPayload(orderDocument)
    .setHeader("documentType", "PURCHASE_ORDER")
    .setHeader("version", "2.0")
    .build();
```

#### Request-Reply Message

```java
Message<String> request = MessageBuilder
    .withPayload("Check inventory")
    .setReplyChannel(replyChannel)
    .setHeader("correlationId", UUID.randomUUID().toString())
    .build();
```

## Message Routing

### Routing Patterns

#### Content-Based Router

```
                    ┌──> Channel A (if type=A)
Message ─── Router ─┼──> Channel B (if type=B)
                    └──> Channel C (if type=C)
```

Routes messages based on content inspection.

#### Message Filter

```
Message ─── Filter ──┬──> Channel (if accepted)
                     └──> (discarded)
```

Selectively passes messages based on criteria.

#### Recipient List

```
                    ┌──> Consumer 1
Message ─── Router ─┼──> Consumer 2
                    └──> Consumer 3
```

Dynamic list of recipients determined at runtime.

#### Message Splitter

```
Message (batch) ─── Splitter ──┬──> Message 1
                               ├──> Message 2
                               └──> Message 3
```

Breaks composite messages into individual messages.

#### Message Aggregator

```
Message 1 ─┐
Message 2 ─┼── Aggregator ──> Message (combined)
Message 3 ─┘
```

Combines individual messages into a composite message.

#### Resequencer

```
Message 3 ─┐
Message 1 ─┼── Resequencer ──┬──> Message 1
Message 2 ─┘                 ├──> Message 2
                             └──> Message 3
```

Reorders messages into their original sequence.

## Message Transformation

### Common Transformations

#### Message Translator

```
Message (JSON) ─── Translator ──> Message (XML)
```

Converts message format while preserving content.

#### Content Enricher

```
Message ─── Enricher ──> Message + additional data
```

Adds information from external sources.

#### Content Filter

```
Message (full) ─── Filter ──> Message (partial)
```

Removes or selects specific content.

#### Claim Check

```
Message (large) ─── Store ──> Message (reference)
Message (reference) ─── Retrieve ──> Message (large)
```

Stores large payloads and uses references.

## Messaging Endpoints

### Endpoint Types

#### Inbound Channel Adapter

```java
@Bean
public MessageSource<String> fileSource() {
    return new FileInboundChannelAdapterSpec()
        .directory(new File("/input"))
        .patternFilter("*.csv")
        .get();
}
```

#### Outbound Channel Adapter

```java
@Bean
public MessageHandler fileWriter() {
    return new FileWritingMessageHandler(new File("/output"));
}
```

#### Service Activator

```java
@ServiceActivator(inputChannel = "inputChannel", outputChannel = "outputChannel")
public Message<?> process(Message<?> message) {
    // Process message
    return message;
}
```

#### Transformer

```java
@Transformer(inputChannel = "rawChannel", outputChannel = "transformedChannel")
public Order transform(RawOrder raw) {
    return new Order(raw.getId(), raw.getItems());
}
```

#### Gateway

```java
@MessagingGateway(defaultRequestChannel = "requestChannel")
public interface OrderGateway {
    OrderConfirmation processOrder(@Payload Order order);
}
```

## System Management

### Message Store

- Persistent storage for messages
- Supports deferred processing
- Enables message replay

### Control Bus

- System monitoring and control
- Dynamic configuration
- Health checks

### Message History

- Tracks message flow through system
- Debugging and auditing
- Performance monitoring

## Implementation Considerations

### Reliability

- **At-most-once**: Message may be lost
- **At-least-once**: Message may be duplicated
- **Exactly-once**: Guaranteed single delivery (complex)

### Ordering

- **Global ordering**: All messages in sequence
- **Partition ordering**: Ordered within partitions
- **No ordering**: Best effort

### Error Handling

- **Retry**: Automatic retry on failure
- **Dead Letter**: Failed messages to DLQ
- **Circuit Breaker**: Prevent cascade failures

### Scalability

- **Horizontal scaling**: Add more consumers
- **Partitioning**: Distribute load across channels
- **Load balancing**: Distribute across consumers

## Best Practices

1. **Idempotency**: Design handlers to be idempotent
2. **Correlation**: Use correlation IDs for request-response
3. **Timeouts**: Set appropriate message TTL
4. **Monitoring**: Track message flow and metrics
5. **Documentation**: Document message contracts
6. **Versioning**: Plan for message schema evolution
7. **Security**: Encrypt sensitive data in messages
8. **Testing**: Test with realistic message volumes

## References

- [Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/)
- [EIP Catalog](https://www.enterpriseintegrationpatterns.com/patterns/)
- [Gregor Hohpe - Enterprise Integration Patterns](https://www.amazon.com/Enterprise-Integration-Patterns-Designing-Deploying/dp/0321200683)
