# Object-Oriented Programming Fundamentals

## 1. Introduction

Object-Oriented Programming (OOP) is a programming paradigm that organizes software design around objects rather than functions and logic. An object is a data field that has unique attributes and behavior. OOP focuses on binding data with the methods that operate on that data, and restricting direct access to some of an object's components — a principle called encapsulation.

Java is a class-based, object-oriented language. Everything in Java resides inside a class (except primitives). Mastering OOP is essential for writing maintainable, scalable, and testable Java applications.

This document covers the complete OOP theory — from fundamentals through advanced design principles — with Java 21 examples following Google Java Style.

---

## 2. Learning Objectives

By the end of this material, you will be able to:

- Explain the four pillars of OOP: Encapsulation, Inheritance, Polymorphism, and Abstraction
- Design well-structured classes using proper access control and encapsulation
- Implement inheritance hierarchies and apply polymorphism in real scenarios
- Use abstract classes and interfaces effectively
- Differentiate between composition and aggregation and know when to use each
- Apply SOLID principles to produce clean, maintainable object-oriented designs
- Override `equals()`, `hashCode()`, and `toString()` correctly
- Use modern Java features: Records (Java 16+), sealed classes (Java 17+), and pattern matching
- Recognize common OOP mistakes and avoid them

---

## 3. Prerequisites

Before proceeding, you should be familiar with:

- **Java basics**: variables, data types, operators, control flow
- **Methods**: declaring, calling, parameter passing, return values
- **Arrays and basic collections**: `ArrayList`, `HashMap`
- **Basic UML diagrams**: class diagrams, association arrows
- **Command-line compilation**: `javac` and `java` commands
- **Java 17+ features**: sealed classes, pattern matching for `instanceof`

---

## 4. Why This Concept Exists

Before OOP, programs were written in procedural style — a sequence of instructions operating on shared data. This created problems as programs grew:

| Problem | Description |
|---------|-------------|
| **Code duplication** | Similar logic repeated across many functions |
| **Tight coupling** | Changes in data format broke unrelated functions |
| **Difficult maintenance** | Bug fixes required understanding the entire codebase |
| **No reuse** | Functions could not be easily reused across projects |
| **Scalability limits** | Adding features meant modifying existing, working code |

OOP solves these problems through:

- **Encapsulation**: hides internal state, exposes only what is necessary
- **Inheritance**: enables code reuse through class hierarchies
- **Polymorphism**: allows interchangeable components via common interfaces
- **Abstraction**: manages complexity by hiding unnecessary details

---

## 5. Problem Statement

A retail system needs to manage different product types — each with unique pricing rules, discount logic, and tax calculations. A procedural approach would require scattered `if-else` blocks throughout the codebase:

```java
// Procedural approach — fragile and hard to extend
String calculatePrice(String type, double basePrice) {
    if (type.equals("ELECTRONICS")) {
        return basePrice * 1.18; // 18% tax
    } else if (type.equals("FOOD")) {
        return basePrice * 1.05; // 5% tax
    } else if (type.equals("CLOTHING")) {
        return basePrice * 1.12; // 12% tax
    }
    return basePrice;
}
```

Adding a new product type means modifying this method — violating the Open/Closed Principle. OOP solves this by modeling each product type as a class that encapsulates its own pricing logic.

---

## 6. Theory

### 6.1 Classes and Objects

A **class** is a blueprint that defines the structure (fields) and behavior (methods) of objects. An **object** is a runtime instance of a class — it has state, behavior, and a unique identity (memory reference).

```
Class (Blueprint)                 Object (Instance)
┌──────────────────┐            ┌──────────────────┐
│  Dog             │            │  Dog "Rex"        │
│  ─ name: String  │   new →   │  ─ name: "Rex"   │
│  ─ age: int      │            │  ─ age: 5         │
│  + bark(): void  │            │  + bark(): void   │
└──────────────────┘            └──────────────────┘
        ▲                                ▲
   One blueprint              Many objects from one class
```

### 6.2 The Four Pillars

```
                    ┌─────────────────────┐
                    │       OOP           │
                    │   Four Pillars      │
                    └─────────┬───────────┘
          ┌───────────┬───────┴───────┬───────────┐
          ▼           ▼               ▼           ▼
   ┌──────────┐ ┌──────────┐  ┌──────────┐ ┌──────────┐
   │Encapsul- │ │Inherit-  │  │Poly-     │ │Abstract- │
   │ation     │ │ance      │  │morphism  │ │ion       │
   └──────────┘ └──────────┘  └──────────┘ └──────────┘
```

### 6.3 Encapsulation

Encapsulation bundles data with the methods that operate on that data and restricts direct access to an object's internal state. This is achieved through access modifiers:

| Modifier | Class | Package | Subclass | World |
|----------|:-----:|:-------:|:--------:|:-----:|
| `private` | Yes | No | No | No |
| default | Yes | Yes | No | No |
| `protected` | Yes | Yes | Yes | No |
| `public` | Yes | Yes | Yes | Yes |

### 6.4 Inheritance

Inheritance allows a class (subclass) to derive from another class (superclass), inheriting its fields and methods. Java supports single inheritance — each class has exactly one direct superclass. Multiple inheritance of type is achieved through interfaces.

### 6.5 Polymorphism

Polymorphism means "many forms." It allows objects of different classes to be treated through a common interface. Compile-time polymorphism is achieved via method overloading; runtime polymorphism via method overriding and virtual dispatch.

### 6.6 Abstraction

Abstraction hides complexity by exposing only relevant details. Abstract classes define partial implementations; interfaces define pure contracts. Both allow you to program to an interface rather than a concrete implementation.

---

## 7. Internal Working

### 7.1 Object Creation Lifecycle

When you write `Dog rex = new Dog("Rex", 5);`, the JVM performs these steps:

1. **Class Loading**: The JVM loads `Dog.class` into the metaspace if not already loaded
2. **Memory Allocation**: Allocates memory on the heap for all instance fields (including inherited ones)
3. **Default Initialization**: All fields receive default values (`0`, `null`, `false`)
4. **Constructor Execution**: The constructor runs top-down (`super()` first, then field initializers, then constructor body)
5. **Reference Assignment**: The heap address is stored on the stack in the local variable `rex`

### 7.2 Method Resolution

At runtime, when `rex.bark()` is called:

1. The JVM looks up the virtual method table (vtable) for the object's actual class
2. If `bark()` is overridden in `Dog`, the Dog's implementation is called
3. If not overridden, the superclass implementation is called
4. For `static` methods, the reference type determines which method is called (no virtual dispatch)

### 7.3 Inheritance Chain Resolution

```
Dog → Animal → Object
```

When `rex.toString()` is called and Dog does not override it:
1. JVM checks Dog — no `toString()` found
2. JVM checks Animal — no `toString()` found
3. JVM checks Object — `toString()` found, calls it

---

## 8. JVM Perspective

### 8.1 Class Loading

The JVM loads class files into the **Metaspace** (native memory, replaces PermGen since Java 8). Each loaded class is represented by a `java.lang.Class` object.

```
ClassLoader Hierarchy:
┌─────────────────────┐
│ Bootstrap ClassLoader │ ← loads java.lang.*, java.util.* (rt.jar)
├─────────────────────┤
│ Platform ClassLoader │ ← loads java.xml.*, java.sql.* (since Java 9)
├─────────────────────┤
│ Application CL       │ ← loads your classes from classpath
└─────────────────────┘
```

### 8.2 Virtual Method Table (vtable)

Every class with overridden methods has a vtable — an array of method pointers. The JVM uses this for dynamic dispatch:

```
Object vtable:          Dog vtable (extends Animal):
┌──────────────────┐   ┌──────────────────┐
│ toString()       │   │ toString()       │ ← Dog's impl
│ equals()         │   │ equals()         │ ← Dog's impl
│ hashCode()       │   │ hashCode()       │ ← Dog's impl
│ getClass()       │   │ getClass()       │
│ clone()          │   │ clone()          │
│ finalize()       │   │ finalize()       │
└──────────────────┘   │ bark()           │ ← Dog-specific
                       │ sound()          │ ← Animal's impl
                       └──────────────────┘
```

### 8.3 Interface Dispatch

Interfaces use **itable** (interface method table) which is more complex than vtables because a class can implement multiple interfaces. The JVM performs a linear search or uses a cache for itable lookups, which is why interface method calls are slightly slower than virtual calls.

### 8.4 Memory Layout of Objects

On a 64-bit JVM with compressed oops (Ordinary Object Pointers):

```
Object Header (12 bytes):
┌────────────────────────────────────────┐
│ Mark Word (8 bytes): hashcode, age,    │
│   lock bits, GC age                    │
│ Klass Pointer (4 bytes): points to     │
│   class metadata in Metaspace          │
└────────────────────────────────────────┘
Instance Fields (aligned to 8 bytes):
┌────────────────────────────────────────┐
│ field1: type (size)                    │
│ field2: type (size)                    │
│ ... padding to 8-byte boundary         │
└────────────────────────────────────────┘
```

---

## 9. Memory Representation

### 9.1 Stack vs Heap

```
Stack (per thread)                     Heap (shared)
┌──────────────────────┐
│ main()               │              ┌──────────────────┐
│  └─ Dog rex          │──────────────│ Dog object       │
│     ref: 0x7f3a      │              │  header: 12B     │
├──────────────────────┤              │  name: "Rex" ────────┐
│ createDog()          │              │  age: 5          │   │
│  └─ String localName │              └──────────────────┘   │
│     ref: 0x7f2b      │──┐                                 │
├──────────────────────┤  │         ┌──────────────────┐    │
│ ...                  │  └────────▶│ "Rex" (String)   │    │
└──────────────────────┘            │  header: 12B     │    │
                                    │  value: char[] ────────┐
Stack frames are per-thread,        └──────────────────┘    │
temporary. Heap is shared,                                          │
managed by GC.                                         ┌──────────────────┐
                                                       │ char[]           │
                                                       │  {'R','e','x'}  │
                                                       └──────────────────┘
```

### 9.2 Object References

```java
Dog rex = new Dog("Rex", 5);
Dog sameRex = rex;        // sameRex points to the SAME object
Dog anotherRex = new Dog("Rex", 5); // different object, same values

rex == sameRex;           // true — same reference
rex == anotherRex;        // false — different objects
rex.equals(anotherRex);   // true if equals() is properly overridden
```

### 9.3 Garbage Collection

The JVM's garbage collector reclaims heap memory when objects are no longer reachable:

```
Dog rex = new Dog("Rex", 5);  // Dog object: reachable
rex = null;                    // Dog object: unreachable → eligible for GC
```

Key GC algorithms:
- **G1 (default since Java 9)**: Partitioned heap, balances latency and throughput
- **ZGC (production since Java 15)**: Ultra-low latency (< 10ms pauses)
- **Shenandoah**: Low-pause concurrent collector

---

## 10. Syntax

### 10.1 Class Declaration

```java
[modifiers] class ClassName [extends SuperClass] [implements Interface1, Interface2] {
    // fields
    // constructors
    // methods
    // inner classes
}
```

### 10.2 Fields

```java
public class Employee {
    private final String name;           // instance field
    private int age;
    static int employeeCount;            // class field (shared)
    public static final String COMPANY = "Acme"; // constant
}
```

### 10.3 Constructors

```java
public class Person {
    private final String name;
    private final int age;

    // No-arg constructor
    public Person() {
        this("Unknown", 0);  // constructor chaining
    }

    // Parameterized constructor
    public Person(String name, int age) {
        this.name = Objects.requireNonNull(name, "name");
        this.age = age;
    }

    // Copy constructor
    public Person(Person other) {
        this(other.name, other.age);
    }
}
```

### 10.4 Methods

```java
public class Calculator {
    // Instance method
    public int add(int a, int b) {
        return a + b;
    }

    // Static method
    public static int multiply(int a, int b) {
        return a * b;
    }

    // Varargs method
    public int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) {
            total += n;
        }
        return total;
    }

    // Generic method
    public <T> T first(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }
}
```

### 10.5 Inheritance Syntax

```java
public class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public void speak() {
        System.out.println(name + " makes a sound");
    }
}

public class Dog extends Animal {
    private final String breed;

    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;
    }

    @Override
    public void speak() {
        System.out.println(name + " barks");
    }

    public void fetch(String item) {
        System.out.println(name + " fetches the " + item);
    }
}
```

### 10.6 Interface Syntax

```java
public interface Drawable {
    void draw();

    default void fill(Color color) {
        System.out.println("Filling with " + color);
    }

    static Drawable empty() {
        return g -> { };
    }
}

public interface Resizable {
    void resize(double factor);
}

// Multiple interface implementation
public class Circle implements Drawable, Resizable {
    private double radius;

    @Override
    public void draw() {
        System.out.println("Drawing circle with radius " + radius);
    }

    @Override
    public void resize(double factor) {
        radius *= factor;
    }
}
```

---

## 11. Easy Example

A simple example demonstrating classes, objects, constructors, and basic encapsulation.

```java
public class Car {
    private final String make;
    private final String model;
    private int speed;

    public Car(String make, String model) {
        this.make = make;
        this.model = model;
        this.speed = 0;
    }

    public void accelerate(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        speed += amount;
    }

    public void brake(int amount) {
        speed = Math.max(0, speed - amount);
    }

    public String getInfo() {
        return make + " " + model + " @ " + speed + " km/h";
    }

    public int getSpeed() {
        return speed;
    }

    public static void main(String[] args) {
        Car car = new Car("Toyota", "Camry");
        car.accelerate(60);
        car.brake(20);
        System.out.println(car.getInfo());
        // Output: Toyota Camry @ 40 km/h
    }
}
```

---

## 12. Medium Example

Demonstrating inheritance, polymorphism, and abstract classes with a shape hierarchy.

```java
public abstract class Shape {
    private final String color;

    protected Shape(String color) {
        this.color = color;
    }

    public abstract double area();
    public abstract double perimeter();

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "%s[color=%s, area=%.2f]".formatted(
            getClass().getSimpleName(), color, area());
    }
}

public class Circle extends Shape {
    private final double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
}

public class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
}

public class Triangle extends Shape {
    private final double a, b, c; // sides

    public Triangle(String color, double a, double b, double c) {
        super(color);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double area() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    @Override
    public double perimeter() {
        return a + b + c;
    }
}

// Usage — polymorphism in action
public class ShapeDemo {
    public static void printShapeInfo(Shape shape) {
        System.out.println(shape);
    }

    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle("Red", 5),
            new Rectangle("Blue", 4, 6),
            new Triangle("Green", 3, 4, 5)
        };

        for (Shape shape : shapes) {
            printShapeInfo(shape);
        }
    }
}
```

---

## 13. Hard Example

Enterprise-level example demonstrating interfaces, composition, dependency injection, and SOLID principles.

```java
// Domain interfaces
public interface PaymentProcessor {
    PaymentResult process(PaymentRequest request);
    boolean supports(PaymentMethod method);
}

public interface NotificationService {
    void sendPaymentConfirmation(Order order, PaymentResult result);
}

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(String id);
}

// Value objects (records)
public record PaymentRequest(
    String orderId,
    BigDecimal amount,
    PaymentMethod method,
    String customerEmail
) {
    public PaymentRequest {
        Objects.requireNonNull(orderId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}

public enum PaymentMethod {
    CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING
}

public record PaymentResult(
    boolean success,
    String transactionId,
    String failureReason
) {
    public static PaymentResult success(String transactionId) {
        return new PaymentResult(true, transactionId, null);
    }

    public static PaymentResult failure(String reason) {
        return new PaymentResult(false, null, reason);
    }
}

// Concrete implementation
public class StripePaymentProcessor implements PaymentProcessor {
    private static final Set<PaymentMethod> SUPPORTED = Set.of(
        PaymentMethod.CREDIT_CARD, PaymentMethod.DEBIT_CARD
    );

    @Override
    public PaymentResult process(PaymentRequest request) {
        if (!supports(request.method())) {
            return PaymentResult.failure("Unsupported method: " + request.method());
        }
        // Simulate Stripe API call
        String txnId = "txn_" + UUID.randomUUID().toString().substring(0, 8);
        return PaymentResult.success(txnId);
    }

    @Override
    public boolean supports(PaymentMethod method) {
        return SUPPORTED.contains(method);
    }
}

// Order service — uses constructor injection
public class OrderService {
    private final PaymentProcessor paymentProcessor;
    private final NotificationService notificationService;
    private final OrderRepository orderRepository;

    public OrderService(
            PaymentProcessor paymentProcessor,
            NotificationService notificationService,
            OrderRepository orderRepository) {
        this.paymentProcessor = Objects.requireNonNull(paymentProcessor);
        this.notificationService = Objects.requireNonNull(notificationService);
        this.orderRepository = Objects.requireNonNull(orderRepository);
    }

    public OrderResult placeOrder(Order order) {
        PaymentRequest request = new PaymentRequest(
            order.id(),
            order.total(),
            order.paymentMethod(),
            order.customerEmail()
        );

        PaymentResult paymentResult = paymentProcessor.process(request);

        if (paymentResult.success()) {
            order.markPaid(paymentResult.transactionId());
            orderRepository.save(order);
            notificationService.sendPaymentConfirmation(order, paymentResult);
            return OrderResult.success(order.id());
        }

        return OrderResult.failure(paymentResult.failureReason());
    }
}
```

---

## 14. Enterprise Example

A complete enterprise-style example with multiple layers, demonstrating composition, SOLID principles, and design patterns.

```java
// Entity layer
public class Product {
    private final String id;
    private final String name;
    private final BigDecimal price;
    private int stockQuantity;

    public Product(String id, String name, BigDecimal price, int stockQuantity) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.price = Objects.requireNonNull(price);
        this.stockQuantity = stockQuantity;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity > stockQuantity) {
            throw new InsufficientStockException(id, quantity, stockQuantity);
        }
        stockQuantity -= quantity;
    }

    // Getters
    public String id() { return id; }
    public String name() { return name; }
    public BigDecimal price() { return price; }
    public int stockQuantity() { return stockQuantity; }
}

// Repository layer
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    void save(T entity);
    void delete(T entity);
}

public class InMemoryProductRepository implements Repository<Product, String> {
    private final Map<String, Product> store = new ConcurrentHashMap<>();

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public void save(Product product) {
        store.put(product.id(), product);
    }

    @Override
    public void delete(Product product) {
        store.remove(product.id());
    }
}

// Service layer
public class InventoryService {
    private final Repository<Product, String> productRepository;

    public InventoryService(Repository<Product, String> productRepository) {
        this.productRepository = productRepository;
    }

    public boolean checkAvailability(String productId, int quantity) {
        return productRepository.findById(productId)
            .map(p -> p.stockQuantity() >= quantity)
            .orElse(false);
    }

    public void reserveStock(String productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
        product.reduceStock(quantity);
        productRepository.save(product);
    }
}

// Exception hierarchy
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(String productId) {
        super("Product not found: " + productId);
    }
}

public class InsufficientStockException extends DomainException {
    public InsufficientStockException(String productId, int requested, int available) {
        super("Insufficient stock for %s: requested %d, available %d"
            .formatted(productId, requested, available));
    }
}
```

---

## 15. Performance

### 15.1 Object Creation Cost

Creating objects in Java is relatively cheap but not free:

| Operation | Approximate Cost |
|-----------|-----------------|
| Object allocation (empty) | ~12-16 bytes header + alignment |
| `new Object()` | ~50-100ns on modern JVM |
| `new String("abc")` | ~100-150ns (includes char array) |
| Primitive array allocation | ~10-20ns + data size |

### 15.2 Method Dispatch Cost

| Call Type | Mechanism | Relative Cost |
|-----------|-----------|:-------------:|
| Static method | Direct call | 1x (baseline) |
| Final/private method | Direct call | 1x |
| Virtual method (monomorphic) | vtable + inline cache | 1-2x |
| Virtual method (megamorphic) | vtable (cache miss) | 3-5x |
| Interface method | itable lookup | 2-4x |
| `invokevirtual` | vtable | ~1x |
| `invokeinterface` | itable (can be slower) | ~1-3x |

### 15.3 Memory Optimization Tips

- **Prefer primitives** over boxed types (`int` over `Integer`) when performance matters
- **Use records** for immutable value objects — they avoid boilerplate and are stack-allocated for small values
- **Reuse objects** with object pools for expensive-to-create objects (connections, buffers)
- **Use `String.intern()`** carefully for deduplicating frequently repeated strings
- **Favor composition** over deep inheritance chains — deep hierarchies increase vtable lookup depth

### 15.4 JIT Compiler Optimizations

- **Monomorphic inlining**: If a virtual call always targets one implementation, the JVM inlines it
- **Escape analysis**: If an object doesn't escape the method, the JVM may allocate it on the stack
- **Class hierarchy analysis**: The JVM can devirtualize calls when it knows the full class hierarchy

### 15.5 Benchmarks

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(2)
public class OopBenchmark {

    @Benchmark
    public int virtualCall(Blackhole bh) {
        Shape shape = new Circle("Red", 5.0);
        return (int) shape.area();
    }

    @Benchmark
    public int interfaceCall(Blackhole bh) {
        Drawable drawable = new Circle("Red", 5.0);
        drawable.draw();
        return 0;
    }
}
```

---

## 16. Best Practices

### 16.1 Class Design

1. **Keep classes small** — each class should have a single, well-defined responsibility
2. **Favor immutability** — make fields `final`, avoid setters, use records for value objects
3. **Program to an interface** — declare variables using interface types, not concrete classes
4. **Use composition over deep inheritance** — prefer "has-a" over "is-a" for code reuse
5. **Validate constructor parameters** — fail fast with clear error messages

### 16.2 Encapsulation

6. **Minimize field visibility** — `private` by default, widen only when necessary
7. **Return defensive copies** — return unmodifiable collections or new instances
8. **Avoid exposing mutable state** — never return mutable fields directly

### 16.3 Inheritance

9. **Respect the Liskov Substitution Principle** — subclasses must be substitutable for their parent
10. **Use `@Override` annotation** — catches signature mismatches at compile time
11. **Prefer `final` methods** when overriding would break invariants
12. **Prefer `final` classes** when inheritance is not designed for

### 16.4 Polymorphism

13. **Use `instanceof` sparingly** — prefer polymorphic dispatch or sealed class pattern matching
14. **Override `equals()` and `hashCode()` together** — always both or never
15. **Override `toString()`** — makes debugging dramatically easier

### 16.5 Design

16. **Apply SOLID principles** — especially Single Responsibility and Dependency Inversion
17. **Prefer constructor injection** — makes dependencies explicit and testable
18. **Use factory methods** over public constructors when construction logic is complex
19. **Design for change** — identify the axes of variation and isolate them

---

## 17. Common Mistakes

### 17.1 Forgetting to Override `hashCode()` with `equals()`

```java
// WRONG — breaks HashMap, HashSet contracts
public class Person {
    private String name;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person p = (Person) o;
        return age == p.age && Objects.equals(name, p.name);
    }
    // Missing hashCode()!
}

// CORRECT
@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

### 17.2 Exposing Mutable Internal State

```java
// WRONG — caller can modify internal list
public class Team {
    private final List<String> members = new ArrayList<>();

    public List<String> getMembers() {
        return members; // caller can add/remove!
    }
}

// CORRECT — return unmodifiable copy
public List<String> getMembers() {
    return List.copyOf(members);
}
```

### 17.3 Calling Overridable Methods from Constructor

```java
// WRONG — method called before subclass is initialized
public class Parent {
    public Parent() {
        printName(); // calls Child.printName() before Child fields are set
    }
}

public class Child extends Parent {
    private final String name;

    public Child(String name) {
        super(); // Parent() runs first, calls printName()
        this.name = name;
    }

    @Override
    public void printName() {
        System.out.println(name.toUpperCase()); // NPE!
    }
}
```

### 17.4 Using `==` Instead of `.equals()` for Objects

```java
String a = new String("hello");
String b = new String("hello");

a == b;      // false — different objects
a.equals(b); // true — same content
```

### 17.5 Mutating Objects Used as HashMap Keys

```java
Map<Person, String> map = new HashMap<>();
Person key = new Person("Alice", 30);
map.put(key, "engineer");

key.age = 31; // WRONG — changes hashCode, entry is now unreachable!
```

---

## 18. Pitfalls

### 18.1 The Fragile Base Class Problem

Changes in a superclass can break subclasses silently. If a superclass changes its internal implementation (e.g., adding a method that conflicts with a subclass method), subclasses may exhibit unexpected behavior without compile errors.

**Mitigation**: Use composition over inheritance, or design inheritance hierarchies carefully with documented contracts.

### 18.2 The Diamond Problem (with Default Methods)

```java
interface A {
    default void hello() {
        System.out.println("A");
    }
}

interface B {
    default void hello() {
        System.out.println("B");
    }
}

// Compilation error: class C inherits unrelated defaults
// class C implements A, B { }

// Must explicitly resolve
class C implements A, B {
    @Override
    public void hello() {
        A.super.hello(); // explicit choice
    }
}
```

### 18.3 Reference Type vs Object Type Mismatch

```java
Animal animal = new Dog();    // reference is Animal, object is Dog
animal.makeSound();           // calls Dog.makeSound() (virtual dispatch)

// BUT for static methods:
Animal.staticMethod();        // calls Animal's version
Dog.staticMethod();           // calls Dog's version
// Static methods use the reference type, not the object type
```

### 18.4 Covariant Return Types Pitfall

```java
class Animal {
    public Animal copy() { return new Animal(); }
}

class Dog extends Animal {
    @Override
    public Dog copy() { return new Dog(); } // covariant return — OK
}
```

But be careful: returning `null` in a covariant override can break client code expecting non-null.

### 18.5 Tight Coupling Through Inheritance

Deep inheritance hierarchies create tight coupling. A change in any level affects all descendants.

```
Object → AbstractList → AbstractSequentialList → LinkedList
```

**Mitigation**: Prefer composition. Implement interfaces to define contracts without coupling to implementation.

---

## 19. Debugging Tips

### 19.1 Use `toString()` Everywhere

Override `toString()` in every class. When debugging, the first thing you check is what an object contains.

```java
@Override
public String toString() {
    return "Person{name='%s', age=%d}".formatted(name, age);
}
```

### 19.2 Inspect Object Identity

```java
System.out.println(System.identityHashCode(obj)); // unique object ID
System.out.println(obj.getClass().getName());      // runtime class name
```

### 19.3 Debugging Polymorphism Issues

When a method seems to call the wrong implementation:
1. Check the **reference type** vs **object type**
2. Verify the method signature matches exactly (name + parameter types)
3. Confirm the `@Override` annotation is present
4. Check if the method is `static`, `final`, or `private` (these are not overridden)

### 19.4 Diagnosing Memory Issues

```java
// Check object graph
jmap -dump:format=b,file=heap.bin <pid>

// Use jshell for quick testing
jshell> var list = new ArrayList<Integer>();
jshell> list.add(42);
jshell> list.size()
$3 ==> 1
```

### 19.5 Common Runtime Exceptions in OOP

| Exception | Cause | Fix |
|-----------|-------|-----|
| `NullPointerException` | Calling method on null reference | Null checks, `Optional`, `Objects.requireNonNull` |
| `ClassCastException` | Invalid downcast | Use `instanceof` check first |
| `StackOverflowError` | Infinite recursion (e.g., `toString()` calling itself) | Ensure methods terminate |
| `UnsupportedOperationException` | Calling unsupported operation on unmodifiable collection | Check collection mutability |

### 19.6 Thread Safety in OOP

Shared mutable state between objects can cause race conditions:

```java
// NOT thread-safe
public class Counter {
    private int count = 0;
    public void increment() { count++; } // race condition
}

// Thread-safe
public class Counter {
    private final AtomicInteger count = new AtomicInteger(0);
    public void increment() { count.incrementAndGet(); }
}
```

---

## 20. Comparison Table

### 20.1 Access Modifiers

| Modifier | Class | Package | Subclass | World | Use Case |
|----------|:-----:|:-------:|:--------:|:-----:|----------|
| `private` | Yes | No | No | No | Internal implementation |
| default | Yes | Yes | No | No | Package-internal APIs |
| `protected` | Yes | Yes | Yes | No | Subclass extension points |
| `public` | Yes | Yes | Yes | Yes | Public API |

### 20.2 Abstract Class vs Interface

| Feature | Abstract Class | Interface |
|---------|:--------------:|:---------:|
| Multiple inheritance | No | Yes |
| Constructors | Yes | No |
| Instance fields | Yes | No (constants only) |
| Default methods | Yes | Yes (Java 8+) |
| Static methods | Yes | Yes |
| Access modifiers on methods | Any | `public` (default since Java 9) |
| State | Can have mutable state | Immutable only |
| When to use | Shared code + contract | Pure contract, multiple type |

### 20.3 Composition vs Inheritance

| Aspect | Composition | Inheritance |
|--------|:-----------:|:-----------:|
| Relationship | "has-a" | "is-a" |
| Coupling | Loose | Tight |
| Flexibility | High (swap implementations) | Low (fixed hierarchy) |
| Code reuse | Delegation | Direct |
| Testability | Easy to mock | Harder to test in isolation |
| Multiple behaviors | Yes (multiple fields) | No (single parent) |
| Preferred when | Behavior varies at runtime | True subtype relationship |

### 20.4 Records vs Classes

| Feature | Record | Class |
|---------|:------:|:-----:|
| Immutability | Automatic (final fields) | Manual |
| `equals()`, `hashCode()`, `toString()` | Auto-generated | Manual override |
| Extends other classes | No | Yes |
| Implements interfaces | Yes | Yes |
| Instance fields | Final only (in canonical constructor) | Any |
| Mutability | Immutable by design | Any |
| Use case | Data carriers, DTOs, value objects | Entities, services, any mutable object |

### 20.5 Method Overloading vs Overriding

| Aspect | Overloading | Overriding |
|--------|:-----------:|:----------:|
| Scope | Same class (or inherited) | Subclass |
| Signature | Different parameters | Same parameters |
| Return type | Can differ | Must be covariant |
| `@Override` | Optional (compiles differently) | Required (for safety) |
| Binding | Compile-time (static) | Runtime (dynamic) |
| `static` methods | Can overload | Cannot override |
| `final` methods | Can overload | Cannot override |

---

## 21. Decision Tree

### 21.1 Should I Use Abstract Class or Interface?

```
Do you need multiple inheritance of type?
├── YES → Interface
└── NO
    ├── Do you need to share state or constructors?
    │   ├── YES → Abstract class
    │   └── NO
    │       ├── Are you defining a contract only?
    │       │   ├── YES → Interface
    │       │   └── NO → Abstract class
    │       └── Will unrelated classes implement it?
    │           ├── YES → Interface
    │           └── NO → Either (prefer interface for flexibility)
```

### 21.2 Should I Use Composition or Inheritance?

```
Is there a true "is-a" relationship?
├── NO → Composition
└── YES
    ├── Do you need to override behavior in subclasses?
    │   ├── YES
    │   │   ├── Is the hierarchy shallow (≤ 2 levels)?
    │   │   │   ├── YES → Inheritance is OK
    │   │   │   └── NO → Composition + delegation
    │   │   └── Does the base class have mutable state?
    │   │       ├── YES → Composition (avoids fragile base class)
    │   │       └── NO → Inheritance OK
    │   └── NO → Composition
    └── Do you need polymorphism?
        ├── YES → Interface (can combine with composition)
        └── NO → Composition
```

### 21.3 When to Use Records?

```
Is this a simple data carrier?
├── YES
│   ├── Should it be immutable?
│   │   ├── YES → Record
│   │   └── NO → Class with manual fields
│   └── Does it need validation in constructor?
│       ├── YES → Record with compact constructor
│       └── NO → Record
└── NO → Class
```

---

## 22. Interview Questions

### Q1: What is the difference between an abstract class and an interface?

**Answer**: An abstract class can have constructors, instance fields, and both abstract and concrete methods. It supports single inheritance. An interface can only have constants (`public static final`), abstract methods, default methods, and static methods. It supports multiple inheritance of type. Use an abstract class when you need to share code among closely related classes; use an interface when you need to define a contract that unrelated classes can implement.

### Q2: Can we override a static method? Why?

**Answer**: No. Static methods belong to the class, not instances. They are resolved at compile time based on the reference type (method hiding), not at runtime via virtual dispatch. Overriding requires runtime polymorphism, which static methods do not participate in.

### Q3: Why must `hashCode()` be overridden when `equals()` is overridden?

**Answer**: The contract requires that objects which are `equals()` must have the same `hashCode()`. HashMap and HashSet use `hashCode()` to find the bucket, then `equals()` to find the exact entry. If two equal objects have different hash codes, they may end up in different buckets, breaking lookup, `contains()`, and `remove()` operations.

### Q4: Composition vs Inheritance — when to use which?

**Answer**: Use inheritance when there is a clear "is-a" relationship and the hierarchy is shallow. Use composition when you need flexibility, when the relationship is "has-a", when you want to avoid the fragile base class problem, or when you need to swap implementations at runtime. The general guideline is: "Favor composition over inheritance."

### Q5: What is Dependency Injection? What are its types?

**Answer**: DI is the practice of providing dependencies from outside rather than creating them internally. Types: **Constructor Injection** (preferred — makes dependencies explicit, enables immutability), **Setter Injection** (optional dependencies, reconfigurable), and **Field Injection** (avoid — hard to test, hides dependencies).

### Q6: Explain SOLID with examples.

**Answer**:
- **S**ingle Responsibility: A class has one reason to change. `OrderService` handles orders, `PaymentService` handles payments.
- **O**pen/Closed: Open for extension, closed for modification. Add new `PaymentProcessor` implementations without changing `OrderService`.
- **L**iskov Substitution: Subtypes must be substitutable. A `Square` extending `Rectangle` violates this if it changes width/height behavior.
- **I**nterface Segregation: Many specific interfaces > one general. `Readable`, `Writable` > `ReadWrite`.
- **D**ependency Inversion: Depend on abstractions. `OrderService` depends on `PaymentProcessor` interface, not `StripeProcessor`.

### Q7: What is method overloading vs overriding?

**Answer**: Overloading is having multiple methods with the same name but different parameter lists in the same class. It is resolved at compile time. Overriding is when a subclass provides a specific implementation of a superclass method with the same signature. It is resolved at runtime via virtual dispatch.

### Q8: Can a constructor be private? When?

**Answer**: Yes. Private constructors are used in: **Singleton pattern** (restrict instantiation), **Factory methods** (control object creation), **Utility classes** (prevent instantiation), and **Builder pattern** (force use of builder). Example: `Runtime.getRuntime()` uses a private constructor internally.

### Q9: What is method hiding vs overriding?

**Answer**: Method hiding occurs when a subclass defines a static method with the same signature as a static method in the superclass. The subclass method hides the superclass method. The method called depends on the reference type (compile-time), not the object type (runtime). This is different from overriding, where the object type determines which method runs.

### Q10: What is the difference between `==` and `.equals()`?

**Answer**: `==` compares references (whether two variables point to the same object in memory). `.equals()` compares content/logical equality (whether two objects represent the same value). For primitive types, `==` compares values. For objects, always use `.equals()` for content comparison.

---

## 23. Exercises

### Exercise 1: Class Design
Design a `Library` class that manages a collection of `Book` objects. Each `Book` should have title, author, ISBN, and availability status. Implement methods to borrow, return, and search books. Use proper encapsulation.

### Exercise 2: Inheritance Hierarchy
Create an inheritance hierarchy for a zoo: `Animal` (abstract) → `Mammal`, `Bird`, `Reptile`. Each subclass should override `speak()` and `move()`. Add at least two concrete animals per category.

### Exercise 3: Interface Implementation
Design a `PaymentSystem` with a `Payable` interface. Implement `CreditCardPayment`, `UpiPayment`, and `NetBankingPayment`. Each should handle its own validation and processing logic.

### Exercise 4: equals() and hashCode()
Create a `Money` class with `amount` (BigDecimal) and `currency` (String). Override `equals()`, `hashCode()`, and `toString()`. Verify that `Money` works correctly as a `HashMap` key.

### Exercise 5: Composition
Refactor a deep inheritance hierarchy (at least 4 levels) into a composition-based design. Identify the axes of variation and use interfaces and composition to achieve the same functionality.

---

## 24. Assignments

### Assignment 1: ATM System
Design an ATM system with OOP principles:
- Classes: `ATM`, `BankAccount`, `Card`, `Transaction`
- Use encapsulation to protect account balance
- Use inheritance for different account types (Savings, Current)
- Use polymorphism for different transaction types (Withdrawal, Deposit, Transfer)
- Apply SOLID principles

### Assignment 2: Library Management System
Build a library system with:
- `Member`, `Librarian`, `Admin` roles (inheritance from `User`)
- `Book`, `Magazine`, `DVD` as catalog items (inheritance from `CatalogItem`)
- `BorrowTransaction`, `Reservation` as transaction types
- Use composition: Library has Members, has Catalog, has Transactions
- Use interfaces: `Borrowable`, `Reservable`, `Searchable`

### Assignment 3: E-Commerce Order Processing
Implement an order processing pipeline:
- `Product` hierarchy with different pricing strategies
- `Order` with `OrderItem` (composition)
- `PaymentProcessor` interface with multiple implementations
- `ShippingStrategy` interface with different shipping options
- Use dependency injection throughout

---

## 25. Mini Project

### Project: Employee Management System

Build a complete Employee Management System demonstrating all OOP concepts.

**Requirements:**

```
Classes:
├── Employee (abstract)
│   ├── Manager
│   ├── Developer
│   └── Intern
├── Department (composition: has List<Employee>)
├── Company (composition: has List<Department>)
├── PaySlip (record)
├── Attendance (record)
└── interfaces:
    ├── Payable
    ├── Printable
    └── Serializable
```

**Features to implement:**
1. Employee hierarchy with proper inheritance and polymorphism
2. Department management with composition
3. Payroll calculation using polymorphism (different pay rules per role)
4. Attendance tracking using records
5. Search functionality using interface contracts
6. Report generation using `toString()` and `Printable` interface
7. Equality handling for employees using `equals()` and `hashCode()`

**SOLID Application:**
- Single Responsibility: Separate classes for payroll, attendance, search
- Open/Closed: Add new employee types without modifying existing code
- Liskov Substitution: All Employee subclasses work interchangeably
- Interface Segregation: Separate interfaces for different capabilities
- Dependency Inversion: High-level modules depend on abstractions

---

## 26. Summary

| Concept | Key Takeaway |
|---------|-------------|
| **Class** | Blueprint defining structure and behavior |
| **Object** | Runtime instance with state, behavior, and identity |
| **Encapsulation** | Private fields, public methods; controls access |
| **Inheritance** | `extends`; single inheritance, code reuse |
| **Polymorphism** | Overloading (compile-time) vs Overriding (runtime) |
| **Abstraction** | Abstract class (partial) vs Interface (pure contract) |
| **Composition** | "has-a"; flexible, loose coupling; preferred over deep inheritance |
| **Aggregation** | Weak "has-a"; parts can exist independently |
| **Dependency Injection** | Constructor injection preferred; testable, explicit |
| **SOLID** | Five principles for maintainable OOP design |
| **equals/hashCode** | Override together; use `Objects.hash()` and `Objects.equals()` |
| **Records** | Immutable data carriers (Java 16+); auto-generate boilerplate |
| **Sealed Classes** | Restrict which classes can extend (Java 17+) |
| **Memory** | Objects on heap, references on stack; GC manages lifecycle |
| **JVM** | vtable for virtual dispatch, itable for interface dispatch |

---

## 27. References

### Official Documentation
- [Java Language Specification — Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html)
- [Java Language Specification — Interfaces](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html)
- [Java Tutorials — Object-Oriented Programming Concepts](https://docs.oracle.com/en/java/javase/21/java/concepts/)
- [Effective Java, 3rd Edition — Joshua Bloch](https://www.oreilly.com/library/view/effective-java/9780134686097/)

### Design Principles
- [SOLID Principles Explained](https://java-design-patterns.com/principles/)
- [Composition Over Inheritance](https://java-design-patterns.com/recipes/composition-over-inheritance/)
- [Liskov Substitution Principle](https://en.wikipedia.org/wiki/Liskov_substitution_principle)

### JVM Internals
- [Oracle JVM Documentation](https://docs.oracle.com/en/java/javase/21/docs/technotes/guides/vm/)
- [OpenJDK Internals](https://openjdk.org/groups/hotspot/)
- [Java Memory Model (JEP 425)](https://openjdk.org/jeps/425)

### Books
- *Effective Java* — Joshua Bloch
- *Head First Design Patterns* — Eric Freeman, Elisabeth Robson
- *Clean Architecture* — Robert C. Martin
- *Design Patterns: Elements of Reusable Object-Oriented Software* — GoF
- *Java Concurrency in Practice* — Brian Goetz
