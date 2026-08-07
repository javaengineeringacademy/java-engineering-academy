# Asyncio

When you need to handle many concurrent network connections efficiently, asyncio provides single-threaded concurrent execution. Python's async/await, asyncio.gather, and asynchronous patterns enable non-blocking I/O and high-concurrency servers.

## Overview

asyncio provides single-threaded concurrent execution using coroutines. It's ideal for I/O-bound tasks with many concurrent connections.

## When to Use

- Network I/O (HTTP, WebSocket, databases)
- High-concurrency servers
- Background tasks
- Coordinating multiple async operations

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Basic async | `async_basics.py:5-12` | async def, await |
| gather | `async_basics.py:16-33` | Parallel execution |
| Tasks | `async_basics.py:37-52` | create_task, cancel |
| Async iterator | `async_basics.py:56-71` | __aiter__, __anext__ |
| Async context | `async_basics.py:75-86` | __aenter__, __aexit__ |
| Semaphore | `async_basics.py:90-100` | Limit concurrency |
| Async generator | `async_basics.py:104-112` | async yield |
| Thread integration | `async_basics.py:126-134` | run_in_executor |

## Common Mistakes

1. **Forgetting await** — coroutine won't execute
2. **Blocking in async** — use run_in_executor for blocking calls
3. **Not using gather** — sequential awaits waste time
4. **Overusing asyncio** — use threading for simple I/O

## Interview Questions

1. What is the difference between asyncio and threading?
2. How does asyncio.gather work?
3. When would you use a Semaphore in asyncio?
4. What is the event loop?

## Production Incidents

### Incident 1: Blocking Call in Async Function

**Problem:** An async function calls a blocking library (e.g., `requests`, `time.sleep`), freezing the entire event loop and blocking all other coroutines.

**Cause:** Using synchronous/blocking functions inside `async def` without `await`. The blocking call holds the event loop thread, preventing other coroutines from running.

**Impact:** All concurrent HTTP requests timeout. Database connection pools exhausted. Application becomes unresponsive under load. Health checks fail.

**Detection:** Monitor event loop latency. Add timeouts to all I/O operations. Profile with `asyncio` debug mode enabled.

**Solution:** Use `asyncio.to_thread()` or `loop.run_in_executor()` for blocking calls:
```python
import asyncio
import requests  # blocking library

async def fetch_data(url):
    # Bad: blocks event loop
    # response = requests.get(url)

    # Good: runs in thread pool
    response = await asyncio.to_thread(requests.get, url)
    return response.json()
```

** Prevention:** Enable asyncio debug mode (`PYTHONASYNCIODEBUG=1`). Use async-native libraries (aiohttp, asyncpg). Add linting rules to flag blocking calls in async functions.

---

### Incident 2: Event Loop Starvation

**Problem:** A long-running coroutine monopolizes the event loop, preventing other coroutines from executing. All tasks appear to hang.

**Cause:** A coroutine performs too much work in a single iteration without yielding control via `await`. This blocks the event loop's single thread.

**Impact:** All scheduled tasks are delayed. Heartbeats fail, causing load balancers to mark the service as unhealthy. Cascading failures in dependent services.

**Detection:** Monitor task execution times. Add heartbeats that verify event loop responsiveness. Use `asyncio.all_tasks()` to check for stuck tasks.

**Solution:** Break long-running work into smaller chunks with `await asyncio.sleep(0)`:
```python
async def process_large_batch(items):
    for i, item in enumerate(items):
        await process(item)
        if i % 100 == 0:
            await asyncio.sleep(0)  # yield control
```

** Prevention:** Set timeouts on all coroutines. Use `asyncio.wait_for()` for bounded execution. Add monitoring for event loop latency.

## Production Incidents

### Incident 1: Blocking Call in Async Function

**Problem:** An async function calls a blocking library (e.g., `requests`, `time.sleep`), freezing the entire event loop and blocking all other coroutines.

**Cause:** Using synchronous/blocking functions inside `async def` without `await`. The blocking call holds the event loop thread, preventing other coroutines from running.

**Impact:** All concurrent HTTP requests timeout. Database connection pools exhausted. Application becomes unresponsive under load. Health checks fail.

**Detection:** Monitor event loop latency. Add timeouts to all I/O operations. Profile with `asyncio` debug mode enabled.

**Solution:** Use `asyncio.to_thread()` or `loop.run_in_executor()` for blocking calls:
```python
import asyncio
import requests  # blocking library

async def fetch_data(url):
    # Bad: blocks event loop
    # response = requests.get(url)

    # Good: runs in thread pool
    response = await asyncio.to_thread(requests.get, url)
    return response.json()
```

** Prevention:** Enable asyncio debug mode (`PYTHONASYNCIODEBUG=1`). Use async-native libraries (aiohttp, asyncpg). Add linting rules to flag blocking calls in async functions.

---

### Incident 2: Event Loop Starvation

**Problem:** A long-running coroutine monopolizes the event loop, preventing other coroutines from executing. All tasks appear to hang.

**Cause:** A coroutine performs too much work in a single iteration without yielding control via `await`. This blocks the event loop's single thread.

**Impact:** All scheduled tasks are delayed. Heartbeats fail, causing load balancers to mark the service as unhealthy. Cascading failures in dependent services.

**Detection:** Monitor task execution times. Add heartbeats that verify event loop responsiveness. Use `asyncio.all_tasks()` to check for stuck tasks.

**Solution:** Break long-running work into smaller chunks with `await asyncio.sleep(0)`:
```python
async def process_large_batch(items):
    for i, item in enumerate(items):
        await process(item)
        if i % 100 == 0:
            await asyncio.sleep(0)  # yield control
```

** Prevention:** Set timeouts on all coroutines. Use `asyncio.wait_for()` for bounded execution. Add monitoring for event loop latency.
