# Spring Integration - Adapters

## Overview

Adapters connect Spring Integration with external systems. They provide inbound (consume) and outbound (produce) connectivity.

## Table of Contents

1. [Adapter Architecture](#adapter-architecture)
2. [Inbound Adapters](#inbound-adapters)
3. [Outbound Adapters](#outbound-adapters)
4. [Gateways](#gateways)
5. [File Adapter](#file-adapter)
6. [JMS Adapter](#jms-adapter)
7. [HTTP Adapter](#http-adapter)

## Adapter Architecture

### Inbound vs Outbound

```
External System ──> Inbound Adapter ──> Channel ──> Endpoint
Channel ──> Endpoint ──> Outbound Adapter ──> External System
```

### Adapter Types

| Adapter | Direction | Description |
|---------|-----------|-------------|
| File Inbound | In | Read files |
| File Outbound | Out | Write files |
| JMS Inbound | In | Consume JMS messages |
| JMS Outbound | Out | Send JMS messages |
| HTTP Inbound | In | Receive HTTP requests |
| HTTP Outbound | Out | Send HTTP requests |

## Inbound Adapters

### File Inbound

```java
@Bean
@InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "5000"))
public MessageSource<File> fileSource() {
    FileReadingMessageSource source = new FileReadingMessageSource();
    source.setDirectory(new File("/input"));
    source.setFilter(new SimplePatternFileListFilter("*.csv"));
    return source;
}
```

### JMS Inbound

```java
@Bean
@InboundChannelAdapter(value = "jmsChannel", poller = @Poller(fixedDelay = "1000"))
public MessageSource<String> jmsSource() {
    JmsMessageDrivenChannelAdapter adapter = 
        new JmsMessageDrivenChannelAdapter(jmsListenerContainerFactory(), "orders-queue");
    return adapter;
}
```

### HTTP Inbound

```java
@Bean
public RequestMappingEndpointMapping mapping() {
    RequestMappingEndpointMapping mapping = new RequestMappingEndpointMapping();
    mapping.setRequestMapping("/api/orders");
    mapping.setMethod("POST");
    mapping.setRequestChannelName("httpChannel");
    return mapping;
}
```

## Outbound Adapters

### File Outbound

```java
@Bean
@ServiceActivator(inputChannel = "outputChannel")
public MessageHandler fileOutbound() {
    FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("/output"));
    handler.setFileNameGenerator(message -> 
        message.getHeaders().get("orderId", String.class) + ".json");
    handler.setExpectReply(false);
    return handler;
}
```

### JMS Outbound

```java
@Bean
@ServiceActivator(inputChannel = "jmsOutputChannel")
public MessageHandler jmsOutbound() {
    JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate(), "orders-queue");
    return handler;
}
```

### HTTP Outbound

```java
@Bean
@ServiceActivator(inputChannel = "httpOutputChannel")
public MessageHandler httpOutbound() {
    HttpRequestExecutingMessageHandler handler = 
        new HttpRequestExecutingMessageHandler("http://localhost:8080/api/orders");
    handler.setHttpMethod(HttpMethod.POST);
    handler.setExpectedResponseType(String.class);
    return handler;
}
```

## Gateways

### Simple Gateway

```java
@MessagingGateway(defaultRequestChannel = "requestChannel")
public interface OrderGateway {
    OrderConfirmation processOrder(@Payload Order order);
    
    @Gateway(requestChannel = "asyncChannel")
    void processOrderAsync(@Payload Order order);
}
```

### Gateway with Headers

```java
@MessagingGateway(defaultRequestChannel = "requestChannel")
public interface OrderGateway {
    @Gateway(requestChannel = "requestChannel")
    OrderConfirmation processOrder(
        @Payload Order order,
        @Header("priority") int priority,
        @Header("correlationId") String correlationId);
}
```

### Gateway with Reply

```java
@MessagingGateway(defaultRequestChannel = "requestChannel", 
                  defaultReplyTimeout = "5000")
public interface OrderGateway {
    OrderConfirmation processOrder(@Payload Order order);
}
```

## File Adapter

### Complete Example

```java
@Configuration
@EnableIntegration
public class FileIntegrationConfig {
    
    @Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public MessageChannel fileOutputChannel() {
        return new DirectChannel();
    }
    
    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", 
                           poller = @Poller(fixedDelay = "5000"))
    public MessageSource<File> fileSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("/input"));
        source.setFilter(new SimplePatternFileListFilter("*.csv"));
        return source;
    }
    
    @Bean
    @ServiceActivator(inputChannel = "fileInputChannel", outputChannel = "fileOutputChannel")
    public MessageHandler fileProcessor() {
        return message -> {
            File file = (File) message.getPayload();
            // Process file
        };
    }
    
    @Bean
    @ServiceActivator(inputChannel = "fileOutputChannel")
    public MessageHandler fileOutbound() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("/output"));
        handler.setFileNameGenerator(message -> "processed-" + System.currentTimeMillis() + ".csv");
        handler.setExpectReply(false);
        return handler;
    }
}
```

## JMS Adapter

### Complete Example

```java
@Configuration
@EnableIntegration
public class JmsIntegrationConfig {
    
    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }
    
    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFactory());
    }
    
    @Bean
    public MessageChannel jmsInputChannel() {
        return new DirectChannel();
    }
    
    @Bean
    @InboundChannelAdapter(value = "jmsInputChannel", 
                           poller = @Poller(fixedDelay = "1000"))
    public MessageSource<String> jmsSource() {
        JmsMessageDrivenChannelAdapter adapter = 
            new JmsMessageDrivenChannelAdapter(jmsListenerContainerFactory(), "orders-queue");
        return adapter;
    }
    
    @Bean
    public DefaultMessageListenerContainer jmsListenerContainerFactory() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setDestinationName("orders-queue");
        return container;
    }
    
    @Bean
    @ServiceActivator(inputChannel = "jmsOutputChannel")
    public MessageHandler jmsOutbound() {
        JmsSendingMessageHandler handler = new JmsSendingMessageHandler(jmsTemplate(), "processed-queue");
        return handler;
    }
}
```

## HTTP Adapter

### Complete Example

```java
@Configuration
@EnableIntegration
public class HttpIntegrationConfig {
    
    @Bean
    public MessageChannel httpRequestChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public MessageChannel httpResponseChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public RequestMappingEndpointMapping httpEndpoint() {
        RequestMappingEndpointMapping mapping = new RequestMappingEndpointMapping();
        mapping.setRequestMapping("/api/orders");
        mapping.setMethod("POST");
        mapping.setRequestChannelName("httpRequestChannel");
        return mapping;
    }
    
    @Bean
    @ServiceActivator(inputChannel = "httpRequestChannel", outputChannel = "httpResponseChannel")
    public MessageHandler httpRequestHandler() {
        return message -> {
            // Process HTTP request
        };
    }
    
    @Bean
    @ServiceActivator(inputChannel = "httpOutputChannel")
    public MessageHandler httpOutbound() {
        HttpRequestExecutingMessageHandler handler = 
            new HttpRequestExecutingMessageHandler("http://external-api.com/orders");
        handler.setHttpMethod(HttpMethod.POST);
        handler.setExpectedResponseType(String.class);
        return handler;
    }
}
```

## Best Practices

1. **Use appropriate adapter**: Match adapter to protocol
2. **Configure polling**: Set appropriate poll intervals
3. **Error handling**: Configure error channels
4. **Timeouts**: Set connection and read timeouts
5. **Connection pooling**: Use connection pools
6. **Security**: Use secure protocols
7. **Monitoring**: Track adapter metrics
8. **Testing**: Test with mock adapters

## References

- [Spring Integration Adapters](https://docs.spring.io/spring-integration/reference/)
