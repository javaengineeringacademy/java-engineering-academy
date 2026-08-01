# Classes and Objects

## Objective
Understand the fundamental building blocks of OOP: classes as blueprints and objects as instances.

## Theory

### What is a Class?
A **class** is a blueprint or template that defines the structure and behavior of objects. It encapsulates data (fields) and operations (methods) into a single unit.

### What is an Object?
An **object** is an instance of a class. It has:
- **State**: Values of its fields (instance variables)
- **Behavior**: What it can do (methods)
- **Identity**: Unique reference in memory

### Real-World Analogy
- **Class** = Architectural blueprint for a house
- **Object** = Actual house built from the blueprint
- You can build many houses (objects) from one blueprint (class)

## Class Structure

```java
public class ClassName {
    // Fields (state)
    private Type fieldName;
    
    // Constructors (initialization)
    public ClassName(parameters) { ... }
    
    // Methods (behavior)
    public ReturnType methodName(parameters) { ... }
    
    // Getters/Setters (encapsulation)
    public Type getFieldName() { ... }
    public void setFieldName(Type value) { ... }
}
```

## Object Creation & Memory

```
Stack                          Heap
─────────────────              ─────────────────
reference: obj ──────────────▶ Object: ClassName
                                  - field1: value
                                  - field2: value
```

## Example: Person Class

```java
public final class Person {
    private final String name;
    private int age;
    private final String email;

    public Person(String name, int age, String email) {
        this.name = Objects.requireNonNull(name, "Name required");
        this.age = validateAge(age);
        this.email = Objects.requireNonNull(email, "Email required");
    }

    private int validateAge(int age) {
        if (age < 0) throw new IllegalArgumentException("Age must be >= 0");
        return age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }

    public void setAge(int age) {
        this.age = validateAge(age);
    }

    @Override
    public String toString() {
        return "Person{name='%s', age=%d, email='%s'}".formatted(name, age, email);
    }
}
```

## Execution Flow (Stack vs Heap)

```java
Person person = new Person("Alice", 30, "alice@example.com");
```

**Stack**: `person` reference variable
**Heap**: `Person` object with fields `name="Alice"`, `age=30`, `email="alice@example.com"`

## Execution Flow Step-by-Step

1. **Class Loading**: JVM loads `Person.class`
2. **Memory Allocation**: Heap space for `Person` object
3. **Constructor Execution**: Fields initialized
3. **Reference Assignment**: Stack variable `person` points to heap object

## Introduction

Classes and objects are the foundational building blocks of Object-Oriented Programming in Java. A class defines the structure and behavior that objects will have, while objects are the runtime instances that hold actual data and execute operations. Understanding this relationship is essential before exploring inheritance, polymorphism, and encapsulation.

## Learning Objectives

- Define what a class is and identify its components (fields, constructors, methods)
- Create objects from class blueprints using the `new` keyword
- Explain the difference between a class and an object
- Understand how objects are allocated on the heap and referenced from the stack
- Apply encapsulation principles using private fields and public accessors
- Use the `final` keyword to create immutable objects

## Prerequisites

- Basic Java syntax (variables, data types, operators)
- Understanding of control flow statements (if/else, loops)
- Familiarity with compiling and running Java programs
- Basic understanding of memory concepts (stack vs heap)

## Why This Concept Exists

Before OOP, programs were written as collections of functions operating on global data. This led to scattered logic, duplicated code, and difficulty maintaining large systems. Classes solve this by bundling related data and behavior into a single reusable unit. Objects provide runtime flexibility, allowing programs to model real-world entities with distinct states and behaviors.

## Problem Statement

Without classes and objects, managing state becomes chaotic:
- Functions must pass state manually through parameters
- Related data has no enforced grouping
- Code reuse is limited to copy-paste
- Debugging is harder with no clear ownership of data

## Theory

### What is a Class?
A **class** is a blueprint or template that defines the structure and behavior of objects. It encapsulates data (fields) and operations (methods) into a single unit.

### What is an Object?
An **object** is an instance of a class. It has:
- **State**: Values of its fields (instance variables)
- **Behavior**: What it can do (methods)
- **Identity**: Unique reference in memory

### Real-World Analogy
- **Class** = Architectural blueprint for a house
- **Object** = Actual house built from the blueprint
- You can build many houses (objects) from one blueprint (class)

## Internal Working

When `new Person("Alice", 30, "alice@example.com")` is called:

1. JVM allocates memory on the heap for the Person object
2. The constructor is invoked with the provided arguments
3. Fields are initialized with the constructor parameter values
4. A reference to the heap object is returned and stored in a stack variable
5. The object is now accessible through that reference

The class itself (`Person.class`) is loaded once into the Metaspace and shared across all instances.

## JVM Perspective

- **Class Loading**: The `Person.class` bytecode is loaded into Metaspace (Java 8+) or PermGen (Java 7 and earlier)
- **Object Header**: Each heap object has a 12-16 byte header containing the class pointer and lock state
- **Field Layout**: Instance fields are laid out in memory according to their types (padding for alignment)
- **Reference**: The variable `person` on the stack holds a 4-byte (compressed oops) or 8-byte pointer to the heap object

## Memory Representation

```
Stack                          Heap                          Metaspace
─────────────────              ─────────────────             ─────────────────
person: 0x7f3a ──────────────▶ Object Header                Person.class
                                - name: 0x7f4b (ref) ─────▶ "Alice"
                                - age: 30                   (bytecode)
                                - email: 0x7f5c (ref) ─────▶ "alice@example.com"
```

## Syntax

```java
// Class definition
public class ClassName {
    // Fields (instance variables)
    private Type fieldName;
    
    // Constructor
    public ClassName(Type param) {
        this.fieldName = param;
    }
    
    // Methods
    public ReturnType methodName() {
        // body
    }
    
    // Getter
    public Type getFieldName() {
        return fieldName;
    }
    
    // Setter
    public void setFieldName(Type value) {
        this.fieldName = value;
    }
}

// Object creation
ClassName obj = new ClassName(value);
```

## Easy Example

```java
public class Car {
    private String brand;
    private int speed;

    public Car(String brand) {
        this.brand = brand;
        this.speed = 0;
    }

    public void accelerate(int amount) {
        speed += amount;
    }

    public String getBrand() { return brand; }
    public int getSpeed() { return speed; }
}

// Usage
Car myCar = new Car("Toyota");
myCar.accelerate(60);
System.out.println(myCar.getBrand() + " at " + myCar.getSpeed() + " km/h");
// Output: Toyota at 60 km/h
```

## Medium Example

```java
public final class BankAccount {
    private final String accountId;
    private final String owner;
    private double balance;
    private final List<String> transactionHistory;

    public BankAccount(String accountId, String owner, double initialBalance) {
        this.accountId = Objects.requireNonNull(accountId);
        this.owner = Objects.requireNonNull(owner);
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        transactionHistory.add("Account created with balance: " + initialBalance);
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be positive");
        balance += amount;
        transactionHistory.add("Deposited: " + amount);
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Withdrawal must be positive");
        if (amount > balance) throw new IllegalStateException("Insufficient funds");
        balance -= amount;
        transactionHistory.add("Withdrew: " + amount);
    }

    public String getAccountId() { return accountId; }
    public String getOwner() { return owner; }
    public double getBalance() { return balance; }
    public List<String> getTransactionHistory() {
        return List.copyOf(transactionHistory); // Defensive copy
    }
}

// Usage
BankAccount account = new BankAccount("ACC-001", "Alice", 1000);
account.deposit(500);
account.withdraw(200);
System.out.println("Balance: " + account.getBalance()); // 1300.0
```

## Hard Example

```java
public final class ImmutablePoint {
    private final double x;
    private final double y;
    private final Optional<String> label;

    public ImmutablePoint(double x, double y) {
        this(x, y, Optional.empty());
    }

    public ImmutablePoint(double x, double y, String label) {
        this(x, y, Optional.ofNullable(label));
    }

    private ImmutablePoint(double x, double y, Optional<String> label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }

    public ImmutablePoint translate(double dx, double dy) {
        return new ImmutablePoint(x + dx, y + dy, label);
    }

    public double distanceTo(ImmutablePoint other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public Optional<String> getLabel() { return label; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImmutablePoint other)) return false;
        return Double.compare(x, other.x) == 0 && Double.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return label.map(l -> l + ": ").orElse("") + "(%f, %f)".formatted(x, y);
    }
}

// Usage
ImmutablePoint origin = new ImmutablePoint(0, 0, "Origin");
ImmutablePoint moved = origin.translate(3, 4);
System.out.println(origin.distanceTo(moved)); // 5.0
```

## Enterprise Example

```java
public final class Order {
    public enum Status { PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED }

    private final String orderId;
    private final String customerId;
    private final List<OrderItem> items;
    private final LocalDateTime createdAt;
    private Status status;
    private BigDecimal totalAmount;

    public Order(String orderId, String customerId) {
        this.orderId = Objects.requireNonNull(orderId);
        this.customerId = Objects.requireNonNull(customerId);
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.status = Status.PENDING;
        this.totalAmount = BigDecimal.ZERO;
    }

    public void addItem(String productId, int quantity, BigDecimal unitPrice) {
        if (status != Status.PENDING) throw new IllegalStateException("Cannot modify confirmed order");
        items.add(new OrderItem(productId, quantity, unitPrice));
        recalculateTotal();
    }

    public void confirm() {
        if (status != Status.PENDING) throw new IllegalStateException("Order not in pending state");
        if (items.isEmpty()) throw new IllegalStateException("Cannot confirm empty order");
        this.status = Status.CONFIRMED;
    }

    private void recalculateTotal() {
        totalAmount = items.stream()
            .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return List.copyOf(items); }
    public Status getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}

public record OrderItem(String productId, int quantity, BigDecimal unitPrice) {
    public OrderItem {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        Objects.requireNonNull(productId);
        Objects.requireNonNull(unitPrice);
    }
}

// Usage
Order order = new Order("ORD-2024-001", "CUST-42");
order.addItem("PROD-A", 2, new BigDecimal("29.99"));
order.addItem("PROD-B", 1, new BigDecimal("49.99"));
order.confirm();
System.out.println("Order " + order.getOrderId() + " total: " + order.getTotalAmount());
```

## Performance

- **Object Creation Cost**: Creating objects on the heap has overhead (~16 bytes header + field storage + alignment padding)
- **Reference Overhead**: Each reference adds 4-8 bytes depending on JVM configuration (compressed oops)
- **GC Impact**: More objects mean more garbage collection activity
- **Optimization**: Use primitive types over wrappers when possible (e.g., `int` over `Integer`)
- **Object Pooling**: For expensive objects (database connections), reuse via pooling instead of repeated creation
- **Record Types**: Java 16+ records are more memory-efficient for simple data carriers

## Pitfalls

- **NullPointerException**: Dereferencing an uninitialized or null reference
- **Memory Leaks**: Holding references to objects no longer needed
- **Premature Optimization**: Using primitive arrays instead of objects without measuring
- **Mutable Shared State**: Multiple references to the same mutable object causing unintended side effects
- **Forgetting toString()**: Hard to debug without meaningful string representation
- **Ignoring equals/hashCode**: Breaking collections behavior when objects are used as keys

## Debugging Tips

- Use `System.identityHashCode(obj)` to check if two references point to the same object
- In IDE debuggers, use "Evaluate Expression" to inspect object state at breakpoints
- Add `toString()` to all classes for meaningful logging
- Use `jmap -histo:live <pid>` to see object counts on the heap
- Use VisualVM or JProfiler to track object creation and garbage collection
- Check for unintended object retention by examining reference chains

## Comparison Table

| Feature | Class | Object |
|---------|-------|--------|
| What it is | Blueprint/template | Instance of a class |
| Memory | Loaded once in Metaspace | Multiple instances on heap |
| Creation | `class` keyword | `new` keyword |
| Data | Defines fields (no values) | Holds actual field values |
| Purpose | Defines structure and behavior | Performs operations with real data |
| Lifetime | Exists for program duration | Created and destroyed dynamically |

## Decision Tree

```
Do you need to model an entity with state and behavior?
├── YES → Create a class
│   ├── Is it a simple data carrier with no logic?
│   │   ├── YES → Use a record (Java 16+)
│   │   └── NO → Use a class with methods
│   ├── Should it be immutable?
│   │   ├── YES → Make fields final, no setters
│   │   └── NO → Add setters with validation
│   └── Will it be inherited?
│       ├── YES → Leave class non-final
│       └── NO → Make class final
└── NO → Use a utility class with static methods
```

## Interview Questions

1. **Difference between class and object?**
   - Class = blueprint, Object = instance

2. **What happens when you create an object?**
   - Memory allocated on heap, constructor runs, reference returned

3. **Why use `final` class?**
   - Prevents inheritance (e.g., `String`, `Integer`)

4. **Can a class exist without objects?**
   - Yes, utility classes with only static methods

5. **What is the default value of instance fields?**
   - `0` for numeric, `false` for boolean, `null` for references

## Exercises

1. Create a `Student` class with fields: name, studentId, gpa. Add methods to check if the student is on the honor roll (GPA >= 3.5).
2. Create an immutable `Color` class with red, green, blue components (0-255). Add a method to lighten the color by a percentage.
3. Create a `Playlist` class that manages a list of song titles. Methods: addSong, removeSong, getSongCount, shuffle.

## Assignments

1. **Library System**: Create `Book`, `Member`, and `Library` classes. Book tracks title, author, and availability. Member can borrow/return books. Library manages the collection.
2. **Shape Hierarchy**: Create a `Shape` base class with `Circle` and `Rectangle` subclasses. Each calculates area and perimeter. Include a test class that creates an array of shapes and prints their properties.

## Mini Project

Build a `TaskManager` application:
- Create a `Task` class with fields: id, title, description, priority (LOW/MEDIUM/HIGH), completed status, createdAt timestamp
- Implement methods: complete(), getAge(), isOverdue(int maxDays)
- Create a `TaskManager` class that manages a collection of tasks
- Support operations: addTask, removeTask, getTasksByPriority, getCompletedTasks, getOverdueTasks
- Use proper encapsulation with defensive copying

## Summary

- A **class** is a blueprint defining structure (fields) and behavior (methods)
- An **object** is a runtime instance created with `new`, allocated on the heap
- References to objects live on the stack; the objects themselves live on the heap
- Use `final` for immutable fields and classes
- Always override `toString()`, `equals()`, and `hashCode()` when objects will be compared or logged
- Apply encapsulation: private fields with controlled public access

## Related Topics

- [Constructors](constructors.md) →
- [Methods](methods.md) →
- [Encapsulation](encapsulation.md) →
- [Memory Management](../theory.md#object-creation--memory)

## References

- [Java Language Specification - Classes](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html)
- [Effective Java Item 17: Minimize Mutability](https://www.oracle.com/technical-resources/articles/java/effective-java.html)
- [Oracle Docs - Classes and Objects](https://docs.oracle.com/javase/tutorial/java/javaOO/index.html)
- [Effective Java Item 10: Obey the general contract when overriding equals](https://www.oracle.com/java/technologies/javase/effective-java.html)