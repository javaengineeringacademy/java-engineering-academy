# Virtual Threads Internals

## How Virtual Threads Work

### Architecture

```
Virtual Thread 1 ──┐
Virtual Thread 2 ──┤
Virtual Thread 3 ──┼── Carrier Thread (ForkJoinPool)
...                │
Virtual Thread N ──┘
```

### Key Concepts

1. **Virtual Thread** — Lightweight thread managed by JVM, ~1KB stack
2. **Carrier Thread** — Platform thread from ForkJoinPool, runs virtual threads
3. **Continuation** — JVM mechanism for saving/restoring virtual thread state
4. **Mount/Unmount** — Virtual thread attaches/detaches from carrier thread

### Lifecycle

```
Created → Runnable → Mounted (on carrier)
                    → Unmounted (blocked, waiting)
                    → Terminated
```

### Blocking Mechanism

When a virtual thread blocks (I/O, sleep, lock):
1. JVM saves the virtual thread's stack frame (continuation)
2. Virtual thread unmounts from carrier thread
3. Carrier thread becomes free for other virtual threads
4. When blocked operation completes, continuation resumes
5. Virtual thread remounts on any available carrier

### Pinning

Pinning occurs when a virtual thread cannot unmount:
- Inside `synchronized` block — JVM cannot safely save continuation
- During native method execution (JNI)
- Carrier thread is pinned → reduces concurrency

```java
// BAD: Pinning
synchronized (lock) {
    blockingOperation(); // Virtual thread stuck on carrier
}

// GOOD: No pinning
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    blockingOperation(); // Virtual thread can unmount
} finally {
    lock.unlock();
}
```

### Carrier Thread Pool

- Default: ForkJoinPool with `availableProcessors()` threads
- Configurable via system properties:
  - `-Djdk.virtualThreadScheduler.parallelism=N`
  - `-Djdk.virtualThreadScheduler.maxPoolSize=N`
  - `-Djdk.virtualThreadScheduler.minRunnable=N`

### Stack Management

- Initial stack: 1KB (vs 1MB for platform threads)
- Grows on demand (up to platform thread size)
- Shrinks when possible
- Stored on heap (not native memory)

### Structured Concurrency

StructuredTaskScope provides structured lifecycle:
- Subtasks must complete before scope closes
- Cancellation propagates automatically
- Error handling is localized
