# Synchronization - Decision Guide

## When to Synchronize

| Problem | Solution | When to Use |
|---------|----------|-------------|
| Race condition on shared variable | `synchronized` | Simple critical sections |
| Visibility of writes across threads | `volatile` | Flags, status indicators |
| Atomic compound operations | `AtomicInteger` etc. | Counters, accumulators |
| Multiple condition variables | `Condition` | Complex coordination |
| Read-heavy with occasional writes | `ReadWriteLock` | Caching, config |
| Lock-free algorithms | `AtomicReference` | High contention |

## Choosing Synchronization Mechanism

```
Need to protect a critical section?
├── Simple read-modify-write?
│   ├── Yes → Atomic classes (AtomicInteger, etc.)
│   └── No → synchronized block
├── Need visibility only (no compound ops)?
│   ├── Yes → volatile
│   └── No → synchronized or Atomic
├── Multiple threads reading, few writing?
│   ├── Yes → ReadWriteLock
│   └── No → synchronized or ReentrantLock
├── Need to wait for a condition?
│   ├── Simple condition → wait()/notify()
│   ├── Multiple conditions → Condition object
│   └── Complex coordination → CompletableFuture
└── Need tryLock or timed lock?
    ├── Yes → ReentrantLock
    └── No → synchronized is simpler
```

## synchronized vs volatile vs Atomic

| Feature | `synchronized` | `volatile` | `Atomic` |
|---------|---------------|-----------|----------|
| Atomicity | Yes | No | Yes |
| Visibility | Yes | Yes | Yes |
| Blocking | Yes | No | No |
| Performance | Slowest | Fastest | Fast |
| Use case | Compound ops | Flags | Simple atomics |
| Reentrant | Yes | N/A | N/A |
| Interruptible | Yes (via Lock) | N/A | N/A |

## Common Synchronization Patterns

```java
// Pattern 1: synchronized block
synchronized (lock) {
    // critical section
}

// Pattern 2: volatile flag
volatile boolean running = true;
while (running) { /* work */ }

// Pattern 3: Atomic counter
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();

// Pattern 4: synchronized method
public synchronized void increment() { count++; }

// Pattern 5: Double-checked locking
if (instance == null) {
    synchronized (this) {
        if (instance == null) {
            instance = new Singleton();
        }
    }
}
```

## Performance Guidelines

1. **Minimize synchronized regions** — Only protect shared mutable state
2. **Prefer `volatile` over `synchronized`** for simple flags
3. **Use `Atomic` classes** for simple counters
4. **Avoid `synchronized` on `this`** — Use private lock objects
5. **Consider `ReadWriteLock`** for read-heavy workloads
6. **Profile before optimizing** — synchronization overhead varies
