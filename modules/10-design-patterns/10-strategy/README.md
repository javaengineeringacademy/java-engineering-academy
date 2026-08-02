# Strategy Pattern

## 1. Introduction

The Strategy Pattern is a behavioral design pattern that defines a family of algorithms, encapsulates each one, and makes them interchangeable. Strategy lets the algorithm vary independently from clients that use it. It is particularly useful when you have multiple algorithms for a specific task and want to switch between them dynamically at runtime.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement the Strategy pattern with functional interfaces
- Replace conditional logic with strategy objects
- Understand when to use strategy over other patterns
- Recognize strategy usage in Java (Collections.sort, Comparator)
- Combine strategy with factory for dynamic selection

---

## 3. Prerequisites

- Understanding of interfaces and abstract classes
- Knowledge of functional programming in Java
- Familiarity with polymorphism
- Understanding of SOLID principles

---

## 4. Why This Concept Exists

The Strategy pattern exists because:

- **Eliminate conditionals**: Replace if-else/switch with polymorphism
- **Open/Closed Principle**: Add new algorithms without modifying context
- **Runtime flexibility**: Switch algorithms at runtime
- **Testability**: Test algorithms in isolation
- **Single Responsibility**: Each strategy has one algorithm

Without Strategy, you would have complex conditional logic scattered throughout the code.

---

## 5. Problem Statement

Consider sorting:

```java
public class Sorter {
    public void sort(List<Integer> list, String algorithm) {
        if (algorithm.equals("bubble")) {
            // Bubble sort implementation
        } else if (algorithm.equals("quick")) {
            // Quick sort implementation
        } else if (algorithm.equals("merge")) {
            // Merge sort implementation
        }
    }
}
```

This violates OCP and is hard to maintain.

---

## 6. Theory

### 6.1 Strategy Structure

1. **Strategy**: Interface for algorithm family
2. **ConcreteStrategy**: Implementation of algorithm
3. **Context**: Uses strategy, maintains reference

### 6.2 Strategy vs. Similar Patterns

| Pattern | Purpose | Change |
|---------|---------|--------|
| Strategy | Encapsulate algorithm | Algorithm |
| State | Change behavior based on state | Internal state |
| Template Method | Define algorithm skeleton | Subclass steps |

---

## 7. Internal Working

```
Context -> Strategy interface -> ConcreteStrategy
                |
        Algorithm selection
                |
        Algorithm execution
```

---

## 8. JVM Perspective

- Virtual method table used for strategy dispatch
- JIT can inline small strategies
- Lambda expressions optimized by JVM

---

## 9. Memory Representation

```
Context
  |-- strategy: Strategy
        |-- ConcreteStrategyA
        |-- ConcreteStrategyB
        |-- ConcreteStrategyC
```

---

## 10. Syntax

```java
public interface Strategy {
    int execute(int a, int b);
}

public class AddStrategy implements Strategy {
    @Override
    public int execute(int a, int b) {
        return a + b;
    }
}

public class Context {
    private Strategy strategy;

    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public int executeStrategy(int a, int b) {
        return strategy.execute(a, b);
    }
}
```

---

## 11. Easy Example

### Payment Strategy

```java
public interface PaymentStrategy {
    void pay(double amount);
}

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " via Credit Card");
    }
}

public class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " via PayPal");
    }
}

public class ShoppingCart {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void checkout(double total) {
        paymentStrategy.pay(total);
    }
}

// Usage
Cart cart = new ShoppingCart();
cart.setPaymentStrategy(new CreditCardPayment());
cart.checkout(99.99);
```

---

## 12. Medium Example

### Sort Strategy with Lambda

```java
@FunctionalInterface
public interface SortStrategy<T> {
    void sort(List<T> list);
}

public class Sorter<T extends Comparable<T>> {
    private SortStrategy<T> strategy;

    public void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void sort(List<T> list) {
        strategy.sort(list);
    }
}

// Usage
Sorter<Integer> sorter = new Sorter<>();
sorter.setStrategy(list -> Collections.sort(list));
sorter.setStrategy(list -> list.sort(Comparator.naturalOrder()));
```

---

## 13. Hard Example

### Strategy with Factory and Enum

```java
public enum CompressionStrategy implements java.util.function.BiConsumer<byte[], OutputStream> {
    GZIP((data, out) -> {
        try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(data);
        } catch (IOException e) { throw new RuntimeException(e); }
    }),
    ZIP((data, out) -> {
        try (ZipOutputStream zip = new ZipOutputStream(out);
             ZipEntry entry = new ZipEntry("file")) {
            zip.putNextEntry(entry);
            zip.write(data);
            zip.closeEntry();
        } catch (IOException e) { throw new RuntimeException(e); }
    }),
    NONE((data, out) -> {
        try { out.write(data); } catch (IOException e) { throw new RuntimeException(e); }
    });

    private final BiConsumer<byte[], OutputStream> compressor;

    CompressionStrategy(BiConsumer<byte[], OutputStream> compressor) {
        this.compressor = compressor;
    }

    @Override
    public void accept(byte[] data, OutputStream out) {
        compressor.accept(data, out);
    }
}

public class FileCompressor {
    private CompressionStrategy strategy;

    public FileCompressor(CompressionStrategy strategy) {
        this.strategy = strategy;
    }

    public void compress(byte[] data, OutputStream out) {
        strategy.accept(data, out);
    }
}
```

---

## 14. Enterprise Example

### Pricing Strategy System

```java
public interface PricingStrategy {
    BigDecimal calculatePrice(BigDecimal basePrice, Customer customer);
}

public class RegularPricing implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, Customer customer) {
        return basePrice;
    }
}

public class PremiumPricing implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, Customer customer) {
        return basePrice.multiply(BigDecimal.valueOf(0.9)); // 10% discount
    }
}

public class VipPricing implements PricingStrategy {
    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, Customer customer) {
        return basePrice.multiply(BigDecimal.valueOf(0.8)); // 20% discount
    }
}

public class PricingContext {
    private PricingStrategy strategy;
    private final Map<String, PricingStrategy> strategies = new HashMap<>();

    public void registerStrategy(String tier, PricingStrategy strategy) {
        strategies.put(tier, strategy);
    }

    public void setStrategy(String tier) {
        this.strategy = strategies.get(tier);
    }

    public BigDecimal calculate(BigDecimal basePrice, Customer customer) {
        return strategy.calculatePrice(basePrice, customer);
    }
}

// Usage
PricingContext pricing = new PricingContext();
pricing.registerStrategy("regular", new RegularPricing());
pricing.registerStrategy("premium", new PremiumPricing());
pricing.registerStrategy("vip", new VipPricing());

pricing.setStrategy("premium");
BigDecimal finalPrice = pricing.calculate(BigDecimal.valueOf(100), customer);
```

---

## 15. Performance

| Strategy Type | Performance | Memory |
|--------------|-------------|--------|
| Class-based | Good | More objects |
| Lambda | Excellent | Minimal |
| Enum | Excellent | Minimal |

---

## 16. Best Practices

1. Use functional interfaces for simple strategies
2. Prefer immutability in strategy objects
3. Use factory for strategy creation
4. Document strategy contracts clearly
5. Consider enum for fixed set of strategies

---

## 17. Common Mistakes

1. Over-engineering simple conditionals
2. Not documenting strategy contract
3. Creating mutable strategy objects
4. Ignoring null strategy in context

---

## 18. Pitfalls

- Increased number of classes
- Client must be aware of different strategies
- Communication overhead between context and strategy

---

## 19. Debugging Tips

1. Log strategy selection
2. Use debugger to verify strategy being used
3. Add toString() to strategies for identification
4. Test each strategy in isolation

---

## 20. Comparison Table

| Feature | Strategy | Conditional | State |
|---------|----------|-------------|-------|
| Flexibility | High | Low | High |
| OCP | Yes | No | Yes |
| Complexity | Medium | Low | High |
| Runtime change | Yes | No | Yes |

---

## 21. Decision Tree

```
Multiple algorithms for same task?
  -> Yes: Strategy pattern
  -> No: Simple method

Need to switch at runtime?
  -> Yes: Strategy
  -> No: Consider Template Method

Algorithm depends on object state?
  -> Yes: State pattern
  -> No: Strategy
```

---

## 22. Interview Questions

### Q1: What is the Strategy pattern?
A behavioral pattern that defines a family of algorithms and makes them interchangeable.

### Q2: Strategy vs. State?
Strategy: Client selects algorithm. State: Object changes behavior based on internal state.

### Q3: When to use Strategy over inheritance?
When you need runtime algorithm switching or want to avoid class explosion.

### Q4: Real-world examples?
Java Comparator, Collections.sort(), payment processing, compression algorithms.

---

## 23. Exercises

1. Create a text compression strategy (GZIP, ZIP, None)
2. Implement a validation strategy for different input types
3. Build a logging strategy (console, file, database)

---

## 24. Assignments

1. Implement pricing strategies for an e-commerce platform
2. Create notification strategies (Email, SMS, Push)
3. Build sorting strategies with benchmarking

---

## 25. Mini Project

### Order Processing System
Implement multiple pricing, discount, and shipping strategies that can be combined and swapped at runtime.

---

## 26. Summary

- Strategy pattern encapsulates algorithms
- Eliminates conditional logic
- Supports runtime algorithm switching
- Promotes OCP and single responsibility
- Use functional interfaces for simple strategies

---

## 27. References

1. Gamma, E., et al. (1994). Design Patterns, Chapter 5
2. Bloch, J. (2018). Effective Java, Item 64
3. Refactoring Guru: https://refactoring.guru/design-patterns/strategy
