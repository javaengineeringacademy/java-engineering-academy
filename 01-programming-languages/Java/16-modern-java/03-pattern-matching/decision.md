# Pattern Matching - Decision Guide

## Use Pattern Matching When

### Replacing instanceof Chains
```java
// Before
if (obj instanceof String) {
    String s = (String) obj;
    // use s
} else if (obj instanceof Integer) {
    Integer i = (Integer) obj;
    // use i
}

// After
String result = switch (obj) {
    case String s -> "String: " + s;
    case Integer i -> "Integer: " + i;
    default -> "Unknown";
};
```

### Complex Type Checking
When you need to check type AND condition:
```java
case String s && s.length() > 10 -> "Long string";
```

### Sealed Class Hierarchies
Exhaustive checking ensures all cases are handled.

## Don't Use Pattern Matching When

### Simple Equality Checks
Use regular switch or if-else for value comparisons.

### Only One Type to Check
Simple instanceof is sufficient.

### Runtime Type Determination
When types are only known at runtime via reflection.

## Comparison with Alternatives

| Approach | Pros | Cons |
|----------|------|------|
| Pattern Matching | Concise, safe | Requires Java 16+ |
| instanceof chains | Compatible | Verbose, error-prone |
| Visitor pattern | Extensible | Complex setup |
| Reflection | Dynamic | Slow, unsafe |

## Best Practices

1. **Handle null explicitly** - Don't rely on default
2. **Order matters** - Put more specific patterns first
3. **Use guards sparingly** - Complex conditions reduce readability
4. **Combine with sealed** - Best for exhaustive checking
