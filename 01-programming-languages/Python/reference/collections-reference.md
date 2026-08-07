# Python Collections Reference

## What are Python Collections?

Python collections are container data types that store groups of objects. The collections module provides specialized containers beyond the built-in list, tuple, dict, and set.

## Why does Python Collections matter?

Understanding collections helps you:
- Choose the right data structure for your needs
- Write more efficient and readable code
- Avoid common collection-related bugs
- Optimize performance for specific use cases

---

## 1. list

Lists are mutable sequences of items.

### Basic Operations

```python
# Creation
lst = [1, 2, 3]
lst = list([1, 2, 3])
lst = list(range(5))

# Access
print(lst[0])      # First element
print(lst[-1])     # Last element
print(lst[1:3])    # Slice

# Modification
lst.append(4)      # Add to end
lst.insert(0, 0)   # Insert at index
lst.extend([5, 6])  # Add multiple
lst[0] = 10        # Update element

# Removal
lst.remove(10)     # Remove first occurrence
lst.pop()          # Remove and return last
lst.pop(0)         # Remove and return at index
del lst[0]         # Delete at index
lst.clear()        # Remove all

# Searching
print(lst.index(5))  # Find index
print(lst.count(5))  # Count occurrences
print(5 in lst)      # Check membership

# Sorting
lst.sort()          # Sort in place
lst.sort(reverse=True)  # Sort descending
lst.reverse()       # Reverse in place
sorted_lst = sorted(lst)  # Return new sorted list

# Utility
print(len(lst))     # Length
print(min(lst))     # Minimum
print(max(lst))     # Maximum
print(sum(lst))     # Sum
```

### List Comprehension

```python
# Basic
squares = [x**2 for x in range(10)]

# With condition
evens = [x for x in range(10) if x % 2 == 0]

# Nested
matrix = [[i*j for j in range(3)] for i in range(3)]

# Flatten
nested = [[1, 2], [3, 4], [5, 6]]
flat = [x for sublist in nested for x in sublist]

# With function
words = ['hello', 'world']
upper = [w.upper() for w in words]
```

---

## 2. tuple

Tuples are immutable sequences of items.

### Basic Operations

```python
# Creation
t = (1, 2, 3)
t = tuple([1, 2, 3])
t = (1,)  # Single element tuple (note the comma)

# Access
print(t[0])      # First element
print(t[-1])     # Last element
print(t[1:3])    # Slice

# Unpacking
x, y, z = t
a, *b = (1, 2, 3, 4)  # a=1, b=[2, 3, 4]
a, *b, c = (1, 2, 3, 4)  # a=1, b=[2, 3], c=4

# Named tuples
from collections import namedtuple
Point = namedtuple('Point', ['x', 'y'])
p = Point(1, 2)
print(p.x, p.y)  # 1 2
```

### Tuple Methods

```python
t = (1, 2, 3, 2, 1)

print(t.count(2))  # 2
print(t.index(3))  # 2
print(len(t))      # 5
print(min(t))      # 1
print(max(t))      # 3
print(sum(t))      # 9
```

---

## 3. dict

Dictionaries are mutable mappings of keys to values.

### Basic Operations

```python
# Creation
d = {'a': 1, 'b': 2, 'c': 3}
d = dict(a=1, b=2, c=3)
d = dict([('a', 1), ('b', 2)])

# Access
print(d['a'])           # Get value
print(d.get('d', 0))   # Get with default
print(d.keys())        # View of keys
print(d.values())      # View of values
print(d.items())       # View of (key, value) pairs

# Modification
d['d'] = 4              # Add/update
d.update({'e': 5, 'f': 6})  # Update multiple
d.setdefault('g', 7)    # Set if not exists

# Removal
del d['a']              # Delete key
d.pop('b')              # Remove and return
d.popitem()             # Remove last item
d.clear()               # Remove all

# Iteration
for key in d:
    print(key, d[key])

for key, value in d.items():
    print(key, value)
```

### Dictionary Comprehension

```python
# Basic
squares = {x: x**2 for x in range(10)}

# With condition
evens = {x: x**2 for x in range(10) if x % 2 == 0}

# Swap keys and values
d = {'a': 1, 'b': 2}
swapped = {v: k for k, v in d.items()}

# Merge dictionaries
d1 = {'a': 1}
d2 = {'b': 2}
merged = {**d1, **d2}  # Python 3.5+
```

### Dictionary Methods

```python
d = {'a': 1, 'b': 2}

# Get
print(d.get('a'))        # 1
print(d.get('c', 0))    # 0

# Set
d.setdefault('c', 3)    # d = {'a': 1, 'b': 2, 'c': 3}

# Update
d.update({'d': 4, 'e': 5})

# Remove
d.pop('a')
d.popitem()
del d['b']
d.clear()
```

---

## 4. set

Sets are mutable collections of unique elements.

### Basic Operations

```python
# Creation
s = {1, 2, 3}
s = set([1, 2, 3])

# Add
s.add(4)
s.update([5, 6, 7])

# Remove
s.remove(1)      # Raises KeyError if not found
s.discard(8)     # No error if not found
s.pop()          # Remove arbitrary element
s.clear()        # Remove all

# Membership
print(1 in s)    # True
print(len(s))    # Length
```

### Set Operations

```python
s1 = {1, 2, 3, 4}
s2 = {3, 4, 5, 6}

# Union
print(s1 | s2)          # {1, 2, 3, 4, 5, 6}
print(s1.union(s2))

# Intersection
print(s1 & s2)          # {3, 4}
print(s1.intersection(s2))

# Difference
print(s1 - s2)          # {1, 2}
print(s1.difference(s2))

# Symmetric Difference
print(s1 ^ s2)          # {1, 2, 5, 6}
print(s1.symmetric_difference(s2))

# Subset and Superset
s3 = {1, 2}
print(s3.issubset(s1))      # True
print(s1.issuperset(s3))    # True

# Disjoint
s4 = {7, 8}
print(s1.isdisjoint(s4))    # True
```

### Set Comprehension

```python
# Basic
evens = {x for x in range(10) if x % 2 == 0}

# From string
unique_chars = {c for c in "hello"}
```

---

## 5. frozenset

Frozensets are immutable sets.

```python
# Creation
fs = frozenset([1, 2, 3])

# Operations (same as set, but immutable)
print(fs | frozenset([4, 5]))  # Union
print(fs & frozenset([2, 3]))  # Intersection
print(fs - frozenset([2]))     # Difference

# Can be used as dictionary keys
d = {frozenset([1, 2]): 'first'}
```

---

## 6. collections Module

### Counter

```python
from collections import Counter

# Creation
c = Counter(['a', 'b', 'a', 'c', 'b', 'a'])
print(c)  # Counter({'a': 3, 'b': 2, 'c': 1})

# Access
print(c['a'])        # 3
print(c.get('d', 0))  # 0

# Update
c.update(['a', 'a', 'd'])
c.subtract(['a'])

# Common operations
print(c.most_common(2))  # [('a', 4), ('b', 2)]
print(c.total())         # 6
```

### defaultdict

```python
from collections import defaultdict

# Default value
dd = defaultdict(int)
dd['a'] += 1
print(dd)  # defaultdict(<class 'int'>, {'a': 1})

# With list
dd = defaultdict(list)
dd['a'].append(1)
dd['a'].append(2)
print(dd)  # defaultdict(<class 'list'>, {'a': [1, 2]})

# With lambda
dd = defaultdict(lambda: 'default')
print(dd['missing'])  # 'default'
```

### OrderedDict

```python
from collections import OrderedDict

# Creation
od = OrderedDict()
od['a'] = 1
od['b'] = 2
od['c'] = 3

# Move to end
od.move_to_end('a')
print(od)  # OrderedDict([('b', 2), ('c', 3), ('a', 1)])

# Pop last
od.popitem(last=True)

# Pop first
od.popitem(last=False)
```

### deque

```python
from collections import deque

# Creation
dq = deque([1, 2, 3])
dq = deque(maxlen=5)

# Operations
dq.append(4)         # Add to right
dq.appendleft(0)     # Add to left
dq.extend([5, 6])    # Add multiple to right
dq.extendleft([-1, -2])  # Add multiple to left

dq.pop()             # Remove from right
dq.popleft()         # Remove from left

dq.rotate(1)         # Rotate right
dq.rotate(-1)        # Rotate left

dq.reverse()         # Reverse in place
```

### ChainMap

```python
from collections import ChainMap

# Creation
d1 = {'a': 1, 'b': 2}
d2 = {'c': 3, 'd': 4}
cm = ChainMap(d1, d2)

# Access
print(cm['a'])  # 1
print(cm['c'])  # 3

# Maps
print(cm.maps)  # [{'a': 1, 'b': 2}, {'c': 3, 'd': 4}]

# New child
cm.new_child({'e': 5})
```

### namedtuple

```python
from collections import namedtuple

# Creation
Point = namedtuple('Point', ['x', 'y'])
p = Point(1, 2)

# Access
print(p.x, p.y)  # 1 2
print(p[0], p[1])  # 1 2

# Unpack
x, y = p

# As dict
print(p._asdict())  # {'x': 1, 'y': 2}

# Replace
p2 = p._replace(x=10)
```

---

## One-Minute Revision Table

| Type | Mutable | Ordered | Unique | Example |
|------|---------|---------|--------|---------|
| **list** | Yes | Yes | No | `[1, 2, 3]` |
| **tuple** | No | Yes | No | `(1, 2, 3)` |
| **dict** | Yes | Yes | Keys | `{'a': 1}` |
| **set** | Yes | No | Yes | `{1, 2, 3}` |
| **frozenset** | No | No | Yes | `frozenset([1, 2, 3])` |

---

## Common Mistakes

### 1. Modifying List While Iterating

```python
# WRONG
lst = [1, 2, 3, 4, 5]
for x in lst:
    if x % 2 == 0:
        lst.remove(x)

# RIGHT
lst = [x for x in lst if x % 2 != 0]
```

### 2. Using Mutable Default Argument

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

### 3. Dictionary Key Error

```python
# WRONG
d = {'a': 1}
print(d['b'])  # KeyError

# RIGHT
print(d.get('b', 0))  # 0
```

### 4. Set Membership Test

```python
# WRONG
s = set(range(1000000))
if 999999 in s:
    pass

# RIGHT (use set for O(1) lookup)
s = set(range(1000000))
if 999999 in s:  # O(1)
    pass
```

---

## Production Notes

1. **Use `collections.defaultdict` for defaultdicts** - Avoids key existence checks
2. **Use `collections.Counter` for counting** - More efficient than manual counting
3. **Use `collections.deque` for queues** - O(1) append/pop from both ends
4. **Use `collections.namedtuple` for simple classes** - Less memory than regular classes
5. **Use `collections.ChainMap` for multiple dictionaries** - Treat multiple dicts as one
6. **Use set for membership testing** - O(1) vs O(n) for list
7. **Use tuple for immutable sequences** - Hashable, can be dict keys
8. **Use `__slots__` for memory optimization** - Reduces instance memory usage
9. **Profile memory usage** - Use `memory_profiler` to find memory leaks
10. **Choose the right data structure** - Based on your use case

---

## Further Reading

- Python documentation on collections module
- Python documentation on built-in types
- Fluent Python by Luciano Ramalho
- Python Cookbook by David Beazley
