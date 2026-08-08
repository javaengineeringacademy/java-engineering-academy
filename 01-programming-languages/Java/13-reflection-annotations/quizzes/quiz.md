# Reflection & Annotations — Quizzes

## Topic 1: Introduction to Reflection

**Q1.** What is reflection in Java?
- A) A design pattern for creating objects
- B) The ability to examine and modify code structure at runtime
- C) A compile-time optimization technique
- D) A way to implement multiple inheritance

**Answer:** B

**Q2.** Which of the following is NOT a valid use case for reflection?
- A) Framework dependency injection
- B) Business logic that works with known types
- C) Plugin architecture loading unknown classes
- D) Test utilities accessing private members

**Answer:** B

**Q3.** Approximately how much slower is reflection compared to direct access?
- A) 2-3x
- B) 10-50x
- C) 100-200x
- D) Reflection is the same speed

**Answer:** B

---

## Topic 2: Class Introspection

**Q4.** Which is NOT a way to obtain a Class object?
- A) `String.class`
- B) `"hello".getClass()`
- C) `new Class("String")`
- D) `Class.forName("java.lang.String")`

**Answer:** C (Class has no public constructor)

**Q5.** What does `clazz.getSimpleName()` return for `java.util.ArrayList`?
- A) `java.util.ArrayList`
- B) `ArrayList`
- C) `class java.util.ArrayList`
- D) `null`

**Answer:** B

**Q6.** What is the difference between `getInterfaces()` and `getGenericInterfaces()`?
- A) No difference
- B) `getGenericInterfaces()` preserves generic type information
- C) `getInterfaces()` includes inherited interfaces
- D) `getGenericInterfaces()` only returns direct interfaces

**Answer:** B

---

## Topic 3: Field Access

**Q7.** What must you call before accessing a private field via reflection?
- A) `field.makePublic()`
- B) `field.setAccessible(true)`
- C) `field.enableAccess()`
- D) Nothing, private fields are always accessible

**Answer:** B

**Q8.** How do you read a static field via reflection?
- A) `field.get(clazz)`
- B) `field.get(null)`
- C) `field.getStatic()`
- D) Static fields cannot be read via reflection

**Answer:** B

**Q9.** Can you modify a final field via reflection?
- A) No, never
- B) Yes, always reliably
- C) Technically yes in most JVMs, but unreliable
- D) Only with special permissions

**Answer:** C

---

## Topic 4: Method Invocation

**Q10.** What does `method.invoke(obj, args)` return for a void method?
- A) `void`
- B) `null`
- C) The method name
- D) `0`

**Answer:** B

**Q11.** What exception wraps the actual exception when a reflectively invoked method throws?
- A) `IllegalAccessException`
- B) `InvocationTargetException`
- C) `RuntimeException`
- D) `NoSuchMethodException`

**Answer:** B

**Q12.** How do you invoke a static method via reflection?
- A) Pass the class as the first argument
- B) Pass `null` as the first argument
- C) Use `Method.invokeStatic()`
- D) Static methods cannot be invoked via reflection

**Answer:** B

---

## Topic 5: Constructor Access

**Q13.** Why is `Constructor.newInstance()` preferred over `Class.newInstance()`?
- A) It is faster
- B) It properly handles checked exceptions and works with parameterized constructors
- C) It does not require setAccessible
- D) It supports static constructors

**Answer:** B

**Q14.** How do you construct an inner class via reflection?
- A) Use the inner class's constructor directly
- B) Pass the enclosing instance as the first argument
- C) Use `Class.forName()` with the inner class name
- D) Inner classes cannot be constructed via reflection

**Answer:** B

---

## Topic 6: Dynamic Proxy

**Q15.** JDK dynamic proxy can only proxy:
- A) Any class
- B) Classes that implement Serializable
- C) Interfaces
- D) Abstract classes

**Answer:** C

**Q16.** What should you avoid calling inside an InvocationHandler?
- A) `method.invoke(target, args)`
- B) `proxy.toString()`
- C) `target.getClass()`
- D) `method.getName()`

**Answer:** B (causes infinite recursion)

**Q17.** What is CGLIB used for in the context of proxies?
- A) Proxying interfaces
- B) Proxying concrete classes (creating subclasses)
- C) Compiling Java code
- D) Generating documentation

**Answer:** B

---

## Topic 7: Custom Annotations

**Q18.** What is the default retention policy if @Retention is not specified?
- A) SOURCE
- B) RUNTIME
- C) CLASS
- D) NONE

**Answer:** C

**Q19.** Which of these is NOT a valid annotation element return type?
- A) `String`
- B) `List<String>`
- C) `int`
- D) `Class<?>`

**Answer:** B (arrays are allowed, but not List)

**Q20.** What does @Inherited do?
- A) Makes annotation available at runtime
- B) Allows annotation on subclasses of annotated class
- C) Makes annotation repeatable
- D) Includes annotation in Javadoc

**Answer:** B

---

## Topic 8: Annotation Processing

**Q21.** When does annotation processing happen?
- A) At runtime only
- B) At compile time
- C) Both compile time and runtime
- D) At class loading time

**Answer:** B (standard annotation processing is compile-time)

**Q22.** What does `Filer.createSourceFile()` do?
- A) Reads a source file
- B) Creates a new Java source file to be compiled
- C) Deletes a source file
- D) Validates a source file

**Answer:** B

**Q23.** How do you register an annotation processor?
- A) Via @Register annotation
- B) Via META-INF/services/javax.annotation.processing.Processor
- C) Via reflection
- D) Via command line only

**Answer:** B

---

## Topic 9: Real-World Use Cases

**Q24.** How does Spring implement @Transactional?
- A) Compile-time code generation
- B) Dynamic proxy that intercepts method calls
- C) Bytecode instrumentation
- D) A special classloader

**Answer:** B

**Q25.** Why does Lombok NOT use runtime reflection?
- A) It is not powerful enough
- B) Lombok generates code at compile time for zero runtime overhead
- C) Lombok only works with interfaces
- D) Runtime reflection is faster

**Answer:** B
