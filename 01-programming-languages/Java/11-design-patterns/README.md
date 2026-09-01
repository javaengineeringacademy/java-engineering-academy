# Module 11: Design Patterns

> **Difficulty:** ⭐⭐⭐ Intermediate  
> **Reading:** 40 min | **Practice:** 60 min | **Total:** 100 min

## Overview

Design patterns are reusable solutions to common problems in software design. The Gang of Four (GoF) catalog identified 23 patterns organized into creational, structural, and behavioral categories. This module covers all 23 GoF patterns plus enterprise patterns with Java implementations, trade-offs, and real-world use cases.

## Learning Objectives

- [ ] Identify when to apply each design pattern
- [ ] Implement all 23 GoF patterns in Java
- [ ] Choose the right pattern for a given problem
- [ ] Understand trade-offs and anti-patterns
- [ ] Apply enterprise patterns (Repository, DTO, Service Layer)
- [ ] Refactor code to use appropriate patterns

## Prerequisites

- Java fundamentals and OOP
- Understanding of interfaces, abstract classes, and inheritance
- Familiarity with common SOLID principles

## History

- **1994** — Gang of Four (Gamma, Helm, Johnson, Vlissides) published "Design Patterns: Elements of Reusable Object-Oriented Software"
- **1996** — Java 1.0 demonstrated pattern implementations in core libraries
- **2004** — Java 5 introduced enums, generics, annotations — enabling new pattern implementations
- **2014** — Java 8 lambdas enabled functional patterns (Strategy, Command with less boilerplate)
- **2021** — Java 17 sealed classes enabled new pattern implementations (Visitor, State)
- **2023** — Java 21 pattern matching for switch reduced need for some patterns (Visitor)

## Production Notes

- **Where is it used?** In every well-designed Java application
- **Why is it useful?** Provides proven solutions, common vocabulary, maintainable code
- **When should it be avoided?** When a simpler solution exists; don't over-engineer
- **Alternative?** SOLID principles, architectural patterns (MVC, MVVM)

## Why This Concept Exists

- **Code reuse** — Patterns are battle-tested solutions
- **Communication** — Common vocabulary for developers
- **Maintainability** — Proven structures are easier to understand
- **Interview prep** — Frequently asked in technical interviews

## Core Concepts

### Pattern Categories

```
Design Patterns
├── Creational (5) — How to create objects
│   ├── Singleton
│   ├── Factory Method
│   ├── Abstract Factory
│   ├── Builder
│   └── Prototype
├── Structural (7) — How to compose classes
│   ├── Adapter
│   ├── Bridge
│   ├── Composite
│   ├── Decorator
│   ├── Facade
│   ├── Flyweight
│   └── Proxy
└── Behavioral (11) — How to manage behavior
    ├── Chain of Responsibility
    ├── Command
    ├── Interpreter
    ├── Iterator
    ├── Mediator
    ├── Memento
    ├── Observer
    ├── State
    ├── Strategy
    ├── Template Method
    └── Visitor
```

### Pattern Selection Guide

```
Need to create objects? → Creational Patterns
Need to compose classes? → Structural Patterns
Need to manage behavior? → Behavioral Patterns
```

## Internal Working

### Singleton Pattern

```java
// Bill Pugh Singleton (recommended)
public class Singleton {
    private Singleton() {}
    
    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }
    
    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }
}

// Thread-safe with double-checked locking
public class ThreadSafeSingleton {
    private static volatile ThreadSafeSingleton instance;
    
    private ThreadSafeSingleton() {}
    
    public static ThreadSafeSingleton getInstance() {
        if (instance == null) {
            synchronized (ThreadSafeSingleton.class) {
                if (instance == null) {
                    instance = new ThreadSafeSingleton();
                }
            }
        }
        return instance;
    }
}
```

### Factory Method Pattern

```java
public interface Vehicle {
    void drive();
}

public class Car implements Vehicle {
    public void drive() { System.out.println("Driving car"); }
}

public class Truck implements Vehicle {
    public void drive() { System.out.println("Driving truck"); }
}

public class VehicleFactory {
    public static Vehicle create(String type) {
        return switch (type) {
            case "car" -> new Car();
            case "truck" -> new Truck();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}
```

### Strategy Pattern

```java
public interface SortStrategy<T extends Comparable<T>> {
    void sort(List<T> list);
}

public class BubbleSort<T extends Comparable<T>> implements SortStrategy<T> {
    public void sort(List<T> list) {
        // Bubble sort implementation
    }
}

public class QuickSort<T extends Comparable<T>> implements SortStrategy<T> {
    public void sort(List<T> list) {
        // Quick sort implementation
    }
}

public class Sorter<T extends Comparable<T>> {
    private SortStrategy<T> strategy;
    
    public Sorter(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }
    
    public void sort(List<T> list) {
        strategy.sort(list);
    }
}
```

### Observer Pattern

```java
public interface EventListener {
    void onEvent(String event);
}

public class EventManager {
    private final Map<String, List<EventListener>> listeners = new HashMap<>();
    
    public void subscribe(String event, EventListener listener) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }
    
    public void unsubscribe(String event, EventListener listener) {
        listeners.get(event).remove(listener);
    }
    
    public void notify(String event) {
        listeners.getOrDefault(event, List.of())
            .forEach(listener -> listener.onEvent(event));
    }
}
```

## Syntax

```java
// Singleton
Singleton instance = Singleton.getInstance();

// Factory
Vehicle vehicle = VehicleFactory.create("car");

// Strategy
Sorter<Integer> sorter = new Sorter<>(new QuickSort<>());
sorter.sort(numbers);

// Observer
EventManager manager = new EventManager();
manager.subscribe("click", e -> System.out.println("Clicked: " + e));
manager.notify("click");

// Decorator
InputStream stream = new BufferedInputStream(new FileInputStream("file.txt"));

// Builder
Person person = new Person.Builder()
    .name("Alice")
    .age(30)
    .build();
```

## Examples

### Easy: Singleton
```java
public class AppConfig {
    private static AppConfig instance;
    private String appName;
    private int maxConnections;
    
    private AppConfig() {
        this.appName = "MyApp";
        this.maxConnections = 10;
    }
    
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    public String getAppName() { return appName; }
    public int getMaxConnections() { return maxConnections; }
}
```

### Medium: Decorator
```java
public interface Coffee {
    String getDescription();
    double getCost();
}

public class SimpleCoffee implements Coffee {
    public String getDescription() { return "Simple coffee"; }
    public double getCost() { return 2.00; }
}

public class MilkDecorator implements Coffee {
    private final Coffee coffee;
    
    public MilkDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
    
    public String getDescription() { return coffee.getDescription() + ", milk"; }
    public double getCost() { return coffee.getCost() + 0.50; }
}

public class DecoratorDemo {
    public static void main(String[] args) {
        Coffee coffee = new MilkDecorator(new SimpleCoffee());
        System.out.println(coffee.getDescription() + " $" + coffee.getCost());
    }
}
```

### Hard: Visitor Pattern
```java
public interface Shape {
    void accept(ShapeVisitor visitor);
}

public class Circle implements Shape {
    private final double radius;
    
    public Circle(double radius) { this.radius = radius; }
    public double getRadius() { return radius; }
    
    public void accept(ShapeVisitor visitor) { visitor.visit(this); }
}

public class Rectangle implements Shape {
    private final double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    
    public void accept(ShapeVisitor visitor) { visitor.visit(this); }
}

public interface ShapeVisitor {
    void visit(Circle circle);
    void visit(Rectangle rectangle);
}

public class AreaCalculator implements ShapeVisitor {
    private double totalArea;
    
    public void visit(Circle circle) {
        totalArea += Math.PI * circle.getRadius() * circle.getRadius();
    }
    
    public void visit(Rectangle rectangle) {
        totalArea += rectangle.getWidth() * rectangle.getHeight();
    }
    
    public double getTotalArea() { return totalArea; }
}
```

### Enterprise: Repository Pattern
```java
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(ID id);
    boolean exists(ID id);
}

public class UserRepository implements Repository<User, Long> {
    private final Map<Long, User> store = new ConcurrentHashMap<>();
    
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
    
    public List<User> findAll() { return List.copyOf(store.values()); }
    
    public User save(User user) {
        store.put(user.getId(), user);
        return user;
    }
    
    public void delete(Long id) { store.remove(id); }
    
    public boolean exists(Long id) { return store.containsKey(id); }
}
```

## Performance Considerations

| Pattern | Cost | Notes |
|---------|------|-------|
| Singleton | Minimal | Thread-safe adds synchronization overhead |
| Factory | Minimal | Object creation cost |
| Proxy | Low | Extra method invocation |
| Observer | Low | List iteration for notifications |
| Decorator | Low | Extra object wrapping |

## Best Practices

**Do's:**
- Apply patterns only when needed (don't over-engineer)
- Prefer composition over inheritance
- Program to interfaces, not implementations
- Use enums for Singleton when possible
- Document pattern usage in code

**Don'ts:**
- Don't apply every pattern to every project
- Don't create patterns without a clear use case
- Don't use Singleton for stateful objects
- Don't use Factory when constructor is sufficient
- Don't forget to consider thread safety

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Over-engineering with patterns | Unnecessary complexity | Apply patterns only when needed |
| Singleton for stateful objects | Thread safety issues | Use dependency injection |
| Factory without clear benefit | Added complexity | Use constructor when sufficient |
| Observer without cleanup | Memory leaks | Unsubscribe when done |
| Decorator without interface | Type inconsistency | Always use interface |

## Interview Questions

### Q1: What is the difference between Singleton and Factory?
**Answer:** Singleton ensures one instance; Factory creates objects. Singleton is about instance control; Factory is about object creation. They can be combined (Factory returning Singleton).

### Q2: When should you use Strategy vs Template Method?
**Answer:** Strategy uses composition (pass algorithm as object). Template Method uses inheritance (override method). Strategy is more flexible; Template Method is simpler.

### Q3: What is the difference between Adapter and Decorator?
**Answer:** Adapter changes interface. Decorator adds behavior while keeping interface. Adapter converts; Decorator enhances.

### Q4: What is the Observer pattern and when to use it?
**Answer:** Observer defines one-to-many dependency: when one object changes state, all dependents are notified. Use for event systems, UI updates, message queues.

### Q5: What are the SOLID principles and how do they relate to patterns?
**Answer:** Single Responsibility, Open-Closed, Liskov Substitution, Interface Segregation, Dependency Inversion. Patterns implement SOLID principles (Strategy = Open-Closed, Adapter = Interface Segregation).

### Q6: What is the difference between Abstract Factory and Factory Method?
**Answer:** Factory Method creates one product. Abstract Factory creates families of related products. Factory Method uses inheritance; Abstract Factory uses composition.

### Q7: When should you use Decorator vs Proxy?
**Answer:** Decorator adds behavior transparently. Proxy controls access (lazy, caching, security). Decorator is about enhancement; Proxy is about control.

### Q8: What is the Builder pattern and when to use it?
**Answer:** Builder constructs complex objects step by step. Use when constructor has many parameters, when you want immutable objects, or when construction involves multiple steps.

### Q9: What is the Composite pattern?
**Answer:** Composite lets you treat individual objects and compositions uniformly. Use for tree structures (file systems, UI components, organization charts).

### Q10: What is the difference between Command and Strategy?
**Answer:** Command encapsulates a request as an object (undo/redo). Strategy encapsulates an algorithm as an object (interchangeable algorithms). Command is about actions; Strategy is about behavior.

## Cross-References

- **Previous Module:** [10 - JVM Internals](../10-jvm-internals/)
- **Next Module:** [12 - Testing](../12-testing/)
- **Related:** [02 - OOP](../02-oop/) — inheritance, polymorphism, encapsulation
- **Related:** [07 - Functional Programming](../07-functional-programming/) — lambda-based patterns
- **Related:** [15 - Senior](../15-senior/) — architecture patterns

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Singleton not thread-safe | Thread dump + stress testing | Run with multiple threads, check instance count |
| Factory returning wrong type | Debug stepping | Verify factory switch/if-else logic |
| Observer memory leak | Heap dump | Find unsubscribed listeners |
| Decorator type mismatch | Compile-time checking | Verify decorator implements same interface |
| Builder missing field | Unit test | Test builder with all fields set |

## Code Review Checklist

- [ ] Pattern application is justified (not over-engineering)
- [ ] Thread safety considered for Singleton
- [ ] Factory methods are static or use DI
- [ ] Observer subscriptions are cleaned up
- [ ] Decorators implement the same interface
- [ ] Builders validate required fields

## Architecture Considerations

Design patterns are building blocks of software architecture. At scale, pattern choice determines code maintainability, extensibility, and team productivity. For microservices, patterns like Repository, DTO, and Service Layer provide consistent structure. For event-driven systems, Observer and Strategy enable loose coupling.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Singleton | Configuration, caches | Pros: Single instance; Cons: Testing difficulty |
| Factory | Object creation | Pros: Flexibility; Cons: Added complexity |
| Strategy | Algorithm selection | Pros: Open-Closed; Cons: Class explosion |
| Observer | Event systems | Pros: Loose coupling; Cons: Memory leaks |
| Decorator | Feature enhancement | Pros: Transparent; Cons: Many small classes |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Singleton reflection attack | Multiple instances | Use enum Singleton or serialization protection |
| Factory injection | Wrong object creation | Validate factory input |
| Proxy bypass | Security bypass | Ensure proxy is not optional |
| Observer data leakage | Information exposure | Validate observer access |
| Builder missing validation | Invalid objects | Validate in build() method |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | Basic patterns | N/A — foundational |
| Java 5 | Enums for Singleton | Use enum Singleton |
| Java 8 | Lambdas for Strategy/Command | Replace anonymous classes with lambdas |
| Java 14 | Switch expressions | Use switch expressions in Factory |
| Java 17 | Sealed classes | Use sealed classes for Visitor/State |
| Java 21 | Pattern matching | Use pattern matching for Visitor |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Basic patterns | Java 1.0 | Stable |
| Enum Singleton | Java 5 | Stable |
| Lambda Strategy | Java 8 | Stable |
| Sealed classes | Java 17 | Stable |
| Pattern matching | Java 21 | Stable |

## Production Incidents

### Incident 1: Singleton Not Thread-Safe

**Problem:** A configuration singleton was created multiple times under high concurrency, causing inconsistent settings.
**Cause:** Double-checked locking without `volatile` keyword; JVM reordering caused partial initialization visibility.
**Impact:** Application used mixed configurations; 5% of requests had wrong settings.
**Detection:** Configuration values varied between requests; thread dump showed multiple initialization paths.
**Solution:** Used enum Singleton or added `volatile` to double-checked locking.
**Prevention:** Use enum Singleton; always use `volatile` with double-checked locking.

### Incident 2: Observer Memory Leak

**Problem:** A UI application leaked memory over time due to unsubscribed event listeners.
**Cause:** Event listeners were registered but never unsubscribed; objects remained referenced.
**Impact:** Memory usage grew 10MB per hour; eventually caused OOM.
**Detection:** Heap dump showed growing listener lists; MAT analysis found listener references.
**Solution:** Added cleanup in `destroy()` methods; used WeakReference for listeners.
**Prevention:** Always unsubscribe observers; use WeakReference for event listeners.

### Incident 3: Factory Pattern Overuse

**Problem:** A simple application had 15 factory classes for 20 domain objects, making code hard to navigate.
**Cause:** Developer applied Factory pattern to every object creation, even simple ones.
**Impact:** Codebase bloated; new developers confused by unnecessary complexity.
**Production:** Code review identified over-engineering; simplified to direct constructors.
**Solution:** Removed factories for simple objects; kept factories only for complex creation logic.
**Prevention:** Apply patterns only when justified; prefer simplicity.

## Production Checklist

- [ ] Singleton is thread-safe (enum or volatile)
- [ ] Factory validates input
- [ ] Observer subscriptions are cleaned up
- [ ] Decorators implement same interface
- [ ] Builders validate required fields
- [ ] Patterns are justified (not over-engineered)

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Knows Singleton and Factory; doesn't understand when to use patterns |
| Intermediate | Applies patterns correctly; understands trade-offs |
| Advanced | Chooses appropriate patterns; refactors to patterns; mentors on patterns |
| Expert | Designs pattern-based architectures; contributes to pattern literature |

## Common Myths

1. **Myth**: Always use design patterns
   **Truth**: Apply patterns only when needed. Over-engineering with patterns creates unnecessary complexity.

2. **Myth**: Singleton is always safe
   **Truth**: Singleton is not thread-safe without proper implementation (enum, volatile, synchronization).

3. **Myth**: Patterns are language-agnostic
   **Truth**: While patterns are conceptual, implementation varies. Java patterns differ from Python patterns.

4. **Myth**: More patterns = better code
   **Truth**: Simpler code is often better. Use patterns when they add clear value.

5. **Myth**: Patterns replace good design
   **Truth**: Patterns are tools, not substitutes for good design principles (SOLID, KISS, YAGNI).

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Reusable solutions to common problems |
| Categories | Creational (5), Structural (7), Behavioral (11) |
| Singleton | One instance per JVM |
| Factory | Creates objects without specifying class |
| Strategy | Interchangeable algorithms |
| Observer | One-to-many dependency |
| Decorator | Adds behavior transparently |
| Best practice | Apply only when needed |
| Common mistake | Over-engineering |
| When to use | When solving common design problems |
| When to avoid | When simpler solution exists |
