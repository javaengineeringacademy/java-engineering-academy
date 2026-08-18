# Atomic Classes — Internals

## Overview

This directory explores the internal implementation of atomic classes — how CAS operations, spin loops, and the `Unsafe` API provide lock-free thread safety.

## Key Topics

### CAS Operations Internals

- `Unsafe.compareAndSwapInt()`: Native method → CPU `CMPXCHG` instruction
- Hardware guarantee: atomic read-modify-write in a single bus cycle
- No locks, no blocking — just a CAS loop

### AtomicLong Internals

- `value` field: volatile long
- `incrementAndGet()`: CAS loop: read → compute → CAS(expected, newValue)
- If CAS fails (another thread modified), retry in a loop
- Bounded retry count: falls back to `LongAdder` strategy

### LongAdder Internals

- `base`: Base value (updated via CAS when no contention)
- `cells[]`: Stripe array for high-contention updates
- Each thread updates its own cell (reduces contention)
- `sum()`: base + sum of all cells (not atomic but approximately correct)

### AtomicReference Internals

- Uses `Unsafe.compareAndSwapObject()` for reference CAS
- Supports ABA detection with `AtomicStampedReference`
- `weakCompareAndSet()`: May spuriously fail (allows JVM optimization)

## Files

- [CASInternals.java](CASInternals.java) — CAS and atomic class internals
