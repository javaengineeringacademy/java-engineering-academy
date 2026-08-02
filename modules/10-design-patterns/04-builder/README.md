# Builder Pattern

## 1. Introduction

The Builder Pattern is a creational design pattern that separates the construction of a complex object from its representation. It allows you to produce different types and representations of an object using the same construction code. The Builder pattern is particularly useful when you need to create an object with lots of possible configuration options.

Unlike other creational patterns, Builder focuses on constructing complex objects step by step. The pattern is especially useful when you need to create an object with many optional parameters.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement the Builder pattern with fluent API
- Create thread-safe builders
- Handle optional and required parameters elegantly
- Understand Lombok @Builder and manual implementation
- Compare Builder with other construction approaches

---

## 3. Prerequisites

- Understanding of Java classes and constructors
- Knowledge of method chaining
- Familiarity with immutability concepts
- Understanding of static inner classes

---

## 4. Why This Concept Exists

The Builder pattern exists because:

- **Telescoping constructors**: Too many constructor parameters
- **Optional parameters**: Not all fields are required
- **Immutability**: Build once, use many times
- **Readability**: Clear, step-by-step construction
- **Validation**: Validate before construction completes
- **Complex objects**: Objects with many dependencies

Without Builder, you'd have constructors with dozens of parameters or mutable objects with setters.

---

## 5. Problem Statement

Consider an HTTP request builder:

```java
// BAD: Telescoping constructor
public class HttpRequest {
    public HttpRequest(String url) { }
    public HttpRequest(String url, String method) { }
    public HttpRequest(String url, String method, Map<String, String> headers) { }
    public HttpRequest(String url, String method, Map<String, String> headers, String body) { }
    public HttpRequest(String url, String method, Map<String, String> headers, String body, int timeout) { }
    // Gets worse with more parameters...
}

// BAD: Mutable object with setters
HttpRequest request = new HttpRequest();
request.setUrl("https://api.example.com");
request.setMethod("POST");
request.setHeaders(headers);
request.setBody(body);
request.setTimeout(5000);
// Object is in inconsistent state until all setters called
```

**Problems:**
1. **Constructor explosion**: Many constructors for different combinations
2. **Inconsistent state**: Object partially constructed
3. **Mutable**: Can be modified after creation
4. **Hard to read**: Unclear which parameters are which

---

## 6. Theory

### 6.1 Builder vs. Other Approaches

| Approach | Pros | Cons |
|----------|------|------|
| Telescoping constructor | Simple | Hard to read, many constructors |
| JavaBeans (setters) | Flexible | Mutable, inconsistent state |
| Builder | Readable, immutable | More code, extra class |

### 6.2 Builder Structure

1. **Product**: The complex object being built
2. **Builder**: Provides methods to configure the product
3. **Director** (optional): Defines build order
4. **Client**: Invokes builder to create product

### 6.3 Immutability

Builder pattern naturally supports immutability:
- Builder is mutable (during construction)
- Product is immutable (after construction)
- Thread-safe for concurrent use

---

## 7. Internal Working

### 7.1 Builder Flow

```
1. Client creates Builder with required parameters
2. Client chains optional parameter methods
3. Client calls build()
4. Builder validates and creates Product
5. Product is returned, immutable
```

### 7.2 Method Chaining

```java
builder.field1(value1)  // returns this
       .field2(value2)  // returns this
       .field3(value3)  // returns this
       .build();        // returns Product
```

---

## 8. JVM Perspective

### 8.1 Memory Allocation

- Builder object created on heap
- Product object created on heap during build()
- Builder can be garbage collected after build()

### 8.2 Optimization

- JIT can inline builder methods
- Escape analysis may allocate on stack
- String concatenation optimized with StringBuilder (similar concept)

---

## 9. Memory Representation

### 9.1 Builder Memory Model

```
┌─────────────────────────────────────┐
│             Client                  │
└──────────────┬──────────────────────┘
               │ creates
               ↓
┌─────────────────────────────────────┐
│           Builder                   │
│  - field1, field2, field3           │
│  + field1(value): this              │
│  + field2(value): this              │
│  + build(): Product                 │
└──────────────┬──────────────────────┘
               │ build()
               ↓
┌─────────────────────────────────────┐
│           Product                   │
│  - final field1, field2, field3     │
└─────────────────────────────────────┘
```

---

## 10. Syntax

### 10.1 Basic Builder Structure

```java
public class Product {
    private final String required;
    private final int optional;

    private Product(Builder builder) {
        this.required = builder.required;
        this.optional = builder.optional;
    }

    public static class Builder {
        private final String required;
        private int optional;

        public Builder(String required) {
            this.required = required;
        }

        public Builder optional(int optional) {
            this.optional = optional;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}

// Usage
Product product = new Product.Builder("required")
    .optional(42)
    .build();
```

---

## 11. Easy Example

### Simple Email Builder

```java
public class Email {
    private final String to;
    private final String subject;
    private final String body;
    private final boolean isHtml;

    private Email(Builder builder) {
        this.to = builder.to;
        this.subject = builder.subject;
        this.body = builder.body;
        this.isHtml = builder.isHtml;
    }

    public static class Builder {
        private final String to;
        private final String subject;
        private String body = "";
        private boolean isHtml = false;

        public Builder(String to, String subject) {
            this.to = to;
            this.subject = subject;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder isHtml(boolean isHtml) {
            this.isHtml = isHtml;
            return this;
        }

        public Email build() {
            return new Email(this);
        }
    }

    @Override
    public String toString() {
        return "Email{to='" + to + "', subject='" + subject + "'}";
    }
}

// Usage
Email email = new Email.Builder("user@example.com", "Welcome")
    .body("<h1>Welcome!</h1>")
    .isHtml(true)
    .build();
```

---

## 12. Medium Example

### HTTP Request Builder

```java
public class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final String body;
    private final int timeout;
    private final boolean followRedirects;

    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
        this.timeout = builder.timeout;
        this.followRedirects = builder.followRedirects;
    }

    public static class Builder {
        private final String url;
        private String method = "GET";
        private Map<String, String> headers = new HashMap<>();
        private String body;
        private int timeout = 30000;
        private boolean followRedirects = true;

        public Builder(String url) {
            this.url = url;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder followRedirects(boolean follow) {
            this.followRedirects = follow;
            return this;
        }

        public HttpRequest build() {
            if (url == null || url.isBlank()) {
                throw new IllegalArgumentException("URL cannot be null or blank");
            }
            return new HttpRequest(this);
        }
    }

    public String getUrl() { return url; }
    public String getMethod() { return method; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
    public int getTimeout() { return timeout; }
    public boolean isFollowRedirects() { return followRedirects; }
}

// Usage
HttpRequest request = new HttpRequest.Builder("https://api.example.com/users")
    .method("POST")
    .header("Content-Type", "application/json")
    .header("Authorization", "Bearer token")
    .body("{\"name\": \"John\"}")
    .timeout(5000)
    .build();
```

---

## 13. Hard Example

### Generic Builder with Validation

```java
public class ValidatedBuilder<T> {
    private final Supplier<T> factory;
    private final List<Consumer<T>> validators;
    private final List<Consumer<T>> initializers;

    private ValidatedBuilder(Supplier<T> factory) {
        this.factory = factory;
        this.validators = new ArrayList<>();
        this.initializers = new ArrayList<>();
    }

    public static <T> ValidatedBuilder<T> of(Supplier<T> factory) {
        return new ValidatedBuilder<>(factory);
    }

    public ValidatedBuilder<T> initialize(Consumer<T> initializer) {
        this.initializers.add(initializer);
        return this;
    }

    public ValidatedBuilder<T> validate(Consumer<T> validator) {
        this.validators.add(validator);
        return this;
    }

    public T build() {
        T instance = factory.get();
        initializers.forEach(init -> init.accept(instance));
        validators.forEach(validator -> validator.accept(instance));
        return instance;
    }
}

// Usage with custom validator
ValidatedBuilder<User> userBuilder = ValidatedBuilder.of(User::new)
    .initialize(user -> {
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.ACTIVE);
    })
    .validate(user -> {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email is required");
        }
        if (user.getAge() < 0 || user.getAge() > 150) {
            throw new ValidationException("Invalid age");
        }
    });

User user = userBuilder.build();
```

### Thread-Safe Builder

```java
public class ThreadSafeBuilder<T> {
    private final List<Function<T, T>> modifiers;
    private final Supplier<T> factory;

    public ThreadSafeBuilder(Supplier<T> factory) {
        this.factory = factory;
        this.modifiers = new CopyOnWriteArrayList<>();
    }

    public ThreadSafeBuilder<T> with(Function<T, T> modifier) {
        modifiers.add(modifier);
        return this;
    }

    public T build() {
        T instance = factory.get();
        for (Function<T, T> modifier : modifiers) {
            instance = modifier.apply(instance);
        }
        return instance;
    }
}

// Usage
ThreadSafeBuilder<Config> builder = new ThreadSafeBuilder<>(Config::new)
    .with(config -> config.setHost("localhost"))
    .with(config -> config.setPort(8080));

// Can be shared across threads
Config config = builder.build();
```

---

## 14. Enterprise Example

### Database Connection Builder

```java
public class DatabaseConnection {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final int poolSize;
    private final Duration connectionTimeout;
    private final Duration idleTimeout;
    private final boolean ssl;
    private final Map<String, String> properties;

    private DatabaseConnection(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.database = builder.database;
        this.username = builder.username;
        this.password = builder.password;
        this.poolSize = builder.poolSize;
        this.connectionTimeout = builder.connectionTimeout;
        this.idleTimeout = builder.idleTimeout;
        this.ssl = builder.ssl;
        this.properties = Map.copyOf(builder.properties);
    }

    public static class Builder {
        private final String host;
        private final String database;
        private int port = 5432;
        private String username = "";
        private String password = "";
        private int poolSize = 10;
        private Duration connectionTimeout = Duration.ofSeconds(30);
        private Duration idleTimeout = Duration.ofMinutes(10);
        private boolean ssl = false;
        private Map<String, String> properties = new HashMap<>();

        public Builder(String host, String database) {
            this.host = Objects.requireNonNull(host, "Host cannot be null");
            this.database = Objects.requireNonNull(database, "Database cannot be null");
        }

        public Builder port(int port) {
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("Invalid port: " + port);
            }
            this.port = port;
            return this;
        }

        public Builder credentials(String username, String password) {
            this.username = username;
            this.password = password;
            return this;
        }

        public Builder poolSize(int poolSize) {
            if (poolSize < 1) {
                throw new IllegalArgumentException("Pool size must be positive");
            }
            this.poolSize = poolSize;
            return this;
        }

        public Builder connectionTimeout(Duration timeout) {
            this.connectionTimeout = timeout;
            return this;
        }

        public Builder idleTimeout(Duration timeout) {
            this.idleTimeout = timeout;
            return this;
        }

        public Builder ssl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public Builder property(String key, String value) {
            this.properties.put(key, value);
            return this;
        }

        public DatabaseConnection build() {
            return new DatabaseConnection(this);
        }
    }

    public String toJdbcUrl() {
        return String.format("jdbc:postgresql://%s:%d/%s?ssl=%s",
            host, port, database, ssl);
    }
}

// Usage
DatabaseConnection conn = new DatabaseConnection.Builder("localhost", "mydb")
    .port(5432)
    .credentials("admin", "secret")
    .poolSize(20)
    .connectionTimeout(Duration.ofSeconds(10))
    .ssl(true)
    .property("charset", "UTF-8")
    .build();
```

---

## 15. Performance

### 15.1 Performance Metrics

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Method call | O(1) | Simple assignment |
| build() | O(n) | n = number of fields |
| Validation | O(v) | v = number of validators |

### 15.2 Optimization Tips

1. **Reuse builder**: For similar objects, reuse and modify
2. **Lazy validation**: Validate only at build time
3. **Immutable collections**: Use Map.copyOf() for immutability
4. **Consider records**: For simple DTOs, use Java records

---

## 16. Best Practices

1. **Make product immutable**: Use final fields
2. **Validate in build()**: Throw exceptions for invalid state
3. **Use fluent API**: Enable method chaining
4. **Document required vs optional**: Clear Javadoc
5. **Consider Lombok**: @Builder annotation for simple cases
6. **Handle nulls**: Validate or use Optional
7. **Copy collections**: Don't expose mutable collections
8. **Consider static factory**: `Builder.create()` instead of `new Builder()`

---

## 17. Common Mistakes

1. **Mutable product**: Product should be immutable
2. **No validation**: Not validating state at build time
3. **Exposing builder**: Making builder accessible to clients
4. **Over-engineering**: Using builder for simple objects
5. **Not copying collections**: Exposing mutable internal state
6. **Null handling**: Not handling null parameters

---

## 18. Pitfalls

- **Verbose**: More code than simple constructors
- **Extra object**: Builder object created and discarded
- **Memory overhead**: Temporary builder objects
- **Learning curve**: Team must understand pattern
- **Overuse**: Using builder when constructor suffices

---

## 19. Debugging Tips

1. **Add toString()**: To both builder and product
2. **Log build()**: Track when build is called
3. **Validate early**: Fail fast on invalid state
4. **Test edge cases**: Null values, empty strings
5. **Use IDE debugger**: Step through builder methods

---

## 20. Comparison Table

| Approach | Readability | Immutability | Validation | Code Volume |
|----------|-------------|--------------|------------|-------------|
| Constructor | Low (many params) | Yes | Yes | Low |
| JavaBeans | High | No | No | Medium |
| Builder | Very High | Yes | Yes | High |
| Record | High | Yes | Limited | Very Low |

---

## 21. Decision Tree

```
Need to create complex object?
├── Many parameters (>4)? → Builder
├── Many optional parameters? → Builder
├── Need immutability? → Builder or Record
├── Simple DTO? → Record
└── Few parameters? → Constructor
```

---

## 22. Interview Questions

### Q1: What problem does Builder pattern solve?
**Answer**: Telescoping constructors, optional parameters, immutability, and readable construction.

### Q2: Builder vs. Factory?
**Answer**: Factory creates different types. Builder constructs complex objects step by step. They serve different purposes.

### Q3: How to make Builder thread-safe?
**Answer**: Use CopyOnWriteArrayList for modifiers, synchronize build() method, or create new builder per thread.

### Q4: What about Lombok @Builder?
**Answer**: Convenient for simple cases, but manual implementation gives more control over validation and logic.

### Q5: Can Builder be reused?
**Answer**: Yes, but be careful. Modify fields and call build() again, or create new builder.

---

## 23. Exercises

### Exercise 1: Simple Builder
Create a Builder for a `UserProfile` with required fields (username, email) and optional fields (bio, avatar, age).

### Exercise 2: Validating Builder
Add validation to ensure email is valid and age is between 0-150.

### Exercise 3: Generic Builder
Create a generic Builder that works with any class using reflection.

---

## 24. Assignments

1. **Assignment 1**: Create a Builder for `QueryBuilder` that builds SQL queries
2. **Assignment 2**: Implement a thread-safe Builder for configuration objects
3. **Assignment 3**: Build a Builder with fluent API for test data generation

---

## 25. Mini Project

### Test Data Builder Framework
Create a framework that:
- Builds test objects with realistic data
- Supports required and optional fields
- Generates random data for optional fields
- Supports validation
- Works with any object type

---

## 26. Summary

- Builder separates construction from representation
- Provides fluent API for readable construction
- Supports immutability and validation
- Ideal for objects with many optional parameters
- Consider records for simple DTOs
- Use Lombok @Builder for simple cases

---

## 27. References

1. Gamma, E., et al. (1994). *Design Patterns*, Chapter 3
2. Bloch, J. (2018). *Effective Java*, Item 2
3. Refactoring Guru: https://refactoring.guru/design-patterns/builder
4. Java Design Patterns: https://java-design-patterns.com/patterns/builder/
