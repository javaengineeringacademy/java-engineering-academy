"""
Module 14: Memory Management - Weak References Exercises
========================================================
Practice using weak references in Python.
"""

import weakref
import gc

# =============================================================================
# Exercise 1: Basic Weak Reference (★☆☆☆☆)
# =============================================================================
# TODO: Create weak reference to object

class ManagedObject:
    """Object that can be weakly referenced."""
    pass

# Test Cases
def test_basic_weakref():
    obj = ManagedObject()
    ref = weakref.ref(obj)
    
    assert ref() is obj
    assert ref is not None
    
    del obj
    gc.collect()
    assert ref() is None
    print("✓ Exercise 1 passed: weak reference tracks object lifecycle")

# =============================================================================
# Exercise 2: Weak Value Dictionary (★★☆☆☆)
# =============================================================================
# TODO: Use WeakValueDictionary for cache

class WeakCache:
    """Cache using WeakValueDictionary."""
    # TODO: Store values weakly
    # TODO: Return None for expired entries
    pass

# Test Cases
def test_weak_cache():
    cache = WeakCache()
    
    obj1 = ManagedObject()
    cache.set("key1", obj1)
    assert cache.get("key1") is obj1
    
    del obj1
    gc.collect()
    assert cache.get("key1") is None
    print("✓ Exercise 2 passed: weak cache entries expire")

# =============================================================================
# Exercise 3: Reference Callbacks (★★★☆☆)
# =============================================================================
# TODO: Use callbacks to track object destruction

class DestructionTracker:
    """Track when objects are destroyed."""
    # TODO: Register weak references with callbacks
    pass

# Test Cases
def test_destruction_tracker():
    tracker = DestructionTracker()
    destroyed = []
    
    obj = ManagedObject()
    tracker.track(obj, lambda ref: destroyed.append(ref))
    
    del obj
    gc.collect()
    
    assert len(destroyed) == 1
    print(f"✓ Exercise 3 passed: tracked {len(destroyed)} destructions")

# =============================================================================
# Exercise 4: WeakSet for Parent References (★★★★☆)
# =============================================================================
# TODO: Implement child objects with weak parent references

class Parent:
    """Parent that tracks children weakly."""
    pass

class Child:
    """Child with weak reference to parent."""
    def __init__(self, parent):
        # TODO: Store weak reference to parent
        pass

# Test Tests
def test_weak_parent():
    parent = Parent()
    child = Child(parent)
    
    assert child.get_parent() is parent
    
    del parent
    gc.collect()
    assert child.get_parent() is None
    print("✓ Exercise 4 passed: weak parent reference works")

# =============================================================================
# Exercise 5: Weak Reference Proxy (★★★★★)
# =============================================================================
# TODO: Implement transparent proxy using weak references

class WeakProxy:
    """Proxy that allows accessing object through weak reference."""
    # TODO: Implement __getattr__ to forward attribute access
    # TODO: Raise appropriate error if referent is dead
    pass

# Test Cases
def test_weak_proxy():
    class RealObject:
        def __init__(self, value):
            self.value = value
        
        def get_value(self):
            return self.value
    
    obj = RealObject(42)
    proxy = WeakProxy(obj)
    
    assert proxy.value == 42
    assert proxy.get_value() == 42
    
    del obj
    gc.collect()
    
    try:
        _ = proxy.value
        assert False, "Should have raised ReferenceError"
    except ReferenceError:
        pass
    
    print("✓ Exercise 5 passed: weak proxy forwards access and raises on death")

if __name__ == "__main__":
    print("Running Weak References Exercises...")
    print("=" * 50)
    test_basic_weakref()
    test_weak_cache()
    test_destruction_tracker()
    test_weak_parent()
    test_weak_proxy()
    print("=" * 50)
    print("All tests passed!")
