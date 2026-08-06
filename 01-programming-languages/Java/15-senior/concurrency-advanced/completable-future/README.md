# CompletableFuture Deep Dive

## API Reference

### Creation Methods

| Method | Description |
|--------|-------------|
| `completedFuture(value)` | Creates an already-completed future |
| `supplyAsync(Supplier)` | Runs supplier on ForkJoinPool.commonPool |
| `supplyAsync(Supplier, Executor)` | Runs supplier on custom executor |
| `runAsync(Runnable)` | Runs runnable, returns Void future |
| `failedFuture(ex)` | Creates an already-failed future |

### Chaining Methods

| Method | Input | Output | Use Case |
|--------|-------|--------|----------|
| `thenApply(Function)` | Result | Mapped result | Transform values |
| `thenApplyAsync(Function)` | Result | Mapped result | Offload to another thread |
| `thenAccept(Consumer)` | Result | Void | Side-effect processing |
| `thenCompose(Function)` | Result | Future | Flat-map / chain async |
| `thenCombine(Future, BiFunction)` | Two results | Combined | Merge parallel results |
| `thenAcceptAll(Future... )` | All results | Void | Process all results |

### Exception Handling

| Method | Behavior |
|--------|----------|
| `exceptionally(Function)` | Returns fallback on exception, swallows error |
| `handle(BiFunction)` | Always runs; receives (result, exception) |
| `whenComplete(BiConsumer)` | Always runs; cannot transform result |
| `completeExceptionally(ex)` | Complete exceptionally from outside |

### Timeout Methods

| Method | Description |
|--------|-------------|
| `orTimeout(timeout, unit)` | Fails with TimeoutException |
| `completeOnTimeout(value, timeout, unit)` | Returns default on timeout |

### Composition

| Method | Description |
|--------|-------------|
| `allOf(Future...)` | Waits for all futures |
| `anyOf(Future...)` | Resolves when first completes |

---

## When to Use Which Method

### thenApply vs thenCompose

```java
// thenApply: synchronous transformation
future.thenApply(x -> x * 2);

// thenCompose: returns a new CompletableFuture (flat-map)
future.thenCompose(x -> anotherAsyncOperation(x));
```

Use `thenApply` for simple transformations. Use `thenCompose` when the
transformation itself returns a `CompletableFuture`.

### thenCombine vs allOf

- `thenCombine`: merges exactly two futures into one result
- `allOf`: waits for N futures, returns `CompletableFuture<Void>`

### handle vs exceptionally

- `exceptionally`: only handles errors, cannot access success path
- `handle`: always invoked, can inspect both result and exception

---

## Common Patterns

### Parallel API Aggregation

```java
CompletableFuture<User> userF = fetchUser(id);
CompletableFuture<Orders> ordersF = fetchOrders(id);
CompletableFuture<Balance> balanceF = fetchBalance(id);

return CompletableFuture.allOf(userF, ordersF, balanceF)
    .thenApply(v -> new Dashboard(userF.join(), ordersF.join(), balanceF.join()));
```

### Fallback Chain

```java
CompletableFuture.supplyAsync(() -> primaryService.call())
    .exceptionally(ex -> cache.get(key))
    .exceptionally(ex -> defaultService.call())
    .thenAccept(result -> saveToLocal(result));
```

### Timeout + Fallback

```java
CompletableFuture.supplyAsync(() -> slowService.call())
    .orTimeout(2, TimeUnit.SECONDS)
    .exceptionally(ex -> "fallback")
    .thenAccept(this::process);
```

---

## Error Handling Strategies

1. **Always provide a fallback** for external calls
2. **Log exceptions** in `whenComplete` before applying fallback
3. **Use `handle`** when you need both success and error paths
4. **Avoid `join()`** without timeout in production code
5. **Use `completeOnTimeout`** for graceful degradation
6. **Chain exceptionally calls** for multi-level fallbacks

---

## Common Pitfalls

- Blocking inside `supplyAsync` defeats the purpose
- Forgetting to handle exceptions causes silent failures
- Not using custom executors for I/O-bound work
- Using `get()` instead of `join()` (checked vs unchecked exception)
- Creating too many futures without composition (fan-out without fan-in)
