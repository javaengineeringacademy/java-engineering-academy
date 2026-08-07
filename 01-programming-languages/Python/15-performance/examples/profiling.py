"""
Python Profiling Demo
Demonstrates profiling techniques for performance analysis
"""

import cProfile
import time
import pstats
from io import StringIO

# ============================================
# Basic Profiling
# ============================================

def basic_profiling() -> None:
    """Demonstrate basic profiling."""
    print("=== Basic Profiling ===")
    
    def slow_function():
        """Function to profile."""
        total = 0
        for i in range(1000000):
            total += i
        return total
    
    def another_function():
        """Another function to profile."""
        return [i ** 2 for i in range(100000)]
    
    # Profile the functions
    profiler = cProfile.Profile()
    profiler.enable()
    
    slow_function()
    another_function()
    
    profiler.disable()
    
    # Print stats
    print("  Profile results:")
    stats = pstats.Stats(profiler, stream=sys.stdout)
    stats.sort_stats('cumulative')
    stats.print_stats(10)  # Top 10 functions

# ============================================
# Timing Functions
# ============================================

def timing_example() -> None:
    """Demonstrate timing techniques."""
    print("\n=== Timing ===")
    
    import sys
    
    # Simple timing
    start = time.time()
    result = sum(range(1000000))
    end = time.time()
    print(f"  Sum: {result}, Time: {end - start:.4f}s")
    
    # Using timeit
    import timeit
    
    def test_function():
        return sum(range(100000))
    
    # Timeit with number
    time_taken = timeit.timeit(test_function, number=100)
    print(f"  Timeit (100 runs): {time_taken:.4f}s")
    
    # Timeit with setup
    time_taken = timeit.timeit(
        'sum(range(100000))',
        number=100
    )
    print(f"  Timeit (statement): {time_taken:.4f}s")

# ============================================
# Memory Profiling
# ============================================

def memory_profiling() -> None:
    """Demonstrate memory profiling."""
    print("\n=== Memory Profiling ===")
    
    import sys
    
    # Get size of objects
    data = list(range(10000))
    print(f"  List size: {sys.getsizeof(data)} bytes")
    
    # Get size recursively
    def get_size(obj, seen=None):
        """Get size of object recursively."""
        size = sys.getsizeof(obj)
        if seen is None:
            seen = set()
        
        obj_id = id(obj)
        if obj_id in seen:
            return 0
        
        seen.add(obj_id)
        
        if isinstance(obj, dict):
            size += sum(get_size(k, seen) + get_size(v, seen) for k, v in obj.items())
        elif isinstance(obj, (list, tuple, set, frozenset)):
            size += sum(get_size(i, seen) for i in obj)
        
        return size
    
    nested = {'a': [1, 2, 3], 'b': {'c': 4, 'd': 5}}
    print(f"  Nested dict size: {get_size(nested)} bytes")

# ============================================
# Line Profiling
# ============================================

def line_profiling() -> None:
    """Demonstrate line profiling (conceptual)."""
    print("\n=== Line Profiling ===")
    
    def process_data(data):
        """Process data with multiple steps."""
        # Step 1: Filter
        filtered = [x for x in data if x > 0]
        
        # Step 2: Transform
        transformed = [x ** 2 for x in filtered]
        
        # Step 3: Aggregate
        result = sum(transformed)
        
        return result
    
    data = list(range(-1000, 1000))
    
    # Manual timing for each step
    start = time.time()
    filtered = [x for x in data if x > 0]
    step1_time = time.time() - start
    
    start = time.time()
    transformed = [x ** 2 for x in filtered]
    step2_time = time.time() - start
    
    start = time.time()
    result = sum(transformed)
    step3_time = time.time() - start
    
    print(f"  Step 1 (filter): {step1_time:.6f}s")
    print(f"  Step 2 (transform): {step2_time:.6f}s")
    print(f"  Step 3 (aggregate): {step3_time:.6f}s")
    print(f"  Total: {step1_time + step2_time + step3_time:.6f}s")

# ============================================
# Main Execution
# ============================================

import sys

if __name__ == "__main__":
    basic_profiling()
    timing_example()
    memory_profiling()
    line_profiling()
