# 00 - Introduction to Multithreading

## Overview

Multithreading allows a program to perform multiple tasks concurrently, improving responsiveness, throughput, and resource utilization. Java was designed from the ground up with multithreading support.

## Learning Objectives

- Understand what threads are and how they differ from processes
- Learn why multithreading is necessary
- Understand the relationship between threads and the JVM
- Know when to use multithreading and when to avoid it
- Understand the difference between concurrency and parallelism

## Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Basic understanding of how programs execute on a CPU

## Why This Concept Exists

Before multithreading, programs executed sequentially:
1. **Idle CPU**: When waiting for I/O, the CPU sits idle
2. **Poor responsiveness**: GUI applications freeze during long operations
3. **Underutilization**: Modern CPUs have multiple cores, but sequential programs use only one

Multithreading solves these by overlapping I/O with computation, improving responsiveness, using multiple cores, and simplifying program structure.

## Core Concepts

| Concept | Description |
|---------|-------------|
| Thread | Lightweight process sharing memory with other threads |
| Process | Independent program with its own memory space |
| Concurrency | Multiple tasks making progress (not necessarily simultaneously) |
| Parallelism | Multiple tasks executing simultaneously on different cores |
| Thread Safety | Code that behaves correctly when accessed by multiple threads |

## Thread States

```
NEW → RUNNABLE → BLOCKED → RUNNABLE → TERMINATED
           ↓
      WAITING / TIMED_WAITING
```

| State | Description |
|-------|-------------|
| NEW | Created but not started |
| RUNNABLE | Executing or ready to execute |
| BLOCKED | Waiting for a monitor lock |
| WAITING | Waiting indefinitely |
| TIMED_WAITING | Waiting with a timeout |
| TERMINATED | Completed execution |

## References

- [Oracle Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Java Concurrency in Practice](https://jcip.net/)
