# Introduction to Multithreading - Decision Guide

## When to Use Multithreading

| Scenario | Use Multithreading? | Alternative |
|----------|-------------------|-------------|
| CPU-bound computation on multi-core | Yes | Parallel streams, ForkJoinPool |
| I/O-bound operations (network, disk) | Yes | Virtual threads, async/await |
| Simple sequential tasks | No | Sequential execution |
| Real-time GUI responsiveness | Yes | SwingWorker, JavaFX Platform |
| High-throughput web server | Yes | ExecutorService, Netty |
| Simple timer/scheduler | Maybe | ScheduledExecutorService |
| Simple periodic task | No | Timer, ScheduledExecutorService |

## Choosing a Threading Approach

```
Need concurrent execution?
├── I/O-bound work?
│   ├── Yes → Virtual threads (Java 21+)
│   └── No → CPU-bound work?
│       ├── Yes → Thread pool with fixed size
│       └── No → Single thread may suffice
├── Need return value from task?
│   ├── Yes → Callable + Future
│   └── No → Runnable
├── Need to share state between threads?
│   ├── Yes → Consider synchronization strategy
│   └── No → Thread-per-task is fine
└── Need lightweight spawning?
    ├── Yes → Virtual threads
    └── No → Platform threads
```

## Thread Creation Strategy

| Method | Use When | Pros | Cons |
|--------|----------|------|------|
| `extends Thread` | Simple one-off threads | Quick to write | Can't extend other classes |
| `implements Runnable` | Shared task, multiple threads | Flexible, testable | No return value |
| `Callable` + `ExecutorService` | Need return value | Type-safe results | More boilerplate |
| Virtual Threads | I/O-bound, high concurrency | Lightweight, scalable | Java 21+ only |
| `CompletableFuture` | Async pipelines | Composable, non-blocking | Complex error handling |

## Common Anti-Patterns to Avoid

1. **Thread-per-request on blocking I/O** → Use virtual threads or async
2. **Synchronizing everything** → Minimize synchronized regions
3. **Ignoring InterruptedException** → Always handle or rethrow
4. **Using `Thread.stop()`** → Use interruption instead
5. **Not naming threads** → Always set meaningful thread names
6. **Creating threads in tight loops** → Use thread pools

## Quick Decision Matrix

- **Need 1 background task?** → `new Thread(() -> ...).start()`
- **Need N concurrent tasks?** → `ExecutorService` with fixed pool
- **Need result from task?** → `Callable` + `Future.get()`
- **Need 10,000+ concurrent I/O?** → Virtual threads
- **Need periodic execution?** → `ScheduledExecutorService`
- **Need async composition?** → `CompletableFuture`
