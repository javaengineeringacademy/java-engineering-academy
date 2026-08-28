# instanceof Pattern Matching - Decision Guide

## Use instanceof Pattern When

### Type Checking + Casting
```java
// Before
if (obj instanceof String) {
    String s = (String) obj;
    process(s);
}

// After
if (obj instanceof String s) {
    process(s);
}
```

### Complex Conditions
```java
if (obj instanceof String s && s.length() > 5 && !s.isEmpty()) {
    // s is a non-empty String with length > 5
}
```

### Replacing instanceof Chains
```java
if (obj instanceof String s) {
    handleString(s);
} else if (obj instanceof Integer i) {
    handleInteger(i);
} else if (obj instanceof List<?> list) {
    handleList(list);
}
```

## Don't Use instanceof Pattern When

### No Casting Needed
Simple `instanceof` check is sufficient.

### Multiple Conditions Without Type
Use regular boolean expressions.

### Pattern Variable Not Used
If you don't need the variable, use plain instanceof.

## Comparison with Alternatives

| Approach | Pros | Cons |
|----------|------|------|
| Pattern matching | Concise, safe | Requires Java 16+ |
| instanceof + cast | Compatible | Verbose, error-prone |
| Visitor pattern | Extensible | Complex |

## Best Practices

1. **Use when type + cast needed** - Pattern matching shines here
2. **Keep guards simple** - Complex conditions reduce readability
3. **Consider scope** - Pattern variable scope follows definite assignment
4. **Use with sealed types** - For exhaustive checking
