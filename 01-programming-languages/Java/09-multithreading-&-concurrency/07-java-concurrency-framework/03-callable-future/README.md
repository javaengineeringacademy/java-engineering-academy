# Callable and Future - Comprehensive Guide

## Overview

`Runnable` and `Callable` are interfaces for defining tasks that can be executed by threads or thread pools. `Future` represents the result of an asynchronous computation, providing methods to check status, retrieve results, and cancel tasks.

---

## Runnable Interface

```java
@FunctionalInterface
public interface Runnable {
    void run();
}
```

### Key Characteristics

| Feature | Detail |
|---------|--------|
| Return value | None (`void`) |
| Checked exceptions | Cannot throw checked exceptions |
| Submission to ExecutorService | `executor.submit(Runnable)` returns `Future<Void>` |
| Thread.start() | Can be passed directly |
| Lambda syntax | `() -> { ... }` or `() -> expression` |

### Examples

```java
// Simple Runnable
Runnable task = () -> System.out.println("Running");
new Thread(task).start();

// Submit to ExecutorService
Future<Void> future = executor.submit(() -> {
    System.out.println("Running in thread pool");
});
// future.get() returns null
```

---

## Callable Interface

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

### Key Characteristics

| Feature | Detail |
|---------|--------|
| Return value | Yes, generic type `V` |
| Checked exceptions | Can throw checked exceptions |
| Submission to ExecutorService | `executor.submit(Callable)` returns `Future<V>` |
| Thread.start() | Cannot be used directly (no `start(Callable)`) |
| Lambda syntax | `() -> { return value; }` or `() -> expression` |

### Examples

```java
// Simple Callable
Callable<Long> sumTask = () -> {
    long sum = 0;
    for (int i = 1; i <= 1000000; i++) sum += i;
    return sum;
};

// Submit to ExecutorService
Future<Long> future = executor.submit(sumTask);
Long result = future.get(); // Blocks until result available
```

---

## Future Interface

```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) throws ... TimeoutException;
}
```

### Method Reference

| Method | Description | Blocking? |
|--------|-------------|-----------|
| `get()` | Block until result available, throws `ExecutionException` if task failed | Yes |
| `get(timeout, unit)` | Block with timeout, throws `TimeoutException` if timeout expires | Yes, bounded |
| `isDone()` | Check if completed (normal, exception, or cancelled) | No |
| `cancel(mayInterrupt)` | Attempt to cancel; returns `false` if already started and non-interruptible | No |
| `isCancelled()` | Check if cancelled before completion | No |

### Future.get() Exceptions

| Exception | Cause |
|-----------|-------|
| `InterruptedException` | Thread was interrupted while waiting |
| `ExecutionException` | Task threw an exception; get the cause via `getCause()` |
| `TimeoutException` | `get(timeout, unit)` timed out |

---

## ExecutorService.submit() Returns

| Submit Method | Returns |
|---------------|---------|
| `submit(Runnable)` | `Future<Void>` — `get()` returns `null` |
| `submit(Callable<V>)` | `Future<V>` — `get()` returns the result |

---

## CompletableFuture (Java 8+)

`CompletableFuture` extends `Future` with composition methods for non-blocking chaining.

### Creation

```java
CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<Void> run = CompletableFuture.runAsync(() -> System.out.println("Running"));
```

### Chaining Methods

| Method | Description | Signature |
|--------|-------------|-----------|
| `thenApply(fn)` | Transform result synchronously | `CF<U>` — like `map()` |
| `thenApplyAsync(fn)` | Transform result on another thread | `CF<U>` |
| `thenCompose(fn)` | FlatMap — return CF from function | `CF<U>` |
| `thenCombine(other, fn)` | Combine two results with function | `CF<V>` |
| `thenAccept(fn)` | Consume result, return `Void` | `CF<Void>` |
| `thenRun(runnable)` | Run after completion, ignore result | `CF<Void>` |

### Composition

| Method | Description |
|--------|-------------|
| `allOf(cf1, cf2, ...)` | Wait for all to complete; returns `CF<Void>` |
| `anyOf(cf1, cf2, ...)` | Wait for first to complete; returns `CF<Object>` |

### Error Handling

| Method | Description |
|--------|-------------|
| `exceptionally(fn)` | Handle exception, return fallback value |
| `handle(fn)` | Handle both success and failure |
| `whenComplete(fn)` | Run action after completion (success or error) |

### Examples

```java
// thenApply - Transform
CompletableFuture<String> cf = CompletableFuture
    .supplyAsync(() -> "Hello")
    .thenApply(s -> s + " World");
System.out.println(cf.get()); // "Hello World"

// thenCompose - FlatMap
CompletableFuture<Integer> cf = CompletableFuture
    .supplyAsync(() -> "Hello")
    .thenCompose(s -> CompletableFuture.supplyAsync(() -> s.length()));

// thenCombine - Combine two futures
CompletableFuture<String> a = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> b = CompletableFuture.supplyAsync(() -> "World");
CompletableFuture<String> combined = a.thenCombine(b, (x, y) -> x + " " + y);

// allOf - Wait for all
CompletableFuture<Void> all = CompletableFuture.allOf(cf1, cf2, cf3);
all.join(); // blocks until all complete

// anyOf - Wait for first
CompletableFuture<Object> any = CompletableFuture.anyOf(cf1, cf2, cf3);

// exceptionally - Error handling
CompletableFuture<String> cf = CompletableFuture
    .supplyAsync(() -> { throw new RuntimeException("fail"); })
    .exceptionally(ex -> "fallback");

// handle - Both success and error
CompletableFuture<String> cf = CompletableFuture
    .supplyAsync(() -> "Hello")
    .handle((result, ex) -> ex != null ? "error" : result);
```

---

## Runnable vs Callable: When to Use

| Use Case | Choose |
|----------|--------|
| Fire-and-forget task | `Runnable` |
| Need result from task | `Callable` |
| Need to throw checked exceptions | `Callable` |
| Simple logging / side effects | `Runnable` |
| Computing values, fetching data | `Callable` |
| Submitting to `Thread.start()` | `Runnable` only |

---

## Common Patterns

### 1. Async Computation with Timeout

```java
Future<Long> future = executor.submit(() -> computeValue());
try {
    Long result = future.get(5, TimeUnit.SECONDS);
    System.out.println("Result: " + result);
} catch (TimeoutException e) {
    future.cancel(true);
    System.out.println("Computation timed out");
}
```

### 2. Parallel Tasks with Results

```java
Future<Integer> f1 = executor.submit(() -> computeA());
Future<Integer> f2 = executor.submit(() -> computeB());
Future<Integer> f3 = executor.submit(() -> computeC());

int sum = f1.get() + f2.get() + f3.get();
```

### 3. Cancel Long-Running Task

```java
Future<String> future = executor.submit(() -> longRunningTask());
Thread.sleep(1000); // let it start
if (!future.isDone()) {
    future.cancel(true); // interrupt
}
```

### 4. CompletableFuture Pipeline

```java
CompletableFuture.supplyAsync(() -> fetchUser(id))
    .thenApply(user -> fetchOrders(user))
    .thenApply(orders -> calculateTotal(orders))
    .thenAccept(total -> sendNotification(total))
    .exceptionally(ex -> { log.error(ex); return null; });
```

### 5. Parallel Data Loading

```java
CompletableFuture<Data> userF = CompletableFuture.supplyAsync(() -> loadUser());
CompletableFuture<Data> productF = CompletableFuture.supplyAsync(() -> loadProduct());
CompletableFuture<Data> orderF = CompletableFuture.supplyAsync(() -> loadOrder());

CompletableFuture.allOf(userF, productF, orderF).join();
Dashboard dash = new Dashboard(userF.get(), productF.get(), orderF.get());
```

---

## Quick Reference

```
Runnable:
  void run()
  → No return, no checked exceptions
  → submit(Runnable) → Future<Void>

Callable<V>:
  V call() throws Exception
  → Returns value, can throw checked exceptions
  → submit(Callable) → Future<V>

Future<V>:
  get()              → blocks forever
  get(timeout, unit) → blocks with timeout
  isDone()           → non-blocking check
  cancel(interrupt)  → attempt cancel
  isCancelled()      → check cancel status

CompletableFuture<V>:
  thenApply(fn)      → transform (sync)
  thenCompose(fn)    → flatMap (sync)
  thenCombine(cf,fn) → combine two
  allOf(cfs...)      → wait for all
  anyOf(cfs...)      → wait for first
  exceptionally(fn)  → error fallback
  handle(fn)         → success + error
```
