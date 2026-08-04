# Apache Camel - DSL (Domain Specific Languages)

## Overview

Apache Camel provides multiple Domain Specific Languages (DSL) for defining integration routes. Each DSL offers different advantages depending on the use case.

## Table of Contents

1. [DSL Overview](#dsl-overview)
2. [Java DSL](#java-dsl)
3. [XML DSL](#xml-dsl)
4. [YAML DSL](#yaml-dsl)
5. [REST DSL](#rest-dsl)
6. [DSL Comparison](#dsl-comparison)

## DSL Overview

### Available DSLs

| DSL | File Extension | Best For |
|-----|----------------|----------|
| Java DSL | .java | Programmatic, type-safe |
| XML DSL | .xml | Declarative, configuration |
| YAML DSL | .yaml | Simple routes, readability |
| REST DSL | .yaml/.xml | REST API definition |
| Kotlin DSL | .kt | Kotlin applications |

### Choosing a DSL

```
┌─────────────────┐
│  Route Definition│
└────────┬────────┘
         │
    ┌────▼────┐
    │ Java DSL │──── Type-safe, IDE support
    └────┬────┘
         │
    ┌────▼────┐
    │ XML DSL  │──── Declarative, external config
    └────┬────┘
         │
    ┌────▼────┐
    │ YAML DSL │──── Simple, readable
    └─────────┘
```

## Java DSL

### RouteBuilder

```java
import org.apache.camel.builder.RouteBuilder;

public class OrderRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Error handling
        onException(ValidationException.class)
            .handled(true)
            .log("Validation error: ${exception.message}")
            .to("direct:error");
        
        // Main route
        from("jms:queue:orders")
            .routeId("order-processing")
            .log("Received order")
            .unmarshal().json(JsonLibrary.Jackson, Order.class)
            .validate(body().isNotNull())
            .process(new OrderValidator())
            .marshal().json(JsonLibrary.Jackson)
            .to("jms:queue:processed-orders");
    }
}
```

### Expression DSL

```java
from("direct:start")
    // Simple expressions
    .filter(simple("${header.type} == 'order'"))
    .transform(simple("Order ${body.id}"))
    
    // XPath expressions
    .filter(xpath("/order/priority > 5"))
    .transform(xpath("/order/customerId"))
    
    // JSONPath expressions
    .filter(jsonpath("$.order.priority > 5"))
    .transform(jsonpath("$.order.id"))
    
    // Method expressions
    .filter(body().method("isValid"))
    .transform(body().method("transform"))
    
    .to("direct:end");
```

### Choice/When DSL

```java
from("direct:start")
    .choice()
        .when(header("type").isEqualTo("order"))
            .to("jms:queue:orders")
        .when(header("type").isEqualTo("invoice"))
            .to("jms:queue:invoices")
        .when(simple("${header.priority} > 5"))
            .to("jms:queue:high-priority")
        .otherwise()
            .to("jms:queue:default")
    .end();
```

### Split/Aggregate DSL

```java
from("direct:start")
    // Split
    .split(body().cast(List.class))
        .log("Processing: ${body}")
        .process(new ItemProcessor())
    .end()
    
    // Aggregate
    .aggregate(header("correlationId"), new OrderAggregator())
        .completionSize(3)
        .completionTimeout(5000)
    .to("direct:end");

from("direct:start")
    // Split and aggregate
    .split(xpath("/order/items/item"))
        .aggregate(header("orderId"), new ItemAggregator())
            .completionSize(3)
        .end()
    .to("direct:end");
```

### Error Handling DSL

```java
from("direct:start")
    .onException(ConnectException.class)
        .maximumRedeliveries(3)
        .redeliveryDelay(1000)
        .retryAttemptedLogLevel(LoggingLevel.WARN)
        .handled(true)
        .to("direct:error")
    .end()
    .onException(ValidationException.class)
        .handled(true)
        .log("Validation error")
    .end()
    .doTry()
        .to("direct:process")
    .doCatch(Exception.class)
        .log("Error: ${exception.message}")
    .doFinally()
        .log("Finally block")
    .end();
```

### Bean Integration DSL

```java
from("direct:start")
    // Call bean method
    .bean(OrderService.class, "processOrder")
    
    // Call bean with expression
    .bean("orderService", "process(${body})")
    
    // Call bean method on body
    .transform(body().method("transform"))
    
    // Call bean method with headers
    .bean("orderService", "process(${body}, ${header.type})")
    
    .to("direct:end");
```

### Data Format DSL

```java
from("direct:start")
    // Marshal/Unmarshal
    .unmarshal().json(JsonLibrary.Jackson, Order.class)
    .marshal().json(JsonLibrary.Jackson)
    
    // XML
    .unmarshal().jaxb(Order.class)
    .marshal().jaxb()
    
    // CSV
    .unmarshal().csv()
    .marshal().csv()
    
    // Binary
    .unmarshal().binary()
    .marshal().binary()
    
    .to("direct:end");
```

## XML DSL

### Basic Route

```xml
<route id="order-processing">
    <from uri="jms:queue:orders"/>
    <log message="Received order"/>
    <unmarshal>
        <json library="Jackson" type="com.example.Order"/>
    </unmarshal>
    <validate>
        <simple>${body} != null</simple>
    </validate>
    <process ref="orderValidator"/>
    <marshal>
        <json library="Jackson"/>
    </marshal>
    <to uri="jms:queue:processed-orders"/>
</route>
```

### Choice/When

```xml
<route>
    <from uri="direct:start"/>
    <choice>
        <when>
            <simple>${header.type} == 'order'</simple>
            <to uri="jms:queue:orders"/>
        </when>
        <when>
            <simple>${header.type} == 'invoice'</simple>
            <to uri="jms:queue:invoices"/>
        </when>
        <otherwise>
            <to uri="jms:queue:default"/>
        </otherwise>
    </choice>
</route>
```

### Split/Aggregate

```xml
<route>
    <from uri="direct:start"/>
    <split>
        <body/>
        <log message="Processing: ${body}"/>
        <process ref="itemProcessor"/>
    </split>
</route>

<route>
    <from uri="direct:start"/>
    <split>
        <xpath>/order/items/item</xpath>
        <aggregate strategyRef="aggregator" completionSize="3">
            <correlationExpression>
                <simple>${header.correlationId}</simple>
            </correlationExpression>
            <process ref="aggregationProcessor"/>
        </aggregate>
    </split>
</route>
```

### Error Handling

```xml
<route>
    <from uri="direct:start"/>
    <onException>
        <exception>java.io.IOException</exception>
        <redeliveryPolicy maximumRedeliveries="3" redeliveryDelay="1000"/>
        <handled><constant>true</constant></handled>
        <to uri="direct:error"/>
    </onException>
    <doTry>
        <to uri="direct:process"/>
    </doTry>
    <doCatch>
        <exception>java.lang.Exception</exception>
        <log message="Error: ${exception.message}"/>
    </doCatch>
    <doFinally>
        <log message="Finally block"/>
    </doFinally>
</route>
```

### Bean Integration

```xml
<route>
    <from uri="direct:start"/>
    <bean ref="orderService" method="processOrder"/>
    <to uri="direct:end"/>
</route>

<route>
    <from uri="direct:start"/>
    <transform>
        <method ref="orderTransformer" method="transform"/>
    </transform>
    <to uri="direct:end"/>
</route>
```

## YAML DSL

### Basic Route

```yaml
- route:
    from:
      uri: "jms:queue:orders"
    steps:
      - log: "Received order"
      - unmarshal:
          json:
            library: Jackson
            type: com.example.Order
      - validate:
          simple: "${body} != null"
      - process:
          ref: orderValidator
      - marshal:
          json:
            library: Jackson
      - to: "jms:queue:processed-orders"
```

### Choice/When

```yaml
- route:
    from:
      uri: "direct:start"
    steps:
      - choice:
          when:
            - simple: "${header.type} == 'order'"
              steps:
                - to: "jms:queue:orders"
            - simple: "${header.type} == 'invoice'"
              steps:
                - to: "jms:queue:invoices"
          otherwise:
            steps:
              - to: "jms:queue:default"
```

### Split/Aggregate

```yaml
- route:
    from:
      uri: "direct:start"
    steps:
      - split:
          body: {}
          steps:
            - log: "Processing: ${body}"
            - process:
                ref: itemProcessor

- route:
    from:
      uri: "direct:start"
    steps:
      - split:
          xpath: "/order/items/item"
          steps:
            - aggregate:
                strategyRef: aggregator
                completionSize: 3
                correlationExpression:
                  simple: "${header.correlationId}"
                steps:
                  - process:
                      ref: aggregationProcessor
```

### Error Handling

```yaml
- route:
    from:
      uri: "direct:start"
    steps:
      - onException:
          exception: java.io.IOException
          redeliveryPolicy:
            maximumRedeliveries: 3
            redeliveryDelay: 1000
          handled: true
          to: "direct:error"
      - doTry:
          - to: "direct:process"
        doCatch:
          - exception: java.lang.Exception
            steps:
              - log: "Error: ${exception.message}"
        doFinally:
          steps:
            - log: "Finally block"
```

## REST DSL

### Java REST DSL

```java
rest("/api/orders")
    .description("Order REST API")
    .consumes("application/json")
    .produces("application/json")
    
    .get()
    .description("Get all orders")
    .outType(List.class)
    .to("bean:orderService?method=getAll")
    
    .get("/{id}")
    .description("Get order by ID")
    .outType(Order.class)
    .to("bean:orderService?method=getById(${header.id})")
    
    .post()
    .description("Create order")
    .type(Order.class)
    .outType(OrderConfirmation.class)
    .to("bean:orderService?method=create")
    
    .put("/{id}")
    .description("Update order")
    .type(Order.class)
    .to("bean:orderService?method=update(${header.id})")
    
    .delete("/{id}")
    .description("Delete order")
    .to("bean:orderService?method=delete(${header.id})");
```

### XML REST DSL

```xml
<rest path="/api/orders" consumes="application/json" produces="application/json">
    <get uri="/" outType="java.util.List">
        <to uri="bean:orderService?method=getAll"/>
    </get>
    <get uri="/{id}" outType="com.example.Order">
        <to uri="bean:orderService?method=getById(${header.id})"/>
    </get>
    <post uri="/" type="com.example.Order" outType="com.example.OrderConfirmation">
        <to uri="bean:orderService?method=create"/>
    </post>
    <put uri="/{id}" type="com.example.Order">
        <to uri="bean:orderService?method=update(${header.id})"/>
    </put>
    <delete uri="/{id}">
        <to uri="bean:orderService?method=delete(${header.id})"/>
    </delete>
</rest>
```

### YAML REST DSL

```yaml
- rest:
    path: "/api/orders"
    consumes: "application/json"
    produces: "application/json"
    get:
      uri: "/"
      outType: "java.util.List"
      to: "bean:orderService?method=getAll"
    get:
      uri: "/{id}"
      outType: "com.example.Order"
      to: "bean:orderService?method=getById(${header.id})"
    post:
      uri: "/"
      type: "com.example.Order"
      outType: "com.example.OrderConfirmation"
      to: "bean:orderService?method=create"
```

## DSL Comparison

### Feature Comparison

| Feature | Java DSL | XML DSL | YAML DSL |
|---------|----------|---------|----------|
| Type Safety | ✅ | ❌ | ❌ |
| IDE Support | ✅ | ✅ | ✅ |
| Readability | Good | Good | Excellent |
| Flexibility | High | Medium | Low |
| External Config | ❌ | ✅ | ✅ |
| Hot Reload | ❌ | ✅ | ✅ |
| Testing | Easy | Medium | Medium |

### When to Use

- **Java DSL**: Complex logic, type safety, IDE support
- **XML DSL**: External configuration, tooling support
- **YAML DSL**: Simple routes, readability, configuration
- **REST DSL**: REST API definition

## Best Practices

1. **Choose appropriate DSL**: Match DSL to use case
2. **Keep routes focused**: Single responsibility
3. **Use properties**: Externalize configuration
4. **Document routes**: Add descriptions and IDs
5. **Version control**: Track route changes
6. **Test routes**: Use Camel Test
7. **Monitor routes**: Add logging and metrics
8. **Error handling**: Configure error handling

## References

- [Camel DSL](https://camel.apache.org/manual/dsl.html)
- [Java DSL](https://camel.apache.org/manual/java-dsl.html)
- [XML DSL](https://camel.apache.org/manual/xml-dsl.html)
- [YAML DSL](https://camel.apache.org/manual/yaml-dsl.html)
