# Threading

When you need to perform multiple I/O-bound tasks concurrently, threading allows parallel execution without true parallelism. Python's Thread, Lock, Queue, and concurrent.futures provide tools for concurrent execution and synchronization.

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

## Production Checklist

- [ ] Always use `Lock` or `RLock` for shared mutable state
- [ ] Use `Queue` instead of shared lists for producer/consumer patterns
- [ ] Call `thread.join()` before exiting to ensure cleanup
- [ ] Profile CPU-bound work; switch to `multiprocessing` if threads slow down
- [ ] Set daemon threads for background tasks that shouldn't block shutdown
- [ ] Use `threading.local()` for thread-specific data
- [ ] Prefer `concurrent.futures.ThreadPoolExecutor` over manual thread management
- [ ] Implement timeouts on `Lock.acquire()` to prevent deadlocks
- [ ] Test with high thread counts in CI to expose race conditions
- [ ] Monitor GIL contention with `sys.setswitchinterval()` tuning

## Maturity Levels

| Level | Description |
|-------|-------------|
| **Beginner** | Creates threads with `Thread(target=fn).start()`; understands basic join and daemon threads |
| **Intermediate** | Uses Lock, Event, Condition for synchronization; implements producer/consumer with Queue |
| **Advanced** | Employs ThreadPoolExecutor with as_completed; handles GIL-aware profiling and deadlock detection |
| **Expert** | Designs thread pools with backpressure; uses threading.local for connection pools; tunes GIL switch interval |

## Common Myths

1. **"GIL means threads can't run concurrently"** — Threads run concurrently; they don't run in parallel for CPU-bound Python code
2. **"Lock makes code thread-safe"** — Only protects the critical section; race conditions exist outside the lock
3. **"Daemon threads clean up automatically"** — They're killed abruptly; use proper shutdown signals
4. **"More threads = faster"** — Thread creation has overhead; too many threads cause context switching thrashing
5. **"Global variables need no synchronization if protected by GIL"** — GIL protects bytecode execution, not compound operations
6. **"ThreadPoolExecutor handles all concurrency"** — Use ProcessPoolExecutor for CPU-bound, asyncio for high-concurrency I/O

## One-Minute Revision

- **threading.Thread**: `start()` launches; `join()` waits; daemon threads die with main thread
- **threading.Lock**: `with lock:` for mutual exclusion; non-reentrant; prevents race conditions
- **threading.RLock**: Reentrant lock; same thread can acquire multiple times
- **threading.Event**: Signal between threads; `set()` / `wait()` / `clear()`
- **Queue**: Thread-safe producer/consumer; `put()` / `get()` / `task_done()` / `join()`
- **ThreadPoolExecutor**: Manages thread pool; `submit()` / `as_completed()` / `map()`
- **GIL**: Only one thread executes Python bytecode; released during I/O and C extensions
- **Best practice**: Use Queue over shared state; prefer ProcessPoolExecutor for CPU-bound work
- **Deadlock prevention**: Always acquire locks in the same order; use timeouts

## Production Incidents

### Incident 1: Race Condition in Shared State

**Problem:** Two threads simultaneously modify a shared counter, causing lost updates and incorrect final values.

**Cause:** Non-atomic read-modify-write operations (`counter += 1`) without proper synchronization. The GIL doesn't prevent race conditions at the Python level.

**Impact:** Incorrect totals in financial calculations. Inventory counts drift. Data corruption that's hard to reproduce and debug.

**Detection:** Run concurrent tests with high thread counts. Use `threading.Barrier` to force race conditions in tests. Add assertions on final values.

**Solution:** Use `threading.Lock` for critical sections:
```python
import threading

counter = 0
lock = threading.Lock()

def increment():
    global counter
    with lock:  # atomic operation
        counter += 1
```

** Prevention:** Prefer `Queue` over shared state. Use `threading.local()` for thread-specific data. Write concurrent tests in CI.

---

### Incident 2: GIL Causing CPU-Bound Slowdown

**Problem:** CPU-intensive workloads run slower with multiple threads than with a single thread due to GIL contention.

**Cause:** Python's Global Interpreter Lock (GIL) allows only one thread to execute Python bytecode at a time. CPU-bound threads spend time acquiring and releasing the GIL.

**Impact:** Application throughput drops 2-10x compared to sequential execution. CPU usage shows high context switching overhead. Response times increase under load.

**Detection:** Profile with `cProfile` to identify CPU-bound hotspots. Monitor CPU usage across cores. Benchmark single vs. multi-threaded performance.

**Solution:** Use `multiprocessing` instead of `threading` for CPU-bound tasks:
```python
from multiprocessing import Pool

def cpu_intensive(x):
    return sum(i * i for i in range(x))

with Pool(4) as p:
    results = p.map(cpu_intensive, data_chunks)
```

** Prevention:** Profile early to identify CPU vs. I/O bound work. Use `concurrent.futures.ProcessPoolExecutor` for CPU-bound work. Consider `asyncio` for I/O-bound work.
