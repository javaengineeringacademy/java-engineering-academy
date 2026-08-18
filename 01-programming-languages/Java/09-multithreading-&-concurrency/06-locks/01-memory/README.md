# Locks — Memory Model

## Overview

This directory explores how memory is managed in lock implementations — AQS state, CLH queues, and the memory barriers that ensure visibility.

## Key Topics

### AQS Memory Structure

- **State field**: Atomic integer (volatile read/write via CAS)
- **CLH queue**: Doubly-linked list of `Node` objects (each holding thread reference)
- Queue nodes are allocated on the heap and GC'd when removed
- CAS operations provide lock-free state transitions

### Memory Barriers in Locks

- `lock()`: Acquire barrier — all subsequent reads/writes see current state
- `unlock()`: Release barrier — all prior reads/writes are visible to next acquirer
- `tryLock()` (non-blocking): CAS provides atomic acquire/release semantics

### ReadWriteLock Memory

- Read lock acquisition: CAS on shared state (multiple readers allowed)
- Write lock acquisition: CAS on exclusive state (blocks all readers/writers)
- Both establish happens-before relationships with prior releases

## Files

- [LockMemory.java](LockMemory.java) — Memory layout and visibility in lock implementations
