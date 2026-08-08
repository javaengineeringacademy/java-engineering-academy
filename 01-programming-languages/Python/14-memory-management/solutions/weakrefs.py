"""
Module 14 - Memory Management: Weak References Solutions
Complete solutions with explanations
"""

import weakref


# =============================================================================
# Exercise 1: Basic Weak Reference - SOLUTION
# =============================================================================

def exercise_1_basic_weakref():
    """
    Create and use basic weak references.
    """
    class Data:
        def __init__(self, value):
            self.value = value
    
    # Create object
    obj = Data(42)
    
    # Create weak reference
    weak_ref = weakref.ref(obj)
    
    result = {
        'alive_before_delete': weak_ref() is not None,
        'value_before_delete': weak_ref().value if weak_ref() else None,
    }
    
    # Delete object
    del obj
    
    result['alive_after_delete'] = weak_ref() is not None
    
    return result


# =============================================================================
# Exercise 2: Weak Value Dictionary - SOLUTION
# =============================================================================

def exercise_2_weak_value_dict():
    """
    Use WeakValueDictionary for caching.
    """
    cache = weakref.WeakValueDictionary()
    
    class Data:
        def __init__(self, value):
            self.value = value
    
    # Add objects
    obj1 = Data(1)
    obj2 = Data(2)
    cache['obj1'] = obj1
    cache['obj2'] = obj2
    
    # Objects are in cache
    assert 'obj1' in cache
    assert 'obj2' in cache
    
    # Delete strong reference
    del obj1
    
    # Object automatically removed from cache
    assert 'obj1' not in cache
    assert 'obj2' in cache
    
    return cache


# =============================================================================
# Exercise 3: Weak Set - SOLUTION
# =============================================================================

def exercise_3_weak_set():
    """
    Use WeakSet for tracking objects.
    """
    tracked = weakref.WeakSet()
    
    class Data:
        def __init__(self, value):
            self.value = value
    
    # Add objects
    obj1 = Data(1)
    obj2 = Data(2)
    tracked.add(obj1)
    tracked.add(obj2)
    
    # Objects are in set
    assert len(tracked) == 2
    
    # Delete strong reference
    del obj1
    
    # Object automatically removed from set
    assert len(tracked) == 1
    
    return tracked


# =============================================================================
# Exercise 4: Callback on Deletion - SOLUTION
# =============================================================================

def exercise_4_weakref_callback():
    """
    Use callbacks when weak references are invalidated.
    """
    deleted = []
    
    def on_delete(ref):
        deleted.append("deleted")
    
    class Data:
        def __init__(self, value):
            self.value = value
    
    # Create object and weak reference with callback
    obj = Data(42)
    weak_ref = weakref.ref(obj, on_delete)
    
    # Delete object
    del obj
    
    # Callback was invoked
    assert len(deleted) == 1
    assert deleted[0] == "deleted"
    
    return deleted


# =============================================================================
# Exercise 5: Weak Reference Cache - SOLUTION
# =============================================================================

class WeakRefCache:
    """
    Implement a cache using weak references.
    """
    def __init__(self):
        self._cache = weakref.WeakValueDictionary()
        self._hits = 0
        self._misses = 0
    
    def get(self, key):
        """Get from cache."""
        if key in self._cache:
            self._hits += 1
            return self._cache[key]
        self._misses += 1
        return None
    
    def set(self, key, value):
        """Set in cache."""
        self._cache[key] = value
    
    def stats(self):
        """Return cache statistics."""
        return {
            'hits': self._hits,
            'misses': self._misses,
            'hit_rate': self._hits / (self._hits + self._misses) if (self._hits + self._misses) > 0 else 0
        }


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 14 - Weak References Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Weak Reference")
    result = exercise_1_basic_weakref()
    assert isinstance(result, dict)
    assert result['alive_before_delete'] == True
    assert result['alive_after_delete'] == False
    print(f"  Result: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Weak Value Dictionary")
    cache = exercise_2_weak_value_dict()
    assert isinstance(cache, weakref.WeakValueDictionary)
    assert len(cache) == 1
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Weak Set")
    tracked = exercise_3_weak_set()
    assert isinstance(tracked, weakref.WeakSet)
    assert len(tracked) == 1
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Callback on Deletion")
    result = exercise_4_weakref_callback()
    assert isinstance(result, list)
    assert len(result) == 1
    print(f"  Deleted: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Weak Reference Cache")
    cache = WeakRefCache()
    
    # Create test objects
    class Data:
        def __init__(self, value):
            self.value = value
    
    obj1 = Data(1)
    cache.set('key1', obj1)
    
    # Hit
    result = cache.get('key1')
    assert result is not None
    
    # Miss
    result = cache.get('nonexistent')
    assert result is None
    
    stats = cache.stats()
    assert stats['hits'] == 1
    assert stats['misses'] == 1
    print(f"  Cache stats: {stats}")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
