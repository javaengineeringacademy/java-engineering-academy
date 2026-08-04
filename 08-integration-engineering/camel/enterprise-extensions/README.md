# Apache Camel - Enterprise Extensions

## Overview

Apache Camel provides enterprise extensions implementing Enterprise Integration Patterns (EIP) with support for transactions, clustering, and monitoring.

## Table of Contents

1. [EIP Support](#eip-support)
2. [Splitter](#splitter)
3. [Aggregator](#aggregator)
4. [Router](#router)
5. [Resequencer](#resequencer)
6. [Wire Tap](#wire-tap)
7. [Routing Slip](#routing-slip)
8. [Dead Letter Channel](#dead-letter-channel)
9. [Transactions](#transactions)

## EIP Support

### Pattern Implementation Map

| Pattern | Camel Method | Description |
|---------|-------------|-------------|
| Content-Based Router | `.choice().when()` | Route by content |
| Message Filter | `.filter()` | Selective processing |
| Splitter | `.split()` | Break messages |
| Aggregator | `.aggregate()` | Combine messages |
| Resequencer | `.resequence()` | Order messages |
| Content Enricher | `.enrich()` | Add data |
| Wire Tap | `.wireTap()` | Monitor messages |
| Routing Slip | `.routingSlip()` | Dynamic routing |
| Dead Letter Channel | `.onException()` | Error handling |

## Splitter

### Basic Splitting

```java
from("direct:start")
    .split(body().cast(List.class))
    .log("Item: ${body}")
    .to("direct:end");

from("direct:start")
    .split(xpath("/order/items/item"))
    .log("Item: ${body}")
    .to("direct:end");
```

### Split with Aggregation

```java
from("direct:start")
    .split(xpath("/order/items/item"))
        .process(new ItemProcessor())
    .aggregate(header("orderId"), new ItemAggregator())
        .completionSize(3)
    .to("direct:end");
```

## Aggregator

### Basic Aggregation

```java
from("direct:start")
    .aggregate(header("correlationId"), new OrderAggregator())
    .completionSize(3)
    .completionTimeout(5000)
    .to("direct:end");

public class OrderAggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange old, Exchange new) {
        if (old == null) return new;
        List<Order> orders = old.getIn().getBody(List.class);
        if (orders == null) orders = new ArrayList<>();
        orders.add(new.getIn().getBody(Order.class));
        old.getIn().setBody(orders);
        return old;
    }
}
```

## Router

### Content-Based Router

```java
from("direct:start")
    .choice()
        .when(header("type").isEqualTo("order"))
            .to("jms:queue:orders")
        .when(header("type").isEqualTo("invoice"))
            .to("jms:queue:invoices")
        .otherwise()
            .to("jms:queue:default")
    .end();
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

### Recipient List

```java
from("direct:start")
    .recipientList(header("recipients"))
    .parallelProcessing()
    .stopOnException();
```

## Resequencer

```java
from("direct:start")
    .resequence(header("sequenceNumber"))
    .batch()
    .capacity(100)
    .timeout(5000)
    .to("direct:end");

from("direct:start")
    .resequence(header("sequenceNumber"))
    .stream()
    .timeout(5000)
    .allowReordering()
    .to("direct:end");
```

## Wire Tap

```java
from("direct:start")
    .wireTap("jms:queue:audit")
    .log("Processing: ${body}")
    .to("direct:end");

from("direct:start")
    .wireTap("direct:monitor")
    .log("Message tapped")
    .to("direct:end");
```

## Routing Slip

```java
from("direct:start")
    .routingSlip(header("routingSlip"));

from("direct:start")
    .routingSlip(simple("jms:queue:${header.type}-queue"));
```

## Dead Letter Channel

```java
from("direct:start")
    .onException(Exception.class)
        .maximumRedeliveries(3)
        .redeliveryDelay(1000)
        .handled(true)
        .to("jms:queue:dead-letter")
    .end()
    .to("direct:end");
```

## Transactions

### JMS Transactions

```java
from("jms:queue:orders?transacted=true")
    .transacted()
    .process(new OrderProcessor())
    .to("jms:queue:processed");
```

### Database Transactions

```java
from("direct:start")
    .transacted("PROPAGATION_REQUIRED")
    .to("jdbc:dataSource")
    .to("direct:end");
```

## Best Practices

1. **Use appropriate patterns**: Match pattern to use case
2. **Error handling**: Configure dead letter channels
3. **Transactions**: Use transactions for reliability
4. **Monitoring**: Use wire tap for auditing
5. **Testing**: Test patterns with mock endpoints
6. **Performance**: Consider async processing
7. **Documentation**: Document pattern usage

## References

- [Camel EIP](https://camel.apache.org/eip/)
- [Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/)
