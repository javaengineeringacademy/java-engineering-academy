# Structured Concurrency

## Overview

Structured Concurrency (Preview in JDK 21+) provides a way to scope concurrent tasks to a block, ensuring better error handling and lifecycle management.

## Key Concepts

### StructuredTaskScope
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> task1 = scope.fork(() -> { /* work */ });
    Subtask<String> task2 = scope.fork(() -> { /* work */ });
    
    scope.join();  // Wait for all tasks
    
    String result1 = task1.get();
    String result2 = task2.get();
}
// All tasks guaranteed to be complete or cancelled
```

### Two Policies

1. **ShutdownOnFailure** - Cancel all if any fails
2. **ShutdownOnSuccess** - Return first success, cancel rest

## vs CompletableFuture

| Aspect | Structured Concurrency | CompletableFuture |
|--------|----------------------|-------------------|
| Lifecycle | Scoped to block | May outlive scope |
| Error handling | Automatic propagation | Manual exception handling |
| Cancellation | Automatic on scope exit | Manual cancellation |
| Readability | Linear, imperative | Chain-based, functional |
| Resource cleanup | Guaranteed | Not guaranteed |

## When to Use

### Good Candidates
- Request-scoped operations
- Operations that must complete or fail together
- When you need guaranteed cleanup
- When error handling is critical

### Consider Alternatives For
- Long-running background tasks
- Fire-and-forget operations
- When you need manual lifecycle control

## Migration from CompletableFuture

### Before (CompletableFuture)
```java
CompletableFuture<User> userFuture = getUserAsync();
CompletableFuture<Order> orderFuture = getOrderAsync();

CompletableFuture.allOf(userFuture, orderFuture).join();

User user = userFuture.join();
Order order = orderFuture.join();
```

### After (Structured Concurrency)
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<User> userTask = scope.fork(() -> getUser());
    Subtask<Order> orderTask = scope.fork(() -> getOrder());
    
    scope.join();
    scope.throwIfFailed();
    
    User user = userTask.get();
    Order order = orderTask.get();
}
```

## Best Practices

1. Use try-with-resources for automatic cleanup
2. Handle exceptions with `throwIfFailed()`
3. Use appropriate policy (ShutdownOnFailure vs ShutdownOnSuccess)
4. Keep scope lifetime minimal

## See Also

- `StructuredConcurrencyDemo.java` - Practical examples
- JEP 453: Structured Concurrency
