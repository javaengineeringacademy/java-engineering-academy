# Collections Module Roadmap

## Overview

This roadmap guides you through the Java Collections Framework module, from fundamentals to advanced topics.

## Learning Path

### Phase 1: Core Collections (Start Here)

| Order | Topic | Description | Difficulty |
|-------|-------|-------------|------------|
| 01 | [Introduction](01-introduction/) | Framework overview, hierarchy, when to use what | Beginner |
| 02 | [List](02-list/) | ArrayList, LinkedList, Vector, Stack, CopyOnWriteArrayList | Beginner |
| 03 | [Set](03-set/) | HashSet, LinkedHashSet, TreeSet | Beginner |
| 04 | [Map](04-map/) | HashMap, LinkedHashMap, TreeMap, ConcurrentHashMap | Beginner |
| 05 | [Queue](05-queue/) | PriorityQueue, Deque (ArrayDeque) | Beginner |

### Phase 2: Iteration & Comparison

| Order | Topic | Description | Difficulty |
|-------|-------|-------------|------------|
| 06 | [Enumeration](06-enumeration/) | Legacy traversal (Vector, Hashtable, StringTokenizer) | Beginner |
| 07 | [Iterator](07-iterator/) | Iterator, ListIterator, Iterable, iterator internals | Intermediate |
| 08 | [Comparable & Comparator](08-comparable-comparator/) | Natural ordering, custom ordering, sorting strategies | Intermediate |

### Phase 3: Correctness & Safety

| Order | Topic | Description | Difficulty |
|-------|-------|-------------|------------|
| 09 | [Fail-Fast vs Fail-Safe](09-fail-fast-vs-fail-safe/) | ConcurrentModificationException, concurrent iteration | Intermediate |
| 10 | [Collection Algorithms](10-collection-algorithms/) | Collections utility methods (sort, search, shuffle) | Intermediate |
| 11 | [Collections Utilities](11-collections-utilities/) | Unmodifiable, synchronized, empty/singleton wrappers | Intermediate |

### Phase 4: Internals & Performance

| Order | Topic | Description | Difficulty |
|-------|-------|-------------|------------|
| 12 | [Internals](12-internals/) | ArrayList internals, HashMap internals, cache locality | Advanced |
| 13 | [Memory](13-memory/) | Memory footprint analysis for different collections | Advanced |
| 14 | [Stream Operations](14-stream-operations/) | Stream API, Collectors, parallel streams | Advanced |
| 15 | [Why Not](15-why-not/) | When NOT to use LinkedList, Hashtable, Vector, Stack | Intermediate |

## Quick Reference: Choosing the Right Collection

```
Need ordered, duplicates?        → List (ArrayList default)
Need unique elements?            → Set (HashSet default)
Need key-value pairs?            → Map (HashMap default)
Need priority processing?        → PriorityQueue
Need FIFO queue?                 → ArrayDeque
Need stack?                      → ArrayDeque (not Stack!)
Need thread-safe map?            → ConcurrentHashMap
Need thread-safe list (read)?    → CopyOnWriteArrayList
Need sorted keys?                → TreeMap
Need sorted elements?            → TreeSet
Need insertion order?            → LinkedHashSet / LinkedHashMap
```

## Prerequisites

- Java fundamentals (variables, control flow, methods)
- OOP concepts (interfaces, inheritance, polymorphism)
- Generics basics
- Basic time complexity (Big O notation)

## Estimated Time

| Level | Topics | Time |
|-------|--------|------|
| Beginner | 01-05 | 4-6 hours |
| Intermediate | 06-11 | 4-6 hours |
| Advanced | 12-15 | 4-6 hours |
| **Total** | All | **12-18 hours** |
