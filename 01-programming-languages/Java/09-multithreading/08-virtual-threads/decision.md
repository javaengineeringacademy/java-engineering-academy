# Virtual Threads Decision Framework

## When to Use Virtual Threads

| Scenario | Use Virtual Threads? | Alternative |
|----------|---------------------|-------------|
| I/O-bound at scale (HTTP, DB, files) | Yes | - |
| Millions of concurrent connections | Yes | - |
| Simple blocking code needed | Yes | - |
| CPU-bound work | No | Thread pools |
| ThreadLocal spans blocking ops | No | Platform threads |
| synchronized held during I/O | No (pinning) | ReentrantLock |
| Legacy code already tuned | No | Keep as-is |

## Virtual Threads vs Alternatives

```
What's your primary concern?
├── Simplicity + Scalability → Virtual Threads
├── CPU parallelism → Thread pools (cores + 1)
├── Async composition → CompletableFuture
├── Backpressure + streams → Reactor/Vert.x
└── Recursive parallelism → ForkJoinPool
```

## Migration Decision

### Migrate to Virtual Threads when:
- Thread pool sizing is complex and error-prone
- Blocking I/O causes thread starvation
- You want simple thread-per-request model
- You're using CompletableFuture mainly for blocking I/O

### Keep Platform Thread Pools when:
- CPU-bound work dominates
- ThreadLocal is used extensively
- synchronized blocks are held during I/O
- Current pool is well-tuned and performing

## Pinning Decision

| Current Code | Migration Path |
|-------------|----------------|
| `synchronized` + I/O inside | Replace with `ReentrantLock` |
| `synchronized` + no I/O | Keep as-is |
| `ThreadLocal` in pool | Use `ScopedValue` or pass explicitly |
| `BlockingQueue.take()` | Works fine — no pinning |
| `Thread.sleep()` in pool | Works fine — no pinning |

## Structured Concurrency Decision

| Pattern | Use StructuredTaskScope? |
|---------|-------------------------|
| Independent parallel tasks | Yes — ShutdownOnFailure |
| Dependent tasks (first wins) | Yes — ShutdownOnSuccess |
| Simple sequential chain | No — use thenApply() |
| Fire-and-forget | No — just submit() |
