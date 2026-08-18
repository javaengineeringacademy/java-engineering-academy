# Scheduled Executor — Memory Model

## Overview

This directory explores how memory is managed in scheduled executors — task scheduling data structures, periodic task references, and cleanup.

## Key Memory Concepts

### DelayedWorkQueue Memory

- Uses an array-based min-heap (`ScheduledFutureTask[]`)
- Grows dynamically (initial capacity 128)
- Each node stores the task, trigger time, and sequence number
- Removed tasks leave gaps that are compacted during heap operations

### Periodic Task Memory

- Fixed-rate tasks: the task object persists across invocations
- Fixed-delay tasks: same object, but re-offered after each execution
- If a task captures variables, those references persist for the task's lifetime
- Cancelled periodic tasks are removed from the queue, releasing references

### Task Retention

- By default, completed tasks are retained until `get()` is called or `purge()` is invoked
- `setRemoveOnCancelPolicy(true)` removes cancelled tasks immediately
- Periodic tasks accumulate if they execute faster than their interval

### Memory Leaks

- Long-running scheduled tasks can prevent garbage collection of captured objects
- `purge()` can be called periodically to clean up cancelled tasks
- In containers, use `setRemoveOnCancelPolicy(true)` to prevent stale task accumulation

## Files

- [ScheduledExecutorMemory.java](ScheduledExecutorMemory.java) — Memory layout and cleanup in scheduled executors
