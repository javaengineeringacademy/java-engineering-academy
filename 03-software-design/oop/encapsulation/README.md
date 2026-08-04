# Encapsulation

Encapsulation is the bundling of data with methods that operate on that data, restricting direct access to object internals. It protects object invariants and provides a clean API.

## Table of Contents

1. [Concepts](#concepts)
2. [Private Fields](#private-fields)
3. [Getters and Setters](#getters-and-setters)
4. [Validation](#validation)
5. [Immutable Objects](#immutable-objects)
6. [Defensive Copying](#defensive-copying)
7. [Information Hiding](#information-hiding)
8. [Records as Encapsulation](#records-as-encapsulation)
9. [Best Practices](#best-practices)
10. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Encapsulation?

Encapsulation controls access to an object's internal state through a well-defined interface.

```
┌─────────────────────────────────────────┐
│           PUBLIC INTERFACE              │
│  ┌─────────────────────────────────┐   │
│  │         PRIVATE STATE           │   │
│  │  - field1                       │   │
│  │  - field2                       │   │
│  │  - internal methods             │   │
│  └─────────────────────────────────┘   │
│  Public methods provide controlled     │
│  access to private state               │
└─────────────────────────────────────────┘
```

### Benefits

- **Data Integrity** - prevent invalid states
- **Flexibility** - change internal representation freely
- **Maintainability** - reduce coupling between components
- **Security** - hide sensitive implementation details

---

## Private Fields

### Basic Encapsulation

```java
public class BankAccount {
    // Private fields - cannot be accessed directly
    private String accountId;
    private double balance;
    private String ownerName;
    private boolean active;

    // Constructor provides initial state
    public BankAccount(String accountId, String ownerName, double initialBalance) {
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.balance = initialBalance;
        this.active = true;
    }

    // Public methods control access
    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            return false;  // Insufficient funds
        }
        balance -= amount;
        return true;
    }
}

// Usage
BankAccount account = new BankAccount("ACC001", "Alice", 1000);
account.deposit(500);      // Works
// account.balance = -1000; // Compile error - private field
```

### Package-Private Access

```java
// File: com/example/model/User.java
public class User {
    private String name;
    String internalId;  // Package-private - accessible in same package only

    public User(String name) {
        this.name = name;
        this.internalId = generateId();
    }

    // Package-private method for internal use
    String getInternalId() {
        return internalId;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }
}

// File: com/example/service/UserService.java (same package)
public class UserService {
    public void process(User user) {
        // Can access package-private members
        String id = user.getInternalId();
        processInternal(id);
    }
}

// File: com/example/external/ExternalCode.java (different package)
public class ExternalCode {
    public void process(User user) {
        // user.getInternalId();  // Compile error - different package
    }
}
```

---

## Getters and Setters

### Basic Getters and Setters

```java
public class Person {
    private String name;
    private int age;
    private String email;

    // Getter - provides read access
    public String getName() {
        return name;
    }

    // Setter - provides write access with validation
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }
}
```

### Computed Properties

```java
public class Rectangle {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    // Computed property - no stored field
    public double area() {
        return width * height;
    }

    public double perimeter() {
        return 2 * (width + height);
    }

    public double diagonal() {
        return Math.sqrt(width * width + height * height);
    }

    public boolean isSquare() {
        return width == height;
    }

    // Standard getters/setters
    public double getWidth() { return width; }
    public void setWidth(double width) {
        if (width <= 0) throw new IllegalArgumentException("Width must be positive");
        this.width = width;
    }

    public double getHeight() { return height; }
    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.height = height;
    }
}
```

### Fluent Setters (Method Chaining)

```java
public classQueryBuilder {
    private String table;
    private List<String> conditions = new ArrayList<>();
    private List<String> orderBy = new ArrayList<>();
    private Integer limit;

    // Fluent setter - returns this for chaining
    public QueryBuilder from(String table) {
        this.table = table;
        return this;
    }

    public QueryBuilder where(String condition) {
        conditions.add(condition);
        return this;
    }

    public QueryBuilder orderBy(String column) {
        orderBy.add(column);
        return this;
    }

    public QueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public String build() {
        StringBuilder sql = new StringBuilder("SELECT * FROM " + table);
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }
        if (!orderBy.isEmpty()) {
            sql.append(" ORDER BY ").append(String.join(", ", orderBy));
        }
        if (limit != null) {
            sql.append(" LIMIT ").append(limit);
        }
        return sql.toString();
    }
}

// Usage with fluent API
String query = new QueryBuilder()
    .from("users")
    .where("age > 18")
    .where("active = true")
    .orderBy("name")
    .limit(10)
    .build();
```

---

## Validation

### In Setter Validation

```java
public class Product {
    private String name;
    private double price;
    private int stockQuantity;

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name required");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Name too long (max 100 chars)");
        }
        this.name = name;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (price > 1_000_000) {
            throw new IllegalArgumentException("Price too high");
        }
        // Round to 2 decimal places
        this.price = Math.round(price * 100.0) / 100.0;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stockQuantity = stockQuantity;
    }
}
```

### Builder with Validation

```java
public class User {
    private final String username;
    private final String email;
    private final int age;

    private User(String username, String email, int age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public static class Builder {
        private String username;
        private String email;
        private int age;

        public Builder username(String username) {
            if (username == null || username.length() < 3) {
                throw new IllegalArgumentException("Username must be at least 3 chars");
            }
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                throw new IllegalArgumentException("Invalid email");
            }
            this.email = email;
            return this;
        }

        public Builder age(int age) {
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Invalid age");
            }
            this.age = age;
            return this;
        }

        public User build() {
            // Validate all required fields
            Objects.requireNonNull(username, "Username required");
            Objects.requireNonNull(email, "Email required");
            return new User(username, email, age);
        }
    }
}

// Usage
User user = new User.Builder()
    .username("alice")
    .email("alice@example.com")
    .age(30)
    .build();
```

### Custom Validation Annotations

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface Email {
    String message() default "Invalid email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class EmailValidator implements ConstraintValidator<Email, String> {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;  // Use @NotNull for null check
        return EMAIL_PATTERN.matcher(value).matches();
    }
}

// Usage
public class Contact {
    @Email
    private String email;
    
    @NotNull
    @Size(min = 2, max = 50)
    private String name;
}
```

---

## Immutable Objects

### Basic Immutable Class

```java
public final class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Only getters, no setters
    public double x() { return x; }
    public double y() { return y; }

    // Methods that appear to modify actually create new instances
    public Point translate(double dx, double dy) {
        return new Point(x + dx, y + dy);
    }

    public Point scale(double factor) {
        return new Point(x * factor, y * factor);
    }

    public double distanceTo(Point other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point p)) return false;
        return Double.compare(p.x, x) == 0 && Double.compare(p.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
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
        this.name = name;
        // Defensive copy - external changes won't affect this object
        this.settings = Map.copyOf(settings);
        this.features = List.copyOf(features);
    }

    public String name() { return name; }

    // Return unmodifiable views
    public Map<String, String> settings() {
        return settings;  // Already immutable
    }

    public List<String> features() {
        return features;  // Already immutable
    }

    // Derived properties
    public String getSetting(String key) {
        return settings.get(key);
    }

    public boolean hasFeature(String feature) {
        return features.contains(feature);
    }
}
```

### Record as Immutable Object

```java
// Records are automatically immutable
public record Money(
    BigDecimal amount,
    Currency currency
) {
    // Compact constructor for validation
    public Money {
        Objects.requireNonNull(amount, "Amount required");
        Objects.requireNonNull(currency, "Currency required");
        if (amount.scale() > currency.getDefaultFractionDigits()) {
            throw new IllegalArgumentException(
                "Too many decimal places for " + currency
            );
        }
    }

    // Canonical constructor (validation)
    public Money {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    // Methods
    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(amount.add(other.amount), currency);
    }

    public Money multiply(int factor) {
        return new Money(amount.multiply(BigDecimal.valueOf(factor)), currency);
    }
}
```

---

## Defensive Copying

### Protecting Internal State

```java
public class Event {
    private final String name;
    private final List<String> attendees;

    public Event(String name, List<String> attendees) {
        this.name = name;
        // Defensive copy - prevent external modification
        this.attendees = new ArrayList<>(attendees);
    }

    // Return a copy - prevent external modification of internal state
    public List<String> getAttendees() {
        return new ArrayList<>(attendees);  // Defensive copy
    }

    public void addAttendee(String person) {
        attendees.add(person);
    }
}

// Without defensive copy - vulnerable!
public class VulnerableEvent {
    private final String name;
    private final List<String> attendees;

    public VulnerableEvent(String name, List<String> attendees) {
        this.name = name;
        this.attendees = attendees;  // Reference shared!
    }

    public List<String> getAttendees() {
        return attendees;  // External code can modify internal list!
    }
}

// Attack:
List<String> list = new ArrayList<>(List.of("Alice"));
VulnerableEvent event = new VulnerableEvent("Party", list);
list.add("Hacker");  // Modifies event's internal state!
```

### Immutable Collections

```java
public class ImmutableExample {
    private final List<String> items;
    private final Map<String, Integer> counts;

    public ImmutableExample(List<String> items, Map<String, Integer> counts) {
        // Java 9+ factory methods create immutable collections
        this.items = List.copyOf(items);
        this.counts = Map.copyOf(counts);
    }

    // Alternative using Collections.unmodifiable*
    public ImmutableExample(List<String> items) {
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    public List<String> getItems() {
        return items;  // Already immutable
    }
}
```

---

## Information Hiding

### Hiding Implementation Details

```java
// Public API
public class Cache<K, V> {
    private final Map<K, V> map = new LinkedHashMap<>(16, 0.75f, true);
    private final int maxSize;

    public Cache(int maxSize) {
        this.maxSize = maxSize;
    }

    public void put(K key, V value) {
        if (map.size() >= maxSize) {
            // LRU eviction - implementation detail hidden
            K oldest = map.keySet().iterator().next();
            map.remove(oldest);
        }
        map.put(key, value);
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    public int size() {
        return map.size();
    }

    // Internal implementation hidden
    // Users don't need to know about LRU, LinkedHashMap, etc.
}
```

### Facade Pattern for Encapsulation

```java
// Complex subsystem
public class CPU {
    public void freeze() { /* ... */ }
    public void jump(long address) { /* ... */ }
    public void execute() { /* ... */ }
}

public class Memory {
    public void load(long address, String data) { /* ... */ }
}

public class HardDrive {
    public String read(long sector, int size) { /* ... */ }
}

// Facade - simple interface hiding complexity
public class ComputerFacade {
    private final CPU cpu = new CPU();
    private final Memory memory = new Memory();
    private final HardDrive hardDrive = new HardDrive();

    public void start() {
        cpu.freeze();
        memory.load(0, "BOOT");
        cpu.jump(0);
        cpu.execute();
    }

    public String readData(long address, int size) {
        return hardDrive.read(address, size);
    }
}

// Usage - simple interface
ComputerFacade computer = new ComputerFacade();
computer.start();  // Complex sequence hidden
```

---

## Records as Encapsulation

### Record with Validation

```java
public record Temperature(
    double value,
    TemperatureUnit unit
) {
    public enum TemperatureUnit { CELSIUS, FAHRENHEIT, KELVIN }

    public Temperature {
        if (unit == TemperatureUnit.KELVIN && value < 0) {
            throw new IllegalArgumentException("Kelvin cannot be negative");
        }
    }

    public Temperature toCelsius() {
        return switch (unit) {
            case CELSIUS -> this;
            case FAHRENHEIT -> new Temperature((value - 32) * 5/9, TemperatureUnit.CELSIUS);
            case KELVIN -> new Temperature(value - 273.15, TemperatureUnit.CELSIUS);
        };
    }

    public Temperature toFahrenheit() {
        return switch (unit) {
            case FAHRENHEIT -> this;
            case CELSIUS -> new Temperature(value * 9/5 + 32, TemperatureUnit.FAHRENHEIT);
            case KELVIN -> toCelsius().toFahrenheit();
        };
    }
}
```

### Enriched Records

```java
public record Range(int start, int end) {
    public Range {
        if (start > end) {
            throw new IllegalArgumentException("Start must be <= end");
        }
    }

    public boolean contains(int value) {
        return value >= start && value <= end;
    }

    public boolean overlaps(Range other) {
        return start <= other.end && other.start <= end;
    }

    public Range intersect(Range other) {
        int newStart = Math.max(start, other.start);
        int newEnd = Math.min(end, other.end);
        if (newStart > newEnd) {
            return null;  // No intersection
        }
        return new Range(newStart, newEnd);
    }

    public int length() {
        return end - start + 1;
    }
}
```

---

## Best Practices

### Do

```java
// 1. Make fields private
public class Example {
    private String name;  // Good
}

// 2. Validate in setters
public void setAge(int age) {
    if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
    this.age = age;
}

// 3. Return immutable collections
public List<String> getItems() {
    return List.copyOf(items);  // Good
}

// 4. Use defensive copying for mutable arguments
public Event(List<String> attendees) {
    this.attendees = new ArrayList<>(attendees);  // Good
}

// 5. Prefer records for data carriers
public record Point(int x, int y) {}  // Good
```

### Don't

```java
// 1. Don't expose mutable fields
public class Bad {
    public List<String> items;  // Bad - mutable and public
}

// 2. Don't return mutable internal state
public List<String> getItems() {
    return items;  // Bad - external modification possible
}

// 3. Don't skip validation
public void setName(String name) {
    this.name = name;  // Bad - no validation
}

// 4. Don't create getter/setter for everything
public class OverEncapsulated {
    private int x;
    public int getX() { return x; }      // Unnecessary?
    public void setX(int x) { this.x = x; }  // Unnecessary?
}
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Private Fields** | Prevent direct access to internal state |
| **Getters/Setters** | Provide controlled access with validation |
| **Validation** | Ensure object invariants are maintained |
| **Immutable Objects** | Cannot be modified after creation |
| **Defensive Copying** | Protect against external modification |
| **Information Hiding** | Hide implementation complexity |
| **Records** | Immutable data carriers with auto-generated methods |
| **Unmodifiable Collections** | Prevent modification of returned collections |
| **Final Class** | Prevent subclassing that could break invariants |
| **Encapsulation ≠ Data Class** | Not everything needs getters and setters |
