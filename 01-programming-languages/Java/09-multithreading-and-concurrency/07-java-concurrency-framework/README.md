# 07 - Java Concurrency Framework

## Overview

Java's `java.util.concurrent` package provides high-level concurrency utilities: ExecutorService for thread pool management, CompletableFuture for async composition, and ForkJoinPool for divide-and-conquer parallelism.

## Sub-Topics

| Topic | Description |
|-------|-------------|
| [00-introduction](00-introduction/) | Framework overview and Executor hierarchy |
| [01-executor-service](01-executor-service/) | Task submission and lifecycle |
| [02-thread-pools](02-thread-pools/) | Pool types, sizing, monitoring |
| [03-callable-future](03-callable-future/) | Callable, Future, async results |
| [04-scheduled-executor](04-scheduled-executor/) | Delayed and periodic task execution |
| [05-completable-future](05-completable-future/) | Async composition and chaining |
| [06-fork-join-framework](06-fork-join-framework/) | Work-stealing, recursive tasks |

## Key Classes

| Class | Purpose |
|-------|---------|
| ExecutorService | Thread pool for task submission |
| ThreadPoolExecutor | Configurable thread pool |
| ScheduledExecutorService | Delayed/periodic execution |
| CompletableFuture | Async composition |
| ForkJoinPool | Work-stealing pool |
| CountDownLatch | One-time synchronization barrier |
| CyclicBarrier | Reusable synchronization barrier |
| Semaphore | Permit-based access control |
