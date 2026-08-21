# Quiz: Method Invocation

## Question 1 (MCQ)
What does `method.invoke(obj, args)` return for a void method?
- A) The method name
- B) `null`
- C) An empty string
- D) Throws an exception

**Answer: B**

---

## Question 2 (MCQ)
How do you handle exceptions thrown by a reflectively invoked method?
- A) Catch `IllegalAccessException`
- B) Catch `InvocationTargetException` and use `getTargetException()`
- C) Catch `NoSuchMethodException`
- D) Exceptions are not wrapped

**Answer: B**

---

## Question 3 (MCQ)
What is the difference between `getMethods()` and `getDeclaredMethods()`?
- A) `getMethods()` returns all methods; `getDeclaredMethods()` returns only public
- B) `getMethods()` returns public methods including inherited; `getDeclaredMethods()` returns all methods in this class only
- C) They are identical
- D) `getDeclaredMethods()` includes inherited methods

**Answer: B**

---

## Question 4 (Code Output)
What does this print?

```java
Method m = String.class.getMethod("length");
String s = "hello";
System.out.println(m.invoke(s));
```

**Answer:** `5`

---

## Question 5 (MCQ)
How do you invoke a static method via reflection?
- A) `method.invoke(new Object())`
- B) `method.invoke(null, args)`
- C) `method.invokeStatic(args)`
- D) `method.call(args)`

**Answer: B**

---

## Question 6 (Bug Finding)
Find the bug:

```java
Method m = MyClass.class.getDeclaredMethod("process", int.class);
Object result = m.invoke(new MyClass(), 42);
```

**Bug:** If `process()` is private, this throws `IllegalAccessException`. Need `m.setAccessible(true)`.

---

## Question 7 (MCQ)
How do you resolve overloaded methods?
- A) Just use the method name
- B) Specify exact parameter types in `getDeclaredMethod()`
- C) Use `getMethod()` with partial types
- D) Overloaded methods cannot be resolved via reflection

**Answer: B**

---

## Question 8 (MCQ)
What wraps checked exceptions thrown by a reflectively invoked method?
- A) `RuntimeException`
- B) `InvocationTargetException`
- C) `ReflectiveOperationException`
- D) `ExecutionException`

**Answer: B**

---

## Question 9 (MCQ)
What does `method.getParameterTypes()` return?
- A) An array of parameter names
- B) An array of `Class<?>` objects representing parameter types
- C) A list of parameter annotations
- D) The number of parameters

**Answer: B**

---

## Question 10 (Scenario)
You need to call a method whose name and parameters come from user input. What's the safest approach?
- A) Use `Method.invoke()` with a whitelist of allowed methods
- B) Use `Runtime.exec()` with the method name
- C) Use `Class.forName()` and `getMethod()` without validation
- D) Concatenate the method name into a string and eval

**Answer: A**
