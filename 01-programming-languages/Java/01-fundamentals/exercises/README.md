# Fundamentals Exercises

Practice exercises covering Java fundamentals: variables, operators, control flow, methods, arrays, and strings.

---

## Variables (3 Exercises)

### Exercise 1: Type Conversion

**Problem:** Write a program that demonstrates all types of type conversion in Java. Create variables of different types and convert between them, showing both implicit (widening) and explicit (narrowing) conversions.

**Expected Input/Output:**
```
int -> long: 100 -> 100
long -> int: 100000L -> 100000
double -> int: 3.14 -> 3
char -> int: 'A' -> 65
int -> String: 42 -> "42"
String -> int: "100" -> 100
```

**Hint:** Use casting for narrowing conversions and `Integer.parseInt()` or `String.valueOf()` for String conversions.

**Solution Reference:** `VariablesExercises.java` — method `typeConversionDemo()`

---

### Exercise 2: Variable Naming Conventions

**Problem:** Given a list of poorly named variables, refactor them to follow Java naming conventions. Write the corrected declarations with proper camelCase, meaningful names, and appropriate types.

**Expected Input/Output:**
```
Original:              Refactored:
int x = 5;          -> int studentCount = 5;
String s = "hello"; -> String greeting = "hello";
boolean flag = true;-> boolean isEligible = true;
int MAX = 100;      -> static final int MAX_VALUE = 100;
```

**Hint:** Instance variables use camelCase, constants use UPPER_SNAKE_CASE, classes use PascalCase.

**Solution Reference:** `VariablesExercises.java` — method `namingConventions()`

---

### Exercise 3: Constants and Final Variables

**Problem:** Create a program that defines constants for a circle (PI, radius) and calculates area and circumference. Use `final` keyword appropriately and demonstrate that constants cannot be reassigned.

**Expected Input/Output:**
```
Circle radius: 5.0
PI: 3.141592653589793
Area: 78.53981633974483
Circumference: 31.41592653589793
Attempting to reassign PI... Compilation error!
```

**Hint:** Declare constants as `static final` at the class level.

**Solution Reference:** `VariablesExercises.java` — method `circleConstants()`

---

## Operators (3 Exercises)

### Exercise 4: Simple Calculator

**Problem:** Build a calculator that takes two numbers and an operator (+, -, *, /, %) and performs the operation. Handle division by zero and invalid operators gracefully.

**Expected Input/Output:**
```
10 + 5 = 15
10 - 5 = 5
10 * 5 = 50
10 / 5 = 2
10 % 3 = 1
10 / 0 = Error: Division by zero
10 ^ 5 = Error: Invalid operator
```

**Hint:** Use a switch statement to handle different operators.

**Solution Reference:** `OperatorsExercises.java` — method `calculate()`

---

### Exercise 5: Parity Check

**Problem:** Write a method that determines if a number is even or odd without using the modulo operator. Use bitwise AND operator instead.

**Expected Input/Output:**
```
isEven(4) -> true
isEven(7) -> false
isEven(0) -> true
isEven(-2) -> true
```

**Hint:** The last bit of an even number is always 0. Use `num & 1`.

**Solution Reference:** `OperatorsExercises.java` — method `isEven()`

---

### Exercise 6: Bitwise Operations

**Problem:** Implement methods that perform the following using only bitwise operators: swap two numbers without a temporary variable, check if a number is a power of 2, and toggle the nth bit.

**Expected Input/Output:**
```
swap(5, 3) -> a=3, b=5
isPowerOfTwo(16) -> true
isPowerOfTwo(18) -> false
toggleBit(10, 1) -> 8 (binary: 1010 -> 1000)
```

**Hint:** For swap use XOR (`a ^= b; b ^= a; a ^= b;`). For power of 2 check `n > 0 && (n & (n-1)) == 0`.

**Solution Reference:** `OperatorsExercises.java` — methods `swap()`, `isPowerOfTwo()`, `toggleBit()`

---

## Control Flow (4 Exercises)

### Exercise 7: FizzBuzz

**Problem:** Print numbers from 1 to 100. For multiples of 3 print "Fizz", for multiples of 5 print "Buzz", for multiples of both print "FizzBuzz", otherwise print the number.

**Expected Input/Output:**
```
1, 2, Fizz, 4, Buzz, Fizz, 7, 8, Fizz, Buzz, 11, Fizz, 13, 14, FizzBuzz, 16...
```

**Hint:** Use the modulo operator. Check `i % 15 == 0` first for FizzBuzz.

**Solution Reference:** `ControlFlowExercises.java` — method `fizzBuzz()`

---

### Exercise 8: Prime Number Checker

**Problem:** Write a method that checks if a given number is prime. Then extend it to print all primes up to a given limit using the Sieve of Eratosthenes.

**Expected Input/Output:**
```
isPrime(7) -> true
isPrime(10) -> false
primesUpTo(30) -> [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
```

**Hint:** For basic check, test divisibility from 2 to sqrt(n). For sieve, create a boolean array and mark multiples.

**Solution Reference:** `ControlFlowExercises.java` — methods `isPrime()`, `sieveOfEratosthenes()`

---

### Exercise 9: Pattern Printing

**Problem:** Print the following patterns using nested loops:

Pattern A (Right Triangle):
```
*
**
***
****
*****
```

Pattern B (Inverted Triangle):
```
*****
****
***
**
*
```

**Hint:** Use nested for loops. Outer loop controls rows, inner loop controls columns.

**Solution Reference:** `ControlFlowExercises.java` — methods `rightTriangle()`, `invertedTriangle()`

---

### Exercise 10: Diamond Shape

**Problem:** Print a diamond pattern of asterisks with a given height. The diamond should be symmetrical.

**Expected Input/Output (height=5):**
```
    *
   ***
  *****
 *******
*********
 *******
  *****
   ***
    *
```

**Hint:** Split into upper and lower halves. Calculate spaces and stars for each row.

**Solution Reference:** `ControlFlowExercises.java` — method `diamond()`

---

## Methods (3 Exercises)

### Exercise 11: Factorial

**Problem:** Implement factorial using both iteration and recursion. Compare the results and handle edge cases (negative numbers, zero).

**Expected Input/Output:**
```
factorial(5) -> 120
factorial(0) -> 1
factorial(-1) -> Error: Number must be non-negative
recursiveFactorial(5) -> 120
```

**Hint:** Base case for recursion is `n <= 1` returns 1. Recursive case is `n * factorial(n-1)`.

**Solution Reference:** `MethodsExercises.java` — methods `factorial()`, `recursiveFactorial()`

---

### Exercise 12: Palindrome Checker

**Problem:** Write a method that checks if a given string is a palindrome (reads the same forwards and backwards). Ignore case and non-alphanumeric characters.

**Expected Input/Output:**
```
isPalindrome("racecar") -> true
isPalindrome("A man a plan a canal Panama") -> true
isPalindrome("hello") -> false
isPalindrome("Was it a car or a cat I saw") -> true
```

**Hint:** Remove non-alphanumeric characters, convert to lowercase, then compare with its reverse.

**Solution Reference:** `MethodsExercises.java` — method `isPalindrome()`

---

### Exercise 13: Array Rotation

**Problem:** Write methods to rotate an array left and right by a given number of positions. Handle edge cases like empty arrays and rotation greater than array length.

**Expected Input/Output:**
```
rotateLeft([1,2,3,4,5], 2) -> [3,4,5,1,2]
rotateRight([1,2,3,4,5], 2) -> [4,5,1,2,3]
rotateLeft([1,2,3], 5) -> [3,1,2]  // 5 % 3 = 2
```

**Hint:** Use the formula: `newIndex = (oldIndex + rotation) % length` for left rotation.

**Solution Reference:** `MethodsExercises.java` — methods `rotateLeft()`, `rotateRight()`

---

## Arrays (3 Exercises)

### Exercise 14: Matrix Transpose

**Problem:** Write a method that transposes a given 2D matrix (swaps rows and columns).

**Expected Input/Output:**
```
Input:
1 2 3
4 5 6

Output:
1 4
2 5
3 6
```

**Hint:** Create a new matrix with swapped dimensions. `transposed[j][i] = original[i][j]`.

**Solution Reference:** `ArraysExercises.java` — method `transpose()`

---

### Exercise 15: Array Rotation (In-Place)

**Problem:** Rotate an array in-place using the reversal algorithm. This should use O(1) extra space.

**Expected Input/Output:**
```
Input: [1, 2, 3, 4, 5, 6, 7], k = 3
Output: [5, 6, 7, 1, 2, 3, 4]
```

**Hint:** Reverse the first k elements, reverse the rest, then reverse the entire array.

**Solution Reference:** `ArraysExercises.java` — method `rotateInPlace()`

---

### Exercise 16: Find Missing Number

**Problem:** Given an array containing n distinct numbers from 0 to n, find the one that is missing.

**Expected Input/Output:**
```
findMissing([3, 0, 1]) -> 2
findMissing([0, 1]) -> 2
findMissing([9,6,4,2,3,5,7,0,1]) -> 8
```

**Hint:** Use the sum formula: `expectedSum = n * (n + 1) / 2`, then subtract actual sum.

**Solution Reference:** `ArraysExercises.java` — method `findMissingNumber()`

---

## Strings (4 Exercises)

### Exercise 17: Reverse Words in String

**Problem:** Write a method that reverses the order of words in a string. Handle multiple spaces and leading/trailing spaces.

**Expected Input/Output:**
```
reverseWords("the sky is blue") -> "blue is sky the"
reverseWords("  hello world  ") -> "world hello"
reverseWords("a good   example") -> "example good a"
```

**Hint:** Split the string by spaces, filter empty strings, then reverse the array.

**Solution Reference:** `StringsExercises.java` — method `reverseWords()`

---

### Exercise 18: Anagram Checker

**Problem:** Write a method that checks if two strings are anagrams of each other (contain the same characters in different order). Ignore case and spaces.

**Expected Input/Output:**
```
isAnagram("listen", "silent") -> true
isAnagram("Dormitory", "Dirty Room") -> true
isAnagram("hello", "world") -> false
```

**Hint:** Sort both strings after removing spaces and converting to lowercase, then compare.

**Solution Reference:** `StringsExercises.java` — method `isAnagram()`

---

### Exercise 19: String Compression

**Problem:** Implement basic string compression using counts of repeated characters. If the compressed string is not smaller than the original, return the original.

**Expected Input/Output:**
```
compress("aabcccccaaa") -> "a2b1c5a3"
compress("abcd") -> "abcd"
compress("aabb") -> "aabb"
```

**Hint:** Use StringBuilder. Iterate through the string counting consecutive characters.

**Solution Reference:** `StringsExercises.java` — method `compress()`

---

### Exercise 20: First Non-Repeating Character

**Problem:** Find the first non-repeating character in a string and return its index. Return -1 if none exists.

**Expected Input/Output:**
```
firstNonRepeating("leetcode") -> 0 ('l')
firstNonRepeating("loveleetcode") -> 2 ('v')
firstNonRepeating("aabb") -> -1
```

**Hint:** Use a LinkedHashMap to store character counts while preserving insertion order.

**Solution Reference:** `StringsExercises.java` — method `firstNonRepeatingChar()`

---

## Solutions

All solutions are provided in Java files within the `solutions/` directory. Each solution file corresponds to a topic and contains implementations for all exercises in that topic.

To run any solution:
```bash
cd solutions/
javac TopicExercises.java
java TopicExercises
```
