# HashSet Quiz

## Questions

**1. What data structure does HashSet use internally?**
a) Array
b) Linked list
c) Hash table
d) Binary tree

**2. Does HashSet maintain insertion order?**
a) Yes
b) No
c) Sometimes
d) Only in Java 11+

**3. What is the time complexity of add() in HashSet?**
a) O(n)
b) O(log n)
c) O(1) amortized
d) O(n²)

**4. Can HashSet contain null elements?**
a) No
b) Yes, exactly one
c) Yes, unlimited
d) Only with generics

**5. What does HashSet use for hashing?**
a) hashCode()
b) toString()
c) equals()
d) compareTo()

**6. What happens when two elements have the same hash code?**
a) Exception
b) They are stored in the same bucket (collision handling)
c) Only one is stored
d) Both are rejected

**7. Is HashSet synchronized?**
a) Yes
b) No
c) Only for reads
d) Only in Java 11+

**8. Which method checks if an element exists?**
a) search()
b) find()
c) contains()
d) has()

**9. What is the initial default capacity of HashSet?**
a) 8
b) 16
c) 32
d) 64

**10. What is the load factor of HashSet?**
a) 0.5
b) 0.75
c) 1.0
d) 2.0

---

## Answers

1. **c) Hash table** - HashSet is backed by a HashMap internally.

2. **b) No** - HashSet does not guarantee any order.

3. **c) O(1) amortized** - Constant time for add, remove, contains.

4. **b) Yes, exactly one** - Allows one null element.

5. **a) hashCode()** - Uses hashCode() to determine bucket placement.

6. **b) They are stored in the same bucket** - Collision handling via chaining.

7. **b) No** - HashSet is not synchronized; use Collections.synchronizedSet().

8. **c) contains()** - Returns true if element exists.

9. **b) 16** - Default initial capacity is 16.

10. **b) 0.75** - Default load factor is 0.75 (75%).
