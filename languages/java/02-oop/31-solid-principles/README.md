# SOLID Principles

## Introduction

SOLID is an acronym for five fundamental object-oriented design principles that help developers create software that is maintainable, flexible, and scalable. Formulated by Robert C. Martin (Uncle Bob), these principles — Single Responsibility Principle (SRP), Open/Closed Principle (OCP), Liskov Substitution Principle (LSP), Interface Segregation Principle (ISP), and Dependency Inversion Principle (DIP) — form the foundation of clean architecture. Each principle addresses a specific type of design problem: SRP reduces coupling by ensuring classes have one reason to change, OCP enables extension without modification, LSP ensures behavioral consistency in inheritance hierarchies, ISP prevents fat interfaces that force unnecessary dependencies, and DIP decouples high-level modules from low-level implementations. Together, SOLID principles lead to code that is easier to test, refactor, and extend over time.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Explain and apply all five SOLID principles in Java applications
- [ ] Identify code smells that indicate violations of SOLID principles
- [ ] Refactor poorly designed code to comply with SOLID principles
- [ ] Recognize how SOLID principles interact and support each other

## Prerequisites

- [02-classes](../02-classes/) — SOLID principles apply to class design
- [08-encapsulation](../08-encapsulation/) — Encapsulation supports SRP and DIP
- [09-inheritance](../09-inheritance/) — LSP governs inheritance hierarchies
- [12-interfaces](../12-interfaces/) — ISP and DIP rely on proper interface design
- [13-abstract-classes](../13-abstract-classes/) — Abstract classes support OCP and DIP

## Why This Concept Exists

### The Problem

Without design principles, codebases suffer from:
- **Rigid code**: Changes in one module cascade through many others
- **Fragile code**: Small changes cause unexpected failures
- **Immobile code**: Components cannot be reused in different contexts
- **Hard-to-test code**: Tightly coupled classes are difficult to unit test

### The Solution

SOLID principles provide guidelines for designing classes and modules that are loosely coupled, highly cohesive, and easy to change. Each principle addresses a specific aspect of software design, and together they create a foundation for maintainable systems.

### Real-World Analogy

Think of SOLID like building codes for houses. Each code addresses a specific concern — structural integrity, electrical safety, plumbing standards. Together, they ensure the house is safe, functional, and can be modified without collapsing. Similarly, SOLID principles ensure software is robust and adaptable.

## Internal Working

### The Five Principles at a Glance

| Principle | Focus | Key Question |
|-----------|-------|-------------|
| SRP | Class responsibility | Does this class have only one reason to change? |
| OCP | Extension mechanism | Can I add new features without modifying existing code? |
| LSP | Inheritance correctness | Can I substitute a subclass for its parent without breaking anything? |
| ISP | Interface design | Are clients forced to depend on methods they don't use? |
| DIP | Dependency direction | Do high-level modules depend on low-level modules? |

### How Principles Interact

SRP and ISP are closely related — both address the problem of classes/interfaces doing too much. OCP and LSP work together — OCP enables extension through inheritance/polymorphism, and LSP ensures that extensions maintain behavioral contracts. DIP supports OCP by enabling extension through abstraction rather than inheritance.

## Syntax

SOLID principles are not about syntax but about design patterns. They manifest in code through interfaces, abstract classes, dependency injection, and proper class design.

```java
// SRP: Each class has one responsibility
class OrderProcessor { /* processes orders */ }
class OrderRepository { /* persists orders */ }
class OrderNotifier { /* sends notifications */ }

// OCP: Open for extension, closed for modification
interface DiscountStrategy {
    double calculate(double price);
}
class SeasonalDiscount implements DiscountStrategy { /* ... */ }
class LoyaltyDiscount implements DiscountStrategy { /* ... */ }

// LSP: Subclasses can replace parent classes
abstract class Shape {
    abstract double area();
}
class Circle extends Shape { /* ... */ }
class Rectangle extends Shape { /* ... */ }

// ISP: Clients depend only on methods they use
interface Readable { String read(); }
interface Writable { void write(String data); }

// DIP: Depend on abstractions, not concretions
class OrderService {
    private final OrderRepository repository; // Interface
    OrderService(OrderRepository repository) { // Dependency injection
        this.repository = repository;
    }
}
```

## Easy Examples

### Example 1: Single Responsibility Principle (SRP)

**Problem Statement**: A class handles order processing, persistence, and notification. Refactor to follow SRP.

**Implementation**:

```java
package academy.javaengineering.oop.solid.srp;

// VIOLATION: One class does everything
class BadOrderService {
    void processOrder(String orderId) {
        // Business logic
        System.out.println("Processing order: " + orderId);

        // Persistence
        System.out.println("Saving to database...");

        // Notification
        System.out.println("Sending email notification...");
    }
}

// COMPLIANT: Each class has one responsibility
class Order {
    private String id;
    private double total;

    Order(String id, double total) {
        this.id = id;
        this.total = total;
    }

    String getId() { return id; }
    double getTotal() { return total; }
}

class OrderProcessor {
    Order process(String orderId, double amount) {
        System.out.println("Processing order: " + orderId);
        return new Order(orderId, amount);
    }
}

class OrderRepository {
    void save(Order order) {
        System.out.println("Saving order " + order.getId() + " to database");
    }
}

class OrderNotifier {
    void sendConfirmation(Order order) {
        System.out.println("Email sent for order " + order.getId());
    }
}

public class SRPDemo {
    public static void main(String[] args) {
        OrderProcessor processor = new OrderProcessor();
        OrderRepository repository = new OrderRepository();
        OrderNotifier notifier = new OrderNotifier();

        Order order = processor.process("ORD-001", 99.99);
        repository.save(order);
        notifier.sendConfirmation(order);
    }
}
```

**Expected Output**:
```
Processing order: ORD-001
Saving order ORD-001 to database
Email sent for order ORD-001
```

**Code Walkthrough**: The `BadOrderService` violates SRP — it handles processing, persistence, and notification. Refactored, each class has a single reason to change: `OrderProcessor` changes for business rules, `OrderRepository` changes for persistence, `OrderNotifier` changes for notification channels.

### Example 2: Open/Closed Principle (OCP)

**Problem Statement**: A payment system uses `if-else` chains for different payment methods. Refactor to support new payment methods without modifying existing code.

**Implementation**:

```java
package academy.javaengineering.oop.solid.ocp;

// VIOLATION: Adding new payment method requires modifying this class
class BadPaymentProcessor {
    void process(String type, double amount) {
        if (type.equals("CREDIT_CARD")) {
            System.out.println("Processing credit card: $" + amount);
        } else if (type.equals("PAYPAL")) {
            System.out.println("Processing PayPal: $" + amount);
        } else if (type.equals("CRYPTO")) {
            System.out.println("Processing crypto: $" + amount);
        }
        // Adding BANK_TRANSFER requires modifying this method!
    }
}

// COMPLIANT: New payment methods extend without modifying existing code
interface PaymentMethod {
    void pay(double amount);
    String getName();
}

class CreditCard implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Processing credit card: $" + amount);
    }

    @Override
    public String getName() { return "CREDIT_CARD"; }
}

class PayPal implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Processing PayPal: $" + amount);
    }

    @Override
    public String getName() { return "PAYPAL"; }
}

class CryptoPayment implements PaymentMethod {
    @Override
    public void pay(double amount) {
        System.out.println("Processing crypto: $" + amount);
    }

    @Override
    public String getName() { return "CRYPTO"; }
}

class PaymentProcessor {
    void process(PaymentMethod method, double amount) {
        method.pay(amount);
    }
}

public class OCPDemo {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        processor.process(new CreditCard(), 50.00);
        processor.process(new PayPal(), 30.00);
        processor.process(new CryptoPayment(), 100.00);

        // Adding new payment method — no modification to existing code!
        PaymentMethod bankTransfer = new PaymentMethod() {
            @Override
            public void pay(double amount) {
                System.out.println("Processing bank transfer: $" + amount);
            }
            @Override
            public String getName() { return "BANK_TRANSFER"; }
        };
        processor.process(bankTransfer, 200.00);
    }
}
```

**Expected Output**:
```
Processing credit card: $50.0
Processing PayPal: $30.0
Processing crypto: $100.0
Processing bank transfer: $200.0
```

**Code Walkthrough**: The `PaymentProcessor` depends on the `PaymentMethod` interface (abstraction). New payment methods implement this interface without modifying `PaymentProcessor`. This is the Open/Closed Principle in action — open for extension, closed for modification.

### Example 3: Liskov Substitution Principle (LSP)

**Problem Statement**: A `Square` class extends `Rectangle` but breaks the expected behavior. Identify and fix the LSP violation.

**Implementation**:

```java
package academy.javaengineering.oop.solid.lsp;

// VIOLATION: Square breaks Rectangle's behavioral contract
class BadRectangle {
    protected int width, height;

    void setWidth(int width) { this.width = width; }
    void setHeight(int height) { this.height = height; }
    int getArea() { return width * height; }
}

class BadSquare extends BadRectangle {
    @Override
    void setWidth(int width) {
        this.width = width;
        this.height = width; // Unexpected side effect!
    }

    @Override
    void setHeight(int height) {
        this.width = height;
        this.height = height; // Unexpected side effect!
    }
}

// This code works with Rectangle but breaks with Square:
// void increaseWidth(BadRectangle rect) {
//     int oldHeight = rect.getArea() / rect.getWidth();
//     rect.setWidth(rect.getWidth() + 1);
//     assert rect.getArea() == (oldHeight * (rect.getWidth() + 1)); // Fails for Square!
// }

// COMPLIANT: Immutable shapes that don't violate LSP
interface Shape {
    double area();
}

class ImmutableRectangle implements Shape {
    private final double width, height;

    ImmutableRectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    double getWidth() { return width; }
    double getHeight() { return height; }

    @Override
    public double area() { return width * height; }
}

class ImmutableSquare implements Shape {
    private final double side;

    ImmutableSquare(double side) {
        this.side = side;
    }

    double getSide() { return side; }

    @Override
    public double area() { return side * side; }
}

class ShapeProcessor {
    static void printArea(Shape shape) {
        System.out.printf("Area: %.2f%n", shape.area());
    }
}

public class LSPDemo {
    public static void main(String[] args) {
        Shape rect = new ImmutableRectangle(5, 3);
        Shape square = new ImmutableSquare(4);

        ShapeProcessor.printArea(rect);   // Works correctly
        ShapeProcessor.printArea(square); // Works correctly

        // Both can be used interchangeably without surprises
    }
}
```

**Expected Output**:
```
Area: 15.00
Area: 16.00
```

**Code Walkthrough**: The mutable `BadSquare` violates LSP — it changes the behavior of inherited methods. The immutable shapes avoid this by not having setters that create side effects. LSP ensures that any code working with `Shape` will work correctly with both `ImmutableRectangle` and `ImmutableSquare`.

## Medium Examples

### Example 1: Interface Segregation Principle (ISP)

**Problem Statement**: A `Worker` interface forces all implementors to define `work()`, `eat()`, and `sleep()`. Robots don't eat or sleep. Refactor to follow ISP.

**Implementation**:

```java
package academy.javaengineering.oop.solid.isp;

// VIOLATION: Fat interface forces unnecessary implementations
interface BadWorker {
    void work();
    void eat();
    void sleep();
}

class Robot implements BadWorker {
    @Override
    public void work() { System.out.println("Robot working"); }
    @Override
    public void eat() { } // Robots don't eat — forced to implement
    @Override
    public void sleep() { } // Robots don't sleep — forced to implement
}

// COMPLIANT: Segregated interfaces
interface Workable {
    void work();
}

interface Feedable {
    void feed();
}

interface Sleepable {
    void sleep();
}

class HumanWorker implements Workable, Feedable, Sleepable {
    @Override
    public void work() { System.out.println("Human working"); }
    @Override
    public void feed() { System.out.println("Human eating"); }
    @Override
    public void sleep() { System.out.println("Human sleeping"); }
}

class RoboticWorker implements Workable {
    @Override
    public void work() { System.out.println("Robot working"); }
}

class Manager implements Workable, Feedable {
    @Override
    public void work() { System.out.println("Manager managing"); }
    @Override
    public void feed() { System.out.println("Manager eating lunch"); }
}

public class ISPDemo {
    public static void main(String[] args) {
        Workable human = new HumanWorker();
        Workable robot = new RoboticWorker();
        Workable manager = new Manager();

        human.work();
        robot.work();
        manager.work();

        // Feed only those who can eat
        if (human instanceof Feedable) ((Feedable) human).feed();
        if (manager instanceof Feedable) ((Feedable) manager).feed();
        // Robot doesn't implement Feedable — no empty method needed
    }
}
```

**Expected Output**:
```
Human working
Robot working
Manager managing
Human eating
Manager eating lunch
```

**Code Walkthrough**: The fat `BadWorker` interface forces robots to implement irrelevant methods. Segregated interfaces (`Workable`, `Feedable`, `Sleepable`) let each class implement only what it needs. Clients depend only on interfaces they use — a `Workable` reference doesn't know about `eat()` or `sleep()`.

### Example 2: Dependency Inversion Principle (DIP)

**Problem Statement**: A `NotificationService` directly depends on `EmailSender`. Refactor to depend on an abstraction so the sending mechanism can be swapped.

**Implementation**:

```java
package academy.javaengineering.oop.solid.dip;

// VIOLATION: High-level module depends on low-level module
class BadEmailSender {
    void send(String to, String message) {
        System.out.println("Email to " + to + ": " + message);
    }
}

class BadNotificationService {
    private final BadEmailSender sender = new BadEmailSender();

    void notifyUser(String to, String message) {
        sender.send(to, message); // Tightly coupled to email
    }
}

// COMPLIANT: Depend on abstraction
interface MessageSender {
    void send(String to, String message);
    String getChannel();
}

class EmailSender implements MessageSender {
    @Override
    public void send(String to, String message) {
        System.out.println("Email to " + to + ": " + message);
    }

    @Override
    public String getChannel() { return "EMAIL"; }
}

class SmsSender implements MessageSender {
    @Override
    public void send(String to, String message) {
        System.out.println("SMS to " + to + ": " + message);
    }

    @Override
    public String getChannel() { return "SMS"; }
}

class PushNotificationSender implements MessageSender {
    @Override
    public void send(String to, String message) {
        System.out.println("Push to " + to + ": " + message);
    }

    @Override
    public String getChannel() { return "PUSH"; }
}

class NotificationService {
    private final MessageSender sender;

    NotificationService(MessageSender sender) { // Constructor injection
        this.sender = sender;
    }

    void notifyUser(String to, String message) {
        System.out.println("Sending via " + sender.getChannel());
        sender.send(to, message);
    }
}

public class DIPDemo {
    public static void main(String[] args) {
        NotificationService emailService = new NotificationService(new EmailSender());
        emailService.notifyUser("alice@example.com", "Welcome!");

        NotificationService smsService = new NotificationService(new SmsSender());
        smsService.notifyUser("+1234567890", "Your OTP is 1234");

        NotificationService pushService = new NotificationService(new PushNotificationSender());
        pushService.notifyUser("device-token-abc", "New message");
    }
}
```

**Expected Output**:
```
Sending via EMAIL
Email to alice@example.com: Welcome!
Sending via SMS
SMS to +1234567890: Your OTP is 1234
Sending via PUSH
Push to device-token-abc: New message
```

**Code Walkthrough**: `NotificationService` depends on the `MessageSender` interface (abstraction), not on `EmailSender` (concretion). The concrete sender is injected via the constructor. Switching from email to SMS requires only changing the injected object, not the service class.

### Example 3: Applying All Five Principles Together

**Problem Statement**: Design a file processing system that follows all five SOLID principles.

**Implementation**:

```java
package academy.javaengineering.oop.solid;

import java.util.ArrayList;
import java.util.List;

// SRP: Each class has one responsibility
// OCP: New processors and exporters extend without modification
// ISP: Interfaces are focused and minimal
// DIP: High-level modules depend on abstractions

interface FileReader {
    String read(String filePath);
}

interface DataProcessor {
    List<String> process(String data);
}

interface DataExporter {
    void export(List<String> data);
}

interface Logger {
    void log(String message);
}

class TextFileReader implements FileReader {
    @Override
    public String read(String filePath) {
        return "Line 1\nLine 2\nLine 3";
    }
}

class UpperCaseProcessor implements DataProcessor {
    @Override
    public List<String> process(String data) {
        List<String> result = new ArrayList<>();
        for (String line : data.split("\n")) {
            result.add(line.toUpperCase());
        }
        return result;
    }
}

class ConsoleExporter implements DataExporter {
    @Override
    public void export(List<String> data) {
        data.forEach(System.out::println);
    }
}

class SimpleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

// LSP: Any FileReader, DataProcessor, DataExporter can be substituted
class FilePipeline {
    private final FileReader reader;
    private final DataProcessor processor;
    private final DataExporter exporter;
    private final Logger logger;

    FilePipeline(FileReader reader, DataProcessor processor,
                 DataExporter exporter, Logger logger) {
        this.reader = reader;
        this.processor = processor;
        this.exporter = exporter;
        this.logger = logger;
    }

    void execute(String filePath) {
        logger.log("Reading: " + filePath);
        String data = reader.read(filePath);

        logger.log("Processing data");
        List<String> processed = processor.process(data);

        logger.log("Exporting results");
        exporter.export(processed);

        logger.log("Pipeline complete");
    }
}

public class SOLIDDemo {
    public static void main(String[] args) {
        FilePipeline pipeline = new FilePipeline(
                new TextFileReader(),
                new UpperCaseProcessor(),
                new ConsoleExporter(),
                new SimpleLogger()
        );
        pipeline.execute("input.txt");
    }
}
```

**Expected Output**:
```
[LOG] Reading: input.txt
[LOG] Processing data
[LOG] Exporting results
LINE 1
LINE 2
LINE 3
[LOG] Pipeline complete
```

## Hard Examples

### Example 1: SOLID-Compliant E-Commerce Order System

**Problem Statement**: Design an order processing system that follows all SOLID principles, supports multiple payment methods, notification channels, and shipping providers.

**Architecture**:

```
┌─────────────────────────────────────────────────┐
│                OrderService (SRP)                │
│  ┌──────────┐  ┌───────────┐  ┌──────────────┐ │
│  │ Payment  │  │ Shipping  │  │ Notification │ │
│  │ Processor│  │ Processor │  │ Service      │ │
│  └────┬─────┘  └─────┬─────┘  └──────┬───────┘ │
│       │              │               │          │
│  ┌────▼─────┐  ┌─────▼─────┐  ┌──────▼───────┐ │
│  │ Payment  │  │ Shipping  │  │ Notification │ │
│  │ Strategy │  │ Strategy  │  │ Strategy     │ │
│  │ (ISP)    │  │ (ISP)     │  │ (ISP)        │ │
│  └──────────┘  └───────────┘  └──────────────┘ │
└─────────────────────────────────────────────────┘
         All depend on abstractions (DIP)
```

**Implementation**:

```java
package academy.javaengineering.oop.solid.ecommerce;

import java.util.ArrayList;
import java.util.List;

// ISP: Focused interfaces
interface PaymentProcessor {
    boolean processPayment(double amount);
    String getPaymentMethod();
}

interface ShippingCalculator {
    double calculateShipping(String destination, double weight);
    String getShippingMethod();
}

interface NotificationService {
    void sendNotification(String recipient, String message);
}

// SRP: Single responsibility for each strategy
class CreditCardPayment implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        System.out.printf("  Charging $%.2f to credit card%n", amount);
        return true;
    }

    @Override
    public String getPaymentMethod() { return "Credit Card"; }
}

class StandardShipping implements ShippingCalculator {
    @Override
    public double calculateShipping(String destination, double weight) {
        return 5.99 + (weight * 0.50);
    }

    @Override
    public String getShippingMethod() { return "Standard"; }
}

class EmailNotification implements NotificationService {
    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("  EMAIL to " + recipient + ": " + message);
    }
}

// LSP: Any implementation can be substituted
class Order {
    private final String id;
    private final double amount;
    private final String customerEmail;
    private final String destination;
    private final double weight;

    Order(String id, double amount, String customerEmail, String destination, double weight) {
        this.id = id;
        this.amount = amount;
        this.customerEmail = customerEmail;
        this.destination = destination;
        this.weight = weight;
    }

    String getId() { return id; }
    double getAmount() { return amount; }
    String getCustomerEmail() { return customerEmail; }
    String getDestination() { return destination; }
    double getWeight() { return weight; }
}

class OrderResult {
    private final boolean success;
    private final String orderId;
    private final List<String> steps = new ArrayList<>();

    OrderResult(boolean success, String orderId) {
        this.success = success;
        this.orderId = orderId;
    }

    void addStep(String step) { steps.add(step); }
    boolean isSuccess() { return success; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ").append(orderId).append(": ").append(success ? "SUCCESS" : "FAILED");
        sb.append("\nSteps taken:");
        steps.forEach(step -> sb.append("\n  - ").append(step));
        return sb.toString();
    }
}

// DIP: Depends on abstractions
class OrderService {
    private final PaymentProcessor paymentProcessor;
    private final ShippingCalculator shippingCalculator;
    private final NotificationService notificationService;

    OrderService(PaymentProcessor paymentProcessor,
                 ShippingCalculator shippingCalculator,
                 NotificationService notificationService) {
        this.paymentProcessor = paymentProcessor;
        this.shippingCalculator = shippingCalculator;
        this.notificationService = notificationService;
    }

    OrderResult processOrder(Order order) {
        OrderResult result = new OrderResult(true, order.getId());

        // Process payment
        result.addStep("Payment via " + paymentProcessor.getPaymentMethod());
        if (!paymentProcessor.processPayment(order.getAmount())) {
            return new OrderResult(false, order.getId());
        }

        // Calculate shipping
        double shipping = shippingCalculator.calculateShipping(
                order.getDestination(), order.getWeight());
        result.addStep("Shipping: " + shippingCalculator.getShippingMethod()
                + " ($" + String.format("%.2f", shipping) + ")");

        // Send notification
        notificationService.sendNotification(order.getCustomerEmail(),
                "Order " + order.getId() + " confirmed!");
        result.addStep("Notification sent");

        return result;
    }
}

public class ECommerceDemo {
    public static void main(String[] args) {
        Order order = new Order("ORD-001", 49.99, "alice@example.com", "New York", 2.5);

        OrderService service = new OrderService(
                new CreditCardPayment(),
                new StandardShipping(),
                new EmailNotification()
        );

        OrderResult result = service.processOrder(order);
        System.out.println(result);
    }
}
```

**Unit Tests**:

```java
package academy.javaengineering.oop.solid.ecommerce;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OrderServiceTest {
    @Test
    void testSuccessfulOrder() {
        PaymentProcessor mockPayment = new PaymentProcessor() {
            @Override
            public boolean processPayment(double amount) { return true; }
            @Override
            public String getPaymentMethod() { return "Mock"; }
        };
        ShippingCalculator mockShipping = new ShippingCalculator() {
            @Override
            public double calculateShipping(String dest, double weight) { return 5.0; }
            @Override
            public String getShippingMethod() { return "Mock"; }
        };
        NotificationService mockNotification = new NotificationService() {
            @Override
            public void sendNotification(String to, String msg) { }
        };

        OrderService service = new OrderService(mockPayment, mockShipping, mockNotification);
        Order order = new Order("TEST-001", 100.0, "test@test.com", "NY", 1.0);
        OrderResult result = service.processOrder(order);

        assertTrue(result.isSuccess());
    }

    @Test
    void testFailedPayment() {
        PaymentProcessor failPayment = new PaymentProcessor() {
            @Override
            public boolean processPayment(double amount) { return false; }
            @Override
            public String getPaymentMethod() { return "Fail"; }
        };
        OrderService service = new OrderService(failPayment, null, null);
        Order order = new Order("TEST-002", 50.0, "test@test.com", "NY", 1.0);
        OrderResult result = service.processOrder(order);

        assertFalse(result.isSuccess());
    }
}
```

**Execution Flow**: `OrderService` receives dependencies via constructor (DIP). Each dependency implements a focused interface (ISP). Each strategy has a single responsibility (SRP). New payment/shipping/notification types can be added without modifying existing code (OSP). Any implementation can be substituted without breaking the service (LSP).

**Complexity**: O(1) for all operations — each step is a single method call.

**Best Practices**:
- Use constructor injection for all dependencies
- Keep interfaces small and focused
- Validate all inputs in strategy implementations
- Mock dependencies in unit tests

## Exercises

### Easy

1. Identify which SOLID principle is violated in a class that handles both user authentication and profile management. Refactor it.

2. Create a `Shape` hierarchy following LSP — `Circle`, `Rectangle`, `Triangle` — where all can be used interchangeably with `area()` and `describe()` methods.

3. Refactor a payment processor that uses `if-else` chains to follow OCP using strategy pattern.

### Medium

4. Design a logging system that follows all five SOLID principles. Support console, file, and database logging.

5. Create a `Repository<T>` pattern that follows DIP — depend on interfaces, not implementations. Support in-memory and file-based storage.

6. Refactor a God class (100+ lines, multiple responsibilities) into SOLID-compliant classes.

### Hard

7. Design a complete e-commerce system (cart, checkout, payment, shipping, notification) following all SOLID principles. Support multiple payment methods and shipping providers.

8. Implement a plugin system that follows OCP and DIP — plugins can be loaded dynamically without modifying the core system.

9. Create a microservice communication framework following ISP — each service exposes only the interfaces its consumers need.

## Interview Questions

### Easy

1. **What does SOLID stand for?**
   Single Responsibility Principle, Open/Closed Principle, Liskov Substitution Principle, Interface Segregation Principle, and Dependency Inversion Principle. They are five design principles for writing maintainable, flexible, and scalable object-oriented code.

2. **Explain the Single Responsibility Principle.**
   A class should have only one reason to change — it should have one job or responsibility. This reduces coupling and makes classes easier to understand, test, and modify.

3. **What is the difference between OCP and LSP?**
   OCP says classes should be open for extension but closed for modification — you can add new behavior without changing existing code. LSP says subclasses must be substitutable for their parent classes without breaking behavior. OCP enables extension; LSP ensures correctness of extensions.

### Intermediate

4. **How does Dependency Inversion Principle relate to SOLID?**
   DIP states that high-level modules should not depend on low-level modules; both should depend on abstractions. This supports OCP (abstractions enable extension) and SRP (dependencies are isolated). In Java, DIP is implemented through interfaces and dependency injection.

5. **What is an LSP violation in the classic Square-Rectangle problem?**
   `Square extends Rectangle` but overriding `setWidth()` and `setHeight()` creates side effects that break the parent's behavioral contract. Code that works with `Rectangle` (e.g., setting width independently of height) breaks when a `Square` is substituted. Immutable shapes avoid this problem.

6. **How do you apply ISP in Java?**
   Split fat interfaces into smaller, focused ones. `java.io` demonstrates this: `InputStream`, `OutputStream`, `Reader`, `Writer` are separate interfaces. Classes implement only the interfaces they need. Clients depend only on interfaces they use.

### Hard

7. **How do SOLID principles interact? Can violating one affect the others?**
   Yes, they're interdependent. SRP violations lead to ISP violations (one class implementing many interfaces). LSP violations prevent OCP (can't safely extend broken hierarchies). DIP supports OCP (abstractions enable extension) and SRP (isolated dependencies). Violating one principle often cascades to others.

8. **Design a system that balances SOLID compliance with practical complexity. When is strict adherence over-engineering?**
   Apply SOLID pragmatically. For simple CRUD applications, SRP and DIP are most valuable. OCP and LSP matter most in systems with evolving business rules. ISP is critical for public APIs. Avoid over-abstraction — if a class is unlikely to change, the cost of extra interfaces may not justify the benefit. YAGNI (You Aren't Gonna Need It) complements SOLID.

## Common Pitfalls

### 1. God Class Violating SRP

**Wrong**:
```java
class UserManager {
    void createUser(String name) { /* ... */ }
    void deleteUser(String id) { /* ... */ }
    void sendEmail(String to, String subject) { /* ... */ }
    void logActivity(String action) { /* ... */ }
    void exportToCsv(String filename) { /* ... */ }
}
```

**Right**:
```java
class UserService {
    void createUser(String name) { /* ... */ }
    void deleteUser(String id) { /* ... */ }
}

class EmailService {
    void sendEmail(String to, String subject) { /* ... */ }
}

class ActivityLogger {
    void logActivity(String action) { /* ... */ }
}
```

### 2. LSP Violation with Mutable Subclasses

**Wrong**:
```java
class ImmutableCollection {
    List<String> items = new ArrayList<>();
}

class SortableCollection extends ImmutableCollection {
    void sort() { items.sort(null); } // Changes parent behavior
}
```

**Right**:
```java
interface Collection {
    List<String> getItems();
}

class ImmutableCollection implements Collection {
    private final List<String> items;
    ImmutableCollection(List<String> items) { this.items = List.copyOf(items); }
    @Override public List<String> getItems() { return items; }
}

class SortableCollection implements Collection {
    private final List<String> items;
    SortableCollection(List<String> items) { this.items = new ArrayList<>(items); }
    void sort() { items.sort(null); }
    @Override public List<String> getItems() { return List.copyOf(items); }
}
```

### 3. DIP Violation with `new` in Constructors

**Wrong**:
```java
class OrderService {
    private final OrderRepository repository = new JdbcOrderRepository(); // Tightly coupled
}
```

**Right**:
```java
class OrderService {
    private final OrderRepository repository;
    OrderService(OrderRepository repository) { // Injected dependency
        this.repository = repository;
    }
}
```

## Best Practices

1. **Apply SRP first** — It's the most impactful principle and makes all others easier to follow.
2. **Use dependency injection** — Constructor injection makes dependencies explicit and supports testing.
3. **Design interfaces around client needs** — Small, focused interfaces are easier to implement and test.
4. **Test substitutability** — If your unit tests pass with mocks, you're likely following LSP and DIP.
5. **Apply principles pragmatically** — Not every class needs all five principles. Apply them where they provide value.

## Real World Usage

### How Spring Uses This

Spring Framework embodies SOLID: `@Component`/`@Service` classes follow SRP. `ApplicationContext` depends on abstractions (DIP). `BeanFactory` is open for extension via `BeanPostProcessor` (OCP). Spring Security uses strategy pattern for authentication (OCP + SRP).

### How JDK Uses This

The JDK's Collections framework follows SOLID: `List`, `Set`, `Map` are focused interfaces (ISP). `ArrayList`, `LinkedList` are interchangeable (LSP). `Collections.sort()` depends on `Comparator` (DIP). `Collections.unmodifiableList()` extends behavior without modification (OCP).

### Enterprise Usage

Enterprise applications apply SOLID through layered architecture (SRP), dependency injection frameworks like Spring/Guice (DIP), strategy patterns for business rules (OCP), adapter patterns for third-party integration (OCP + LSP), and API gateway pattern (ISP).

## References

- [Clean Code by Robert C. Martin](https://www.oreilly.com/library/view/clean-code/9780136083238/)
- [Agile Software Development by Robert C. Martin](https://www.oreilly.com/library/view/agile-software-development/9780135974445/)
- [Baeldung — SOLID Principles](https://www.baeldung.com/solid-principles)
- [Refactoring Guru — SOLID](https://refactoring.guru/design-patterns/solid)

## Summary

- **SRP**: A class should have only one reason to change — one responsibility
- **OCP**: Open for extension, closed for modification — use abstractions for extensibility
- **LSP**: Subclasses must be substitutable for parent classes without breaking behavior
- **ISP**: Clients should not depend on methods they don't use — prefer small, focused interfaces
- **DIP**: Depend on abstractions, not concretions — inject dependencies via interfaces
- SOLID principles work together — violating one often cascades to others
- Apply pragmatically — prioritize SRP and DIP, then add others as needed

**Next Step**: [32-design-principles](../32-design-principles/)
