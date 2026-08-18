# Concurrent Collections — Memory Model

## Overview

This directory explores how memory is managed in concurrent collections — lock striping, snapshot iteration, and memory visibility.

## Key Topics

### ConcurrentHashMap Memory

- Single `Node<K,V>` array with volatile references
- Each node: key (final), value (volatile), hash (final), next (volatile)
- Resize creates a new table; old table is GC'd after transfer
- `CounterCell[]` for size tracking — avoids contention on single counter

### BlockingQueue Memory

- `ArrayBlockingQueue`: Pre-allocated `Object[]` array
- `LinkedBlockingQueue`: `Node` objects per enqueued item (heap allocation)
- Two locks: `takeLock` and `putLock` — allows concurrent put and take
- `notEmpty` and `notFull` conditions for thread coordination

### CopyOnWriteArrayList Memory

- On every write: new `Object[]` array is allocated (copy-on-write)
- Old array becomes eligible for GC when no iterators reference it
- Snapshot iteration: each iterator holds its own array reference

## Files

- [CollectionsMemory.java](CollectionsMemory.java) — Memory layout in concurrent collections
