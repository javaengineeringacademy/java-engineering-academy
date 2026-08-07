# Memory Management Quiz

## Question 1 (Multiple Choice)
How does Python's garbage collector primarily reclaim memory?

- A) Manual deallocation like C
- B) Reference counting (primary) plus a cyclic garbage collector (secondary) for detecting reference cycles
- C) Mark-and-sweep only — reference counting is deprecated
- D) The OS reclaims memory when the process exits

**Answer: B**
**Explanation:** Python uses reference counting as its primary mechanism — when an object's reference count drops to zero, it's immediately deallocated. However, reference counting can't handle reference cycles (A→B→A). The cyclic garbage collector (GC) runs periodically to detect and collect these cycles. `gc.collect()` forces a collection. Understanding both mechanisms is crucial for diagnosing memory leaks — cyclic GC can't collect objects with `__del__` methods in Python < 3.4.

---

## Question 2 (Multiple Choice)
What is the purpose of `__slots__` in a Python class?

- A) It creates class-level variables accessible to all instances
- B) It restricts instance attributes to a fixed set, eliminating `__dict__` and reducing memory per instance
- C) It defines static methods that can't be overridden
- D) It marks the class as abstract

**Answer: B**
**Explanation:** `__slots__` tells Python to use a fixed-size array instead of a dictionary for instance attributes. This eliminates the per-instance `__dict__` overhead (~100+ bytes per object). For classes with millions of instances, this saves significant memory. The trade-off: you can't add arbitrary attributes at runtime, and inheritance with `__slots__` requires careful planning. Use it for data-heavy classes like ORM models or parsed record objects.

---

## Question 3 (Multiple Choice)
What is a `weakref` and when should you use it?

- A) A reference that's automatically garbage collected — always preferred over strong references
- B) A reference that doesn't increase the reference count, allowing the object to be garbage collected even if the weakref exists
- C) A reference to objects that are about to be deleted
- D) A weaker version of Python's `import` system

**Answer: B**
**Explanation:** `weakref` creates a reference that doesn't increment the object's reference count. If the only remaining references are weak references, the object is collected. Use cases: caches (don't prevent garbage collection of cached objects), event listeners (avoid reference cycles between subject and observer), and parent-child relationships where the child shouldn't prevent parent cleanup. `weakref.WeakKeyDictionary` and `WeakValueDictionary` are built on this.

---

## Question 4 (Multiple Choice)
Which tool should you use to find memory leaks in a Python application?

- A) `print()` statements to track object creation
- B) `tracemalloc` — built-in module that tracks memory allocations with source line information
- C) `sys.getsizeof()` — tells you total memory usage
- D) `gc.get_objects()` — lists all objects and their memory addresses

**Answer: B**
**Explanation:** `tracemalloc` is Python's built-in memory profiler. It snapshots memory allocations, compares snapshots over time, and identifies the source lines where allocations happen. `sys.getsizeof()` only reports the shallow size of one object (not its contents). `gc.get_objects()` lists all objects but doesn't track allocation sources. For production profiling, also consider `memory_profiler` (line-by-line profiling) and `objgraph` (object reference graph visualization).

---

## Question 5 (Code Output)
What is the output of this code?

```python
import sys

class Point:
    __slots__ = ('x', 'y')
    def __init__(self, x, y):
        self.x = x
        self.y = y

class DictPoint:
    def __init__(self, x, y):
        self.x = x
        self.y = y

p1 = Point(1, 2)
p2 = DictPoint(1, 2)

print("Point has __dict__:", hasattr(p1, '__dict__'))
print("DictPoint has __dict__:", hasattr(p2, '__dict__'))
print("Point sizeof:", sys.getsizeof(p1))
print("DictPoint sizeof:", sys.getsizeof(p2))
```

**Answer:**
```
Point has __dict__: False
DictPoint has __dict__: True
Point sizeof: 48
DictPoint sizeof: 48
```
**Explanation:** `Point` with `__slots__` doesn't have a `__dict__` — attributes are stored in a fixed-size array. `DictPoint` has `__dict__` — attributes are stored in a hash table. The `sys.getsizeof()` values shown here are the object header sizes (similar for both); the real savings come from the per-instance `__dict__` overhead that `DictPoint` carries (~100+ bytes) which `sys.getsizeof()` doesn't recursively include. For a single object the difference seems small, but multiply by millions and it's significant.

---

## Question 6 (Code Output)
What is the output of this code?

```python
import gc
import weakref

class ExpensiveObject:
    def __init__(self, name):
        self.name = name
    def __del__(self):
        print(f"Deleting {self.name}")

obj = ExpensiveObject("resource")
ref = weakref.ref(obj)

print("obj alive:", ref() is not None)
del obj
print("After del, alive:", ref() is not None)
gc.collect()
print("After gc, alive:", ref() is not None)
```

**Answer:**
```
obj alive: True
After del, alive: False
Deleting resource
After gc, alive: False
```
**Explanation:** `weakref.ref(obj)` creates a weak reference. While `obj` exists, `ref()` returns the object. After `del obj`, the strong reference is removed, the reference count drops to zero, and the object is immediately deallocated (Python's reference counting). `ref()` now returns `None`. The `__del__` method runs at deallocation time. `gc.collect()` doesn't change anything here because the object was already collected by reference counting, not the cyclic GC.

---

## Question 7 (Bug Finding)
Find the memory leak in this code:

```python
import weakref

class Node:
    def __init__(self, name):
        self.name = name
        self.children = []

def create_tree():
    parent = Node("parent")
    child = Node("child")
    parent.children.append(child)
    child.children.append(parent)  # Reference cycle!
    return weakref.ref(parent)

ref = create_tree()
print(ref())  # What happens here?
```

**Bug:** There's a reference cycle: `parent → child → parent`. When `create_tree()` returns, the local variable `parent` goes out of scope, but the cycle keeps both objects alive (reference count never reaches 0). The `weakref` doesn't prevent collection because it doesn't add to the reference count — but the cycle between `parent` and `child` does. The cyclic GC *may* collect them, but the `__del__` method (if present) would prevent collection in Python < 3.4. The weakref returns the object because the cycle keeps it alive.
**Fix:** Break the cycle explicitly:
```python
def create_tree():
    parent = Node("parent")
    child = Node("child")
    parent.children.append(child)
    child.parent = weakref.ref(parent)  # Weak reference instead of strong
    return weakref.ref(parent)
```

---

## Question 8 (Bug Finding)
Find the bug in this caching implementation:

```python
import weakref

class Cache:
    def __init__(self):
        self._cache = weakref.WeakValueDictionary()

    def get(self, key):
        return self._cache.get(key)

    def set(self, key, value):
        self._cache[key] = value

cache = Cache()
data = {"big": "data" * 10000}
cache.set("key1", data)
print(cache.get("key1"))  # Works
del data
print(cache.get("key1"))  # What happens?
```

**Bug:** After `del data`, the only strong reference to the dict is removed. Since `WeakValueDictionary` only holds weak references, the object becomes eligible for garbage collection. `cache.get("key1")` returns `None` because the value has been collected. This is the intended behavior of `WeakValueDictionary` — but if the developer expected the cache to keep objects alive, this is a bug. The cache effectively becomes useless because values are collected as soon as the caller loses its reference.
**Fix:** Use a regular `dict` if you want the cache to retain objects, or use `WeakValueDictionary` with a reference held elsewhere (e.g., in a registry).

---

## Question 9 (Scenario)
You're building a Python application that processes large datasets (50GB+) and runs for hours. You notice memory usage grows steadily over time, even though you're not accumulating data. How should you diagnose this?

- A) Restart the application periodically — it's just Python's memory fragmentation
- B) Use `tracemalloc` to take snapshots and compare allocations over time, identify which lines are allocating the most memory
- C) Add more RAM — Python is inherently memory-hungry
- D) Switch to C — Python can't handle large datasets

**Answer: B**
**Explanation:** `tracemalloc` is the built-in tool for this exact scenario. Take a snapshot early, take another after the growth, compare them: `tracemalloc.take_snapshot().compare_to(old_snapshot, 'lineno')`. This shows exactly which lines allocated the most new memory. Common causes: growing caches, accumulating log messages, reference cycles preventing GC, or closures capturing large objects. The steady growth pattern strongly suggests a leak — objects being created but never freed.

---

## Question 10 (Architecture Decision)
You're designing a Python service that maintains an in-memory cache of 10 million user sessions (each ~2KB). Sessions expire after 30 minutes of inactivity. How should you architect the memory management?

- A) Use a regular `dict` with a background thread that periodically deletes expired entries
- B) Use `weakref.WeakValueDictionary` where session objects are weakly referenced, combined with a TTL dict that tracks expiry — expired entries are naturally collected when no strong references remain
- C) Store sessions in a database — don't use in-memory storage
- D) Use `__slots__` on the session class and a regular `dict`

**Answer: B**
**Explanation:** `WeakValueDictionary` prevents the cache from keeping sessions alive beyond their useful lifetime. When a session expires, the application code stops referencing it, and it's automatically collected. The TTL dict provides a deterministic expiry mechanism. This combination gives you O(1) lookup, automatic cleanup without explicit deletion, and bounded memory usage. For a 20GB cache, this prevents unbounded growth. Libraries like `cachetools` implement exactly this pattern with `TTLCache` and weak references.

---
