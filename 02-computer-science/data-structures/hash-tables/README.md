# Hash Tables

## Table of Contents

- [Overview](#overview)
- [Hash Functions](#hash-functions)
- [Collision Handling](#collision-handling)
  - [Chaining](#chaining)
  - [Open Addressing](#open-addressing)
- [Load Factor and Resizing](#load-factor-and-resizing)
- [Implementation](#implementation)
- [Time Complexity](#time-complexity)
- [Concurrent Hash Maps](#concurrent-hash-maps)
- [Use Cases](#use-cases)

---

## Overview

A hash table (hash map) is a data structure that maps keys to values using a hash function for O(1) average-time operations.

```
Hash Table:
Key → Hash Function → Index → Value

┌───────┬───────────────────────┐
│ Index │ Chain (Key-Value)     │
├───────┼───────────────────────┤
│   0   │ → (name, Alice) → None│
│   1   │ → None                │
│   2   │ → (age, 30) → (id, 5)│
│   3   │ → None                │
│   4   │ → (city, NYC) → None  │
└───────┴───────────────────────┘
```

### Key Characteristics

- **O(1) average** for insert, delete, search
- **Unordered** - no guaranteed order
- **Hash function** converts key to array index
- **Collision handling** needed for same hash values

---

## Hash Functions

A good hash function should:
1. Be deterministic (same input → same output)
2. Uniformly distribute keys
3. Be fast to compute
4. Minimize collisions

```python
def simple_hash(key: str, table_size: int) -> int:
    """Simple polynomial rolling hash."""
    hash_value = 0
    for char in key:
        hash_value = (hash_value * 31 + ord(char)) % table_size
    return hash_value

# Examples
print(simple_hash("hello", 10))  # Deterministic
print(simple_hash("world", 10))  # Different key, different hash

# Python's built-in hash
print(hash("hello"))    # Large integer
print(hash(42))         # Same for same value
print(hash((1, 2, 3)))  # Works for tuples
```

### Common Hash Functions

| Function | Description | Use Case |
|----------|-------------|----------|
| Division | h(k) = k mod m | Simple, quick |
| Multiplication | h(k) = floor(m(kA mod 1)) | Good distribution |
| Polynomial | h(k) = Σ k[i] × p^i | Strings |
| Cryptographic | SHA-256, MD5 | Security |

---

## Collision Handling

### Chaining

Each bucket contains a linked list of entries.

```
Chaining:
┌─────┐
│  0  │ → (A,1) → (D,4) → None
├─────┤
│  1  │ → (B,2) → None
├─────┤
│  2  │ → (C,3) → (E,5) → (F,6) → None
├─────┤
│  3  │ → None
└─────┘
```

```python
from typing import Any, Optional, List

class HashNode:
    def __init__(self, key: Any, value: Any):
        self.key = key
        self.value = value
        self.next: Optional['HashNode'] = None

class ChainingHashTable:
    def __init__(self, capacity: int = 16):
        self.capacity = capacity
        self.size = 0
        self.buckets: List[Optional[HashNode]] = [None] * capacity

    def _hash(self, key: Any) -> int:
        return hash(key) % self.capacity

    def put(self, key: Any, value: Any) -> None:
        index = self._hash(key)
        node = self.buckets[index]

        while node:
            if node.key == key:
                node.value = value
                return
            node = node.next

        new_node = HashNode(key, value)
        new_node.next = self.buckets[index]
        self.buckets[index] = new_node
        self.size += 1

        if self.size / self.capacity > 0.75:
            self._resize()

    def get(self, key: Any) -> Optional[Any]:
        index = self._hash(key)
        node = self.buckets[index]

        while node:
            if node.key == key:
                return node.value
            node = node.next

        return None

    def delete(self, key: Any) -> bool:
        index = self._hash(key)
        node = self.buckets[index]
        prev = None

        while node:
            if node.key == key:
                if prev:
                    prev.next = node.next
                else:
                    self.buckets[index] = node.next
                self.size -= 1
                return True
            prev = node
            node = node.next

        return False

    def _resize(self) -> None:
        old_buckets = self.buckets
        self.capacity *= 2
        self.buckets = [None] * self.capacity
        self.size = 0

        for bucket in old_buckets:
            node = bucket
            while node:
                self.put(node.key, node.value)
                node = node.next

    def keys(self) -> List[Any]:
        result = []
        for bucket in self.buckets:
            node = bucket
            while node:
                result.append(node.key)
                node = node.next
        return result

    def values(self) -> List[Any]:
        result = []
        for bucket in self.buckets:
            node = bucket
            while node:
                result.append(node.value)
                node = node.next
        return result
```

### Open Addressing

All entries stored in the table itself. On collision, probe for next empty slot.

```
Linear Probing:
Insert A (hash=2), B (hash=2), C (hash=2):

Step 1: A at index 2
[_, _, A, _, _]

Step 2: B collides, probe to 3
[_, _, A, B, _]

Step 3: C collides, probe to 4
[_, _, A, B, C]
```

```python
class OpenAddressingHashTable:
    def __init__(self, capacity: int = 16):
        self.capacity = capacity
        self.size = 0
        self.keys = [None] * capacity
        self.values = [None] * capacity
        self.DELETED = object()  # Sentinel for deleted slots

    def _hash(self, key: Any) -> int:
        return hash(key) % self.capacity

    def _probe(self, key: Any, probe_func) -> int:
        index = self._hash(key)
        i = 0
        while i < self.capacity:
            probe_index = probe_func(index, i)
            if self.keys[probe_index] is None or self.keys[probe_index] is self.DELETED:
                return probe_index
            if self.keys[probe_index] == key:
                return probe_index
            i += 1
        raise Exception("Hash table is full")

    def put(self, key: Any, value: Any) -> None:
        if self.size / self.capacity > 0.7:
            self._resize()

        index = self._probe(key, lambda h, i: (h + i) % self.capacity)

        if self.keys[index] is None or self.keys[index] is self.DELETED:
            self.size += 1

        self.keys[index] = key
        self.values[index] = value

    def get(self, key: Any) -> Optional[Any]:
        index = self._hash(key)
        i = 0
        while i < self.capacity:
            probe_index = (index + i) % self.capacity
            if self.keys[probe_index] is None:
                return None
            if self.keys[probe_index] == key:
                return self.values[probe_index]
            i += 1
        return None

    def delete(self, key: Any) -> bool:
        index = self._hash(key)
        i = 0
        while i < self.capacity:
            probe_index = (index + i) % self.capacity
            if self.keys[probe_index] is None:
                return False
            if self.keys[probe_index] == key:
                self.keys[probe_index] = self.DELETED
                self.values[probe_index] = None
                self.size -= 1
                return True
            i += 1
        return False

    def _resize(self) -> None:
        old_keys = self.keys
        old_values = self.values
        self.capacity *= 2
        self.keys = [None] * self.capacity
        self.values = [None] * self.capacity
        self.size = 0

        for key, value in zip(old_keys, old_values):
            if key is not None and key is not self.DELETED:
                self.put(key, value)

# Quadratic Probing variant
class QuadraticProbingHashTable(OpenAddressingHashTable):
    def _probe(self, key: Any, i: int) -> int:
        index = self._hash(key)
        return (index + i * i) % self.capacity

# Double Hashing variant
class DoubleHashingHashTable(OpenAddressingHashTable):
    def _hash2(self, key: Any) -> int:
        return 1 + (hash(key) % (self.capacity - 1))

    def _probe(self, key: Any, i: int) -> int:
        h1 = self._hash(key)
        h2 = self._hash2(key)
        return (h1 + i * h2) % self.capacity
```

---

## Load Factor and Resizing

**Load factor** = number of entries / number of buckets

```
Load Factor Behavior:
┌─────────────┬───────────────────────────────────┐
│ Load Factor │ Behavior                          │
├─────────────┼───────────────────────────────────┤
│    0.0      │ Empty table                       │
│   < 0.5     │ Good performance                  │
│   0.5-0.75  │ Acceptable                        │
│   > 0.75    │ Too many collisions, resize       │
│    1.0      │ One entry per bucket (worst case) │
└─────────────┴───────────────────────────────────┘
```

### Resizing Strategy

```python
def should_resize(capacity: int, size: int, threshold: float = 0.75) -> bool:
    return size / capacity > threshold

# Typical resize:
# 1. Create new table with 2x capacity
# 2. Rehash all existing entries
# 3. Replace old table with new one
# Time: O(n) but amortized O(1) per operation
```

---

## Implementation

```python
class HashMap:
    """Complete hash map implementation."""

    def __init__(self):
        self._capacity = 8
        self._size = 0
        self._buckets = [[] for _ in range(self._capacity)]
        self._keys = set()

    def _hash(self, key: Any) -> int:
        return hash(key) % self._capacity

    def __setitem__(self, key: Any, value: Any) -> None:
        if self._size / self._capacity > 0.75:
            self._resize()

        index = self._hash(key)
        bucket = self._buckets[index]

        for i, (k, v) in enumerate(bucket):
            if k == key:
                bucket[i] = (key, value)
                return

        bucket.append((key, value))
        self._keys.add(key)
        self._size += 1

    def __getitem__(self, key: Any) -> Any:
        index = self._hash(key)
        bucket = self._buckets[index]

        for k, v in bucket:
            if k == key:
                return v

        raise KeyError(key)

    def __delitem__(self, key: Any) -> None:
        index = self._hash(key)
        bucket = self._buckets[index]

        for i, (k, v) in enumerate(bucket):
            if k == key:
                del bucket[i]
                self._keys.discard(key)
                self._size -= 1
                return

        raise KeyError(key)

    def __contains__(self, key: Any) -> bool:
        return key in self._keys

    def __len__(self) -> int:
        return self._size

    def get(self, key: Any, default: Any = None) -> Any:
        try:
            return self[key]
        except KeyError:
            return default

    def keys(self):
        return self._keys.copy()

    def values(self):
        result = []
        for bucket in self._buckets:
            for k, v in bucket:
                result.append(v)
        return result

    def items(self):
        result = []
        for bucket in self._buckets:
            for k, v in bucket:
                result.append((k, v))
        return result

    def _resize(self) -> None:
        old_buckets = self._buckets
        self._capacity *= 2
        self._buckets = [[] for _ in range(self._capacity)]
        self._size = 0
        self._keys.clear()

        for bucket in old_buckets:
            for key, value in bucket:
                self[key] = value

    def __repr__(self) -> str:
        items = [f"{k!r}: {v!r}" for k, v in self.items()]
        return "{" + ", ".join(items) + "}"

# Usage
hash_map = HashMap()
hash_map["name"] = "Alice"
hash_map["age"] = 30
hash_map["city"] = "NYC"

print(hash_map["name"])      # Alice
print("age" in hash_map)     # True
print(len(hash_map))         # 3
```

---

## Time Complexity

| Operation | Average | Worst | Space |
|-----------|---------|-------|-------|
| Insert | O(1) | O(n) | O(n) |
| Search | O(1) | O(n) | O(1) |
| Delete | O(1) | O(n) | O(1) |
| Resize | O(n) | O(n) | O(n) |

**Worst case** occurs when all keys hash to the same bucket (degenerates to linked list).

---

## Concurrent Hash Maps

### Java ConcurrentHashMap

```java
import java.util.concurrent.ConcurrentHashMap;

// Thread-safe hash map
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("key", 1);
map.computeIfAbsent("key", k -> 0);

// Segment-level locking (Java 7)
// Lock-striping: multiple locks protect different segments

// CAS-based (Java 8+)
// Uses volatile reads and CAS for lock-free operations
```

### Python Thread-Safe Operations

```python
import threading
from typing import Any, Dict

class ThreadSafeHashMap:
    def __init__(self):
        self._data: Dict[Any, Any] = {}
        self._lock = threading.RLock()

    def put(self, key: Any, value: Any) -> None:
        with self._lock:
            self._data[key] = value

    def get(self, key: Any) -> Any:
        with self._lock:
            return self._data.get(key)

    def delete(self, key: Any) -> bool:
        with self._lock:
            if key in self._data:
                del self._data[key]
                return True
            return False

    def contains(self, key: Any) -> bool:
        with self._lock:
            return key in self._data
```

---

## Use Cases

| Use Case | Description |
|----------|-------------|
| Caching | Store computed results |
| Database indexing | Fast lookups |
| Symbol tables | Compiler variable storage |
| Counting | Frequency of elements |
| Deduplication | Remove duplicates |
| Dictionary | Spell checkers |
| Routing tables | Network routers |
| Session storage | Web applications |

---

## Summary

| Aspect | Description |
|--------|-------------|
| Average Time | O(1) for all operations |
| Worst Time | O(n) - rare, with good hash function |
| Space | O(n) |
| Ordering | None |
| Best For | Fast lookups, inserts, deletes |
| Avoid When | Need ordered iteration, range queries |
