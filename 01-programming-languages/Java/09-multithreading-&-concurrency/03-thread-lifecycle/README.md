# 03 - Thread Lifecycle

## Overview

A thread exists in one of six states throughout its lifetime. Understanding state transitions is critical for debugging concurrency issues and writing correct concurrent code.

## Learning Objectives

- Understand all six thread states
- Learn what triggers each state transition
- Know how to inspect thread state programmatically
- Understand interrupted vs blocked vs waiting

## Thread States

| State | Description | How to Enter | How to Leave |
|-------|-------------|--------------|--------------|
| NEW | Created, not started | `new Thread()` | `start()` |
| RUNNABLE | Ready or running | `start()`, from BLOCKED/WAITING | Scheduler preempts, `yield()` |
| BLOCKED | Waiting for monitor lock | Contention on synchronized | Lock acquired |
| WAITING | Indefinite wait | `wait()`, `join()`, `park()` | `notify()`, `unpark()`, thread completes |
| TIMED_WAITING | Timed wait | `sleep(ms)`, `wait(ms)`, `join(ms)` | Timeout, `notify()`, `unpark()` |
| TERMINATED | Completed | `run()` completes or exception | N/A |

## Debugging

```java
// Get thread state
Thread.State state = thread.getState();

// Thread dump analysis
// jstack <pid> — shows all thread states and stack traces
```
