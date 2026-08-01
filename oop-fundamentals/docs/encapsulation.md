# Encapsulation

## Introduction

Encapsulation is one of the four fundamental pillars of Object-Oriented Programming (OOP). It is the mechanism of bundling data (fields) and the methods that operate on that data into a single unit—a class—while restricting direct access to some of the object's components. This principle ensures that the internal representation of an object is hidden from the outside view, and only accessible through a controlled interface.

Encapsulation is often confused with abstraction, but they serve different purposes. Abstraction deals with hiding complexity by showing only essential features, while encapsulation deals with hiding the internal state and requiring all interaction to occur through well-defined methods.

## Learning Objectives

By the end of this topic, you will be able to:

- Explain what encapsulation is and why it matters in OOP
- Identify the four access modifiers in Java and their visibility scopes
- Implement encapsulation using private fields and public getters/setters
- Apply validation logic in setter methods to protect object invariants
- Create immutable objects that cannot be modified after construction
- Use defensive copying to protect mutable internal state
- Recognize common encapsulation mistakes and avoid them
- Compare encapsulated designs with non-encapsulated alternatives

## Prerequisites

Before studying encapsulation, you should be familiar with:

- Basic Java syntax and class definitions
- Understanding of what fields and methods are
- Familiarity with the concept of object state
- Basic knowledge of data types (primitives and reference types)

## Why This Concept Exists

Without encapsulation, any code can directly modify an object's internal state. This leads to several problems:

1. **No validation**: Invalid states can be set (e.g., negative bank balance)
2. **Tight coupling**: Changing internal implementation breaks all dependent code
3. **No access control**: Sensitive data is exposed to untrusted code
4. **Difficult debugging**: State changes happen unpredictably across the codebase

Encapsulation solves these problems by providing a controlled interface to object state, ensuring that all modifications go through validated methods.

## Problem Statement

Consider a `BankAccount` class without encapsulation:

```java
public class BankAccount {
    public BigDecimal balance;  // Direct access!
}

// Anywhere in the code:
account.balance = new BigDecimal("-1000");  // Invalid state!
account.balance = null;                     // NullPointerException!
```

There is no way to prevent invalid state, enforce business rules, or track when the balance changes. How do we protect the object's invariants while still allowing controlled access?

## Theory

### What is Encapsulation?
**Encapsulation** is the bundling of data (fields) and methods that operate on that data into a single unit (class), with restricted access to internal state.

### Access Modifiers

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `private` | ✓ | ✗ | ✗ | ✗ |
| (package) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

### Getter and Setter Pattern

Getters and setters provide controlled access to private fields:

```java
public class Person {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        this.age = age;
    }
}
```

### Benefits

| Benefit | Description |
|---------|-------------|
| **Control** | Validate before setting |
| **Flexibility** | Change internals without breaking clients |
| **Security** | Protect invariants |
| **Maintainability** | Single point of change |

## Internal Working

When encapsulation is applied, the JVM loads the class and allocates memory for private fields. These fields are stored in the object's memory space on the heap, but they are not directly accessible from outside the class. When external code calls a getter or setter, the JVM executes the method within the class's context, allowing access to the private fields through the method's scope.

The access control check happens at compile time. The compiler verifies that the calling code has the appropriate access level. If a method tries to access a private field from outside its class, the compiler raises an error. This means encapsulation is enforced at compile time, not runtime—there is zero performance overhead.

## JVM Perspective

From the JVM's perspective, encapsulation is implemented through access control modifiers in the class file:

1. **Class Loading**: The JVM loads the `.class` file and stores field metadata in the method area
2. **Access Flags**: Each field and method has access flags indicating visibility (private, public, etc.)
3. **Runtime Checks**: The JVM verifies access permissions when fields/methods are accessed via `invokevirtual`, `getfield`, `putfield` instructions
4. **Compiler Enforcement**: Most access checks happen at compile time, so the JVM performs minimal runtime validation

The `private` modifier is purely a compile-time construct. The JVM does not truly prevent access—it relies on the class loader and bytecode verifier to enforce restrictions. This is why reflection can bypass private access at runtime.

## Memory Representation

```java
Person person = new Person("Alice", 30);
```

Heap memory layout:

```
Stack Frame          Heap Memory
┌──────────┐        ┌─────────────────┐
│ person   │───────►│ Person Object    │
│ ref      │        │ ┌─────────────┐ │
└──────────┘        │ │ name (ref)  │──────► "Alice"
                    │ │ age: 30     │ │
                    │ └─────────────┘ │
                    └─────────────────┘
```

- Private fields (`name`, `age`) are stored in the heap object
- External code holds only the reference (`person` on stack)
- Direct field access is blocked; only method calls are allowed
- The field values are protected by access modifiers at compile time

## Syntax

### Basic Class with Encapsulation

```java
public class ClassName {
    private Type fieldName;

    public Type getFieldName() {
        return fieldName;
    }

    public void setFieldName(Type value) {
        // validation logic
        this.fieldName = value;
    }
}
```

### Record Alternative (Java 16+)

Records provide encapsulation with less boilerplate:

```java
public record Point(int x, int y) {
    // Compact constructor for validation
    public Point {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates must be non-negative");
        }
    }
}
```

### Immutable Class

```java
public final class ImmutableClass {
    private final Type field;

    public ImmutableClass(Type field) {
        this.field = Objects.requireNonNull(field);
    }

    public Type getField() {
        return field;  // No setter, field is final
    }
}
```

## Easy Example

A simple `Rectangle` class with encapsulated dimensions:

```java
public class Rectangle {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        setWidth(width);
        setHeight(height);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive");
        }
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
        this.height = height;
    }

    public double getArea() {
        return width * height;
    }
}
```

## Medium Example

A `Temperature` class that encapsulates validation and conversion logic:

```java
public final class Temperature {
    private final double celsius;

    public Temperature(double celsius) {
        this.celsius = celsius;
    }

    public static Temperature fromCelsius(double celsius) {
        return new Temperature(celsius);
    }

    public static Temperature fromFahrenheit(double fahrenheit) {
        return new Temperature((fahrenheit - 32) * 5.0 / 9.0);
    }

    public double getCelsius() {
        return celsius;
    }

    public double getFahrenheit() {
        return celsius * 9.0 / 5.0 + 32;
    }

    public boolean isFreezing() {
        return celsius <= 0;
    }

    public boolean isBoiling() {
        return celsius >= 100;
    }
}
```

## Hard Example

A thread-safe `Cache` with encapsulated eviction logic:

```java
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<K, V> {
    private final Map<K, Entry<V>> store = new ConcurrentHashMap<>();
    private final long ttlMillis;

    public Cache(java.time.Duration ttl) {
        this.ttlMillis = ttl.toMillis();
    }

    public void put(K key, V value) {
        store.put(key, new Entry<>(value, Instant.now().toEpochMilli()));
    }

    public V get(K key) {
        Entry<V> entry = store.get(key);
        if (entry == null) {
            return null;
        }
        if (isExpired(entry)) {
            store.remove(key);
            return null;
        }
        return entry.value();
    }

    public void evict(K key) {
        store.remove(key);
    }

    public int size() {
        return store.size();
    }

    private boolean isExpired(Entry<V> entry) {
        return Instant.now().toEpochMilli() - entry.timestamp() > ttlMillis;
    }

    private record Entry<V>(V value, long timestamp) {}
}
```

## Enterprise Example

A `UserAccount` entity with encapsulation for a banking system:

```java
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public final class UserAccount {
    private final String accountId;
    private final Instant createdAt;
    private BigDecimal balance;
    private AccountStatus status;
    private String email;

    public enum AccountStatus {
        ACTIVE, SUSPENDED, CLOSED
    }

    public UserAccount(String accountId, BigDecimal initialBalance, String email) {
        this.accountId = Objects.requireNonNull(accountId);
        this.balance = Objects.requireNonNull(initialBalance);
        this.email = Objects.requireNonNull(email);
        this.status = AccountStatus.ACTIVE;
        this.createdAt = Instant.now();

        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void updateEmail(String newEmail) {
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Cannot update email for inactive account");
        }
        this.email = Objects.requireNonNull(newEmail);
    }

    public void deposit(BigDecimal amount) {
        requireActive();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        requireActive();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance = balance.subtract(amount);
    }

    public void suspend() {
        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot suspend a closed account");
        }
        status = AccountStatus.SUSPENDED;
    }

    public void close() {
        if (status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Account already closed");
        }
        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Cannot close account with balance");
        }
        status = AccountStatus.CLOSED;
    }

    private void requireActive() {
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return accountId.equals(that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "UserAccount{accountId='%s', status=%s}".formatted(accountId, status);
    }
}
```

## Performance

Encapsulation has virtually **zero runtime overhead** in Java:

- Access modifiers are enforced at compile time, not runtime
- Getters and setters are inlined by the JIT compiler
- No additional memory is consumed by access control
- The JVM's escape analysis can eliminate method call overhead entirely

**Key performance facts:**

| Aspect | Impact |
|--------|--------|
| Compile-time checks | Zero runtime cost |
| JIT inlining | Getters/setters become direct field access |
| Memory overhead | None (fields stored identically) |
| Method call overhead | Eliminated by JIT optimizer |

Encapsulation does not introduce any performance penalty. The JVM treats `private` fields the same as package-private or public fields at the bytecode level—the access control is enforced by the compiler and class loader, not by runtime checks.

## Best Practices

1. **Validate in setters**: Always check preconditions before modifying state
2. **Return defensive copies**: Never return mutable internal objects directly
3. **Prefer immutability**: Use `final` fields and no setters when possible
4. **Use records for data carriers**: Records provide encapsulation with less boilerplate (Java 16+)
5. **Keep fields private**: Never expose fields as public (except `static final` constants)
6. **Document invariants**: Clearly state what conditions must always be true
7. **Consider defensive copying in constructors**: Copy mutable parameters to prevent external mutation
8. **Use `Objects.requireNonNull()`**: Validate reference parameters immediately
9. **Separate validation logic**: Extract complex validation into private helper methods

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Public fields | Use private + getters/setters |
| Setters without validation | Validate in setters |
| Returning mutable internal state | Return defensive copies |
| Mutable objects as fields | Use immutable types or defensive copies |
| Exposing collection references | Return unmodifiable views |
| Forgetting `Objects.requireNonNull` | Always validate constructor parameters |

## Pitfalls

1. **Over-encapsulation**: Adding getters/setters for every field defeats the purpose—design the interface based on what callers actually need
2. **Anemic domain models**: Classes with only getters/setters and no business logic are not truly encapsulated
3. **Leaking `this`**: Passing `this` reference in constructors or methods can expose partially constructed objects
4. **Circular dependencies**: Two classes that expose themselves to each other break encapsulation boundaries
5. **Breaking encapsulation via serialization**: `Serializable` can access private fields via reflection—consider `readObject`/`writeObject` carefully
6. **Defensive copy overhead**: Creating copies of large objects for every getter call can be expensive—balance safety with performance

## Debugging Tips

1. **Use IDE inspectors**: Most IDEs can show you where fields are accessed from—verify all access goes through methods
2. **Add logging in setters**: Temporarily log setter calls to track unexpected state changes
3. **Breakpoint in setters**: Set breakpoints in setters to catch invalid state being set
4. **Use `jshell`**: Quickly test encapsulation behavior interactively
5. **Check bytecode**: Use `javap -p` to verify field access modifiers in compiled classes
6. **Static analysis tools**: Use SpotBugs, SonarQube, or similar tools to detect encapsulation violations

## Comparison Table

| Feature | Encapsulated | Non-Encapsulated |
|---------|-------------|------------------|
| Field access | Via methods | Direct |
| Validation | In setters/constructors | None |
| Internal changes | Safe (method boundary) | Breaks callers |
| Testing | Easier (mock methods) | Harder (mock fields) |
| Debugging | Track method calls | Track all field accesses |
| Thread safety | Easier to make safe | Very difficult |
| Code coupling | Low | High |

## Decision Tree

```
Should you encapsulate this field?

Is it mutable state? ──► YES ──► Make it private
       │
       NO
       │
Is it a constant? ──► YES ──► Make it public static final
       │
       NO
       │
Can it change? ──► YES ──► Make it private with getter/setter
       │
       NO
       │
Make it private final with getter only
```

## Interview Questions

1. **Why encapsulate?** Control, validation, flexibility
2. **Mutable vs Immutable?** Immutable = thread-safe, predictable
3. **When to use defensive copy?** Mutable fields returned or passed in
4. **What is the difference between encapsulation and abstraction?** Abstraction hides complexity; encapsulation hides state
5. **Can you access private fields via reflection?** Yes, but it breaks encapsulation and is discouraged
6. **What are records and how do they relate to encapsulation?** Records are immutable classes with auto-generated getters, constructors, and `equals`/`hashCode`
7. **How does encapsulation affect testability?** It improves testability by allowing mocking of methods and clear contracts
8. **When should you NOT use encapsulation?** For simple data carriers (records), constants, or performance-critical code where method call overhead matters (rare)

## Defensive Copying

```java
public class Person {
    private final Date birthDate;

    public Person(Date birthDate) {
        this.birthDate = new Date(birthDate.getTime());  // Defensive copy
    }

    public Date getBirthDate() {
        return new Date(birthDate.getTime());  // Return copy
    }
}
```

## Exercises

1. Create a `Money` class with encapsulated amount and currency. Include validation that prevents negative amounts and ensures currency consistency in arithmetic operations.
2. Refactor a class with public fields to use proper encapsulation. Add validation in setters and document the invariants.
3. Create an immutable `Coordinates` class with latitude and longitude. Include validation that latitude is between -90 and 90, and longitude between -180 and 180.
4. Implement a `Password` class that stores a hashed password and provides a `verify()` method. The raw password should never be retrievable.

## Assignments

1. **Bank Account System**: Create `BankAccount`, `SavingsAccount`, and `CheckingAccount` classes with full encapsulation. Include deposit, withdrawal, transfer, and interest calculation methods. All fields must be private with validated access.
2. **Configuration Manager**: Build a `Config` class that encapsulates application settings. Changes to configuration should be logged, and certain fields should be immutable after initialization.
3. **Inventory System**: Create an `InventoryItem` class that encapsulates stock levels. Include methods for adding, removing, and querying stock with proper validation (no negative stock, no removing more than available).

## Mini Project

**Encapsulated Library Management System**

Build a library management system demonstrating encapsulation principles:

- `Book`: Encapsulate title, author, ISBN, availability status. ISBN must be valid format.
- `Member`: Encapsulate name, membership ID, borrowed books list. Limit maximum borrowed books.
- `Library`: Encapsulate the collection of books and members. Provide methods for borrowing, returning, and searching.
- `Loan`: Encapsulate checkout date, due date, and return date. Calculate fines based on return date.

Requirements:
- All fields must be private
- Validate all inputs in constructors and setters
- Return defensive copies of collections
- Create immutable value objects for ISBN and Money
- Use records where appropriate

## Summary

Encapsulation is the OOP principle of bundling data with methods that operate on that data, while restricting direct access to the internal state. Key takeaways:

- **Access modifiers** (`private`, package-private, `protected`, `public`) control visibility
- **Getters and setters** provide controlled access to private fields
- **Validation** in setters and constructors protects object invariants
- **Immutable objects** with `final` fields provide the strongest encapsulation
- **Defensive copying** prevents external mutation of internal state
- **Records** (Java 16+) provide concise encapsulation for data carriers
- **Zero runtime overhead**: access control is enforced at compile time
- Encapsulation improves maintainability, testability, and security

## References

- Effective Java, 3rd Edition, Joshua Bloch - Item 15-17 (Minimize Mutability, Prefer Immutability)
- Head First Design Patterns, 2nd Edition - Encapsulation chapters
- Java Language Specification - Class Members (https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html)
- Oracle Java Tutorials - Controlling Access to Members (https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html)
- Clean Code, Robert Martin - Chapter 6: Objects and Data Structures
