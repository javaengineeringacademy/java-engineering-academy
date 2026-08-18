# Thread Basics — Memory Model

## Overview

This directory explores how memory is allocated for thread stacks, local variables, and thread-local data.

## Key Topics

### Thread Stack Memory

- Each thread has a private stack (default 1MB)
- Stack frames contain: local variables, method parameters, return address
- Stack is allocated by the OS, not the JVM heap
- Deep recursion causes `StackOverflowError`

### Local Variable Memory

- Primitive locals: stored directly on the stack (no GC overhead)
- Object reference locals: reference on stack, object on heap
- Stack-allocated objects (Valhalla): potential future optimization

### Thread-Local Storage

- Each thread has a `ThreadLocalMap` for ThreadLocal values
- Entries are `WeakReference<ThreadLocal>` keys → values
- Map grows lazily, entries cleaned up on next access

## Files

- [ThreadStackMemory.java](ThreadStackMemory.java) — Thread stack layout and local variables
