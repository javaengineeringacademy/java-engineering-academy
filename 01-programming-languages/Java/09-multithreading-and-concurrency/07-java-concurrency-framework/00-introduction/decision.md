# Executor Framework Introduction - Decision Guide

## When to Use Executor Framework vs Raw Threads

| Scenario | Choice | Reason |
|----------|--------|--------|
| Few long-lived tasks | Raw Thread | Less overhead |
| Many short tasks | ExecutorService | Thread reuse |
| Need thread lifecycle management | ExecutorService | Built-in shutdown |
| One-off background task | Raw Thread | Simpler setup |
| Production task submission | ExecutorService | Monitoring, control |

## Executor Type Selection

| Need | Use | Why |
|------|-----|-----|
| Fire-and-forget tasks | `execute(Runnable)` | No result needed |
| Task returning result | `submit(Callable)` | Future-based result |
| All tasks must complete | `invokeAll()` | Wait for all |
| First result wins | `invokeAny()` | Best-of-N pattern |
| Periodic execution | `ScheduledExecutorService` | Timer replacement |

## Shutdown Strategy

| Situation | Strategy |
|-----------|----------|
| Graceful (let tasks finish) | `shutdown()` + `awaitTermination()` |
| Immediate (interrupt running) | `shutdownNow()` |
| Unknown duration | `shutdown()` → wait → `shutdownNow()` |
| Critical sections | `shutdownNow()` + task-level interrupts |

## Common Pitfalls

| Pitfall | Solution |
|---------|----------|
| Forgetting to shutdown | Use try-with-resources or shutdown hooks |
| Unbounded task queues | Use bounded queues with rejection policies |
| Swallowing exceptions in execute() | Use submit() + Future.get() |
| Calling shutdownNow() without await | Always wait for termination |
