# Thread Creation - Decision Guide

## How to Create Threads

| Method | Return Value | Exception Handling | Use Case |
|--------|-------------|-------------------|----------|
| `extends Thread` | None | In `run()` | Simple, single-purpose threads |
| `implements Runnable` | None | In `run()` | Shared task, multiple threads |
| `Callable` + `ExecutorService` | `Future<V>` | Checked exceptions | Need result from task |
| `CompletableFuture` | `CompletableFuture<T>` | Via `exceptionally()` | Async composition |
| Virtual Threads | `Thread` | In `run()` | I/O-bound, high concurrency |

## Choosing the Right Approach

```
Do you need a return value?
├── Yes
│   ├── Single result? → Callable + Future.get()
│   ├── Composed results? → CompletableFuture
│   └── Multiple results? → ExecutorService.invokeAll()
└── No
    ├── Simple task? → Lambda Runnable
    ├── Task needs to be reusable? → Implement Runnable
    ├── Need custom Thread features? → Extend Thread
    └── High concurrency (10k+)? → Virtual Threads
```

## When to Use Each

### Extend Thread
- When you need to override Thread methods (name, behavior)
- When the thread has unique lifecycle needs
- Avoid when you need to extend another class

### Implement Runnable
- When task can be shared among multiple threads
- When you want separation of task from execution
- Preferred over extending Thread

### Callable + Future
- When task must return a value
- When task might throw checked exceptions
- When you need to cancel tasks

### Virtual Threads
- When tasks are I/O-bound (HTTP, DB, file)
- When you need millions of concurrent tasks
- When thread-per-task is too expensive with platform threads

## Common Patterns

```java
// Pattern 1: Simple fire-and-forget
new Thread(() -> doWork()).start();

// Pattern 2: Wait for result
ExecutorService exec = Executors.newSingleThreadExecutor();
Future<String> result = exec.submit(() -> computeValue());
String value = result.get();

// Pattern 3: Async composition
CompletableFuture.supplyAsync(() -> fetchUser())
    .thenApply(user -> fetchOrders(user))
    .thenAccept(orders -> process(orders));

// Pattern 4: Thread pool for batch work
ExecutorService pool = Executors.newFixedThreadPool(4);
IntStream.range(0, 100).forEach(i ->
    pool.submit(() -> processItem(i)));
pool.shutdown();
```
