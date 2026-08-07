"""
Module 15: Performance - Optimization Solutions
Practice code optimization techniques in Python.
"""

import time
from functools import lru_cache
from typing import List, Dict
import sys


def optimize_list_comprehension(data):
    """Optimize list operations using comprehensions."""
    # Slow way
    result_slow = []
    for x in data:
        if x % 2 == 0:
            result_slow.append(x * 2)

    # Fast way with list comprehension
    result_fast = [x * 2 for x in data if x % 2 == 0]

    return result_fast


@lru_cache(maxsize=128)
def fibonacci_cached(n):
    """Cached Fibonacci calculation."""
    if n < 2:
        return n
    return fibonacci_cached(n - 1) + fibonacci_cached(n - 2)


def fibonacci_uncached(n):
    """Uncached Fibonacci calculation."""
    if n < 2:
        return n
    return fibonacci_uncached(n - 1) + fibonacci_uncached(n - 2)


def optimize_dict_operations(data):
    """Optimize dictionary operations."""
    # Use set for O(1) lookups instead of list for O(n)
    fast_lookup = set(data)

    # Use dict comprehension
    result = {x: x * 2 for x in data}

    return result


def optimize_string_concatenation(parts):
    """Optimize string concatenation."""
    # Slow way: repeated concatenation
    result_slow = ""
    for part in parts:
        result_slow += part

    # Fast way: join
    result_fast = "".join(parts)

    return result_fast


def optimize_generators(data):
    """Use generators for memory efficiency."""
    # Generator expression (memory efficient)
    gen = (x * 2 for x in data if x % 2 == 0)

    # List comprehension (loads all into memory)
    lst = [x * 2 for x in data if x % 2 == 0]

    return list(gen)


class OptimizedCache:
    """LRU cache implementation."""

    def __init__(self, maxsize=128):
        self.maxsize = maxsize
        self.cache = {}
        self.order = []

    def get(self, key):
        """Get value from cache."""
        if key in self.cache:
            # Move to end (most recently used)
            self.order.remove(key)
            self.order.append(key)
            return self.cache[key]
        return None

    def put(self, key, value):
        """Put value in cache."""
        if key in self.cache:
            # Update existing
            self.order.remove(key)
        elif len(self.order) >= self.maxsize:
            # Remove oldest
            oldest = self.order.pop(0)
            del self.cache[oldest]

        self.cache[key] = value
        self.order.append(key)

    def size(self):
        """Get cache size."""
        return len(self.cache)


def benchmark(func, *args, iterations=1000):
    """Benchmark a function."""
    start = time.time()
    for _ in range(iterations):
        func(*args)
    end = time.time()
    return (end - start) / iterations


if __name__ == "__main__":
    print("Testing Optimization Solutions...")

    # Test list optimization
    data = list(range(10000))
    result = optimize_list_comprehension(data)
    assert all(x % 2 == 0 for x in result)
    print("✓ Exercise 1 passed: list optimization works")

    # Test cached vs uncached
    start = time.time()
    fibonacci_uncached(30)
    uncached_time = time.time() - start

    start = time.time()
    fibonacci_cached(30)
    cached_time = time.time() - start

    assert cached_time < uncached_time
    print(f"✓ Exercise 2 passed: cached={cached_time:.4f}s, uncached={uncached_time:.4f}s")

    # Test dict optimization
    data = list(range(1000))
    result = optimize_dict_operations(data)
    assert len(result) == 1000
    print("✓ Exercise 3 passed: dict optimization works")

    # Test string optimization
    parts = ["hello", " ", "world", "!"] * 1000
    result = optimize_string_concatenation(parts)
    assert result == ("hello world!") * 1000
    print("✓ Exercise 4 passed: string optimization works")

    # Test generator optimization
    data = list(range(10000))
    result = optimize_generators(data)
    assert len(result) == 5000
    print("✓ Exercise 5 passed: generator optimization works")

    # Test custom cache
    cache = OptimizedCache(maxsize=3)
    cache.put("a", 1)
    cache.put("b", 2)
    cache.put("c", 3)

    assert cache.get("a") == 1
    assert cache.size() == 3

    cache.put("d", 4)  # Should evict "b" (oldest unused)
    assert cache.size() == 3
    assert cache.get("b") is None
    print("✓ Exercise 6 passed: custom cache works")

    # Benchmark
    time_per_op = benchmark(lambda: sum(range(1000)))
    assert time_per_op < 0.001  # Should be fast
    print(f"✓ Benchmark: {time_per_op*1000:.4f}ms per operation")

    print("All Optimization solutions passed!")
