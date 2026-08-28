# Multi-catch Internals

## Bytecode Generation

Multi-catch is syntactic sugar that the compiler expands:

### Compiler Transformation
```java
// Your code:
try {
    riskyOperation();
} catch (IOException | SQLException e) {
    handleError(e);
}

// Compiler generates (conceptually):
try {
    riskyOperation();
} catch (IOException e) {
    handleError(e);
} catch (SQLException e) {
    handleError(e);
}
```

### Exception Table
The compiler creates multiple entries in the exception table, one for each exception type.

### Variable Binding
The same variable name is used in each generated catch block.

## Performance

Multi-catch has the same performance as separate catch blocks:
- Same exception table entries
- Same stack unwinding
- Same catch block execution

## Compiler Checks

1. **Exception types must be unrelated** - No inheritance relationship
2. **Single variable name** - All exceptions bind to same variable
3. **Effectively final** - Variable cannot be reassigned
4. **Accessible** - Exception types must be accessible in scope

## Best Practices

1. **Use when handling is identical** - Don't combine if logic differs
2. **Consider parent type** - If exceptions share a common parent
3. **Document reasoning** - Comment if non-obvious grouping
4. **Keep catch blocks simple** - Complex logic belongs in methods
