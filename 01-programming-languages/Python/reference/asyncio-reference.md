# Python asyncio Reference

## What is asyncio?

asyncio is a module for writing concurrent code using the async/await syntax. It's designed for I/O-bound and high-level structured network code.

## Why does asyncio matter?

Understanding asyncio helps you:
- Write concurrent I/O-bound programs
- Handle thousands of connections efficiently
- Avoid callback hell
- Write cleaner asynchronous code

---

## 1. Coroutines

```python
import asyncio

# Basic coroutine
async def say_hello():
    print("Hello")
    await asyncio.sleep(1)
    print("World")

# Run coroutine
asyncio.run(say_hello())
```

---

## 2. Tasks

```python
import asyncio

async def fetch_data(name, delay):
    print(f"Fetching {name}")
    await asyncio.sleep(delay)
    print(f"Got {name}")
    return f"{name} data"

async def main():
    # Create tasks
    task1 = asyncio.create_task(fetch_data("A", 2))
    task2 = asyncio.create_task(fetch_data("B", 1))
    
    # Wait for tasks
    result1 = await task1
    result2 = await task2
    
    print(result1, result2)

asyncio.run(main())
```

---

## 3. Futures

```python
import asyncio

async def set_future_result(future):
    await asyncio.sleep(1)
    future.set_result("Result")

async def main():
    loop = asyncio.get_event_loop()
    future = loop.create_future()
    
    asyncio.create_task(set_future_result(future))
    
    result = await future
    print(result)

asyncio.run(main())
```

---

## 4. Gathering

```python
import asyncio

async def fetch_data(name, delay):
    await asyncio.sleep(delay)
    return f"{name} data"

async def main():
    # Gather multiple tasks
    results = await asyncio.gather(
        fetch_data("A", 2),
        fetch_data("B", 1),
        fetch_data("C", 3)
    )
    print(results)

asyncio.run(main())
```

---

## 5. Waiting

```python
import asyncio

async def fetch_data(name, delay):
    await asyncio.sleep(delay)
    return f"{name} data"

async def main():
    tasks = [
        fetch_data("A", 2),
        fetch_data("B", 1),
        fetch_data("C", 3)
    ]
    
    # Wait with timeout
    done, pending = await asyncio.wait(tasks, timeout=2)
    
    for task in done:
        print(task.result())

asyncio.run(main())
```

---

## 6. Queues

```python
import asyncio

async def producer(queue):
    for i in range(5):
        await queue.put(i)
        print(f"Produced {i}")
    await queue.put(None)  # Signal completion

async def consumer(queue):
    while True:
        item = await queue.get()
        if item is None:
            break
        print(f"Consumed {item}")
        queue.task_done()

async def main():
    queue = asyncio.Queue()
    
    # Run producer and consumer
    await asyncio.gather(
        producer(queue),
        consumer(queue)
    )

asyncio.run(main())
```

---

## 7. Synchronization

```python
import asyncio

async def worker(lock, name):
    async with lock:
        print(f"{name} has lock")
        await asyncio.sleep(1)
        print(f"{name} releasing lock")

async def main():
    lock = asyncio.Lock()
    
    await asyncio.gather(
        worker(lock, "A"),
        worker(lock, "B"),
        worker(lock, "C")
    )

asyncio.run(main())
```

---

## 8. Event Loop

```python
import asyncio

async def main():
    loop = asyncio.get_event_loop()
    
    # Get current time
    now = loop.time()
    
    # Call soon
    loop.call_soon(print, "Called soon")
    
    # Call later
    loop.call_later(1, print, "Called later")
    
    # Call at
    loop.call_at(now + 2, print, "Called at")

asyncio.run(main())
```

---

## One-Minute Revision Table

| Concept | Description | Example |
|---------|-------------|---------|
| **async** | Define coroutine | `async def func():` |
| **await** | Wait for coroutine | `await coroutine()` |
| **create_task** | Create task | `asyncio.create_task(coro())` |
| **gather** | Wait for multiple | `await asyncio.gather(*tasks)` |
| **wait** | Wait with options | `await asyncio.wait(tasks)` |
| **Queue** | Async queue | `asyncio.Queue()` |
| **Lock** | Async lock | `asyncio.Lock()` |
| **Event** | Async event | `asyncio.Event()` |
| **Semaphore** | Async semaphore | `asyncio.Semaphore(n)` |
| **run** | Run coroutine | `asyncio.run(coro())` |

---

## Common Mistakes

### 1. Forgetting to Await

```python
# WRONG
async def main():
    asyncio.sleep(1)  # Warning: coroutine never awaited

# RIGHT
async def main():
    await asyncio.sleep(1)
```

### 2. Blocking the Event Loop

```python
# WRONG
async def main():
    time.sleep(1)  # Blocks event loop

# RIGHT
async def main():
    await asyncio.sleep(1)
```

### 3. Not Using create_task

```python
# WRONG (sequential)
async def main():
    await fetch_data("A")
    await fetch_data("B")

# RIGHT (concurrent)
async def main():
    await asyncio.gather(
        fetch_data("A"),
        fetch_data("B")
    )
```

### 4. Forgetting to Handle Exceptions

```python
# WRONG
async def main():
    task = asyncio.create_task(risky_operation())
    # Task exception may be lost

# RIGHT
async def main():
    task = asyncio.create_task(risky_operation())
    try:
        await task
    except Exception as e:
        print(f"Error: {e}")
```

---

## Production Notes

1. **Use async/await for I/O-bound tasks** - Don't use for CPU-bound
2. **Use create_task for concurrent operations** - More efficient
3. **Use gather for multiple tasks** - Wait for all to complete
4. **Use wait for advanced waiting** - With timeout, return when
5. **Use Queue for producer-consumer** - Decouple producers and consumers
6. **Use synchronization primitives** - For shared state
7. **Handle exceptions properly** - Use try/except or gather(return_exceptions=True)
8. **Don't block the event loop** - Use run_in_executor for CPU-bound
9. **Use asyncio.run()** - For entry points
10. **Test async code properly** - Use pytest-asyncio

---

## Further Reading

- Python documentation on asyncio module
- PEP 492 - async/await syntax
- asyncio documentation
