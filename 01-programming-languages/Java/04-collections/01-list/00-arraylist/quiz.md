# ArrayList Quiz

## Questions

**1. What is the default initial capacity of an ArrayList?**
a) 5
b) 10
c) 16
d) 20

**2. By how much does ArrayList grow when it reaches capacity?**
a) Doubles
b) Triples
c) Adds 5
d) Adds 10

**3. What is the time complexity of get() in ArrayList?**
a) O(n)
b) O(log n)
c) O(1)
d) O(n²)

**4. Which method trims the ArrayList capacity to its current size?**
a) shrink()
b) trimToSize()
c) compact()
d) resize()

**5. Can ArrayList contain null elements?**
a) No, never
b) Yes, only one
c) Yes, multiple
d) Only in Java 8+

**6. What does clone() return for an ArrayList?**
a) A reference to the same list
b) A shallow copy
c) A deep copy
d) null

**7. Which constructor creates an ArrayList with a specified initial capacity?**
a) new ArrayList()
b) new ArrayList(Collection c)
c) new ArrayList(int initialCapacity)
d) new ArrayList(int size)

**8. What happens when you add elements beyond the capacity?**
a) Throws exception
b) Silently drops elements
c) Automatically resizes
d) Returns false

**9. Which method returns the index of the first occurrence of an element?**
a) indexOf()
b) find()
c) search()
d) locate()

**10. What is ArrayList NOT thread-safe?**
a) It uses arrays
b) It has no synchronization
c) It is final
d) Both a and b

## True/False

**11. ArrayList uses a linked list internally.**
Answer: False — ArrayList uses an Object array (Object[]) internally.

**12. ArrayList doubles its capacity each time it grows.**
Answer: False — ArrayList grows by 50% (oldCapacity + oldCapacity >> 1).

**13. ArrayList.get(0) has O(1) time complexity.**
Answer: True — Array index access is constant time.

**14. ArrayList is faster than LinkedList for random access.**
Answer: True — ArrayList has O(1) random access vs LinkedList's O(n).

**15. ArrayList.trimToSize() increases the capacity.**
Answer: False — trimToSize() reduces capacity to match current size.

## Code Output

**16. What does this code print?**
```java
List<String> list = new ArrayList<>();
list.add("A");
list.add("B");
list.add("C");
list.remove(1);
System.out.println(list);
```
Answer: [A, C] — remove(1) removes element at index 1 ("B").

**17. What does this code print?**
```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
list.add(0, 10);
System.out.println(list);
```
Answer: [10, 1, 2, 3] — add(0, 10) inserts at index 0.

**18. What does this code print?**
```java
List<String> list = new ArrayList<>(Arrays.asList("X", "Y"));
System.out.println(list.get(2));
```
Answer: IndexOutOfBoundsException — Index 2 out of bounds for size 2.

---

## Answers

1. **b) 10** - The default initial capacity is 10.

2. **a) Doubles** - ArrayList grows by approximately 50% (newCapacity = oldCapacity + oldCapacity >> 1).

3. **c) O(1)** - ArrayList provides constant-time random access via index.

4. **b) trimToSize()** - This method reduces capacity to match size.

5. **c) Yes, multiple** - ArrayList allows any number of null elements.

6. **b) A shallow copy** - clone() creates a shallow copy of the list.

7. **c) new ArrayList(int initialCapacity)** - This constructor sets the initial capacity.

8. **c) Automatically resizes** - ArrayList grows dynamically when capacity is exceeded.

9. **a) indexOf()** - indexOf(Object o) returns the index or -1.

10. **b) It has no synchronization** - ArrayList is not synchronized for performance reasons.
