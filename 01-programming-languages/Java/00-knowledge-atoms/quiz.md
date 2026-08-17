# Quiz: Knowledge Atoms

## Multiple Choice Questions

### Q1. What happens when you compare two `Integer` objects with values 128 using `==`?
- A) `true` — they are the same value
- B) `false` — they are different objects outside the cache range
- C) `Compilation error`
- D) `throws NullPointerException`

**Answer: B** — Java caches `Integer` objects only for values -128 to 127. Values outside this range create distinct objects.

### Q2. Which of the following is required when overriding `equals()`?
- A) Override `toString()`
- B) Override `hashCode()`
- C) Implement `Comparable`
- D) Extend `AbstractMap`

**Answer: B** — The Java specification mandates that if `equals()` is overridden, `hashCode()` must also be overridden to maintain the contract: equal objects must have equal hash codes.

### Q3. What is the default garbage collector in Java 9+?
- A) Serial GC
- B) Parallel GC
- C) G1 GC
- D) ZGC

**Answer: C** — G1 (Garbage First) became the default collector starting in Java 9, balancing throughput and latency.

### Q4. Which keyword ensures a field is visible to all threads immediately?
- A) `synchronized`
- B) `transient`
- C) `volatile`
- D) `final`

**Answer: C** — `volatile` guarantees visibility by flushing writes to main memory and reading from main memory, preventing thread-local caching.

### Q5. How does Java pass object references to methods?
- A) By reference
- B) By value (copy of the reference)
- C) By pointer
- D) By memory address

**Answer: B** — Java is always pass-by-value. For objects, a copy of the reference is passed, not the reference itself.

### Q6. Which of the following makes an object truly immutable?
- A) Making the class `public`
- B) Making all fields `private` with no setters and no references to mutable objects
- C) Using `synchronized` on all methods
- D) Implementing `Cloneable`

**Answer: B** — True immutability requires final fields, no setters, no references to mutable objects (or defensive copies), and the class should be final to prevent subclass mutation.

### Q7. What does type erasure do to generics at runtime?
- A) Removes all generic type information, using raw types
- B) Preserves generic types in bytecode
- C) Converts generics to dynamic types
- D) Stores generic metadata in Metaspace

**Answer: A** — Java erases generic type parameters at runtime, replacing them with their bounds (or `Object`). This means `List<String>` and `List<Integer>` are the same class at runtime.

### Q8. What is the result of autoboxing `null` to a primitive?
- A) `0`
- B) `null`
- C) `NullPointerException`
- D) `Compilation error`

**Answer: C** — Unboxing `null` to a primitive throws `NullPointerException` because primitives cannot hold `null`.

### Q9. Which happens-before rule guarantees visibility after `Thread.start()`?
- A) Program Order Rule
- B) Volatile Variable Rule
- C) Thread Start Rule
- D) Transitivity Rule

**Answer: C** — The Thread Start Rule states that any action in the parent thread before calling `start()` happens-before any action in the started thread.

### Q10. What is the primary risk of using mutable objects in `hashCode()`?
- A) Performance overhead
- B) Objects may become unfindable in hash-based collections after field mutation
- C) Thread safety issues only
- D) Compilation warnings

**Answer: B** — If a mutable field used in `hashCode()` changes after insertion into a `HashMap`, the bucket lookup will fail because the hash code no longer matches the original bucket.

---

## True/False Questions

### T1. Java supports both pass-by-value and pass-by-reference.
**Answer: False** — Java is always pass-by-value. Object references are passed by value (a copy of the reference), which is different from pass-by-reference.

### T2. The `Integer` cache can be configured via JVM flags.
**Answer: True** — The flag `-XX:AutoBoxCacheMax=N` sets the upper bound of the Integer cache (lower bound is always -128).

### T3. `String` is mutable in Java.
**Answer: False** — `String` is immutable. Methods like `toUpperCase()` return a new `String` object rather than modifying the original.

---

## Code Output Questions

### C1. What does this code print?
```java
Integer a = 200;
Integer b = 200;
System.out.println(a == b);
System.out.println(a.equals(b));
```
**Answer:**
```
false
true
```
`200` is outside the cache range (-128 to 127), so `a` and `b` are different objects. `==` compares references, while `.equals()` compares values.

### C2. What does this code print?
```java
List<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(3);
list.remove(1);
System.out.println(list);
System.out.println(list.size());
```
**Answer:**
```
[1, 3]
2
```
`list.remove(1)` calls `remove(int index)`, removing the element at index 1 (the value `2`), not the value `1`. To remove by value, use `list.remove(Integer.valueOf(1))`.
