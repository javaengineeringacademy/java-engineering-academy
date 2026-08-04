# Spring Integration - Routing

## Overview

Message routing determines how messages flow through the integration system. Routers direct messages to appropriate channels based on content, headers, or other criteria.

## Table of Contents

1. [Routing Patterns](#routing-patterns)
2. [Content-Based Router](#content-based-router)
3. [Header Enricher](#header-enricher)
4. [Router](#router)
5. [Splitter](#splitter)
6. [Aggregator](#aggregator)
7. [Resequencer](#resequencer)
8. [Dynamic Routing](#dynamic-routing)

## Routing Patterns

### Content-Based Router

```
                    ┌──> Channel A (if type=A)
Message ─── Router ─┼──> Channel B (if type=B)
                    └──> Channel C (if type=C)
```

### Recipient List

```
                    ┌──> Consumer 1
Message ─── Router ─┼──> Consumer 2
                    └──> Consumer 3
```

### Message Splitter

```
Message (batch) ─── Splitter ──┬──> Message 1
                               ├──> Message 2
                               └──> Message 3
```

### Message Aggregator

```
Message 1 ─┐
Message 2 ─┼── Aggregator ──> Combined Message
Message 3 ─┘
```

## Content-Based Router

### @Router Annotation

```java
@Router(inputChannel = "inputChannel")
public String route(Order order) {
    if (order.getTotal() > 1000) {
        return "highValueChannel";
    } else if (order.getTotal() > 100) {
        return "mediumValueChannel";
    }
    return "lowValueChannel";
}
```

### Router with Headers

```java
@Router(inputChannel = "inputChannel")
public String route(Message<Order> message) {
    Order order = message.getPayload();
    String region = message.getHeaders().get("region", String.class);
    
    if ("US".equals(region)) {
        return "usChannel";
    } else if ("EU".equals(region)) {
        return "euChannel";
    }
    return "globalChannel";
}
```

### XML Configuration

```xml
<int:router input-channel="inputChannel"
            ref="orderRouter"
            method="route">
    <int:poller fixed-rate="1000"/>
</int:router>

<bean id="orderRouter" class="com.example.OrderRouter"/>
```

### Expression Router

```java
@Bean
@Router(inputChannel = "inputChannel")
public MessageRouter expressionRouter() {
    ExpressionEvaluatingRouter router = new ExpressionEvaluatingRouter(
        "payload.total > 1000 ? 'highValueChannel' : 'normalChannel'");
    return router;
}
```

## Header Enricher

### Enrich Headers

```java
@Bean
@Transformer(inputChannel = "inputChannel", outputChannel = "outputChannel")
public Message<?> enrichHeaders(Message<?> message) {
    return MessageBuilder
        .withPayload(message.getPayload())
        .setHeader("processedAt", new Date())
        .setHeader("processor", "orderService")
        .copyHeader("correlationId", message.getHeaders())
        .build();
}
```

### XML Configuration

```xml
<int:header-enricher input-channel="inputChannel" output-channel="outputChannel">
    <int:header name="processedAt" ref="currentDate"/>
    <int:header name="processor" value="orderService"/>
    <int:header name="correlationId" expression="headers['correlationId']"/>
</int:header-enricher>
```

## Router

### Static Router

```java
@Bean
@Router(inputChannel = "inputChannel")
public MessageRouter staticRouter() {
    MessageRouter router = new MessageRouter();
    router.setChannelMapping("ORDER", "orderChannel");
    router.setChannelMapping("INVOICE", "invoiceChannel");
    router.setChannelMapping("PAYMENT", "paymentChannel");
    router.setDefaultOutputChannelName("defaultChannel");
    return router;
}
```

### Dynamic Router

```java
@Router(inputChannel = "inputChannel")
public String dynamicRoute(Message<?> message) {
    String type = message.getHeaders().get("type", String.class);
    return determineChannel(type);
}

private String determineChannel(String type) {
    // Dynamic channel determination logic
    return type.toLowerCase() + "-channel";
}
```

### Recipient List Router

```java
@Bean
@Router(inputChannel = "inputChannel")
public RecipientListRouter recipientListRouter() {
    RecipientListRouter router = new RecipientListRouter();
    router.setRecipientExpression("headers['recipients']");
    router.setApplySequence(true);
    return router;
}
```

## Splitter

### Simple Splitter

```java
@Transformer(inputChannel = "orderChannel", outputChannel = "itemChannel")
public List<OrderItem> splitOrder(Message<Order> message) {
    Order order = message.getPayload();
    return order.getItems();
}
```

### Splitter with Aggregation

```java
@Configuration
@EnableIntegration
public class SplitAggregateConfig {
    
    @Bean
    public MessageChannel orderChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public MessageChannel itemChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public MessageChannel aggregatedChannel() {
        return new DirectChannel();
    }
    
    @Bean
    @Transformer(inputChannel = "orderChannel", outputChannel = "itemChannel")
    public List<OrderItem> splitOrder(Message<Order> message) {
        return message.getPayload().getItems();
    }
    
    @Bean
    @Aggregator(inputChannel = "itemChannel", outputChannel = "aggregatedChannel")
    public Order aggregateItems(MessageGroup group) {
        List<OrderItem> items = group.getMessages().stream()
            .map(msg -> (OrderItem) msg.getPayload())
            .collect(Collectors.toList());
        
        return new Order(items);
    }
    
    @Bean
    @CorrelationStrategy
    public Object correlate(Message<?> message) {
        return message.getHeaders().get("orderId");
    }
    
    @Bean
    @CompletionCondition
    public boolean complete(MessageGroup group) {
        return group.size() >= 3;
    }
}
```

## Aggregator

### Basic Aggregator

```java
@Aggregator(inputChannel = "itemChannel", outputChannel = "aggregatedChannel")
public List<OrderItem> aggregate(MessageGroup group) {
    return group.getMessages().stream()
        .map(msg -> (OrderItem) msg.getPayload())
        .collect(Collectors.toList());
}
```

### Aggregator with Correlation

```java
@Bean
@Aggregator(inputChannel = "inputChannel", outputChannel = "outputChannel")
public Order aggregate(MessageGroup group) {
    List<OrderItem> items = group.getMessages().stream()
        .map(msg -> (OrderItem) msg.getPayload())
        .collect(Collectors.toList());
    
    return new Order(items);
}

@Bean
@CorrelationStrategy
public String correlate(Message<?> message) {
    return message.getHeaders().get("correlationId", String.class);
}

@Bean
@ReleaseStrategy
public boolean release(MessageGroup group) {
    return group.size() >= 3;
}
```

## Resequencer

### Basic Resequencer

```java
@Bean
@Transformer(inputChannel = "inputChannel", outputChannel = "outputChannel")
public Message<?> resequence(Message<?> message) {
    return message;
}

@Bean
@Transformer(inputChannel = "inputChannel", outputChannel = "outputChannel")
public List<Message<?>> resequence(List<Message<?>> messages) {
    return messages.stream()
        .sorted(Comparator.comparingInt(msg -> 
            msg.getHeaders().get("sequenceNumber", Integer.class)))
        .collect(Collectors.toList());
}
```

### XML Configuration

```xml
<int:resequencer input-channel="inputChannel"
                 output-channel="outputChannel"
                 release-partial-sequences="false">
    <int:resequencer id="resequencer" release-partial-sequences="false"/>
</int:resequencer>
```

## Dynamic Routing

### Dynamic Router

```java
@Service
public class DynamicRoutingService {
    public String determineRoute(Message<?> message) {
        String type = message.getHeaders().get("type", String.class);
        
        switch (type) {
            case "ORDER":
                return "orderChannel";
            case "INVOICE":
                return "invoiceChannel";
            case "PAYMENT":
                return "paymentChannel";
            default:
                return "defaultChannel";
        }
    }
}

// Usage
@Bean
@Router(inputChannel = "inputChannel")
public String dynamicRoute(Message<?> message) {
    return routingService.determineRoute(message);
}
```

## Best Practices

1. **Choose appropriate router**: Match router to use case
2. **Keep routing simple**: Avoid complex routing logic
3. **Use default channel**: Always have a default
4. **Log routing decisions**: For debugging
5. **Test routing**: Verify routing logic
6. **Document routes**: Document routing rules
7. **Handle errors**: Configure error handling
8. **Performance**: Consider routing performance

## References

- [Spring Integration Router](https://docs.spring.io/spring-integration/reference/core/router.html)
- [Spring Integration Splitter](https://docs.spring.io/spring-integration/reference/core/splitter.html)
- [Spring Integration Aggregator](https://docs.spring.io/spring-integration/reference/core/aggregator.html)
