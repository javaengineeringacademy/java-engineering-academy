# TreeSet Quiz

## Questions

**1. What data structure does TreeSet use internally?**
a) Hash table
b) Red-Black tree (NavigableMap)
c) Array
d) Linked list

**2. What is the time complexity of add() in TreeSet?**
a) O(1)
b) O(log n)
c) O(n)
d) O(n²)

**3. Does TreeSet allow null elements?**
a) No
b) Yes, one null
c) Yes, unlimited
d) Only with Comparator

**4. What interface does TreeSet implement?**
a) Set
b) NavigableSet
c) SortedSet
d) All of the above

**5. What does the lower() method return?**
a) Element equal to key
b) Greatest element strictly less than key
c) Least element greater than key
d) Element at index

**6. What is the difference between floor() and lower()?**
a) floor() includes equality
b) lower() includes equality
c) They are the same
d) floor() is faster

**7. How do you create a descending TreeSet?**
a) new TreeSet().descending()
b) new TreeSet(Comparator.reverseOrder())
c) new TreeSet().reversed()
d) TreeSet.descending()

**8. What does headSet(toElement) return?**
a) Elements after toElement
b) Elements before toElement
c) All elements
d) Elements equal to toElement

**9. Can TreeSet have custom ordering?**
a) No
b) Yes, via Comparator
c) Yes, via Comparable only
d) Only for String

**10. What happens if you add an element that is not Comparable?**
a) Works fine
b) Throws ClassCastException
c) Throws IllegalArgumentException
d) Silently ignores

---

## Answers

1. **b) Red-Black tree (NavigableMap)** - Uses TreeMap internally.

2. **b) O(log n)** - Tree operations are logarithmic.

3. **b) Yes, one null** - Allows one null if elements are Comparable.

4. **d) All of the above** - Implements Set, NavigableSet, and SortedSet.

5. **b) Greatest element strictly less than key** - lower() is exclusive.

6. **a) floor() includes equality** - floor() returns element <= key.

7. **b) new TreeSet(Comparator.reverseOrder())** - Use reverse comparator.

8. **b) Elements before toElement** - Returns a view of elements less than toElement.

9. **b) Yes, via Comparator** - Pass a Comparator to the constructor.

10. **b) Throws ClassCastException** - Elements must be Comparable or use Comparator.
