# Topic 03: Functional Interfaces (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)
 | [📖 Continue to Part 4](README-part4.md)

---

        }
    }
    
    @FunctionalInterface
    public interface Supplier2<T> {
        T get();
        
        default Supplier2<T> memoize() {
            return new Supplier2<T>() {
                private T cachedValue;
                private boolean computed = false;
                
                @Override
                public T get() {
                    if (!computed) {
                        cachedValue = Supplier2.this.get();
                        computed = true;
                    }
                    return cachedValue;
                }
            };
        }
    }
    
    // Predicate builder
    public static <T> Predicate2<T> buildPredicate(Predicate2<T>... predicates) {
        return Arrays.stream(predicates)
            .reduce(Predicate2::and, Predicate2::and);
    }
    
    public static void main(String[] args) {
        // Test Predicate2
        Predicate2<String> isLong = s -> s.length() > 5;
        Predicate2<String> startsWithJ = s -> s.startsWith("J");
        Predicate2<String> combined = isLong.and(startsWithJ);
        
        System.out.println("Java is long and starts with J: " + combined.test("Java"));
        System.out.println("J is long and starts with J: " + combined.test("J"));
        
        // Test Function2 pipeline
        Function2<String, String> pipeline = Function2.<String>identity()
            .andThen(String::trim)
            .andThen(String::toLowerCase)
            .andThen(s -> s.replaceAll("\\s+", "_"));
        
        System.out.println("Pipeline: " + pipeline.apply("  Hello World  "));
        
        // Test Supplier2 memoization
        Supplier2<String> expensiveComputation = () -> {
            System.out.println("Computing expensive value...");
            return "EXPENSIVE_RESULT";
        }.memoize();
        
```
        System.out.println("First access:");
        System.out.println("Value: " + expensiveComputation.get());
        System.out.println("Second access (cached):");
        System.out.println("Value: " + expensiveComputation.get());
    }
}
```

---

## 14. Enterprise Example

### Example 1: Order Processing with Functional Interfaces

```java
package academy.javaengineering.functional.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.*;

public class OrderProcessing {
    
    public record Order(
        String id,
        String customerId,
        List<OrderItem> items,
        OrderStatus status,
        LocalDateTime createdAt,
        BigDecimal totalAmount
    ) {}
    
    public record OrderItem(
        String productId,
        String productName,
        int quantity,
        BigDecimal unitPrice
    ) {}
    
    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
    
    // Functional interfaces for order operations
    @FunctionalInterface
    public interface OrderValidator {
        ValidationResult validate(Order order);
        
        default OrderValidator and(OrderValidator other) {
            return order -> {
                ValidationResult result = this.validate(order);
                if (!result.isValid()) return result;
                return other.validate(order);
            };
        }
    }
    
    @FunctionalInterface
    public interface OrderTransformer<T> {
        T transform(Order order);
        
        default <R> OrderTransformer<R> andThen(OrderTransformer<T> after) {
            return order -> after.transform(this.transform(order));
        }
    }
    
    @FunctionalInterface
    public interface OrderPredicate {
        boolean test(Order order);
        
        default OrderPredicate and(OrderPredicate other) {
            return order -> this.test(order) && other.test(order);
        }
        
        default OrderPredicate or(OrderPredicate other) {
            return order -> this.test(order) || other.test(order);
        }
        
        default OrderPredicate negate() {
            return order -> !this.test(order);
        }
    }
    
    public record ValidationResult(boolean isValid, String message) {
        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
    }
    
    public static void main(String[] args) {
        // Create test data
        List<Order> orders = createSampleOrders();
        
        // Define validators
        OrderValidator hasItems = order -> 
            !order.items().isEmpty() 
                ? ValidationResult.valid() 
                : ValidationResult.invalid("Order has no items");
        
        OrderValidator hasPositiveTotal = order -> 
            order.totalAmount().compareTo(BigDecimal.ZERO) > 0
                ? ValidationResult.valid() 
                : ValidationResult.invalid("Total must be positive");
        
        OrderValidator isRecent = order ->
            order.createdAt().isAfter(LocalDateTime.now().minusDays(7))
                ? ValidationResult.valid()
                : ValidationResult.invalid("Order is too old");
        
        // Compose validators
        OrderValidator processable = hasItems
            .and(hasPositiveTotal)
            .and(isRecent);
        
        // Define predicates
        OrderPredicate isPending = order -> order.status() == OrderStatus.PENDING;
        OrderPredicate isHighValue = order -> 
            order.totalAmount().compareTo(new BigDecimal("100")) > 0;
        
        OrderPredicate shouldPrioritize = isPending.and(isHighValue);
        
        // Process orders
        System.out.println("=== Order Validation ===");
        orders.forEach(order -> {
            ValidationResult result = processable.validate(order);
            System.out.printf("Order %s: %s%n", 
                order.id(), 
                result.isValid() ? "VALID" : "INVALID: " + result.message());
        });
        
        System.out.println("\n=== Priority Orders ===");
        orders.stream()
            .filter(shouldPrioritize::test)
            .forEach(order -> System.out.println("  " + order.id()));
    }
    
    private static List<Order> createSampleOrders() {
        return List.of(
            new Order("ORD-001", "CUST-001", 
                List.of(new OrderItem("P001", "Laptop", 1, new BigDecimal("999.99"))),
                OrderStatus.PENDING, LocalDateTime.now().minusDays(2), new BigDecimal("999.99")),
            new Order("ORD-002", "CUST-002", 
                List.of(new OrderItem("P002", "Mouse", 2, new BigDecimal("29.99"))),
                OrderStatus.SHIPPED, LocalDateTime.now().minusDays(10), new BigDecimal("59.98")),
            new Order("ORD-003", "CUST-003", List.of(),
                OrderStatus.PENDING, LocalDateTime.now().minusDays(1), BigDecimal.ZERO)
        );
    }
}
```

---

## 15. Performance

### 15.1 Primitive Specialized Interfaces

Using primitive specialized interfaces avoids boxing overhead:

```java
// SLOW: Boxing overhead
Function<Integer, Integer> square = x -> x * x;
IntStream.range(0, 1000000).map(square::apply).sum();

// FAST: No boxing
IntUnaryOperator squarePrimitive = x -> x * x;
IntStream.range(0, 1000000).map(squarePrimitive).sum();
```

### 15.2 Performance Comparison

| Interface | Generic | Primitive | Speedup |
|-----------|---------|-----------|---------|
| Predicate | `Predicate<Integer>` | `IntPredicate` | ~2x |
| Function | `Function<Integer, R>` | `IntFunction<R>` | ~1.5x |
| Consumer | `Consumer<Integer>` | `IntConsumer` | ~2x |
| Supplier | `Supplier<Integer>` | `IntSupplier` | ~1.5x |

### 15.3 Benchmarking

```java
package academy.javaengineering.functional.interfaces;

import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;

public class InterfaceBenchmark {
    
    private static final int ITERATIONS = 100_000_000;
    
    public static void main(String[] args) {
        // Warmup
        for (int i = 0; i < 1_000_000; i++) {
            ((IntUnaryOperator) x -> x * x).applyAsInt(i);
        }
        
        // Benchmark: Generic UnaryOperator
        long start = System.nanoTime();
        UnaryOperator<Integer> genericSquare = x -> x * x;
        int sum1 = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum1 += genericSquare.apply(i);
        }
        long genericTime = System.nanoTime() - start;
        
        // Benchmark: Primitive IntUnaryOperator
        start = System.nanoTime();
        IntUnaryOperator primitiveSquare = x -> x * x;
        int sum2 = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum2 += primitiveSquare.applyAsInt(i);
        }
        long primitiveTime = System.nanoTime() - start;
        
        System.out.printf("Generic UnaryOperator: %.2f ms%n", genericTime / 1_000_000.0);
        System.out.printf("Primitive IntUnaryOperator: %.2f ms%n", primitiveTime / 1_000_000.0);
        System.out.printf("Speedup: %.2fx%n", (double) genericTime / primitiveTime);
    }
}
```

---

## 16. Best Practices

1. **Use @FunctionalInterface annotation**: Provides compile-time validation
2. **Prefer primitive specialized interfaces**: Avoid boxing overhead for performance-critical code
3. **Document side effects**: If a functional interface has side effects, document them
4. **Keep interfaces focused**: One responsibility per functional interface
5. **Use default methods for composition**: Enable chaining and combination
6. **Provide static factory methods**: Make common instances easily accessible
7. **Consider null handling**: Document whether null is accepted or use Optional

---

## 17. Common Mistakes

### Mistake 1: Multiple Abstract Methods

```java
// WRONG: Two abstract methods
@FunctionalInterface
interface Invalid {
    void method1();
    void method2();  // Compilation error!
}

// CORRECT: One abstract method
@FunctionalInterface
interface Valid {
    void method1();
    default void method2() {}  // Default method is OK
}
```

### Mistake 2: Ignoring Boxing Overhead

```java
// WRONG: Boxing overhead in tight loop
Function<Integer, Integer> square = x -> x * x;
IntStream.range(0, 1000000).map(square::apply).sum();

// CORRECT: Use primitive specialized interface
IntUnaryOperator squarePrimitive = x -> x * x;
IntStream.range(0, 1000000).map(squarePrimitive).sum();
```

### Mistake 3: Confusing Function with Consumer

```java
// WRONG: Using Function when you mean Consumer
Function<String, Void> log = s -> {
    System.out.println(s);
    return null;  // Awkward return
};

// CORRECT: Use Consumer for void operations
Consumer<String> log = System.out::println;
```

---

## 18. Pitfalls

1. **Type erasure**: Generic functional interfaces lose type information at runtime
2. **Null handling**: Functional interfaces don't handle null automatically
3. **Serialization**: Lambda implementations of functional interfaces are not serializable by default
4. **Default method conflicts**: Multiple inheritance of default methods can cause ambiguity

---

## 19. Debugging Tips

### 1. Use Named Methods for Complex Logic

```java
// Instead of complex lambda
list.stream()
    .filter(item -> item.getStatus() == Status.ACTIVE && item.getPriority() > 5)
    .toList();

// Extract to named Predicate
Predicate<Item> isHighPriorityActive = this::isHighPriorityActive;
list.stream().filter(isHighPriorityActive).toList();
```

### 2. Add Debug Logging

```java
Predicate<Integer> isPositive = n -> {
    boolean result = n > 0;
    System.out.println("Testing " + n + " > 0: " + result);
    return result;
};
```

### 3. Use peek() for Stream Debugging

```java
list.stream()
    .filter(predicate)
    .peek(item -> System.out.println("After filter: " + item))
    .map(transformer)
    .peek(item -> System.out.println("After map: " + item))
    .toList();
```

---

## 20. Comparison Table

| Feature | Generic Interface | Primitive Interface | Anonymous Class |
|---------|-------------------|---------------------|-----------------|
| **Syntax** | `Predicate<Integer>` | `IntPredicate` | `new Predicate<Integer>() {...}` |
| **Boxing** | Required | None | Required |
| **Performance** | Baseline | ~2x faster | Slower |
| **Memory** | Standard | Standard | More (separate class) |
| **Use Case** | General | Performance-critical | Complex logic |

---

## 21. Decision Tree
