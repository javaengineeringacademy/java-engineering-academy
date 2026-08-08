# Interview Questions

## Collections Framework

### Q1: What is the difference between ArrayList and LinkedList?
**Answer:** `ArrayList` uses a resizable array — O(1) random access, O(n) insert/delete. `LinkedList` uses a doubly-linked list — O(n) random access, O(1) insert/delete at ends. ArrayList is preferred for most use cases due to cache locality.

### Q2: How does HashMap handle collisions?
**Answer:** Each bucket holds a linked list of entries with the same hash. Java 8+ converts lists to trees when bucket size exceeds 8, reducing lookup from O(n) to O(log n). The treeify threshold is 8, untreeify threshold is 6.

### Q3: What is the difference between HashMap and Hashtable?
**Answer:** `Hashtable` is synchronized (thread-safe but slow), doesn't allow null keys/values. `HashMap` is not synchronized, allows one null key. Use `ConcurrentHashMap` for thread safety.

### Q4: What is the fail-fast property of collections?
**Answer:** Iterators throw `ConcurrentModificationException` if the collection is modified structurally during iteration, unless done through the iterator itself. This is implemented via a `modCount` field.

### Q5: When should you use TreeMap over HashMap?
**Answer:** When you need keys sorted by natural order or a custom `Comparator`. `TreeMap` provides `firstKey()`, `lastKey()`, `headMap()`, `tailMap()` operations. HashMap is faster for pure key-value lookup.

### Q6: What is the difference between Iterator and ListIterator?
**Answer:** `Iterator` works for any collection, traverses forward only. `ListIterator` works only for `List`, traverses both directions, and can add/set elements.

### Q7: How do you create an unmodifiable collection?
**Answer:** Use `Collections.unmodifiableList()`, or Java 9+ factory methods: `List.of()`, `List.copyOf()`.

### Q8: What is the difference between PriorityQueue and TreeSet?
**Answer:** `PriorityQueue` is a heap — O(1) peek, O(log n) add/poll, no ordering guarantee on iteration. `TreeSet` is a red-black tree — sorted order, O(log n) all operations, supports NavigableSet operations.

### Q9: What is copy-on-write?
**Answer:** `CopyOnWriteArrayList` creates a new copy of the underlying array on every write. Good for read-heavy, write-rarely scenarios. Iterators work on snapshots and never throw `ConcurrentModificationException`.

### Q10: What is the fail-safe iterator?
**Answer:** Iterators on concurrent collections (e.g., `ConcurrentHashMap`, `CopyOnWriteArrayList`) don't throw `ConcurrentModificationException`. They may not reflect concurrent modifications but provide consistent views.

## Advanced Questions

### Q11: What is the time complexity of HashMap operations?
**Answer:** Average O(1) for get/put/remove. Worst case O(n) with poor hash codes, or O(log n) in Java 8+ when buckets treeify. Amortized O(1) due to resizing.

### Q12: How does ConcurrentHashMap achieve thread safety?
**Answer:** Java 7 used segment locking. Java 8+ uses CAS + synchronized on individual buckets. Reads are lock-free. Only the affected bucket is locked during writes.

### Q13: What is the difference between fail-fast and fail-safe?
**Answer:** Fail-fast iterators throw `ConcurrentModificationException` on concurrent modification. Fail-safe iterators work on copies and never throw the exception. CopyOnWriteArrayList and ConcurrentHashMap use fail-safe iterators.

### Q14: When would you use a LinkedList over an ArrayList?
**Answer:** Rarely. LinkedList is preferable when you frequently add/remove at both ends AND don't need random access. For queue/deque operations, prefer ArrayDeque. For most list operations, ArrayList is faster due to cache locality.

### Q15: What is the significance of load factor in HashMap?
**Answer:** Load factor (default 0.75) determines when to resize. When `size > capacity * loadFactor`, the map doubles capacity and rehashes. Lower load factor = less collisions but more memory. Higher load factor = more collisions but less memory.
