# Synchronization — Internals

## Overview

This directory explores the internal mechanics of `synchronized`, `volatile`, and monitor locks — how the JVM implements mutual exclusion and memory visibility.

## Key Topics

### Monitor Lock Internals

- Every Java object has a header word containing the monitor
- **Thin lock**: CAS-based lightweight lock (no OS involvement)
- **Fat lock**: OS mutex when contention occurs
- **Biased locking**: Optimization for single-threaded access (removed in JDK 15)

### synchronized Block Internals

1. Thread attempts CAS on object header to acquire lock
2. If successful: enters the block (thin lock)
3. If contended: inflates to fat lock, thread blocks in OS
4. On exit: releases lock, another blocked thread may acquire

### volatile Internals

- Volatile read: LoadFence + LoadLoad + LoadStore barriers
- Volatile write: StoreStore + StoreStore + StoreFence barriers
- Ensures writes go directly to main memory
- Ensures reads come from main memory (not CPU cache)

### happens-before Rules

- Unlock of a monitor happens-before every subsequent lock of that monitor
- Volatile write happens-before every subsequent volatile read
- Thread.start() happens-before any action in started thread
- Thread.join() return happens-before any action after join

## Files

- [MonitorLockInternals.java](MonitorLockInternals.java) — Monitor lock and volatile internals
