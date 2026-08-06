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

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
