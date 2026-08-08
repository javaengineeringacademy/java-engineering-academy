# EnumSet Quiz

## Questions

**1. What is EnumSet designed for?**
a) General objects
b) Enum types only
c) Primitives
d) Strings

**2. How does EnumSet achieve high performance?**
a) Uses arrays
b) Uses bit vectors internally
c) Uses hash tables
d) Uses linked lists

**3. Can EnumSet contain null elements?**
a) Yes
b) No
c) Only one
d) Only if enum has null

**4. What is the time complexity of add() in EnumSet?**
a) O(n)
b) O(log n)
c) O(1)
d) Depends on enum size

**5. Which method creates an empty EnumSet?**
a) new EnumSet()
b) EnumSet.noneOf()
c) EnumSet.empty()
d) EnumSet.of()

**6. What does EnumSet.complementOf() return?**
a) An empty set
b) All values not in the set
c) The same set
d) A copy of the set

**7. What does EnumSet.range() do?**
a) Returns all values
b) Returns values between two enum constants (inclusive)
c) Returns first half
d) Returns random subset

**8. Is EnumSet thread-safe?**
a) Yes
b) No
c) Only for reads
d) Only in Java 11+

**9. What is the maximum number of enum values EnumSet supports efficiently?**
a) 32
b) 64
c) No practical limit
d) 256

**10. Can you create an EnumSet from a subset of enum values?**
a) No
b) Yes, with of() or range()
c) Only with of()
d) Only with range()

---

## Answers

1. **b) Enum types only** - EnumSet is specifically designed for enum types.

2. **b) Uses bit vectors internally** - Each enum constant maps to a bit position.

3. **b) No** - EnumSet does not allow null elements.

4. **c) O(1)** - Bit operations are constant time.

5. **b) EnumSet.noneOf()** - Creates an empty set for the specified enum type.

6. **b) All values not in the set** - Returns the complement relative to all values.

7. **b) Returns values between two enum constants (inclusive)** - range(from, to) returns a view.

8. **b) No** - Not synchronized; use Collections.synchronizedSet() if needed.

9. **c) No practical limit** - Uses long internally for up to 64 values, then arrays.

10. **b) Yes, with of() or range()** - Both methods create subsets of enum values.
