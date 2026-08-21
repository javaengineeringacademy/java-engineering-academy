# Quiz: Class Introspection

## Question 1 (MCQ)
What is the difference between `getName()` and `getSimpleName()`?
- A) `getName()` returns the simple name, `getSimpleName()` returns the full name
- B) `getName()` returns the fully-qualified name, `getSimpleName()` returns just the class name
- C) They return the same value
- D) `getSimpleName()` is deprecated

**Answer: B**
**Explanation:** `getName()` returns the fully-qualified name (e.g., `java.util.ArrayList`), while `getSimpleName()` returns just the class name without the package (e.g., `ArrayList`).

---

## Question 2 (MCQ)
Which method returns only the direct interfaces implemented by a class?
- A) `getInterfaces()`
- B) `getGenericInterfaces()`
- C) `getSuperclass()`
- D) `getDeclaredClasses()`

**Answer: A**
**Explanation:** `getInterfaces()` returns only the directly implemented interfaces. For inherited interfaces, you must walk the class hierarchy.

---

## Question 3 (Code Output)
What does this print?

```java
Class<?> clazz = int.class;
System.out.println(clazz.isPrimitive());
System.out.println(clazz.getName());
```

**Answer:** `true` then `int`
**Explanation:** `int.class` is the primitive type Class object. `getName()` returns `"int"` for primitive types.

---

## Question 4 (MCQ)
When is `Class.forName()` preferred over `.class`?
- A) When you know the type at compile time
- B) When the class name comes from a string at runtime (config, user input)
- C) For better performance
- D) When you need to avoid exceptions

**Answer: B**
**Explanation:** `Class.forName()` is for dynamic class loading when the name isn't known until runtime.

---

## Question 5 (Bug Finding)
Find the bug:

```java
Class<?> clazz = Class.forName("java.lang.String");
Package pkg = clazz.getPackage();
System.out.println(pkg.getName());
```

**Bug:** If `Class.forName()` fails, it throws `ClassNotFoundException` which is not caught. Add a try-catch or throws clause.

---

## Question 6 (MCQ)
What does `isAssignableFrom()` check?
- A) If the calling class is an instance of the parameter class
- B) If the parameter class can be assigned to the calling class (is-a relationship)
- C) If both classes are identical
- D) If the classes share a common interface

**Answer: B**
**Explanation:** `A.isAssignableFrom(B)` returns true if B is a subclass of A or implements A. It's the reverse of `instanceof`.

---

## Question 7 (MCQ)
What does `getCanonicalName()` return for anonymous classes?
- A) The full class name
- B) The simple name
- C) `null`
- D) The enclosing class name

**Answer: C**
**Explanation:** `getCanonicalName()` returns `null` for anonymous and local classes because they don't have a canonical name.

---

## Question 8 (MCQ)
How do you check if a class is an enum via reflection?
- A) `clazz.getType() == Type.ENUM`
- B) `clazz.isEnum()`
- C) `Modifier.isEnum(clazz.getModifiers())`
- D) `clazz instanceof Enum`

**Answer: B**
**Explanation:** `Class.isEnum()` returns true if the class represents an enum type.

---

## Question 9 (Code Output)
What does this print?

```java
Class<?> clazz = String.class;
System.out.println(clazz.getModifiers() & java.lang.reflect.Modifier.PUBLIC);
System.out.println(Modifier.isFinal(clazz.getModifiers()));
```

**Answer:** `1` (or the value of PUBLIC) then `true`
**Explanation:** `String` is `public final`. `Modifier.PUBLIC` is 1, and `Modifier.isFinal()` returns true.

---

## Question 10 (Scenario)
You need to load a class named by a user at runtime and call its `process()` method. Which class loading approach should you use?
- A) `Class.forName(className)` with the default classloader
- B) `getClass().getClassLoader().loadClass(className)`
- C) `Thread.currentThread().getContextClassLoader().loadClass(className)`
- D) Any of the above, depending on the classloader context needed

**Answer: D**
**Explanation:** The choice depends on the classloader context. For web applications, the context classloader is preferred. For plugin systems, a custom classloader may be needed.
