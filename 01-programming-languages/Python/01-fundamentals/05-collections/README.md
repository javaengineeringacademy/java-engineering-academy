# Collections

Lists, dictionaries, sets, and tuples — Python's core data structures.

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
