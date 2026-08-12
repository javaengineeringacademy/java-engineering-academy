# Quiz: Generic Methods

### Q1:
What is the correct syntax for declaring a generic method that returns a `List<T>`?

A) `public List<T> <T> method()`
B) `public <T> List<T> method()`
C) `public <T> method() List<T>`
D) `public List.method<T> List<T>`

**Answer:** B — The type parameter `<T>` must appear before the return type.

---

### Q2:
True or False: A generic method can have its own type parameters independent of the enclosing class.

**Answer:** True — Generic methods introduce their own type parameters scoped to the method.

---

### Q3:
What happens when you call `Collections.emptyList()` without a type witness?

A) Compile error
B) Returns `List<Object>`
C) Type is inferred from context (target-type inference)
D) Returns a raw `List`

**Answer:** C — Java infers the type from the assignment context since JDK 7+.

---

### Q4:
Given:
```java
public static <T> T first(List<T> list) { return list.get(0); }
```
Which call requires an explicit type witness?

A) `String s = first(myStringList);`
B) `Object o = first(myStringList);`
C) `first(myStringList);` (discarded result)
D) `var x = first(myStringList);`

**Answer:** C — When the result is not assigned, the compiler cannot infer `T`, so a type witness is needed.

---

### Q5:
True or False: You can declare more than one type parameter on a generic method.

**Answer:** True — e.g., `public static <K, V> Map<K, V> pair(K k, V v)`.

---

### Q6:
What does the following declaration mean? `<T extends Comparable<T>>`

A) `T` must implement `Comparable` of some unrelated type
B) `T` must implement `Comparable` of itself
C) `T` must be a subclass of `Comparable`
D) `T` can be any type

**Answer:** B — It's a self-bounded type: `T` must be comparable to itself.

---

### Q7:
Given a non-generic class, where should you declare a type parameter for a method that needs it?

A) On the class
B) On the method itself
C) As a field
D) In a constructor

**Answer:** B — A generic method declares its own type parameter, keeping the class non-generic.

---

### Q8:
True or False: Generic methods can be static.

**Answer:** True — Static methods can have their own type parameters since the type is inferred per invocation.

---

### Q9:
What is a "type witness" in Java generics?

A) A runtime type check
B) An explicit type argument provided at the method call site: `<String>`
C) A cast to a generic type
D) A reflection API method

**Answer:** B — A type witness is the explicit `<Type>` before the method name.

---

### Q10:
When should you prefer a generic method over an overloaded method?

A) When you have exactly two types
B) When the logic is identical for all types
C) When you need runtime polymorphism
D) When types are unrelated and logic differs

**Answer:** B — If the implementation is the same, one generic method is cleaner than multiple overloads.

---

### Q11:
What is the scope of a type parameter declared on a generic method?

A) The entire class
B) Only the method signature and body
C) The package
D) The module

**Answer:** B — Method type parameters are scoped to the method declaration and body only.

---

### Q12:
True or False: You cannot use a generic method type parameter in a `throws` clause.

**Answer:** False — You can: `public static <T extends Exception> void risky() throws T`.

---

### Q13:
Which naming convention is recommended for type parameters in generic methods?

A) Use concrete class names like `String`
B) Use single uppercase letters: `T`, `E`, `K`, `V`
C) Use lowercase like regular variables
D) Use prefixed names like `typeT`

**Answer:** B — Single uppercase letters are the Java convention for type parameters.
