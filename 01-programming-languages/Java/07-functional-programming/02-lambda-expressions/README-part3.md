# Topic 02: Lambda Expressions (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)
 | [📖 Continue to Part 4](README-part4.md)

---

    // Y-combinator: enables recursion in lambda expressions
    @SuppressWarnings("unchecked")
    public static <T, R> Function<T, R> y(UnaryOperatorWithSelf<Function<T, R>> f) {
        return (Function<T, R>) new Object() {
            Function<T, R> func = arg -> f.apply(this.func, arg);
        }.func;
    }
    
    public static void main(String[] args) {
        // Factorial using Y-combinator
        Function<Integer, Integer> factorial = y(
            (self, n) -> n <= 1 ? 1 : n * self.apply(n - 1)
        );
        
        // Fibonacci using Y-combinator
        Function<Integer, Integer> fibonacci = y(
            (self, n) -> n <= 1 ? n : self.apply(n - 1) + self.apply(n - 2)
        );
        
        System.out.println("Factorials:");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("  %d! = %d%n", i, factorial.apply(i));
        }
        
        System.out.println("\nFibonacci sequence:");
        for (int i = 0; i <= 15; i++) {
            System.out.printf("  F(%d) = %d%n", i, fibonacci.apply(i));
        }
    }
}
```

### Example 3: Stateful Lambda with Thread Safety

```java
package academy.javaengineering.functional.lambda;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class StatefulLambdas {
    
    // Thread-safe counter using AtomicInteger
    public static class AtomicCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        
        public IntUnaryOperator incrementer() {
            return count::incrementAndGet;
        }
        
        public IntUnaryOperator adder(int value) {
            return x -> count.addAndGet(value);
        }
        
        public int get() {
            return count.get();
        }
    }
    
    // Accumulator using functional interface
    @FunctionalInterface
    public interface Accumulator<T> {
        T accumulate(T current, T value);
        
        default Accumulator<T> andThen(Accumulator<T> after) {
            return (current, value) -> after.accumulate(this.accumulate(current, value), value);
        }
    }
    
    public static void main(String[] args) {
        // Atomic counter example
        AtomicCounter counter = new AtomicCounter();
        IntUnaryOperator increment = counter.incrementer();
        
        System.out.println("Counter: " + increment.applyAsInt(0));
        System.out.println("Counter: " + increment.applyAsInt(0));
        System.out.println("Counter: " + increment.applyAsInt(0));
        
        // Accumulator example
        Accumulator<Integer> sumAccumulator = Integer::sum;
        Accumulator<Integer> productAccumulator = (a, b) -> a * b;
        
        // Chain accumulators
        Accumulator<Integer> combined = sumAccumulator.andThen(productAccumulator);
        
        int result = 0;
        result = combined.accumulate(result, 5);  // (0 + 5) * 5 = 25
        result = combined.accumulate(result, 3);  // (25 + 3) * 3 = 84
        System.out.println("Combined accumulator result: " + result);
    }
}
```

---

## 14. Enterprise Example

### Example 1: Event-Driven Architecture with Lambdas

```java
package academy.javaengineering.functional.lambda;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventSystem {
    
    @FunctionalInterface
    public interface EventListener<T> {
        void onEvent(T event);
    }
    
    @FunctionalInterface
    public interface EventFilter<T> {
        boolean shouldHandle(T event);
    }
    
    public static class EventPublisher<T> {
        private final Map<String, List<EventListener<T>>> listeners = new ConcurrentHashMap<>();
        private final Map<String, List<EventFilter<T>>> filters = new ConcurrentHashMap<>();
        
        public void subscribe(String eventType, EventListener<T> listener) {
            listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
        }
        
        public void addFilter(String eventType, EventFilter<T> filter) {
            filters.computeIfAbsent(eventType, k -> new ArrayList<>()).add(filter);
        }
        
        public void publish(String eventType, T event) {
            List<EventFilter<T>> eventFilters = filters.getOrDefault(eventType, List.of());
            boolean shouldHandle = eventFilters.stream()
                .allMatch(filter -> filter.shouldHandle(event));
            
            if (shouldHandle) {
                List<EventListener<T>> eventListeners = listeners.getOrDefault(eventType, List.of());
                eventListeners.forEach(listener -> listener.onEvent(event));
            }
        }
    }
    
    // Event types
    public record UserCreatedEvent(String userId, String username, String email) {}
    public record OrderPlacedEvent(String orderId, String userId, double amount) {}
    
    public static void main(String[] args) {
        // Create event publisher
        EventPublisher<Object> publisher = new EventPublisher<>();
        
        // Subscribe to events with lambdas
        publisher.subscribe("UserCreated", event -> {
            UserCreatedEvent e = (UserCreatedEvent) event;
            System.out.println("Welcome email sent to: " + e.email());
        });
        
        publisher.subscribe("UserCreated", event -> {
            UserCreatedEvent e = (UserCreatedEvent) event;
            System.out.println("User created in database: " + e.username());
        });
        
        publisher.subscribe("OrderPlaced", event -> {
            OrderPlacedEvent e = (OrderPlacedEvent) event;
            System.out.println("Order processed: " + e.orderId() + " - $" + e.amount());
        });
        
        // Add filter
        publisher.addFilter("OrderPlaced", event -> {
            OrderPlacedEvent e = (OrderPlacedEvent) event;
            return e.amount() > 0;
        });
        
        // Publish events
        publisher.publish("UserCreated", new UserCreatedEvent("U001", "alice", "alice@example.com"));
        publisher.publish("OrderPlaced", new OrderPlacedEvent("ORD001", "U001", 99.99));
        publisher.publish("OrderPlaced", new OrderPlacedEvent("ORD002", "U001", -10.00)); // Filtered
    }
}
```

### Example 2: Configuration System with Lambda Validation

```java
package academy.javaengineering.functional.lambda;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConfigSystem {
    
    @FunctionalInterface
    public interface ConfigValidator<T> {
        ValidationResult validate(T value);
        
        default ConfigValidator<T> and(ConfigValidator<T> other) {
            return value -> {
                ValidationResult result = this.validate(value);
                if (!result.isValid()) return result;
                return other.validate(value);
            };
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
    
    public static class ConfigValue<T> {
        private final String key;
        private final T value;
        private final List<ConfigValidator<T>> validators;
        
        private ConfigValue(String key, T value, List<ConfigValidator<T>> validators) {
            this.key = key;
            this.value = value;
            this.validators = validators;
        }
        
        public ValidationResult validate() {
            for (ConfigValidator<T> validator : validators) {
                ValidationResult result = validator.validate(value);
                if (!result.isValid()) return result;
            }
            return ValidationResult.valid();
        }
        
        public T getValue() { return value; }
        public String getKey() { return key; }
        
        public static <T> Builder<T> builder(String key) {
            return new Builder<>(key);
        }
        
        public static class Builder<T> {
            private final String key;
            private T value;
            private final List<ConfigValidator<T>> validators = new ArrayList<>();
            
            Builder(String key) { this.key = key; }
            
            public Builder<T> value(T value) {
                this.value = value;
                return this;
            }
            
            public Builder<T> validate(ConfigValidator<T> validator) {
                this.validators.add(validator);
                return this;
            }
            
            public Builder<T> required() {
                this.validators.add(v -> v != null 
                    ? ValidationResult.valid() 
                    : ValidationResult.invalid(key + " is required"));
                return this;
            }
            
            public ConfigValue<T> build() {
                return new ConfigValue<>(key, value, validators);
            }
        }
    }
    
    public static void main(String[] args) {
        // Create config values with validators
        ConfigValue<String> dbUrl = ConfigValue.<String>builder("database.url")
            .value("jdbc:postgresql://localhost:5432/mydb")
            .required()
            .validate(url -> url.startsWith("jdbc:") 
                ? ValidationResult.valid() 
                : ValidationResult.invalid("Invalid JDBC URL"))
            .build();
        
        ConfigValue<Integer> poolSize = ConfigValue.<Integer>builder("database.pool.size")
            .value(10)
            .required()
            .validate(size -> size > 0 && size <= 100 
                ? ValidationResult.valid() 
                : ValidationResult.invalid("Pool size must be 1-100"))
            .build();
        
        ConfigValue<String> appName = ConfigValue.<String>builder("app.name")
            .value("OrderService")
            .required()
            .validate(name -> name.length() >= 3 
                ? ValidationResult.valid() 
                : ValidationResult.invalid("App name must be at least 3 characters"))
            .build();
        
        // Validate all configs
        List<ConfigValue<?>> configs = List.of(dbUrl, poolSize, appName);
        
        System.out.println("Configuration validation:");
        configs.forEach(config -> {
            ValidationResult result = config.validate();
            String status = result.isValid() ? "VALID" : "INVALID: " + result.message();
            System.out.printf("  %s: %s%n", config.getKey(), status);
        });
    }
}
```

---

## 15. Performance

### 15.1 Lambda Performance Characteristics

| Metric | Lambda | Anonymous Class | Improvement |
|--------|--------|-----------------|-------------|
| **Creation Time** | ~100ns | ~200ns | 2x faster |
| **Memory per Instance** | ~16 bytes | ~40 bytes | 60% less |
| **Call Overhead** | ~5ns | ~10ns | 2x faster |
| **JIT Optimization** | Excellent | Good | Better inlining |

### 15.2 Performance Best Practices

```java
// PREFERRED: Reuse lambda instances
private static final Function<String, Integer> TO_LENGTH = String::length;

// LESS PREFERRED: Create new instance each time
public int getLength(String s) {
    return ((Function<String, Integer>) String::length).apply(s);
}

// PREFERRED: Use primitive streams
int sum = IntStream.range(0, 1000).sum();

// LESS PREFERRED: Boxing overhead
int sum = Stream.iterate(0, i -> i + 1).limit(1000).mapToInt(Integer::intValue).sum();
```

### 15.3 Benchmarking

```java
package academy.javaengineering.functional.lambda;

import java.util.function.IntBinaryOperator;

public class LambdaBenchmark {
    
    private static final IntBinaryOperator ADD = (a, b) -> a + b;
    private static final int ITERATIONS = 100_000_000;
    
    public static void main(String[] args) {
        // Warmup
        for (int i = 0; i < 10_000_000; i++) {
            ADD.applyAsInt(0, i);
        }
        
        // Benchmark: Reused lambda
        long start = System.nanoTime();
        int sum1 = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum1 = ADD.applyAsInt(sum1, i);
        }
        long reusedTime = System.nanoTime() - start;
        
        // Benchmark: New lambda each time
        start = System.nanoTime();
        int sum2 = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            IntBinaryOperator newLambda = (a, b) -> a + b;
            sum2 = newLambda.applyAsInt(sum2, i);
        }
        long newTime = System.nanoTime() - start;
        
        System.out.printf("Reused lambda: %.2f ms%n", reusedTime / 1_000_000.0);
        System.out.printf("New lambda: %.2f ms%n", newTime / 1_000_000.0);
        System.out.printf("Speedup: %.2fx%n", (double) newTime / reusedTime);
    }
}
```

---

## 16. Best Practices

1. **Keep lambdas short**: If a lambda exceeds 3-4 lines, extract it to a named method
2. **Use method references**: When a lambda simply calls an existing method
3. **Prefer effectively final captures**: Don't capture mutable variables
4. **Document side effects**: If a lambda has side effects, document them
5. **Use var parameters (Java 11+)**: For clarity when types are obvious
6. **Test lambdas independently**: Extract complex lambdas to testable methods
7. **Reuse frequently used lambdas**: Store in static final fields
8. **Avoid complex nesting**: Deeply nested lambdas are hard to read

---

## 17. Common Mistakes

