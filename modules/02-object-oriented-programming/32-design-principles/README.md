# Design Principles

## Introduction

Design principles are foundational guidelines that help developers create software that is maintainable, readable, and adaptable. While SOLID principles focus on object-oriented class design, broader design principles like DRY (Don't Repeat Yourself), KISS (Keep It Simple, Stupid), YAGNI (You Aren't Gonna Need It), and the Law of Demeter address different aspects of software quality. These principles are not rigid rules but pragmatic guidelines that, when applied thoughtfully, lead to codebases that are easier to understand, modify, and extend. They are universally applicable across programming languages, frameworks, and project sizes. Understanding and applying these principles is what separates junior developers from senior engineers — they represent the collective wisdom of decades of software engineering practice.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Apply DRY, KISS, YAGNI, and the Law of Demeter in Java applications
- [ ] Recognize code smells that indicate principle violations
- [ ] Balance these principles with practical project constraints
- [ ] Integrate these principles with SOLID for comprehensive design quality

## Prerequisites

- [31-solid-principles](../31-solid-principles/) — SOLID principles complement these design principles
- [08-encapsulation](../08-encapsulation/) — Encapsulation supports DRY and Law of Demeter
- [12-interfaces](../12-interfaces/) — Interfaces support DRY and Law of Demeter
- [05-methods](../05-methods/) — Method design is guided by KISS

## Why This Concept Exists

### The Problem

Without guiding principles, developers create code that is:
- **Duplicated**: The same logic appears in multiple places, making changes error-prone
- **Over-engineered**: Unnecessary complexity makes code hard to understand
- **Prematurely optimized**: Building features that may never be needed
- **Tightly coupled**: Components depend on internal details of other components

### The Solution

Design principles provide mental models for making better decisions. DRY reduces maintenance burden. KISS reduces cognitive load. YAGNI prevents waste. The Law of Demeter reduces coupling.

### Real-World Analogy

Think of design principles like a carpenter's rules of thumb: "Measure twice, cut once," "Keep it simple," "Don't build what the customer didn't ask for." These aren't laws of physics — they're practical wisdom that helps you build better furniture with less effort.

## Internal Working

### How Principles Interact

```
┌─────────────────────────────────────────────┐
│              Design Principles              │
├──────────┬──────────┬──────────┬────────────┤
│   DRY    │   KISS   │  YAGNI   │    LoD     │
├──────────┼──────────┼──────────┼────────────┤
│ Reduce   │ Minimize │ Build    │ Reduce     │
│ duplica- │ complex- │ only     │ coupling   │
│ tion     │ ity      │ what's   │ between    │
│          │          │ needed   │ objects    │
└──────────┴──────────┴──────────┴────────────┘
         │           │           │
         ▼           ▼           ▼
    ┌──────────────────────────────────┐
    │          SOLID Principles        │
    │  SRP │ OCP │ LSP │ ISP │ DIP    │
    └──────────────────────────────────┘
```

### Priority and Application

In practice, DRY and KISS are universally applicable. YAGNI is most relevant during requirements gathering and feature design. The Law of Demeter is most relevant in object-oriented design with deep collaboration chains. SOLID principles are most relevant when designing class hierarchies and module boundaries.

## Syntax

Design principles are not syntactic constructs but design guidelines that manifest in code patterns:

```java
// DRY: Extract common logic
// Instead of duplicating:
// if (type.equals("A")) { processA(); }
// if (type.equals("B")) { processB(); }

// Use a single method:
void process(String type) {
    processors.get(type).process();
}

// KISS: Simple solution over clever solution
// Instead of:
// return (a > b) ? (a > c ? a : (b > c ? b : c)) : (b > c ? b : (c > a ? c : a));

// Use:
return Math.max(a, Math.max(b, c));

// YAGNI: Don't add features until needed
// Don't build a plugin system if you only have one processor

// Law of Demeter: Only talk to immediate friends
// Instead of:
// order.getCustomer().getAddress().getCity().toUpperCase();

// Use:
// order.getShippingCity().toUpperCase();
```

## Easy Examples

### Example 1: DRY — Eliminate Duplicate Code

**Problem Statement**: Multiple methods contain the same validation and logging logic. Extract it to follow DRY.

**Implementation**:

```java
package academy.javaengineering.oop.designprinciples.dry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// VIOLATION: Duplicated code
class BadUserService {
    void createUser(String name, String email) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name required");
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("Email required");
        System.out.println("[" + LocalDateTime.now() + "] Creating user: " + name);
        // ... create user
        System.out.println("[" + LocalDateTime.now() + "] User created: " + name);
    }

    void deleteUser(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name required");
        System.out.println("[" + LocalDateTime.now() + "] Deleting user: " + name);
        // ... delete user
        System.out.println("[" + LocalDateTime.now() + "] User deleted: " + name);
    }
}

// COMPLIANT: DRY principle applied
class UserService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    void createUser(String name, String email) {
        validateRequired(name, "Name");
        validateRequired(email, "Email");
        log("Creating user: " + name);
        // ... create user
        log("User created: " + name);
    }

    void deleteUser(String name) {
        validateRequired(name, "Name");
        log("Deleting user: " + name);
        // ... delete user
        log("User deleted: " + name);
    }

    private void validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    private void log(String message) {
        System.out.println("[" + LocalDateTime.now().format(FORMATTER) + "] " + message);
    }
}

public class DRYDemo {
    public static void main(String[] args) {
        UserService service = new UserService();
        service.createUser("Alice", "alice@example.com");
        service.deleteUser("Alice");
    }
}
```

**Expected Output**:
```
[2024-01-15 10:30:00] Creating user: Alice
[2024-01-15 10:30:00] User created: Alice
[2024-01-15 10:30:00] Deleting user: Alice
[2024-01-15 10:30:00] User deleted: Alice
```

**Code Walkthrough**: The `BadUserService` duplicates validation and logging in every method. The `UserService` extracts these into `validateRequired()` and `log()` methods. When validation rules change, only one place needs updating.

### Example 2: KISS — Choose the Simplest Solution

**Problem Statement**: A method to find the maximum value in a list uses an unnecessarily complex implementation. Simplify it following KISS.

**Implementation**:

```java
package academy.javaengineering.oop.designprinciples.kiss;

import java.util.Arrays;
import java.util.List;

public class KISSDemo {
    // VIOLATION: Overly complex
    static int findMaxComplex(List<Integer> numbers) {
        int max = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            int current = numbers.get(i);
            if (((current > max) ? 1 : 0) == 1) {
                max = current;
            }
        }
        return max;
    }

    // COMPLIANT: Simple and clear
    static int findMaxSimple(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("Empty list"));
    }

    // Alternative: Even simpler with Collections
    static int findMaxSimplest(List<Integer> numbers) {
        return java.util.Collections.max(numbers);
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(3, 7, 2, 9, 4, 6, 1);

        System.out.println("Numbers: " + numbers);
        System.out.println("Max (complex): " + findMaxComplex(numbers));
        System.out.println("Max (simple): " + findMaxSimple(numbers));
        System.out.println("Max (simplest): " + findMaxSimplest(numbers));
    }
}
```

**Expected Output**:
```
Numbers: [3, 7, 2, 9, 4, 6, 1]
Max (complex): 9
Max (simple): 9
Max (simplest): 9
```

**Code Walkthrough**: The complex version uses ternary operators and manual iteration. The simple version uses the Stream API. The simplest version uses `Collections.max()`. All produce the same result, but the simplest is most readable and maintainable.

### Example 3: YAGNI — Don't Build What You Don't Need

**Problem Statement**: A developer builds a generic caching framework for a simple blog application. Identify what's YAGNI.

**Implementation**:

```java
package academy.javaengineering.oop.designprinciples.yagni;

import java.util.HashMap;
import java.util.Map;

// YAGNI VIOLATION: Over-engineered generic cache
class GenericCache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new HashMap<>();
    private final long defaultTtlMs;
    private final int maxSize;
    private final EvictionPolicy evictionPolicy;
    private final CacheMetrics metrics;

    enum EvictionPolicy { LRU, LFU, FIFO }
    static class CacheMetrics {
        long hits, misses, evictions;
    }
    static class CacheEntry<V> {
        V value;
        long createdAt;
        long lastAccessed;
        int accessCount;
        CacheEntry(V value, long ttl) {
            this.value = value;
            this.createdAt = System.currentTimeMillis();
            this.lastAccessed = createdAt;
        }
    }

    GenericCache(int maxSize, long defaultTtlMs, EvictionPolicy policy) {
        this.maxSize = maxSize;
        this.defaultTtlMs = defaultTtlMs;
        this.evictionPolicy = policy;
        this.metrics = new CacheMetrics();
    }

    void put(K key, V value) { /* Complex eviction logic */ }
    V get(K key) { return null; }
    void evict(K key) { /* ... */ }
    CacheMetrics getMetrics() { return metrics; }
}

// YAGNI COMPLIANT: Simple cache for a blog
class SimpleBlogCache {
    private final Map<String, String> cache = new HashMap<>();

    void put(String key, String value) {
        cache.put(key, value);
    }

    String get(String key) {
        return cache.get(key);
    }

    void invalidate(String key) {
        cache.remove(key);
    }
}

public class YAGNIDemo {
    public static void main(String[] args) {
        SimpleBlogCache cache = new SimpleBlogCache();
        cache.put("post:1", "First blog post");
        cache.put("post:2", "Second blog post");

        System.out.println("Post 1: " + cache.get("post:1"));
        System.out.println("Post 2: " + cache.get("post:2"));

        cache.invalidate("post:1");
        System.out.println("After invalidation: " + cache.get("post:1"));
    }
}
```

**Expected Output**:
```
Post 1: First blog post
Post 2: Second blog post
After invalidation: null
```

**Code Walkthrough**: The `GenericCache` has eviction policies, metrics, TTL, and max size — none of which a simple blog needs. The `SimpleBlogCache` provides just `put`, `get`, and `invalidate`. When the blog grows and needs TTL or eviction, the cache can be enhanced — but don't build it until you need it.

## Medium Examples

### Example 1: Law of Demeter — Avoid Train Wrecks

**Problem Statement**: Code has deep method chaining that violates the Law of Demeter. Refactor to reduce coupling.

**Implementation**:

```java
package academy.javaengineering.oop.designprinciples.lod;

// VIOLATION: Train wreck — knowing too many objects
class BadOrder {
    Customer getCustomer() { return new Customer(); }
}

class Customer {
    Address getAddress() { return new Address(); }
}

class Address {
    City getCity() { return new City(); }
}

class City {
    String getName() { return "New York"; }
}

// This violates Law of Demeter:
// String city = order.getCustomer().getAddress().getCity().getName();

// COMPLIANT: Each object provides the service directly
class Order {
    private Customer customer;

    Order(Customer customer) {
        this.customer = customer;
    }

    String getShippingCity() {
        return customer.getShippingCity();
    }

    String getShippingAddress() {
        return customer.getFullAddress();
    }
}

class Customer {
    private Address address;

    Customer(Address address) {
        this.address = address;
    }

    String getShippingCity() {
        return address.getCityName();
    }

    String getFullAddress() {
        return address.getFormattedAddress();
    }
}

class Address {
    private String street, city, zip;

    Address(String street, String city, String zip) {
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    String getCityName() { return city; }

    String getFormattedAddress() {
        return street + ", " + city + " " + zip;
    }
}

public class LoDDemo {
    public static void main(String[] args) {
        Address addr = new Address("123 Main St", "New York", "10001");
        Customer customer = new Customer(addr);
        Order order = new Order(customer);

        System.out.println("City: " + order.getShippingCity());
        System.out.println("Address: " + order.getShippingAddress());
    }
}
```

**Expected Output**:
```
City: New York
Address: 123 Main St, New York 10001
```

**Code Walkthrough**: The `BadOrder` chain `order.getCustomer().getAddress().getCity().getName()` violates LoD — `Order` knows about `Customer`, `Address`, and `City`. The refactored version has `Order.getShippingCity()` which delegates to `Customer.getShippingCity()`, which delegates to `Address.getCityName()`. Each object only talks to its immediate collaborators.

### Example 2: DRY with Template Method Pattern

**Problem Statement**: Multiple report generators duplicate the same report structure. Extract the common pattern using Template Method.

**Implementation**:

```java
package academy.javaengineering.oop.designprinciples.dry;

import java.util.Arrays;
import java.util.List;

abstract class ReportGenerator {
    // Template method — defines the algorithm skeleton
    final String generate(String title, List<String> data) {
        StringBuilder report = new StringBuilder();
        report.append(header(title));
        report.append("\n");
        report.append(separator());
        report.append("\n");
        for (String item : data) {
            report.append(body(item));
            report.append("\n");
        }
        report.append(separator());
        report.append("\n");
        report.append(footer());
        return report.toString();
    }

    protected abstract String header(String title);
    protected abstract String separator();
    protected abstract String body(String item);
    protected abstract String footer();
}

class TextReportGenerator extends ReportGenerator {
    @Override protected String header(String title) { return "=== " + title + " ==="; }
    @Override protected String separator() { return "-------------------"; }
    @Override protected String body(String item) { return "  " + item; }
    @Override protected String footer() { return "--- End of Report ---"; }
}

class HtmlReportGenerator extends ReportGenerator {
    @Override protected String header(String title) { return "<h1>" + title + "</h1>"; }
    @Override protected String separator() { return "<hr/>"; }
    @Override protected String body(String item) { return "<p>" + item + "</p>"; }
    @Override protected String footer() { return "<footer>End of Report</footer>"; }
}

public class DRYTemplateDemo {
    public static void main(String[] args) {
        List<String> data = Arrays.asList("Sales: $10,000", "Expenses: $7,500", "Profit: $2,500");

        ReportGenerator textReport = new TextReportGenerator();
        System.out.println(textReport.generate("Q4 Financial Report", data));

        System.out.println();

        ReportGenerator htmlReport = new HtmlReportGenerator();
        System.out.println(htmlReport.generate("Q4 Financial Report", data));
    }
}
```

**Expected Output**:
```
=== Q4 Financial Report ===
-------------------
  Sales: $10,000
  Expenses: $7,500
  Profit: $2,500
-------------------
--- End of Report ---

<h1>Q4 Financial Report</h1>
<hr/>
<p>Sales: $10,000</p>
<p>Expenses: $7,500</p>
<p>Profit: $2,500</p>
<hr/>
<footer>End of Report</footer>
```

**Code Walkthrough**: The Template Method pattern defines the algorithm skeleton in the base class (`generate()`) and lets subclasses implement specific steps (`header`, `body`, `footer`). This eliminates duplication — the report structure is defined once, and new formats only need to implement the abstract methods.

### Example 3: KISS + YAGNI Combined

**Problem Statement**: A developer builds an overly complex notification system. Simplify it using KISS and YAGNI.

**Implementation**:

```java
package academy.javaengineering.oop.designprinciples;

import java.util.ArrayList;
import java.util.List;

// VIOLATION: KISS and YAGNI
interface NotificationStrategy<T> {
    void execute(T context);
}

class NotificationPipeline<T> {
    private final List<NotificationStrategy<T>> preProcessors = new ArrayList<>();
    private final NotificationStrategy<T> mainStrategy;
    private final List<NotificationStrategy<T>> postProcessors = new ArrayList<>();

    NotificationPipeline(NotificationStrategy<T> main) { this.mainStrategy = main; }
    void addPreProcessor(NotificationStrategy<T> p) { preProcessors.add(p); }
    void addPostProcessor(NotificationStrategy<T> p) { postProcessors.add(p); }
    void execute(T context) {
        preProcessors.forEach(p -> p.execute(context));
        mainStrategy.execute(context);
        postProcessors.forEach(p -> p.execute(context));
    }
}

// KISS + YAGNI COMPLIANT: Simple, focused classes
class Notification {
    private final String recipient;
    private final String message;

    Notification(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    String getRecipient() { return recipient; }
    String getMessage() { return message; }
}

interface NotificationSender {
    void send(Notification notification);
}

class EmailSender implements NotificationSender {
    @Override
    public void send(Notification notification) {
        System.out.printf("EMAIL to %s: %s%n", notification.getRecipient(), notification.getMessage());
    }
}

class SmsSender implements NotificationSender {
    @Override
    public void send(Notification notification) {
        System.out.printf("SMS to %s: %s%n", notification.getRecipient(), notification.getMessage());
    }
}

class NotificationService {
    private final NotificationSender sender;

    NotificationService(NotificationSender sender) {
        this.sender = sender;
    }

    void notify(String recipient, String message) {
        sender.send(new Notification(recipient, message));
    }
}

public class KISSYAGNIDemo {
    public static void main(String[] args) {
        NotificationService email = new NotificationService(new EmailSender());
        NotificationService sms = new NotificationService(new SmsSender());

        email.notify("alice@example.com", "Welcome!");
        sms.notify("+1234567890", "Your code is 1234");
    }
}
```

**Expected Output**:
```
EMAIL to alice@example.com: Welcome!
SMS to +1234567890: Your code is 1234
```

**Code Walkthrough**: The complex pipeline with pre/post processors is over-engineered for a notification system. The simple version has `Notification`, `NotificationSender`, and `NotificationService` — three classes with clear responsibilities. When you need pipelines, add them later.

## Hard Examples

### Example 1: Applying All Principles to a File Processing System

**Problem Statement**: Design a file processing system that applies DRY, KISS, YAGNI, and Law of Demeter.

**Implementation**:

```java
package academy.javaengineering.oop.designprinciples;

import java.util.ArrayList;
import java.util.List;

// DRY: Common interface for all processors
interface FileProcessor {
    boolean canProcess(String filename);
    String process(String content);
}

// KISS: Simple implementations
class UpperCaseProcessor implements FileProcessor {
    @Override
    public boolean canProcess(String filename) {
        return filename.endsWith(".upper");
    }

    @Override
    public String process(String content) {
        return content.toUpperCase();
    }
}

class WordCountProcessor implements FileProcessor {
    @Override
    public boolean canProcess(String filename) {
        return filename.endsWith(".count");
    }

    @Override
    public String process(String content) {
        long words = content.split("\\s+").length;
        return "Word count: " + words;
    }
}

// YAGNI: Simple result class — no metrics, no logging framework
class ProcessResult {
    private final String filename;
    private final String output;
    private final boolean success;

    ProcessResult(String filename, String output, boolean success) {
        this.filename = filename;
        this.output = output;
        this.success = success;
    }

    String getFilename() { return filename; }
    String getOutput() { return output; }
    boolean isSuccess() { return success; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", success ? "OK" : "FAIL", filename, output);
    }
}

// LoD: Each class only talks to its immediate friends
class FileProcessingService {
    private final List<FileProcessor> processors;

    FileProcessingService(List<FileProcessor> processors) {
        this.processors = processors;
    }

    ProcessResult process(String filename, String content) {
        FileProcessor processor = findProcessor(filename);
        if (processor == null) {
            return new ProcessResult(filename, "No processor found", false);
        }
        return new ProcessResult(filename, processor.process(content), true);
    }

    private FileProcessor findProcessor(String filename) {
        for (FileProcessor p : processors) {
            if (p.canProcess(filename)) return p;
        }
        return null;
    }
}

public class PrinciplesCombinedDemo {
    public static void main(String[] args) {
        List<FileProcessor> processors = List.of(
                new UpperCaseProcessor(),
                new WordCountProcessor()
        );

        FileProcessingService service = new FileProcessingService(processors);

        System.out.println(service.process("data.upper", "hello world"));
        System.out.println(service.process("data.count", "hello world this is a test"));
        System.out.println(service.process("data.unknown", "content"));
    }
}
```

**Unit Tests**:

```java
package academy.javaengineering.oop.designprinciples;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

class FileProcessingServiceTest {
    @Test
    void testUpperCase() {
        FileProcessingService service = new FileProcessingService(List.of(new UpperCaseProcessor()));
        ProcessResult result = service.process("test.upper", "hello");
        assertTrue(result.isSuccess());
        assertEquals("HELLO", result.getOutput());
    }

    @Test
    void testWordCount() {
        FileProcessingService service = new FileProcessingService(List.of(new WordCountProcessor()));
        ProcessResult result = service.process("test.count", "one two three");
        assertTrue(result.isSuccess());
        assertEquals("Word count: 3", result.getOutput());
    }

    @Test
    void testNoProcessor() {
        FileProcessingService service = new FileProcessingService(List.of());
        ProcessResult result = service.process("test.xyz", "content");
        assertFalse(result.isSuccess());
    }
}
```

**Execution Flow**: The service receives a filename and content. It finds the matching processor (DRY — common interface). The processor implements simple logic (KISS). The service doesn't know about processor internals (LoD). No caching or metrics are built (YAGNI).

**Complexity**: O(n) for finding the processor where n is the number of processors. O(m) for processing where m is the content size.

**Best Practices**:
- Apply DRY by extracting common patterns into shared abstractions
- Keep implementations KISS — single responsibility, minimal complexity
- Build YAGNI — only add features when requirements demand it
- Follow LoD — objects should only collaborate with immediate neighbors

### Example 2: Design Principles in a Microservice Context

**Problem Statement**: Design a simple order service that applies all design principles in a microservice architecture.

**Implementation**:

```java
package academy.javaengineering.oop.designprinciples;

import java.util.UUID;

// DRY: Shared value objects
class Money {
    private final double amount;
    private final String currency;

    Money(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    double getAmount() { return amount; }
    String getCurrency() { return currency; }

    Money add(Money other) {
        if (!currency.equals(other.currency)) throw new IllegalArgumentException("Currency mismatch");
        return new Money(amount + other.amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }
}

class OrderId {
    private final String value;

    OrderId() { this.value = "ORD-" + UUID.randomUUID().toString().substring(0, 8); }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object o) {
        return o instanceof OrderId && value.equals(((OrderId) o).value);
    }

    @Override
    public int hashCode() { return value.hashCode(); }
}

// KISS: Simple domain events
interface DomainEvent {
    String describe();
}

record OrderCreatedEvent(OrderId orderId, Money total) implements DomainEvent {
    @Override
    public String describe() { return "Order " + orderId + " created: " + total; }
}

record OrderCancelledEvent(OrderId orderId, String reason) implements DomainEvent {
    @Override
    public String describe() { return "Order " + orderId + " cancelled: " + reason; }
}

// YAGNI: No event bus, no CQRS, no complex event sourcing
interface EventPublisher {
    void publish(DomainEvent event);
}

class ConsoleEventPublisher implements EventPublisher {
    @Override
    public void publish(DomainEvent event) {
        System.out.println("EVENT: " + event.describe());
    }
}

// LoD: Order doesn't know about EventPublisher internals
class Order {
    private final OrderId id;
    private Money total;
    private boolean active;

    Order(Money total) {
        this.id = new OrderId();
        this.total = total;
        this.active = true;
    }

    OrderId getId() { return id; }
    Money getTotal() { return total; }
    boolean isActive() { return active; }

    void cancel(String reason) {
        if (!active) throw new IllegalStateException("Order already cancelled");
        active = false;
    }
}

class OrderService {
    private final EventPublisher eventPublisher;

    OrderService(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    Order createOrder(double amount, String currency) {
        Order order = new Order(new Money(amount, currency));
        eventPublisher.publish(new OrderCreatedEvent(order.getId(), order.getTotal()));
        return order;
    }

    void cancelOrder(Order order, String reason) {
        order.cancel(reason);
        eventPublisher.publish(new OrderCancelledEvent(order.getId(), reason));
    }
}

public class MicroserviceDesignDemo {
    public static void main(String[] args) {
        OrderService service = new OrderService(new ConsoleEventPublisher());

        Order order = service.createOrder(99.99, "USD");
        System.out.println("Created: " + order.getId());

        service.cancelOrder(order, "Customer changed mind");
        System.out.println("Active: " + order.isActive());
    }
}
```

**Execution Flow**: `OrderService` depends on `EventPublisher` (DIP). It only calls `publish()` on the publisher (LoD). Value objects are simple (KISS). No complex event infrastructure is built (YAGNI). Shared value objects eliminate duplication (DRY).

**Complexity**: O(1) for all operations.

**Best Practices**:
- Use value objects (Money, OrderId) to encapsulate domain concepts
- Keep domain services focused and simple
- Use domain events for loose coupling between services
- Don't over-engineer — build what you need now

## Exercises

### Easy

1. Identify DRY violations in a class that calculates tax for different product types. Refactor to eliminate duplication.

2. Simplify a complex boolean expression following KISS. The expression checks if a user has access based on multiple conditions.

3. Identify YAGNI violations in a utility class that has methods for operations that no current code uses.

### Medium

4. Refactor a chain of method calls `order.getCustomer().getAddress().getCity()` to follow the Law of Demeter.

5. Apply DRY to create a reusable validation framework that can validate different entity types with common rules.

6. Redesign a notification system that follows KISS — remove unnecessary abstraction layers.

### Hard

7. Design a complete order processing system applying all four principles. Include domain events, value objects, and simple services.

8. Refactor a legacy codebase with duplicated business logic, complex abstractions, and unnecessary features to follow DRY, KISS, YAGNI, and LoD.

9. Create a plugin architecture that follows all principles — plugins should be simple, not duplicated, and loosely coupled.

## Interview Questions

### Easy

1. **What does DRY stand for and why is it important?**
   DRY = Don't Repeat Yourself. When the same logic appears in multiple places, changing it requires updating all instances — error-prone and time-consuming. DRY extracts common logic into shared abstractions, ensuring changes happen in one place.

2. **Explain KISS with a simple example.**
   KISS = Keep It Simple, Stupid. Choose the simplest solution that works. For finding the maximum of three numbers, use `Math.max(a, Math.max(b, c))` instead of nested ternary operators. Simple code is easier to read, debug, and maintain.

3. **What is the Law of Demeter?**
   The Law of Demeter states that an object should only talk to its immediate friends — not to friends of friends. Avoid method chains like `a.getB().getC().doSomething()`. Instead, have each object provide the service directly.

### Intermediate

4. **How do DRY and YAGNI sometimes conflict?**
   DRY says "extract common patterns." YAGNI says "don't build what you don't need." If two methods have similar code but might diverge in the future, premature extraction (DRY over-application) creates unnecessary abstractions. Apply DRY when duplication is certain; defer when it's speculative.

5. **How does the Law of Demeter improve testability?**
   LoD reduces the number of objects you need to mock in tests. If `Order` only talks to `Customer` (not `Customer.getAddress().getCity()`), you only need to mock `Customer`, not the entire chain. This makes tests simpler and more focused.

6. **When is it acceptable to violate KISS?**
   KISS should not lead to oversimplification. If the domain is inherently complex (e.g., financial calculations), the code should reflect that complexity rather than hide it. KISS means "don't add unnecessary complexity," not "make everything trivial."

### Hard

7. **How would you apply all four principles to a microservices architecture?**
   DRY: Share value objects and DTOs via a common library. KISS: Each service has minimal responsibilities. YAGNI: Don't build service mesh, CQRS, or event sourcing until needed. LoD: Services communicate through well-defined APIs, not internal databases. Apply each principle at the appropriate granularity.

8. **Explain the tension between DRY and the Single Responsibility Principle.**
   DRY extracts common code. SRP ensures classes have one responsibility. If you extract too aggressively, you might create classes that serve multiple purposes (violating SRP). Balance: extract behavior that genuinely belongs together, keep extractions cohesive.

## Common Pitfalls

### 1. DRY Over-Application (Premature Abstraction)

**Wrong**:
```java
class UserService {
    void processUser(User user) {
        validate(user);
        save(user);
        notify(user);
    }
}

class ProductService {
    void processProduct(Product product) {
        validate(product);
        save(product);
        notify(product);
    }
}

// Prematurely extracting a generic "process" method
class ProcessingService<T> {
    void process(T entity, Validator<T> v, Repository<T> r, Notifier<T> n) { /* ... */ }
}
```

**Right**:
```java
class UserService {
    void processUser(User user) {
        validate(user);
        save(user);
        notify(user);
    }
}

class ProductService {
    void processProduct(Product product) {
        validate(product);
        save(product);
        notify(product);
    }
}
```

If the validation, save, and notify logic is genuinely different for users and products, premature extraction creates unnecessary abstraction. Only extract when the logic is truly identical.

### 2. YAGNI Violation — Building for Hypothetical Future

**Wrong**:
```java
// Building a complete logging framework for a prototype
class LoggingFramework {
    void addAppender(Appender a) { /* ... */ }
    void setLevel(Level l) { /* ... */ }
    void setFormatter(Formatter f) { /* ... */ }
    void log(Level l, String msg) { /* ... */ }
    // ... 200 more lines
}

// Usage: System.out.println("debug: " + message); // All that code for this?
```

**Right**:
```java
class SimpleLogger {
    void log(String message) {
        System.out.println("[LOG] " + message);
    }
}
```

### 3. LoD Violation — Train Wreck Method Chains

**Wrong**:
```java
String city = order.getCustomer().getAddress().getCity().getName().toUpperCase();
```

**Right**:
```java
String city = order.getShippingCity().toUpperCase();
```

## Best Practices

1. **Apply DRY to behavior, not just code** — Duplicate behavior (same validation rules, same business logic) is worse than duplicate syntax.
2. **Choose KISS based on context** — A sorting algorithm's implementation should be correct and efficient, not necessarily the simplest possible code.
3. **Defer YAGNI decisions** — When tempted to add a feature "just in case," wait until there's a concrete use case.
4. **Follow LoD in collaboration chains** — If you find yourself writing `a.b().c().d()`, refactor so each object provides the service directly.
5. **Balance principles with pragmatism** — No principle is absolute. Apply them where they provide value and don't over-engineer.

## Real World Usage

### How Spring Uses This

Spring follows DRY through shared annotations and configuration. KISS is reflected in Spring Boot's convention-over-configuration approach. YAGNI is evident in Spring's modular design — you only include what you need. LoD is maintained through dependency injection.

### How JDK Uses This

The JDK follows DRY through shared utility methods. KISS is evident in `java.lang.Math` and `java.util.Collections`. YAGNI is reflected in the JDK's modular design (Java 9+). LoD is maintained through encapsulation — you interact with `Map` interface methods, not internal buckets.

### Enterprise Usage

Enterprise applications apply these principles through shared libraries (DRY), microservice boundaries (KISS), iterative development (YAGNI), and API design (LoD). DRY is enforced through code reviews and linting. KISS is supported by architectural reviews. YAGNI is managed through backlog prioritization.

## References

- [The Pragmatic Programmer by Hunt and Thomas](https://pragprog.com/titles/tpp20/)
- [Clean Code by Robert C. Martin](https://www.oreilly.com/library/view/clean-code/9780136083238/)
- [Baeldung — DRY Principle](https://www.baeldung.com/java-dry-principle)
- [Law of Demeter](https://en.wikipedia.org/wiki/Law_of_Demeter)

## Summary

- **DRY**: Eliminate duplication — every piece of knowledge should have a single representation
- **KISS**: Choose the simplest solution that works — avoid unnecessary complexity
- **YAGNI**: Don't build features until you need them — defer speculative development
- **Law of Demeter**: Only talk to immediate friends — avoid deep method chains
- These principles complement SOLID — DRY and LoD support SRP, KISS supports OCP, YAGNI prevents premature abstraction
- Apply pragmatically — no principle is absolute; balance with project context

**Next Step**: [33-mini-projects](../33-mini-projects/)
