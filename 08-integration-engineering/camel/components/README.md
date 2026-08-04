# Apache Camel - Components

## Overview

Camel Components provide connectivity to external systems. Each component implements a specific protocol or technology, allowing Camel to integrate with a wide range of systems.

## Table of Contents

1. [Component Architecture](#component-architecture)
2. [Core Components](#core-components)
3. [HTTP Components](#http-components)
4. [File Components](#file-components)
5. [JMS Components](#jms-components)
6. [Timer Components](#timer-components)
7. [Database Components](#database-components)
8. [Custom Components](#custom-components)

## Component Architecture

### Component Structure

```
┌─────────────────────────────────────────┐
│              Component                  │
├─────────────────────────────────────────┤
│  createEndpoint(uri) ─────> Endpoint    │
│                                    │     │
│                              getProducer()│
│                              getConsumer()│
└─────────────────────────────────────────┘
```

### URI Format

```
component:name?option1=value1&option2=value2
```

Examples:
```
file:input?noop=true
jms:queue:orders
http://localhost:8080/api
timer:tick?period=1000
```

## Core Components

### Direct Component

Synchronous invocation within the same CamelContext.

```java
from("direct:start")
    .log("Step 1")
    .to("direct:step2");

from("direct:step2")
    .log("Step 2")
    .to("direct:step3");
```

**Options**:
- `timeout`: Timeout in milliseconds

### SEDA Component

Asynchronous invocation with in-memory queue.

```java
from("seda:async-processing")
    .log("Async processing")
    .to("direct:end");

// With concurrency
from("seda:queue?concurrentConsumers=5")
    .process(new Processor())
    .to("direct:end");
```

**Options**:
- `concurrentConsumers`: Number of concurrent consumers
- `queueSize`: Maximum queue size
- `blockWhenFull`: Block when queue is full

### Log Component

Logging messages for debugging.

```java
from("direct:start")
    .to("log:out?level=INFO&showAll=true&multiline=true");
```

**Options**:
- `level`: Log level (DEBUG, INFO, WARN, ERROR)
- `showAll`: Show all message details
- `multiline`: Enable multiline logging

### Mock Component

Testing component for route verification.

```java
from("direct:start")
    .to("mock:end");

// Test
MockEndpoint mock = getMockEndpoint("mock:end");
mock.expectedMessageCount(1);
mock.expectedBodiesReceived("expected");
```

## HTTP Components

### HTTP Client (HTTP4)

```java
// GET request
from("direct:get")
    .setHeader(Exchange.HTTP_METHOD, constant("GET"))
    .to("http://localhost:8080/api/orders")
    .log("Response: ${body}");

// POST request
from("direct:post")
    .setHeader(Exchange.HTTP_METHOD, constant("POST"))
    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
    .to("http://localhost:8080/api/orders")
    .log("Response code: ${header.CamelHttpResponseCode}");

// With authentication
from("direct:auth")
    .setHeader(Exchange.HTTP_METHOD, constant("GET"))
    .to("http://localhost:8080/api/orders?authMethod=Basic&authUsername=user&authPassword=pass");
```

**Options**:
- `httpMethod`: HTTP method
- `connectTimeout`: Connection timeout
- `socketTimeout`: Socket timeout
- `authMethod`: Authentication method

### Jetty Component (HTTP Server)

```java
// REST endpoint
from("jetty:http://0.0.0.0:8080/orders")
    .log("Received request")
    .setBody(constant("Order received"))
    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200));

// With context path
from("jetty:http://0.0.0.0:8080/api?contextPath=/v1")
    .to("direct:process");

// HTTPS
from("jetty:https://0.0.0.0:8443/orders?sslContextParameters=sslContext")
    .to("direct:process");
```

### REST Component

```java
// REST DSL
rest("/api/orders")
    .get()
    .to("direct:getOrders")
    .post()
    .to("direct:createOrder");

from("direct:getOrders")
    .bean(OrderService.class, "getAll");

from("direct:createOrder")
    .bean(OrderService.class, "create");
```

## File Components

### File Component

```java
// Read files
from("file:input?noop=true")
    .log("File: ${header.CamelFileName}")
    .to("file:output");

// With filter
from("file:input?include=.*\\.csv&noop=true")
    .to("direct:process-csv");

// Write file
from("direct:start")
    .to("file:output?fileName=${header.orderId}.json");

// With temp directory
from("file:input?tempDir=/tmp/camel")
    .to("file:output");
```

**Options**:
- `noop`: Don't delete source file
- `include`: Filename pattern to include
- `exclude`: Filename pattern to exclude
- `fileName`: Output filename
- `tempDir`: Temporary directory

### FTP Component

```java
// FTP download
from("ftp://ftp.example.com/incoming?username=user&password=pass&noop=true")
    .to("file:local-input");

// FTP upload
from("file:local-output")
    .to("ftp://ftp.example.com/outgoing?username=user&password=pass");

// FTP with passive mode
from("ftp://ftp.example.com/incoming?passiveMode=true")
    .to("file:local-input");
```

### SFTP Component

```java
// SFTP download
from("sftp://sftp.example.com/incoming?username=user&password=pass&privateKeyFile=/path/to/key")
    .to("file:local-input");

// SFTP upload
from("file:local-output")
    .to("sftp://sftp.example.com/outgoing?username=user&password=pass");
```

## JMS Components

### JMS Queue

```java
// Consume from queue
from("jms:queue:orders")
    .log("Received: ${body}")
    .to("direct:process");

// Produce to queue
from("direct:start")
    .to("jms:queue:orders");

// With acknowledgment
from("jms:queue:orders?acknowledgeMode=AUTO")
    .to("direct:process");
```

### JMS Topic

```java
// Subscribe to topic
from("jms:topic:events")
    .log("Event received: ${body}")
    .to("direct:process-event");

// Publish to topic
from("direct:start")
    .to("jms:topic:events");
```

### JMS Request-Reply

```java
// Request-reply pattern
from("direct:request")
    .to("jms:request-queue?replyTo=reply-queue&replyToType=Exclusive")
    .log("Reply: ${body}");
```

### JMS Transactions

```java
// Transactional consumer
from("jms:queue:orders?transacted=true&acknowledgeMode=TRANSACTED")
    .transacted()
    .process(new OrderProcessor())
    .to("jms:queue:processed");
```

## Timer Components

### Timer Component

```java
// Fixed period
from("timer:tick?period=5000")
    .log("Tick at ${header.CamelTimerFiredTime}");

// One-shot timer
from("timer:once?delay=1000")
    .log("One-shot timer fired");

// Timer with repeat count
from("timer:repeat?repeatCount=5&period=1000")
    .log("Repeat: ${header.CamelTimerCounter}");
```

### Quartz Component

```java
// Cron expression
from("quartz:cron?cron=0 0/5 * * * ?")
    .log("Cron timer fired");

// Simple trigger
from("quartz:simple?repeatCount=10&repeatInterval=5000")
    .log("Simple trigger fired");
```

**Timer Options**:
- `period`: Time between fires in milliseconds
- `delay`: Initial delay before first fire
- `repeatCount`: Number of times to fire
- `daemon`: Run as daemon thread

## Database Components

### JPA Component

```java
// Read from database
from("jpa:com.example.Order?consumeDelete=false")
    .log("Order: ${body.id}")
    .to("direct:process");

// Write to database
from("direct:start")
    .to("jpa:com.example.Order");

// With query
from("jpa:com.example.Order?query=SELECT o FROM Order o WHERE o.status = 'PENDING'")
    .to("direct:pending-orders");
```

### JDBC Component

```java
// Execute query
from("direct:query")
    .setBody(constant("SELECT * FROM orders WHERE status = 'PENDING'"))
    .to("jdbc:dataSource")
    .log("Results: ${body}");

// Update
from("direct:update")
    .setBody(constant("UPDATE orders SET status = 'PROCESSED' WHERE id = 123"))
    .to("jdbc:dataSource");
```

### SQL Component

```java
// SQL query
from("sql:classpath:sql/select-orders.sql?dataSource=dataSource")
    .log("Orders: ${body}");

// SQL update
from("direct:update")
    .setBody(constant("INSERT INTO orders (id, status) VALUES (123, 'NEW')"))
    .to("sql:update?dataSource=dataSource");
```

### MongoDB Component

```java
// Read from MongoDB
from("mongodb:db?database=orders&collection=orders&operation=findAll")
    .log("Order: ${body}");

// Write to MongoDB
from("direct:start")
    .to("mongodb:db?database=orders&collection=orders&operation=insert");
```

## Email Components

### SMTP Component

```java
// Send email
from("direct:send-email")
    .setHeader("to", constant("user@example.com"))
    .setHeader("subject", constant("Order Confirmation"))
    .to("smtp://smtp.example.com?username=user&password=pass");
```

### IMAP Component

```java
// Read emails
from("imap://imap.example.com?username=user&password=pass&folder=INBOX")
    .log("Email: ${header.subject}")
    .to("direct:process-email");
```

## Custom Components

### Creating a Component

```java
public class CustomComponent extends DefaultComponent {
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, String> parameters) {
        return new CustomEndpoint(uri, this, parameters);
    }
}

public class CustomEndpoint extends DefaultEndpoint {
    private final Map<String, String> parameters;
    
    public CustomEndpoint(String uri, Component component, Map<String, String> parameters) {
        super(uri, component);
        this.parameters = parameters;
    }
    
    @Override
    public Producer createProducer() {
        return new CustomProducer(this);
    }
    
    @Override
    public Consumer createConsumer(Processor processor) {
        return new CustomConsumer(this, processor);
    }
}
```

### Registering Custom Component

```java
CamelContext context = new DefaultCamelContext();
context.addComponent("custom", new CustomComponent());

// Use in route
from("custom:endpoint?option=value")
    .to("direct:end");
```

## Component Configuration

### Properties Component

```java
// application.properties
order.queue=orders
order.timeout=5000

// Usage
from("jms:queue:${order.queue}?requestTimeout=${order.timeout}")
    .to("direct:end");
```

### Externalized Configuration

```java
// From properties file
PropertiesComponent properties = new PropertiesComponent();
properties.setLocation("classpath:camel.properties");
context.setPropertiesComponent(properties);
```

## Best Practices

1. **Use appropriate components**: Match component to protocol
2. **Configure timeouts**: Set connection and socket timeouts
3. **Handle errors**: Configure error handling per component
4. **Use properties**: Externalize component configuration
5. **Pool resources**: Use connection pooling for databases
6. **Monitor components**: Track component health and metrics
7. **Test components**: Use mock components for testing
8. **Security**: Use secure protocols (HTTPS, SFTP)

## References

- [Camel Components](https://camel.apache.org/components/)
- [Component List](https://camel.apache.org/components/latest/)
- [Component Configuration](https://camel.apache.org/manual/component-option-converter.html)
