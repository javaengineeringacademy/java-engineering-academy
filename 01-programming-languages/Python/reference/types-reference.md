# Python Types Reference

## What are Python Types?

Python types are the building blocks of data in Python. Every value in Python has a type that determines what operations can be performed on it and how it's stored in memory.

## Why does Python Types matter?

Understanding types helps you:
- Choose the right data structure for your needs
- Avoid type-related bugs
- Optimize memory usage
- Write more efficient code

---

## 1. Numeric Types

### int

Integers are whole numbers without decimal points.

```python
# Basic integers
x = 42
y = -100
z = 0

# Different bases
binary = 0b1010      # 10
octal = 0o17         # 15
hexadecimal = 0xFF   # 255

# Large integers (arbitrary precision)
big = 10**100
print(big)  # 1 followed by 100 zeros

# Integer methods
print(abs(-42))        # 42
print(pow(2, 10))     # 1024
print(divmod(17, 5))  # (3, 2)

# Bit operations
print(12 & 8)   # 8 (AND)
print(12 | 8)   # 12 (OR)
print(12 ^ 8)   # 4 (XOR)
print(12 << 2)  # 48 (left shift)
print(12 >> 2)  # 3 (right shift)
```

### float

Floating-point numbers are decimal numbers.

```python
# Basic floats
x = 3.14
y = -2.5
z = 0.0

# Scientific notation
large = 1.5e10    # 15000000000.0
small = 1.5e-10   # 1.5e-10

# Special values
import math
print(math.inf)    # inf
print(-math.inf)   # -inf
print(math.nan)    # nan

# Float precision issues
print(0.1 + 0.2)  # 0.30000000000000004

# Use decimal for precision
from decimal import Decimal
print(Decimal('0.1') + Decimal('0.2'))  # 0.3

# Float methods
print((3.14).as_integer_ratio())  # (157, 50)
print((3.14).is_integer())        # False
```

### complex

Complex numbers have real and imaginary parts.

```python
# Basic complex numbers
z1 = 3 + 4j
z2 = complex(3, 4)

# Access parts
print(z1.real)    # 3.0
print(z1.imag)    # 4.0

# Operations
z3 = 1 + 2j
z4 = 3 + 4j
print(z1 + z3)    # (4+6j)
print(z1 * z3)    # (-5+10j)

# Conjugate
print(z1.conjugate())  # (3-4j)

# Absolute value (magnitude)
print(abs(z1))    # 5.0
```

---

## 2. Boolean Type

### bool

Booleans are a subclass of int with two values: True and False.

```python
# Basic booleans
t = True
f = False

# Boolean operations
print(True and False)   # False
print(True or False)    # True
print(not True)         # False

# Booleans as integers
print(True + True)      # 2
print(True * 10)        # 10
print(False + 5)        # 5

# Truthiness
print(bool(0))          # False
print(bool(1))          # True
print(bool(""))         # False
print(bool("hello"))    # True
print(bool([]))         # False
print(bool([1, 2, 3]))  # True
print(bool(None))       # False

# Boolean conversion
print(int(True))        # 1
print(int(False))       # 0
```

---

## 3. Sequence Types

### str

Strings are immutable sequences of Unicode characters.

```python
# Basic strings
s1 = 'hello'
s2 = "world"
s3 = '''Multi-line
string'''
s4 = """Another
multi-line
string"""

# String operations
print(len("hello"))         # 5
print("hello" + " world")  # hello world
print("ha" * 3)            # hahaha
print("hello"[0])          # h

# String methods
s = "Hello, World!"
print(s.lower())           # hello, world!
print(s.upper())           # HELLO, WORLD!
print(s.replace("World", "Python"))  # Hello, Python!
print(s.split(", "))       # ['Hello', 'World!']
print(s.find("World"))     # 7
print(s.count("l"))        # 3

# String formatting
name = "Alice"
age = 30
print(f"{name} is {age} years old")
print("{} is {} years old".format(name, age))
print("%s is %d years old" % (name, age))

# String encoding
s = "Hello"
b = s.encode('utf-8')  # b'Hello'
print(b.decode('utf-8'))  # Hello
```

### bytes

Bytes are immutable sequences of bytes (integers 0-255).

```python
# Basic bytes
b1 = b'hello'
b2 = bytes([72, 101, 108, 108, 111])

# Operations
print(len(b1))           # 5
print(b1[0])             # 104 (ASCII for 'h')
print(b1 + b' world')   # b'hello world'

# Methods
print(b1.upper())        # b'HELLO'
print(b1.replace(b'h', b'j'))  # b'jello'

# Converting between bytes and strings
s = "Hello"
b = s.encode('utf-8')  # bytes
s2 = b.decode('utf-8')  # string

# Bytearray (mutable)
ba = bytearray(b'hello')
ba[0] = 74  # 'J'
print(ba)   # bytearray(b'Jello')
```

### list

Lists are mutable sequences.

```python
# Basic lists
lst1 = [1, 2, 3]
lst2 = ['a', 'b', 'c']
lst3 = [1, 'two', 3.0, [4, 5]]

# Operations
print(len(lst1))           # 3
print(lst1 + [4, 5])      # [1, 2, 3, 4, 5]
print(lst1 * 2)           # [1, 2, 3, 1, 2, 3]
print(lst1[0])            # 1
print(lst1[-1])           # 3
print(lst1[0:2])          # [1, 2]

# Methods
lst = [3, 1, 4, 1, 5, 9]
lst.append(2)             # Add to end
lst.insert(0, 0)          # Insert at index
lst.extend([6, 7])        # Add multiple
lst.remove(1)             # Remove first occurrence
lst.pop()                 # Remove and return last
lst.pop(0)                # Remove and return at index
lst.sort()                # Sort in place
lst.reverse()             # Reverse in place
lst.clear()               # Remove all elements

# List comprehension
squares = [x**2 for x in range(10)]
evens = [x for x in range(10) if x % 2 == 0]
```

### tuple

Tuples are immutable sequences.

```python
# Basic tuples
t1 = (1, 2, 3)
t2 = ('a', 'b', 'c')
t3 = (1,)  # Single element tuple (note the comma)

# Operations (same as list, but no modification)
print(len(t1))           # 3
print(t1 + (4, 5))      # (1, 2, 3, 4, 5)
print(t1 * 2)           # (1, 2, 3, 1, 2, 3)
print(t1[0])            # 1

# Tuple unpacking
x, y, z = (1, 2, 3)
a, *b = (1, 2, 3, 4)  # a=1, b=[2, 3, 4]

# Named tuples
from collections import namedtuple
Point = namedtuple('Point', ['x', 'y'])
p = Point(1, 2)
print(p.x, p.y)  # 1 2
```

---

## 4. Mapping Types

### dict

Dictionaries are mutable mappings of keys to values.

```python
# Basic dictionaries
d1 = {'a': 1, 'b': 2, 'c': 3}
d2 = dict(x=1, y=2, z=3)

# Operations
print(d1['a'])           # 1
print(d1.get('d', 0))   # 0 (default if key not found)
print('a' in d1)         # True
print(len(d1))           # 3

# Methods
d = {'a': 1, 'b': 2}
d['c'] = 3               # Add/update
d.update({'d': 4, 'e': 5})  # Update multiple
d.pop('a')               # Remove and return
d.popitem()              # Remove last item
d.clear()                # Remove all items
d.keys()                 # View of keys
d.values()               # View of values
d.items()                # View of (key, value) pairs

# Dictionary comprehension
squares = {x: x**2 for x in range(10)}
evens = {x: x**2 for x in range(10) if x % 2 == 0}

# Nested dictionaries
person = {
    'name': 'Alice',
    'age': 30,
    'address': {
        'street': '123 Main St',
        'city': 'Anytown'
    }
}
```

---

## 5. Set Types

### set

Sets are mutable collections of unique elements.

```python
# Basic sets
s1 = {1, 2, 3}
s2 = set([1, 2, 3])  # From list

# Operations
print(1 in s1)          # True
print(len(s1))          # 3

# Methods
s = {1, 2, 3}
s.add(4)                # Add element
s.remove(1)             # Remove (raises KeyError if not found)
s.discard(5)            # Remove (no error if not found)
s.pop()                 # Remove and return arbitrary element
s.clear()               # Remove all elements

# Set operations
s1 = {1, 2, 3, 4}
s2 = {3, 4, 5, 6}

print(s1 | s2)          # Union: {1, 2, 3, 4, 5, 6}
print(s1 & s2)          # Intersection: {3, 4}
print(s1 - s2)          # Difference: {1, 2}
print(s1 ^ s2)          # Symmetric difference: {1, 2, 5, 6}

# Set comprehension
evens = {x for x in range(10) if x % 2 == 0}
```

### frozenset

Frozensets are immutable sets.

```python
# Basic frozenset
fs1 = frozenset([1, 2, 3])
fs2 = frozenset([3, 4, 5])

# Operations (same as set, but immutable)
print(fs1 | fs2)          # Union
print(fs1 & fs2)          # Intersection
print(fs1 - fs2)          # Difference

# Can be used as dictionary keys
d = {frozenset([1, 2]): 'first'}
```

---

## 6. NoneType

### None

None is Python's null value.

```python
# Basic None
x = None
print(x)        # None
print(type(x))  # <class 'NoneType'>

# Check for None
print(x is None)     # True
print(x is not None)  # False

# None in functions
def greet(name=None):
    if name is None:
        return "Hello, stranger!"
    return f"Hello, {name}!"

print(greet())        # Hello, stranger!
print(greet("Alice"))  # Hello, Alice!
```

---

## 7. Range Type

### range

Range represents an immutable sequence of numbers.

```python
# Basic ranges
r1 = range(5)           # 0, 1, 2, 3, 4
r2 = range(1, 10)       # 1, 2, ..., 9
r3 = range(0, 10, 2)    # 0, 2, 4, 6, 8

# Operations
print(list(r1))         # [0, 1, 2, 3, 4]
print(len(r1))          # 5
print(3 in r1)          # True
print(r1[2])            # 2

# Range is memory efficient
r = range(1000000)  # Doesn't create 1M numbers in memory
```

---

## 8. Iterator Types

### enumerate

enumerate adds a counter to an iterable.

```python
# Basic enumerate
fruits = ['apple', 'banana', 'cherry']
for i, fruit in enumerate(fruits):
    print(f"{i}: {fruit}")

# Start index at 1
for i, fruit in enumerate(fruits, 1):
    print(f"{i}. {fruit}")

# With start parameter
print(list(enumerate(['a', 'b', 'c'], 1)))  # [(1, 'a'), (2, 'b'), (3, 'c')]
```

### map

map applies a function to all items in an iterable.

```python
# Basic map
numbers = [1, 2, 3, 4, 5]
squared = map(lambda x: x**2, numbers)
print(list(squared))  # [1, 4, 9, 16, 25]

# With multiple iterables
a = [1, 2, 3]
b = [4, 5, 6]
result = map(lambda x, y: x + y, a, b)
print(list(result))  # [5, 7, 9]

# With built-in function
words = ['hello', 'world']
upper = map(str.upper, words)
print(list(upper))  # ['HELLO', 'WORLD']
```

### filter

filter creates an iterator from elements that return True.

```python
# Basic filter
numbers = [1, 2, 3, 4, 5, 6]
evens = filter(lambda x: x % 2 == 0, numbers)
print(list(evens))  # [2, 4, 6]

# With None (removes falsy values)
mixed = [0, 1, False, True, '', 'hello']
truthy = filter(None, mixed)
print(list(truthy))  # [1, True, 'hello']

# Filter with function
def is_positive(n):
    return n > 0

numbers = [-2, -1, 0, 1, 2]
positive = filter(is_positive, numbers)
print(list(positive))  # [1, 2]
```

### zip

zip combines iterables element-wise.

```python
# Basic zip
names = ['Alice', 'Bob', 'Charlie']
ages = [25, 30, 35]
combined = zip(names, ages)
print(list(combined))  # [('Alice', 25), ('Bob', 30), ('Charlie', 35)]

# With different lengths (stops at shortest)
a = [1, 2, 3]
b = ['a', 'b']
print(list(zip(a, b)))  # [(1, 'a'), (2, 'b')]

# With fill value
from itertools import zip_longest
a = [1, 2, 3]
b = ['a', 'b']
print(list(zip_longest(a, b, fillvalue='-')))  # [(1, 'a'), (2, 'b'), (3, '-')]

# Unzip
pairs = [('Alice', 25), ('Bob', 30)]
names, ages = zip(*pairs)
print(names)  # ('Alice', 'Bob')
print(ages)   # (25, 30)
```

---

## One-Minute Revision Table

| Type | Mutable | Sequence | Example |
|------|---------|----------|---------|
| **int** | No | No | `x = 42` |
| **float** | No | No | `x = 3.14` |
| **complex** | No | No | `x = 3+4j` |
| **bool** | No | No | `x = True` |
| **str** | No | Yes | `x = "hello"` |
| **bytes** | No | Yes | `x = b"hello"` |
| **list** | Yes | Yes | `x = [1, 2, 3]` |
| **tuple** | No | Yes | `x = (1, 2, 3)` |
| **dict** | Yes | No | `x = {'a': 1}` |
| **set** | Yes | No | `x = {1, 2, 3}` |
| **frozenset** | No | No | `x = frozenset([1, 2, 3])` |
| **None** | No | No | `x = None` |
| **range** | No | Yes | `x = range(5)` |

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

### 2. Modifying Strings

```python
# WRONG
s = "hello"
s[0] = 'H'  # TypeError

# RIGHT
s = "hello"
s = 'H' + s[1:]
```

### 3. Using `==` for Identity Check

```python
# WRONG
if x is None:  # Using == instead of is
    pass

# RIGHT
if x is None:
    pass
```

### 4. Integer Caching

```python
# Small integers are cached
a = 256
b = 256
print(a is b)  # True (cached)

a = 257
b = 257
print(a is b)  # False (not cached)
```

---

## Production Notes

1. **Use `collections.defaultdict` for defaultdicts** - Avoids key existence checks
2. **Use `collections.Counter` for counting** - More efficient than manual counting
3. **Use `collections.namedtuple` for simple classes** - Less memory than regular classes
4. **Use `array.array` for numeric arrays** - More memory efficient than lists
5. **Use `__slots__` for memory optimization** - Reduces instance memory usage
6. **Be careful with floating-point precision** - Use `decimal.Decimal` for financial calculations
7. **Use `frozenset` for immutable sets** - Can be used as dictionary keys
8. **Use `range` for large sequences** - Memory efficient
9. **Use generators for large datasets** - Don't load everything into memory
10. **Profile memory usage** - Use `memory_profiler` to find memory leaks

---

## Further Reading

- Python documentation on built-in types
- Python Data Model documentation
- Fluent Python by Luciano Ramalho
- Python Cookbook by David Beazley
