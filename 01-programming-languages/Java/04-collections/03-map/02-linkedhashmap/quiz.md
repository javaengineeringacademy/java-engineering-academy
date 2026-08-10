# LinkedHashMap Quiz

## Questions

### Q1: What is a LinkedHashMap in Java?
**Answer:** A HashMap that maintains a doubly-linked list running through all entries to preserve insertion order.

### Q2: What are the two ordering modes of LinkedHashMap?
**Answer:** Insertion order (default) and access order.

### Q3: How do you create a LinkedHashMap that uses access order?
**Answer:** new LinkedHashMap<>(initialCapacity, loadFactor, true).

### Q4: What is the access order feature used for?
**Answer:** It reorders entries based on access, making it useful for implementing LRU (Least Recently Used) caches.

### Q5: What method does LinkedHashMap override to support access ordering?
**Answer:** The afterNodeAccess() method, which moves the accessed node to the end of the linked list.

### Q6: Can a LinkedHashMap have null keys?
**Answer:** Yes, it allows one null key.

### Q7: What is removeEldestEntry() used for?
**Answer:** It can be overridden to automatically remove the oldest entry when the map exceeds a specified size, enabling cache behavior.

### Q8: What is the time complexity of LinkedHashMap operations?
**Answer:** O(1) for get() and put() operations.

### Q9: Is LinkedHashMap thread-safe?
**Answer:** No, it is not synchronized. Use Collections.synchronizedMap() or ConcurrentHashMap for thread safety.

### Q10: When should you prefer LinkedHashMap over HashMap?
**Answer:** When you need to maintain insertion order or access order, or when implementing an LRU cache.

## Bonus Questions

### Q11: What is the difference between iteration order of LinkedHashMap and TreeMap?
**Answer:** LinkedHashMap maintains insertion/access order (O(1) per step), while TreeMap sorts by key natural ordering (O(log n) per step).

### Q12: How can you implement an LRU cache using LinkedHashMap?
**Answer:** Override removeEldestEntry() to return true when the size exceeds a threshold, combined with accessOrder=true.
