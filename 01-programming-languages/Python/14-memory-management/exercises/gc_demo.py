"""
Module 14 - Memory Management: GC Demo Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Garbage collection and memory management
"""

import gc
import sys


# =============================================================================
# Exercise 1: Reference Counting (⭐⭐⭐)
# =============================================================================

def exercise_1_reference_counting():
    """
    Understand reference counting in Python.
    
    TODO:
    1. Create objects and track reference counts
    2. Add and remove references
    3. Observe count changes
    """
    counts = []
    
    # TODO: Create object and track reference counts
    # TODO: Add references and track
    # TODO: Remove references and track
    
    return counts


# =============================================================================
# Exercise 2: Circular References (⭐⭐⭐⭐)
# =============================================================================

def exercise_2_circular_references():
    """
    Create and detect circular references.
    
    TODO:
    1. Create circular reference between two objects
    2. Enable gc debugging
    3. Force collection and count collected objects
    """
    gc.set_debug(gc.DEBUG_SAVEALL)
    
    collected = 0
    
    # TODO: Create circular references
    # TODO: Delete references
    # TODO: Force collection
    
    gc.set_debug(0)
    return collected


# =============================================================================
# Exercise 3: GC Generations (⭐⭐⭐)
# =============================================================================

def exercise_3_gc_generations():
    """
    Understand GC generations.
    
    TODO:
    1. Get current generation counts
    2. Create objects to trigger collection
    3. Return generation statistics
    """
    stats = {}
    
    # TODO: Get initial stats
    # TODO: Create objects
    # TODO: Get final stats
    
    return stats


# =============================================================================
# Exercise 4: Memory Profiling (⭐⭐⭐⭐)
# =============================================================================

def exercise_4_memory_profiling():
    """
    Profile memory usage of data structures.
    
    TODO:
    1. Create different data structures
    2. Measure their memory usage
    3. Return size comparison
    """
    import sys
    
    sizes = {}
    
    # TODO: Measure memory of different structures
    
    return sizes


# =============================================================================
# Exercise 5: Garbage Collection Tuning (⭐⭐⭐⭐⭐)
# =============================================================================

def exercise_5_gc_tuning():
    """
    Tune garbage collection parameters.
    
    TODO:
    1. Get current thresholds
    2. Set new thresholds
    3. Measure performance difference
    """
    import time
    
    results = {}
    
    # TODO: Benchmark with different thresholds
    
    return results


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 14 - GC Demo Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Reference Counting")
    try:
        result = exercise_1_reference_counting()
        assert isinstance(result, list)
        print(f"  Counts: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Circular References")
    try:
        result = exercise_2_circular_references()
        assert isinstance(result, int)
        print(f"  Collected: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: GC Generations")
    try:
        result = exercise_3_gc_generations()
        assert isinstance(result, dict)
        print(f"  Stats: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Memory Profiling")
    try:
        result = exercise_4_memory_profiling()
        assert isinstance(result, dict)
        print(f"  Sizes: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: GC Tuning")
    try:
        result = exercise_5_gc_tuning()
        assert isinstance(result, dict)
        print(f"  Results: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
