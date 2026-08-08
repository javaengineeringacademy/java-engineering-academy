# Module 04: Collections Framework

> **Difficulty:** Intermediate
> **Reading:** 35 min | **Practice:** 60 min | **Total:** 95 min

## Overview

Almost every Java application needs to store and manipulate groups of objects efficiently. Arrays are fixed-size and lack built-in methods for common operations. The Collections Framework gives you standard data structures — List, Set, Queue, and Map — with proven implementations and algorithms, so you don't have to reinvent them.

## Module Structure

| # | Topic | Description |
|---|-------|-------------|
| 01 | [Introduction](01-introduction/) | Framework overview, hierarchy, when to use what |
| 02 | [List](02-list/) | ArrayList, LinkedList, Vector, Stack, CopyOnWriteArrayList |
| 03 | [Set](03-set/) | HashSet, LinkedHashSet, TreeSet |
| 04 | [Map](04-map/) | HashMap, LinkedHashMap, TreeMap, ConcurrentHashMap |
| 05 | [Queue](05-queue/) | PriorityQueue, Deque (ArrayDeque) |
| 06 | [Enumeration](06-enumeration/) | Legacy traversal (Vector, Hashtable, StringTokenizer) |
| 07 | [Iterator](07-iterator/) | Iterator, ListIterator, Iterable, iterator internals |
| 08 | [Comparable & Comparator](08-comparable-comparator/) | Natural ordering, custom ordering, sorting strategies |
| 09 | [Fail-Fast vs Fail-Safe](09-fail-fast-vs-fail-safe/) | ConcurrentModificationException, concurrent iteration |
| 10 | [Collection Algorithms](10-collection-algorithms/) | Collections utility methods (sort, search, shuffle) |
| 11 | [Collections Utilities](11-collections-utilities/) | Unmodifiable, synchronized, empty/singleton wrappers |
| 12 | [Internals](12-internals/) | ArrayList internals, HashMap internals, cache locality |
| 13 | [Memory](13-memory/) | Memory footprint analysis for different collections |
| 14 | [Stream Operations](14-stream-operations/) | Stream API, Collectors, parallel streams |
| 15 | [Why Not](15-why-not/) | When NOT to use LinkedList, Hashtable, Vector, Stack |

## Supporting Resources

| Resource | Description |
|----------|-------------|
| [Roadmap](roadmap.md) | Learning path and prerequisites |
| [Exercises](exercises/) | Practice exercises for each topic |
| [Solutions](solutions/) | Exercise solutions |
| [Quizzes](quizzes/) | Knowledge checks |
| [Interview](interview/) | Common interview questions |
| [Projects](projects/) | Mini-project: Student Management System |
| [Examples](examples/) | Additional code examples |
| [References](references/) | External resources and documentation |

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

## Performance Comparison

| Collection | Access | Insert | Delete | Thread-Safe |
|------------|--------|--------|--------|-------------|
| ArrayList | O(1) | O(n) | O(n) | No |
| LinkedList | O(n) | O(1) | O(1) | No |
| HashSet | O(1) | O(1) | O(1) | No |
| TreeSet | O(log n) | O(log n) | O(log n) | No |
| HashMap | O(1) | O(1) | O(1) | No |
| TreeMap | O(log n) | O(log n) | O(log n) | No |
| ConcurrentHashMap | O(1) | O(1) | O(1) | Yes |

## History

- **1996** — Java 1.0: Vector, Hashtable, Enumeration
- **1998** — Java 1.2: Collections Framework (List, Set, Map, Iterator)
- **2001** — Java 1.3: Collections.unmodifiable* wrappers
- **2004** — Java 5: Generics, for-each, autoboxing
- **2011** — Java 7: Diamond operator, NavigableMap/NavigableSet
- **2014** — Java 8: Stream API, forEach, removeIf
- **2017** — Java 9: Factory methods (List.of, Set.of, Map.of)
- **2021** — Java 16: Stream.toList()
- **2021** — Java 17: SequencedCollection interface

## Prerequisites

- OOP concepts
- Generics basics
- Exception handling

## Cross-References

- **Previous Module:** [03 - Exception Handling](../03-exception-handling/)
- **Next Module:** [05 - Text Processing](../05-text-processing/)
- **Related:** [06 - Generics](../06-generics/) — parameterized collection types
- **Related:** [07 - Functional Programming](../07-functional-programming/) — Stream API for collection processing
- **Related:** [09 - Multithreading](../09-multithreading/) — thread-safe collections
