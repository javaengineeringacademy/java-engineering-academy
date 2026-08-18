# Virtual Threads — Internals

## Overview

This directory explores how virtual threads work internally — carrier thread scheduling, mounting/unmounting, and pinning behavior.

## Key Topics

### Virtual Thread Internals

- Virtual threads are lightweight threads managed by the JVM
- Stack is a heap-allocated `StackChunk` (~1KB default)
- Mounted on a **carrier thread** (platform thread from `ForkJoinPool`)
- When blocked: unmounted from carrier, carrier picks up another virtual thread

### Mount/Unmount Mechanism

```
Virtual Thread    Carrier Thread
    mount ──────►  execute task
    unmount ◄─────  blocked on I/O
    (parked)       pick up next virtual thread
    mount ──────►  resume execution
```

### Pinning

- **Synchronized block**: Prevents unmounting (carrier thread blocked)
- **Native method call**: Prevents unmounting (JVM cannot intercept)
- **JNI call**: May prevent unmounting depending on implementation
- Use `ReentrantLock` instead of `synchronized` for virtual threads

### Carrier Thread Pool

- Default: `ForkJoinPool` with `Runtime.getRuntime().availableProcessors()` threads
- Custom carrier pool: `Executors.newFixedThreadPool(N)`
- Carrier threads are reused across many virtual threads

### Structured Concurrency (Preview)

- `StructuredTaskScope` ties child task lifetime to parent scope
- Parent scope closes only when all children complete
- Enables clean error propagation and resource cleanup

## Files

- [VirtualThreadInternals.java](VirtualThreadInternals.java) — Virtual thread execution mechanics
