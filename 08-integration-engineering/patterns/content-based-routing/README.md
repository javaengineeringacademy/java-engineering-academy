# Integration Patterns - Content-Based Routing

## Overview

Content-Based Routing (CBR) directs messages to different destinations based on message content, headers, or other criteria.

## Table of Contents

1. [CBR Pattern](#cbr-pattern)
2. [Implementation](#implementation)
3. [Routing Strategies](#routing-strategies)
4. [Examples](#examples)

## CBR Pattern

### Pattern Structure

```
                    ┌──> Channel A (if content=A)
Message ─── Router ─┼──> Channel B (if content=B)
                    └──> Channel C (if content=C)
```

### When to Use

- Different processing for different message types
- Message filtering and routing
- Load balancing across services

## Implementation

### Camel Implementation

```java
from("direct:start")
    .choice()
        .when(header("type").isEqualTo("ORDER"))
            .to("jms:queue:orders")
        .when(header("type").isEqualTo("INVOICE"))
            .to("jms:queue:invoices")
        .when(simple("${body.total} > 1000"))
            .to("jms:queue:high-value")
        .otherwise()
            .to("jms:queue:default")
    .end();
```

### Spring Integration

```java
@Router(inputChannel = "inputChannel")
public String route(Message<?> message) {
    String type = message.getHeaders().get("type", String.class);
    
    switch (type) {
        case "ORDER": return "orderChannel";
        case "INVOICE": return "invoiceChannel";
        default: return "defaultChannel";
    }
}
```

### MuleSoft

```xml
<choice>
    <when expression="#[payload.type == 'ORDER']">
        <flow-ref name="processOrder"/>
    </when>
    <when expression="#[payload.type == 'INVOICE']">
        <flow-ref name="processInvoice"/>
    </when>
    <otherwise>
        <flow-ref name="processDefault"/>
    </otherwise>
</choice>
```

## Routing Strategies

### Header-Based

```java
.choice()
    .when(header("priority").isEqualTo("HIGH"))
        .to("jms:queue:high-priority")
    .otherwise()
        .to("jms:queue:normal-priority")
.end();
```

### Content-Based

```java
.choice()
    .when(simple("${body.type} == 'ORDER'"))
        .to("jms:queue:orders")
    .when(simple("${body.amount} > 1000"))
        .to("jms:queue:large-amounts")
.end();
```

### XPath-Based

```java
.choice()
    .when(xpath("/order/priority > 5"))
        .to("jms:queue:high-priority")
    .otherwise()
        .to("jms:queue:normal-priority")
.end();
```

## Best Practices

1. **Keep routing simple**: Avoid complex conditions
2. **Use default channel**: Always have a fallback
3. **Document routes**: Document routing logic
4. **Test routes**: Verify routing decisions
5. **Monitor routing**: Track routing metrics
6. **Error handling**: Handle routing errors
7. **Performance**: Consider routing performance
8. **Logging**: Log routing decisions

## References

- [Content-Based Router](https://www.enterpriseintegrationpatterns.com/patterns/messaging/ContentBasedRouter.html)
