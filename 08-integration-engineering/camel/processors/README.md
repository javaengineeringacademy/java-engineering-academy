# Apache Camel - Processors

## Overview

Processors in Apache Camel perform custom logic on messages as they flow through routes. They enable message transformation, enrichment, validation, and other processing operations.

## Table of Contents

1. [Processor Basics](#processor-basics)
2. [Message Translator](#message-translator)
3. [Content Enricher](#content-enricher)
4. [Transformer](#transformer)
5. [Validator](#validator)
6. [Aggregator](#aggregator)
7. [Splitter](#splitter)
8. [Custom Processors](#custom-processors)

## Processor Basics

### Processor Interface

```java
import org.apache.camel.Processor;
import org.apache.camel.Exchange;

public class MyProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // Access message
        Message in = exchange.getIn();
        
        // Get payload
        Object body = in.getBody();
        
        // Get headers
        String orderId = in.getHeader("orderId", String.class);
        
        // Modify payload
        in.setBody("Processed: " + body);
        
        // Set headers
        in.setHeader("processed", true);
    }
}
```

### Using Processors in Routes

```java
from("direct:start")
    .process(new MyProcessor())
    .to("direct:end");

// Inline processor
from("direct:start")
    .process(exchange -> {
        String body = exchange.getIn().getBody(String.class);
        exchange.getIn().setBody(body.toUpperCase());
    })
    .to("direct:end");
```

## Message Translator

### Pattern Description

Converts message format while preserving semantic content.

```
Message (JSON) ─── Translator ──> Message (XML)
```

### Implementation

```java
public class JsonToXmlTranslator implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String json = exchange.getIn().getBody(String.class);
        
        // Parse JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        
        // Convert to XML
        String xml = convertToXml(node);
        
        exchange.getIn().setBody(xml);
        exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/xml");
    }
    
    private String convertToXml(JsonNode node) {
        // XML conversion logic
        return "<order>" + node.get("orderId").asText() + "</order>";
    }
}
```

### Using @Transformer

```java
@Transformer(inputChannel = "jsonChannel", outputChannel = "xmlChannel")
public String transform(String json) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Order order = mapper.readValue(json, Order.class);
    
    JAXBContext context = JAXBContext.newInstance(Order.class);
    Marshaller marshaller = context.createMarshaller();
    StringWriter writer = new StringWriter();
    marshaller.marshal(order, writer);
    
    return writer.toString();
}
```

### Data Format Transformers

```java
// JSON to XML
from("direct:start")
    .unmarshal().json(JsonLibrary.Jackson, Order.class)
    .marshal().jaxb()
    .to("direct:end");

// XML to JSON
from("direct:start")
    .unmarshal().jaxb(Order.class)
    .marshal().json(JsonLibrary.Jackson)
    .to("direct:end");

// CSV to JSON
from("direct:start")
    .unmarshal().csv()
    .marshal().json(JsonLibrary.Jackson)
    .to("direct:end");
```

## Content Enricher

### Pattern Description

Adds information from external sources to the message.

```
Message ─── Enricher ──> Message + additional data
```

### Implementation

```java
public class OrderEnricher implements Processor {
    private final CustomerService customerService;
    
    @Override
    public void process(Exchange exchange) throws Exception {
        Order order = exchange.getIn().getBody(Order.class);
        
        // Enrich with customer data
        Customer customer = customerService.getCustomer(order.getCustomerId());
        order.setCustomerName(customer.getName());
        order.setCustomerEmail(customer.getEmail());
        
        exchange.getIn().setBody(order);
    }
}
```

### Using Content Enricher Pattern

```java
// Enrich with customer data
from("direct:orders")
    .enrich("jms:queue:customer-lookup?requestTimeout=5000",
        (oldExchange, newExchange) -> {
            Order order = oldExchange.getIn().getBody(Order.class);
            Customer customer = newExchange.getIn().getBody(Customer.class);
            order.setCustomerName(customer.getName());
            return oldExchange;
        })
    .to("direct:enriched-orders");

// Enrich with header enrichment
from("direct:start")
    .enrichWith(header("customerId"))
    .process(exchange -> {
        String customerId = exchange.getIn().getHeader("customerId", String.class);
        // Lookup customer
        exchange.getIn().setHeader("customerName", customerName);
    })
    .to("direct:end");
```

### Poll Enricher

```java
from("direct:start")
    .pollEnrich("jms:queue:data-source?timeout=5000",
        (oldExchange, newExchange) -> {
            Object data = newExchange.getIn().getBody();
            oldExchange.getIn().setHeader("externalData", data);
            return oldExchange;
        })
    .to("direct:end");
```

## Transformer

### Bean Transformer

```java
public class OrderTransformer {
    public OrderSummary transform(Order order) {
        return new OrderSummary(
            order.getId(),
            order.getTotal(),
            order.getStatus()
        );
    }
}

// Usage
from("direct:start")
    .bean(OrderTransformer.class, "transform")
    .to("direct:end");
```

### Expression Transformer

```java
// Simple expression
from("direct:start")
    .transform(simple("Order ${body.id} processed"))
    .to("direct:end");

// XPath expression
from("direct:start")
    .transform(xpath("/order/customerId"))
    .to("direct:end");

// JSONPath expression
from("direct:start")
    .transform(jsonpath("$.order.id"))
    .to("direct:end");
```

### Data Format Transformer

```java
// Marshal to JSON
from("direct:start")
    .marshal().json(JsonLibrary.Jackson)
    .to("direct:end");

// Unmarshal from JSON
from("direct:start")
    .unmarshal().json(JsonLibrary.Jackson, Order.class)
    .to("direct:end");

// Marshal to XML
from("direct:start")
    .marshal().jaxb()
    .to("direct:end");
```

## Validator

### Message Validator

```java
public class OrderValidator implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Order order = exchange.getIn().getBody(Order.class);
        
        if (order == null) {
            throw new ValidationException("Order cannot be null");
        }
        
        if (order.getId() == null || order.getId().isEmpty()) {
            throw new ValidationException("Order ID is required");
        }
        
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new ValidationException("Order must have at least one item");
        }
        
        if (order.getTotal() <= 0) {
            throw new ValidationException("Order total must be positive");
        }
    }
}
```

### Using Validators

```java
from("direct:start")
    .validate(body().isNotNull())
    .validate(simple("${body.id} != null"))
    .validate(simple("${body.total} > 0"))
    .to("direct:end");

// With custom validator
from("direct:start")
    .process(new OrderValidator())
    .to("direct:end");
```

### Validation with Exception Handling

```java
from("direct:start")
    .doTry()
        .process(new OrderValidator())
        .to("direct:valid-order")
    .doCatch(ValidationException.class)
        .log("Validation failed: ${exception.message}")
        .to("direct:invalid-order")
    .end();
```

## Aggregator

### Pattern Description

Combines multiple related messages into a single message.

```
Message 1 ─┐
Message 2 ─┼── Aggregator ──> Combined Message
Message 3 ─┘
```

### Implementation

```java
from("direct:start")
    .aggregate(header("correlationId"), new OrderAggregator())
    .completionSize(3)
    .completionTimeout(5000)
    .to("direct:aggregated");

public class OrderAggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }
        
        List<Order> orders = oldExchange.getIn().getBody(List.class);
        if (orders == null) {
            orders = new ArrayList<>();
        }
        orders.add(newExchange.getIn().getBody(Order.class));
        oldExchange.getIn().setBody(orders);
        
        return oldExchange;
    }
}
```

### Aggregation Strategies

```java
// List aggregation
from("direct:start")
    .aggregate(header("correlationId"), 
        (old, new) -> {
            if (old == null) return new;
            List<Object> list = new ArrayList<>();
            list.add(old.getIn().getBody());
            list.add(new.getIn().getBody());
            new.getIn().setBody(list);
            return new;
        })
    .completionSize(3)
    .to("direct:end");

// String concatenation
from("direct:start")
    .aggregate(header("correlationId"),
        (old, new) -> {
            if (old == null) return new;
            String oldBody = old.getIn().getBody(String.class);
            String newBody = new.getIn().getBody(String.class);
            new.getIn().setBody(oldBody + "," + newBody);
            return new;
        })
    .completionSize(3)
    .to("direct:end");
```

## Splitter

### Pattern Description

Breaks a composite message into individual messages.

```
Composite Message ─── Splitter ──┬──> Message 1
                                 ├──> Message 2
                                 └──> Message 3
```

### Implementation

```java
// Split list
from("direct:start")
    .split(body().cast(List.class))
    .log("Item: ${body}")
    .to("direct:end");

// Split by expression
from("direct:start")
    .split(xpath("/order/items/item"))
    .log("Item: ${body}")
    .to("direct:end");

// Split with aggregator
from("direct:start")
    .split(body().cast(List.class))
    .log("Processing: ${body}")
    .aggregate(header("correlationId"), 
        (old, new) -> {
            // Aggregate results
            return new;
        })
    .completionSize(3)
    .to("direct:end");
```

### Custom Splitter

```java
public class OrderSplitter implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Order order = exchange.getIn().getBody(Order.class);
        List<OrderItem> items = order.getItems();
        
        for (OrderItem item : items) {
            exchange.getContext().createProducerTemplate()
                .sendBody("direct:split-items", item);
        }
    }
}
```

## Custom Processors

### Service Activator

```java
@Service
public class OrderService {
    @ServiceActivator(inputChannel = "input", outputChannel = "output")
    public Message<?> process(Message<?> message) {
        Order order = (Order) message.getPayload();
        // Process order
        return MessageBuilder.withPayload(order).build();
    }
}
```

### Router

```java
@Service
public class OrderRouter {
    @Router(inputChannel = "input")
    public String route(Order order) {
        if (order.getTotal() > 1000) {
            return "high-value-channel";
        }
        return "normal-channel";
    }
}
```

### Filter

```java
@Service
public class OrderFilter {
    @Filter(inputChannel = "input", outputChannel = "output")
    public boolean filter(Order order) {
        return order.getTotal() > 100;
    }
}
```

### Handler

```java
@Service
public class ErrorHandler {
    @MessageExceptionHandler
    public void handleException(Exception exception) {
        log.error("Error processing message", exception);
    }
}
```

## Best Practices

1. **Single Responsibility**: Each processor should do one thing well
2. **Idempotency**: Design processors to be idempotent when possible
3. **Error Handling**: Include proper error handling in processors
4. **Logging**: Add appropriate logging for debugging
5. **Testing**: Write unit tests for processors
6. **Performance**: Consider async processing for heavy operations
7. **Statelessness**: Keep processors stateless when possible
8. **Documentation**: Document processor purpose and behavior

## References

- [Camel Processors](https://camel.apache.org/manual/processor.html)
- [Message Translator](https://www.enterpriseintegrationpatterns.com/patterns/messaging/MessageTranslator.html)
- [Content Enricher](https://www.enterpriseintegrationpatterns.com/patterns/messaging/ContentEnricher.html)
