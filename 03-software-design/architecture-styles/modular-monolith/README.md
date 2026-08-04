# Modular Monolith

A modular monolith structures a single deployable unit into well-separated modules with clear boundaries, gaining modularity benefits without microservices complexity.

## Table of Contents

1. [Concepts](#concepts)
2. [Module Structure](#module-structure)
3. [Module Boundaries](#module-boundaries)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Modular Monolith?

Single deployable application with strict module boundaries. Modules communicate through well-defined APIs.

```
┌───────────────────────────────────────────┐
│              Single Deployment            │
├───────────┬───────────┬───────────────────┤
│  Module A │  Module B │    Module C       │
│ (orders)  │ (users)   │   (inventory)    │
├───────────┼───────────┼───────────────────┤
│  API      │  API      │    API            │
├───────────┼───────────┼───────────────────┤
│  Domain   │  Domain   │    Domain         │
├───────────┼───────────┼───────────────────┤
│  DB       │  DB       │    DB             │
└───────────┴───────────┴───────────────────┘
```

### Benefits

- **Modularity** - clear boundaries within monolith
- **Simplicity** - single deployment
- **Performance** - in-process calls
- **Evolvability** - can extract to microservices later

---

## Module Structure

### Module Layout

```
src/main/java/com/app/
├── orders/
│   ├── api/
│   │   └── OrderFacade.java
│   ├── domain/
│   │   └── Order.java
│   └── persistence/
│       └── OrderRepository.java
├── users/
│   ├── api/
│   │   └── UserFacade.java
│   ├── domain/
│   │   └── User.java
│   └── persistence/
│       └── UserRepository.java
└── inventory/
    ├── api/
    │   └── InventoryFacade.java
    ├── domain/
    │   └── Stock.java
    └── persistence/
        └── StockRepository.java
```

### Module API

```java
// orders/api/OrderFacade.java
public interface OrderFacade {
    OrderDto createOrder(CreateOrderRequest request);
    OrderDto getOrder(Long id);
}

// orders/api/OrderDto.java
public record OrderDto(Long id, Long userId, List<OrderItemDto> items, double total) {}
```

### Module Implementation

```java
// orders/domain/Order.java
public class Order {
    private Long id;
    private Long userId;
    private List<OrderItem> items;

    public static Order create(Long userId, List<OrderItem> items) {
        Order order = new Order();
        order.userId = userId;
        order.items = items;
        return order;
    }
}

// orders/persistence/OrderRepository.java
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {}

// orders/api/OrderFacadeImpl.java
@Service
public class OrderFacadeImpl implements OrderFacade {
    private final OrderRepository orderRepo;

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        Order order = Order.create(request.userId(), request.items());
        orderRepo.save(order);
        return toDto(order);
    }
}
```

---

## Module Boundaries

### Enforcing Boundaries

```java
// Module only exposes API, not internals
// GOOD: Other modules use OrderFacade
@Service
public class InventoryServiceImpl implements InventoryFacade {
    @Autowired private OrderFacade orderFacade;  // Uses API

    public void checkStock(Long orderId) {
        OrderDto order = orderFacade.getOrder(orderId);
        // ...
    }
}

// BAD: Other modules access internal classes
@Service
public class BadInventoryService {
    @Autowired private OrderRepository orderRepo;  // Direct access!
}
```

### Inter-Module Communication

```java
// Synchronous - through API
@Service
public class OrderServiceImpl implements OrderFacade {
    private final UserFacade userFacade;

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        UserDto user = userFacade.getUser(request.userId());
        // ...
    }
}

// Asynchronous - through events
@Service
public class OrderEventPublisher {
    @Autowired private ApplicationEventPublisher events;

    public void publishOrderCreated(Order order) {
        events.publishEvent(new OrderCreatedEvent(order.getId()));
    }
}

@Component
public class InventoryEventHandler {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        // React to order created
    }
}
```

---

## Best Practices

### Do

```java
// 1. Define public API per module
public interface OrderFacade {
    OrderDto createOrder(CreateOrderRequest request);
}

// 2. Use package structure to enforce boundaries
// orders.api - public
// orders.domain - package-private
// orders.persistence - package-private
```

### Don't

```java
// 1. Don't access other module's internals
// Use public API only

// 2. Don't share domain entities between modules
// Use DTOs

// 3. Don't create circular dependencies
// Module A → Module B → Module A is forbidden
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Modular Monolith** | Modules in single deployment |
| **Module API** | Public interface per module |
| **Boundaries** | Strict module isolation |
| **DTOs** | Data transfer between modules |
| **Events** | Async communication |
| **Simplicity** | Single deployment unit |
| **Evolvability** | Can extract to microservices |
| **vs Microservices** | Simpler operations, similar modularity |
