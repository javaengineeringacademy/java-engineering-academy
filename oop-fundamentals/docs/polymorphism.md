# Polymorphism

## 1. Introduction

Polymorphism, derived from Greek meaning "many forms," is a fundamental pillar of object-oriented programming. It allows objects of different types to be treated through a single reference, with the runtime system determining which implementation to invoke. Java supports two forms: compile-time (overloading) and runtime (overriding).

## 2. Learning Objectives

- Distinguish compile-time and runtime polymorphism
- Apply method overloading and overriding correctly
- Understand dynamic dispatch and the vtable mechanism
- Use covariant return types and bridge methods
- Leverage `instanceof` pattern matching and `switch` expressions

## 3. Prerequisites

- Basic Java syntax, classes, and inheritance
- Understanding of interfaces and abstract classes
- Familiarity with method signatures and return types

## 4. Why This Concept Exists

Without polymorphism, code that handles multiple types requires verbose conditional logic (`if-else` chains or `switch` statements checking types). Polymorphism eliminates this by allowing a single interface to represent multiple implementations, making code extensible and maintainable.

## 5. Problem Statement

Consider processing different animal sounds without polymorphism:

```java
// Without polymorphism
public class SoundProcessor {
    public void process(Object animal) {
        if (animal instanceof Dog) {
            System.out.println("Woof!");
        } else if (animal instanceof Cat) {
            System.out.println("Meow!");
        } else if (animal instanceof Bird) {
            System.out.println("Tweet!");
        }
        // Every new animal type requires modifying this method
    }
}
```

This violates the Open/Closed Principle. With polymorphism:

```java
public interface SoundProducer {
    String sound();
}

public void process(SoundProducer producer) {
    System.out.println(producer.sound());
}
```

## 6. Theory

Polymorphism operates at two levels:

**Compile-time (Static) Polymorphism** — Method overloading resolves at compile time based on the reference type and arguments. The compiler selects the most specific matching signature.

**Runtime (Dynamic) Polymorphism** — Method overriding resolves at runtime based on the actual object type. The JVM uses virtual method dispatch to find the correct implementation in the class hierarchy.

Both mechanisms enable writing code that works with general types while allowing specific behavior to vary.

## 7. Internal Working

### Static Dispatch (Overloading)

The compiler performs overload resolution by:
1. Collecting all methods with the same name in scope
2. Matching argument types against parameter types
3. Selecting the most specific applicable method
4. Applying widening, autoboxing, or varargs conversion if needed

### Dynamic Dispatch (Overriding)

The JVM maintains a virtual method table (vtable) for each class:
1. Each class has a vtable mapping method signatures to implementations
2. When a virtual method is called, the JVM looks up the method in the actual object's vtable
3. The vtable inherits entries from parent classes, overriding entries for overridden methods

```
Animal vtable:          Dog vtable:
┌──────────────────┐    ┌──────────────────┐
│ makeSound →      │    │ makeSound →      │
│   Animal.sound() │    │   Dog.sound()    │
│ toString  →      │    │ toString  →      │
│   Object.toString│    │   Object.toString│
└──────────────────┘    └──────────────────┘
```

## 8. JVM Perspective

The JVM implements polymorphism through:

- **Virtual method dispatch**: Invoked via `invokevirtual` bytecode instruction
- **Interface method dispatch**: Invoked via `invokeinterface` instruction (slightly slower due to multiple interface support)
- **Static method dispatch**: Invoked via `invokestatic` (no polymorphism)
- **Private/final method dispatch**: Invoked via `invokespecial` (no polymorphism)

The JIT compiler optimizes virtual calls through:
- **Inline caches**: Caches the target method for repeated calls on the same type
- **Devirtualization**: Converts virtual calls to direct calls when the type is monomorphic
- **Speculative optimization**: Inlines the most common target and deoptimizes if a different type appears

## 9. Memory Representation

```
Heap:
┌─────────────────────────────────┐
│  Dog Object                     │
│  ┌───────────────────────────┐  │
│  │ vtable pointer ──────────►│──┼──► Dog vtable
│  │ name: "Rex"              │  │     (makeSound → Dog.sound)
│  └───────────────────────────┘  │
└─────────────────────────────────┘

Stack:
┌─────────────────────────────────┐
│  Animal a ─────────────────────►│──► Dog Object (heap)
│  (reference type: Animal)       │
└─────────────────────────────────┘
```

## 10. Architecture Diagram

```
┌─────────────────────────────────────────────────┐
│               Polymorphism                       │
├────────────────────┬────────────────────────────┤
│  Compile-time      │  Runtime                   │
│  (Static)          │  (Dynamic)                 │
├────────────────────┼────────────────────────────┤
│  Method Overloading│  Method Overriding         │
│  Resolved by       │  Resolved by               │
│  compiler          │  JVM at runtime            │
│  invokestatic /    │  invokevirtual /           │
│  invokespecial     │  invokeinterface           │
└────────────────────┴────────────────────────────┘
```

## 11. Flow Diagram

### Overloading Resolution
```
Method call: add(1, 2)
    │
    ▼
Compiler collects: add(int,int), add(double,double), add(int,int,int)
    │
    ▼
Match arguments: (int, int) → add(int,int)
    │
    ▼
Emit: invokevirtual add(int,int)
```

### Overriding Resolution
```
Animal a = new Dog();
a.makeSound();
    │
    ▼
JVM: actual type = Dog
    │
    ▼
Lookup Dog vtable for "makeSound"
    │
    ▼
Found: Dog.makeSound()
    │
    ▼
Execute Dog.sound()
```

## 12. Syntax

### Method Overloading
```java
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public int add(int... numbers) {
        return Arrays.stream(numbers).sum();
    }
}
```

### Method Overriding
```java
class Shape {
    public double area() { return 0; }
    public String describe() { return "Shape"; }
}

class Circle extends Shape {
    private final double radius;

    public Circle(double radius) { this.radius = radius; }

    @Override
    public double area() { return Math.PI * radius * radius; }

    @Override
    public String describe() { return "Circle[radius=%.2f]".formatted(radius); }
}
```

### Covariant Return Type
```java
class Animal {
    Animal getSelf() { return this; }
}

class Dog extends Animal {
    @Override
    Dog getSelf() { return this; }
}
```

## 13. Easy Example

```java
public class Printer {
    public void print(int value) {
        System.out.println("Integer: " + value);
    }

    public void print(String value) {
        System.out.println("String: " + value);
    }

    public static void main(String[] args) {
        Printer p = new Printer();
        p.print(42);       // Integer: 42
        p.print("Hello");  // String: Hello
    }
}
```

## 14. Medium Example

```java
abstract class Animal {
    abstract String sound();

    void describe() {
        System.out.println("Animal makes: " + sound());
    }
}

class Dog extends Animal {
    @Override
    String sound() { return "Woof"; }
}

class Cat extends Animal {
    @Override
    String sound() { return "Meow"; }
}

class Owl extends Animal {
    @Override
    String sound() { return "Hoot"; }
}

public class Main {
    public static void main(String[] args) {
        List<Animal> animals = List.of(new Dog(), new Cat(), new Owl());
        for (Animal a : animals) {
            a.describe(); // Runtime dispatch to correct sound()
        }
    }
}
```

## 15. Hard Example

```java
interface Processable<T> {
    T process(T input);
    default T fallback() { return null; }
}

class StringProcessor implements Processable<String> {
    @Override
    public String process(String input) {
        return input.toUpperCase();
    }
}

class IntegerProcessor implements Processable<Integer> {
    @Override
    public Integer process(Integer input) {
        return input * 2;
    }

    @Override
    public Integer fallback() { return 0; }
}

public class ProcessorFactory {
    private static final Map<Class<?>, Processable<?>> PROCESSORS = Map.of(
        String.class, new StringProcessor(),
        Integer.class, new IntegerProcessor()
    );

    @SuppressWarnings("unchecked")
    public static <T> Processable<T> getProcessor(Class<T> type) {
        return (Processable<T>) PROCESSORS.get(type);
    }
}

public class Main {
    public static void main(String[] args) {
        Processable<String> sp = ProcessorFactory.getProcessor(String.class);
        Processable<Integer> ip = ProcessorFactory.getProcessor(Integer.class);

        System.out.println(sp.process("hello")); // HELLO
        System.out.println(ip.process(21));      // 42
    }
}
```

## 16. Enterprise Example

```java
public interface PaymentProcessor {
    String type();
    PaymentResult process(PaymentRequest request);
    default boolean supports(String currency) { return true; }
}

public record PaymentRequest(
    String orderId,
    long amountCents,
    String currency
) {}

public record PaymentResult(
    boolean success,
    String transactionId,
    String error
) {
    public static PaymentResult success(String transactionId) {
        return new PaymentResult(true, transactionId, null);
    }

    public static PaymentResult failure(String error) {
        return new PaymentResult(false, null, error);
    }
}

public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public String type() { return "CREDIT_CARD"; }

    @Override
    public PaymentResult process(PaymentRequest request) {
        // Simulate processing
        return PaymentResult.success("CC-" + request.orderId());
    }
}

public class PayPalProcessor implements PaymentProcessor {
    @Override
    public String type() { return "PAYPAL"; }

    @Override
    public PaymentResult process(PaymentRequest request) {
        return PaymentResult.success("PP-" + request.orderId());
    }
}

public class PaymentService {
    private final Map<String, PaymentProcessor> processors;

    public PaymentService(List<PaymentProcessor> processorList) {
        this.processors = processorList.stream()
            .collect(Collectors.toMap(PaymentProcessor::type, p -> p));
    }

    public PaymentResult charge(String type, PaymentRequest request) {
        PaymentProcessor processor = processors.get(type);
        if (processor == null) {
            return PaymentResult.failure("Unsupported: " + type);
        }
        if (!processor.supports(request.currency())) {
            return PaymentResult.failure("Currency not supported");
        }
        return processor.process(request); // Dynamic dispatch
    }
}
```

## 17. Performance

**Overloading**: Zero runtime overhead. Resolution happens at compile time. The JVM executes the directly resolved method.

**Overriding**: Minor overhead from vtable lookup (one pointer dereference). Modern JVMs mitigate this through:
- Inline caches: ~1-2ns for monomorphic calls
- Devirtualization: eliminates overhead for known types
- Inlining: JIT compiles the method body directly at the call site

Measured overhead is typically negligible (<5%) in real applications.

## 18. Time Complexity

| Operation | Complexity |
|-----------|------------|
| Overload resolution (compile-time) | O(n) where n = number of overloads |
| Virtual dispatch | O(1) vtable lookup |
| Interface dispatch | O(1) with itable caching |
| `instanceof` check | O(1) per type in hierarchy |
| Pattern matching switch | O(n) where n = number of cases |

## 19. Space Complexity

Polymorphism adds minimal space overhead:
- vtable per class: O(m) where m = number of virtual methods
- itable per interface: O(k) where k = interface methods
- No per-instance overhead for dispatch

## 20. Thread Safety

Polymorphism itself is thread-safe. The vtable and itable are immutable after class loading and shared across threads. However, the implementations may require synchronization:

```java
class ThreadSafeCounter {
    private int count = 0;

    public synchronized void increment() { count++; }
    public int get() { return count; }
}
```

Vtable reads are safe because vtables are published after class initialization completes, ensuring safe publication via the class loading happens-before relationship.

## 21. Best Practices

1. **Program to interfaces** — Declare variables as interface types, instantiate concrete implementations.
2. **Use `@Override` annotation** — Catches signature mismatches at compile time.
3. **Favor composition over inheritance** — Use interfaces and composition for flexible polymorphic behavior.
4. **Keep override contracts** — Do not strengthen preconditions or weaken postconditions (Liskov Substitution Principle).
5. **Avoid deep hierarchies** — Flat hierarchies with interfaces are more flexible than deep class trees.
6. **Use sealed classes for controlled polymorphism** — Java 17+ sealed classes restrict which classes can implement an interface.
7. **Prefer pattern matching over casting** — Use `instanceof` patterns and `switch` expressions (Java 16+/17+) instead of manual casting.
8. **Document override behavior** — Javadoc should explain behavioral contracts, not just method signatures.
9. **Be careful with overloaded methods in inheritance** — Overloading in a subclass can shadow parent methods unexpectedly.
10. **Test with the base type** — Write tests against the interface to ensure polymorphic correctness.

## 22. Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Overloading instead of overriding | Creates new method, does not override parent | Ensure exact signature match and use `@Override` |
| Changing parameter types | Breaks override contract | Keep identical parameter list |
| Weakening access modifier | Compile-time error | Keep same or wider access |
| Throwing broader checked exceptions | Compile-time error | Keep same or narrower exceptions |
| Not calling super method | Skips parent behavior | Call `super.method()` when needed |
| Overloading with similar types | Ambiguous call, autoboxing surprises | Use distinct parameter types |
| Overriding final methods | Compile-time error | Remove `final` or do not override |

## 23. Pitfalls

- **Covariant return types** require careful handling with bridge methods; incorrect use can cause `ClassCastException`.
- **Autoboxing ambiguity** in overloaded methods: `method(int)` vs `method(Integer)` can produce unexpected results.
- **Method hiding** (static methods) creates confusion when a subclass redeclares a static method with the same name.
- **Generic type erasure** can cause bridge methods to be generated, affecting `instanceof` and `getDeclaredMethod`.
- **Diamond problem** with default methods in interfaces requires explicit resolution.

## 24. Debugging Tips

- Set breakpoints in overridden methods and inspect the call stack to verify dispatch.
- Use `javap -c` to examine bytecode instructions (`invokevirtual` vs `invokestatic`).
- Add logging at override entry points: `Logger.getLogger(getClass()).info("Entering method")`.
- Use `Thread.currentThread().getStackTrace()` to verify the dispatch chain.
- Test with the base type to catch unintended static dispatch.

## 25. Comparison Table

| Aspect | Overloading | Overriding |
|--------|-------------|------------|
| Resolution | Compile-time | Runtime |
| Signature | Same name, different params | Same name and params |
| Return type | Can differ | Covariant |
| Access modifier | Can differ | Same or wider |
| `final` methods | Can overload | Cannot override |
| `static` methods | Can overload | Hides, does not override |
| `private` methods | Can overload | Cannot override |
| Binding | Static | Dynamic |
| Performance | Zero overhead | Vtable lookup |

## 26. Decision Tree

```
Need different behavior based on type?
├── Yes, same class, different parameters → Overloading
├── Yes, subclass provides implementation → Overriding
├── Yes, compile-time known types → Overloading
└── Yes, runtime unknown types → Overriding + dynamic dispatch

Need to check type at runtime?
├── Simple check → instanceof
├── Check + cast → instanceof pattern matching (Java 16+)
└── Multiple types → switch expression with patterns (Java 17+)
```

## 27. Interview Questions

1. **What is polymorphism in Java?**
   Polymorphism allows objects of different types to be treated through a single reference. Java supports compile-time (overloading) and runtime (overriding) polymorphism.

2. **Overloading vs Overriding?**
   Overloading: same name, different parameter list, resolved at compile time. Overriding: same signature in subclass, provides specific implementation, resolved at runtime.

3. **What is dynamic method dispatch?**
   The JVM resolves the actual method to call at runtime based on the object's real type, not the reference type. This enables polymorphic behavior.

4. **What is covariant return type?**
   An overriding method can return a subclass of the parent method's return type (Java 5+).

5. **Can we override static methods?**
   No. Static methods belong to the class, not instances. They are hidden (not overridden).

6. **Can we override private methods?**
   No. Private methods are not visible to subclasses and cannot be overridden.

7. **Can we override final methods?**
   No. The `final` modifier prevents method overriding.

8. **Can constructors be overloaded?**
   Yes. This is the basis for `this()` constructor chaining.

9. **Can constructors be overridden?**
   No. Constructors are not inherited and cannot be overridden.

10. **What is the Liskov Substitution Principle (LSP)?**
    Subtypes must be substitutable for their base types without altering correctness. Overriding methods must not strengthen preconditions or weaken postconditions.

11. **Explain bridge methods.**
    The compiler generates bridge methods to preserve polymorphism with generics. When a generic class overrides a method, the bridge method handles type erasure by casting and delegating.

12. **How does `instanceof` pattern matching help?**
    It eliminates explicit casting and boilerplate. `if (obj instanceof Dog dog)` combines the type check and cast in one step.

13. **What is the performance impact of dynamic dispatch?**
    Minor overhead due to vtable lookup. JVM uses inline caches and devirtualization to mitigate this. Overhead is negligible in most applications.

14. **Can overloaded methods have the same return type?**
    Yes. Overloading is determined by the parameter list, not the return type.

15. **What is method hiding?**
    When a subclass redeclares a static method with the same signature as the parent, it hides the parent method. The reference type determines which is called.

## 28. Exercises

1. **Overloading exercise**: Create a `Logger` class with overloaded `log()` methods accepting `String`, `Exception`, `String + Exception`, and `String + Object[]`.
2. **Overriding exercise**: Create a `Vehicle` hierarchy with `Car`, `Motorcycle`, and `Truck`, each overriding `accelerate()` and `brake()`.
3. **Dynamic dispatch exercise**: Write a `ShapeDrawer` that takes a `List<Shape>` and calls `draw()` on each, demonstrating runtime polymorphism.
4. **Pattern matching exercise**: Use a `switch` expression with `sealed` interfaces and pattern matching to process different `Payment` types.
5. **Bridge method exercise**: Create a generic `Box<T>` implementing `Comparable<Box<T>>` and inspect the generated bridge method with `javap`.

## 29. Assignments

1. **Design a plugin system**: Create an `interface Plugin` with `start()` and `stop()`. Implement three plugins and a `PluginManager` that discovers and manages them at runtime.
2. **Refactor conditional logic**: Find a large `if-else` or `switch` that checks types, and refactor it using polymorphism with an interface or abstract class.
3. **Implement a strategy pattern**: Create a `SortStrategy` interface with `BubbleSort`, `QuickSort`, and `MergeSort` implementations. Demonstrate runtime polymorphism by selecting the strategy dynamically.

## 30. Mini Project

**Polymorphic Notification System**

```java
public interface Notification {
    String channel();
    void send(String recipient, String message);
}

public record EmailNotification(String smtpHost) implements Notification {
    @Override
    public String channel() { return "EMAIL"; }

    @Override
    public void send(String recipient, String message) {
        System.out.println("[EMAIL via " + smtpHost + "] To: " + recipient + " - " + message);
    }
}

public record SmsNotification(String provider) implements Notification {
    @Override
    public String channel() { return "SMS"; }

    @Override
    public void send(String recipient, String message) {
        System.out.println("[SMS via " + provider + "] To: " + recipient + " - " + message);
    }
}

public class NotificationService {
    private final List<Notification> notifications;

    public NotificationService(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void broadcast(String recipient, String message) {
        for (Notification n : notifications) {
            n.send(recipient, message); // Dynamic dispatch
        }
    }
}

public class Main {
    public static void main(String[] args) {
        var service = new NotificationService(List.of(
            new EmailNotification("smtp.example.com"),
            new SmsNotification("Twilio")
        ));
        service.broadcast("user@example.com", "Hello!");
    }
}
```

## 31. Summary

Polymorphism enables writing flexible, extensible code by allowing a single interface to represent multiple implementations. Compile-time polymorphism (overloading) provides API flexibility, while runtime polymorphism (overriding) enables behavioral customization. Combined with modern Java features like pattern matching, sealed classes, and covariant return types, polymorphism remains a cornerstone of clean, maintainable Java code.

## 32. References

- [JLS §8.4 — Method Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4)
- [JLS §15.12 — Method Invocation Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.12)
- [JEP 394: Pattern Matching for instanceof](https://openjdk.org/jeps/394)
- [JEP 441: Pattern Matching for switch](https://openjdk.org/jeps/441)
- [Baeldung — Java Polymorphism](https://www.baeldung.com/java-polymorphism)
- [Java SE 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
