# Apache Camel - Routes

## Overview

Routes are the core building blocks in Apache Camel. A route defines the flow of messages from a source (endpoint) through a series of processing steps to a destination (endpoint).

## Table of Contents

1. [Route Basics](#route-basics)
2. [From/To Pattern](#fromto-pattern)
3. [Predicates](#predicates)
4. [Filters](#filters)
5. [Choice/When](#choicewhen)
6. [Routing Patterns](#routing-patterns)
7. [Dynamic Routing](#dynamic-routing)
8. [Route Configuration](#route-configuration)
9. [Testing Routes](#testing-routes)

## Route Basics

### Route Structure

```
┌──────────┐     ┌──────────┐     ┌──────────┐
│   From   │────>│  Steps   │────>│    To    │
│ (Source) │     │(Process) │     │  (Dest)  │
└──────────┘     └──────────┘     └──────────┘
```

### Simple Route

```java
from("file:input")
    .to("file:output");
```

### Route with Multiple Steps

```java
from("jms:queue:orders")
    .log("Received: ${body}")
    .unmarshal().json(JsonLibrary.Jackson, Order.class)
    .filter(body().isInstanceOf(Order.class))
    .process(new OrderValidator())
    .marshal().json(JsonLibrary.Jackson)
    .to("jms:queue:validated-orders");
```

### Route Builder Pattern

```java
public class OrderRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Error handling
        onException(ValidationException.class)
            .handled(true)
            .to("jms:queue:error-orders");
        
        // Main route
        from("jms:queue:incoming-orders")
            .routeId("order-processing")
            .transacted()
            .unmarshal().json(JsonLibrary.Jackson, Order.class)
            .validate(body().isNotNull())
            .validate(simple("${body.id} != null"))
            .process(new OrderProcessor())
            .marshal().json(JsonLibrary.Jackson)
            .to("jms:queue:processed-orders");
    }
}
```

## From/To Pattern

### From Endpoint

```java
// File
from("file:input?noop=true&include=.*\\.csv")

// HTTP
from("jetty:http://0.0.0.0:8080/orders")

// JMS
from("jms:queue:orders")
from("jms:topic:events")

// Timer
from("timer:tick?period=5000")

// Direct
from("direct:start")

// SEDA
from("seda:async-processing")

// REST
from("rest:///orders")
```

### To Endpoint

```java
// File
.to("file:output?fileName=${header.orderId}.json")

// HTTP
.to("http://localhost:8080/api/orders")

// JMS
.to("jms:queue:processed-orders")

// Log
.to("log:out?level=INFO&showAll=true")

// Mock (testing)
.to("mock:end")

// Bean
.to("bean:orderService?method=process")
```

### Multicast (Multiple To)

```java
from("direct:start")
    .multicast()
    .to("jms:queue:queue1", "jms:queue:queue2", "jms:queue:queue3");
```

## Predicates

### Simple Language Predicates

```java
// Header predicates
from("direct:start")
    .choice()
        .when(header("type").isEqualTo("order"))
            .to("jms:queue:orders")
        .when(header("type").isEqualTo("invoice"))
            .to("jms:queue:invoices")
        .otherwise()
            .to("jms:queue:unknown")
    .end();

// Body predicates
from("direct:start")
    .filter(body().contains("error"))
    .to("jms:queue:errors");

// Complex predicates
from("direct:start")
    .choice()
        .when(simple("${header.priority} > 5 && ${header.type} == 'order'"))
            .to("jms:queue:high-priority")
        .when(simple("${header.priority} <= 5"))
            .to("jms:queue:normal-priority")
    .end();
```

### Predicate Operators

| Operator | Description | Example |
|----------|-------------|---------|
| == | Equal | `${header.type} == 'order'` |
| != | Not equal | `${header.type} != 'test'` |
| > | Greater than | `${header.priority} > 5` |
| < | Less than | `${header.amount} < 100` |
| >= | Greater or equal | `${header.count} >= 10` |
| <= | Less or equal | `${header.level} <= 3` |
| contains | Contains | `${body} contains 'error'` |
| matches | Regex | `${header.id} matches 'ORD-.*'` |
| endsWith | Ends with | `${header.file} ends with '.csv'` |
| startsWith | Starts with | `${header.type} starts with 'order'` |

### XPath Predicates

```java
from("direct:start")
    .choice()
        .when(xpath("/order/priority > 5"))
            .to("jms:queue:high-priority")
        .otherwise()
            .to("jms:queue:normal-priority")
    .end();
```

### JSONPath Predicates

```java
from("direct:start")
    .choice()
        .when(jsonpath("$.order.priority > 5"))
            .to("jms:queue:high-priority")
        .otherwise()
            .to("jms:queue:normal-priority")
    .end();
```

## Filters

### Content Filter

```java
// Filter by header
from("direct:start")
    .filter(header("type").isEqualTo("order"))
    .to("jms:queue:orders");

// Filter by body
from("direct:start")
    .filter(body().contains("important"))
    .to("direct:important");

// Filter with multiple conditions
from("direct:start")
    .filter(simple("${header.priority} >= 8 && ${header.type} == 'order'"))
    .to("jms:queue:critical");
```

### Message Filter Pattern

```java
from("direct:start")
    .filter(simple("${body} != null"))
    .filter(simple("${header.correlationId} != null"))
    .to("direct:process");
```

### Content Filter (Transformer)

```java
from("direct:start")
    .filter(body().isInstanceOf(Order.class))
    .transform(body().method("extractSummary"))
    .to("direct:summary");
```

## Choice/When

### Basic Choice

```java
from("direct:start")
    .choice()
        .when(header("type").isEqualTo("order"))
            .to("jms:queue:orders")
        .when(header("type").isEqualTo("invoice"))
            .to("jms:queue:invoices")
        .when(header("type").isEqualTo("payment"))
            .to("jms:queue:payments")
        .otherwise()
            .to("jms:queue:unknown")
    .end();
```

### Nested Choice

```java
from("direct:start")
    .choice()
        .when(header("type").isEqualTo("order"))
            .choice()
                .when(header("priority").isEqualTo("high"))
                    .to("jms:queue:high-orders")
                .otherwise()
                    .to("jms:queue:normal-orders")
            .end()
        .when(header("type").isEqualTo("invoice"))
            .to("jms:queue:invoices")
    .end();
```

### Choice with Complex Predicates

```java
from("direct:start")
    .choice()
        .when(simple("${header.country} == 'US' && ${header.amount} > 1000"))
            .to("jms:queue:us-high-value")
        .when(simple("${header.country} == 'EU'"))
            .to("jms:queue:eu-orders")
        .when(simple("${header.country} == 'UK'"))
            .to("jms:queue:uk-orders")
        .otherwise()
            .to("jms:queue:other-orders")
    .end();
```

## Routing Patterns

### Recipient List

```java
// Static recipient list
from("direct:start")
    .recipientList(constant("jms:queue:queue1,jms:queue:queue2,jms:queue:queue3"));

// Dynamic recipient list
from("direct:start")
    .recipientList(header("recipients"));

// With delimiter
from("direct:start")
    .recipientList(header("recipients"), ",");
```

### Routing Slip

```java
from("direct:start")
    .routingSlip(header("routingSlip"));

// With dynamic expression
from("direct:start")
    .routingSlip(simple("jms:queue:${header.type}-queue"));
```

### Wire Tap

```java
from("direct:start")
    .wireTap("jms:queue:audit")
    .to("direct:process");
```

### Scatter-Gather

```java
from("direct:start")
    .scatterGather()
    .to("http://service1/api", "http://service2/api", "http://service3/api")
    .aggregate(body().listAggregation())
    .to("direct:combined");
```

## Dynamic Routing

### Dynamic Endpoint

```java
from("direct:start")
    .process(exchange -> {
        String queueName = determineQueue(exchange);
        exchange.getIn().setHeader("targetQueue", queueName);
    })
    .toD("jms:queue:${header.targetQueue}");
```

### Dynamic Router

```java
from("direct:start")
    .dynamicRouter(method("routingStrategy", "determineRoute"));

public class RoutingStrategy {
    public String determineRoute(Exchange exchange) {
        String type = exchange.getIn().getHeader("type", String.class);
        return "jms:queue:" + type + "-queue";
    }
}
```

### Content-Based Router

```java
from("direct:start")
    .route(Message.class)
    .choice()
        .when(exchange -> {
            Message msg = exchange.getIn();
            return "ORDER".equals(msg.getHeader("messageType"));
        })
        .to("jms:queue:orders")
    .end();
```

## Route Configuration

### Route ID

```java
from("direct:start")
    .routeId("order-processing-route")
    .log("Processing order")
    .to("direct:end");
```

### Route Description

```java
from("direct:start")
    .description("Processes incoming orders")
    .log("Processing order")
    .to("direct:end");
```

### Route Properties

```java
from("direct:start")
    .routeProperty("audit", "true")
    .routeProperty("retryCount", "3")
    .log("Processing")
    .to("direct:end");
```

### Stream Caching

```java
from("file:input")
    .streamCaching()
    .log("Processing large file")
    .to("file:output");
```

### Transacted Routes

```java
from("jms:queue:orders")
    .transacted("PROPAGATION_REQUIRED")
    .log("Processing")
    .to("jms:queue:processed");
```

## Testing Routes

### Camel Test

```java
public class OrderRouteTest extends CamelTestSupport {
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new OrderRouteBuilder();
    }
    
    @Test
    public void testOrderProcessing() {
        Order order = new Order("123", "Product", 10.0);
        
        template.sendBody("direct:start", order);
        
        Order result = consumer.receiveBody("mock:end", Order.class);
        assertNotNull(result);
        assertEquals("123", result.getId());
    }
    
    @Test
    public void testFiltering() {
        template.sendBodyAndHeader("direct:start", "test", "type", "order");
        
        MockEndpoint mock = getMockEndpoint("mock:end");
        mock.expectedMessageCount(1);
        
        template.sendBodyAndHeader("direct:start", "test", "type", "other");
        
        mock.assertIsSatisfied();
    }
}
```

### Mock Endpoints

```java
@Test
public void testWithMock() throws Exception {
    MockEndpoint mockEnd = getMockEndpoint("mock:end");
    mockEnd.expectedMessageCount(1);
    mockEnd.expectedBodiesReceived("expected body");
    mockEnd.expectedHeaderReceived("type", "order");
    
    template.sendBodyAndHeader("direct:start", "test body", "type", "order");
    
    mockEnd.assertIsSatisfied();
}
```

### AdviceWith

```java
@Test
public void testWithAdvice() throws Exception {
    AdviceWith.adviceWith(context, "order-route", builder -> {
        builder.replaceFromWith("direct:test-input");
        builder.mockEndpoints("mock:*");
    });
    
    template.sendBody("direct:test-input", "test");
    
    MockEndpoint mock = getMockEndpoint("mock:direct:end");
    mock.expectedMessageCount(1);
    mock.assertIsSatisfied();
}
```

## Best Practices

1. **Use meaningful route IDs**: Easy identification and monitoring
2. **Keep routes focused**: Single responsibility principle
3. **Use properties**: Externalize configuration
4. **Error handling**: Always configure error handling
5. **Testing**: Test routes with mock endpoints
6. **Logging**: Use Camel logging for debugging
7. **Documentation**: Add route descriptions
8. **Versioning**: Plan for route evolution

## References

- [Camel Routes](https://camel.apache.org/manual/route.html)
- [Camel DSL](https://camel.apache.org/manual/dsl.html)
- [Camel Testing](https://camel.apache.org/manual/test.html)
