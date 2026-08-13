# Value Objects

## Learning Objectives
By the end of this topic, you will be able to:
- Understand what value objects are and their characteristics
- Implement immutable value objects
- Differentiate between value objects and entities
- Use records for value objects (Java 16+)

## Prerequisites
- Understanding of object identity
- Knowledge of immutability concepts
- Basic OOP principles

## Why This Concept Exists

### The Problem
Many objects represent data values rather than entities with identity. Using identity-based objects for values leads to confusion, bugs, and unnecessary complexity.

### The Solution
Value objects are immutable objects defined by their attributes rather than identity. They simplify code, are inherently thread-safe, and can be freely shared.

## Internal Working

### Characteristics of Value Objects
1. **Immutable**: State cannot change after creation
2. **Equality by Value**: Two objects are equal if their attributes are equal
3. **No Identity**: No unique identifier; defined by attributes
4. **Replaceable**: Can be replaced with an equal object
5. **Self-validating**: Validate state in constructor

### JVM Perspective
- Value objects are allocated on the heap like any other object
- Immutable value objects can be safely shared
- The JVM may optimize with escape analysis
- Records (Java 16+) are compact value objects

## Syntax

```java
// Traditional value object
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;
    
    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Money other)) return false;
        return amount.equals(other.amount) && 
               currency.equals(other.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}

// Record value object (Java 16+)
public record Money(BigDecimal amount, Currency currency) {}
```

## Easy Examples

### Example 1: Simple Value Object
```java
public final class Point {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int x() { return x; }
    public int y() { return y; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point other)) return false;
        return x == other.x && y == other.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return "Point[x=" + x + ", y=" + y + "]";
    }
}
```

## Medium Examples

### Example 1: Money Value Object
```java
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private final Currency currency;
    
    public Money(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
    }
    
    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(amount.add(other.amount), currency);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Money other)) return false;
        return amount.compareTo(other.amount) == 0 && 
               currency.equals(other.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
```

## Hard Examples

### Example 1: Value Object with Validation
```java
import java.util.Objects;

public final class Email {
    private final String value;
    
    public Email(String value) {
        Objects.requireNonNull(value, "Email cannot be null");
        if (!isValidEmail(value)) {
            throw new IllegalArgumentException("Invalid email: " + value);
        }
        this.value = value.toLowerCase();
    }
    
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    public String getValue() { return value; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Email other)) return false;
        return value.equals(other.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public String toString() {
        return value;
    }
}
```

## Best Practices
1. Make value objects `final` to prevent subclassing
2. Make all fields `final` for immutability
3. Validate state in constructor
4. Override `equals()` and `hashCode()` together
5. Consider using records for simple value objects
6. Use factory methods for complex construction

## Common Pitfalls
- Mutable value objects defeat the purpose
- Forgetting to override `equals()` and `hashCode()`
- Not validating state in constructor
- Exposing mutable internal state through getters

## Related Topics
- 25-immutable-objects
- 33-object-identity
- 36-records
