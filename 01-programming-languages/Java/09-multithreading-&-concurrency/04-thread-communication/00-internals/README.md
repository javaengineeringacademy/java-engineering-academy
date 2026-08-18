# Thread Communication — Internals

## Overview

This directory explores the internal mechanics of `wait()`, `notify()`, `join()`, and other thread communication mechanisms.

## Key Topics

### wait()/notify() Internals

- `wait()` atomically releases the monitor and parks the thread
- Thread is added to the object's **entry set** (wait set)
- `notify()` removes one thread from the wait set and moves it to the entry set
- `notifyAll()` moves all threads from the wait set to the entry set
- Thread must re-acquire the monitor before returning from `wait()`

### Thread.join() Internals

- `join()` calls `wait(0)` on the target Thread object
- The joining thread parks until the target thread terminates
- Target thread calls `notifyAll()` on itself when it finishes

### LockSupport Internals

- `park()`: Parks the current thread (no lock required)
- `unpark(thread)`: Unparks the specified thread
- Uses a permit counter (0 or 1) — `park` consumes, `unpark` produces
- More flexible than `wait()`/`notify()` — no monitor required

## Files

- [WaitNotifyInternals.java](WaitNotifyInternals.java) — wait/notify and join internals
