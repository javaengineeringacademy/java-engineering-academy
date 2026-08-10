# Collection Interface Quiz

## Questions

**1. What is the root interface in the Java Collections hierarchy?**
a) List
b) Set
c) Collection
d) Map

**2. Which method is used to add an element to a Collection?**
a) insert()
b) put()
c) add()
d) append()

**3. What does the size() method return?**
a) The capacity of the collection
b) The number of elements in the collection
c) The memory usage
d) The hash code

**4. Which method removes all elements from a collection?**
a) delete()
b) clear()
c) remove()
d) empty()

**5. What happens if you call remove() on an unmodifiable collection?**
a) Returns false
b) Throws UnsupportedOperationException
c) Throws NullPointerException
d) Silently ignores

**6. Which method converts a Collection to an array?**
a) toArray()
b) asArray()
c) toList()
d) array()

**7. What does the contains() method check?**
a) If the collection has a specific type
b) If the collection contains a specific element
c) If the collection is full
d) If the collection is empty

**8. Which collection interface maintains insertion order?**
a) Set
b) Queue
c) Collection
d) None by default

**9. What is the return type of iterator()?**
a) Iterator
b) Iterable
c) ListIterator
d) Enumeration

**10. Which method checks if a collection contains all elements of another collection?**
a) contains()
b) containsAll()
c) hasAll()
d) includesAll()

## True/False

**11. Collection is a class, not an interface.**
Answer: False — Collection is an interface. Implementations include ArrayList, HashSet, etc.

**12. The add() method returns true if the element was added successfully.**
Answer: True — add(E e) returns true if the collection changed as a result of the call.

**13. All Collection implementations allow null elements.**
Answer: False — Some implementations like EnumSet and ConcurrentHashMap do not allow nulls.

**14. The iterator() method returns an Iterable object.**
Answer: False — iterator() returns an Iterator object. The class itself implements Iterable.

**15. size() returns the capacity of the collection, not the number of elements.**
Answer: False — size() returns the number of elements. Capacity is an internal detail.

## Code Output

**16. What does this code print?**
```java
Collection<String> col = new ArrayList<>();
col.add("A");
col.add("B");
col.add("A");
System.out.println(col.size());
```
Answer: 3 — Collection allows duplicate elements.

**17. What does this code print?**
```java
Collection<Integer> col = List.of(1, 2, 3);
System.out.println(col.isEmpty());
```
Answer: false — List.of creates a collection with 3 elements.

**18. What does this code print?**
```java
Collection<String> col = new ArrayList<>(Arrays.asList("X", "Y", "Z"));
Object[] arr = col.toArray();
System.out.println(arr.length);
```
Answer: 3 — toArray() returns an array with all elements.

---

## Answers

1. **c) Collection** - Collection is the root interface of the entire Collections framework.

2. **c) add()** - The add(E e) method appends the element to the end of the collection.

3. **b) The number of elements in the collection** - size() returns the number of elements currently in the collection.

4. **b) clear()** - clear() removes all elements from the collection.

5. **b) Throws UnsupportedOperationException** - Unmodifiable collections throw this exception on mutation attempts.

6. **a) toArray()** - toArray() returns an array containing all elements.

7. **b) If the collection contains a specific element** - contains(Object o) returns true if the collection contains the specified element.

8. **d) None by default** - The Collection interface does not guarantee order; specific implementations like List maintain order.

9. **a) Iterator** - iterator() returns an Iterator over the elements.

10. **b) containsAll()** - containsAll(Collection<?> c) returns true if the collection contains all specified elements.
