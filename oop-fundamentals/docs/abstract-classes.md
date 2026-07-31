# Abstract Classes

## Definition
Cannot be instantiated. May contain abstract (no body) and concrete methods.

## Syntax
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

## Abstract Methods
- No body, ends with semicolon
- Must be implemented by concrete subclass
- Class with abstract methods must be abstract

## Concrete Methods
```java
public abstract class Shape {
    // ... abstract methods ...
    
    // Concrete - shared implementation
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    // Final - cannot override
    public final void display() {
        System.out.println("Color: " + color);
    }
}
```

## Abstract Class vs Interface

| Feature | Abstract Class | Interface |
|---------|----------------|-----------|
| Inheritance | Single | Multiple |
| Fields | Instance + constants | Constants only |
| Constructors | Yes | No |
| Methods | Abstract + concrete | Abstract + default/static |
| Use case | Shared code + contract | Pure contract |

## When to Use Abstract Class
- "Is-a" relationship with shared code
- Template Method pattern
- Need constructors/state
- Partial implementation

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

class CSVProcessor extends DataProcessor {
    @Override protected void readData() { /* read CSV */ }
    @Override protected void processData() { /* process CSV */ }
    @Override protected void writeData() { /* write CSV */ }
}
```

## Abstract Class vs Interface

| Feature | Abstract Class | Interface |
|---------|----------------|-----------|
| Multiple inheritance | No | Yes |
| Instance fields | Yes | No (constants only) |
| Constructors | Yes | No |
| Methods | Abstract + concrete | Abstract + default/static |
| Access modifiers | Any | public (methods) |

## When to Use Each

| Scenario | Choice |
|----------|--------|
| Shared code + contract | Abstract class |
| Pure contract, multiple impl | Interface |
| Need instance fields | Abstract class |
| Multiple implementations | Interface |
| Need constructors | Abstract class |
| Functional interface | Interface |

## Abstract Class with Constructors
```java
public abstract class Account {
    private final String accountNumber;
    protected BigDecimal balance;

    protected Account(String accountNumber, BigDecimal openingBalance) {
        this.accountNumber = Objects.requireNonNull(accountNumber);
        this.balance = Objects.requireNonNull(openingBalance);
    }
    
    public abstract void withdraw(BigDecimal amount);
}
```