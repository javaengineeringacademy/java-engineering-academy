# CQRS Pattern

CQRS (Command Query Responsibility Segregation) separates read and write operations into different models, optimizing each for its specific purpose.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic CQRS](#basic-cqrs)
3. [Separate Models](#separate-models)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is CQRS?

Separates commands (writes) from queries (reads) with different models for each.

```
Command ──▶ Write Model ──▶ Write DB
                               │
Query ──▶ Read Model ◀── Read DB (synced)
```

### When to Use

- Read and write workloads differ significantly
- Need to scale reads and writes independently
- Complex domain with different read/write models
- Event sourcing integration

---

## Basic CQRS

### Command Side

```java
// Commands
public record CreateOrderCommand(String userId, List<OrderItemDto> items) {}
public record ShipOrderCommand(String orderId, String trackingNumber) {}

// Command handler
@Component
public class OrderCommandHandler {
    private final OrderRepository orderRepo;
    private final EventPublisher eventPublisher;

    @Transactional
    public String handle(CreateOrderCommand command) {
        Order order = Order.create(command.userId(), command.items());
        orderRepo.save(order);
        eventPublisher.publish(new OrderCreatedEvent(order.getId()));
        return order.getId();
    }

    @Transactional
    public void handle(ShipOrderCommand command) {
        Order order = orderRepo.findById(command.orderId())
            .orElseThrow();
        order.ship(command.trackingNumber());
        orderRepo.save(order);
        eventPublisher.publish(new OrderShippedEvent(
            command.orderId(), command.trackingNumber()));
    }
}
```

### Query Side

```java
// Query models (denormalized for reads)
public record OrderSummary(String id, String status, double total, Instant createdAt) {}
public record OrderDetail(String id, String status, List<OrderItemDto> items,
                          double total, String trackingNumber) {}

// Query handler
@Component
public class OrderQueryHandler {
    private final OrderReadRepository readRepo;

    public List<OrderSummary> handle(GetOrdersQuery query) {
        return readRepo.findByUserId(query.userId());
    }

    public OrderDetail handle(GetOrderDetailQuery query) {
        return readRepo.findById(query.orderId())
            .orElseThrow(() -> new NotFoundException("Order not found"));
    }
}

// Read model repository (separate from write)
@Repository
public interface OrderReadRepository {
    List<OrderSummary> findByUserId(String userId);
    Optional<OrderDetail> findById(String id);
}
```

### Syncing Read Model

```java
// Event handler syncs write → read
@Component
public class OrderSyncHandler {
    private final OrderReadRepository readRepo;

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Write to read model
        readRepo.save(new OrderSummary(
            event.orderId(), "CREATED", 0.0, event.timestamp()));
    }

    @EventListener
    public void handleOrderShipped(OrderShippedEvent event) {
        readRepo.updateStatus(event.orderId(), "SHIPPED", event.trackingNumber());
    }
}
```

---

## Separate Models

### Write Model

```java
// Rich domain model for writes
public class Order {
    private String id;
    private String userId;
    private List<OrderItem> items;
    private OrderStatus status;
    private String trackingNumber;

    // Business logic
    public void ship(String tracking) {
        if (status != OrderStatus.PAID) {
            throw new IllegalStateException("Order must be paid before shipping");
        }
        this.status = OrderStatus.SHIPPED;
        this.trackingNumber = tracking;
    }
}
```

### Read Model

```java
// Flat, denormalized for reads
public record OrderSummary(
    String id,
    String userName,    // Joined from user service
    String status,
    int itemCount,
    double total,
    Instant createdAt
) {}

// Optimized queries
@Repository
public interface OrderReadRepository {
    @Query("SELECT new com.app.read.OrderSummary(" +
           "o.id, u.name, o.status, SIZE(o.items), o.total, o.createdAt) " +
           "FROM Order o JOIN User u ON o.userId = u.id " +
           "WHERE o.userId = :userId")
    List<OrderSummary> findByUserId(String userId);
}
```

---

## Best Practices

### Do

```java
// 1. Keep read and write models separate
public class WriteModel { ... }
public record ReadModel(...) {}

// 2. Optimize read model for queries
// Denormalize, add indexes, pre-compute

// 3. Use events to sync models
@EventListener
public void onOrderCreated(OrderCreatedEvent event) {
    updateReadModel(event);
}
```

### Don't

```java
// 1. Don't use same model for reads and writes
// That defeats the purpose of CQRS

// 2. Don't over-complicate simple CRUD
// CQRS adds complexity - use when justified
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **CQRS** | Separate read and write models |
| **Commands** | Write operations |
| **Queries** | Read operations |
| **Write Model** | Rich domain logic |
| **Read Model** | Denormalized for fast reads |
| **Sync** | Events keep models in sync |
| **Scalability** | Scale reads and writes independently |
| **Complexity** | Use when justified |
| **Event Sourcing** | Natural fit with CQRS |
