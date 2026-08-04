# Polymorphism

Polymorphism ("many forms") allows objects of different types to be treated through a single interface. It enables flexible, extensible code through compile-time and runtime mechanisms.

## Table of Contents

1. [Concepts](#concepts)
2. [Compile-Time Polymorphism](#compile-time-polymorphism)
3. [Runtime Polymorphism](#runtime-polymorphism)
4. [Dynamic Dispatch](#dynamic-dispatch)
5. [Pattern Matching](#pattern-matching)
6. [Covariance and Contravariance](#covariance-and-contravariance)
7. [Best Practices](#best-practices)
8. [Key Takeaways](#key-takeaways)

---

## Concepts

### Types of Polymorphism

```
┌─────────────────────────────────────────────────────────┐
│                    POLYMORPHISM                         │
├────────────────────┬────────────────────────────────────┤
│   Compile-Time     │         Runtime                    │
│   (Static)         │         (Dynamic)                  │
├────────────────────┼────────────────────────────────────┤
│ • Method Overload  │ • Method Override                  │
│ • Operator Overload│ • Dynamic Dispatch                 │
│ • Generics         │ • Interface Implementation         │
│                    │ • Pattern Matching                  │
├────────────────────┼────────────────────────────────────┤
│ Resolved at        │ Resolved at                        │
│ compile time       │ execution time                     │
└────────────────────┴────────────────────────────────────┘
```

### Why Polymorphism?

- **Flexibility** - Code works with any type implementing an interface
- **Extensibility** - New types can be added without modifying existing code
- **Decoupling** - Reduces dependency on concrete implementations
- **Testability** - Easy to substitute mocks for testing

---

## Compile-Time Polymorphism

### Method Overloading

```java
public class Calculator {
    // Same method name, different parameter lists
    public int add(int a, int b) {
        return a + b;
    }

    public double add(double a, double b) {
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }

    public String add(String a, String b) {
        return a + b;
    }
}

// The compiler determines which method to call based on arguments
Calculator calc = new Calculator();
calc.add(1, 2);           // Calls int version
calc.add(1.5, 2.5);       // Calls double version
calc.add(1, 2, 3);        // Calls three-int version
calc.add("a", "b");       // Calls String version
```

### Overloading Resolution Rules

```java
public class OverloadExample {
    // 1. Exact match
    public void method(String s) {
        System.out.println("String");
    }

    // 2. Widening (int -> long -> float -> double)
    public void method(long l) {
        System.out.println("long");
    }

    // 3. Autoboxing
    public void method(Integer i) {
        System.out.println("Integer");
    }

    // 4. Varargs (least preferred)
    public void method(Object... args) {
        System.out.println("varargs");
    }
}

// Resolution order:
// 1. Exact match
// 2. Subtype match
// 3. Widening conversion
// 4. Autoboxing
// 5. Varargs
```

### Generics (Type-Safe Polymorphism)

```java
// Generic class
public class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A first() { return first; }
    public B second() { return second; }
}

// Generic method
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}

// Usage
Pair<String, Integer> pair = new Pair<>("age", 30);
String name = max("apple", "banana");  // "banana"
Integer value = max(10, 20);           // 20
```

### Bounded Generics

```java
// Upper bound - T must be Number or its subclasses
public static double sum(List<? extends Number> list) {
    return list.stream()
        .mapToDouble(Number::doubleValue)
        .sum();
}

// Lower bound - T must be Integer or its superclasses
public static void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

// Wildcard with bounds
public static void printAll(List<? extends Comparable<?>> list) {
    list.forEach(System.out::println);
}
```

---

## Runtime Polymorphism

### Method Overriding

```java
public abstract class PaymentProcessor {
    protected String name;
    
    public PaymentProcessor(String name) {
        this.name = name;
    }
    
    // Abstract - must be implemented
    public abstract boolean processPayment(double amount);
    
    // Concrete - can be overridden
    public void sendReceipt(String email) {
        System.out.println("Sending receipt to " + email);
    }
}

public class CreditCardProcessor extends PaymentProcessor {
    public CreditCardProcessor() {
        super("Credit Card");
    }
    
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing credit card payment: $" + amount);
        return true;
    }
}

public class PayPalProcessor extends PaymentProcessor {
    public PayPalProcessor() {
        super("PayPal");
    }
    
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing PayPal payment: $" + amount);
        return true;
    }
}

public class CryptoProcessor extends PaymentProcessor {
    public CryptoProcessor() {
        super("Cryptocurrency");
    }
    
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing crypto payment: $" + amount);
        return true;
    }
    
    @Override
    public void sendReceipt(String email) {
        // Crypto doesn't use email receipts
        System.out.println("Receipt stored on blockchain");
    }
}
```

### Polymorphic Collections

```java
// Process any payment type uniformly
public class PaymentService {
    private final List<PaymentProcessor> processors;
    
    public PaymentService(List<PaymentProcessor> processors) {
        this.processors = processors;
    }
    
    public boolean processOrder(double amount, String paymentType) {
        PaymentProcessor processor = findProcessor(paymentType);
        if (processor == null) {
            throw new IllegalArgumentException("Unknown payment type: " + paymentType);
        }
        return processor.processPayment(amount);
    }
    
    private PaymentProcessor findProcessor(String type) {
        return processors.stream()
            .filter(p -> p.name.equalsIgnoreCase(type))
            .findFirst()
            .orElse(null);
    }
}

// Usage
List<PaymentProcessor> processors = List.of(
    new CreditCardProcessor(),
    new PayPalProcessor(),
    new CryptoProcessor()
);

PaymentService service = new PaymentService(processors);
service.processOrder(99.99, "Credit Card");  // Credit card processing
service.processOrder(49.99, "PayPal");       // PayPal processing
```

### Interface-Based Polymorphism

```java
public interface Shape {
    double area();
    double perimeter();
    void draw(GraphicsContext gc);
}

public class Circle implements Shape {
    private final double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        gc.fillOval(0, 0, radius * 2, radius * 2);
    }
}

public class Rectangle implements Shape {
    private final double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double area() {
        return width * height;
    }
    
    @Override
    public double perimeter() {
        return 2 * (width + height);
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        gc.fillRect(0, 0, width, height);
    }
}

// Client code doesn't know about specific types
public class Canvas {
    private final List<Shape> shapes = new ArrayList<>();
    
    public void addShape(Shape shape) {
        shapes.add(shape);
    }
    
    public void drawAll(GraphicsContext gc) {
        shapes.forEach(s -> s.draw(gc));  // Polymorphic call
    }
    
    public double totalArea() {
        return shapes.stream()
            .mapToDouble(Shape::area)
            .sum();
    }
}
```

---

## Dynamic Dispatch

### How Dynamic Dispatch Works

```java
public class Animal {
    public void speak() {
        System.out.println("...");
    }
}

public class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Woof!");
    }
}

public class Cat extends Animal {
    @Override
    public void speak() {
        System.out.println("Meow!");
    }
}

// Dynamic dispatch at runtime
Animal animal = getAnimal();  // Could be Animal, Dog, or Cat
animal.speak();  // JVM determines which speak() to call at runtime

// The method called depends on the ACTUAL object type,
// not the reference type
```

### Virtual Method Invocation

```java
public class Base {
    public void method() {
        System.out.println("Base.method()");
    }
}

public class Derived extends Base {
    @Override
    public void method() {
        System.out.println("Derived.method()");
    }
    
    public void derivedOnly() {
        System.out.println("Derived.derivedOnly()");
    }
}

Base ref = new Derived();
ref.method();        // Derived.method() - dynamic dispatch
// ref.derivedOnly(); // Compile error - reference type is Base

// To call derived-only methods, need explicit cast
if (ref instanceof Derived d) {
    d.derivedOnly();  // Derived.derivedOnly()
}
```

### Performance Considerations

```java
// JIT compiler can devirtualize calls when:
// 1. Only one implementation exists
// 2. Class is final (no subclasses)
// 3. Method is final (no overriding)
// 4. Monomorphic call site (same type always)

public final class OptimizedService {
    // JIT can inline this - no dynamic dispatch needed
    public final void fastMethod() {
        // Hot path code
    }
}
```

---

## Pattern Matching

### Pattern Matching for instanceof (Java 16+)

```java
// Before Java 16
if (obj instanceof String) {
    String s = (String) obj;
    System.out.println(s.length());
}

// Java 16+ - pattern variable binding
if (obj instanceof String s) {
    System.out.println(s.length());
}

// With additional conditions
if (obj instanceof String s && s.length() > 5) {
    System.out.println("Long string: " + s);
}
```

### Pattern Matching in Switch (Java 21+)

```java
// Sealed class hierarchy
public sealed interface Shape 
    permits Circle, Rectangle, Triangle {
}

public record Circle(double radius) implements Shape {}
public record Rectangle(double width, double height) implements Shape {}
public record Triangle(double a, double b, double c) implements Shape {}

// Pattern matching switch
public static double area(Shape shape) {
    return switch (shape) {
        case Circle c -> Math.PI * c.radius() * c.radius();
        case Rectangle r -> r.width() * r.height();
        case Triangle t -> {
            double s = (t.a() + t.b() + t.c()) / 2;
            yield Math.sqrt(s * (s - t.a()) * (s - t.b()) * (s - t.c()));
        }
    };
}

// With guards
public static String describe(Shape shape) {
    return switch (shape) {
        case Circle c when c.radius() > 10 -> "Large circle";
        case Circle c -> "Small circle";
        case Rectangle r when r.width() == r.height() -> "Square";
        case Rectangle r -> "Rectangle";
        case Triangle t -> "Triangle";
    };
}
```

### Exhaustiveness Checking

```java
// Compiler ensures all cases are handled
public static String describe(Shape shape) {
    return switch (shape) {
        case Circle c -> "Circle";
        case Rectangle r -> "Rectangle";
        // Compile error if Triangle case is missing
        // (with sealed hierarchy)
    };
}

// Default case for non-sealed types
public static String describeObject(Object obj) {
    return switch (obj) {
        case Integer i -> "Integer: " + i;
        case String s -> "String: " + s;
        case null -> "null";
        default -> "Unknown: " + obj.getClass();
    };
}
```

---

## Covariance and Contravariance

### Covariant Return Types (Java 5+)

```java
public class Animal {
    public Animal create() {
        return new Animal();
    }
}

public class Dog extends Animal {
    @Override
    public Dog create() {  // Covariant - more specific return type
        return new Dog();
    }
}

// Usage
Dog dog = new Dog();
Dog puppy = dog.create();  // Returns Dog, not Animal
```

### Arrays are Covariant

```java
// Arrays support covariance
String[] strings = {"a", "b", "c"};
Object[] objects = strings;  // Valid - String[] is subtype of Object[]
objects[0] = 42;  // ArrayStoreException at runtime!

// This is why arrays covariance is sometimes considered a flaw
```

### Generics are Invariant

```java
// Generics are invariant
List<String> strings = List.of("a", "b");
// List<Object> objects = strings;  // Compile error!

// Use wildcards for flexibility
List<? extends Object> objects = strings;  // Producer - can read
List<? super Object> mutable = new ArrayList<Object>();  // Consumer - can write
```

### PECS Principle

```java
// Producer Extends, Consumer Super
public static <T> void copy(
    List<? super T> dest,      // Consumer - writes to dest
    List<? extends T> src      // Producer - reads from src
) {
    for (T item : src) {
        dest.add(item);
    }
}

// Example
List<Object> dest = new ArrayList<>();
List<String> src = List.of("a", "b", "c");
copy(dest, src);  // Works!
```

---

## Best Practices

### Program to an Interface

```java
// BAD - depends on concrete class
ArrayList<String> list = new ArrayList<>();

// GOOD - depends on interface
List<String> list = new ArrayList<>();

// BETTER - minimal interface
Collection<String> collection = getCollection();
```

### Use Polymorphism for Extensibility

```java
// Strategy pattern using polymorphism
public interface SortStrategy<T> {
    void sort(List<T> list);
}

public class QuickSort<T extends Comparable<T>> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list) {
        // QuickSort implementation
    }
}

public class MergeSort<T extends Comparable<T>> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list) {
        // MergeSort implementation
    }
}

public class Sorter<T extends Comparable<T>> {
    private SortStrategy<T> strategy;
    
    public void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }
    
    public void sort(List<T> list) {
        strategy.sort(list);  // Polymorphic call
    }
}
```

### Prefer Composition with Polymorphism

```java
// Use composition to achieve polymorphic behavior
public class TextEditor {
    private final List<TextOperation> operations = new ArrayList<>();
    
    public void addOperation(TextOperation op) {
        operations.add(op);
    }
    
    public void executeAll(String document) {
        for (TextOperation op : operations) {
            document = op.execute(document);  // Polymorphic
        }
    }
}

// Different operations implement TextOperation
public class UpperCase implements TextOperation {
    public String execute(String text) {
        return text.toUpperCase();
    }
}

public class Trim implements TextOperation {
    public String execute(String text) {
        return text.trim();
    }
}
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Compile-Time** | Resolved by compiler (overloading, generics) |
| **Runtime** | Resolved by JVM (overriding, dynamic dispatch) |
| **Overloading** | Same name, different parameters |
| **Overriding** | Same signature in subclass |
| **Dynamic Dispatch** | JVM selects method based on actual object type |
| **Pattern Matching** | Type check + cast in one step |
| **Covariant Return** | Override can return subclass type |
| **PECS** | Producer Extends, Consumer Super |
| **Interface Programming** | Depend on abstractions, not concretions |
| **Sealed Classes** | Enable exhaustive pattern matching |
