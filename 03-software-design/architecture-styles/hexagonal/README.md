# Hexagonal Architecture

Hexagonal architecture (Ports and Adapters) isolates the application's core logic from external concerns like databases, UI, or messaging through well-defined ports.

## Table of Contents

1. [Concepts](#concepts)
2. [Ports](#ports)
3. [Adapters](#adapters)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Hexagonal Architecture?

Core business logic is surrounded by ports (interfaces) and adapters (implementations), making the core independent of external systems.

```
          ┌─────────────────────────┐
  REST ──▶│  Inbound Port           │
          │  ┌─────────────────┐    │
          │  │  Domain/Service │    │
          │  └─────────────────┘    │
          │  Outbound Port ◀── DB   │
          └─────────────────────────┘
```

### Benefits

- **Testability** - core testable without infrastructure
- **Flexibility** - swap adapters easily
- **Independence** - core doesn't depend on frameworks

---

## Ports

### Inbound Ports (Use Cases)

```java
// Use case interface
public interface CreateOrderUseCase {
    OrderDto execute(CreateOrderRequest request);
}

public interface GetOrderUseCase {
    OrderDto execute(Long orderId);
}
```

### Outbound Ports (Infrastructure)

```java
// Repository port
public interface OrderRepository {
    Order findById(Long id);
    Order save(Order order);
}

// Event publisher port
public interface OrderEventPublisher {
    void publishOrderCreated(Order order);
}

// Payment port
public interface PaymentPort {
    PaymentResult processPayment(String orderId, double amount);
}
```

---

## Adapters

### Inbound Adapters

```java
// REST adapter
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final CreateOrderUseCase createOrder;

    public OrderController(CreateOrderUseCase createOrder) {
        this.createOrder = createOrder;
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(createOrder.execute(request));
    }
}

// CLI adapter
public class OrderCliAdapter {
    private final GetOrderUseCase getOrder;

    public void showOrder(Long id) {
        OrderDto order = getOrder.execute(id);
        System.out.println("Order: " + order);
    }
}
```

### Outbound Adapters

```java
// JPA adapter
@Repository
public class JpaOrderRepository implements OrderRepository {
    private final OrderJpaRepository jpaRepo;

    @Override
    public Order findById(Long id) {
        return jpaRepo.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public Order save(Order order) {
        return toDomain(jpaRepo.save(toEntity(order)));
    }
}

// Kafka adapter
@Service
public class KafkaOrderEventPublisher implements OrderEventPublisher {
    private final KafkaTemplate<String, Object> kafka;

    @Override
    public void publishOrderCreated(Order order) {
        kafka.send("order-events", new OrderCreatedEvent(order.getId()));
    }
}
```

### Application Service (Core)

```java
@Service
public class OrderService implements CreateOrderUseCase {
    private final OrderRepository orderRepo;
    private final PaymentPort paymentPort;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepo,
                        PaymentPort paymentPort,
                        OrderEventPublisher eventPublisher) {
        this.orderRepo = orderRepo;
        this.paymentPort = paymentPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public OrderDto execute(CreateOrderRequest request) {
        Order order = Order.create(request);
        paymentPort.processPayment(order.getId(), order.getTotal());
        orderRepo.save(order);
        eventPublisher.publishOrderCreated(order);
        return toDto(order);
    }
}
```

---

## Best Practices

### Do

```java
// 1. Core depends on ports (interfaces)
public class OrderService {
    private final OrderRepository repository;  // Port, not implementation
}

// 2. Adapters implement ports
public class JpaOrderRepository implements OrderRepository { ... }
```

### Don't

```java
// 1. Don't let core depend on frameworks
// No @Entity, @Repository in domain

// 2. Don't let adapters contain business logic
// Adapters only translate
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Ports** | Interfaces for core logic |
| **Adapters** | Implementations connecting to outside |
| **Inbound** | Controllers, CLI (drive the app) |
| **Outbound** | DB, messaging (driven by app) |
| **Core Independence** | No framework dependencies |
| **Testability** | Test core without infrastructure |
| **Swappability** | Change adapters easily |
