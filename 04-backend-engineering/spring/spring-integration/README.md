# Spring Integration

## Comprehensive Guide to Spring Integration

Spring Integration provides an enterprise integration solution that implements the Enterprise Integration Patterns (EIP). This guide covers channels, adapters, filters, transformers, and routers.

---

## Table of Contents

1. [Channels](#channels)
2. [Adapters](#adapters)
3. [Filters](#filters)
4. [Transformers](#transformers)
5. [Routers](#routers)
6. [Endpoints](#endpoints)
7. [Best Practices](#best-practices)

---

## Channels

### Message Channels

```java
@Configuration
@EnableIntegration
public class IntegrationConfig {
    
    // Direct channel (synchronous)
    @Bean
    public MessageChannel directChannel() {
        return new DirectChannel();
    }
    
    // Queue channel (asynchronous)
    @Bean
    public MessageChannel queueChannel() {
        return new QueueChannel(100); // Capacity of 100
    }
    
    // Publish-subscribe channel
    @Bean
    public MessageChannel publishSubscribeChannel() {
        return new PublishSubscribeChannel();
    }
    
    // Priority channel
    @Bean
    public MessageChannel priorityChannel() {
        return new PriorityChannel(100, Comparator.comparingInt(
            msg -> (int) msg.getHeaders().getOrDefault("priority", 0)));
    }
    
    // Rendezvous channel (zero capacity)
    @Bean
    public MessageChannel rendezvousChannel() {
        return new RendezvousChannel();
    }
    
    // Executor channel
    @Bean
    public MessageChannel executorChannel() {
        return new ExecutorChannel(Executors.newFixedThreadPool(10));
    }
}
```

### Channel Configuration

```java
@Configuration
@EnableIntegration
public class ChannelConfig {
    
    @Bean
    public MessageChannel orderChannel() {
        return MessageChannels.direct("orderChannel").get();
    }
    
    @Bean
    public MessageChannel processedOrderChannel() {
        return MessageChannels.queue("processedOrderChannel", 50).get();
    }
    
    @Bean
    public MessageChannel errorChannel() {
        return MessageChannels.direct("errorChannel").get();
    }
    
    // Channel with interceptors
    @Bean
    public MessageChannel monitoredChannel() {
        return MessageChannels.direct("monitoredChannel")
            .interceptor(new ChannelInterceptor() {
                @Override
                public Message<?> preSend(Message<?> message, MessageChannel channel) {
                    System.out.println("Sending message to: " + channel);
                    return message;
                }
                
                @Override
                public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
                    System.out.println("Message sent: " + sent);
                }
            })
            .get();
    }
}
```

### Wire Tap

```java
@Bean
public IntegrationFlow loggingFlow() {
    return IntegrationFlow.from("inputChannel")
        .wireTap("auditChannel")
        .channel("outputChannel")
        .get();
}

@Bean
public IntegrationFlow auditFlow() {
    return IntegrationFlow.from("auditChannel")
        .handle(message -> {
            System.out.println("Audit: " + message.getPayload());
        })
        .get();
}
```

---

## Adapters

### File Adapter

```java
@Configuration
@EnableIntegration
public class FileAdapterConfig {
    
    @Bean
    public IntegrationFlow fileReadFlow() {
        return IntegrationFlow.from(
                Files.inboundAdapter(new File("/input"))
                    .patternFilter("*.csv")
                    .preventDuplicates(true),
                e -> e.poller(Pollers.fixedDelay(5000)))
            .transform(Files.toStringTransformer())
            .channel("fileContentChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow fileWriteFlow() {
        return IntegrationFlow.from("fileOutputChannel")
            .handle(Files.outboundAdapter(new File("/output"))
                .fileNameGenerator(message -> 
                    "output-" + System.currentTimeMillis() + ".csv")
                .autoCreateDirectory(true))
            .get();
    }
}
```

### HTTP Adapter

```java
@Configuration
@EnableIntegration
public class HttpAdapterConfig {
    
    @Bean
    public IntegrationFlow httpInboundFlow() {
        return IntegrationFlow.from(
                Http.inboundGateway("/api/orders")
                    .requestMapping(mapping -> mapping
                        .methods(HttpMethod.POST)
                        .consumes("application/json"))
                    .requestPayloadType(Order.class)
                    .replyTimeout(5000))
            .channel("orderProcessingChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow httpOutboundFlow() {
        return IntegrationFlow.from("httpOutboundChannel")
            .handle(Http.outboundGateway("http://external-api/orders")
                .mappedRequestHeaders("Authorization", "X-Request-Id")
                .expectedResponseType(OrderResponse.class))
            .get();
    }
}
```

### JMS Adapter

```java
@Configuration
@EnableIntegration
public class JmsAdapterConfig {
    
    @Bean
    public IntegrationFlow jmsInboundFlow(ConnectionFactory connectionFactory) {
        return IntegrationFlow.from(
                Jms.messageDrivenChannelAdapter(connectionFactory)
                    .destination("orderQueue")
                    .errorChannel("jmsErrorChannel"))
            .channel("orderProcessingChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow jmsOutboundFlow(ConnectionFactory connectionFactory) {
        return IntegrationFlow.from("jmsOutputChannel")
            .handle(Jms.outboundAdapter(connectionFactory)
                .destination("orderResponseQueue"))
            .get();
    }
}
```

### Database Adapter

```java
@Configuration
@EnableIntegration
public class DatabaseAdapterConfig {
    
    @Bean
    public IntegrationFlow jdbcInboundFlow(DataSource dataSource) {
        return IntegrationFlow.from(
                Jdbc.inboundChannelAdapter(dataSource,
                        "SELECT * FROM orders WHERE status = 'PENDING'")
                    .updateSql("UPDATE orders SET status = 'PROCESSING' WHERE id IN (:id)")
                    .rowMapper(new BeanPropertyRowMapper<>(Order.class))
                    .maxRows(100)
                    .poller(Pollers.fixedDelay(5000)))
            .channel("orderProcessingChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow jdbcOutboundFlow(DataSource dataSource) {
        return IntegrationFlow.from("jdbcOutputChannel")
            .handle(Jdbc.outboundAdapter(dataSource)
                .sql("UPDATE orders SET status = :payload.status WHERE id = :payload.id"))
            .get();
    }
}
```

### Email Adapter

```java
@Configuration
@EnableIntegration
public class EmailAdapterConfig {
    
    @Bean
    public IntegrationFlow emailInboundFlow(JavaMailReceiver receiver) {
        return IntegrationFlow.from(
                Mail.imapInboundAdapter(receiver)
                    .shouldDeleteMessages(false)
                    .shouldMarkMessagesAsRead(true))
            .transform(Email.transformer())
            .channel("emailProcessingChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow emailOutboundFlow() {
        return IntegrationFlow.from("emailOutputChannel")
            .handle(Mail.outboundAdapter("smtp.example.com")
                .port(587)
                .protocol("smtp")
                .credentials("user", "password"))
            .get();
    }
}
```

---

## Filters

### Message Filter

```java
@Configuration
@EnableIntegration
public class FilterConfig {
    
    @Bean
    public IntegrationFlow filterFlow() {
        return IntegrationFlow.from("inputChannel")
            .filter(Message.class, message -> 
                message.getHeaders().get("type").equals("ORDER"))
            .channel("orderChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow filterWithDiscard() {
        return IntegrationFlow.from("inputChannel")
            .filter(Order.class, order -> order.getAmount() > 100,
                spec -> spec.discardChannel("lowValueOrders"))
            .channel("highValueOrders")
            .get();
    }
    
    @Bean
    public IntegrationFlow filterWithAdvice() {
        return IntegrationFlow.from("inputChannel")
            .filter(Message.class, 
                message -> message.getPayload() != null,
                spec -> spec.advice(retryAdvice()))
            .channel("outputChannel")
            .get();
    }
    
    @Bean
    public RequestHandlerRetryAdvice retryAdvice() {
        RequestHandlerRetryAdvice advice = new RequestHandlerRetryAdvice();
        advice.setRetryTemplate(new RetryTemplate());
        return advice;
    }
}
```

### Content-Based Filter

```java
@Bean
public IntegrationFlow contentBasedFilter() {
    return IntegrationFlow.from("inputChannel")
        .<Message<?>, Boolean>filter(
            message -> {
                Object payload = message.getPayload();
                if (payload instanceof Order order) {
                    return order.getAmount() > 1000;
                }
                return false;
            })
        .channel("largeOrders")
        .get();
}
```

### Filter with Expression

```java
@Bean
public IntegrationFlow expressionFilter() {
    return IntegrationFlow.from("inputChannel")
        .filter("payload.amount > 1000")
        .channel("largeOrders")
        .get();
}
```

---

## Transformers

### Object Transformer

```java
@Configuration
@EnableIntegration
public class TransformerConfig {
    
    @Bean
    public IntegrationFlow transformerFlow() {
        return IntegrationFlow.from("inputChannel")
            .transform(Order.class, order -> {
                OrderDTO dto = new OrderDTO();
                dto.setId(order.getId());
                dto.setCustomerName(order.getCustomer().getName());
                dto.setTotal(order.getItems().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum());
                return dto;
            })
            .channel("outputChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow headerEnricherFlow() {
        return IntegrationFlow.from("inputChannel")
            .enrichHeaders(h -> h
                .header("processedAt", Instant.now())
                .header("processor", "orderService")
                .header("version", 2))
            .channel("outputChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow payloadConverterFlow() {
        return IntegrationFlow.from("inputChannel")
            .transform(String.class, String::toUpperCase)
            .transform(String.class, s -> s.trim())
            .channel("outputChannel")
            .get();
    }
}
```

### File Content Transformer

```java
@Bean
public IntegrationFlow fileTransformerFlow() {
    return IntegrationFlow.from("fileContentChannel")
        .transform(Files.toStringTransformer())
        .transform(String.class, content -> {
            String[] lines = content.split("\n");
            return Arrays.stream(lines)
                .map(this::parseLine)
                .collect(Collectors.toList());
        })
        .channel("parsedContentChannel")
        .get();
}
```

### Custom Transformer

```java
@Component
public class OrderTransformer implements GenericHandler<Order> {
    
    @Override
    public Object handle(Order order, Map<String, Object> headers) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setTotal(calculateTotal(order));
        dto.setStatus("PROCESSED");
        
        headers.put("processedAt", Instant.now());
        
        return dto;
    }
    
    private double calculateTotal(Order order) {
        return order.getItems().stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
    }
}

// Usage in configuration
@Bean
public IntegrationFlow customTransformerFlow() {
    return IntegrationFlow.from("inputChannel")
        .handle(new OrderTransformer())
        .channel("outputChannel")
        .get();
}
```

---

## Routers

### Message Router

```java
@Configuration
@EnableIntegration
public class RouterConfig {
    
    @Bean
    public IntegrationFlow routerFlow() {
        return IntegrationFlow.from("inputChannel")
            .<Order, Class<?>>route(
                order -> {
                    if (order.getAmount() > 10000) {
                        return LargeOrderHandler.class;
                    } else if (order.getAmount() > 1000) {
                        return MediumOrderHandler.class;
                    } else {
                        return SmallOrderHandler.class;
                    }
                })
            .get();
    }
    
    @Bean
    public IntegrationFlow expressionRouterFlow() {
        return IntegrationFlow.from("inputChannel")
            .route("headers['orderType']")
            .get();
    }
    
    @Bean
    public IntegrationFlow recipientListRouterFlow() {
        return IntegrationFlow.from("inputChannel")
            .routeToRecipients(r -> r
                .recipient("orderChannel", "payload.amount > 1000")
                .recipient("notificationChannel", "payload.notify == true")
                .recipient("auditChannel"))
            .get();
    }
}
```

### Content-Based Router

```java
@Bean
public IntegrationFlow contentBasedRouter() {
    return IntegrationFlow.from("inputChannel")
        .<Object, String>route(
            payload -> {
                if (payload instanceof Order order) {
                    return "orderChannel";
                } else if (payload instanceof Payment payment) {
                    return "paymentChannel";
                } else {
                    return "defaultChannel";
                }
            })
        .get();
}
```

### Scatter-Gather Router

```java
@Bean
public IntegrationFlow scatterGatherFlow() {
    return IntegrationFlow.from("inputChannel")
        .scatterGather(
            scatterer -> scatterer
                .recipientFlow("channel1")
                .recipientFlow("channel2")
                .recipientFlow("channel3"),
            gatherer -> gatherer
                .outputProcessor(messageGroup -> {
                    List<Object> results = messageGroup.getMessages().stream()
                        .map(Message::getPayload)
                        .collect(Collectors.toList());
                    return MessageBuilder.withPayload(results).build();
                }))
        .channel("outputChannel")
        .get();
}
```

### Router with Expression

```java
@Bean
public IntegrationFlow expressionRouter() {
    return IntegrationFlow.from("inputChannel")
        .route("payload.status", mapping -> mapping
            .subFlowMapping("PENDING", sf -> sf
                .handle(orderService::processPending))
            .subFlowMapping("COMPLETED", sf -> sf
                .handle(orderService::processCompleted))
            .defaultSubFlowMapping(sf -> sf
                .handle(orderService::processDefault)))
        .get();
}
```

---

## Endpoints

### Service Activator

```java
@Configuration
@EnableIntegration
public class EndpointConfig {
    
    @Bean
    public IntegrationFlow serviceActivatorFlow() {
        return IntegrationFlow.from("inputChannel")
            .handle(Order.class, (order, headers) -> {
                // Process order
                order.setStatus("PROCESSED");
                return order;
            })
            .channel("outputChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow methodInvokingFlow() {
        return IntegrationFlow.from("inputChannel")
            .handle(orderService, "processOrder")
            .channel("outputChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow gatewayFlow() {
        return IntegrationFlow.from(OrderGateway.class)
            .handle(orderService, "processOrder")
            .get();
    }
}
```

### Gateway Interface

```java
@MessagingGateway(defaultRequestChannel = "inputChannel")
public interface OrderGateway {
    
    Order processOrder(Order order);
    
    @Gateway(requestChannel = "asyncInputChannel", replyTimeout = 5000)
    CompletableFuture<Order> processOrderAsync(Order order);
    
    @Gateway(requestChannel = "inputChannel")
    Message<Order> processOrderWithHeaders(Message<Order> message);
}

// Usage
@Component
public class OrderProcessor {
    
    @Autowired
    private OrderGateway orderGateway;
    
    public Order processOrder(Order order) {
        return orderGateway.processOrder(order);
    }
    
    public CompletableFuture<Order> processOrderAsync(Order order) {
        return orderGateway.processOrderAsync(order);
    }
}
```

### Poller Configuration

```java
@Configuration
@EnableIntegration
public class PollerConfig {
    
    @Bean
    public IntegrationFlow pollingFlow() {
        return IntegrationFlow.from("inputChannel", 
                e -> e.poller(Pollers.fixedDelay(1000)
                    .maxMessagesPerPoll(10)
                    .errorHandler(t -> log.error("Polling error", t))))
            .channel("outputChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow cronPollingFlow() {
        return IntegrationFlow.from("inputChannel",
                e -> e.poller(Pollers.cron("0 0/5 * * * *")))
            .channel("outputChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow fixedRatePollingFlow() {
        return IntegrationFlow.from("inputChannel",
                e -> e.poller(Pollers.fixedRate(5000)))
            .channel("outputChannel")
            .get();
    }
}
```

---

## Best Practices

### Error Handling

```java
@Configuration
@EnableIntegration
public class ErrorHandlingConfig {
    
    @Bean
    public IntegrationFlow errorHandlingFlow() {
        return IntegrationFlow.from("inputChannel")
            .handle(orderService, "processOrder")
            .channel("outputChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow errorChannelFlow() {
        return IntegrationFlow.from("errorChannel")
            .handle(message -> {
                Message<?> errorMessage = (Message<?>) message.getPayload();
                Exception exception = (Exception) errorMessage.getPayload();
                log.error("Error processing message: {}", exception.getMessage());
                
                // Send to dead letter queue
                return errorMessage;
            })
            .get();
    }
    
    @Bean
    public ErrorChannelRecoverer recoverer() {
        return new ErrorChannelRecoverer(errorChannel());
    }
}

// Custom error handler
@Component
public class CustomErrorHandler implements ErrorMessageHandler {
    
    @Override
    public void handle(ErrorMessage errorMessage) {
        log.error("Handling error: {}", errorMessage.getPayload().getMessage());
        
        // Log to monitoring system
        monitoringService.logError(errorMessage);
        
        // Send alert if critical
        if (isCriticalError(errorMessage)) {
            alertService.sendAlert(errorMessage);
        }
    }
    
    private boolean isCriticalError(ErrorMessage errorMessage) {
        return errorMessage.getPayload() instanceof DatabaseAccessException;
    }
}
```

### Transaction Management

```java
@Bean
public IntegrationFlow transactionalFlow() {
    return IntegrationFlow.from("inputChannel")
        .handle(orderService, "processOrder")
        .get();
}

// Configure transaction interceptor
@Bean
public TransactionInterceptor transactionInterceptor() {
    return new TransactionInterceptor(transactionManager,
        Collections.singletonMap("processOrder*", 
            new DefaultTransactionDefinition()));
}
```

### Performance Tuning

```java
@Configuration
@EnableIntegration
public class PerformanceConfig {
    
    @Bean
    public ThreadPoolTaskExecutor integrationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("integration-");
        executor.initialize();
        return executor;
    }
    
    @Bean
    public IntegrationFlow asyncFlow() {
        return IntegrationFlow.from("inputChannel")
            .channel(c -> c.executor(integrationExecutor()))
            .handle(orderService, "processOrder")
            .channel("outputChannel")
            .get();
    }
    
    @Bean
    public IntegrationFlow batchProcessingFlow() {
        return IntegrationFlow.from("inputChannel",
                e -> e.poller(Pollers.fixedDelay(1000)
                    .maxMessagesPerPoll(100)))
            .batch(10)
            .handle(orderService, "processOrders")
            .channel("outputChannel")
            .get();
    }
}
```

### Monitoring

```java
@Component
public class IntegrationMonitor {
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    private Counter messageCounter;
    private Timer processingTimer;
    
    @PostConstruct
    public void init() {
        messageCounter = Counter.builder("integration.messages")
            .description("Total messages processed")
            .register(meterRegistry);
        
        processingTimer = Timer.builder("integration.processing.time")
            .description("Processing time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry);
    }
    
    @ServiceActivator(inputChannel = "inputChannel")
    public void monitorInput(Message<?> message) {
        messageCounter.increment();
    }
    
    @ServiceActivator(inputChannel = "outputChannel")
    public void monitorOutput(Message<?> message) {
        processingTimer.record(() -> {
            // Processing time is measured
        });
    }
}
```

---

## Common Pitfalls

### 1. Not Using Error Channels

```java
// Bad - No error handling
.handle(orderService, "processOrder")

// Good - With error handling
.handle(orderService, "processOrder")
.channel("outputChannel")
.enrichErrorChannel(e -> e
    .header("errorChannel", "customErrorChannel"))
```

### 2. Synchronous Processing in High-Throughput Systems

```java
// Bad - Synchronous processing
IntegrationFlow.from("inputChannel")
    .handle(orderService, "processOrder")
    .channel("outputChannel");

// Good - Asynchronous processing
IntegrationFlow.from("inputChannel")
    .channel(c -> c.executor(taskExecutor))
    .handle(orderService, "processOrder")
    .channel("outputChannel");
```

### 3. Not Monitoring Integration Flows

```java
// Bad - No monitoring
IntegrationFlow.from("inputChannel")
    .handle(orderService, "processOrder")
    .channel("outputChannel");

// Good - With monitoring
IntegrationFlow.from("inputChannel")
    .wireTap("auditChannel")
    .handle(orderService, "processOrder")
    .channel("outputChannel");
```

---

## Further Reading

- [Spring Integration Official Documentation](https://spring.io/projects/spring-integration)
- [Spring Integration Reference](https://docs.spring.io/spring-integration/reference/)
- [Enterprise Integration Patterns](https://www.eaipatterns.com/)
- [Baeldung Spring Integration](https://www.baeldung.com/spring-integration)
