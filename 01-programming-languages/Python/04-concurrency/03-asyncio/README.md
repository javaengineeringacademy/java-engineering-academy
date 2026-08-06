# Asyncio

async/await, asyncio.gather, and asynchronous patterns.

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
