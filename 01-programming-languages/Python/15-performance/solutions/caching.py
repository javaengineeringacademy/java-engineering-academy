"""
Module 15 - Performance: Caching Solutions
Complete solutions with explanations
"""

from functools import lru_cache, wraps
from collections import OrderedDict
import time
from datetime import datetime, timedelta


# =============================================================================
# Exercise 1: LRU Cache - SOLUTION
# =============================================================================

def exercise_1_lru_cache():
    """
    Use functools.lru_cache for memoization.
    """
    call_count = 0
    
    @lru_cache(maxsize=128)
    def expensive_function(n):
        nonlocal call_count
        call_count += 1
        time.sleep(0.001)  # Simulate expensive operation
        return n * n
    
    # Call function multiple times
    results = []
    for i in [1, 2, 3, 1, 2]:  # 1 and 2 are repeated
        results.append(expensive_function(i))
    
    # Get cache info
    cache_info = expensive_function.cache_info()
    
    return {
        'results': results,
        'call_count': call_count,
        'cache_info': {
            'hits': cache_info.hits,
            'misses': cache_info.misses,
            'maxsize': cache_info.maxsize,
            'currsize': cache_info.currsize,
        }
    }


# =============================================================================
# Exercise 2: Simple Cache - SOLUTION
# =============================================================================

class SimpleCache:
    """
    Implement a simple cache with TTL.
    """
    def __init__(self, ttl=60):
        self.ttl = ttl
        self._cache = {}
    
    def get(self, key):
        """Get item if not expired."""
        if key in self._cache:
            value, expiry = self._cache[key]
            if datetime.now() < expiry:
                return value
            else:
                del self._cache[key]
        return None
    
    def set(self, key, value):
        """Set item with expiration."""
        expiry = datetime.now() + timedelta(seconds=self.ttl)
        self._cache[key] = (value, expiry)
    
    def cleanup(self):
        """Remove expired items."""
        now = datetime.now()
        expired_keys = [k for k, (_, exp) in self._cache.items() if now >= exp]
        for key in expired_keys:
            del self._cache[key]


# =============================================================================
# Exercise 3: FIFO Cache - SOLUTION
# =============================================================================

class FIFOCache:
    """
    Implement a FIFO cache.
    """
    def __init__(self, capacity):
        self.capacity = capacity
        self._cache = OrderedDict()
    
    def get(self, key):
        """Get item from cache."""
        return self._cache.get(key)
    
    def put(self, key, value):
        """Add item, evict if full."""
        if key in self._cache:
            self._cache.move_to_end(key)
        self._cache[key] = value
        
        if len(self._cache) > self.capacity:
            self._cache.popitem(last=False)  # Remove oldest


# =============================================================================
# Exercise 4: Memoization Decorator - SOLUTION
# =============================================================================

def memoize(func):
    """
    Create a memoization decorator.
    """
    cache = {}
    stats = {'hits': 0, 'misses': 0}
    
    @wraps(func)
    def wrapper(*args):
        if args in cache:
            stats['hits'] += 1
            return cache[args]
        
        stats['misses'] += 1
        result = func(*args)
        cache[args] = result
        return result
    
    wrapper.cache = cache
    wrapper.stats = stats
    wrapper.invalidate = lambda: cache.clear()
    
    return wrapper


# =============================================================================
# Exercise 5: Cache Strategies - SOLUTION
# =============================================================================

class CacheStrategy:
    """
    Implement different cache eviction strategies.
    """
    def __init__(self, capacity, strategy='lru'):
        self.capacity = capacity
        self.strategy = strategy
        self._cache = {}
        self._usage = {}  # For LRU/LFU
        self._order = []  # For LRU
        self._frequency = {}  # For LFU
    
    def get(self, key):
        """Get item and update usage."""
        if key in self._cache:
            if self.strategy == 'lru':
                self._order.remove(key)
                self._order.append(key)
            elif self.strategy == 'lfu':
                self._frequency[key] = self._frequency.get(key, 0) + 1
            return self._cache[key]
        return None
    
    def put(self, key, value):
        """Add item, evict based on strategy."""
        if key in self._cache:
            self._cache[key] = value
            if self.strategy == 'lru':
                self._order.remove(key)
                self._order.append(key)
            elif self.strategy == 'lfu':
                self._frequency[key] = self._frequency.get(key, 0) + 1
            return
        
        if len(self._cache) >= self.capacity:
            self._evict()
        
        self._cache[key] = value
        if self.strategy == 'lru':
            self._order.append(key)
        elif self.strategy == 'lfu':
            self._frequency[key] = 1
    
    def _evict(self):
        """Evict based on strategy."""
        if self.strategy == 'lru':
            oldest = self._order.pop(0)
            del self._cache[oldest]
        elif self.strategy == 'lfu':
            least_frequent = min(self._frequency, key=self._frequency.get)
            del self._cache[least_frequent]
            del self._frequency[least_frequent]
        elif self.strategy == 'random':
            import random
            key = random.choice(list(self._cache.keys()))
            del self._cache[key]


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 15 - Caching Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: LRU Cache")
    result = exercise_1_lru_cache()
    assert result['call_count'] == 3  # Only 3 unique calls
    assert result['cache_info']['hits'] == 2  # 2 cache hits
    print(f"  Cache info: {result['cache_info']}")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Simple Cache")
    cache = SimpleCache(ttl=1)
    cache.set('key', 'value')
    assert cache.get('key') == 'value'
    time.sleep(1.1)  # Wait for expiration
    assert cache.get('key') is None
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: FIFO Cache")
    cache = FIFOCache(capacity=3)
    cache.put('a', 1)
    cache.put('b', 2)
    cache.put('c', 3)
    cache.put('d', 4)  # Should evict 'a'
    assert cache.get('a') is None
    assert cache.get('b') == 2
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Memoization Decorator")
    @memoize
    def add(a, b):
        return a + b
    
    assert add(1, 2) == 3
    assert add(1, 2) == 3  # Cache hit
    assert add.stats['hits'] == 1
    assert add.stats['misses'] == 1
    print(f"  Stats: {add.stats}")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Cache Strategies")
    cache = CacheStrategy(capacity=3, strategy='lru')
    cache.put('a', 1)
    cache.put('b', 2)
    cache.put('c', 3)
    cache.get('a')  # Make 'a' recently used
    cache.put('d', 4)  # Should evict 'b' (least recently used)
    assert cache.get('b') is None
    assert cache.get('a') == 1
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
