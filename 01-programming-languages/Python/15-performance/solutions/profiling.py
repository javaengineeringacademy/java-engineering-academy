"""
Module 15: Performance - Profiling Solutions
Practice profiling Python code for performance analysis.
"""

import time
import cProfile
import pstats
from functools import wraps
from io import StringIO


def timer(func):
    """Decorator that prints execution time."""
    @wraps(func)
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()
        print(f"{func.__name__} took {(end - start) * 1000:.2f}ms")
        return result
    return wrapper


class Profiler:
    """Context manager for profiling code blocks."""

    def __init__(self):
        self.profiler = cProfile.Profile()
        self.stats = None

    def __enter__(self):
        self.profiler.enable()
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.profiler.disable()
        stream = StringIO()
        stats = pstats.Stats(self.profiler, stream=stream)
        self.stats = {
            "tottime": stats.total_tt,
            "ncalls": stats.total_calls
        }
        return False

    def get_stats(self):
        """Get profiling statistics."""
        return self.stats or {"tottime": 0, "ncalls": 0}


class MemoryProfiler:
    """Profile memory usage of code blocks."""

    def __init__(self):
        self.start_memory = 0
        self.end_memory = 0
        self.peak_memory = 0

    def __enter__(self):
        import tracemalloc
        tracemalloc.start()
        self.start_memory = tracemalloc.get_traced_memory()[0]
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        import tracemalloc
        current, peak = tracemalloc.get_traced_memory()
        self.end_memory = current
        self.peak_memory = peak
        tracemalloc.stop()
        return False

    def get_report(self):
        """Get memory profiling report."""
        return {
            "current_memory": (self.end_memory - self.start_memory) / 1024,
            "peak_memory": self.peak_memory / 1024
        }


class CallCounter:
    """Track function call statistics."""

    def __init__(self):
        self._stats = {}

    def track(self, func):
        """Decorator to track function calls."""
        @wraps(func)
        def wrapper(*args, **kwargs):
            start = time.time()
            result = func(*args, **kwargs)
            end = time.time()

            if func not in self._stats:
                self._stats[func] = {
                    "calls": 0,
                    "total_time": 0
                }

            self._stats[func]["calls"] += 1
            self._stats[func]["total_time"] += end - start

            return result
        return wrapper

    def get_stats(self, func):
        """Get statistics for a specific function."""
        if func not in self._stats:
            return {"calls": 0, "total_time": 0, "avg_time": 0}

        stats = self._stats[func]
        return {
            "calls": stats["calls"],
            "total_time": stats["total_time"],
            "avg_time": stats["total_time"] / stats["calls"] if stats["calls"] > 0 else 0
        }


class BenchmarkSuite:
    """Benchmark multiple implementations."""

    def __init__(self):
        self._implementations = {}

    def add(self, name, func):
        """Add an implementation to benchmark."""
        self._implementations[name] = func

    def run(self, iterations=100):
        """Run each implementation multiple times."""
        results = {}

        for name, func in self._implementations.items():
            times = []
            for _ in range(iterations):
                start = time.time()
                func()
                end = time.time()
                times.append(end - start)

            results[name] = {
                "mean": sum(times) / len(times),
                "min": min(times),
                "max": max(times),
                "times": times
            }

        return results

    def compare(self):
        """Compare implementations and return the winner."""
        results = self.run()
        return min(results.keys(), key=lambda k: results[k]["mean"])


if __name__ == "__main__":
    print("Testing Profiling Solutions...")

    # Test timer
    @timer
    def slow_function():
        time.sleep(0.01)
        return "done"

    result = slow_function()
    assert result == "done"
    print("✓ Exercise 1 passed: timer decorator works")

    # Test profiler
    with Profiler() as profiler:
        total = sum(range(10000))

    stats = profiler.get_stats()
    assert "tottime" in stats
    assert stats["tottime"] >= 0
    print(f"✓ Exercise 2 passed: profiler captured {stats['tottime']:.4f}s")

    # Test memory profiler
    with MemoryProfiler() as profiler:
        data = [i ** 2 for i in range(10000)]

    report = profiler.get_report()
    assert "current_memory" in report
    assert "peak_memory" in report
    assert report["peak_memory"] > 0
    print(f"✓ Exercise 3 passed: peak memory {report['peak_memory']:.1f}KB")

    # Test call counter
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

    # Test benchmark suite
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

    print("All Profiling solutions passed!")
