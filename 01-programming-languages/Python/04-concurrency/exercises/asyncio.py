"""
Python Concurrency - Asyncio Exercises
Complete each exercise by implementing the required coroutine.
Run the test cases to verify your solution.
"""

import asyncio
import time
from collections import deque


# Exercise 1: Async Hello World (Easy)
# Basic async/await

async def async_hello(name, delay=0.1):
    """
    Async function that returns greeting after delay.
    
    Args:
        name: Name to greet
        delay: Delay in seconds
    
    Returns:
        Greeting string
    """
    # TODO: Implement this async function using await
    pass


async def run_concurrent_hellos(names):
    """
    Run multiple hellos concurrently.
    
    Args:
        names: List of names
    
    Returns:
        List of greetings
    """
    # TODO: Implement this function using asyncio.gather
    pass


# Exercise 2: Async HTTP Fetcher (Medium)
# Concurrent URL fetching (simulated)

async def async_fetch(url, delay=0.1):
    """
    Simulate fetching a URL.
    
    Args:
        url: URL to fetch
        delay: Simulated network delay
    
    Returns:
        Dict with url and content
    """
    # TODO: Implement this async function
    # Simulate network delay with asyncio.sleep
    pass


async def fetch_all_urls(urls, max_concurrent=5):
    """
    Fetch multiple URLs concurrently with limit.
    
    Args:
        urls: List of URLs
        max_concurrent: Maximum concurrent requests
    
    Returns:
        List of results
    """
    # TODO: Implement this function using asyncio.Semaphore
    pass


# Exercise 3: Async Task Scheduler (Medium)
# Schedule async tasks

class AsyncScheduler:
    """
    Scheduler for async tasks with delay.
    
    Requirements:
    - Schedule tasks with delay
    - Cancel scheduled tasks
    - Track running tasks
    """
    
    def __init__(self):
        # TODO: Implement this method
        pass
    
    async def schedule(self, func, delay, *args, **kwargs):
        """
        Schedule a function to run after delay.
        
        Returns:
            Task object
        """
        # TODO: Implement this method
        pass
    
    async def schedule_repeating(self, func, interval, *args, **kwargs):
        """
        Schedule a function to run repeatedly.
        
        Args:
            func: Async function
            interval: Seconds between runs
        
        Returns:
            Task object
        """
        # TODO: Implement this method
        pass
    
    def cancel(self, task):
        """Cancel a scheduled task."""
        # TODO: Implement this method
        pass
    
    async def wait_all(self):
        """Wait for all scheduled tasks to complete."""
        # TODO: Implement this method
        pass


# Exercise 4: Async Producer-Consumer (Hard)
# Async queue implementation

class AsyncQueue:
    """
    Async producer-consumer queue.
    
    Requirements:
    - Async put and get
    - Support for max size
    - Graceful shutdown
    """
    
    def __init__(self, maxsize=0):
        # TODO: Implement this method
        pass
    
    async def put(self, item):
        """Add item to queue. Wait if full."""
        # TODO: Implement this method
        pass
    
    async def get(self):
        """Get item from queue. Wait if empty."""
        # TODO: Implement this method
        pass
    
    def task_done(self):
        """Signal that a task is complete."""
        # TODO: Implement this method
        pass
    
    async def join(self):
        """Wait for all items to be processed."""
        # TODO: Implement this method
        pass
    
    def put_nowait(self, item):
        """Add item without waiting. Raise QueueFull if full."""
        # TODO: Implement this method
        pass
    
    def get_nowait(self):
        """Get item without waiting. Raise QueueEmpty if empty."""
        # TODO: Implement this method
        pass


# Exercise 5: Async Rate Limiter (Hard)
# Async rate limiting

class AsyncRateLimiter:
    """
    Rate limiter for async functions.
    
    Requirements:
    - Limit calls per time window
    - Wait when limit reached
    - Support burst
    """
    
    def __init__(self, rate, period):
        """
        Args:
            rate: Maximum calls allowed
            period: Time period in seconds
        """
        # TODO: Implement this method
        pass
    
    async def acquire(self):
        """Acquire permission to call. May wait."""
        # TODO: Implement this method
        pass
    
    def release(self):
        """Release permission (optional, for sliding window)."""
        # TODO: Implement this method
        pass
    
    async def __aenter__(self):
        """Async context manager entry."""
        # TODO: Implement this method
        pass
    
    async def __aexit__(self, exc_type, exc_val, exc_tb):
        """Async context manager exit."""
        # TODO: Implement this method
        pass


# ==================== TEST CASES ====================

async def test_exercises():
    print("Testing Exercise 1: Async Hello World")
    result = await async_hello("World")
    assert result == "Hello, World!"
    
    results = await run_concurrent_hellos(["Alice", "Bob", "Charlie"])
    assert len(results) == 3
    assert "Alice" in results[0]
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 2: Async Fetcher")
    result = await async_fetch("http://example.com")
    assert result["url"] == "http://example.com"
    assert "content" in result
    
    urls = [f"http://example.com/{i}" for i in range(5)]
    results = await fetch_all_urls(urls, max_concurrent=2)
    assert len(results) == 5
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 3: Async Scheduler")
    scheduler = AsyncScheduler()
    results = []
    
    async def append_result(value):
        results.append(value)
    
    await scheduler.schedule(append_result, 0.01, "task1")
    await scheduler.schedule(append_result, 0.02, "task2")
    await asyncio.sleep(0.05)
    assert "task1" in results
    assert "task2" in results
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 4: Async Queue")
    queue = AsyncQueue(maxsize=3)
    items = []
    
    async def producer():
        for i in range(5):
            await queue.put(i)
    
    async def consumer():
        for _ in range(5):
            item = await queue.get()
            items.append(item)
            queue.task_done()
    
    await asyncio.gather(producer(), consumer())
    assert sorted(items) == [0, 1, 2, 3, 4]
    print("  ✓ All tests passed!\n")

    print("Testing Exercise 5: Async Rate Limiter")
    limiter = AsyncRateLimiter(rate=3, period=1)
    call_times = []
    
    async def rate_limited_call():
        async with limiter:
            call_times.append(time.time())
    
    start = time.time()
    await asyncio.gather(*[rate_limited_call() for _ in range(5)])
    
    # First 3 should be immediate, next 2 should wait
    assert len(call_times) == 5
    print("  ✓ All tests passed!\n")

    print("All asyncio exercises passed!")


if __name__ == "__main__":
    asyncio.run(test_exercises())
