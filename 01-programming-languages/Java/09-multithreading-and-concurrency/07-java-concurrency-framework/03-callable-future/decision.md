# Callable and Future - Decision Guide

## When to Use Callable vs Runnable

| Criteria | Runnable | Callable |
|----------|----------|----------|
| Returns a value | No | Yes (via Future) |
| Throws checked exceptions | No | Yes |
| Submit to ExecutorService | Yes | Yes |
| Use case | Fire-and-forget | Result-producing tasks |
| Thread.start() | Yes | No (use executor) |

## Future Method Selection

| Need | Method | Blocking? |
|------|--------|-----------|
| Get result, wait indefinitely | `get()` | Yes |
| Get result, timeout | `get(timeout, unit)` | Yes, bounded |
| Check completion without blocking | `isDone()` | No |
| Cancel before execution | `cancel(mayInterrupt)` | No |
| Check if cancelled | `isCancelled()` | No |

## Cancellation Strategy

| Scenario | cancel(true) | cancel(false) |
|----------|-------------|---------------|
| CPU-bound, no interrupts | Safe | Safe |
| Blocking IO | Interrupts IO | Waits for natural completion |
| Thread.sleep() | Interrupts sleep | Waits for natural completion |
| Non-interruptible code | No effect | No effect |

## Common Patterns

| Pattern | Implementation |
|---------|---------------|
| Timeout result | `future.get(timeout, unit)` with try-catch |
| Cancel long task | `future.cancel(true)` + check `isCancelled()` |
| Multiple results | `invokeAll()` + iterate Futures |
| First result | `invokeAny()` |
| Best of N | Submit N tasks, cancel losers after first completes |
