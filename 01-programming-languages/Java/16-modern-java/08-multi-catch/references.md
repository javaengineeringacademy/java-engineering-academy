# Multi-catch References

## Official Documentation

- [Java Language Specification - Catch Clauses](https://docs.oracle.com/javase/specs/jls/se17/html/jls-14.html#jls-14.20)
- [Java Tutorial - Catching Multiple Exception Types](https://docs.oracle.com/javase/tutorial/essential/catch/multicatch.html)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Multi-catch | Catching multiple exception types in one catch block |
| Pipe Separator | `\|` used to separate exception types |
| Effectively Final | Exception variable cannot be reassigned |
| Unrelated Types | Exception types should not share inheritance |

## Code Examples

### Basic Multi-catch
```java
try {
    riskyOperation();
} catch (IOException | SQLException e) {
    handleError(e);
}
```

### With Finally
```java
try {
    riskyOperation();
} catch (IOException | SQLException e) {
    handleError(e);
} finally {
    cleanup();
}
```

### In Lambda
```java
list.forEach(item -> {
    try {
        process(item);
    } catch (IOException | SQLException e) {
        log.error("Error", e);
    }
});
```

## Common Patterns

1. **IO operations:** `catch (IOException | URISyntaxException e)`
2. **Database operations:** `catch (SQLException | DatabaseException e)`
3. **Parsing:** `catch (ParseException | NumberFormatException e)`
4. **Reflection:** `catch (ClassNotFoundException | NoSuchMethodException e)`
