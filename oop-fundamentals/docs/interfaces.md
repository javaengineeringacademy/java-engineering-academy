# Interfaces

## Modern Interfaces (Java 8+)

```java
public interface Payable {
    // Abstract - must implement
    void pay(BigDecimal amount);

    // Default - optional override
    default void printReceipt() {
        System.out.println("Receipt printed");
    }

    // Static utility
    static BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.18));
    }

    // Private (Java 9+)
    private void log(String msg) { System.out.println(msg); }
}
```

## Interface Rules

| Element | Modifiers |
|---------|-----------|
| Fields | `public static final` (implicit) |
| Methods | `public` (default) |
| Constructors | Not allowed |
| Methods since Java 8 | default, static, private |

## Default Methods

```java
interface Drawable {
    void draw();  // Abstract

    default void drawTwice() {  // Default
        draw();
        draw();
    }
}

class Circle implements Drawable {
    @Override public void draw() { System.out.println("Circle"); }
}
```

## Static Methods

```java
interface MathUtils {
    static int max(int a, int b) { return Math.max(a, b); }
}

// Call: MathUtils.max(5, 10)
```

## Private Methods (Java 9+)

```java
interface Validator {
    default boolean validate(String s) { return check(s); }
    private boolean check(String s) { return s != null && !s.isBlank(); }
}
```

## Functional Interfaces

Single abstract method → Lambda support:

```java
@FunctionalInterface
interface Operation {
    int apply(int a, int b);
}

// Lambdas
Operation add = (a, b) -> a + b;
Operation multiply = (a, b) -> a * b;
```

## Interface vs Abstract Class

| Feature | Interface | Abstract Class |
|---------|-----------|----------------|
| Inheritance | Multiple | Single |
| Fields | Constants only | Instance + constants |
| Constructors | No | Yes |
| Methods | Abstract, default, static | Abstract + concrete |
| Access | public | Any |

## Functional Interfaces

```java
@FunctionalInterface
interface Calculator {
    int calc(int a, int b);
}

// Built-in: java.util.function
Function<String, Integer> parser = Integer::parseInt;
Predicate<String> isEmpty = String::isEmpty;
Consumer<String> printer = System.out::println;
Supplier<LocalDate> now = LocalDate::now;
```

## Default Methods & Diamond Problem

```java
interface A { default void m() { System.out.println("A"); } }
interface B { default void m() { System.out.println("B"); } }

class C implements A, B {
    @Override public void m() { A.super.m(); }  // Must override
}
```

## Interface vs Abstract Class

| Scenario | Choice |
|----------|--------|
| Shared code + contract | Abstract class |
| Pure contract | Interface |
| Multiple implementations | Interface |
| Need constructors/fields | Abstract class |
| Functional interface | Interface |

## Best Practices

- Use `@FunctionalInterface` annotation
- Prefer interfaces for contracts
- Default methods for backward compatibility
- Static methods for utilities
- Keep interfaces focused (ISP)