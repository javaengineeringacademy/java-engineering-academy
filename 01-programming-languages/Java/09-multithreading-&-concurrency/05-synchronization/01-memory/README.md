# Synchronization — Memory Model

## Overview

This directory explores how memory is managed during synchronization — monitor state, volatile semantics, and the happens-before relationships that prevent data races.

## Key Topics

### Monitor Memory Structure

- Object header contains: hash code, age, lock state, biased thread ID
- **Unlocked**: Header stores normal object metadata
- **Thin lock**: Header stores lock record pointer (CAS)
- **Fat lock**: Object points to a heavyweight monitor (mutex + condition)

### volatile Memory Semantics

- Volatile read: Forces a load from main memory (LoadLoad barrier)
- Volatile write: Forces a store to main memory (StoreStore barrier)
- Prevents instruction reordering around volatile accesses
- Does NOT provide atomicity for compound operations (count++)

### Memory Visibility Guarantees

- Without synchronization: reads may see stale cached values
- Synchronized block: all writes inside are visible to next thread entering same monitor
- volatile write: all prior writes are visible to next volatile read of same variable

## Files

- [SynchronizationMemory.java](SynchronizationMemory.java) — Memory layout and visibility in synchronization
