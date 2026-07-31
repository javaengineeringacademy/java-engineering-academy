# Abstraction

## Objective
Understand abstraction as hiding complex implementation details while exposing essential features.

## Abstract Class

### Definition
Cannot be instantiated. May contain abstract (no body) and concrete methods.

```java
public abstract class Shape {
    protected String color;

    public Shape(String color) {
        this.color = Objects.requireNonNull(color);
    }

    // Abstract - must implement
    public abstract double area();
    public abstract double perimeter();

    // Concrete - shared implementation
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
```

### When to Use
- "Is-a" relationship with shared code
- Template Method pattern
- Need constructors/state

## Interface

### Modern Interface (Java 8+)
```java
public interface Payable {
    // Abstract
    void pay(BigDecimal amount);

    // Default - optional override
    default void printReceipt() {
        System.out.println("Receipt printed");
    }

    // Static utility
    static BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.18));
    }
}
```

### Interface Evolution
| Version | Feature |
|---------|---------|
| Pre-Java 8 | Only abstract methods, constants |
| Java 8 | Default, static methods |
| Java 9 | Private methods |
| Java 11 | Private static methods |

## Abstract Class vs Interface

| Feature | Abstract Class | Interface |
|---------|----------------|-----------|
| Inheritance | Single | Multiple |
| Fields | Instance + constants | Constants only |
| Constructors | Yes | No |
| Methods | Abstract + concrete | Abstract + default/static |
| Use case | Shared code + contract | Pure contract |

## When to Use Which

| Scenario | Choice |
|----------|--------|
| Shared code + contract | Abstract class |
| Pure contract, multiple impl | Interface |
| Need instance fields | Abstract class |
| Multiple implementations | Interface |
| Need constructors | Abstract class |

## Abstract Methods vs Default Methods

| Aspect | Abstract | Default |
|--------|----------|---------|
| Implementation | None | Provided |
| Override | Required | Optional |
| Use case | Contract | Optional behavior |

## Template Method Pattern

```java
public abstract class DataProcessor {
    // Template method - final
    public final void process() {
        readData();
        processData();
        writeData();
    }

    protected abstract void readData();
    protected abstract void processData();
    protected abstract void writeData();
}
```

## When to Use Abstraction
- Multiple implementations exist
- Common behavior with variations
- Enforce contract
- Reduce coupling