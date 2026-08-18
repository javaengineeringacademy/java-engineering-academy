# Atomic Classes — Memory Model

## Overview

This directory explores how memory is managed in atomic classes — volatile semantics, CAS memory barriers, and the `Unsafe` API.

## Key Topics

### volatile Semantics in Atomic Classes

- `AtomicInteger.value` is `volatile` — reads/writes go to main memory
- CAS provides atomicity AND visibility
- No additional synchronization needed for simple operations

### CAS Memory Barriers

- CAS is a full memory barrier (acquire + release)
- Prior writes are visible to the CAS operation
- CAS writes are visible to subsequent reads by any thread
- This is stronger than volatile (volatile has separate read/write barriers)

### Unsafe Memory Access

- `Unsafe.getObject()` / `putObject()`: Direct memory access
- `compareAndSwapObject()`: CAS with hardware memory barriers
- `volatileIntFieldOffset()`: Gets the memory offset of a volatile field

### Memory Ordering

- Java guarantees CAS operations are totally ordered for each variable
- No torn reads/writes for 32-bit values
- 64-bit CAS is NOT guaranteed atomic on all platforms (but practically is on x86)

## Files

- [AtomicMemory.java](AtomicMemory.java) — Memory layout and visibility in atomic classes
