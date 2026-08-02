# Design Patterns - Introduction

## 1. Introduction

Design patterns are reusable solutions to commonly occurring problems in software design. They are not finished designs that can be transformed directly into code but are templates for how to solve a problem in many different situations. Design patterns were formalized by the "Gang of Four" (GoF) — Erich Gamma, Richard Helm, Ralph Johnson, and John Vlissides — in their seminal book *Design Patterns: Elements of Reusable Object-Oriented Software* (1994).

In the context of Java engineering, design patterns provide a shared vocabulary for developers, improve code maintainability, and promote best practices in object-oriented design. They are not language-specific, but Java's strong OOP features make it an excellent language for implementing these patterns.

---

## 2. Learning Objectives

By the end of this module, you will be able to:

- Understand the three categories of design patterns: Creational, Structural, and Behavioral
- Identify when to apply specific design patterns in real-world scenarios
- Implement common design patterns in Java 21
- Recognize anti-patterns and common mistakes when applying patterns
- Make informed decisions about pattern selection using decision trees
- Understand the performance implications of each pattern
- Answer design pattern interview questions with confidence
- Apply patterns in enterprise-grade applications

---

## 3. Prerequisites

Before diving into design patterns, you should be comfortable with:

- **Java fundamentals**: Classes, interfaces, inheritance, polymorphism
- **Object-Oriented Programming**: Encapsulation, abstraction, SOLID principles
- **Java 17+ features**: Records, sealed classes, text blocks, pattern matching
- **UML basics**: Class diagrams, sequence diagrams (helpful but not required)
- **Exception handling**: Try-catch, custom exceptions
- **Collections framework**: Lists, maps, sets

---

## 4. Why This Concept Exists

Design patterns exist because software development faces recurring problems. Without patterns:

- **No shared vocabulary**: Developers struggle to communicate solutions
- **Reinventing the wheels**: Common problems get solved poorly repeatedly
- **Inconsistent code**: Different developers solve the same problem differently
- **Maintenance nightmare**: Code without proven structures becomes hard to maintain

Design patterns provide:
- **Proven solutions**: Tested by thousands of developers over decades
- **Common vocabulary**: "Use a Factory" communicates a complete concept
- **Flexibility**: Patterns can be adapted to specific needs
- **Best practices**: Encode years of experience into reusable templates

---

## 5. Problem Statement

Consider a typical enterprise application development scenario:

```
You're building an e-commerce platform. You need:
1. Only one instance of the payment processor (Singleton)
2. Different types of notifications without tight coupling (Factory)
3. Complex object construction with many optional parameters (Builder)
4. Clone existing product configurations (Prototype)
5. Integrate with third-party payment gateways (Adapter)
6. Add logging, caching, security layers (Decorator)
7. Control access to premium features (Proxy)
8. Notify users when orders ship (Observer)
9. Multiple sorting strategies for products (Strategy)
10. Undo/redo functionality (Command)
11. Standardized report generation (Template Method)

Without design patterns, you'd face:
- Tight coupling between classes
- Difficult to modify or extend
- Code duplication
- Testing challenges
- Poor maintainability
```

---

## 6. Theory

### 6.1 The Three Categories

| Category | Purpose | Examples |
|----------|---------|----------|
| **Creational** | Object creation mechanisms | Singleton, Factory, Builder, Prototype |
| **Structural** | Object composition and relationships | Adapter, Decorator, Proxy |
| **Behavioral** | Communication between objects | Observer, Strategy, Command, Template Method |

### 6.2 SOLID Principles and Patterns

Design patterns reinforce SOLID principles:

- **S**ingle Responsibility: Each pattern has a focused purpose
- **O**pen/Closed: Patterns enable extension without modification
- **L**iskov Substitution: Patterns use proper inheritance hierarchies
- **I**nterface Segregation: Patterns favor composition over inheritance
- **D**ependency Inversion: Patterns depend on abstractions

### 6.3 Pattern Relationships

Patterns often work together:
- **Abstract Factory + Singleton**: Factory is often a singleton
- **Decorator + Factory**: Creating decorated objects
- **Observer + Mediator**: Complex event systems
- **Strategy + Factory**: Selecting strategies dynamically

---

## 7. Internal Working

### 7.1 How Patterns Modify Code Structure

Design patterns change how classes and objects interact:

**Without Pattern:**
```java
// Tight coupling, hard to modify
public class NotificationService {
    public void sendEmail(String message) {
        // Email logic
    }
    public void sendSMS(String message) {
        // SMS logic
    }
    public void sendPush(String message) {
        // Push logic
    }
}
```

**With Factory Pattern:**
```java
// Loose coupling, easy to extend
public interface Notification {
    void send(String message);
}

public class EmailNotification implements Notification {
    public void send(String message) { /* Email logic */ }
}

public class NotificationFactory {
    public static Notification create(String type) {
        return switch (type) {
            case "email" -> new EmailNotification();
            case "sms" -> new SMSNotification();
            default -> throw new IllegalArgumentException();
        };
    }
}
```

### 7.2 Object Creation vs. Object Composition

| Approach | Pattern | When to Use |
|----------|---------|-------------|
| Creation | Singleton, Factory, Builder | Controlling how objects are created |
| Composition | Adapter, Decorator, Proxy | Building complex objects from simpler ones |
| Behavior | Observer, Strategy, Command | Defining how objects interact |

---

## 8. JVM Perspective

### 8.1 Pattern Impact on Class Loading

- **Singleton**: Only one instance loaded by classloader
- **Factory**: Dynamic class loading based on type
- **Prototype**: Object cloning avoids class loading overhead
- **Proxy**: Additional class generated at runtime (JDK/CGLIB)

### 8.2 Memory Implications

- **Singleton**: Single object in heap, shared across threads
- **Factory**: Multiple instances, garbage collected normally
- **Builder**: Temporary builder objects, final product retained
- **Prototype**: Deep copy may duplicate entire object graph

---

## 9. Memory Representation

### 9.1 Object Creation Patterns

```
Singleton:
┌─────────────┐
│ ClassLoader  │
│     ↓        │
│ Static Field │────→ Single Instance
└─────────────┘

Factory:
┌─────────────┐
│   Factory    │
│     ↓        │
│  Creates     │────→ Multiple Instances
│  Different   │
│  Types       │
└─────────────┘
```

### 9.2 Structural Patterns

```
Decorator:
┌─────────────┐     ┌─────────────┐
│   Component  │────→│  Decorator   │
│  (interface) │     │  (wraps)     │
└─────────────┘     └─────────────┘

Proxy:
┌─────────────┐     ┌─────────────┐
│    Client    │────→│    Proxy     │────→ Real Object
└─────────────┘     └─────────────┘
```

---

## 10. Syntax

### 10.1 Pattern Template Structure

Most patterns follow a common structure:

```java
// 1. Define the interface/abstract class
public interface PatternInterface {
    void operation();
}

// 2. Implement concrete classes
public class ConcreteImplementation implements PatternInterface {
    @Override
    public void operation() {
        // Implementation
    }
}

// 3. Create the pattern's coordinating class (if needed)
public class PatternCoordinator {
    // Pattern-specific logic
}
```

---

## 11. Easy Example

### Problem: Creating different types of objects

**Without Pattern (Bad):**
```java
public class BadNotificationService {
    public void notify(String type, String message) {
        if (type.equals("email")) {
            System.out.println("Email: " + message);
        } else if (type.equals("sms")) {
            System.out.println("SMS: " + message);
        }
        // Adding new types requires modifying this class
    }
}
```

**With Factory Pattern (Good):**
```java
public interface Notification {
    void send(String message);
}

public class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

public class SMSNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

public class NotificationFactory {
    public static Notification create(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms" -> new SMSNotification();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}

// Usage
Notification notification = NotificationFactory.create("email");
notification.send("Hello!");
```

---

## 12. Medium Example

### Problem: Building complex objects with optional parameters

```java
// Builder Pattern Example
public class HttpClient {
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final String body;
    private final int timeout;

    private HttpClient(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.body = builder.body;
        this.timeout = builder.timeout;
    }

    public static class Builder {
        private final String url;
        private String method = "GET";
        private Map<String, String> headers = new HashMap<>();
        private String body;
        private int timeout = 30000;

        public Builder(String url) {
            this.url = url;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpClient build() {
            return new HttpClient(this);
        }
    }
}

// Usage
HttpClient client = new HttpClient.Builder("https://api.example.com")
    .method("POST")
    .header("Content-Type", "application/json")
    .body("{\"key\": \"value\"}")
    .timeout(5000)
    .build();
```

---

## 13. Hard Example

### Problem: Implementing a thread-safe Singleton with lazy initialization

```java
public class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        // Private constructor prevents instantiation
        initializeConnection();
    }

    private void initializeConnection() {
        // Initialize database connection
        this.connection = createConnection();
    }

    private Connection createConnection() {
        // Actual connection creation logic
        return null; // Placeholder
    }

    // Double-checked locking for thread safety
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    // Prevent reflection-based instantiation
    private Object readResolve() {
        return getInstance();
    }

    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
```

---

## 14. Enterprise Example

### Real-world: E-commerce order processing system

```java
// Strategy Pattern for payment processing
public interface PaymentStrategy {
    PaymentResult pay(BigDecimal amount);
}

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public PaymentResult pay(BigDecimal amount) {
        // Credit card payment logic
        return new PaymentResult(true, "Credit card charged: " + amount);
    }
}

public class PayPalPayment implements PaymentStrategy {
    @Override
    public PaymentResult pay(BigDecimal amount) {
        // PayPal payment logic
        return new PaymentResult(true, "PayPal charged: " + amount);
    }
}

// Factory Pattern for creating payment strategies
public class PaymentFactory {
    public static PaymentStrategy create(String type) {
        return switch (type.toLowerCase()) {
            case "creditcard" -> new CreditCardPayment();
            case "paypal" -> new PayPalPayment();
            default -> throw new IllegalArgumentException("Unknown payment type");
        };
    }
}

// Observer Pattern for order notifications
public interface OrderObserver {
    void onOrderStatusChanged(Order order);
}

public class EmailNotificationObserver implements OrderObserver {
    @Override
    public void onOrderStatusChanged(Order order) {
        // Send email notification
    }
}

public class InventoryObserver implements OrderObserver {
    @Override
    public void onOrderStatusChanged(Order order) {
        // Update inventory
    }
}

// Builder Pattern for order construction
public class Order {
    private final String orderId;
    private final List<OrderItem> items;
    private final PaymentStrategy payment;
    private final List<OrderObserver> observers;

    private Order(Builder builder) {
        this.orderId = builder.orderId;
        this.items = builder.items;
        this.payment = builder.payment;
        this.observers = builder.observers;
    }

    public static class Builder {
        private String orderId;
        private List<OrderItem> items = new ArrayList<>();
        private PaymentStrategy payment;
        private List<OrderObserver> observers = new ArrayList<>();

        // Builder methods...

        public Order build() {
            return new Order(this);
        }
    }

    public void process() {
        PaymentResult result = payment.pay(calculateTotal());
        notifyObservers();
    }

    private void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.onOrderStatusChanged(this);
        }
    }
}
```

---

## 15. Performance

### 15.1 Performance Comparison

| Pattern | Time Complexity | Space Complexity | Thread Safety |
|---------|----------------|------------------|---------------|
| Singleton | O(1) access | O(1) | Requires sync |
| Factory | O(1) creation | O(n) types | Depends |
| Builder | O(n) fields | O(n) | Not thread-safe |
| Prototype | O(n) deep copy | O(n) | Not thread-safe |
| Adapter | O(1) delegation | O(1) | Depends |
| Decorator | O(1) delegation | O(d) depth | Depends |
| Proxy | O(1) delegation | O(1) | Depends |
| Observer | O(n) notify | O(n) observers | Requires sync |
| Strategy | O(1) selection | O(1) | Depends |

### 15.2 Memory Footprint

- **Singleton**: Minimal — one instance per application
- **Factory**: Moderate — depends on number of types
- **Builder**: Moderate — temporary builder object created
- **Decorator**: Can be heavy — chain of decorators

---

## 16. Best Practices

1. **Don't over-engineer**: Use patterns only when they solve a real problem
2. **Favor composition over inheritance**: Most patterns use composition
3. **Program to interfaces**: Depend on abstractions, not concretions
4. **Keep it simple**: The simplest solution that works is best
5. **Document pattern usage**: Future developers need to understand your choices
6. **Consider performance**: Some patterns add overhead
7. **Test thoroughly**: Patterns can introduce subtle bugs
8. **Use appropriate granularity**: Don't use a pattern for a single use case

---

## 17. Common Mistakes

1. **Pattern hunting**: Trying to force patterns where they don't belong
2. **Over-abstraction**: Creating too many interfaces and abstractions
3. **Ignoring simplicity**: Using complex patterns for simple problems
4. **Copy-paste implementation**: Not understanding the pattern's intent
5. **Missing the forest for the trees**: Focusing on patterns over business logic

---

## 18. Pitfalls

- **Performance overhead**: Some patterns add indirection
- **Increased complexity**: More classes and interfaces
- **Over-engineering**: Solutions worse than the problems they solve
- **Learning curve**: Team must understand the patterns used
- **Documentation burden**: Patterns require documentation

---

## 19. Debugging Tips

1. **Use logging**: Add logs in pattern implementations to trace execution
2. **Breakpoints**: Set breakpoints in pattern coordinating classes
3. **UML diagrams**: Visualize pattern structure
4. **Unit tests**: Test pattern implementations in isolation
5. **Code reviews**: Have team members review pattern usage

---

## 20. Comparison Table

| Pattern | Category | Purpose | Complexity |
|---------|----------|---------|------------|
| Singleton | Creational | Single instance | Low |
| Factory | Creational | Object creation | Medium |
| Builder | Creational | Complex construction | Medium |
| Prototype | Creational | Object cloning | Medium |
| Adapter | Structural | Interface conversion | Low |
| Decorator | Structural | Add behavior | Medium |
| Proxy | Structural | Control access | Medium |
| Observer | Behavioral | Event notification | Medium |
| Strategy | Behavioral | Algorithm selection | Low |
| Command | Behavioral | Encapsulate requests | Medium |
| Template Method | Behavioral | Define algorithm skeleton | Low |

---

## 21. Decision Tree

```
Need a single instance? → Singleton
Creating complex objects? → Builder
Need different object types? → Factory
Need to clone objects? → Prototype
Adapting interfaces? → Adapter
Adding behavior dynamically? → Decorator
Controlling access? → Proxy
Notifying multiple objects? → Observer
Need algorithm flexibility? → Strategy
Need undo/redo? → Command
Need standardized process? → Template Method
```

---

## 22. Interview Questions

### Q1: What are design patterns and why are they important?
**Answer**: Design patterns are reusable solutions to common software design problems. They provide a shared vocabulary, encode best practices, and improve code maintainability and flexibility.

### Q2: Name the three categories of design patterns.
**Answer**: Creational (object creation), Structural (object composition), and Behavioral (object communication).

### Q3: When would you NOT use a design pattern?
**Answer**: When the problem is simple, when it adds unnecessary complexity, or when a simpler solution exists. Don't force patterns where they don't belong.

### Q4: What's the difference between Singleton and Factory?
**Answer**: Singleton ensures only one instance exists. Factory creates different types of objects based on input. They serve different purposes and can be used together.

### Q5: How do patterns relate to SOLID principles?
**Answer**: Patterns reinforce SOLID principles — they promote single responsibility, open/closed, dependency inversion, and interface segregation.

---

## 23. Exercises

### Exercise 1: Pattern Identification
Given code snippets, identify which pattern (if any) is being used.

### Exercise 2: Anti-pattern Detection
Find and fix anti-patterns in provided code.

### Exercise 3: Pattern Selection
Given requirements, select the most appropriate pattern and justify your choice.

---

## 24. Assignments

1. **Assignment 1**: Implement a simple Factory pattern for creating different shapes
2. **Assignment 2**: Convert a Singleton implementation to use double-checked locking
3. **Assignment 3**: Create a Builder pattern for a complex configuration object

---

## 25. Mini Project

Design a **Notification System** that uses multiple patterns:
- Factory: Creating different notification types
- Observer: Notifying subscribers
- Strategy: Different delivery strategies
- Decorator: Adding logging, retry logic

---

## 26. Summary

- Design patterns are proven solutions to recurring problems
- Three categories: Creational, Structural, Behavioral
- Use patterns when they add value, not just for the sake of using them
- Patterns improve code maintainability, readability, and extensibility
- Java's OOP features make it ideal for pattern implementation
- Practice is essential to understand when and how to apply patterns

---

## 27. References

1. Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*
2. Freeman, E., & Robson, E. (2004). *Head First Design Patterns*
3. Bloch, J. (2018). *Effective Java* (3rd Edition)
4. Martin, R. C. (2017). *Clean Architecture*
5. Bloch, J. (2001). *Effective Java Programming Language Guide*
6. Gamma, E., et al. (1994). *Design Patterns — CD*
7. Freeman, S., & Freeman, E. (2004). *Head First Design Patterns*
8. Vlissides, J. (1996). *Pattern Hatching*
9. Meta programming: https://java-design-patterns.com
10. Refactoring Guru: https://refactoring.guru/design-patterns
