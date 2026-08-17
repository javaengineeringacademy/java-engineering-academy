# Introduction to Multithreading (Part 2)

[📖 Back to Part 1](README.md)

---

## Advanced Concepts

### Java Memory Model (JMM) Deep Dive

The JMM defines how threads interact through memory. Every action in a program has a *happens-before* relationship with subsequent actions:

```
Program Order Rule:
  Within a thread, action A happens-before action B if A comes before B in program order.

Monitor Lock Rule:
  An unlock on a monitor happens-before every subsequent lock on that monitor.

volatile Variable Rule:
  A write to a volatile field happens-before every subsequent read of that field.

Thread Start Rule:
  A call to Thread.start() happens-before any action in the started thread.

Thread Termination Rule:
  Any action in a thread happens-before another thread successfully joins that thread.

Transitivity:
  If A happens-before B, and B happens-before C, then A happens-before C.
```

### False Sharing and Cache Coherency

Modern CPUs use cache lines (typically 64 bytes). When two threads modify variables that reside on the same cache line, the CPU must invalidate and transfer the cache line between cores:

```java
// BAD: False sharing - variables may share cache line
class Bad {
    volatile long x;
    volatile long y; // May be on same cache line as x
}

// GOOD: Padding to avoid false sharing
class Good {
    volatile long x;
    long p1, p2, p3, p4, p5, p6, p7; // Padding
    volatile long y; // Now on separate cache line
}
```

### Thread-Safety Levels

1. **Immutable**: No synchronization needed (String, Integer)
2. **Unconditionally thread-safe**: Internal synchronization (Random, AtomicLong)
3. **Conditionally thread-safe**: External synchronization required (Collections)
4. **Not thread-safe**: Must be synchronized externally (ArrayList, HashMap)
5. **Thread-hostile**: Cannot be made thread-safe (static fields without sync)

### Daemon Thread Lifecycle

Daemon threads are terminated when only daemon threads remain:

```java
Thread daemon = new Thread(() -> {
    while (true) {
        // This loop runs until JVM shuts down
        doCleanup();
        try { Thread.sleep(1000); } catch (InterruptedException e) { return; }
    }
});
daemon.setDaemon(true);
daemon.start();
// When main thread finishes and only daemon threads remain, JVM exits
```

### Thread Interruption Best Practices

```java
// GOOD: Re-interrupt after catching
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore interrupt flag
    // Clean up and return
}

// GOOD: Propagate interruption
public void doWork() throws InterruptedException {
    while (!Thread.currentThread().isInterrupted()) {
        // Do work
    }
}

// BAD: Swallowing interruption
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // Do nothing - THIS IS WRONG
}
```

---

[📖 Back to Part 1](README.md)
