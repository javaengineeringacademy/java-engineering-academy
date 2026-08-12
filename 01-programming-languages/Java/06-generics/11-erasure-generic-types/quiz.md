# Quiz: Erasure of Generic Types

### Q1:
What does `List<String>` become after type erasure?

A) `List<String>`
B) `List`
C) `ArrayList`
D) `Object`

**Answer:** B — The type argument `String` is erased, leaving the raw type `List`.

---

### Q2:
What does `<T>` (unbounded) erase to?

A) `T`
B) `Object`
C) `Nothing`
D) The first used type argument

**Answer:** B — Unbounded type parameters erase to `Object`.

---

### Q3:
What does `<T extends Comparable>` erase to?

A) `Object`
B) `T`
C) `Comparable`
D) `Comparable<T>`

**Answer:** C — Bounded type parameters erase to their first (leftmost) bound.

---

### Q4:
True or False: At runtime, you can distinguish `List<String>` from `List<Integer>`.

**Answer:** False — Both erase to `List` at runtime; they are indistinguishable.

---

### Q5:
What does `Map<String, Integer>` erase to?

A) `Map<String, Integer>`
B) `Map<String, Object>`
C) `Map<Object, Object>`
D) `Map`

**Answer:** D — All type arguments are erased, leaving the raw `Map`.

---

### Q6:
What method does the compiler generate to preserve polymorphism after erasure?

A) Synthetic method
B) Bridge method
C) Helper method
D) Proxy method

**Answer:** B — Bridge methods ensure overriding works correctly after type erasure.

---

### Q7:
Why is `new List<String>[10]` illegal?

A) Arrays don't support generics
B) The array needs reified type info, but generic types are erased
C) `String` is not a valid array component
D) It would cause memory leaks

**Answer:** B — Arrays store type info at runtime (reified), but generic type info is erased.

---

### Q8:
What is the erasure of `<T extends Comparable<T> & Serializable>`?

A) `Object`
B) `Comparable`
C) `Comparable & Serializable`
D) `Serializable`

**Answer:** C — Multiple bounds erase to an intersection type.

---

### Q9:
True or False: The JVM executes generic type checks at runtime.

**Answer:** False — Type checks for generics are performed at compile time. At runtime, only compiler-inserted casts remain.

---

### Q10:
When a generic method is erased, what happens to its type parameter in the signature?

A) It becomes the first bound or `Object`
B) It stays as `T`
C) It becomes `var`
D) It is removed entirely

**Answer:** A — The type parameter in the method signature is replaced with its erasure.

---

### Q11:
What bytecode tool can you use to inspect erasure?

A) `javap`
B) `jshell`
C) `jlink`
D) `jmod`

**Answer:** A — `javap -c` shows bytecode, revealing erased types and bridge methods.

---

### Q12:
True or False: Bridge methods can cause unexpected behavior in stack traces.

**Answer:** True — Bridge methods are compiler-generated and may appear in stack traces, confusing developers.

---

### Q13:
What is the erasure of a method `T process(T input)` where T is unbounded?

A) `T process(T input)`
B) `Object process(Object input)`
C) `Object process(T input)`
D) `void process(Object input)`

**Answer:** B — Both the return type and parameter type erase to `Object`.
