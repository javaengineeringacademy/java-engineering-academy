# Set Interface

## Overview

The `Set` interface is a collection that contains no duplicate elements. It models the mathematical set abstraction, providing operations for membership testing, union, intersection, and difference. Set extends the `Collection` interface and adds the constraint that all elements must be unique, enforced through `equals()` and `hashCode()`.

## Learning Objectives

- Understand the Set interface and its uniqueness contract
- Learn about element uniqueness enforcement via `equals()` and `hashCode()`
- Understand Set implementations (HashSet, LinkedHashSet, TreeSet)
- Know when to use Set vs List
- Master set operations (union, intersection, difference)
- Recognize Set's thread-safety considerations

## Implementations

| Implementation | Underlying Structure | Order | Null Elements | Thread-Safe |
|----------------|---------------------|-------|---------------|-------------|
| `HashSet` | Hash table | None | Yes (1) | No |
| `LinkedHashSet` | Hash table + linked list | Insertion order | Yes (1) | No |
| `TreeSet` | Red-black tree | Sorted | No | No |

## Key Concepts

### hashCode() and equals() Contract

For Set to work correctly, elements must properly implement:
- `hashCode()`: Returns consistent hash value for equal objects
- `equals()`: Defines equality between objects

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return age == person.age && Objects.equals(name, person.name);
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

### Set Operations

```java
// Union
Set<Integer> union = new HashSet<>(set1);
union.addAll(set2);

// Intersection
Set<Integer> intersection = new HashSet<>(set1);
intersection.retainAll(set2);

// Difference
Set<Integer> difference = new HashSet<>(set1);
difference.removeAll(set2);

// Symmetric Difference
Set<Integer> symDiff = new HashSet<>(set1);
symDiff.addAll(set2);
Set<Integer> common = new HashSet<>(set1);
common.retainAll(set2);
symDiff.removeAll(common);
```

## When to Use Each

- **HashSet**: Default choice, fastest O(1) operations, no order needed
- **LinkedHashSet**: Need insertion order preserved with O(1) operations
- **TreeSet**: Need sorted order or NavigableSet operations (ceiling, floor, etc.)

## Subtopics

- [HashSet](01-hashset/)
- [LinkedHashSet](02-linkedhashset/)
- [TreeSet](03-treeset/)
