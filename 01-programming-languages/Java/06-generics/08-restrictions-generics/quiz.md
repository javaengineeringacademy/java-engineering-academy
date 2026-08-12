# Quiz: Restrictions on Generics

### Q1:
Why can't you use `int` as a type argument for generics?

A) `int` is too small
B) Type arguments must be reference types; `int` is a primitive
C) Generics don't support numbers
D) It's a style violation only

**Answer:** B — Generic type parameters must be reference types. Primitives are not Objects.

---

### Q2:
What is the correct replacement for `List<int>`?

A) `List<int>`
B) `List<Integer>`
C) `int[]`
D) `List<Number>`

**Answer:** B — Use the wrapper class `Integer` instead of the primitive `int`.

---

### Q3:
Why can't you create `new T[size]`?

A) Arrays don't support generics
B) `T` is erased at runtime; the array component type is unknown
C) It's a performance issue
D) Arrays can only hold primitives

**Answer:** B — After type erasure, the JVM doesn't know what `T` is, so it cannot create a properly typed array.

---

### Q4:
True or False: You can have `static` fields that use a class's type parameter.

**Answer:** False — Static context has no instance, so the type parameter has no meaning. Use a static inner class or `Class<T>` token instead.

---

### Q5:
What is the workaround for `instanceof` with generic types?

A) Use `instanceof List<?>` or pass `Class<T>` token
B) Use raw types
C) Use casting
D) It cannot be worked around

**Answer:** A — Use unbounded wildcard `instanceof` or `Class<T>` for runtime type checking.

---

### Q6:
Why can't you have generic exception classes?

A) `extends Exception` is not allowed with type parameters
B) Type erasure makes the catch clause unable to distinguish types
C) Exceptions must be concrete classes
D) Both B and C

**Answer:** D — Erasure removes type info, and catch clauses need concrete types.

---

### Q7:
What is the correct way to create an array of a generic type at runtime?

A) `new T[size]`
B) `(T[]) new Object[size]`
C) `Array.newInstance(clazz, size)` with cast
D) Both B and C

**Answer:** D — Both work, though C is more correct with `Class<T>` tokens.

---

### Q8:
True or False: `class Container<T> extends T` is valid Java.

**Answer:** False — You cannot extend a type parameter. Use F-bounded polymorphism: `<T extends Base<T>>`.

---

### Q9:
What happens if you try to use a primitive as a type argument?

A) Compile error
B) Runtime error
C) Autoboxing occurs and it works
D) Warning only

**Answer:** A — The compiler rejects primitive types as type arguments.

---

### Q10:
Why are static fields restricted from using type parameters?

A) Performance concern
B) Static fields belong to the class, not an instance — there's no type parameter in static context
C) JVM limitation
D) Coding convention only

**Answer:** B — Type parameters are per-instance; static fields have no instance to reference.

---

### Q11:
What library pattern works around type erasure for runtime type info?

A) TypeToken (Gson)
B) ParameterizedTypeReference (Spring)
C) Class<T> tokens
D) All of the above

**Answer:** D — All these patterns capture generic type information before erasure.

---

### Q12:
True or False: You can catch a parameterized exception type like `catch (Exception<E> e)`.

**Answer:** False — Exception types cannot be parameterized in catch clauses due to type erasure.

---

### Q13:
Which is NOT a restriction on Java generics?

A) No primitive type arguments
B) No generic array creation
C) No multiple type parameters
D) No static use of type parameters

**Answer:** C — Multiple type parameters are fully supported (e.g., `Map<K, V>`).
