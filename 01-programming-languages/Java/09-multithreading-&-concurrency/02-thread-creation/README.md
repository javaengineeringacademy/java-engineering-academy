# 02 - Thread Creation

## Overview

Java provides multiple ways to create threads: extending Thread, implementing Runnable, using Callable for return values, and lambdas. Understanding these approaches and when to use each is fundamental.

## Learning Objectives

- Create threads using different approaches
- Understand Runnable vs Callable
- Learn about thread naming and uncaught exception handling
- Know when to use each creation method

## Core Concepts

| Approach | Returns Value | Exception Handling | Use Case |
|----------|--------------|-------------------|----------|
| Thread subclass | No | In run() | Legacy code |
| Runnable | No | In run() | Simple tasks |
| Callable | Yes (via Future) | Checked exceptions | Tasks with results |
| Lambda Runnable | No | In run() | Concise tasks |

## Syntax

```java
// Callable with Future
Callable<Integer> task = () -> {
    Thread.sleep(1000);
    return 42;
};
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Integer> future = executor.submit(task);
Integer result = future.get(); // blocks until done
executor.shutdown();
```
