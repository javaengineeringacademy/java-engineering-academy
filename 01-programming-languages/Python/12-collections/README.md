# Collections in Python

> **Right tool, right job — same applies to data structures.**

## What

Python's collections module provides specialized containers beyond the built-in `list`, `dict`, `set`, and `tuple`. These include `Counter`, `defaultdict`, `deque`, `OrderedDict`, `ChainMap`, and `namedtuple`. Each solves specific problems more elegantly and efficiently than generic alternatives.

Understanding time complexity, memory usage, and thread safety of each collection is essential for writing performant, production-ready code.

## Why

- **Performance hinges on data structure choice.** A wrong choice can make O(n) operations become O(1).
- **Memory efficiency matters at scale.** A `namedtuple` uses less memory than a `dict` for fixed attributes.
- **Thread safety is not optional.** Some collections are inherently thread-safe; others need locks.
- **Specialized collections reduce code.** `Counter` replaces 10-line loops with one-liners.
- **Algorithm design starts with data structures.** Choosing the right one often determines your algorithm's complexity.

## When

| Scenario | Best Collection | Why |
|----------|-----------------|-----|
| FIFO queue | `deque` | O(1) append/pop from both ends |
| Counting occurrences | `Counter` | Built-in `most_common()`, arithmetic ops |
| Grouping items | `defaultdict(list)` | No `KeyError`, auto-initialization |
| Fixed attributes | `namedtuple` | Immutable, memory-efficient, readable |
| LRU cache | `functools.lru_cache` + `OrderedDict` | O(1) lookup with eviction |
| Stack | `list` | O(1) append/pop from end |
| Priority queue | `heapq` | O(log n) insert/extract-min |
| Fast membership test | `set` | O(1) lookup vs O(n) for list |
| Thread-safe queue | `queue.Queue` | Blocks on empty/full |

## How

### list vs deque

```python
# list: O(1) append/pop at end, O(n) at beginning
stack = []
stack.append(item)      # O(1)
stack.pop()             # O(1)
stack.insert(0, item)   # O(n) — AVOID
stack.pop(0)            # O(n) — AVOID

# deque: O(1) append/pop at both ends
from collections import deque
queue = deque()
queue.append(item)      # O(1) right end
queue.appendleft(item)  # O(1) left end
queue.pop()             # O(1) right end
queue.popleft()         # O(1) left end
```

### defaultdict vs dict.setdefault

```python
# dict.setdefault — creates default on every call (wasteful)
word_count = {}
for word in words:
    word_count.setdefault(word, 0)  # Called even if key exists

# defaultdict — cleaner, slightly faster
from collections import defaultdict
word_count = defaultdict(int)
for word in words:
    word_count[word] += 1

# Grouping with defaultdict
from collections import defaultdict
students = defaultdict(list)
for name, grade in student_data:
    students[grade].append(name)
```

### Counter

```python
from collections import Counter

# Count occurrences
text = "hello world"
freq = Counter(text)  # Counter({'l': 3, 'o': 2, ...})

# Most common items
top_3 = freq.most_common(3)  # [('l', 3), ('o', 2), ...]

# Arithmetic operations
counter_a = Counter(a=3, b=1)
counter_b = Counter(a=1, b=2)
combined = counter_a + counter_b   # Counter(a=4, b=3)
diff = counter_a - counter_b      # Counter(a=2)
```

### namedtuple vs dataclass vs dict

```python
from collections import namedtuple
from dataclasses import dataclass

# namedtuple: immutable, memory-efficient, no validation
Point = namedtuple('Point', ['x', 'y'])
p = Point(1, 2)

# dataclass: mutable, validation, type hints, more features
@dataclass
class Point:
    x: float
    y: float
    def distance_from_origin(self) -> float:
        return (self.x**2 + self.y**2)**0.5

# dict: flexible, no structure, highest memory
p = {"x": 1, "y": 2}
```

### Time Complexity Reference

```
Operation          | list    | deque   | dict    | set     | Counter
-------------------|---------|---------|---------|---------|---------
Access by index    | O(1)    | O(n)    | O(1)*   | N/A     | N/A
Access by key      | N/A     | N/A     | O(1)    | O(1)    | O(1)
Search             | O(n)    | O(n)    | O(1)*   | O(1)    | O(1)
Insert (end)       | O(1)    | O(1)    | O(1)    | O(1)    | O(1)
Insert (beginning) | O(n)    | O(1)    | O(1)    | O(1)    | O(1)
Delete             | O(n)    | O(n)    | O(1)    | O(1)    | O(1)
Pop (end)          | O(1)    | O(1)    | O(1)    | N/A     | N/A
Pop (beginning)    | O(n)    | O(1)    | O(1)    | N/A     | N/A

* Amortized O(1) for dict/set with good hash function
```

### Memory Usage Comparison

```python
import sys

# list vs tuple (fixed data)
l = [1, 2, 3, 4, 5]
t = (1, 2, 3, 4, 5)
sys.getsizeof(l)  # ~96 bytes
sys.getsizeof(t)  # ~80 bytes (saves ~17%)

# dict vs namedtuple vs __slots__
class PointDict:
    def __init__(self, x, y):
        self.x = x
        self.y = y

PointNT = namedtuple('PointNT', ['x', 'y'])

class PointSlots:
    __slots__ = ('x', 'y')
    def __init__(self, x, y):
        self.x = x
        self.y = y

# PointDict: ~152 bytes
# PointNT:   ~64 bytes
# PointSlots: ~56 bytes
```

### Thread Safety

```python
from collections import deque
import threading

# deque is thread-safe for append/pop operations
# but NOT for read-modify-write sequences
safe_queue = deque()

# For compound operations, use locks
lock = threading.Lock()
with lock:
    if safe_queue:
        item = safe_queue.popleft()

# For producer-consumer, use queue.Queue (built-in locking)
from queue import Queue
q = Queue(maxsize=100)
```

## Production Checklist

- [ ] **Choose `deque` for queues** — never use `list` for FIFO operations
- [ ] **Use `Counter` for counting** — don't write manual counting loops
- [ ] **Prefer `defaultdict` for grouping** — avoids `setdefault` boilerplate
- [ ] **Use `namedtuple` or `dataclass` for fixed structures** — not raw dicts
- [ ] **Test with realistic data sizes** — performance characteristics change at scale
- [ ] **Consider `__slots__`** for memory-critical classes
- [ ] **Document collection assumptions** — what's the expected order, uniqueness, mutability?

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **Basic** | Uses only `list`, `dict`, `set`. No specialized collections. |
| 2 | **Familiar** | Uses `Counter`, `defaultdict`. Knows basic time complexity. |
| 3 | **Competent** | Uses `deque`, `namedtuple`. Understands thread safety implications. |
| 4 | **Proficient** | Chooses collections based on performance profiling. Uses `ChainMap`, `OrderedDict`. |
| 5 | **Expert** | Implements custom collections. Understands hash tables, collision resolution, memory layouts. |

## Common Myths

### Myth 1: "Lists are always the default choice"
**Reality:** Lists are great for stacks and random access, but terrible for queues. `deque` is O(1) for both ends, while `list` is O(n) for pop(0). Always consider your access pattern first.

### Myth 2: "dicts are unordered in Python"
**Reality:** Since Python 3.7+, dicts maintain insertion order. This is guaranteed by the language specification, not an implementation detail. You can rely on it.

### Myth 3: "Sets are just unordered lists"
**Reality:** Sets are hash tables, not lists. They provide O(1) membership testing vs O(n) for lists. But they can't store duplicates, can't be indexed, and require hashable elements.

## One-Minute Revision

| Collection | Use Case | Key Method | Thread Safe |
|------------|----------|------------|-------------|
| `list` | Stack, random access | `append()`, `pop()` | No (GIL helps) |
| `deque` | Queue, both-end operations | `appendleft()`, `popleft()` | Yes (appends/pops) |
| `dict` | Key-value mapping | `get()`, `keys()`, `values()` | No |
| `set` | Membership testing, dedup | `add()`, `remove()` | No |
| `Counter` | Frequency counting | `most_common()`, arithmetic | No |
| `defaultdict` | Auto-initializing groups | Direct assignment | No |
| `namedtuple` | Fixed-attribute records | Attribute access | Yes (immutable) |
| `OrderedDict` | Order-sensitive operations | `move_to_end()` | Partial |
| `ChainMap` | Merged mappings | `maps` attribute | No |

## Alternatives Comparison

| Need | Option 1 | Option 2 | Option 3 | Winner |
|------|----------|----------|----------|--------|
| Counting | `Counter` | `dict` + loop | `collections.Counter` | `Counter` |
| Grouping | `defaultdict` | `dict.setdefault` | Manual check | `defaultdict` |
| FIFO queue | `deque` | `list` + `pop(0)` | `queue.Queue` | `deque` |
| Fixed record | `namedtuple` | `dataclass` | `dict` | Context-dependent |
| Caching | `lru_cache` | Manual dict | `cachetools` | `lru_cache` |
| Mapping merge | `ChainMap` | `{**d1, **d2}` | `dict.update` | `ChainMap` (lazy) |

## Related Topics

- [01-basics](../01-basics/) - Built-in types and operations
- [02-oop](../02-oop/) - Custom classes and `__slots__`
- [06-generators](../06-generators/) - Iterator patterns with collections
- [09-exception-handling](../09-exception-handling/) - KeyError handling
- [15-async](../15-async/) - Thread-safe collections in async code

---

> **Remember:** Data structure choice is algorithm choice. Pick wisely, and your code practically writes itself.
