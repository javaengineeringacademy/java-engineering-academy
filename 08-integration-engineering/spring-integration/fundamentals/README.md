# Spring Integration - Fundamentals

## Overview

Spring Integration provides an implementation of Enterprise Integration Patterns within the Spring ecosystem. It enables lightweight messaging and supports integration with external systems.

## Table of Contents

1. [What is Spring Integration](#what-is-spring-integration)
2. [Core Concepts](#core-concepts)
3. [Messages](#messages)
4. [Channels](#channels)
5. [Endpoints](#endpoints)
6. [Messaging Styles](#messaging-styles)
7. [Configuration](#configuration)
8. [First Example](#first-example)

## What is Spring Integration

Spring Integration extends the Spring programming model to support the Enterprise Integration Patterns. It provides:

- Message-based architecture
- Channel-based messaging
- Endpoint abstractions
- Integration with Spring ecosystem
- External system adapters

## Core Concepts

### Architecture

```
┌─────────────────────────────────────────────────┐
│              Spring Integration                 │
├─────────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────┐     │
│  │ Message  │  │ Channel  │  │ Endpoint │     │
│  └──────────┘  └──────────┘  └──────────┘     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐     │
│  │ Adapter  │  │ Gateway  │  │Transformer│    │
│  └──────────┘  └──────────┘  └──────────┘     │
└─────────────────────────────────────────────────┘
```

### Message Flow

```
Inbound ──> Channel ──> Processor ──> Channel ──> Outbound
Adapter                  (Service)                Adapter
```

## Messages

### Message Structure

```java
public interface Message<T> {
    MessageHeaders getHeaders();
    T getPayload();
}
```

### Creating Messages

```java
// Simple message
Message<String> message = MessageBuilder
    .withPayload("Hello World")
    .setHeader("type", "greeting")
    .build();

// With payload
Message<Order> orderMessage = MessageBuilder
    .withPayload(new Order("123", "Product", 10.0))
    .setHeader("priority", "HIGH")
    .build();
```

### Message Headers

| Header | Description |
|--------|-------------|
| ID | Unique message identifier |
| TIMESTAMP | Creation time |
| CORRELATION_ID | Links request/response |
| REPLY_TO | Response channel |
| EXPIRATION | Message TTL |
| PRIORITY | Processing priority |

## Channels

### Channel Types

```java
// Direct Channel (synchronous)
@Bean
public MessageChannel directChannel() {
    return new DirectChannel();
}

// Queue Channel (buffered)
@Bean
public MessageChannel queueChannel() {
    return new QueueChannel(100);
}

// Publish-Subscribe Channel
@Bean
public MessageChannel pubSubChannel() {
    return new PublishSubscribeChannel();
}

// Executor Channel (thread pool)
@Bean
public MessageChannel executorChannel() {
    return new ExecutorChannel(executorService());
}
```

## Endpoints

### Service Activator

```java
@ServiceActivator(inputChannel = "input", outputChannel = "output")
public Message<?> process(Message<?> message) {
    // Process message
    return message;
}
```

### Transformer

```java
@Transformer(inputChannel = "raw", outputChannel = "transformed")
public Order transform(RawOrder raw) {
    return new Order(raw.getId(), raw.getItems());
}
```

### Filter

```java
@Filter(inputChannel = "input", outputChannel = "output")
public boolean filter(Message<?> message) {
    Integer priority = message.getHeaders().get("priority", Integer.class);
    return priority != null && priority >= 5;
}
```

### Router

```java
@Router(inputChannel = "input")
public String route(Order order) {
    if (order.getTotal() > 1000) return "highValueChannel";
    return "normalChannel";
}
```

### Gateway

```java
@MessagingGateway(defaultRequestChannel = "requestChannel")
public interface OrderGateway {
    OrderConfirmation processOrder(@Payload Order order);
}
```

## Messaging Styles

### Document Messaging

```java
Message<OrderDocument> doc = MessageBuilder
    .withPayload(new OrderDocument(orderData))
    .setHeader("documentType", "PURCHASE_ORDER")
    .build();
```

### Command Messaging

```java
Message<ProcessOrderCommand> cmd = MessageBuilder
    .withPayload(new ProcessOrderCommand(orderId))
    .setHeader("commandType", "PROCESS_ORDER")
    .build();
```

### Event Messaging

```java
Message<OrderCreatedEvent> event = MessageBuilder
    .withPayload(new OrderCreatedEvent(orderId))
    .setHeader("eventType", "ORDER_CREATED")
    .build();
```

## Configuration

### Java Configuration

```java
@Configuration
@EnableIntegration
public class IntegrationConfig {
    
    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }
    
    @Bean
    @ServiceActivator(inputChannel = "inputChannel", outputChannel = "outputChannel")
    public MessageHandler processor() {
        return message -> {
            // Process message
        };
    }
}
```

### XML Configuration

```xml
<int:channel id="inputChannel"/>
<int:channel id="outputChannel"/>

<int:service-activator input-channel="inputChannel" 
                       output-channel="outputChannel"
                       ref="processor" method="process"/>

<int:gateway id="orderGateway" 
             default-request-channel="inputChannel"/>
```

## First Example

### Simple Route

```java
@Configuration
@EnableIntegration
public class SimpleRoute {
    
    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }
    
    @Bean
    @ServiceActivator(inputChannel = "inputChannel")
    public MessageHandler handler() {
        return message -> {
            String payload = (String) message.getPayload();
            System.out.println("Received: " + payload);
        };
    }
}

// Usage
@Autowired
private MessageChannel inputChannel;

public void send(String message) {
    inputChannel.send(MessageBuilder.withPayload(message).build());
}
```

## Best Practices

1. **Use channels**: Decouple producers and consumers
2. **Message structure**: Keep messages simple
3. **Error handling**: Configure error channels
4. **Testing**: Use Spring Integration Test
5. **Monitoring**: Track message flow
6. **Documentation**: Document message contracts
7. **Versioning**: Plan for message evolution
8. **Security**: Secure message channels

## References

- [Spring Integration Reference](https://docs.spring.io/spring-integration/reference/)
- [Spring Integration Samples](https://github.com/spring-projects/spring-integration-samples)
