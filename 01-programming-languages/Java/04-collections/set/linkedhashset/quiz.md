# LinkedHashSet Quiz

## Questions

**1. What does LinkedHashSet maintain that HashSet does not?**
a) Sorted order
b) Insertion order
c) Priority order
d) Random order

**2. What data structure does LinkedHashSet use internally?**
a) Array
b) Hash table + doubly-linked list
c) Binary tree
d) Stack

**3. Is LinkedHashSet thread-safe?**
a) Yes
b) No
c) Only for reads
d) Only in Java 11+

**4. What is the time complexity of add() in LinkedHashSet?**
a) O(n)
b) O(log n)
c) O(1)
d) O(n²)

**5. When should you use LinkedHashSet over HashSet?**
a) When you need sorted order
b) When you need insertion order
c) When you need fastest performance
d) When you need null elements

**6. What is the main difference between LinkedHashSet and TreeSet?**
a) LinkedHashSet is sorted
b) TreeSet maintains insertion order
c) LinkedHashSet is faster for basic operations
d) TreeSet allows null

**7. Can LinkedHashSet contain null elements?**
a) No
b) Yes
c) Only one
d) Only in Java 8+

**8. What happens to iteration order if you remove and re-add an element?**
a) Element moves to end
b) Element stays in original position
c) Throws exception
d) Random order

**9. What is the space overhead of LinkedHashSet vs HashSet?**
a) Less memory
b) Same memory
c) More memory due to linked list
d) Depends on elements

**10. What is LinkedHashSet NOT suitable for?**
a) Caching in access order
b) Preserving insertion order
c) High-frequency concurrent writes
d) Enum-like constants

---

## Answers

1. **b) Insertion order** - LinkedHashSet uses a doubly-linked list for ordering.

2. **b) Hash table + doubly-linked list** - Combines HashMap with linked list.

3. **b) No** - Not synchronized; use Collections.synchronizedSet().

4. **c) O(1)** - Same performance as HashSet for basic operations.

5. **b) When you need insertion order** - Use when iteration order matters.

6. **c) LinkedHashSet is faster for basic operations** - O(1) vs O(log n) for TreeSet.

7. **b) Yes** - Allows one null element.

8. **a) Element moves to end** - Removing and re-adding moves it to the end.

9. **c) More memory due to linked list** - Extra pointers for maintaining order.

10. **c) High-frequency concurrent writes** - Not thread-safe; consider ConcurrentHashMap.
