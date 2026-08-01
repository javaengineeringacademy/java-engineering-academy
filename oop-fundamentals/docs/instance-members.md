# Instance Members

## Introduction

Instance members are variables and methods that belong to individual object instances. Each object has its own copy of instance variables and shares access to instance methods. Understanding instance members is crucial for proper object-oriented design and memory management.

## Learning Objectives

- Understand the difference between instance and static members
- Declare and use instance variables effectively
- Implement instance methods that operate on object state
- Use instance initializer blocks appropriately
- Recognize memory implications of instance members

## Prerequisites

- Basic Java syntax
- Understanding of classes and objects
- Familiarity with data types and access modifiers

## Why This Concept Exists

Instance members exist to:
- **Represent object state** unique to each instance
- **Enable encapsulation** of data within objects
- **Support polymorphism** through instance methods
- **Manage memory** efficiently by sharing method code

## Problem Statement

Without instance members:
- Objects would have no state
- Each object couldn't maintain its own data
- Polymorphism wouldn't work properly
- Memory would be wasted on duplicate method code

## Instance Variables (Fields)
Non-static fields that belong to each object instance.

```java
public class Person {
    // Instance variables
    private String name;
    private int age;
    private Address address;
}
```

## Instance Methods
Methods that operate on instance state.

```java
public class Person {
    private String name;

    public void greet() {  // Instance method
        System.out.println("Hello, I'm " + name);
    }
}
```

## Instance Initializer Block
Code that runs before constructor, for each instance.

```java
public class Person {
    private final String id;
    private String name;

    // Instance initializer block
    {
        this.id = UUID.randomUUID().toString();
        System.out.println("Initializing Person...");
    }

    public Person(String name) {
        this.name = name;
    }
}
```

## Internal Working

When an object is created:
1. **Memory allocated** on heap
2. **Instance variables** initialized to default values
3. **Instance initializers** executed in declaration order
4. **Constructor** executes
5. **Reference** returned to caller

```
new Person("Alice")
┌─────────────────────────────────────┐
│ Heap: Person Object                 │
├─────────────────────────────────────┤
│ name: null → "Alice"               │
│ age: 0                             │
│ id: generated in initializer       │
└─────────────────────────────────────┘
```

## JVM Perspective

- Instance variables stored in **heap memory** within the object
- Instance methods stored in **method area**, shared by all instances
- **Object header** contains class pointer and synchronization state
- **Reference** is a pointer to the heap location

## Memory Representation

```
Stack (Thread)          Heap (Shared)
┌─────────────┐         ┌─────────────────────┐
│ reference: obj │────────▶ Object: Person     │
└─────────────┘         │ - name: "Alice"     │
                        │ - age: 30           │
                        └─────────────────────┘
```

## Syntax

```java
public class ClassName {
    // Instance variable declaration
    [access-modifier] [final] type fieldName [= initialValue];

    // Instance method declaration
    [access-modifier] [final] returnType methodName([params]) {
        // method body
    }
}
```

## Instance vs Static

| Aspect | Instance | Static |
|--------|----------|--------|
| Memory | Per object | Single copy |
| Access | `obj.field` | `Class.field` |
| this reference | Available | Not available |
| Override | Yes | No (hidden) |

## Easy Example

```java
public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}

// Each instance has its own balance
BankAccount account1 = new BankAccount(1000);
BankAccount account2 = new BankAccount(500);
```

## Medium Example

```java
public class Student {
    private final String id;
    private String name;
    private List<Double> grades = new ArrayList<>();

    {
        this.id = UUID.randomUUID().toString().substring(0, 8);
    }

    public Student(String name) {
        this.name = name;
    }

    public void addGrade(double grade) {
        if (grade >= 0 && grade <= 100) {
            grades.add(grade);
        }
    }

    public double getAverage() {
        return grades.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    public boolean isPassing() {
        return getAverage() >= 60.0;
    }
}
```

## Hard Example

```java
public class Observable<T> {
    private T value;
    private final List<Consumer<T>> listeners = new CopyOnWriteArrayList<>();
    private final List<Consumer<T>> onceListeners = new ArrayList<>();

    public Observable(T initialValue) {
        this.value = initialValue;
    }

    public T get() {
        return value;
    }

    public void set(T newValue) {
        if (!Objects.equals(value, newValue)) {
            this.value = newValue;
            notifyListeners(newValue);
        }
    }

    public Observable<T> onChange(Consumer<T> listener) {
        listeners.add(listener);
        return this;
    }

    public Observable<T> onChangeOnce(Consumer<T> listener) {
        onceListeners.add(listener);
        return this;
    }

    private void notifyListeners(T value) {
        listeners.forEach(listener -> listener.accept(value));
        onceListeners.forEach(listener -> listener.accept(value));
        onceListeners.clear();
    }
}

// Usage
Observable<Integer> counter = new Observable<>(0);
counter.onChange(val -> System.out.println("Changed to: " + val));
counter.set(5); // Prints: Changed to: 5
```

## Enterprise Example

```java
public class UserSession {
    private final String sessionId;
    private final User user;
    private final Instant createdAt;
    private volatile Instant lastAccessedAt;
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    public UserSession(User user) {
        this.sessionId = UUID.randomUUID().toString();
        this.user = user;
        this.createdAt = Instant.now();
        this.lastAccessedAt = createdAt;
    }

    public <T> void setAttribute(String key, T value) {
        attributes.put(key, value);
        touch();
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        touch();
        return (T) attributes.get(key);
    }

    public boolean isExpired(Duration timeout) {
        return Instant.now().isAfter(lastAccessedAt.plus(timeout));
    }

    private void touch() {
        lastAccessedAt = Instant.now();
    }
}
```

## Performance

- **Object size**: Each instance variable adds to object size
- **Cache locality**: Related fields should be declared together
- **Memory alignment**: JVM aligns objects to 8-byte boundaries
- **Avoid excessive fields**: Reduces memory footprint

```java
// Bad: scattered related fields
class Bad {
    String name;
    int age;
    String email;  // Related to name
    int salary;    // Related to age
}

// Better: grouped related fields
class Better {
    String name;
    String email;
    int age;
    int salary;
}
```

## Best Practices

- Declare instance variables as `private` with getters/setters
- Initialize variables at declaration or in constructor
- Use `final` for immutable state
- Prefer constructor injection over setter injection
- Group related instance variables together
- Document invariants in class javadoc

## Common Mistakes

1. **Uninitialized final variables** - must be set in constructor or initializer
2. **Shadowing instance variables** with local variables
3. **Not initializing collections** - leads to NullPointerException
4. **Circular references** causing memory leaks

## Pitfalls

- **Memory leaks** from uncleaned resources
- **Thread safety** issues with shared mutable state
- **Deep copy vs shallow copy** confusion
- **Serialization** complications with transient fields

## Debugging Tips

1. Use IDE debugger to inspect instance variables
2. Override `toString()` for better debugging output
3. Use `jshell` to quickly test instance behavior
4. Monitor heap dumps for memory issues
5. Add logging for state changes

## Decision Tree

```
Need to store data?
├── Shared across all instances?
│   ├── Yes → Static field
│   └── No → Instance field
├── Immutable after construction?
│   ├── Yes → Final instance field
│   └── No → Mutable instance field
└── Thread-safe access needed?
    ├── Yes → Synchronized or volatile
    └── No → Regular instance field
```

## Interview Questions

1. **What is the difference between instance and static members?**
   - Instance: per object; Static: per class

2. **When are instance variables initialized?**
   - Default values → instance initializers → constructor

3. **Can instance methods access static variables?**
   - Yes, but not recommended (use class name)

4. **What is the `this` keyword?**
   - Reference to current object instance

5. **How do you prevent modification of instance state?**
   - Use `final` keyword and defensive copies

## Exercises

1. Create a `Temperature` class with instance variables for Celsius and Fahrenheit, with conversion methods
2. Implement a `Counter` class that tracks its creation count using a static variable
3. Build a `StudentGradeBook` that manages multiple students and their grades

## Assignments

1. Design a `ThreadSafeCache<K,V>` with proper synchronization
2. Implement a `Builder` pattern for a complex object with many instance variables
3. Create a `DeepCopy` utility that handles circular references

## Mini Project

**Entity Base Class**

Create a base class for JPA entities:

```java
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Implement equals and hashCode based on id
}
```

## Summary

- Instance variables belong to individual objects, not the class
- Instance methods operate on instance state using `this`
- Instance initializers run before constructors
- Each object has its own copy of instance variables
- Proper encapsulation protects instance state

## References

- [Java Language Specification - Instance Members](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.3)
- [Java Language Specification - Fields](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.3)
- [Java Language Specification - Method Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4)
- [Java Language Specification - Instance Initializers](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.6)
- [Effective Java - Item 19: Design and document for inheritance](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Performance: The Definitive Guide](https://www.oreilly.com/library/view/java-performance-the/9781492056034/)
