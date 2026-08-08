# Fail-Fast vs Fail-Safe Iterators

## Overview

Fail-fast and fail-safe iterators describe how collections handle concurrent modification during iteration. Understanding this distinction is critical for writing correct multithreaded code.

## Learning Objectives

- Understand the difference between fail-fast and fail-safe iterators
- Learn about `ConcurrentModificationException` and its causes
- Identify which collections use which iterator type
- Understand when to use each type
- Learn practical patterns for safe iteration

## Fail-Fast Iterators

**Behavior**: Throw `ConcurrentModificationException` immediately on structural modification.

**Mechanism**: Use `modCount` field to detect modifications.

**Collections**: `ArrayList`, `LinkedList`, `HashMap`, `HashSet`, `TreeMap`, `TreeSet`

```java
// Fail-fast: ArrayList
List<String> list = new ArrayList<>();
Iterator<String> iterator = list.iterator();
list.add("new");  // Structural modification
iterator.next();  // Throws ConcurrentModificationException
```

## Fail-Safe Iterators

**Behavior**: Do not throw exceptions; iterate over a copy or snapshot.

**Mechanism**: Create defensive copies or use concurrent data structures.

**Collections**: `CopyOnWriteArrayList`, `CopyOnWriteArraySet`, `ConcurrentHashMap`, `ConcurrentLinkedQueue`

```java
// Fail-safe: CopyOnWriteArrayList
List<String> concurrentList = new CopyOnWriteArrayList<>();
Iterator<String> safeIterator = concurrentList.iterator();
concurrentList.add("new");  // No exception
safeIterator.next();  // Works fine
```

## Structural Modification

Structural modifications are changes that affect the number of elements or internal structure:
- `add()`, `remove()`, `clear()` are structural
- `Iterator.remove()` is allowed in fail-fast iterators
- `set()` is NOT structural for most collections

## Comparison Table

| Feature | Fail-Fast | Fail-Safe |
|---------|-----------|-----------|
| Exception on concurrent modification | Yes (`ConcurrentModificationException`) | No |
| Mechanism | `modCount` tracking | Snapshot/copy |
| Performance | Low overhead | Higher memory, copy cost |
| Consistency | Detects inconsistency | May miss updates |
| Thread-safe | No | Yes |
| Use case | Single-threaded | Multi-threaded |

## Practical Patterns

### Pattern 1: Use removeIf() (Java 8+)
```java
list.removeIf(s -> s.startsWith("A")); // Safe, no iterator needed
```

### Pattern 2: Iterator.remove()
```java
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().startsWith("A")) {
        it.remove(); // Safe removal
    }
}
```

### Pattern 3: CopyOnWrite for read-heavy
```java
List<String> listeners = new CopyOnWriteArrayList<>();
// Thread-safe iteration, expensive writes
```

### Pattern 4: ConcurrentHashMap for concurrent maps
```java
Map<String, Integer> map = new ConcurrentHashMap<>();
// Never throws ConcurrentModificationException
```
