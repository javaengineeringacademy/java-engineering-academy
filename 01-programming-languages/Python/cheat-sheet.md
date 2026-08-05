# Python Cheat Sheet

## Variables and Types

```python
# Variables
x = 10
name = "Python"
is_active = True
value = None

# Type checking
type(x)  # <class 'int'>
isinstance(x, int)  # True

# Type conversion
int("42")      # 42
float("3.14")  # 3.14
str(42)        # "42"
list("abc")    # ['a', 'b', 'c']
```

## Data Structures

```python
# List (mutable, ordered)
lst = [1, 2, 3]
lst.append(4)
lst[0]  # 1

# Tuple (immutable, ordered)
tup = (1, 2, 3)
tup[0]  # 1

# Dictionary (key-value pairs)
dct = {"a": 1, "b": 2}
dct["a"]  # 1
dct.get("c", 0)  # 0

# Set (unique, unordered)
st = {1, 2, 3}
st.add(4)
```

## Strings

```python
# String methods
s = "Hello, World!"
s.lower()        # "hello, world!"
s.upper()        # "HELLO, WORLD!"
s.strip()        # Remove whitespace
s.split(",")     # ['Hello', ' World!']
s.replace("H", "J")  # "Jello, World!"
s.find("World")  # 7
s.startswith("H")  # True

# f-strings
name = "Python"
f"Hello, {name}!"  # "Hello, Python!"
f"{2 + 2}"  # "4"
f"{3.14159:.2f}"  # "3.14"
```

## Control Flow

```python
# if/elif/else
if condition:
    pass
elif other:
    pass
else:
    pass

# for loop
for i in range(10):
    pass

for item in lst:
    pass

# while loop
while condition:
    pass

# List comprehension
squares = [x**2 for x in range(10)]
evens = [x for x in range(10) if x % 2 == 0]
```

## Functions

```python
# Basic function
def greet(name, greeting="Hello"):
    """Docstring."""
    return f"{greeting}, {name}!"

# *args and **kwargs
def flexible(*args, **kwargs):
    for arg in args:
        print(arg)
    for key, value in kwargs.items():
        print(f"{key}: {value}")

# Lambda
add = lambda a, b: a + b

# Decorator
def timer(func):
    import time
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        print(f"{func.__name__} took {time.time() - start:.2f}s")
        return result
    return wrapper
```

## Classes

```python
class MyClass:
    def __init__(self, name):
        self.name = name
    
    def greet(self):
        return f"Hello, {self.name}!"
    
    @property
    def name_upper(self):
        return self.name.upper()
    
    def __repr__(self):
        return f"MyClass('{self.name}')"
```

## Error Handling

```python
try:
    result = 10 / 0
except ZeroDivisionError as e:
    print(f"Error: {e}")
except Exception as e:
    print(f"Unexpected: {e}")
finally:
    print("Cleanup")
```

## File I/O

```python
# Read
with open("file.txt") as f:
    content = f.read()

# Write
with open("file.txt", "w") as f:
    f.write("Hello")

# Read lines
with open("file.txt") as f:
    for line in f:
        print(line.strip())
```

## Imports

```python
import os
from os import path
from os.path import join, exists
import numpy as np
from collections import defaultdict, Counter
```

## Useful Standard Library

```python
import os
import sys
import json
import math
import random
import datetime
from collections import defaultdict, Counter, namedtuple
from pathlib import Path
from typing import List, Dict, Optional
from functools import lru_cache, wraps
from contextlib import contextmanager
```

## Type Hints

```python
from typing import List, Dict, Optional, Tuple

def greet(name: str) -> str:
    return f"Hello, {name}"

def process(items: List[int]) -> Dict[str, int]:
    return {"count": len(items), "sum": sum(items)}
```

## Generators

```python
def fibonacci():
    a, b = 0, 1
    while True:
        yield a
        a, b = b, a + b

# Generator expression
squares = (x**2 for x in range(10))
```

## Context Managers

```python
from contextlib import contextmanager

@contextmanager
def timer():
    import time
    start = time.time()
    yield
    print(f"Elapsed: {time.time() - start:.2f}s")

with timer():
    pass
```

## List Operations

```python
lst = [1, 2, 3, 4, 5]

lst.append(6)      # Add to end
lst.insert(0, 0)   # Insert at index
lst.extend([7, 8]) # Extend list
lst.pop()          # Remove last
lst.pop(0)         # Remove at index
lst.remove(3)      # Remove by value
lst.sort()         # Sort in place
lst.reverse()      # Reverse in place
len(lst)           # Length
sum(lst)           # Sum
min(lst)           # Minimum
max(lst)           # Maximum
```

## Dictionary Operations

```python
dct = {"a": 1, "b": 2}

dct.keys()    # dict_keys(['a', 'b'])
dct.values()  # dict_values([1, 2])
dct.items()   # dict_items([('a', 1), ('b', 2)])
dct.get("c", 0)  # 0
dct.pop("a")  # Remove and return
```

## Virtual Environment

```bash
# Create
python -m venv venv

# Activate (macOS/Linux)
source venv/bin/activate

# Activate (Windows)
venv\Scripts\activate

# Deactivate
deactivate
```

## Common Patterns

```python
# Swap values
a, b = b, a

# Unpack
first, *rest = [1, 2, 3, 4]

# Enumerate
for i, item in enumerate(lst):
    print(i, item)

# Zip
for a, b in zip(lst1, lst2):
    print(a, b)

# Map and filter
results = list(map(str, lst))
evens = list(filter(lambda x: x % 2 == 0, lst))
```
