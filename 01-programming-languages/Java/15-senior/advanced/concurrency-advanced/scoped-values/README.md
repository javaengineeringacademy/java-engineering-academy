# Scoped Values

## Overview

Scoped Values (Preview in JDK 21+) provide a way to pass data down the call stack without explicit parameters. They offer automatic lifecycle management similar to try-with-resources.

## Basic Usage

```java
private static final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();

// Set value within a scope
ScopedValue.where(CURRENT_USER, user).run(() -> {
    // Value available here
    processRequest();
});

// Value automatically cleared after scope
```

## vs ThreadLocal

| Aspect | ScopedValue | ThreadLocal |
|--------|------------|-------------|
| Lifecycle | Scoped to block | Manual set/remove |
| Cleanup | Automatic | Manual (can leak) |
| Mutability | Immutable binding | Mutable state |
| Access | Within scope only | Any thread |
| Predictability | High | Low |

### ThreadLocal Issues
```java
// ThreadLocal can leak resources
ThreadLocal<Connection> conn = new ThreadLocal<>();
conn.set(getConnection());
// Must remember: conn.remove()
// If forgotten, connection leaks
```

### ScopedValue Solution
```java
// ScopedValue prevents leaks
ScopedValue.where(CONN, getConnection()).run(() -> {
    // Use connection
});
// Automatically cleaned up - no leak possible
```

## Propagation Rules

1. **Regular threads** - Do NOT inherit scoped values
2. **Virtual threads** - Do NOT inherit (must be in scope)
3. **StructuredTaskScope** - Forked tasks do NOT inherit
4. **Only direct call stack** - Values propagate down the call chain

## When to Use

### Good Candidates
- Request-scoped data (user, request ID)
- Context that should be thread-confined
- Replacing ThreadLocal for scoped data
- When automatic cleanup is important

### Consider Alternatives For
- Data that must be shared across threads
- Long-lived state
- Data that needs to outlive the scope

## Best Practices

1. Define as `private static final`
2. Use meaningful names (e.g., `CURRENT_USER`)
3. Prefer immutable values
4. Keep scopes minimal

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## See Also

- `ScopedValuesDemo.java` - Practical examples
- JEP 446: Scoped Values

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
