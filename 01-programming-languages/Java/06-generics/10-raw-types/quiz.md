# Quiz: Raw Types

### Q1:
What is a raw type?

A) A generic type without type parameters: `List` instead of `List<String>`
B) A type that cannot be instantiated
C) A primitive type
D) A type used only at runtime

**Answer:** A — A raw type is a generic class or interface used without specifying type arguments.

---

### Q2:
True or False: Raw types provide compile-time type safety.

**Answer:** False — Raw types bypass generics, losing all compile-time type checking.

---

### Q3:
What warning does the compiler emit when raw types are used?

A) `@Deprecated`
B) `unchecked`
C) `rawtypes`
D) `unsafe`

**Answer:** B — The compiler emits "unchecked" or "unchecked or unsafe operations" warnings.

---

### Q4:
What is the subtyping relationship between raw `List` and `List<String>`?

A) They are the same type
B) Raw `List` is a supertype of `List<String>`
C) `List<String>` is a supertype of raw `List`
D) They are unrelated

**Answer:** B — Raw `List` is a supertype of all `List<T>` types for backward compatibility.

---

### Q5:
Which is the correct replacement for a raw `List` parameter?

A) `List<Object>`
B) `List<?>`
C) `List`
D) Both A and B depending on context

**Answer:** D — Use `List<?>` when you don't know the type, or `List<Object>` when you truly mean any object.

---

### Q6:
True or False: Raw types were introduced in JDK 5.

**Answer:** False — Raw types existed before JDK 5 (pre-generics). Generics were introduced in JDK 5; raw types were retained for backward compatibility.

---

### Q7:
How do you suppress unchecked warnings from raw type usage?

A) `@Ignore`
B) `@SuppressWarnings("unchecked")`
C) `@SafeVarargs`
D) `@Override`

**Answer:** B — Use `@SuppressWarnings("unchecked")` with a comment explaining why.

---

### Q8:
What happens at runtime when you use a raw `List`?

A) Elements are checked for type safety
B) All operations work as `Object` — no type checking
C) A `ClassCastException` is always thrown
D) The list becomes immutable

**Answer:** B — Raw types work with `Object` at runtime, with no type checks (beyond what the compiler inserted).

---

### Q9:
Given:
```java
List raw = new ArrayList();
List<String> safe = raw;
```
Does this compile?

A) Yes — with an unchecked warning
B) No — compile error
C) Yes — no warnings
D) Only with a cast

**Answer:** A — Assigning a raw type to a parameterized type compiles with an unchecked warning.

---

### Q10:
Why were raw types kept in Java after generics were added?

A) For performance optimization
B) For backward compatibility with pre-generics code
C) Because they are useful
D) Because removing them would break the JVM

**Answer:** B — Raw types ensure existing pre-generics code continues to compile.

---

### Q11:
What is the best practice when encountering raw types in legacy code?

A) Delete all the code
B) Leave as-is forever
C) Gradually migrate to parameterized types
D) Convert everything to `Object`

**Answer:** C — Gradual migration to parameterized types improves type safety without breaking the system.

---

### Q12:
True or False: `new ArrayList()` and `new ArrayList<>()` are the same at runtime.

**Answer:** True — Due to type erasure, both produce the same `ArrayList` at runtime. The difference is compile-time type safety only.

---

### Q13:
Which is NOT a valid reason to use raw types?

A) Interfacing with pre-generics libraries
B) Backward compatibility with JDK 1.4 code
C) Avoiding compile-time type checks intentionally
D) New development in modern Java

**Answer:** D — Raw types should never be used in new development.
