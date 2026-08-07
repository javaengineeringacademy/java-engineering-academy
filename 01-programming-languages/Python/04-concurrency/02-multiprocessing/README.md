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

## Production Checklist

- [ ] Always include `if __name__ == "__main__":` guard on Windows
- [ ] Ensure all data passed between processes is picklable
- [ ] Use `Pool` or `ProcessPoolExecutor` over manual `Process` management
- [ ] Set process pool size to `min(cpu_count(), expected_load)` to avoid over-subscription
- [ ] Use `multiprocessing.Queue` for inter-process communication (not `queue.Queue`)
- [ ] Share state with `Value`/`Array` and their locks for synchronized access
- [ ] Use `Pipe` for high-speed two-way communication between two processes
- [ ] Implement graceful shutdown with `pool.terminate()` and `pool.join()`
- [ ] Profile process creation overhead; reuse pools across tasks
- [ ] Monitor memory usage; each process has its own memory space

## Maturity Levels

| Level | Description |
|-------|-------------|
| **Beginner** | Creates processes with `Process(target=fn).start()`; uses `Pool.map()` for parallel map |
| **Intermediate** | Uses `Value`/`Array` for shared state; implements producer/consumer with `multiprocessing.Queue` |
| **Advanced** | Employs `ProcessPoolExecutor` with `as_completed()`; manages process pools with lifecycle hooks |
| **Expert** | Designs fault-tolerant pools with chunking strategies; tunes `fork` vs `spawn` start methods; uses shared memory and manager objects |

## Common Myths

1. **"Multiprocessing always makes code faster"** — Process creation overhead can outweigh gains for small tasks
2. **"fork and spawn are interchangeable"** — `fork` copies memory; `spawn` starts fresh; behavior differs on macOS
3. **"Shared state is easy with multiprocessing"** — Requires explicit locks; race conditions persist across processes
4. **"Pickling is only for serialization"** — It's required for inter-process data transfer; limits what you can pass
5. **"More processes = better performance"** — Over-subscription causes context switching; match CPU core count
6. **"ProcessPoolExecutor and Pool are identical"** — Pool manages workers; ProcessPoolExecutor integrates with `concurrent.futures`

## One-Minute Revision

- **Process**: Separate memory space; bypasses GIL; start/join lifecycle
- **Pool**: Worker pool for parallel map/starmap; auto-manages process lifecycle
- **ProcessPoolExecutor**: `concurrent.futures` interface; submit()/as_completed()/map()
- **Shared state**: `Value`/`Array` with locks; `multiprocessing.Manager()` for complex objects
- **Communication**: `Queue` (process-safe); `Pipe` (two-way, two processes)
- **Pickling**: All data passed between processes must be picklable
- **Start methods**: `fork` (default Unix, fast), `spawn` (default Windows, safer), `forkserver`
- **Best practice**: Use `Pool` for simple parallelism; `ProcessPoolExecutor` for futures-based patterns
- **Memory**: Each process has full copy; avoid large shared data; use `shared_memory` (Python 3.8+)
