# Quiz: Generics and Inheritance / Subtyping

### Q1:
True or False: `List<Integer>` is a subtype of `List<Number>`.

**Answer:** False — Generics are invariant in Java. `List<Integer>` is not a subtype of `List<Number>`.

---

### Q2:
How can you accept a `List<Integer>` where a `List<Number>` parameter is expected?

A) Cast it explicitly
B) Use `List<? extends Number>`
C) Use `List<? super Number>`
D) Use `List<Object>`

**Answer:** B — `? extends Number` provides covariant acceptance of subtypes.

---

### Q3:
Which is true about Java arrays vs generics regarding variance?

A) Both are invariant
B) Arrays are covariant, generics are invariant
C) Arrays are invariant, generics are covariant
D) Both are covariant

**Answer:** B — Arrays are covariant (unsafely); generics are invariant for type safety.

---

### Q4:
What does `Container<? super Cat>` mean?

A) A container that holds `Cat` or its supertypes
B) A container that holds `Cat` or its subtypes
C) A container of exactly `Cat`
D) A container of any type

**Answer:** A — `? super Cat` is contravariant: accepts `Cat` and its supertypes (e.g., `Animal`, `Object`).

---

### Q5:
What is F-bounded polymorphism?

A) A type that bounds itself: `<T extends Base<T>>`
B) A final generic class
C) A generic method with multiple bounds
D) A raw type pattern

**Answer:** A — F-bounded (or recursive) polymorphism uses `<T extends Base<T>>` for self-referential type safety.

---

### Q6:
Given:
```java
class Animal { }
class Cat extends Animal { }
List<Animal> animals = new ArrayList<Cat>(); // Line X
```
Does Line X compile?

A) Yes
B) No — generics are invariant
C) Only with a cast
D) Only with a wildcard

**Answer:** B — Generics are invariant. Use `List<? extends Animal>` to accept `List<Cat>`.

---

### Q7:
Why are arrays covariant but generics are not?

A) Arrays need covariance for performance
B) Generic invariance prevents runtime `ArrayStoreException`-like errors
C) Generics don't support inheritance
D) Arrays are older language features

**Answer:** B — Invariant generics catch type mismatches at compile time, avoiding runtime errors.

---

### Q8:
True or False: `Object` is a supertype of every generic type.

**Answer:** True — `Object` is a supertype of all reference types, including parameterized types. But `List<Object>` is NOT a supertype of `List<String>`.

---

### Q9:
What pattern uses wildcards to both read and write across types?

A) Producer-Consumer with PECS
B) Singleton
C) Builder
D) Observer

**Answer:** A — PECS (Producer Extends, Consumer Super) uses both `? extends` and `? super` wildcards.

---

### Q10:
Given:
```java
void process(List<? extends Comparable> list) { ... }
```
Can you pass `List<String>`?

A) Yes — `String` implements `Comparable`
B) No — `Comparable` is not `String`
C) Only if you cast
D) Only with raw types

**Answer:** A — `String` implements `Comparable<String>`, which satisfies `? extends Comparable`.

---

### Q11:
True or False: A generic class can extend a non-generic class.

**Answer:** True — e.g., `class SpecialArrayList<T> extends AbstractList<T>` is valid.

---

### Q12:
What prevents `List<Cat>` from being assigned to `List<Animal>`?

A) Type erasure
B) Invariance — generic types must match exactly (or use wildcards)
C) Runtime type checking
D) The JVM prevents it

**Answer:** B — Invariance requires exact type matches or wildcard usage for subtyping.

---

### Q13:
Which is a valid covariant return in a generic hierarchy?

A) `Box<Animal> get()` returning `Box<Cat>`
B) `Box<? extends Animal> get()` returning `Box<Cat>`
C) `Box<Cat> get()` returning `Box<Animal>`
D) Covariant returns aren't possible with generics

**Answer:** B — With wildcards in the return type, covariant returns work. Raw `Box<Animal>` → `Box<Cat>` doesn't compile without wildcards.
