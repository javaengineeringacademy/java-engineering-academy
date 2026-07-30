package com.javaacademy.sprint1.methods;

/**
 * RecursionDemo - Demonstrates recursive methods and recursion vs iteration.
 * 
 * <p><b>Recursion:</b> A method that calls itself.
 * <ul>
 *   <li><b>Base case:</b> Condition to stop recursion (prevents infinite loop)</li>
 *   <li><b>Recursive case:</b> Calls itself with smaller/simpler input</li>
 *   <li>Each call creates new stack frame</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> Like Russian dolls - each doll contains a smaller doll,
 * until the smallest doll (base case) which contains nothing.
 * 
 * <p><b>When to Use:</b>
 * <ul>
 *   <li>Tree/graph traversal (natural fit)</li>
 *   <li>Divide and conquer algorithms</li>
 *   <li>Problems with recursive structure</li>
 * </ul>
 * 
 * <p><b>When to Avoid:</b>
 * <ul>
 *   <li>Simple iteration works (factorial, fibonacci)</li>
 *   <li>Deep recursion (stack overflow risk)</li>
 *   <li>Performance-critical code</li>
 * </ul>
 * 
 * <p><b>Tail Recursion:</b> Recursive call is LAST operation.
 * Java does NOT optimize tail recursion (unlike Scala/Kotlin).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class RecursionDemo {

    private RecursionDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Recursion Demo ===\n");

        // Factorial
        System.out.println("--- Factorial ---");
        System.out.println("factorial(5) = " + factorial(5));       // 120
        System.out.println("factorial(0) = " + factorial(0));       // 1
        System.out.println("factorialIterative(5) = " + factorialIterative(5)); // 120

        // Fibonacci
        System.out.println("\n--- Fibonacci ---");
        for (int i = 0; i < 10; i++) {
            System.out.print(fibonacci(i) + " "); // 0 1 1 2 3 5 8 13 21 34
        }
        System.out.println();
        
        // Fibonacci with memoization
        System.out.println("\n--- Fibonacci (Memoized) ---");
        for (int i = 0; i < 10; i++) {
            System.out.print(fibonacciMemo(i) + " ");
        }
        System.out.println();

        // Power
        System.out.println("\n--- Power ---");
        System.out.println("power(2, 10) = " + power(2, 10)); // 1024
        System.out.println("power(3, 4) = " + power(3, 4));   // 81

        // GCD (Greatest Common Divisor) - Euclidean algorithm
        System.out.println("\n--- GCD ---");
        System.out.println("gcd(48, 18) = " + gcd(48, 18));     // 6
        System.out.println("gcd(101, 10) = " + gcd(101, 10));   // 1

        // Binary Search (recursive)
        System.out.println("\n--- Binary Search (Recursive) ---");
        int[] sorted = {1, 3, 5, 7, 9, 11, 13, 15};
        System.out.println("Search 7: index " + binarySearch(sorted, 7, 0, sorted.length - 1)); // 3
        System.out.println("Search 6: index " + binarySearch(sorted, 6, 0, sorted.length - 1)); // -1

        // String Reversal
        System.out.println("\n--- String Reverse ---");
        System.out.println("reverse('hello') = " + reverse("hello")); // olleh

        // Tower of Hanoi
        System.out.println("\n--- Tower of Hanoi (3 disks) ---");
        hanoi(3, 'A', 'C', 'B');

        // Stack overflow demo (commented out)
        // System.out.println("\n--- Stack Overflow ---");
        // infiniteRecursion(); // StackOverflowError!

        // Expected output demonstrates recursion patterns
    }

    // Factorial: n! = n * (n-1)!; 0! = 1
    static long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative not allowed");
        if (n <= 1) return 1; // Base case
        return n * factorial(n - 1); // Recursive case
    }

    // Iterative factorial (more efficient)
    static long factorialIterative(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }

    // Fibonacci: F(n) = F(n-1) + F(n-2); F(0)=0, F(1)=1
    static long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2); // Exponential time!
    }

    // Memoized Fibonacci: O(n) time, O(n) space
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

    // Power: x^n = x * x^(n-1)
    static long power(int base, int exp) {
        if (exp < 0) throw new IllegalArgumentException("Negative exponent");
        if (exp == 0) return 1;
        return base * power(base, exp - 1);
    }

    // GCD: Euclidean algorithm
    static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    // Binary Search recursive
    static int binarySearch(int[] arr, int target, int left, int right) {
        if (left > right) return -1; // Not found
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        if (arr[mid] > target) return binarySearch(arr, target, left, mid - 1);
        return binarySearch(arr, target, mid + 1, right);
    }

    // String reverse
    static String reverse(String s) {
        if (s == null || s.length() <= 1) return s;
        return reverse(s.substring(1)) + s.charAt(0);
    }

    // Tower of Hanoi
    static void hanoi(int n, char from, char to, char aux) {
        if (n == 1) {
            System.out.println("Move disk 1 from " + from + " to " + to);
            return;
        }
        hanoi(n - 1, from, aux, to);
        System.out.println("Move disk " + n + " from " + from + " to " + to);
        hanoi(n - 1, aux, to, from);
    }

    // Stack overflow example (don't run!)
    static void infiniteRecursion() {
        infiniteRecursion(); // No base case!
    }
}