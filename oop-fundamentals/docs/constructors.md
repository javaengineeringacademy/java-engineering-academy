# Constructors

## Objective
Understand constructor patterns, chaining, and best practices for object initialization.

## Theory

### What is a Constructor?
A **constructor** is a special method that initializes a new object. It has the same name as the class and no return type.

### Types of Constructors

| Type | Syntax | Purpose |
|------|--------|---------|
| **No-arg** | `public ClassName() { }` | Default initialization |
| **Parameterized** | `public ClassName(Type param) { }` | Initialize with values |
| **Copy Constructor** | `public ClassName(ClassName other) { }` | Create copy of existing object |
| **Builder Pattern** | Static inner class | Complex objects with many optional fields |

## Constructor Rules
- Same name as class
- No return type (not even `void`)
- Can be overloaded
- If none defined → compiler provides default no-arg
- First statement can be `this()` or `super()`

## Constructor Chaining

```java
public class Person {
    private String name;
    private int age;
    private String address;

    // Primary constructor (most parameters)
    public Person(String name, int age, String address) {
        this.name = Objects.requireNonNull(name, "Name required");
        this.age = validateAge(age);
        this.address = address;
    }

    // Chaining to primary constructor
    public Person(String name, int age) {
        this(name, age, "Unknown");  // Calls primary with default
    }

    public Person(String name) {
        this(name, 0);  // Chains to two-arg, which chains to primary
    }

    private int validateAge(int age) {
        if (age < 0) throw new IllegalArgumentException("Age >= 0");
        return age;
    }
}
```

## Copy Constructor

```java
public class Person {
    private String name;
    private int age;

    // Copy constructor
    public Person(Person other) {
        this.name = other.name;
        this.age = other.age;
    }
}

// Usage
Person original = new Person("Alice", 30);
Person copy = new Person(original);  // Deep copy for immutable fields
```

## Copy Constructor vs Clone

| Aspect | Copy Constructor | Clone |
|--------|------------------|-------|
| Type Safety | Compile-time checked | Requires cast |
| Inheritance | Works with inheritance | Tricky with inheritance |
| Final Fields | Can copy final fields | Cannot set final fields |
| Exception Handling | Normal exceptions | Checked `CloneNotSupportedException` |

## Constructor Best Practices

| Practice | Reason |
|----------|--------|
| Validate parameters | Fail fast with clear messages |
| Use `final` for immutable fields | Ensures immutability |
| Chain to primary constructor | Single initialization logic |
| Use `Objects.requireNonNull()` | Null safety |
| Prefer factory methods for complex creation | Better naming, can return subtypes |

## Constructor Overloading vs Factory Methods

```java
// Overloading (limited)
public Person(String name, int age) { ... }
public Person(String name, int age, String address) { ... }

// Factory Methods (more flexible)
public static Person createAdult(String name, String email) {
    return new Person(name, 18, email);
}

public static Person createChild(String name, String email) {
    return new Person(name, 10, email);
}
```

## Common Mistakes

| Mistake | Correct Approach |
|---------|------------------|
| No validation in constructor | Validate early, fail fast |
| No `this()` chaining | DRY - single initialization path |
| Mutable fields without defense | Defensive copy for mutable objects |
| Overloaded constructors with same types | Use factory methods or Builder |

## Interview Questions

1. **Can a constructor be private?**
   - Yes, for Singleton or Factory pattern

2. **Difference between constructor and method?**
   - Constructor: same name as class, no return type, creates object
   - Method: has return type, performs action

3. **Can constructor throw exception?**
   - Yes, for validation failures

3. **Can we call constructor from method?**
   - No, only from another constructor via `this()` or `super()`

## Related Topics

← [Classes](classes.md) | → [Methods](methods.md)

## References

- [Java Language Specification - Constructors](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.8)
- [Effective Java Item 1: Consider Static Factory Methods](https://www.oracle.com/technical-resources/articles/java/effectivejava.html)