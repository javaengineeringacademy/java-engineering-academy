# Message Routing Patterns

## Overview

Message Routing patterns direct messages to the appropriate destination based on message content, structure, or metadata. These patterns are fundamental to building flexible, decoupled integration architectures where message flow can be dynamically configured without modifying producers or consumers.

---

## Table of Contents

1. [Content-Based Router](#1-content-based-router)
2. [Message Filter](#2-message-filter)
3. [Dynamic Router](#3-dynamic-router)
4. [Recipient List](#4-recipient-list)
5. [Splitter](#5-splitter)
6. [Aggregator](#6-aggregator)
7. [Resequencer](#7-resequencer)
8. [Scatter-Gather](#8-scatter-gather)

---

## 1. Content-Based Router

### Problem

A messaging system needs to route messages to different destinations based on the content or header values of the message. A single input channel must dispatch to multiple output channels without coupling the producer to specific consumers.

### Solution

Implement a Content-Based Router that inspects the message and determines which channel(s) the message should be forwarded to. The router acts as a decision point in the message flow, evaluating conditions against message content and forwarding to the appropriate downstream channel.

### Implementation

```java
@Component
@Slf4j
public class ContentBasedRouter {

    @Bean
    public IntegrationFlow orderRoutingFlow() {
        return IntegrationFlow.from("orderInputChannel")
            .route(Order.class, order -> {
                if (order.getType() == OrderType.PRIORITY) {
                    return "priorityOrderChannel";
                } else if (order.getType() == OrderType.STANDARD) {
                    return "standardOrderChannel";
                } else {
                    return "bulkOrderChannel";
                }
            })
            .get();
    }
}
```

**Spring Integration DSL Configuration:**

```java
@Configuration
@EnableIntegration
public class ContentRouterConfig {

    @Bean
    public IntegrationFlow contentBasedRouter() {
        return IntegrationFlow.from("inputChannel")
            .<String, String>route(
                payload -> {
                    if (payload.contains("ERROR")) return "errorChannel";
                    if (payload.contains("WARNING")) return "warningChannel";
                    return "infoChannel";
                },
                mapping -> mapping
                    .subFlowMapping("errorChannel", sf -> sf
                        .handle(m -> log.error("Error: {}", m.getPayload())))
                    .subFlowMapping("warningChannel", sf -> sf
                        .handle(m -> log.warn("Warning: {}", m.getPayload())))
                    .subFlowMapping("infoChannel", sf -> sf
                        .handle(m -> log.info("Info: {}", m.getPayload())))
            )
            .get();
    }
}
```

**XML Configuration:**

```xml
<int:router input-channel="orderInputChannel"
            output-channel="orderOutputChannel"
            ref="orderRouter"
            method="route"/>

<bean id="orderRouter" class="com.example.OrderRouter"/>
```

### When to Use

- Multiple consumers exist but each should only receive specific message types
- Message processing logic varies based on message content
- You need to implement business rules for message distribution
- Routing logic may change over time without modifying producers

### Trade-offs

| Pros | Cons |
|------|------|
| Decouples producers from consumers | Router becomes a potential bottleneck |
| Centralizes routing logic | Routing rules can become complex |
| Easy to modify routing without affecting producers | May introduce single point of failure |
| Supports conditional message distribution | Performance impact with complex evaluations |

### Production Use Cases

- **E-commerce Order Processing**: Route orders to different fulfillment centers based on product type or customer location
- **Log Management**: Direct log entries to different processing pipelines based on severity level
- **Financial Systems**: Route transactions to different processing queues based on amount thresholds

---

## 2. Message Filter

### Problem

A consumer receives messages it cannot process. The consumer needs a way to receive only messages that meet certain criteria without modifying the producer.

### Solution

Implement a Message Filter that evaluates messages against a set of criteria and forwards only those that pass. Messages that do not meet the criteria can be discarded, logged, or redirected.

### Implementation

```java
@Component
public class MessageFilterExample {

    @Bean
    public IntegrationFlow messageFilterFlow() {
        return IntegrationFlow.from("inputChannel")
            .filter(String.class,
                message -> message.startsWith("VALID"),
                filterSpec -> filterSpec
                    .discardChannel("invalidMessagesChannel")
                    .throwExceptionOnRejection(true))
            .handle(m -> processValidMessage(m.getPayload()))
            .get();
    }

    @Bean
    public IntegrationFlow rejectedMessageFlow() {
        return IntegrationFlow.from("invalidMessagesChannel")
            .handle(m -> logRejectedMessage(m.getPayload()))
            .get();
    }

    private void processValidMessage(String message) {
        // Process valid messages
    }

    private void logRejectedMessage(String message) {
        // Log or store rejected messages
    }
}
```

**Custom Filter with Predicate:**

```java
@Component
public class CustomMessageFilter implements MessageFilter {

    @Override
    public boolean accept(Message<?> message) {
        // Complex filtering logic
        Map<String, Object> headers = message.getHeaders();
        String messageType = (String) headers.get("messageType");
        Long timestamp = (Long) headers.get("timestamp");

        boolean isValidType = Arrays.asList("ORDER", "PAYMENT", "SHIPMENT")
            .contains(messageType);
        boolean isNotExpired = timestamp > System.currentTimeMillis() - 3600000;

        return isValidType && isNotExpired;
    }
}
```

**Filter with AOP-style configuration:**

```java
@Bean
public IntegrationFlow priorityFilter() {
    return IntegrationFlow.from("allMessages")
        .filter(Message.class,
            msg -> {
                String priority = msg.getHeaders().get("priority", String.class);
                return "HIGH".equals(priority) || "CRITICAL".equals(priority);
            })
        .handle(m -> handleHighPriority(m.getPayload()))
        .get();
}
```

### When to Use

- Consumers should only process a subset of messages
- You want to implement message validation at the routing level
- Legacy systems need to filter messages before processing
- You need to implement message-level access control

### Trade-offs

| Pros | Cons |
|------|------|
| Simple to implement | May discard important messages accidentally |
| Reduces unnecessary processing | Filter logic can become complex |
| Can redirect filtered messages | Performance overhead for complex filters |
| Supports message validation | Debugging filtered messages can be difficult |

### Production Use Cases

- **Email Processing**: Filter spam or automated messages before human review
- **Sensor Data**: Filter out sensor readings below a certain threshold
- **API Gateway**: Filter requests based on rate limiting or authentication tokens

---

## 3. Dynamic Router

### Problem

The routing logic changes frequently, and hardcoding routing rules is not feasible. New routes need to be added without restarting the application.

### Solution

Implement a Dynamic Router that can update its routing rules at runtime. The router consults a dynamic registry or service to determine message destinations.

### Implementation

```java
@Component
@Slf4j
public class DynamicRouter {

    private final Map<String, String> routingRegistry = new ConcurrentHashMap<>();

    @Bean
    public IntegrationFlow dynamicRoutingFlow() {
        return IntegrationFlow.from("inputChannel")
            .route(Message.class, this::dynamicRoute)
            .get();
    }

    private String dynamicRoute(Message<?> message) {
        String messageType = message.getHeaders().get("messageType", String.class);
        String destination = routingRegistry.get(messageType);

        if (destination == null) {
            log.warn("No route found for message type: {}", messageType);
            return "defaultChannel";
        }

        return destination;
    }

    @EventListener
    public void onRouteUpdate(RouteUpdateEvent event) {
        routingRegistry.put(event.getMessageType(), event.getDestination());
        log.info("Updated route: {} -> {}", event.getMessageType(), event.getDestination());
    }
}
```

**Service-Registry Based Dynamic Router:**

```java
@Component
public class ServiceRegistryRouter {

    @Autowired
    private RouteRegistryService routeRegistry;

    @Bean
    public IntegrationFlow serviceRegistryFlow() {
        return IntegrationFlow.from("inputChannel")
            .enrichHeaders(h -> h.headerExpression("routingKey", "payload.routingKey"))
            .<Message<?>, String>route(
                msg -> {
                    String routingKey = msg.getHeaders().get("routingKey", String.class);
                    return routeRegistry.resolveDestination(routingKey);
                })
            .get();
    }
}

@Service
public class RouteRegistryService {

    private final Map<String, String> routes = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        routes.put("order.create", "orderCreationChannel");
        routes.put("order.update", "orderUpdateChannel");
        routes.put("payment.process", "paymentProcessingChannel");
    }

    public String resolveDestination(String routingKey) {
        return routes.getOrDefault(routingKey, "defaultChannel");
    }

    @Transactional
    public void updateRoute(String routingKey, String destination) {
        routes.put(routingKey, destination);
    }
}
```

### When to Use

- Routing rules change frequently without application redeployment
- You need to support hot-swappable routing configurations
- Microservices need dynamic service discovery for message routing
- Multi-tenant systems require tenant-specific routing

### Trade-offs

| Pros | Cons |
|------|------|
| Highly flexible routing | Increased complexity |
| Supports runtime configuration | Potential for routing errors |
| Enables hot-swappable routes | Requires careful synchronization |
| Supports microservice discovery | Debugging becomes more difficult |

### Production Use Cases

- **Multi-tenant SaaS**: Route messages based on tenant-specific configurations
- **A/B Testing**: Dynamically route traffic for testing different implementations
- **Service Discovery**: Route messages based on service availability and health

---

## 4. Recipient List

### Problem

A message needs to be sent to multiple consumers, and the list of recipients is determined dynamically based on message content or metadata.

### Solution

Implement a Recipient List that determines the list of destination channels for a message and sends a copy of the message to each destination.

### Implementation

```java
@Component
public class RecipientListExample {

    @Bean
    public IntegrationFlow recipientListFlow() {
        return IntegrationFlow.from("inputChannel")
            .publishSubscribeChannel(Executors.newCachedThreadPool(), channel -> {
                channel.subscribe(m -> handleNotificationService(m));
                channel.subscribe(m -> handleEmailService(m));
                channel.subscribe(m -> handleSmsService(m));
            })
            .get();
    }

    @Bean
    public IntegrationFlow dynamicRecipientFlow() {
        return IntegrationFlow.from("dynamicInputChannel")
            .<Message<?>, List<String>>route(
                msg -> determineRecipients(msg),
                mapping -> mapping
                    .subFlowMapping("notification-service", sf -> sf
                        .handle(m -> notificationService.process(m)))
                    .subFlowMapping("email-service", sf -> sf
                        .handle(m -> emailService.process(m)))
                    .subFlowMapping("audit-service", sf -> sf
                        .handle(m -> auditService.process(m)))
            )
            .get();
    }

    private List<String> determineRecipients(Message<?> message) {
        Map<String, Object> metadata = message.getHeaders();
        List<String> recipients = new ArrayList<>();

        if (metadata.containsKey("notify")) {
            recipients.add("notification-service");
        }
        if (metadata.containsKey("email")) {
            recipients.add("email-service");
        }
        if (metadata.containsKey("audit")) {
            recipients.add("audit-service");
        }

        return recipients;
    }
}
```

### When to Use

- Messages need to be distributed to multiple processing systems
- The list of recipients varies based on message content
- You need to implement fan-out patterns
- Cross-cutting concerns need to be applied to multiple systems

### Trade-offs

| Pros | Cons |
|------|------|
| Flexible message distribution | Increased network traffic |
| Supports dynamic recipient lists | More complex error handling |
| Enables parallel processing | Potential for message duplication |
| Simple to implement | Resource consumption increases with recipients |

### Production Use Cases

- **Event Broadcasting**: Send events to multiple downstream systems
- **Notification Systems**: Distribute notifications across multiple channels
- **Data Synchronization**: Replicate data changes to multiple databases

---

## 5. Splitter

### Problem

A message contains a composite payload that needs to be processed by different consumers. The message must be split into individual messages for independent processing.

### Solution

Implement a Splitter that decomposes a composite message into individual messages, each containing a part of the original payload.

### Implementation

```java
@Component
public class SplitterExample {

    @Bean
    public IntegrationFlow batchOrderSplitter() {
        return IntegrationFlow.from("batchOrderChannel")
            .split(BatchOrder.class, BatchOrder::getOrderItems)
            .channel("individualOrderChannel")
            .get();
    }

    @Bean
    public IntegrationFlow xmlSplitterFlow() {
        return IntegrationFlow.from("xmlInputChannel")
            .split()
            .xpath("/orders/order")
            .channel("individualOrderChannel")
            .get();
    }

    @Bean
    public IntegrationFlow jsonSplitterFlow() {
        return IntegrationFlow.from("jsonInputChannel")
            .split(JsonNode.class, node -> {
                List<Order> orders = new ArrayList<>();
                node.get("items").forEach(item -> {
                    orders.add(new Order(
                        item.get("id").asText(),
                        item.get("product").asText(),
                        item.get("quantity").asInt()
                    ));
                });
                return orders;
            })
            .channel("individualOrderChannel")
            .get();
    }
}
```

**Custom Splitter with Error Handling:**

```java
@Component
@Slf4j
public class CustomSplitter {

    @Bean
    public IntegrationFlow customSplitterFlow() {
        return IntegrationFlow.from("inputChannel")
            .<Message<?>, List<?>>split(
                msg -> {
                    try {
                        return splitMessage(msg);
                    } catch (Exception e) {
                        log.error("Split failed", e);
                        return Collections.emptyList();
                    }
                })
            .channel("outputChannel")
            .get();
    }

    private List<?> splitMessage(Message<?> message) {
        String payload = (String) message.getPayload();
        return Arrays.asList(payload.split(","));
    }
}
```

### When to Use

- Composite messages need to be processed independently
- Batch processing needs to be parallelized
- Large messages need to be chunked for processing
- You need to implement fan-out patterns

### Trade-offs

| Pros | Cons |
|------|------|
| Enables parallel processing | Increased message overhead |
| Simplifies consumer logic | Potential for ordering issues |
| Supports batch processing | Memory consumption for large splits |
| Reduces coupling between producers and consumers | Complex error handling required |

### Production Use Cases

- **Batch Processing**: Split batch files into individual records for processing
- **Order Fulfillment**: Split multi-item orders into individual fulfillment tasks
- **Data Migration**: Split large datasets into smaller chunks for processing

---

## 6. Aggregator

### Problem

Multiple messages need to be combined into a single message for further processing. The aggregation logic must handle messages arriving in different orders and at different times.

### Solution

Implement an Aggregator that collects related messages and combines them when all expected messages have arrived or a timeout occurs.

### Implementation

```java
@Component
@Slf4j
public class OrderAggregator {

    @Bean
    public IntegrationFlow orderAggregationFlow() {
        return IntegrationFlow.from("splitOrderChannel")
            .aggregate(agg -> agg
                .correlationStrategy(msg ->
                    msg.getHeaders().get("orderId", String.class))
                .releaseStrategy(group ->
                    group.size() == group.getSequenceSize())
                .outputProcessor(group -> {
                    List<OrderItem> items = group.getMessages().stream()
                        .map(msg -> (OrderItem) msg.getPayload())
                        .collect(Collectors.toList());
                    return new AggregatedOrder(
                        group.getCorrelationKey().toString(),
                        items);
                })
                .groupTimeout(5000L)
                .sendPartialResultOnExpiry(true))
            .channel("aggregatedOrderChannel")
            .get();
    }
}

@Component
@Slf4j
public class TimeWindowAggregator {

    private final Map<String, List<Event>> eventBuffer = new ConcurrentHashMap<>();

    @Bean
    public IntegrationFlow timeWindowAggregation() {
        return IntegrationFlow.from("eventChannel")
            .aggregate(agg -> agg
                .correlationStrategy(msg ->
                    msg.getHeaders().get("sensorId", String.class))
                .releaseStrategy(group -> {
                    long latestTimestamp = group.getMessages().stream()
                        .mapToLong(msg ->
                            (Long) msg.getHeaders().get("timestamp"))
                        .max()
                        .orElse(0L);
                    long earliestTimestamp = group.getMessages().stream()
                        .mapToLong(msg ->
                            (Long) msg.getHeaders().get("timestamp"))
                        .min()
                        .orElse(0L);
                    return latestTimestamp - earliestTimestamp >= 60000; // 1 minute window
                })
                .groupTimeout(60000L)
                .sendPartialResultOnExpiry(true))
            .channel("aggregatedEventChannel")
            .get();
    }
}
```

### When to Use

- Results from parallel processing need to be combined
- Multiple related messages need to be processed as a unit
- You need to implement request aggregation patterns
- Time-window-based aggregation is required

### Trade-offs

| Pros | Cons |
|------|------|
| Enables parallel processing results combination | State management complexity |
| Supports correlation-based aggregation | Potential for memory leaks |
| Handles out-of-order messages | Timeout handling complexity |
| Supports partial result processing | Resource consumption |

### Production Use Cases

- **Distributed Transactions**: Aggregate results from multiple service calls
- **Batch Reporting**: Combine results from parallel data processing
- **Sensor Data**: Aggregate sensor readings over time windows

---

## 7. Resequencer

### Problem

Messages arrive out of order and need to be processed in a specific sequence. The system must reorder messages before they reach consumers.

### Solution

Implement a Resequencer that collects messages and releases them in the correct order based on sequence numbers or timestamps.

### Implementation

```java
@Component
@Slf4j
public class MessageResequencer {

    @Bean
    public IntegrationFlow resequencingFlow() {
        return IntegrationFlow.from("outOfOrderChannel")
            .resequence(resh -> resh
                .correlationStrategy(msg ->
                    msg.getHeaders().get("batchId", String.class))
                .releaseStrategy(group -> {
                    List<Message<?>> messages = new ArrayList<>(
                        group.getMessages());
                    messages.sort(Comparator.comparingInt(
                        m -> (Integer) m.getHeaders().get("sequenceNumber")));
                    return isSequential(messages);
                })
                .groupTimeout(10000L)
                .sendPartialResultOnExpiry(true))
            .channel("inOrderChannel")
            .get();
    }

    private boolean isSequential(List<Message<?>> messages) {
        for (int i = 0; i < messages.size() - 1; i++) {
            int current = (Integer) messages.get(i).getHeaders()
                .get("sequenceNumber");
            int next = (Integer) messages.get(i + 1).getHeaders()
                .get("sequenceNumber");
            if (next != current + 1) {
                return false;
            }
        }
        return true;
    }
}
```

**Timestamp-Based Resequencer:**

```java
@Component
public class TimestampResequencer {

    @Bean
    public IntegrationFlow timestampBasedResequence() {
        return IntegrationFlow.from("unorderedChannel")
            .resequence(resh -> resh
                .correlationStrategy(msg ->
                    msg.getHeaders().get("conversationId", String.class))
                .releaseStrategy(group -> {
                    long maxTimestamp = group.getMessages().stream()
                        .mapToLong(msg -> (Long) msg.getHeaders().get("timestamp"))
                        .max()
                        .orElse(0L);
                    long minTimestamp = group.getMessages().stream()
                        .mapToLong(msg -> (Long) msg.getHeaders().get("timestamp"))
                        .min()
                        .orElse(Long.MAX_VALUE);
                    return maxTimestamp - minTimestamp <= 5000; // 5 second window
                }))
            .channel("orderedChannel")
            .get();
    }
}
```

### When to Use

- Messages must be processed in strict sequence order
- Network issues cause message reordering
- You need to implement exactly-once processing guarantees
- Order-dependent business logic requires sequential processing

### Trade-offs

| Pros | Cons |
|------|------|
| Guarantees message ordering | Memory consumption for buffering |
| Supports sequence-based reordering | Potential for deadlock with missing messages |
| Handles out-of-order delivery | Complexity increases with message volume |
| Supports timeout-based release | Performance impact under high load |

### Production Use Cases

- **Financial Transactions**: Ensure transactions are processed in chronological order
- **Event Sourcing**: Maintain event ordering for state reconstruction
- **Log Processing**: Reorder log entries from multiple sources

---

## 8. Scatter-Gather

### Problem

A request needs to be sent to multiple services, and the responses from all services must be combined into a single response before returning to the caller.

### Solution

Implement Scatter-Gather that sends a request to multiple services in parallel, collects all responses, and aggregates them into a unified response.

### Implementation

```java
@Component
@Slf4j
public class ScatterGatherExample {

    @Bean
    public IntegrationFlow scatterGatherFlow() {
        return IntegrationFlow.from("aggregationRequestChannel")
            .scatterGather(
                scatterer -> scatterer
                    .recipientFlow("service-a-channel")
                    .recipientFlow("service-b-channel")
                    .recipientFlow("service-c-channel"),
                gatherer -> gatherer
                    .releaseStrategy(group -> group.size() == 3)
                    .outputProcessor(group -> {
                        List<Object> results = group.getMessages().stream()
                            .map(Message::getPayload)
                            .collect(Collectors.toList());
                        return new AggregatedResponse(results);
                    })
                    .groupTimeout(10000L)
                    .sendPartialResultOnExpiry(true))
            .channel("aggregatedResponseChannel")
            .get();
    }
}
```

**Dynamic Scatter-Gather:**

```java
@Component
public class DynamicScatterGather {

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Bean
    public IntegrationFlow dynamicScatterGather() {
        return IntegrationFlow.from("dynamicRequestChannel")
            .scatterGather(
                scatterer -> scatterer
                    .applySequence(true)
                    .recipientFlow(msg -> {
                        List<String> services = serviceRegistry
                            .getAvailableServices(msg);
                        return services.stream()
                            .map(service -> "service-" + service + "-channel")
                            .collect(Collectors.toList());
                    }),
                gatherer -> gatherer
                    .releaseStrategy(group ->
                        group.size() == group.getExpectedCount())
                    .groupTimeout(30000L))
            .channel("dynamicResponseChannel")
            .get();
    }
}
```

### When to Use

- Multiple parallel service calls are required for a single request
- Response aggregation from multiple sources is needed
- You need to implement parallel request patterns
- Timeout handling for distributed requests is required

### Trade-offs

| Pros | Cons |
|------|------|
| Parallel processing improves performance | Increased complexity |
| Supports timeout handling | Resource consumption |
| Enables partial result processing | Error handling complexity |
| Flexible service composition | Potential for cascading failures |

### Production Use Cases

- **API Gateway**: Aggregate responses from multiple microservices
- **Search Systems**: Query multiple data sources in parallel
- **Price Comparison**: Fetch prices from multiple vendors simultaneously

---

## Pattern Comparison Matrix

| Pattern | Primary Use | Complexity | Performance Impact | Use When |
|---------|-------------|------------|-------------------|----------|
| Content-Based Router | Message routing by content | Low | Low | Multiple consumers, different processing |
| Message Filter | Message selection | Low | Low | Consumer needs subset of messages |
| Dynamic Router | Runtime routing changes | Medium | Medium | Routing rules change frequently |
| Recipient List | Multi-destination messaging | Medium | Medium | Fan-out to multiple consumers |
| Splitter | Message decomposition | Medium | Medium | Composite messages, parallel processing |
| Aggregator | Message combination | High | Medium | Parallel processing results combination |
| Resequencer | Message ordering | High | High | Strict ordering requirements |
| Scatter-Gather | Parallel request aggregation | High | High | Multiple parallel service calls |

---

## References

- Enterprise Integration Patterns - Gregor Hohpe, Bobby Woolf
- Spring Integration Reference Guide
- Apache Camel Documentation
- Apache ServiceMix / ActiveMQ
