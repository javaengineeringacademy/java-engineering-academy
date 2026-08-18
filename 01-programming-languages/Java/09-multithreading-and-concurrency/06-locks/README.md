# 06 - Locks

## Overview

Java's explicit lock framework in `java.util.concurrent.locks` provides more flexible synchronization than `synchronized`: timed locking, interruptible locking, fair ordering, and multiple condition variables.

## Learning Objectives

- Use ReentrantLock for advanced locking scenarios
- Understand ReadWriteLock for read-heavy workloads
- Learn StampedLock for optimistic reading
- Use Condition variables for complex coordination
- Know when to use each lock type

## Lock Types

| Lock | Use Case | Features |
|------|----------|----------|
| ReentrantLock | General-purpose | tryLock, timed, interruptible, fair |
| ReadWriteLock | Read-heavy workloads | Concurrent reads, exclusive writes |
| StampedLock | High-performance reads | Optimistic read, write lock, read write lock |

## Syntax

```java
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}

// Try with timeout
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try { /* work */ }
    finally { lock.unlock(); }
} else {
    // handle timeout
}
```
