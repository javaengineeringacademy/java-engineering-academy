# Design Patterns Quiz

## Question 1 (Production Scenario)
Your application uses a database connection pool that must be shared across all threads. Multiple instances would cause resource exhaustion. The code runs in a clustered environment where multiple JVMs might start simultaneously. Which pattern should you use?

- A) Factory Method for creating connections
- B) Singleton with double-checked locking for thread-safe single instance
- C) Builder for configuring the pool
- D) Prototype for cloning connections

**Answer: B**
**Explanation:** Singleton ensures only one instance of the connection pool exists per JVM. Double-checked locking with `volatile` provides thread-safe lazy initialization without synchronization overhead on every access. In clustered environments, each JVM has its own Singleton instance.

---

## Question 2 (Production Scenario)
Your e-commerce system has an `OrderProcessor` class with a `processOrder(String type)` method that uses if-else to handle "standard", "express", and "international" orders. A new "subscription" order type must be added. The team is concerned about modifying existing code. Which pattern addresses this?

- A) Singleton to manage order processing
- B) Strategy pattern — define an `OrderStrategy` interface, implement separately for each type
- C) Adapter to convert order types
- D) Observer to notify when orders complete

**Answer: B**
**Explanation:** Strategy pattern defines a family of algorithms (order processing logic) and makes them interchangeable. Adding a new order type requires creating a new `OrderStrategy` implementation — no existing code is modified. This follows the Open/Closed Principle.

---

## Question 3 (Debugging)
A developer implements a Singleton for a configuration manager. In production, two instances occasionally appear under high concurrency. The code:

```java
public class ConfigManager {
    private static ConfigManager instance;
    private ConfigManager() {}
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
}
```

What is the bug?

- A) The constructor should be public
- B) The Singleton is not thread-safe — two threads can pass the null check simultaneously and create two instances
- C) Static methods cannot create instances
- D) The class should implement an interface

**Answer: B**
**Explanation:** Without synchronization, Thread A checks `instance == null`, then Thread B also checks before Thread A assigns. Both create instances, and one is lost. Fix: use double-checked locking with `volatile`, enum Singleton, or lazy initialization holder pattern.

---

## Question 4 (Production Scenario)
Your HTTP server needs to process requests through multiple middleware steps (authentication, logging, validation, rate limiting). Steps may be added or removed dynamically. Which pattern allows flexible composition?

- A) Create a single method with all steps hardcoded
- B) Chain of Responsibility — each middleware processes the request and passes it to the next handler
- C) Use inheritance to create middleware classes
- D) Store middleware configuration in a database

**Answer: B**
**Explanation:** Chain of Responsibility allows flexible composition of processing steps. Each handler decides whether to process the request and pass it forward. New middleware can be added without modifying existing ones, following the Open/Closed Principle.

---

## Question 5 (Code Output)
What pattern does this code demonstrate?

```java
interface PaymentProcessor {
    PaymentResult process(double amount);
}

class CreditCardProcessor implements PaymentProcessor {
    public PaymentResult process(double amount) {
        return new PaymentResult(true, "Credit card charged: $" + amount);
    }
}

class PayPalProcessor implements PaymentProcessor {
    public PaymentResult process(double amount) {
        return new PaymentResult(true, "PayPal charged: $" + amount);
    }
}

class OrderService {
    private PaymentProcessor processor;

    OrderService(PaymentProcessor processor) {
        this.processor = processor;
    }

    void checkout(double amount) {
        processor.process(amount);
    }
}
```

**Answer:** Strategy pattern (also demonstrates Dependency Injection)
**Explanation:** Different payment processors implement the same interface. The OrderService depends on the abstraction, not concrete classes. The processor can be swapped at runtime, demonstrating the Strategy pattern.

---

## Question 6 (Code Output)
What does this code demonstrate?

```java
abstract class Shape {
    abstract void draw();
}

class Circle extends Shape {
    void draw() { System.out.println("Drawing circle"); }
}

class Rectangle extends Shape {
    void draw() { System.out.println("Drawing rectangle"); }
}

public class Main {
    public static void main(String[] args) {
        Shape s1 = new Circle();
        Shape s2 = new Rectangle();
        s1.draw();
        s2.draw();
    }
}
```

**Answer:** Polymorphism (runtime) — the correct draw() method is called based on the actual object type at runtime
**Explanation:** This demonstrates runtime polymorphism through method overriding. The reference type is Shape, but the actual objects are Circle and Rectangle, so their respective draw() methods are called.

---

## Question 7 (Bug Finding)
Find the bug:

```java
public class Singleton {
    private static Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

**Bug:** This Singleton implementation is not thread-safe. In a multithreaded environment, two threads could simultaneously pass the null check and create two instances.
**Fix:** Use double-checked locking or an enum:
```java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
public class OrderProcessor {
    public void processOrder(String type) {
        if (type.equals("standard")) {
            // process standard order
        } else if (type.equals("express")) {
            // process express order
        } else if (type.equals("international")) {
            // process international order
        }
    }
}
```

**Bug:** This violates the Open/Closed Principle. Adding a new order type requires modifying the processOrder method. It also violates the Single Responsibility Principle — the method knows about all order types.
**Fix:** Use the Strategy pattern:
```java
interface OrderStrategy {
    void process();
}

class StandardOrder implements OrderStrategy {
    public void process() { /* ... */ }
}

class OrderProcessor {
    private OrderStrategy strategy;

    OrderProcessor(OrderStrategy strategy) {
        this.strategy = strategy;
    }

    void processOrder() {
        strategy.process();
    }
}
```

---

## Question 9 (Scenario-based)
You are building a system where objects need to be created with different configurations (size, color, material). The construction process is complex with many optional parameters. Which creational pattern should you use?

- A) Singleton
- B) Factory Method
- C) Builder
- D) Prototype

**Answer: C**
**Explanation:** The Builder pattern is ideal for constructing complex objects step by step, especially when there are many optional parameters. It provides a fluent API for configuration and produces immutable objects.

---

## Question 10 (Architecture Decision)
You need to design a middleware pipeline for an HTTP server where each request passes through multiple processing steps (authentication, logging, validation, rate limiting). Steps may be added or removed. How should you architect this?

- A) Create a single method with all steps hardcoded
- B) Implement the Chain of Responsibility pattern where each middleware processes the request and passes it to the next handler
- C) Use inheritance to create middleware classes
- D) Store middleware configuration in a database

**Answer: B**
**Explanation:** Chain of Responsibility allows flexible composition of processing steps. Each handler decides whether to process the request and pass it forward. New middleware can be added without modifying existing ones, following the Open/Closed Principle.

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
class Singleton {
    private static Singleton instance;
    private Singleton() { System.out.print("Created "); }
    static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

public class Main {
    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        Singleton s3 = Singleton.getInstance();
        System.out.println(s1 == s2);
        System.out.println(s2 == s3);
    }
}
```

A) Created true true
B) Created Created Created false false
C) Created true true
D) Created false false

**Answer: A**
**Explanation:** First call to `getInstance()` creates the instance (prints "Created ") since it's null. Subsequent calls return the same instance without creating new ones. `s1 == s2` and `s2 == s3` are both true because all references point to the same object. Output: `Created true true`.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
interface Observer {
    void update(String event);
}

class EventEmitter {
    private java.util.List<Observer> observers = new java.util.ArrayList<>();
    void subscribe(Observer o) { observers.add(o); }
    void emit(String event) {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(event);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        EventEmitter emitter = new EventEmitter();
        emitter.subscribe(e -> System.out.print("A:" + e + " "));
        emitter.subscribe(e -> System.out.print("B:" + e + " "));
        emitter.subscribe(e -> System.out.print("C:" + e + " "));
        emitter.emit("Event1");
    }
}
```

A) A:Event1 B:Event1 C:Event1
B) C:Event1 B:Event1 A:Event1
C) A:Event1 C:Event1 B:Event1
D) Output is unpredictable

**Answer: A**
**Explanation:** Observers are notified in the order they were subscribed (iteration uses index 0, 1, 2). The first subscriber prints "A:Event1", second "B:Event1", third "C:Event1". Output: `A:Event1 B:Event1 C:Event1`. The Observer pattern guarantees notification order matches subscription order.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
interface DataSource {
    String writeData(String data);
}

class FileDataSource implements DataSource {
    public String writeData(String data) {
        return "File: " + data;
    }
}

class DataSourceDecorator implements DataSource {
    protected DataSource wrappee;
    DataSourceDecorator(DataSource source) { this.wrappee = source; }
    public String writeData(String data) { return wrappee.writeData(data); }
}

class EncryptionDecorator extends DataSourceDecorator {
    EncryptionDecorator(DataSource source) { super(source); }
    public String writeData(String data) {
        return wrappee.writeData("Encrypted(" + data + ")");
    }
}

class CompressionDecorator extends DataSourceDecorator {
    CompressionDecorator(DataSource source) { super(source); }
    public String writeData(String data) {
        return wrappee.writeData("Compressed(" + data + ")");
    }
}

public class Main {
    public static void main(String[] args) {
        DataSource source = new CompressionDecorator(
            new EncryptionDecorator(
                new FileDataSource("test.txt")));
        System.out.println(source.writeData("Hello"));
    }
}
```

A) File: Compressed(Encrypted(Hello))
B) File: Encrypted(Compressed(Hello))
C) Compressed(Encrypted(File: Hello))
D) Encrypted(Compressed(File: Hello))

**Answer: A**
**Explanation:** Decorator chain: FileDataSource → EncryptionDecorator → CompressionDecorator. `writeData("Hello")` calls CompressionDecorator first, which calls EncryptionDecorator, which calls FileDataSource. FileDataSource returns "File: Hello". EncryptionDecorator wraps it: "File: Encrypted(Hello)". CompressionDecorator wraps that: "File: Compressed(Encrypted(Hello))". Output: `File: Compressed(Encrypted(Hello))`.

