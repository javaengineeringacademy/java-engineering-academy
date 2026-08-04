# Immutability

Immutability ensures objects cannot be modified after creation, eliminating a class of bugs related to shared mutable state. Immutable objects are inherently thread-safe and easier to reason about.

## Table of Contents

1. [Concepts](#concepts)
2. [Immutable Objects](#immutable-objects)
3. [Records](#records)
4. [Unmodifiable Collections](#unmodifiable-collections)
5. [Defensive Copying](#defensive-copying)
6. [Builder Pattern for Immutables](#builder-pattern-for-immutables)
7. [Performance Considerations](#performance-considerations)
8. [Best Practices](#best-practices)
9. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Immutability?

An immutable object's state cannot change after it is fully constructed. Any operation that appears to modify the object actually creates a new instance.

```
Mutable Object:
  obj.setValue("A")  →  obj internal state = "A"
  obj.setValue("B")  →  obj internal state = "B"  (changed!)

Immutable Object:
  obj1 = new Value("A")  →  obj1.state = "A"
  obj2 = obj1.withValue("B")  →  obj2.state = "B", obj1.state = "A" (unchanged!)
```

### Benefits

- **Thread Safety** - no synchronization needed
- **Predictability** - no unexpected state changes
- **Cacheability** - safe to share and reuse
- **Value Semantics** - equals/hashCode are stable
- **Referential Transparency** - same inputs always produce same outputs

### Trade-offs

- **Memory** - new objects for each modification
- **Performance** - may be slower for frequent updates
- **Complexity** - need defensive copying for mutable fields

---

## Immutable Objects

### Basic Immutable Class

```java
public final class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
    }

    // Only getters, no setters
    public BigDecimal amount() { return amount; }
    public Currency currency() { return currency; }

    // Operations return new instances
    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(amount.add(other.amount), currency);
    }

    public Money multiply(double factor) {
        return new Money(amount.multiply(BigDecimal.valueOf(factor)), currency);
    }

    public Money negate() {
        return new Money(amount.negate(), currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.equals(money.amount) && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return Currency.getInstance(currency.getCurrencyCode())
            .getSymbol() + amount;
    }
}
```

### Immutable Object with Collections

```java
public final class Configuration {
    private final String name;
    private final Map<String, String> settings;
    private final List<String> features;

    public Configuration(String name, Map<String, String> settings, List<String> features) {
        this.name = Objects.requireNonNull(name);
        // Defensive copy - prevent external modification
        this.settings = Map.copyOf(settings);
        this.features = List.copyOf(features);
    }

    public String name() { return name; }
    public Map<String, String> settings() { return settings; }
    public List<String> features() { return features; }

    // Derived properties
    public String getSetting(String key) {
        return settings.get(key);
    }

    public boolean hasFeature(String feature) {
        return features.contains(feature);
    }

    // Modified instance
    public Configuration withSetting(String key, String value) {
        Map<String, String> newSettings = new HashMap<>(settings);
        newSettings.put(key, value);
        return new Configuration(name, newSettings, features);
    }

    public Configuration withFeature(String feature) {
        List<String> newFeatures = new ArrayList<>(features);
        newFeatures.add(feature);
        return new Configuration(name, settings, newFeatures);
    }
}
```

### Immutable Class with Mutable Internal State

```java
public final class DateRange {
    private final LocalDate start;
    private final LocalDate end;

    public DateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start must be before end");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDate start() { return start; }
    public LocalDate end() { return end; }

    public long days() {
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(start) && !date.isAfter(end);
    }

    public boolean overlaps(DateRange other) {
        return !start.isAfter(other.end) && !end.isBefore(other.start);
    }

    public DateRange extendTo(LocalDate newEnd) {
        return new DateRange(start, newEnd);
    }
}
```

---

## Records

### Records as Immutable Data

```java
// Records are inherently immutable
public record Point(int x, int y) {}

public record Person(
    String name,
    int age,
    String email
) {
    // Compact constructor for validation
    public Person {
        Objects.requireNonNull(name, "Name required");
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
    }

    // Derived methods
    public boolean isAdult() {
        return age >= 18;
    }

    public Person withName(String newName) {
        return new Person(newName, age, email);
    }
}
```

### Nested Records

```java
public record Address(
    String street,
    String city,
    String state,
    String zipCode
) {
    public String fullAddress() {
        return street + ", " + city + ", " + state + " " + zipCode;
    }
}

public record Employee(
    String id,
    String name,
    Address address,
    List<String> skills
) {
    public Employee {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(address);
        skills = List.copyOf(skills);  // Make immutable copy
    }

    public Employee relocate(Address newAddress) {
        return new Employee(id, name, newAddress, skills);
    }

    public Employee addSkill(String skill) {
        List<String> newSkills = new ArrayList<>(skills);
        newSkills.add(skill);
        return new Employee(id, name, address, newSkills);
    }
}
```

### Sealed Records

```java
public sealed interface Shape
    permits Circle, Rectangle, Triangle {
    double area();
}

public record Circle(double radius) implements Shape {
    public Circle {
        if (radius <= 0) throw new IllegalArgumentException("Radius must be positive");
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

public record Rectangle(double width, double height) implements Shape {
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
    }

    @Override
    public double area() {
        return width * height;
    }
}

public record Triangle(double a, double b, double c) implements Shape {
    public Triangle {
        if (a + b <= c || a + c <= b || b + c <= a) {
            throw new IllegalArgumentException("Invalid triangle sides");
        }
    }

    @Override
    public double area() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
}
```

---

## Unmodifiable Collections

### Creating Unmodifiable Collections

```java
// Java 9+ factory methods (preferred)
List<String> immutableList = List.of("a", "b", "c");
Map<String, Integer> immutableMap = Map.of("key1", 1, "key2", 2);
Set<Integer> immutableSet = Set.of(1, 2, 3);

// Java 10+ copyOf
List<String> source = new ArrayList<>(List.of("a", "b", "c"));
List<String> immutableCopy = List.copyOf(source);

// Pre-Java 9 approach
List<String> oldStyle = Collections.unmodifiableList(new ArrayList<>(List.of("a", "b")));
Map<String, Integer> oldStyleMap = Collections.unmodifiableMap(new HashMap<>(Map.of("key", 1)));
```

### Nested Immutable Collections

```java
// Lists of lists
List<List<Integer>> immutableNested = List.of(
    List.of(1, 2, 3),
    List.of(4, 5, 6),
    List.of(7, 8, 9)
);

// Maps with mutable values
public record AppState(
    Map<String, List<String>> userRoles,
    Map<String, Map<String, String>> configurations
) {
    public AppState {
        // Deep copy for nested mutable structures
        userRoles = userRoles.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                e -> List.copyOf(e.getValue())
            ));
        configurations = configurations.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                e -> Map.copyOf(e.getValue())
            ));
    }
}
```

### Builder for Immutable Collections

```java
public class ImmutableListBuilder<T> {
    private final List<T> items = new ArrayList<>();

    public ImmutableListBuilder<T> add(T item) {
        items.add(item);
        return this;
    }

    public ImmutableListBuilder<T> addAll(Collection<T> items) {
        this.items.addAll(items);
        return this;
    }

    public List<T> build() {
        return List.copyOf(items);
    }
}

// Usage
List<String> list = new ImmutableListBuilder<String>()
    .add("a")
    .add("b")
    .add("c")
    .build();
```

---

## Defensive Copying

### Constructor Defensive Copying

```java
public final class Event {
    private final String name;
    private final Instant timestamp;
    private final List<String> attendees;

    public Event(String name, Instant timestamp, List<String> attendees) {
        this.name = Objects.requireNonNull(name);
        // Defensive copy for mutable parameter
        this.timestamp = Instant.from(timestamp);
        this.attendees = List.copyOf(attendees);
    }

    public String name() { return name; }
    public Instant timestamp() { return timestamp; }
    public List<String> attendees() { return attendees; }
}
```

### Getter Defensive Copying

```java
public class mutableinternal {
    private final List<String> items = new ArrayList<>();

    // Return defensive copy
    public List<String> getItems() {
        return List.copyOf(items);  // or new ArrayList<>(items)
    }

    // Return unmodifiable view
    public List<String> getView() {
        return Collections.unmodifiableList(items);
    }
}
```

### When Defensive Copying is Needed

```java
public final class SecureConfig {
    private final char[] password;

    public SecureConfig(char[] password) {
        // Defensive copy
        this.password = Arrays.copyOf(password, password.length);
        // Clear the original
        Arrays.fill(password, '\0');
    }

    public char[] getPassword() {
        // Defensive copy - prevent external modification
        return Arrays.copyOf(password, password.length);
    }
}
```

---

## Builder Pattern for Immutables

### Step Builder

```java
public final class HttpRequest {
    private final String method;
    private final String url;
    private final Map<String, String> headers;
    private final byte[] body;

    private HttpRequest(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body != null ? builder.body.clone() : new byte[0];
    }

    public String method() { return method; }
    public String url() { return url; }
    public Map<String, String> headers() { return headers; }
    public byte[] body() { return body.clone(); }

    public static class Builder {
        private final String method;
        private final String url;
        private final Map<String, String> headers = new HashMap<>();
        private byte[] body;

        public Builder(String method, String url) {
            this.method = Objects.requireNonNull(method);
            this.url = Objects.requireNonNull(url);
        }

        public Builder header(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body != null ? body.clone() : null;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}

// Usage
HttpRequest request = new HttpRequest.Builder("GET", "https://api.example.com")
    .header("Accept", "application/json")
    .header("Authorization", "Bearer token")
    .build();
```

---

## Performance Considerations

### String Immutability

```java
// Strings are immutable - concatenation creates new objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result += "a";  // Creates 1000 new String objects!
}

// Use StringBuilder for mutable operations
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append("a");
}
String result = sb.toString();  // Single String object
```

### Lazy Evaluation

```java
public final class LazyValue<T> {
    private final Supplier<T> supplier;
    private volatile T value;
    private volatile boolean computed;

    public LazyValue(Supplier<T> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    public T get() {
        if (!computed) {
            synchronized (this) {
                if (!computed) {
                    value = supplier.get();
                    computed = true;
                }
            }
        }
        return value;
    }
}

// Usage
LazyValue<BigInteger> cachedPi = new LazyValue<>(() -> computePi(10000));
// Computation happens only on first get()
BigInteger pi = cachedPi.get();
```

---

## Best Practices

### Do

```java
// 1. Make classes final
public final class ImmutablePoint { ... }

// 2. Make fields final
private final int x;
private final int y;

// 3. Use defensive copying
public List<String> getItems() {
    return List.copyOf(items);
}

// 4. Use records for data carriers
public record Point(int x, int y) {}

// 5. Validate in constructor
public Money(BigDecimal amount, Currency currency) {
    if (amount == null || currency == null) throw new IllegalArgumentException();
    this.amount = amount;
    this.currency = currency;
}
```

### Don't

```java
// 1. Don't expose mutable internal state
public List<String> getItems() {
    return items;  // BAD - external modification possible
}

// 2. Don't forget defensive copying in constructors
public Event(List<String> attendees) {
    this.attendees = attendees;  // BAD - shared reference
}

// 3. Don't create mutable fields in records
public record BadRecord(String name, List<String> items) {
    // items is mutable even though record is "immutable"
}

// 4. Don't use lazy initialization without synchronization
private Object cached;  // BAD - race condition
public Object get() {
    if (cached == null) cached = compute();
    return cached;
}
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Immutable Object** | Cannot be modified after creation |
| **`final` fields** | Prevent reassignment |
| **Defensive Copying** | Protect against external modification |
| **Records** | Built-in immutable data carriers |
| **Unmodifiable Collections** | Prevent collection modification |
| **Thread Safety** | Immutable objects are inherently safe |
| **Value Semantics** | equals/hashCode are stable |
| **Trade-offs** | Memory overhead for modifications |
| **Builder Pattern** | Complex immutable object construction |
| **Lazy Evaluation** | Cache expensive computations |
