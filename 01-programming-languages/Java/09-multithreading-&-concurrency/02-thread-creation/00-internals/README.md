# Thread Creation — Internals

## Overview

This directory explores the internal mechanisms of thread creation — how `Thread.start()`, `Runnable`, `Callable`, and lambdas create executable tasks.

## Key Topics

### Thread.start() Internals

1. Validates thread state is NEW
2. Calls native `start0()` (JNI → OS `pthread_create`)
3. JVM allocates thread stack
4. JVM registers thread in thread list
5. New thread calls `Thread.run()` → `Runnable.run()`

### Runnable vs Callable Internals

- `Runnable.run()`: Returns void, no checked exceptions
- `Callable.call()`: Returns value, declares `throws Exception`
- `FutureTask` wraps `Callable` and implements `Runnable`
- `FutureTask.call()` invokes the callable and stores the result

### Lambda Internals

- Lambdas are compiled to private methods in the enclosing class
- Captured variables are stored as fields in the lambda object
- The lambda object implements the target functional interface
- No performance difference from anonymous classes

## Files

- [CallableInternals.java](CallableInternals.java) — Thread creation and Callable internals
