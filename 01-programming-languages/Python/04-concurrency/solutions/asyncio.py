"""
Module 04 - Concurrency: Asyncio Solutions
Difficulty: Intermediate to Advanced
"""

import asyncio

# =============================================================================
# Exercise 1: Basic Async/Await - Solution
# =============================================================================
async def fetch_data(url, delay):
    """Simulate fetching data from URL."""
    print(f"Fetching {url}...")
    await asyncio.sleep(delay)
    return f"Data from {url}"

async def main():
    """Run multiple fetch operations concurrently."""
    urls = [
        ("http://api.example.com/users", 1),
        ("http://api.example.com/posts", 2),
        ("http://api.example.com/comments", 1.5)
    ]
    tasks = [fetch_data(url, delay) for url, delay in urls]
    results = await asyncio.gather(*tasks)
    for result in results:
        print(result)

asyncio.run(main())


# =============================================================================
# Exercise 2: Async Context Managers - Solution
# =============================================================================
class AsyncDatabase:
    """Async database connection."""

    def __init__(self, connection_string):
        self.connection_string = connection_string
        self.connection = None

    async def __aenter__(self):
        print(f"Connecting to {self.connection_string}")
        await asyncio.sleep(0.1)
        self.connection = {"connected": True}
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        print("Closing connection")
        self.connection = None
        return False

    async def execute(self, query):
        await asyncio.sleep(0.05)
        return f"Result of: {query}"

async def query_database():
    """Use async database context manager."""
    async with AsyncDatabase("sqlite:///:memory:") as db:
        result = await db.execute("SELECT * FROM users")
        print(result)

asyncio.run(query_database())


# =============================================================================
# Exercise 3: Async Generators - Solution
# =============================================================================
async def async_range(start, stop, delay=0.1):
    """Async generator that yields numbers with delay."""
    for i in range(start, stop):
        await asyncio.sleep(delay)
        yield i

async def process_async_generator():
    """Process items from async generator."""
    async for num in async_range(0, 5):
        print(f"Got: {num}")

asyncio.run(process_async_generator())


# =============================================================================
# Exercise 4: Task Management - Solution
# =============================================================================
async def fetch_with_timeout(url, timeout):
    """Fetch with timeout."""
    try:
        async with asyncio.timeout(timeout):
            await asyncio.sleep(1)
            return f"Data from {url}"
    except asyncio.TimeoutError:
        return f"Timeout fetching {url}"

async def long_running_task():
    """Long running task that can be cancelled."""
    try:
        for i in range(100):
            await asyncio.sleep(0.1)
            print(f"Task progress: {i}")
    except asyncio.CancelledError:
        print("Task was cancelled")
        raise

async def run_with_cancellation():
    """Run task and cancel after delay."""
    task = asyncio.create_task(long_running_task())
    await asyncio.sleep(0.5)
    task.cancel()
    try:
        await task
    except asyncio.CancelledError:
        pass
    print("Task completed")

asyncio.run(run_with_cancellation())


# =============================================================================
# Exercise 5: Async Patterns - Solution
# =============================================================================
class AsyncSemaphore:
    """Async semaphore for rate limiting."""

    def __init__(self, max_concurrent):
        self.max_concurrent = max_concurrent
        self.current = 0
        self._lock = asyncio.Lock()

    async def acquire(self):
        async with self._lock:
            while self.current >= self.max_concurrent:
                await asyncio.sleep(0.01)
            self.current += 1

    def release(self):
        self.current -= 1

class AsyncQueue:
    """Async queue for producer-consumer."""

    def __init__(self, maxsize=0):
        self.queue = asyncio.Queue(maxsize=maxsize)

    async def put(self, item):
        await self.queue.put(item)

    async def get(self):
        return await self.queue.get()

async def main():
    sem = AsyncSemaphore(3)
    async def worker(n):
        await sem.acquire()
        print(f"Worker {n} started")
        await asyncio.sleep(1)
        sem.release()
        print(f"Worker {n} finished")

    tasks = [worker(i) for i in range(6)]
    await asyncio.gather(*tasks)

asyncio.run(main())
