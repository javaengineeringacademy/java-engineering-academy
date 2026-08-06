# ConcurrentHashMap Source Code Walkthrough

ConcurrentHashMap is the go-to thread-safe Map in Java. Understanding its implementation is crucial for concurrent programming.

## Evolution: Java 7 vs Java 8+

### Java 7: Segment Locking

```java
// Before Java 8
final Segment<K,V>[] segments;
```

- Divided into segments (default 16)
- Each segment is a mini-HashMap with its own lock
- Fine-grained locking for better concurrency

### Java 8+: CAS + Synchronized

```java
// Java 8+
transient volatile Node<K,V>[] table;
```

- No more segments
- Uses CAS for updates
- `synchronized` for complex operations
- Better performance under high contention

## Internal Structure

### Node Class

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    volatile V val;
    volatile Node<K,V> next;
    
    Node(int hash, K key, V val, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.val = val;
        this.next = next;
    }
}
```

### TreeNode (Treeified Buckets)

```java
static final class TreeNode<K,V> extends Node<K,V> {
    TreeNode<K,V> parent;
    TreeNode<K,V> left;
    TreeNode<K,V> right;
    TreeNode<K,V> prev;
    boolean red;
}
```

### ForwardingNode (During Resize)

```java
static final class ForwardingNode<K,V> extends Node<K,V> {
    final Node<K,V>[] nextTable;
    
    ForwardingNode(Node<K,V>[] tab) {
        super(MOVED, null, null, null);
        this.nextTable = tab;
    }
}
```

## put() Implementation

### Entry Point

```java
public V put(K key, V value) {
    return putVal(key, value, false);
}
```

### putVal() Method

```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    
    int hash = spread(key.hashCode());
    Node<K,V>[] tab = table;
    int n, i, f;
    
    // Step 1: Initialize table if needed
    while (tab == null || (n = tab.length) == 0)
        tab = initTable();
    
    // Step 2: Find bucket (empty slot)
    if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
        // Use CAS to insert
        if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
            break; // Successfully inserted
    }
    
    // Step 3: Handle resize
    else if ((f = tabAt(tab, i)).hash == MOVED)
        tab = helpTransfer(tab, f);
    
    // Step 4: Handle collision
    else {
        V oldVal = null;
        synchronized (f) { // Lock only the bucket
            if (tabAt(tab, i) == f) {
                Node<K,V> e; K k;
                
                // Case 1: First node matches key
                if (f.hash == hash && 
                    ((k = f.key) == key || (key != null && key.equals(k))))
                    e = f;
                
                // Case 2: Treeified bucket
                else if (f instanceof TreeNode)
                    e = ((TreeNode<K,V>)f).putTreeVal(this, tab, hash, key, value);
                
                // Case 3: Linked list
                else {
                    for (int binCount = 0; ; ++binCount) {
                        if ((e = f.next) == null) {
                            f.next = new Node<K,V>(hash, key, value, null);
                            if (binCount >= TREEIFY_THRESHOLD - 1)
                                treeifyBin(tab, hash);
                            break;
                        }
                        if (e.hash == hash && 
                            ((k = e.key) == key || (key != null && key.equals(k))))
                            break;
                        f = e;
                    }
                }
                
                // Update existing key
                if (e != null) {
                    V oldVal = e.val;
                    if (!onlyIfAbsent || oldVal == null)
                        e.val = value;
                    return oldVal;
                }
            }
        }
        
        // Check if resize needed
        if (check == RESIZE) {
            addCount(1L, check);
            return null;
        }
    }
    
    // Step 5: Add to count
    addCount(1L, binCount);
    return null;
}
```

### Key Differences from HashMap

1. **CAS for empty slots**: No lock needed
2. **Synchronized per bucket**: Fine-grained locking
3. **Volatile fields**: Ensure visibility
4. **Help during resize**: Cooperative resizing

## get() Implementation

### Entry Point

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.val;
}
```

### getNode() Method

```java
final Node<K,V> getNode(Object key) {
    char[] key;
    if (key == null) throw new NullPointerException();
    
    int hash = spread(key.hashCode());
    Node<K,V>[] tab = table;
    Node<K,V> first, e; int n; K k;
    
    // Check if table exists
    if (tab != null && (n = tab.length) > 0 &&
        (first = tabAt(tab, (n - 1) & hash)) != null) {
        
        // Check first node
        if (first.hash == hash && 
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        
        // Check if bucket is treeified or has next
        if ((e = first.next) != null) {
            // Treeified bucket
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            
            // Linked list
            do {
                if (e.hash == hash && 
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

**Note**: No locking needed for reads (volatile ensures visibility).

## computeIfAbsent() Implementation

### Entry Point

```java
public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
    if (key == null || mappingFunction == null)
        throw new NullPointerException();
    
    int h = spread(key.hashCode());
    Node<K,V> tab = table;
    Node<K,V> first, f; int n, i; K k; V v;
    
    // Step 1: Find or create bucket
    while (tab == null || (n = tab.length) == 0 ||
           (first = tabAt(tab, i = (n - 1) & h)) == null)
        return null; // Not initialized
    
    // Step 2: Check if key exists
    if (first.hash == h && 
        ((k = first.key) == key || (key != null && key.equals(k))))
        return first.val; // Key exists
    
    // Step 3: Handle treeified bucket
    else if (first instanceof TreeNode) {
        TreeNode<K,V> t = (TreeNode<K,V>) first;
        V val = t.getTreeNode(h, key);
        if (val != null)
            return val;
    }
    
    // Step 4: Compute and insert
    else {
        Node<K,V> e = first;
        int binCount = 0;
        
        while (e != null) {
            if (e.hash == h && 
                ((k = e.key) == key || (key != null && key.equals(k)))) {
                // Key found, return value
                return e.val;
            }
            ++binCount;
            e = e.next;
        }
        
        // Key not found, compute value
        V val = mappingFunction.apply(key);
        if (val != null) {
            synchronized (first) {
                // Recheck under lock
                if (tabAt(tab, i) == first) {
                    Node<K,V> n = new Node<K,V>(h, key, val, first.next);
                    if (first instanceof TreeNode)
                        ((TreeNode<K,V>)first).putTreeVal(this, tab, h, key, val);
                    else
                        first.next = n;
                }
            }
        }
        return val;
    }
    
    return null;
}
```

**Key insight**: Double-checked locking pattern.

## size() Implementation

### Base Count + CounterCell[]

```java
@Contended static final class CounterCell {
    volatile long value;
    CounterCell(long x) { value = x; }
}

transient volatile long baseCount;
transient volatile CounterCell[] counterCells;
```

### size() Method

```java
public int size() {
    long n = sumCount();
    return ((n < 0) ? 0 :
            (n > Integer.MAX_VALUE) ? Integer.MAX_VALUE :
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

**Design**: Distributed counting to avoid contention on single counter.

### addCount() Method

```java
private final void addCount(long x, int check) {
    long b, c;
    
    // Try to update baseCount first
    if ((c = baseCount) != null ||
        U.compareAndSwapLong(this, BASECOUNT, c, c + x))
        return;
    
    // If CAS fails, use CounterCell
    CounterCell[] as; long b; CounterCell a; int n;
    if ((as = counterCells) != null || (n = as.length) > 0) {
        CounterCell a = as[(n - 1) & h]; // Hash to cell
        if (a == null && cellsBusy == 0) {
            // Create new CounterCell
            if (cellsBusy == 0) {
                CounterCell r = new CounterCell(x);
                if (cellsBusy == 0 &&
                    U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                    boolean created = false;
                    try {
                        CounterCell[] rs; int m, j;
                        if ((rs = counterCells) == null ||
                            (m = rs.length) == 0 ||
                            rs[j = (m - 1) & h] == null) {
                            counterCells = new CounterCell[2];
                            counterCells[h] = r;
                            created = true;
                        }
                    } finally {
                        cellsBusy = 0;
                    }
                }
            }
        }
    }
}
```

**Design**: Hash-based distribution of counts across multiple cells.

## Key Design Decisions

### 1. CAS for Empty Slots

```java
casTabAt(tab, i, null, new Node<>(hash, key, value, null))
```

- **Why?**: Avoid lock overhead for common case
- **When fails?**: Another thread inserted first

### 2. Synchronized per Bucket

```java
synchronized (f) { // Lock only the bucket head
    // ...
}
```

- **Why?**: Fine-grained locking
- **When?**: Only for complex operations (collision, treeify)

### 3. Volatile Fields

```java
volatile V val;
volatile Node<K,V> next;
```

- **Why?**: Ensure visibility across threads
- **Tradeoff**: Performance vs correctness

### 4. ForwardingNode for Resize

```java
if ((f = tabAt(tab, i)).hash == MOVED)
    tab = helpTransfer(tab, f);
```

- **Why?**: Enable concurrent access during resize
- **How?**: Point to new table

### 5. Distributed Counting

```java
volatile long baseCount;
volatile CounterCell[] counterCells;
```

- **Why?**: Avoid contention on single counter
- **How?**: Hash-based distribution

## Performance Characteristics

| Operation | Average | Worst Case |
|-----------|---------|------------|
| put() | O(1) | O(log n) |
| get() | O(1) | O(log n) |
| remove() | O(1) | O(log n) |
| size() | O(n) | O(n) |
| containsKey() | O(1) | O(log n) |

**Note**: size() is approximate (sum of all cells).

## Thread Safety Guarantees

### Atomic Operations

```java
// Atomic operations
V putIfAbsent(K key, V value);
boolean remove(Object key, Object value);
V replace(K key, V oldValue, V newValue);
```

### Weakly Consistent Iterators

```java
// Iterator reflects state at some point since creation
Iterator<Map.Entry<K,V>> it = map.entrySet().while();
while (it.hasNext()) {
    // May or may not see concurrent modifications
}
```

### No ConcurrentModificationException

Unlike `HashMap`, iterators are weakly consistent.

## Common Mistakes

### 1. Using size() for Conditions

```java
// Bad: size() is approximate
if (map.size() == 0) {
    // Another thread might have added an element
}

// Good: Use isEmpty()
if (map.isEmpty()) {
    // Atomic check
}
```

### 2. Compound Operations

```java
// Bad: Non-atomic check-then-act
if (!map.containsKey(key)) {
    map.put(key, value); // Race condition
}

// Good: Atomic operation
map.putIfAbsent(key, value);
```

### 3. Relying on Iteration Order

```java
// Bad: Order is not guaranteed
for (Map.Entry<K,V> entry : map.entrySet()) {
    // Entry order may change during iteration
}
```

### 4. Ignoring null Values

```java
// ConcurrentHashMap doesn't allow null keys/values
map.put(null, "value"); // NullPointerException
map.put("key", null); // NullPointerException
```

## Resources

- **Java ConcurrentHashMap Official Docs**
- **OpenJDK Source**: `src/java.base/java/util/concurrent/ConcurrentHashMap.java`
- **"Java Concurrency in Practice"** by Brian Goetz
- **"Concurrent Programming in Java"** by Doug Lea