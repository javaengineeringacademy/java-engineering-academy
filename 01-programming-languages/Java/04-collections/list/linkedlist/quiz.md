# LinkedList Quiz

## Questions

**1. What data structure does LinkedList use internally?**
a) Array
b) Doubly-linked nodes
c) Hash table
d) Binary tree

**2. What is the time complexity of adding at the beginning of LinkedList?**
a) O(n)
b) O(log n)
c) O(1)
d) O(n²)

**3. Which interface does LinkedList implement besides List?**
a) Queue
b) Set
c) Map
d) Stack

**4. What is the main advantage of LinkedList over ArrayList?**
a) Random access
b) Better memory usage
c) Efficient insertions/deletions at ends
d) Thread safety

**5. What does addFirst() do?**
a) Adds at the end
b) Adds at the beginning
c) Adds at middle
d) Replaces first element

**6. Which method removes and returns the first element?**
a) getFirst()
b) peek()
c) pollFirst()
d) pop()

**7. Can LinkedList be used as a Stack?**
a) No
b) Yes, via push/pop
c) Only in Java 11+
d) Only with Integer type

**8. What is the space complexity of LinkedList per element?**
a) O(1)
b) O(log n)
c) O(n)
d) O(n²)

**9. Which method returns the element at a specific index?**
a) get(index)
b) peek(index)
c) element(index)
d) index(index)

**10. What happens when you call get(1000000) on a LinkedList?**
a) Returns null
b) Very slow due to traversal
c) Throws IndexOutOfBoundsException
d) Returns default value

---

## Answers

1. **b) Doubly-linked nodes** - Each node contains references to next and previous nodes.

2. **c) O(1)** - Adding at either end is constant time.

3. **a) Queue** - LinkedList implements both List and Deque interfaces.

4. **c) Efficient insertions/deletions at ends** - No shifting needed at beginning/end.

5. **b) Adds at the beginning** - addFirst() inserts at the head.

6. **c) pollFirst()** - Returns null if empty; removeFirst() throws exception.

7. **b) Yes, via push/pop** - push() and pop() methods make it usable as a stack.

8. **a) O(1)** - Each node has fixed overhead regardless of list size.

9. **a) get(index)** - Returns element at specified position.

10. **b) Very slow due to traversal** - Must traverse from head to reach that index.
