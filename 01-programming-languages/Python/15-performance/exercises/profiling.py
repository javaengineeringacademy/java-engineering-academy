"""
Module 15 - Performance: Profiling Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Performance profiling and benchmarking
"""

import time
import cProfile
import pstats
from functools import wraps


# =============================================================================
# Exercise 1: Basic Timing (⭐⭐⭐)
# =============================================================================

def exercise_1_basic_timing():
    """
    Time function execution.
    
    TODO:
    1. Create a timing decorator
    2. Measure function execution time
    3. Return timing results
    """
    def timing_decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            # TODO: Measure execution time
            pass
        return wrapper
    
    @timing_decorator
    def slow_function():
        time.sleep(0.01)
        return "done"
    
    # TODO: Call function and get timing
    pass


# =============================================================================
# Exercise 2: Memory Profiling (⭐⭐⭐⭐)
# =============================================================================

def exercise_2_memory_profiling():
    """
    Profile memory usage.
    
    TODO:
    1. Use memory_profiler if available
    2. Measure memory before and after operations
    3. Return memory usage
    """
    import sys
    
    results = {}
    
    # TODO: Profile memory usage
    
    return results


# =============================================================================
# Exercise 3: cProfile Usage (⭐⭐⭐⭐)
# =============================================================================

def exercise_3_cprofile():
    """
    Use cProfile for function profiling.
    
    TODO:
    1. Profile a function using cProfile
    2. Get statistics
    3. Return top functions by time
    """
    def compute():
        total = 0
        for i in range(10000):
            total += i * i
        return total
    
    results = {}
    
    # TODO: Profile compute function
    
    return results


# =============================================================================
# Exercise 4: Line Profiling (⭐⭐⭐⭐)
# =============================================================================

def exercise_4_line_profiling():
    """
    Profile individual lines of code.
    
    TODO:
    1. Use line_profiler if available
    2. Identify slow lines
    3. Return line-by-line timing
    """
    def slow_algorithm(data):
        result = []
        for item in data:
            if item % 2 == 0:
                result.append(item ** 2)
        return sorted(result)
    
    results = {}
    
    # TODO: Profile slow_algorithm
    
    return results


# =============================================================================
# Exercise 5: Benchmark Suite (⭐⭐⭐⭐⭐)
# =============================================================================

def exercise_5_benchmark_suite():
    """
    Create a benchmark suite for comparing implementations.
    
    TODO:
    1. Create benchmark decorator
    2. Run function multiple times
    3. Calculate statistics (mean, std, min, max)
    """
    def benchmark(iterations=100):
        def decorator(func):
            @wraps(func)
            def wrapper(*args, **kwargs):
                # TODO: Run benchmark
                pass
            return wrapper
        return decorator
    
    @benchmark(iterations=10)
    def test_function():
        return sum(range(1000))
    
    # TODO: Run benchmark and return results
    pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 15 - Profiling Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Timing")
    try:
        result = exercise_1_basic_timing()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Memory Profiling")
    try:
        result = exercise_2_memory_profiling()
        assert isinstance(result, dict)
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: cProfile Usage")
    try:
        result = exercise_3_cprofile()
        assert isinstance(result, dict)
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Line Profiling")
    try:
        result = exercise_4_line_profiling()
        assert isinstance(result, dict)
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Benchmark Suite")
    try:
        result = exercise_5_benchmark_suite()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
