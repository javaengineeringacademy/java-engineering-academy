# Quiz: Dynamic Proxy

## Question 1 (MCQ)
What is a dynamic proxy in Java?
- A) A proxy created at compile time
- B) A proxy created at runtime that implements one or more interfaces
- C) A type of network proxy
- D) A class that extends java.lang.reflect.Proxy

**Answer: B**

---

## Question 2 (MCQ)
What is the difference between JDK dynamic proxy and CGLIB?
- A) JDK proxy requires interfaces; CGLIB creates subclasses of concrete classes
- B) They are identical
- C) CGLIB is slower than JDK proxy
- D) JDK proxy only works with abstract classes

**Answer: A**

---

## Question 3 (MCQ)
What does InvocationHandler.invoke() receive?
- A) Only the method and arguments
- B) The proxy instance, the method being called, and the arguments
- C) Only the target object
- D) The class loader and interfaces

**Answer: B**

---

## Question 4 (Code Output)
What does this print?

```java
interface Greeter { String greet(String name); }
Greeter proxy = (Greeter) Proxy.newProxyInstance(
    Greeter.class.getClassLoader(),
    new Class[]{Greeter.class},
    (p, m, a) -> "Hello, " + a[0] + "!"
);
System.out.println(proxy.greet("World"));
```

**Answer:** Hello, World!

---

## Question 5 (Bug Finding)
Find the bug in InvocationHandler that calls method.invoke(proxy, args) instead of method.invoke(target, args).

**Bug:** Calling on proxy causes infinite recursion. Must call on target.

---

## Question 6 (MCQ)
How do you avoid infinite recursion in an InvocationHandler?
- A) Dont call any methods
- B) Call methods on the target object, not the proxy
- C) Use try-catch
- D) Use a timeout

**Answer: B**

---

## Question 7 (MCQ)
What interfaces does a proxy implement?
- A) Only InvocationHandler
- B) The interfaces specified in newProxyInstance()
- C) All interfaces the target implements
- D) Serializable only

**Answer: B**

---

## Question 8 (MCQ)
Name three real-world uses of dynamic proxy.
- A) Spring @Transactional, JPA lazy loading, JUnit mocking
- B) String parsing, file I/O, thread management
- C) Array sorting, binary search

**Answer: A**

---

## Question 9 (MCQ)
What happens if you call a method on the proxy not in its interfaces?
- A) Method is called on target
- B) NoSuchMethodException
- C) InvocationHandler.invoke() is still called
- D) Compilation error

**Answer: C**

---

## Question 10 (Scenario)
You need logging on a class with no interfaces. What to use?
- A) JDK dynamic proxy
- B) CGLIB or ByteBuddy
- C) Java Agent
- D) Impossible

**Answer: B**
