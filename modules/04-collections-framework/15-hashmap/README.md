# HashMap

## 1. Introduction

HashMap is the most widely used implementation of the `Map` interface in Java. It provides O(1) average-time performance for basic operations (get, put, remove) using a hash table data structure. HashMap stores key-value pairs and allows null keys and values.

HashMap works by computing a hash code from the key, using it to determine which "bucket" (index in an internal array) to store the value in. When retrieving, it computes the hash of the key again to find the correct bucket, then searches within that bucket for the exact key using `equals()`.

Understanding HashMap internals is essential for Java developers because:
1. It's the default choice for most Map use cases
2. Improper use of hashCode()/equals() causes subtle bugs
3. Performance depends on hash distribution and load factor
4. Java 8+ introduced treeification (red-black trees for collisions)

## 2. Learning Objectives

- Create and use HashMap with generics
- Understand hashing, buckets, and collision resolution
- Learn about hashCode() and equals() contracts
- Understand the load factor and resizing mechanism
- Master Java 8+ treeification (linked list → red-black tree)
- Compare HashMap vs TreeMap vs LinkedHashMap
- Learn about HashMap null key handling
- Understand thread-safety issues with HashMap

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 03: Generics basics
- Understanding of hashCode() and equals() methods
- Basic knowledge of data structures (arrays, linked lists)

## 4. Why This Concept Exists

Before HashMap, developers used Hashtable (synchronized, slow) or manual array-based lookups. HashMap provides:
1. **O(1) average performance**: Fast lookups regardless of data size
2. **No synchronization overhead**: Faster than Hashtable in single-threaded code
3. **Null support**: Allows one null key and multiple null values
4. **Dynamic resizing**: Automatically grows as data is added
5. **Flexible key types**: Any object can be a key (with proper hashCode/equals)

HashMap is essential for:
- Caching and memoization
- Database indexing
- Configuration storage
- Counting and frequency analysis
- Object relationship mapping

## 5. Problem Statement

Consider building a phone book application:
- Add contacts with name (key) and phone number (value)
- Look up phone number by name (fast)
- Check if a name exists (fast)
- Remove contacts
- The phone book may have 10 contacts or 100,000 contacts

Without HashMap, you'd need:
- A list of pairs and linear search O(n)
- Or a sorted array and binary search O(log n)
- Both require manual resizing and management

With HashMap, all operations are O(1) average case, and the data structure handles resizing automatically.

## 6. Theory

### Hash Table Structure

HashMap uses an array of `Node` buckets:

```java
transient Node<K,V>[] table;  // Array of buckets
transient int size;           // Number of key-value mappings
int threshold;                // size at which to resize
final float loadFactor;       // resize threshold ratio
```

### Node Structure

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;    // Precomputed hash
    final K key;       // Key (immutable)
    V value;           // Value
    Node<K,V> next;    // Linked list for collisions
}
```

### Hash Computation

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

The hash is spread across all bits to reduce collisions. The `h >>> 16` operation mixes the upper bits into the lower bits.

### Bucket Index Calculation

```java
int index = hash & (table.length - 1);  // Equivalent to hash % length when length is power of 2
```

### Collision Resolution

When two keys hash to the same bucket:
1. **Java 7**: Linked list (chain hashing)
2. **Java 8+**: Linked list → Red-black tree when bucket has 8+ entries

### Load Factor and Resizing

- Default load factor: 0.75
- Default initial capacity: 16
- When `size > capacity * loadFactor`, the table is resized
- New capacity = old capacity * 2
- All entries are rehashed to new positions

### Capacity as Power of 2

HashMap capacity is always a power of 2 (16, 32, 64, 128, ...). This allows efficient index calculation using bitwise AND instead of modulo.

## 7. Internal Working

### The put() Operation

```java
public V put(K key, Value value) {
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;  // Initialize on first put
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);  // Empty bucket
    else {
        Node<K,V> e; K k;
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;  // Key exists, replace value
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);  // Tree bucket
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1)  // 7
                        treeifyBin(tab, hash);  // Convert to tree
                    break;
                }
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    break;  // Key found
                p = e;
            }
        }
        if (e != null) {  // Key already exists
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;  // Replace value
            afterNodeAccess(e);
            return oldValue;
        }
    }
    if (++size > threshold)
        resize();  // Resize if needed
    afterNodeInsertion(evict);
    return null;
}
```

### The get() Operation

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
            return first;  // First node is the key
        if ((e = first.next) != null) {
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);  // Tree lookup
            do {
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;  // Found in linked list
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```

### The resize() Operation

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1;  // Double threshold
    }
    else if (oldThr > 0)
        newCap = oldThr;
    else {
        newCap = DEFAULT_INITIAL_CAPACITY;  // 16
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);  // 12
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && (float)ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;  // Single node
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);  // Split tree
                else {
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

## 8. JVM Perspective

### Memory Allocation

```java
Map<String, Integer> map = new HashMap<>();
// JVM allocates:
// - HashMap object header: 12 bytes
// - table reference: 8 bytes
// - size field: 4 bytes
// - threshold field: 4 bytes
// - loadFactor field: 4 bytes
// Total HashMap object: ~40 bytes

// First put() triggers resize():
// - Creates Node[] of size 16
// - Node[] array: 16 × 8 = 128 bytes (references)
// - Each Node: ~40 bytes (hash, key, value, next)
```

### JIT Optimization

The JIT compiler optimizes HashMap operations:
- Inline `hash()` and `getNode()` methods
- Optimize the bit manipulation for index calculation
- Eliminate null checks for non-null keys
- Devirtualize calls when concrete type is known

### Hash Distribution Quality

Good hash codes distribute keys uniformly across the table. Poor hash codes (e.g., always returning 0) cause all keys to collide in bucket 0, degrading performance to O(n).

## 9. Memory Representation

```
HashMap<String, Integer> map = new HashMap<>();
map.put("Alice", 30);
map.put("Bob", 25);
map.put("Charlie", 35);

Memory layout:
┌───────────────────────────────┐
│ HashMap object                │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ table ──────────────────────────┐
│ size = 3 (4 bytes)            │     │
│ threshold = 12 (4 bytes)      │     │
│ loadFactor = 0.75f (4 bytes)  │     │
│ (padding 4 bytes)             │     │
└───────────────────────────────┘     │
                                      ▼
                               Node[] table (capacity 16)
                               ┌────────────────────────┐
                               │ [0] → null             │
                               │ [1] → null             │
                               │ [2] → null             │
                               │ ...                    │
                               │ [5] → Node("Alice",30) │ ← hash("Alice") & 15 = 5
                               │ [6] → Node("Bob",25)   │ ← hash("Bob") & 15 = 6
                               │ [7] → null             │
                               │ [8] → null             │
                               │ [9] → null             │
                               │ [10] → null            │
                               │ [11] → null            │
                               │ [12] → null            │
                               │ [13] → null            │
                               │ [14] → null            │
                               │ [15] → null            │
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

### Collision Handling

When two keys hash to the same bucket:

```
Bucket[5] → Node("Alice", 30) → Node("David", 40) → null
                   ↑                    ↑
              hash=5, next          hash=5, next=null
```

## 10. Syntax

```java
import java.util.HashMap;
import java.util.Map;

// ============================================
// CREATION
// ============================================
Map<K, V> map = new HashMap<>();
Map<K, V> map = new HashMap<>(16);           // Initial capacity
Map<K, V> map = new HashMap<>(16, 0.75f);   // Capacity and load factor
Map<K, V> map = new HashMap<>(otherMap);    // Copy constructor
Map<K, V> immutable = Map.of("k1", "v1");   // Java 9+

// ============================================
// ADDING/UPDATING
// ============================================
map.put(key, value);                    // Add or replace
map.putIfAbsent(key, value);            // Add only if absent
map.putAll(otherMap);                   // Add all from other map
map.replace(key, value);                // Replace if present
map.replace(key, oldValue, newValue);   // Conditional replace

// ============================================
// RETRIEVING
// ============================================
V value = map.get(key);                 // Returns null if absent
V value = map.getOrDefault(key, default); // Returns default if absent
V value = map.computeIfAbsent(key, k -> createValue(k));

// ============================================
// REMOVING
// ============================================
V removed = map.remove(key);            // Remove by key
boolean removed = map.remove(key, value); // Conditional remove
map.clear();                            // Remove all

// ============================================
// CHECKING
// ============================================
boolean hasKey = map.containsKey(key);      // O(1) average
boolean hasValue = map.containsValue(value); // O(n)
boolean empty = map.isEmpty();
int size = map.size();

// ============================================
// VIEW COLLECTIONS
// ============================================
Set<K> keys = map.keySet();              // Set view of keys
Collection<V> values = map.values();     // Collection view of values
Set<Map.Entry<K,V>> entries = map.entrySet(); // Set view of entries

// ============================================
// ITERATION
// ============================================
// Iterate over entries
for (Map.Entry<K, V> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " = " + entry.getValue());
}

// Iterate over keys
for (K key : map.keySet()) {
    System.out.println(key);
}

// Iterate over values
for (V value : map.values()) {
    System.out.println(value);
}

// forEach with lambda
map.forEach((key, value) -> System.out.println(key + " = " + value));

// ============================================
// BULK OPERATIONS
// ============================================
map.putAll(otherMap);               // Add all
map.keySet().removeAll(otherKeys);  // Remove all matching keys
map.values().removeAll(otherValues); // Remove all matching values

// ============================================
// COMPUTE OPERATIONS
// ============================================
map.compute(key, (k, v) -> v == null ? defaultValue : transform(v));
map.computeIfAbsent(key, k -> expensiveComputation(k));
map.computeIfPresent(key, (k, v) -> v + 1);
map.merge(key, value, (old, newVal) -> old + newVal);

// ============================================
// SEARCHING
// ============================================
boolean containsKey = map.containsKey(key);
boolean containsValue = map.containsValue(value);
```

## 11. Easy Example

```java
import java.util.HashMap;
import java.util.Map;

public class HashMapBasics {
    public static void main(String[] args) {
        // Create and populate
        Map<String, Integer> ages = new HashMap<>();
        ages.put("Alice", 30);
        ages.put("Bob", 25);
        ages.put("Charlie", 35);
        ages.put("Diana", 28);

        System.out.println("Map: " + ages);
        System.out.println("Size: " + ages.size());

        // Access values
        System.out.println("Alice's age: " + ages.get("Alice"));
        System.out.println("Unknown: " + ages.getOrDefault("Unknown", 0));

        // Check if key exists
        System.out.println("Contains Bob: " + ages.containsKey("Bob"));
        System.out.println("Contains age 35: " + ages.containsValue(35));

        // Update value
        ages.put("Alice", 31);
        System.out.println("Updated Alice: " + ages.get("Alice"));

        // Remove entry
        ages.remove("Diana");
        System.out.println("After removing Diana: " + ages);

        // Iterate
        System.out.println("\nAll entries:");
        for (Map.Entry<String, Integer> entry : ages.entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        // Convert to string representation
        System.out.println("\nKey-Value pairs:");
        ages.forEach((name, age) ->
            System.out.println("  " + name + ": " + age + " years old")
        );
    }
}
```

## 12. Medium Example

```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HashMapOperations {
    public static void main(String[] args) {
        // Word frequency counter
        System.out.println("=== Word Frequency ===");
        String text = "the quick brown fox jumps over the lazy dog the fox";
        Map<String, Integer> frequency = wordFrequency(text);
        System.out.println("Frequency: " + frequency);

        // Group by first letter
        System.out.println("\n=== Group by First Letter ===");
        List<String> words = List.of("apple", "avocado", "banana", "blueberry", "cherry");
        Map<Character, List<String>> grouped = groupByFirstLetter(words);
        grouped.forEach((letter, wordList) ->
            System.out.println(letter + ": " + wordList)
        );

        // Invert map
        System.out.println("\n=== Invert Map ===");
        Map<String, Integer> original = Map.of("one", 1, "two", 2, "three", 3);
        Map<Integer, String> inverted = invertMap(original);
        System.out.println("Original: " + original);
        System.out.println("Inverted: " + inverted);

        // Merge maps
        System.out.println("\n=== Merge Maps ===");
        Map<String, Integer> map1 = Map.of("a", 1, "b", 2);
        Map<String, Integer> map2 = Map.of("b", 3, "c", 4);
        Map<String, Integer> merged = mergeMaps(map1, map2);
        System.out.println("Merged: " + merged);

        // Two Sum problem
        System.out.println("\n=== Two Sum ===");
        int[] nums = {2, 7, 11, 15, 3, 6};
        int target = 9;
        int[] result = twoSum(nums, target);
        System.out.println("Indices: " + Arrays.toString(result));
    }

    static Map<String, Integer> wordFrequency(String text) {
        Map<String, Integer> freq = new HashMap<>();
        for (String word : text.split("\\s+")) {
            freq.merge(word, 1, Integer::sum);
        }
        return freq;
    }

    static Map<Character, List<String>> groupByFirstLetter(List<String> words) {
        Map<Character, List<String>> grouped = new HashMap<>();
        for (String word : words) {
            grouped.computeIfAbsent(word.charAt(0), k -> new ArrayList<>()).add(word);
        }
        return grouped;
    }

    static <K, V> Map<V, K> invertMap(Map<K, V> map) {
        Map<V, K> inverted = new HashMap<>();
        map.forEach((k, v) -> inverted.put(v, k));
        return inverted;
    }

    static Map<String, Integer> mergeMaps(Map<String, Integer> map1, Map<String, Integer> map2) {
        Map<String, Integer> merged = new HashMap<>(map1);
        map2.forEach((k, v) -> merged.merge(k, v, Integer::sum));
        return merged;
    }

    static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) {
                return new int[]{seen.get(complement), i};
            }
            seen.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }
}
```

## 13. Hard Example

```java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedHashMap {
    public static void main(String[] args) {
        // Pattern 1: Custom hashCode/equals
        System.out.println("=== Custom Key Objects ===");
        Map<Employee, String> departments = new HashMap<>();
        departments.put(new Employee(1, "Alice"), "Engineering");
        departments.put(new Employee(2, "Bob"), "Marketing");
        departments.put(new Employee(1, "Alice"), "Management"); // Replaces
        departments.forEach((emp, dept) ->
            System.out.println("  " + emp.name() + " -> " + dept)
        );

        // Pattern 2: ConcurrentHashMap for thread safety
        System.out.println("\n=== Concurrent HashMap ===");
        ConcurrentHashMap<String, AtomicInteger> wordCount = new ConcurrentHashMap<>();
        String[] words = {"java", "is", "great", "java", "is", "fun"};
        Arrays.stream(words).parallel().forEach(word ->
            wordCount.computeIfAbsent(word, k -> new AtomicInteger(0)).incrementAndGet()
        );
        wordCount.forEach((word, count) ->
            System.out.println("  " + word + ": " + count.get())
        );

        // Pattern 3: WeakHashMap for caching
        System.out.println("\n=== WeakHashMap Cache ===");
        WeakHashMap<Object, String> cache = new WeakHashMap<>();
        Object key1 = new Object();
        Object key2 = new Object();
        cache.put(key1, "value1");
        cache.put(key2, "value2");
        System.out.println("Before GC: " + cache.size());
        key1 = null; // Allow GC
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        System.out.println("After GC: " + cache.size());

        // Pattern 4: IdentityHashMap for reference equality
        System.out.println("\n=== IdentityHashMap ===");
        Map<Object, String> identityMap = new IdentityHashMap<>();
        String s1 = new String("hello");
        String s2 = new String("hello");
        identityMap.put(s1, "first");
        identityMap.put(s2, "second"); // Different key (reference equality)
        System.out.println("Size: " + identityMap.size()); // 2

        // Pattern 5: EnumMap for enum keys
        System.out.println("\n=== EnumMap ===");
        Map<DayOfWeek, String> schedule = new EnumMap<>(DayOfWeek.class);
        schedule.put(DayOfWeek.MONDAY, "Work");
        schedule.put(DayOfWeek.SATURDAY, "Rest");
        schedule.forEach((day, activity) ->
            System.out.println("  " + day + ": " + activity)
        );

        // Pattern 6: Nested maps
        System.out.println("\n=== Nested Maps ===");
        Map<String, Map<String, Integer>> scores = new HashMap<>();
        scores.computeIfAbsent("Math", k -> new HashMap<>()).put("Alice", 95);
        scores.computeIfAbsent("Math", k -> new HashMap<>()).put("Bob", 87);
        scores.computeIfAbsent("Science", k -> new HashMap<>()).put("Alice", 92);
        scores.forEach((subject, studentScores) -> {
            System.out.println("  " + subject + ":");
            studentScores.forEach((student, score) ->
                System.out.println("    " + student + ": " + score)
            );
        });
    }

    record Employee(int id, String name) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return id == employee.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class UserSessionManager {
    private final ConcurrentHashMap<String, UserSession> activeSessions;
    private final Map<String, List<String>> userSessions; // userId -> sessionIds
    private final Map<String, Date> lastAccessTimes;

    public UserSessionManager() {
        this.activeSessions = new ConcurrentHashMap<>();
        this.userSessions = new ConcurrentHashMap<>();
        this.lastAccessTimes = new ConcurrentHashMap<>();
    }

    public String createSession(String userId) {
        String sessionId = UUID.randomUUID().toString();
        UserSession session = new UserSession(userId, sessionId, new Date());
        activeSessions.put(sessionId, session);
        userSessions.computeIfAbsent(userId, k -> new ArrayList<>()).add(sessionId);
        lastAccessTimes.put(sessionId, new Date());
        return sessionId;
    }

    public Optional<UserSession> getSession(String sessionId) {
        UserSession session = activeSessions.get(sessionId);
        if (session != null) {
            lastAccessTimes.put(sessionId, new Date()); // Update access time
        }
        return Optional.ofNullable(session);
    }

    public void invalidateSession(String sessionId) {
        UserSession session = activeSessions.remove(sessionId);
        if (session != null) {
            List<String> sessions = userSessions.get(session.userId());
            if (sessions != null) {
                sessions.remove(sessionId);
            }
            lastAccessTimes.remove(sessionId);
        }
    }

    public void invalidateAllUserSessions(String userId) {
        List<String> sessions = userSessions.remove(userId);
        if (sessions != null) {
            sessions.forEach(activeSessions::remove);
            sessions.forEach(lastAccessTimes::remove);
        }
    }

    public List<UserSession> getRecentSessions(int count) {
        return lastAccessTimes.entrySet().stream()
            .sorted(Map.Entry.<String, Date>comparingByValue().reversed())
            .limit(count)
            .map(entry -> activeSessions.get(entry.getKey()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public Map<String, Long> getSessionCountByUser() {
        return activeSessions.values().stream()
            .collect(Collectors.groupingBy(
                UserSession::userId,
                Collectors.counting()
            ));
    }

    public void cleanupExpiredSessions(long maxAgeMillis) {
        long cutoff = System.currentTimeMillis() - maxAgeMillis;
        lastAccessTimes.entrySet().removeIf(entry -> {
            if (entry.getValue().getTime() < cutoff) {
                activeSessions.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }

    public static void main(String[] args) {
        UserSessionManager manager = new UserSessionManager();

        // Create sessions
        String session1 = manager.createSession("user1");
        String session2 = manager.createSession("user1");
        String session3 = manager.createSession("user2");

        System.out.println("=== Active Sessions ===");
        manager.getSession(session1).ifPresent(s ->
            System.out.println("  Session 1: " + s)
        );

        System.out.println("\n=== Session Count by User ===");
        manager.getSessionCountByUser().forEach((user, count) ->
            System.out.println("  " + user + ": " + count + " sessions")
        );

        System.out.println("\n=== Invalidate Session ===");
        manager.invalidateSession(session2);
        manager.getSessionCountByUser().forEach((user, count) ->
            System.out.println("  " + user + ": " + count + " sessions")
        );
    }

    record UserSession(String userId, String sessionId, Date createdAt) {}
}
```

## 15. Performance

### Time Complexity

| Operation | Average | Worst Case | Notes |
|-----------|---------|------------|-------|
| put() | O(1) | O(n) → O(log n) | Treeification at 8 collisions |
| get() | O(1) | O(n) → O(log n) | Treeification at 8 collisions |
| remove() | O(1) | O(n) → O(log n) | Treeification at 8 collisions |
| containsKey() | O(1) | O(n) → O(log n) | Same as get() |
| containsValue() | O(n) | O(n) | Must scan all buckets |
| size() | O(1) | O(1) | Field access |
| iteration | O(n) | O(n) | All buckets and nodes |

### Load Factor Impact

| Load Factor | Pros | Cons |
|-------------|------|------|
| 0.5 | Fewer collisions | More memory (50% empty) |
| 0.75 | Balanced | Default, good tradeoff |
| 1.0 | Less memory | More collisions |
| >1.0 | Even less memory | Many collisions, slower |

### HashMap vs TreeMap vs LinkedHashMap

| Feature | HashMap | TreeMap | LinkedHashMap |
|---------|---------|---------|---------------|
| Structure | Hash table | Red-black tree | Hash table + linked list |
| Ordering | None | Sorted by key | Insertion/access order |
| get() | O(1) | O(log n) | O(1) |
| put() | O(1) | O(log n) | O(1) |
| Memory | Less | More | More |
| Null keys | One | None | One |
| Thread-safe | No | No | No |

### Capacity Calculation

For expected N entries with load factor 0.75:
- Required capacity = N / 0.75 = N * 1.33
- Round up to next power of 2
- Example: 1000 entries → 1000 * 1.33 = 1333 → 2048 (next power of 2)

## 16. Best Practices

1. **Set initial capacity**: If you know the expected size, set it to avoid resizing
   ```java
   // For 1000 entries:
   Map<K, V> map = new HashMap<>(2048); // 1000 / 0.75 ≈ 1333, next power of 2
   ```

2. **Override both equals() and hashCode()**: For custom key classes
   ```java
   @Override
   public boolean equals(Object o) { ... }
   @Override
   public int hashCode() { return Objects.hash(field1, field2); }
   ```

3. **Use computeIfAbsent/merge**: For atomic operations
   ```java
   map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
   map.merge(key, 1, Integer::sum);
   ```

4. **Prefer immutable keys**: Keys should be immutable for consistent hashing

5. **Use Map.of() for immutable maps**: When data doesn't change

6. **Check containsKey before get**: If you need to know if key exists

7. **Use entrySet() for iteration**: More efficient than keySet() + get()

8. **Thread safety**: Use ConcurrentHashMap for concurrent access

## 17. Common Mistakes

```java
// Mistake 1: Not overriding equals/hashCode for custom keys
class BadKey {
    String value;
    // Missing equals() and hashCode()!
}
Map<BadKey, String> map = new HashMap<>();
BadKey key1 = new BadKey("test");
BadKey key2 = new BadKey("test");
map.put(key1, "value");
map.get(key2); // Returns null! Different objects

// Mistake 2: Using mutable keys
List<String> key = new ArrayList<>(List.of("a"));
Map<List<String>, String> map = new HashMap<>();
map.put(key, "value");
key.add("b"); // Changes hashCode!
map.get(key); // Returns null! Key was lost

// Mistake 3: Not setting initial capacity for large maps
// Bad - causes multiple resizes
Map<String, Integer> map = new HashMap<>();
for (int i = 0; i < 10000; i++) {
    map.put("key" + i, i);
}

// Good - single allocation
Map<String, Integer> map = new HashMap<>(2048);

// Mistake 4: Using HashMap in multi-threaded code
// Bad - data corruption
Map<String, Integer> map = new HashMap<>();
// Multiple threads accessing...

// Good - thread-safe
Map<String, Integer> map = new ConcurrentHashMap<>();

// Mistake 5: Confusing containsKey and containsValue
boolean hasKey = map.containsKey(key);   // O(1) average
boolean hasValue = map.containsValue(value); // O(n) always
```

## 18. Pitfalls

### NullPointerException
- HashMap allows null key (hash = 0) and null values
- TreeMap does NOT allow null keys (throws NPE)
- LinkedHashMap follows HashMap rules

### Hash DoS Attacks
- Malicious keys with same hash code can cause O(n) lookups
- Mitigation: Use ConcurrentHashMap or random hash seeds

### ConcurrentModificationException
- Not thread-safe; concurrent modifications can corrupt internal structure
- Use ConcurrentHashMap or Collections.synchronizedMap()

### Memory Leaks
- HashMap holds strong references to keys and values
- Use WeakHashMap for caches where keys should be GC'd

### Thread Safety
- HashMap is NOT thread-safe
- Concurrent access can cause infinite loops (in Java 7) or data corruption
- Always use ConcurrentHashMap for concurrent access

## 19. Debugging Tips

1. **Override toString()**: For custom key/value classes
2. **Check hashCode distribution**: Use IDE debugger to inspect bucket distribution
3. **Monitor resize operations**: Track threshold and capacity
4. **Use VisualVM**: Monitor HashMap size and memory usage
5. **Enable assertions**: Check hash distribution uniformity
6. **Log put/get operations**: For debugging lookup issues
7. **Check for null keys**: Verify null key handling

## 20. Comparison Table

| Feature | HashMap | Hashtable | ConcurrentHashMap | LinkedHashMap |
|---------|---------|-----------|-------------------|---------------|
| Thread-safe | No | Yes (all) | Yes (fine-grained) | No |
| Null keys | One | None | One | One |
| Null values | Multiple | None | Multiple | Multiple |
| Ordering | None | None | None | Insertion/access |
| Performance | O(1) | O(1)* | O(1) | O(1) |
| Synchronization | None | Full | Segment-level | None |

*Hashtable is synchronized but slower

## 21. Decision Tree

```
Need a Map?
├── Yes → Need thread safety?
│   ├── Yes → ConcurrentHashMap
│   └── No → Need sorted keys?
│       ├── Yes → TreeMap
│       └── No → Need insertion/access order?
│           ├── Yes → LinkedHashMap
│           └── No → HashMap (default)
├── Need immutable map?
│   └── Use Map.of() or Map.copyOf()
└── Need enum keys?
    └── Use EnumMap
```

## 22. Interview Questions

### Q1: How does HashMap handle collisions?
**A**: Java 7: Chaining (linked list). Java 8+: When a bucket has 8+ entries, the linked list is converted to a red-black tree (O(log n) lookup). When the tree shrinks to 6 entries, it converts back to a linked list.

### Q2: What is the load factor?
**A**: The ratio of entries to buckets. Default 0.75 means the table resizes when 75% full. Lower load factor = fewer collisions but more memory. Higher load factor = more collisions but less memory.

### Q3: Why is HashMap capacity always a power of 2?
**A**: Allows efficient index calculation using bitwise AND (hash & (capacity-1)) instead of modulo. Also ensures uniform bucket distribution when combined with the hash spreading function.

### Q4: Can HashMap have null keys?
**A**: Yes, HashMap allows one null key (hash code = 0). The null key is always placed in bucket 0. TreeMap does NOT allow null keys.

### Q5: What is treeification in HashMap?
**A**: Java 8+ optimization: When a bucket has 8+ entries, the linked list is converted to a red-black tree. This changes worst-case lookup from O(n) to O(log n). When entries are removed and bucket size drops to 6, it converts back to a linked list.

### Q6: How do you make a thread-safe Map?
**A**: Use ConcurrentHashMap (preferred), Collections.synchronizedMap() (wraps all operations with synchronized), or Hashtable (legacy, avoid).

### Q7: What happens when you put() a duplicate key?
**A**: The old value is replaced and returned. The key's position in the table doesn't change. Only the value is updated.

## 23. Exercises

### Exercise 1: Word Frequency
Write a program that:
1. Reads a text file
2. Counts frequency of each word
3. Finds the most common word
4. Groups words by length

### Exercise 2: Two Sum
Given an array of integers and a target, find two numbers that add up to the target. Return their indices. (Hint: Use HashMap)

### Exercise 3: Group Anagrams
Given a list of strings, group anagrams together. (Hint: Use sorted string as key)

### Exercise 4: LRU Cache
Implement an LRU (Least Recently Used) cache using HashMap and LinkedList.

## 24. Assignments

### Assignment 1: Phone Book
Build a phone book application using HashMap:
- Add/remove contacts
- Search by name (partial match)
- Search by phone number
- Import/export to CSV
- Handle duplicate names

### Assignment 2: Student Grade Tracker
Create a grade tracking system:
- Store student grades by subject
- Calculate GPA
- Find top students
- Generate reports

### Assignment 3: Simple Cache
Implement a cache with:
- TTL (time-to-live) for entries
- Maximum size limit
- LRU eviction
- Hit/miss statistics

## 25. Mini Project

### URL Shortener

Build a URL shortener using HashMap:

```java
// Features:
// 1. Shorten URL to short code
// 2. Redirect short code to original URL
// 3. Track click counts
// 4. Expiration for short URLs
// 5. Custom short codes
// 6. Analytics (top URLs, recent clicks)
```

**Requirements:**
- Use HashMap for short code → URL mapping
- Use another HashMap for analytics
- Implement proper hashCode/equals if using custom keys
- Handle thread safety for concurrent requests

## 26. Summary

HashMap is the most commonly used Map implementation:

- **Internal structure**: Hash table with buckets (linked lists → trees)
- **Performance**: O(1) average for get/put/remove
- **Capacity**: Always power of 2, load factor 0.75
- **Treeification**: Linked list → red-black tree at 8 collisions
- **Null support**: One null key, multiple null values
- **Thread safety**: NOT thread-safe; use ConcurrentHashMap
- **Key requirement**: Proper hashCode() and equals() implementation

## 27. References

### Official Documentation
- [HashMap JavaDoc](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/HashMap.html)
- [Map Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/Map.html)

### Books
- *Effective Java* by Joshua Bloch (Item 11-12)
- *Java Concurrency in Practice* by Brian Goetz

### Online Resources
- [Baeldung HashMap Guide](https://www.baeldung.com/java-hashmap)
- [GeeksforGeeks HashMap](https://www.geeksforgeeks.org/java-util-hashmap-in-java/)
- [OpenJDK HashMap Source](https://hg.openjdk.java.net/jdk8/jdk8/jdk/file/tip/src/share/classes/java/util/HashMap.java)

### Related Topics
- [LinkedHashMap](../16-linkedhashmap/README.md)
- [TreeMap](../17-treemap/README.md)
- [ConcurrentHashMap](../18-concurrenthashmap/README.md)
