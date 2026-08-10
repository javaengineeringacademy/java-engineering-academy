# Map Interface Quiz

## Questions

### Q1: What is a Map in Java?
**Answer:** A Map is a collection that stores key-value pairs, where each key is unique and maps to exactly one value.

### Q2: What is the main difference between Map and Collection?
**Answer:** Map stores key-value pairs while Collection stores only elements. Map does not extend the Collection interface.

### Q3: What happens if you put() a duplicate key in a Map?
**Answer:** The old value associated with the key is replaced by the new value, and the method returns the old value.

### Q4: What does the get() method return if the key is not found?
**Answer:** It returns null.

### Q5: Which Map implementation maintains insertion order?
**Answer:** LinkedHashMap.

### Q6: What is the time complexity of put() and get() in a well-implemented Map like HashMap?
**Answer:** O(1) amortized (constant time).

### Q7: How do you iterate over all key-value pairs in a Map?
**Answer:** Using the entrySet() method, which returns a Set<Map.Entry<K,V>>.

### Q8: What is the containsKey() method used for?
**Answer:** To check if a specific key exists in the Map.

### Q9: What does Map.computeIfAbsent() do?
**Answer:** If the key is not present, it computes a value using the provided function, associates it, and returns the value.

### Q10: Can a Map have null keys?
**Answer:** It depends on the implementation. HashMap allows one null key, TreeMap does not allow null keys, and Hashtable does not allow null keys.

## Bonus Questions

### Q11: What interface does a Map's keySet() method return?
**Answer:** Set<K> - a Set view of all keys in the Map.

### Q12: What is the difference between Map.merge() and Map.put()?
**Answer:** merge() allows defining custom logic for combining values when a key already exists, while put() simply replaces the old value.
