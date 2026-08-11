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

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| try-catch in Runnable | Simple; immediate handling at point of failure | Exception handling coupled to thread logic |
| UncaughtExceptionHandler | Safety net; centralized error handling | Exception is already lost; cannot recover |
| Future.get() with try-catch | Exception propagates to caller; typed handling | Blocks caller thread; requires timeout handling |
| CompletableFuture.exceptionally() | Functional recovery; composable pipelines | Complex chains; harder to debug |

## Common Code Review Comments

- "You're using `submit()` without ever calling `Future.get()` — exceptions are silently lost."
- "Set a `UncaughtExceptionHandler` on this thread pool — otherwise failures disappear."
- "Use `execute()` instead of `submit()` if you don't need the Future — fire-and-forget should not use submit."
- "Always set a timeout on `Future.get()` — it can block forever."
- "Catch `ExecutionException` when calling `Future.get()`, not `Exception` — the task exception is wrapped."

## Common Production Mistakes

- **Silently lost exceptions with submit()**: `executor.submit(task)` without `Future.get()` — if the task throws, the exception is stored in the Future but never retrieved. Use `execute()` for fire-and-forget.
- **No UncaughtExceptionHandler**: Thread pool threads die silently on unhandled exceptions — the pool replaces them but the failure is invisible.
- **Blocking forever on Future.get()**: No timeout specified — if the task hangs, the calling thread hangs forever. Always use `get(timeout, unit)`.
- **Catching Exception instead of ExecutionException**: `Future.get()` wraps task exceptions in `ExecutionException` — catching `Exception` misses the wrapping and loses type information.

## When to Escalate

- You are designing a thread pool error handling strategy for a production system — the UncaughtExceptionHandler, Future handling, and logging need architectural review.
- A production system has silently lost exceptions in thread pools — the error handling strategy needs to be redesigned.
- You are building async pipelines with complex failure modes — the CompletableFuture error handling design needs review.
