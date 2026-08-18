# 01 - Thread Basics

## Overview

The `Thread` class and `Runnable` interface are the foundation of Java concurrency. Understanding how threads work, their lifecycle, and how to manage them is essential before diving into advanced concurrency.

## Learning Objectives

- Understand the Thread class and its key methods
- Learn the Runnable interface and why it's preferred over extending Thread
- Understand daemon threads and thread groups
- Learn about thread priorities and scheduling

## Prerequisites

- 00-introduction: Why multithreading exists

## Core Concepts

| Concept | Description |
|---------|-------------|
| Thread | A thread of execution within a process |
| Runnable | Functional interface for thread body |
| Daemon Thread | Background thread that doesn't prevent JVM shutdown |
| Thread Group | Container for managing a group of threads |

## Syntax

```java
// Extending Thread
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running");
    }
}

// Implementing Runnable
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running");
    }
}

// Lambda
Thread t = new Thread(() -> System.out.println("Lambda thread"));
t.start();
```

## Key Methods

| Method | Description |
|--------|-------------|
| start() | Begin execution, calls run() in new thread |
| run() | Task to execute (override or pass Runnable) |
| join() | Wait for thread to complete |
| sleep(ms) | Pause for specified milliseconds |
| interrupt() | Request thread to stop |
| setDaemon(boolean) | Mark as daemon thread |
| setPriority(int) | Set thread priority (1-10) |
