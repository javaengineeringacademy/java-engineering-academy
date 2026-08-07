"""
Caching in Python
Demonstrates various caching techniques
"""

import time
from functools import lru_cache, cache
from typing import Dict, Any

# ============================================
# Manual Caching
# ============================================

def manual_caching() -> None:
    """Demonstrate manual caching."""
    print("=== Manual Caching ===")
    
    cache = {}
    
    def fibonacci(n: int) -> int:
        """Calculate Fibonacci with manual cache."""
        if n in cache:
            return cache[n]
        
        if n <= 1:
            return n
        
        result = fibonacci(n - 1) + fibonacci(n - 2)
        cache[n] = result
        return result
    
    # Time without cache optimization
    start = time.time()
    result = fibonacci(30)
    manual_time = time.time() - start
    
    print(f"  Fibonacci(30) = {result}")
    print(f"  Time: {manual_time:.6f}s")

# ============================================
# lru_cache
# ============================================

def lru_cache_example() -> None:
    """Demonstrate lru_cache."""
    print("\n=== lru_cache ===")
    
    @lru_cache(maxsize=128)
    def fibonacci_lru(n: int) -> int:
        """Calculate Fibonacci with LRU cache."""
        if n <= 1:
            return n
        return fibonacci_lru(n - 1) + fibonacci_lru(n - 2)
    
    # Time with LRU cache
    start = time.time()
    result = fibonacci_lru(30)
    lru_time = time.time() - start
    
    print(f"  Fibonacci(30) = {result}")
    print(f"  Time: {lru_time:.6f}s")
    
    # Cache info
    print(f"  Cache info: {fibonacci_lru.cache_info()}")

# ============================================
# cache (Python 3.9+)
# ============================================

def cache_example() -> None:
    """Demonstrate cache (unlimited)."""
    print("\n=== cache (unlimited) ===")
    
    @cache
    def fibonacci_unlimited(n: int) -> int:
        """Calculate Fibonacci with unlimited cache."""
        if n <= 1:
            return n
        return fibonacci_unlimited(n - 1) + fibonacci_unlimited(n - 2)
    
    # Time with unlimited cache
    start = time.time()
    result = fibonacci_unlimited(30)
    cache_time = time.time() - start
    
    print(f"  Fibonacci(30) = {result}")
    print(f"  Time: {cache_time:.6f}s")

# ============================================
# Dictionary Cache
# ============================================

def dict_cache() -> None:
    """Demonstrate dictionary-based cache."""
    print("\n=== Dictionary Cache ===")
    
    class DictCache:
        def __init__(self, max_size: int = 100) -> None:
            self.cache = {}
            self.max_size = max_size
        
        def get(self, key: Any) -> Any:
            """Get from cache."""
            return self.cache.get(key)
        
        def set(self, key: Any, value: Any) -> None:
            """Set in cache."""
            if len(self.cache) >= self.max_size:
                # Remove oldest item
                oldest_key = next(iter(self.cache))
                del self.cache[oldest_key]
            self.cache[key] = value
        
        def clear(self) -> None:
            """Clear cache."""
            self.cache.clear()
    
    # Usage
    cache = DictCache(max_size=3)
    
    cache.set("a", 1)
    cache.set("b", 2)
    cache.set("c", 3)
    print(f"  Cache: {cache.cache}")
    
    cache.set("d", 4)  # Should remove "a"
    print(f"  After adding 'd': {cache.cache}")

# ============================================
# Time-based Cache
# ============================================

def time_cache() -> None:
    """Demonstrate time-based cache."""
    print("\n=== Time-based Cache ===")
    
    class TimeCache:
        def __init__(self, ttl: float = 60.0) -> None:
            self.cache = {}
            self.ttl = ttl
        
        def get(self, key: Any) -> Any:
            """Get from cache if not expired."""
            if key in self.cache:
                value, timestamp = self.cache[key]
                if time.time() - timestamp < self.ttl:
                    return value
                else:
                    del self.cache[key]
            return None
        
        def set(self, key: Any, value: Any) -> None:
            """Set in cache with timestamp."""
            self.cache[key] = (value, time.time())
    
    # Usage
    cache = TimeCache(ttl=1.0)  # 1 second TTL
    
    cache.set("key1", "value1")
    print(f"  Get immediately: {cache.get('key1')}")
    
    time.sleep(0.5)
    print(f"  Get after 0.5s: {cache.get('key1')}")
    
    time.sleep(0.6)
    print(f"  Get after 1.1s: {cache.get('key1')}")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    manual_caching()
    lru_cache_example()
    cache_example()
    dict_cache()
    time_cache()
