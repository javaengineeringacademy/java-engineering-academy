# Thread Exception Handling — Decision Guide

## When to Use Each Approach

### Use try-catch in Runnable when:
- Thread is simple and short-lived
- You need immediate handling at the point of failure
- Exception recovery is straightforward

### Use UncaughtExceptionHandler when:
- You need a safety net for all threads
- Thread creation is centralized or managed by frameworks
- You want to log/report exceptions without preventing thread death

### Use Future.get() when:
- You need the result of a submitted task
- You want to propagate exceptions to the calling thread
- Task is part of a batch and you need per-task error handling

### Use CompletableFuture exception methods when:
- Building async pipelines with multiple stages
- You need functional-style error recovery
- You want to compose multiple async operations

## Decision Flowchart

```
Exception in thread?
├─ Simple thread, can handle locally → try-catch
├─ Need safety net for all threads → UncaughtExceptionHandler
├─ ExecutorService task
│  ├─ Need result → Future.get() with try-catch
│  ├─ Fire-and-forget → execute() (not submit())
│  └─ Multiple tasks → invokeAll/invokeAny
└─ Async pipeline → CompletableFuture
   ├─ Fallback value → exceptionally()
   ├─ Handle both outcomes → handle()
   └─ Side effects only → whenComplete()
```

## Common Mistakes

1. Using `submit()` without checking `Future.get()` — exception silently lost
2. Not setting `setDefaultUncaughtExceptionHandler` at app startup
3. Catching `Exception` instead of `ExecutionException` when calling `Future.get()`
4. Ignoring `TimeoutException` in `Future.get()` — can block forever

## Production Checklist

- [ ] Set global `UncaughtExceptionHandler` at application startup
- [ ] Always use timeout with `Future.get()`
- [ ] Log all exceptions in `CompletableFuture.exceptionally()` or `handle()`
- [ ] Monitor thread pool metrics (queue size, active threads, rejections)
- [ ] Test exception scenarios in thread pools
