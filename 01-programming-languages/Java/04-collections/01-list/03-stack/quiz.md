# Stack Quiz

## Questions

**1. What principle does a Stack follow?**
a) FIFO
b) LIFO
c) Random access
d) Priority-based

**2. Which method adds an element to the top of the stack?**
a) add()
b) push()
c) insert()
d) enqueue()

**3. What does peek() do?**
a) Removes and returns top
b) Returns top without removing
c) Returns bottom element
d) Returns size

**4. Is the Java Stack class thread-safe?**
a) No
b) Yes, it is synchronized
c) Only in Java 11+
d) Only for primitives

**5. What is the preferred replacement for Stack?**
a) ArrayList
b) LinkedList
c) ArrayDeque
d) Vector

**6. What exception does pop() throw when stack is empty?**
a) NullPointerException
b) EmptyStackException
c) NoSuchElementException
d) IllegalStateException

**7. What does search() return if element not found?**
a) -1
b) 0
c) null
d) -1

**8. Which real-world problem uses stacks?**
a) BFS
b) Undo operations
c) Sorting
d) Searching

**9. What is the time complexity of push() and pop()?**
a) O(n)
b) O(log n)
c) O(1)
d) O(n²)

**10. What does isEmpty() check?**
a) If capacity is 0
b) If no elements exist
c) If top is null
d) If size equals capacity

---

## Answers

1. **b) LIFO** - Last In, First Out is the fundamental principle.

2. **b) push()** - push() adds to the top of the stack.

3. **b) Returns top without removing** - peek() inspects without modifying.

4. **b) Yes, it is synchronized** - Stack extends Vector which is synchronized.

5. **c) ArrayDeque** - More efficient and recommended for stack behavior.

6. **b) EmptyStackException** - Thrown when stack is empty and pop/peek is called.

7. **d) -1** - search() returns position from top, or -1 if not found.

8. **b) Undo operations** - Stacks naturally support undo/redo functionality.

9. **c) O(1)** - Both operations are constant time.

10. **b) If no elements exist** - isEmpty() returns true when size is 0.
