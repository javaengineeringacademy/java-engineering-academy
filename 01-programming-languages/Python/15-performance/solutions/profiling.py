"""
Module 15 - Performance: Profiling Solutions
Complete solutions with explanations
"""

import time
import cProfile
import pstats
from functools import wraps
from io import StringIO


# =============================================================================
# Exercise 1: Basic Timing - SOLUTION
# =============================================================================

def exercise_1_basic_timing():
    """
    Time function execution.
    """
    def timing_decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            start = time.perf_counter()
            result = func(*args, **kwargs)
            end = time.perf_counter()
            wrapper.execution_time = end - start
            return result
        return wrapper
    
    @timing_decorator
    def slow_function():
        time.sleep(0.01)
        return "done"
    
    result = slow_function()
    
    return {
        'result': result,
        'execution_time': slow_function.execution_time
    }


# =============================================================================
# Exercise 2: Memory Profiling - SOLUTION
# =============================================================================

def exercise_2_memory_profiling():
    """
    Profile memory usage.
    """
    import sys
    
    # Measure memory of different operations
    results = {}
    
    # List creation
    start_mem = sys.getsizeof([])
    lst = [i for i in range(1000)]
    end_mem = sys.getsizeof(lst)
    results['list_1000'] = end_mem - start_mem
    
    # Dict creation
    start_mem = sys.getsizeof({})
    d = {i: i for i in range(1000)}
    end_mem = sys.getsizeof(d)
    results['dict_1000'] = end_mem - start_mem
    
    # Set creation
    start_mem = sys.getsizeof(set())
    s = {i for i in range(1000)}
    end_mem = sys.getsizeof(s)
    results['set_1000'] = end_mem - start_mem
    
    return results


# =============================================================================
# Exercise 3: cProfile Usage - SOLUTION
# =============================================================================

def exercise_3_cprofile():
    """
    Use cProfile for function profiling.
    """
    def compute():
        total = 0
        for i in range(10000):
            total += i * i
        return total
    
    # Profile the function
    profiler = cProfile.Profile()
    profiler.enable()
    
    result = compute()
    
    profiler.disable()
    
    # Get statistics
    stream = StringIO()
    stats = pstats.Stats(profiler, stream=stream)
    stats.sort_stats('cumulative')
    
    # Get top functions
    top_functions = []
    for func, (cc, nc, tt, ct, callers) in stats.stats.items():
        filename, lineno, funcname = func
        top_functions.append({
            'function': funcname,
            'calls': nc,
            'time': tt
        })
    
    top_functions.sort(key=lambda x: x['time'], reverse=True)
    
    return {
        'result': result,
        'top_functions': top_functions[:5]
    }


# =============================================================================
# Exercise 4: Line Profiling - SOLUTION
# =============================================================================

def exercise_4_line_profiling():
    """
    Profile individual lines of code.
    """
    import time
    
    def slow_algorithm(data):
        result = []
        for item in data:
            if item % 2 == 0:
                result.append(item ** 2)
        return sorted(result)
    
    # Simple line timing
    data = list(range(10000))
    
    start = time.perf_counter()
    result = slow_algorithm(data)
    total_time = time.perf_counter() - start
    
    # Estimate line times
    results = {
        'total_time': total_time,
        'result_length': len(result),
        'estimated_loop_time': total_time * 0.7,  # Rough estimate
        'estimated_sort_time': total_time * 0.3,
    }
    
    return results


# =============================================================================
# Exercise 5: Benchmark Suite - SOLUTION
# =============================================================================

def exercise_5_benchmark_suite():
    """
    Create a benchmark suite for comparing implementations.
    """
    import statistics
    
    def benchmark(iterations=100):
        def decorator(func):
            @wraps(func)
            def wrapper(*args, **kwargs):
                times = []
                for _ in range(iterations):
                    start = time.perf_counter()
                    result = func(*args, **kwargs)
                    end = time.perf_counter()
                    times.append(end - start)
                
                wrapper.stats = {
                    'iterations': iterations,
                    'mean': statistics.mean(times),
                    'median': statistics.median(times),
                    'min': min(times),
                    'max': max(times),
                    'stdev': statistics.stdev(times) if len(times) > 1 else 0,
                }
                return result
            return wrapper
        return decorator
    
    @benchmark(iterations=10)
    def test_function():
        return sum(range(1000))
    
    result = test_function()
    
    return {
        'result': result,
        'stats': test_function.stats
    }


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 15 - Profiling Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: Basic Timing")
    result = exercise_1_basic_timing()
    assert result['result'] == 'done'
    assert result['execution_time'] > 0
    print(f"  Execution time: {result['execution_time']:.4f}s")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Memory Profiling")
    result = exercise_2_memory_profiling()
    assert isinstance(result, dict)
    assert 'list_1000' in result
    print(f"  Memory usage: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: cProfile Usage")
    result = exercise_3_cprofile()
    assert isinstance(result, dict)
    assert 'top_functions' in result
    print(f"  Top functions: {result['top_functions'][:3]}")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Line Profiling")
    result = exercise_4_line_profiling()
    assert isinstance(result, dict)
    assert 'total_time' in result
    print(f"  Results: {result}")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Benchmark Suite")
    result = exercise_5_benchmark_suite()
    assert isinstance(result, dict)
    assert 'stats' in result
    print(f"  Stats: {result['stats']}")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
