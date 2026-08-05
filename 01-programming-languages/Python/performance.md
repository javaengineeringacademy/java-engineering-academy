# Python Performance

## Profiling

### cProfile
```python
import cProfile

def my_function():
    total = 0
    for i in range(1000000):
        total += i
    return total

# Profile function
cProfile.run('my_function()')

# Profile specific code block
profiler = cProfile.Profile()
profiler.enable()
# Code to profile
profiler.disable()
profiler.print_stats(sort='cumulative')
```

### line_profiler
```bash
pip install line_profiler
```

```python
@profile  # Add decorator
def slow_function():
    import time
    time.sleep(1)
    return sum(range(1000000))
```

```bash
kernprof -l -v myscript.py
```

### memory_profiler
```bash
pip install memory_profiler
```

```python
@profile
def memory_heavy():
    data = [i for i in range(1000000)]
    return sum(data)
```

```bash
python -m memory_profiler myscript.py
```

### py-spy
```bash
pip install py-spy

# Profile running process
py-spy top --pid 12345

# Generate flame graph
py-spy record -o profile.svg --pid 12345
```

## Async Programming

### asyncio
```python
import asyncio

async def fetch_data(url):
    await asyncio.sleep(1)  # Simulate I/O
    return f"Data from {url}"

async def main():
    tasks = [
        fetch_data("http://example.com/1"),
        fetch_data("http://example.com/2"),
    ]
    results = await asyncio.gather(*tasks)
    return results

asyncio.run(main())
```

### aiohttp
```python
import aiohttp
import asyncio

async def fetch(session, url):
    async with session.get(url) as response:
        return await response.text()

async def main():
    async with aiohttp.ClientSession() as session:
        html = await fetch(session, 'http://python.org')
```

## Cython

```python
# example.pyx
def fibonacci(int n):
    cdef int a = 0, b = 1
    for i in range(n):
        a, b = b, a + b
    return a
```

```python
# setup.py
from setuptools import setup
from Cython.Build import cythonize

setup(ext_modules=cythonize("example.pyx"))
```

```bash
python setup.py build_ext --inplace
```

## PyPy

Alternative Python interpreter with JIT compilation.

```bash
# Install PyPy
# Download from https://pypy.org

# Run with PyPy
pypy3 myscript.py

# Benchmark comparison
python -m timeit "sum(range(1000000))"
pypy3 -m timeit "sum(range(1000000))"
```

## Optimization Techniques

### Built-in Functions
```python
# Slow
result = []
for i in range(1000000):
    result.append(i * 2)

# Fast
result = list(map(lambda x: x * 2, range(1000000)))

# Faster
result = [i * 2 for i in range(1000000)]
```

### Data Structure Selection
```python
# Slow - O(n) lookup
my_list = [1, 2, 3, 4, 5]
if 3 in my_list:  # O(n)
    pass

# Fast - O(1) lookup
my_set = {1, 2, 3, 4, 5}
if 3 in my_set:  # O(1)
    pass
```

### String Concatenation
```python
# Slow
result = ""
for s in strings:
    result += s

# Fast
result = "".join(strings)
```

### Generator Expressions
```python
# Memory efficient
sum(i**2 for i in range(1000000))

# vs list comprehension (uses more memory)
sum([i**2 for i in range(1000000)])
```

## NumPy for Numerical Computing

```python
import numpy as np

# Slow Python
result = sum([i**2 for i in range(1000000)])

# Fast NumPy
arr = np.arange(1000000)
result = np.sum(arr**2)
```

## Benchmarking

```python
import timeit

# Benchmark code snippet
time = timeit.timeit(
    'sum(range(1000000))',
    number=100
)
print(f"Time: {time:.4f}s")

# Compare approaches
time1 = timeit.timeit('[i**2 for i in range(1000)]', number=1000)
time2 = timeit.timeit('list(map(lambda x: x**2, range(1000)))', number=1000)
print(f"List comp: {time1:.4f}s, Map: {time2:.4f}s")
```

## Best Practices

1. Profile before optimizing
2. Use appropriate data structures
3. Leverage built-in functions
4. Consider Cython for CPU-bound code
5. Use async for I/O-bound tasks
6. Cache expensive computations
7. Avoid premature optimization
