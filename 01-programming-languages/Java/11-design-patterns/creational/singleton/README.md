# Singleton Pattern

## Overview
Singleton ensures a class has only one instance and provides a global point of access to it.

## When to Use
- Database connection pools
- Configuration managers
- Logging services
- Cache managers
- Thread pools

## Implementation Approaches

### Double-Checked Locking
```java
if (instance == null) {
    synchronized (Singleton.class) {
        if (instance == null) {
            instance = new Singleton();
        }
    }
}
```

### Static Holder
```java
private static class Holder {
    private static final Singleton INSTANCE = new Singleton();
}
```

### Enum Singleton
```java
public enum Singleton {
    INSTANCE;
}
```

## Thread Safety
- Double-checked locking requires `volatile` keyword
- Static holder is inherently thread-safe
- Enum approach is thread-safe by JVM guarantee

## Common Mistakes
1. Using Singleton when dependency injection works better
2. Forgetting volatile in double-checked locking
3. Making Singleton testable (static methods hard to mock)
4. Overusing Singleton as global state

## Interview Questions
1. What are the three ways to implement Singleton in Java?
2. Why is double-checked locking thread-safe?
3. What is the advantage of enum Singleton?
4. When should you avoid Singleton?
5. How does Static Holder pattern achieve lazy initialization?

## Performance

[Performance considerations and benchmarks]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Examples

[Code examples demonstrating the concept]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
