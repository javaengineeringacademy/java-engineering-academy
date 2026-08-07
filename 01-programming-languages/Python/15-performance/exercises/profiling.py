"""
Module 15: Performance - Profiling Exercises
============================================
Practice profiling Python code for performance analysis.
"""

import time
import cProfile
import pstats
from functools import wraps

# =============================================================================
# Exercise 1: Timing Decorator (★☆☆☆☆)
# =============================================================================
# TODO: Create decorator that measures execution time

def timer(func):
    """Decorator that prints execution time."""
    # TODO: Implement timing logic
    pass

# Test Cases
@timer
def slow_function():
    time.sleep(0.01)
    return "done"

def test_timer():
    result = slow_function()
    assert result == "done"
    print("✓ Exercise 1 passed: timer decorator works")

# =============================================================================
# Exercise 2: Profiling Context Manager (★★☆☆☆)
# =============================================================================
# TODO: Create context manager for profiling

class Profiler:
    """Context manager for profiling code blocks."""
    # TODO: Use cProfile internally
    # TODO: Provide stats access
    pass

# Test Tests
def test_profiler():
    with Profiler() as profiler:
        total = sum(range(10000))
    
    stats = profiler.get_stats()
    assert "tottime" in stats
    assert stats["tottime"] >= 0
    print(f"✓ Exercise 2 passed: profiler captured {stats['tottime']:.4f}s")

# =============================================================================
# Exercise 3: Memory Profiler (★★★☆☆)
# =============================================================================
# TODO: Profile memory usage of code blocks

class MemoryProfiler:
    """Profile memory usage of code blocks."""
    # TODO: Track memory before and after
    # TODO: Report peak memory usage
    pass

# Test Tests
def test_memory_profiler():
    with MemoryProfiler() as profiler:
        data = [i ** 2 for i in range(10000)]
    
    report = profiler.get_report()
    assert "current_memory" in report
    assert "peak_memory" in report
    assert report["peak_memory"] > 0
    print(f"✓ Exercise 3 passed: peak memory {report['peak_memory']:.1f}KB")

# =============================================================================
# Exercise 4: Function Call Counter (★★★★☆)
# =============================================================================
# TODO: Count function calls and track timing

class CallCounter:
    """Track function call statistics."""
    # TODO: Count calls, total time, average time
    pass

# Test Cases
def test_call_counter():
    counter = CallCounter()
    
    @counter.track
    def compute(n):
        return sum(range(n))
    
    compute(100)
    compute(200)
    compute(300)
    
    stats = counter.get_stats(compute)
    assert stats["calls"] == 3
    assert stats["total_time"] > 0
    assert stats["avg_time"] == stats["total_time"] / 3
    print(f"✓ Exercise 4 passed: {stats['calls']} calls tracked")

# =============================================================================
# Exercise 5: Performance Benchmark Suite (★★★★★)
# =============================================================================
# TODO: Create benchmark suite for comparing implementations

class BenchmarkSuite:
    """Benchmark multiple implementations."""
    # TODO: Run each implementation multiple times
    # TODO: Calculate statistics (mean, median, std)
    # TODO: Compare results
    pass

# Test Cases
def test_benchmark_suite():
    suite = BenchmarkSuite()
    
    def impl_a():
        return sorted([3, 1, 4, 1, 5, 9, 2, 6])
    
    def impl_b():
        data = [3, 1, 4, 1, 5, 9, 2, 6]
        data.sort()
        return data
    
    suite.add("sorted()", impl_a)
    suite.add("list.sort()", impl_b)
    
    results = suite.run(iterations=100)
    
    assert "sorted()" in results
    assert "list.sort()" in results
    assert results["sorted()"]["mean"] > 0
    winner = suite.compare()
    assert winner in ["sorted()", "list.sort()"]
    print(f"✓ Exercise 5 passed: winner is '{winner}'")

if __name__ == "__main__":
    print("Running Profiling Exercises...")
    print("=" * 50)
    test_timer()
    test_profiler()
    test_memory_profiler()
    test_call_counter()
    test_benchmark_suite()
    print("=" * 50)
    print("All tests passed!")
