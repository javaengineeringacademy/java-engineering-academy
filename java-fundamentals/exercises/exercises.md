# Sprint 1 Exercises - Java Fundamentals

---

## 🟢 Level 1: Beginner (Syntax Practice)

### Exercise 1.1: Hello Variables
**Task:** Declare variables of each primitive type and print them.

```java
// TODO: Declare and initialize:
// - byte: 100
// - short: 30000
// - int: 2_000_000_000
// - long: 9_000_000_000L
// - float: 3.14f
// - double: 3.14159
// - char: 'A'
// - boolean: true
// Print each with descriptive label
```

**Expected Output:**
```
byte: 100
short: 30000
int: 2000000000
long: 9000000000
float: 3.14
double: 3.14159
char: A
boolean: true
```

---

### Exercise 1.2: Temperature Converter
**Task:** Convert Celsius to Fahrenheit.

```java
// Given: double celsius = 25.0;
// Formula: fahrenheit = celsius * 9/5 + 32
// Print: "25.0°C = 77.0°F"
```

**Expected Output:**
```
25.0°C = 77.0°F
```

---

### Exercise 1.3: Even or Odd
**Task:** Check if a number is even or odd using modulus.

```java
// int number = 42;
// Print: "42 is even" or "42 is odd"
```

**Expected Output:**
```
42 is even
```

---

### Exercise 1.4: Grade Calculator
**Task:** Convert numeric score to letter grade.

```java
// int score = 85;
// 90-100: A, 80-89: B, 70-79: C, 60-69: D, <60: F
// Use if-else-if ladder
```

**Expected Output:**
```
Score: 85, Grade: B
```

---

## 🟡 Level 2: Intermediate (Combining Concepts)

### Exercise 2.1: Array Statistics
**Task:** Find min, max, sum, and average of an array.

```java
// int[] numbers = {15, 42, 8, 23, 16, 4, 91, 37};
// Calculate and print: min, max, sum, average
```

**Expected Output:**
```
Min: 4
Max: 91
Sum: 236
Average: 29.5
```

---

### Exercise 2.2: String Reversal
**Task:** Reverse a string without using `StringBuilder.reverse()`.

```java
// String input = "Hello World";
// Output: "dlroW olleH"
// Use char array or StringBuilder (manual)
```

**Expected Output:**
```
Original: Hello World
Reversed: dlroW olleH
```

---

### Exercise 2.3: FizzBuzz
**Task:** Classic FizzBuzz implementation.

```java
// For numbers 1 to 20:
// - Divisible by 3: "Fizz"
// - Divisible by 5: "Buzz"
// - Divisible by both: "FizzBuzz"
// - Otherwise: the number
```

**Expected Output:**
```
1
2
Fizz
4
Buzz
Fizz
7
8
Fizz
Buzz
11
Fizz
13
14
FizzBuzz
16
17
Fizz
19
Buzz
```

---

### Exercise 2.4: Palindrome Checker
**Task:** Check if a string is a palindrome (ignoring spaces and case).

```java
// String[] tests = {"racecar", "A man a plan a canal Panama", "hello"};
// For each: print "Palindrome" or "Not palindrome"
```

**Expected Output:**
```
racecar: Palindrome
A man a plan a canal Panama: Palindrome
hello: Not palindrome
```

---

### Exercise 2.5: Prime Number Finder
**Task:** Find all prime numbers up to N.

```java
// int limit = 30;
// Print all primes from 2 to 30
```

**Expected Output:**
```
Primes up to 30: 2 3 5 7 11 13 17 19 23 29
```

---

## 🔴 Level 3: Advanced (Algorithms)

### Exercise 3.1: Binary Search Implementation
**Task:** Implement binary search on sorted array.

```java
// int[] sorted = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
// int target = 23;
// Return index or -1 if not found
```

**Expected Output:**
```
Element 23 found at index: 5
```

---

### Exercise 3.2: Selection Sort
**Task:** Implement selection sort algorithm.

```java
// int[] arr = {64, 25, 12, 22, 11};
// Sort in ascending order using selection sort
// Print array after each pass
```

**Expected Output:**
```
Pass 1: 11 25 12 22 64
Pass 2: 11 12 25 22 64
Pass 3: 11 12 22 25 64
Pass 4: 11 12 22 25 64
Sorted: 11 12 22 25 64
```

---

### Exercise 3.3: String Compression
**Task:** Run-length encoding compression.

```java
// compress("aaabbc") -> "a3b2c1"
// compress("abcd") -> "abcd" (return original if not smaller)
```

**Expected Output:**
```
aaabbc -> a3b2c1
abcd -> abcd
aaaaaaaaaa -> a10
```

---

### Exercise 3.4: Recursive Fibonacci
**Task:** Implement Fibonacci with and without memoization.

```java
// fib(10) = 55
// Compare recursive vs memoized performance
```

**Expected Output:**
```
Recursive fib(10): 55 (time: X ms)
Memoized fib(10): 55 (time: Y ms)
Memoized fib(40): 102334155
```

---

### Exercise 3.5: 2D Array Operations
**Task:** Matrix transpose and multiplication.

```java
// int[][] a = {{1,2}, {3,4}};
// int[][] b = {{5,6}, {7,8}};
// Transpose a, multiply a * b
```

**Expected Output:**
```
Matrix A:
1 2
3 4

Transpose of A:
1 3
2 4

A * B:
19 22
43 50
```

---

## 📋 Submission Guidelines

1. Create a Java class for each exercise in `exercises/` folder
2. Name format: `Exercise{number}.java` (e.g., `Exercise1_1.java`)
3. Include `main` method with test cases
4. Follow Google Java Style
5. Add Javadoc comments
6. Run tests before submitting

---

## 💡 Hints

| Exercise | Hint |
|----------|------|
| 1.2 | Watch integer division: use `9.0/5.0` or `celsius * 9 / 5 + 32` |
| 2.1 | Initialize min/max with first element, not 0 |
| 2.2 | Use `toCharArray()` or loop backwards |
| 2.3 | Check 15 (3×5) first, then 3, then 5 |
| 2.4 | Clean string first: `s.replaceAll("\\s", "").toLowerCase()` |
| 2.5 | Check divisibility up to √n |
| 3.1 | Use `left + (right - left) / 2` to avoid overflow |
| 3.3 | Track count, append when char changes |
| 3.4 | Use `Map<Integer, Long>` for memoization |
| 3.5 | Transpose: `result[j][i] = matrix[i][j]` |

---