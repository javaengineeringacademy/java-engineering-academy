# Locks — Internals

## Overview

This directory explores the internal implementation of `ReentrantLock`, `ReadWriteLock`, `StampedLock`, and `Condition` — how they differ from monitors and when to use each.

## Key Topics

### ReentrantLock Internals

- Uses `AbstractQueuedSynchronizer` (AQS) as the foundation
- AQS maintains a **CLH queue** of waiting threads
- State field: 0 = unlocked, N = reentrant lock count
- `lock()`: CAS(0→1) for first acquire, else increment count
- `unlock()`: Decrement count, release when 0

### ReadWriteLock Internals

- `ReentrantReadWriteLock` uses AQS with split state
- **Write lock**: Exclusive (state = exclusive count)
- **Read lock**: Shared (state = shared count in upper bits)
- Multiple readers allowed, but only one writer

### StampedLock Internals

- Combines read, write, and optimistic read locks
- **Optimistic read**: No lock — just a stamp (version number)
- Validate stamp after optimistic read to detect concurrent writes
- Best for read-heavy, low-contention scenarios

### Condition Internals

- Each `Condition` is linked to a `ReentrantLock`
- `await()`: Releases lock, adds thread to condition wait queue
- `signal()`: Moves one thread from condition queue to lock queue
- Multiple conditions per lock allow fine-grained waiting

## Files

- [LockInternals.java](LockInternals.java) — Lock implementation details
