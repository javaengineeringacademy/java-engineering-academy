# Hashtable Quiz

## Questions

### Q1: What is a Hashtable in Java?
**Answer:** A legacy synchronized Map implementation that stores key-value pairs using a hash table.

### Q2: Why is Hashtable considered a legacy class?
**Answer:** It was introduced in Java 1.0 and has been largely replaced by HashMap and ConcurrentHashMap.

### Q3: What makes Hashtable different from HashMap?
**Answer:** Hashtable is synchronized (thread-safe) and does not allow null keys or null values.

### Q4: Can a Hashtable have null keys?
**Answer:** No, Hashtable throws NullPointerException if you try to insert a null key or null value.

### Q5: What is the time complexity of Hashtable operations?
**Answer:** O(1) amortized for get() and put() operations.

### Q6: How does Hashtable handle hash collisions?
**Answer:** It uses separate chaining, where each bucket contains a linked list of entries that hash to the same index.

### Q7: Can you use Hashtable in a multi-threaded environment?
**Answer:** Yes, it is synchronized, but ConcurrentHashMap is generally preferred for better performance.

### Q8: What is the initial default capacity of Hashtable?
**Answer:** 11 (an odd prime number).

### Q9: What is the load factor of Hashtable?
**Answer:** 0.75 by default.

### Q10: Does Hashtable maintain insertion order?
**Answer:** No, Hashtable does not guarantee any order of entries.

## Bonus Questions

### Q11: What is the difference between Hashtable and Properties?
**Answer:** Properties extends Hashtable and is used for storing string key-value pairs, typically for configuration settings.

### Q12: Why should you prefer ConcurrentHashMap over Hashtable?
**Answer:** ConcurrentHashMap uses lock striping for better concurrency, while Hashtable locks the entire map for each operation.
