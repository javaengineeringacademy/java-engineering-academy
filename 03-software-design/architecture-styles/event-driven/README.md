# Event-Driven Architecture

Event-Driven Architecture uses events as the primary mechanism for communication between components. Producers emit events, and consumers react to them.

## Table of Contents

1. [Concepts](#concepts)
2. [Event Types](#event-types)
3. [Publish/Subscribe](#publishsubscribe)
4. [Event Sourcing](#event-sourcing)
5. [Best Practices](#best-practices)
6. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Event-Driven?

Components communicate by producing and consuming events, not by direct method calls.

```
Producer ──▶ Event ──▶ Consumer1
                   ──▶ Consumer2
                   ──▶ Consumer3
```

### Benefits

- **Loose Coupling** - producers don't know consumers
- **Scalability** - add consumers without changing producers
- **Flexibility** - multiple reactions to same event
- **Auditability** - events provide natural audit trail

---

## Event Types

### Domain Events

```java
public sealed interface OrderEvent
    permits OrderCreated, OrderShipped, OrderDelivered {

    record OrderCreated(String orderId, Instant timestamp) implements OrderEvent {}
    record OrderShipped(String orderId, String trackingNumber, Instant timestamp) implements OrderEvent {}
    record OrderDelivered(String orderId, Instant timestamp) implements OrderEvent {}
}

// Publishing events
@Service
public class OrderService {
    private final ApplicationEventPublisher publisher;

    public Order createOrder(CreateOrderRequest request) {
        Order order = orderRepo.save(Order.create(request));
        publisher.publishEvent(new OrderCreated(order.getId(), Instant.now()));
        return order;
    }
}
```

### Integration Events

```java
// Events that cross service boundaries
public record PaymentProcessedEvent(
    String orderId,
    String paymentId,
    double amount,
    Instant timestamp
) {}

public record InventoryReservedEvent(
    String orderId,
    List<String> productIds,
    Instant timestamp
) {}
```

---

## Publish/Subscribe

### In-Process Events

```java
// Spring events
@Component
public class OrderEventHandler {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        inventoryService.reserveStock(event.orderId());
    }

    @EventListener
    @Async
    public void handleOrderCreatedAsync(OrderCreatedEvent event) {
        emailService.sendConfirmation(event.orderId());
    }
}
```

### Message Queue Events

```java
// Kafka producer
@Service
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafka;

    public void publishOrderCreated(Order order) {
        kafka.send("order-events", new OrderCreatedEvent(order.getId()));
    }
}

// Kafka consumer
@Component
public class OrderEventConsumer {
    @KafkaListener(topics = "order-events")
    public void handleOrderCreated(OrderCreatedEvent event) {
        inventoryService.reserveStock(event.orderId());
    }
}
```

### Event Router

```java
public class EventRouter {
    private final Map<Class<?>, List<EventHandler<?>>> handlers = new HashMap<>();

    public <T> void register(Class<T> eventType, EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
    }

    @SuppressWarnings("unchecked")
    public <T> void route(T event) {
        List<EventHandler<?>> eventHandlers = handlers.getOrDefault(event.getClass(), List.of());
        for (EventHandler<?> handler : eventHandlers) {
            ((EventHandler<T>) handler).handle(event);
        }
    }
}
```

---

## Event Sourcing

### Storing Events Instead of State

```java
// Event store
public class OrderEventStore {
    private final Map<String, List<OrderEvent>> events = new HashMap<>();

    public void append(String orderId, OrderEvent event) {
        events.computeIfAbsent(orderId, k -> new ArrayList<>()).add(event);
    }

    public List<OrderEvent> getEvents(String orderId) {
        return events.getOrDefault(orderId, List.of());
    }

    // Rebuild state from events
    public Order rebuild(String orderId) {
        return getEvents(orderId).stream()
            .reduce(new Order(orderId),
                (order, event) -> order.apply(event),
                (a, b) -> b);
    }
}
```

---

## Best Practices

### Do

```java
// 1. Use meaningful event names
public record OrderCreatedEvent(String orderId, Instant timestamp) {}

// 2. Make events immutable
public record OrderCreatedEvent(
    String orderId,
    Instant timestamp
) {}  // Record = immutable

// 3. Include event metadata
public record DomainEvent<T>(
    String eventId,
    String aggregateId,
    Instant timestamp,
    T payload
) {}
```

### Don't

```java
// 1. Don't create circular event chains
// A → B → C → A

// 2. Don't rely on event ordering unless guaranteed
// Kafka guarantees per partition, not globally
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Event-Driven** | Communication via events |
| **Producer** | Emits events |
| **Consumer** | Reacts to events |
| **Loose Coupling** | Producers don't know consumers |
| **Pub/Sub** | Many consumers per event |
| **Message Queue** | Async event delivery |
| **Event Sourcing** | Store events, not state |
| **Audit Trail** | Natural logging of changes |
