# Topic 01: Introduction to Functional Programming (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)
 | [📖 Continue to Part 4](README-part4.md)

---

    public interface OrderTransformer<T> {
        T transform(Order order);
        default <R> OrderTransformer<R> andThen(OrderTransformer<T> after) {
            return order -> after.transform(this.transform(order));
        }
    }
    
    public static void main(String[] args) {
        // Create test data
        List<Order> orders = createSampleOrders();
        
        // Define validators
        OrderValidator hasItems = order -> !order.items().isEmpty();
        OrderValidator isPending = order -> order.status() == OrderStatus.PENDING;
        OrderValidator isRecent = order -> order.createdAt().isAfter(LocalDateTime.now().minusDays(7));
        
        // Compose validators
        OrderValidator processable = hasItems.and(isPending).and(isRecent);
        
        // Filter and process
        List<Order> processableOrders = orders.stream()
            .filter(processable::validate)
            .toList();
        
        System.out.println("Processable orders: " + processableOrders.size());
    }
    
    private static List<Order> createSampleOrders() {
        return List.of(
            new Order("ORD-001", "CUST-001", 
                List.of(new OrderItem("P001", "Laptop", 1, new BigDecimal("999.99"))),
                OrderStatus.PENDING, LocalDateTime.now().minusDays(2)),
            new Order("ORD-002", "CUST-002", 
                List.of(new OrderItem("P002", "Mouse", 2, new BigDecimal("29.99"))),
                OrderStatus.SHIPPED, LocalDateTime.now().minusDays(10)),
            new Order("ORD-003", "CUST-003", List.of(),
                OrderStatus.PENDING, LocalDateTime.now().minusDays(1))
        );
    }
}
```

### Example 2: Configuration Builder with Immutability

```java
package academy.javaengineering.functional.introduction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ImmutableConfig {
    
    private final Map<String, Object> properties;
    
    private ImmutableConfig(Map<String, Object> properties) {
        this.properties = Map.copyOf(properties);
    }
    
    public <T> T get(String key, Class<T> type) {
        Object value = properties.get(key);
        if (value == null) return null;
        return type.cast(value);
    }
    
    public <T> T getOrDefault(String key, Class<T> type, T defaultValue) {
        T value = get(key, type);
        return value != null ? value : defaultValue;
    }
    
    public ImmutableConfig with(String key, Object value) {
        Map<String, Object> newProps = new HashMap<>(properties);
        newProps.put(key, value);
        return new ImmutableConfig(newProps);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private final Map<String, Object> properties = new HashMap<>();
        
        public <T> Builder set(String key, T value, Class<T> type) {
            properties.put(key, type.cast(value));
            return this;
        }
        
        public Builder setString(String key, String value) {
            properties.put(key, value);
            return this;
        }
        
        public Builder setInt(String key, int value) {
            properties.put(key, value);
            return this;
        }
        
        public ImmutableConfig build() {
            return new ImmutableConfig(properties);
        }
    }
    
    public void forEach(BiConsumer<String, Object> action) {
        properties.forEach(action);
    }
    
    public static void main(String[] args) {
        ImmutableConfig config = ImmutableConfig.builder()
            .setString("database.url", "jdbc:postgresql://localhost:5432/mydb")
            .setString("database.username", "admin")
            .setInt("database.pool.size", 10)
            .setString("app.name", "OrderService")
            .build();
        
        String url = config.get("database.url", String.class);
        int poolSize = config.getOrDefault("database.pool.size", Integer.class, 5);
        
        System.out.println("Database URL: " + url);
        System.out.println("Pool size: " + poolSize);
        
        // Immutability: with() returns new instance
        ImmutableConfig devConfig = config.with("app.name", "OrderService-DEV");
        System.out.println("Original: " + config.get("app.name", String.class));
        System.out.println("Dev: " + devConfig.get("app.name", String.class));
    }
}
```

---

## 15. Performance

### 15.1 Lambda vs Anonymous Class Benchmarks

| Metric | Lambda | Anonymous Class |
|--------|--------|-----------------|
| **Creation Time** | ~2x faster | Baseline |
| **Memory per Instance** | ~16 bytes | ~40 bytes |
| **Class Loading** | No separate class | Separate .class file |
| **JIT Optimization** | Better inlining | Limited inlining |

### 15.2 Performance Tips

1. **Reuse lambda instances**: Store frequently used lambdas in final fields
2. **Avoid autoboxing**: Use primitive specializations (IntStream, etc.)
3. **Prefer method references**: Often generate more optimizable bytecode
4. **Consider lazy evaluation**: Stream operations are lazy by default

### 15.3 Benchmarking Example

```java
package academy.javaengineering.functional.introduction;

import java.util.function.IntBinaryOperator;

public class LambdaPerformance {
    
    // Reused lambda (preferred)
    private static final IntBinaryOperator ADD = (a, b) -> a + b;
    
    public static void main(String[] args) {
        int iterations = 100_000_000;
        
        // Benchmark 1: Method reference
        long start = System.nanoTime();
        int sum1 = 0;
        for (int i = 0; i < iterations; i++) {
            sum1 = ADD.applyAsInt(sum1, i);
        }
        long methodRefTime = System.nanoTime() - start;
        
        // Benchmark 2: Lambda in loop (creates new instance each time)
        start = System.nanoTime();
        int sum2 = 0;
        for (int i = 0; i < iterations; i++) {
            sum2 = ((IntBinaryOperator) (a, b) -> a + b).applyAsInt(sum2, i);
        }
        long lambdaInLoopTime = System.nanoTime() - start;
        
        System.out.printf("Method reference: %.2f ms%n", methodRefTime / 1_000_000.0);
        System.out.printf("Lambda in loop: %.2f ms%n", lambdaInLoopTime / 1_000_000.0);
        System.out.printf("Results match: %b%n", sum1 == sum2);
    }
}
```

---

## 16. Best Practices

1. **Keep lambdas short**: If a lambda exceeds 3-4 lines, extract it to a named method
2. **Use method references**: When a lambda simply calls an existing method
3. **Prefer immutable captures**: Don't capture mutable variables
4. **Name functional interfaces**: Use `@FunctionalInterface` annotation
5. **Document side effects**: If a lambda has side effects, document them
6. **Prefer primitive streams**: Use `IntStream`, `LongStream`, `DoubleStream` for performance
7. **Avoid null in lambdas**: Use `Optional` instead of returning null
8. **Test lambdas independently**: Extract complex lambdas to testable methods

---

## 17. Common Mistakes

### Mistake 1: Mutable Variable Capture

```java
// WRONG: Compiler error - variable must be effectively final
int counter = 0;
list.forEach(item -> counter++);  // Won't compile!

// CORRECT: Use AtomicInteger or collect
AtomicInteger counter = new AtomicInteger(0);
list.forEach(item -> counter.incrementAndGet());
```

### Mistake 2: Overusing Lambdas

```java
// WRONG: Lambda is too complex
list.stream()
    .filter(item -> {
        if (item == null) return false;
        if (item.getStatus() == null) return false;
        if (item.getStatus() == Status.INACTIVE) return false;
        if (item.getCreatedAt().isBefore(LocalDate.now().minusDays(30))) return false;
        return true;
    })
    .toList();

// CORRECT: Extract to a named Predicate
Predicate<Item> isActiveRecentItem = this::isActiveRecent;
list.stream().filter(isActiveRecentItem).toList();
```

### Mistake 3: Ignoring Exception Handling

```java
// WRONG: Unchecked exception handling
list.forEach(item -> riskyOperation(item));

// CORRECT: Handle exceptions explicitly
list.forEach(item -> {
    try {
        riskyOperation(item);
    } catch (Exception e) {
        logger.error("Failed to process item: " + item, e);
    }
});
```

---

## 18. Pitfalls

1. **Performance with large datasets**: Intermediate operations create new Stream objects; avoid creating unnecessary streams
2. **Parallel stream overhead**: Parallel streams use ForkJoinPool; don't use for small datasets
3. **Side effects in lambdas**: Side effects break referential transparency and make code unpredictable
4. **Debugging difficulty**: Stack traces with lambdas can be cryptic; use named methods for complex operations
5. **Memory leaks with captured variables**: Long-lived lambdas can prevent garbage collection of captured objects

---

## 19. Debugging Tips

### 1. Use Named Methods for Complex Logic

```java
// Instead of complex lambda
list.stream()
    .filter(item -> item.getStatus() == Status.ACTIVE && item.getPriority() > 5)
    .toList();

// Extract to named method
Predicate<Item> isHighPriorityActive = this::isHighPriorityActive;
list.stream().filter(isHighPriorityActive).toList();
```

### 2. Add Debug Logging

```java
list.stream()
    .filter(item -> {
        boolean result = item.getPrice() > 100;
        System.out.println("Item " + item.getId() + " > 100: " + result);
        return result;
    })
    .toList();
```

### 3. Use peek() for Stream Debugging

```java
list.stream()
    .filter(item -> item.getPrice() > 100)
    .peek(item -> System.out.println("After filter: " + item))
    .map(Item::getName)
    .peek(name -> System.out.println("After map: " + name))
    .toList();
```

### 4. Enable JVM Lambda Debugging

```bash
java -Djdk.internal.lambdaDumpProxyClasses=true -jar app.jar
```

---

## 20. Comparison Table

| Feature | Imperative | Functional |
|---------|-----------|------------|
| **State** | Mutable | Immutable |
| **Loop** | for/while | Stream operations |
| **Conditionals** | if/else | Predicate composition |
| **Assignment** | Variable mutation | Expression evaluation |
| **Parallelization** | Manual threading | Parallel streams |
| **Testability** | Hard (side effects) | Easy (pure functions) |
| **Readability** | Verbose | Concise |
| **Debugging** | Step through | Harder to trace |

---

## 21. Decision Tree

```
Should you use functional programming in Java?

┌─ Is the operation data transformation?
│  ├─ YES → Use Stream API with lambdas
│  └─ NO → Continue
│
├─ Is the operation filtering/reducing?
│  ├─ YES → Use Stream operations (filter, reduce)
│  └─ NO → Continue
│
├─ Is the logic simple (< 3 lines)?
│  ├─ YES → Use inline lambda
│  └─ NO → Extract to named method
│
├─ Is the operation parallelizable?
│  ├─ YES → Consider parallel streams
│  └─ NO → Use sequential streams
│
└─ Do you need to reuse the logic?
   ├─ YES → Create @FunctionalInterface
   └─ NO → Use inline lambda
```

---

## 22. Interview Questions

### Q1: What is the difference between a lambda and an anonymous class?

**Answer**: Lambdas use `invokedynamic` for efficient implementation without creating a separate class file. Anonymous classes create a separate `.class` file and have more overhead. Lambdas can only be used with functional interfaces (single abstract method).

### Q2: Can lambdas capture mutable variables?

**Answer**: No. Lambdas can only capture **effectively final** variables. This is a design decision to avoid concurrency issues and maintain referential transparency.

### Q3: What is a functional interface?

**Answer**: An interface with exactly one abstract method (SAM - Single Abstract Method). Examples include `Predicate<T>`, `Function<T,R>`, `Consumer<T>`, and `Supplier<T>`. Annotated with `@FunctionalInterface` for compile-time validation.

### Q4: When should you use method references over lambdas?

**Answer**: Use method references when a lambda simply calls an existing method. Method references are more readable and sometimes generate more efficient bytecode. Prefer `ClassName::methodName` over `x -> ClassName.methodName(x)`.

### Q5: What is the performance impact of using functional programming in Java?

**Answer**: Lambdas are generally faster than anonymous classes due to `invokedynamic` optimization. However, creating streams and intermediate operations has overhead. For small datasets, traditional loops may be faster. For large datasets, parallel streams can provide significant speedup.

---

## 23. Exercises

### Exercise 1: Lambda Basics
Convert the following anonymous classes to lambda expressions:

```java
// 1. Comparator
Comparator<String> comp = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return Integer.compare(a.length(), b.length());
    }
};

// 2. ActionListener
ActionListener listener = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked!");
    }
};

// 3. Thread
