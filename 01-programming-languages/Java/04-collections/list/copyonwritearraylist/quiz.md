# CopyOnWriteArrayList Quiz

## Questions

**1. What does CopyOnWriteArrayList do on every modification?**
a) Locks the list
b) Creates a new copy of the underlying array
c) Uses a hash table
d) Blocks readers

**2. What is the primary use case for CopyOnWriteArrayList?**
a) Write-heavy workloads
b) Read-heavy, write-seldom scenarios
c) Small collections
d) Single-threaded access

**3. What happens to iterators when the list is modified?**
a) Throws ConcurrentModificationException
b) Shows updated data
c) Iterates over a snapshot
d) Returns null

**4. Is CopyOnWriteArrayList thread-safe?**
a) No
b) Yes
c) Only for reads
d) Only for writes

**5. What is the time complexity of add() in CopyOnWriteArrayList?**
a) O(1)
b) O(log n)
c) O(n)
d) O(n²)

**6. What method safely adds an element if absent?**
a) add()
b) addIfAbsent()
c) putIfAbsent()
d) offer()

**7. What is the memory trade-off of CopyOnWriteArrayList?**
a) Uses less memory
b) Uses more memory due to copying
c) Same as ArrayList
d) No memory usage

**8. Can you modify the list during iteration without exception?**
a) No
b) Yes, but it won't reflect in current iterator
c) Yes, and iterator sees it
d) Only in Java 11+

**9. Which package provides CopyOnWriteArrayList?**
a) java.util
b) java.util.concurrent
c) java.lang
d) java.io

**10. What is CopyOnWriteArrayList best suited for?**
a) Event listener lists
b) Sorting algorithms
c) Graph traversal
d) Binary search

---

## Answers

1. **b) Creates a new copy of the underlying array** - Each write creates a new array copy.

2. **b) Read-heavy, write-seldom scenarios** - Optimized for concurrent reads with rare writes.

3. **c) Iterates over a snapshot** - Iterator works on the array at time of creation.

4. **b) Yes** - All operations are thread-safe without explicit synchronization.

5. **c) O(n)** - Must copy the entire array on each modification.

6. **b) addIfAbsent()** - Atomically adds only if element is not present.

7. **b) Uses more memory due to copying** - Each modification creates a new array.

8. **b) Yes, but it won't reflect in current iterator** - Iterator sees snapshot at creation.

9. **b) java.util.concurrent** - Part of the concurrent collections package.

10. **a) Event listener lists** - Perfect for lists that are iterated frequently but changed rarely.
