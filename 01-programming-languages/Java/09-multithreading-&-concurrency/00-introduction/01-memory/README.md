# Introduction — Memory Model

## Overview

This directory explores how memory is allocated and accessed in Java's threading model — thread stacks, shared heap, and memory visibility.

## Key Topics

### Memory Layout Per Thread

- **Stack**: 1MB default per platform thread (configurable with `-Xss`)
- **Program counter**: Tracks current instruction per thread
- **Thread-local variables**: Stored on the stack, not shared

### Shared Heap Memory

- All threads share the same heap
- Instance fields and static fields are accessible from any thread
- No built-in synchronization — race conditions possible

### Memory Barriers

- Reads/writes to volatile variables create memory barriers
- Synchronized blocks establish happens-before edges
- Without barriers, CPU caches may hold stale values

## Files

- [ThreadMemoryLayout.java](ThreadMemoryLayout.java) — Thread memory layout and visibility
