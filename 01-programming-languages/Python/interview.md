# Python Interview Questions

## Basic Concepts

### 1. What is Python?
Python is a high-level, interpreted, general-purpose programming language. It emphasizes code readability with its significant indentation and supports multiple programming paradigms including procedural, object-oriented, and functional programming.

### 2. What is the difference between a list and a tuple?
- **List**: Mutable, can be modified after creation, uses square brackets `[]`
- **Tuple**: Immutable, cannot be modified after creation, uses parentheses `()`

```python
my_list = [1, 2, 3]  # Mutable
my_list[0] = 10  # Allowed

my_tuple = (1, 2, 3)  # Immutable
my_tuple[0] = 10  # TypeError
```

### 3. What are Python's built-in data types?
- **Numeric**: int, float, complex
- **Sequence**: list, tuple, range
- **Text**: str
- **Mapping**: dict
- **Set**: set, frozenset
- **Boolean**: bool
- **Binary**: bytes, bytearray, memoryview

### 4. What is the difference between `is` and `==`?
- `is`: Checks if two variables point to the same object in memory
- `==`: Checks if two variables have the same value

```python
a = [1, 2, 3]
b = [1, 2, 3]
c = a

a == b  # True (same value)
a is b  # False (different objects)
a is c  # True (same object)
```

### 5. What are decorators?
Decorators are functions that modify the behavior of other functions or classes. They are applied using the `@decorator` syntax.

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

## Intermediate Concepts

### 6. What is the GIL?
The Global Interpreter Lock (GIL) is a mutex in CPython that protects access to Python objects, preventing multiple threads from executing Python bytecode simultaneously.

**Implications:**
- CPU-bound threads cannot run in parallel
- I/O-bound operations release the GIL
- Use `multiprocessing` for CPU-bound work

### 7. What are generators?
Generators are functions that return an iterator using `yield` instead of `return`. They produce values lazily, one at a time.

```python
def fibonacci():
    a, b = 0, 1
    while True:
        yield a
        a, b = b, a + b

# Use
fib = fibonacci()
next(fib)  # 0
next(fib)  # 1
```

### 8. What are context managers?
Context managers manage resources using the `with` statement, ensuring proper setup and cleanup.

```python
from contextlib import contextmanager

@contextmanager
def timer():
    import time
    start = time.time()
    try:
        yield
    finally:
        print(f"Elapsed: {time.time() - start:.2f}s")

with timer():
    # Code to time
    pass
```

### 9. Explain list comprehensions vs generator expressions
- **List comprehension**: Creates entire list in memory
- **Generator expression**: Yields items one at a time (memory efficient)

```python
# List comprehension
squares_list = [x**2 for x in range(1000000)]

# Generator expression
squares_gen = (x**2 for x in range(1000000))
```

### 10. What are *args and **kwargs?
- `*args`: Variable number of positional arguments (tuple)
- `**kwargs`: Variable number of keyword arguments (dictionary)

```python
def flexible(*args, **kwargs):
    for arg in args:
        print(arg)
    for key, value in kwargs.items():
        print(f"{key}: {value}")
```

## Advanced Concepts

### 11. What are metaclasses?
Metaclasses are classes of classes. They define how classes behave. `type` is the default metaclass.

```python
class Meta(type):
    def __new__(cls, name, bases, dict):
        print(f"Creating class {name}")
        return super().__new__(cls, name, bases, dict)

class MyClass(metaclass=Meta):
    pass
```

### 12. What are descriptors?
Descriptors are objects that define `__get__`, `__set__`, or `__delete__` methods, controlling attribute access.

```python
class Property:
    def __init__(self, fget):
        self.fget = fget
    
    def __get__(self, obj, objtype=None):
        return self.fget(obj)

class MyClass:
    @Property
    def value(self):
        return 42
```

### 13. What is monkey patching?
Dynamically modifying a class or module at runtime.

```python
# Original
class MyClass:
    def method(self):
        return "original"

# Monkey patch
def new_method(self):
    return "patched"

MyClass.method = new_method
```

### 14. Explain the difference between deep and shallow copy
- **Shallow copy**: New object, but references to nested objects
- **Deep copy**: New object, recursively copies all nested objects

```python
import copy

original = [[1, 2], [3, 4]]
shallow = copy.copy(original)
deep = copy.deepcopy(original)

original[0][0] = 99
print(shallow)  # [[99, 2], [3, 4]] - affected
print(deep)      # [[1, 2], [3, 4]] - not affected
```

### 15. What are slots?
Slots restrict attribute creation to specific names, reducing memory usage.

```python
class WithSlots:
    __slots__ = ['name', 'age']
    
    def __init__(self, name, age):
        self.name = name
        self.age = age

# Cannot add arbitrary attributes
obj = WithSlots("John", 30)
obj.email = "test@test.com"  # AttributeError
```

## System Design

### 16. How would you design a URL shortener?
- Use hash function or base62 encoding
- Store mappings in database (hash -> original URL)
- Use caching for frequently accessed URLs
- Implement rate limiting
- Add analytics tracking

### 17. How would you design a chat application?
- Use WebSockets for real-time communication
- Store messages in database with timestamps
- Implement user authentication
- Use Redis for pub/sub messaging
- Add message queuing for reliability

## Best Practices

### 18. What are Python best practices?
1. Follow PEP 8 style guide
2. Use virtual environments
3. Write tests with pytest
4. Use type hints
5. Document code with docstrings
6. Use context managers for resources
7. Handle exceptions properly
8. Use generators for large datasets
9. Profile before optimizing
10. Use meaningful variable names

### 19. How do you handle errors in Python?
```python
try:
    risky_operation()
except ValueError as e:
    logger.warning(f"Value error: {e}")
    raise
except Exception as e:
    logger.error(f"Unexpected: {e}")
    raise
finally:
    cleanup()
```

### 20. What are common Python anti-patterns?
1. Using mutable default arguments
2. Late binding closures
3. Not using virtual environments
4. Ignoring exception handling
5. Not writing tests
6. Using global variables
7. Not using context managers
8. Premature optimization
