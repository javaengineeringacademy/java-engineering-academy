# Java Collections Framework

## 1. Introduction

The Java Collections Framework (JCF) is a unified architecture for representing and manipulating groups of objects. Introduced in Java 1.2, it provides a coherent set of interfaces, implementations, and algorithms that replace the legacy `Vector`, `Hashtable`, and manual array management with a modern, high-performance system.

## 2. Why It Exists

Before JCF, Java developers faced several problems:

| Problem | Description |
|---------|-------------|
| No unified API | Each class (Vector, Hashtable, Stack) had its own methods |
| Poor performance | Legacy classes synchronized everything, adding overhead |
| No algorithms | No standard sort, search, or shuffle |
| No interoperability | Difficult to convert between collection types |
| Type safety | Raw Object storage required manual casting |

JCF provides a consistent API, high-performance implementations, standard algorithms, and generics for type safety.

## 3. History

| Version | Change |
|---------|--------|
| Java 1.2 | Collections Framework introduced (List, Set, Map, Queue) |
| Java 5 | Generics added for type safety |
| Java 6 | NavigableMap, NavigableSet added |
| Java 7 | Deque interface completed |
| Java 8 | Stream API, lambda support, default methods |
| Java 9 | List.of(), Map.of(), Set.of() factory methods |
| Java 10 | CopyOnWriteArrayList improvements |
| Java 16 | Record types as Map keys |
| Java 21 | SequencedCollection interface |

## 4. Arrays vs Collections

| Feature | Arrays | Collections |
|---------|--------|-------------|
| Size | Fixed | Dynamic |
| Type safety | Runtime | Compile-time (generics) |
| API | Basic (length, clone) | Rich (add, remove, search, sort) |
| Memory | Primitives allowed | Objects only |
| Performance | Fastest | Slight overhead |

## 5. Framework Hierarchy

```
Iterable<E>
├── Collection<E>
│   ├── List<E>       → ArrayList, LinkedList, Vector
│   ├── Set<E>        → HashSet, LinkedHashSet, TreeSet
│   ├── Queue<E>      → PriorityQueue, ArrayDeque
│   └── Deque<E>      → ArrayDeque, LinkedList

Map<K,V>
├── HashMap<K,V>      → LinkedHashMap
├── TreeMap<K,V>
├── Hashtable<K,V>
└── ConcurrentHashMap<K,V>
```

## 6. Collection vs Map

| Aspect | Collection | Map |
|--------|-----------|-----|
| Stores | Individual elements | Key-value pairs |
| Duplicates | Depends on subinterface | No duplicate keys |
| Interface | Collection<E> | Map<K,V> |
| Hierarchy | Part of Collection | Separate hierarchy |

## 7. Interface vs Implementation

| Interface | Purpose | Common Implementation |
|-----------|---------|----------------------|
| List | Ordered, duplicates | ArrayList |
| Set | Unique elements | HashSet |
| Queue | FIFO processing | ArrayDeque |
| Deque | Double-ended queue | ArrayDeque |
| Map | Key-value pairs | HashMap |

## 8. Thread-Safe Overview

| Collection | Thread-Safe | Alternative |
|------------|------------|-------------|
| ArrayList | No | CopyOnWriteArrayList |
| HashMap | No | ConcurrentHashMap |
| HashSet | No | Collections.synchronizedSet() |
| TreeMap | No | Collections.synchronizedSortedMap() |
| Vector | Yes (all sync) | Avoid in new code |
| Hashtable | Yes (all sync) | Avoid in new code |

## 9. Performance Overview

| Operation | ArrayList | LinkedList | HashSet | HashMap | TreeMap |
|-----------|-----------|------------|---------|---------|---------|
| Add | O(1) amortized | O(1) | O(1) | O(1) | O(log n) |
| Remove | O(n) | O(n) | O(1) | O(1) | O(log n) |
| Contains | O(n) | O(n) | O(1) | O(1) | O(log n) |
| Get by index | O(1) | O(n) | N/A | N/A | N/A |
| Get by key | N/A | N/A | N/A | O(1) | O(log n) |

## 10. Learning Roadmap

```
Phase 1: Basics
├── 00-collection/        Collection interface
├── 01-list/              ArrayList
├── 02-set/               HashSet
└── 03-map/               HashMap

Phase 2: Intermediate
├── 04-queue/             PriorityQueue, ArrayDeque
├── 05-iteration/         Iterators, Lambda, Streams
├── 06-sorting/           Comparable, Comparator
└── 07-searching/         Linear, Binary search

Phase 3: Advanced
├── 08-stream-operations/ Filter, Map, Reduce, Collect
├── 09-exercises/         Practice problems
├── 10-solutions/         Solution code
├── 11-references/        External docs
├── 12-quiz.md            50 questions
└── 13-roadmap.md         Visual path
```
