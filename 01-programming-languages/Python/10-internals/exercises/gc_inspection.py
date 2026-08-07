"""
Module 10: Python Internals - GC Inspection Exercises
=====================================================
Practice inspecting and understanding Python's garbage collection.
"""

import gc
import weakref
import sys

# =============================================================================
# Exercise 1: Object Counter (★☆☆☆☆)
# =============================================================================
# TODO: Track object creation and destruction

class ObjectCounter:
    """Count instances of a class that have been created."""
    # TODO: Use __init__ and __del__ to track counts
    pass

# Test Cases
def test_object_counter():
    class TrackedClass(ObjectCounter):
        pass
    
    a = TrackedClass()
    b = TrackedClass()
    assert TrackedClass.instance_count == 2
    
    del b
    gc.collect()
    assert TrackedClass.instance_count == 1
    print("✓ Exercise 1 passed: object counting works")

# =============================================================================
# Exercise 2: Reference Cycle Detector (★★☆☆☆)
# =============================================================================
# TODO: Detect reference cycles in objects

def has_reference_cycle(obj):
    """Check if object is part of a reference cycle."""
    # TODO: Use gc.get_referrers and gc.get_referrers
    pass

# Test Cases
def test_cycle_detection():
    # Create a cycle
    a = []
    b = [a]
    a.append(b)
    
    assert has_reference_cycle(a)
    assert has_reference_cycle(b)
    
    # No cycle
    c = [1, 2, 3]
    assert not has_reference_cycle(c)
    print("✓ Exercise 2 passed: cycle detection works")

# =============================================================================
# Exercise 3: Memory Usage Tracker (★★★☆☆)
# =============================================================================
# TODO: Track memory usage of objects

def get_memory_usage(obj):
    """Return memory usage in bytes for object and its referents."""
    # TODO: Use sys.getsizeof recursively
    pass

# Test Cases
def test_memory_tracking():
    small = [1, 2, 3]
    large = list(range(1000))
    
    small_mem = get_memory_usage(small)
    large_mem = get_memory_usage(large)
    
    assert small_mem > 0
    assert large_mem > small_mem
    print(f"✓ Exercise 3 passed: small={small_mem}B, large={large_mem}B")

# =============================================================================
# Exercise 4: Weak Reference Manager (★★★★☆)
# =============================================================================
# TODO: Create manager that tracks objects via weak references

class WeakRegistry:
    """Registry that tracks objects without preventing GC."""
    # TODO: Use weakref.WeakSet or WeakValueDictionary
    pass

# Test Cases
def test_weak_registry():
    registry = WeakRegistry()
    
    class Obj:
        def __init__(self, name):
            self.name = name
    
    obj1 = Obj("first")
    obj2 = Obj("second")
    registry.register(obj1)
    registry.register(obj2)
    
    assert len(registry) == 2
    
    del obj1
    gc.collect()
    assert len(registry) == 1
    print("✓ Exercise 4 passed: weak references working")

# =============================================================================
# Exercise 5: GC Event Logger (★★★★★)
# =============================================================================
# TODO: Log garbage collection events

class GCLogger:
    """Track and log GC statistics."""
    # TODO: Hook into gc callbacks to log events
    pass

# Test Cases
def test_gc_logger():
    logger = GCLogger()
    logger.enable()
    
    # Create some garbage
    for _ in range(100):
        _ = [i for i in range(100)]
    
    gc.collect()
    stats = logger.get_stats()
    
    assert "collections" in stats
    assert "collected" in stats
    assert stats["collections"] > 0
    logger.disable()
    print(f"✓ Exercise 5 passed: logged {stats['collections']} GC events")

if __name__ == "__main__":
    print("Running GC Inspection Exercises...")
    print("=" * 50)
    test_object_counter()
    test_cycle_detection()
    test_memory_tracking()
    test_weak_registry()
    test_gc_logger()
    print("=" * 50)
    print("All tests passed!")
