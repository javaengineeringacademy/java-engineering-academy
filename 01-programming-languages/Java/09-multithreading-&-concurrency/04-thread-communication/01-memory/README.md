# Thread Communication — Memory Model

## Overview

This directory explores how memory is managed during thread communication — monitor state, wait sets, and happens-before guarantees.

## Key Topics

### Monitor State in Memory

- Each object's monitor has an **owner** field (the owning thread)
- The **entry set** is a list of threads waiting to acquire the monitor
- The **wait set** is a list of threads waiting for notification
- These structures are managed by the JVM, not visible to Java code

### Happens-Before for Communication

- `notify()` happens-before the released thread returns from `wait()`
- `Thread.start()` happens-before any action in the started thread
- `Thread.join()` return happens-before any action after join
- These rules ensure memory visibility between communicating threads

### Spurious Wakeup Memory

- A thread may wake from `wait()` without `notify()` (spurious wakeup)
- The condition must be re-checked in a `while` loop
- The JVM may generate spurious wakeups for performance reasons

## Files

- [CommunicationMemory.java](CommunicationMemory.java) — Memory layout and visibility in communication
