# Spring Integration - Transformation

## Overview

Message transformation converts messages from one format to another while preserving semantic content. This is essential for integrating heterogeneous systems.

## Table of Contents

1. [Transformation Patterns](#transformation-patterns)
2. [Transformers](#transformers)
3. [Content Enricher](#content-enricher)
4. [Content Filter](#content-filter)
5. [Message Translator](#message-translator)
6. [Data Format Conversion](#data-format-conversion)
7. [Custom Transformers](#custom-transformers)

## Transformation Patterns

### Message Translator

```
Message (JSON) ─── Translator ──> Message (XML)
```

### Content Enricher

```
Message ─── Enricher ──> Message + additional data
```

### Content Filter

```
Message (full) ─── Filter ──> Message (partial)
```

### Normalizer

```
Message (format A) ─┐
Message (format B) ─┼── Normalizer ──> Message (canonical)
Message (format C) ─┘
```

## Transformers

### @Transformer Annotation

```java
@Transformer(inputChannel = "rawChannel", outputChannel = "transformedChannel")
public Order transform(RawOrder raw) {
    return new Order(
        raw.getId(),
        raw.getItems(),
        raw.getTotal()
    );
}
```

### Transformer with Headers

```java
@Transformer(inputChannel = "rawChannel", outputChannel = "transformedChannel")
public Message<Order> transform(Message<RawOrder> message) {
    RawOrder raw = message.getPayload();
    Order order = new Order(raw.getId(), raw.getItems(), raw.getTotal());
    
    return MessageBuilder
        .withPayload(order)
        .copyHeaders(message.getHeaders())
        .setHeader("transformed", true)
        .build();
}
```

### XML Configuration

```xml
<int:transformer input-channel="rawChannel"
                 output-channel="transformedChannel"
                 ref="orderTransformer"
                 method="transform"/>
```

## Content Enricher

### Enrich with External Data

```java
@Bean
@Transformer(inputChannel = "orderChannel", outputChannel = "enrichedChannel")
public Order enrichOrder(Message<Order> message) {
    Order order = message.getPayload();
    
    // Enrich with customer data
    Customer customer = customerService.getCustomer(order.getCustomerId());
    order.setCustomerName(customer.getName());
    order.setCustomerEmail(customer.getEmail());
    
    return order;
}
```

### Using Enricher Pattern

```java
@Bean
@ServiceActivator(inputChannel = "orderChannel", outputChannel = "enrichedChannel")
public MessageHandler enricher() {
    return message -> {
        Order order = message.getPayload();
        
        // Call external service
        Customer customer = customerService.getCustomer(order.getCustomerId());
        
        // Enrich message
        message.getHeaders().set("customerName", customer.getName());
        message.getHeaders().set("customerEmail", customer.getEmail());
    };
}
```

### Poll Enricher

```java
@Bean
@Transformer(inputChannel = "orderChannel", outputChannel = "enrichedChannel")
public Order pollEnrich(Message<Order> message) {
    Order order = message.getPayload();
    
    // Poll for external data
    Message<?> externalData = externalChannel.receive(5000);
    if (externalData != null) {
        order.setExternalInfo(externalData.getPayload());
    }
    
    return order;
}
```

## Content Filter

### Filter Content

```java
@Transformer(inputChannel = "fullMessageChannel", outputChannel = "filteredChannel")
public OrderSummary filterContent(Message<Order> message) {
    Order order = message.getPayload();
    
    return new OrderSummary(
        order.getId(),
        order.getTotal(),
        order.getStatus()
    );
}
```

### Remove Fields

```java
@Transformer(inputChannel = "sensitiveChannel", outputChannel = "sanitizedChannel")
public Order sanitize(Message<Order> message) {
    Order order = message.getPayload();
    
    // Remove sensitive data
    order.setCreditCardNumber(null);
    order.setSocialSecurityNumber(null);
    
    return order;
}
```

## Message Translator

### JSON to XML

```java
@Transformer(inputChannel = "jsonChannel", outputChannel = "xmlChannel")
public String jsonToXml(Message<String> message) throws Exception {
    String json = message.getPayload();
    
    ObjectMapper mapper = new ObjectMapper();
    Order order = mapper.readValue(json, Order.class);
    
    JAXBContext context = JAXBContext.newInstance(Order.class);
    Marshaller marshaller = context.createMarshaller();
    StringWriter writer = new StringWriter();
    marshaller.marshal(order, writer);
    
    return writer.toString();
}
```

### XML to JSON

```java
@Transformer(inputChannel = "xmlChannel", outputChannel = "jsonChannel")
public String xmlToJson(Message<String> message) throws Exception {
    String xml = message.getPayload();
    
    JAXBContext context = JAXBContext.newInstance(Order.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    Order order = (Order) unmarshaller.unmarshal(new StringReader(xml));
    
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(order);
}
```

### CSV to Object

```java
@Transformer(inputChannel = "csvChannel", outputChannel = "objectChannel")
public Order csvToObject(Message<String> message) throws Exception {
    String csv = message.getPayload();
    String[] parts = csv.split(",");
    
    return new Order(
        parts[0],
        parts[1],
        Double.parseDouble(parts[2])
    );
}
```

## Data Format Conversion

### Using Spring Integration Data Format

```java
// JSON
@Bean
@Transformer(inputChannel = "jsonChannel", outputChannel = "objectChannel")
public Order fromJson(Message<String> message) {
    return objectMapper.readValue(message.getPayload(), Order.class);
}

// XML
@Bean
@Transformer(inputChannel = "xmlChannel", outputChannel = "objectChannel")
public Order fromXml(Message<String> message) throws Exception {
    JAXBContext context = JAXBContext.newInstance(Order.class);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    return (Order) unmarshaller.unmarshal(new StringReader(message.getPayload()));
}
```

## Custom Transformers

### Custom Transformer Class

```java
public class OrderTransformer {
    public Order transform(RawOrder raw) {
        return new Order(raw.getId(), raw.getItems(), raw.getTotal());
    }
    
    public OrderSummary summarize(Order order) {
        return new OrderSummary(order.getId(), order.getTotal());
    }
}

// Usage
@Bean
@Transformer(inputChannel = "rawChannel", outputChannel = "orderChannel")
public Order transformOrder(Message<RawOrder> message) {
    OrderTransformer transformer = new OrderTransformer();
    return transformer.transform(message.getPayload());
}
```

### Transformer Chain

```java
@Configuration
@EnableIntegration
public class TransformationConfig {
    
    @Bean
    public MessageChannel rawChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public MessageChannel intermediateChannel() {
        return new DirectChannel();
    }
    
    @Bean
    public MessageChannel finalChannel() {
        return new DirectChannel();
    }
    
    @Bean
    @Transformer(inputChannel = "rawChannel", outputChannel = "intermediateChannel")
    public Order step1(Message<RawOrder> message) {
        // Step 1: Convert raw to order
        return new Order(message.getPayload());
    }
    
    @Bean
    @Transformer(inputChannel = "intermediateChannel", outputChannel = "finalChannel")
    public Order step2(Message<Order> message) {
        // Step 2: Enrich order
        Order order = message.getPayload();
        order.setEnriched(true);
        return order;
    }
}
```

## Best Practices

1. **Single responsibility**: Each transformer does one thing
2. **Idempotency**: Make transformers idempotent
3. **Error handling**: Handle transformation errors
4. **Logging**: Log transformation steps
5. **Testing**: Test transformation logic
6. **Performance**: Consider transformation performance
7. **Validation**: Validate input before transformation
8. **Documentation**: Document transformation rules

## References

- [Spring Integration Transformer](https://docs.spring.io/spring-integration/reference/core/transformation.html)
