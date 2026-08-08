"""
Module 14 - Memory Management: GC Demo Solutions
Complete solutions with explanations
"""

import gc
import sys
import time


# =============================================================================
# Exercise 1: Reference Counting - SOLUTION
# =============================================================================

def exercise_1_reference_counting():
    """
    Understand reference counting in Python.
    """
    counts = []
    
    # Create object and check initial reference count
    my_list = [1, 2, 3]
    counts.append(sys.getrefcount(my_list))
    
    # Add more references
    ref1 = my_list
    counts.append(sys.getrefcount(my_list))
    
    ref2 = my_list
    counts.append(sys.getrefcount(my_list))
    
    # Remove references
    del ref1
    counts.append(sys.getrefcount(my_list))
    
    del ref2
    counts.append(sys.getrefcount(my_list))
    
    return counts


# =============================================================================
# Exercise 2: Circular References - SOLUTION
# =============================================================================

def exercise_2_circular_references():
    """
    Create and detect circular references.
    """
    gc.set_debug(gc.DEBUG_SAVEALL)
    
    class Node:
        def __init__(self, value):
            self.value = value
            self.ref = None
    
    # Create circular reference
    node_a = Node('A')
    node_b = Node('B')
    node_a.ref = node_b
    node_b.ref = node_a
    
    # Delete references
    del node_a
    del node_b
    
    # Force collection
    collected = gc.collect()
    
    gc.set_debug(0)
    return collected


# =============================================================================
# Exercise 3: GC Generations - SOLUTION
# =============================================================================

def exercise_3_gc_generations():
    """
    Understand GC generations.
    """
    # Get initial stats
    initial_stats = gc.get_stats()
    
    # Create many objects
    objects = []
    for i in range(1000):
        objects.append([i, str(i)])
    
    # Get final stats
    final_stats = gc.get_stats()
    
    stats = {
        'gen0_collections': final_stats[0]['collections'] - initial_stats[0]['collections'],
        'gen0_collected': final_stats[0]['collected'] - initial_stats[0]['collected'],
        'gen1_collections': final_stats[1]['collections'] - initial_stats[1]['collections'],
        'gen2_collections': final_stats[2]['collections'] - initial_stats[2]['collections'],
        'thresholds': gc.get_threshold(),
    }
    
    del objects
    return stats


# =============================================================================
# Exercise 4: Memory Profiling - SOLUTION
# =============================================================================

def exercise_4_memory_profiling():
    """
    Profile memory usage of data structures.
    """
    import sys
    
    # Measure memory of different structures
    list_size = sys.getsizeof([1, 2, 3, 4, 5])
    tuple_size = sys.getsizeof((1, 2, 3, 4, 5))
    dict_size = sys.getsizeof({1: 'a', 2: 'b', 3: 'c'})
    set_size = sys.getsizeof({1, 2, 3, 4, 5})
    
    # Empty containers
    empty_list = sys.getsizeof([])
    empty_tuple = sys.getsizeof(())
    empty_dict = sys.getsizeof({})
    empty_set = sys.getsizeof(set())
    
    sizes = {
        'list_5_items': list_size,
        'tuple_5_items': tuple_size,
        'dict_3_items': dict_size,
        'set_5_items': set_size,
        'empty_list': empty_list,
        'empty_tuple': empty_tuple,
        'empty_dict': empty_dict,
        'empty_set': empty_set,
        'list_savings': empty_list - list_size,
    }
    
    return sizes


# =============================================================================
# Exercise 5: GC Tuning - SOLUTION
# =============================================================================

def exercise_5_gc_tuning():
    """
    Tune garbage collection parameters.
    """
    # Get original thresholds
    original_thresholds = gc.get_threshold()
    
    # Benchmark with default thresholds
    start = time.time()
    for _ in range(10000):
        gc.collect()
    default_time = time.time() - start
    
    # Set more aggressive thresholds
    gc.set_threshold(50, 5, 5)
    
    start = time.time()
    for _ in range(10000):
        gc.collect()
    aggressive_time = time.time() - start
    
    # Set less aggressive thresholds
    gc.set_threshold(200, 20, 20)
    
    start = time.time()
    for _ in range(10000):
        gc.collect()
    lazy_time = time.time() - start
    
    # Restore original thresholds
    gc.set_threshold(*original_thresholds)
    
    results = {
        'original_thresholds': original_thresholds,
        'default_time': round(default_time, 4),
        'aggressive_time': round(aggressive_time, 4),
        'lazy_time': round(lazy_time, 4),
        'current_thresholds': gc.get_threshold(),
    }
    
    return results


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 14 - GC Demo Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Reference Counting")
    result = exercise_1_reference_counting()
    assert isinstance(result, list)
    assert len(result) >= 3
    print(f"  Counts: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Circular References")
    result = exercise_2_circular_references()
    assert isinstance(result, int)
    print(f"  Collected: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: GC Generations")
    result = exercise_3_gc_generations()
    assert isinstance(result, dict)
    assert 'gen0_collections' in result
    print(f"  Stats: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Memory Profiling")
    result = exercise_4_memory_profiling()
    assert isinstance(result, dict)
    assert 'list_5_items' in result
    print(f"  Sizes: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: GC Tuning")
    result = exercise_5_gc_tuning()
    assert isinstance(result, dict)
    assert 'default_time' in result
    print(f"  Results: {result}")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
