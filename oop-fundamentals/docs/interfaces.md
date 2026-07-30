# Interfaces

## What is an Interface?
Contract specifying what a class can do, without saying how. Pure abstraction (pre-Java 8).

## Modern Interfaces (Java 8+)
```java
public interface Payable {
    void pay(BigDecimal amount);  // Abstract

    default void printReceipt() {  // Default implementation
        System.out.println("Receipt printed");
    }

    static BigDecimal calculateTax(BigDecimal amount) {  // Static
        return amount.multiply(BigDecimal.valueOf(0.18));
    }
}
```

## Interface Rules
- All fields: `public static final` (constants)
- All methods: `public` (default since Java 8)
- Multiple inheritance: `class A implements B, C { }`
- Functional interface: Single abstract method → lambda support

## Interface vs Abstract Class
| Feature | Interface | Abstract Class |
|---------|-----------|----------------|
| Multiple impl | Yes | No (single) |
| Fields | Constants only | Instance fields OK |
| Constructors | No | Yes |
| Default methods | Yes (Java 8+) | N/A |
| Use case | Contract, capability | Shared code + contract |