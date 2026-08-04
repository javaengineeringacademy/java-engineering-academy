# Builder Pattern

The Builder pattern separates the construction of a complex object from its representation, allowing the same construction process to create different representations. It provides a fluent API for readable object creation.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Builder](#basic-builder)
3. [Fluent Builder API](#fluent-builder-api)
4. [Builder with Validation](#builder-with-validation)
5. [Step Builder](#step-builder)
6. [Lombok Builder](#lombok-builder)
7. [Best Practices](#best-practices)
8. [Key Takeaways](#key-takeaways)

---

## Concepts

### When to Use Builder

- Many constructor parameters (telescoping constructor)
- Optional parameters
- Complex object construction
- Immutable objects with many fields
- Readable object creation

### Builder vs Constructor

```java
// Constructor - hard to read with many params
User user = new User("Alice", 30, "alice@email.com", "NYC", "USA", true, null);

// Builder - readable and flexible
User user = User.builder()
    .name("Alice")
    .age(30)
    .email("alice@email.com")
    .city("NYC")
    .country("USA")
    .active(true)
    .build();
```

---

## Basic Builder

### Classic Builder Implementation

```java
public class User {
    private final String name;
    private final int age;
    private final String email;
    private final String phone;
    private final Address address;

    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
        this.phone = builder.phone;
        this.address = builder.address;
    }

    // Getters
    public String name() { return name; }
    public int age() { return age; }
    public String email() { return email; }
    public String phone() { return phone; }
    public Address address() { return address; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private int age;
        private String email;
        private String phone;
        private Address address;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

// Usage
User user = User.builder()
    .name("Alice")
    .age(30)
    .email("alice@example.com")
    .build();
```

---

## Fluent Builder API

### Method Chaining

```java
public class QueryBuilder<T> {
    private final Class<T> entityType;
    private final List<String> conditions = new ArrayList<>();
    private final List<String> orderBy = new ArrayList<>();
    private final List<String> selectFields = new ArrayList<>();
    private Integer limit;
    private Integer offset;

    public QueryBuilder(Class<T> entityType) {
        this.entityType = entityType;
    }

    public static <T> QueryBuilder<T> from(Class<T> type) {
        return new QueryBuilder<>(type);
    }

    public QueryBuilder<T> select(String... fields) {
        selectFields.addAll(List.of(fields));
        return this;
    }

    public QueryBuilder<T> where(String condition) {
        conditions.add(condition);
        return this;
    }

    public QueryBuilder<T> and(String condition) {
        conditions.add(condition);
        return this;
    }

    public QueryBuilder<T> orderBy(String column) {
        orderBy.add(column);
        return this;
    }

    public QueryBuilder<T> orderByDesc(String column) {
        orderBy.add(column + " DESC");
        return this;
    }

    public QueryBuilder<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public QueryBuilder<T> offset(int offset) {
        this.offset = offset;
        return this;
    }

    public Query<T> build() {
        return new Query<>(entityType, selectFields, conditions, orderBy, limit, offset);
    }
}

// Usage
Query<User> query = QueryBuilder.from(User.class)
    .select("name", "email")
    .where("age > 18")
    .and("active = true")
    .orderBy("name")
    .limit(10)
    .build();
```

### Builder with Inheritance

```java
public abstract class BaseBuilder<T extends BaseBuilder<T>> {
    protected String name;
    protected String description;

    @SuppressWarnings("unchecked")
    protected T self() { return (T) this; }

    public T name(String name) {
        this.name = name;
        return self();
    }

    public T description(String description) {
        this.description = description;
        return self();
    }
}

public class UserBuilder extends BaseBuilder<UserBuilder> {
    private int age;
    private String email;

    public UserBuilder age(int age) {
        this.age = age;
        return self();
    }

    public UserBuilder email(String email) {
        this.email = email;
        return self();
    }

    public User build() {
        return new User(name, age, email);
    }
}

public class ProductBuilder extends BaseBuilder<ProductBuilder> {
    private double price;
    private int stock;

    public ProductBuilder price(double price) {
        this.price = price;
        return self();
    }

    public ProductBuilder stock(int stock) {
        this.stock = stock;
        return self();
    }

    public Product build() {
        return new Product(name, price, stock);
    }
}

// Usage
User user = new UserBuilder()
    .name("Alice")
    .age(30)
    .email("alice@example.com")
    .build();

Product product = new ProductBuilder()
    .name("Widget")
    .price(9.99)
    .stock(100)
    .build();
```

---

## Builder with Validation

### Validation in Build

```java
public class HttpRequest {
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

    public static class Builder {
        private final String method;
        private final String url;
        private final Map<String, String> headers = new HashMap<>();
        private byte[] body;

        public Builder(String method, String url) {
            this.method = Objects.requireNonNull(method, "Method required");
            this.url = Objects.requireNonNull(url, "URL required");
            if (!isValidMethod(method)) {
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
            }
        }

        public Builder header(String name, String value) {
            headers.put(
                Objects.requireNonNull(name, "Header name required"),
                Objects.requireNonNull(value, "Header value required")
            );
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public Builder body(String body) {
            this.body = body.getBytes();
            return this;
        }

        public HttpRequest build() {
            validate();
            return new HttpRequest(this);
        }

        private void validate() {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                throw new IllegalArgumentException("URL must start with http:// or https://");
            }
        }

        private boolean isValidMethod(String method) {
            return Set.of("GET", "POST", "PUT", "DELETE", "PATCH")
                .contains(method.toUpperCase());
        }
    }
}

// Usage
HttpRequest request = new HttpRequest.Builder("GET", "https://api.example.com")
    .header("Accept", "application/json")
    .build();
```

### Collecting Validation Errors

```java
public class UserBuilder {
    private String name;
    private int age;
    private String email;
    private final List<String> errors = new ArrayList<>();

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder age(int age) {
        this.age = age;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ValidationResult<User> build() {
        errors.clear();

        if (name == null || name.isBlank()) {
            errors.add("Name is required");
        } else if (name.length() < 2) {
            errors.add("Name must be at least 2 characters");
        }

        if (age < 0 || age > 150) {
            errors.add("Age must be between 0 and 150");
        }

        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            errors.add("Valid email is required");
        }

        if (errors.isEmpty()) {
            return ValidationResult.success(new User(name, age, email));
        } else {
            return ValidationResult.failure(errors);
        }
    }
}

// Usage
ValidationResult<User> result = new UserBuilder()
    .name("A")
    .age(30)
    .email("invalid")
    .build();

if (result.isSuccess()) {
    User user = result.getValue();
} else {
    List<String> errors = result.getErrors();
}
```

---

## Step Builder

### Enforcing Build Order

```java
public class OrderBuilder {
    private String customer;
    private List<String> items;
    private PaymentMethod payment;
    private ShippingMethod shipping;

    private OrderBuilder() {}

    public static CustomerStep create() {
        return new OrderBuilder()::setCustomer;
    }

    @FunctionalInterface
    public interface CustomerStep {
        ItemsStep withCustomer(String customer);
    }

    @FunctionalInterface
    public interface ItemsStep {
        PaymentStep withItems(String... items);
    }

    @FunctionalInterface
    public interface PaymentStep {
        ShippingStep withPayment(PaymentMethod payment);
    }

    @FunctionalInterface
    public interface ShippingStep {
        BuildStep withShipping(ShippingMethod shipping);
    }

    @FunctionalInterface
    public interface BuildStep {
        Order build();
    }

    private ItemsStep setCustomer(String customer) {
        this.customer = customer;
        return items -> {
            this.items = List.of(items);
            return payment -> {
                this.payment = payment;
                return shipping -> {
                    this.shipping = shipping;
                    return this::build;
                };
            };
        };
    }

    private Order build() {
        return new Order(customer, items, payment, shipping);
    }
}

// Usage - must follow order
Order order = OrderBuilder.create()
    .withCustomer("Alice")
    .withItems("Widget", "Gadget")
    .withPayment(PaymentMethod.CREDIT_CARD)
    .withShipping(ShippingMethod.EXPRESS)
    .build();
```

---

## Lombok Builder

### Using @Builder Annotation

```java
@Builder
public class User {
    private final String name;
    private final int age;
    private final String email;
    @Builder.Default
    private final boolean active = true;
    @Builder.Default
    private final List<String> roles = List.of("USER");
}

// Usage
User user = User.builder()
    .name("Alice")
    .age(30)
    .email("alice@example.com")
    .build();

// With defaults
User userWithDefaults = User.builder()
    .name("Bob")
    .age(25)
    .email("bob@example.com")
    .build();  // active=true, roles=["USER"]
```

### Builder with SuperClass

```java
@SuperBuilder
public class Person {
    private final String name;
    private final int age;
}

@SuperBuilder
public class Employee extends Person {
    private final String employeeId;
    private final String department;
}

// Usage
Employee emp = Employee.builder()
    .name("Alice")
    .age(30)
    .employeeId("E001")
    .department("Engineering")
    .build();
```

---

## Best Practices

### Do

```java
// 1. Make constructor private
private User(Builder builder) { ... }

// 2. Return this from setters
public Builder name(String name) {
    this.name = name;
    return this;
}

// 3. Validate in build()
public User build() {
    Objects.requireNonNull(name, "Name required");
    return new User(this);
}

// 4. Use @Builder.Default for default values
@Builder.Default
private final boolean active = true;

// 5. Make inner builder static
public static class Builder { ... }
```

### Don't

```java
// 1. Don't forget to clone mutable fields
public User build() {
    // BAD: shared reference
    return new User(this.roles);
    // GOOD: defensive copy
    return new User(new ArrayList<>(this.roles));
}

// 2. Don't create builders for simple objects
// Use records or simple constructors instead

// 3. Don't make builder too complex
// Keep it focused on essential fields
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Builder** | Separates construction from representation |
| **Fluent API** | Method chaining for readability |
| **Validation** | Validate in build() method |
| **Step Builder** | Enforce build order |
| **Immutable** | Builder creates immutable objects |
| **Defaults** | Handle optional parameters |
| **Inheritance** | Self-type pattern for hierarchy |
| **Lombok** | @Builder for boilerplate reduction |
| **Defensive Copy** | Clone mutable parameters |
| **Private Constructor** | Force use of builder |
