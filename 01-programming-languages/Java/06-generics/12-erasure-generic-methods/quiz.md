# Quiz: Erasure of Generic Methods

### Q1:
What is the erasure of `<T> T process(T input)` where T is unbounded?

A) `T process(T input)`
B) `Object process(Object input)`
C) `void process(Object input)`
D) `Object process(T input)`

**Answer:** B — Both the return type and parameter erase to `Object`.

---

### Q2:
What is a bridge method?

A) A method connecting two modules
B) A compiler-generated method that preserves polymorphism after erasure
C) A deprecated method
D) A method used for testing

**Answer:** B — Bridge methods are synthetic methods the compiler generates to ensure overriding works after erasure.

---

### Q3:
Why do bridge methods exist?

A) For performance optimization
B) To preserve polymorphic behavior when erasure changes method signatures
C) For backward compatibility with JDK 1.4
D) To enable reflection

**Answer:** B — When a subclass overrides a generic method, erasure may change the signature; the bridge method ensures correct dispatch.

---

### Q4:
What happens if you have two methods with the same erased signature?

A) They coexist fine
B) Compile error — overloading conflict
C) One is preferred at runtime
D) A bridge method resolves the conflict

**Answer:** B — Erasure makes both methods have the same signature, causing an overloading conflict.

---

### Q5:
Given:
```java
class Parent {
    <T extends Number> T process(T input) { return input; }
}
class Child extends Parent {
    @Override
    Integer process(Integer input) { return input; }
}
```
What does the compiler generate?

A) A bridge method in `Child`
B) Nothing — this is standard overriding
C) A compile error
D) A synthetic constructor

**Answer:** A — The compiler generates a bridge method `Number process(Number)` in `Child` that delegates to the narrower `Integer process(Integer)`.

---

### Q6:
How can you reflect on the generic return type of a method?

A) `method.getReturnType()`
B) `method.getGenericReturnType()`
C) `method.getTypeParameters()`
D) `method.getRawReturnType()`

**Answer:** B — `getGenericReturnType()` returns the `Type` object preserving generic info.

---

### Q7:
True or False: Bridge methods are visible in your source code.

**Answer:** False — Bridge methods are compiler-generated and only appear in bytecode, not source code.

---

### Q8:
What is the erasure of `<T extends Comparable<T>> int compare(T a, T b)`?

A) `int compare(Object a, Object b)`
B) `int compare(Comparable a, Comparable b)`
C) `int compare(Comparable<T> a, Comparable<T> b)`
D) `<T> int compare(T a, T b)`

**Answer:** B — The type parameter erases to its bound `Comparable` (raw form).

---

### Q9:
Why might bridge methods appear in stack traces?

A) They are called during normal execution
B) The JVM executes bridge methods when dispatching overridden generic methods
C) They are debug symbols
D) They only appear in debug mode

**Answer:** B — When the JVM calls the overridden method through the bridge, it can appear in stack traces.

---

### Q10:
Which is NOT affected by generic method erasure?

A) Method parameter types
B) Method return type
C) Method name
D) Type parameter bounds

**Answer:** C — Method names are never affected by erasure. Only types in the signature are erased.

---

### Q11:
True or False: You can manually write a bridge method in your source code.

**Answer:** False — Bridge methods are synthetic and compiler-generated. Attempting to write one would be a different method, not a bridge.

---

### Q12:
What is the erasure of a method `<T> List<T> filter(List<T> list)`?

A) `List<T> filter(List<T> list)`
B) `List filter(List list)`
C) `List<Object> filter(List<Object> list)`
D) `List<?> filter(List<?> list)`

**Answer:** B — Both the return type and parameter type erase to their raw forms.

---

### Q13:
When do bridge methods get generated?

A) Always for every method
B) Only when erasure changes the method signature in a subclass
C) Only for abstract methods
D) Only for static methods

**Answer:** B — Bridge methods are only generated when overriding with a narrower type creates a signature mismatch after erasure.
