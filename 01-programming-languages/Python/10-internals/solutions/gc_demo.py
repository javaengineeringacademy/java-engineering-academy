"""
Module 10 - Python Internals: Garbage Collection Solutions
Complete solutions with explanations
"""

import gc
import sys
import weakref
from collections import deque


# =============================================================================
# Exercise 1: Reference Counting Basics - SOLUTION
# =============================================================================

def exercise_1_reference_counting():
    """
    Understand reference counting in Python.
    """
    counts = []
    
    # Create an object and check its reference count
    my_list = [1, 2, 3]
    counts.append(sys.getrefcount(my_list))  # +1 for the temporary reference in getrefcount
    
    # Add more references to the same object
    ref1 = my_list
    counts.append(sys.getrefcount(my_list))
    
    ref2 = my_list
    counts.append(sys.getrefcount(my_list))
    
    ref3 = [my_list, my_list]  # Two references in a list
    counts.append(sys.getrefcount(my_list))
    
    # Remove references
    del ref1
    counts.append(sys.getrefcount(my_list))
    
    del ref2
    counts.append(sys.getrefcount(my_list))
    
    del ref3
    counts.append(sys.getrefcount(my_list))
    
    print("\nReference counts at each step:")
    steps = ['Initial', 'After ref1', 'After ref2', 'After ref3', 
             'After del ref1', 'After del ref2', 'After del ref3']
    for step, count in zip(steps, counts):
        print(f"  {step}: {count}")
    
    return counts


# =============================================================================
# Exercise 2: Circular Reference Detection - SOLUTION
# =============================================================================

def exercise_2_circular_reference():
    """
    Create circular references and detect them using gc module.
    """
    # Enable gc debugging
    gc.set_debug(gc.DEBUG_SAVEALL)
    
    # Create circular reference
    class Node:
        def __init__(self, value):
            self.value = value
            self.ref = None
    
    collected_count = 0
    
    # Create objects that reference each other
    node_a = Node('A')
    node_b = Node('B')
    node_a.ref = node_b
    node_b.ref = node_a
    
    # Delete references (creates circular reference)
    del node_a
    del node_b
    
    # Force garbage collection
    collected = gc.collect()
    collected_count = collected
    
    print(f"\nCircular reference collection:")
    print(f"  Objects collected: {collected}")
    
    # Reset gc debug
    gc.set_debug(0)
    
    return collected_count


# =============================================================================
# Exercise 3: GC Thresholds - SOLUTION
# =============================================================================

def exercise_3_gc_thresholds():
    """
    Understand and manipulate GC thresholds.
    """
    # Get current GC thresholds
    old_thresholds = gc.get_threshold()
    
    print(f"\nOriginal thresholds: {old_thresholds}")
    print(f"  Generation 0: collect after {old_thresholds[0]} allocations")
    print(f"  Generation 1: collect after {old_thresholds[1]} gen0 collections")
    print(f"  Generation 2: collect after {old_thresholds[2]} gen1 collections")
    
    # Set new thresholds (more aggressive collection)
    new_thresholds = (50, 8, 10)
    gc.set_threshold(*new_thresholds)
    
    # Verify the changes
    current_thresholds = gc.get_threshold()
    print(f"\nNew thresholds: {current_thresholds}")
    
    return (old_thresholds, current_thresholds)


# =============================================================================
# Exercise 4: Weak References - SOLUTION
# =============================================================================

def exercise_4_weak_references():
    """
    Use weak references to avoid preventing garbage collection.
    """
    import weakref
    
    class Data:
        def __init__(self, value):
            self.value = value
        def __repr__(self):
            return f"Data({self.value})"
    
    # Create an object and a weak reference to it
    obj = Data(42)
    weak_ref = weakref.ref(obj)
    
    print(f"\nWeak reference test:")
    print(f"  Object: {obj}")
    print(f"  Weak ref alive (before delete): {weak_ref() is not None}")
    print(f"  Weak ref value: {weak_ref()}")
    
    # Delete the original object
    del obj
    
    # Check if weak reference is still valid
    is_alive = weak_ref() is not None
    print(f"  Weak ref alive (after delete): {is_alive}")
    
    return is_alive


# =============================================================================
# Exercise 5: GC Statistics - SOLUTION
# =============================================================================

def exercise_5_gc_statistics():
    """
    Collect and analyze garbage collection statistics.
    """
    # Enable GC statistics collection
    gc.set_debug(gc.DEBUG_STATS)
    
    # Create many objects to trigger GC
    objects = []
    for i in range(1000):
        objects.append([i, i**2, str(i)])
    
    # Force garbage collection to ensure stats are collected
    gc.collect()
    
    # Get GC statistics
    stats = gc.get_stats()
    
    # Analyze statistics
    summary = {
        'generation_0': {
            'collections': stats[0]['collections'],
            'collected': stats[0]['collected'],
            'uncollectable': stats[0]['uncollectable'],
        },
        'generation_1': {
            'collections': stats[1]['collections'],
            'collected': stats[1]['collected'],
            'uncollectable': stats[1]['uncollectable'],
        },
        'generation_2': {
            'collections': stats[2]['collections'],
            'collected': stats[2]['collected'],
            'uncollectable': stats[2]['uncollectable'],
        },
        'total_tracked': len(gc.get_objects()),
    }
    
    print("\nGC Statistics:")
    for gen, data in summary.items():
        if gen != 'total_tracked':
            print(f"\n  {gen}:")
            print(f"    Collections: {data['collections']}")
            print(f"    Collected: {data['collected']}")
            print(f"    Uncollectable: {data['uncollectable']}")
    print(f"\n  Total tracked objects: {summary['total_tracked']}")
    
    # Clean up
    del objects
    gc.set_debug(0)
    
    return summary


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 10 - Garbage Collection Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Reference Counting Basics")
    result = exercise_1_reference_counting()
    print(f"  Counts: {result}")
    assert isinstance(result, list), "Should return a list"
    assert len(result) >= 2, "Should have at least 2 counts"
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Circular Reference Detection")
    result = exercise_2_circular_reference()
    print(f"  Collected objects: {result}")
    assert isinstance(result, int), "Should return an integer"
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: GC Thresholds")
    result = exercise_3_gc_thresholds()
    print(f"  Old thresholds: {result[0]}, New: {result[1]}")
    assert isinstance(result, tuple), "Should return a tuple"
    assert len(result) == 2, "Should have old and new thresholds"
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Weak References")
    result = exercise_4_weak_references()
    print(f"  Weak reference alive: {result}")
    assert isinstance(result, bool), "Should return a boolean"
    assert result is False, "Should be False after deleting original"
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: GC Statistics")
    result = exercise_5_gc_statistics()
    print(f"  Stats: {result}")
    assert isinstance(result, dict), "Should return a dictionary"
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
