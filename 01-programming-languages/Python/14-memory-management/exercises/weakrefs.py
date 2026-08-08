"""
Module 14 - Memory Management: Weak References Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Weak references and their uses
"""

import weakref


# =============================================================================
# Exercise 1: Basic Weak Reference (⭐⭐⭐)
# =============================================================================

def exercise_1_basic_weakref():
    """
    Create and use basic weak references.
    
    TODO:
    1. Create an object
    2. Create weak reference to it
    3. Check if reference is alive
    4. Delete object and check again
    """
    class Data:
        def __init__(self, value):
            self.value = value
    
    result = {}
    
    # TODO: Create weak reference and test lifecycle
    
    return result


# =============================================================================
# Exercise 2: Weak Value Dictionary (⭐⭐⭐⭐)
# =============================================================================

def exercise_2_weak_value_dict():
    """
    Use WeakValueDictionary for caching.
    
    TODO:
    1. Create WeakValueDictionary
    2. Add objects
    3. Delete strong references
    4. Observe automatic cleanup
    """
    cache = weakref.WeakValueDictionary()
    
    # TODO: Test WeakValueDictionary behavior
    
    return cache


# =============================================================================
# Exercise 3: Weak Set (⭐⭐⭐)
# =============================================================================

def exercise_3_weak_set():
    """
    Use WeakSet for tracking objects.
    
    TODO:
    1. Create WeakSet
    2. Add objects
    3. Delete objects
    4. Observe automatic removal
    """
    tracked = weakref.WeakSet()
    
    # TODO: Test WeakSet behavior
    
    return tracked


# =============================================================================
# Exercise 4: Callback on Deletion (⭐⭐⭐⭐)
# =============================================================================

def exercise_4_weakref_callback():
    """
    Use callbacks when weak references are invalidated.
    
    TODO:
    1. Create weak reference with callback
    2. Delete object
    3. Capture callback invocation
    """
    deleted = []
    
    def on_delete(ref):
        deleted.append("deleted")
    
    # TODO: Test callback behavior
    
    return deleted


# =============================================================================
# Exercise 5: Weak Reference Cache (⭐⭐⭐⭐⭐)
# =============================================================================

class WeakRefCache:
    """
    Implement a cache using weak references.
    
    TODO:
    1. Store objects with weak references
    2. Auto-cleanup when objects are deleted
    3. Support hit/miss tracking
    """
    def __init__(self):
        self._cache = weakref.WeakValueDictionary()
        self._hits = 0
        self._misses = 0
    
    def get(self, key):
        # TODO: Get from cache
        pass
    
    def set(self, key, value):
        # TODO: Set in cache
        pass
    
    def stats(self):
        # TODO: Return cache statistics
        pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 14 - Weak References Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Weak Reference")
    try:
        result = exercise_1_basic_weakref()
        assert isinstance(result, dict)
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Weak Value Dictionary")
    try:
        cache = exercise_2_weak_value_dict()
        assert isinstance(cache, weakref.WeakValueDictionary)
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Weak Set")
    try:
        tracked = exercise_3_weak_set()
        assert isinstance(tracked, weakref.WeakSet)
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Callback on Deletion")
    try:
        result = exercise_4_weakref_callback()
        assert isinstance(result, list)
        print(f"  Deleted: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Weak Reference Cache")
    try:
        cache = WeakRefCache()
        assert hasattr(cache, '_cache')
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
