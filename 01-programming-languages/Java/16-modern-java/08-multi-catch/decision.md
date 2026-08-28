# Multi-catch - Decision Guide

## Use Multi-catch When

### Same Handling for Different Exceptions
```java
// Before
try {
    riskyOperation();
} catch (IOException e) {
    log.error("Error: " + e.getMessage());
} catch (SQLException e) {
    log.error("Error: " + e.getMessage());
} catch (ParseException e) {
    log.error("Error: " + e.getMessage());
}

// After
try {
    riskyOperation();
} catch (IOException | SQLException | ParseException e) {
    log.error("Error: " + e.getMessage());
}
```

### Exception Types are Unrelated
When exceptions don't share a common useful parent type.

### Reducing Code Duplication
When catch blocks would be identical.

## Don't Use Multi-catch When

### Different Handling Needed
When each exception requires different recovery logic.

### Exception Types are Related
When you can catch the parent type instead.

### Need Exception-Specific Information
When you need to access exception-specific methods.

## Comparison with Alternatives

| Approach | Pros | Cons |
|----------|------|------|
| Multi-catch | Concise, less duplication | Single variable |
| Separate catches | Specific handling | More verbose |
| Common parent type | Simple | May catch unrelated exceptions |

## Best Practices

1. **Only when handling is identical** - Don't combine if logic differs
2. **Use descriptive variable name** - `e` is often sufficient
3. **Consider exception hierarchy** - May be a common parent type
4. **Document why** - Comment if non-obvious why these are grouped
