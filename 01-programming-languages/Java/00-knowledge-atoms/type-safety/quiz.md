# Type Safety Quiz

Test your Java type safety knowledge with these 10 questions.

---

### Question 1
What happens when you try to assign a `String` to an `int` variable in Java?

```java
String s = "Hello";
int n = s;
```

A) Runtime exception
B) Compile error
C) Runtime error
D) No error

---

### Question 2
What is type erasure in Java generics?

A) Removing all types from the program
B) Converting types to their primitive forms
C) Removing generic type information at runtime, leaving only raw types
D) Compiling generic code to native code

---

### Question 3
Which of the following is a safe cast?

```java
Object obj = "Hello";
```

A) `Integer i = (Integer) obj;`
B) `String s = (String) obj;`
C) `Double d = (Double) obj;`
D) `Boolean b = (Boolean) obj;`

---

### Question 4
What does `List<?>` represent in Java?

A) A list that can hold any type
B) A list that cannot hold any elements
C) A list that can hold only null values
D) A list of unknown type (read-only)

---

### Question 5
What is the result of this code?

```java
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
System.out.println(strings.getClass() == integers.getClass());
```

A) `false`
B) `true`
C) Compile error
D) Runtime exception

---

### Question 6
Which keyword is used to check an object's type before casting?

A) `typeof`
B) `class`
C) `instanceof`
D) `isinstance`

---

### Question 7
What is the benefit of using `List<? extends Number>` over `List<Number>`?

A) Better performance
B) It accepts lists of Number's subclasses (List<Integer>, List<Double>)
C) It uses less memory
D) It avoids type erasure

---

### Question 8
What is the output of this code?

```java
Object obj = "Hello";
if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
}
```

A) `null`
B) `HELLO`
C) Compile error
D) Runtime exception

---

### Question 9
Which approach preserves generic type information at runtime?

A) Using `instanceof` with parameterized types
B) Creating generic arrays
C) Passing a `Class<T>` object to a method
D) Using raw types

---

### Question 10
What is a bounded type parameter?

A) A type parameter with no restrictions
B) A type parameter that extends a specific class or implements an interface
C) A type parameter that can only be primitive types
D) A type parameter that is erased at runtime

---

## Answer Key

| Question | Answer |
|----------|--------|
| 1 | B - Compile error |
| 2 | C - Removing generic type information at runtime |
| 3 | B - `String s = (String) obj;` |
| 4 | D - A list of unknown type (read-only) |
| 5 | B - `true` |
| 6 | C - `instanceof` |
| 7 | B - Accepts lists of Number's subclasses |
| 8 | B - `HELLO` |
| 9 | C - Passing a `Class<T>` object |
| 10 | B - Extends a specific class or implements an interface |
