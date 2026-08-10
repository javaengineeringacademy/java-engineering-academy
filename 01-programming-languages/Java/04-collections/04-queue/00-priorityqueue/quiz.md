# PriorityQueue Quiz

## Questions

### Q1: What is the time complexity of add() and poll() operations in PriorityQueue?
**Answer:** O(log n) for both. The internal binary heap ensures efficient insertion and removal of the highest priority element.

### Q2: Can PriorityQueue contain null elements?
**Answer:** No. PriorityQueue throws NullPointerException if you try to add null. This is because null cannot be compared.

### Q3: Is PriorityQueue thread-safe?
**Answer:** No. For concurrent priority queue operations, use PriorityBlockingQueue.

### Q4: How does PriorityQueue determine element order?
**Answer:** By natural ordering (Comparable) or custom Comparator. Elements are arranged so the least element is at the head.

### Q5: What is the difference between peek() and poll()?
**Answer:** peek() returns the head element without removing it. poll() returns and removes the head element. poll() returns null if empty, remove() throws exception.

### Q6: What is the default initial capacity of PriorityQueue?
**Answer:** 11. It grows by doubling when needed.

### Q7: Can you iterate over PriorityQueue in priority order?
**Answer:** No. iterator() doesn't guarantee order. Use poll() to process elements in priority order.

### Q8: What is the difference between PriorityQueue and TreeMap?
**Answer:** PriorityQueue is a queue (FIFO by priority), TreeMap is a sorted map (key-value pairs). PriorityQueue doesn't allow duplicates, TreeMap allows different values for same key.

### Q9: When should you use PriorityBlockingQueue over PriorityQueue?
**Answer:** When multiple threads need to add/remove elements concurrently. PriorityBlockingQueue is thread-safe, PriorityQueue is not.

### Q10: How do you create a PriorityQueue with reverse order?
**Answer:** Use Collections.reverseOrder() as comparator: `new PriorityQueue<>(Collections.reverseOrder())` or `new PriorityQueue<>(Comparator.reverseOrder())`

## Bonus Questions

### Q11: What happens if you add an element that violates the heap property?
**Answer:** The heap property is maintained automatically. Elements are sifted up/down during add/poll operations.

### Q12: Can you use PriorityQueue as a general-purpose sorting mechanism?
**Answer:** Technically yes (add all elements, then poll them out), but it's inefficient. Use Collections.sort() or Arrays.sort() instead.

## True/False

**Q13: PriorityQueue is a FIFO queue.**
Answer: False — PriorityQueue is a priority queue, not FIFO. Elements are served by priority, not insertion order.

**Q14: PriorityQueue allows duplicate elements.**
Answer: True — PriorityQueue allows duplicate elements (unlike TreeSet).

**Q15: PriorityQueue uses a binary heap internally.**
Answer: True — PriorityQueue uses a binary heap (array-based) for efficient priority operations.

**Q16: PriorityQueue.get(0) returns the highest priority element.**
Answer: False — PriorityQueue doesn't support indexed access. Use peek() to get the head element.

**Q17: PriorityQueue automatically sorts elements when you add them.**
Answer: True — Elements are inserted into the heap structure, maintaining the heap property.

## Code Output

**Q18: What does this code print?**
```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.add(3);
pq.add(1);
pq.add(2);
System.out.println(pq.poll());
```
Answer: 1 — poll() returns the smallest element (natural ordering).

**Q19: What does this code print?**
```java
PriorityQueue<String> pq = new PriorityQueue<>();
pq.add("banana");
pq.add("apple");
pq.add("cherry");
System.out.println(pq.peek());
```
Answer: apple — peek() returns the smallest element alphabetically.

**Q20: What does this code print?**
```java
PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
pq.add(3);
pq.add(1);
pq.add(2);
System.out.println(pq.poll());
```
Answer: 3 — reverseOrder() makes it a max-heap, so poll() returns the largest.
