# Dependency

## Introduction

Dependency is the weakest form of relationship in object-oriented programming where one class uses another class temporarily to perform a specific operation, without maintaining a long-term relationship or ownership, creating a transient connection that exists only during method execution and disappears when the operation completes. Unlike association, aggregation, or composition which represent structural relationships, dependency represents a usage relationship where one class depends on another for a specific functionality but does not store a reference to it as an instance variable. Dependencies are typically created through method parameters, local variables, or static method calls, and they represent the most loose form of coupling between classes. Understanding dependencies is crucial for designing maintainable, testable systems because excessive or poorly managed dependencies can lead to tight coupling, making code difficult to understand, modify, and test.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Understand the concept of dependency and how it differs from other relationships
- [ ] Identify different types of dependencies (method parameter, local variable, static call)
- [ ] Implement dependencies using dependency injection to reduce coupling
- [ ] Apply dependency management principles to create testable, maintainable designs

## Prerequisites

- [20-association](../20-association/README.md) - Understanding object relationships
- [19-composition](../19-composition/README.md) - Understanding ownership relationships
- [05-methods](../05-methods/README.md) - Method parameters and invocation
- [12-interfaces](../12-interfaces/README.md) - Interface contracts and polymorphism

## Why This Concept Exists

### The Problem

Without proper dependency management, classes become tightly coupled:

1. **Hard-coded dependencies**: Classes directly instantiate their dependencies
2. **Difficult testing**: Hard to mock or stub dependencies for unit testing
3. **Reduced flexibility**: Dependencies cannot be swapped without changing the dependent class
4. **Maintenance nightmare**: Changes in dependencies cascade through the system

```java
// Problem: Tight coupling through direct instantiation
class OrderService {
    private DatabaseRepository repository = new DatabaseRepository(); // Hard dependency
    private EmailService emailService = new EmailService(); // Hard dependency

    public void processOrder(Order order) {
        repository.save(order);
        emailService.sendConfirmation(order);
    }
}
```

### The Solution

Dependency management solves these problems by:

- Defining dependencies through interfaces rather than concrete classes
- Injecting dependencies from outside rather than creating them internally
- Making dependencies explicit and configurable
- Enabling easy testing through mock objects

### Real-World Analogy

Think of dependency as a **restaurant customer ordering food**. The customer depends on the kitchen to prepare the meal, but:
- The customer doesn't own or control the kitchen
- The dependency exists only during the meal preparation
- The kitchen could be replaced (different restaurant) without affecting the customer
- The customer doesn't know the internal workings of the kitchen

This is different from composition (customer owns a phone) or aggregation (customer belongs to a loyalty program).

## Internal Working

### JVM Perspective

Dependencies are implemented through temporary object references:

1. **Method Parameters**: Dependencies passed as method arguments
2. **Local Variables**: Dependencies created and used within methods
3. **Static Calls**: Dependencies invoked through static methods
4. **No Persistent State**: Dependencies are not stored as instance variables

### Memory Representation

```
Dependency in Memory:

OrderService Method Execution:
┌─────────────────────────────┐
│ Stack Frame: processOrder() │
├─────────────────────────────┤
│ Parameters:                 │
│ ├── order: Order → ───────────────→ Order Object
│                             │
│ Local Variables:            │
│ └── repository → ──────────────────→ DatabaseRepository Object
│                             │
│ (Dependencies are temporary)│
└─────────────────────────────┘

After method completes:
┌─────────────────────────────┐
│ Stack Frame: processOrder() │
├─────────────────────────────┘
│ (Stack frame popped, local   │
│  variables destroyed,        │
│  dependencies dereferenced)  │
└─────────────────────────────┘
```

### Dependency Types

```
Dependency Relationships:

1. Method Parameter Dependency:
   class Service {
       void process(Repository repo) { // Dependency through parameter
           repo.save(data);
       }
   }

2. Local Variable Dependency:
   class Service {
       void process() {
           Repository repo = new DatabaseRepository(); // Dependency through local variable
           repo.save(data);
       }
   }

3. Static Call Dependency:
   class Service {
       void process() {
           UtilityHelper.format(data); // Dependency through static call
       }
   }

4. Dependency Injection:
   class Service {
       private final Repository repo; // Dependency through injection

       Service(Repository repo) { // Injected dependency
           this.repo = repo;
       }
   }
```

## Syntax

### Method Parameter Dependency

```java
class OrderProcessor {
    public void processOrder(Order order, PaymentProcessor processor) {
        // Dependency on PaymentProcessor through parameter
        processor.processPayment(order.getTotal());
    }
}

// Usage
OrderProcessor processor = new OrderProcessor();
PaymentProcessor creditCard = new CreditCardProcessor();
processor.processOrder(order, creditCard); // Dependency injected through parameter
```

### Local Variable Dependency

```java
class ReportGenerator {
    public Report generateReport(Data data) {
        // Dependency on ReportFormatter through local variable
        ReportFormatter formatter = new PDFReportFormatter();
        return formatter.format(data);
    }
}
```

### Static Call Dependency

```java
class DataValidator {
    public boolean validate(String data) {
        // Dependency on ValidationUtils through static call
        return ValidationUtils.isNotEmpty(data) && ValidationUtils.isValidFormat(data);
    }
}
```

### Dependency Injection

```java
class OrderService {
    private final Repository repository;
    private final NotificationService notificationService;

    // Dependencies injected through constructor
    public OrderService(Repository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public void processOrder(Order order) {
        repository.save(order);
        notificationService.sendConfirmation(order);
    }
}

// Dependencies provided externally
Repository repo = new DatabaseRepository();
NotificationService notifier = new EmailNotificationService();
OrderService service = new OrderService(repo, notifier);
```

## Easy Examples

### Example 1: Payment Processing System

**Problem Statement**: Design a payment processing system that demonstrates different types of dependencies and how to manage them for flexibility and testability.

**Implementation**:

```java
package academy.javaengineering.oop.dependency;

import java.time.LocalDateTime;
import java.util.UUID;

// Interfaces for dependencies (reduces coupling)
interface PaymentGateway {
    boolean processPayment(double amount, String currency);
    String getTransactionId();
}

interface NotificationService {
    void sendNotification(String recipient, String message);
}

interface Logger {
    void log(String level, String message);
}

// Concrete implementations
class StripePaymentGateway implements PaymentGateway {
    private String transactionId;

    @Override
    public boolean processPayment(double amount, String currency) {
        System.out.println("Stripe: Processing $" + amount + " " + currency);
        transactionId = UUID.randomUUID().toString();
        System.out.println("Stripe: Payment successful. Transaction ID: " + transactionId);
        return true;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }
}

class PayPalPaymentGateway implements PaymentGateway {
    private String transactionId;

    @Override
    public boolean processPayment(double amount, String currency) {
        System.out.println("PayPal: Processing $" + amount + " " + currency);
        transactionId = "PP-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("PayPal: Payment successful. Transaction ID: " + transactionId);
        return true;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }
}

class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("Email to " + recipient + ": " + message);
    }
}

class SMSNotificationService implements NotificationService {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("SMS to " + recipient + ": " + message);
    }
}

class ConsoleLogger implements Logger {
    @Override
    public void log(String level, String message) {
        System.out.println("[" + level + "] " + LocalDateTime.now() + ": " + message);
    }
}

class FileLogger implements Logger {
    @Override
    public void log(String level, String message) {
        System.out.println("FILE [" + level + "] " + message);
    }
}

// Order class (domain object)
class Order {
    private String orderId;
    private String customerEmail;
    private double amount;
    private String currency;

    public Order(String customerEmail, double amount, String currency) {
        this.orderId = UUID.randomUUID().toString();
        this.customerEmail = customerEmail;
        this.amount = amount;
        this.currency = currency;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerEmail() { return customerEmail; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
}

// OrderService with dependency injection
class OrderService {
    private final PaymentGateway paymentGateway;
    private final NotificationService notificationService;
    private final Logger logger;

    // Constructor injection (primary dependency injection method)
    public OrderService(PaymentGateway paymentGateway,
                       NotificationService notificationService,
                       Logger logger) {
        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
        this.logger = logger;
    }

    public boolean processOrder(Order order) {
        logger.log("INFO", "Processing order: " + order.getOrderId());

        // Use injected dependencies
        boolean paymentSuccess = paymentGateway.processPayment(order.getAmount(), order.getCurrency());

        if (paymentSuccess) {
            String message = "Order " + order.getOrderId() + " confirmed. " +
                           "Transaction ID: " + paymentGateway.getTransactionId();
            notificationService.sendNotification(order.getCustomerEmail(), message);
            logger.log("INFO", "Order processed successfully: " + order.getOrderId());
            return true;
        } else {
            logger.log("ERROR", "Payment failed for order: " + order.getOrderId());
            return false;
        }
    }
}

// Method parameter dependency example
class PaymentRefunder {
    public void refund(Order order, PaymentGateway gateway, Logger logger) {
        // Dependencies through method parameters
        logger.log("INFO", "Processing refund for order: " + order.getOrderId());
        System.out.println("Refunding $" + order.getAmount() + " via " + gateway.getClass().getSimpleName());
        logger.log("INFO", "Refund completed for order: " + order.getOrderId());
    }
}

// Local variable dependency example
class ReceiptGenerator {
    public String generateReceipt(Order order) {
        // Dependency on receipt formatter through local variable
        ReceiptFormatter formatter = new SimpleReceiptFormatter();
        return formatter.format(order);
    }
}

interface ReceiptFormatter {
    String format(Order order);
}

class SimpleReceiptFormatter implements ReceiptFormatter {
    @Override
    public String format(Order order) {
        return String.format(
            "RECEIPT%nOrder ID: %s%nAmount: $%.2f %s%nDate: %s",
            order.getOrderId(), order.getAmount(), order.getCurrency(), LocalDateTime.now()
        );
    }
}

public class PaymentDemo {
    public static void main(String[] args) {
        System.out.println("=== Payment Processing System Demo ===\n");

        // Create dependencies
        PaymentGateway stripe = new StripePaymentGateway();
        NotificationService emailService = new EmailNotificationService();
        Logger logger = new ConsoleLogger();

        // Inject dependencies into service
        OrderService orderService = new OrderService(stripe, emailService, logger);

        // Process orders
        Order order1 = new Order("alice@email.com", 99.99, "USD");
        Order order2 = new Order("bob@email.com", 149.99, "EUR");

---

## Continue Reading

- [Part 2](README-part2.md)
