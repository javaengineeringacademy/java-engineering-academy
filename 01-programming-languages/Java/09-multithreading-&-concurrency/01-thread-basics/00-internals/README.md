# Thread Basics — Internals

## Overview

This directory explores the internal implementation of the `Thread` class — its fields, native methods, and how the JVM manages thread objects.

## Key Topics

### Thread Class Structure

- **name**: Thread identifier (String)
- **priority**: 1-10 (default 5)
- **daemon**: Background thread flag
- **target**: Runnable to execute (set in constructor)
- **group**: ThreadGroup for batch management
- **state**: Current `Thread.State` enum value

### Native Thread Operations

- `start0()`: Native method that creates the OS thread
- `sleep0()`: Native sleep (may use `Thread.sleep()` or `parkNanos`)
- `yield0()`: Native yield hint to the OS scheduler
- `interrupt0()`: Native interrupt signal

### Thread Lifecycle Internals

- NEW: Object created, no OS thread yet
- RUNNABLE: OS thread created, may be waiting for CPU
- BLOCKED: Waiting to acquire a monitor lock
- WAITING: Waiting for notification (wait/join/park)
- TIMED_WAITING: Waiting with a timeout
- TERMINATED: Thread has completed

## Files

- [ThreadClassInternals.java](ThreadClassInternals.java) — Thread class structure and native methods
