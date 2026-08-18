# Concurrent Collections — Internals

## Overview

This directory explores the internal implementation of concurrent collections — how `ConcurrentHashMap`, `BlockingQueue`, and `CopyOnWriteArrayList` achieve thread safety.

## Key Topics

### ConcurrentHashMap Internals (Java 8+)

- Single `Node` array (no segment locking since JDK 8)
- **Bin locking**: Only the bin (bucket) is locked during structural modification
- **CAS for empty bins**: Avoids locking when adding to empty bucket
- **synchronized on bin head**: For collision resolution
- **Size calculation**: Uses `CounterCell` striping for high throughput

### BlockingQueue Internals

- `ArrayBlockingQueue`: Single lock, two `Condition` objects (not-empty, not-full)
- `LinkedBlockingQueue`: Two locks (take lock + put lock) for better concurrency
- `SynchronousQueue`: No queue — direct handoff between producer and consumer
- `PriorityBlockingQueue`: Unbounded priority queue with a single lock

### CopyOnWriteArrayList Internals

- Array field is `volatile`
- Writes: copy array → modify → set reference (atomic via volatile)
- Iterators: hold reference to the array snapshot at creation time
- Thread-safe for iteration without external synchronization

## Files

- [ConcurrentHashMapInternals.java](ConcurrentHashMapInternals.java) — Concurrent collection internals
