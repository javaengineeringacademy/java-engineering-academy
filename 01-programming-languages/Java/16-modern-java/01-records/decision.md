# Records - Decision Guide

## Use Records When

### Data is Immutable
Records are implicitly final and all components are final. Perfect for:
- Value objects (Money, Coordinate, Range)
- DTOs for data transfer
- Events and messages
- Configuration data

### You Need Structural Equality
Records automatically implement equals() based on component values.

### You Want Concise Code
```java
// Before: 50+ lines of boilerplate
// After: 1 line
record Point(int x, int y) {}
```

## Don't Use Records When

### Mutable State is Needed
Records cannot have mutable instance fields. Use classes instead.

### You Need to Extend Classes
Records implicitly extend `java.lang.Record`. You cannot extend other classes.

### Complex Validation Logic
While compact constructors help, complex initialization logic may be better in a class.

### Builder Pattern is Required
Records don't support step-by-step construction. Consider a builder pattern with a record.

## Comparison with Alternatives

| Aspect | Record | Class | Lombok @Value |
|--------|--------|-------|---------------|
| Immutability | Enforced | Manual | Enforced |
| equals/hashCode | Auto-generated | Manual | Auto-generated |
| toString | Auto-generated | Manual | Auto-generated |
| Extensibility | No | Yes | No |
| Boilerplate | Minimal | High | Low |
| Validation | Compact constructor | Constructor | Limited |
