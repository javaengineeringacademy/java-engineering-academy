# Thread Lifecycle — Memory Model

## Overview

This directory explores how memory is managed during thread state changes — stack preservation, interrupt flag storage, and state transitions.

## Key Topics

### Stack Preservation During WAITING

- When a thread enters WAITING, its stack is preserved in memory
- The thread is removed from the scheduler but its stack remains
- On wakeup, the stack is restored and execution resumes
- Platform threads: stack stays in OS memory; virtual threads: stack moves to heap

### Interrupt Flag Storage

- The `interrupt` flag is a boolean field in the Thread object
- Set atomically via CAS operations
- Thread-safe: multiple threads can check/set the flag concurrently
- Cleared on `Thread.interrupted()` or when `InterruptedException` is thrown

### State Field Memory

- `threadStatus` is an `int` field updated via CAS
- Reads may be stale without synchronization
- Thread states are visible to the JVM for scheduling decisions

## Files

- [LifecycleMemory.java](LifecycleMemory.java) — Memory management during lifecycle transitions
