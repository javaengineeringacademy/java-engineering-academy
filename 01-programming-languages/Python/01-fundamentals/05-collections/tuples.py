"""Tuple operations, immutability, and named tuples."""

# ── Creating Tuples ──────────────────────────────────────────────────
empty = ()
single = (42,)               # Comma required for single-element tuple
not_tuple = (42)             # This is just an int!
multi = (1, 2, 3)
nested = ((1, 2), (3, 4))

# From iterable
from_list = tuple([1, 2, 3])
from_string = tuple("abc")  # ('a', 'b', 'c')
from_range = tuple(range(5))  # (0, 1, 2, 3, 4)

# ── Immutability ─────────────────────────────────────────────────────
t = (1, 2, 3)
# t[0] = 5  # TypeError — tuples are immutable

# But mutable elements inside can change
t2 = ([1, 2], [3, 4])
t2[0].append(3)  # ([1, 2, 3], [3, 4]) — the list changed

# ── Indexing and Slicing ─────────────────────────────────────────────
data = (10, 20, 30, 40, 50)
print(data[0])      # 10
print(data[-1])     # 50
print(data[1:3])    # (20, 30)
print(data[::-1])   # (50, 40, 30, 20, 10)

# ── Tuple Methods ────────────────────────────────────────────────────
t = (1, 2, 3, 2, 2, 4)
print(t.count(2))    # 3
print(t.index(3))    # 2 (first occurrence)

# ── Tuple Unpacking ──────────────────────────────────────────────────
point = (10, 20)
x, y = point

# Nested unpacking
nested = ((1, 2), (3, 4))
(a, b), (c, d) = nested

# Star unpacking
first, *rest = (1, 2, 3, 4, 5)
# first=1, rest=[2, 3, 4, 5]

*head, last = (1, 2, 3, 4, 5)
# head=[1, 2, 3, 4], last=5

# Swap variables (Pythonic)
a, b = b, a

# ── Named Tuples ─────────────────────────────────────────────────────
from collections import namedtuple

# Define a type
Point = namedtuple("Point", ["x", "y"])
p = Point(10, 20)
print(p.x, p.y)       # 10 20
print(p[0], p[1])     # 10 20 (also indexable)
print(p._asdict())    # {'x': 10, 'y': 20}

# With defaults
Color = namedtuple("Color", "red green blue", defaults=[0, 0, 0])
red = Color(255)
print(red)  # Color(red=255, green=0, blue=0)

# ── When to Use Tuples vs Lists ──────────────────────────────────────
# Tuples: immutable data, dict keys, function returns, records
# Lists: mutable collections, homogeneous data, stacks/queues

# Tuple as dict key
locations = {(40.7, -74.0): "New York", (51.5, -0.1): "London"}

# Tuple as record
record = ("Alice", 30, "Engineer")
name, age, role = record  # Destructuring

# ── Performance ──────────────────────────────────────────────────────
# Tuples are slightly faster than lists
# Tuples use less memory
import sys
print(sys.getsizeof((1, 2, 3)))   # ~64 bytes
print(sys.getsizeof([1, 2, 3]))   # ~88 bytes
