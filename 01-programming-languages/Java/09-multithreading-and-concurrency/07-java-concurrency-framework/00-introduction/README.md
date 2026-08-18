# Executor Framework Introduction

## Overview

The Executor framework decouples task submission from task execution. Instead of creating threads manually, submit tasks to an Executor that manages thread lifecycle.

## Executor Hierarchy

```
Executor
  └── ExecutorService (adds lifecycle)
        └── ScheduledExecutorService (adds scheduling)
              └── ScheduledThreadPoolExecutor
```

## Key Methods

| Method | Description |
|--------|-------------|
| execute(Runnable) | Submit task, no result |
| submit(Callable) | Submit task, returns Future |
| invokeAll() | Submit all, wait for all |
| invokeAny() | Submit all, return first result |
| shutdown() | Stop accepting new tasks |
| shutdownNow() | Stop and attempt to interrupt running |
| awaitTermination() | Block until all complete |
