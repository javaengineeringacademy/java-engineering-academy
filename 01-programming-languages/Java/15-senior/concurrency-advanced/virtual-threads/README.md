# Virtual Threads (Project Loom)

## Core Concepts

Virtual threads are lightweight threads managed by the JVM rather than the OS.
They were introduced as a preview in Java 19 and finalized in Java 21.

### Key Properties

- Created by the JVM, not the operating system
- Millions can run concurrently on a single machine
- Use carrier threads (platform threads) for actual execution
- Designed for I/O-bound workloads
- Cheap to create and context-switch

---

## Loom Project

Project Loom introduced three main features:

1. **Virtual Threads** - lightweight threads (Java 21)
2. **Structured Concurrency** - scoped task management (Java 21)
3. **Scoped Values** - efficient thread-local alternative (Java 21)

### Structured Concurrency

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> user = scope.fork(() -> fetchUser(id));
    Subtask<Orders> orders = scope.fork(() -> fetchOrders(id));

    scope.join(); // waits for both tasks

    return new Dashboard(user.get(), orders.get());
}
```

Two policies:
- `ShutdownOnFailure`: cancels all tasks if any fails
- `ShutdownOnSuccess`: cancels remaining tasks after first success

---

## Migration from Thread Pools

### Before (Thread Pool)

```java
ExecutorService executor = Executors.newFixedThreadPool(200);
executor.submit(() -> handleRequest());
```

### After (Virtual Threads)

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> handleRequest());
}
```

### Migration Checklist

1. Replace `Executors.newFixedThreadPool(N)` with `newVirtualThreadPerTaskExecutor`
2. Replace `synchronized` blocks with `ReentrantLock`
3. Remove thread pool sizing logic (virtual threads handle scale)
4. Audit `ThreadLocal` usage for memory implications
5. Test for pinning with `-Djdk.tracePinnedThreads=full`

---

## Best Practices

### Do

- Use for I/O-bound work (HTTP, DB, file operations)
- Use structured concurrency for related tasks
- Use `ReentrantLock` instead of `synchronized`
- Clean up `ThreadLocal` values after use
- Monitor with JFR events (`jdk.VirtualThreadStart`)

### Don't

- Pool virtual threads (one per task is the model)
- Use for CPU-bound computation (no benefit)
- Use `synchronized` in long-running sections
- Hold `ThreadLocal` across task boundaries without cleanup
- Assume virtual threads fix all concurrency problems

---

## When to Use

| Scenario | Use Virtual Threads? |
|----------|---------------------|
| Database queries | Yes |
| HTTP client calls | Yes |
| File I/O | Yes |
| Waiting on locks | Yes |
| CPU-bound math | No |
| Graphics rendering | No |
| Low-latency trading | No |

---

## Performance Characteristics

- Creation: ~1000x cheaper than platform threads
- Memory: ~1KB vs ~1MB per platform thread
- Context switch: ~200ns vs ~2000ns (OS level)
- Throughput: scales linearly with I/O tasks

Run with: `java -Djdk.tracePinnedThreads=full VirtualThreadsDemo.java`

## Why Virtual Threads Over Thread Pools?

| Criteria | Virtual Threads | Thread Pools |
|----------|----------------|--------------|
| Creation cost | ~1KB | ~1MB |
| Max count | Millions | Thousands |
| Blocking | Cheap (unmounts) | Expensive (wastes thread) |
| Complexity | Simple | Complex sizing |
| Use when | I/O-bound, many connections | CPU-bound, few connections |

### Decision Flowchart
I/O-bound? → Yes → Many concurrent? → Yes → Use Virtual Threads
CPU-bound? → Yes → Use Thread Pools
