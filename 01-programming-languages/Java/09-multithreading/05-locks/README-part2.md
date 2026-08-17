# Locks (Part 2)

[📖 Back to Part 1](README.md)

---

## Advanced Concepts

### AQS (AbstractQueuedSynchronizer) Framework

Most Java locks are built on AQS:

```
AQS Structure:
┌──────────────────────────────────────────────┐
│ volatile int state                           │
│ Thread exclusiveOwnerThread                  │
│                                              │
│ CLH Queue (doubly-linked list of Nodes):     │
│ head ──▶ [Node:T1:WAIT] ──▶ [Node:T2:WAIT] ──▶ tail
│                                              │
│ Node states:                                 │
│   SIGNAL(-1): Must unpark successor          │
│   CANCELLED(1): Thread cancelled             │
│   CONDITION(-2): On condition wait set       │
│   0: Default/propagate                       │
└──────────────────────────────────────────────┘
```

**How ReentrantLock uses AQS:**
- `lock()`: CAS on state (0→1), set owner
- `unlock()`: CAS on state (N→N-1), unpark successor at 0
- Reentrant: increment state count

**How ReadWriteLock uses AQS:**
- Read lock: shared mode, succeeds if state high bits = 0
- Write lock: exclusive mode, succeeds if state = 0

### StampedLock Advanced Patterns

```java
// 1.悲观读锁 (Pessimistic read)
long stamp = sl.readLock();
try {
    // Read data
} finally {
    sl.unlockRead(stamp);
}

// 2.乐观读锁 (Optimistic read)
long stamp = sl.tryOptimisticRead();
int x = data.x;
int y = data.y;
if (!sl.validate(stamp)) {
    // Fallback to pessimistic read
    stamp = sl.readLock();
    try { x = data.x; y = data.y; }
    finally { sl.unlockRead(stamp); }
}

// 3.写锁 (Write lock)
long stamp = sl.writeLock();
try { data.x++; }
finally { sl.unlockWrite(stamp); }

// 4.转换锁 (Convert lock)
long stamp = sl.writeLock();
try {
    stamp = sl.tryConvertToReadLock(stamp);
    // Now holding read lock
} finally {
    sl.unlock(stamp);
}
```

### Condition vs wait/notify

| Feature | `wait()/notify()` | `Condition` |
|---------|-------------------|-------------|
| Wait sets per lock | 1 | Multiple |
| Signal specific group | No | Yes (`signal()`) |
| Interruptible await | No | Yes (`awaitInterruptibly()`) |
| Timed await | `wait(ms)` | `await(time, unit)` |
| Lock type | Intrinsic monitor | Explicit lock |

### Lock Performance Tuning

```java
// 1. Use fair lock only when needed
ReentrantLock lock = new ReentrantLock(false); // Unfair (default)

// 2. Minimize lock hold time
lock.lock();
try {
    // Do minimal work while holding lock
} finally {
    lock.unlock();
}

// 3. Use tryLock to avoid blocking
if (lock.tryLock(100, MILLISECONDS)) {
    try { /* work */ } finally { lock.unlock(); }
}

// 4. Use StampedLock for read-heavy workloads
StampedLock sl = new StampedLock(); // Best for reads
```

### Common Lock Anti-Patterns

1. **Forgetting `finally { lock.unlock() }`** → Deadlock
2. **Calling `await()` outside synchronized** → `IllegalMonitorStateException`
3. **Using `notify()` instead of `notifyAll()`** → Missed signals
4. **Locking `this` in public methods** → External deadlocks
5. **Nested locks without ordering** → Deadlock
6. **Using `Thread.sleep()` while holding lock** → Reduced throughput

---

[📖 Back to Part 1](README.md)
