# Thread Creation — Memory Model

## Overview

This directory explores how memory is allocated during thread creation — thread objects, captured variables, and Future result storage.

## Key Topics

### Thread Object Allocation

- `new Thread()` allocates a Thread object on the heap
- Thread stack is allocated by the OS (not on the JVM heap)
- Thread object holds references to name, group, target Runnable
- After termination, Thread object becomes eligible for GC

### Lambda Capture Memory

- Lambdas that capture variables create objects on the heap
- Captured variables are copied into the lambda's fields
- Large captured objects increase per-task memory consumption
- Avoid capturing unnecessary large objects

### Future Result Storage

- `FutureTask` stores result in `outcome` field (volatile)
- Exception stored in `exception` field
- After `get()`, result is returned and FutureTask can be GC'd

## Files

- [ObjectCreationMemory.java](ObjectCreationMemory.java) — Object allocation during thread creation
