# Virtual Threads

## Why Virtual Threads Were Introduced

Virtual threads (Project Loom, Java 21) were created to solve a fundamental scalability problem in Java's concurrency model.

### The Problem: Platform Threads Are Expensive

Every platform (OS) thread in Java maps to a native OS thread, which carries significant overhead:

- **Stack memory**: Each platform thread reserves ~1MB of stack space by default (`-Xss`). With 10,000 threads, that's 10GB of memory — just for stacks.
- **Context switching**: The OS must save and restore registers, TLBs, and CPU caches for each thread switch. At thousands of threads, context switching consumes 30-50% of CPU time.
- **Kernel resources**: Each thread requires kernel data structures (thread control block, scheduling queues), kernel memory, and syscall overhead for creation/destruction.
- **Scheduling cost**: The OS scheduler uses algorithms (CFS on Linux) that become less efficient as the number of runnable threads grows beyond a few hundred.

This means the practical limit for platform threads in a JVM is roughly 10,000-20,000 — far below the concurrency demands of modern applications handling millions of simultaneous connections.

### The Cost in Numbers

| Resource | Platform Thread (1MB stack) | Virtual Thread (1KB stack) |
|----------|---------------------------|---------------------------|
| 10,000 threads | 10 GB memory | 10 MB memory |
| 100,000 threads | 100 GB (impractical) | 100 MB (feasible) |
| 1,000,000 threads | 1 TB (impossible) | 1 GB (possible) |

### The Thread Pool Sizing Dilemma

Platform threads force developers into a painful tradeoff:

- **Too few threads**: Tasks queue up, latency increases, CPU sits idle during blocking
- **Too many threads**: Memory exhaustion, context switching kills throughput
- **Just right**: Requires complex tuning — measuring wait/compute ratios, calculating optimal pool sizes, and adjusting for different workloads

```java
// The old way: complex pool sizing
int poolSize = Runtime.getRuntime().availableProcessors()
    * (1 + (waitTime / computeTime));  // What are waitTime and computeTime?
// This formula changes per workload, per endpoint, per deployment
```

### How Virtual Threads Solve This

Virtual threads are scheduled by the JVM (not the OS). They are backed by carrier threads (platform threads from a ForkJoinPool), but millions of virtual threads can share a small number of carriers:

- **1KB stack** (grows/shrinks on demand) instead of 1MB reserved
- **No kernel involvement** for scheduling — the JVM uses continuation-based switching
- **No pool sizing needed** — create one virtual thread per task, let the JVM handle multiplexing
- **Blocking is free** — when a virtual thread blocks on I/O, the JVM unmounts it from the carrier and mounts another

```java
// The new way: just create a thread per task
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> handleRequest(request));
}
```

### The Concurrency Model Shift

Virtual threads enable the simple **thread-per-request** model to scale again:

1. **Old model**: Thread pools + async/callbacks (complex, hard to debug)
2. **New model**: Virtual threads + blocking I/O (simple, easy to debug)

With virtual threads, you write straightforward sequential code that blocks on I/O — the same code that works at 10 threads works at 1,000,000 threads without modification.

### Key Takeaway

Virtual threads eliminate the tradeoff between simplicity (blocking I/O) and scalability (async I/O). They let you write simple blocking code that scales to millions of concurrent operations — solving the thread pool sizing problem entirely.

## Engineering Decision Framework

### ✅ Use Virtual Threads when:
- I/O-bound workloads dominate (HTTP, DB, file operations)
- Millions of concurrent connections are needed
- Simple blocking code is preferred over async/callback patterns
- Thread-per-request model should scale
- Legacy blocking code needs improved concurrency

### ❌ Avoid Virtual Threads when:
- CPU-bound work is the bottleneck (use thread pools)
- Thread-local state is heavily used (carrier thread switching loses it)
- Synchronized blocks are held during blocking operations (pins carrier)
- Platform thread pools are already tuned for the workload

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| Platform thread pools | CPU-bound, predictable workloads |
| CompletableFuture | Async composition and chaining |
| Reactor/Vert.x | Reactive streams with backpressure |
| ExecutorService | Traditional thread pool management |

### Production Examples
- Web server request handling at scale
- Database connection pooling without pool sizing
- Microservice-to-microservice HTTP calls
- WebSocket connection management
- Legacy application scalability improvements

### Common Production Mistakes
- Using synchronized blocks inside virtual threads (causes pinning)
- Not using StructuredTaskScope for complex concurrent tasks
- Assuming virtual threads fix CPU-bound performance
- Using thread-local variables that span blocking operations
- Not monitoring carrier thread pool utilization

## See Also
- [CompletableFuture](../../15-senior/advanced/concurrency-advanced/completable-future/) — Async alternative virtual threads replace
- [ThreadPoolExecutor Source](../../15-senior/java-platform/source-exploration/threadpool-executor-source/) — How carrier threads work internally
- [Structured Concurrency](../../15-senior/advanced/concurrency-advanced/structured-concurrency/) — Companion API for virtual threads
- [ForkJoinPool](../../15-senior/advanced/concurrency-advanced/fork-join/) — The executor backing virtual threads
