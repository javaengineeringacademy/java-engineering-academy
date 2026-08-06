# Builder Pattern

## Overview
Builder separates object construction from representation, allowing step-by-step creation of complex objects.

## When to Use
- Objects with many optional parameters
- Avoiding telescoping constructors
- Creating immutable objects
- When construction involves multiple steps

## Code Structure

### Product with Nested Builder
```java
public class User {
    private final String firstName;
    private final String lastName;

    private User(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    public static class Builder {
        public Builder firstName(String fn) { ... return this; }
        public User build() { return new User(this); }
    }
}
```

### Usage
```java
User user = new User.Builder()
    .firstName("John")
    .lastName("Doe")
    .age(30)
    .build();
```

## Common Mistakes
1. Forgetting to make fields final in product
2. Not validating in build() method
3. Overcomplicating with too many builder methods
4. Creating mutable builder for immutable product

## Interview Questions
1. What problem does Builder solve?
2. How does Builder differ from Factory Method?
3. What are the benefits of nested Builder class?
4. When would you use Builder over Constructor?
5. How do you handle optional parameters with Builder?

## Performance

Builder adds ~5-20ns overhead for method chaining compared to a direct constructor. This is negligible for most applications. The real benefit is readability and maintainability — telescoping constructors with 8+ parameters are error-prone. Lombok `@Builder` eliminates boilerplate with zero runtime cost. For immutable objects, the builder pattern is the standard approach.

## Examples

```java
// HTTP request builder
class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final String body;
    private final int timeoutMs;
    
    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
        this.timeoutMs = builder.timeoutMs;
    }
    
    public static class Builder {
        private final String url;
        private String method = "GET";
        private final Map<String, String> headers = new HashMap<>();
        private String body;
        private int timeoutMs = 30000;
        
        public Builder(String url) { this.url = url; }
        
        public Builder method(String method) {
            this.method = method;
            return this;
        }
        
        public Builder header(String key, String value) {
            headers.put(key, value);
            return this;
        }
        
        public Builder body(String body) {
            this.body = body;
            return this;
        }
        
        public Builder timeout(int ms) {
            this.timeoutMs = ms;
            return this;
        }
        
        public HttpRequest build() {
            if (url == null || url.isBlank()) {
                throw new IllegalArgumentException("URL is required");
            }
            return new HttpRequest(this);
        }
    }
}

// Usage
HttpRequest request = new HttpRequest.Builder("https://api.example.com/users")
    .method("POST")
    .header("Content-Type", "application/json")
    .body("{\"name\": \"Alice\"}")
    .timeout(5000)
    .build();
```

## Internal Working

The builder is a static inner class that holds mutable copies of all fields. Each setter method returns `this` (the builder instance) enabling method chaining. The `build()` method validates accumulated state and constructs the final immutable object by passing the builder to the product's private constructor. The builder has no magic — it is syntactic sugar for a parameter object.

## Why This Concept Exists

When a class has more than 4-5 constructor parameters, especially with optional ones, the telescoping constructor anti-pattern emerges: multiple constructors with different parameter combinations. This is hard to read and error-prone (swapping two String parameters). Builder makes construction explicit, readable, and validates at build time. It also enables immutable objects — all fields are set once in the constructor.

## Pitfalls

1. **Boilerplate**: Without Lombok, builders add significant code (3x the product class)
2. **Validation timing**: Validation in `build()` means errors are caught late — consider fail-fast
3. **Incomplete builds**: Calling `build()` before setting required fields may produce invalid objects
4. **Mutable builder**: If the builder is mutable, thread safety is your responsibility
5. **Overuse**: Simple objects with 2-3 fields don't need a builder

## References

- [Effective Java - Item 2: Consider a builder when faced with many constructor parameters](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Refactoring.Guru - Builder Pattern](https://refactoring.guru/design-patterns/builder)
- [Lombok @Builder Documentation](https://projectlombok.org/features/Builder)
