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

## Production Checklist

### ✅ Before using concurrency in production:

☐ I know the time/space complexity of thread/process creation
☐ I know common mistakes (race conditions, deadlocks, thread-unsafe globals)
☐ I know alternatives (asyncio vs threading vs multiprocessing, concurrent.futures)
☐ I know limitations (GIL prevents true threading parallelism for CPU work)
☐ I know how to debug it (threading.enumerate, logging with thread names, debugger attach)
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Threading makes Python programs faster
**Reality:** Threading helps I/O-bound tasks only. For CPU-bound work, use multiprocessing to bypass the GIL.

### ❌ Myth 2: Asyncio is always better than threading
**Reality:** asyncio shines for high-concurrency network I/O but adds complexity. Threading is simpler for moderate I/O-bound tasks.

### ❌ Myth 3: Multiprocessing has no overhead
**Reality:** Process creation and IPC have significant overhead. Use it only when parallelism gains outweigh the cost.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Execute multiple tasks concurrently for better throughput |
| Complexity | Thread spawn: ~ms; Process spawn: ~100ms; Context switch: ~μs |
| Thread Safe | No — requires Lock, Queue, or shared memory primitives |
| Best Alternative | concurrent.futures for simple pool-based parallelism |
| When to Use | I/O-bound (threading), CPU-bound (multiprocessing), high-concurrency network (asyncio) |
| When to Avoid | Simple sequential scripts, shared mutable state without synchronization |
