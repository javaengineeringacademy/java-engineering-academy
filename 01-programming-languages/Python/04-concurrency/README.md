# Concurrency

Execute multiple tasks concurrently — threading for I/O-bound work, multiprocessing for CPU-bound work, and asyncio for asynchronous programs.

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | Threading | Thread, Lock, Queue, concurrent.futures for I/O-bound tasks |
| 02 | Multiprocessing | Process, Pool, shared memory for CPU-bound tasks |
| 03 | Asyncio | async/await, event loop, coroutines, async iteration |

## Prerequisites

- Python Fundamentals (01-fundamentals)
- Object-Oriented Programming (02-oop)

## Learning Objectives

By the end of this module you will be able to:

- Spawn and manage threads for concurrent I/O operations
- Use multiprocessing to bypass the GIL for CPU-bound work
- Write asynchronous code with async/await syntax
- Synchronize shared state with locks, queues, and shared memory
- Choose the right concurrency model for a given problem

## Quick Start

```bash
# Run any topic directly
python 01-threading/threading_basics.py
python 02-multiprocessing/multiprocessing_basics.py
python 03-asyncio/asyncio_basics.py
```
