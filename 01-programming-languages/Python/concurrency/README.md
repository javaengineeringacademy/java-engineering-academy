# Python Concurrency

A detailed guide to Python concurrency, parallelism, and async programming.

## Table of Contents

- [Concurrency vs Parallelism](#concurrency-vs-parallelism)
- [Threading](#threading)
- [Multiprocessing](#multiprocessing)
- [asyncio](#asyncio)
- [concurrent.futures](#concurrentfutures)
- [GIL (Global Interpreter Lock)](#gil-global-interpreter-lock)
- [Coroutines](#coroutines)
- [Event Loop](#event-loop)
- [Synchronization Primitives](#synchronization-primitives)
- [Best Practices](#best-practices)

---

## Concurrency vs Parallelism

```python
# Concurrency: Dealing with multiple things at once (interleaved)
# Parallelism: Doing multiple things at once (simultaneous)

# I/O-bound tasks: Use threading or asyncio
# CPU-bound tasks: Use multiprocessing
```

---

## Threading

### Basic Threading

```python
import threading
import time

def worker(name, duration):
    print(f"Worker {name} starting")
    time.sleep(duration)
    print(f"Worker {name} finished")

# Creating threads
thread1 = threading.Thread(target=worker, args=("A", 2))
thread2 = threading.Thread(target=worker, args=("B", 1))

# Starting threads
thread1.start()
thread2.start()

# Waiting for completion
thread1.join()
thread2.join()

print("All workers finished")
```

### Thread with Class

```python
import threading
import time

class Worker(threading.Thread):
    def __init__(self, name, duration):
        super().__init__()
        self.name = name
        self.duration = duration
        self.result = None

    def run(self):
        print(f"Worker {self.name} starting")
        time.sleep(self.duration)
        self.result = f"Result from {self.name}"
        print(f"Worker {self.name} finished")

workers = [Worker(f"Worker-{i}", i) for i in range(3)]

for w in workers:
    w.start()

for w in workers:
    w.join()
    print(w.result)
```

### Daemon Threads

```python
import threading
import time

def background_task():
    while True:
        print("Background task running")
        time.sleep(1)

# Daemon thread dies when main thread exits
daemon = threading.Thread(target=background_task, daemon=True)
daemon.start()

time.sleep(3)
print("Main thread exiting")
# Daemon thread is killed
```

### Thread Pool

```python
from concurrent.futures import ThreadPoolExecutor
import requests

def fetch_url(url):
    response = requests.get(url)
    return response.status_code

urls = [
    "https://example.com",
    "https://httpbin.org/get",
    "https://httpbin.org/ip",
]

with ThreadPoolExecutor(max_workers=5) as executor:
    results = list(executor.map(fetch_url, urls))
    print(results)  # [200, 200, 200]
```

### Thread Safety

```python
import threading

# Race condition example
counter = 0

def unsafe_increment():
    global counter
    for _ in range(100000):
        counter += 1  # Not atomic!

threads = [threading.Thread(target=unsafe_increment) for _ in range(10)]
for t in threads:
    t.start()
for t in threads:
    t.join()

print(f"Expected: 1000000, Got: {counter}")  # Wrong!

# Safe increment with lock
counter = 0
lock = threading.Lock()

def safe_increment():
    global counter
    for _ in range(100000):
        with lock:
            counter += 1

threads = [threading.Thread(target=safe_increment) for _ in range(10)]
for t in threads:
    t.start()
for t in threads:
    t.join()

print(f"Expected: 1000000, Got: {counter}")  # Correct!
```

---

## Multiprocessing

### Basic Multiprocessing

```python
import multiprocessing
import time

def worker(name, duration):
    print(f"Worker {name} starting (PID: {multiprocessing.current_process().pid})")
    time.sleep(duration)
    return f"Worker {name} finished"

if __name__ == "__main__":
    # Creating processes
    process1 = multiprocessing.Process(target=worker, args=("A", 2))
    process2 = multiprocessing.Process(target=worker, args=("B", 1))

    process1.start()
    process2.start()

    process1.join()
    process2.join()

    print("All processes finished")
```

### Process Pool

```python
from multiprocessing import Pool, cpu_count
import time

def heavy_computation(n):
    total = sum(i ** 2 for i in range(n))
    return total

if __name__ == "__main__":
    numbers = [10**6] * 4

    # Serial execution
    start = time.time()
    results = [heavy_computation(n) for n in numbers]
    print(f"Serial: {time.time() - start:.2f}s")

    # Parallel execution
    start = time.time()
    with Pool(processes=cpu_count()) as pool:
        results = pool.map(heavy_computation, numbers)
    print(f"Parallel: {time.time() - start:.2f}s")
```

### Inter-Process Communication

```python
from multiprocessing import Process, Queue, Pipe
import time

# Queue
def producer(queue):
    for i in range(5):
        queue.put(f"Item {i}")
        time.sleep(0.5)
    queue.put(None)

def consumer(queue):
    while True:
        item = queue.get()
        if item is None:
            break
        print(f"Consumed: {item}")

if __name__ == "__main__":
    queue = Queue()
    p1 = Process(target=producer, args=(queue,))
    p2 = Process(target=consumer, args=(queue,))

    p1.start()
    p2.start()

    p1.join()
    p2.join()

# Pipe
def sender(conn):
    conn.send({"data": [1, 2, 3]})
    conn.close()

def receiver(conn):
    data = conn.recv()
    print(f"Received: {data}")

if __name__ == "__main__":
    parent_conn, child_conn = Pipe()
    p1 = Process(target=sender, args=(parent_conn,))
    p2 = Process(target=receiver, args=(child_conn,))

    p1.start()
    p2.start()

    p1.join()
    p2.join()

# Shared Memory
from multiprocessing import Value, Array

def increment(shared_value):
    for _ in range(100):
        shared_value.value += 1

if __name__ == "__main__":
    shared_value = Value('i', 0)
    processes = [Process(target=increment, args=(shared_value,)) for _ in range(10)]

    for p in processes:
        p.start()
    for p in processes:
        p.join()

    print(f"Final value: {shared_value.value}")  # 1000
```

---

## asyncio

### Basic asyncio

```python
import asyncio

async def fetch_data(delay):
    print("Fetching data...")
    await asyncio.sleep(delay)
    return {"data": "some data"}

async def main():
    # Sequential
    result1 = await fetch_data(1)
    result2 = await fetch_data(1)
    print(f"Sequential: {result1}, {result2}")

    # Concurrent
    result1, result2 = await asyncio.gather(
        fetch_data(1),
        fetch_data(1)
    )
    print(f"Concurrent: {result1}, {result2}")

asyncio.run(main())
```

### Tasks and Gather

```python
import asyncio
import time

async def task(name, duration):
    print(f"Task {name} started")
    await asyncio.sleep(duration)
    print(f"Task {name} completed")
    return f"Result from {name}"

async def main():
    # Create tasks
    tasks = [
        asyncio.create_task(task("A", 2)),
        asyncio.create_task(task("B", 1)),
        asyncio.create_task(task("C", 3)),
    ]

    # Wait for all
    results = await asyncio.gather(*tasks)
    print(results)

    # Wait for first to complete
    done, pending = await asyncio.wait(
        [task("X", 2), task("Y", 1)],
        return_when=asyncio.FIRST_COMPLETED
    )
    for t in done:
        print(f"First done: {t.result()}")

asyncio.run(main())
```

### Timeouts

```python
import asyncio

async def slow_operation():
    await asyncio.sleep(10)
    return "Done"

async def main():
    try:
        result = await asyncio.wait_for(slow_operation(), timeout=2)
        print(result)
    except asyncio.TimeoutError:
        print("Operation timed out")

    # Alternative with shield
    task = asyncio.create_task(slow_operation())
    try:
        result = await asyncio.wait_for(
            asyncio.shield(task),
            timeout=2
        )
    except asyncio.TimeoutError:
        print("Timed out, but task continues")
        result = await task
        print(f"Got result: {result}")

asyncio.run(main())
```

### Async Context Managers and Iterators

```python
import asyncio
from typing import AsyncIterator

# Async context manager
class AsyncDatabase:
    async def __aenter__(self):
        print("Connecting...")
        await asyncio.sleep(1)
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        print("Disconnecting...")
        await asyncio.sleep(1)
        return False

    async def query(self, sql):
        await asyncio.sleep(0.5)
        return f"Results for: {sql}"

# Async iterator
class AsyncCounter:
    def __init__(self, stop):
        self.current = 0
        self.stop = stop

    def __aiter__(self):
        return self

    async def __anext__(self):
        if self.current >= self.stop:
            raise StopAsyncIteration
        await asyncio.sleep(0.1)
        self.current += 1
        return self.current

async def main():
    # Async context manager
    async with AsyncDatabase() as db:
        result = await db.query("SELECT * FROM users")
        print(result)

    # Async iterator
    async for num in AsyncCounter(5):
        print(num)

    # Async generator
    async def async_range(start, stop):
        current = start
        while current < stop:
            await asyncio.sleep(0.1)
            yield current
            current += 1

    async for num in async_range(0, 5):
        print(num)

asyncio.run(main())
```

### Async Queues

```python
import asyncio

async def producer(queue, n):
    for i in range(n):
        print(f"Producing item {i}")
        await asyncio.sleep(0.5)
        await queue.put(f"Item {i}")
    await queue.put(None)

async def consumer(queue):
    while True:
        item = await queue.get()
        if item is None:
            break
        print(f"Consuming {item}")
        await asyncio.sleep(1)
        queue.task_done()

async def main():
    queue = asyncio.Queue(maxsize=5)
    producer_task = asyncio.create_task(producer(queue, 10))
    consumer_task = asyncio.create_task(consumer(queue))
    await asyncio.gather(producer_task, consumer_task)

asyncio.run(main())
```

---

## concurrent.futures

### ThreadPoolExecutor

```python
from concurrent.futures import ThreadPoolExecutor, as_completed, wait
import requests
import time

def fetch_url(url):
    response = requests.get(url, timeout=10)
    return {"url": url, "status": response.status_code, "size": len(response.content)}

urls = [
    "https://httpbin.org/get",
    "https://httpbin.org/ip",
    "https://httpbin.org/user-agent",
    "https://httpbin.org/headers",
]

# Basic usage
with ThreadPoolExecutor(max_workers=4) as executor:
    results = list(executor.map(fetch_url, urls))
    for result in results:
        print(f"{result['url']}: {result['status']}")

# With as_completed
with ThreadPoolExecutor(max_workers=4) as executor:
    future_to_url = {executor.submit(fetch_url, url): url for url in urls}
    for future in as_completed(future_to_url):
        url = future_to_url[future]
        try:
            result = future.result()
            print(f"{url}: {result['status']}")
        except Exception as e:
            print(f"{url} failed: {e}")

# With timeout
with ThreadPoolExecutor(max_workers=4) as executor:
    future = executor.submit(fetch_url, urls[0])
    try:
        result = future.result(timeout=5)
    except TimeoutError:
        print("Request timed out")
```

### ProcessPoolExecutor

```python
from concurrent.futures import ProcessPoolExecutor
import multiprocessing
import os

def heavy_computation(n):
    pid = os.getpid()
    total = sum(i ** 2 for i in range(n))
    return {"pid": pid, "result": total}

if __name__ == "__main__":
    numbers = [10**6] * 4

    with ProcessPoolExecutor(max_workers=multiprocessing.cpu_count()) as executor:
        results = list(executor.map(heavy_computation, numbers))
        for result in results:
            print(f"PID {result['pid']}: {result['result']}")

    # With arguments
    with ProcessPoolExecutor() as executor:
        future = executor.submit(heavy_computation, 10**7)
        result = future.result()
        print(f"Result: {result['result']}")
```

### Future Objects

```python
from concurrent.futures import Future, ThreadPoolExecutor
import time

def slow_operation():
    time.sleep(2)
    return "Operation completed"

# Creating futures manually
future = Future()

def callback(future):
    print(f"Callback: {future.result()}")

future.add_done_callback(callback)

# Set result (in another thread)
import threading

def set_result():
    time.sleep(1)
    future.set_result("Result from thread")

threading.Thread(target=set_result).start()

# Wait for result
result = future.result()
print(f"Main: {result}")
```

---

## GIL (Global Interpreter Lock)

### Understanding GIL

```python
# The GIL is a mutex that protects access to Python objects
# Only one thread can execute Python bytecode at a time

# Impact:
# - CPU-bound threads don't run in parallel
# - I/O-bound threads can run concurrently (GIL released during I/O)

import threading
import multiprocessing
import time

def cpu_bound(n):
    total = 0
    for i in range(n):
        total += i * i
    return total

# Threading (GIL limits parallelism)
start = time.time()
threads = [
    threading.Thread(target=cpu_bound, args=(10**7,)) for _ in range(4)
]
for t in threads:
    t.start()
for t in threads:
    t.join()
print(f"Threading: {time.time() - start:.2f}s")

# Multiprocessing (true parallelism)
start = time.time()
processes = [
    multiprocessing.Process(target=cpu_bound, args=(10**7,)) for _ in range(4)
]
for p in processes:
    p.start()
for p in processes:
    p.join()
print(f"Multiprocessing: {time.time() - start:.2f}s")
```

### GIL and I/O

```python
import threading
import time
import requests

def fetch_url(url):
    start = time.time()
    response = requests.get(url, timeout=10)
    print(f"Fetched {url} in {time.time() - start:.2f}s")
    return response.status_code

urls = [
    "https://httpbin.org/delay/1",
    "https://httpbin.org/delay/2",
    "https://httpbin.org/delay/1",
]

# Sequential (3 seconds total)
start = time.time()
for url in urls:
    fetch_url(url)
print(f"Sequential: {time.time() - start:.2f}s")

# Concurrent with threading (2 seconds total - GIL released during I/O)
start = time.time()
threads = [threading.Thread(target=fetch_url, args=(url,)) for url in urls]
for t in threads:
    t.start()
for t in threads:
    t.join()
print(f"Threading: {time.time() - start:.2f}s")
```

### Bypassing GIL

```python
# 1. Use multiprocessing for CPU-bound tasks
# 2. Use C extensions that release the GIL
# 3. Use asyncio for I/O-bound tasks
# 4. Use concurrent.futures with ProcessPoolExecutor

# C extension example (numpy releases GIL for array operations)
import numpy as np
import time

# This runs in parallel despite GIL
a = np.random.rand(10000, 10000)
start = time.time()
result = np.dot(a, a)
print(f"NumPy: {time.time() - start:.2f}s")
```

---

## Coroutines

### Generator-based Coroutines (Legacy)

```python
# Old-style coroutines using yield
def accumulator():
    total = 0
    while True:
        value = yield total
        if value is None:
            break
        total += value

# Usage
acc = accumulator()
next(acc)  # Prime the coroutine
acc.send(10)  # 10
acc.send(20)  # 30
acc.send(30)  # 60
```

### Native Coroutines (async/await)

```python
import asyncio

# Native coroutine using async/await
async def fetch_data():
    print("Fetching data...")
    await asyncio.sleep(1)  # Simulate I/O
    return {"data": "value"}

# Coroutine function vs coroutine object
print(type(fetch_data))           # <class 'function'>
print(type(fetch_data()))         # <class 'coroutine'>

# Running coroutines
async def main():
    result = await fetch_data()
    print(result)

asyncio.run(main())

# Task creation
async def main():
    # Method 1: Direct await
    result = await fetch_data()

    # Method 2: Create task
    task = asyncio.create_task(fetch_data())
    result = await task

    # Method 3: Gather multiple
    results = await asyncio.gather(
        fetch_data(),
        fetch_data(),
        fetch_data()
    )

asyncio.run(main())
```

### Coroutine Patterns

```python
import asyncio
from typing import AsyncGenerator

# Producer-Consumer
async def producer(queue):
    for i in range(5):
        await asyncio.sleep(0.5)
        await queue.put(f"Item {i}")
        print(f"Produced: Item {i}")
    await queue.put(None)

async def consumer(queue):
    while True:
        item = await queue.get()
        if item is None:
            break
        print(f"Consumed: {item}")
        await asyncio.sleep(1)

async def main():
    queue = asyncio.Queue()
    await asyncio.gather(
        producer(queue),
        consumer(queue)
    )

asyncio.run(main())

# Async generator
async def async_range(start, stop, delay=0.1):
    current = start
    while current < stop:
        await asyncio.sleep(delay)
        yield current
        current += 1

# Async context manager
class AsyncLock:
    def __init__(self):
        self._locked = False

    async def __aenter__(self):
        while self._locked:
            await asyncio.sleep(0.1)
        self._locked = True
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        self._locked = False
        return False
```

---

## Event Loop

### Understanding the Event Loop

```python
import asyncio

# The event loop is the core of asyncio
# It schedules and runs coroutines, callbacks, and IO operations

async def main():
    print("Hello")
    await asyncio.sleep(1)
    print("World")

# Get or create event loop
loop = asyncio.get_event_loop()
loop.run_until_complete(main())

# Or use asyncio.run() (Python 3.7+)
asyncio.run(main())

# Event loop methods
async def main():
    loop = asyncio.get_running_loop()

    # Call in executor (thread/process pool)
    import functools
    loop = asyncio.get_running_loop()
    result = await loop.run_in_executor(
        None,  # Default executor
        functools.partial(time.sleep, 1)
    )

    # Schedule callback
    loop.call_soon(print, "Scheduled")
    loop.call_later(1, print, "Delayed")

    # Create task
    task = loop.create_task(some_coroutine())

asyncio.run(main())
```

### Event Loop Internals

```python
import asyncio

# Selector event loop (default on Unix)
# Proactor event loop (default on Windows)

# Custom event loop
class CustomEventLoop(asyncio.SelectorEventLoop):
    def __init__(self):
        super().__init__()
        self.custom_state = {}

    def _run_once(self):
        super()._run_once()
        # Custom processing

# Running the event loop
async def main():
    await asyncio.sleep(1)

# Method 1: asyncio.run()
asyncio.run(main())

# Method 2: Manual loop management
loop = asyncio.new_event_loop()
asyncio.set_event_loop(loop)
try:
    loop.run_until_complete(main())
finally:
    loop.close()

# Method 3: Context manager
async with asyncio.Runner() as runner:
    await main()
```

---

## Synchronization Primitives

### Locks

```python
import asyncio
import threading

# Threading Lock
lock = threading.Lock()

def safe_increment(counter):
    with lock:
        counter.value += 1

# Asyncio Lock
async def safe_operation(lock, data):
    async with lock:
        # Critical section
        await asyncio.sleep(0.1)
        data.append(1)

async def main():
    lock = asyncio.Lock()
    data = []
    tasks = [safe_operation(lock, data) for _ in range(10)]
    await asyncio.gather(*tasks)
    print(len(data))  # 10

asyncio.run(main())
```

### Events

```python
import asyncio

# Threading Event
event = threading.Event()

def waiter():
    print("Waiting for event...")
    event.wait()
    print("Event received!")

def setter():
    time.sleep(2)
    event.set()

# asyncio Event
async def waiter(event):
    print("Waiting...")
    await event.wait()
    print("Event received!")

async def setter(event):
    await asyncio.sleep(2)
    event.set()

async def main():
    event = asyncio.Event()
    await asyncio.gather(
        waiter(event),
        setter(event)
    )

asyncio.run(main())
```

### Semaphores and Conditions

```python
import asyncio

# Semaphore (limits concurrent access)
async def limited_operation(semaphore, name):
    async with semaphore:
        print(f"{name} started")
        await asyncio.sleep(1)
        print(f"{name} finished")

async def main():
    semaphore = asyncio.Semaphore(3)  # Max 3 concurrent
    tasks = [limited_operation(semaphore, f"Task-{i}") for i in range(10)]
    await asyncio.gather(*tasks)

# BoundedSemaphore
async def bounded_operation(semaphore):
    async with semaphore:
        await asyncio.sleep(1)

async def main():
    semaphore = asyncio.BoundedSemaphore(5)
    # Cannot release more than acquired
```

---

## Best Practices

### When to Use What

```python
# I/O-bound (network, file, database):
# 1. asyncio (preferred for many connections)
# 2. threading (when asyncio is not suitable)
# 3. concurrent.futures.ThreadPoolExecutor

# CPU-bound:
# 1. multiprocessing (preferred)
# 2. concurrent.futures.ProcessPoolExecutor
# 3. NumPy/SciPy (release GIL for array operations)

# Mixed (I/O + CPU):
# 1. multiprocessing for CPU + asyncio for I/O
# 2. concurrent.futures with both executors
```

### Common Patterns

```python
import asyncio
from concurrent.futures import ThreadPoolExecutor, ProcessPoolExecutor

# Pattern 1: asyncio + ThreadPoolExecutor
async def io_bound_with_threads():
    loop = asyncio.get_running_loop()
    with ThreadPoolExecutor() as pool:
        result = await loop.run_in_executor(pool, blocking_io_operation)

# Pattern 2: asyncio + ProcessPoolExecutor
async def cpu_bound_with_processes():
    loop = asyncio.get_running_loop()
    with ProcessPoolExecutor() as pool:
        result = await loop.run_in_executor(pool, cpu_intensive_operation)

# Pattern 3: Structured concurrency
async def structured_concurrency():
    async with asyncio.TaskGroup() as tg:
        task1 = tg.create_task(fetch_data())
        task2 = tg.create_task(process_data())
    # All tasks complete here

# Error handling
async def safe_concurrent():
    tasks = [asyncio.create_task(risky_operation(i)) for i in range(10)]
    done, pending = await asyncio.wait(tasks, return_when=asyncio.FIRST_EXCEPTION)
    for task in done:
        if task.exception():
            print(f"Task failed: {task.exception()}")
```

### Avoiding Common Pitfalls

```python
# 1. Don't forget to await coroutines
async def bad():
    asyncio.sleep(1)  # Missing await!

async def good():
    await asyncio.sleep(1)

# 2. Don't create too many tasks
async def bad():
    tasks = [asyncio.create_task(slow_operation(i)) for i in range(10000)]
    await asyncio.gather(*tasks)  # Memory issues!

async def good():
    semaphore = asyncio.Semaphore(100)
    async def limited_task(i):
        async with semaphore:
            await slow_operation(i)
    tasks = [limited_task(i) for i in range(10000)]
    await asyncio.gather(*tasks)

# 3. Don't block the event loop
async def bad():
    time.sleep(5)  # Blocks entire event loop!

async def good():
    await asyncio.sleep(5)  # Non-blocking

# 4. Use asyncio.run() instead of manual loop management
# Bad
loop = asyncio.get_event_loop()
loop.run_until_complete(main())
loop.close()

# Good
asyncio.run(main())
```

---

## Summary

Python concurrency options:

| Approach | Use Case | Parallelism | Complexity |
|----------|----------|-------------|------------|
| threading | I/O-bound | No (GIL) | Low |
| multiprocessing | CPU-bound | Yes | Medium |
| asyncio | I/O-bound (many) | No | Medium |
| concurrent.futures | Both | Depends | Low |

Key takeaways:
- Use **asyncio** for high-concurrency I/O operations
- Use **multiprocessing** for CPU-bound tasks
- Use **threading** for simple I/O concurrency
- Use **concurrent.futures** for simple parallel execution
- Understand the **GIL** to choose the right approach
