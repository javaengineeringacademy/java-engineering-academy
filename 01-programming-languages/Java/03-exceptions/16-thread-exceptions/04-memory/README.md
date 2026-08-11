# Thread Exceptions — Memory Behavior

## Per-Thread Exception Memory

Each thread maintains its own exception handling context:

```
Thread 1 (main):
  Stack frame → exception handler table → exception object

Thread 2 (worker):
  Stack frame → exception handler table → exception object

Thread 3 (worker):
  Stack frame → exception handler table → exception object
```

## ThreadLocal Exception State

```java
// Each thread has its own:
// - Stack trace (via fillInStackTrace)
// - Exception handler chain
// - UncaughtExceptionHandler reference

// ThreadLocal overhead per thread: ~16 bytes base
// Plus any thread-local exception state
```

## UncaughtExceptionHandler Memory

```java
Thread t = new Thread(() -> {
    throw new RuntimeException("thread error");
});
// Handler reference: 8 bytes (pointer)
// Handler object: ~50-200 bytes (depending on implementation)
// Exception: ~2KB (with stack trace)
```

## Exception in Thread Pool

```
Pool with 10 threads, each throwing 1 exception/sec:

Exceptions/sec:     10
Memory/sec:        20KB (10 × 2KB per exception)
GC pressure:       High (constant allocation)
```

## Mitigation

```java
// Thread pool with custom handler
ExecutorService pool = Executors.newFixedThreadPool(10, r -> {
    Thread t = new Thread(r);
    t.setUncaughtExceptionHandler((thread, ex) -> {
        // Log once, don't accumulate
        log.error("Thread {} failed", thread.getName(), ex);
    });
    return t;
});
```

## Key Insight

Each thread handles exceptions independently. In thread pools, exception memory scales with thread count × exception rate. Custom UncaughtHandlers prevent memory accumulation.
