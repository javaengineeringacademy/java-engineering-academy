# Enterprise Integration Patterns - Complete Catalog

## Overview

This document catalogs all 65+ Enterprise Integration Patterns (EIP) organized by category. Each pattern includes a description, problem statement, solution, and implementation examples.

## Table of Contents

1. [Messaging Patterns](#messaging-patterns)
2. [Message Channel Patterns](#message-channel-patterns)
3. [Message Routing Patterns](#message-routing-patterns)
4. [Message Transformation Patterns](#message-transformation-patterns)
5. [Messaging Endpoint Patterns](#messaging-endpoint-patterns)
6. [System Management Patterns](#system-management-patterns)

---

## Messaging Patterns

### 1. Message Channel

**Problem**: How can one application communicate with another using messaging?

**Solution**: Use a Message Channel, a conduit that links senders and receivers.

```
┌──────────┐      ┌──────────┐      ┌──────────┐
│ Producer │ ───> │ Channel  │ ───> │ Consumer │
└──────────┘      └──────────┘      └──────────┘
```

**Variants**:
- Point-to-Point Channel
- Publish-Subscribe Channel
- Datatype Channel
- Invalid Message Channel
- Dead Letter Channel
- Guaranteed Delivery Channel
- Channel Adapter
- Messaging Bridge
- Message Bus

### 2. Message

**Problem**: How can two applications exchange data via messaging?

**Solution**: Use a Message, a data structure encapsulating data with headers and payload.

```json
{
  "headers": {
    "messageId": "msg-123",
    "timestamp": 1705312200000,
    "correlationId": "corr-456"
  },
  "payload": {
    "type": "OrderCreated",
    "data": { ... }
  }
}
```

### 3. Command Message

**Problem**: How can messaging be used to invoke a procedure in another application?

**Solution**: Use a Command Message to tell the receiving application to perform a specific action.

```xml
<command>
  <type>ProcessOrder</type>
  <orderId>12345</orderId>
  <action>APPROVE</action>
</command>
```

### 4. Document Message

**Problem**: How can messaging be used to transfer data between applications?

**Solution**: Use a Document Message to reliably deliver a data structure.

```json
{
  "documentType": "PurchaseOrder",
  "version": "2.0",
  "content": { ... }
}
```

### 5. Event Message

**Problem**: How can messaging be used to deliver event notifications?

**Solution**: Use an Event Message to deliver notification of an event.

```json
{
  "eventType": "OrderShipped",
  "timestamp": "2024-01-15T10:30:00Z",
  "data": { ... }
}
```

### 6. Request-Reply

**Problem**: How can the caller obtain a result from the receiver using messaging?

**Solution**: Use a Request-Reply pattern with two channels.

```
Caller ─── Request ──> Receiver
Caller <── Reply ──── Receiver
```

### 7. Return Address

**Problem**: How can a reply be sent to the correct application?

**Solution**: Include a Return Address in the message header specifying the reply channel.

```java
Message<String> request = MessageBuilder
    .withPayload("data")
    .setReplyChannel(replyChannel)
    .build();
```

### 8. Correlation Identifier

**Problem**: How can a requester correlate a request message with a reply message?

**Solution**: Include a Correlation Identifier in the message headers.

```java
String correlationId = UUID.randomUUID().toString();
Message<String> request = MessageBuilder
    .withPayload("data")
    .setHeader("correlationId", correlationId)
    .build();
```

### 9. Correlation Identifier

**Problem**: How can a requestor that sends more than one request correlate requests with replies?

**Solution**: Use a unique Correlation Identifier for each request-reply pair.

### 10. Message Sequence

**Problem**: How can messaging transmit a block of data that is too large for a single message?

**Solution**: Split the data into a Message Sequence with sequence information.

```java
// Split large payload into sequence
List<Message<DataChunk>> chunks = splitPayload(largePayload, chunkSize);
for (int i = 0; i < chunks.size(); i++) {
    chunks.get(i).getHeaders().setHeader("sequenceNumber", i + 1);
    chunks.get(i).getHeaders().setHeader("sequenceSize", chunks.size());
}
```

---

## Message Channel Patterns

### 11. Point-to-Point Channel

**Problem**: How can the messaging system ensure that only one receiver gets each message?

**Solution**: Use a Point-to-Point Channel that delivers each message to exactly one consumer.

```
Producer ──── Queue ──── Consumer 1
                    └─── Consumer 2 (next message)
```

### 12. Publish-Subscribe Channel

**Problem**: How can the messaging system broadcast a message to all interested receivers?

**Solution**: Use a Publish-Subscribe Channel that delivers each message to all subscribers.

```
                    ┌──> Subscriber 1
Publisher ─── Topic ─┼──> Subscriber 2
                    └──> Subscriber 3
```

### 13. Datatype Channel

**Problem**: How does the messaging system ensure proper processing of messages?

**Solution**: Use a Datatype Channel that is typed to a specific data format.

```java
MessageChannel stringChannel = new DirectChannel(); // String messages only
MessageChannel orderChannel = new DirectChannel(); // Order messages only
```

### 14. Invalid Message Channel

**Problem**: What should the messaging system do with messages that cannot be processed?

**Solution**: Use an Invalid Message Channel to receive messages that fail processing.

```java
@Bean
public MessageChannel invalidChannel() {
    return new QueueChannel();
}

@ServiceActivator(inputChannel = "processingChannel", 
                  errorChannel = "invalidChannel")
public Message<?> process(Message<?> message) {
    // Process or throw exception
}
```

### 15. Dead Letter Channel

**Problem**: What should be done with messages that cannot be delivered or processed?

**Solution**: Use a Dead Letter Channel to capture failed messages.

```java
@Bean
public MessageHandler deadLetterHandler() {
    return new MessageHandler() {
        @Override
        public void handleMessage(Message<?> message) {
            // Log and store failed message
            log.error("Dead letter: {}", message);
        }
    };
}
```

### 16. Guaranteed Delivery

**Problem**: How can messaging be made reliable?

**Solution**: Use Guaranteed Delivery with persistent storage and acknowledgment.

```
Producer ───> Store-and-Forward ───> Channel ───> Consumer
                    │                              │
                    └── Persistence ────────────────┘
```

### 17. Channel Adapter

**Problem**: How can an application be connected to a messaging system?

**Solution**: Use a Channel Adapter to bridge between application and messaging system.

```java
@Bean
public MessageSource<File> fileAdapter() {
    return new FileInboundChannelAdapterSpec()
        .directory(new File("/input"))
        .get();
}
```

### 18. Messaging Bridge

**Problem**: How can multiple messaging systems be connected?

**Solution**: Use a Messaging Bridge to transfer messages between systems.

### 19. Message Bus

**Problem**: How can many applications communicate via a shared messaging infrastructure?

**Solution**: Use a Message Bus as a shared messaging backbone.

---

## Message Routing Patterns

### 20. Content-Based Router

**Problem**: How can the consumer select which message to process based on content?

**Solution**: Use a Content-Based Router to route messages based on content inspection.

```java
@Bean
public IntegrationFlow routingFlow() {
    return IntegrationFlow.from("inputChannel")
        .route(String.class, payload -> {
            if (payload.contains("ORDER")) return "orderChannel";
            if (payload.contains("INVOICE")) return "invoiceChannel";
            return "defaultChannel";
        })
        .get();
}
```

### 21. Message Filter

**Problem**: How can a component avoid receiving unwanted messages?

**Solution**: Use a Message Filter to selectively accept or discard messages.

```java
@Bean
public MessageSelector highPriorityFilter() {
    return message -> {
        Integer priority = message.getHeaders().get("priority", Integer.class);
        return priority != null && priority >= 8;
    };
}
```

### 22. Dynamic Router

**Problem**: How can a message be routed without the sender knowing the destinations?

**Solution**: Use a Dynamic Router that determines destinations at runtime.

```java
@ServiceActivator(inputChannel = "dynamicRouterChannel")
public String route(String message, @Header("routingKey") String key) {
    return routingTable.get(key);
}
```

### 23. Recipient List

**Problem**: How can a message be sent to a dynamic list of recipients?

**Solution**: Use a Recipient List to determine recipients at runtime.

```java
@Bean
public MessageHandler recipientListHandler() {
    RecipientListRouter router = new RecipientListRouter();
    router.setRecipientExpression("headers['recipients']");
    return router;
}
```

### 24. Splitter

**Problem**: How can a message be processed if it contains a block of data that needs to be split?

**Solution**: Use a Splitter to break a composite message into individual messages.

```java
@Bean
public MessageHandler splitter() {
    return new MessageHandler() {
        @Override
        public void handleMessage(Message<?> message) {
            List<OrderItem> items = (List<OrderItem>) message.getPayload();
            for (OrderItem item : items) {
                outputChannel.send(MessageBuilder.withPayload(item).build());
            }
        }
    };
}
```

### 25. Aggregator

**Problem**: How can a set of related messages be combined into a single message?

**Solution**: Use an Aggregator to collect and combine related messages.

```java
@Bean
public MessageHandler aggregator() {
    return new AggregatingMessageHandler(
        new MessageGroupProcessor() {
            @Override
            public Object aggregate(MessageGroup group) {
                return group.getMessages().stream()
                    .map(Message::getPayload)
                    .collect(Collectors.toList());
            }
        }
    );
}
```

### 26. Resequencer

**Problem**: How can a set of related messages be put into a specific order?

**Solution**: Use a Resequencer to reorder messages based on sequence information.

```java
@Bean
public MessageHandler resequencer() {
    ResequencingMessageHandler handler = new ResequencingMessageHandler();
    handler.setReleasePartialSequences(false);
    return handler;
}
```

### 27. Composed Message Processor

**Problem**: How can a set of steps that each process a message be implemented?

**Solution**: Use a Composed Message Processor to chain processing steps.

### 28. Scatter-Gather

**Problem**: How can a request message be sent to multiple recipients and the responses combined?

**Solution**: Use Scatter-Gather to broadcast and aggregate responses.

```
                ┌──> Recipient 1 ───> Response 1 ─┐
Request ─── Scatter                               Aggregator ───> Combined
                └──> Recipient 2 ───> Response 2 ─┘                   Response
```

### 29. Routing Slip

**Problem**: How can a message be routed through a series of processing steps dynamically?

**Solution**: Use a Routing Slip to define the processing path in the message.

```java
String[] steps = {"validate", "enrich", "transform", "send"};
message.getHeaders().setHeader("routingSlip", steps);
```

### 30. Wire Tap

**Problem**: How can you monitor messages flowing through a channel?

**Solution**: Use a Wire Tap to copy messages to a monitoring channel.

```java
@Bean
public MessageHandler wireTap() {
    return message -> {
        auditChannel.send(message);
        log.info("Message tapped: {}", message.getPayload());
    };
}
```

### 31. Message Router

**Problem**: How can you control the sequence of message processing steps?

**Solution**: Use a Message Router to determine the next processing step.

### 32. Process Manager

**Problem**: How can you manage a complex process involving multiple steps and participants?

**Solution**: Use a Process Manager to orchestrate the workflow.

### 33. Message Broker

**Problem**: How can you decouple message producers from consumers?

**Solution**: Use a Message Broker to mediate between producers and consumers.

---

## Message Transformation Patterns

### 34. Message Translator

**Problem**: How should a system translate data from one format to another?

**Solution**: Use a Message Translator to convert message formats.

```java
@Transformer(inputChannel = "jsonChannel", outputChannel = "xmlChannel")
public Document translate(Order order) {
    return JAXB.marshal(order);
}
```

### 35. Content Enricher

**Problem**: How does a receiver get the data it needs if it doesn't have access to a data source?

**Solution**: Use a Content Enricher to add information from external sources.

```java
@Bean
public MessageHandler enricher() {
    ContentEnricher enricher = new ContentEnricher();
    enricher.setShouldCopyPayload(false);
    enricher.setHeaderExpressions(Map.of(
        "customerInfo", "payload.customerId"
    ));
    enricher.setRequestChannel(customerLookupChannel);
    return enricher;
}
```

### 36. Content Filter

**Problem**: How do you remove unnecessary data from a message?

**Solution**: Use a Content Filter to remove or select specific content.

```java
@Transformer(inputChannel = "fullMessage", outputChannel = "filteredMessage")
public OrderSummary filter(Order order) {
    return new OrderSummary(order.getId(), order.getTotal());
}
```

### 37. Message Filter

**Problem**: How do you remove unwanted messages from a channel?

**Solution**: Use a Message Filter to selectively pass or discard messages.

### 38. Claim Check

**Problem**: How do you handle large messages that cannot be transmitted efficiently?

**Solution**: Use a Claim Check to store large payloads and pass references.

```java
@Bean
public MessageHandler claimCheckIn() {
    return message -> {
        Object payload = message.getPayload();
        String reference = store.put(payload);
        outputChannel.send(MessageBuilder
            .withPayload(reference)
            .setHeader("claimCheck", true)
            .build());
    };
}
```

### 39. Content-Based Router

**Problem**: How do you route messages based on their content?

**Solution**: Use a Content-Based Router to inspect and route.

### 40. Normalizer

**Problem**: How do you process messages from multiple formats?

**Solution**: Use a Normalizer to convert multiple formats to a canonical format.

```java
@Bean
public MessageHandler normalizer() {
    MessageRouter router = new MessageRouter();
    router.setChannelMapping("JSON", "jsonNormalizer");
    router.setChannelMapping("XML", "xmlNormalizer");
    router.setChannelMapping("CSV", "csvNormalizer");
    return router;
}
```

### 41. Enricher

**Problem**: How do you combine data from multiple sources?

**Solution**: Use an Enricher to aggregate data from multiple sources.

### 42. Template Message

**Problem**: How do you construct messages using templates?

**Solution**: Use a Template Message with placeholders for dynamic content.

---

## Messaging Endpoint Patterns

### 43. Messaging Gateway

**Problem**: How do you integrate an application with a messaging system without coupling to the messaging API?

**Solution**: Use a Messaging Gateway to hide messaging complexity.

```java
@MessagingGateway(defaultRequestChannel = "requestChannel")
public interface OrderGateway {
    OrderConfirmation processOrder(@Payload Order order);
    
    @Gateway(requestChannel = "asyncChannel")
    void processOrderAsync(@Payload Order order);
}
```

### 44. Service Activator

**Problem**: How do you design a service to be invoked via messaging?

**Solution**: Use a Service Activator to invoke a service from a message channel.

```java
@ServiceActivator(inputChannel = "inputChannel", outputChannel = "outputChannel")
public Message<?> activate(Message<?> message) {
    Object payload = message.getPayload();
    Object result = service.process(payload);
    return MessageBuilder.withPayload(result).build();
}
```

### 45. Channel Adapter

**Problem**: How do you connect an application to a messaging system?

**Solution**: Use a Channel Adapter as the connection point.

### 46. Messaging Bridge

**Problem**: How do you connect two messaging systems?

**Solution**: Use a Messaging Bridge to transfer messages between systems.

### 47. Message Endpoint

**Problem**: How do you hide the messaging system from the application?

**Solution**: Use a Message Endpoint to isolate application logic from messaging.

---

## System Management Patterns

### 48. Control Bus

**Problem**: How do you manage a messaging system?

**Solution**: Use a Control Bus to monitor and control the messaging system.

```java
@Bean
public MessageHandler controlBusHandler() {
    return message -> {
        String command = (String) message.getPayload();
        if ("START".equals(command)) {
            startComponents();
        } else if ("STOP".equals(command)) {
            stopComponents();
        }
    };
}
```

### 49. Detour

**Problem**: How do you route messages to intermediate destinations for processing?

**Solution**: Use a Detour to optionally redirect messages for additional processing.

### 50. Wire Tap

**Problem**: How do you monitor messages flowing through a channel?

**Solution**: Use a Wire Tap to copy messages to a monitoring channel.

### 51. Message History

**Problem**: How do you track the flow of messages through the system?

**Solution**: Use Message History to record processing steps.

```java
@Bean
public MessageHandler historyTracker() {
    return message -> {
        List<String> history = message.getHeaders()
            .get("messageHistory", List.class);
        history.add(new Date() + ": " + "Processing at step X");
        message.getHeaders().put("messageHistory", history);
    };
}
```

### 52. Message Store

**Problem**: How do you persist messages for later retrieval?

**Solution**: Use a Message Store to persist messages.

```java
@Bean
public MessageStore messageStore() {
    return new JdbcMessageStore(dataSource());
}
```

### 53. Smart Proxy

**Problem**: How do you intercept and modify messages in transit?

**Solution**: Use a Smart Proxy to intercept messages.

### 54. Test Message Endpoint

**Problem**: How do you test messaging-based applications?

**Solution**: Use Test Message Endpoints to capture messages for verification.

### 55. Channel Purger

**Problem**: How do you clear messages from a channel?

**Solution**: Use a Channel Purger to remove messages.

---

## Enterprise Integration Patterns Summary

### Pattern Categories

| Category | Patterns | Purpose |
|----------|----------|---------|
| Messaging | 10 | Message types and styles |
| Channels | 9 | Message transport |
| Routing | 14 | Message routing |
| Transformation | 9 | Data conversion |
| Endpoints | 5 | System integration |
| Management | 8 | System control |

### Pattern Relationships

```
                    ┌─────────────────┐
                    │   MESSAGE       │
                    │   CONSTRUCTION  │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │   MESSAGE       │
                    │   CHANNEL       │
                    └────────┬────────┘
                             │
           ┌─────────────────┼─────────────────┐
           │                 │                 │
    ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐
    │   ROUTING   │  │TRANSLATION  │  │  ENDPOINTS  │
    └──────┬──────┘  └──────┬──────┘  └──────┬──────┘
           │                │                │
           └────────────────┼────────────────┘
                            │
                   ┌────────▼────────┐
                   │    SYSTEM       │
                   │   MANAGEMENT    │
                   └─────────────────┘
```

## Implementation Guide

### Choosing Patterns

1. **Identify Integration Points**: Determine what systems need to communicate
2. **Select Communication Style**: Choose between synchronous/asynchronous
3. **Choose Routing**: Determine how messages will be routed
4. **Plan Transformations**: Identify format conversions needed
5. **Design Error Handling**: Plan for failure scenarios
6. **Consider Monitoring**: Include management and monitoring patterns

### Common Pattern Combinations

- **Request-Reply + Correlation ID**: For synchronous integrations
- **Content-Based Router + Splitter**: For complex routing scenarios
- **Aggregator + Resequencer**: For collecting and ordering results
- **Dead Letter Channel + Retry**: For reliable error handling
- **Wire Tap + Message History**: For auditing and monitoring

## References

- [Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/)
- [EIP Implementation Patterns](https://www.enterpriseintegrationpatterns.com/patterns/)
- [Gregor Hohpe - Enterprise Integration Patterns](https://www.amazon.com/Enterprise-Integration-Patterns-Designing-Deploying/dp/0321200683)
