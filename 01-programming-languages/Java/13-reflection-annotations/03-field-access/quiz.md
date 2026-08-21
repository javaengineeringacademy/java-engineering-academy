# Quiz: Field Access

## Question 1 (MCQ)
What is the difference between `getFields()` and `getDeclaredFields()`?
- A) `getFields()` returns all fields; `getDeclaredFields()` returns only public fields
- B) `getFields()` returns public fields including inherited; `getDeclaredFields()` returns all fields in this class only
- C) They are identical
- D) `getDeclaredFields()` includes inherited fields

**Answer: B**
**Explanation:** `getFields()` returns only public fields from the class and its superclasses. `getDeclaredFields()` returns all fields declared in the class itself (any access level), excluding inherited fields.

---

## Question 2 (MCQ)
Why must you call `setAccessible(true)` before reading a private field?
- A) To change the field's modifier to public
- B) To tell the JVM to skip the access control check
- C) To make the field visible in the debugger
- D) To load the field into memory

**Answer: B**
**Explanation:** `setAccessible(true)` does not change the field's visibility. It suppresses the JVM's access check for subsequent reflective operations on that field.

---

## Question 3 (Code Output)
What does this code print?

```java
Field field = String.class.getDeclaredField("hash");
field.setAccessible(true);
System.out.println(field.getType().getSimpleName());
```

**Answer:** `int`
**Explanation:** The `hash` field in String is of type `int`. `getType()` returns the Class object for the field's declared type.

---

## Question 4 (MCQ)
How do you read a static field via reflection?
- A) `field.get(this)`
- B) `field.get(null)`
- C) `field.getStatic()`
- D) `field.readStatic()`

**Answer: B**
**Explanation:** Static fields belong to the class, not an instance. Pass `null` as the instance to `field.get()`.

---

## Question 5 (Bug Finding)
Find the bug:

```java
Field field = MyClass.class.getDeclaredField("count");
field.set(field.get(null), 10);
```

**Bug:** `field.get(null)` reads the current value (an int), but `field.set(field.get(null), 10)` passes that int as the first argument instead of `null`. The correct call is `field.set(null, 10)`.

---

## Question 6 (MCQ)
What happens when you try to modify a `static final` field via reflection?
- A) It always works
- B) It throws `IllegalAccessException`
- C) It may work but behavior is implementation-dependent due to constant folding
- D) It throws `UnsupportedOperationException`

**Answer: C**
**Explanation:** Modifying final static fields via reflection works in most JVMs but is unreliable because the compiler may inline the original value as a constant.

---

## Question 7 (MCQ)
Which method returns a field's generic type information?
- A) `field.getType()`
- B) `field.getGenericType()`
- C) `field.getGenericClass()`
- D) `field.getParameterizedType()`

**Answer: B**
**Explanation:** `getGenericType()` returns a `Type` object that includes parameterized type information (e.g., `List<String>`), while `getType()` returns only the raw `Class<?>`.

---

## Question 8 (MCQ)
What is the purpose of `field.getDeclaringClass()`?
- A) Returns the class that contains the field access call
- B) Returns the class where the field is actually declared
- C) Returns the field's type class
- D) Returns the superclass of the field's type

**Answer: B**
**Explanation:** `getDeclaringClass()` returns the class that originally declared the field, which may differ from the class you obtained the field from (if inherited).

---

## Question 9 (Code Output)
What does this print?

```java
class A { private int x = 1; }
class B extends A { private int x = 2; }

B obj = new B();
Field field = A.class.getDeclaredField("x");
field.setAccessible(true);
System.out.println(field.get(obj));
```

**Answer:** `1`
**Explanation:** Even though `obj` is of type `B`, the field `x` from class `A` reads A's private field, not B's shadowed `x`.

---

## Question 10 (Scenario)
You need to copy all field values from one POJO to another. Which approach is most efficient?
- A) Look up each field by name inside the loop
- B) Cache all Field objects and setAccessible once, then copy in a tight loop
- C) Use getter/setter methods via reflection
- D) Serialize to JSON and deserialize

**Answer: B**
**Explanation:** Caching Field objects and calling setAccessible once avoids repeated lookups and security checks, giving the best performance for batch operations.
