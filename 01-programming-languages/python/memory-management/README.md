# Python Memory Management

A comprehensive guide to Python's memory management, garbage collection, and profiling.

## Table of Contents

- [Memory Management Overview](#memory-management-overview)
- [Reference Counting](#reference-counting)
- [Generational Garbage Collection](#generational-garbage-collection)
- [Memory Allocation](#memory-allocation)
- [Memory Profiling](#memory-profiling)
- [tracemalloc](#tracemalloc)
- [objgraph](#objgraph)
- [Memory Optimization](#memory-optimization)

---

## Memory Management Overview

Python uses automatic memory management through:

1. **Reference counting** - Primary mechanism
2. **Generational garbage collector** - Handles circular references
3. **Memory pools** - Efficient allocation of small objects

```python
import sys

# Every object has a reference count
a = [1, 2, 3]
print(sys.getrefcount(a))  # 2 (a + getrefcount argument)

b = a  # Another reference
print(sys.getrefcount(a))  # 3

del b  # Remove reference
print(sys.getrefcount(a))  # 2
```

---

## Reference Counting

### How Reference Counting Works

```python
import sys

class ReferenceCountDemo:
    def __init__(self, name):
        self.name = name

    def __del__(self):
        print(f"Deleting {self.name}")

# Creating an object (refcount = 1)
obj = ReferenceCountDemo("obj1")
print(sys.getrefcount(obj))  # 2

# Assigning to another variable (refcount += 1)
obj2 = obj
print(sys.getrefcount(obj))  # 3

# Adding to a container (refcount += 1)
container = [obj]
print(sys.getrefcount(obj))  # 4

# Removing references (refcount -= 1)
del obj2
print(sys.getrefcount(obj))  # 3

container.remove(obj)
print(sys.getrefcount(obj))  # 2

# When refcount reaches 0, object is deleted
del obj  # Deleting obj1
```

### Circular References

```python
import gc

# Circular reference
class Node:
    def __init__(self, name):
        self.name = name
        self.parent = None
        self.children = []

    def __del__(self):
        print(f"Deleting {self.name}")

# Create circular reference
parent = Node("parent")
child = Node("child")
parent.children.append(child)
child.parent = parent  # Circular reference!

# Even with del, objects aren't immediately freed
del parent
del child
# Objects still exist due to circular reference

# Force garbage collection
gc.collect()
# Now objects are deleted
```

### Reference Count Operations

```python
import sys
import ctypes

def get_refcount(address):
    """Get reference count of object at memory address."""
    return ctypes.c_long.from_address(address).value

# Create object
a = [1, 2, 3]
print(f"Initial refcount: {sys.getrefcount(a)}")

# Increase references
b = a
c = a
print(f"After assignments: {sys.getrefcount(a)}")

# Decrease references
del b
del c
print(f"After deletions: {sys.getrefcount(a)}")

# Check memory address
print(f"Memory address: {id(a)}")
print(f"Refcount at address: {get_refcount(id(a))}")
```

---

## Generational Garbage Collection

### GC Generations

```python
import gc

# Three generations
# Generation 0: New objects (most frequently collected)
# Generation 1: Survived one collection
# Generation 2: Survived two collections (long-lived)

# Get thresholds
print(gc.get_threshold())  # (700, 10, 10)
# 700 allocations trigger Gen 0 collection
# 10 Gen 0 collections trigger Gen 1
# 10 Gen 1 collections trigger Gen 2

# Get counts
print(gc.get_count())  # Objects in each generation

# Set custom thresholds
gc.set_threshold(500, 5, 5)  # More frequent collections

# Force collection
gc.collect()
gc.collect(generation=0)  # Only collect Gen 0
```

### GC Debugging

```python
import gc

# Enable debugging
gc.set_debug(gc.DEBUG_LEAK)  # Report unreachable objects
gc.set_debug(gc.DEBUG_STATS)  # Print collection statistics
gc.set_debug(gc.DEBUG_COLLECTABLE)  # Print collectable objects
gc.set_debug(gc.DEBUG_UNCOLLECTABLE)  # Print uncollectable objects

# Find leaks
gc.set_debug(gc.DEBUG_LEAK)
gc.collect()
print(f"Leaked objects: {len(gc.garbage)}")
for obj in gc.garbage:
    print(f"  {type(obj).__name__}: {obj}")

# Track allocations
gc.set_debug(gc.DEBUG_STATS)
gc.collect()
```

### Weak References

```python
import weakref

class ExpensiveObject:
    def __init__(self, name):
        self.name = name

    def __del__(self):
        print(f"Deleting {self.name}")

# Create weak reference
obj = ExpensiveObject("data")
weak_ref = weakref.ref(obj)

print(weak_ref())  # ExpensiveObject data
print(weak_ref() is obj)  # True

# Delete object
del obj
print(weak_ref())  # None

# Weak reference with callback
def callback(ref):
    print("Object was deleted")

obj = ExpensiveObject("data")
weak_ref = weakref.ref(obj, callback)

del obj  # Output: Object was deleted, Deleting data

# WeakValueDictionary
class Data:
    def __init__(self, value):
        self.value = value

cache = weakref.WeakValueDictionary()
obj = Data(42)
cache['key'] = obj

print(cache['key'].value)  # 42
del obj
print('key' in cache)  # False

# WeakSet
class Tracked:
    def __init__(self, name):
        self.name = name

instances = weakref.WeakSet()
obj = Tracked("item")
instances.add(obj)

print(len(instances))  # 1
del obj
print(len(instances))  # 0
```

---

## Memory Allocation

### Memory Pools

```python
import sys

# Small objects (<= 512 bytes) use memory pools
# Pools are organized by size class (8 bytes to 512 bytes)

#查看对象大小
print(sys.getsizeof(0))          # 24 bytes (small int)
print(sys.getsizeof(""))         # 49 bytes (empty string)
print(sys.getsizeof([]))         # 56 bytes (empty list)
print(sys.getsizeof({}))         # 64 bytes (empty dict)
print(sys.getsizeof(set()))      # 216 bytes (empty set)

# Size classes
for i in range(8, 513, 8):
    print(f"Size {i}: {sys.getsizeof(b'x' * i)} bytes")
```

### Object-Specific Allocation

```python
import sys

# Integer caching (-5 to 256)
a = 256
b = 256
print(a is b)  # True (same object)

a = 257
b = 257
print(a is b)  # False (different objects)

# String interning
a = "hello"
b = "hello"
print(a is b)  # True

a = "hello world"
b = "hello world"
print(a is b)  # False (not interned)

# Force interning
import sys
a = sys.intern("hello world")
b = sys.intern("hello world")
print(a is b)  # True

# Tuple optimization
a = (1, 2, 3)
b = (1, 2, 3)
print(a is b)  # May be True (immutable, small tuples)

# List allocation
import time
lst = []
for i in range(1000):
    lst.append(i)
    # Python over-allocates to reduce reallocations
```

---

## Memory Profiling

### Using tracemalloc

```python
import tracemalloc
import sys

# Start tracing
tracemalloc.start()

# Your code
data = [i ** 2 for i in range(100000)]

# Get current and peak memory
current, peak = tracemalloc.get_traced_memory()
print(f"Current: {current / 1024:.2f} KB")
print(f"Peak: {peak / 1024:.2f} KB")

# Take snapshot
snapshot = tracemalloc.take_snapshot()

# Display top memory consumers
top_stats = snapshot.statistics('lineno')
print("[ Top 10 ]")
for stat in top_stats[:10]:
    print(stat)

# Compare snapshots
tracemalloc.start()
snapshot1 = tracemalloc.take_snapshot()

# More code
data2 = [i ** 3 for i in range(100000)]

snapshot2 = tracemalloc.take_snapshot()
top_stats = snapshot2.compare_to(snapshot1, 'lineno')

print("[ Top 10 differences ]")
for stat in top_stats[:10]:
    print(stat)

tracemalloc.stop()
```

### Using memory_profiler

```python
# pip install memory_profiler

from memory_profiler import profile

@profile
def my_function():
    a = [i for i in range(10000)]
    b = [i ** 2 for i in range(10000)]
    del b
    return a

# Run: python -m memory_profiler script.py
# Output:
# Line #    Mem usage    Increment   Line Contents
# ================================================
#      3     45.0 MiB     45.0 MiB   @profile
#      4                         def my_function():
#      5     48.0 MiB      3.0 MiB       a = [i for i in range(10000)]
#      6     52.0 MiB      4.0 MiB       b = [i ** 2 for i in range(10000)]
#      7     48.0 MiB     -4.0 MiB       del b
#      8     48.0 MiB      0.0 MiB       return a
```

### Using pympler

```python
from pympler import asizeof, tracker

# Get deep size of objects
asizeof.asizeof([1, 2, 3])  # Size including all nested objects
asizeof.deepsize([1, 2, 3])  # Detailed breakdown

# Track memory changes
tr = tracker.SummaryTracker()

# Your code
data1 = [i for i in range(10000)]
tr.print_diff()  # Show what changed

data2 = {i: i**2 for i in range(10000)}
tr.print_diff()  # Show what changed

# Get object details
from pympler import mofify
print(mofify.asizeof([1, 2, 3]))
```

---

## tracemalloc

### Basic Usage

```python
import tracemalloc

# Start tracing
tracemalloc.start()

# Your code here
data = [i ** 2 for i in range(100000)]

# Get memory usage
current, peak = tracemalloc.get_traced_memory()
print(f"Current: {current / 1024 / 1024:.2f} MB")
print(f"Peak: {peak / 1024 / 1024:.2f} MB")

# Stop tracing
tracemalloc.stop()
```

### Taking Snapshots

```python
import tracemalloc

tracemalloc.start()

# Take snapshot at specific point
snapshot1 = tracemalloc.take_snapshot()

# Do more work
data = [i ** 2 for i in range(100000)]

# Take another snapshot
snapshot2 = tracemalloc.take_snapshot()

# Compare snapshots
stats = snapshot2.compare_to(snapshot1, 'lineno')
print("[ Top 10 differences ]")
for stat in stats[:10]:
    print(stat)
```

### Analyzing Snapshots

```python
import tracemalloc

tracemalloc.start()

# Your code
data = [i ** 2 for i in range(100000)]

snapshot = tracemalloc.take_snapshot()

# Filter by filename
stats = snapshot.statistics('filename')
print("[ By filename ]")
for stat in stats[:10]:
    print(stat)

# Filter by lineno
stats = snapshot.statistics('lineno')
print("[ By line number ]")
for stat in stats[:10]:
    print(stat)

# Filter by traceback
stats = snapshot.statistics('traceback')
print("[ By traceback ]")
for stat in stats[:10]:
    print(stat)

# Filter specific files
stats = snapshot.statistics('lineno', cumulative=False)
stats = [s for s in stats if '/usr/lib/python3' not in s.traceback[0].filename]
```

### Practical Example

```python
import tracemalloc
import linecache

def display_top(snapshot, key_type='lineno', limit=10):
    top_stats = snapshot.statistics(key_type)

    print(f"\n{'=' * 80}")
    print(f"Top {limit} lines")
    print(f"{'=' * 80}")
    for index, stat in enumerate(top_stats[:limit], 1):
        frame = stat.traceback[0]
        print(f"#{index}: {frame.filename}:{frame.lineno}: {stat.size / 1024:.1f} KiB")
        line = linecache.getline(frame.filename, frame.lineno).strip()
        if line:
            print(f"    {line}")

    other = top_stats[limit:]
    if other:
        size = sum(stat.size for stat in other)
        print(f"Other {len(other)} lines: {size / 1024:.1f} KiB")

    total = sum(stat.size for stat in top_stats)
    print(f"\nTotal: {total / 1024:.1f} KiB")

# Usage
tracemalloc.start()

# Your code
data = [i ** 2 for i in range(100000)]

snapshot = tracemalloc.take_snapshot()
display_top(snapshot)
```

---

## objgraph

### Basic Usage

```python
import objgraph

# Show most common types
objgraph.show_most_common_types(limit=10)

# Show growth over time
objgraph.show_growth(limit=10)

# Show backrefs to an object
class MyClass:
    def __init__(self):
        self.data = [1, 2, 3]

obj = MyClass()
objgraph.show_backrefs(obj, max_depth=5)

# Show forward references
objgraph.show_refs(obj, max_depth=5)
```

### Finding Leaks

```python
import objgraph

# Find objects growing over time
objgraph.show_growth()

# Your code
data = [i ** 2 for i in range(100000)]

# Check growth again
objgraph.show_growth()

# Show objects by type
objgraph.show_most_common_types()

# Show objects by class
objgraph.show_most_common_types(classes=[list, dict, tuple])
```

### Visualization

```python
import objgraph

# Generate graph files
objgraph.show_backrefs(
    obj,
    max_depth=5,
    output='/tmp/backrefs.png',
    filename='/tmp/backrefs.dot'
)

# Show refs
objgraph.show_refs(
    obj,
    max_depth=5,
    output='/tmp/refs.png',
    filename='/tmp/refs.dot'
)
```

---

## Memory Optimization

### Using __slots__

```python
import sys

class RegularClass:
    def __init__(self, x, y):
        self.x = x
        self.y = y

class SlottedClass:
    __slots__ = ('x', 'y')
    def __init__(self, x, y):
        self.x = x
        self.y = y

regular = RegularClass(1, 2)
slotted = SlottedClass(1, 2)

print(sys.getsizeof(regular))  # 48 bytes
print(sys.getsizeof(slotted))  # 40 bytes
print(sys.getsizeof(regular.__dict__))  # 64 bytes (extra!)
```

### Using Generators

```python
import sys

# List comprehension (stores all in memory)
list_comp = [i ** 2 for i in range(1000000)]
print(f"List: {sys.getsizeof(list_comp)} bytes")

# Generator expression (lazy evaluation)
gen_exp = (i ** 2 for i in range(1000000))
print(f"Generator: {sys.getsizeof(gen_exp)} bytes")

# Generator function
def gen_func():
    for i in range(1000000):
        yield i ** 2

gen = gen_func()
print(f"Generator function: {sys.getsizeof(gen)} bytes")
```

### Using array Module

```python
import sys
from array import array

# List of integers
lst = list(range(1000000))
print(f"List: {sys.getsizeof(lst)} bytes")

# Array of integers
arr = array('i', range(1000000))
print(f"Array: {sys.getsizeof(arr)} bytes")
```

### Using numpy

```python
import sys
import numpy as np

# List
lst = list(range(1000000))
print(f"List: {sys.getsizeof(lst)} bytes")

# NumPy array
arr = np.arange(1000000, dtype=np.int64)
print(f"NumPy array: {sys.getsizeof(arr)} bytes")
print(f"NumPy array (nbytes): {arr.nbytes} bytes")
```

### String Optimization

```python
import sys

# String concatenation (inefficient)
result = ""
for i in range(1000):
    result += str(i)  # Creates new string each time

# Join (efficient)
result = "".join(str(i) for i in range(1000))

# Intern strings
import sys
a = sys.intern("hello world")
b = sys.intern("hello world")
print(a is b)  # True

# Use f-strings (efficient)
name = "Alice"
age = 30
msg = f"Hello {name}, you are {age} years old"
```

### Memory-Mapped Files

```python
import mmap
import os

# Create a memory-mapped file
with open("large_file.bin", "r+b") as f:
    # Memory map the file
    mm = mmap.mmap(f.fileno(), 0)

    # Read/write like a file
    data = mm[:100]  # Read first 100 bytes
    mm[100:200] = b"x" * 100  # Write

    mm.close()

# For large files
def process_large_file(filename):
    with open(filename, "r+b") as f:
        mm = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
        # Process without loading entire file
        for i in range(0, len(mm), 4096):
            chunk = mm[i:i+4096]
            # Process chunk
        mm.close()
```

---

## Best Practices

### Memory-Efficient Code

```python
# 1. Use generators for large datasets
def process_large_file(filename):
    with open(filename) as f:
        for line in f:  # Generator - one line at a time
            yield process(line)

# 2. Use slots for classes with many instances
class Point:
    __slots__ = ('x', 'y')
    def __init__(self, x, y):
        self.x = x
        self.y = y

# 3. Use array for homogeneous numeric data
from array import array
arr = array('i', range(1000000))  # 4 bytes per int

# 4. Use numpy for large numerical computations
import numpy as np
arr = np.arange(1000000)  # 8 bytes per int

# 5. Use weak references for caches
import weakref
cache = weakref.WeakValueDictionary()

# 6. Del large objects when done
large_data = process_huge_file()
result = analyze(large_data)
del large_data

# 7. Use context managers for resources
with open("file.txt") as f:
    data = f.read()

# 8. Avoid circular references when possible
```

### Debugging Memory Issues

```python
# 1. Use tracemalloc for memory profiling
import tracemalloc
tracemalloc.start()
# Your code
snapshot = tracemalloc.take_snapshot()

# 2. Use objgraph for object graphs
import objgraph
objgraph.show_most_common_types()

# 3. Use gc for garbage collection debugging
import gc
gc.set_debug(gc.DEBUG_LEAK)
gc.collect()

# 4. Monitor memory over time
import psutil
process = psutil.Process()
print(f"Memory: {process.memory_info().rss / 1024 / 1024:.2f} MB")
```

---

## Summary

Python's memory management:

- **Reference counting** is the primary mechanism
- **Generational GC** handles circular references
- **Memory pools** optimize allocation of small objects
- **Weak references** allow caching without preventing garbage collection
- **Slots** reduce memory usage for class instances
- **Generators** provide memory-efficient iteration
- **tracemalloc** and **objgraph** help debug memory issues
