# Python Core Concepts

## Variables and Data Types

### Dynamic Typing
- No explicit type declarations
- Type determined at runtime
- `type()` returns object type
- `isinstance()` checks type

### Primitive Types
```python
x = 42          # int
y = 3.14        # float
s = "hello"     # str
b = True        # bool
n = None        # NoneType
```

### Collections
```python
lst = [1, 2, 3]           # list - mutable, ordered
tup = (1, 2, 3)           # tuple - immutable, ordered
st = {1, 2, 3}            # set - mutable, unordered, unique
dct = {"a": 1}            # dict - key-value pairs
```

### Type Conversion
```python
int("42")      # str to int
float("3.14")  # str to float
str(42)        # int to str
list("abc")    # str to list ['a', 'b', 'c']
```

## Control Flow

### Conditionals
```python
if condition:
    pass
elif other:
    pass
else:
    pass
```

### Loops
```python
for item in iterable:
    pass

while condition:
    pass

# Loop control
break      # Exit loop
continue   # Skip iteration
else       # Executes if no break
```

### List Comprehensions
```python
squares = [x**2 for x in range(10)]
evens = [x for x in range(10) if x % 2 == 0]
```

## Functions

### Basic Functions
```python
def greet(name, greeting="Hello"):
    """Docstring for documentation."""
    return f"{greeting}, {name}!"
```

### *args and **kwargs
```python
def flexible(*args, **kwargs):
    for arg in args:
        print(arg)
    for key, value in kwargs.items():
        print(f"{key}: {value}")
```

### Lambda Functions
```python
add = lambda a, b: a + b
```

## Object-Oriented Programming

### Classes
```python
class Animal:
    def __init__(self, name):
        self.name = name

    def speak(self):
        raise NotImplementedError

class Dog(Animal):
    def speak(self):
        return "Woof!"
```

### Special Methods
```python
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return f"Vector({self.x}, {self.y})"

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)
```

### Properties
```python
class Circle:
    def __init__(self, radius):
        self._radius = radius

    @property
    def radius(self):
        return self._radius

    @radius.setter
    def radius(self, value):
        if value < 0:
            raise ValueError("Radius cannot be negative")
        self._radius = value
```

## Decorators

```python
def timer(func):
    import time
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        print(f"{func.__name__} took {time.time() - start:.2f}s")
        return result
    return wrapper

@timer
def slow_function():
    import time
    time.sleep(1)
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
    import time
    time.sleep(1)
```

## Exception Handling

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

## Iterators

```python
class CountDown:
    def __init__(self, start):
        self.start = start

    def __iter__(self):
        return self

    def __next__(self):
        if self.start <= 0:
            raise StopIteration
        self.start -= 1
        return self.start + 1
```

## Type Hints

```python
from typing import List, Dict, Optional

def greet(name: str) -> str:
    return f"Hello, {name}"

def process(items: List[int]) -> Dict[str, int]:
    return {"count": len(items), "sum": sum(items)}
```
