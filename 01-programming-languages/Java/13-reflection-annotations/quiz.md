# Reflection and Annotations Quiz

## Question 1
Which method is used to access a private field of a class via reflection?
- A) `field.get(obj)`
- B) `field.setAccessible(true)` followed by `field.get(obj)`
- C) `field.readAs(obj)`
- D) `field.private(obj)`

**Answer: B**
**Explanation:** To access private fields via reflection, you must first call `setAccessible(true)` to bypass the access check, then use `field.get(obj)` to retrieve the value.

## Question 2
What is the difference between `@Retention(RetentionPolicy.SOURCE)` and `@Retention(RetentionPolicy.RUNTIME)`?
- A) SOURCE annotations are faster, RUNTIME are slower
- B) SOURCE annotations are discarded after compilation, RUNTIME are available via reflection at runtime
- C) There is no difference
- D) RUNTIME annotations only work with interfaces

**Answer: B**
**Explanation:** SOURCE retention means the annotation exists only in source code and is discarded by the compiler. RUNTIME retention means the annotation is stored in the .class file and can be accessed via reflection at runtime.

## Question 3
What is a Dynamic Proxy in Java?
- A) A proxy pattern implemented at compile time
- B) A mechanism to create proxy instances at runtime that implement one or more interfaces
- C) A type of network proxy
- D) A class that extends java.lang.Proxy

**Answer: B**
**Explanation:** Dynamic Proxy creates proxy objects at runtime using `java.lang.reflect.Proxy`. It requires an `InvocationHandler` and is commonly used for AOP, logging, and transaction management.

## Question 4
Which annotation is used to indicate that a method overrides a superclass method and should trigger a compile-time check?
- A) `@Deprecated`
- B) `@SuppressWarnings`
- C) `@Override`
- D) `@FunctionalInterface`

**Answer: C**
**Explanation:** `@Override` tells the compiler that the method is intended to override a method from a superclass. If the method doesn't actually override anything, the compiler generates an error.

## Question 5
What is a significant performance implication of using reflection in production code?
- A) Reflection makes code run faster
- B) Reflection calls are slower than direct calls due to boxing/unboxing, method lookup, and access checks
- C) Reflection has no performance impact
- D) Reflection only affects memory, not speed

**Answer: B**
**Explanation:** Reflection involves runtime method lookup, parameter boxing/unboxing, and security checks, making it significantly slower than direct method calls. It should be used judiciously in performance-critical code.