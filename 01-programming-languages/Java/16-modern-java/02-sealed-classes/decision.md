# Sealed Classes - Decision Guide

## Use Sealed Classes When

### You Have a Fixed Set of Subtypes
```java
sealed interface Payment permits CreditCard, BankTransfer, PayPal {}
```

### You Need Exhaustive Pattern Matching
The compiler can verify all cases are handled.

### You're Modeling Algebraic Data Types
```java
sealed interface Result<T> permits Success, Failure {}
record Success<T>(T value) implements Result<T> {}
record Failure<T>(Exception error) implements Result<T> {}
```

### API Design with Controlled Extension
Only known implementations should exist.

## Don't Use Sealed Classes When

### Extension is Genuinely Open
Third-party code needs to extend your class.

### Hierarchy is Dynamic
New subtypes are added at runtime (e.g., via reflection).

### You Need Deep Hierarchies
Sealed classes work best with flat hierarchies.

## Comparison with Alternatives

| Approach | Pros | Cons |
|----------|------|------|
| Sealed Classes | Exhaustive checking, controlled hierarchy | Restricted extension |
| Regular Classes | Open extension | No exhaustive checking |
| Enums | Fixed set, simple | No state, no inheritance |
| Interfaces | Flexible | No control over implementations |

## Best Practices

1. **Use records for leaf nodes** - Combine sealed with records
2. **Keep hierarchy flat** - Avoid deep sealed hierarchies
3. **Document reasoning** - Explain why hierarchy is sealed
4. **Consider pattern matching** - Sealed + pattern matching is powerful
