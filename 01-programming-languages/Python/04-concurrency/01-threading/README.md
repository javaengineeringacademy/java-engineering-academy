# Threading

Thread, Lock, Queue, and concurrent.futures.

## Overview

Threading enables concurrent execution. Python's GIL limits true parallelism for CPU-bound tasks, but threads are effective for I/O-bound work.

## When to Use

- I/O-bound tasks (network, file, database)
- GUI responsiveness
- Background tasks (daemon threads)
- Producer/consumer patterns

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Basic thread | `threading_basics.py:5-17` | Thread, start, join |
| Thread result | `threading_basics.py:21-36` | Return values via class |
| Lock | `threading_basics.py:40-54` | Mutual exclusion |
| Event | `threading_basics.py:61-72` | Signal between threads |
| Queue | `threading_basics.py:76-90` | Producer/consumer |
| ThreadPoolExecutor | `threading_basics.py:94-106` | as_completed |
| GIL | `threading_basics.py:110-113` | I/O vs CPU bound |
| Daemon threads | `threading_basics.py:129-134` | Background tasks |

## Common Mistakes

1. **Race conditions** — always use Lock for shared state
2. **Forgetting to join** — main thread may exit early
3. **Using threads for CPU-bound** — use multiprocessing instead
4. **Deadlocks** — always acquire locks in same order

## Interview Questions

1. What is the GIL and how does it affect threading?
2. What is the difference between Lock and RLock?
3. When would you use Queue over a shared list?
4. How do you handle thread exceptions?
