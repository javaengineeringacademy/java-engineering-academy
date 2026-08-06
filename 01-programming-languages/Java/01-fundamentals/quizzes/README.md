# Fundamentals Quizzes

Test your knowledge of Java fundamentals with these 30 quiz questions covering variables, operators, control flow, methods, arrays, and strings.

---

## Variables (5 Questions)

### Q1. What is the default value of a boolean instance variable in Java?

A) `true`
B) `false`
C) `0`
D) `null`

**Answer:** B) `false`

**Explanation:** In Java, all primitive type instance variables are initialized to their default values. For `boolean`, the default is `false`. For reference types, the default is `null`.

---

### Q2. Which of the following is a valid variable name in Java?

A) `2ndPlace`
B) `my-variable`
C) `_count`
D) `class`

**Answer:** C) `_count`

**Explanation:** Variable names cannot start with a digit (eliminates A), cannot contain hyphens (eliminates B), and cannot be reserved keywords like `class` (eliminates D). An underscore is a valid starting character.

---

### Q3. What is the result of this code?
```java
byte b = 130;
```

A) Compiles successfully
B) Compilation error: incompatible types
C) Runtime error
D) b = 130

**Answer:** B) Compilation error: incompatible types

**Explanation:** A `byte` in Java can hold values from -128 to 127. The value 130 exceeds this range, so Java requires an explicit cast: `byte b = (byte) 130;`. Without the cast, it's a compilation error.

---

### Q4. What is the scope of a variable declared inside a for loop?

A) Entire class
B) Entire method
C) Only within the loop block
D) Entire program

**Answer:** C) Only within the loop block

**Explanation:** Variables declared in a loop's initialization or within the loop body have block scope — they exist only from declaration to the end of the enclosing block (the loop body). They cannot be accessed outside the loop.

---

### Q5. What is the difference between `==` and `.equals()` when comparing Strings?

A) They are identical
B) `==` compares references, `.equals()` compares content
C) `.equals()` compares references, `==` compares content
D) Both compare content

**Answer:** B) `==` compares references, `.equals()` compares content

**Explanation:** The `==` operator compares memory addresses (whether two references point to the same object), while `.equals()` compares the actual content of the strings. For value comparison of strings, always use `.equals()`.

---

## Operators (5 Questions)

### Q6. What is the output of this expression?
```java
int result = 10 + 5 * 2;
```

A) 30
B) 20
C) 25
D) 100

**Answer:** A) 30

**Explanation:** Due to operator precedence, multiplication is performed before addition. So `5 * 2 = 10` first, then `10 + 10 = 20`. Wait — that's 20. Let me recalculate: `10 + (5 * 2) = 10 + 10 = 20`. Answer B is correct. Actually: `5 * 2 = 10`, `10 + 10 = 20`. Answer is B.

**Answer:** B) 20

---

### Q7. What is the result of `15 & 9` in binary?

A) 1
B) 9
C) 24
D) 6

**Answer:** B) 9

**Explanation:** 
- 15 in binary: `1111`
- 9 in binary: `1001`
- Bitwise AND: `1001` = 9

The AND operator returns 1 only where both bits are 1.

---

### Q8. What does the ternary operator do?

A) Performs three operations at once
B) Is a shorthand for if-else
C) Compares three values
D) Loops three times

**Answer:** B) Is a shorthand for if-else

**Explanation:** The ternary operator `condition ? valueIfTrue : valueIfFalse` is a compact way to write simple if-else statements. It evaluates the condition and returns one of two values based on the result.

---

### Q9. What is the output?
```java
int x = 5;
System.out.println(x++ + ++x);
```

A) 10
B) 11
C) 12
D) 13

**Answer:** C) 12

**Explanation:** `x++` returns 5 then increments x to 6. `++x` increments x to 7 then returns 7. So the expression is `5 + 7 = 12`.

---

### Q10. Which operator has the highest precedence?

A) `+`
B) `*`
C) `=`
D) `()`

**Answer:** D) `()`

**Explanation:** Parentheses have the highest precedence among these options. The order from highest to lowest is: `()` > `*` > `+` > `=`. Parentheses are used to override default precedence.

---

## Control Flow (5 Questions)

### Q11. How many times will this loop execute?
```java
for (int i = 0; i < 10; i++) {
    // body
}
```

A) 9 times
B) 10 times
C) 11 times
D) Infinite

**Answer:** B) 10 times

**Explanation:** The loop starts at `i = 0` and continues while `i < 10`. Values of i: 0,1,2,3,4,5,6,7,8,9 — that's 10 iterations.

---

### Q12. What happens if you forget the `break` in a switch statement?

A) Compilation error
B) Only the first matching case executes
C) All subsequent cases also execute (fall-through)
D) The program crashes

**Answer:** C) All subsequent cases also execute (fall-through)

**Explanation:** Without a `break`, execution "falls through" to the next case regardless of whether it matches. This is called fall-through behavior and is a common source of bugs.

---

### Q13. What is the output?
```java
int i = 0;
while (i < 5) {
    if (i == 3) break;
    System.out.print(i + " ");
    i++;
}
```

A) `0 1 2 3`
B) `0 1 2`
C) `0 1 2 3 4`
D) `0 1 2 4`

**Answer:** B) `0 1 2`

**Explanation:** When `i == 3`, the `break` statement exits the loop. So only values 0, 1, and 2 are printed.

---

### Q14. What is the difference between `break` and `continue`?

A) They are identical
B) `break` exits the loop, `continue` skips to next iteration
C) `continue` exits the loop, `break` skips to next iteration
D) Both exit the loop

**Answer:** B) `break` exits the loop, `continue` skips to next iteration

**Explanation:** `break` completely terminates the innermost loop. `continue` skips the remaining code in the current iteration and jumps to the next iteration of the loop.

---

### Q15. What is the result of this switch expression?
```java
String result = switch (3) {
    case 1 -> "one";
    case 2 -> "two";
    case 3 -> "three";
    default -> "other";
};
```

A) "one"
B) "two"
C) "three"
D) Compilation error

**Answer:** C) "three"

**Explanation:** This uses Java 14+ switch expressions with arrow syntax. The case `3` matches, so `"three"` is assigned to `result`. Arrow syntax doesn't require `break`.

---

## Methods (5 Questions)

### Q16. Can two methods have the same name but different parameter lists?

A) No, this causes a compilation error
B) Yes, this is called method overloading
C) Yes, this is called method overriding
D) Only if they return different types

**Answer:** B) Yes, this is called method overloading

**Explanation:** Method overloading allows multiple methods with the same name but different parameter lists (different number, types, or order of parameters). The return type alone is not sufficient for overloading.

---

### Q17. What is the base case in a recursive method?

A) The case that calls the method again
B) The case that stops the recursion
C) The first parameter
D) The return type

**Answer:** B) The case that stops the recursion

**Explanation:** The base case is the condition under which the recursive method returns a value without making another recursive call. Without a base case, recursion would continue indefinitely causing a stack overflow.

---

### Q18. What does `varargs` allow you to do?

A) Declare variables of any type
B) Pass a variable number of arguments to a method
C) Create variable-length arrays
D) Use variables without declaration

**Answer:** B) Pass a variable number of arguments to a method

**Explanation:** Varargs (variable arguments) syntax `Type... args` allows a method to accept zero or more arguments of the specified type. The arguments are treated as an array inside the method.

---

### Q19. What is the output?
```java
public static void modify(int x) {
    x = 10;
}
public static void main(String[] args) {
    int a = 5;
    modify(a);
    System.out.println(a);
}
```

A) 5
B) 10
C) 0
D) Compilation error

**Answer:** A) 5

**Explanation:** Java is pass-by-value. The method receives a copy of `a`'s value (5). Changing `x` inside the method doesn't affect the original variable `a` in main.

---

### Q20. Which modifier makes a method accessible only within its own class?

A) `public`
B) `protected`
C) `private`
D) `static`

**Answer:** C) `private`

**Explanation:** `private` restricts access to within the same class only. `public` is accessible everywhere, `protected` is accessible within the package and subclasses, and `static` relates to class-level vs instance-level, not access.

---

## Arrays (5 Questions)

### Q21. What is the index of the first element in a Java array?

A) 1
B) -1
C) 0
D) It depends on the array type

**Answer:** C) 0

**Explanation:** Java arrays are zero-indexed. The first element is at index 0, the second at index 1, and so on. This is consistent with most modern programming languages.

---

### Q22. What happens when you access `arr[arr.length]`?

A) Returns the last element
B) Returns null
C) Throws ArrayIndexOutOfBoundsException
D) Returns 0

**Answer:** C) Throws ArrayIndexOutOfBoundsException

**Explanation:** Valid indices are 0 to `arr.length - 1`. Accessing index `arr.length` is one past the end of the array, which throws an `ArrayIndexOutOfBoundsException` at runtime.

---

### Q23. What is the output?
```java
int[] arr = {1, 2, 3, 4, 5};
int[] copy = arr;
copy[0] = 99;
System.out.println(arr[0]);
```

A) 1
B) 99
C) 0
D) NullPointerException

**Answer:** B) 99

**Explanation:** `copy = arr` copies the reference, not the array. Both `copy` and `arr` point to the same array in memory. Changing `copy[0]` also changes `arr[0]`. To copy an array, use `Arrays.copyOf()` or `clone()`.

---

### Q24. How do you find the length of a 2D array `matrix`?

A) `matrix.length`
B) `matrix[0].length`
C) Both A and B (for rows and columns respectively)
D) `matrix.size()`

**Answer:** C) Both A and B (for rows and columns respectively)

**Explanation:** `matrix.length` gives the number of rows. `matrix[0].length` gives the number of columns (length of the first row). 2D arrays in Java are arrays of arrays.

---

### Q25. What is the output?
```java
int[] a = {1, 2, 3};
int[] b = {4, 5, 6};
int[] c = new int[6];
System.arraycopy(a, 0, c, 0, 3);
System.arraycopy(b, 0, c, 3, 3);
System.out.println(Arrays.toString(c));
```

A) `[1, 2, 3, 4, 5, 6]`
B) `[4, 5, 6, 1, 2, 3]`
C) `[1, 2, 3, 0, 0, 0]`
D) Compilation error

**Answer:** A) `[1, 2, 3, 4, 5, 6]`

**Explanation:** `System.arraycopy` copies elements from source to destination. First copy copies `a` into the first 3 positions of `c`. Second copy copies `b` into positions 3-5 of `c`.

---

## Strings (5 Questions)

### Q26. Why are Strings immutable in Java?

A) For performance reasons only
B) For security, caching, and thread-safety
C) Because strings are primitives
D) Java strings are mutable

**Answer:** B) For security, caching, and thread-safety

**Explanation:** Immutability ensures strings can be safely shared, cached (String Pool), used as class keys, and passed securely. Mutable strings would create security vulnerabilities and thread-safety issues.

---

### Q27. What is the output?
```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = new String("Hello");
System.out.println(s1 == s2);
System.out.println(s1 == s3);
System.out.println(s1.equals(s3));
```

A) `true`, `true`, `true`
B) `true`, `false`, `true`
C) `false`, `false`, `true`
D) `false`, `false`, `false`

**Answer:** B) `true`, `false`, `true`

**Explanation:** `s1` and `s2` both point to the same String Pool entry, so `==` is true. `s3` is a new object on the heap, so `s1 == s3` is false. `.equals()` compares content, so it's true.

---

### Q28. What does `StringBuilder` offer that `String` doesn't?

A) Immutability
B) Better performance for concatenation
C) Thread-safety
D) More methods

**Answer:** B) Better performance for concatenation

**Explanation:** `StringBuilder` is mutable, so it modifies the same object without creating new ones. This makes repeated concatenation (like in loops) much more efficient than using `String`, which creates a new object each time.

---

### Q29. What is the output?
```java
String str = "Hello World";
System.out.println(str.substring(6));
System.out.println(str.substring(0, 5));
```

A) `Hello`, `World`
B) `World`, `Hello`
C) `World`, `Hello `
D) `orld`, `Hello`

**Answer:** B) `World`, `Hello`

**Explanation:** `substring(6)` returns from index 6 to end: `"World"`. `substring(0, 5)` returns from index 0 to index 4 (end index is exclusive): `"Hello"`.

---

### Q30. Which method should you use to check if a string contains a specific substring?

A) `str.contains("substring")`
B) `str.includes("substring")`
C) `str.has("substring")`
D) `str.find("substring")`

**Answer:** A) `str.contains("substring")`

**Explanation:** The `contains()` method in Java's String class checks if the string contains the specified character sequence. It returns `true` if found, `false` otherwise. The `indexOf()` method can also be used: `str.indexOf("substring") != -1`.
