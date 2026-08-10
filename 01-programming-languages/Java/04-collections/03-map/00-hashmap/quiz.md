# HashMap Quiz

## Questions

### Q1: What is the time complexity of get() and put() operations in HashMap?
**Answer:** O(1) average case, O(n) worst case (when all keys hash to same bucket), O(log n) with treeification (Java 8+).

### Q2: Can HashMap have null keys?
**Answer:** Yes, HashMap allows one null key and multiple null values. This is different from Hashtable and ConcurrentHashMap which don't allow null keys or values.

### Q3: What happens when two keys have the same hash code?
**Answer:** They are stored in the same bucket using a linked list (Java 7) or tree (Java 8+). When the bucket exceeds 8 entries, it converts to a red-black tree for O(log n) lookup.

### Q4: Is HashMap thread-safe?
**Answer:** No. For concurrent access, use ConcurrentHashMap or Collections.synchronizedMap(). HashMap can throw ConcurrentModificationException if modified during iteration.

### Q5: What is the default initial capacity of HashMap?
**Answer:** 16, with a load factor of 0.75. When the map reaches 75% capacity (12 entries), it doubles in size.

### Q6: How does HashMap handle null keys?
**Answer:** Null keys always hash to bucket 0. HashMap stores null key with a special null hash code.

### Q7: What is the difference between HashMap and Hashtable?
**Answer:** HashMap is not thread-safe, allows null keys/values, and is faster. Hashtable is synchronized, doesn't allow nulls, and is legacy (use ConcurrentHashMap instead).

### Q8: When should you use TreeMap instead of HashMap?
**Answer:** When you need sorted keys, range queries (headMap, tailMap, subMap), or navigation (floor, ceiling, higher, lower).

### Q9: What is the purpose of the load factor in HashMap?
**Answer:** The load factor determines when the map resizes. Default 0.75 means resize when 75% full. Lower values use more memory but have fewer collisions.

### Q10: Can you use HashMap with custom objects as keys?
**Answer:** Yes, but you must override hashCode() and equals() methods. If you don't, the map uses identity hashing (memory address) which won't work for value-based lookups.

## Bonus Questions

### Q11: What is the difference between HashMap and LinkedHashMap?
**Answer:** LinkedHashMap maintains insertion order (or access order if configured) by maintaining a linked list alongside the hash table. HashMap has no ordering guarantee.

### Q12: How do you safely iterate over a HashMap?
**Answer:** Use Iterator and call iterator.remove() for removal, or use forEach() method. Don't modify the map during for-each iteration.

## True/False

**Q13: HashMap maintains insertion order.**
Answer: False — HashMap does not maintain any order. Use LinkedHashMap for insertion order.

**Q14: HashMap allows multiple null keys.**
Answer: False — HashMap allows at most one null key.

**Q15: HashMap is faster than Hashtable because it is not synchronized.**
Answer: True — HashMap has no synchronization overhead, making it faster in single-threaded scenarios.

**Q16: HashMap doubles its capacity when it resizes.**
Answer: True — HashMap capacity doubles (16 → 32 → 64 → ...) when the threshold is exceeded.

**Q17: You can use primitive types as HashMap keys.**
Answer: False — HashMap requires object keys. Primitives are autoboxed to their wrapper classes (int → Integer).

## Code Output

**Q18: What does this code print?**
```java
Map<String, Integer> map = new HashMap<>();
map.put("a", 1);
map.put("b", 2);
map.put("a", 3);
System.out.println(map.get("a"));
```
Answer: 3 — put("a", 3) replaces the previous value for key "a".

**Q19: What does this code print?**
```java
Map<String, Integer> map = new HashMap<>();
map.put(null, 0);
map.put("key", 1);
System.out.println(map.size());
```
Answer: 2 — HashMap allows one null key.

**Q20: What does this code print?**
```java
Map<String, Integer> map = new HashMap<>();
map.put("x", 10);
System.out.println(map.getOrDefault("y", 99));
```
Answer: 99 — key "y" doesn't exist, so default value 99 is returned.
