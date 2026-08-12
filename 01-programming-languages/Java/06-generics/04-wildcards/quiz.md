# Quiz: Wildcards

### Q1:
What does `List<?>` represent?

A) A list of unknown type
B) A list of any type
C) A list that accepts anything
D) An empty list

**Answer:** A — `<?>` means the type parameter is unknown. It can hold references to any parameterized type.

---

### Q2:
Can you add elements to a `List<?>`?

A) Yes, any object
B) Yes, but only `null`
C) No, you cannot add any element
D) Yes, but only via cast

**Answer:** B — You can only add `null` to `List<?>`. The compiler cannot verify type safety for any other value.

---

### Q3:
What does `List<? extends Number>` allow?

A) Reading `Number` objects
B) Writing `Number` objects
C) Both reading and writing `Number` objects
D) Neither reading nor writing

**Answer:** A — Upper wildcard allows reading as the bound type, but prevents writing.

---

### Q4:
In the PECS principle, what does "Producer Extends" mean?

A) If the generic type produces items, use `extends`
B) If the generic type extends something, use it as producer
C) If the type parameter extends the producer, use it
D) None of the above

**Answer:** A — When a generic type produces (reads) items for you, use `extends` to accept subtypes.

---

### Q5:
What does `List<? super Integer>` allow you to add?

A) Only `Integer` objects
B) `Integer` and its subtypes
C) Any `Number`
D) Any `Object`

**Answer:** B — Lower wildcard allows adding `Integer` and its subtypes (though practically `Integer` has no subtypes).

---

### Q6:
Given:
```java
public static void addNumber(List<? super Number> list, Number n) {
    list.add(n);
}
```
Which calls compile?

A) `addNumber(new ArrayList<Number>(), 5)`
B) `addNumber(new ArrayList<Object>(), 5)`
C) `addNumber(new ArrayList<Integer>(), 5)`
D) Both A and B

**Answer:** D — Both `List<Number>` and `List<Object>` accept `? super Number`. `List<Integer>` does not since `Integer` is not a supertype of `Number`.

---

### Q7:
True or False: You can use wildcards in return types.

**Answer:** False — Wildcards in return types cause compile errors because the caller cannot capture the unknown type for variable assignment.

---

### Q8:
What is "capture conversion"?

A) Converting a wildcard to a concrete type at runtime
B) The compiler assigning a unique type variable to a wildcard within a scope
C) Capturing exceptions from generic methods
D) Converting between generic and raw types

**Answer:** B — Capture conversion is the compiler's mechanism to assign a fresh type variable to `?` within a block, enabling type-safe operations.

---

### Q9:
Which is correct for the PECS pattern in `Collections.copy`?

A) `copy(List<? extends T> dest, List<? super T> src)`
B) `copy(List<? super T> dest, List<? extends T> src)`
C) `copy(List<T> dest, List<T> src)`
D) `copy(List<?> dest, List<?> src)`

**Answer:** B — The destination is a consumer (`? super T`), the source is a producer (`? extends T`).

---

### Q10:
True or False: `List<? extends Number>` and `List<Number>` are the same type.

**Answer:** False — `List<? extends Number>` accepts `List<Integer>`, `List<Double>`, etc., while `List<Number>` only accepts `List<Number>`.

---

### Q11:
What happens if you try this?
```java
List<? extends Number> list = new ArrayList<Integer>();
list.add(5);
```

A) Compiles and adds successfully
B) Compile error — cannot add to `? extends` wildcard
C) Runtime exception
D) Warning but compiles

**Answer:** B — You cannot add to a list typed as `? extends` because the compiler doesn't know the concrete type.

---

### Q12:
Given `Comparator<? super T>`, what does this allow?

A) Comparing any supertype of T
B) A comparator that can compare T values (possibly accepting supertypes)
C) A comparator for the exact type T only
D) A null comparator

**Answer:** B — `? super T` means the comparator can handle T or any supertype of T, making it more flexible.

---

### Q13:
Which wildcard form should you use when a method only iterates over a list without knowing or caring about the element type?

A) `List<T>`
B) `List<? extends Object>`
C) `List<?>`
D) `List<Object>`

**Answer:** C — `<?>` is the simplest and most flexible choice when type is irrelevant.
