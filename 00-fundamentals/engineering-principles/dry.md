# DRY Principle (Don't Repeat Yourself)

Comprehensive guide to understanding and applying the DRY principle, identifying duplication, and creating effective abstractions.

---

## Table of Contents

1. [Overview](#overview)
2. [Why DRY Matters](#why-dry-matters)
3. [Types of Duplication](#types-of-duplication)
4. [Identifying Duplication](#identifying-duplication)
5. [Abstraction Strategies](#abstraction-strategies)
6. [DRY in Practice](#dry-in-practice)
7. [Best Practices](#best-practices)
8. [Common Mistakes](#common-mistakes)
9. [Key Takeaways](#key-takeaways)

---

## Overview

DRY (Don't Repeat Yourself) is a principle of software development aimed at reducing repetition of software patterns. It states that "every piece of knowledge must have a single, unambiguous, authoritative representation within a system."

### The DRY Principle

- **Every piece of knowledge** must have a single, unambiguous representation
- **Every piece of logic** must exist in one place
- **Every piece of data** must exist in one place
- **Every piece of configuration** must exist in one place

### What DRY Covers

- **Code duplication**: Same logic in multiple places
- **Knowledge duplication**: Same information in multiple places
- **Process duplication**: Same steps in multiple places
- **Data duplication**: Same data in multiple places

---

## Why DRY Matters

### Benefits

**1. Maintainability**
- Change logic in one place
- Fix bugs in one place
- Update configuration in one place
- Reduce risk of inconsistencies

**2. Readability**
- Code is more concise
- Logic is centralized
- Intent is clearer
- Easier to understand

**3. Testability**
- Test logic once
- Ensure consistency
- Reduce test duplication
- Focus on edge cases

**4. Reliability**
- Fewer bugs from inconsistencies
- Consistent behavior
- Predictable results
- Easier debugging

### Real-World Impact

**Without DRY**
```java
// Same validation logic in multiple places
public class OrderService {
    public void processOrder(Order order) {
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have items");
        }
        if (order.getTotal() <= 0) {
            throw new IllegalArgumentException("Order total must be positive");
        }
        // Process order
    }
    
    public void updateOrder(Order order) {
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have items");
        }
        if (order.getTotal() <= 0) {
            throw new IllegalArgumentException("Order total must be positive");
        }
        // Update order
    }
}
```

**With DRY**
```java
public class OrderService {
    public void processOrder(Order order) {
        validateOrder(order);
        // Process order
    }
    
    public void updateOrder(Order order) {
        validateOrder(order);
        // Update order
    }
    
    private void validateOrder(Order order) {
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have items");
        }
        if (order.getTotal() <= 0) {
            throw new IllegalArgumentException("Order total must be positive");
        }
    }
}
```

---

## Types of Duplication

### 1. Code Duplication

**Same logic in multiple places**
```java
// Duplicated code
public void processOrder(Order order) {
    double discount = 0;
    if (order.getCustomer().isPremium()) {
        discount = order.getTotal() * 0.1;
    }
    double finalAmount = order.getTotal() - discount;
    paymentService.charge(order.getCustomer(), finalAmount);
}

public void processRefund(Refund refund) {
    double discount = 0;
    if (refund.getCustomer().isPremium()) {
        discount = refund.getTotal() * 0.1;
    }
    double finalAmount = refund.getTotal() - discount;
    paymentService.refund(refund.getCustomer(), finalAmount);
}
```

### 2. Knowledge Duplication

**Same information in multiple places**
```java
// Duplicated knowledge
public class User {
    private String email;
    
    public boolean isValidEmail() {
        return email != null && email.contains("@");
    }
}

public class EmailValidator {
    public boolean isValid(String email) {
        return email != null && email.contains("@");
    }
}
```

### 3. Process Duplication

**Same steps in multiple places**
```java
// Duplicated process
public void createOrder(Order order) {
    validateItems(order);
    calculateTotal(order);
    processPayment(order);
    sendConfirmation(order);
}

public void recreateOrder(Order order) {
    validateItems(order);
    calculateTotal(order);
    processPayment(order);
    sendConfirmation(order);
}
```

### 4. Data Duplication

**Same data in multiple places**
```java
// Duplicated data
public class Order {
    private double total;
    private double tax;
    private double discount;
    private double finalAmount; // Calculated from other fields
}

// Better: Calculate when needed
public class Order {
    private double total;
    private double taxRate;
    private double discountRate;
    
    public double getFinalAmount() {
        return total * (1 + taxRate) * (1 - discountRate);
    }
}
```

---

## Identifying Duplication

### Warning Signs

1. **Copy-paste code**: Code that looks similar
2. **Similar method signatures**: Methods with same parameters
3. **Parallel hierarchies**: Similar class structures
4. **Magic numbers**: Same numbers in multiple places
5. **String literals**: Same strings in multiple places

### Detection Techniques

**Manual Review**
- Code reviews
- Pair programming
- Refactoring sessions

**Automated Tools**
- Copy-paste detectors
- Code analysis tools
- Refactoring tools

**Metrics**
- Code coverage
- Duplication percentage
- Complexity metrics

### Duplication Checklist

- [ ] Same logic in multiple methods
- [ ] Same validation in multiple places
- [ ] Same calculation in multiple places
- [ ] Same string literals in multiple places
- [ ] Same configuration in multiple places
- [ ] Same error handling in multiple places
- [ ] Same database queries in multiple places

---

## Abstraction Strategies

### 1. Extract Method

**Before**
```java
public void processOrder(Order order) {
    // Validation
    if (order.getItems().isEmpty()) {
        throw new IllegalArgumentException("Order must have items");
    }
    if (order.getTotal() <= 0) {
        throw new IllegalArgumentException("Order total must be positive");
    }
    
    // Calculation
    double discount = 0;
    if (order.getCustomer().isPremium()) {
        discount = order.getTotal() * 0.1;
    }
    double finalAmount = order.getTotal() - discount;
    
    // Payment
    paymentService.charge(order.getCustomer(), finalAmount);
}
```

**After**
```java
public void processOrder(Order order) {
    validateOrder(order);
    double finalAmount = calculateFinalAmount(order);
    processPayment(order.getCustomer(), finalAmount);
}

private void validateOrder(Order order) {
    if (order.getItems().isEmpty()) {
        throw new IllegalArgumentException("Order must have items");
    }
    if (order.getTotal() <= 0) {
        throw new IllegalArgumentException("Order total must be positive");
    }
}

private double calculateFinalAmount(Order order) {
    double discount = calculateDiscount(order);
    return order.getTotal() - discount;
}

private double calculateDiscount(Order order) {
    if (order.getCustomer().isPremium()) {
        return order.getTotal() * 0.1;
    }
    return 0;
}

private void processPayment(Customer customer, double amount) {
    paymentService.charge(customer, amount);
}
```

### 2. Extract Class

**Before**
```java
public class Order {
    private List<Item> items;
    private Customer customer;
    
    public void validate() {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order must have items");
        }
        if (getTotal() <= 0) {
            throw new IllegalArgumentException("Order total must be positive");
        }
    }
    
    public double calculateTotal() {
        double total = 0;
        for (Item item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
    
    public double calculateDiscount() {
        if (customer.isPremium()) {
            return calculateTotal() * 0.1;
        }
        return 0;
    }
}
```

**After**
```java
public class Order {
    private List<Item> items;
    private Customer customer;
    private OrderValidator validator;
    private OrderCalculator calculator;
    
    public Order(OrderValidator validator, OrderCalculator calculator) {
        this.validator = validator;
        this.calculator = calculator;
    }
    
    public void validate() {
        validator.validate(this);
    }
    
    public double calculateTotal() {
        return calculator.calculateTotal(this);
    }
    
    public double calculateDiscount() {
        return calculator.calculateDiscount(this);
    }
}

public class OrderValidator {
    public void validate(Order order) {
        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have items");
        }
        if (order.calculateTotal() <= 0) {
            throw new IllegalArgumentException("Order total must be positive");
        }
    }
}

public class OrderCalculator {
    public double calculateTotal(Order order) {
        double total = 0;
        for (Item item : order.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
    
    public double calculateDiscount(Order order) {
        if (order.getCustomer().isPremium()) {
            return calculateTotal(order) * 0.1;
        }
        return 0;
    }
}
```

### 3. Extract Interface

**Before**
```java
public class MySQLDatabase {
    public void save(String data) {
        // MySQL logic
    }
    
    public String find(String query) {
        // MySQL logic
    }
}

public class PostgreSQLDatabase {
    public void save(String data) {
        // PostgreSQL logic
    }
    
    public String find(String query) {
        // PostgreSQL logic
    }
}
```

**After**
```java
public interface Database {
    void save(String data);
    String find(String query);
}

public class MySQLDatabase implements Database {
    public void save(String data) {
        // MySQL logic
    }
    
    public String find(String query) {
        // MySQL logic
    }
}

public class PostgreSQLDatabase implements Database {
    public void save(String data) {
        // PostgreSQL logic
    }
    
    public String find(String query) {
        // PostgreSQL logic
    }
}
```

### 4. Use Inheritance

**Before**
```java
public class PremiumDiscount {
    public double calculate(double amount) {
        return amount * 0.1;
    }
}

public class RegularDiscount {
    public double calculate(double amount) {
        return amount * 0.05;
    }
}
```

**After**
```java
public abstract class Discount {
    public abstract double calculate(double amount);
}

public class PremiumDiscount extends Discount {
    public double calculate(double amount) {
        return amount * 0.1;
    }
}

public class RegularDiscount extends Discount {
    public double calculate(double amount) {
        return amount * 0.05;
    }
}
```

### 5. Use Composition

**Before**
```java
public class OrderService {
    private EmailService emailService;
    private PaymentService paymentService;
    private NotificationService notificationService;
    
    public void processOrder(Order order) {
        // Uses all services
    }
}
```

**After**
```java
public class OrderService {
    private final OrderProcessor processor;
    private final OrderNotifier notifier;
    
    public OrderService(OrderProcessor processor, OrderNotifier notifier) {
        this.processor = processor;
        this.notifier = notifier;
    }
    
    public void processOrder(Order order) {
        processor.process(order);
        notifier.notify(order);
    }
}

public class OrderProcessor {
    private final PaymentService paymentService;
    
    public void process(Order order) {
        paymentService.charge(order.getCustomer(), order.getTotal());
    }
}

public class OrderNotifier {
    private final EmailService emailService;
    private final NotificationService notificationService;
    
    public void notify(Order order) {
        emailService.sendConfirmation(order);
        notificationService.sendUpdate(order);
    }
}
```

---

## DRY in Practice

### Real-World Examples

**Example 1: User Validation**
```java
// DRY: Single validation logic
public class UserValidator {
    public void validate(User user) {
        validateEmail(user.getEmail());
        validateName(user.getName());
        validatePassword(user.getPassword());
    }
    
    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }
    
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
    }
    
    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
}
```

**Example 2: API Response**
```java
// DRY: Single response builder
public class ApiResponseBuilder {
    public <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }
    
    public <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
}

// Usage
ApiResponse<User> response = apiResponseBuilder.success(user);
ApiResponse<User> errorResponse = apiResponseBuilder.error("User not found");
```

**Example 3: Database Queries**
```java
// DRY: Single query builder
public class QueryBuilder {
    public String buildSelectQuery(String table, Map<String, Object> conditions) {
        StringBuilder query = new StringBuilder("SELECT * FROM " + table);
        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            conditions.forEach((key, value) -> {
                query.append(key).append(" = ").append(value).append(" AND ");
            });
            query.setLength(query.length() - 5); // Remove last AND
        }
        return query.toString();
    }
}
```

---

## Best Practices

### When to Apply DRY

1. **Repeated code**: Same logic in multiple places
2. **Similar patterns**: Structures that repeat
3. **Knowledge sharing**: Information used in multiple contexts
4. **Process repetition**: Steps that repeat
5. **Configuration duplication**: Same settings in multiple places

### How to Apply DRY

1. **Identify duplication**: Look for repeated patterns
2. **Extract abstraction**: Create reusable components
3. **Centralize knowledge**: Single source of truth
4. **Document decisions**: Record why choices were made
5. **Test thoroughly**: Ensure abstractions work correctly

### DRY Checklist

- [ ] No repeated logic in multiple methods
- [ ] No repeated validation in multiple places
- [ ] No repeated calculation in multiple places
- [ ] No repeated string literals in multiple places
- [ ] No repeated configuration in multiple places
- [ ] No repeated error handling in multiple places
- [ ] Single source of truth for all knowledge

---

## Common Mistakes

### Over-DRY

1. **Premature abstraction**: Abstracting too early
2. **Over-engineering**: Making things complex
3. **Wrong abstraction**: Abstraction doesn't fit
4. **Tight coupling**: Abstractions create dependencies
5. **Reduced readability**: Code becomes hard to understand

### Under-DRY

1. **Ignoring duplication**: Not addressing repeated code
2. **Copy-paste programming**: Not extracting common logic
3. **Hard-coded values**: Magic numbers and strings
4. **Scattered logic**: Business logic in multiple places
5. **Inconsistent behavior**: Same operation, different results

### Process Mistakes

1. **Not refactoring**: Letting duplication grow
2. **Not reviewing**: Missing duplication in code reviews
3. **Not testing**: Not verifying abstractions work
4. **Not documenting**: Not recording why choices were made
5. **Not updating**: Not keeping abstractions current

---

## Key Takeaways

1. **DRY reduces duplication**: Single source of truth
2. **Identify patterns**: Look for repeated code
3. **Extract abstractions**: Create reusable components
4. **Centralize knowledge**: One place for each piece of logic
5. **Test thoroughly**: Ensure abstractions work
6. **Balance**: Don't over-DRY or under-DRY
7. **Refactor continuously**: Improve code over time
8. **Document decisions**: Record why choices were made

---

## Additional Resources

- [SOLID Principles](../../README.md) - Design principles
- [KISS Principle](../../README.md) - Keep it simple
- [Clean Code](../../README.md) - Writing quality code
- [Engineering Principles](../engineering-principles/README.md) - Core principles
- [Books](../../README.md) - Recommended reading

---

*Last Updated: August 2026*
