# Why Not Use These Collections?

## Why NOT Use LinkedList

### Cache Locality Problem

LinkedList nodes are scattered in memory, causing cache misses:

```java
// LinkedList: Poor cache locality
LinkedList<Integer> list = new LinkedList<>();
// Nodes are pointers to objects scattered in heap memory
// Each node requires separate memory allocation
// Cache lines loaded for each node access
```

**Impact:**
- 2-3x slower iteration than ArrayList
- Poor CPU cache performance
- Higher memory overhead (16 bytes per node for pointers)

### Memory Overhead

Each LinkedList node contains:
- Element reference (8 bytes)
- Next pointer (8 bytes)
- Previous pointer (8 bytes)
- Object header (16 bytes)
- **Total: 40 bytes per element**

vs ArrayList:
- Element reference (8 bytes)
- **Total: 8 bytes per element**

### When to Use ArrayList Instead

- Random access (get/set)
- Iteration
- Most use cases

### When LinkedList is Acceptable

- Frequent insert/delete at both ends (use ArrayDeque instead)
- Never: Use ArrayDeque for queue/deque operations

---

## Why NOT Use Hashtable

### Synchronized Overhead

Every method is synchronized:

```java
// Hashtable: All methods synchronized
Hashtable<String, Integer> table = new Hashtable<>();
table.put("key", 1);  // Synchronized
table.get("key");      // Synchronized
table.containsKey("k"); // Synchronized
```

**Impact:**
- Thread contention under high load
- Reduced concurrency
- Slower than ConcurrentHashMap

### Legacy API

- Pre-dates Collections Framework (Java 1.2)
- Uses Enumeration instead of Iterator
- Doesn't allow null keys or values
- Not recommended for new code

### Use HashMap Instead

```java
// For non-threaded
HashMap<String, Integer> map = new HashMap<>();

// For threaded
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
```

---

## Why NOT Use Vector

### Synchronized Overhead

Like Hashtable, all methods synchronized:

```java
// Vector: All methods synchronized
Vector<Integer> vector = new Vector<>();
vector.add(1);      // Synchronized
vector.get(0);      // Synchronized
vector.remove(0);   // Synchronized
```

**Impact:**
- Unnecessary synchronization overhead
- Thread contention
- Slower than ArrayList

### Legacy API

- Pre-dates Collections Framework
- Uses Enumeration
- Not recommended for new code

### Use ArrayList Instead

```java
// For non-threaded
ArrayList<Integer> list = new ArrayList<>();

// For threaded
CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
```

---

## Why NOT Use Stack

### Deprecated Class

Stack extends Vector, inheriting all its synchronization overhead:

```java
// Stack: Deprecated, use Deque
Stack<Integer> stack = new Stack<>();
stack.push(1);
stack.pop();
```

### Use ArrayDeque Instead

```java
// ArrayDeque: Faster, non-synchronized
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);
stack.pop();
```

**Benefits:**
- No synchronization overhead
- Faster operations
- Better memory efficiency
- Implements Deque interface

---

## Why NOT Use TreeMap for Small Datasets

### HashMap is Faster

For small datasets, HashMap outperforms TreeMap:

```java
// TreeMap: O(log n) operations
TreeMap<Integer, String> treeMap = new TreeMap<>();
treeMap.put(1, "one"); // O(log n)

// HashMap: O(1) amortized
HashMap<Integer, String> hashMap = new HashMap<>();
hashMap.put(1, "one"); // O(1)
```

**Performance comparison:**
- HashMap: O(1) average, O(n) worst case
- TreeMap: O(log n) always
- For small N, HashMap wins

### When TreeMap is Better

- Need sorted order
- Need ceiling/floor operations
- Need range queries
- Need consistent O(log n) performance

---

## Why NOT Use TreeSet When Order Doesn't Matter

### HashSet is Faster

```java
// TreeSet: O(log n) operations
TreeSet<Integer> treeSet = new TreeSet<>();
treeSet.add(1); // O(log n)

// HashSet: O(1) amortized
HashSet<Integer> hashSet = new HashSet<>();
hashSet.add(1); // O(1)
```

**Performance comparison:**
- HashSet: O(1) average
- TreeSet: O(log n) always
- For unordered operations, HashSet wins

### When TreeSet is Better

- Need sorted iteration
- Need NavigableSet operations
- Need range queries
- Need consistent O(log n) performance

---

## Summary

| Collection | Use Instead | Reason |
|------------|-------------|--------|
| LinkedList | ArrayList/ArrayDeque | Cache locality, memory |
| Hashtable | HashMap/ConcurrentHashMap | Synchronization overhead |
| Vector | ArrayList/CopyOnWriteArrayList | Synchronization overhead |
| Stack | ArrayDeque | Deprecated, slower |
| TreeMap | HashMap | Faster for small datasets |
| TreeSet | HashSet | Faster when order doesn't matter |

## Key Takeaways

1. Prefer ArrayList over LinkedList (cache locality)
2. Use HashMap over Hashtable (no synchronization overhead)
3. Use ArrayList over Vector (no synchronization overhead)
4. Use ArrayDeque over Stack (deprecated, slower)
5. Use HashMap over TreeMap unless you need sorted order
6. Use HashSet over TreeSet unless you need sorted order
