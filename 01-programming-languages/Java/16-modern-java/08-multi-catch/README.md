# Multi-catch (Java 7)

Multi-catch allows you to catch multiple exception types in a single catch block, reducing code duplication when different exceptions are handled the same way.

## Key Features

- **Single catch block** - Handle multiple exception types
- **Reduced duplication** - Same handling for different exceptions
- **Type safety** - Exception variable is implicitly final
- **Pipe separator** - Use `|` to separate exception types

## Syntax

```java
try {
    // code that may throw exceptions
} catch (IOException | SQLException | ParseException e) {
    // handle all three exceptions the same way
    log.error("Error: " + e.getMessage());
}
```

## Rules

1. Exception types must be unrelated (no inheritance relationship)
2. Only one variable name for the multi-catch
3. The variable is implicitly final
4. Can catch checked and unchecked exceptions

## Examples

```java
// Basic multi-catch
try {
    riskyOperation();
} catch (IOException | SQLException e) {
    handleError(e);
}

// With finally
try {
    riskyOperation();
} catch (IOException | SQLException e) {
    handleError(e);
} finally {
    cleanup();
}

// In lambdas
list.forEach(item -> {
    try {
        process(item);
    } catch (IOException | SQLException e) {
        log.error("Error processing item", e);
    }
});
```

## When to Use

- Multiple exceptions handled identically
- Reducing catch block duplication
- When exception types are unrelated

## When NOT to Use

- Different handling for different exceptions
- Exception types are related (use parent type)
- Need different recovery strategies
