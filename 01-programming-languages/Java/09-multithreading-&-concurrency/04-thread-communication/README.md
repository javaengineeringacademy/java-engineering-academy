# 04 - Thread Communication

## Overview

Threads often need to coordinate: waiting for each other, signaling events, or sharing data safely. Java provides `wait()`, `notify()`, `notifyAll()`, `join()`, and interrupt mechanisms for thread communication.

## Learning Objectives

- Use wait/notify for producer-consumer patterns
- Understand why notifyAll is preferred over notify
- Use Thread.join() for thread ordering
- Handle InterruptedException properly
- Understand Thread.interrupt() and interrupted status

## Core Concepts

| Mechanism | Purpose |
|-----------|---------|
| `wait()` | Release lock and wait for notification |
| `notify()` | Wake one waiting thread |
| `notifyAll()` | Wake all waiting threads |
| `join()` | Wait for another thread to complete |
| `interrupt()` | Signal thread to stop |
| `Thread.interrupted()` | Check and clear interrupt status |

## Producer-Consumer Pattern

```java
synchronized (buffer) {
    while (buffer.isEmpty()) buffer.wait();
    item = buffer.remove();
    buffer.notifyAll();
}
```
