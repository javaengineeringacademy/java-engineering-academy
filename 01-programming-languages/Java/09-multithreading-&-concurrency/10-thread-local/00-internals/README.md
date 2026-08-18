# ThreadLocal — Internals

## Overview

This directory explores how `ThreadLocal` works internally — per-thread storage, the `ThreadLocalMap`, and cleanup strategies.

## Key Topics

### ThreadLocal Internals

- `ThreadLocal.get()`: Accesses `Thread.currentThread().threadLocals`
- `ThreadLocalMap`: Open-addressing hash map with `WeakReference<ThreadLocal>` keys
- Each entry: `WeakReference<ThreadLocal>` key + `Object` value
- No hash table resizing — linear probing for collisions

### InheritableThreadLocal Internals

- Child thread inherits parent's values at creation time
- `childValue()` method creates a copy of the parent's value
- Only the initial value is copied — subsequent changes in parent don't propagate
- Virtual threads use copy-on-write semantics

### ThreadLocalRandom Internals

- Each thread has its own random state (no contention)
- `ThreadLocalRandom.current()` returns the current thread's instance
- Uses `Unsafe` to access per-thread random seed directly
- Much faster than `java.util.Random` in multi-threaded scenarios

## Files

- [ThreadLocalInternals.java](ThreadLocalInternals.java) — ThreadLocal implementation details
