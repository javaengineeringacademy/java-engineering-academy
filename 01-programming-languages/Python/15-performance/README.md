# Python Performance

## Why Performance Matters

Every Python application eventually faces performance constraints — slow APIs, memory-hungry processes, or unresponsive user interfaces. Understanding how to profile, benchmark, and optimize Python code is critical for building applications that scale and meet user expectations. Without performance knowledge, you'd optimize blindly, waste time on non-bottlenecks, and miss the improvements that actually matter.

Without performance optimization skills, you'd either accept slow code as "good enough" or make changes that don't address the real bottlenecks. That's why performance optimization exists — it provides the tools and techniques to measure, analyze, and improve your code's speed and resource usage systematically, ensuring your applications can handle real-world load.

## What You'll Learn

By the end of this module, you'll be able to:

- Profile Python code to identify performance bottlenecks
- Write meaningful benchmarks that measure real improvements
- Apply optimization techniques from algorithmic to system-level
- Leverage C extensions, Cython, and NumPy for performance-critical code
- Make informed decisions about when to optimize and when to accept trade-offs

## Table of Contents

- [Profiling](#profiling)
- [Benchmarking](#benchmarking)
- [Optimization Techniques](#optimization-techniques)
- [C Extensions](#c-extensions)
- [Cython](#cython)
- [NumPy](#numpy)
- [Performance Best Practices](#performance-best-practices)

---

## Profiling

### cProfile

```python
import cProfile
import pstats

def slow_function():
    total = 0
    for i in range(1000000):
        total += i ** 2
    return total

def another_function():
    return sum(range(1000000))

# Profile a function
cProfile.run('slow_function()')

# Detailed profiling
profiler = cProfile.Profile()
profiler.enable()
slow_function()
another_function()
profiler.disable()

# Print stats
stats = pstats.Stats(profiler)
stats.sort_stats('cumulative')
stats.print_stats(10)  # Top 10 functions

# Save and analyze
stats.dump_stats('profile_results.prof')
# Use snakeviz to visualize: snakeviz profile_results.prof
```

### line_profiler

```python
# pip install line_profiler

from line_profiler import profile

@profile
def slow_function():
    total = 0
    for i in range(1000000):
        total += i ** 2
    return total

# Run: kernprof -l -v script.py
# Output:
# Line #      Hits         Time  Per Hit   % Time  Line Contents
# ==============================================================
#      3                                           def slow_function():
#      4         1          0.0      0.0      0.0      total = 0
#      5   1000000     500000.0      0.5     80.0      for i in range(1000000):
#      6   1000000     125000.0      0.1     20.0          total += i ** 2
#      7         1          0.0      0.0      0.0      return total
```

### py-spy

```python
# pip install py-spy

# Record CPU profile
# py-spy record -o profile.svg -- python script.py

# Top view
# py-spy top -- python script.py

# Dump all thread stacks
# py-spy dump --pid 12345
```

### Memory Profiling

```python
# Using tracemalloc
import tracemalloc

tracemalloc.start()

# Your code
data = [i ** 2 for i in range(100000)]

snapshot = tracemalloc.take_snapshot()
top_stats = snapshot.statistics('lineno')

print("[ Top 10 ]")
for stat in top_stats[:10]:
    print(stat)

# Using memory_profiler
# pip install memory_profiler

from memory_profiler import profile

@profile
def my_function():
    a = [i for i in range(10000)]
    b = [i ** 2 for i in range(10000)]
    del b
    return a

# Run: python -m memory_profiler script.py
```

---

## Benchmarking

### timeit

```python
import timeit

# Basic timing
time = timeit.timeit('sum(range(1000))', number=10000)
print(f"Time: {time:.4f}s")

# Timing with setup
time = timeit.timeit(
    'sum(data)',
    setup='data = list(range(1000))',
    number=10000
)
print(f"Time: {time:.4f}s")

# Compare approaches
list_comp_time = timeit.timeit(
    '[x**2 for x in range(1000)]',
    number=10000
)
generator_time = timeit.timeit(
    'list(x**2 for x in range(1000))',
    number=10000
)
print(f"List comprehension: {list_comp_time:.4f}s")
print(f"Generator: {generator_time:.4f}s")

# timeit module
timeit.main(['-n', '10000', '-r', '5', 'sum(range(1000))'])
```

### Benchmarking Libraries

```python
# Using pytest-benchmark
# pip install pytest-benchmark

def test_list_comprehension(benchmark):
    result = benchmark(lambda: [x**2 for x in range(1000)])
    assert len(result) == 1000

def test_generator(benchmark):
    result = benchmark(lambda: list(x**2 for x in range(1000)))
    assert len(result) == 1000

# Run: pytest benchmarks.py --benchmark-compare

# Using pyperf
# pip install pyperf

import pyperf

runner = pyperf.Runner()
runner.timeit('sum(range(1000))')
runner.timeit('list(x**2 for x in range(1000))')
```

### Custom Benchmarking

```python
import time
from contextlib import contextmanager

@contextmanager
def timer(name):
    start = time.perf_counter()
    yield
    end = time.perf_counter()
    print(f"{name}: {end - start:.6f}s")

# Usage
with timer("List comprehension"):
    result = [x**2 for x in range(100000)]

with timer("Generator"):
    result = list(x**2 for x in range(100000))

# Statistical benchmarking
import statistics

def benchmark(func, runs=10):
    times = []
    for _ in range(runs):
        start = time.perf_counter()
        func()
        end = time.perf_counter()
        times.append(end - start)

    return {
        'mean': statistics.mean(times),
        'median': statistics.median(times),
        'stdev': statistics.stdev(times) if len(times) > 1 else 0,
        'min': min(times),
        'max': max(times),
    }

result = benchmark(lambda: sum(range(100000)))
print(f"Mean: {result['mean']:.6f}s")
print(f"Median: {result['median']:.6f}s")
print(f"Std dev: {result['stdev']:.6f}s")
```

---

## Optimization Techniques

### Algorithm Optimization

```python
# Bad: O(n²) algorithm
def find_duplicates_bad(lst):
    duplicates = []
    for i in range(len(lst)):
        for j in range(i + 1, len(lst)):
            if lst[i] == lst[j] and lst[i] not in duplicates:
                duplicates.append(lst[i])
    return duplicates

# Good: O(n) algorithm
def find_duplicates_good(lst):
    seen = set()
    duplicates = set()
    for item in lst:
        if item in seen:
            duplicates.add(item)
        seen.add(item)
    return list(duplicates)

# Benchmarking
import timeit

lst = list(range(1000)) * 2
time_bad = timeit.timeit(lambda: find_duplicates_bad(lst), number=100)
time_good = timeit.timeit(lambda: find_duplicates_good(lst), number=100)
print(f"Bad: {time_bad:.4f}s")
print(f"Good: {time_good:.4f}s")
```

### Data Structure Optimization

```python
import timeit
from collections import deque, defaultdict, Counter

# List vs deque for left operations
list_time = timeit.timeit(
    'lst.insert(0, 1)',
    setup='lst = list(range(10000))',
    number=10000
)
deque_time = timeit.timeit(
    'dq.appendleft(1)',
    setup='from collections import deque; dq = deque(range(10000))',
    number=10000
)
print(f"List insert(0): {list_time:.4f}s")
print(f"Deque appendleft: {deque_time:.4f}s")

# dict.get vs defaultdict
text = "hello world " * 10000

dict_time = timeit.timeit(
    '''d = {}
    for c in text:
        d[c] = d.get(c, 0) + 1''',
    number=1000
)
dd_time = timeit.timeit(
    '''from collections import defaultdict
    d = defaultdict(int)
    for c in text:
        d[c] += 1''',
    number=1000
)
print(f"dict.get: {dict_time:.4f}s")
print(f"defaultdict: {dd_time:.4f}s")
```

### String Optimization

```python
import timeit

# String concatenation (bad)
def concat_bad():
    result = ""
    for i in range(1000):
        result += str(i)
    return result

# Join (good)
def concat_good():
    return "".join(str(i) for i in range(1000))

# f-string (good)
def concat_fstring():
    return "".join(f"{i}" for i in range(1000))

time_bad = timeit.timeit(concat_bad, number=10000)
time_good = timeit.timeit(concat_good, number=10000)
time_fstring = timeit.timeit(concat_fstring, number=10000)

print(f"Concat: {time_bad:.4f}s")
print(f"Join: {time_good:.4f}s")
print(f"f-string: {time_fstring:.4f}s")
```

### Caching

```python
from functools import lru_cache, cache
import time

# LRU Cache
@lru_cache(maxsize=128)
def fibonacci_lru(n):
    if n < 2:
        return n
    return fibonacci_lru(n-1) + fibonacci_lru(n-2)

# Infinite cache
@cache
def fibonacci_cache(n):
    if n < 2:
        return n
    return fibonacci_cache(n-1) + fibonacci_cache(n-2)

# Benchmarking
start = time.time()
fibonacci_lru(100)
print(f"LRU Cache: {time.time() - start:.4f}s")

start = time.time()
fibonacci_cache(100)
print(f"Infinite Cache: {time.time() - start:.4f}s")
```

---

## C Extensions

### Writing C Extensions

```python
# mymodule.c
"""
#include <Python.h>

static PyObject* my_function(PyObject* self, PyObject* args) {
    int x;
    if (!PyArg_ParseTuple(args, "i", &x)) {
        return NULL;
    }
    return PyLong_FromLong(x * x);
}

static PyMethodDef methods[] = {
    {"my_function", my_function, METH_VARARGS, "Square a number"},
    {NULL, NULL, 0, NULL}
};

static struct PyModuleDef module = {
    PyModuleDef_HEAD_INIT,
    "mymodule",
    NULL,
    -1,
    methods
};

PyMODINIT_FUNC PyInit_mymodule(void) {
    return PyModule_Create(&module);
}
"""

# setup.py
"""
from setuptools import setup, Extension

module = Extension('mymodule', sources=['mymodule.c'])
setup(
    name='mymodule',
    version='1.0',
    ext_modules=[module]
)
"""

# Build: python setup.py build_ext --inplace

# Usage
import mymodule
print(mymodule.my_function(5))  # 25
```

### Using ctypes

```python
import ctypes

# Call C library functions
libc = ctypes.CDLL("libc.so.6")
print(libc.time(None))

# Call custom C code
# Compile: gcc -shared -o libmylib.so -fPIC mylib.c

# mylib.c
"""
int square(int x) {
    return x * x;
}
"""

# Python
lib = ctypes.CDLL("./libmylib.so")
lib.square.argtypes = [ctypes.c_int]
lib.square.restype = ctypes.c_int
print(lib.square(5))  # 25
```

---

## Cython

### Basic Cython

```python
# mymodule.pyx
"""
def fibonacci(int n):
    cdef int a = 0
    cdef int b = 1
    cdef int i
    for i in range(n):
        a, b = b, a + b
    return a
"""

# setup.py
"""
from setuptools import setup
from Cython.Build import cythonize

setup(
    ext_modules=cythonize("mymodule.pyx")
)
"""

# Build: python setup.py build_ext --inplace

# Usage
import mymodule
print(mymodule.fibonacci(100))
```

### Cython with Type Annotations

```python
# mymodule.pyx
"""
# cython: language_level=3

def fibonacci(int n):
    cdef int a = 0, b = 1, i
    for i in range(n):
        a, b = b, a + b
    return a

def sum_list(list lst):
    cdef int total = 0
    cdef int x
    for x in lst:
        total += x
    return total
"""
```

---

## NumPy

### Vectorized Operations

```python
import numpy as np
import time

# Python loop (slow)
def python_sum():
    total = 0
    for i in range(1000000):
        total += i ** 2
    return total

# NumPy (fast)
def numpy_sum():
    arr = np.arange(1000000)
    return np.sum(arr ** 2)

# Benchmarking
start = time.time()
python_sum()
print(f"Python: {time.time() - start:.4f}s")

start = time.time()
numpy_sum()
print(f"NumPy: {time.time() - start:.4f}s")

# Typical results:
# Python: 0.1500s
# NumPy: 0.0020s
```

### NumPy Operations

```python
import numpy as np

# Array creation
arr = np.array([1, 2, 3, 4, 5])
arr = np.arange(0, 10, 0.5)
arr = np.zeros((10, 10))
arr = np.ones((10, 10))
arr = np.random.rand(10, 10)

# Mathematical operations
arr = np.array([1, 2, 3, 4, 5])
print(np.sum(arr))      # 15
print(np.mean(arr))     # 3.0
print(np.std(arr))      # 1.414...
print(np.max(arr))      # 5
print(np.min(arr))      # 1

# Array operations
a = np.array([1, 2, 3])
b = np.array([4, 5, 6])
print(a + b)    # [5 7 9]
print(a * b)    # [4 10 18]
print(np.dot(a, b))  # 32

# Broadcasting
arr = np.array([[1, 2, 3], [4, 5, 6]])
print(arr + 10)  # [[11 12 13] [14 15 16]]
```

---

## Performance Best Practices

### General Tips

```python
# 1. Use built-in functions and libraries
# Bad
def sum_list(lst):
    total = 0
    for x in lst:
        total += x
    return total

# Good
def sum_list(lst):
    return sum(lst)

# 2. Use list comprehensions
# Bad
result = []
for x in range(1000):
    result.append(x ** 2)

# Good
result = [x ** 2 for x in range(1000)]

# 3. Use generators for large datasets
# Bad
def process_large():
    return [process(x) for x in range(1000000)]

# Good
def process_large():
    return (process(x) for x in range(1000000))

# 4. Use local variables
# Bad
def process():
    import math
    return [math.sqrt(x) for x in range(1000)]

# Good
def process():
    from math import sqrt
    return [sqrt(x) for x in range(1000)]

# 5. Avoid global variables
# Bad
counter = 0
def increment():
    global counter
    counter += 1

# Good
def increment(counter):
    return counter + 1
```

### Memory Optimization

```python
# 1. Use slots
class Point:
    __slots__ = ('x', 'y')
    def __init__(self, x, y):
        self.x = x
        self.y = y

# 2. Use array for homogeneous data
from array import array
arr = array('i', range(1000000))

# 3. Use numpy for numerical data
import numpy as np
arr = np.arange(1000000)

# 4. Use generators
def process():
    for i in range(1000000):
        yield process(i)

# 5. Delete large objects
large_data = process_huge_file()
result = analyze(large_data)
del large_data
```

### Profiling Workflow

```python
# 1. Profile first
import cProfile
cProfile.run('your_function()', 'profile.prof')

# 2. Analyze results
import pstats
stats = pstats.Stats('profile.prof')
stats.sort_stats('cumulative')
stats.print_stats(20)

# 3. Optimize hotspots
# Focus on functions that take the most time

# 4. Benchmark improvements
import timeit
time = timeit.timeit('optimized_function()', number=1000)

# 5. Verify correctness
import unittest
# Run tests to ensure optimization doesn't break functionality
```

---

## Summary

Python performance optimization:

1. **Profile first** - Find actual bottlenecks
2. **Use built-ins** - They're implemented in C
3. **Choose right data structures** - deque for queues, set for lookups
4. **Cache expensive computations** - lru_cache
5. **Use NumPy** - Vectorized operations are fast
6. **Consider Cython** - For CPU-critical code
7. **Use generators** - For memory efficiency
8. **Optimize algorithms** - O(n) vs O(n²) matters

## Production Checklist

- [ ] Profile before optimizing; never optimize without data
- [ ] Use `cProfile` or `py-spy` to identify actual bottlenecks
- [ ] Benchmark with `timeit` to verify improvements are real
- [ ] Use built-in functions (`sum`, `map`, `filter`) over manual loops
- [ ] Prefer list comprehensions for simple transformations
- [ ] Use generators for large datasets to reduce memory pressure
- [ ] Apply `@lru_cache` or `@cache` for expensive pure functions
- [ ] Use `deque` for queue operations; `set` for membership testing
- [ ] Replace string concatenation with `join()` or f-strings
- [ ] Consider Cython or C extensions for hot paths after profiling

## Maturity Levels

| Level | Description |
|-------|-------------|
| **Beginner** | Uses `timeit` for simple benchmarks; understands built-ins are faster |
| **Intermediate** | Profiles with `cProfile`; applies caching and appropriate data structures |
| **Advanced** | Writes Cython extensions; uses NumPy for vectorized operations; benchmarks statistically |
| **Expert** | Writes C extensions; tunes memory allocation; contributes to PyPy or CPython JIT |

## Common Myths

1. **"Python is too slow for production"** — Profile first; bottlenecks are often in I/O or algorithms, not Python itself
2. **"Optimizing everything improves performance"** — Focus on hot paths; premature optimization is the root of all evil
3. **"More cores = faster Python"** — GIL limits CPU-bound parallelism; use multiprocessing or C extensions
4. **"Caching always helps"** — Cache invalidation is complex; profile to confirm cache hits
5. **"NumPy is always faster"** — Overhead for small arrays; benchmark with your actual data
6. **"Profiling in production is unsafe"** — Use `py-spy` (sampling) or `Austin` for low-overhead production profiling

## One-Minute Revision

- **Profile first**: `cProfile.run()` or `py-spy` to find actual bottlenecks
- **Built-ins**: `sum()`, `min()`, `max()`, `sorted()` implemented in C; always faster
- **List comprehensions**: Faster than `for` loops with `append()`; single allocation
- **Generators**: Constant memory; `(expr for x in iter)`; use for large datasets
- **Caching**: `@lru_cache(maxsize=N)` or `@cache` (Python 3.9+); pure functions only
- **Data structures**: `deque` for O(1) left ops; `set` for O(1) lookups; `Counter` for counting
- **String optimization**: `"".join(parts)` not `+=`; f-strings over `str.format()`
- **NumPy**: Vectorized operations; 10-100x faster for numerical computations
- **Cython**: Compile Python to C; add type declarations for 10-100x speedup
- **C extensions**: Ultimate performance; release GIL for parallel execution
- **Statistical benchmarking**: Run multiple iterations; report mean, median, stdev
