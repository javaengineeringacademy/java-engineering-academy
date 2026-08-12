# Quiz: Bounded Type Parameters

### Q1:
What does `<T extends Number>` mean?

A) `T` must be exactly `Number`
B) `T` must be `Number` or a subclass of `Number`
C) `T` must be a superclass of `Number`
D) `T` can be any type

**Answer:** B — Upper bound means T must be Number or any subclass (e.g., Integer, Double).

---

### Q2:
In multiple bounds `<T extends A & B & C>`, which must come first?

A) An interface
B) A class
C) The most general type
D) Order doesn't matter

**Answer:** B — If one bound is a class, it must appear first in the bound list.

---

### Q3:
True or False: A type parameter can extend multiple classes.

**Answer:** False — Java supports single class inheritance. You can extend one class and multiple interfaces.

---

### Q4:
What is a self-bounded type parameter?

A) `<T extends T>`
B) `<T extends Comparable<T>>`
C) `<T super T>`
D) `<T extends Object>`

**Answer:** B — A self-bounded type is one where the type parameter is used as an argument to its own bound.

---

### Q5:
Given `<T extends Comparable<T>>`, which of the following is valid?

A) `method(List<Integer>)` — since `Integer` implements `Comparable<Integer>`
B) `method(List<Object>)` — since `Object` exists
C) `method(List<StringBuilder>)` — since `StringBuilder` is a class
D) None of the above

**Answer:** A — `Integer` implements `Comparable<Integer>`, satisfying the bound.

---

### Q6:
What happens if you write `<T extends ArrayList & Comparable>` without specifying which is the class?

A) Compile error — only one class allowed and it must be first
B) It's valid — both are treated equally
C) Runtime error
D) Only `ArrayList` is used as the bound

**Answer:** A — The compiler requires exactly one class, and it must appear first.

---

### Q7:
True or False: `<T extends Comparable<T>>` is equivalent to `<T extends Comparable>`.

**Answer:** False — `Comparable<T>` enforces that T compares to itself; raw `Comparable` doesn't provide that type safety.

---

### Q8:
When should you use a bounded type parameter instead of a wildcard?

A) When you need to write to the collection
B) When you only need read access
C) When the method body calls methods on the type parameter
D) Both A and C

**Answer:** D — Bounded type parameters let you both read and write, and call type-specific methods.

---

### Q9:
What is the upper bound of an unbounded type parameter `<T>`?

A) `Nothing`
B) `Object`
C) `T` has no upper bound
D) `Comparable`

**Answer:** B — Every unbounded `<T>` implicitly extends `Object`.

---

### Q10:
Given:
```java
public static <T extends Comparable<T>> T min(T a, T b) {
    return a.compareTo(b) <= 0 ? a : b;
}
```
Which call compiles?

A) `min(5, 3)` — autoboxed Integer
B) `min("a", "b")` — String
C) `min(new Object(), new Object())` — Object
D) Both A and B

**Answer:** D — Both `Integer` and `String` implement `Comparable` of themselves. `Object` does not.

---

### Q11:
True or False: You can use `&` to combine a class bound and an interface bound.

**Answer:** True — e.g., `<T extends Comparable<T> & Serializable>`.

---

### Q12:
What is a common purpose of self-bounded types in builder patterns?

A) To ensure the builder returns its own concrete type from setter methods
B) To allow null values
C) To support runtime type checking
D) To reduce memory usage

**Answer:** A — Self-bounds ensure fluent API returns the correct subtype: `return (T) this`.
