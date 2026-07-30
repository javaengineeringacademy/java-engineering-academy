# Sprint 1 Quiz - Java Fundamentals

---

## 📝 Instructions
- **Time limit:** 30 minutes
- **Format:** Multiple choice (single/multiple), True/False, Code output
- **Passing score:** 70%
- **No external resources allowed**

---

## Section A: Multiple Choice (Single Answer)

### 1. What is the output of this code?
```java
int a = 10 / 3;
System.out.println(a);
```
- A) 3.33
- B) 3
- C) 3.0
- D) Compile error

### 2. Which of these is NOT a primitive type in Java?
- A) `int`
- B) `Integer`
- C) `double`
- D) `boolean`

### 3. What is the result of `"hello" == "hello"`?
- A) true
- B) false
- C) Compile error
- D) Runtime exception

### 4. Which operator has the highest precedence?
- A) `&&`
- B) `||`
- C) `!`
- D) `==`

### 5. What is the default value of a `char` field?
- A) '0'
- B) '\u0000'
- C) null
- D) ' '

---

## Section B: Multiple Choice (Multiple Answers)

### 6. Which are valid ways to declare an array? (Select all that apply)
- A) `int[] arr = new int[5];`
- B) `int arr[] = {1, 2, 3};`
- C) `int[] arr = new int[]{1, 2, 3};`
- D) `int arr = new int[5];`

### 7. Which statements about `String` are true? (Select all that apply)
- A) Strings are mutable
- B) Strings are stored in the String Pool
- C) `StringBuilder` is thread-safe
- D) `"a" + "b"` creates a new String object

### 8. Which loops are guaranteed to execute at least once? (Select all that apply)
- A) `for (int i = 0; i < 0; i++)`
- B) `while (false)`
- C) `do { } while (false);`
- D) `for (;;)`

---

## Section C: True/False

### 9. Java is pass-by-reference for objects.
- [ ] True
- [ ] False

### 10. `switch` expression (Java 12+) requires `break` statements.
- [ ] True
- [ ] False

### 11. `float f = 3.14;` compiles without error.
- [ ] True
- [ ] False

### 12. A `byte` can hold the value 200.
- [ ] True
- [ ] False

### 13. `var` can be used for field declarations.
- [ ] True
- [ ] False

---

## Section D: Code Output Prediction

### 14. What is the output?
```java
int x = 5;
System.out.println(x++ + ++x);
```

### 15. What is the output?
```java
String s = "Java";
s.toUpperCase();
System.out.println(s);
```

### 16. What is the output?
```java
int[] a = {1, 2, 3};
int[] b = a;
b[0] = 99;
System.out.println(a[0]);
```

### 17. What is the output?
```java
for (int i = 0; i < 3; i++) {
    if (i == 1) continue;
    System.out.print(i + " ");
}
```

### 18. What is the output?
```java
int x = 10;
if (x > 5)
    if (x < 15)
        System.out.println("A");
else
    System.out.println("B");
```

---

## Section E: Fill in the Blank

### 19. The `__________` operator returns the remainder of division.

### 20. To compare String content, use the `__________` method.

### 21. A `__________` loop checks the condition before each iteration.

### 22. The `__________` keyword exits a loop immediately.

### 23. `__________` is the process of converting a primitive to its wrapper class.

---

## Section F: Short Answer

### 24. Explain the difference between `==` and `.equals()` for String comparison.

### 25. Why does `10 / 3` return `3` instead of `3.33`?

### 25. What is the output of `System.out.println(-5 % 2);` and why?

### 27. When would you use `StringBuilder` vs `String`?

### 28. What is a "dangling else" problem and how to avoid it?

---

## 📊 Answer Key

| Q | Answer | Explanation |
|---|--------|-------------|
| 1 | B | Integer division truncates |
| 2 | B | Integer is wrapper class |
| 3 | A | String literals are interned |
| 4 | C | `!` is unary, highest precedence |
| 5 | B | Null character |
| 6 | A, B, C | D is invalid syntax |
| 7 | B, D | A: immutable, C: StringBuffer is thread-safe |
| 8 | C, D | C: do-while runs once, D: infinite loop |
| 9 | False | Java is pass-by-value (reference passed by value) |
| 10 | False | Arrow syntax has no fall-through |
| 11 | False | Needs `f` suffix: `3.14f` |
| 12 | False | byte max is 127 |
| 13 | False | `var` only for local variables |
| 14 | 12 | x++=5, x=6, ++x=7, 5+7=12 |
| 15 | Java | Strings immutable, toUpperCase() returns new String |
| 16 | 99 | Both reference same array |
| 17 | 0 2 | continue skips i=1 |
| 18 | A | else matches nearest if |
| 19 | modulus / % | |
| 20 | equals | |
| 21 | while | |
| 22 | break | |
| 23 | Autoboxing | |

### 24. `==` compares references, `.equals()` compares content
### 25. Integer division truncates toward zero
### 26. -1 (sign follows dividend in Java)
### 27. StringBuilder for mutable operations (building strings), String for constants
### 28. Else binds to nearest if; avoid with braces {}

---

## 🎯 Scoring
- **28-30:** Excellent (Mastery)
- **24-27:** Good (Proficient)
- **20-23:** Fair (Needs review)
- **<20:** Retake recommended

---

*Self-grade honestly. Review wrong answers with theory.md.*