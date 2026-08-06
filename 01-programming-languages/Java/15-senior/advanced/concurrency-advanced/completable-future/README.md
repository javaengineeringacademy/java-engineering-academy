# CompletableFuture Patterns Guide

## Real-World Patterns

### Parallel API Calls with Timeout
Fetch multiple resources concurrently with a global timeout:
```java
CompletableFuture<String> api1 = CompletableFuture.supplyAsync(() -> callApi1());
CompletableFuture<String> api2 = CompletableFuture.supplyAsync(() -> callApi2());

CompletableFuture<List<String>> all = CompletableFuture
    .allOf(api1, api2)
    .thenApply(v -> List.of(api1.join(), api2.join()));

List<String> results = all.get(5, TimeUnit.SECONDS);
```

### Fallback Chain
Try primary service, fall back to alternatives:
```java
CompletableFuture<String> result = primaryService()
    .exceptionallyCompose(ex -> fallbackService1())
    .exceptionallyCompose(ex -> fallbackService2())
    .exceptionallyCompose(ex -> CompletableFuture.completedFuture("default"));
```

### Retry with Exponential Backoff
```java
public static <T> CompletableFuture<T> retry(Supplier<CompletableFuture<T>> task,
        int maxRetries, long delayMs) {
    return task.get()
        .exceptionallyCompose(ex -> {
            if (maxRetries <= 0) return failedFuture(ex);
            return delayedExecutor(delayMs, MILLISECONDS)
                .thenCompose(v -> retry(task, maxRetries - 1, delayMs * 2));
        });
}
```

## Production Usage

### Circuit Breaker Pattern
```java
private final AtomicInteger failures = new AtomicInteger(0);
private final AtomicLong lastFailureTime = new AtomicLong(0);

public CompletableFuture<String> callWithCircuitBreaker() {
    if (failures.get() >= 5 && System.currentTimeMillis() - lastFailureTime.get() < 30000) {
        return CompletableFuture.failedFuture(new RuntimeException("Circuit open"));
    }
    return callService()
        .whenComplete((r, ex) -> {
            if (ex != null) {
                failures.incrementAndGet();
                lastFailureTime.set(System.currentTimeMillis());
            } else {
                failures.set(0);
            }
        });
}
```

### Rate Limiting with Semaphore
```java
private final Semaphore semaphore = new Semaphore(10);

public CompletableFuture<String> callWithRateLimit() {
    return CompletableFuture.supplyAsync(() -> {
        semaphore.acquire();
        try {
            return callExternalService();
        } finally {
            semaphore.release();
        }
    });
}
```

### Combining Results
```java
CompletableFuture<User> userFuture = fetchUser(userId);
CompletableFuture<List<Order>> ordersFuture = fetchOrders(userId);
CompletableFuture<Profile> profileFuture = fetchProfile(userId);

CompletableFuture<UserProfile> combined = userFuture
    .thenCombine(ordersFuture, (user, orders) -> new UserWithOrders(user, orders))
    .thenCombine(profileFuture, (userWithOrders, profile) -> new UserProfile(userWithOrders, profile));
```

## Best Practices

1. **Always set timeouts** on `get()` calls
2. **Use `exceptionally`** not try-catch for async errors
3. **Prefer `thenCompose`** over `thenApply` for async chaining
4. **Use `allOf`** for parallel independent tasks
5. **Handle cancellation** in long-running tasks
6. **Avoid blocking** in async callbacks
7. **Use custom executors** for different workload types
8. **Log errors** in `exceptionally` and `handle` blocks

## Common Pitfalls

| Pitfall | Solution |
|---------|----------|
| Blocking on `get()` inside async | Use `thenCompose` chain |
| Ignoring exceptions | Always add `exceptionally` |
| Creating too many threads | Use shared executor |
| No timeout on external calls | Use `orTimeout()` |
| Swallowing errors silently | Log in `handle()` |

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

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Related Topics
- [Virtual Threads](../virtual-threads/) — Simpler concurrency alternative
- [Fork/Join](../fork-join/) — Parallel processing
- Thread Pools — ExecutorService basics
- [Structured Concurrency](../structured-concurrency/) — Future of async
- JMM — Memory visibility in async

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
