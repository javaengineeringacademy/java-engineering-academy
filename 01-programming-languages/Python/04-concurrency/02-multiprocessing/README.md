# Multiprocessing

When you need to bypass the GIL and run CPU-bound tasks in parallel, multiprocessing provides true parallelism. Python's Process, Pool, shared state, and inter-process communication enable efficient multi-core computation.

## Overview

Multiprocessing bypasses the GIL by running separate Python processes. Each process has its own memory space and Python interpreter.

## When to Use

- CPU-bound tasks (math, data processing)
- Parallel computation
- Avoiding GIL limitations
- Multi-core utilization

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Basic process | `multiprocessing_basics.py:5-19` | Process, start, join |
| Pool map | `multiprocessing_basics.py:23-38` | pool.map, starmap |
| Shared Value | `multiprocessing_basics.py:42-58` | Value, get_lock |
| Shared Array | `multiprocessing_basics.py:61-72` | Array |
| Queue | `multiprocessing_basics.py:76-88` | Process-safe queue |
| ProcessPoolExecutor | `multiprocessing_basics.py:92-104` | concurrent.futures |
| Pipe | `multiprocessing_basics.py:108-120` | Two-way communication |

## Common Mistakes

1. **Forgetting `if __name__ == "__main__"`** — required on Windows
2. **Pickling errors** — shared objects must be picklable
3. **Overhead** — process creation is expensive
4. **Not using Pool** — manual Process management is error-prone

## Interview Questions

1. What is the difference between threading and multiprocessing?
2. When would you use Process over Thread?
3. How do processes communicate?
4. What is the difference between Pool and ProcessPoolExecutor?
