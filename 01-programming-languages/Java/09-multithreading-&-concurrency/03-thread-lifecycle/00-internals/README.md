# Thread Lifecycle — Internals

## Overview

This directory explores the internal state machine of Java threads — how state transitions are implemented and managed by the JVM.

## Key Topics

### State Transition Triggers

| From | To | Trigger |
|------|----|---------|
| NEW | RUNNABLE | `Thread.start()` |
| RUNNABLE | BLOCKED | Contention for monitor lock |
| BLOCKED | RUNNABLE | Lock acquired |
| RUNNABLE | WAITING | `wait()`, `join()`, `LockSupport.park()` |
| WAITING | RUNNABLE | `notify()`, `notifyAll()`, `unpark()` |
| RUNNABLE | TIMED_WAITING | `sleep(ms)`, `wait(ms)`, `join(ms)` |
| TIMED_WAITING | RUNNABLE | Timeout or notification |
| RUNNABLE | TERMINATED | `run()` completes or exception |

### JVM Thread State Management

- State is stored in the `Thread` object's `threadStatus` field
- State changes require CAS operations for thread safety
- `getState()` reads the field without synchronization (may be stale)

### Interrupt Mechanism

- `interrupt()` sets the `interrupt` flag on the target thread
- If thread is in `wait()`, `sleep()`, or `join()`, it receives `InterruptedException`
- `Thread.interrupted()` checks and clears the flag
- `isInterrupted()` checks without clearing

## Files

- [ThreadStateInternals.java](ThreadStateInternals.java) — Thread state machine and transitions
