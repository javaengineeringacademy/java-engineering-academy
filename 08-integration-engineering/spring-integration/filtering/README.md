# Spring Integration - Filtering

## Overview

Message filtering selectively passes or discards messages based on criteria. This is essential for routing and processing relevant messages.

## Table of Contents

1. [Filter Patterns](#filter-patterns)
2. [Message Filter](#message-filter)
3. [Header Filter](#header-filter)
4. [Content Filter](#content-filter)
5. [Expression-Based Filtering](#expression-based-filtering)
6. [Custom Filters](#custom-filters)
7. [Filter Configuration](#filter-configuration)

## Filter Patterns

### Message Filter

```
Message ─── Filter ──┬──> Channel (if accepted)
                     └──> (discarded)
```

### Content Filter

```
Message (full) ─── Filter ──> Message (partial)
```

### Header Filter

```
Message (all headers) ─── Filter ──> Message (selected headers)
```

## Message Filter

### Simple Filter

```java
@ServiceActivator(inputChannel = "inputChannel", outputChannel = "outputChannel")
public boolean filter(Message<?> message) {
    Integer priority = message.getHeaders().get("priority", Integer.class);
    return priority != null && priority >= 5;
}
```

### With Discard Channel

```java
@Bean
@Filter(inputChannel = "inputChannel", outputChannel = "acceptedChannel",
        discardChannel = "rejectedChannel")
public MessageSelector highPriorityFilter() {
    return message -> {
        Integer priority = message.getHeaders().get("priority", Integer.class);
        return priority != null && priority >= 8;
    };
}
```

### XML Configuration

```xml
<int:filter input-channel="inputChannel"
            output-channel="acceptedChannel"
            discard-channel="rejectedChannel"
            expression="headers['priority'] >= 5"/>
```

## Header Filter

### Remove Headers

```java
@Bean
@Filter(inputChannel = "inputChannel", outputChannel = "outputChannel")
public boolean headerFilter(Message<?> message) {
    // Remove sensitive headers
    message.getHeaders().remove("password");
    message.getHeaders().remove("token");
    return true;
}
```

### Select Headers

```java
@Bean
@Transformer(inputChannel = "inputChannel", outputChannel = "outputChannel")
public Message<?> headerFilter(Message<?> message) {
    return MessageBuilder
        .withPayload(message.getPayload())
        .copyHeader("correlationId", message.getHeaders())
        .copyHeader("timestamp", message.getHeaders())
        .build();
}
```

### XML Configuration

```xml
<int:header-filter input-channel="inputChannel"
                   output-channel="outputChannel"
                   delete-headers="password,token,secret"/>
```

## Content Filter

### Filter by Content

```java
@Bean
@Filter(inputChannel = "inputChannel", outputChannel = "outputChannel")
public boolean contentFilter(Message<?> message) {
    String payload = message.getPayload().toString();
    return payload.contains("important");
}
```

### Filter by Type

```java
@Bean
@Filter(inputChannel = "inputChannel", outputChannel = "outputChannel")
public boolean typeFilter(Message<?> message) {
    return message.getPayload() instanceof Order;
}
```

### Filter with Multiple Conditions

```java
@Bean
@Filter(inputChannel = "inputChannel", outputChannel = "outputChannel")
public boolean multiConditionFilter(Message<?> message) {
    Integer priority = message.getHeaders().get("priority", Integer.class);
    String type = message.getHeaders().get("type", String.class);
    return priority != null && priority >= 5 && "ORDER".equals(type);
}
```

## Expression-Based Filtering

### SpEL Expressions

```java
@Bean
@Filter(inputChannel = "inputChannel", outputChannel = "outputChannel")
public MessageSelector expressionFilter() {
    return new ExpressionEvaluatingSelector(
        "headers['priority'] >= 5 && payload.type == 'ORDER'");
}
```

### XML Expressions

```xml
<int:filter input-channel="inputChannel"
            output-channel="outputChannel"
            expression="headers['priority'] > 5 and payload.type == 'ORDER'"/>
```

### XPath Expressions

```java
@Bean
@Filter(inputChannel = "xmlInputChannel", outputChannel = "outputChannel")
public MessageSelector xpathFilter() {
    return new XPathMessageSelector("/order/priority > 5");
}
```

## Custom Filters

### Custom MessageSelector

```java
public class OrderPriorityFilter implements MessageSelector {
    @Override
    public boolean accept(Message<?> message) {
        Integer priority = message.getHeaders().get("priority", Integer.class);
        String status = message.getHeaders().get("status", String.class);
        
        return priority != null && priority >= 5 
            && "NEW".equals(status);
    }
    
    @Override
    public String getComponentType() {
        return "custom.orderPriorityFilter";
    }
}

// Usage
@Bean
@Filter(inputChannel = "inputChannel", outputChannel = "outputChannel")
public MessageSelector orderPriorityFilter() {
    return new OrderPriorityFilter();
}
```

### Bean Method Filter

```java
@Service
public class OrderFilter {
    public boolean isHighPriority(Message<?> message) {
        Integer priority = message.getHeaders().get("priority", Integer.class);
        return priority != null && priority >= 8;
    }
}

// Usage
@Bean
@Filter(inputChannel = "inputChannel", outputChannel = "outputChannel",
        ref = "orderFilter", method = "isHighPriority")
public MessageSelector orderFilterRef() {
    return null;
}
```

## Filter Configuration

### With Discard Channel

```java
@Bean
@Filter(inputChannel = "inputChannel", 
        outputChannel = "acceptedChannel",
        discardChannel = "rejectedChannel",
        discardOnFailure = true)
public MessageSelector filter() {
    return message -> {
        // Filter logic
        return true;
    };
}
```

### With Advice

```java
@Bean
@Filter(inputChannel = "inputChannel", outputChannel = "outputChannel")
@Advice
public MessageSelector filterWithAdvice() {
    return message -> {
        try {
            // Filter logic
            return true;
        } catch (Exception e) {
            log.error("Filter error", e);
            return false;
        }
    };
}
```

### XML Configuration

```xml
<int:filter input-channel="inputChannel"
            output-channel="outputChannel"
            discard-channel="rejectedChannel"
            expression="headers['priority'] >= 5">
    <int:advice>
        <retry- advice-source="retryAdvice"/>
    </int:advice>
</int:filter>
```

## Best Practices

1. **Keep filters simple**: Single responsibility
2. **Use discard channels**: Don't silently drop messages
3. **Log filter decisions**: For debugging
4. **Test filters**: Verify filter logic
5. **Handle errors**: Configure error handling
6. **Document criteria**: Document filter logic
7. **Performance**: Consider filter performance
8. **Idempotency**: Make filters idempotent

## References

- [Spring Integration Filter](https://docs.spring.io/spring-integration/reference/core/filter.html)
