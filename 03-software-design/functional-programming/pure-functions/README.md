# Pure Functions

Pure functions always produce the same output for the same input and have no observable side effects. They are the foundation of functional programming and make code predictable, testable, and parallelizable.

## Table of Contents

1. [Concepts](#concepts)
2. [Referential Transparency](#referential-transparency)
3. [Side Effects](#side-effects)
4. [Benefits of Pure Functions](#benefits-of-pure-functions)
5. [Impure to Pure Conversion](#impure-to-pure-conversion)
6. [Testing Pure Functions](#testing-pure-functions)
7. [Functional Core, Imperative Shell](#functional-core-imperative-shell)
8. [Best Practices](#best-practices)
9. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is a Pure Function?

A pure function satisfies two conditions:
1. **Deterministic** - same inputs always produce same outputs
2. **No Side Effects** - doesn't modify external state

```
Pure Function:
  f(2, 3) → 5
  f(2, 3) → 5  (always same result)
  (no external state modified)

Impure Function:
  f(2, 3) → 5  (may depend on global state)
  f(2, 3) → 7  (different result due to external change)
  (modifies external state)
```

### Examples

```java
// Pure function
public int add(int a, int b) {
    return a + b;
}

// Impure function - depends on external state
private int discount = 10;
public double applyDiscount(double price) {
    return price - discount;  // Depends on mutable field
}

// Pure version
public double applyDiscount(double price, double discount) {
    return price - discount;
}
```

---

## Referential Transparency

### What is Referential Transparency?

An expression is referentially transparent if it can be replaced with its value without changing program behavior.

```java
// Referentially transparent
int x = add(2, 3);  // Can replace with: int x = 5;

// Not referentially transparent
int x = counter++;  // Cannot replace with: int x = 0;
// (counter has side effect)

// String operations are referentially transparent
String name = "Hello".toUpperCase();  // Always "HELLO"
```

### Benefits of Referential Transparency

```java
// 1. Memoization is safe
Map<String, String> cache = new HashMap<>();
String process(String input) {
    return cache.computeIfAbsent(input, PureFunctions::expensiveComputation);
}

// 2. Lazy evaluation is safe
LazyValue<Integer> lazy = new LazyValue<>(() -> expensiveComputation());
// Computation deferred until needed

// 3. Parallel execution is safe
List<Integer> results = inputs.parallelStream()
    .map(PureFunctions::process)  // Safe because pure
    .toList();
```

---

## Side Effects

### Common Side Effects

```java
// 1. Modifying global state
private int globalCounter = 0;
public int increment() {
    return globalCounter++;  // Side effect!
}

// 2. Modifying parameters
public void addItem(List<String> items, String item) {
    items.add(item);  // Modifies the list!
}

// 3. I/O operations
public String readConfig(String filename) {
    return Files.readString(Path.of(filename));  // I/O!
}

// 4. Network calls
public User fetchUser(Long id) {
    return httpClient.get("/users/" + id);  // Network!
}

// 5. Throwing exceptions
public int divide(int a, int b) {
    if (b == 0) throw new ArithmeticException();  // Side effect!
}
```

### Isolating Side Effects

```java
// BAD: Mixed pure and impure
public User createUser(String name, String email) {
    User user = new User(name, email);  // Pure
    repository.save(user);              // Impure
    emailService.sendWelcome(email);    // Impure
    return user;                        // Pure
}

// GOOD: Separate pure and impure
// Pure core
public User createUser(String name, String email) {
    return new User(name, email);  // Pure
}

// Impure shell
public User createUserAndNotify(String name, String email) {
    User user = createUser(name, email);  // Pure
    repository.save(user);                // Impure
    emailService.sendWelcome(email);      // Impure
    return user;
}
```

---

## Benefits of Pure Functions

### Testability

```java
// Pure function - easy to test
public int calculateTotal(List<OrderItem> items) {
    return items.stream()
        .mapToInt(item -> item.price() * item.quantity())
        .sum();
}

// Test
@Test
void testCalculateTotal() {
    List<OrderItem> items = List.of(
        new OrderItem("A", 10.0, 2),
        new OrderItem("B", 20.0, 1)
    );
    assertEquals(40, calculateTotal(items));
}
```

### Parallelism

```java
// Pure functions are safe to run in parallel
List<Result> results = inputs.parallelStream()
    .map(PureFunctions::process)  // Safe because pure
    .toList();

// No synchronization needed
// No race conditions
// No deadlocks
```

### Caching

```java
// Memoization for expensive pure computations
public class Memoizer<T, R> {
    private final Map<T, R> cache = new ConcurrentHashMap<>();
    private final Function<T, R> function;

    public Memoizer(Function<T, R> function) {
        this.function = function;
    }

    public R apply(T input) {
        return cache.computeIfAbsent(input, function::apply);
    }
}

// Usage
Function<Integer, BigInteger> fibonacci = Memoizer.memoize(n -> {
    if (n <= 1) return BigInteger.valueOf(n);
    return fibonacci.apply(n - 1).add(fibonacci.apply(n - 2));
});
```

### Reasoning

```java
// Pure function - easy to understand
public boolean isEligibleForDiscount(User user, double orderTotal) {
    return user.isPremium() && orderTotal > 100;
}

// Impure function - harder to reason about
private double discountRate = 0.1;
public boolean isEligibleForDiscount(User user, double orderTotal) {
    return user.isPremium() && orderTotal > 100 && discountRate > 0;
    // What is discountRate? Can it change?
}
```

---

## Impure to Pure Conversion

### Extracting Dependencies

```java
// Impure: depends on global state
private Config config;
public String getDatabaseUrl() {
    return config.getDatabaseUrl();
}

// Pure: pass dependency as parameter
public String getDatabaseUrl(Config config) {
    return config.getDatabaseUrl();
}
```

### Returning New State

```java
// Impure: modifies list
public void addItem(List<String> items, String item) {
    items.add(item);
}

// Pure: returns new list
public List<String> addItem(List<String> items, String item) {
    return Stream.concat(items.stream(), Stream.of(item))
        .toList();
}

// Or with builder
public List<String> addItem(List<String> items, String item) {
    List<String> newItems = new ArrayList<>(items);
    newItems.add(item);
    return List.copyOf(newItems);
}
```

### Making Random Pure

```java
// Impure: depends on external random
public int generateRandomId() {
    return random.nextInt(10000);
}

// Pure: inject random as parameter
public int generateRandomId(Random random) {
    return random.nextInt(10000);
}

// Or use seed for reproducibility
public int generateDeterministicId(int seed) {
    return new Random(seed).nextInt(10000);
}
```

### Making Time Pure

```java
// Impure: depends on current time
public boolean isExpired(ExpirationPolicy policy) {
    return Instant.now().isAfter(policy.expiresAt());
}

// Pure: inject time as parameter
public boolean isExpired(ExpirationPolicy policy, Instant now) {
    return now.isAfter(policy.expiresAt());
}
```

---

## Testing Pure Functions

### Unit Testing

```java
public class PriceCalculator {
    // Pure function
    public static Money calculateTotal(List<CartItem> items, Discount discount) {
        BigDecimal subtotal = items.stream()
            .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal discountAmount = subtotal.multiply(discount.percentage());
        return new Money(subtotal.subtract(discountAmount), Currency.USD);
    }
}

// Tests - no mocking needed
@Test
void testCalculateTotalWithNoDiscount() {
    List<CartItem> items = List.of(
        new CartItem("A", Money.of(10), 2),
        new CartItem("B", Money.of(20), 1)
    );
    Money total = PriceCalculator.calculateTotal(items, Discount.NONE);
    assertEquals(Money.of(40), total);
}

@Test
void testCalculateTotalWithDiscount() {
    List<CartItem> items = List.of(
        new CartItem("A", Money.of(10), 2),
        new CartItem("B", Money.of(20), 1)
    );
    Money total = PriceCalculator.calculateTotal(items, new Discount(0.1));
    assertEquals(Money.of(36), total);
}
```

### Property-Based Testing

```java
// Test properties that should always hold
@Test
void testAdditionIsCommutative() {
    forAll(integers(), integers(), (a, b) -> 
        assertEquals(add(a, b), add(b, a))
    );
}

@Test
void testAddingZeroIsIdentity() {
    forAll(integers(), n -> 
        assertEquals(n, add(n, 0))
    );
}

@Test
void testMultiplicationByOneIsIdentity() {
    forAll(integers(), n -> 
        assertEquals(n, multiply(n, 1))
    );
}
```

---

## Functional Core, Imperative Shell

### Architecture Pattern

```java
// IMPERATIVE SHELL - handles side effects
public class OrderService {
    private final OrderRepository repository;
    private final PaymentGateway gateway;
    private final EmailService emailService;

    public OrderConfirmation placeOrder(OrderRequest request) {
        // Call pure core
        Order order = OrderCore.createOrder(request);
        Money total = OrderCore.calculateTotal(order);
        ValidationResult validation = OrderCore.validate(order, total);

        // Handle side effects based on pure result
        if (!validation.isValid()) {
            return OrderConfirmation.rejected(validation.reason());
        }

        PaymentResult payment = gateway.charge(total);
        if (payment.failed()) {
            return OrderConfirmation.paymentFailed(payment.error());
        }

        repository.save(order);
        emailService.sendConfirmation(order);
        return OrderConfirmation.success(order.id());
    }
}

// PURE CORE - all logic is pure
public final class OrderCore {
    private OrderCore() {}

    public static Order createOrder(OrderRequest request) {
        return new Order(
            UUID.randomUUID().toString(),
            request.items(),
            request.customerId(),
            Instant.now()
        );
    }

    public static Money calculateTotal(Order order) {
        return order.items().stream()
            .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(Money.ZERO, Money::add);
    }

    public static ValidationResult validate(Order order, Money total) {
        if (order.items().isEmpty()) {
            return ValidationResult.invalid("Order must have items");
        }
        if (total.isNegative()) {
            return ValidationResult.invalid("Total cannot be negative");
        }
        return ValidationResult.valid();
    }
}
```

---

## Best Practices

### Do

```java
// 1. Keep functions pure when possible
public static int add(int a, int b) {
    return a + b;
}

// 2. Pass dependencies as parameters
public static String formatUser(User user, Locale locale) {
    return MessageFormat.format("{0} ({1})", user.name(), locale);
}

// 3. Return new values instead of modifying
public static List<String> sort(List<String> items) {
    return items.stream().sorted().toList();
}

// 4. Isolate side effects at the boundaries
public User createUser(String name, String email) {
    // Pure validation
    validateName(name);
    validateEmail(email);
    
    // Impure side effects
    User user = new User(name, email);
    repository.save(user);
    return user;
}
```

### Don't

```java
// 1. Don't modify input parameters
public void process(List<String> items) {
    items.add("new");  // BAD - modifies input
}

// 2. Don't depend on external state
private int counter = 0;
public int getNextId() {
    return counter++;  // BAD - depends on and modifies state
}

// 3. Don't mix pure and impure logic
public User processUser(String name) {
    User user = createPure(name);   // Pure
    saveToDb(user);                 // Impure
    return user;                    // Pure
}
// This function is impure overall!
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Pure Function** | Same input → same output, no side effects |
| **Referential Transparency** | Expression can be replaced with its value |
| **Side Effects** | I/O, mutation, exceptions, randomness |
| **Testability** | No mocking needed for pure functions |
| **Parallelism** | Safe to run concurrently |
| **Memoization** | Cache results of pure computations |
| **Functional Core** | Business logic in pure functions |
| **Imperative Shell** | Side effects at system boundaries |
| **Conversion** | Extract dependencies, return new state |
| **Benefits** | Predictable, testable, parallelizable code |
