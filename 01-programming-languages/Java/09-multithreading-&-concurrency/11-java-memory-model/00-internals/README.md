# Java Memory Model — Internals

## Overview

This directory explores the internal mechanics of the Java Memory Model — happens-before relationships, memory barriers, and the rules that govern inter-thread visibility. The JMM is an abstract model that provides portable memory ordering guarantees across all JVM implementations.

## Key Topics

### Happens-Before Rules

The JMM defines 9 happens-before rules. If action A happens-before action B, then A's memory effects are visible to B.

| Rule | Description | Implementation |
|------|-------------|----------------|
| **Program Order** | Actions within a thread are ordered | Trivial — single thread sees own writes |
| **Monitor Lock** | Unlock HB every subsequent lock on same monitor | Acquire/release barriers |
| **Volatile** | Write HB every subsequent read of same volatile | StoreStore + StoreLoad on write; LoadLoad + LoadStore on read |
| **Thread Start** | `start()` HB every action in started thread | Internal lock/unlock pair |
| **Thread Termination** | All actions HB `join()` returns | Internal condition signal/wait |
| **Transitivity** | A HB B and B HB C → A HB C | Chain of happens-before edges |
| **Interruption** | `interrupt()` HB detection of interrupt | Volatile write/read on internal flag |
| **Finalizer** | Constructor end HB finalizer start | JMM lifecycle definition |
| **Object Constructor** | Field writes in constructor HB subthread actions | StoreStore barrier before publication |

### Memory Barrier Implementation

| Barrier | Prevents Reordering Of | Used By |
|---------|----------------------|---------|
| **LoadLoad** | Load before Load | volatile read, synchronized entry |
| **StoreStore** | Store before Store | volatile write, synchronized exit |
| **LoadStore** | Load before Store | volatile read, synchronized entry, synchronized exit |
| **StoreLoad** | Store before Load (full fence) | volatile write, synchronized exit |

### How volatile Maps to Barriers

**Volatile write:**
```
StoreStore barrier  ← prevents prior stores from moving past
[volatile write]
StoreLoad barrier   ← prevents write from moving past subsequent loads
```

**Volatile read:**
```
LoadLoad barrier    ← prevents subsequent loads from moving before
[volatile read]
LoadStore barrier   ← prevents subsequent stores from moving before
```

### How synchronized Maps to Barriers

**Entry (acquire semantics):**
```
LoadLoad barrier
LoadStore barrier
[enter synchronized block]
```

**Exit (release semantics):**
```
[exit synchronized block]
StoreStore barrier
LoadStore barrier
StoreLoad barrier
```

### Instruction Reordering

- **Compiler reordering**: Java compiler may reorder statements if it doesn't affect single-threaded behavior
- **CPU reordering**: Out-of-order execution may reorder loads/stores for performance
- **Memory model**: Defines which reorderings are allowed
- **Barriers**: Prevent specific reorderings

### Final Field Guarantee

- After constructor completes, all threads see correct final field values
- No synchronization required — the JMM enforces this
- Only applies if `this` reference doesn't escape during construction
- StoreStore barrier inserted before constructor returns

### Safe Publication

| Method | How It Works |
|--------|-------------|
| `volatile` field | Write HB read on the reference |
| `final` fields | Constructor writes HB constructor return |
| `synchronized` | Lock/unlock establishes happens-before |
| `AtomicReference` | Volatile semantics + atomic operations |

## Files

- [HappensBeforeInternals.java](HappensBeforeInternals.java) — Happens-before mechanics, transitivity chains, barrier types, final field guarantee
