# Quiz: Constructor Access

## Question 1 (MCQ)
What is the difference between `Class.newInstance()` and `Constructor.newInstance()`?
- A) `Class.newInstance()` is deprecated; `Constructor.newInstance()` handles checked exceptions properly
- B) They are identical
- C) `Class.newInstance()` is faster
- D) `Constructor.newInstance()` only works for no-arg constructors

**Answer: A**

---

## Question 2 (MCQ)
How do you construct an inner class via reflection?
- A) Just use `Inner.class.getDeclaredConstructor().newInstance()`
- B) Pass the enclosing class instance as the first argument
- C) It's not possible
- D) Use `Inner.class.newInstance(outer)`

**Answer: B**

---

## Question 3 (MCQ)
What exception does `Constructor.newInstance()` throw?
- A) `ClassNotFoundException`
- B) `InvocationTargetException` wrapping any constructor exception
- C) `InstantiationException` only
- D) `NoSuchMethodException`

**Answer: B**

---

## Question 4 (Code Output)
What does this print?

```java
Constructor<String> ctor = String.class.getDeclaredConstructor(byte[].class);
String s = (String) ctor.newInstance((Object) new byte[]{72, 101, 108, 108, 111});
System.out.println(s);
```

**Answer:** `Hello`

---

## Question 5 (Bug Finding)
Find the bug:

```java
Constructor<MyClass> ctor = MyClass.class.newInstance();
MyClass obj = ctor.newInstance();
```

**Bug:** `Class.newInstance()` is deprecated. Use `clazz.getDeclaredConstructor().newInstance()`.

---

## Question 6 (MCQ)
Why would you use `setAccessible(true)` on a constructor?
- A) To make the constructor public
- B) To instantiate a class with a private constructor (e.g., singleton, factory)
- C) To add parameters to the constructor
- D) To change the constructor's return type

**Answer: B**

---

## Question 7 (MCQ)
What does `constructor.getParameterTypes()` return?
- A) Parameter names
- B) An array of `Class<?>` objects for each parameter type
- C) Parameter annotations
- D) The number of parameters

**Answer: B**

---

## Question 8 (MCQ)
How do you find the right constructor when there are multiple overloads?
- A) Use `getConstructors()` and pick the first
- B) Use `getDeclaredConstructor(Class<?>... parameterTypes)` with exact types
- C) Use `getConstructor()` without parameters
- D) It's impossible to disambiguate

**Answer: B**

---

## Question 9 (MCQ)
What is the benefit of `Constructor.newInstance()` over `Class.newInstance()`?
- A) It's faster
- B) It properly propagates checked exceptions as `InvocationTargetException`
- C) It supports more constructor parameter types
- D) It doesn't require the class to be loaded

**Answer: B**

---

## Question 10 (Scenario)
You need to instantiate a class that has two constructors: `Service(Config)` and `Service(Config, Logger)`. Only Config is available. What should you do?
- A) Use `Service.class.getDeclaredConstructor(Config.class)`
- B) Use `Service.class.getConstructor(Config.class)`
- C) Either A or B, but B only finds public constructors
- D) Both A and B work the same way

**Answer: C**
