# Microservices

Microservices architecture structures an application as a collection of small, autonomous services, each running in its own process and communicating via lightweight mechanisms.

## Table of Contents

1. [Concepts](#concepts)
2. [Bounded Contexts](#bounded-contexts)
3. [Communication](#communication)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What are Microservices?

Small, independent services that communicate over network, each owning its data and business logic.

```
┌──────────┐  ┌──────────┐  ┌──────────┐
│ Service A│  │ Service B│  │ Service C│
│ (DB A)   │  │ (DB B)   │  │ (DB C)   │
└────┬─────┘  └────┬─────┘  └────┬─────┘
     │             │             │
     └─────────────┼─────────────┘
                   │
              API Gateway
```

### Benefits

- **Independent Deployment** - deploy services independently
- **Technology Diversity** - different tech per service
- **Scalability** - scale individual services
- **Fault Isolation** - failure in one service doesn't kill others

---

## Bounded Contexts

### Identifying Services

```java
// Each service owns its domain
// Order Service
@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final PaymentClient paymentClient;

    public Order createOrder(CreateOrderRequest request) {
        Order order = Order.create(request);
        orderRepo.save(order);
        paymentClient.processPayment(order.getId(), order.getTotal());
        return order;
    }
}

// Inventory Service
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepo;

    public boolean checkStock(String productId, int quantity) {
        return inventoryRepo.findByProductId(productId)
            .map(inv -> inv.getQuantity() >= quantity)
            .orElse(false);
    }
}

// User Service
@Service
public class UserService {
    private final UserRepository userRepo;

    public UserDto getUser(Long id) {
        return userRepo.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
```

### Service Boundaries

```java
// Each service has its own database
// Order Service DB
CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,  // References User Service, not local
    total DECIMAL
);

// User Service DB
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255)
);
// No join across service databases!
```

---

## Communication

### Synchronous (REST)

```java
// Service-to-service REST calls
@Service
public class OrderService {
    private final WebClient userClient;

    public Order createOrder(CreateOrderRequest request) {
        // Call User Service to validate user
        UserDto user = userClient.get()
            .uri("http://user-service/users/{id}", request.userId())
            .retrieve()
            .bodyToMono(UserDto.class)
            .block();

        // Create order
        Order order = Order.create(request, user);
        return orderRepo.save(order);
    }
}
```

### Asynchronous (Events)

```java
// Publish events
@Service
public class OrderService {
    @Autowired private KafkaTemplate<String, Object> kafka;

    public Order createOrder(CreateOrderRequest request) {
        Order order = orderRepo.save(Order.create(request));
        kafka.send("order-events", new OrderCreatedEvent(order.getId()));
        return order;
    }
}

// Consume events
@KafkaListener(topics = "order-events")
public void handleOrderEvent(OrderCreatedEvent event) {
    inventoryService.reserveStock(event.orderId());
}
```

---

## Best Practices

### Do

```java
// 1. Design for failure
public Order getOrder(Long id) {
    try {
        return orderRepo.findById(id).orElseThrow();
    } catch (Exception e) {
        throw new ServiceUnavailableException();
    }
}

// 2. Use circuit breakers
@CircuitBreaker(name = "userService", fallbackMethod = "getUserFallback")
public UserDto getUser(Long id) {
    return userClient.get().uri("/users/{id}", id).retrieve()
        .bodyToMono(UserDto.class).block();
}
```

### Don't

```java
// 1. Don't create distributed monolith
// Services should be independently deployable

// 2. Don't share databases between services
// Each service owns its data
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Microservices** | Small, autonomous services |
| **Bounded Context** | Service owns its domain |
| **Independent Deployment** | Deploy services separately |
| **Communication** | REST, events, messaging |
| **Database per Service** | Each service owns its data |
| **Fault Isolation** | Failure contained to service |
| **Scalability** | Scale individual services |
| **Complexity** | Operational overhead |
