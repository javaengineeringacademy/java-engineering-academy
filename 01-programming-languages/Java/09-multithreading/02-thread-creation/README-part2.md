# Thread Creation (Part 2)

[📖 Back to Part 1](README.md)

---

## Advanced Concepts

### Virtual Threads (Java 21+) Deep Dive

Virtual threads are a game-changer for I/O-bound workloads:

```java
// Creating virtual threads
Thread vt = Thread.ofVirtual().name("vt-1").start(() -> {
    // Runs on a carrier thread
    // When blocked (I/O), JVM unmounts and frees the carrier
});

// Virtual thread executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 100_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1)); // Unmounts carrier
            return i;
        });
    });
}
```

**Key differences from platform threads:**
- Created by JVM, not OS (no `pthread_create`)
- Stack stored on heap, not native memory
- Scheduled by JVM, not OS thread scheduler
- Millions can exist simultaneously
- Should NOT be pooled (one per task)

### CompletableFuture Composition

`CompletableFuture` enables async pipelines without blocking:

```java
CompletableFuture.supplyAsync(() -> fetchUser(userId))
    .thenApply(user -> fetchOrders(user.getId()))
    .thenApply(orders -> calculateTotal(orders))
    .thenAccept(total -> sendEmail(total))
    .exceptionally(ex -> {
        log.error("Pipeline failed", ex);
        return null;
    });
```

**Key combinators:**
- `thenApply()`: Transform result
- `thenCompose()`: Flat-map (async chain)
- `thenCombine()`: Combine two futures
- `allOf()`: Wait for all futures
- `anyOf()`: Wait for first future
- `exceptionally()`: Handle errors
- `handle()`: Transform or handle error

### ThreadFactory Pattern

Custom factories provide consistent thread configuration:

```java
public class AppThreadFactory implements ThreadFactory {
    private final AtomicInteger count = new AtomicInteger(0);
    private final String prefix;
    private final boolean daemon;

    public AppThreadFactory(String prefix, boolean daemon) {
        this.prefix = prefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, prefix + "-" + count.incrementAndGet());
        t.setDaemon(daemon);
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
```

### Common Creation Anti-Patterns

1. **Creating threads in loops** → Use thread pools
2. **Not naming threads** → Makes debugging impossible
3. **Using `Thread.stop()`** → Deprecated, use interruption
4. **Creating threads for simple timers** → Use `ScheduledExecutorService`
5. **Not shutting down ExecutorService** → Resource leak

---

[📖 Back to Part 1](README.md)
