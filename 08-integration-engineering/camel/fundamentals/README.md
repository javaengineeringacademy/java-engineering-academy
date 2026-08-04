# Apache Camel - Fundamentals

## Overview

Apache Camel is an open-source integration framework implementing Enterprise Integration Patterns (EIP). It provides a rule-based routing and mediation engine for integrating diverse systems using a consistent API.

## Table of Contents

1. [What is Apache Camel](#what-is-apache-camel)
2. [Core Concepts](#core-concepts)
3. [CamelContext](#camelcontext)
4. [Routes](#routes)
5. [Endpoints](#endpoints)
6. [Processors](#processors)
7. [Components](#components)
8. [DSL Languages](#dsl-languages)
9. [Error Handling](#error-handling)
10. [First Route Tutorial](#first-route-tutorial)

## What is Apache Camel

Apache Camel is an integration framework that:

- Implements 300+ EIP patterns
- Supports 200+ components (protocols, APIs)
- Provides multiple DSL languages (Java, XML, YAML)
- Includes built-in error handling
- Supports enterprise patterns out of the box

### Architecture

```
┌─────────────────────────────────────────────────────┐
│                    CAMEL CONTEXT                     │
├─────────────────────────────────────────────────────┤
│  ┌─────────┐  ┌─────────┐  ┌─────────┐            │
│  │ Route 1 │  │ Route 2 │  │ Route 3 │            │
│  └────┬────┘  └────┬────┘  └────┬────┘            │
│       │            │            │                   │
│  ┌────▼────┐  ┌────▼────┐  ┌────▼────┐            │
│  │Endpoint │  │Endpoint │  │Endpoint │            │
│  └─────────┘  └─────────┘  └─────────┘            │
│                                                     │
│  ┌─────────────────────────────────────────────┐   │
│  │              Component Registry              │   │
│  └─────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

## Core Concepts

### CamelContext

The CamelContext is the main container for all Camel configuration and routes.

```java
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelApp {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        
        // Add routes
        context.addRouteBuilder(new MyRouteBuilder());
        
        // Start context
        context.start();
        
        // Keep running
        Thread.currentThread().join();
    }
}
```

### Route

A Route defines how messages flow from source to destination.

```java
import org.apache.camel.builder.RouteBuilder;

public class MyRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:input")
            .log("Processing file: ${header.CamelFileName}")
            .to("file:output");
    }
}
```

### Endpoint

An Endpoint is a channel through which messages are sent or received.

```java
// File endpoint
"file:input?noop=true"

// HTTP endpoint
"http://localhost:8080/api/orders"

// JMS endpoint
"jms:queue:order-queue"

// Timer endpoint
"timer:tick?period=1000"
```

### Exchange

An Exchange is the message container carrying message and context.

```java
from("direct:start")
    .process(exchange -> {
        Message in = exchange.getIn();
        String body = in.getBody(String.class);
        in.setBody(body.toUpperCase());
        in.setHeader("processed", true);
    })
    .to("direct:end");
```

### Message

A Message is a data structure with headers and body.

```java
from("direct:start")
    .setHeader("orderId", constant("12345"))
    .setBody(constant("Order processed"))
    .to("mock:end");
```

### Processor

A Processor performs custom logic on messages.

```java
from("direct:start")
    .process(new OrderProcessor())
    .to("direct:end");

public class OrderProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        Order order = in.getBody(Order.class);
        // Process order
        order.setStatus("PROCESSED");
        in.setBody(order);
    }
}
```

## CamelContext

### Configuration

```java
CamelContext context = new DefaultCamelContext();

// Set properties
context.setStreamCaching(true);
context.setAllowUseOriginalMessage(false);

// Add components
context.addComponent("file", new FileComponent());

// Global properties
context.getProperties().put("CamelLogDebugBody", "true");
```

### Lifecycle

```java
// Create context
CamelContext context = new DefaultCamelContext();

// Configure
context.addRouteBuilder(new MyRouteBuilder());

// Start (initializes routes)
context.start();

// Suspend
context.suspend();

// Resume
context.resume();

// Stop (releases resources)
context.stop();
```

### Registry

```java
// Register beans
context.getRegistry().bind("myBean", new MyBean());

// Lookup beans
MyBean bean = context.getRegistry().lookupByNameAndType("myBean", MyBean.class);
```

## Routes

### Route Builder

```java
public class OrderRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Error handling
        onException(Exception.class)
            .handled(true)
            .log("Error: ${exception.message}")
            .to("direct:error-handler");
        
        // Route definition
        from("jms:queue:orders")
            .log("Received order: ${body}")
            .unmarshal().json(JsonLibrary.Jackson, Order.class)
            .filter(body().isInstanceOf(Order.class))
            .process(new OrderValidator())
            .to("jms:queue:validated-orders");
    }
}
```

### Route DSL

```java
// Java DSL
from("file:input")
    .choice()
        .when(header("CamelFileName").endsWith(".xml"))
            .to("jms:queue:xml-orders")
        .when(header("CamelFileName").endsWith(".json"))
            .to("jms:queue:json-orders")
        .otherwise()
            .to("jms:queue:unknown-orders")
    .end();

// XML DSL
<route>
    <from uri="file:input"/>
    <choice>
        <when>
            <simple>${header.CamelFileName} ends with '.xml'</simple>
            <to uri="jms:queue:xml-orders"/>
        </when>
        <otherwise>
            <to uri="jms:queue:unknown-orders"/>
        </otherwise>
    </choice>
</route>
```

## Endpoints

### File Endpoint

```java
// Read file
from("file:input?noop=true")
    .to("file:output");

// Write file with name
from("direct:start")
    .to("file:output?fileName=${header.orderId}.txt");

// File with filter
from("file:input?include=.*\\.csv&noop=true")
    .to("direct:process");
```

### HTTP Endpoint

```java
// HTTP consumer
from("jetty:http://0.0.0.0:8080/orders")
    .log("Received HTTP request")
    .process(new HttpRequestProcessor())
    .setBody(constant("Order processed"))
    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

// HTTP producer
from("direct:start")
    .setHeader(Exchange.HTTP_METHOD, constant("POST"))
    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
    .to("http://localhost:8080/api/orders");
```

### JMS Endpoint

```java
// Queue
from("jms:queue:orders")
    .log("Received from queue")
    .to("jms:queue:processed-orders");

// Topic
from("jms:topic:events")
    .log("Received event")
    .to("direct:process-event");

// Request-reply
from("direct:request")
    .to("jms:request-queue")
    .log("Got reply: ${body}");
```

### Timer Endpoint

```java
// Fixed period
from("timer:tick?period=5000")
    .log("Tick at ${header.CamelTimerFiredTime}");

// One-shot timer
from("timer:once?delay=1000")
    .log("One-shot timer fired");

// Cron expression
from("timer:cron?cron=0 0/5 * * * ?")
    .log("Cron timer fired");
```

## Processors

### Built-in Processors

```java
// Log
from("direct:start")
    .log("Processing message: ${body}");

// Set header
from("direct:start")
    .setHeader("customHeader", constant("value"));

// Set body
from("direct:start")
    .body(body -> body.toString().toUpperCase());

// Transform
from("direct:start")
    .transform(body().regexReplaceAll("[0-9]", "*"));

// Filter
from("direct:start")
    .filter(body().contains("important"))
    .to("direct:important");
```

### Custom Processor

```java
public class OrderProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Message in = exchange.getIn();
        String body = in.getBody(String.class);
        
        // Custom logic
        Order order = parseOrder(body);
        validateOrder(order);
        
        // Update message
        in.setBody(order);
        in.setHeader("orderId", order.getId());
    }
}

// Usage
from("direct:start")
    .process(new OrderProcessor())
    .to("direct:end");
```

### Bean Processor

```java
// Service bean
@Service
public class OrderService {
    public Order processOrder(Order order) {
        // Business logic
        return order;
    }
}

// Route using bean
from("direct:start")
    .bean(OrderService.class, "processOrder")
    .to("direct:end");

// Or with method name
from("direct:start")
    .bean("orderService", "processOrder")
    .to("direct:end");
```

## Components

### Core Components

| Component | URI Prefix | Description |
|-----------|------------|-------------|
| file | file: | File system access |
| http | http:// | HTTP client/server |
| jms | jms: | JMS messaging |
| timer | timer: | Timer triggers |
| direct | direct: | Direct invocation |
| seda | seda: | Async in-memory |
| bean | bean: | Java bean invocation |
| log | log: | Logging |
| mock | mock: | Testing |
| dataset | dataset: | Data generation |

### Loading Components

```java
// Automatic (from classpath)
CamelContext context = new DefaultCamelContext();

// Explicit
context.addComponent("myComponent", new MyComponent());

// From URI (auto-discovered)
from("file:input") // FileComponent auto-loaded
```

## DSL Languages

### Java DSL

```java
from("file:input")
    .choice()
        .when(header("type").isEqualTo("order"))
            .to("jms:queue:orders")
        .otherwise()
            .to("jms:queue:other")
    .end();
```

### XML DSL

```xml
<route>
    <from uri="file:input"/>
    <choice>
        <when>
            <simple>${header.type} == 'order'</simple>
            <to uri="jms:queue:orders"/>
        </when>
        <otherwise>
            <to uri="jms:queue:other"/>
        </otherwise>
    </choice>
</route>
```

### YAML DSL

```yaml
- route:
    from:
      uri: "file:input"
    steps:
      - choice:
          when:
            - simple: "${header.type} == 'order'"
              steps:
                - to: "jms:queue:orders"
          otherwise:
            steps:
              - to: "jms:queue:other"
```

## Error Handling

### Basic Error Handling

```java
from("direct:start")
    .doTry()
        .to("direct:process")
    .doCatch(Exception.class)
        .log("Error: ${exception.message}")
        .to("direct:error-handler")
    .doFinally()
        .log("Processing complete")
    .end();
```

### OnException

```java
from("direct:start")
    .onException(ConnectException.class)
        .maximumRedeliveries(3)
        .redeliveryDelay(1000)
        .handled(true)
        .to("direct:error")
    .end()
    .to("direct:process");
```

## First Route Tutorial

### Step 1: Create Project

```bash
mvn archetype:generate \
  -DarchetypeGroupId=org.apache.camel.archetypes \
  -DarchetypeArtifactId=camel-archetype-java \
  -DarchetypeVersion=4.0.0
```

### Step 2: Define Route

```java
import org.apache.camel.builder.RouteBuilder;

public class MyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:hello?period=5000")
            .log("Hello Camel! Time: ${header.CamelTimerFiredTime}")
            .setBody(constant("Hello World!"))
            .to("log:out");
    }
}
```

### Step 3: Run

```java
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class Main {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRouteBuilder(new MyRoute());
        context.start();
        Thread.currentThread().join();
    }
}
```

## Best Practices

1. **Use CamelContext wisely**: One context per application
2. **Prefer DSL**: Use Java/XML/YAML DSL over raw APIs
3. **Error handling**: Always configure error handling
4. **Logging**: Use Camel's built-in logging
5. **Testing**: Use Camel Test for route testing
6. **Properties**: Externalize configuration
7. **Components**: Use appropriate components for protocols
8. **Processors**: Keep processors focused and testable

## References

- [Apache Camel Manual](https://camel.apache.org/manual/)
- [Camel Components](https://camel.apache.org/components/)
- [Camel Examples](https://github.com/apache/camel/tree/main/examples)
