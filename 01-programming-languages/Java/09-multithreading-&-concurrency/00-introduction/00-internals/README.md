# Introduction — Internals

## Overview

This directory explores the internal implementation of Java threads — how the JVM creates threads, manages their lifecycle, and interacts with the OS.

## Key Topics

### Thread Creation Internals

- `Thread.start()` calls native `start0()` which invokes OS thread creation
- Each Java thread maps to a native OS thread (1:1 mapping since JDK 1.3)
- Thread object is allocated on the heap; stack is allocated by the OS

### Thread Object Structure

- **Program counter**: Points to current bytecode instruction
- **Stack**: Local variables, method frames, return addresses
- **Thread-local storage**: Per-thread data (ThreadLocal values)
- **State**: NEW → RUNNABLE → ... → TERMINATED

### JVM Thread Management

- JVM maintains a thread list with references to all live threads
- Thread group hierarchy for batch operations
- Daemon vs user thread distinction for JVM shutdown

## Files

- [ThreadInternals.java](ThreadInternals.java) — Thread creation and lifecycle internals
