# Set Interface Quiz

## Questions

**1. What is the main characteristic of a Set?**
a) Allows duplicates
b) Maintains insertion order
c) No duplicate elements
d) Sorted by default

**2. Which Set implementation maintains insertion order?**
a) HashSet
b) TreeSet
c) LinkedHashSet
d) EnumSet

**3. Can a Set contain null elements?**
a) No
b) Yes, at most one
c) Yes, depends on implementation
d) Only HashSet

**4. What does Set.of() return?**
a) A mutable set
b) An immutable set
c) A synchronized set
d) A sorted set

**5. Which method removes all elements from a Set?**
a) remove()
b) delete()
c) clear()
d) empty()

**6. What is the time complexity of contains() in HashSet?**
a) O(n)
b) O(log n)
c) O(1)
d) O(n²)

**7. Which Set is best for natural ordering?**
a) HashSet
b) LinkedHashSet
c) TreeSet
d) All are equal

**8. What does addAll() do for Sets?**
a) Replaces elements
b) Adds elements, ignoring duplicates
c) Throws exception
d) Returns new set

**9. Can Sets contain different data types?**
a) No
b) Yes, only in raw form
c) Yes, due to type erasure
d) Only with Object type

**10. What is the difference between HashSet and TreeSet?**
a) HashSet is faster
b) TreeSet is sorted
c) HashSet allows null
d) All of the above

---

## Answers

1. **c) No duplicate elements** - Sets enforce uniqueness by contract.

2. **c) LinkedHashSet** - Maintains a doubly-linked list across elements.

3. **c) Yes, depends on implementation** - Most allow one null; EnumSet doesn't.

4. **b) An immutable set** - Set.of() returns an unmodifiable set.

5. **c) clear()** - Removes all elements from the set.

6. **c) O(1)** - HashSet provides constant-time contains().

7. **c) TreeSet** - Uses natural ordering or a Comparator.

8. **b) Adds elements, ignoring duplicates** - Duplicates are silently discarded.

9. **c) Yes, due to type erasure** - Runtime type is just Object[].

10. **d) All of the above** - HashSet is faster (O(1)), TreeSet is sorted (O(log n)), HashSet allows null.
