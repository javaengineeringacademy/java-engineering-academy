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

## Overview

`CompletableFuture` is Java's implementation of the `CompletionStage` interface, enabling non-blocking asynchronous composition with functional-style chaining. Introduced in Java 8, it supports parallel execution, error handling, and combining multiple async operations without blocking threads.

## Interview Questions

1. **What is the difference between `thenApply`, `thenCompose`, and `thenAccept`?**
   - `thenApply(Function<T,R>)`: transforms the result synchronously. Returns `CompletableFuture<R>`. Equivalent to `map()` in functional programming.
   - `thenCompose(Function<T,CompletableFuture<R>>)`: chains async operations. Flattens nested `CompletableFuture<CompletableFuture<R>>` into `CompletableFuture<R>`. Equivalent to `flatMap()`.
   - `thenAccept(Consumer<T>)`: performs side effects, returns `CompletableFuture<Void>`.
   Rule of thumb: use `thenApply` for sync transforms, `thenCompose` when the next step is itself async.

2. **Explain the difference between `join()` and `get()`. When would you use each?**
   `get()` throws checked `InterruptedException` and `ExecutionException`. Use when you need to handle interruption (e.g., in a service that respects thread interruption). `join()` throws unchecked `CompletionException`. Use in async chains and lambdas where checked exceptions are cumbersome. Neither should be called in async callbacks (blocks the thread). Use `orTimeout()` (Java 9+) for deadline-based waiting.

3. **How does `CompletableFuture` handle exceptions in chained operations?**
   Exception handling follows the async chain: if a stage throws, downstream stages are skipped until an error handler is reached. `exceptionally(fn)` catches exceptions and provides a fallback. `handle(fn)` receives both result and exception (useful for cleanup). `whenComplete(fn)` runs after completion regardless of success/failure but doesn't transform the result. Unhandled exceptions propagate to the terminal `get()`/`join()`.

4. **What is the default executor for `CompletableFuture` and why is it important?**
   The default executor is `ForkJoinPool.commonPool()` (size = cores - 1). If all async operations use the common pool and one blocks (e.g., on I/O), it starves other `CompletableFuture` chains and parallel streams. Solution: supply a custom executor via `supplyAsync(() -> ..., customExecutor)` for blocking operations. CPU-bound tasks can safely use the common pool.

5. **How do you combine three or more futures and wait for all results?**
   Use `CompletableFuture.allOf()`:
   ```java
   CompletableFuture<User> userF = fetchUser();
   CompletableFuture<Order> orderF = fetchOrder();
   CompletableFuture<Payment> payF = fetchPayment();
   CompletableFuture.allOf(userF, orderF, payF).join();
   return new Receipt(userF.join(), orderF.join(), payF.join());
   ```
   For partial results, use `thenCombine`/`thenCompose` chains. For first-success racing, use `anyOf()`.

6. **How would you implement a retry mechanism with `CompletableFuture`?**
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
   Exponential backoff prevents thundering herd. `exceptionallyCompose` chains a new future on failure. `delayedExecutor` schedules the retry without blocking.

7. **Compare `CompletableFuture` with `CompletionStage`. Why does the interface exist?**
   `CompletionStage` is the interface defining composition methods (`thenApply`, `thenCompose`, etc.). `CompletableFuture` is the primary implementation. The interface exists to allow alternative implementations (e.g., in reactive frameworks like Project Reactor or RxJava that implement `CompletionStage`). Use `CompletableFuture` directly for most cases; use `CompletionStage` as a parameter type when you want implementation flexibility.

8. **What happens when an exception occurs inside a `thenApply` lambda?**
   The exception is wrapped in a `CompletionException` and the returned future completes exceptionally. Downstream stages are skipped until an error handler (`exceptionally`, `handle`) is encountered. If no handler exists, the exception propagates to `join()`/`get()`. The original exception is accessible via `CompletionException.getCause()`.

## Performance

| Operation | Latency | Notes |
|-----------|---------|-------|
| `supplyAsync` (common pool) | ~1-5 μs | Task submission overhead |
| `thenApply` (sync) | ~0.1 μs | No thread switch |
| `thenApplyAsync` | ~1-5 μs | Thread pool submission |
| `get()` (blocking) | ~2000 ns | Context switch + parking |
| `join()` (non-blocking) | ~50 ns | If already completed |
| `allOf` (10 futures) | ~10 μs | Completion tracking |

- Custom executors add ~1-5 μs per submission vs common pool
- Chaining 10 async stages: ~50-100 μs total (vs ~500 μs with blocking `get()`)
- `CompletableFuture` scales better than blocking threads for I/O-bound workloads

## Examples

```java
// Production: microservice aggregation with circuit breaker
public class OrderService {
    private final CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("payment");

    public CompletableFuture<OrderResult> createOrder(OrderRequest req) {
        CompletableFuture<User> userF = userClient.getUser(req.userId());
        CompletableFuture<Inventory> stockF = inventoryClient.checkStock(req.items());
        CompletableFuture<Boolean> fraudF = fraudClient.checkTransaction(req);

        return userF.thenCombine(stockF, (user, stock) -> {
            validateInventory(stock, req.items());
            return new OrderDraft(user, req);
        }).thenCompose(draft -> {
            if (!circuitBreaker.allowRequest()) {
                return CompletableFuture.failedFuture(new CircuitOpenException());
            }
            return fraudF.thenCompose(isFraud -> {
                if (isFraud) return failedFuture(new FraudDetectedException());
                return paymentClient.charge(draft.total());
            });
        }).thenApply(payment -> new OrderResult(req, payment, CONFIRMED))
          .exceptionallyCompose(ex -> CompletableFuture.completedFuture(
              new OrderResult(req, null, FAILED)));
    }
}

// Parallel independent operations with timeout
CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> callService1());
CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> callService2());
CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> callService3());

CompletableFuture<List<String>> results = CompletableFuture
    .allOf(f1, f2, f3)
    .thenApply(v -> List.of(f1.join(), f2.join(), f3.join()));

List<String> data = results.orTimeout(5, TimeUnit.SECONDS).join();
```

## Internal Working

- **Completion stack**: `CompletableFuture` uses a Treiber stack of `UniCompletion` nodes. Each `thenApply`/`thenCompose` pushes a `UniCompletion` onto the stack. When a stage completes, it pops all pending completions and runs them.
- **Executor submission**: If no executor is specified, `ForkJoinPool.commonPool()` is used. The completion is submitted as a `ForkJoinTask` (extends `Runnable`/`Callable`). Sync continuations (`thenApply`) run on the completing thread—no executor submission.
- **CAS-based stack**: `Unsafe.compareAndSwapObject` on the stack head for lock-free push/pop. contention is low because completions are pushed rarely and popped on completion.
- **`allOf` implementation**: Creates a `BiCompletion` that counts down remaining tasks using `AtomicInteger`. When count reaches 0, the combined future completes.
- **Thread propagation**: Sync stages (`thenApply`) run on the thread that completes the previous stage. Async stages (`thenApplyAsync`) always run on the specified executor.

## Why This Concept Exists

Before `CompletableFuture`, Java async programming relied on `Future` (no composition), callbacks (callback hell), or `ExecutorService` (manual thread management). Problems solved:

1. **Callback hell**: `CompletableFuture` chains replace deeply nested callbacks with flat, readable pipelines.
2. **Composition**: Combine parallel operations (`allOf`, `thenCombine`) without manual synchronization.
3. **Error propagation**: Exceptions flow through chains like synchronous try-catch, instead of being swallowed.
4. **Non-blocking**: Async callbacks avoid blocking threads while waiting for results.
5. **Timeout support**: `orTimeout()` (Java 9+) prevents indefinite waiting on slow services.

`CompletableFuture` bridges imperative and reactive programming, providing a familiar API for developers transitioning from blocking to non-blocking code.

## Related Topics
- [Virtual Threads](../virtual-threads/) — Simpler concurrency alternative
- [Fork/Join](../fork-join/) — Parallel processing
- Thread Pools — ExecutorService basics
- [Structured Concurrency](../structured-concurrency/) — Future of async
- JMM — Memory visibility in async

## Pitfalls

- **Blocking inside `thenApply`/`thenCompose`**: defeats the purpose. Use `thenApplyAsync` with a dedicated executor for blocking I/O.
- **Swallowing exceptions**: forgetting `exceptionally`/`handle` means errors propagate to `join()` and crash.
- **Overusing `get()`**: calling `get()` in async chains blocks the thread pool. Prefer `join()` in non-interruptible contexts or async chaining.
- **Creating executors per request**: creates thread explosion. Use shared executors or `ForkJoinPool.commonPool()` for CPU-bound tasks.
- **Ignoring cancellation**: `CompletableFuture.cancel()` only sets the interrupt flag; the task continues running unless explicitly checked.

## References

- [Oracle: CompletableFuture API](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
- [Javadoc: CompletionStage](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionStage.html)
- [Baeldung: CompletableFuture Guide](https://www.baeldung.com/java-completablefuture)
- [JEP 123: Scalable Variable-Length Handles](https://openjdk.org/jeps/123)
- [Aysel Fatullayev: CompletableFuture Patterns](https://zeroturnaround.com/rebellabs/java-8-completablefuture-cheat-sheet/)
