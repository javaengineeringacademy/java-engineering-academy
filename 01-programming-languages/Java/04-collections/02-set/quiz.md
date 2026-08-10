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

## True/False

**11. Set allows duplicate elements.**
Answer: False — Set enforces uniqueness; duplicates are rejected.

**12. HashSet maintains insertion order.**
Answer: False — HashSet has no guaranteed order. Use LinkedHashSet for insertion order.

**13. TreeSet sorts elements using natural ordering or Comparator.**
Answer: True — TreeSet uses Comparable or Comparator for sorting.

**14. Set can contain multiple null elements.**
Answer: False — Most implementations allow at most one null element.

**15. Set.of() returns a mutable set.**
Answer: False — Set.of() returns an immutable set.

## Code Output

**16. What does this code print?**
```java
Set<String> set = new HashSet<>();
set.add("A");
set.add("B");
set.add("A");
System.out.println(set.size());
```
Answer: 2 — Duplicate "A" is rejected, set contains only A and B.

**17. What does this code print?**
```java
Set<Integer> set = new TreeSet<>();
set.add(5);
set.add(1);
set.add(3);
System.out.println(set);
```
Answer: [1, 3, 5] — TreeSet sorts elements in natural order.

**18. What does this code print?**
```java
Set<String> set = Set.of("X", "Y", "Z");
System.out.println(set.contains("Y"));
```
Answer: true — Set.of() creates a set with all three elements.

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
