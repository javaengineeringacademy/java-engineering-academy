# When to Use Methods

## Decision Guide

### Method Design Principles

| Principle | Description | Example |
|-----------|-------------|---------|
| Single Responsibility | One method, one job | `calculateTax()` not `calculateAndValidate()` |
| Descriptive Naming | Name describes what it does | `findUserById()` not `process()` |
| Minimal Parameters | 3 or fewer parameters | Use object for 4+ parameters |
| Pure Functions | No side effects when possible | `int add(int a, int b)` |
| Appropriate Scope | Private by default | Only expose what's needed |

### When to Create a Method

| Scenario | Create Method? | Why |
|----------|---------------|-----|
| Code repeated 2+ times | Yes | DRY principle |
| Method exceeds 20 lines | Yes | Readability |
| Logic needs testing | Yes | Testability |
| Multiple responsibilities | Yes | Separation of concerns |
| Used in multiple places | Yes | Reusability |

### Parameter Types

| Type | Use When | Example |
|------|----------|---------|
| Primitive | Simple values, immutable | `int count`, `boolean active` |
| Reference | Complex objects | `String name`, `List<Item> items` |
| Varargs | Variable number of same type | `String... values` |
| Default | Optional parameters (overloading) | `log(msg)` vs `log(msg, level)` |

### Static vs Instance Methods

| Use Static When | Use Instance When |
|-----------------|-------------------|
| No object state needed | Accesses/modifies instance fields |
| Utility function | Operates on specific object |
| Factory method | Part of object's behavior |
| Test helper | Requires polymorphism |

### Method Overloading Guidelines

| Good Overloading | Bad Overloading |
|------------------|-----------------|
| Same concept, different types | Different behavior entirely |
| `print(int)`, `print(String)` | `save(File)`, `save(Db)` |
| Varargs version | Ambiguous parameter types |
| Default parameter via overloading | More than 3 overloads |

## Production Guidelines

### Parameter Validation
```java
public void processOrder(String orderId, double amount) {
    // Validate at method boundary
    Objects.requireNonNull(orderId, "orderId cannot be null");
    if (amount <= 0) throw new IllegalArgumentException("amount must be positive");

    // Business logic here
}
```

### Builder Pattern for Many Parameters
```java
// AVOID: Too many parameters
public User create(String name, String email, int age, String phone, String address, boolean active) { ... }

// PREFER: Builder pattern
User user = User.builder()
    .name("John")
    .email("john@example.com")
    .age(30)
    .build();
```

### Optional for Nullable Returns
```java
// AVOID: Null return
public User findUser(String id) {
    return db.query(id); // Returns null if not found
}

// PREFER: Optional
public Optional<User> findUser(String id) {
    return Optional.ofNullable(db.query(id));
}
```
