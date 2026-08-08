# Concurrency

## Why Concurrency Matters

Every modern application needs to handle multiple tasks simultaneously — processing user requests while fetching data from databases, downloading files while updating progress bars, or running background jobs while serving responses. Python's concurrency models — threading for I/O-bound work, multiprocessing for CPU-bound work, and asyncio for asynchronous programs — provide the tools to handle these scenarios efficiently.

Without concurrency, your applications would process tasks sequentially, leading to poor responsiveness and wasted resources. That's why concurrency exists — it allows your programs to make progress on multiple fronts at once, improving throughput and user experience without requiring multiple machines.

## What You'll Learn

By the end of this module, you'll be able to:

- Spawn and manage threads for concurrent I/O operations
- Use multiprocessing to bypass the GIL for CPU-bound work
- Write asynchronous code with async/await syntax
- Synchronize shared state with locks, queues, and shared memory
- Choose the right concurrency model for a given problem

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | I/O-bound (threading), CPU-bound (multiprocessing), high-concurrency (asyncio) | Sequential for simple scripts |
| When NOT to use | Don't use threading for CPU work; don't use asyncio for CPU-bound | Use `multiprocessing` for CPU |
| Alternatives | concurrent.futures for simple pools, multiprocessing for CPU | Sequential processing |
| Production Examples | Web servers, data processing, background jobs | Simple scripts, prototypes |
| Common Mistakes | Race conditions, deadlocks, thread-unsafe globals | Use `Lock`, `Queue`, shared memory |

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | Threading | Thread, Lock, Queue, concurrent.futures for I/O-bound tasks |
| 02 | Multiprocessing | Process, Pool, shared memory for CPU-bound tasks |
| 03 | Asyncio | async/await, event loop, coroutines, async iteration |

## Prerequisites

- Python Fundamentals (01-fundamentals)
- Object-Oriented Programming (02-oop)

## Interview Questions

### Q1: When would you use threading vs multiprocessing?
**Answer:** Threading for I/O-bound tasks (file I/O, network). Multiprocessing for CPU-bound tasks (computation, data processing). GIL prevents CPU parallelism in threads.

### Q2: What is the difference between a thread and a process?
**Answer:** A process has its own memory space, a thread shares memory with its parent. Processes are heavier to create, threads are lighter but受限 by GIL.

### Q3: Explain asyncio's event loop.
**Answer:** The event loop runs coroutines, manages I/O events, and schedules callbacks. It's single-threaded but handles concurrency through cooperative multitasking.

### Q4: What is a race condition?
**Answer:** A race condition occurs when multiple threads access shared data concurrently and the result depends on timing. Use locks, queues, or thread-safe data structures.

### Q5: What is a deadlock?
**Answer:** A deadlock occurs when two or more threads wait for each other to release locks. Prevention: acquire locks in consistent order, use timeouts, use threading.RLock.

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

## Production Incidents

### Incident 1: Race Condition in Counter

**Problem:** Request counter showed 5000 instead of expected 10000
**Cause:** Multiple threads incrementing shared `counter` without synchronization
**Impact:** Metrics dashboard showed incorrect throughput numbers
**Detection:** Comparison with load balancer logs revealed discrepancy
**Solution:**
```python
from threading import Lock
counter_lock = Lock()
def increment():
    global counter
    with counter_lock:
        counter += 1
```
**Prevention:** Use `threading.Lock` for shared state; prefer `Queue` for thread communication; use `threading.local()` for per-thread state

### Incident 2: Deadlock in Payment Processing

**Problem:** Payment service hung indefinitely under high load
**Cause:** Two threads acquired locks in different orders (A→B and B→A)
**Impact:** 2000+ transactions stuck; revenue loss estimated at $50K
**Detection:** Health check timeouts; thread dumps showed deadlock
**Solution:**
```python
# Enforce consistent lock ordering
from contextlib import contextmanager

@contextmanager
def acquire_locks(lock1, lock2):
    with lock1:
        with lock2:
            yield

# Or use single lock for related operations
payment_lock = Lock()
def process_payment():
    with payment_lock:
        # All payment operations here
        pass
```
**Prevention:** Document lock ordering; use `threading.RLock` for reentrant locks; add timeout to `acquire()`

### Incident 3: GIL Causing CPU-Bound Thread Starvation

**Problem:** Web server response time degraded 10x during CPU-intensive operations
**Cause:** CPU-bound thread held GIL, starving I/O-bound request handlers
**Impact:** API latency increased from 50ms to 500ms during data processing
**Detection:** Response time monitoring alerted on degradation
**Solution:**
```python
# Move CPU-bound work to separate process
from multiprocessing import Process

def cpu_intensive_task(data):
    return heavy_computation(data)

# Launch in separate process, not thread
process = Process(target=cpu_intensive_task, args=(data,))
process.start()
# Continue handling I/O in main thread
```
**Prevention:** Profile to identify CPU vs I/O bound; use `multiprocessing` for CPU work; use `asyncio` for I/O

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

## Related Topics

- [10-internals](../10-internals/) - GIL and CPython internals
- [14-memory-management](../14-memory-management/) - Shared memory patterns
- [15-performance](../15-performance/) - Concurrency performance optimization

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Execute multiple tasks concurrently for better throughput |
| Complexity | Thread spawn: ~ms; Process spawn: ~100ms; Context switch: ~μs |
| Thread Safe | No — requires Lock, Queue, or shared memory primitives |
| Best Alternative | concurrent.futures for simple pool-based parallelism |
| When to Use | I/O-bound (threading), CPU-bound (multiprocessing), high-concurrency network (asyncio) |
| When to Avoid | Simple sequential scripts, shared mutable state without synchronization |

---

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Race condition in shared counter | `threading.enumerate()` + lock inspection | Add `threading.Lock` around shared state; use `Queue` for thread communication |
| Deadlock from inconsistent lock ordering | Thread dump via `faulthandler` or debugger | Enforce consistent lock acquisition order; use `threading.RLock` for reentrant locks |
| GIL starving CPU-bound threads | `sys.getswitchinterval()` monitoring | Move CPU work to `multiprocessing`; tune GIL interval for latency |
| Asyncio event loop blocking | `asyncio.debug()` mode enabled | Ensure no blocking I/O in async functions; use `asyncio.to_thread()` for sync code |
| Thread-unsafe global state | `threading.local()` inspection | Use `threading.local()` for per-thread state; avoid module-level mutable globals |

## Code Review Checklist

- [ ] `threading.Lock` used for all shared mutable state
- [ ] Lock acquisition order documented and consistent across codebase
- [ ] CPU-bound work delegated to `multiprocessing`, not `threading`
- [ ] Async I/O functions are non-blocking; no sync calls in `async def`
- [ ] Thread-unsafe globals protected with `threading.local()` or locks
- [ ] `concurrent.futures` used for simple pool-based parallelism
- [ ] `asyncio` used for high-concurrency network I/O, not CPU-bound tasks

## Architecture Considerations

Concurrency models determine system throughput and responsiveness. Threading suits I/O-bound work with moderate concurrency. Multiprocessing provides true parallelism for CPU-bound tasks. Asyncio enables high-concurrency with low overhead but requires careful coroutine design. The choice depends on workload type and latency requirements.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Threading with Lock | I/O-bound with shared state | Simple but GIL limits CPU parallelism |
| ProcessPoolExecutor | CPU-bound computation | True parallelism but IPC overhead |
| Asyncio with aiohttp | High-concurrency network I/O | Scalable but requires async ecosystem |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Race condition exposing shared data | Data corruption or information leakage | Use `threading.Lock` or `Queue` for thread communication |
| Thread-unsafe module-level state | Concurrent modification bugs | Use `threading.local()` for per-thread state |
| Asyncio task cancellation not handled | Resource leak or inconsistent state | Use `try/finally` in async context managers; implement proper cancellation |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Python 3.12+ | Task groups (`asyncio.TaskGroup`) | Replace `asyncio.gather()` with `TaskGroup` for structured concurrency |
| Python 3.13+ | Free-threaded mode (no GIL) | Test with `PYTHON_GIL=0`; prepare for true threading parallelism |
| Python 3.14+ | Per-interpreter GIL (PEP 684) | Enable sub-interpreters for isolation; reduces GIL contention |

## Version Validation

| Feature | Python Version | Status |
|---------|---------------|--------|
| `asyncio` | 3.4+ | Stable, mature async framework |
| `concurrent.futures` | 3.2+ | Stable, thread/process pool abstraction |
| `asyncio.TaskGroup` | 3.11+ | Stable, structured concurrency |
| Free-threaded mode | 3.13+ experimental | Available behind `PYTHON_GIL=0` |
