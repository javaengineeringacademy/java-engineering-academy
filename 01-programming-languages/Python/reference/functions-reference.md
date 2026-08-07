# Python Functions Reference

## What are Python Functions?

Functions are reusable blocks of code that perform a specific task. They are first-class objects in Python, meaning they can be assigned to variables, passed as arguments, and returned from other functions.

## Why does Python Functions matter?

Understanding functions helps you:
- Write modular and reusable code
- Avoid code duplication
- Create clean and maintainable codebases
- Implement functional programming patterns

---

## 1. Function Anatomy

```python
# Basic function
def greet(name):
    """Greet a person by name."""
    return f"Hello, {name}!"

# Function with multiple parameters
def add(a, b=0):
    """Add two numbers."""
    return a + b

# Function with *args and **kwargs
def func(*args, **kwargs):
    """Accept any number of arguments."""
    print(f"args: {args}")
    print(f"kwargs: {kwargs}")

# Function with type hints
def add_numbers(a: int, b: int) -> int:
    """Add two integers."""
    return a + b
```

---

## 2. Arguments

### Positional Arguments

```python
def greet(name, greeting):
    return f"{greeting}, {name}!"

# Order matters
print(greet("Alice", "Hello"))  # Hello, Alice!
print(greet("Hello", "Alice"))  # Hello, Alice! (wrong order)
```

### Keyword Arguments

```python
def greet(name, greeting):
    return f"{greeting}, {name}!"

# Order doesn't matter
print(greet(name="Alice", greeting="Hello"))  # Hello, Alice!
print(greet(greeting="Hello", name="Alice"))  # Hello, Alice!
```

### Default Arguments

```python
def greet(name, greeting="Hello"):
    return f"{greeting}, {name}!"

print(greet("Alice"))  # Hello, Alice!
print(greet("Alice", "Hi"))  # Hi, Alice!
```

### Variable-length Arguments

```python
# *args - tuple of positional arguments
def func(*args):
    for arg in args:
        print(arg)

func(1, 2, 3)  # 1 2 3

# **kwargs - dictionary of keyword arguments
def func(**kwargs):
    for key, value in kwargs.items():
        print(f"{key}: {value}")

func(name="Alice", age=30)  # name: Alice age: 30
```

### Keyword-only Arguments

```python
def func(*, name, age):
    print(f"{name} is {age} years old")

func(name="Alice", age=30)  # Works
# func("Alice", 30)  # TypeError
```

### Positional-only Arguments

```python
def func(x, /, y=0):
    print(f"x={x}, y={y}")

func(1, 2)  # x=1, y=2
func(1)     # x=1, y=0
```

---

## 3. Decorators

### Basic Decorator

```python
def my_decorator(func):
    def wrapper(*args, **kwargs):
        print("Before function call")
        result = func(*args, **kwargs)
        print("After function call")
        return result
    return wrapper

@my_decorator
def say_hello(name):
    print(f"Hello, {name}!")

say_hello("Alice")
# Before function call
# Hello, Alice!
# After function call
```

### Decorator with Arguments

```python
def repeat(times):
    def decorator(func):
        def wrapper(*args, **kwargs):
            for _ in range(times):
                result = func(*args, **kwargs)
            return result
        return wrapper
    return decorator

@repeat(times=3)
def greet(name):
    print(f"Hello, {name}!")

greet("Alice")
# Hello, Alice!
# Hello, Alice!
# Hello, Alice!
```

### Class Decorator

```python
class MyDecorator:
    def __init__(self, func):
        self.func = func
    
    def __call__(self, *args, **kwargs):
        print("Before function call")
        result = self.func(*args, **kwargs)
        print("After function call")
        return result

@MyDecorator
def say_hello(name):
    print(f"Hello, {name}!")

say_hello("Alice")
```

### Built-in Decorators

```python
class MyClass:
    @staticmethod
    def static_method():
        return "Static method"
    
    @classmethod
    def class_method(cls):
        return f"Class method of {cls.__name__}"
    
    @property
    def value(self):
        return self._value
    
    @value.setter
    def value(self, value):
        self._value = value
```

---

## 4. Closures

```python
def make_multiplier(factor):
    def multiplier(x):
        return x * factor  # factor is from enclosing scope
    return multiplier

double = make_multiplier(2)
triple = make_multiplier(3)

print(double(5))  # 10
print(triple(5))  # 15

# Inspect closure variables
print(double.__closure__[0].cell_contents)  # 2
```

### Closure Pitfalls

```python
# Late binding closure
functions = []
for i in range(5):
    functions.append(lambda: i)  # All reference same i

print([f() for f in functions])  # [4, 4, 4, 4, 4]

# Fix: Capture i by value
functions = []
for i in range(5):
    functions.append(lambda i=i: i)  # Capture i by value

print([f() for f in functions])  # [0, 1, 2, 3, 4]
```

---

## 5. Lambda Functions

```python
# Basic lambda
add = lambda a, b: a + b
print(add(1, 2))  # 3

# With filter
numbers = [1, 2, 3, 4, 5]
evens = list(filter(lambda x: x % 2 == 0, numbers))
print(evens)  # [2, 4]

# With map
squared = list(map(lambda x: x**2, numbers))
print(squared)  # [1, 4, 9, 16, 25]

# With sorted
pairs = [(1, 'b'), (2, 'a'), (3, 'c')]
sorted_pairs = sorted(pairs, key=lambda x: x[1])
print(sorted_pairs)  # [(2, 'a'), (1, 'b'), (3, 'c')]
```

---

## 6. Higher-order Functions

```python
# Function as argument
def apply(func, value):
    return func(value)

print(apply(lambda x: x**2, 5))  # 25

# Function as return value
def make_adder(x):
    def adder(y):
        return x + y
    return adder

add5 = make_adder(5)
print(add5(3))  # 8
```

---

## 7. Recursion

```python
# Basic recursion
def factorial(n):
    if n == 0:
        return 1
    return n * factorial(n - 1)

print(factorial(5))  # 120

# Tail recursion (Python doesn't optimize)
def factorial_tail(n, acc=1):
    if n == 0:
        return acc
    return factorial_tail(n - 1, acc * n)
```

---

## 8. Function Annotations

```python
# Basic annotations
def greet(name: str) -> str:
    return f"Hello, {name}!"

# Complex annotations
from typing import List, Dict, Optional

def process(items: List[int], config: Dict[str, Any]) -> Optional[str]:
    if not items:
        return None
    return str(sum(items))
```

---

## One-Minute Revision Table

| Concept | Description | Example |
|---------|-------------|---------|
| **def** | Define function | `def func():` |
| **return** | Return value | `return x` |
| **args** | Variable positional args | `def func(*args):` |
| **kwargs** | Variable keyword args | `def func(**kwargs):` |
| **decorator** | Modify function behavior | `@decorator` |
| **closure** | Function with enclosing scope | `def outer(): def inner():` |
| **lambda** | Anonymous function | `lambda x: x**2` |
| **annotation** | Type hints | `def func(x: int) -> int:` |

---

## Common Mistakes

### 1. Mutable Default Arguments

```python
# WRONG
def append(item, lst=[]):
    lst.append(item)
    return lst

# RIGHT
def append(item, lst=None):
    if lst is None:
        lst = []
    lst.append(item)
    return lst
```

### 2. Late Binding Closures

```python
# WRONG
functions = [lambda: i for i in range(5)]

# RIGHT
functions = [lambda i=i: i for i in range(5)]
```

### 3. Modifying Global Variables

```python
# WRONG
counter = 0
def increment():
    global counter
    counter += 1

# RIGHT
def increment(counter):
    return counter + 1
```

### 4. Returning Multiple Values

```python
# WRONG
def get_stats(data):
    return min(data), max(data), sum(data)/len(data)

# RIGHT (unpacking)
min_val, max_val, avg = get_stats(data)
```

---

## Production Notes

1. **Use type hints** - Improve code clarity and IDE support
2. **Use docstrings** - Document your functions properly
3. **Keep functions small** - Single responsibility principle
4. **Avoid side effects** - Functions should be predictable
5. **Use `functools.lru_cache`** - Cache expensive function calls
6. **Use `@property` for computed attributes** - More Pythonic than getters/setters
7. **Use `@staticmethod` for utility methods** - When you don't need instance/class
8. **Use `@classmethod` for factory methods** - When you need class instead of instance
9. **Use `*args` and `**kwargs` sparingly** - Can reduce readability
10. **Use `functools.wraps` in decorators** - Preserve function metadata

---

## Further Reading

- Python documentation on functions
- Python documentation on decorators
- Fluent Python by Luciano Ramalho
- Python Cookbook by David Beazley
