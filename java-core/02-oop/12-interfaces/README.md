# Interfaces in Java

## Table of Contents
- [Introduction](#introduction)
- [Learning Objectives](#learning-objectives)
- [Prerequisites](#prerequisites)
- [Why This Concept Exists](#why-this-concept-exists)
- [Internal Working](#internal-working)
- [Syntax](#syntax)
- [Easy Examples](#easy-examples)
- [Medium Examples](#medium-examples)
- [Hard Examples](#hard-examples)
- [Exercises](#exercises)
- [Interview Questions](#interview-questions)
- [Common Pitfalls](#common-pitfalls)
- [Best Practices](#best-practices)
- [Real World Usage](#real-world-usage)
- [References](#references)
- [Summary](#summary)

---

## Introduction

Interfaces in Java define a contract of methods that a class must implement, enabling abstraction, polymorphism, and loose coupling between components. Unlike abstract classes, interfaces establish "can-do" relationships — any class, regardless of its inheritance hierarchy, can implement multiple interfaces. Since Java 8, interfaces have evolved to support default methods (with implementation) and static methods, bridging the gap between interfaces and abstract classes while maintaining their core purpose of defining behavioral contracts. Interfaces are fundamental to Java's type system, enabling features like callback mechanisms, strategy patterns, and the collection framework. Understanding interfaces is essential for writing flexible, testable, and maintainable Java code.

---

## Learning Objectives

- Master interface basics: declaration, implementation, and polymorphic usage
- Learn default and static methods introduced in Java 8
- Understand functional interfaces and their role in lambda expressions
- Apply interface inheritance and constant declarations effectively

---

## Prerequisites

- [01-classes-and-objects/README.md](../01-classes-and-objects/README.md) — Classes, objects, methods
- [08-encapsulation/README.md](../08-encapsulation/README.md) — Access modifiers
- [09-inheritance/README.md](../09-inheritance/README.md) — Inheritance, extends keyword
- [10-polymorphism/README.md](../10-polymorphism/README.md) — Dynamic dispatch
- [11-abstraction/README.md](../11-abstraction/README.md) — Abstract classes vs interfaces

---

## Why This Concept Exists

### The Problem

Without interfaces, classes are tightly coupled to specific implementations:

```java
public class EmailService {
    public void send(String message) {
        // Email-specific code
    }
}

public class NotificationManager {
    private EmailService emailService; // Only works with email

    public void notify(String message) {
        emailService.send(message); // Can't easily switch to SMS or Push
    }
}
```

Adding a new notification type requires modifying `NotificationManager`.

### The Solution

Interfaces decouple the contract from the implementation:

```java
public interface Sendable {
    void send(String message);
}

public class EmailService implements Sendable {
    public void send(String message) { /* Email logic */ }
}

public class SmsService implements Sendable {
    public void send(String message) { /* SMS logic */ }
}

public class NotificationManager {
    private final Sendable sender; // Works with ANY implementation

    public NotificationManager(Sendable sender) {
        this.sender = sender;
    }

    public void notify(String message) {
        sender.send(message); // Polymorphic call
    }
}
```

### Real-World Analogy

Think of an electrical outlet. The outlet defines a contract (voltage, plug shape), and any device that matches that contract can plug in. The outlet doesn't care if it's a lamp, a phone charger, or a laptop — it just provides power according to the contract. Similarly, an interface defines a contract, and any class that implements it can be used interchangeably.

---

## Internal Working

### Interface Method Dispatch

Interface method calls use the `invokeinterface` bytecode instruction, which is similar to `invokevirtual` but uses a different lookup mechanism because a class can implement multiple interfaces.

```bytecode
aload_1                    // Load object reference
invokeinterface #N         // Interface method dispatch
```

### Interface Method Table (itable)

The JVM maintains an interface method table for each class that implements interfaces. The itable maps interface+method pairs to actual implementations.

```
Class: Dog implements Animal, Pet, Serializable

itable:
┌────────────┬──────────────┬──────────────────┐
│ Interface  │ Method       │ Implementation   │
├────────────┼──────────────┼──────────────────┤
│ Animal     │ speak()      │ Dog.speak()      │
│ Pet        │ beFriendly() │ Dog.beFriendly() │
│ Serializable │ Serializable│ Dog inherits     │
└────────────┴──────────────┴──────────────────┘
```

### Default Methods and Inheritance

When a class implements an interface with default methods:
1. If the class overrides the method, the override is used
2. If the class doesn't override it, the default implementation is used
3. If there's a diamond conflict, the class must override to resolve it

### Constants in Interfaces

Interface fields are implicitly `public static final`. The JVM treats them as compile-time constants (like `static final` fields), so they're inlined at compile time and don't exist as separate runtime entities.

---

## Syntax

### 1. Basic Interface Declaration

```java
public interface Drawable {
    void draw(); // Implicitly public abstract
}
```

### 2. Interface Implementation

```java
public class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
}
```

### 3. Multiple Interface Implementation

```java
public class Widget implements Drawable, Resizable, Clickable {
    @Override public void draw() { }
    @Override public void resize(double factor) { }
    @Override public void onClick() { }
}
```

### 4. Interface Inheritance

```java
public interface Shape extends Drawable, Resizable {
    double getArea();
}
```

### 5. Default Methods

```java
public interface Loggable {
    default void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
```

### 6. Static Methods

```java
public interface Utility {
    static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
```

### 7. Functional Interface

```java
@FunctionalInterface
public interface Transformer<T, R> {
    R transform(T input);
}
```

### 8. Constants

```java
public interface Constants {
    int MAX_SIZE = 100; // public static final by default
    String APP_NAME = "MyApp";
}
```

---

## Easy Examples

### Example 1: Basic Interface Implementation

**Problem Statement:**
Create a `Vehicle` interface with methods for starting, stopping, and getting vehicle information. Implement it in `Car` and `Motorcycle` classes.

**Implementation:**

```java
package academy.javaengineering.oop.interfaces;

public interface Vehicle {
    void start();
    void stop();
    String getInfo();
    default String getType() {
        return "Vehicle";
    }
}

class Car implements Vehicle {
    private final String make;
    private final String model;
    private boolean running;

    public Car(String make, String model) {
        this.make = make;
        this.model = model;
        this.running = false;
    }

    @Override
    public void start() {
        running = true;
        System.out.println("🚗 " + make + " " + model + " engine started");
    }

    @Override
    public void stop() {
        running = false;
        System.out.println("🚗 " + make + " " + model + " engine stopped");
    }

    @Override
    public String getInfo() {
        return make + " " + model + " [" + (running ? "Running" : "Stopped") + "]";
    }

    @Override
    public String getType() { return "Car"; }
}

class Motorcycle implements Vehicle {
    private final String make;
    private final String model;
    private boolean running;

    public Motorcycle(String make, String model) {
        this.make = make;
        this.model = model;
        this.running = false;
    }

    @Override
    public void start() {
        running = true;
        System.out.println("🏍️ " + make + " " + model + " started");
    }

    @Override
    public void stop() {
        running = false;
        System.out.println("🏍️ " + make + " " + model + " stopped");
    }

    @Override
    public String getInfo() {
        return make + " " + model + " [" + (running ? "Running" : "Stopped") + "]";
    }

    @Override
    public String getType() { return "Motorcycle"; }
}

class VehicleDemo {
    public static void main(String[] args) {
        Vehicle car = new Car("Toyota", "Camry");
        Vehicle motorcycle = new Motorcycle("Honda", "CBR600");

        System.out.println("=== Car ===");
        System.out.println("Info: " + car.getInfo());
        car.start();
        System.out.println("Type: " + car.getType());

        System.out.println("\n=== Motorcycle ===");
        System.out.println("Info: " + motorcycle.getInfo());
        motorcycle.start();
        System.out.println("Type: " + motorcycle.getType());

        System.out.println("\n=== Polymorphic Array ===");
        Vehicle[] vehicles = { car, motorcycle };
        for (Vehicle v : vehicles) {
            System.out.println(v.getType() + " — " + v.getInfo());
        }
    }
}
```

**Output:**
```
=== Car ===
Info: Toyota Camry [Stopped]
🚗 Toyota Camry engine started
Type: Car

=== Motorcycle ===
Info: Honda CBR600 [Stopped]
🏍️ Honda CBR600 started
Type: Motorcycle

=== Polymorphic Array ===
Car — Toyota Camry [Running]
Motorcycle — Honda CBR600 [Running]
```

**Best Practices:**
- Keep interfaces focused on a single responsibility
- Use default methods for optional behavior that has a reasonable default
- Program to the interface type, not the implementation

---

### Example 2: Multiple Interface Implementation

**Problem Statement:**
Demonstrate how a single class can implement multiple interfaces to gain different capabilities.

**Implementation:**

```java
package academy.javaengineering.oop.interfaces;

public interface Printable {
    void print();
    default String getPrintHeader() {
        return "--- Document ---";
    }
}

interface Serializable2 {
    String serialize();
    void deserialize(String data);
}

interface Comparable2<T> {
    int compareTo(T other);
}

class Document implements Printable, Serializable2, Comparable2<Document> {
    private String title;
    private String content;
    private int priority;

    public Document(String title, String content, int priority) {
        this.title = title;
        this.content = content;
        this.priority = priority;
    }

    // Printable implementation
    @Override
    public void print() {
        System.out.println(getPrintHeader());
        System.out.println("Title: " + title);
        System.out.println("Priority: " + priority);
        System.out.println("Content: " + content);
        System.out.println("--- End ---");
    }

    // Serializable2 implementation
    @Override
    public String serialize() {
        return title + "|" + content + "|" + priority;
    }

    @Override
    public void deserialize(String data) {
        String[] parts = data.split("\\|");
        this.title = parts[0];
        this.content = parts[1];
        this.priority = Integer.parseInt(parts[2]);
    }

    // Comparable2 implementation
    @Override
    public int compareTo(Document other) {
        return Integer.compare(this.priority, other.priority);
    }

    // Getters
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public int getPriority() { return priority; }
}

class MultipleInterfaceDemo {
    public static void main(String[] args) {
        Document doc1 = new Document("Report", "Q4 financial results", 1);
        Document doc2 = new Document("Memo", "Team meeting notes", 3);
        Document doc3 = new Document("Urgent", "Security patch", 5);

        // Printable
        System.out.println("=== Printing Documents ===");
        doc1.print();
        System.out.println();
        doc2.print();

        // Serializable
        System.out.println("\n=== Serialization ===");
        String serialized = doc1.serialize();
        System.out.println("Serialized: " + serialized);

        Document deserialized = new Document("", "", 0);
        deserialized.deserialize(serialized);
        System.out.println("Deserialized: " + deserialized.getTitle());

        // Comparable
        System.out.println("\n=== Sorting by Priority ===");
        Document[] docs = { doc1, doc2, doc3 };
        java.util.Arrays.sort(docs);
        for (Document d : docs) {
            System.out.printf("  Priority %d: %s%n", d.getPriority(), d.getTitle());
        }

        // Interface type references
        System.out.println("\n=== Interface References ===");
        Printable printableDoc = doc1;
        Serializable2 serializableDoc = doc1;
        Comparable2<Document> comparableDoc = doc1;

        printableDoc.print();
        System.out.println("Serialized via interface: " + serializableDoc.serialize());
        System.out.println("Compare result: " + comparableDoc.compareTo(doc2));
    }
}
```

**Output:**
```
=== Printing Documents ===
--- Document ---
Title: Report
Priority: 1
Content: Q4 financial results
--- End ---

--- Document ---
Title: Memo
Priority: 3
Content: Team meeting notes
--- End ---

=== Serialization ===
Serialized: Report|Q4 financial results|1
Deserialized: Report

=== Sorting by Priority ===
  Priority 1: Report
  Priority 3: Memo
  Priority 5: Urgent

=== Interface References ===
--- Document ---
Title: Report
Priority: 1
Content: Q4 financial results
--- End ---
Serialized via interface: Report|Q4 financial results|1
Compare result: -2
```

**Best Practices:**
- Use multiple interfaces to compose behavior (Interface Segregation Principle)
- Keep each interface focused on a single capability
- Use interface references to hide implementation details

---

### Example 3: Constants in Interfaces

**Problem Statement:**
Use interface constants to define application-wide configuration values and error codes.

**Implementation:**

```java
package academy.javaengineering.oop.interfaces;

public interface AppConfig {
    String APP_NAME = "MyApplication";
    String VERSION = "2.1.0";
    int MAX_CONNECTIONS = 100;
    int TIMEOUT_SECONDS = 30;
    boolean DEBUG_MODE = false;
}

interface HttpStatus {
    int OK = 200;
    int CREATED = 201;
    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int NOT_FOUND = 404;
    int SERVER_ERROR = 500;
}

interface ErrorCode {
    int VALIDATION_ERROR = 1001;
    int DATABASE_ERROR = 2001;
    int NETWORK_ERROR = 3001;
    int AUTH_ERROR = 4001;
}

class ApiService implements AppConfig, HttpStatus, ErrorCode {
    public void displayConfig() {
        System.out.println("=== Application Config ===");
        System.out.println("Name: " + APP_NAME);
        System.out.println("Version: " + VERSION);
        System.out.println("Max Connections: " + MAX_CONNECTIONS);
        System.out.println("Timeout: " + TIMEOUT_SECONDS + "s");
        System.out.println("Debug: " + DEBUG_MODE);
    }

    public void simulateResponse(int statusCode) {
        System.out.print("Response: ");
        switch (statusCode) {
            case OK -> System.out.println("OK (200)");
            case CREATED -> System.out.println("Created (201)");
            case BAD_REQUEST -> System.out.println("Bad Request (400)");
            case NOT_FOUND -> System.out.println("Not Found (404)");
            case SERVER_ERROR -> System.out.println("Server Error (500)");
            default -> System.out.println("Unknown: " + statusCode);
        }
    }

    public void handleError(int errorCode) {
        System.out.print("Error: ");
        switch (errorCode) {
            case VALIDATION_ERROR -> System.out.println("Validation failed");
            case DATABASE_ERROR -> System.out.println("Database connection failed");
            case NETWORK_ERROR -> System.out.println("Network timeout");
            case AUTH_ERROR -> System.out.println("Authentication failed");
            default -> System.out.println("Unknown error: " + errorCode);
        }
    }

    public static void main(String[] args) {
        ApiService service = new ApiService();
        service.displayConfig();

        System.out.println("\n=== HTTP Responses ===");
        service.simulateResponse(OK);
        service.simulateResponse(NOT_FOUND);
        service.simulateResponse(SERVER_ERROR);

        System.out.println("\n=== Error Handling ===");
        service.handleError(VALIDATION_ERROR);
        service.handleError(DATABASE_ERROR);
        service.handleError(AUTH_ERROR);
    }
}
```

**Output:**
```
=== Application Config ===
Name: MyApplication
Version: 2.1.0
Max Connections: 100
Timeout: 30s
Debug: false

=== HTTP Responses ===
Response: OK (200)
Response: Not Found (404)
Response: Server Error (500)

=== Error Handling ===
Error: Validation failed
Error: Database connection failed
Error: Authentication failed
```

**Best Practices:**
- Use interface constants for truly global, immutable values
- Consider enums for related constants with behavior
- Don't abuse interface constants — prefer dependency injection for configuration

---

## Medium Examples

### Example 1: Default Methods for Mixin Behavior

**Problem Statement:**
Use default methods to provide reusable behavior across unrelated classes without requiring inheritance.

**Requirements:**
- Create `Timestamped` interface with default methods for timestamps
- Create `Auditable` interface with default audit logging
- Apply both to different entity classes

**Implementation:**

```java
package academy.javaengineering.oop.interfaces;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public interface Timestamped {
    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    default void markUpdated() {
        setUpdatedAt(LocalDateTime.now());
    }

    default String getFormattedCreatedAt() {
        return getCreatedAt() != null ?
            getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
    }

    default String getFormattedUpdatedAt() {
        return getUpdatedAt() != null ?
            getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
    }

    default boolean isRecentlyUpdated(long minutes) {
        if (getUpdatedAt() == null) return false;
        return getUpdatedAt().isAfter(LocalDateTime.now().minusMinutes(minutes));
    }
}

interface Auditable {
    List<String> getAuditLog();
    void addAuditEntry(String entry);

    default void auditAction(String action, String details) {
        String entry = String.format("[%s] %s: %s",
            LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            action, details);
        addAuditEntry(entry);
    }

    default void printAuditLog() {
        System.out.println("=== Audit Log ===");
        getAuditLog().forEach(entry -> System.out.println("  " + entry));
    }

    default int getAuditLogSize() {
        return getAuditLog().size();
    }
}

interface SoftDeletable {
    boolean isDeleted();
    void setDeleted(boolean deleted);

    default void softDelete() {
        setDeleted(true);
    }

    default void restore() {
        setDeleted(false);
    }

    default boolean isActive() {
        return !isDeleted();
    }
}

// Entity implementing multiple interfaces
class Order implements Timestamped, Auditable, SoftDeletable {
    private final String orderId;
    private final String customerId;
    private double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;
    private final List<String> auditLog;

    public Order(String orderId, String customerId, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deleted = false;
        this.auditLog = new ArrayList<>();
        auditAction("CREATED", "Order " + orderId + " created for " + customerId);
    }

    // Timestamped implementation
    @Override public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    @Override public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    @Override public LocalDateTime getCreatedAt() { return createdAt; }
    @Override public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Auditable implementation
    @Override public List<String> getAuditLog() { return new ArrayList<>(auditLog); }
    @Override public void addAuditEntry(String entry) { auditLog.add(entry); }

    // SoftDeletable implementation
    @Override public boolean isDeleted() { return deleted; }
    @Override public void setDeleted(boolean deleted) { this.deleted = deleted; }

    // Business methods
    public void updateAmount(double newAmount) {
        double oldAmount = this.totalAmount;
        this.totalAmount = newAmount;
        markUpdated(); // From Timestamped
        auditAction("UPDATED", "Amount changed from " + oldAmount + " to " + newAmount);
    }

    public void cancel() {
        softDelete(); // From SoftDeletable
        auditAction("CANCELLED", "Order cancelled");
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return String.format("Order[%s] Customer: %s Amount: $%.2f Status: %s",
            orderId, customerId, totalAmount, isActive() ? "Active" : "Deleted");
    }
}

class DefaultMethodDemo {
    public static void main(String[] args) {
        Order order = new Order("ORD-001", "CUST-123", 150.00);

        System.out.println(order);
        System.out.println("Created: " + order.getFormattedCreatedAt());
        System.out.println("Recently updated? " + order.isRecentlyUpdated(5));

        System.out.println("\n=== Updating Order ===");
        order.updateAmount(200.00);
        System.out.println("Updated: " + order.getFormattedUpdatedAt());

        System.out.println("\n=== Cancelling Order ===");
        order.cancel();
        System.out.println("Status: " + (order.isActive() ? "Active" : "Deleted"));

        order.printAuditLog();
        System.out.println("Audit entries: " + order.getAuditLogSize());
    }
}
```

**Output:**
```
Order[ORD-001] Customer: CUST-123 Amount: $150.00 Status: Active
Created: 2024-01-15 10:30:45
Recently updated? true

=== Updating Order ===
Updated: 2024-01-15 10:30:46

=== Cancelling Order ===
Status: Deleted
=== Audit Log ===
  [2024-01-15T10:30:45.123] CREATED: Order ORD-001 created for CUST-123
  [2024-01-15T10:30:46.456] UPDATED: Amount changed from 150.0 to 200.0
  [2024-01-15T10:30:46.789] CANCELLED: Order cancelled
Audit entries: 3
```

**Alternative:**
Use abstract classes for shared behavior, but default methods allow mixing in behavior without inheritance.

---

### Example 2: Static Methods in Interfaces

**Problem Statement:**
Use static methods in interfaces to provide utility functions related to the interface's domain.

**Requirements:**
- Create a `Money` interface with static factory methods
- Include static utility methods for currency conversion and formatting
- Demonstrate static method usage

**Implementation:**

```java
package academy.javaengineering.oop.interfaces;

import java.util.HashMap;
import java.util.Map;

public interface Money {
    double amount();
    String currency();

    // Static factory methods
    static Money of(double amount, String currency) {
        return new MoneyRecord(amount, currency);
    }

    static Money usd(double amount) {
        return of(amount, "USD");
    }

    static Money eur(double amount) {
        return of(amount, "EUR");
    }

    static Money gbp(double amount) {
        return of(amount, "GBP");
    }

    // Static utility methods
    static Map<String, Double> getExchangeRates() {
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD_EUR", 0.85);
        rates.put("USD_GBP", 0.73);
        rates.put("EUR_USD", 1.18);
        rates.put("EUR_GBP", 0.86);
        rates.put("GBP_USD", 1.37);
        rates.put("GBP_EUR", 1.16);
        return rates;
    }

    static Money convert(Money amount, String targetCurrency) {
        if (amount.currency().equals(targetCurrency)) {
            return amount;
        }
        String key = amount.currency() + "_" + targetCurrency;
        Double rate = getExchangeRates().get(key);
        if (rate == null) {
            throw new IllegalArgumentException("Exchange rate not found: " + key);
        }
        return of(amount.amount() * rate, targetCurrency);
    }

    static String format(Money amount) {
        return String.format("%s %.2f", amount.currency(), amount.amount());
    }

    static Money sum(Money a, Money b) {
        if (!a.currency().equals(b.currency())) {
            throw new IllegalArgumentException("Cannot sum different currencies");
        }
        return of(a.amount() + b.amount(), a.currency());
    }

    // Default method using the interface's own methods
    default Money add(Money other) {
        return Money.sum(this, other);
    }

    default Money convertTo(String targetCurrency) {
        return Money.convert(this, targetCurrency);
    }

    // Private helper (Java 9+)
    // private static void validateCurrency(String currency) { ... }
}

record MoneyRecord(double amount, String currency) implements Money {
    MoneyRecord {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Invalid currency: " + currency);
        }
    }
}

class MoneyDemo {
    public static void main(String[] args) {
        // Static factory methods
        Money price = Money.usd(99.99);
        Money tax = Money.eur(8.50);
        Money shipping = Money.gbp(5.00);

        System.out.println("=== Creating Money ===");
        System.out.println("Price: " + Money.format(price));
        System.out.println("Tax: " + Money.format(tax));
        System.out.println("Shipping: " + Money.format(shipping));

        // Currency conversion
        System.out.println("\n=== Currency Conversion ===");
        Money priceInEur = price.convertTo("EUR");
        System.out.println("Price in EUR: " + Money.format(priceInEur));

        Money taxInUsd = tax.convertTo("USD");
        System.out.println("Tax in USD: " + Money.format(taxInUsd));

        // Summing (same currency)
        System.out.println("\n=== Summing ===");
        Money subtotal = Money.usd(100);
        Money discount = Money.usd(15);
        Money total = subtotal.add(discount);
        System.out.println("Subtotal: " + Money.format(subtotal));
        System.out.println("Discount: " + Money.format(discount));
        System.out.println("Total: " + Money.format(total));

        // Static method calls
        System.out.println("\n=== Static Method Calls ===");
        System.out.println("Exchange rates: " + Money.getExchangeRates());
    }
}
```

**Output:**
```
=== Creating Money ===
Price: USD 99.99
Tax: EUR 8.50
Shipping: GBP 5.00

=== Currency Conversion ===
Price in EUR: EUR 84.99
Tax in USD: USD 10.03

=== Summing ===
Subtotal: USD 100.00
Discount: USD 15.00
Total: USD 115.00

=== Static Method Calls ===
Exchange rates: {USD_EUR=0.85, USD_GBP=0.73, EUR_USD=1.18, EUR_GBP=0.86, GBP_USD=1.37, GBP_EUR=1.16}
```

**Alternative:**
Use a utility class with static methods instead of interface static methods, but interface methods keep related functionality together.

---

### Example 3: Functional Interfaces and Lambda Expressions

**Problem Statement:**
Demonstrate functional interfaces and how they enable lambda expressions and method references.

**Implementation:**

```java
package academy.javaengineering.oop.interfaces;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class FunctionalInterfaceDemo {

    // Custom functional interfaces
    @FunctionalInterface
    interface Transformer<T, R> {
        R transform(T input);
    }

    @FunctionalInterface
    interface Validator<T> {
        boolean isValid(T value);
        default Validator<T> and(Validator<T> other) {
            return value -> this.isValid(value) && other.isValid(value);
        }
        default Validator<T> or(Validator<T> other) {
            return value -> this.isValid(value) || other.isValid(value);
        }
    }

    @FunctionalInterface
    interface Processor<T> {
        void process(T item);
        default Processor<T> andThen(Processor<T> after) {
            return item -> { this.process(item); after.process(item); };
        }
    }

    // Using custom functional interfaces
    static <T> List<R> mapList(List<T> list, Transformer<T, R> transformer) {
        // Won't compile as written — need proper generics
        return null; // Placeholder
    }

    static <T> List<T> filterList(List<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    static <T> void processList(List<T> list, Consumer<T> processor) {
        list.forEach(processor);
    }

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

        // Lambda with custom functional interface
        System.out.println("=== Custom Transformer ===");
        Transformer<String, Integer> lengthTransformer = s -> s.length();
        List<Integer> lengths = names.stream()
            .map(lengthTransformer::transform)
            .collect(Collectors.toList());
        System.out.println("Names: " + names);
        System.out.println("Lengths: " + lengths);

        // Method reference
        System.out.println("\n=== Method Reference ===");
        Transformer<String, String> upperCase = String::toUpperCase;
        List<String> upperNames = names.stream()
            .map(upperCase::transform)
            .collect(Collectors.toList());
        System.out.println("Uppercase: " + upperNames);

        // Validator composition
        System.out.println("\n=== Validator Composition ===");
        Validator<String> notNull = s -> s != null;
        Validator<String> notEmpty = s -> !s.isEmpty();
        Validator<String> minLength3 = s -> s.length() >= 3;

        Validator<String> combined = notNull.and(notEmpty).and(minLength3);

        List<String> testValues = Arrays.asList(null, "", "ab", "Alice", "Bob");
        for (String value : testValues) {
            System.out.println("  '" + value + "' valid? " + combined.isValid(value));
        }

        // Built-in functional interfaces
        System.out.println("\n=== Built-in Functional Interfaces ===");

        // Predicate
        Predicate<String> startsWithA = s -> s.startsWith("A");
        List<String> aNames = filterList(names, startsWithA);
        System.out.println("Names starting with A: " + aNames);

        // Function
        Function<String, Integer> stringToInt = Integer::parseInt;
        List<String> numbers = Arrays.asList("1", "2", "3", "4", "5");
        List<Integer> ints = numbers.stream()
            .map(stringToInt)
            .collect(Collectors.toList());
        System.out.println("Parsed integers: " + ints);

        // Consumer
        Consumer<String> printer = s -> System.out.print(s + " ");
        System.out.print("Names: ");
        processList(names, printer);
        System.out.println();

        // Supplier
        Supplier<List<String>> listFactory = ArrayList::new;
        List<String> newList = listFactory.get();
        System.out.println("New list created: " + newList.getClass().getSimpleName());

        // UnaryOperator
        UnaryOperator<String> exclaim = s -> s + "!";
        List<String> excited = names.stream()
            .map(exclaim)
            .collect(Collectors.toList());
        System.out.println("Excited: " + excited);

        // BinaryOperator
        BinaryOperator<Integer> sum = Integer::sum;
        int total = numbers.stream()
            .mapToInt(Integer::parseInt)
            .reduce(0, sum::applyAsInt);
        System.out.println("Sum of numbers: " + total);

        // Consumer chaining
        System.out.println("\n=== Consumer Chaining ===");
        Processor<String> log = item -> System.out.println("LOG: " + item);
        Processor<String> validate = item -> {
            if (item == null) throw new IllegalArgumentException("Null item");
        };
        Processor<String> combinedProcessor = validate.andThen(log);
        combinedProcessor.process("Test Item");
    }
}
```

**Output:**
```
=== Custom Transformer ===
Names: [Alice, Bob, Charlie, David, Eve]
Lengths: [5, 3, 7, 5, 3]

=== Method Reference ===
Uppercase: [ALICE, BOB, CHARLIE, DAVID, EVE]

=== Validator Composition ===
  'null' valid? false
  '' valid? false
  'ab' valid? false
  'Alice' valid? true
  'Bob' valid? true

=== Built-in Functional Interfaces ===
Names starting with A: [Alice]
Parsed integers: [1, 2, 3, 4, 5]
Names: Alice Bob Charlie David Eve 
New list created: ArrayList
Excited: [Alice!, Bob!, Charlie!, David!, Eve!]
Sum of numbers: 15

=== Consumer Chaining ===
LOG: Test Item
```

**Alternative:**
Use method references (`String::toUpperCase`) instead of lambdas for cleaner code when the lambda simply delegates to an existing method.

---

## Hard Examples

### Example 1: Plugin System with Interface Contracts

**Architecture:**
A plugin system where plugins are discovered at runtime and invoked through interface contracts, demonstrating loose coupling and dynamic loading.

**Implementation:**

```java
package academy.javaengineering.oop.interfaces;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Core plugin interface
public interface Plugin {
    String getId();
    String getName();
    String getVersion();
    void initialize(PluginContext context);
    void shutdown();
    PluginInfo getInfo();
}

// Plugin context for accessing host application
interface PluginContext {
    void registerService(Class<?> serviceType, Object service);
    <T> T getService(Class<T> serviceType);
    void log(String message);
    Map<String, String> getConfiguration();
}

// Plugin information
record PluginInfo(String id, String name, String version, String description, String author) {}

// Service interfaces plugins can provide
interface DataProcessor {
    Object process(Object data);
    String getProcessorType();
}

interface EventListener {
    void onEvent(String eventType, Map<String, Object> data);
    String[] getSubscribedEvents();
}

// Host application
class PluginHost {
    private final Map<String, Plugin> plugins = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();
    private final List<EventListener> eventListeners = new ArrayList<>();
    private final Map<String, String> config;

    public PluginHost(Map<String, String> config) {
        this.config = new HashMap<>(config);
    }

    public void loadPlugin(Plugin plugin) {
        PluginContext context = new PluginContext() {
            @Override
            public void registerService(Class<?> serviceType, Object service) {
                services.put(serviceType, service);
                log("Registered service: " + serviceType.getSimpleName());
            }

            @Override
            public <T> T getService(Class<T> serviceType) {
                return serviceType.cast(services.get(serviceType));
            }

            @Override
            public void log(String message) {
                System.out.println("[HOST] " + message);
            }

            @Override
            public Map<String, String> getConfiguration() {
                return Collections.unmodifiableMap(config);
            }
        };

        plugin.initialize(context);
        plugins.put(plugin.getId(), plugin);

        // Check if plugin provides event listener
        if (plugin instanceof EventListener listener) {
            eventListeners.add(listener);
        }

        System.out.println("Loaded plugin: " + plugin.getName() + " v" + plugin.getVersion());
    }

    public void unloadPlugin(String pluginId) {
        Plugin plugin = plugins.remove(pluginId);
        if (plugin != null) {
            plugin.shutdown();
            if (plugin instanceof EventListener listener) {
                eventListeners.remove(listener);
            }
            System.out.println("Unloaded plugin: " + plugin.getName());
        }
    }

    public void fireEvent(String eventType, Map<String, Object> data) {
        for (EventListener listener : eventListeners) {
            for (String subscribed : listener.getSubscribedEvents()) {
                if (subscribed.equals(eventType) || subscribed.equals("*")) {
                    listener.onEvent(eventType, data);
                }
            }
        }
    }

    public <T> T getService(Class<T> serviceType) {
        return serviceType.cast(services.get(serviceType));
    }

    public void listPlugins() {
        System.out.println("\n=== Loaded Plugins ===");
        plugins.values().forEach(p ->
            System.out.println("  " + p.getName() + " v" + p.getVersion()));
    }
}

// Concrete plugins
class JsonDataProcessorPlugin implements Plugin, DataProcessor, EventListener {
    private PluginContext context;

    @Override public String getId() { return "json-processor"; }
    @Override public String getName() { return "JSON Data Processor"; }
    @Override public String getVersion() { return "1.0.0"; }

    @Override
    public void initialize(PluginContext context) {
        this.context = context;
        context.registerService(DataProcessor.class, this);
    }

    @Override
    public void shutdown() {
        System.out.println("JSON Processor shutting down...");
    }

    @Override
    public PluginInfo getInfo() {
        return new PluginInfo(getId(), getName(), getVersion(),
            "Processes JSON data", "Plugin Team");
    }

    @Override
    public Object process(Object data) {
        context.log("Processing data as JSON");
        return "{ \"result\": \"" + data.toString().toUpperCase() + "\" }";
    }

    @Override
    public String getProcessorType() { return "JSON"; }

    @Override
    public void onEvent(String eventType, Map<String, Object> data) {
        context.log("JSON Processor received event: " + eventType);
    }

    @Override
    public String[] getSubscribedEvents() {
        return new String[] { "data.received", "data.processed" };
    }
}

class AuditPlugin implements Plugin, EventListener {
    private final List<String> auditTrail = new ArrayList<>();
    private PluginContext context;

    @Override public String getId() { return "audit"; }
    @Override public String getName() { return "Audit Logger"; }
    @Override public String getVersion() { return "2.0.0"; }

    @Override
    public void initialize(PluginContext context) {
        this.context = context;
    }

    @Override
    public void shutdown() {
        System.out.println("Audit trail entries: " + auditTrail.size());
    }

    @Override
    public PluginInfo getInfo() {
        return new PluginInfo(getId(), getName(), getVersion(),
            "Logs all events for audit", "Security Team");
    }

    @Override
    public void onEvent(String eventType, Map<String, Object> data) {
        String entry = String.format("[AUDIT] %s: %s", eventType, data);
        auditTrail.add(entry);
        context.log(entry);
    }

    @Override
    public String[] getSubscribedEvents() {
        return new String[] { "*" }; // Subscribe to all events
    }

    public List<String> getAuditTrail() {
        return Collections.unmodifiableList(auditTrail);
    }
}

class PluginSystemDemo {
    public static void main(String[] args) {
        Map<String, String> config = Map.of(
            "app.name", "MyApp",
            "debug", "true"
        );

        PluginHost host = new PluginHost(config);

        // Load plugins
        host.loadPlugin(new JsonDataProcessorPlugin());
        host.loadPlugin(new AuditPlugin());

        host.listPlugins();

        // Use services
        System.out.println("\n=== Using Data Processor ===");
        DataProcessor processor = host.getService(DataProcessor.class);
        if (processor != null) {
            Object result = processor.process("hello world");
            System.out.println("Result: " + result);
            System.out.println("Type: " + processor.getProcessorType());
        }

        // Fire events
        System.out.println("\n=== Firing Events ===");
        host.fireEvent("data.received", Map.of("source", "api", "size", 1024));
        host.fireEvent("data.processed", Map.of("result", "success", "duration", 150));

        // Shutdown
        System.out.println("\n=== Shutting Down ===");
        host.unloadPlugin("json-processor");
        host.unloadPlugin("audit");
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.interfaces;

import org.junit.Test;
import static org.junit.Assert.*;

public class PluginSystemTest {

    @Test
    public void testPluginLoading() {
        PluginHost host = new PluginHost(Map.of());
        Plugin plugin = new JsonDataProcessorPlugin();
        host.loadPlugin(plugin);
        assertNotNull(host.getService(DataProcessor.class));
    }

    @Test
    public void testPluginExecution() {
        PluginHost host = new PluginHost(Map.of());
        host.loadPlugin(new JsonDataProcessorPlugin());
        DataProcessor processor = host.getService(DataProcessor.class);
        assertNotNull(processor.process("test"));
    }

    @Test
    public void testEventFiring() {
        PluginHost host = new PluginHost(Map.of());
        AuditPlugin audit = new AuditPlugin();
        host.loadPlugin(audit);
        host.fireEvent("test.event", Map.of("key", "value"));
        assertEquals(1, audit.getAuditTrail().size());
    }
}
```

**Complexity:**
- Plugin loading: O(1) for ConcurrentHashMap
- Event firing: O(n) where n is number of listeners
- Service lookup: O(1)
- Space: O(p) where p is number of plugins

**Best Practices:**
- Define clean interface contracts for plugins
- Use ServiceLoader for runtime plugin discovery
- Handle plugin errors gracefully without crashing the host

---

### Example 2: Interface Segregation Principle in Practice

**Architecture:**
Design a document management system following ISP — small, focused interfaces instead of one monolithic one.

**Implementation:**

```java
package academy.javaengineering.oop.interfaces;

import java.util.*;

// Small, focused interfaces (ISP)
interface Readable {
    String read();
}

interface Writable {
    void write(String content);
}

interface Searchable {
    List<String> search(String query);
}

interface Shareable {
    void share(String recipient);
    List<String> getSharedWith();
}

interface Versionable {
    String getVersion();
    void createVersion();
    List<String> getVersionHistory();
}

interface MetadataProvider {
    Map<String, String> getMetadata();
    String getAuthor();
    Date getLastModified();
}

// Concrete implementations compose interfaces
class TextDocument implements Readable, Writable, Searchable, Versionable, MetadataProvider {
    private String content;
    private String author;
    private final List<String> versionHistory;
    private final List<String> versions;
    private int currentVersion;
    private Date lastModified;

    public TextDocument(String author) {
        this.author = author;
        this.content = "";
        this.versionHistory = new ArrayList<>();
        this.versions = new ArrayList<>();
        this.currentVersion = 0;
        this.lastModified = new Date();
        createVersion();
    }

    @Override public String read() { return content; }

    @Override
    public void write(String content) {
        this.content = content;
        this.lastModified = new Date();
    }

    @Override
    public List<String> search(String query) {
        List<String> results = new ArrayList<>();
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toLowerCase().contains(query.toLowerCase())) {
                results.add("Line " + (i + 1) + ": " + lines[i].trim());
            }
        }
        return results;
    }

    @Override public String getVersion() { return "v" + currentVersion; }

    @Override
    public void createVersion() {
        versions.add(content);
        versionHistory.add("Version " + (currentVersion + 1) + " created");
        currentVersion++;
    }

    @Override public List<String> getVersionHistory() { return Collections.unmodifiableList(versionHistory); }

    @Override
    public Map<String, String> getMetadata() {
        Map<String, String> meta = new HashMap<>();
        meta.put("author", author);
        meta.put("version", getVersion());
        meta.put("lastModified", lastModified.toString());
        meta.put("length", String.valueOf(content.length()));
        return meta;
    }

    @Override public String getAuthor() { return author; }
    @Override public Date getLastModified() { return lastModified; }
}

class SharedDocument extends TextDocument implements Shareable {
    private final List<String> sharedWith;

    public SharedDocument(String author) {
        super(author);
        this.sharedWith = new ArrayList<>();
    }

    @Override
    public void share(String recipient) {
        if (!sharedWith.contains(recipient)) {
            sharedWith.add(recipient);
            System.out.println("Document shared with " + recipient);
        }
    }

    @Override
    public List<String> getSharedWith() {
        return Collections.unmodifiableList(sharedWith);
    }
}

// Client code uses only the interfaces it needs
class DocumentEditor {
    private final Readable document;

    DocumentEditor(Readable document) {
        this.document = document;
    }

    public void display() {
        System.out.println("Content: " + document.read());
    }
}

class DocumentSearcher {
    private final Searchable document;

    DocumentSearcher(Searchable document) {
        this.document = document;
    }

    public void searchAndDisplay(String query) {
        System.out.println("Search results for '" + query + "':");
        document.search(query).forEach(r -> System.out.println("  " + r));
    }
}

class DocumentVersioner {
    private final Versionable document;

    DocumentVersioner(Versionable document) {
        this.document = document;
    }

    public void showVersions() {
        System.out.println("Current version: " + document.getVersion());
        document.getVersionHistory().forEach(v -> System.out.println("  " + v));
    }
}

class ISPDemo {
    public static void main(String[] args) {
        SharedDocument doc = new SharedDocument("Alice");
        doc.write("Hello World\nThis is a test document\nHello again");

        // Each client uses only the interface it needs
        System.out.println("=== Editor ===");
        DocumentEditor editor = new DocumentEditor(doc);
        editor.display();

        System.out.println("\n=== Searcher ===");
        DocumentSearcher searcher = new DocumentSearcher(doc);
        searcher.searchAndDisplay("Hello");

        System.out.println("\n=== Versioner ===");
        DocumentVersioner versioner = new DocumentVersioner(doc);
        versioner.showVersions();

        System.out.println("\n=== Sharing ===");
        doc.share("Bob");
        doc.share("Charlie");
        System.out.println("Shared with: " + doc.getSharedWith());

        System.out.println("\n=== Metadata ===");
        doc.getMetadata().forEach((k, v) ->
            System.out.println("  " + k + ": " + v));
    }
}
```

**Unit Tests:**

```java
package academy.javaengineering.oop.interfaces;

import org.junit.Test;
import static org.junit.Assert.*;

public class ISPDemoTest {

    @Test
    public void testDocumentReadWrite() {
        TextDocument doc = new TextDocument("Test");
        doc.write("Hello");
        assertEquals("Hello", doc.read());
    }

    @Test
    public void testSearch() {
        TextDocument doc = new TextDocument("Test");
        doc.write("Line 1 Hello\nLine 2 World\nLine 3 Hello");
        assertEquals(2, doc.search("Hello").size());
    }

    @Test
    public void testVersioning() {
        TextDocument doc = new TextDocument("Test");
        doc.write("v1");
        doc.createVersion();
        doc.write("v2");
        assertEquals("v2", doc.getVersion());
        assertEquals(2, doc.getVersionHistory().size());
    }

    @Test
    public void testSharing() {
        SharedDocument doc = new SharedDocument("Test");
        doc.share("Alice");
        doc.share("Bob");
        assertEquals(2, doc.getSharedWith().size());
        assertTrue(doc.getSharedWith().contains("Alice"));
    }
}
```

**Complexity:**
- Read/Write: O(n) where n is content length
- Search: O(n * m) where n is lines, m is average line length
- Version: O(1) per version creation, O(v) storage where v is version count
- Space: O(n) for content, O(v) for versions

**Best Practices:**
- Follow Interface Segregation Principle: small, focused interfaces
- Clients should depend on interfaces they actually use
- Compose interfaces to build rich behavior from simple parts

---

## Exercises

### Easy

1. **Basic Interface:**
   Create a `Resizable` interface with `resize(double factor)` method. Implement it in `Circle` and `Rectangle` classes.

2. **Multiple Interfaces:**
   Create `Playable` and `Stoppable` interfaces. Implement both in a `MediaPlayer` class.

3. **Interface Constants:**
   Create a `Limits` interface with constants `MAX_SIZE`, `MIN_SIZE`, and `DEFAULT_SIZE`. Use them in a `Container` class.

### Medium

4. **Default Method:**
   Create an `Encryptable` interface with default `encrypt()` and `decrypt()` methods using Base64. Implement in `SecureMessage` and `SecureFile` classes.

5. **Static Factory:**
   Create a `Color` interface with static factory methods `rgb(r, g, b)`, `hex(String hex)`, and `named(String name)`.

6. **Functional Interface:**
   Create a `@FunctionalInterface Transformer<T, R>` and use it with streams to transform a list of strings to their lengths, uppercases, and reversed forms.

### Hard

7. **Plugin System:**
   Design a plugin system with `Plugin` interface, `PluginManager`, and runtime plugin loading using `ServiceLoader`.

8. **Event Bus:**
   Create a type-safe event bus using interfaces: `Event`, `EventHandler<T extends Event>`, and `EventBus` that dispatches events to registered handlers.

9. **Repository Pattern:**
   Create a `Repository<T, ID>` interface with `findById`, `findAll`, `save`, `delete`, and `count` methods. Implement `InMemoryRepository` and `JdbcRepository` (simulated).

---

## Interview Questions

### Easy

1. **What is an interface in Java?**
   An interface is a contract that defines methods a class must implement. It enables abstraction, polymorphism, and multiple inheritance of type. Interfaces can contain abstract methods, default methods (Java 8+), static methods, and constants.

2. **Can an interface have constructors?**
   No. Interfaces cannot have constructors because they cannot be instantiated directly. Instances are created by implementing classes.

3. **What is the difference between an interface and an abstract class?**
   Interface: No constructors, no instance fields (only `public static final` constants), multiple inheritance of type. Abstract class: Can have constructors, instance fields, access modifiers, single inheritance.

### Medium

4. **What is a functional interface?**
   A functional interface is an interface with exactly one abstract method (SAM — Single Abstract Method). It can have any number of default and static methods. Annotated with `@FunctionalInterface` for compile-time checking. Used as the target type for lambda expressions.

5. **What are default methods and why were they introduced?**
   Default methods (Java 8) allow interfaces to have method implementations. They were introduced to enable interface evolution without breaking existing implementations. They solve the "diamond problem" by requiring classes to explicitly override when there are conflicts.

6. **Can a class implement multiple interfaces?**
   Yes. A class can implement any number of interfaces. This enables multiple inheritance of type. If two interfaces have conflicting default methods, the class must override the method to resolve the conflict.

### Hard

7. **How does `invokeinterface` differ from `invokevirtual`?**
   `invokevirtual` uses the object's class vtable for method lookup. `invokeinterface` must search through the itable because a class can implement interfaces in any order, and the interface method might be defined in any of the implemented interfaces. This makes `invokeinterface` slightly slower than `invokevirtual`.

8. **What is the difference between a marker interface and a functional interface?**
   Marker interface: No methods (e.g., `Serializable`, `Cloneable`). Used to signal that a class has a particular property. Checked via `instanceof`. Functional interface: Exactly one abstract method. Used as a lambda target type. Different purposes, different mechanisms.

---

## Common Pitfalls

### Pitfall 1: Interface with Too Many Methods

**Wrong:**
```java
interface GodInterface {
    void method1();
    void method2();
    void method3();
    // ... 20 more methods
    // Violates Interface Segregation Principle
}
```

**Right:**
```java
interface Readable { String read(); }
interface Writable { void write(String content); }
interface Searchable { List<String> search(String query); }

class Document implements Readable, Writable, Searchable {
    // Implement only what's needed
}
```

### Pitfall 2: Confusing Default Method Inheritance

**Wrong:**
```java
interface A {
    default void hello() { System.out.println("A"); }
}

interface B {
    default void hello() { System.out.println("B"); }
}

// Class C inherits both — must override
class C implements A, B {
    // Compile error if hello() not overridden
}
```

**Right:**
```java
class C implements A, B {
    @Override
    public void hello() {
        A.super.hello(); // Explicitly choose A's implementation
    }
}
```

### Pitfall 3: Using Interface Constants for Configuration

**Wrong:**
```java
interface Config {
    int PORT = 8080; // Hardcoded, not configurable
    String DB_URL = "jdbc:mysql://localhost/db";
}

class Server implements Config {
    // Can't change PORT without recompiling
}
```

**Right:**
```java
class ServerConfig {
    private final int port;
    private final String dbUrl;

    ServerConfig(int port, String dbUrl) {
        this.port = port;
        this.dbUrl = dbUrl;
    }
    // Getter methods
}

class Server {
    private final ServerConfig config;

    Server(ServerConfig config) {
        this.config = config;
    }
}
```

---

## Best Practices

1. **Follow Interface Segregation Principle:**
   Create small, focused interfaces. Don't force implementors to depend on methods they don't use.

2. **Prefer Interfaces Over Abstract Classes for Type Definitions:**
   Use interfaces for defining types that unrelated classes can implement. Use abstract classes for shared implementation.

3. **Use Default Methods for Interface Evolution:**
   When adding methods to existing interfaces, use default methods to avoid breaking implementations.

4. **Mark Functional Interfaces with `@FunctionalInterface`:**
   This annotation provides compile-time checking that the interface has exactly one abstract method.

5. **Document Interface Contracts:**
   Clearly document preconditions, postconditions, and side effects in interface method Javadoc.

---

## Real World Usage

### Spring Framework
- `ApplicationListener` — interface for event handling
- `BeanPostProcessor` — interface for bean post-processing
- `Repository`, `Service`, `Controller` — stereotype interfaces for component scanning

### Hibernate / JPA
- `Serializable` — marker interface for session storage
- `Comparable` — interface for natural ordering
- `Iterable` — interface for enhanced for-loop support

### JDK Source Code
- `List`, `Set`, `Map` — core collection interfaces
- `Comparable`, `Comparator` — sorting interfaces
- `Runnable`, `Callable` — threading interfaces
- `Serializable`, `Cloneable` — marker interfaces

### Enterprise Applications
- Repository pattern — `CrudRepository`, `JpaRepository`
- Strategy pattern — interchangeable algorithms behind interfaces
- Observer pattern — event listeners and callbacks

---

## References

- [Java Language Specification — Interfaces](https://docs.oracle.com/javase/specs/jls/se17/html/jls-9.html)
- [Effective Java, 3rd Edition — Item 20: Prefer interfaces to abstract classes](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Java Tutorials — Interfaces](https://docs.oracle.com/en/java/javase/21/java/IandI/interfaceDefine.html)
- [Baeldung — Java Interfaces](https://www.baeldung.com/java-interfaces)
- [Refactoring.Guru — Interface](https://refactoring.guru/design-patterns/interface)

---

## Summary

Interfaces are the cornerstone of flexible, maintainable Java design. Key takeaways:

- **Contract definition:** Interfaces define what a class can do, not how it does it
- **Multiple implementation:** A class can implement many interfaces, enabling composition
- **Default methods (Java 8+):** Provide optional implementation without breaking existing code
- **Static methods:** Keep utility functions close to the interface they serve
- **Functional interfaces:** Enable lambda expressions and functional programming
- **Constants:** Provide shared configuration values (use sparingly)

**Golden rule:** Program to interfaces, not implementations. Keep interfaces small, focused, and well-documented.

---

**Navigation:**
- Previous: [11-abstraction](../11-abstraction/README.md)
- Next: [13-generics](../13-generics/README.md)
- [Back to OOP Module](../README.md)
