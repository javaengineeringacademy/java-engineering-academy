# Python Collections

A detailed guide to Python's built-in collection types and the collections module.

## Table of Contents

- [Built-in Collections](#built-in-collections)
  - [list](#list)
  - [tuple](#tuple)
  - [dict](#dict)
  - [set](#set)
- [collections Module](#collections-module)
  - [deque](#deque)
  - [Counter](#counter)
  - [defaultdict](#defaultdict)
  - [OrderedDict](#ordereddict)
  - [namedtuple](#namedtuple)
  - [ChainMap](#chainmap)
  - [UserDict/UserList/UserString](#userdictuserlistuserstring)
- [Performance Comparison](#performance-comparison)
- [Best Practices](#best-practices)

---

## Built-in Collections

### list

An ordered, mutable collection.

```python
# Creation
empty = []
numbers = [1, 2, 3, 4, 5]
mixed = [1, "hello", 3.14, True, None]
nested = [[1, 2], [3, 4], [5, 6]]
from_iterable = list(range(10))
from_string = list("hello")  # ['h', 'e', 'l', 'l', 'o']

# Access
first = numbers[0]       # 1
last = numbers[-1]       # 5
middle = numbers[1:3]    # [2, 3]
every_other = numbers[::2]  # [1, 3, 5]

# Methods
numbers.append(6)           # Add to end
numbers.insert(0, 0)        # Insert at index
numbers.extend([7, 8])      # Extend with iterable
numbers.remove(3)           # Remove first occurrence
popped = numbers.pop()      # Remove and return last
popped_idx = numbers.pop(0) # Remove and return at index
numbers.sort()              # Sort in place
numbers.reverse()           # Reverse in place
count = numbers.count(2)    # Count occurrences
idx = numbers.index(2)      # Find index of value
numbers.clear()             # Remove all elements

# List operations
a = [1, 2, 3]
b = [4, 5, 6]
combined = a + b        # [1, 2, 3, 4, 5, 6]
repeated = a * 3        # [1, 2, 3, 1, 2, 3, 1, 2, 3]
length = len(a)         # 3
exists = 2 in a         # True

# List comprehensions
squares = [x**2 for x in range(10)]
evens = [x for x in range(20) if x % 2 == 0]
flattened = [num for row in matrix for num in row]
```

### tuple

An ordered, immutable collection.

```python
# Creation
empty = ()
single = (1,)  # Note the comma
numbers = (1, 2, 3, 4, 5)
mixed = (1, "hello", 3.14)
nested = ((1, 2), (3, 4))
from_list = tuple([1, 2, 3])

# Access (same as list)
first = numbers[0]
last = numbers[-1]
slice = numbers[1:3]

# Tuple methods
count = numbers.count(2)
index = numbers.index(3)

# Tuple unpacking
a, b, c = (1, 2, 3)
x, y, *rest = (1, 2, 3, 4, 5)  # x=1, y=2, rest=[3, 4, 5]
first, *middle, last = (1, 2, 3, 4, 5)  # first=1, middle=[2,3,4], last=5

# Named tuples
from collections import namedtuple
Point = namedtuple('Point', ['x', 'y'])
p = Point(1, 2)
print(p.x, p.y)  # 1 2

# Tuple as dictionary key (immutable)
locations = {(0, 0): "origin", (1, 0): "right"}
```

### dict

An ordered (Python 3.7+), mutable key-value collection.

```python
# Creation
empty = {}
literal = {"name": "Alice", "age": 30}
from_pairs = dict([("a", 1), ("b", 2)])
from_kwargs = dict(name="Alice", age=30)
from_keys = dict.fromkeys(["a", "b", "c"], 0)
merged = {**dict1, **dict2}  # Python 3.5+

# Access
value = dict["key"]           # KeyError if missing
value = dict.get("key")       # None if missing
value = dict.get("key", 0)    # Default value if missing

# Methods
dict["new_key"] = value       # Add/update
dict.update({"a": 1, "b": 2})  # Update multiple
dict.pop("key")               # Remove and return
dict.pop("key", default)      # Remove with default
dict.popitem()                # Remove last inserted
del dict["key"]               # Delete key
dict.clear()                  # Remove all

# Iteration
for key in dict:              # Iterate keys
    pass
for key, value in dict.items():  # Iterate items
    pass
for value in dict.values():   # Iterate values
    pass

# Dictionary comprehensions
squares = {x: x**2 for x in range(10)}
filtered = {k: v for k, v in dict.items() if v > 10}
inverted = {v: k for k, v in dict.items()}

# Set operations on dicts
merged = {**dict1, **dict2}  # Merge (last wins)
```

### set

An unordered, mutable collection of unique elements.

```python
# Creation
empty = set()  # Note: {} creates empty dict
numbers = {1, 2, 3, 4, 5}
from_iterable = set([1, 2, 2, 3, 3, 3])  # {1, 2, 3}
from_string = set("hello")  # {'h', 'e', 'l', 'o'}

# Methods
numbers.add(6)            # Add element
numbers.update([7, 8])    # Add multiple
numbers.remove(3)         # Remove (KeyError if missing)
numbers.discard(3)        # Remove (no error if missing)
popped = numbers.pop()    # Remove and return arbitrary element
numbers.clear()           # Remove all

# Set operations
a = {1, 2, 3, 4}
b = {3, 4, 5, 6}

# Union (all elements)
a | b           # {1, 2, 3, 4, 5, 6}
a.union(b)

# Intersection (common elements)
a & b           # {3, 4}
a.intersection(b)

# Difference (elements in a but not b)
a - b           # {1, 2}
a.difference(b)

# Symmetric difference (elements in either but not both)
a ^ b           # {1, 2, 5, 6}
a.symmetric_difference(b)

# Subset/Superset
a <= b          # a is subset of b
a >= b          # a is superset of b
a.issubset(b)
a.issuperset(b)

# Frozen set (immutable)
frozen = frozenset([1, 2, 3])
```

---

## collections Module

### deque

Double-ended queue for O(1) appends and pops from both ends.

```python
from collections import deque

# Creation
dq = deque()
dq = deque([1, 2, 3])
dq = deque("hello")
dq = deque(range(10), maxlen=5)  # Bounded deque

# Methods
dq.append(4)            # Add to right
dq.appendleft(0)        # Add to left
dq.extend([5, 6])       # Extend right
dq.extendleft([−1, −2]) # Extend left (reversed)
dq.pop()                # Remove from right
dq.popleft()            # Remove from left
dq.remove(3)            # Remove first occurrence
dq.insert(2, 10)        # Insert at index
dq.rotate(1)            # Rotate right
dq.rotate(-1)           # Rotate left
dq.reverse()            # Reverse in place
dq.clear()              # Remove all

# Memory efficient
# deque uses a doubly-linked list of blocks
# Each block holds 64 items
# More memory efficient for large collections with frequent两端操作

# Practical uses
# Sliding window
def sliding_window(iterable, size):
    it = iter(iterable)
    window = deque(maxlen=size)
    for _ in range(size):
        window.append(next(it))
    yield tuple(window)
    for item in it:
        window.append(item)
        yield tuple(window)

list(sliding_window([1, 2, 3, 4, 5], 3))
# [(1, 2, 3), (2, 3, 4), (3, 4, 5)]

# BFS with deque
def bfs(graph, start):
    visited = set()
    queue = deque([start])
    visited.add(start)
    while queue:
        vertex = queue.popleft()
        for neighbor in graph[vertex]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)
```

### Counter

Dictionary subclass for counting hashable objects.

```python
from collections import Counter

# Creation
c = Counter()
c = Counter(['red', 'blue', 'red'])
c = Counter("hello world")
c = Counter(a=4, b=2, c=0)
c = Counter({"red": 4, "blue": 2})

# Methods
c['red']          # Count (0 if missing)
c.get('green', 0) # Count with default
c.elements()      # Iterator over elements
c.most_common(3)  # Top 3 most common
c.total()         # Sum of all counts (Python 3.10+)
c.update(['red', 'red', 'green'])  # Add counts
c.subtract(['red'])  # Subtract counts

# Arithmetic
c1 = Counter(a=3, b=1)
c2 = Counter(a=1, b=2)
c1 + c2    # Counter(a=4, b=3)
c1 - c2    # Counter(a=2)
c1 & c2    # Counter(a=1) (min)
c1 | c2    # Counter(a=3, b=2) (max)

# Practical uses
# Word frequency
text = "the cat sat on the mat the cat"
words = text.split()
word_counts = Counter(words)
print(word_counts.most_common(2))  # [('the', 3), ('cat', 2)]

# Find duplicates
from itertools import chain
lists = [[1, 2, 3], [2, 3, 4], [3, 4, 5]]
all_items = chain.from_iterable(lists)
duplicates = [item for item, count in Counter(all_items).items() if count > 1]
print(duplicates)  # [2, 3, 4]
```

### defaultdict

Dictionary with default value for missing keys.

```python
from collections import defaultdict

# Creation
dd = defaultdict(int)        # Default 0
dd = defaultdict(float)      # Default 0.0
dd = defaultdict(str)        # Default ''
dd = defaultdict(list)       # Default []
dd = defaultdict(set)        # Default set()
dd = defaultdict(lambda: "N/A")  # Custom default

# Usage
dd = defaultdict(int)
for word in "hello world":
    dd[word] += 1
print(dict(dd))  # {'h': 1, 'e': 1, 'l': 3, 'o': 2, ' ': 1, 'w': 1, 'r': 1, 'd': 1}

# Grouping
dd = defaultdict(list)
items = [("fruit", "apple"), ("fruit", "banana"), ("veggie", "carrot")]
for category, item in items:
    dd[category].append(item)
print(dict(dd))  # {'fruit': ['apple', 'banana'], 'veggie': ['carrot']}

# Counting
dd = defaultdict(int)
for char in "mississippi":
    dd[char] += 1
print(dict(dd))  # {'m': 1, 'i': 4, 's': 4, 'p': 2}

# Matrix initialization
def create_matrix(rows, cols, default=0):
    return defaultdict(lambda: defaultdict(lambda: default))

matrix = create_matrix(3, 3)
matrix[1][2] = 5
```

### OrderedDict

Dictionary that maintains insertion order (Python 3.7+ dict also does this).

```python
from collections import OrderedDict

# Creation
od = OrderedDict()
od = OrderedDict([('b', 2), ('a', 1), ('c', 3)])

# Methods
od.move_to_end('b')        # Move to end
od.move_to_end('c', last=False)  # Move to beginning
od.popitem(last=True)      # Remove last item
od.popitem(last=False)     # Remove first item

# Equality comparison considers order
od1 = OrderedDict([('a', 1), ('b', 2)])
od2 = OrderedDict([('b', 2), ('a', 1)])
print(od1 == od2)  # False (different order)

# LRU Cache implementation
class LRUCache:
    def __init__(self, capacity):
        self.cache = OrderedDict()
        self.capacity = capacity

    def get(self, key):
        if key in self.cache:
            self.cache.move_to_end(key)
            return self.cache[key]
        return -1

    def put(self, key, value):
        if key in self.cache:
            self.cache.move_to_end(key)
        self.cache[key] = value
        if len(self.cache) > self.capacity:
            self.cache.popitem(last=False)
```

### namedtuple

Lightweight immutable class created from a tuple.

```python
from collections import namedtuple

# Creation
Point = namedtuple('Point', ['x', 'y'])
Point = namedtuple('Point', 'x y')
Point = namedtuple('Point', 'x, y')

# Instance
p = Point(1, 2)
print(p.x, p.y)  # 1 2

# Methods
p._asdict()         # OrderedDict({'x': 1, 'y': 2})
p._replace(x=10)    # Point(x=10, y=2)
p._fields           # ('x', 'y')
Point._make([3, 4])  # Point(x=3, y=4)

# Inheritance
class Point3D(Point):
    def __new__(cls, x, y, z):
        return super().__new__(cls, x, y)

    def __init__(self, x, y, z):
        self.z = z

# Practical uses
Employee = namedtuple('Employee', 'name age department')
emp = Employee("Alice", 30, "Engineering")
print(f"{emp.name} works in {emp.department}")

# Replacing dataclass for simple cases
from typing import NamedTuple

class Point(NamedTuple):
    x: float
    y: float
    z: float = 0.0

p = Point(1.0, 2.0)
```

### ChainMap

Group multiple dicts into a single view.

```python
from collections import ChainMap

# Creation
dict1 = {'a': 1, 'b': 2}
dict2 = {'b': 3, 'c': 4}
cm = ChainMap(dict1, dict2)

# Access (first dict wins)
print(cm['a'])  # 1
print(cm['b'])  # 2 (from dict1)

# Methods
cm.maps            # List of dicts
cm.new_child()     # New empty map added to front
cm.parents         # ChainMap without first map

# Practical uses
# Default values
defaults = {'color': 'red', 'user': 'guest'}
overrides = {'user': 'admin'}
config = ChainMap(overrides, defaults)
print(config['user'])   # 'admin'
print(config['color'])  # 'red'

# Nested scopes
defscope = {'x': 10}
global_scope = {'x': 1}
local_scope = {'x': 5}
scope = ChainMap(local_scope, defscope, global_scope)
print(scope['x'])  # 5 (local wins)
```

### UserDict/UserList/UserString

Customizable versions of built-in types.

```python
from collections import UserDict, UserList, UserString

# UserDict
class StatsDict(UserDict):
    def __setitem__(self, key, value):
        super().__setitem__(key, value)
        self._update_stats()

    def _update_stats(self):
        self.sum = sum(self.data.values())
        self.count = len(self.data)

stats = StatsDict()
stats['a'] = 10
stats['b'] = 20
print(stats.sum)   # 30
print(stats.count) # 2

# UserList
class UniqueList(UserList):
    def append(self, item):
        if item not in self.data:
            super().append(item)

ul = UniqueList()
ul.append(1)
ul.append(1)  # Ignored
ul.append(2)
print(ul)  # [1, 2]

# UserString
class CapitalizedString(UserString):
    def __init__(self, seq):
        super().__init__(seq.upper())

cs = CapitalizedString("hello")
print(cs)  # HELLO
```

---

## Performance Comparison

### Time Complexity

| Operation | list | deque | dict | set |
|-----------|------|-------|------|-----|
| Access by index | O(1) | O(n) | N/A | N/A |
| Access by key | N/A | N/A | O(1) avg | N/A |
| Search | O(n) | O(n) | O(1) avg | O(1) avg |
| Insert at beginning | O(n) | O(1) | N/A | N/A |
| Insert at end | O(1) | O(1) | O(1) avg | O(1) avg |
| Insert in middle | O(n) | O(n) | N/A | N/A |
| Delete at beginning | O(n) | O(1) | N/A | N/A |
| Delete at end | O(1) | O(1) | O(1) avg | O(1) avg |

### Memory Usage

```python
import sys

# Memory comparison
list_obj = [1, 2, 3, 4, 5]
tuple_obj = (1, 2, 3, 4, 5)
set_obj = {1, 2, 3, 4, 5}
dict_obj = {1: 1, 2: 2, 3: 3, 4: 4, 5: 5}

print(f"list: {sys.getsizeof(list_obj)} bytes")
print(f"tuple: {sys.getsizeof(tuple_obj)} bytes")
print(f"set: {sys.getsizeof(set_obj)} bytes")
print(f"dict: {sys.getsizeof(dict_obj)} bytes")

# Typical results:
# list: 104 bytes
# tuple: 80 bytes
# set: 728 bytes (empty set is 216 bytes)
# dict: 232 bytes (empty dict is 64 bytes)
```

### Benchmarking

```python
import timeit
from collections import deque, defaultdict, Counter

# List vs deque for left operations
list_time = timeit.timeit(
    'lst.insert(0, 1)',
    setup='lst = list(range(10000))',
    number=10000
)
deque_time = timeit.timeit(
    'dq.appendleft(1)',
    setup='from collections import deque; dq = deque(range(10000))',
    number=10000
)
print(f"List insert(0): {list_time:.4f}s")
print(f"Deque appendleft: {deque_time:.4f}s")

# dict vs defaultdict for counting
text = "hello world " * 10000

dict_time = timeit.timeit(
    '''d = {}
    for c in text:
        d[c] = d.get(c, 0) + 1''',
    number=1000
)
dd_time = timeit.timeit(
    '''from collections import defaultdict
    d = defaultdict(int)
    for c in text:
        d[c] += 1''',
    number=1000
)
print(f"dict.get: {dict_time:.4f}s")
print(f"defaultdict: {dd_time:.4f}s")
```

---

## Best Practices

### When to Use What

```python
# Use list when:
# - You need ordered collection
# - You need to access by index
# - You need to modify (add/remove) elements
items = [1, 2, 3]

# Use tuple when:
# - You need immutable sequence
# - You need to use as dictionary key
# - You need to return multiple values
point = (1, 2)

# Use dict when:
# - You need key-value mapping
# - You need fast lookup by key
config = {"debug": True}

# Use set when:
# - You need unique elements
# - You need set operations (union, intersection)
unique = {1, 2, 3}

# Use deque when:
# - You need fast appends/pops from both ends
# - You need a sliding window
queue = deque()

# Use Counter when:
# - You need to count occurrences
word_counts = Counter(text.split())

# Use defaultdict when:
# - You need to group items
# - You need automatic initialization
groups = defaultdict(list)
```

### Common Patterns

```python
from collections import defaultdict, Counter, deque

# Group by
def group_by(items, key_func):
    result = defaultdict(list)
    for item in items:
        result[key_func(item)].append(item)
    return dict(result)

# Flatten nested list
def flatten(nested):
    for sublist in nested:
        for item in sublist:
            yield item

# Remove duplicates while preserving order
def remove_duplicates(items):
    seen = set()
    result = []
    for item in items:
        if item not in seen:
            seen.add(item)
            result.append(item)
    return result

# Sliding window
def sliding_window(iterable, size):
    it = iter(iterable)
    window = deque(maxlen=size)
    for _ in range(size):
        window.append(next(it))
    yield tuple(window)
    for item in it:
        window.append(item)
        yield tuple(window)
```

---

## Summary

Python's collections provide efficient data structures for various use cases:

- **list**: General-purpose ordered mutable sequence
- **tuple**: Immutable ordered sequence
- **dict**: Key-value mapping (O(1) lookup)
- **set**: Unique elements with set operations
- **deque**: Double-ended queue (O(1) both ends)
- **Counter**: Counting occurrences
- **defaultdict**: Auto-initializing dictionary
- **OrderedDict**: Order-preserving dictionary
- **namedtuple**: Lightweight immutable class
- **ChainMap**: Multiple dictionary views

Choose the right collection based on your specific needs for ordering, mutability, and access patterns.
