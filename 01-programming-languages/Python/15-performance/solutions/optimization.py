"""
Module 15 - Performance: Optimization Solutions
Complete solutions with explanations
"""

import time
import sys
from collections import deque
from functools import reduce


# =============================================================================
# Exercise 1: String Optimization - SOLUTION
# =============================================================================

def exercise_1_string_optimization():
    """
    Optimize string operations.
    """
    n = 10000
    
    # Method 1: String concatenation with +
    start = time.perf_counter()
    result1 = ""
    for i in range(n):
        result1 += str(i)
    time_concat = time.perf_counter() - start
    
    # Method 2: Join with list
    start = time.perf_counter()
    result2 = "".join(str(i) for i in range(n))
    time_join = time.perf_counter() - start
    
    # Method 3: StringIO
    from io import StringIO
    start = time.perf_counter()
    buffer = StringIO()
    for i in range(n):
        buffer.write(str(i))
    result3 = buffer.getvalue()
    time_stringio = time.perf_counter() - start
    
    return {
        'concat_time': time_concat,
        'join_time': time_join,
        'stringio_time': time_stringio,
        'fastest': 'join' if time_join < time_concat else 'concat',
        'speedup': time_concat / time_join if time_join > 0 else 0,
    }


# =============================================================================
# Exercise 2: List Optimization - SOLUTION
# =============================================================================

def exercise_2_list_optimization():
    """
    Optimize list operations.
    """
    n = 100000
    
    # Method 1: List comprehension
    start = time.perf_counter()
    result1 = [i * 2 for i in range(n)]
    time_comprehension = time.perf_counter() - start
    
    # Method 2: For loop with append
    start = time.perf_counter()
    result2 = []
    for i in range(n):
        result2.append(i * 2)
    time_loop = time.perf_counter() - start
    
    # Method 3: Map function
    start = time.perf_counter()
    result3 = list(map(lambda x: x * 2, range(n)))
    time_map = time.perf_counter() - start
    
    # Method 4: Deque for frequent insertions
    start = time.perf_counter()
    dq = deque()
    for i in range(n):
        dq.appendleft(i)
    time_deque = time.perf_counter() - start
    
    return {
        'comprehension_time': time_comprehension,
        'loop_time': time_loop,
        'map_time': time_map,
        'deque_time': time_deque,
        'fastest': min([
            ('comprehension', time_comprehension),
            ('loop', time_loop),
            ('map', time_map),
        ], key=lambda x: x[1])[0],
    }


# =============================================================================
# Exercise 3: Generator Optimization - SOLUTION
# =============================================================================

def exercise_3_generator_optimization():
    """
    Use generators for memory efficiency.
    """
    # Compare memory usage
    n = 1000000
    
    # List
    list_memory = sys.getsizeof([i for i in range(n)])
    
    # Generator
    gen = (i for i in range(n))
    gen_memory = sys.getsizeof(gen)
    
    # Generator function
    def gen_func():
        for i in range(n):
            yield i
    
    gen_func_memory = sys.getsizeof(gen_func)
    
    # Pipeline example
    def pipeline():
        # Stage 1: Generate numbers
        numbers = (i for i in range(1000))
        # Stage 2: Filter even
        evens = (x for x in numbers if x % 2 == 0)
        # Stage 3: Square
        squared = (x ** 2 for x in evens)
        return squared
    
    result = list(pipeline())[:5]
    
    return {
        'list_memory': list_memory,
        'generator_memory': gen_memory,
        'gen_func_memory': gen_func_memory,
        'memory_savings': list_memory - gen_memory,
        'pipeline_result': result,
    }


# =============================================================================
# Exercise 4: Algorithm Optimization - SOLUTION
# =============================================================================

def exercise_4_algorithm_optimization():
    """
    Optimize algorithms for better performance.
    """
    # Naive search vs binary search
    import bisect
    
    data = list(range(10000))
    target = 9999
    
    # Naive search
    start = time.perf_counter()
    for _ in range(1000):
        result_naive = target in data
    time_naive = time.perf_counter() - start
    
    # Binary search (using bisect)
    start = time.perf_counter()
    for _ in range(1000):
        idx = bisect.bisect_left(data, target)
        result_binary = idx < len(data) and data[idx] == target
    time_binary = time.perf_counter() - start
    
    # Set lookup
    data_set = set(data)
    start = time.perf_counter()
    for _ in range(1000):
        result_set = target in data_set
    time_set = time.perf_counter() - start
    
    return {
        'naive_time': time_naive,
        'binary_time': time_binary,
        'set_time': time_set,
        'fastest': min([
            ('naive', time_naive),
            ('binary', time_binary),
            ('set', time_set),
        ], key=lambda x: x[1])[0],
        'speedup_binary': time_naive / time_binary if time_binary > 0 else 0,
        'speedup_set': time_naive / time_set if time_set > 0 else 0,
    }


# =============================================================================
# Exercise 5: Optimization Techniques - SOLUTION
# =============================================================================

def exercise_5_optimization_techniques():
    """
    Apply various optimization techniques.
    """
    # Technique 1: Use built-in functions
    data = list(range(10000))
    
    start = time.perf_counter()
    result_sum = sum(data)
    time_builtin_sum = time.perf_counter() - start
    
    start = time.perf_counter()
    result_manual = 0
    for x in data:
        result_manual += x
    time_manual_sum = time.perf_counter() - start
    
    # Technique 2: Avoid repeated computations
    start = time.perf_counter()
    for _ in range(1000):
        _ = len(data)  # Called repeatedly
    time_repeated = time.perf_counter() - start
    
    start = time.perf_counter()
    data_len = len(data)
    for _ in range(1000):
        _ = data_len  # Computed once
    time_cached = time.perf_counter() - start
    
    # Technique 3: Use appropriate data structures
    import random
    lookup_data = list(range(10000))
    lookup_set = set(lookup_data)
    
    targets = [random.randint(0, 9999) for _ in range(1000)]
    
    start = time.perf_counter()
    for t in targets:
        _ = t in lookup_data
    time_list_lookup = time.perf_counter() - start
    
    start = time.perf_counter()
    for t in targets:
        _ = t in lookup_set
    time_set_lookup = time.perf_counter() - start
    
    return {
        'builtin_sum_speedup': time_manual_sum / time_builtin_sum if time_builtin_sum > 0 else 0,
        'cached_length_speedup': time_repeated / time_cached if time_cached > 0 else 0,
        'set_lookup_speedup': time_list_lookup / time_set_lookup if time_set_lookup > 0 else 0,
    }


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 15 - Optimization Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: String Optimization")
    result = exercise_1_string_optimization()
    assert isinstance(result, dict)
    assert 'concat_time' in result
    print(f"  Speedup: {result['speedup']:.2f}x")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: List Optimization")
    result = exercise_2_list_optimization()
    assert isinstance(result, dict)
    assert 'comprehension_time' in result
    print(f"  Fastest: {result['fastest']}")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Generator Optimization")
    result = exercise_3_generator_optimization()
    assert isinstance(result, dict)
    assert result['memory_savings'] > 0
    print(f"  Memory savings: {result['memory_savings']} bytes")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Algorithm Optimization")
    result = exercise_4_algorithm_optimization()
    assert isinstance(result, dict)
    assert result['speedup_set'] > 1
    print(f"  Set lookup speedup: {result['speedup_set']:.2f}x")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Optimization Techniques")
    result = exercise_5_optimization_techniques()
    assert isinstance(result, dict)
    assert result['builtin_sum_speedup'] > 1
    print(f"  Builtin sum speedup: {result['builtin_sum_speedup']:.2f}x")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
