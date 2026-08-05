# Message Endpoint Patterns

## Overview

Message Endpoint patterns provide the connection point between messaging systems and application code. These patterns define how applications send and receive messages, bridging the gap between messaging infrastructure and business logic.

---

## Table of Contents

1. [Service Activator](#1-service-activator)
2. [Messaging Bridge](#2-messaging-bridge)
3. [Service Facade](#3-service-facade)
4. [Gateway](#4-gateway)
5. [Splitter](#5-splitter)
6. [Aggregator](#6-aggregator)
7. [Dispatcher](#7-dispatcher)
8. [Adapter](#8-adapter)

---

## 1. Service Activator

### Problem

Business logic needs to be triggered by messages from a messaging system. The application must receive messages and invoke corresponding business operations without tight coupling to the messaging infrastructure.

### Solution

Implement a Service Activator that connects a message channel to a service, allowing the service to be invoked by receiving messages and optionally sending reply messages.

### Implementation

```java
@Component
@Slf4j
public class OrderServiceActivator {

    @ServiceActivator(inputChannel = "orderInputChannel",
                     outputChannel = "orderOutputChannel")
    public OrderConfirmation processOrder(Order order) {
        log.info("Processing order: {}", order.getOrderId());

        // Business logic
        order.setStatus(OrderStatus.PROCESSING);
        order.setProcessedAt(Instant.now());

        // Validate and process
        validateOrder(order);
        calculateTotal(order);
        applyDiscounts(order);

        // Create confirmation
        OrderConfirmation confirmation = new OrderConfirmation();
        confirmation.setOrderId(order.getOrderId());
        confirmation.setStatus("PROCESSED");
        confirmation.setTotal(order.getTotal());
        confirmation.setEstimatedDelivery(
            calculateDeliveryDate(order));

        return confirmation;
    }

    private void validateOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order has no items");
        }
    }

    private void calculateTotal(Order order) {
        BigDecimal total = order.getItems().stream()
            .map(item -> item.getPrice().multiply(
                BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
    }

    private void applyDiscounts(Order order) {
        if (order.getCustomer().isPremium()) {
            BigDecimal discount = order.getTotal()
                .multiply(BigDecimal.valueOf(0.1));
            order.setDiscount(discount);
            order.setTotal(order.getTotal().subtract(discount));
        }
    }

    private Instant calculateDeliveryDate(Order order) {
        return Instant.now().plus(Duration.ofDays(
            order.isExpedited() ? 2 : 7));
    }
}
```

**Spring Integration Configuration:**

```java
@Configuration
@EnableIntegration
public class ServiceActivatorConfig {

    @Bean
    public IntegrationFlow serviceActivatorFlow() {
        return IntegrationFlow.from("inputChannel")
            .handle(Order.class, (payload, headers) -> {
                // Process order
                return processOrder(payload);
            })
            .channel("outputChannel")
            .get();
    }

    @Bean
    public IntegrationFlow asyncServiceActivatorFlow() {
        return IntegrationFlow.from("asyncInputChannel")
            .handle(Order.class, (payload, headers) -> {
                CompletableFuture<OrderConfirmation> future =
                    CompletableFuture.supplyAsync(() ->
                        processOrder(payload));
                return future;
            })
            .channel("asyncOutputChannel")
            .get();
    }
}
```

**Error-Handling Service Activator:**

```java
@Component
@Slf4j
public class RobustServiceActivator {

    @ServiceActivator(inputChannel = "inputChannel",
                     outputChannel = "outputChannel",
                     errorChannel = "errorChannel")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public String processWithRetry(String payload) {
        log.info("Processing payload: {}", payload);

        // Processing logic
        return "Processed: " + payload;
    }

    @ServiceActivator(inputChannel = "errorChannel")
    public void handleError(ErrorMessage errorMessage) {
        log.error("Error processing message: {}",
            errorMessage.getPayload());

        // Error handling logic
        sendToDeadLetterQueue(errorMessage);
        notifyOperations(errorMessage);
    }
}
```

### When to Use

- Business logic needs to be triggered by messages
- Asynchronous processing is required
- You need to decouple business logic from messaging infrastructure
- Services need to be invoked from multiple message channels

### Trade-offs

| Pros | Cons |
|------|------|
| Simple to implement | Synchronous processing limitations |
| Easy to test business logic | Error handling complexity |
| Supports request-reply patterns | Resource management |
| Decouples business from messaging | State management challenges |

### Production Use Cases

- **Order Processing**: Trigger order fulfillment when orders are received
- **Email Processing**: Process incoming email messages
- **Data Validation**: Validate and transform incoming data

---

## 2. Messaging Bridge

### Problem

Two separate messaging systems need to communicate. Messages from one system must be forwarded to another system without modifying the message content.

### Solution

Implement a Messaging Bridge that connects two messaging systems, forwarding messages between them while maintaining message integrity and optionally handling protocol differences.

### Implementation

```java
@Component
@Slf4j
public class MessagingBridgeExample {

    @Bean
    public IntegrationFlow jmsToAmqpBridge() {
        return IntegrationFlow.from(
                Jms.messageDrivenChannelAdapter("jmsConnectionFactory")
                    .destination("incoming.queue"))
            .transform(String.class, payload -> {
                log.info("Bridging message from JMS to AMQP");
                return payload;
            })
            .handle(Amqp.outboundAdapter(amqpTemplate())
                .routingKey("outgoing.queue"))
            .get();
    }

    @Bean
    public IntegrationFlow amqpToJmsBridge() {
        return IntegrationFlow.from(
                Amqp.inboundAdapter(amqpListenerContainerFactory(),
                    "incoming.amqp.queue"))
            .transform(byte[].class, payload -> {
                log.info("Bridging message from AMQP to JMS");
                return new String(payload);
            })
            .handle(Jms.outboundAdapter(jmsConnectionFactory())
                .destination("outgoing.jms.queue"))
            .get();
    }
}
```

**Protocol Bridge with Transformation:**

```java
@Component
public class ProtocolTransformationBridge {

    @Bean
    public IntegrationFlow ftpToHttpBridge() {
        return IntegrationFlow.from(
                Ftp.inboundAdapter(ftpTemplate())
                    .remoteDirectory("/incoming")
                    .patternFilter("*.xml"))
            .transform(File.class, file -> {
                try {
                    String content = new String(
                        Files.readAllBytes(file.toPath()));
                    return convertFtpToHttpPayload(content);
                } catch (IOException e) {
                    throw new RuntimeException("Bridge processing failed", e);
                }
            })
            .handle(Http.outboundChannelAdapter("http://api.example.com/data")
                .httpMethod(HttpMethod.POST)
                .expectedResponseType(String.class))
            .get();
    }
}
```

**File System to Message Queue Bridge:**

```java
@Component
public class FileToQueueBridge {

    @Bean
    public IntegrationFlow fileToJmsBridge() {
        return IntegrationFlow.from(
                Files.inboundAdapter(new File("/input"))
                    .preventDuplicates(true))
            .transform(File.class, this::processFile)
            .handle(Jms.outboundAdapter(jmsConnectionFactory())
                .destination("processed.files.queue"))
            .get();
    }

    private String processFile(File file) {
        // Process file content
        return processFileContent(file);
    }
}
```

### When to Use

- Integrating different messaging technologies (JMS, AMQP, Kafka)
- Migrating from one messaging system to another
- Connecting disparate systems across network boundaries
- Implementing multi-protocol messaging

### Trade-offs

| Pros | Cons |
|------|------|
| Enables system integration | Added complexity |
| Supports protocol conversion | Potential for message loss |
| Maintains message integrity | Performance overhead |
| Enables gradual migration | Debugging complexity |

### Production Use Cases

- **Hybrid Cloud**: Bridge on-premises JMS with cloud-based AMQP
- **System Migration**: Gradually migrate from one message broker to another
- **Multi-Protocol Systems**: Connect systems using different messaging protocols

---

## 3. Service Facade

### Problem

Multiple services need to be exposed through a unified interface. Clients should be able to interact with multiple backend services through a single endpoint.

### Solution

Implement a Service Facade that provides a unified interface to multiple backend services, abstracting the complexity of service interactions and providing a simplified API.

### Implementation

```java
@Component
@Slf4j
@ServiceFacade
public class OrderServiceFacade {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private NotificationService notificationService;

    @ServiceActivator(inputChannel = "facadeRequestChannel",
                     outputChannel = "facadeResponseChannel")
    public OrderResult processCompleteOrder(CompleteOrderRequest request) {
        log.info("Processing complete order via facade");

        try {
            // Step 1: Check inventory
            InventoryResult inventory = inventoryService
                .checkAvailability(request.getItems());

            if (!inventory.isAvailable()) {
                return OrderResult.failure("Items not available");
            }

            // Step 2: Process payment
            PaymentResult payment = paymentService
                .processPayment(request.getPaymentInfo());

            if (!payment.isSuccessful()) {
                return OrderResult.failure("Payment failed");
            }

            // Step 3: Arrange shipping
            ShippingResult shipping = shippingService
                .arrangeShipping(request.getShippingAddress());

            // Step 4: Send confirmation
            notificationService.sendOrderConfirmation(
                request.getCustomerEmail(),
                createConfirmation(inventory, payment, shipping));

            return OrderResult.success(
                createOrderConfirmation(inventory, payment, shipping));

        } catch (Exception e) {
            log.error("Order processing failed", e);
            return OrderResult.failure("Order processing failed: " +
                e.getMessage());
        }
    }
}
```

**Multi-Service Facade:**

```java
@Component
@Slf4j
public class CustomerServiceFacade {

    @ServiceActivator(inputChannel = "customerRequestChannel")
    public CustomerProfile getCustomerProfile(String customerId) {
        log.info("Building customer profile via facade");

        // Gather data from multiple services
        CustomerData customer = customerDataService.getCustomer(customerId);
        OrderHistory orders = orderService.getOrderHistory(customerId);
        LoyaltyStatus loyalty = loyaltyService.getLoyaltyStatus(customerId);
        Preferences preferences = preferenceService.getPreferences(customerId);

        // Build unified profile
        CustomerProfile profile = new CustomerProfile();
        profile.setCustomerData(customer);
        profile.setOrderHistory(orders);
        profile.setLoyaltyStatus(loyalty);
        profile.setPreferences(preferences);

        return profile;
    }
}
```

### When to Use

- Multiple services need to be exposed through a single interface
- Client complexity needs to be reduced
- You need to implement composite service patterns
- Cross-cutting concerns need centralized handling

### Trade-offs

| Pros | Cons |
|------|------|
| Simplifies client interactions | Increased complexity |
| Centralizes business logic | Single point of failure |
| Enables service composition | Performance overhead |
| Supports cross-cutting concerns | Maintenance complexity |

### Production Use Cases

- **E-commerce**: Unified order processing across inventory, payment, and shipping
- **Banking**: Account operations across multiple banking services
- **Healthcare**: Patient management across multiple medical systems

---

## 4. Gateway

### Problem

Application code needs to interact with the messaging system without being tightly coupled to messaging APIs. The application should send and receive messages using a simple interface.

### Solution

Implement a Gateway that provides a clean interface to the messaging system, hiding the complexity of message creation, sending, and receiving.

### Implementation

```java
@Component
public interface OrderGateway {

    @Gateway(requestChannel = "orderInputChannel",
            replyChannel = "orderOutputChannel")
    OrderConfirmation sendOrder(@Payload Order order);

    @Gateway(requestChannel = "orderStatusChannel")
    OrderStatus checkStatus(@Payload String orderId);

    @Gateway(requestChannel = "orderUpdateChannel")
    void updateOrder(@Payload OrderUpdate update);
}

@Component
@Slf4j
public class OrderService {

    @Autowired
    private OrderGateway orderGateway;

    public void processNewOrder(Order order) {
        log.info("Sending order via gateway");

        // Use gateway to send order
        OrderConfirmation confirmation = orderGateway.sendOrder(order);

        log.info("Order confirmed: {}", confirmation.getOrderId());
    }

    public OrderStatus getOrderStatus(String orderId) {
        return orderGateway.checkStatus(orderId);
    }
}
```

**Async Gateway:**

```java
public interface AsyncOrderGateway {

    @Gateway(requestChannel = "asyncOrderInputChannel",
            replyChannel = "asyncOrderOutputChannel",
            replyTimeout = 30000)
    CompletableFuture<OrderConfirmation> sendOrderAsync(@Payload Order order);

    @Gateway(requestChannel = "orderNotificationChannel")
    ListenableFuture<Void> sendNotification(@Payload Notification notification);
}

@Component
@Slf4j
public class AsyncOrderService {

    @Autowired
    private AsyncOrderGateway asyncOrderGateway;

    public CompletableFuture<OrderConfirmation> processOrderAsync(Order order) {
        return asyncOrderGateway.sendOrderAsync(order)
            .thenApply(confirmation -> {
                log.info("Async order processed: {}",
                    confirmation.getOrderId());
                return confirmation;
            })
            .exceptionally(ex -> {
                log.error("Async order processing failed", ex);
                throw new RuntimeException(ex);
            });
    }
}
```

**Generic Gateway:**

```java
public interface GenericMessagingGateway {

    @Gateway(requestChannel = "genericInputChannel")
    <T> T sendAndReceive(@Payload Object payload,
                         @Header("messageType") String messageType);

    @Gateway(requestChannel = "fireAndForgetChannel")
    void send(@Payload Object payload);
}

@Component
public class GenericMessagingService {

    @Autowired
    private GenericMessagingGateway gateway;

    public <T> T querySystem(Object request, Class<T> responseType) {
        return gateway.sendAndReceive(request, "QUERY");
    }

    public void notifySystem(Object event) {
        gateway.send(event);
    }
}
```

### When to Use

- Application code should not depend on messaging APIs
- You need to abstract messaging complexity
- Request-reply patterns need simple interfaces
- Multiple messaging protocols need unified access

### Trade-offs

| Pros | Cons |
|------|------|
| Clean application interface | Added abstraction layer |
| Decouples from messaging APIs | Potential performance overhead |
| Supports multiple messaging protocols | Error handling complexity |
| Enables easy testing | Maintenance overhead |

### Production Use Cases

- **Microservices**: Clean interface for inter-service communication
- **Domain Services**: Abstract messaging from domain logic
- **Integration Layers**: Unified interface for heterogeneous systems

---

## 5. Splitter (Endpoint)

### Problem

An incoming message contains a batch of items that need to be processed individually. The endpoint must decompose the batch into individual messages for processing.

### Solution

Implement a Splitter Endpoint that receives batch messages and produces individual messages for each item in the batch.

### Implementation

```java
@Component
@Slf4j
public class BatchOrderSplitterEndpoint {

    @ServiceActivator(inputChannel = "batchOrderInputChannel",
                     outputChannel = "individualOrderChannel")
    public List<OrderItem> splitBatchOrder(BatchOrder batchOrder) {
        log.info("Splitting batch order: {} with {} items",
            batchOrder.getBatchId(),
            batchOrder.getItems().size());

        return batchOrder.getItems().stream()
            .map(item -> {
                item.setBatchId(batchOrder.getBatchId());
                item.setSourceBatchTimestamp(batchOrder.getReceivedAt());
                return item;
            })
            .collect(Collectors.toList());
    }
}
```

**XPath Splitter Endpoint:**

```java
@Component
public class XmlSplitterEndpoint {

    @ServiceActivator(inputChannel = "xmlInputChannel",
                     outputChannel = "individualRecordChannel")
    public List<String> splitXmlDocument(String xmlContent) {
        try {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(
                new InputSource(new StringReader(xmlContent)));

            NodeList nodeList = document.getElementsByTagName("record");
            List<String> records = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                records.add(convertNodeToString(element));
            }

            return records;
        } catch (Exception e) {
            throw new RuntimeException("XML splitting failed", e);
        }
    }
}
```

**File Splitter Endpoint:**

```java
@Component
public class FileSplitterEndpoint {

    @ServiceActivator(inputChannel = "fileInputChannel",
                     outputChannel = "lineOutputChannel")
    public List<String> splitFile(File file) {
        try {
            return Files.readAllLines(file.toPath())
                .stream()
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("File splitting failed", e);
        }
    }
}
```

### When to Use

- Batch messages need individual processing
- Large messages need chunking
- Parallel processing of message parts is required
- Fan-out patterns need endpoint-level splitting

### Trade-offs

| Pros | Cons |
|------|------|
| Enables parallel processing | Increased message overhead |
| Simplifies consumer logic | Ordering issues |
| Supports batch processing | Memory consumption |
| Reduces coupling | Complex error handling |

### Production Use Cases

- **Batch Processing**: Split batch files for individual record processing
- **Order Fulfillment**: Split multi-item orders for item-level processing
- **Data Migration**: Split large datasets for parallel migration

---

## 6. Aggregator (Endpoint)

### Problem

Results from parallel processing need to be combined. Multiple related messages must be collected and merged into a single response.

### Solution

Implement an Aggregator Endpoint that collects related messages and combines them when all expected messages have arrived.

### Implementation

```java
@Component
@Slf4j
public class OrderAggregatorEndpoint {

    @ServiceActivator(inputChannel = "splitOrderChannel",
                     outputChannel = "aggregatedOrderChannel")
    public AggregatedOrder aggregateOrderResults(
            List<OrderItemResult> itemResults) {
        log.info("Aggregating {} order item results", itemResults.size());

        AggregatedOrder result = new AggregatedOrder();

        // Combine results
        result.setItems(itemResults.stream()
            .map(OrderItemResult::getOrderItem)
            .collect(Collectors.toList()));

        result.setTotalAmount(itemResults.stream()
            .map(OrderItemResult::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        result.setStatus(determineOverallStatus(itemResults));
        result.setProcessedAt(Instant.now());

        return result;
    }

    private String determineOverallStatus(List<OrderItemResult> results) {
        boolean allProcessed = results.stream()
            .allMatch(r -> "PROCESSED".equals(r.getStatus()));
        boolean anyFailed = results.stream()
            .anyMatch(r -> "FAILED".equals(r.getStatus()));

        if (allProcessed) return "COMPLETED";
        if (anyFailed) return "PARTIAL_FAILURE";
        return "PROCESSING";
    }
}
```

**Correlation-Based Aggregator:**

```java
@Component
@Slf4j
public class CorrelationAggregatorEndpoint {

    private final Map<String, List<Message<?>>> correlationStore =
        new ConcurrentHashMap<>();

    @ServiceActivator(inputChannel = "correlatedInputChannel")
    public void aggregateByCorrelation(
            Message<?> message,
            @Header("correlationId") String correlationId,
            @Header("sequenceNumber") int sequenceNumber,
            @Header("sequenceSize") int sequenceSize) {

        log.info("Aggregating message {} for correlation {}",
            sequenceNumber, correlationId);

        correlationStore.computeIfAbsent(correlationId,
            k -> new CopyOnWriteArrayList<>());
        correlationStore.get(correlationId).add(message);

        // Check if all messages received
        if (correlationStore.get(correlationId).size() == sequenceSize) {
            List<Message<?>> allMessages = correlationStore
                .remove(correlationId);

            // Process aggregated messages
            processAggregatedMessages(allMessages);
        }
    }
}
```

### When to Use

- Parallel processing results need combination
- Request aggregation from multiple services
- Batch result consolidation
- Correlation-based message grouping

### Trade-offs

| Pros | Cons |
|------|------|
| Enables parallel processing | State management complexity |
| Supports correlation-based grouping | Memory consumption |
| Handles out-of-order messages | Timeout handling |
| Flexible aggregation logic | Error handling complexity |

### Production Use Cases

- **API Gateway**: Aggregate responses from multiple microservices
- **Batch Processing**: Combine results from parallel data processing
- **Distributed Transactions**: Aggregate results from multiple service calls

---

## 7. Dispatcher

### Problem

Messages need to be distributed to multiple consumers based on various criteria. The distribution logic must be flexible and support different dispatching strategies.

### Solution

Implement a Dispatcher that distributes messages to consumers using different strategies (round-robin, broadcast, priority-based, etc.).

### Implementation

```java
@Component
@Slf4j
public class RoundRobinDispatcher {

    private final AtomicInteger counter = new AtomicInteger(0);

    @ServiceActivator(inputChannel = "dispatcherInputChannel")
    public void dispatchMessage(String payload,
                               @Header("targetService") String targetService) {
        // Round-robin dispatching to service instances
        int instance = counter.getAndIncrement() % 3;
        String channel = "serviceInstance" + instance + "Channel";

        log.info("Dispatching message to instance {}: {}", instance, payload);

        // Dispatch to selected instance
        dispatchToChannel(channel, payload);
    }
}
```

**Priority-Based Dispatcher:**

```java
@Component
@Slf4j
public class PriorityDispatcher {

    @ServiceActivator(inputChannel = "priorityInputChannel")
    public void dispatchByPriority(Message<?> message) {
        String priority = message.getHeaders()
            .get("priority", String.class);

        String targetChannel;
        switch (priority) {
            case "HIGH":
                targetChannel = "highPriorityChannel";
                break;
            case "MEDIUM":
                targetChannel = "mediumPriorityChannel";
                break;
            case "LOW":
                targetChannel = "lowPriorityChannel";
                break;
            default:
                targetChannel = "defaultChannel";
        }

        log.info("Dispatching {} priority message to {}",
            priority, targetChannel);

        dispatchToChannel(targetChannel, message.getPayload());
    }
}
```

**Load-Balancing Dispatcher:**

```java
@Component
@Slf4j
public class LoadBalancingDispatcher {

    private final Map<String, AtomicInteger> serviceLoad =
        new ConcurrentHashMap<>();

    @ServiceActivator(inputChannel = "loadBalancedInputChannel")
    public void dispatchWithLoadBalancing(Message<?> message) {
        String serviceType = message.getHeaders()
            .get("serviceType", String.class);

        // Find least loaded instance
        String leastLoadedInstance = findLeastLoadedInstance(serviceType);

        log.info("Dispatching to least loaded instance: {}",
            leastLoadedInstance);

        // Update load counter
        serviceLoad.computeIfAbsent(leastLoadedInstance,
            k -> new AtomicInteger(0));
        serviceLoad.get(leastLoadedInstance).incrementAndGet();

        // Dispatch message
        dispatchToInstance(leastLoadedInstance, message.getPayload());
    }

    private String findLeastLoadedInstance(String serviceType) {
        return serviceLoad.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(serviceType))
            .min(Comparator.comparingInt(
                entry -> entry.getValue().get()))
            .map(Map.Entry::getKey)
            .orElse(serviceType + "-1");
    }
}
```

### When to Use

- Load balancing across multiple service instances
- Priority-based message routing
- Failover and high availability requirements
- Dynamic service instance selection

### Trade-offs

| Pros | Cons |
|------|------|
| Flexible distribution strategies | Increased complexity |
| Supports load balancing | State management overhead |
| Enables failover | Debugging complexity |
| Dynamic instance selection | Performance monitoring required |

### Production Use Cases

- **Microservices**: Load balance requests across service instances
- **Message Processing**: Distribute messages across worker nodes
- **API Gateway**: Route requests based on load and health

---

## 8. Adapter

### Problem

The messaging system needs to connect to external systems that do not natively support the messaging protocol. Integration with legacy systems, file systems, or external APIs is required.

### Solution

Implement an Adapter that bridges the messaging system with external systems, translating between messaging protocols and external system interfaces.

### Implementation

```java
@Component
@Slf4j
public class FileAdapterExample {

    @Bean
    public IntegrationFlow fileAdapterFlow() {
        return IntegrationFlow.from(
                Files.inboundAdapter(new File("/input"))
                    .preventDuplicates(true)
                    .autoCreateDirectory(true))
            .transform(File.class, this::processFile)
            .handle(Jms.outboundAdapter(jmsConnectionFactory())
                .destination("processed.files.queue"))
            .get();
    }

    private String processFile(File file) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            log.info("Processing file: {}", file.getName());

            // Process file content
            String processed = transformContent(content);

            // Archive original file
            Files.move(file.toPath(),
                new File("/archive/" + file.getName()).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

            return processed;
        } catch (IOException e) {
            throw new RuntimeException("File processing failed", e);
        }
    }
}
```

**HTTP Adapter:**

```java
@Component
@Slf4j
public class HttpAdapterExample {

    @Bean
    public IntegrationFlow httpInboundAdapterFlow() {
        return IntegrationFlow.from(
                Http.inboundGateway("/api/orders")
                    .requestMapping(m -> m
                        .methods(HttpMethod.POST)
                        .consumes("application/json"))
                    .requestPayloadType(Order.class)
                    .replyTimeout(5000))
            .handle(Order.class, (order, headers) -> {
                log.info("Received order via HTTP: {}", order.getOrderId());

                // Process order
                OrderConfirmation confirmation = processOrder(order);

                return confirmation;
            })
            .get();
    }

    @Bean
    public IntegrationFlow httpOutboundAdapterFlow() {
        return IntegrationFlow.from("outboundRequestChannel")
            .handle(Http.outboundChannelAdapter("http://external-api.com/orders")
                .httpMethod(HttpMethod.POST)
                .expectedResponseType(ApiResponse.class)
                .mappedRequestHeaders("Authorization", "X-Request-Id"))
            .get();
    }
}
```

**Database Adapter:**

```java
@Component
@Slf4j
public class DatabaseAdapterExample {

    @Bean
    public IntegrationFlow jdbcInboundFlow() {
        return IntegrationFlow.from(
                Jdbc.inboundChannelAdapter(dataSource,
                    "SELECT * FROM orders WHERE status = 'NEW'")
                .updateSql("UPDATE orders SET status = 'PROCESSING' WHERE id IN (:id)")
                .rowMapper(new OrderRowMapper())
                .maxRows(100))
            .channel("databaseOrdersChannel")
            .get();
    }

    @Bean
    public IntegrationFlow jdbcOutboundFlow() {
        return IntegrationFlow.from("processedOrdersChannel")
            .handle(Jdbc.outboundAdapter(dataSource)
                .sql("UPDATE orders SET status = :payload.status, " +
                     "processed_at = :payload.processedAt " +
                     "WHERE id = :payload.orderId"))
            .get();
    }
}
```

**FTP Adapter:**

```java
@Component
public class FtpAdapterExample {

    @Bean
    public IntegrationFlow ftpInboundFlow() {
        return IntegrationFlow.from(
                Ftp.inboundAdapter(ftpTemplate())
                    .remoteDirectory("/data/incoming")
                    .patternFilter("*.csv")
                    .preserveTimestamp(true))
            .transform(File.class, this::processFtpFile)
            .handle(Jms.outboundAdapter(jmsConnectionFactory())
                .destination("ftp.processed.queue"))
            .get();
    }

    @Bean
    public IntegrationFlow ftpOutboundFlow() {
        return IntegrationFlow.from("ftpOutputChannel")
            .handle(Ftp.outboundAdapter(ftpTemplate())
                .remoteDirectory("/data/outgoing")
                .autoCreateDirectory(true))
            .get();
    }
}
```

### When to Use

- Integrating with legacy systems
- Connecting to external APIs
- File system integration
- Database integration

### Trade-offs

| Pros | Cons |
|------|------|
| Enables external system integration | Protocol translation overhead |
| Supports legacy system connectivity | Error handling complexity |
| Flexible integration options | Maintenance overhead |
| Enables heterogeneous system communication | Performance impact |

### Production Use Cases

- **Legacy Integration**: Connect modern messaging with legacy systems
- **File Processing**: Process files from FTP or file systems
- **API Integration**: Connect messaging with external REST APIs
- **Database Integration**: Sync data between databases and messaging

---

## Pattern Comparison Matrix

| Pattern | Primary Use | Complexity | Performance | Use When |
|---------|-------------|------------|-------------|----------|
| Service Activator | Business logic invocation | Low | High | Message-triggered processing |
| Messaging Bridge | System interconnection | Medium | Medium | Different messaging systems |
| Service Facade | Unified service interface | High | Medium | Multiple services exposure |
| Gateway | Clean messaging interface | Low | Medium | Application-messaging decoupling |
| Splitter | Batch decomposition | Medium | Medium | Batch message processing |
| Aggregator | Result combination | High | Medium | Parallel processing results |
| Dispatcher | Message distribution | Medium | Medium | Load balancing/failover |
| Adapter | External system integration | Medium | Medium | Legacy/external system connection |

---

## References

- Enterprise Integration Patterns - Gregor Hohpe, Bobby Woolf
- Spring Integration Reference Guide
- Apache Camel Documentation
- Enterprise Integration Patterns Patterns Catalog
