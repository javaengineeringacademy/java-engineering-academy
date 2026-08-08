# Python Collections — Senior-Level Depth

> **"Amateurs optimize algorithms. Professionals choose the right data structure."**

## Why Collections Matter

Every application needs to store, organize, and process data efficiently. Understanding Python's collections means understanding CPython internals — how data structures are implemented, their performance characteristics, and when to use each one. Without this knowledge, you'd make suboptimal choices that lead to poor performance, excessive memory usage, and code that's hard to maintain.

Without mastering collections, you'd write verbose, error-prone code for common data manipulation tasks and miss opportunities for significant performance improvements. That's why collections exist — they provide optimized, purpose-built data structures that let you write cleaner, faster, and more expressive code in production systems.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Any data processing, counting, grouping, deduplication | Simple lists for small datasets |
| When NOT to use | Don't use list for queues; don't use dict for counting | Use `deque`, `Counter`, `defaultdict` |
| Alternatives | numpy for numeric, pandas for complex data | Built-in types for simple cases |
| Production Examples | Data pipelines, analytics, caching | Prototypes, simple scripts |
| Common Mistakes | `list.pop(0)` in loops, ignoring hash randomization | Use `deque`; set `PYTHONHASHSEED` for tests |

## What You'll Learn

By the end of this module, you'll be able to:

- Choose the right collection for each use case based on performance characteristics
- Understand CPython's internal implementations of lists, dicts, and sets
- Use specialized collections like `Counter`, `defaultdict`, and `deque`
- Apply memory-efficient patterns for large-scale data processing
- Debug collection-related performance issues in production

---

## 1. CPython List Implementation

Lists in Python aren't just "arrays." They're dynamic arrays of pointers to Python objects, managed by CPython's `listobject.c`.

### Dynamic Array with Over-Allocation

When you append to a list, CPython doesn't allocate exactly one new slot each time. It **over-allocates** — allocating extra space to make future appends O(1) amortized.

```python
import sys

# Memory growth pattern of lists
sizes = []
for i in range(20):
    lst = list(range(i))
    sizes.append((i, sys.getsizeof(lst)))

for length, size in sizes:
    print(f"Length {length:2d}: {size:3d} bytes")
```

Output:
```
Length  0:  56 bytes   # Empty list overhead
Length  1:  88 bytes   # +1 item: 8 bytes pointer
Length  2:  88 bytes
Length  3:  88 bytes
Length  4:  96 bytes   # Over-allocation kicks in
Length  5:  96 bytes
Length  6: 104 bytes
Length  7: 104 bytes
Length  8: 120 bytes   # Growth accelerates
```

The over-allocation formula: `new_allocated = ((newsize >> 3) + (newsize < 9 ? 3 : 6))`

**Why this matters:** You'll see memory "wasted" in lists. That's intentional — it's the price of O(1) amortized append.

### The Cost of Operations

```python
import timeit

# list.pop() from end: O(1)
lst = list(range(10000))
timeit.timeit(lambda: lst.pop(), number=100000)
# ~0.005 seconds

# list.pop(0) from front: O(n) — shifts every element
lst = list(range(10000))
timeit.timeit(lambda: lst.pop(0), number=1000)
# ~0.15 seconds (200x slower!)

# list.insert(0, x) from front: O(n) — worst case
timeit.timeit(lambda: lst.insert(0, 0), number=1000)
# ~0.18 seconds
```

### Production Implications

```python
# BAD: Using list as a FIFO queue
def process_queue_slow(items):
    queue = list(items)
    while queue:
        item = queue.pop(0)  # O(n) per operation!
        process(item)

# GOOD: Using deque for FIFO
from collections import deque

def process_queue_fast(items):
    queue = deque(items)
    while queue:
        item = queue.popleft()  # O(1)
        process(item)
```

### Common Misconception

**"Lists have O(1) access everywhere"** — Wrong. Lists have O(1) indexed access from either end, but the constant factor differs due to Python's signed integer indexing. For very large lists (millions of elements), indexing from the front is slightly slower than from the back.

```python
import sys

lst = list(range(1000000))
# sys.getsizeof(lst) shows the LIST overhead, not the elements
# Each element is a pointer (8 bytes) to an int object
```

---

## 2. Dict Insertion Ordering

Python 3.7+ **guarantees** insertion order in dictionaries. This isn't a side effect — it's an intentional design decision in CPython's dict implementation.

### Hash Table with Open Addressing

CPython dicts use a hash table with **open addressing** (not chaining). The table stores entries directly in the array using probe sequences.

```python
# Dict internals (simplified)
import sys

d = {}
print(sys.getsizeof(d))  # 64 bytes (empty dict overhead)

d['key'] = 'value'
print(sys.getsizeof(d))  # 184 bytes (after first insertion)

# Dict grows when 2/3 full (DK_DENSITY)
d = {str(i): i for i in range(100)}
print(sys.getsizeof(d))  # Significant growth
```

### Hash Collision Handling

When two keys hash to the same index, CPython uses **probing** (specifically, a perturbation-based scheme) to find the next slot.

```python
# You can observe collision behavior
import hashlib

# Strings with same prefix often have related hashes
similar_strings = ['test', 'test1', 'test2', 'test3']
hashes = [hash(s) for s in similar_strings]
print(hashes)  # May show patterns

# Dict handles this transparently
d = {}
for s in similar_strings:
    d[s] = len(s)  # No collision errors
```

### Dict Growth Policy

```python
# Dict resizing triggers
d = {}
prev_size = sys.getsizeof(d)

for i in range(100):
    d[i] = i
    new_size = sys.getsizeof(d)
    if new_size != prev_size:
        print(f"Dict grew at {i} entries: {prev_size} -> {new_size} bytes")
        prev_size = new_size
```

Output:
```
Dict grew at 0 entries: 64 -> 184 bytes
Dict grew at 5 entries: 184 -> 344 bytes
Dict grew at 11 entries: 344 -> 648 bytes
# ... continues growing
```

**Why this matters:** Dict memory usage is unpredictable. If you're building large dicts in memory-constrained environments, consider this.

### Production Implications

```python
# Memory-efficient dict with __slots__ for known keys
class Config:
    __slots__ = ('host', 'port', 'debug')
    def __init__(self, host, port, debug):
        self.host = host
        self.port = port
        self.debug = debug

config = Config('localhost', 8080, True)
# Uses ~56 bytes vs ~152 bytes for regular instance
```

### Common Misconception

**"Dict order depends on hash values"** — Incorrect. The order is strictly insertion order. Hash values affect where items are stored in memory, but iteration order follows insertion order in Python 3.7+.

---

## 3. Hash Randomization

Python randomizes string hashes across runs to prevent **hash DoS attacks** — where an attacker crafts input that causes worst-case O(n) lookups.

### PYTHONHASHSEED

```python
# Hash values change between runs
print(hash("hello"))  # Different each run

# Fix hash seed for reproducible behavior
# Run: PYTHONHASHSEED=42 python script.py

# Check if hash randomization is enabled
import sys
print(sys.flags.hash_randomization)  # 1 if enabled (default)
```

### Security Implications

```python
# WITHOUT hash randomization (PYTHONHASHSEED=0)
# Attacker can craft URLs like:
# /search?q=aaaaaaaaaaaaaaaa...
# This causes O(n^2) behavior in dict/set lookups

# WITH hash randomization (default)
# Same input produces different hashes each run
# Attack cannot predict hash collisions
```

### Why This Matters

```python
# If you need reproducible ordering (e.g., for testing)
import os
os.environ['PYTHONHASHSEED'] = '42'

# Or use ordered collections when order matters
from collections import OrderedDict
d = OrderedDict()
d['a'] = 1
d['b'] = 2
```

### Common Misconception

**"Hash randomization makes debugging harder"** — True, but only if you're debugging hash table internals. For normal debugging, it's irrelevant. Use `PYTHONHASHSEED=0` only when you need deterministic behavior.

---

## 4. Set Implementation

Sets are hash tables without values. They provide O(1) membership testing and mathematical set operations.

### Set as Hash Table

```python
s = set()
print(sys.getsizeof(s))  # 216 bytes (empty set)

s.add(1)
print(sys.getsizeof(s))  # 216 bytes (no growth yet)

# Set uses same hash table as dict, but without values
s = {i for i in range(100)}
print(sys.getsizeof(s))  # Significant memory usage
```

### Set Operations Performance

```python
import timeit

a = set(range(10000))
b = set(range(5000, 15000))

# Intersection: O(min(len(a), len(b)))
timeit.timeit(lambda: a & b, number=10000)  # Fast

# Union: O(len(a) + len(b))
timeit.timeit(lambda: a | b, number=10000)  # Fast

# Difference: O(len(a))
timeit.timeit(lambda: a - b, number=10000)  # Fast

# Membership test: O(1) average
timeit.timeit(lambda: 5000 in a, number=100000)  # Very fast
```

### frozenset: Immutable and Hashable

```python
# Regular set: mutable, not hashable
s = {1, 2, 3}
# hash(s)  # TypeError: unhashable type: 'set'

# frozenset: immutable, hashable
fs = frozenset([1, 2, 3])
hash(fs)  # Works!

# Can be used as dict key
d = {frozenset([1, 2]): "pair"}
d[frozenset([3, 4])] = "another pair"

# Can be used in sets
set_of_sets = {frozenset([1, 2]), frozenset([3, 4])}
```

### Set vs List for Membership Testing

```python
import timeit

lst = list(range(10000))
s = set(range(10000))

# List membership: O(n)
timeit.timeit(lambda: 9999 in lst, number=1000)
# ~0.05 seconds

# Set membership: O(1)
timeit.timeit(lambda: 9999 in s, number=1000)
# ~0.000001 seconds (50,000x faster!)
```

### Production Implications

```python
# Use sets for deduplication
def get_unique_users(logs):
    return set(logs)  # O(n), much faster than manual dedup

# Use frozenset for immutable collections
def get_user_permissions(user_id):
    return frozenset(db.get_permissions(user_id))

# Can safely use in sets/dicts
user_perms = {
    "admin": frozenset(["read", "write", "execute"]),
    "viewer": frozenset(["read"]),
}
```

### Common Misconception

**"Sets are unordered"** — Correct, but misleading. Sets have no defined iteration order, but they're not "random." The order depends on hash values and insertion history. Don't rely on order, but don't assume it's random either.

---

## 5. Tuple Optimization

Tuples are immutable sequences with special CPython optimizations.

### Small Tuple Caching

```python
# CPython caches small tuples (0-20 elements)
t1 = (1, 2, 3)
t2 = (1, 2, 3)
print(t1 is t2)  # True — same object!

# Larger tuples are not cached
t3 = tuple(range(21))
t4 = tuple(range(21))
print(t3 is t4)  # False — different objects
```

### Tuple Packing/Unpacking Efficiency

```python
import timeit

# Tuple packing
timeit.timeit(lambda: (1, 2, 3, 4, 5))  # Very fast

# Tuple unpacking
t = (1, 2, 3, 4, 5)
timeit.timeit(lambda: a, b, c, d, e = t)  # Very fast

# Tuple vs list creation
timeit.timeit(lambda: (1, 2, 3, 4, 5))  # Faster
timeit.timeit(lambda: [1, 2, 3, 4, 5])  # Slower
```

### Memory Efficiency

```python
import sys

# Tuple is more memory efficient
lst = [1, 2, 3, 4, 5]
tup = (1, 2, 3, 4, 5)

print(f"List: {sys.getsizeof(lst)} bytes")
print(f"Tuple: {sys.getsizeof(tup)} bytes")
# Tuple saves ~20-30% memory

# For large collections, difference is significant
lst_large = list(range(1000))
tup_large = tuple(range(1000))
print(f"List large: {sys.getsizeof(lst_large)} bytes")
print(f"Tuple large: {sys.getsizeof(tup_large)} bytes")
```

### Named Tuples for Readability

```python
from collections import namedtuple

# Basic namedtuple
Point = namedtuple('Point', ['x', 'y'])
p = Point(1, 2)
print(p.x, p.y)  # 1 2

# With defaults
Point = namedtuple('Point', ['x', 'y'], defaults=[0, 0])
p = Point(1)
print(p)  # Point(x=1, y=0)

# With type hints (Python 3.6+)
from typing import NamedTuple

class Point(NamedTuple):
    x: float
    y: float
    z: float = 0.0

p = Point(1.0, 2.0)
print(p)  # Point(x=1.0, y=2.0, z=0.0)
```

### Tuple vs List: When to Use Which

```python
# Use tuple for:
# - Fixed collections that shouldn't change
# - Dictionary keys (frozenset for mutable collections)
# - Function returns with multiple values
# - Data that represents a record/struct

# Use list for:
# - Collections that need to grow/shrink
# - Collections that need sorting/reordering
# - Collections that need to be modified

def get_user_info(user_id):
    # Tuple for fixed structure
    return (name, email, role)  # Immutable record

def process_items(items):
    # List for mutable collection
    result = []
    for item in items:
        if valid(item):
            result.append(transform(item))
    return result
```

### Common Misconception

**"Tuples are always faster than lists"** — Not necessarily. Tuples are faster to create and access, but the difference is negligible for most operations. Choose based on semantics (immutable vs mutable), not performance.

---

## 6. collections Module

The `collections` module provides specialized containers that solve specific problems more elegantly than built-in types.

### deque: Double-Ended Queue

```python
from collections import deque
import timeit

# deque: O(1) append/pop from both ends
dq = deque()

# Right end operations
dq.append(1)      # O(1)
dq.pop()          # O(1)

# Left end operations
dq.appendleft(0)  # O(1)
dq.popleft()      # O(1)

# Rotation
dq = deque([1, 2, 3, 4, 5])
dq.rotate(2)      # [4, 5, 1, 2, 3]
dq.rotate(-2)     # [1, 2, 3, 4, 5]

# Performance comparison
lst = list(range(10000))
dq = deque(range(10000))

# pop(0) from list: O(n)
timeit.timeit(lambda: lst.pop(0), number=1000)  # ~0.15s

# popleft() from deque: O(1)
timeit.timeit(lambda: dq.popleft(), number=1000)  # ~0.0001s
```

### Counter: Counting Hashable Objects

```python
from collections import Counter

# Count occurrences
text = "hello world"
counter = Counter(text)
print(counter.most_common(3))  # [('l', 3), ('o', 2), (' ', 1)]

# Arithmetic operations
a = Counter(x=3, y=1)
b = Counter(x=1, y=2)

print(a + b)  # Counter({'x': 4, 'y': 3})
print(a - b)  # Counter({'x': 2})
print(a & b)  # Counter({'x': 1, 'y': 1}) — min
print(a | b)  # Counter({'x': 3, 'y': 2}) — max

# From iterable
words = ['apple', 'banana', 'apple', 'cherry', 'banana', 'apple']
word_counts = Counter(words)
print(word_counts.most_common(2))  # [('apple', 3), ('banana', 2)]
```

### defaultdict: Factory for Missing Values

```python
from collections import defaultdict

# Default values by type
d = defaultdict(int)
d['key'] += 1  # No KeyError!
print(d)  # defaultdict(<class 'int'>, {'key': 1})

# Grouping
students = defaultdict(list)
for name, grade in [('Alice', 'A'), ('Bob', 'B'), ('Alice', 'A+')]:
    students[grade].append(name)
print(dict(students))  # {'A': ['Alice'], 'B': ['Bob'], 'A+': ['Alice']}

# Nested structure
tree = defaultdict(lambda: defaultdict(list))
tree['level1']['level2'].append('value')
```

### OrderedDict: Order-Preserving Dict

```python
from collections import OrderedDict

# OrderedDict (legacy — use dict in Python 3.7+)
od = OrderedDict()
od['first'] = 1
od['second'] = 2
od['third'] = 3

# move_to_end
od.move_to_end('first')
print(list(od.keys()))  # ['second', 'third', 'first']

od.move_to_end('last', last=False)
# Insert at beginning

# popitem(last=True) removes last item (LIFO)
od.popitem(last=True)

# Equality considers order
od1 = OrderedDict([('a', 1), ('b', 2)])
od2 = OrderedDict([('b', 2), ('a', 1)])
print(od1 == od2)  # False (order matters)
```

### ChainMap: Multiple Dicts as One

```python
from collections import ChainMap

# ChainMap searches through multiple dicts
defaults = {'color': 'red', 'user': 'guest'}
environment = {'user': 'admin'}
overrides = {'debug': True}

config = ChainMap(overrides, environment, defaults)
print(config['user'])  # 'admin' (first match)
print(config['color'])  # 'red' (from defaults)
print(config['debug'])  # True

# Update creates a new layer
config_child = config.new_child({'timeout': 30})
print(config_child['timeout'])  # 30
print(config['timeout'])  # KeyError (not in parent)
```

### Production Implications

```python
# Counter for frequency analysis
def analyze_text(text):
    words = text.lower().split()
    word_freq = Counter(words)
    return word_freq.most_common(10)

# defaultdict for efficient grouping
def group_by_key(items, key_func):
    groups = defaultdict(list)
    for item in items:
        groups[key_func(item)].append(item)
    return dict(groups)

# deque for sliding window
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

### Common Misconception

**"OrderedDict is always better than dict"** — Wrong. In Python 3.7+, regular dicts maintain insertion order and are faster. OrderedDict is only needed for order-sensitive operations like `move_to_end` or order-sensitive equality.

---

## 7. Performance Comparison

Choosing the right collection directly impacts performance. Here's how to choose.

### List vs deque for Queue Operations

```python
import timeit
from collections import deque

# Simulate queue operations
items = list(range(10000))

# List as queue (BAD)
def list_queue():
    q = list(items)
    while q:
        q.pop(0)

# Deque as queue (GOOD)
def deque_queue():
    q = deque(items)
    while q:
        q.popleft()

print(f"List queue: {timeit.timeit(list_queue, number=10):.4f}s")
print(f"Deque queue: {timeit.timeit(deque_queue, number=10):.4f}s")
# Deque is 100-1000x faster
```

### Dict vs defaultdict for Grouping

```python
from collections import defaultdict

data = [('A', 1), ('B', 2), ('A', 3), ('B', 4)]

# Manual grouping with dict
def group_dict(data):
    result = {}
    for key, value in data:
        if key not in result:
            result[key] = []
        result[key].append(value)
    return result

# defaultdict grouping
def group_defaultdict(data):
    result = defaultdict(list)
    for key, value in data:
        result[key].append(value)
    return dict(result)

# Both work, but defaultdict is cleaner and slightly faster
```

### Set vs List for Membership Testing

```python
import timeit

large_list = list(range(100000))
large_set = set(range(100000))

# Test membership of element at end
test_value = 99999

# List membership: O(n)
time_list = timeit.timeit(lambda: test_value in large_list, number=1000)

# Set membership: O(1)
time_set = timeit.timeit(lambda: test_value in large_set, number=1000)

print(f"List: {time_list:.4f}s")
print(f"Set: {time_set:.6f}s")
# Set is 1000x faster for large collections
```

### Tuple vs List for Immutable Data

```python
import timeit
import sys

# Creation speed
time_list = timeit.timeit(lambda: [1, 2, 3, 4, 5], number=100000)
time_tuple = timeit.timeit(lambda: (1, 2, 3, 4, 5), number=100000)

print(f"List creation: {time_list:.4f}s")
print(f"Tuple creation: {time_tuple:.4f}s")
# Tuple creation is ~30% faster

# Memory
lst = [1, 2, 3, 4, 5]
tup = (1, 2, 3, 4, 5)
print(f"List size: {sys.getsizeof(lst)} bytes")
print(f"Tuple size: {sys.getsizeof(tup)} bytes")
# Tuple uses ~30% less memory
```

### Production Decision Matrix

```python
# When to use what:
DECISION_MATRIX = {
    "need_mutable": {
        "ordered": "list",
        "stack": "list",
        "queue": "deque",
        "key_value": "dict",
    },
    "need_immutable": {
        "sequence": "tuple",
        "set": "frozenset",
        "key_value": "tuple",
    },
    "need_counting": "Counter",
    "need_grouping": "defaultdict",
    "need_unique": "set",
    "need_ordered_key_value": "dict",  # Python 3.7+
    "need_multiple_dicts": "ChainMap",
}
```

### Memory Usage Summary

| Collection | Empty | 1000 items | Notes |
|------------|-------|------------|-------|
| list | 56 bytes | ~8,056 bytes | Over-allocated |
| tuple | 40 bytes | ~8,040 bytes | No over-allocation |
| dict | 64 bytes | ~36,864 bytes | Hash table overhead |
| set | 216 bytes | ~32,768 bytes | Hash table overhead |
| deque | 640 bytes | ~8,064 bytes | Block-based |
| Counter | 232 bytes | ~36,864 bytes | Dict subclass |
| defaultdict | 64 bytes | ~36,864 bytes | Dict subclass |

---

## One-Minute Revision

| Collection | Time Complexity | Memory | Thread Safe | Use Case |
|------------|----------------|--------|-------------|----------|
| list | Append O(1), Pop O(1), Insert(0) O(n) | High | No | Stack, random access |
| deque | Append O(1), Pop O(1), Both ends O(1) | Medium | Yes (appends/pops) | Queue, both-end ops |
| dict | Lookup O(1), Insert O(1) | Very High | No | Key-value mapping |
| set | Lookup O(1), Add O(1) | High | No | Membership, dedup |
| tuple | Access O(1) | Low | Yes (immutable) | Fixed records |
| Counter | Lookup O(1), most_common O(n log n) | Very High | No | Frequency counting |
| defaultdict | Lookup O(1), Insert O(1) | Very High | No | Auto-initializing groups |
| frozenset | Lookup O(1) | High | Yes (immutable) | Immutable sets |
| OrderedDict | Lookup O(1), move_to_end O(1) | Very High | No | Order-sensitive ops |
| ChainMap | Lookup O(k) worst case | Medium | No | Merged mappings |

---

## Common Pitfalls

### 1. Using list.pop(0) in loops
```python
# BAD: O(n) per pop, O(n²) total
queue = list(range(10000))
while queue:
    item = queue.pop(0)  # O(n) each time!

# GOOD: O(1) per pop, O(n) total
from collections import deque
queue = deque(range(10000))
while queue:
    item = queue.popleft()  # O(1)
```

### 2. Ignoring hash randomization in tests
```python
# Tests that depend on dict ordering will fail
def test_dict_order():
    d = {'b': 2, 'a': 1}
    assert list(d.keys()) == ['b', 'a']  # May fail!

# FIX: Use OrderedDict or sort keys
from collections import OrderedDict
d = OrderedDict([('b', 2), ('a', 1)])
assert list(d.keys()) == ['b', 'a']
```

### 3. Creating sets with unhashable elements
```python
# BAD: TypeError
s = {[1, 2], [3, 4]}  # TypeError: unhashable type: 'list'

# GOOD: Use frozenset
s = {frozenset([1, 2]), frozenset([3, 4])}
```

### 4. Using defaultdict with mutable default values
```python
# BAD: Shared mutable default!
from collections import defaultdict
d = defaultdict([])
d['key'].append(1)
d['key'].append(2)
# BUG: All keys share the same list!

# GOOD: Use factory function
d = defaultdict(list)
d['key'].append(1)
d['key'].append(2)
# CORRECT: Each key gets its own list
```

### 5. Assuming dict order is hash order
```python
# WRONG: "Dict order depends on hash"
d = {}
d['b'] = 2  # Hash: some value
d['a'] = 1  # Hash: some other value
# Order is insertion order, NOT hash order
```

---

## Production Incidents

### Incident 1: list.pop(0) Causing O(n²) Performance

**Problem:** Message queue processing degraded from 1000 msg/s to 10 msg/s
**Cause:** `list.pop(0)` is O(n); queue had 100K messages
**Impact:** Message backlog grew; processing delayed by hours
**Detection:** Queue depth monitoring alerted on backlog
**Solution:**
```python
# BAD: O(n) per pop, O(n²) total
queue = list(messages)
while queue:
    msg = queue.pop(0)  # Shifts all elements!

# GOOD: O(1) per pop
from collections import deque
queue = deque(messages)
while queue:
    msg = queue.popleft()  # O(1)
```
**Prevention:** Use `deque` for queues; profile list operations; benchmark with realistic data sizes

### Incident 2: defaultdict Shared Mutable Default

**Problem:** All users had the same permissions list
**Cause:** `defaultdict(list)` created one list shared across all keys
**Impact:** Permission escalation vulnerability in production
**Detection:** Security audit found all users had admin access
**Solution:**
```python
# BAD: Shared mutable default
d = defaultdict([])
d['user1'].append('read')  # All keys share same list!

# GOOD: Factory function creates new list each time
d = defaultdict(list)
d['user1'].append('read')  # Each key gets own list
```
**Prevention:** Always use `list`, `dict`, `set` as factory (not `[]`, `{}`); test defaultdict behavior

### Incident 3: Hash DoS Attack on Dict

**Problem:** API endpoint became unresponsive under attack
**Cause:** Attacker sent crafted keys causing O(n²) dict lookups
**Impact:** Service denial for 10 minutes; CPU spike to 100%
**Detection:** Load balancer health checks failed; CPU monitoring alerted
**Solution:**
```python
# Python 3.3+ has hash randomization by default
# Ensure PYTHONHASHSEED is not 0 in production
import sys
print(sys.flags.hash_randomization)  # Should be 1

# For extra protection: limit input size
def process_input(data):
    if len(data) > 10000:
        raise ValueError("Input too large")
    return {k: v for k, v in data.items()}
```
**Prevention:** Keep hash randomization enabled; validate input size; rate-limit API endpoints

## Production Checklist

- [ ] **Use deque for queues** — never use list for FIFO operations
- [ ] **Use Counter for counting** — don't write manual counting loops
- [ ] **Use defaultdict for grouping** — avoids setdefault boilerplate
- [ ] **Use namedtuple/dataclass for fixed structures** — not raw dicts
- [ ] **Use frozenset for immutable sets** — when you need hashable collections
- [ ] **Test with realistic data sizes** — performance characteristics change at scale
- [ ] **Consider __slots__** for memory-critical classes
- [ ] **Document collection assumptions** — order, uniqueness, mutability
- [ ] **Use PYTHONHASHSEED=0** only when you need deterministic behavior
- [ ] **Profile before optimizing** — the right collection depends on your use case

---

## Maturity Levels

| Level | Characteristics |
|-------|----------------|
| 1 | Uses only `list`, `dict`, `set`. No specialized collections. |
| 2 | Uses `Counter`, `defaultdict`. Knows basic time complexity. |
| 3 | Uses `deque`, `namedtuple`. Understands thread safety implications. |
| 4 | Chooses collections based on performance profiling. Uses `ChainMap`, `OrderedDict`. |
| 5 | Implements custom collections. Understands hash tables, collision resolution, memory layouts. |

---

> **Remember:** Data structure choice is algorithm choice. Understanding CPython internals isn't trivia — it's what separates code that works from code that works at scale.

## Related Topics

- [01-fundamentals](../01-fundamentals/) - Basic collection operations
- [10-internals](../10-internals/) - CPython collection implementations
- [15-performance](../15-performance/) - Collection performance optimization

## References
- Python Docs: collections module
- Python Docs: list
- Python Docs: dict
- Python Docs: set
- PEP 412: Key-Sharing Dictionary
- PEP 572: Assignment Expressions
- Fluent Python (Luciano Ramalho) - Chapter 2

## Version Validation
- Verified against: Python 3.7+ (dict ordering guaranteed)

## Interview Questions

### Q1: What is the time complexity of list.append() vs list.insert(0, x)?
**Answer:** append() is O(1) amortized. insert(0, x) is O(n) because all elements shift. Use deque for O(1) prepend.

### Q2: How does dict maintain insertion order?
**Answer:** Python 3.7+ guarantees insertion order. Dict uses hash table with compact layout. Order is preserved until rehash.

### Q3: What is the difference between set and frozenset?
**Answer:** set is mutable (add, remove). frozenset is immutable (can be dict key, set element). frozenset is hashable.

### Q4: When would you use deque over list?
**Answer:** deque for queue/stack operations (append/pop from both ends). list for random access. deque is O(1) for both ends, list is O(n) for front operations.

### Q5: What is the difference between defaultdict and dict?
**Answer:** defaultdict calls factory function for missing keys. No KeyError. dict raises KeyError for missing keys. Use defaultdict for grouping/counting.

---

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| `list.pop(0)` causing O(n²) performance | `timeit` to benchmark; `collections.deque` | Switch to `deque.popleft()` for O(1) queue operations |
| Hash randomization breaking test ordering | `PYTHONHASHSEED=42` for deterministic runs | Use `OrderedDict` when order matters; pin hash seed in tests |
| `defaultdict` sharing mutable default | Check factory function vs mutable default | Use `defaultdict(list)` not `defaultdict([])` |
| Set membership test failing with unhashable types | Convert to `frozenset` | Use `frozenset` for sets-of-sets; validate hashability |
| Dict memory usage unexpected | `sys.getsizeof()` + `deep_getsizeof()` | Account for hash table overhead; use `__slots__` for memory-critical classes |

## Code Review Checklist

- [ ] `deque` used for FIFO/LIFO queue operations instead of `list`
- [ ] `Counter` used for frequency counting instead of manual loops
- [ ] `defaultdict` used for grouping instead of `setdefault` boilerplate
- [ ] `frozenset` used for immutable sets that need to be hashable
- [ ] `namedtuple` or `@dataclass` used for fixed-structure records
- [ ] `PYTHONHASHSEED` set for deterministic test ordering
- [ ] `__slots__` used for classes with many instances to reduce memory

## Architecture Considerations

Collection choice directly impacts performance and memory usage. Deques provide O(1) queue operations. Sets provide O(1) membership testing. Dicts provide O(1) key lookup. Understanding CPython's implementation (hash tables, over-allocation) enables informed decisions about data structure selection.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| `deque` for queues | FIFO/LIFO operations | O(1) both ends vs O(n) for list front ops |
| `Counter` for frequency | Text analysis, log processing | Optimized but adds import dependency |
| `defaultdict` for grouping | Data transformation pipelines | Cleaner than `setdefault` but factory overhead |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Hash DoS attack on dict/set | O(n²) performance degradation | Keep hash randomization enabled; validate input size |
| `defaultdict` shared mutable default | Permission escalation | Use factory functions (`list`, `dict`) not mutable instances |
| Unhashable elements in set operations | `TypeError` at runtime | Validate hashability; use `frozenset` for nested sets |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Python 3.7+ | Dict insertion order guaranteed | Remove `OrderedDict` unless `move_to_end` needed |
| Python 3.9+ | `dict`, `list`, `tuple` as generic types | Use `dict[str, int]` instead of `Dict[str, int]` |
| Python 3.12+ | Type parameter syntax | Use `class MyDict[T]` for custom generic collections |


