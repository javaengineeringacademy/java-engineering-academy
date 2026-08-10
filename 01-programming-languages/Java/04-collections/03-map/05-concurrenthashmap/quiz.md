# ConcurrentHashMap Quiz

## Questions

### Q1: What is the time complexity of get() and put() operations in ConcurrentHashMap?
**Answer:** O(1) average case. ConcurrentHashMap uses CAS operations for fine-grained locking, providing excellent concurrent performance.

### Q2: Can ConcurrentHashMap have null keys or values?
**Answer:** No. ConcurrentHashMap doesn't allow null keys or null values. Use Optional instead of null values.

### Q3: What is the difference between ConcurrentHashMap and Collections.synchronizedMap()?
**Answer:** ConcurrentHashMap uses fine-grained locking (CAS + synchronized per bucket), while synchronizedMap uses a single lock for all operations. ConcurrentHashMap is much faster under high concurrency.

### Q4: Is ConcurrentHashMap completely lock-free?
**Answer:** No. ConcurrentHashMap uses CAS for simple operations (putIfAbsent, computeIfAbsent) but uses synchronized blocks for complex operations (compute, merge). It's "lock-free" for reads.

### Q5: What happens during iteration of ConcurrentHashMap?
**Answer:** The iterator is weakly consistent — it reflects the state of the map at or since the creation of the iterator. It doesn't throw ConcurrentModificationException.

### Q6: What is the default concurrency level of ConcurrentHashMap?
**Answer:** In Java 8+, ConcurrentHashMap doesn't use a fixed concurrency level. It uses CAS and per-bucket synchronization for better scalability.

### Q7: Can you use null as a value in ConcurrentHashMap?
**Answer:** No. ConcurrentHashMap throws NullPointerException if you try to put a null value. Use a sentinel value or Optional.

### Q8: What is the difference between put() and putIfAbsent() in ConcurrentHashMap?
**Answer:** put() overwrites existing values. putIfAbsent() only puts if the key doesn't exist. putIfAbsent() is atomic and thread-safe.

### Q9: When should you use ConcurrentHashMap over HashMap?
**Answer:** When multiple threads need to read/write concurrently. ConcurrentHashMap is designed for high-concurrency scenarios.

### Q10: How does ConcurrentHashMap handle hash collisions?
**Answer:** In Java 8+, ConcurrentHashMap converts buckets to red-black trees when they exceed 8 entries, providing O(log n) worst-case lookup.

## Bonus Questions

### Q11: What is computeIfAbsent() and when should you use it?
**Answer:** computeIfAbsent() atomically computes a value if the key is absent. Use it for lazy initialization: `map.computeIfAbsent(key, k -> expensiveComputation(k))`

### Q12: Can you use ConcurrentHashMap as a cache?
**Answer:** Yes, but it doesn't have eviction policies. For production caching, use Caffeine or Guava Cache which provide TTL, LRU, and other cache features.
