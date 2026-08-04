# SRP - Single Responsibility Principle

## Overview

A class should have only one reason to change, meaning it should have only one job or responsibility.

## Violations

### Multiple Responsibilities
```java
// BAD - Multiple responsibilities
public class OrderService {
    public void createOrder(Order order) {
        validateOrder(order);
        saveOrder(order);
        sendConfirmationEmail(order);
        updateInventory(order);
        processPayment(order);
        generateReport(order);
    }
    
    private void validateOrder(Order order) { /* validation */ }
    private void saveOrder(Order order) { /* persistence */ }
    private void sendConfirmationEmail(Order order) { /* email */ }
    private void updateInventory(Order order) { /* inventory */ }
    private void processPayment(Order order) { /* payment */ }
    private void generateReport(Order order) { /* reporting */ }
}
```

### Single Responsibility
```java
// GOOD - Each class has one reason to change
public class OrderValidator {
    public void validate(Order order) {
        validateItems(order.getItems());
        validateAddress(order.getShippingAddress());
        validatePayment(order.getPayment());
    }
}

public class OrderRepository {
    public Order save(Order order) {
        return entityManager.merge(order);
    }
}

public class OrderNotificationService {
    public void sendConfirmation(Order order) {
        emailService.send(buildConfirmationEmail(order));
    }
}

public class InventoryService {
    public void updateStock(Order order) {
        order.getItems().forEach(item -> 
            stockRepository.decrement(item.getSku(), item.getQuantity()));
    }
}

public class PaymentProcessor {
    public PaymentResult process(Order order, PaymentMethod payment) {
        return paymentGateway.charge(order.getTotal(), payment);
    }
}
```

### Benefits

| Before | After |
|--------|-------|
| 1 class, 6 reasons to change | 5 classes, 1 reason each |
| Hard to test | Easy to test |
| Tight coupling | Loose coupling |
| Hard to reuse | Reusable components |
| Merge conflicts | Parallel development |

## Identifying Responsibilities

1. What does this class do?
2. Why would this class change?
3. Who is the client of this class?
4. Can you describe the responsibility in one sentence?

## Best Practices

1. Identify and isolate responsibilities
2. Extract classes when responsibilities grow
3. Name classes by their responsibility
4. Keep classes focused and small
5. Use composition to combine responsibilities
6. Test each responsibility independently
7. Document the class's single purpose
8. Refactor when responsibilities merge
