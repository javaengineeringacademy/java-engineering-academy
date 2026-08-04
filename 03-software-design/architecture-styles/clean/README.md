# Clean Architecture

Clean Architecture, coined by Robert C. Martin, organizes code in concentric layers with the domain at the center and dependencies pointing inward.

## Table of Contents

1. [Concepts](#concepts)
2. [Layers](#layers)
3. [Implementation](#implementation)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Clean Architecture?

Dependencies point inward. Inner layers know nothing about outer layers. The center is the domain.

```
┌─────────────────────────────┐
│         Frameworks          │
│  ┌─────────────────────┐   │
│  │      Adapters       │   │
│  │  ┌─────────────┐   │   │
│  │  │  Use Cases  │   │   │
│  │  │  ┌───────┐  │   │   │
│  │  │  │Entities│  │   │   │
│  │  │  └───────┘  │   │   │
│  │  └─────────────┘   │   │
│  └─────────────────────┘   │
└─────────────────────────────┘
```

### The Rule

Dependencies can only point inward. Nothing in an outer circle can know about something in an inner circle.

---

## Layers

### Entities (Innermost)

```java
// Domain entities - pure business objects
public class Order {
    private final Long id;
    private final List<OrderItem> items;
    private final OrderStatus status;

    public Order(Long id, List<OrderItem> items) {
        this.id = id;
        this.items = List.copyOf(items);
        this.status = OrderStatus.CREATED;
    }

    public Money calculateTotal() {
        return items.stream()
            .map(OrderItem::subtotal)
            .reduce(Money.ZERO, Money::add);
    }

    public Order approve() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Can only approve created orders");
        }
        return new Order(id, items, OrderStatus.APPROVED);
    }
}
```

### Use Cases

```java
// Application business rules
public class CreateOrderUseCase {
    private final OrderRepository orderRepo;
    private final PaymentGateway paymentGateway;

    public CreateOrder execute(CreateOrderRequest request) {
        Order order = Order.create(request.getItems());
        Money total = order.calculateTotal();
        paymentGateway.charge(request.getPaymentMethod(), total);
        orderRepo.save(order);
        return order;
    }
}
```

### Interface Adapters

```java
// Controllers, presenters, gateways
@RestController
public class OrderController {
    private final CreateOrderUseCase createOrder;

    @PostMapping("/orders")
    public OrderDto create(@RequestBody OrderRequest request) {
        Order order = createOrder.execute(toUseCaseRequest(request));
        return toDto(order);
    }
}

// Repository implementation
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepo;

    @Override
    public void save(Order order) {
        jpaRepo.save(toEntity(order));
    }
}
```

### Frameworks (Outermost)

```java
// Spring Boot application
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// Configuration
@Configuration
public class AppConfig {
    @Bean
    public CreateOrderUseCase createOrderUseCase(
            OrderRepository repo, PaymentGateway gateway) {
        return new CreateOrderUseCase(repo, gateway);
    }
}
```

---

## Implementation

### Dependency Rule

```java
// GOOD: Use case depends on repository interface
public class CreateOrderUseCase {
    private final OrderRepository repo;  // Interface in domain
}

// BAD: Use case depends on JPA
public class CreateOrderUseCase {
    private final OrderJpaRepository repo;  // Framework in domain!
}
```

### Boundaries

```java
// Entities - no imports from outer layers
package com.app.domain;
// Only JDK imports allowed

// Use cases - depend only on entities
package com.app.usecase;
import com.app.domain.Order;  // OK

// Adapters - depend on use cases
package com.app.adapter;
import com.app.usecase.CreateOrderUseCase;  // OK

// Frameworks - depend on everything
package com.app;
import com.app.adapter.OrderController;  // OK
```

---

## Best Practices

### Do

```java
// 1. Keep entities pure
public class Order {
    // No Spring annotations
    // No JPA annotations
    // Pure Java
}

// 2. Define boundaries with interfaces
public interface OrderRepository {
    Order findById(Long id);
}
```

### Don't

```java
// 1. Don't let entities know about persistence
@Entity  // BAD on domain entity
public class Order { ... }

// 2. Don't skip layers
// Controller → Use Case → Repository
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Entities** | Core business objects |
| **Use Cases** | Application business rules |
| **Adapters** | Interface with outside world |
| **Frameworks** | Outermost layer |
| **Dependency Rule** | Dependencies point inward |
| **Independence** | Core doesn't know about frameworks |
| **Testability** | Test inner layers easily |
| **Robert C. Martin** | Creator of Clean Architecture |
