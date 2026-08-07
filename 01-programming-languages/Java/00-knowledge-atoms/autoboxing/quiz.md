# Autoboxing Quiz

Test your knowledge of Java autoboxing and unboxing.

---

## Question 1
What is autoboxing in Java?
- A) Manual conversion from primitive to wrapper
- B) Automatic conversion from primitive to wrapper
- C) Automatic conversion from wrapper to primitive
- D) Manual conversion from wrapper to primitive

---

## Question 2
What is unboxing in Java?
- A) Manual conversion from primitive to wrapper
- B) Automatic conversion from primitive to wrapper
- C) Automatic conversion from wrapper to primitive
- D) Manual conversion from wrapper to primitive

---

## Question 3
What is the range of the Integer cache in Java?
- A) -256 to 255
- B) -128 to 127
- C) 0 to 127
- D) -128 to 255

---

## Question 4
What will the following code output?
```java
Integer a = 127;
Integer b = 127;
System.out.println(a == b);
```
- A) true
- B) false
- C) Compilation error
- D) NullPointerException

---

## Question 5
What will the following code output?
```java
Integer c = 128;
Integer d = 128;
System.out.println(c == d);
```
- A) true
- B) false
- C) Compilation error
- D) NullPointerException

---

## Question 6
What will the following code output?
```java
Integer e = 128;
Integer f = 128;
System.out.println(e.equals(f));
```
- A) true
- B) false
- C) Compilation error
- D) NullPointerException

---

## Question 7
What happens when you unbox a null Integer?
- A) Returns 0
- B) Returns null
- C) Throws NullPointerException
- D) Compilation error

---

## Question 8
Which wrapper class has no cache?
- A) Integer
- B) Long
- C) Float
- D) Character

---

## Question 9
What is the result of this expression?
```java
Integer a = 10;
Integer b = 20;
Integer c = a + b;
```
- A) Compilation error
- B) c is an Integer with value 30
- C) c is an int with value 30
- D) NullPointerException

---

## Question 10
Which method should you use to compare Integer objects?
- A) ==
- B) equals()
- C) compareTo()
- D) Both B and C

---

## Answers

1. B) Automatic conversion from primitive to wrapper
2. C) Automatic conversion from wrapper to primitive
3. B) -128 to 127
4. A) true (within cache range)
5. B) false (outside cache range)
6. A) true (equals() compares values)
7. C) Throws NullPointerException
8. C) Float (no cache)
9. B) c is an Integer with value 30
10. D) Both B and C (equals() for equality, compareTo() for ordering)
