# Python Performance

Profiling, optimization, and common bottlenecks.

## Profiling Tools

```python
# cProfile — function-level profiling
import cProfile
cProfile.run('my_function()')

# timeit — micro-benchmarks
import timeit
timeit.timeit('sum(range(1000))', number=10000)

# line_profiler — line-by-line (pip install line_profiler)
# @profile
# def my_function(): ...
```

## Data Structure Performance

| Operation | list | dict | set | deque |
|-----------|------|------|-----|-------|
| Index | O(1) | — | — | O(n) |
| Append | O(1) | — | — | O(1) |
| Pop end | O(1) | — | — | O(1) |
| Pop front | O(n) | — | — | O(1) |
| Lookup | O(n) | O(1) | O(1) | O(n) |
| Insert | O(n) | — | — | O(1) |

## Common Optimizations

```python
# Use set for membership testing
if item in large_list:    # O(n)
if item in large_set:     # O(1)

# Use deque for queue operations
from collections import deque
queue = deque()
queue.append(item)    # O(1)
queue.popleft()       # O(1)

# Use generator for large data
sum(x**2 for x in range(10**7))  # Memory efficient

# Use join for string concatenation
result = "".join(words)  # O(n)

# Use f-strings over format
f"{name} is {age}"  # Fastest

# Use defaultdict/setdefault
from collections import defaultdict
d = defaultdict(list)
d[key].append(value)  # No key check needed
```

## Memory Optimization

```python
# Use __slots__ for classes
class Point:
    __slots__ = ('x', 'y')
    def __init__(self, x, y):
        self.x = x
        self.y = y

# Use array for numeric arrays
from array import array
arr = array('i', [1, 2, 3])  # 4 bytes per int vs 28 for list

# Use named tuples for records
from collections import namedtuple
Point = namedtuple('Point', ['x', 'y'])

# Use sys.getsizeof to check sizes
import sys
sys.getsizeof([])         # 56 bytes
sys.getsizeof([1, 2, 3])  # 88 bytes
```

## Concurrency

```python
# I/O-bound: use asyncio or threading
# CPU-bound: use multiprocessing
# GIL prevents true threading for CPU work

import asyncio
async def fetch():
    await asyncio.sleep(1)  # Releases GIL

from concurrent.futures import ProcessPoolExecutor
with ProcessPoolExecutor() as executor:
    executor.submit(cpu_heavy_task)
```

## C Extensions

```python
# NumPy for numerical computation
import numpy as np
arr = np.array([1, 2, 3])
result = arr * 2  # 100x faster than list

# Cython for C-speed Python
# Use C extensions for hot paths
```

## Quick Wins

1. Use `collections.Counter` over manual counting
2. Use `itertools` for iteration patterns
3. Use `functools.lru_cache` for memoization
4. Avoid global variables in hot loops
5. Use list comprehensions over map/filter
