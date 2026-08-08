"""
Module 10 - Python Internals: Garbage Collection Exercises
Difficulty: ⭐⭐⭐⭐ (Advanced)
Topic: Understanding Python's garbage collection mechanism
"""

import gc
import sys
from collections import deque


# =============================================================================
# Exercise 1: Reference Counting Basics (⭐⭐⭐)
# =============================================================================

def exercise_1_reference_counting():
    """
    Understand reference counting in Python.
    
    TODO:
    1. Create an object and check its reference count using sys.getrefcount()
    2. Add more references to the same object
    3. Remove references and observe count changes
    4. Return a list of reference counts at each step
    """
    counts = []
    
    # TODO: Create object and track reference counts
    # TODO: Add references
    # TODO: Remove references
    # TODO: Return counts list
    
    return counts


# =============================================================================
# Exercise 2: Circular Reference Detection (⭐⭐⭐⭐)
# =============================================================================

def exercise_2_circular_reference():
    """
    Create circular references and detect them using gc module.
    
    TODO:
    1. Create two objects that reference each other
    2. Enable gc debugging (gc.set_debug(gc.DEBUG_SAVEALL))
    3. Force garbage collection
    4. Return the number of collected objects
    """
    # TODO: Create circular reference
    # TODO: Track collection
    pass


# =============================================================================
# Exercise 3: Garbage Collection Thresholds (⭐⭐⭐)
# =============================================================================

def exercise_3_gc_thresholds():
    """
    Understand and manipulate GC thresholds.
    
    TODO:
    1. Get current GC thresholds using gc.get_threshold()
    2. Set new thresholds
    3. Verify the changes
    4. Return old and new thresholds as a tuple
    """
    # TODO: Get current thresholds
    # TODO: Set new thresholds
    # TODO: Return old and new values
    pass


# =============================================================================
# Exercise 4: Weak References (⭐⭐⭐⭐)
# =============================================================================

def exercise_4_weak_references():
    """
    Use weak references to avoid preventing garbage collection.
    
    TODO:
    1. Import weakref module
    2. Create an object and a weak reference to it
    3. Delete the original object
    4. Check if weak reference is still valid
    5. Return whether weak reference is alive or dead
    """
    # TODO: Create weak reference
    # TODO: Delete original
    # TODO: Check weak reference status
    pass


# =============================================================================
# Exercise 5: GC Statistics (⭐⭐⭐⭐⭐)
# =============================================================================

def exercise_5_gc_statistics():
    """
    Collect and analyze garbage collection statistics.
    
    TODO:
    1. Enable GC statistics collection using gc.set_debug()
    2. Create many objects to trigger GC
    3. Get GC statistics using gc.get_stats()
    4. Return a summary dict with generation counts and collections
    """
    # TODO: Enable statistics
    # TODO: Create objects to trigger GC
    # TODO: Collect statistics
    pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 10 - Garbage Collection Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Reference Counting Basics")
    try:
        result = exercise_1_reference_counting()
        print(f"  Counts: {result}")
        assert isinstance(result, list), "Should return a list"
        assert len(result) >= 2, "Should have at least 2 counts"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Circular Reference Detection")
    try:
        result = exercise_2_circular_reference()
        print(f"  Collected objects: {result}")
        assert isinstance(result, int), "Should return an integer"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: GC Thresholds")
    try:
        result = exercise_3_gc_thresholds()
        print(f"  Old thresholds: {result[0]}, New: {result[1]}")
        assert isinstance(result, tuple), "Should return a tuple"
        assert len(result) == 2, "Should have old and new thresholds"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Weak References")
    try:
        result = exercise_4_weak_references()
        print(f"  Weak reference alive: {result}")
        assert isinstance(result, bool), "Should return a boolean"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: GC Statistics")
    try:
        result = exercise_5_gc_statistics()
        print(f"  Stats: {result}")
        assert isinstance(result, dict), "Should return a dictionary"
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
