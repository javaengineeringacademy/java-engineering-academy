# Locks - Decision Guide

## When to Use Explicit Locks

| Lock Type | Use Case | Pros | Cons |
|-----------|----------|------|------|
| `synchronized` | Simple critical sections | Simple syntax, auto-release | No tryLock, no interruptible |
| `ReentrantLock` | Need tryLock, interruptibility | Flexible, timed locks | Must manually unlock |
| `ReadWriteLock` | Read-heavy workloads | Multiple readers allowed | Write starvation possible |
| `StampedLock` | Optimistic reads | Best performance | Complex API |
| `Condition` | Multiple wait conditions | Per-lock wait sets | More complex than wait/notify |

## Choosing the Right Lock

```
Need a lock?
├── Simple mutual exclusion?
│   └── Use synchronized (simplest)
├── Need tryLock()?
│   └── Use ReentrantLock
├── Need interruptible lock acquisition?
│   └── Use ReentrantLock.lockInterruptibly()
├── Need timed lock acquisition?
│   └── Use ReentrantLock.tryLock(timeout)
├── Read-heavy, write-light?
│   ├── Need fair ordering? → ReadWriteLock
│   └── Need maximum performance? → StampedLock
└── Multiple wait conditions?
    └── Use Lock.newCondition()
```

## synchronized vs ReentrantLock vs StampedLock

| Feature | synchronized | ReentrantLock | StampedLock |
|---------|-------------|---------------|-------------|
| Reentrant | Yes | Yes | No (read lock) |
| tryLock | No | Yes | Yes |
| Timed lock | No | Yes | Yes |
| Interruptible | No | Yes | Yes |
| Fair ordering | No | Optional | No |
| Optimistic read | No | No | Yes |
| Performance | Good | Good | Best (read-heavy) |
| Auto-release | Yes | No (must unlock) | No (must unlock) |
| Conditions | wait/notify | Condition | Condition |

## Lock Ordering Pattern

To prevent deadlocks when acquiring multiple locks:

```java
// BAD: May deadlock
lockA.lock();
lockB.lock(); // Another thread may hold lockB and wait for lockA

// GOOD: Consistent ordering
if (lockA.hashCode() < lockB.hashCode()) {
    lockA.lock();
    lockB.lock();
} else {
    lockB.lock();
    lockA.lock();
}
```

## Common Lock Patterns

```java
// Pattern 1: tryLock for non-blocking attempt
if (lock.tryLock()) {
    try { /* work */ } finally { lock.unlock(); }
}

// Pattern 2: Timed lock with fallback
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try { /* work */ } finally { lock.unlock(); }
} else {
    System.out.println("Could not acquire lock");
}

// Pattern 3: Interruptible lock
try {
    lock.lockInterruptibly();
    try { /* work */ } finally { lock.unlock(); }
} catch (InterruptedException e) {
    // Lock acquisition was interrupted
}

// Pattern 4: Condition for producer-consumer
lock.lock();
try {
    while (buffer.isEmpty()) notEmpty.await();
    // consume
    notFull.signal();
} finally { lock.unlock(); }
```
