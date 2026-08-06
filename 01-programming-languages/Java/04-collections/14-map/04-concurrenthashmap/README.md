# ConcurrentHashMap

## 1. Introduction

ConcurrentHashMap is a thread-safe implementation of the `Map` interface that allows concurrent access without locking the entire map. Introduced in Java 5 as part of the `java.util.concurrent` package, it provides better performance than `Collections.synchronizedMap()` by allowing concurrent reads and segment-based locking for writes.

Unlike Hashtable which locks the entire map for every operation, ConcurrentHashMap uses a more sophisticated approach:
- **Java 7**: Segment locking (divides the table into 16 segments, each with its own lock)
- **Java 8+**: CAS operations + synchronized on individual buckets (fine-grained locking)

ConcurrentHashMap is the go-to choice for concurrent Map operations in Java. It supports non-blocking reads, fully concurrent updates, and a set of atomic operations (compute, merge, putIfAbsent) that eliminate the need for external synchronization.

## 2. Learning Objectives

- Create and use ConcurrentHashMap for thread-safe operations
- Understand the difference between ConcurrentHashMap and Collections.synchronizedMap()
- Learn about CAS (Compare-And-Swap) operations
- Master atomic operations: compute, merge, computeIfAbsent, computeIfPresent
- Understand the segment locking mechanism (Java 7) vs bucket-level locking (Java 8+)
- Learn about weakly consistent iterators
- Understand when to use ConcurrentHashMap vs other synchronization strategies
- Know the limitations and pitfalls of ConcurrentHashMap

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 15: HashMap (understand hash table internals)
- Basic understanding of threads and synchronization
- Familiarity with atomic operations

## 4. Why This Concept Exists

Before ConcurrentHashMap, Java developers had limited options for concurrent maps:

1. **Hashtable**: Synchronized all methods, terrible performance (single lock)
2. **Collections.synchronizedMap()**: Wrapper that synchronizes all methods, same problem
3. **Manual synchronization**: `synchronized(map) { ... }` for compound operations

These approaches had problems:
- **Lock contention**: Only one thread could access the map at a time
- **No atomic compound operations**: check-then-act required external synchronization
- **Poor scalability**: Performance degraded with more threads

ConcurrentHashMap solves these by:
- **Fine-grained locking**: Lock only the affected bucket, not the entire map
- **Lock-free reads**: `get()` operations never block
- **Atomic operations**: `compute()`, `merge()`, `putIfAbsent()` are thread-safe
- **Weakly consistent iterators**: Don't throw ConcurrentModificationException

## 4b. Why ConcurrentHashMap Uses CAS + Synchronized

ConcurrentHashMap's design evolved from Java 7's segment locking to Java 8+'s CAS + synchronized approach. Each evolution addressed real scalability limitations.

**Java 7 used lock striping — a significant improvement over Hashtable, but with limitations.** The map was divided into 16 segments, each with its own lock. This allowed 16 concurrent writes (one per segment), but had problems: the segment count was fixed at construction time, segments could not be rebalanced, and reads still required volatile reads for visibility. The lock granularity was too coarse for high-contention workloads.

**CAS + synchronized is more scalable because it locks at the bucket level.** In Java 8+, each bucket is an independent lock unit. If thread A writes to bucket 5 and thread B writes to bucket 12, they never contend — no lock is shared. This is fundamentally more parallel than fixed segments because contention scales with the number of buckets, not with a hardcoded segment count.

**CAS enables lock-free reads and writes to empty buckets.** When `put()` encounters an empty bucket, it uses a CAS (Compare-And-Swap) operation to atomically install the new node — no lock needed. CAS is a single CPU instruction (`cmpxchg` on x86) that is extremely fast. This means the most common case (writing to an empty bucket) avoids lock acquisition entirely.

**Synchronized is used only for bucket contention.** When two threads write to the same bucket simultaneously, CAS will fail for one of them. That thread then acquires a synchronized lock on the bucket's first node. This is a narrow lock: only threads contending on that exact bucket are blocked. All other buckets remain fully accessible.

**Why not just synchronized on the entire map?** That's Hashtable's approach — it serializes all operations. With 100 threads writing to different buckets, Hashtable forces them into a single-file line. ConcurrentHashMap with bucket-level locking allows all 100 threads to proceed in parallel, with synchronization only when two threads hit the same bucket (statistically rare with a good hash function and 16+ buckets).

**CAS vs synchronized tradeoff:**

| Scenario | CAS | synchronized |
|----------|-----|-------------|
| Empty bucket write | O(1), no lock | N/A (CAS preferred) |
| Same-bucket contention | Fallback to synchronized | Necessary |
| Read operations | Never blocks | N/A (volatile read) |
| Scalability | Excellent | Good (bucket-level) |

## 5. Problem Statement

Consider building a web application with:
- Multiple threads handling concurrent requests
- A shared cache that stores user sessions
- Frequent reads (session lookups) and writes (session creation/updates)
- High concurrency (thousands of requests per second)

Using Hashtable or synchronizedMap:
- Every request blocks all other requests
- Response time increases with load
- System doesn't scale

Using ConcurrentHashMap:
- Concurrent reads don't block each other
- Writes lock only the affected bucket
- System scales with more threads

## 6. Theory

### ConcurrentHashMap Structure (Java 8+)

```java
transient volatile Node<K,V>[] table;  // Array of buckets
```

### Key Differences from HashMap

| Feature | HashMap | ConcurrentHashMap |
|---------|---------|-------------------|
| Thread-safe | No | Yes |
| Null keys | One | Not allowed |
| Null values | Multiple | Not allowed |
| Iterators | fail-fast | weakly consistent |
| Locking | None | Bucket-level (CAS + synchronized) |

### CAS (Compare-And-Swap)

CAS is a hardware-level atomic operation that:
1. Reads the current value
2. Compares it with expected value
3. If equal, updates to new value
4. Returns whether the update succeeded

```java
// Pseudocode for CAS
boolean compareAndSwap(V expected, V newValue) {
    if (currentValue == expected) {
        currentValue = newValue;
        return true;
    }
    return false;
}
```

### Bucket-Level Locking (Java 8+)

```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();  // CAS to initialize
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
                break;  // CAS to add to empty bucket
        }
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        else {
            synchronized (f) {  // Lock only this bucket
                // ... normal put logic
            }
        }
    }
    return null;
}
```

### Weakly Consistent Iterators

ConcurrentHashMap iterators are weakly consistent:
- Reflect the state of the map at some point since the iterator was created
- May (but are not guaranteed to) reflect modifications made after creation
- Never throw ConcurrentModificationException
- May return duplicates or miss elements

## 7. Internal Working

### Table Initialization

```java
private final Node<K,V>[] initTable() {
    Node<K,V>[] tab; int n;
    while ((tab = table) == null || (n = tab.length) == 0) {
        if ((sc = sizeCtl) < 0)
            Thread.yield();  // Lost initialization race
        else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
            try {
                if ((tab = table) == null || (n = tab.length) == 0) {
                    n = (sc > 0) ? sc : DEFAULT_CAPACITY;  // 16
                    @SuppressWarnings("unchecked")
                    Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                    table = tab = nt;
                    sc = n - (n >>> 2);  // 0.75 * n
                }
            } finally {
                sizeCtl = sc;
            }
            break;
        }
    }
    return tab;
}
```

### TabAt (Volatile Read)

```java
static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
    return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
}
```

Uses `Unsafe.getObjectVolatile()` for volatile read semantics.

### CAS TabAt (CAS Operation)

```java
static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i,
                                     Node<K,V> c, Node<K,V> v) {
    return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
}
```

Uses `Unsafe.compareAndSwapObject()` for CAS operation.

### Size Calculation

```java
public int size() {
    long n = sumCount();
    return ((n < 0) ? 0 :
            (n > (long)Integer.MAX_VALUE) ? Integer.MAX_VALUE :
            (int)n);
}

final long sumCount() {
    CounterCell[] as = counterCells; CounterCell a;
    long sum = baseCount;
    if (as != null) {
        for (int i = 0; i < as.length; ++i) {
            if ((a = as[i]) != null)
                sum += a.value;
        }
    }
    return sum;
}
```

Uses `LongAdder`-style counters for better performance under contention.

## 8. JVM Perspective

### Memory Allocation

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
// JVM allocates:
// - ConcurrentHashMap object header: 12 bytes
// - table reference: 8 bytes
// - baseCount: 8 bytes
// - sizeCtl: 4 bytes
// - counterCells reference: 8 bytes
// Total ConcurrentHashMap object: ~48 bytes

// Each Node:
// - Node object header: 12 bytes
// - hash: 4 bytes
// - key reference: 8 bytes
// - value reference: 8 bytes
// - next reference: 8 bytes
// Total per Node: ~40 bytes
```

### Unsafe Operations

ConcurrentHashMap uses `sun.misc.Unsafe` for:
- Volatile reads (`getObjectVolatile`)
- CAS operations (`compareAndSwapObject`)
- Ordered writes (`putObject`)

### Memory Barriers

CAS operations provide memory barriers:
- **Acquire barrier**: Prevents reordering of subsequent operations
- **Release barrier**: Prevents reordering of preceding operations
- Ensures visibility of updates across threads

## 9. Memory Representation

```
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("Alice", 30);
map.put("Bob", 25);

Memory layout:
┌────────────────────────────────┐
│ ConcurrentHashMap object       │
├────────────────────────────────┤
│ Object header (12 bytes)       │
│ table ──────────────────────────┐
│ baseCount = 0 (8 bytes)        │     │
│ sizeCtl = 12 (4 bytes)         │     │
│ counterCells ──────────────────────┐
└────────────────────────────────┘     │
                                       ▼
                                Node[] table (capacity 16)
                                ┌────────────────────────┐
                                │ [0] → null             │
                                │ [1] → null             │
                                │ ...                    │
                                │ [5] → Node("Alice",30) │
                                │ [6] → Node("Bob",25)   │
                                │ [7] → null             │
                                │ ...                    │
                                └────────────────────────┘

Node("Alice", 30):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ hash (int, 4 bytes)         │
│ key → String "Alice"        │
│ value → Integer 30          │
│ next → null                 │
└─────────────────────────────┘
```

## 10. Syntax

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

// ============================================
// CREATION
// ============================================
Map<K, V> map = new ConcurrentHashMap<>();
Map<K, V> map = new ConcurrentHashMap<>(16);           // Initial capacity
Map<K, V> map = new ConcurrentHashMap<>(16, 0.75f);   // Capacity and load factor
Map<K, V> map = new ConcurrentHashMap<>(16, 0.75f, 8); // With concurrency level

// ============================================
// BASIC OPERATIONS (Thread-Safe)
// ============================================
map.put(key, value);                    // Returns old value or null
map.putIfAbsent(key, value);            // Atomic: add only if absent
map.get(key);                           // Non-blocking read
map.remove(key);                        // Returns old value or null
map.remove(key, value);                 // Atomic: remove only if matches
map.replace(key, newValue);             // Returns old value or null
map.replace(key, oldValue, newValue);   // Atomic: conditional replace

// ============================================
// ATOMIC OPERATIONS
// ============================================
// compute: Atomic update with function
map.compute(key, (k, v) -> v == null ? defaultValue : transform(v));

// computeIfAbsent: Atomic add-if-absent
map.computeIfAbsent(key, k -> expensiveComputation(k));

// computeIfPresent: Atomic update-if-present
map.computeIfPresent(key, (k, v) -> v + 1);

// merge: Atomic merge with function
map.merge(key, value, (old, newVal) -> old + newVal);

// ============================================
// SEARCHING
// ============================================
boolean hasKey = map.containsKey(key);      // O(1) average
boolean hasValue = map.containsValue(value); // O(n)
boolean empty = map.isEmpty();
int size = map.size();                       // Approximate

// ============================================
// VIEW COLLECTIONS
// ============================================
Set<K> keys = map.keySet();              // Weakly consistent
Collection<V> values = map.values();     // Weakly consistent
Set<Map.Entry<K,V>> entries = map.entrySet(); // Weakly consistent

// ============================================
// ITERATION
// ============================================
// forEach (weakly consistent)
map.forEach((key, value) -> System.out.println(key + " = " + value));

// forEach with parallelism threshold
map.forEach(4, (key, value) -> System.out.println(key + " = " + value));

// search (parallel)
V result = map.search(4, (key, value) -> value > 30 ? key : null);

// reduce (parallel)
int sum = map.reduceValues(4, v -> v, Integer::sum);

// ============================================
// AGGREGATE OPERATIONS
// ============================================
long totalCount = map.mappingCount();  // Better than size() for large maps
map.forEachKey(4, key -> process(key));
map.forEachValue(4, value -> process(value));
```

## 11. Easy Example

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ConcurrentHashMapBasics {
    public static void main(String[] args) {
        // Create and populate
        ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);

        System.out.println("Map: " + scores);
        System.out.println("Size: " + scores.size());

        // Thread-safe operations
        scores.putIfAbsent("Diana", 88);
        scores.replace("Bob", 90);

        System.out.println("After updates: " + scores);

        // Atomic operations
        scores.compute("Alice", (key, value) -> value + 5);
        scores.merge("Charlie", 3, Integer::sum);

        System.out.println("After atomic operations: " + scores);

        // Search
        System.out.println("Contains Alice: " + scores.containsKey("Alice"));
        System.out.println("Contains 95: " + scores.containsValue(95));

## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

