# Methods

## Introduction

Methods are blocks of code that perform specific tasks and are executed when called. In Java, every method belongs to a class and can operate on instance data or class-level data. Methods are fundamental building blocks of OOP, enabling code reuse, modularity, and encapsulation.

## Learning Objectives

- Understand method syntax and components
- Differentiate between instance, static, and special methods
- Implement method overloading and overriding effectively
- Apply varargs and method references appropriately
- Recognize pass-by-value semantics in Java
- Follow best practices for method design and documentation

## Prerequisites

- Basic Java syntax (variables, data types, control flow)
- Understanding of classes and objects
- Familiarity with access modifiers

## Why This Concept Exists

Methods exist to:
- **Organize code** into reusable, logical units
- **Encapsulate behavior** within objects
- **Enable polymorphism** through overriding
- **Reduce duplication** via overloading
- **Improve readability** by naming operations clearly

## Problem Statement

Without methods, code would be:
- Monolithic and hard to maintain
- Prone to duplication
- Impossible to reuse across classes
- Difficult to test and debug

## Method Signature

```java
[modifiers] ReturnType methodName(ParameterType paramName) [throws Exception] {
    // body
}
```

## Method Types

| Type | Keyword | Use Case |
|------|---------|----------|
| Instance | (none) | Operates on object state |
| Static | `static` | Utility, no instance needed |
| Final | `final` | Cannot be overridden |
| Abstract | `abstract` | No body, must be implemented |
| Synchronized | `synchronized` | Thread-safe |

## Method Overloading

Same name, different parameter list (number, type, or order):

```java
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public int add(int... numbers) { return Arrays.stream(numbers).sum(); }
}
```

**Rules:**
- Different parameter list (number, type, or order)
- Return type can differ
- Access modifier can differ

## Method Overriding

Subclass provides specific implementation:

```java
class Animal {
    public void makeSound() { System.out.println("Animal sound"); }
}

class Dog extends Animal {
    @Override
    public void makeSound() { System.out.println("Woof!"); }
}
```

**Rules:**
- Same signature (name + parameters)
- Return type: covariant (same or subclass)
- Access modifier: same or less restrictive
- Cannot override: `final`, `static`, `private`, `final` methods

## Varargs

```java
public void printAll(String... items) {
    for (String item : items) System.out.println(item);
}

// Calls
printAll("a", "b", "c");
printAll(new String[]{"a", "b"});
```

**Rules:**
- Only one varargs per method
- Must be last parameter
- Treated as array inside method

## Method References (Java 8+)

```java
// Static method reference
Function<String, Integer> parser = Integer::parseInt;

// Instance method reference
Consumer<String> printer = System.out::println;

// Constructor reference
Supplier<ArrayList<String>> listMaker = ArrayList::new;

// Instance method of arbitrary object
Comparator<String> cmp = String::compareToIgnoreCase;
```

## Pass-by-Value

Java is **always** pass-by-value:
- Primitives: copy of value
- Objects: copy of reference

```java
void modify(int x) { x = 20; }           // Primitive: unchanged
void modify(StringBuilder sb) { sb.append("!"); } // Object: modified!
```

## Internal Working

When a method is called:
1. **Call stack frame** is created on the stack
2. **Parameters** are copied to local variables
3. **Method body** executes
4. **Return value** is sent back to caller
5. **Stack frame** is popped from the stack

```
main() calls calculate(5, 3)
┌─────────────┐
│ calculate()  │ ← New frame pushed
│ a = 5        │
│ b = 3        │
│ return 8     │
└─────────────┘
┌─────────────┐
│ main()       │ ← Previous frame
│ result = 8   │
└─────────────┘
```

## JVM Perspective

- Methods are stored in the **method area** (metaspace in Java 8+)
- Each method has a **constant pool entry** in the class file
- **Bytecode instructions** (`invokevirtual`, `invokestatic`, `invokeinterface`) handle calls
- **Just-In-Time (JIT) compilation** optimizes frequently executed methods
- **Inline caching** speeds up virtual method dispatch

## Memory Representation

```java
Person p = new Person();
p.greet();

// Stack: p → reference to object
// Heap: Person object with method table pointer
// Metaspace: Method bytecode and metadata
```

## Easy Example

```java
public class Circle {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

// Usage
Circle c = new Circle(5.0);
double area = c.calculateArea(); // 78.54
```

## Medium Example

```java
public class StringProcessor {
    public String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }

    public String reverse(String input, char delimiter) {
        String[] parts = input.split("\\" + delimiter);
        StringBuilder result = new StringBuilder();
        for (int i = parts.length - 1; i >= 0; i--) {
            result.append(parts[i]);
            if (i > 0) result.append(delimiter);
        }
        return result.toString();
    }
}

// Usage
StringProcessor sp = new StringProcessor();
sp.reverse("hello");        // "olleh"
sp.reverse("a.b.c", '.');   // "c.b.a"
```

## Hard Example

```java
public class PipelineBuilder<I> {
    private final List<Function<Object, Object>> stages = new ArrayList<>();

    public <O> PipelineBuilder<O> then(Function<I, O> transformer) {
        stages.add(input -> transformer.apply((I) input));
        return (PipelineBuilder<O>) this;
    }

    public <E extends Exception> PipelineBuilder<I>
            thenChecked(CheckedFunction<I, ?, E> fn) throws E {
        stages.add(input -> fn.apply((I) input));
        return this;
    }

    @SuppressWarnings("unchecked")
    public I execute(I input) {
        Object result = input;
        for (Function<Object, Object> stage : stages) {
            result = stage.apply(result);
        }
        return (I) result;
    }

    @FunctionalInterface
    public interface CheckedFunction<T, R, E extends Exception> {
        R apply(T t) throws E;
    }
}
```

## Enterprise Example

```java
@Service
public class OrderService {
    private final OrderRepository repository;
    private final PaymentGateway paymentGateway;
    private final NotificationService notificationService;

    public OrderService(OrderRepository repository,
                        PaymentGateway paymentGateway,
                        NotificationService notificationService) {
        this.repository = repository;
        this.paymentGateway = paymentGateway;
        this.notificationService = notificationService;
    }

    @Transactional
    public OrderResult processOrder(CreateOrderRequest request) {
        Order order = Order.create(request);
        PaymentResult payment = paymentGateway.charge(order.getTotal());

        if (payment.isSuccessful()) {
            order.confirm(payment.getTransactionId());
            repository.save(order);
            notificationService.sendOrderConfirmation(order);
            return OrderResult.success(order.getId());
        }

        return OrderResult.failure(payment.getErrorMessage());
    }
}
```

## Performance

- **Method inlining**: JIT compiler inlines small methods for speed
- **Hot method optimization**: Frequently called methods are aggressively optimized
- **Avoid deep call chains**: Each call adds stack frame overhead
- **Use final/private**: Enables more aggressive compiler optimization
- **Profile before optimizing**: Use JMH for microbenchmarks

```java
// Bad: deep call chain in hot path
public int calculate(int x) {
    return add(multiply(x, 2), subtract(x, 1));
}

// Better: flatten for hot paths
public int calculate(int x) {
    return (x * 2) + (x - 1);
}
```

## Best Practices

- Use `@Override` annotation
- Keep methods small (single responsibility)
- Prefer primitives over wrappers for performance
- Use `@Nullable`/`@NonNull` annotations
- Document exceptions with `@throws`
- Name methods clearly: verbs for actions, `is`/`has` for boolean returns
- Avoid boolean parameters; use separate methods or enums instead
- Limit method parameters to 3-4; use objects for more
- Prefer composition over deep method call chains
- Make methods `final` or `private` by default to prevent accidental overriding
- Use `@Deprecated` annotation for methods being phased out
- Return empty collections or Optionals instead of null

## Common Mistakes

1. **Forgetting return statement** in non-void methods
2. **Using `==` instead of `.equals()`** for object comparison
3. **Not handling exceptions** properly in method signatures
4. **Overloading confusion** with varargs and autoboxing
5. **Stack overflow** from recursive methods without proper base case
6. **Boolean parameter abuse** making method calls unclear

## Pitfalls

- **Null pointer exceptions** when calling methods on null references
- **Concurrent modification** when iterating and modifying collections
- **Memory leaks** from long-running methods holding references
- **Performance issues** with string concatenation in loops
- **Invisible side effects** when methods modify shared state

## Debugging Tips

1. Use IDE debugger with breakpoints at method entry
2. Add logging at method entry/exit with parameters
3. Use `Thread.currentThread().getStackTrace()` for call trace
4. Check method signatures carefully when debugging overrides
5. Use JMH for performance-related issues
6. Enable `-XX:+PrintCompilation` to see JIT activity

## Comparison Table

| Feature | Method | Constructor | Static Block |
|---------|--------|-------------|--------------|
| Return type | Required | None | None |
| Name | Any valid identifier | Class name | None |
| Called via | Object or class | `new` keyword | Class loading |
| Can override | Yes | No | No |
| Purpose | Behavior | Initialization | Static initialization |

## Decision Tree

```
Need to add behavior?
├── Needs object state?
│   ├── Yes → Instance method
│   └── No → Static method
├── Multiple implementations?
│   ├── Yes → Overloading (same class)
│   └── No → Single method
├── Subclass should customize?
│   ├── Yes → Overridable (non-final)
│   └── No → Final method
└── Thread-safe access needed?
    ├── Yes → Synchronized method
    └── No → Regular method
```

## Interview Questions

1. **What is method overloading vs overriding?**
   - Overloading: same class, different parameters
   - Overriding: subclass, same signature

2. **Can we override static methods?**
   - No, they are hidden, not overridden

3. **What is covariant return type?**
   - Override can return subclass of original return type

4. **Why is Java pass-by-value only?**
   - Primitives: copy of value; Objects: copy of reference

5. **What is the advantage of varargs?**
   - Flexible API without array creation at call site

## Exercises

1. Create a `MathUtils` class with overloaded `max()` methods for int, double, and three parameters
2. Implement a `StringFormatter` with methods for camelCase, snake_case, and kebab-case conversion
3. Write a recursive method to calculate factorial with proper base case handling

## Assignments

1. Build a method chain library that supports fluent API calls
2. Implement a method cache that stores results of expensive computations
3. Create a custom annotation processor that validates method signatures

## Mini Project

**Command Parser Library**

Build a library that parses command-line arguments into method calls:

```java
@Command(name = "greet")
public class GreetCommand {
    @Parameter(name = "--name", required = true)
    private String name;

    @Parameter(name = "--formal", defaultValue = "false")
    private boolean formal;

    public void execute() {
        if (formal) {
            System.out.println("Good day, " + name + ".");
        } else {
            System.out.println("Hi, " + name + "!");
        }
    }
}
```

Features to implement:
- Annotation-based parameter definition
- Method dispatch based on command name
- Type conversion for parameters
- Help generation

## Summary

- Methods are named blocks of code that perform specific tasks
- Overloading allows multiple methods with the same name but different parameters
- Overriding allows subclasses to provide specific implementations
- Java uses pass-by-value for both primitives and object references
- Follow best practices: single responsibility, clear naming, proper documentation

## References

- [Java Language Specification - Methods](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4)
- [Effective Java Item 54: Return empty collections or arrays](https://www.oracle.com/java/technologies/javase/effective-java.html)
- [Effective Java Item 55: Return Optionals judiciously](https://www.oracle.com/java/technologies/javase/effective-java.html)
- [Oracle Docs - Defining Methods](https://docs.oracle.com/en/java/javase/21/java/javaOO/methods.html)
