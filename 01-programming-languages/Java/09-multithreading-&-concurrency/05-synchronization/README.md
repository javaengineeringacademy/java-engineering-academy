# 05 - Synchronization

## Overview

Synchronization prevents race conditions by ensuring only one thread accesses shared data at a time. The `synchronized` keyword and `volatile` field modifier are Java's basic synchronization tools.

## Learning Objectives

- Identify race conditions and fix them with synchronization
- Use synchronized blocks and methods correctly
- Understand mutual exclusion vs memory visibility
- Apply volatile for simple flags
- Avoid deadlocks with lock ordering

## Core Concepts

| Concept | Description |
|---------|-------------|
| Race Condition | Outcome depends on timing of thread execution |
| Mutual Exclusion | Only one thread accesses critical section |
| Monitor Lock | Intrinsic lock acquired by synchronized |
| volatile | Guarantees visibility but not atomicity |
| Deadlock | Two threads waiting for each other's locks |

## Synchronized Syntax

```java
// Synchronized method
public synchronized void increment() { count++; }

// Synchronized block
public void update() {
    synchronized (this) { count++; }
}

// Static synchronized
public static synchronized void staticMethod() { ... }
```

## Common Mistakes

1. **Locking on `this`** — external code can also lock on your object
2. **Using `notify()` instead of `notifyAll()`** — may miss threads
3. **Holding lock too long** — causes unnecessary contention
4. **Lock ordering violations** — causes deadlocks
