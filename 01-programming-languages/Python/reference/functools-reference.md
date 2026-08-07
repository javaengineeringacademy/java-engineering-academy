# Python functools Reference

## What is functools?

The functools module provides higher-order functions and operations on callable objects. It includes tools for function manipulation, caching, and more.

## Why does functools matter?

Understanding functools helps you:
- Write more efficient code with caching
- Create flexible functions with partial application
- Implement comparison methods easily
- Use functional programming patterns

---

## 1. reduce

```python
from functools import reduce

# Basic reduce
numbers = [1, 2, 3, 4, 5]
result = reduce(lambda x, y: x + y, numbers)
print(result)  # 15

# With initial value
result = reduce(lambda x, y: x + y, numbers, 10)
print(result)  # 25

# Find maximum
result = reduce(lambda x, y: x if x > y else y, numbers)
print(result)  # 5
```

---

## 2. partial

```python
from functools import partial

# Basic partial
def power(base, exponent):
    return base ** exponent

square = partial(power, exponent=2)
cube = partial(power, exponent=3)

print(square(5))  # 25
print(cube(5))    # 125

# With default arguments
def connect(host, port, protocol='http'):
    return f"Connecting to {protocol}://{host}:{port}"

http_connect = partial(connect, protocol='https')
print(http_connect('example.com', 8080))
# Connecting to https://example.com:8080
```

---

## 3. lru_cache

```python
from functools import lru_cache

# Basic lru_cache
@lru_cache(maxsize=128)
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n-1) + fibonacci(n-2)

print(fibonacci(100))  # Fast!

# Cache info
print(fibonacci.cache_info())

# Clear cache
fibonacci.cache_clear()
```

---

## 4. total_ordering

```python
from functools import total_ordering

@total_ordering
class Student:
    def __init__(self, name, grade):
        self.name = name
        self.grade = grade
    
    def __eq__(self, other):
        return self.grade == other.grade
    
    def __lt__(self, other):
        return self.grade < other.grade

# Now all comparison methods are defined
s1 = Student('Alice', 90)
s2 = Student('Bob', 85)

print(s1 > s2)   # True
print(s1 >= s2)  # True
print(s1 < s2)   # False
print(s1 <= s2)  # False
```

---

## 5. singledispatch

```python
from functools import singledispatch

@singledispatch
def process(value):
    raise NotImplementedError(f"Cannot process {type(value)}")

@process.register(int)
def _(value):
    return f"Processing int: {value}"

@process.register(str)
def _(value):
    return f"Processing str: {value}"

@process.register(list)
def _(value):
    return f"Processing list: {value}"

print(process(42))         # Processing int: 42
print(process("hello"))    # Processing str: hello
print(process([1, 2, 3]))  # Processing list: [1, 2, 3]
```

---

## 6. wraps

```python
from functools import wraps

def my_decorator(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        """Wrapper docstring"""
        return func(*args, **kwargs)
    return wrapper

@my_decorator
def my_function():
    """Function docstring"""
    pass

print(my_function.__name__)  # my_function
print(my_function.__doc__)   # Function docstring
```

---

## 7. cached_property

```python
from functools import cached_property

class MyClass:
    def __init__(self):
        self._data = [1, 2, 3, 4, 5]
    
    @cached_property
    def expensive_computation(self):
        print("Computing...")
        return sum(self._data)

obj = MyClass()
print(obj.expensive_computation)  # Computing... 15
print(obj.expensive_computation)  # 15 (cached)
```

---

## 8. cmp_to_key

```python
from functools import cmp_to_key

def compare(a, b):
    return (a > b) - (a < b)

numbers = [5, 2, 8, 1, 9]
sorted_numbers = sorted(numbers, key=cmp_to_key(compare))
print(sorted_numbers)  # [1, 2, 5, 8, 9]
```

---

## One-Minute Revision Table

| Function | Description | Example |
|----------|-------------|---------|
| **reduce** | Reduce iterable to single value | `reduce(lambda x,y: x+y, [1,2,3])` |
| **partial** | Create partial function | `partial(func, arg=value)` |
| **lru_cache** | Cache function results | `@lru_cache(maxsize=128)` |
| **total_ordering** | Generate comparison methods | `@total_ordering` |
| **singledispatch** | Single dispatch generic | `@singledispatch` |
| **wraps** | Preserve function metadata | `@wraps(func)` |
| **cached_property** | Cache property | `@cached_property` |
| **cmp_to_key** | Convert comparator to key | `cmp_to_key(compare)` |

---

## Common Mistakes

### 1. Forgetting to Use `wraps`

```python
# WRONG
def my_decorator(func):
    def wrapper(*args, **kwargs):
        return func(*args, **kwargs)
    return wrapper

# RIGHT
from functools import wraps

def my_decorator(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        return func(*args, **kwargs)
    return wrapper
```

### 2. Using `reduce` When Built-in is Better

```python
# WRONG
from functools import reduce
sum([1, 2, 3])

# RIGHT (built-in sum is better)
sum([1, 2, 3])
```

### 3. Not Using `lru_cache` for Expensive Functions

```python
# WRONG
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n-1) + fibonacci(n-2)

# RIGHT (with caching)
from functools import lru_cache

@lru_cache(maxsize=128)
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n-1) + fibonacci(n-2)
```

---

## Production Notes

1. **Use `lru_cache` for expensive functions** - Cache results
2. **Use `partial` for creating specialized functions** - More readable
3. **Use `total_ordering` for comparison classes** - Less boilerplate
4. **Use `singledispatch` for type-based dispatch** - More flexible than if/elif
5. **Use `wraps` in decorators** - Preserve function metadata
6. **Use `cached_property` for expensive properties** - Cache computed values
7. **Use `reduce` sparingly** - Usually built-in functions are better
8. **Use `cmp_to_key` for custom comparisons** - When key function is complex
9. **Profile performance** - Sometimes functools is slower than manual
10. **Document caching behavior** - Especially for lru_cache

---

## Further Reading

- Python documentation on functools
- PEP 309 - Function objects
- Fluent Python by Luciano Ramalho
