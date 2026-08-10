# TreeMap Quiz

## Questions

### Q1: What is a TreeMap in Java?
**Answer:** A TreeMap is a Map implementation that stores keys in sorted order based on their natural ordering or a custom Comparator.

### Q2: What is the time complexity of basic operations in TreeMap?
**Answer:** O(log n) for get(), put(), and remove() operations due to the underlying Red-Black tree.

### Q3: What interface must TreeMap keys implement?
**Answer:** The Comparable interface, or a Comparator must be provided.

### Q4: Can a TreeMap have null keys?
**Answer:** No, TreeMap does not allow null keys (it will throw NullPointerException).

### Q5: How does TreeMap differ from HashMap?
**Answer:** TreeMap maintains keys in sorted order with O(log n) operations, while HashMap has no ordering guarantee but O(1) operations.

### Q6: What is the underlying data structure of TreeMap?
**Answer:** A Red-Black tree (a self-balancing binary search tree).

### Q7: How can you create a TreeMap with a custom ordering?
**Answer:** By passing a Comparator to the constructor: new TreeMap<>(Comparator).

### Q8: What methods does TreeMap provide for range queries?
**Answer:** subMap(), headMap(), tailMap(), firstKey(), lastKey(), lowerKey(), higherKey().

### Q9: What is TreeMap.firstEntry() used for?
**Answer:** It returns the key-value pair with the least (lowest) key.

### Q10: Can TreeMap be synchronized?
**Answer:** Not directly, but you can use Collections.synchronizedSortedMap() or ConcurrentSkipListMap for thread safety.

## Bonus Questions

### Q11: What is the difference between lowerKey() and floorKey()?
**Answer:** lowerKey() returns the greatest key strictly less than the given key, while floorKey() returns the greatest key less than or equal to the given key.

### Q12: What is the TreeMap.descendingMap() method used for?
**Answer:** It returns a reverse-order view of the Map, with keys in descending order.
