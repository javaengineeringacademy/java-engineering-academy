# Distributed Locks

> Package: `academy.messaging.zookeeper.distributedlocks`

## Overview

Distributed locks provide mutual exclusion across multiple processes. Zookeeper implements locks using ephemeral sequential znodes and watches.

## Lock Algorithm

```
1. Client creates ephemeral sequential znode
   /locks/resource/lock-0000000001

2. Client gets all children and sorts
   [lock-0000000001, lock-0000000002, lock-0000000003]

3. If my znode is lowest, lock acquired

4. Otherwise, watch the next lower znode
   lock-0000000002 watches lock-0000000001

5. When lock released, watch fires and re-check position
```

## Implementation

### Basic Lock with Curator

```java
InterProcessMutex lock = new InterProcessMutex(client, "/locks/resource");

if (lock.acquire(10, TimeUnit.SECONDS)) {
    try {
        // Critical section
        doWork();
    } finally {
        lock.release();
    }
}
```

### Read-Write Lock

```java
InterProcessReadWriteLock rwLock = 
    new InterProcessReadWriteLock(client, "/locks/resource");

// Read lock
rwLock.readLock().acquire();
try {
    readData();
} finally {
    rwLock.readLock().release();
}

// Write lock
rwLock.writeLock().acquire();
try {
    writeData();
} finally {
    rwLock.writeLock().release();
}
```

## Lock Patterns

| Pattern | Use Case |
|---------|----------|
| Simple Lock | Protecting shared resource |
| Read-Write Lock | Multiple readers, single writer |
| Lease Lock | Lock with timeout |
| Fair Lock | FIFO ordering |

## Best Practices

```
✓ Use ephemeral sequential znodes
✓ Always release locks in finally block
✓ Implement lock acquisition timeout
✓ Handle session expiry
✗ Don't forget to release locks
✗ Don't use blocking waits without timeout
```
