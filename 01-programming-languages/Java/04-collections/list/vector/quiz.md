# Vector Quiz

## Questions

**1. What makes Vector different from ArrayList?**
a) Vector is faster
b) Vector is synchronized
c) Vector uses different storage
d) Vector has more methods

**2. What is the default capacity increment of Vector?**
a) 5
b) 10
c) Doubles the current capacity
d) Adds 10

**3. Which legacy method does Vector support?**
a) add()
b) get()
c) elementAt()
d) All of the above

**4. What interface does Vector implement?**
a) List
b) Collection
c) RandomAccess
d) All of the above

**5. What is the growth policy of Vector?**
a) Doubles when full
b) Increases by 10
c) Customizable via capacityIncrement
d) Both a and c

**6. Which method is unique to Vector (not in List)?**
a) get()
b) add()
c) capacity()
d) size()

**7. Is Vector considered legacy?**
a) No
b) Yes, since Java 1.2
c) Only in Java 8+
d) Only for small collections

**8. What is the thread-safety cost of Vector?**
a) Slower performance
b) No cost
c) Uses less memory
d) Faster access

**9. Which method returns an Enumeration?**
a) iterator()
b) elements()
c) listIterator()
d) getElements()

**10. Can Vector hold null values?**
a) No
b) Yes
c) Only one null
d) Only in Java 11+

---

## Answers

1. **b) Vector is synchronized** - All Vector methods are synchronized for thread safety.

2. **c) Doubles the current capacity** - Default capacity is 10, doubles when full.

3. **d) All of the above** - Vector has all List methods plus legacy methods.

4. **d) All of the above** - Vector implements List, Collection, and RandomAccess.

5. **d) Both a and c** - Doubles by default, but can be customized via capacityIncrement.

6. **c) capacity()** - Returns current capacity, unique to Vector.

7. **b) Yes, since Java 1.2** - Vector is a legacy class from JDK 1.0.

8. **a) Slower performance** - Synchronization adds overhead.

9. **b) elements()** - Returns an Enumeration over the elements.

10. **b) Yes** - Vector allows null values like ArrayList.
