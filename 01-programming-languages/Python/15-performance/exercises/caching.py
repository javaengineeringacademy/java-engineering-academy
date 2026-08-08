"""
Module 15 - Performance: Caching Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Caching strategies and implementations
"""

from functools import lru_cache, cache
from collections import OrderedDict
import time


# =============================================================================
# Exercise 1: LRU Cache (⭐⭐⭐)
# =============================================================================

def exercise_1_lru_cache():
    """
    Use functools.lru_cache for memoization.
    
    TODO:
    1. Create function with @lru_cache
    2. Call function multiple times
    3. Check cache info
    """
    call_count = 0
    
    @lru_cache(maxsize=128)
    def expensive_function(n):
        nonlocal call_count
        call_count += 1
        time.sleep(0.001)  # Simulate expensive operation
        return n * n
    
    results = {}
    
    # TODO: Call function and check cache
    
    return results


# =============================================================================
# Exercise 2: Simple Cache (⭐⭐⭐⭐)
# =============================================================================

class SimpleCache:
    """
    Implement a simple cache with TTL.
    
    TODO:
    1. Store items with expiration time
    2. Check if items are expired
    3. Clean up expired items
    """
    def __init__(self, ttl=60):
        self.ttl = ttl
        self._cache = {}
    
    def get(self, key):
        # TODO: Get item if not expired
        pass
    
    def set(self, key, value):
        # TODO: Set item with expiration
        pass
    
    def cleanup(self):
        # TODO: Remove expired items
        pass


# =============================================================================
# Exercise 3: FIFO Cache (⭐⭐⭐⭐)
# =============================================================================

class FIFOCache:
    """
    Implement a FIFO cache.
    
    TODO:
    1. Maintain order of insertion
    2. Remove oldest item when full
    """
    def __init__(self, capacity):
        self.capacity = capacity
        self._cache = OrderedDict()
    
    def get(self, key):
        # TODO: Get item from cache
        pass
    
    def put(self, key, value):
        # TODO: Add item, evict if full
        pass


# =============================================================================
# Exercise 4: Memoization Decorator (⭐⭐⭐⭐)
# =============================================================================

def memoize(func):
    """
    Create a memoization decorator.
    
    TODO:
    1. Cache function results
    2. Support cache invalidation
    3. Track cache statistics
    """
    cache = {}
    stats = {'hits': 0, 'misses': 0}
    
    @wraps(func)
    def wrapper(*args):
        # TODO: Implement memoization
        pass
    
    wrapper.cache = cache
    wrapper.stats = stats
    wrapper.invalidate = lambda: cache.clear()
    
    return wrapper


# =============================================================================
# Exercise 5: Cache Strategies (⭐⭐⭐⭐⭐)
# =============================================================================

class CacheStrategy:
    """
    Implement different cache eviction strategies.
    
    TODO:
    1. LRU (Least Recently Used)
    2. LFU (Least Frequently Used)
    3. Random eviction
    """
    def __init__(self, capacity, strategy='lru'):
        self.capacity = capacity
        self.strategy = strategy
        self._cache = {}
        self._usage = {}
    
    def get(self, key):
        # TODO: Get item and update usage
        pass
    
    def put(self, key, value):
        # TODO: Add item, evict based on strategy
        pass
    
    def _evict(self):
        # TODO: Evict based on strategy
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 15 - Caching Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: LRU Cache")
    try:
        result = exercise_1_lru_cache()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Simple Cache")
    try:
        cache = SimpleCache(ttl=1)
        cache.set('key', 'value')
        result = cache.get('key')
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: FIFO Cache")
    try:
        cache = FIFOCache(capacity=3)
        cache.put('a', 1)
        cache.put('b', 2)
        cache.put('c', 3)
        result = cache.get('a')
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Memoization Decorator")
    try:
        @memoize
        def add(a, b):
            return a + b
        
        result = add(1, 2)
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Cache Strategies")
    try:
        cache = CacheStrategy(capacity=3, strategy='lru')
        cache.put('a', 1)
        cache.put('b', 2)
        result = cache.get('a')
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
