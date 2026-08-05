# Python Fundamentals

A comprehensive guide to Python programming fundamentals.

## Table of Contents

- [Variables and Data Types](#variables-and-data-types)
- [Operators](#operators)
- [Control Flow](#control-flow)
- [Functions](#functions)
- [Classes and Objects](#classes-and-objects)
- [List Comprehensions](#list-comprehensions)
- [Decorators](#decorators)
- [Context Managers](#context-managers)
- [Modules and Packages](#modules-and-packages)
- [Error Handling](#error-handling)

---

## Variables and Data Types

### Variable Assignment

Python uses dynamic typing — you don't need to declare variable types explicitly.

```python
# Basic assignment
name = "Alice"
age = 30
height = 5.9
is_student = True

# Multiple assignment
x, y, z = 1, 2, 3

# Same value assignment
a = b = c = 0

# Type hinting (Python 3.5+)
name: str = "Alice"
age: int = 30
height: float = 5.9
```

### Numeric Types

```python
# Integers (arbitrary precision)
big_number = 10**100
negative = -42
hex_val = 0xFF
oct_val = 0o77
bin_val = 0b1010

# Floats (double precision)
pi = 3.14159
scientific = 1.5e10

# Complex numbers
complex_num = 3 + 4j
print(complex_num.real)  # 3.0
print(complex_num.imag)  # 4.0

# Decimal (precise decimal arithmetic)
from decimal import Decimal
precise = Decimal('0.1') + Decimal('0.2')
print(precise)  # 0.3

# Fraction (exact rational arithmetic)
from fractions import Fraction
frac = Fraction(1, 3)
print(frac + frac)  # 2/3
```

### Strings

```python
# String creation
single = 'hello'
double = "hello"
triple = """multiline
string"""

# String operations
greeting = "Hello" + " " + "World"  # Concatenation
repeat = "Ha" * 3                    # "HaHaHa"
length = len("hello")                # 5

# String methods
text = "  Hello, World!  "
print(text.strip())       # "Hello, World!"
print(text.lower())       # "  hello, world!  "
print(text.upper())       # "  HELLO, WORLD!  "
print(text.replace("World", "Python"))  # "  Hello, Python!  "
print(text.split(","))    # ['  Hello', ' World!  ']
print(text.find("World")) # 9

# f-strings (Python 3.6+)
name = "Alice"
age = 30
print(f"My name is {name} and I am {age} years old.")
print(f"2 + 2 = {2 + 2}")
print(f"{'centered':^20}")
print(f"{3.14159:.2f}")  # 3.14

# String formatting
print("Name: %s, Age: %d" % (name, age))  # Old style
print("Name: {}, Age: {}".format(name, age))  # .format()

# String methods continued
print("hello world".title())      # "Hello World"
print("hello".capitalize())       # "Hello"
print("hello".startswith("he"))   # True
print("hello".endswith("lo"))     # True
print("123".isdigit())            # True
print("hello".isalpha())          # True
print("hello123".isalnum())       # True
```

### Booleans

```python
# Boolean values
is_active = True
is_deleted = False

# Boolean operations
a = True
b = False
print(a and b)   # False
print(a or b)    # True
print(not a)     # False

# Truthy and Falsy values
bool(0)          # False
bool(1)          # True
bool("")         # False
bool("hello")    # True
bool([])         # False
bool([1, 2, 3])  # True
bool(None)       # False

# Identity vs Equality
a = [1, 2, 3]
b = [1, 2, 3]
c = a
print(a == b)   # True  (equal values)
print(a is b)   # False (different objects)
print(a is c)   # True  (same object)
```

### None

```python
# None represents absence of value
result = None
print(result is None)  # True
print(result == None)  # True (but use 'is' for comparison)

# None as default argument
def greet(name=None):
    if name is None:
        name = "World"
    return f"Hello, {name}!"
```

---

## Operators

### Arithmetic Operators

```python
print(10 + 3)   # Addition: 13
print(10 - 3)   # Subtraction: 7
print(10 * 3)   # Multiplication: 30
print(10 / 3)   # Division: 3.3333...
print(10 // 3)  # Floor Division: 3
print(10 % 3)   # Modulo: 1
print(10 ** 3)  # Exponentiation: 1000
```

### Comparison Operators

```python
print(5 == 5)   # Equal
print(5 != 3)   # Not equal
print(5 > 3)    # Greater than
print(5 < 3)    # Less than
print(5 >= 5)   # Greater or equal
print(5 <= 3)   # Less or equal
```

### Logical Operators

```python
print(True and False)  # False
print(True or False)   # True
print(not True)        # False

# Short-circuit evaluation
x = 5
result = x > 0 and x < 10  # True
```

### Bitwise Operators

```python
a = 0b1100  # 12
b = 0b1010  # 10

print(bin(a & b))   # 0b1000 (AND)
print(bin(a | b))   # 0b1110 (OR)
print(bin(a ^ b))   # 0b0110 (XOR)
print(bin(~a))      # -0b1101 (NOT)
print(bin(a << 2))  # 0b110000 (Left shift)
print(bin(a >> 2))  # 0b11 (Right shift)
```

### Assignment Operators

```python
x = 10
x += 5    # x = 15
x -= 3    # x = 12
x *= 2    # x = 24
x /= 4    # x = 6.0
x //= 2   # x = 3.0
x **= 3   # x = 27.0
x %= 5    # x = 2.0
x &= 3    # Bitwise AND assignment
x |= 3    # Bitwise OR assignment
x ^= 3    # Bitwise XOR assignment
x <<= 2   # Left shift assignment
x >>= 1   # Right shift assignment
```

### Membership Operators

```python
fruits = ["apple", "banana", "cherry"]
print("apple" in fruits)      # True
print("grape" not in fruits)  # True

text = "Hello, World!"
print("World" in text)  # True
```

### Identity Operators

```python
a = [1, 2, 3]
b = [1, 2, 3]
c = a

print(a is b)      # False (different objects)
print(a is c)      # True  (same object)
print(a is not b)  # True
```

---

## Control Flow

### if/elif/else

```python
age = 25

if age < 13:
    print("Child")
elif age < 18:
    print("Teenager")
elif age < 65:
    print("Adult")
else:
    print("Senior")

# Ternary operator
status = "adult" if age >= 18 else "minor"

# Nested conditions
score = 85
if score >= 90:
    grade = "A"
elif score >= 80:
    if score >= 85:
        grade = "B+"
    else:
        grade = "B"
else:
    grade = "C"
```

### for Loop

```python
# Iterating over a list
fruits = ["apple", "banana", "cherry"]
for fruit in fruits:
    print(fruit)

# Using range()
for i in range(5):        # 0, 1, 2, 3, 4
    print(i)

for i in range(2, 10):    # 2, 3, ..., 9
    print(i)

for i in range(0, 10, 2): # 0, 2, 4, 6, 8
    print(i)

# Enumerate
for index, fruit in enumerate(fruits):
    print(f"{index}: {fruit}")

for index, fruit in enumerate(fruits, start=1):
    print(f"{index}: {fruit}")

# Zip
names = ["Alice", "Bob", "Charlie"]
ages = [25, 30, 35]
for name, age in zip(names, ages):
    print(f"{name} is {age} years old.")

# Dictionary iteration
person = {"name": "Alice", "age": 30}
for key, value in person.items():
    print(f"{key}: {value}")

# List comprehension equivalent
squares = [x**2 for x in range(10)]
```

### while Loop

```python
count = 0
while count < 5:
    print(count)
    count += 1

# while with else
n = 10
while n > 0:
    n -= 3
else:
    print("Loop finished normally")

# Infinite loop with break
while True:
    user_input = input("Enter 'quit' to exit: ")
    if user_input == "quit":
        break
```

### break, continue, pass

```python
# break - exits the loop
for i in range(10):
    if i == 5:
        break
    print(i)  # 0, 1, 2, 3, 4

# continue - skips to next iteration
for i in range(10):
    if i % 2 == 0:
        continue
    print(i)  # 1, 3, 5, 7, 9

# pass - does nothing (placeholder)
for i in range(10):
    if i % 2 == 0:
        pass  # TODO: handle even numbers
    print(i)
```

### match/case (Python 3.10+)

```python
def http_status(code):
    match code:
        case 200:
            return "OK"
        case 301:
            return "Moved Permanently"
        case 404:
            return "Not Found"
        case 500:
            return "Internal Server Error"
        case _:
            return "Unknown status code"

# Pattern matching with conditions
def categorize(value):
    match value:
        case x if x < 0:
            return "negative"
        case 0:
            return "zero"
        case x if x > 0:
            return "positive"

# Structural pattern matching
point = (1, 2)
match point:
    case (0, 0):
        print("Origin")
    case (x, 0):
        print(f"On x-axis at {x}")
    case (0, y):
        print(f"On y-axis at {y}")
    case (x, y):
        print(f"Point at ({x}, {y})")
```

---

## Functions

### Basic Functions

```python
def greet():
    print("Hello, World!")

greet()  # Hello, World!

# Function with parameters
def greet_person(name):
    print(f"Hello, {name}!")

greet_person("Alice")

# Return value
def add(a, b):
    return a + b

result = add(3, 5)  # 8

# Multiple return values
def getMinMax(numbers):
    return min(numbers), max(numbers)

minimum, maximum = getMinMax([1, 2, 3, 4, 5])
```

### Default Arguments

```python
def greet(name, greeting="Hello"):
    return f"{greeting}, {name}!"

print(greet("Alice"))              # "Hello, Alice!"
print(greet("Alice", "Hi"))        # "Hi, Alice!"

# Common pitfall with mutable defaults
def append_to(item, lst=None):
    if lst is None:
        lst = []
    lst.append(item)
    return lst
```

### *args and **kwargs

```python
# *args - positional arguments
def sum_all(*args):
    return sum(args)

print(sum_all(1, 2, 3, 4, 5))  # 15

# **kwargs - keyword arguments
def print_info(**kwargs):
    for key, value in kwargs.items():
        print(f"{key}: {value}")

print_info(name="Alice", age=30, city="NYC")

# Combined
def func(a, b, *args, **kwargs):
    print(f"a={a}, b={b}")
    print(f"args={args}")
    print(f"kwargs={kwargs}")

func(1, 2, 3, 4, x=5, y=6)
# a=1, b=2
# args=(3, 4)
# kwargs={'x': 5, 'y': 6}

# Keyword-only arguments
def func(a, b, *, keyword_only):
    print(f"a={a}, b={b}, keyword_only={keyword_only}")

func(1, 2, keyword_only=3)
```

### Lambda Functions

```python
# Lambda syntax
square = lambda x: x ** 2
print(square(5))  # 25

# Lambda with multiple arguments
add = lambda x, y: x + y
print(add(3, 5))  # 8

# Lambda with default argument
greet = lambda name, greeting="Hello": f"{greeting}, {name}!"
print(greet("Alice"))  # "Hello, Alice!"

# Using lambda with sorted()
students = [("Alice", 85), ("Bob", 92), ("Charlie", 78)]
sorted_students = sorted(students, key=lambda s: s[1], reverse=True)
print(sorted_students)  # [('Bob', 92), ('Alice', 85), ('Charlie', 78)]

# Using with map(), filter(), reduce()
from functools import reduce

numbers = [1, 2, 3, 4, 5]
squared = list(map(lambda x: x**2, numbers))
evens = list(filter(lambda x: x % 2 == 0, numbers))
total = reduce(lambda x, y: x + y, numbers)
```

### Higher-Order Functions

```python
# Function as argument
def apply_operation(func, a, b):
    return func(a, b)

def multiply(x, y):
    return x * y

result = apply_operation(multiply, 3, 4)  # 12

# Function as return value
def create_multiplier(factor):
    def multiplier(x):
        return x * factor
    return multiplier

double = create_multiplier(2)
triple = create_multiplier(3)
print(double(5))  # 10
print(triple(5))  # 15

# map(), filter(), reduce()
numbers = [1, 2, 3, 4, 5]

# map - applies function to each element
squared = list(map(lambda x: x**2, numbers))

# filter - filters elements based on condition
evens = list(filter(lambda x: x % 2 == 0, numbers))

# reduce - reduces to single value
from functools import reduce
product = reduce(lambda x, y: x * y, numbers)
```

### Recursion

```python
# Factorial
def factorial(n):
    if n <= 1:
        return 1
    return n * factorial(n - 1)

print(factorial(5))  # 120

# Fibonacci
def fibonacci(n):
    if n <= 1:
        return n
    return fibonacci(n - 1) + fibonacci(n - 2)

# Memoized version
from functools import lru_cache

@lru_cache(maxsize=None)
def fib_memo(n):
    if n <= 1:
        return n
    return fib_memo(n - 1) + fib_memo(n - 2)

# Binary search
def binary_search(arr, target, low=0, high=None):
    if high is None:
        high = len(arr) - 1
    if low > high:
        return -1
    mid = (low + high) // 2
    if arr[mid] == target:
        return mid
    elif arr[mid] < target:
        return binary_search(arr, target, mid + 1, high)
    else:
        return binary_search(arr, target, low, mid - 1)
```

---

## Classes and Objects

### Basic Classes

```python
class Dog:
    # Class attribute
    species = "Canis familiaris"

    # Initializer
    def __init__(self, name, age):
        self.name = name
        self.age = age

    # Instance method
    def bark(self):
        return f"{self.name} says Woof!"

    # String representation
    def __str__(self):
        return f"{self.name} is {self.age} years old"

    # Repr representation
    def __repr__(self):
        return f"Dog('{self.name}', {self.age})"

# Creating instances
dog1 = Dog("Buddy", 3)
dog2 = Dog("Max", 5)

print(dog1.bark())      # Buddy says Woof!
print(dog1.species)     # Canis familiaris
print(str(dog1))        # Buddy is 3 years old
```

### Inheritance

```python
class Animal:
    def __init__(self, name, sound):
        self.name = name
        self.sound = sound

    def speak(self):
        return f"{self.name} says {self.sound}!"

class Dog(Animal):
    def __init__(self, name, breed):
        super().__init__(name, "Woof")
        self.breed = breed

    def fetch(self, item):
        return f"{self.name} fetches the {item}"

class Cat(Animal):
    def __init__(self, name, indoor=True):
        super().__init__(name, "Meow")
        self.indoor = indoor

dog = Dog("Buddy", "Golden Retriever")
cat = Cat("Whiskers")

print(dog.speak())      # Buddy says Woof!
print(dog.fetch("ball"))  # Buddy fetches the ball
print(cat.speak())      # Whiskers says Meow!
print(isinstance(dog, Dog))    # True
print(isinstance(dog, Animal)) # True
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

    @property
    def area(self):
        import math
        return math.pi * self._radius ** 2

    @property
    def circumference(self):
        import math
        return 2 * math.pi * self._radius

circle = Circle(5)
print(circle.radius)        # 5
print(circle.area)          # 78.539...
circle.radius = 10
print(circle.area)          # 314.159...
```

### Class Methods and Static Methods

```python
class Date:
    def __init__(self, year, month, day):
        self.year = year
        self.month = month
        self.day = day

    @classmethod
    def from_string(cls, date_string):
        year, month, day = map(int, date_string.split('-'))
        return cls(year, month, day)

    @staticmethod
    def is_valid(year, month, day):
        return 1 <= month <= 12 and 1 <= day <= 31

    def __str__(self):
        return f"{self.year}-{self.month:02d}-{self.day:02d}"

# Usage
date = Date.from_string("2024-01-15")
print(date)  # 2024-01-15

print(Date.is_valid(2024, 13, 1))  # False
print(Date.is_valid(2024, 6, 15))  # True
```

### Dunder Methods

```python
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)

    def __sub__(self, other):
        return Vector(self.x - other.x, self.y - other.y)

    def __mul__(self, scalar):
        return Vector(self.x * scalar, self.y * scalar)

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __lt__(self, other):
        return abs(self) < abs(other)

    def __abs__(self):
        return (self.x ** 2 + self.y ** 2) ** 0.5

    def __len__(self):
        return 2

    def __getitem__(self, index):
        if index == 0:
            return self.x
        elif index == 1:
            return self.y
        raise IndexError("Vector index out of range")

    def __repr__(self):
        return f"Vector({self.x}, {self.y})"

    def __str__(self):
        return f"({self.x}, {self.y})"

v1 = Vector(1, 2)
v2 = Vector(3, 4)
print(v1 + v2)      # (4, 6)
print(v1 - v2)      # (-2, -2)
print(v1 * 3)       # (3, 6)
print(abs(v1))       # 2.236...
print(v1[0])        # 1
print(v1 > v2)      # False
```

---

## List Comprehensions

### Basic Syntax

```python
# List comprehension
squares = [x**2 for x in range(10)]
print(squares)  # [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]

# With condition
evens = [x for x in range(20) if x % 2 == 0]
print(evens)  # [0, 2, 4, 6, 8, 10, 12, 14, 16, 18]

# With if-else
labels = ["even" if x % 2 == 0 else "odd" for x in range(5)]
print(labels)  # ['even', 'odd', 'even', 'odd', 'even']

# Nested loops
pairs = [(x, y) for x in range(3) for y in range(3)]
print(pairs)  # [(0,0), (0,1), (0,2), (1,0), ...]

# Flattening nested list
matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
flat = [num for row in matrix for num in row]
print(flat)  # [1, 2, 3, 4, 5, 6, 7, 8, 9]

# Dictionary comprehension
word = "hello"
char_count = {c: word.count(c) for c in set(word)}
print(char_count)  # {'h': 1, 'e': 1, 'l': 2, 'o': 1}

# Set comprehension
unique_lengths = {len(word) for word in ["hello", "world", "python"]}
print(unique_lengths)  # {5, 6}

# Generator expression
sum_of_squares = sum(x**2 for x in range(1000000))
```

### Advanced Patterns

```python
# Walrus operator (Python 3.8+)
import re
text = "Contact us at support@example.com"
emails = [match.group() for match in re.finditer(r'[\w.]+@[\w.]+', text)]

# Nested comprehension with condition
matrix = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
greater_than_5 = [num for row in matrix for num in row if num > 5]
print(greater_than_5)  # [6, 7, 8, 9]

# Complex transformation
data = [{"name": "Alice", "age": 25}, {"name": "Bob", "age": 30}]
names = [d["name"] for d in data if d["age"] >= 28]
print(names)  # ['Bob']
```

---

## Decorators

### Basic Decorator

```python
def my_decorator(func):
    def wrapper(*args, **kwargs):
        print("Something before the function is called.")
        result = func(*args, **kwargs)
        print("Something after the function is called.")
        return result
    return wrapper

@my_decorator
def say_hello():
    print("Hello!")

say_hello()
# Something before the function is called.
# Hello!
# Something after the function is called.
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

### Preserving Function Metadata

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
    """My function docstring"""
    pass

print(my_function.__name__)  # my_function
print(my_function.__doc__)   # My function docstring
```

### Built-in Decorators

```python
class MyClass:
    @staticmethod
    def static_method():
        return "Static method called"

    @classmethod
    def class_method(cls):
        return f"Class method called on {cls.__name__}"

    @property
    def value(self):
        return self._value

    @value.setter
    def value(self, val):
        self._value = val

# lru_cache decorator
from functools import lru_cache

@lru_cache(maxsize=128)
def fibonacci(n):
    if n < 2:
        return n
    return fibonacci(n-1) + fibonacci(n-2)

# total_ordering decorator
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
```

### Class Decorators

```python
def singleton(cls):
    instances = {}
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    return get_instance

@singleton
class Database:
    def __init__(self):
        self.connection = "Connected"

db1 = Database()
db2 = Database()
print(db1 is db2)  # True

# Dataclass decorator (Python 3.7+)
from dataclasses import dataclass

@dataclass
class Point:
    x: float
    y: float

    def distance_to(self, other):
        return ((self.x - other.x)**2 + (self.y - other.y)**2)**0.5

p1 = Point(1.0, 2.0)
p2 = Point(4.0, 6.0)
print(p1.distance_to(p2))  # 5.0
```

---

## Context Managers

### Using `with` Statement

```python
# File handling
with open("file.txt", "w") as f:
    f.write("Hello, World!")
# File is automatically closed

# Multiple context managers
with open("input.txt") as fin, open("output.txt", "w") as fout:
    fout.write(fin.read())
```

### Custom Context Manager (Class-based)

```python
class Timer:
    def __init__(self):
        self.start = None
        self.end = None
        self.elapsed = None

    def __enter__(self):
        import time
        self.start = time.time()
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        import time
        self.end = time.time()
        self.elapsed = self.end - self.start
        print(f"Elapsed time: {self.elapsed:.4f} seconds")
        return False  # Don't suppress exceptions

# Usage
with Timer() as t:
    sum(range(1000000))
# Elapsed time: 0.0312 seconds
```

### Custom Context Manager (Generator-based)

```python
from contextlib import contextmanager

@contextmanager
def managed_resource(name):
    print(f"Acquiring {name}")
    resource = {"name": name, "active": True}
    try:
        yield resource
    except Exception as e:
        print(f"Error with {name}: {e}")
        resource["active"] = False
    finally:
        print(f"Releasing {name}")
        resource["active"] = False

# Usage
with managed_resource("database") as db:
    print(f"Using {db['name']}")
# Acquiring database
# Using database
# Releasing database
```

### contextlib Utilities

```python
from contextlib import suppress, redirect_stdout, ExitStack

# suppress - ignore specific exceptions
with suppress(FileNotFoundError):
    os.remove("nonexistent.txt")

# redirect_stdout
import io
f = io.StringIO()
with redirect_stdout(f):
    print("captured")
output = f.getvalue()

# ExitStack - dynamically manage context managers
with ExitStack() as stack:
    files = [stack.enter_context(open(fname)) for fname in filenames]
```

---

## Modules and Packages

### Creating Modules

```python
# math_operations.py
"""A module for math operations."""

def add(a, b):
    """Add two numbers."""
    return a + b

def subtract(a, b):
    """Subtract b from a."""
    return a - b

PI = 3.14159265359

class Calculator:
    def __init__(self):
        self.history = []

    def calculate(self, operation, a, b):
        result = operation(a, b)
        self.history.append(result)
        return result

# Using the module
import math_operations
print(math_operations.add(1, 2))
print(math_operations.PI)

from math_operations import add, subtract
print(add(1, 2))

from math_operations import *
print(add(1, 2))
```

### Creating Packages

```
mypackage/
├── __init__.py
├── module1.py
├── module2.py
└── subpackage/
    ├── __init__.py
    └── submod.py
```

```python
# mypackage/__init__.py
from .module1 import Class1
from .module2 import Class2

__all__ = ['Class1', 'Class2']
__version__ = '1.0.0'

# Using the package
import mypackage
from mypackage import Class1
from mypackage.subpackage import submod
```

### Module Attributes

```python
# mymodule.py
def my_function():
    pass

class MyClass:
    pass

# These are available
print(__name__)      # '__main__' if run directly, 'mymodule' if imported
print(__file__)      # '/path/to/mymodule.py'
print(__doc__)       # Module docstring
print(__package__)   # '' for top-level, 'package' for subpackage

# if __name__ == "__main__" pattern
if __name__ == "__main__":
    # Code that only runs when executed directly
    my_function()
```

### Standard Library Highlights

```python
# os - Operating system interface
import os
os.getcwd()
os.listdir('.')
os.path.exists('file.txt')

# sys - System-specific parameters
import sys
sys.path
sys.version
sys.argv

# collections - Container data types
from collections import Counter, defaultdict, namedtuple
c = Counter(['a', 'b', 'a', 'c'])
d = defaultdict(int)

# datetime - Date and time
from datetime import datetime, timedelta
now = datetime.now()
tomorrow = now + timedelta(days=1)

# json - JSON encoder/decoder
import json
data = {"name": "Alice", "age": 30}
json_str = json.dumps(data)
parsed = json.loads(json_str)

# itertools - Iterator building blocks
import itertools
chain = itertools.chain([1, 2], [3, 4])
grouped = itertools.groupby(sorted_data, key=lambda x: x[0])

# functools - Higher-order functions
from functools import lru_cache, partial, reduce
cached_func = lru_cache(maxsize=128)(expensive_func)
double = partial(multiply, 2)
```

---

## Error Handling

### Exception Basics

```python
# Try-except-else-finally
try:
    result = 10 / 0
except ZeroDivisionError as e:
    print(f"Error: {e}")
else:
    print("No error occurred")
finally:
    print("This always runs")

# Catching multiple exceptions
try:
    value = int(input("Enter a number: "))
    result = 100 / value
except ValueError:
    print("Invalid input")
except ZeroDivisionError:
    print("Cannot divide by zero")
except (ValueError, ZeroDivisionError):
    print("Input error or division by zero")
except Exception as e:
    print(f"Unexpected error: {e}")
```

### Raising Exceptions

```python
def validate_age(age):
    if not isinstance(age, int):
        raise TypeError("Age must be an integer")
    if age < 0 or age > 150:
        raise ValueError(f"Invalid age: {age}")
    return True

# Custom exceptions
class CustomError(Exception):
    def __init__(self, message, code):
        super().__init__(message)
        self.code = code

class ValidationError(CustomError):
    def __init__(self, field, message):
        super().__init__(message, 400)
        self.field = field

try:
    raise ValidationError("email", "Invalid email format")
except ValidationError as e:
    print(f"Error in {e.field}: {e}")
```

### Exception Chaining

```python
try:
    open("nonexistent.txt")
except FileNotFoundError as e:
    raise RuntimeError("Failed to load config") from e

# Suppressing exception context
try:
    open("nonexistent.txt")
except FileNotFoundError:
    raise RuntimeError("Failed to load config") from None
```

### Context Manager Exception Handling

```python
class ManagedFile:
    def __init__(self, filename, mode):
        self.filename = filename
        self.mode = mode
        self.file = None

    def __enter__(self):
        self.file = open(self.filename, self.mode)
        return self.file

    def __exit__(self, exc_type, exc_val, exc_tb):
        if self.file:
            self.file.close()
        if exc_type is not None:
            print(f"Exception occurred: {exc_val}")
        return False  # Don't suppress exceptions
```

---

## Type Hints (Python 3.5+)

```python
from typing import List, Dict, Tuple, Optional, Union, Callable, Type

# Basic type hints
def greet(name: str) -> str:
    return f"Hello, {name}!"

# Collection type hints
def process_items(items: List[str]) -> Dict[str, int]:
    return {item: len(item) for item in items}

# Optional and Union
def find_user(user_id: int) -> Optional[str]:
    if user_id == 1:
        return "Alice"
    return None

def merge(a: Union[str, int], b: Union[str, int]) -> str:
    return f"{a}{b}"

# Callable type hints
def apply(func: Callable[[int, int], int], a: int, b: int) -> int:
    return func(a, b)

# Generic types
from typing import TypeVar, Generic

T = TypeVar('T')

class Stack(Generic[T]):
    def __init__(self) -> None:
        self._items: List[T] = []

    def push(self, item: T) -> None:
        self._items.append(item)

    def pop(self) -> T:
        return self._items.pop()

# TypedDict
from typing import TypedDict

class UserDict(TypedDict):
    name: str
    age: int
    email: str
```

---

## Summary

Python fundamentals cover a wide range of topics from basic variables and types to advanced concepts like decorators and context managers. Key takeaways:

- **Dynamic typing** with optional type hints
- **Rich standard library** with modules for common tasks
- **Multiple programming approachs**: procedural, object-oriented, functional
- **Comprehensive error handling** with try-except-finally
- **Comprehensions** for concise data transformation
- **Decorators** for modifying function behavior
- **Context managers** for resource management
- **Modules and packages** for code organization
