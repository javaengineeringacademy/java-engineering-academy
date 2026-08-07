# Collections

When you need to store and organize multiple values, choosing the right data structure is critical. Python's core collections—lists, dictionaries, sets, and tuples—provide ordered, mutable, or immutable containers for sequences, key-value pairs, unique elements, and fixed records.

## Overview

Python provides four built-in collection types, each with distinct characteristics and use cases.

## When to Use

| Collection | Ordered | Mutable | Duplicates | Use Case |
|-----------|---------|---------|------------|----------|
| list | Yes | Yes | Yes | Sequences, stacks, queues |
| dict | Yes (3.7+) | Yes | No (keys) | Key-value mapping |
| set | No | No | No | Membership testing, dedup |
| tuple | Yes | No | Yes | Records, dict keys, constants |

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| List basics | `lists.py:4-11` | Creation, mixed types |
| List slicing | `lists.py:16-21` | [start:stop:step] |
| List methods | `lists.py:25-35` | append, extend, remove |
| Sorting | `lists.py:41-48` | sorted(), sort() |
| Dict basics | `dicts.py:4-10` | Creation patterns |
| Dict access | `dicts.py:14-18` | [], .get(), in |
| Dict merging | `dicts.py:36-43` | \|, **, update() |
| Set operations | `sets.py:24-39` | union, intersection, diff |
| Subset/superset | `sets.py:43-48` | issubset, <= |
| Tuple unpacking | `tuples.py:40-55` | Star unpacking, swap |
| Named tuples | `tuples.py:58-69` | namedtuple |

## Common Mistakes

1. **Using `{}` for empty set** — creates a dict, use `set()`
2. **Modifying dict while iterating** — create a copy first
3. **Using tuples when lists are needed** — or vice versa
4. **Ignoring set ordering** — sets are unordered

## Interview Questions

1. When would you use a tuple over a list?
2. What is the time complexity of dict lookup?
3. How do you remove duplicates from a list while preserving order?
4. What is a named tuple and when would you use one?

## Production Checklist

### ✅ Before using collections in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Can optimize for specific use cases
- Can explain trade-offs

### Level 5: Master
- Can debug in production
- Can design custom implementations

## Common Myths

### ❌ Myth 1: Lists are always slower than arrays
**Reality:** Lists are optimized for Python objects; arrays are faster for numeric data only.

### ❌ Myth 2: Dictionaries are unordered
**Reality:** Python 3.7+ guarantees insertion order for dictionaries.

### ❌ Myth 3: Sets can't contain lists
**Reality:** Sets can't contain mutable types (like lists), but can contain tuples and frozensets.

## Production Incidents

### Incident 1: Mutable Default Argument Causing Shared State

**Problem:** All instances of a class share the same list as a default argument, causing data to leak between instances.

**Cause:** Using a mutable default argument (e.g., `def __init__(self, items=[])`) in a function or method. The default list is created once at definition time and reused across all calls.

**Impact:** Users see other users' data mixed together. Data corruption across sessions. Difficult to reproduce since it depends on call order.

**Detection:** Check for unexpected shared state between instances using `id()` on mutable attributes. Add logging to track when default values are reused.

**Solution:** Replace mutable defaults with `None` and create new instances inside the function body:
```python
def __init__(self, items=None):
    self.items = items if items is not None else []
```

** Prevention:** Use linters (pylint's `dangerous-default-value`) that flag mutable defaults. Write tests that verify instances don't share state.

---

### Incident 2: List Comprehension Memory Explosion

**Problem:** A list comprehension loads millions of items into memory at once, causing the application to crash with `MemoryError`.

**Cause:** Using a list comprehension `[f(x) for x in large_dataset]` instead of a generator expression `(f(x) for x in large_dataset)` when processing large datasets.

**Impact:** Application crashes with `MemoryError` or gets OOM-killed by the OS. Other services on the same host may be affected.

**Detection:** Monitor memory usage with `psutil` or `/proc/meminfo`. Profile memory with `tracemalloc` to identify allocation hotspots.

**Solution:** Replace list comprehensions with generator expressions when intermediate results don't need indexing:
```python
# Bad: loads all into memory
results = [transform(x) for x in million_items]

# Good: lazy evaluation
results = (transform(x) for x in million_items)
```

** Prevention:** Set memory budgets for critical paths. Use generators for streaming data. Add `tracemalloc`-based tests in CI for memory-sensitive code.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Store and organize data |
| Complexity | O(1) for dict/set lookup, O(n) for list search |
| Thread Safe | No (collections need locks) |
| Best Alternative | Use dataclasses for structured data |
| When to Use | Storing, querying, transforming data |
| When to Avoid | Modifying while iterating, using wrong type |
