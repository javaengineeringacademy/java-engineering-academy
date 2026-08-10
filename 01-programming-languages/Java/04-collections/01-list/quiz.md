# List Interface Quiz

## Questions

**1. Which interface extends Collection and maintains insertion order with index-based access?**
a) Set
b) Queue
c) List
d) Map

**2. What does the get(int index) method return?**
a) The first element
b) The element at the specified index
c) The last element
d) A random element

**3. Which List implementation uses a dynamically resizing array internally?**
a) LinkedList
b) Vector
c) ArrayList
d) Stack

**4. What happens when you call set(index, element) on an empty list?**
a) Adds the element
b) Throws IndexOutOfBoundsException
c) Creates a new list
d) Returns null

**5. Which method returns a view of a portion of a list?**
a) slice()
b) subList()
c) portion()
d) segment()

**6. What is the time complexity of get() in ArrayList?**
a) O(n)
b) O(log n)
c) O(1)
d) O(n²)

**7. Which list implementation allows null elements?**
a) Vector
b) CopyOnWriteArrayList
c) ArrayList
d) All of the above

**8. What does the indexOf() method return if the element is not found?**
a) -1
b) null
c) Throws NoSuchElementException
d) 0

**9. Which method adds an element at a specific index?**
a) set(index, element)
b) add(index, element)
c) insert(index, element)
d) put(index, element)

**10. How do you remove an element by index from a List?**
a) remove(index)
b) delete(index)
c) removeElement(index)
d) erase(index)

## True/False

**11. List maintains insertion order.**
Answer: True — List guarantees elements are in insertion order.

**12. ArrayList allows duplicate elements.**
Answer: True — List allows duplicates (unlike Set).

**13. LinkedList is faster than ArrayList for random access.**
Answer: False — ArrayList has O(1) random access; LinkedList has O(n).

**14. List can contain multiple null elements.**
Answer: True — List allows multiple null elements.

**15. subList() returns a copy of the list portion.**
Answer: False — subList() returns a view, not a copy.

## Code Output

**16. What does this code print?**
```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
list.set(1, "X");
System.out.println(list);
```
Answer: [A, X, C] — set(1, "X") replaces element at index 1.

**17. What does this code print?**
```java
List<Integer> list = new LinkedList<>();
list.add(1);
list.add(2);
list.add(3);
System.out.println(list.get(1));
```
Answer: 2 — get(1) returns element at index 1.

**18. What does this code print?**
```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
list.remove("B");
System.out.println(list.size());
```
Answer: 2 — remove("B") removes the element, size becomes 2.

---

## Answers

1. **c) List** - List extends Collection and provides index-based, ordered access.

2. **b) The element at the specified index** - get(int index) retrieves the element at the given position.

3. **c) ArrayList** - ArrayList uses a dynamic array internally.

4. **b) Throws IndexOutOfBoundsException** - You cannot set an element at an invalid index.

5. **b) subList()** - subList(fromIndex, toIndex) returns a view of the portion.

6. **c) O(1)** - ArrayList provides constant-time random access.

7. **d) All of the above** - All standard List implementations allow null elements.

8. **a) -1** - indexOf() returns -1 if the element is not present.

9. **b) add(index, element)** - add(int index, E element) inserts at the specified position.

10. **a) remove(index)** - remove(int index) removes the element at the specified index.
