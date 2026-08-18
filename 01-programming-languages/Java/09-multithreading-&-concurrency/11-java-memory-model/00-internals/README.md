# Java Memory Model — Internals

## Overview

This directory explores the internal mechanics of the Java Memory Model — happens-before relationships, memory barriers, and the rules that govern inter-thread visibility.

## Key Topics

### Happens-Before Rules

| Rule | Description |
|------|-------------|
| Program order | Action in a single thread happens-before later actions in that thread |
| Monitor unlock | Unlock happens-before every subsequent lock of the same monitor |
| Volatile | Write to volatile field happens-before every subsequent read of that field |
| Thread start | `start()` happens-before any action in the started thread |
| Thread join | `join()` return happens-before any action after join |
| Transitivity | If A hb B and B hb C, then A hb C |

### Memory Barrier Implementation

- **LoadLoad barrier**: Prevents loads from being reordered
- **StoreStore barrier**: Prevents stores from being reordered
- **LoadStore barrier**: Prevents loads from being reordered with prior stores
- **StoreLoad barrier**: Full barrier (expensive) — prevents all reorderings

### Instruction Reordering

- **Compiler reordering**: Java compiler may reorder statements
- **CPU reordering**: Out-of-order execution may reorder loads/stores
- **Memory model**: Defines which reorderings are allowed
- Barriers prevent specific reorderings

### Final Field Guarantee

- After constructor completes, all threads see correct final field values
- No synchronization required — the JMM enforces this
- Only applies if `this` reference doesn't escape during construction

## Files

- [HappensBeforeInternals.java](HappensBeforeInternals.java) — Happens-before mechanics
