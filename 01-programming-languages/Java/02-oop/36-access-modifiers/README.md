# Access Modifiers in Java

## Overview

Access modifiers control the visibility and accessibility of classes, methods, fields, and constructors. They are fundamental to encapsulation and are the first line of defense in protecting your code.

## The Four Access Levels

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `public` | ✓ | ✓ | ✓ | ✓ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `default` (no modifier) | ✓ | ✓ | ✗ | ✗ |
| `private` | ✓ | ✗ | ✗ | ✗ |

## When to Use Each

### `private` - Default for fields
- Use for all instance fields
- Use for helper methods that shouldn't be exposed
- Enforces encapsulation

```java
public class BankAccount {
    private BigDecimal balance;
    private String ownerName;

    public BigDecimal getBalance() {
        return balance;
    }
}
```

### `default` (package-private) - When classes work together
- Use for helper classes in the same package
- Use for methods that should only be called within the package
- Good for tightly coupled package internals

```java
class DatabaseHelper {  // No modifier = package-private
    Connection connect() {  // Also package-private
        // ...
    }
}
```

### `protected` - For subclasses
- Use for methods subclasses need to override
- Use for fields subclasses need to access
- Available in subclasses even in different packages

```java
public abstract class Shape {
    protected Color color;

    protected Shape(Color color) {
        this.color = color;
    }

    protected abstract void draw();
}

public class Circle extends Shape {
    @Override
    protected void draw() {
        System.out.println("Drawing circle with color: " + color);
    }
}
```

### `public` - For the API
- Use for methods that form the public API
- Use for constructors that create objects
- Use for constants (`public static final`)

```java
public class UserService {
    public User findById(int id) {  // Public API
        return repository.find(id);
    }
}
```

## Decision Framework

```
Is this field or method part of the public API?
├── Yes → public
├── No → Is it needed by subclasses?
│   ├── Yes → protected
│   └── No → Is it needed within the package?
│       ├── Yes → default (no modifier)
│       └── No → private
```

## Common Patterns

### Pattern 1: Private fields with public getters
```java
public class Person {
    private String name;
    private int age;

    public String getName() { return name; }
    public int getAge() { return age; }
}
```

### Pattern 2: Package-private for test access
```java
public class OrderService {
    private OrderRepository repository;

    // Package-private for testing
    void setRepository(OrderRepository repo) {
        this.repository = repo;
    }
}
```

### Pattern 3: Protected for extension points
```java
public abstract class AbstractExporter {
    protected abstract byte[] formatData(Data data);

    public void export(Data data) {
        byte[] formatted = formatData(data);
        save(formatted);
    }
}
```

## Misconceptions

1. **"Everything should be private"** - Overly restrictive code is hard to test and extend
2. **"Protected is safe"** - It exposes implementation details to subclasses
3. **"Package-private is useless"** - It's excellent for tightly coupled packages
4. **"Access modifiers affect runtime behavior"** - They're compile-time only

## Interview Questions

1. What is the difference between `private` and `protected`?
2. When would you use package-private (default) access?
3. Can you reduce visibility when overriding a method?
4. What is the Liskov Substitution Principle and how does it relate to access modifiers?
5. How do access modifiers relate to encapsulation?

## Cross-References

- See `08-encapsulation/` for encapsulation patterns
- See `09-inheritance/` for how access modifiers work with inheritance
- See `23-immutable-objects/` for making objects immutable
- See `13-reflection-annotations/` for how reflection can bypass access modifiers
