"""async/await, asyncio.gather, and async patterns."""

import asyncio
import time

# ── Basic async/await ────────────────────────────────────────────────
async def say_hello():
    print("Hello")
    await asyncio.sleep(1)  # Non-blocking sleep
    print("World")

# Run the coroutine
asyncio.run(say_hello())

# ── Multiple Coroutines ──────────────────────────────────────────────
async def fetch_data(name, delay):
    print(f"Fetching {name}...")
    await asyncio.sleep(delay)
    print(f"Got {name}")
    return f"Data from {name}"

async def main():
    # Sequential — slow
    start = time.time()
    result1 = await fetch_data("API1", 2)
    result2 = await fetch_data("API2", 2)
    print(f"Sequential: {time.time() - start:.1f}s")

    # Parallel — fast
    start = time.time()
    result1, result2 = await asyncio.gather(
        fetch_data("API1", 2),
        fetch_data("API2", 2)
    )
    print(f"Parallel: {time.time() - start:.1f}s")

asyncio.run(main())

# ── Tasks and Cancellation ──────────────────────────────────────────
async def long_running():
    try:
        await asyncio.sleep(10)
        return "Completed"
    except asyncio.CancelledError:
        print("Task cancelled")
        raise

async def main():
    task = asyncio.create_task(long_running())
    await asyncio.sleep(1)
    task.cancel()  # Cancel after 1 second

    try:
        await task
    except asyncio.CancelledError:
        print("Task was cancelled")

asyncio.run(main())

# ── Async Iterators ──────────────────────────────────────────────────
class AsyncCounter:
    def __init__(self, stop):
        self.stop = stop
        self.current = 0

    def __aiter__(self):
        return self

    async def __anext__(self):
        if self.current >= self.stop:
            raise StopAsyncIteration
        await asyncio.sleep(0.1)
        self.current += 1
        return self.current

async def main():
    async for num in AsyncCounter(5):
        print(num, end=" ")

asyncio.run(main())

# ── Async Context Manager ───────────────────────────────────────────
class AsyncResource:
    async def __aenter__(self):
        print("Acquiring resource")
        await asyncio.sleep(0.1)
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        print("Releasing resource")
        await asyncio.sleep(0.1)

async def main():
    async with AsyncResource() as r:
        print("Using resource")

asyncio.run(main())

# ── Semaphores — Limit Concurrency ──────────────────────────────────
semaphore = asyncio.Semaphore(5)  # Max 5 concurrent

async def limited_fetch(name):
    async with semaphore:
        print(f"Starting {name}")
        await asyncio.sleep(1)
        print(f"Finished {name}")

async def main():
    tasks = [limited_fetch(f"Task {i}") for i in range(20)]
    await asyncio.gather(*tasks)

asyncio.run(main())

# ── Async Generators ────────────────────────────────────────────────
async def async_range(n):
    for i in range(n):
        await asyncio.sleep(0.1)
        yield i

async def main():
    async for num in async_range(5):
        print(num, end=" ")

asyncio.run(main())

# ── Practical: Async HTTP (conceptual) ──────────────────────────────
import aiohttp  # pip install aiohttp

async def fetch_url(session, url):
    async with session.get(url) as response:
        return await response.text()

async def fetch_all(urls):
    async with aiohttp.ClientSession() as session:
        tasks = [fetch_url(session, url) for url in urls]
        return await asyncio.gather(*tasks)

# ── Thread Integration ──────────────────────────────────────────────
import concurrent.futures

def blocking_io():
    time.sleep(1)
    return "IO result"

async def main():
    loop = asyncio.get_event_loop()
    with concurrent.futures.ThreadPoolExecutor() as pool:
        result = await loop.run_in_executor(pool, blocking_io)
        print(result)

asyncio.run(main())
