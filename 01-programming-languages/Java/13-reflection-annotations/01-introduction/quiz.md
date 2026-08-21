# Quiz: Introduction to Reflection

## Question 1 (MCQ)
What is Java Reflection?
- A) A design pattern for creating immutable objects
- B) The ability of a running program to examine and modify its own structure at runtime
- C) A compile-time code generation technique
- D) A memory management mechanism

**Answer: B**
**Explanation:** Reflection is Java's self-introspection mechanism that allows examining and modifying classes, methods, fields, and constructors at runtime.

---

## Question 2 (MCQ)
Which of the following is NOT a valid way to obtain a `Class` object?
- A) `String.class`
- B) `"hello".getClass()`
- C) `Class.forName("java.lang.String")`
- D) `new Class("java.lang.String")`

**Answer: D**
**Explanation:** `Class` has no public constructor. The three valid ways are: `.class` literal, `getClass()` on an instance, and `Class.forName()` with a string name.

---

## Question 3 (MCQ)
What is the performance overhead of reflective method invocation compared to direct invocation?
- A) 2-5x slower
- B) 10-50x slower
- C) 100-200x slower
- D) No difference

**Answer: B**
**Explanation:** Reflection involves runtime type checking, security checks, boxing/unboxing, and lookup overhead, making it roughly 10-50x slower than direct access.

---

## Question 4 (Code Output)
What does this code print?

```java
String s = "hello";
Class<?> clazz = s.getClass();
System.out.println(clazz.getName());
```

**Answer:** `java.lang.String`
**Explanation:** `getClass()` returns the runtime type of the object, which is `java.lang.String`.

---

## Question 5 (MCQ)
Which exception is thrown when you try to access a private field via reflection without calling `setAccessible(true)`?
- A) `ClassNotFoundException`
- B) `NoSuchFieldException`
- C) `IllegalAccessException`
- D) `SecurityException`

**Answer: C**
**Explanation:** `IllegalAccessException` is thrown when the JVM's access control check prevents reflective access to a private member.

---

## Question 6 (Code Output)
What does this code print?

```java
Class<?> clazz1 = int.class;
Class<?> clazz2 = Integer.class;
Class<?> clazz3 = Integer.TYPE;
System.out.println(clazz1 == clazz2);
System.out.println(clazz1 == clazz3);
```

**Answer:** `false` then `true`
**Explanation:** `int.class` and `Integer.TYPE` refer to the same primitive type Class object. `Integer.class` is the wrapper class, which is different.

---

## Question 7 (Bug Finding)
Find the bug:

```java
Method method = MyClass.class.getDeclaredMethod("process");
Object result = method.invoke(new MyClass());
System.out.println(result);
```

**Bug:** If `process()` is private, `IllegalAccessException` is thrown. The fix is to call `method.setAccessible(true)` before `invoke()`.

---

## Question 8 (MCQ)
When should you avoid using reflection?
- A) Building a dependency injection framework
- B) Creating a plugin system
- C) Writing performance-critical business logic
- D) Implementing AOP proxies

**Answer: C**
**Explanation:** Reflection is 10-50x slower than direct access. For performance-critical code, direct method invocation is preferred.

---

## Question 9 (Scenario)
You are building a library that serializes Java objects to JSON. Should you use reflection?
- A) Yes - you need to discover fields at runtime since user types are unknown
- B) No - you should require users to implement a Serializable interface
- C) No - use annotation processing at compile time only
- D) Yes - but only for private fields

**Answer: A**
**Explanation:** Serialization libraries must work with arbitrary user types. Reflection lets you discover fields dynamically without requiring specific interfaces.

---

## Question 10 (MCQ)
What is the primary reason frameworks like Spring use reflection?
- A) To make code run faster
- B) To work with user-defined types that don't exist at compile time
- C) To avoid writing configuration files
- D) To enable multiple inheritance

**Answer: B**
**Explanation:** Frameworks need to instantiate and wire user classes that haven't been written yet. Reflection enables this dynamic discovery and instantiation.
