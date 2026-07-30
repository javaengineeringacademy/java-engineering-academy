# Sprint 1 Solutions - Java Fundamentals

---

## ✅ Exercise 1.1: Hello Variables

```java
package com.javaacademy.sprint1.exercises;

public class Exercise1_1 {
    public static void main(String[] args) {
        byte b = 100;
        short s = 30000;
        int i = 2_000_000_000;
        long l = 9_000_000_000L;
        float f = 3.14f;
        double d = 3.14159;
        char c = 'A';
        boolean bool = true;

        System.out.println("byte: " + b);
        System.out.println("short: " + s);
        System.out.println("int: " + i);
        System.out.println("long: " + l);
        System.out.println("float: " + f);
        System.out.println("double: " + d);
        System.out.println("char: " + c);
        System.out.println("boolean: " + bool);
    }
}
```

**Explanation:** Each primitive type has a specific range and size. Note the `L` suffix for long and `f` suffix for float. Underscores in numeric literals (Java 7+) improve readability.

---

## ✅ Exercise 1.2: Temperature Converter

```java
package com.javaacademy.sprint1.exercises;

public class Exercise1_2 {
    public static void main(String[] args) {
        double celsius = 25.0;
        double fahrenheit = celsius * 9.0 / 5.0 + 32;
        System.out.printf("%.1f°C = %.1f°F%n", celsius, fahrenheit);
    }
}
```

**Explanation:** Using `9.0 / 5.0` ensures floating-point division. If we used `9/5`, integer division would give `1`, producing wrong result.

---

## ✅ Exercise 1.3: Even or Odd

```java
package com.javaacademy.sprint1.exercises;

public class Exercise1_3 {
    public static void main(String[] args) {
        int number = 42;
        if (number % 2 == 0) {
            System.out.println(number + " is even");
        } else {
            System.out.println(number + " is odd");
        }
    }
}
```

**Explanation:** Modulus operator `%` returns remainder. Even numbers have remainder 0 when divided by 2.

---

## ✅ Exercise 1.4: Grade Calculator

```java
package com.javaacademy.sprint1.exercises;

public class Exercise1_4 {
    public static void main(String[] args) {
        int score = 85;
        char grade;
        
        if (score >= 90) grade = 'A';
        else if (score >= 80) grade = 'B';
        else if (score >= 70) grade = 'C';
        else if (score >= 60) grade = 'D';
        else grade = 'F';
        
        System.out.println("Score: " + score + ", Grade: " + grade);
    }
}
```

**Explanation:** If-else-if ladder evaluates conditions in order. First match wins. Order matters: check highest first.

---

## ✅ Exercise 2.1: Array Statistics

```java
package com.javaacademy.sprint1.exercises;

public class Exercise2_1 {
    public static void main(String[] args) {
        int[] numbers = {15, 42, 8, 23, 16, 4, 91, 37};
        
        int min = numbers[0];
        int max = numbers[0];
        long sum = 0;
        
        for (int num : numbers) {
            if (num < min) min = num;
            if (num > max) max = num;
            sum += num;
        }
        
        double average = (double) sum / numbers.length;
        
        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
        System.out.println("Sum: " + sum);
        System.out.println("Average: " + average);
    }
}
```

**Explanation:** Initialize min/max with first element (not 0!) because array might have all negative or all positive numbers. Cast sum to double for accurate average.

---

## ✅ Exercise 2.2: String Reversal

```java
package com.javaacademy.sprint1.exercises;

public class Exercise2_2 {
    public static void main(String[] args) {
        String input = "Hello World";
        
        // Method 1: StringBuilder
        StringBuilder sb = new StringBuilder(input);
        String reversed1 = sb.reverse().toString();
        
        // Method 2: Manual (char array)
        char[] chars = input.toCharArray();
        for (int i = 0, j = chars.length - 1; i < j; i++, j--) {
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        String reversed2 = new String(chars);
        
        System.out.println("Original: " + input);
        System.out.println("Reversed (StringBuilder): " + reversed1);
        System.out.println("Reversed (Manual): " + reversed2);
    }
}
```

**Explanation:** Two approaches shown. StringBuilder is simpler; manual approach shows algorithm understanding.

---

## ✅ Exercise 2.3: FizzBuzz

```java
package com.javaacademy.sprint1.exercises;

public class Exercise2_3 {
    public static void main(String[] args) {
        for (int i = 1; i <= 20; i++) {
            if (i % 15 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }
    }
}
```

**Explanation:** Check 15 (LCM of 3 and 5) FIRST. If we check 3 first, 15 would print "Fizz" instead of "FizzBuzz".

---

## ✅ Exercise 2.4: Palindrome Checker

```java
package com.javaacademy.sprint1.exercises;

public class Exercise2_4 {
    public static void main(String[] args) {
        String[] tests = {"racecar", "A man a plan a canal Panama", "hello"};
        
        for (String test : tests) {
            String cleaned = test.replaceAll("\\s+", "").toLowerCase();
            boolean isPalindrome = isPalindrome(cleaned);
            System.out.println(test + ": " + (isPalindrome ? "Palindrome" : "Not palindrome"));
        }
    }
    
    static boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left++) != s.charAt(right--)) return false;
        }
        return true;
    }
}
```

**Explanation:** Clean string first (remove spaces, lowercase). Two-pointer approach is O(n) time, O(1) space.

---

## ✅ Exercise 2.5: Prime Number Finder

```java
package com.javaacademy.sprint1.exercises;

public class Exercise2_5 {
    public static void main(String[] args) {
        int limit = 30;
        System.out.print("Primes up to " + limit + ": ");
        
        for (int i = 2; i <= limit; i++) {
            if (isPrime(i)) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
    
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
```

**Explanation:** Only check divisibility up to √n. If n = a×b and both > √n, then a×b > n (contradiction).

---

## ✅ Exercise 3.1: Binary Search

```java
package com.javaacademy.sprint1.exercises;

public class Exercise3_1 {
    public static void main(String[] args) {
        int[] sorted = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
        int target = 23;
        
        int index = binarySearch(sorted, target);
        if (index >= 0) {
            System.out.println("Element " + target + " found at index: " + index);
        } else {
            System.out.println("Element " + target + " not found");
        }
    }
    
    static int binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // Avoids overflow
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }
}
```

**Explanation:** `left + (right - left) / 2` prevents overflow that `(left + right) / 2` could cause with large arrays.

---

## ✅ Exercise 3.2: Selection Sort

```java
package com.javaacademy.sprint1.exercises;

import java.util.Arrays;

public class Exercise3_2 {
    public static void main(String[] args) {
        int[] arr = {64, 25, 12, 22, 11};
        selectionSort(arr);
    }
    
    static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIdx]) minIdx = j;
            }
            if (minIdx != i) {
                int temp = arr[i];
                arr[i] = arr[minIdx];
                arr[minIdx] = temp;
            }
            System.out.println("Pass " + (i + 1) + ": " + Arrays.toString(arr));
        }
        System.out.println("Sorted: " + Arrays.toString(arr));
    }
}
```

**Explanation:** Find minimum in unsorted portion, swap with first unsorted element. O(n²) time, O(1) space, not stable.

---

## ✅ Exercise 3.3: String Compression

```java
package com.javaacademy.sprint1.exercises;

public class Exercise3_3 {
    public static void main(String[] args) {
        String[] tests = {"aaabbc", "abcd", "aaaaaaaaaa"};
        for (String s : tests) {
            System.out.println(s + " -> " + compress(s));
        }
    }
    
    static String compress(String s) {
        if (s == null || s.isEmpty()) return s;
        
        StringBuilder sb = new StringBuilder();
        char current = s.charAt(0);
        int count = 1;
        
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == current) {
                count++;
            } else {
                sb.append(current).append(count);
                current = s.charAt(i);
                count = 1;
            }
        }
        sb.append(current).append(count);
        
        return sb.length() < s.length() ? sb.toString() : s;
    }
}
```

**Explanation:** Track current character and count. When character changes, append to result. Return original if compressed isn't smaller.

---

## ✅ Exercise 3.4: Recursive Fibonacci

```java
package com.javaacademy.sprint1.exercises;

public class Exercise3_4 {
    public static void main(String[] args) {
        int n = 10;
        
        // Recursive
        long start = System.nanoTime();
        long fib1 = fibonacci(n);
        long time1 = System.nanoTime() - start;
        
        // Memoized
        start = System.nanoTime();
        long fib2 = fibonacciMemo(n);
        long time2 = System.nanoTime() - start;
        
        System.out.println("Recursive fib(" + n + "): " + fib1 + " (time: " + time1/1_000_000 + " ms)");
        System.out.println("Memoized fib(" + n + "): " + fib2 + " (time: " + time2/1_000_000 + " ms)");
        
        // Larger test
        System.out.println("Memoized fib(40): " + fibonacciMemo(40));
    }
    
    static long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    
    static long fibonacciMemo(int n) {
        long[] memo = new long[n + 1];
        java.util.Arrays.fill(memo, -1);
        return fibHelper(n, memo);
    }
    
    static long fibHelper(int n, long[] memo) {
        if (n <= 1) return n;
        if (memo[n] != -1) return memo[n];
        memo[n] = fibHelper(n - 1, memo) + fibHelper(n - 2, memo);
        return memo[n];
    }
}
```

**Explanation:** Naive recursion is exponential O(2^n). Memoization reduces to O(n) by caching results.

---

## ✅ Exercise 3.5: 2D Array Operations

```java
package com.javaacademy.sprint1.exercises;

import java.util.Arrays;

public class Exercise3_5 {
    public static void main(String[] args) {
        int[][] a = {{1, 2}, {3, 4}};
        int[][] b = {{5, 6}, {7, 8}};
        
        System.out.println("Matrix A:");
        printMatrix(a);
        
        int[][] transpose = transpose(a);
        System.out.println("\nTranspose of A:");
        printMatrix(transpose);
        
        int[][] product = multiply(a, b);
        System.out.println("\nA * B:");
        printMatrix(product);
    }
    
    static int[][] transpose(int[][] m) {
        int rows = m.length, cols = m[0].length;
        int[][] result = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = m[i][j];
            }
        }
        return result;
    }
    
    static int[][] multiply(int[][] a, int[][] b) {
        int rowsA = a.length, colsA = a[0].length, colsB = b[0].length;
        int[][] result = new int[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }
    
    static void printMatrix(int[][] m) {
        for (int[] row : m) System.out.println(Arrays.toString(row));
    }
}
```

**Explanation:** Transpose swaps rows/columns. Multiplication: result[i][j] = sum of a[i][k] × b[k][j].

---

## 📝 Common Mistakes to Avoid

| Mistake | Correct |
|---------|---------|
| `int avg = sum / count;` | `double avg = (double) sum / count;` |
| `if (a = 5)` | `if (a == 5)` |
| `for (int i = 0; i <= arr.length; i++)` | `i < arr.length` |
| `String s = "a" + "b" + "c";` in loop | Use `StringBuilder` |
| `arr == arr2` for arrays | `Arrays.equals(arr, arr2)` |

---