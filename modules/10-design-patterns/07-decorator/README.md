# Decorator Pattern

## 1. Introduction

The Decorator Pattern is a structural design pattern that lets you attach new behaviors to objects by placing these objects inside special wrapper objects that contain the behaviors. It provides an alternative to subclassing for extending functionality.

The Decorator pattern is particularly useful when you need to add responsibilities to individual objects dynamically and transparently, without affecting other objects.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement the Decorator pattern with Java I/O streams analogy
- Stack multiple decorators for complex behavior
- Understand when to use decorators over inheritance
- Recognize decorator usage in Java standard library
- Handle decorator ordering and composition

---

## 3. Prerequisites

- Understanding of interfaces and abstract classes
- Knowledge of composition over inheritance
- Familiarity with Java I/O streams
- Understanding of polymorphism

---

## 4. Why This Concept Exists

The Decorator pattern exists because:

- **Dynamic behavior**: Add/remove behavior at runtime
- **Flexible alternative to inheritance**: Avoid class explosion
- **Single Responsibility**: Each decorator has one job
- **Open/Closed Principle**: Extend behavior without modifying code
- **Composable**: Stack multiple decorators

Without Decorator, you'd create many subclasses for every combination of behaviors.

---

## 5. Problem Statement

Consider a coffee shop:

```java
// BAD: Subclass explosion
public class Coffee { }
public class CoffeeWithMilk extends Coffee { }
public class CoffeeWithSugar extends Coffee { }
public class CoffeeWithMilkAndSugar extends Coffee { }
public class CoffeeWithMilkAndSugarAndWhippedCream extends Coffee { }
// ... hundreds more combinations

// Or using flags
public class Coffee {
    boolean hasMilk;
    boolean hasSugar;
    boolean hasWhippedCream;
    // ... more flags
}
```

**Problems:**
1. **Class explosion**: 2^n combinations need 2^n classes
2. **Rigid design**: Hard to modify at runtime
3. **Violation of SRP**: Coffee class handles all variations
4. **Code duplication**: Similar code in many subclasses

---

## 6. Theory

### 6.1 Decorator vs. Inheritance

| Aspect | Decorator | Inheritance |
|--------|-----------|-------------|
| Behavior | Dynamic | Static |
| Flexibility | Runtime | Compile-time |
| Coupling | Loose | Tight |
| Complexity | Many small classes | Many subclasses |

### 6.2 Decorator Structure

1. **Component**: Interface for objects that can have responsibilities added
2. **ConcreteComponent**: The object being decorated
3. **Decorator**: Abstract class implementing Component, holds Component reference
4. **ConcreteDecorator**: Adds responsibilities to Component

### 6.3 Java I/O Streams Analogy

Java I/O uses Decorator pattern extensively:
```java
InputStream is = new BufferedInputStream(
    new FileInputStream(
        new File("data.txt")
    )
);
```

---

## 7. Internal Working

### 7.1 Decorator Flow

```
Client → Component interface → Decorator → ConcreteComponent
                                  ↓
                          Wraps and delegates
                                  ↓
                          Adds behavior before/after
```

### 7.2 Method Call Chain

```
decorated.method()
  → decorator.method()
    → pre-processing
    → component.method()
    → post-processing
```

---

## 8. JVM Perspective

### 8.1 Method Dispatch

- Virtual method table chains through decorator stack
- Each decorator holds reference to wrapped component
- Polymorphism ensures correct method called

### 8.2 Memory Layout

- Each decorator adds one object to heap
- Deep decorator stacks consume more memory
- JIT can optimize method inlining

---

## 9. Memory Representation

### 9.1 Decorator Chain Memory

```
┌─────────────────────────────────────┐
│             Client                  │
└──────────────┬──────────────────────┘
               │ references
               ↓
┌─────────────────────────────────────┐
│      ConcreteDecorator2             │
│  - component: Decorator1            │
└──────────────┬──────────────────────┘
               │ wraps
               ↓
┌─────────────────────────────────────┐
│      ConcreteDecorator1             │
│  - component: ConcreteComponent     │
└──────────────┬──────────────────────┘
               │ wraps
               ↓
┌─────────────────────────────────────┐
│      ConcreteComponent              │
│  (base implementation)              │
└─────────────────────────────────────┘
```

---

## 10. Syntax

### 10.1 Basic Decorator Structure

```java
public interface Component {
    void operation();
}

public class ConcreteComponent implements Component {
    @Override
    public void operation() {
        // Base behavior
    }
}

public abstract class Decorator implements Component {
    protected final Component component;

    protected Decorator(Component component) {
        this.component = component;
    }

    @Override
    public void operation() {
        component.operation();
    }
}

public class ConcreteDecorator extends Decorator {
    public ConcreteDecorator(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        // Add behavior before
        super.operation();
        // Add behavior after
    }
}
```

---

## 11. Easy Example

### Coffee Shop

```java
// Component interface
public interface Coffee {
    double getCost();
    String getDescription();
}

// Concrete component
public class SimpleCoffee implements Coffee {
    @Override
    public double getCost() {
        return 5.00;
    }

    @Override
    public String getDescription() {
        return "Simple coffee";
    }
}

// Base decorator
public abstract class CoffeeDecorator implements Coffee {
    protected final Coffee coffee;

    protected CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public double getCost() {
        return coffee.getCost();
    }

    @Override
    public String getDescription() {
        return coffee.getDescription();
    }
}

// Concrete decorators
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public double getCost() {
        return super.getCost() + 1.50;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", milk";
    }
}

public class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.75;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", sugar";
    }
}

// Usage
Coffee coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
coffee = new SugarDecorator(coffee);

System.out.println(coffee.getDescription()); // "Simple coffee, milk, sugar"
System.out.println(coffee.getCost()); // 7.25
```

---

## 12. Medium Example

### Text Formatter Decorator

```java
public interface TextFormatter {
    String format(String text);
}

public class PlainText implements TextFormatter {
    @Override
    public String format(String text) {
        return text;
    }
}

public abstract class TextDecorator implements TextFormatter {
    protected final TextFormatter formatter;

    protected TextDecorator(TextFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public String format(String text) {
        return formatter.format(text);
    }
}

public class UpperCaseDecorator extends TextDecorator {
    public UpperCaseDecorator(TextFormatter formatter) {
        super(formatter);
    }

    @Override
    public String format(String text) {
        return super.format(text).toUpperCase();
    }
}

public class TrimDecorator extends TextDecorator {
    public TrimDecorator(TextFormatter formatter) {
        super(formatter);
    }

    @Override
    public String format(String text) {
        return super.format(text).trim();
    }
}

public class ReplaceDecorator extends TextFormatter {
    private final String target;
    private final String replacement;

    public ReplaceDecorator(TextFormatter formatter, String target, String replacement) {
        super(formatter);
        this.target = target;
        this.replacement = replacement;
    }

    @Override
    public String format(String text) {
        return super.format(text).replace(target, replacement);
    }
}

// Usage
TextFormatter formatter = new ReplaceDecorator(
    new UpperCaseDecorator(
        new TrimDecorator(
            new PlainText()
        )
    ),
    " ", "_"
);

String result = formatter.format("  Hello World  ");
System.out.println(result); // "HELLO_WORLD"
```

---

## 13. Hard Example

### Thread-Safe Decorator with Caching

```java
public interface DataFetcher {
    String fetchData(String key);
}

public class HttpDataFetcher implements DataFetcher {
    @Override
    public String fetchData(String key) {
        // Simulate HTTP call
        return "Data for " + key;
    }
}

public abstract class DataFetcherDecorator implements DataFetcher {
    protected final DataFetcher fetcher;

    protected DataFetcherDecorator(DataFetcher fetcher) {
        this.fetcher = fetcher;
    }
}

public class CachingDecorator extends DataFetcherDecorator {
    private final Map<String, String> cache = new ConcurrentHashMap<>();
    private final Duration ttl;
    private final Map<String, Instant> timestamps = new ConcurrentHashMap<>();

    public CachingDecorator(DataFetcher fetcher, Duration ttl) {
        super(fetcher);
        this.ttl = ttl;
    }

    @Override
    public String fetchData(String key) {
        if (isCached(key)) {
            return cache.get(key);
        }

        String data = super.fetchData(key);
        cache.put(key, data);
        timestamps.put(key, Instant.now());
        return data;
    }

    private boolean isCached(String key) {
        if (!cache.containsKey(key)) {
            return false;
        }
        Instant cachedAt = timestamps.get(key);
        return Instant.now().isBefore(cachedAt.plus(ttl));
    }
}

public class LoggingDecorator extends DataFetcherDecorator {
    private static final Logger logger = Logger.getLogger(LoggingDecorator.class.getName());

    public LoggingDecorator(DataFetcher fetcher) {
        super(fetcher);
    }

    @Override
    public String fetchData(String key) {
        logger.info("Fetching data for key: " + key);
        Instant start = Instant.now();

        String data = super.fetchData(key);

        Duration duration = Duration.between(start, Instant.now());
        logger.info("Fetched data in " + duration.toMillis() + "ms");
        return data;
    }
}

public class RetryDecorator extends DataFetcherDecorator {
    private final int maxRetries;
    private final Duration delay;

    public RetryDecorator(DataFetcher fetcher, int maxRetries, Duration delay) {
        super(fetcher);
        this.maxRetries = maxRetries;
        this.delay = delay;
    }

    @Override
    public String fetchData(String key) {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                return super.fetchData(key);
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxRetries) {
                    throw new RuntimeException("Failed after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep(delay.toMillis());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted during retry", ie);
                }
            }
        }
        throw new RuntimeException("Failed after " + maxRetries + " attempts");
    }
}

// Usage - Stack decorators
DataFetcher fetcher = new RetryDecorator(
    new LoggingDecorator(
        new CachingDecorator(
            new HttpDataFetcher(),
            Duration.ofMinutes(5)
        )
    ),
    3,
    Duration.ofSeconds(1)
);

String data = fetcher.fetchData("user-123");
```

---

## 14. Enterprise Example

### HTTP Request/Response Decorator Chain

```java
public interface HttpClient {
    HttpResponse execute(HttpRequest request) throws IOException;
}

public class SimpleHttpClient implements HttpClient {
    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        // Actual HTTP execution
        return new HttpResponse(200, "OK");
    }
}

public abstract class HttpClientDecorator implements HttpClient {
    protected final HttpClient client;

    protected HttpClientDecorator(HttpClient client) {
        this.client = client;
    }
}

public class LoggingHttpClient extends HttpClientDecorator {
    private static final Logger logger = Logger.getLogger(LoggingHttpClient.class.getName());

    public LoggingHttpClient(HttpClient client) {
        super(client);
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        logger.info("Request: " + request.getMethod() + " " + request.getUrl());
        Instant start = Instant.now();

        HttpResponse response = client.execute(request);

        Duration duration = Duration.between(start, Instant.now());
        logger.info("Response: " + response.getStatusCode() + " in " + duration.toMillis() + "ms");
        return response;
    }
}

public class AuthenticationHttpClient extends HttpClientDecorator {
    private final String token;

    public AuthenticationHttpClient(HttpClient client, String token) {
        super(client);
        this.token = token;
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        HttpRequest authenticatedRequest = HttpRequest.newBuilder()
            .uri(request.getUri())
            .header("Authorization", "Bearer " + token)
            .build();
        return client.execute(authenticatedRequest);
    }
}

public class CachingHttpClient extends HttpClientDecorator {
    private final Map<String, HttpResponse> cache = new ConcurrentHashMap<>();
    private final Duration ttl;
    private final Map<String, Instant> timestamps = new ConcurrentHashMap<>();

    public CachingHttpClient(HttpClient client, Duration ttl) {
        super(client);
        this.ttl = ttl;
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        String cacheKey = request.getMethod() + ":" + request.getUrl();

        if (cache.containsKey(cacheKey) && !isExpired(cacheKey)) {
            return cache.get(cacheKey);
        }

        HttpResponse response = client.execute(request);
        cache.put(cacheKey, response);
        timestamps.put(cacheKey, Instant.now());
        return response;
    }

    private boolean isExpired(String key) {
        return Instant.now().isAfter(timestamps.get(key).plus(ttl));
    }
}

public class RateLimitHttpClient extends HttpClientDecorator {
    private final RateLimiter rateLimiter;

    public RateLimitHttpClient(HttpClient client, RateLimiter rateLimiter) {
        super(client);
        this.rateLimiter = rateLimiter;
    }

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        if (!rateLimiter.tryAcquire()) {
            throw new IOException("Rate limit exceeded");
        }
        return client.execute(request);
    }
}

// Usage - Build decorator chain
HttpClient client = new RateLimitHttpClient(
    new CachingHttpClient(
        new AuthenticationHttpClient(
            new LoggingHttpClient(
                new SimpleHttpClient()
            ),
            "bearer-token"
        ),
        Duration.ofMinutes(5)
    ),
    RateLimiter.create(100) // 100 requests per second
);

HttpResponse response = client.execute(
    HttpRequest.newBuilder()
        .uri(URI.create("https://api.example.com/data"))
        .build()
);
```

---

## 15. Performance

### 15.1 Performance Metrics

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Method delegation | O(1) | Per decorator |
| Total call | O(d) | d = decorator depth |
| Memory per decorator | O(1) | Object + reference |

### 15.2 Optimization Tips

1. **Limit decorator depth**: Deep stacks impact performance
2. **Cache at appropriate level**: Cache near the top
3. **Lazy decoration**: Only decorate when needed
4. **Profile decorator chains**: Measure actual overhead

---

## 16. Best Practices

1. **Keep decorators small**: One responsibility per decorator
2. **Preserve interface**: Decorators must implement same interface
3. **Order matters**: Consider decorator ordering carefully
4. **Document decorator chain**: Clear documentation of behavior
5. **Use base decorator**: Abstract class for common logic
6. **Consider alternative patterns**: Proxy, Chain of Responsibility
7. **Test each decorator**: Unit test in isolation
8. **Limit nesting depth**: Performance and readability

---

## 17. Common Mistakes

1. **Too many decorators**: Hard to understand and debug
2. **Wrong ordering**: Behavior depends on decorator order
3. **Breaking interface**: Decorator doesn't implement component interface
4. **Heavy decorators**: Decorators with too much logic
5. **Not delegating**: Forgetting to call wrapped component's method

---

## 18. Pitfalls

- **Complexity**: Many small classes
- **Debugging difficulty**: Hard to trace method calls
- **Order sensitivity**: Behavior depends on decorator order
- **Performance overhead**: Each decorator adds indirection
- **Maintenance burden**: Many files to maintain

---

## 19. Debugging Tips

1. **Add logging**: Track decorator chain execution
2. **Use debugger**: Step through decorator stack
3. **Visualize chain**: Draw decorator relationships
4. **Test in isolation**: Unit test each decorator
5. **Document ordering**: Keep documentation updated

---

## 20. Comparison Table

| Pattern | Purpose | Interface Change | Dynamic |
|---------|---------|------------------|---------|
| Decorator | Add behavior | No | Yes |
| Proxy | Control access | No | No |
| Adapter | Convert interface | Yes | No |
| Chain of Responsibility | Pass request | No | Yes |

---

## 21. Decision Tree

```
Need to add behavior?
├── Dynamic at runtime? → Decorator
├── Control access? → Proxy
├── Multiple handlers? → Chain of Responsibility
├── Convert interface? → Adapter
└── Static behavior? → Inheritance
```

---

## 22. Interview Questions

### Q1: What is the Decorator pattern?
**Answer**: A structural pattern that allows adding behaviors to objects dynamically by wrapping them in decorator objects.

### Q2: Decorator vs. Inheritance?
**Answer**: Decorator adds behavior dynamically at runtime. Inheritance adds behavior statically at compile-time. Decorator is more flexible.

### Q3: Can decorators be stacked?
**Yes**: Decorators can be stacked to add multiple behaviors. Order matters.

### Q4: What is the Java I/O example?
**Answer**: Java I/O uses decorators: BufferedInputStream wraps FileInputStream, DataInputStream wraps BufferedInputStream.

### Q5: When NOT to use Decorator?
**Answer**: When behavior is fixed at compile-time, when inheritance is simpler, or when decorator chain becomes too deep.

---

## 23. Exercises

### Exercise 1: Simple Decorator
Create decorators for a notification service (logging, retry, rate limiting).

### Exercise 2: Data Stream Decorator
Create decorators for data processing (filter, transform, aggregate).

### Exercise 3: HTTP Client Decorator
Create a full HTTP client decorator chain (auth, logging, caching, retry).

---

## 24. Assignments

1. **Assignment 1**: Create a decorator pattern for a shape drawing library
2. **Assignment 2**: Build decorators for a file reader (buffering, encryption, compression)
3. **Assignment 3**: Create a decorator chain for order processing (validation, discount, tax)

---

## 25. Mini Project

### Plugin System with Decorators
Create a plugin system that:
- Allows dynamic behavior addition
- Supports decorator chaining
- Each decorator has single responsibility
- Is configurable at runtime
- Handles plugin ordering

---

## 26. Summary

- Decorator adds behavior dynamically
- Alternative to subclassing for extending functionality
- Stack decorators for complex behavior
- Keep decorators small and focused
- Consider decorator ordering carefully
- Java I/O streams are a classic example

---

## 27. References

1. Gamma, E., et al. (1994). *Design Patterns*, Chapter 5
2. Bloch, J. (2018). *Effective Java*, Item 18
3. Refactoring Guru: https://refactoring.guru/design-patterns/decorator
4. Java Design Patterns: https://java-design-patterns.com/patterns/decorator/
