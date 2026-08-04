# TreeMap

## 1. Introduction

TreeMap is a `SortedMap` implementation based on a red-black tree data structure. It stores key-value pairs in sorted order according to the natural ordering of keys or a custom `Comparator`. TreeMap provides O(log n) time for basic operations (get, put, remove) and guarantees sorted iteration order.

TreeMap is the go-to choice when you need:
- Keys in sorted order
- Range queries (subMap, headMap, tailMap)
- Closest key lookups (floorKey, ceilingKey, lowerKey, higherKey)
- Guaranteed O(log n) performance (unlike HashMap's O(1) average but O(n) worst)

Unlike HashMap, TreeMap does not allow null keys (throws NullPointerException) but allows multiple null values. TreeMap is not thread-safe.

## 2. Learning Objectives

- Create and use TreeMap with natural ordering and custom comparators
- Understand the red-black tree data structure
- Learn about sorted map operations: firstKey, lastKey, subMap, headMap, tailMap
- Master navigation methods: floorKey, ceilingKey, lowerKey, higherKey
- Compare TreeMap vs HashMap vs LinkedHashMap
- Understand when TreeMap's O(log n) guarantees are worth the overhead
- Learn about TreeMap null key handling

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 15: HashMap (understand hash table basics)
- Understanding of Comparable and Comparator interfaces
- Basic knowledge of balanced trees (red-black trees)

## 4. Why This Concept Exists

HashMap provides O(1) average performance but:
1. **No ordering**: Keys are in unpredictable order
2. **No range queries**: Can't efficiently find keys in a range
3. **No closest key lookup**: Can't find the closest key to a given value
4. **Worst case O(n)**: With many hash collisions

TreeMap provides:
1. **Sorted order**: Keys always in sorted order
2. **Range queries**: subMap, headMap, tailMap in O(log n + k)
3. **Navigation methods**: floorKey, ceilingKey, lowerKey, higherKey
4. **Guaranteed O(log n)**: Red-black tree ensures balanced tree

TreeMap is essential for:
- Applications requiring sorted key iteration
- Range-based queries (find all keys between A and M)
- Finding closest matches (nearest key lookup)
- Building ordered indexes

## 5. Problem Statement

Consider building a leaderboard system:
- Players have scores
- Need to display players in score order
- Need to find players in a score range
- Need to find the closest score to a given value
- Need to find the top N players

HashMap can't maintain order. TreeMap provides all these operations efficiently:
- `lastEntry()`: Highest score
- `subMap(low, high)`: Players in score range
- `floorKey(score)`: Closest score below
- `headMap(score)`: All players below a score

## 6. Theory

### Red-Black Tree Structure

TreeMap uses a red-black tree, a self-balancing binary search tree with these properties:
1. Every node is either red or black
2. The root is black
3. Every leaf (null) is black
4. If a node is red, both children are black
5. All paths from root to leaves have the same number of black nodes

These properties ensure the tree remains approximately balanced, guaranteeing O(log n) for all operations.

### Node Structure

```java
static final class Entry<K,V> implements Map.Entry<K,V> {
    K key;
    V value;
    Entry<K,V> left;
    Entry<K,V> right;
    Entry<K,V> parent;
    boolean color = BLACK;
}
```

### Comparison Methods

TreeMap uses either:
1. **Natural ordering**: Keys implement `Comparable<K>`
2. **Custom ordering**: `Comparator<K>` provided at construction

```java
// Natural ordering
TreeMap<String, Integer> natural = new TreeMap<>();

// Custom ordering (reverse)
TreeMap<String, Integer> reverse = new TreeMap<>(Comparator.reverseOrder());

// Custom comparator
TreeMap<String, Integer> byLength = new TreeMap<>(Comparator.comparing(String::length));
```

## 7. Internal Working

### The put() Operation

```java
public V put(K key, V value) {
    Entry<K,V> t = root;
    if (t == null) {
        // Tree is empty, create root
        compare(key, key); // Type check
        root = new Entry<>(key, value, null);
        size = 1;
        modCount++;
        return null;
    }

    int cmp;
    Entry<K,V> parent;
    Comparator<? super K> cpr = comparator;

    if (cpr != null) {
        // Use comparator
        while (true) {
            parent = t;
            cmp = cpr.compare(key, t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else {
                V oldValue = t.value;
                t.value = value;
                return oldValue;
            }
        }
    } else {
        // Use natural ordering
        if (key == null) throw new NullPointerException();
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        while (true) {
            parent = t;
            cmp = k.compareTo(t.key);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else {
                V oldValue = t.value;
                t.value = value;
                return oldValue;
            }
        }
    }

    // Insert new entry
    Entry<K,V> e = new Entry<>(key, value, parent);
    if (cmp < 0)
        parent.left = e;
    else
        parent.right = e;
    fixAfterInsertion(e);
    size++;
    modCount++;
    return null;
}
```

### The get() Operation

```java
public V get(Object key) {
    Entry<K,V> p = getEntry(key);
    return (p == null ? null : p.value);
}

final Entry<K,V> getEntry(Object key) {
    if (comparator != null)
        return getEntryUsingComparator(key);
    if (key == null) throw new NullPointerException();
    @SuppressWarnings("unchecked")
    Comparable<? super K> k = (Comparable<? super K>) key;
    Entry<K,V> p = root;
    while (p != null) {
        int cmp = k.compareTo(p.key);
        if (cmp < 0)
            p = p.left;
        else if (cmp > 0)
            p = p.right;
        else
            return p;
    }
    return null;
}
```

### Red-Black Tree Rotations

When the tree becomes unbalanced after insertion or deletion, rotations are performed:

```java
// Left rotation
private void rotateLeft(Entry<K,V> p) {
    Entry<K,V> r = p.right;
    p.right = r.left;
    if (r.left != null)
        r.left.parent = p;
    r.parent = p.parent;
    if (p.parent == null)
        root = r;
    else if (p.parent.left == p)
        p.parent.left = r;
    else
        p.parent.right = r;
    r.left = p;
    p.parent = r;
}

// Right rotation (symmetric)
private void rotateRight(Entry<K,V> p) {
    // Similar to rotateLeft but mirrored
}
```

## 8. JVM Perspective

### Memory Allocation

```java
TreeMap<String, Integer> map = new TreeMap<>();
// JVM allocates:
// - TreeMap object header: 12 bytes
// - comparator reference: 8 bytes
// - size field: 4 bytes
// - modCount: 4 bytes
// - root reference: 8 bytes
// Total TreeMap object: ~40 bytes

// Each Entry:
// - Entry object header: 12 bytes
// - key reference: 8 bytes
// - value reference: 8 bytes
// - left reference: 8 bytes
// - right reference: 8 bytes
// - parent reference: 8 bytes
// - color boolean: 1 byte
// Total per Entry: ~52 bytes (rounded to 56 with alignment)
```

### Red-Black Tree Height

For n entries, the red-black tree height is at most 2 * log2(n + 1). This ensures O(log n) for all operations.

### JIT Optimization

The JIT compiler optimizes TreeMap operations:
- Inline comparison methods
- Optimize tree traversal paths
- Devirtualize Comparable/Comparator calls

## 9. Memory Representation

```
TreeMap<String, Integer> map = new TreeMap<>();
map.put("Charlie", 35);
map.put("Alice", 30);
map.put("Bob", 25);
map.put("Diana", 40);

Memory layout:
┌───────────────────────────────┐
│ TreeMap object                │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ comparator = null (8 bytes)   │
│ size = 4 (4 bytes)            │
│ modCount = 4 (4 bytes)        │
│ root ─────────────────────────┐
└───────────────────────────────┘
                                │
                                ▼
                         Entry "Charlie" (root, BLACK)
                         ┌────────────────────┐
                         │ key = "Charlie"    │
                         │ value = 35         │
                         │ color = BLACK      │
                         │ left ──────────────────┐
                         │ right ─────────────────────┐
                         │ parent = null      │
                         └────────────────────┘     │     │
                                                    │     ▼
                                              Entry "Alice" (RED)
                                              ┌────────────────────┐
                                              │ key = "Alice"      │
                                              │ value = 30         │
                                              │ color = RED        │
                                              │ left = null        │
                                              │ right = null       │
                                              │ parent ────────────────→ Charlie
                                              └────────────────────┘
                                                        │
                                                        ▼
                                              Entry "Diana" (RED)
                                              ┌────────────────────┐
                                              │ key = "Diana"      │
                                              │ value = 40         │
                                              │ color = RED        │
                                              │ left = null        │
                                              │ right = null       │
                                              │ parent ────────────────→ Charlie
                                              └────────────────────┘

Tree structure (sorted by key):
        Charlie (BLACK)
       /            \
  Alice (RED)    Diana (RED)
     /
  Bob (RED)
```

## 10. Syntax

```java
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.NavigableMap;
import java.util.Comparator;

// ============================================
// CREATION
// ============================================
TreeMap<K, V> map = new TreeMap<>();                    // Natural ordering
TreeMap<K, V> map = new TreeMap<>(Comparator.reverseOrder()); // Reverse
TreeMap<K, V> map = new TreeMap<>(comparator);          // Custom comparator
TreeMap<K, V> map = new TreeMap<>(otherMap);             // Copy

// ============================================
// BASIC MAP OPERATIONS
// ============================================
map.put(key, value);                    // O(log n)
map.get(key);                           // O(log n)
map.remove(key);                        // O(log n)
map.containsKey(key);                   // O(log n)
map.containsValue(value);               // O(n)
map.size();                             // O(1)

// ============================================
// SORTED MAP OPERATIONS
// ============================================
K firstKey = map.firstKey();            // O(log n)
K lastKey = map.lastKey();              // O(log n)
Map.Entry<K,V> first = map.firstEntry(); // O(log n)
Map.Entry<K,V> last = map.lastEntry();   // O(log n)

SortedMap<K,V> head = map.headMap(key);        // Keys < key
SortedMap<K,V> tail = map.tailMap(key);        // Keys >= key
SortedMap<K,V> sub = map.subMap(from, to);     // Keys in [from, to)

// ============================================
// NAVIGABLE MAP OPERATIONS
// ============================================
K floor = map.floorKey(key);           // Greatest key <= key
K ceiling = map.ceilingKey(key);       // Smallest key >= key
K lower = map.lowerKey(key);           // Greatest key < key
K higher = map.higherKey(key);         // Smallest key > key

Map.Entry<K,V> floorEntry = map.floorEntry(key);
Map.Entry<K,V> ceilingEntry = map.ceilingEntry(key);
Map.Entry<K,V> lowerEntry = map.lowerEntry(key);
Map.Entry<K,V> higherEntry = map.higherEntry(key);

// Descending map
NavigableMap<K,V> descending = map.descendingMap();
K firstDescending = descending.firstKey();

// ============================================
// ITERATION
// ============================================
// Natural order (ascending)
for (Map.Entry<K, V> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}

// Reverse order (descending)
for (Map.Entry<K, V> entry : map.descendingMap().entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}

// SubMap iteration
for (Map.Entry<K, V> entry : map.subMap("A", "M").entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
```

## 11. Easy Example

```java
import java.util.TreeMap;
import java.util.Map;

public class TreeMapBasics {
    public static void main(String[] args) {
        // Create and populate
        TreeMap<String, Integer> ages = new TreeMap<>();
        ages.put("Charlie", 35);
        ages.put("Alice", 30);
        ages.put("Bob", 25);
        ages.put("Diana", 40);

        System.out.println("Map (sorted): " + ages);
        System.out.println("First key: " + ages.firstKey());
        System.out.println("Last key: " + ages.lastKey());

        // Access
        System.out.println("Alice's age: " + ages.get("Alice"));

        // Range queries
        System.out.println("Head (before C): " + ages.headMap("C"));
        System.out.println("Tail (from B): " + ages.tailMap("B"));
        System.out.println("Sub (B to D): " + ages.subMap("B", "D"));

        // Navigation
        System.out.println("Floor of 'Bb': " + ages.floorKey("Bb"));
        System.out.println("Ceiling of 'Bb': " + ages.ceilingKey("Bb"));
        System.out.println("Lower of 'B': " + ages.lowerKey("B"));
        System.out.println("Higher of 'B': " + ages.higherKey("B"));

        // Iterate in order
        System.out.print("Sorted keys: ");
        for (String key : ages.keySet()) {
            System.out.print(key + " ");
        }
        System.out.println();
    }
}
```

## 12. Medium Example

```java
import java.util.TreeMap;
import java.util.Comparator;
import java.util.Map;

public class TreeMapOperations {
    public static void main(String[] args) {
        // Custom comparator (by value)
        TreeMap<String, Integer> scores = new TreeMap<>();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        scores.put("Diana", 88);

        // Sort by value
        TreeMap<String, Integer> sortedByValue = new TreeMap<>(
            Comparator.comparingInt(scores::get)
        );
        sortedByValue.putAll(scores);
        System.out.println("Sorted by value: " + sortedByValue);

        // Leaderboard pattern
        System.out.println("\nLeaderboard:");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : scores.descendingMap().entrySet()) {
            System.out.printf("  #%d %s: %d%n", rank++, entry.getKey(), entry.getValue());
        }

        // Range query: Find students with scores between 88 and 95
        System.out.println("\nStudents with scores 88-95:");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() >= 88 && entry.getValue() <= 95) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }

        // Find closest score to 90
        System.out.println("\nClosest score to 90:");
        int target = 90;
        Map.Entry<String, Integer> floor = scores.floorEntry("Charlie");
        Map.Entry<String, Integer> ceiling = scores.ceilingEntry("Charlie");
        // Manual search for closest value
        Integer closest = null;
        int minDiff = Integer.MAX_VALUE;
        for (Integer score : scores.values()) {
            int diff = Math.abs(score - target);
            if (diff < minDiff) {
                minDiff = diff;
                closest = score;
            }
        }
        System.out.println("  Closest: " + closest);
    }
}
```

## 13. Hard Example

```java
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedTreeMap {
    public static void main(String[] args) {
        // Pattern 1: Interval scheduling
        System.out.println("=== Interval Scheduling ===");
        TreeMap<Integer, Integer> intervals = new TreeMap<>();
        intervals.put(1, 3);
        intervals.put(2, 5);
        intervals.put(4, 7);
        intervals.put(6, 8);
        intervals.put(9, 10);

        // Find overlapping intervals
        System.out.println("Overlapping with point 5:");
        Map.Entry<Integer, Integer> floor = intervals.floorEntry(5);
        if (floor != null && floor.getValue() >= 5) {
            System.out.println("  " + floor.getKey() + "-" + floor.getValue());
        }

        // Pattern 2: Price ranges
        System.out.println("\n=== Price Ranges ===");
        TreeMap<Double, String> priceRanges = new TreeMap<>();
        priceRanges.put(0.0, "Budget");
        priceRanges.put(50.0, "Mid-range");
        priceRanges.put(100.0, "Premium");
        priceRanges.put(500.0, "Luxury");

        double price = 75.0;
        String category = priceRanges.floorEntry(price).getValue();
        System.out.println("  Price " + price + " is " + category);

        // Pattern 3: Time-based events
        System.out.println("\n=== Time-based Events ===");
        TreeMap<LocalDateTime, String> events = new TreeMap<>();
        events.put(LocalDateTime.of(2024, 1, 1, 9, 0), "Meeting");
        events.put(LocalDateTime.of(2024, 1, 1, 10, 30), "Lunch");
        events.put(LocalDateTime.of(2024, 1, 1, 14, 0), "Workshop");

        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 11, 0);
        Map.Entry<LocalDateTime, String> nextEvent = events.ceilingEntry(now);
        System.out.println("  Next event after " + now + ": " + nextEvent.getValue());

        // Pattern 4: sliding window maximum
        System.out.println("\n=== Running Maximum ===");
        int[] data = {3, 1, 4, 1, 5, 9, 2, 6};
        TreeMap<Integer, Integer> window = new TreeMap<>();
        int windowSize = 3;

        for (int i = 0; i < data.length; i++) {
            window.merge(data[i], 1, Integer::sum);
            if (i >= windowSize) {
                int removed = data[i - windowSize];
                if (window.get(removed) == 1) {
                    window.remove(removed);
                } else {
                    window.merge(removed, -1, Integer::sum);
                }
            }
            if (i >= windowSize - 1) {
                System.out.println("  Window " + (i - windowSize + 2) + "-" + (i + 1) +
                    " max: " + window.lastKey());
            }
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class OrderBook {
    private final TreeMap<Double, Queue<Order>> buyOrders;  // Bids
    private final TreeMap<Double, Queue<Order>> sellOrders; // Asks

    public OrderBook() {
        this.buyOrders = new TreeMap<>(Comparator.reverseOrder()); // Highest bid first
        this.sellOrders = new TreeMap<>(); // Lowest ask first
    }

    public void addOrder(Order order) {
        if (order.side() == Order.Side.BUY) {
            buyOrders.computeIfAbsent(order.price(), k -> new LinkedList<>()).add(order);
        } else {
            sellOrders.computeIfAbsent(order.price(), k -> new LinkedList<>()).add(order);
        }
    }

    public Optional<Order> matchOrders() {
        if (buyOrders.isEmpty() || sellOrders.isEmpty()) {
            return Optional.empty();
        }

        double bestBid = buyOrders.firstKey();
        double bestAsk = sellOrders.firstKey();

        if (bestBid >= bestAsk) {
            Queue<Order> buyQueue = buyOrders.get(bestBid);
            Queue<Order> sellQueue = sellOrders.get(bestAsk);

            Order buyOrder = buyQueue.poll();
            Order sellOrder = sellQueue.poll();

            if (buyQueue.isEmpty()) buyOrders.remove(bestBid);
            if (sellQueue.isEmpty()) sellOrders.remove(bestAsk);

            return Optional.of(new Order(
                buyOrder.id(), buyOrder.side(), bestAsk, buyOrder.quantity()
            ));
        }
        return Optional.empty();
    }

    public Map<Double, Integer> getBidDepth(int levels) {
        return buyOrders.entrySet().stream()
            .limit(levels)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream().mapToInt(Order::quantity).sum()
            ));
    }

    public Map<Double, Integer> getAskDepth(int levels) {
        return sellOrders.entrySet().stream()
            .limit(levels)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream().mapToInt(Order::quantity).sum()
            ));
    }

    public static void main(String[] args) {
        OrderBook book = new OrderBook();

        book.addOrder(new Order("B1", Order.Side.BUY, 100.0, 10));
        book.addOrder(new Order("B2", Order.Side.BUY, 99.5, 5));
        book.addOrder(new Order("S1", Order.Side.SELL, 100.5, 8));
        book.addOrder(new Order("S2", Order.Side.SELL, 101.0, 12));

        System.out.println("=== Bid Depth ===");
        book.getBidDepth(3).forEach((price, qty) ->
            System.out.printf("  $%.2f: %d units%n", price, qty)
        );

        System.out.println("\n=== Ask Depth ===");
        book.getAskDepth(3).forEach((price, qty) ->
            System.out.printf("  $%.2f: %d units%n", price, qty)
        );

        System.out.println("\n=== Attempting Match ===");
        book.matchOrders().ifPresent(trade ->
            System.out.println("  Trade: " + trade)
        );
    }

    record Order(String id, Side side, double price, int quantity) {
        enum Side { BUY, SELL }
    }

    record Trade(String id, double price, int quantity) {}
}
```

## 15. Performance

### Time Complexity

| Operation | Average | Worst Case | Notes |
|-----------|---------|------------|-------|
| put() | O(log n) | O(log n) | Red-black tree |
| get() | O(log n) | O(log n) | Tree traversal |
| remove() | O(log n) | O(log n) | Tree traversal |
| containsKey() | O(log n) | O(log n) | Same as get() |
| firstKey() | O(log n) | O(log n) | Leftmost node |
| lastKey() | O(log n) | O(log n) | Rightmost node |
| subMap() | O(log n + k) | O(log n + k) | k = range size |
| floorKey() | O(log n) | O(log n) | Tree traversal |
| iteration | O(n) | O(n) | In-order traversal |

### TreeMap vs HashMap vs LinkedHashMap

| Feature | TreeMap | HashMap | LinkedHashMap |
|---------|---------|---------|---------------|
| Structure | Red-black tree | Hash table | Hash table + linked list |
| Ordering | Sorted | None | Insertion/access |
| get() | O(log n) | O(1) | O(1) |
| put() | O(log n) | O(1) | O(1) |
| Range queries | O(log n + k) | O(n) | O(n) |
| Memory | More | Less | More |
| Null keys | None | One | One |

### When to Use TreeMap

1. **Sorted iteration**: When you need keys in sorted order
2. **Range queries**: When you need subMap, headMap, tailMap
3. **Closest key lookup**: floorKey, ceilingKey, lowerKey, higherKey
4. **Guaranteed O(log n)**: When you can't tolerate HashMap's O(n) worst case

## 16. Best Practices

1. **Use natural ordering when possible**: Simpler code, better performance
2. **Set initial capacity**: TreeMap doesn't resize, but can pre-size for performance
3. **Override compareTo() consistently**: For custom Comparable classes
4. **Use NavigableMap methods**: For efficient key lookups
5. **Prefer TreeMap for sorted data**: When order matters
6. **Use descendingMap()**: For reverse iteration
7. **Thread safety**: Use ConcurrentSkipListMap for concurrent access

## 17. Common Mistakes

```java
// Mistake 1: Using null keys
TreeMap<String, Integer> map = new TreeMap<>();
map.put(null, 1); // NullPointerException!

// Mistake 2: Inconsistent compareTo()
class BadKey implements Comparable<BadKey> {
    int value;
    public int compareTo(BadKey other) {
        return value - other.value; // Overflow possible!
    }
}

// Good - use Integer.compare()
class GoodKey implements Comparable<GoodKey> {
    int value;
    public int compareTo(GoodKey other) {
        return Integer.compare(value, other.value);
    }
}

// Mistake 3: Assuming equals() consistency
// compareTo() == 0 does NOT imply equals() == true
// Override both compareTo() and equals() consistently

// Mistake 4: Using TreeMap when order doesn't matter
// TreeMap is slower than HashMap for basic operations
// Only use when you need sorted order
```

## 18. Pitfalls

### No Null Keys
TreeMap does NOT allow null keys (throws NullPointerException). This is different from HashMap which allows one null key.

### compareTo() Contract
compareTo() must be consistent with equals(). If `a.compareTo(b) == 0`, then `a.equals(b)` should return true. Violating this causes unexpected behavior in TreeMap.

### Performance Overhead
TreeMap has O(log n) for all operations, while HashMap has O(1) average. Use TreeMap only when you need sorted operations.

### Thread Safety
TreeMap is NOT thread-safe. Use ConcurrentSkipListMap for concurrent access.

### Memory Overhead
TreeMap has more memory overhead than HashMap due to tree node pointers (left, right, parent, color).

## 19. Debugging Tips

1. **Override toString()**: For custom key/value classes
2. **Check compareTo()**: Verify consistency with equals()
3. **Use debugger**: Inspect tree structure
4. **Monitor tree height**: Verify balance
5. **Profile memory**: Use JProfiler to check TreeMap memory usage
6. **Test with multiple threads**: Verify thread safety

## 20. Comparison Table

| Feature | TreeMap | HashMap | LinkedHashMap | ConcurrentSkipListMap |
|---------|---------|---------|---------------|----------------------|
| Structure | Red-black tree | Hash table | Hash table + linked list | Skip list |
| Ordering | Sorted | None | Insertion/access | Sorted |
| Thread-safe | No | No | No | Yes |
| Null keys | None | One | One | None |
| Performance | O(log n) | O(1) | O(1) | O(log n) |

## 21. Decision Tree

```
Need a Map?
├── Yes → Need sorted keys?
│   ├── Yes → Need thread safety?
│   │   ├── Yes → ConcurrentSkipListMap
│   │   └── No → TreeMap
│   └── No → Need insertion order?
│       ├── Yes → LinkedHashMap
│       └── No → HashMap
├── Need range queries?
│   └── Yes → TreeMap
└── Need closest key lookup?
    └── Yes → TreeMap
```

## 22. Interview Questions

### Q1: What is the difference between TreeMap and HashMap?
**A**: TreeMap uses a red-black tree (O(log n) operations, sorted order). HashMap uses a hash table (O(1) average operations, no order). TreeMap is better for sorted data and range queries.

### Q2: Why doesn't TreeMap allow null keys?
**A**: TreeMap uses compareTo() or comparator to compare keys. If null keys were allowed, calling compareTo() on null would throw NullPointerException. HashMap uses hashCode() and equals(), which handle null differently.

### Q3: What is the time complexity of TreeMap operations?
**A**: O(log n) for put, get, remove, containsKey. O(1) for size. O(log n + k) for subMap where k is the range size.

### Q4: When would you use TreeMap over HashMap?
**A**: When you need keys in sorted order, need range queries (subMap, headMap, tailMap), need closest key lookups (floorKey, ceilingKey), or need guaranteed O(log n) performance.

### Q5: What is NavigableMap?
**A**: An extension of SortedMap that provides navigation methods like floorKey(), ceilingKey(), lowerKey(), higherKey(), and descendingMap().

### Q6: How does TreeMap handle duplicate compareTo() values?
**A**: If `a.compareTo(b) == 0`, TreeMap treats them as the same key. The new value replaces the old value. This is different from HashMap which uses equals() and hashCode().

### Q7: What is the difference between floorKey() and lowerKey()?
**A**: floorKey(key) returns the greatest key <= key. lowerKey(key) returns the greatest key < key. If key exists, floorKey returns it, lowerKey returns the one before it.

## 23. Exercises

### Exercise 1: Leaderboard
Build a leaderboard using TreeMap that:
- Adds players with scores
- Displays players in score order
- Finds top N players
- Finds players in a score range

### Exercise 2: Price Tracker
Create a price tracking system:
- Store prices with timestamps
- Find highest/lowest prices
- Find prices in a date range
- Calculate average price

### Exercise 3: Event Scheduler
Build an event scheduler:
- Store events with times
- Find next/previous event
- Get all events in a time range
- Handle overlapping events

## 24. Assignments

### Assignment 1: Dictionary
Build a dictionary application using TreeMap:
- Store words with definitions
- Find words starting with a prefix
- Suggest autocomplete
- Track word frequency

### Assignment 2: Calendar System
Create a calendar system:
- Store events with dates
- Find events in a date range
- Find conflicts
- Export to iCal format

## 25. Mini Project

### Stock Market Order Book

Build a stock market order book using TreeMap:

```java
// Features:
// 1. Maintain buy/sell orders in price order
// 2. Match orders when prices cross
// 3. Show order depth
// 4. Handle order updates/cancellations
// 5. Calculate weighted average price
// 6. Export trade history
```

**Requirements:**
- Use TreeMap for order storage (sorted by price)
- Implement order matching logic
- Handle concurrent orders
- Track trade history

## 26. Summary

TreeMap is the sorted Map implementation based on red-black trees:

- **Internal structure**: Red-black tree (self-balancing BST)
- **Performance**: O(log n) for all operations
- **Ordering**: Sorted by natural order or custom Comparator
- **Null keys**: Not allowed
- **Range queries**: subMap, headMap, tailMap
- **Navigation**: floorKey, ceilingKey, lowerKey, higherKey
- **Best for**: Sorted iteration, range queries, closest key lookup

## 27. References

### Official Documentation
- [TreeMap JavaDoc](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/TreeMap.html)
- [SortedMap Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/SortedMap.html)
- [NavigableMap Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/NavigableMap.html)

### Books
- *Effective Java* by Joshua Bloch
- *Introduction to Algorithms* (CLRS) - Red-Black Trees

### Online Resources
- [Baeldung TreeMap Guide](https://www.baeldung.com/java-treemap)
- [GeeksforGeeks TreeMap](https://www.geeksforgeeks.org/treemap-in-java/)

### Related Topics
- [HashMap](../15-hashmap/README.md)
- [LinkedHashMap](../16-linkedhashmap/README.md)
- [TreeSet](../13-treeset/README.md)
