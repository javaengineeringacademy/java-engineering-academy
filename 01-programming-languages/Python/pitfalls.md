# Python Pitfalls

## Mutable Default Arguments

```python
# BAD - shared mutable default
def append_to(item, target=[]):
    target.append(item)
    return target

append_to(1)  # [1]
append_to(2)  # [1, 2] - unexpected!

# GOOD - use None
def append_to(item, target=None):
    if target is None:
        target = []
    target.append(item)
    return target
```

## Late Binding Closures

```python
# BAD - all functions use final value
funcs = []
for i in range(5):
    funcs.append(lambda: i)

[f() for f in funcs]  # [4, 4, 4, 4, 4]

# GOOD - capture value immediately
funcs = []
for i in range(5):
    funcs.append(lambda i=i: i)

[f() for f in funcs]  # [0, 1, 2, 3, 4]
```

## GIL Limitations

```python
import threading

# CPU-bound task - GIL prevents parallelism
def cpu_bound():
    return sum(range(1000000))

# Use multiprocessing instead
from multiprocessing import Pool

with Pool(4) as p:
    results = p.map(cpu_bound, range(4))
```

## Variable Scope

```python
x = 10

def read_x():
    print(x)  # UnboundLocalError if x is assigned

def modify_x():
    global x
    x = 20  # Now works

# Use nonlocal for nested functions
def outer():
    x = 10
    def inner():
        nonlocal x
        x = 20
    inner()
    print(x)  # 20
```

## Iterator Invalidation

```python
# BAD - modifying list during iteration
lst = [1, 2, 3, 4, 5]
for item in lst:
    if item % 2 == 0:
        lst.remove(item)

# GOOD - iterate over copy
lst = [1, 2, 3, 4, 5]
for item in lst[:]:
    if item % 2 == 0:
        lst.remove(item)

# BETTER - use list comprehension
lst = [item for item in lst if item % 2 != 0]
```

## String Concatenation

```python
# BAD - O(n^2) complexity
result = ""
for s in strings:
    result += s

# GOOD - O(n) complexity
result = "".join(strings)
```

## Floating Point Comparison

```python
# BAD - precision issues
0.1 + 0.2 == 0.3  # False

# GOOD - use tolerance
import math
math.isclose(0.1 + 0.2, 0.3)  # True
```

## Import Side Effects

```python
# BAD - code runs on import
# module.py
print("Imported!")  # Runs when imported
data = expensive_computation()

# GOOD - use if __name__
# module.py
def main():
    data = expensive_computation()

if __name__ == "__main__":
    main()
```

## Shadowing Built-ins

```python
# BAD - shadows built-in
def list(items):
    return items

list = [1, 2, 3]  # Now list() won't work

# GOOD - use descriptive names
def process_items(items):
    return items

my_list = [1, 2, 3]
```

## Circular Imports

```python
# a.py
from b import B

class A:
    pass

# b.py
from a import A

class B:
    pass

# Solution: use TYPE_CHECKING
from __future__ import annotations
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from b import B
```

## Mutable Class Attributes

```python
# BAD - shared across instances
class MyClass:
    items = []  # Shared!

# GOOD - use instance attributes
class MyClass:
    def __init__(self):
        self.items = []  # Unique per instance
```

## Async/Await Confusion

```python
# BAD - missing await
async def get_data():
    return fetch_data()  # Returns coroutine, not result

# GOOD - proper await
async def get_data():
    return await fetch_data()
```

## Exception Handling

```python
# BAD - bare except
try:
    risky_operation()
except:
    pass  # Silences all errors

# GOOD - specific exceptions
try:
    risky_operation()
except ValueError as e:
    logger.warning(f"Value error: {e}")
except Exception as e:
    logger.error(f"Unexpected: {e}")
    raise
```

## Threading vs Multiprocessing

```python
# Threading - good for I/O-bound
import threading

def io_bound():
    import time
    time.sleep(1)

threading.Thread(target=io_bound).start()

# Multiprocessing - good for CPU-bound
from multiprocessing import Process

def cpu_bound():
    return sum(range(1000000))

Process(target=cpu_bound).start()
```

## Context Manager Protocol

```python
# BAD - manual resource management
f = open("file.txt")
try:
    data = f.read()
finally:
    f.close()

# GOOD - use context manager
with open("file.txt") as f:
    data = f.read()
```

## List vs Generator

```python
# BAD - memory intensive
squares = [x**2 for x in range(1000000)]

# GOOD - memory efficient
squares = (x**2 for x in range(1000000))
```

## Type Checking

```python
# BAD - type checking with isinstance
def process(value):
    if isinstance(value, int):
        return value * 2
    raise TypeError("Expected int")

# GOOD - use type hints and protocols
from typing import Protocol

class Multipliable(Protocol):
    def __mul__(self, other: int) -> "Multipliable": ...

def process(value: Multipliable) -> Multipliable:
    return value * 2
```

## Dict Key Ordering

```python
# Python 3.7+ guarantees insertion order
d = {"c": 3, "a": 1, "b": 2}
list(d.keys())  # ["c", "a", "b"]

# Don't rely on this in older versions
```

## Walrus Operator Pitfalls

```python
# Can be confusing
if (n := len(data)) > 10:
    print(f"Too long: {n}")

# Consider readability
n = len(data)
if n > 10:
    print(f"Too long: {n}")
```

## Class vs Instance Attributes

```python
class MyClass:
    class_attr = []  # Shared across instances
    
    def __init__(self):
        self.instance_attr = []  # Unique per instance

a = MyClass()
b = MyClass()
a.class_attr.append(1)
print(b.class_attr)  # [1] - unexpected!
```

## Dictionary View Objects

```python
d = {"a": 1, "b": 2}
keys = d.keys()
# keys is a view - reflects changes
del d["a"]
print("a" in keys)  # False
```

## Best Practices

1. Always use `None` as default for mutable arguments
2. Capture closure variables immediately
3. Use `multiprocessing` for CPU-bound work
4. Prefer `with` statements for resource management
5. Use generators for large datasets
6. Never modify collections during iteration
7. Use `math.isclose()` for float comparison
8. Write unit tests to catch these issues
