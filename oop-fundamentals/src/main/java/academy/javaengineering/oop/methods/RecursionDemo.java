package academy.javaengineering.oop.methods;

/**
 * Demonstrates recursion.
 */
public final class RecursionDemo {

    public static void main(String[] args) {
        System.out.println("Factorial of 5: " + factorial(5)); // 120
        System.out.println("Fibonacci(10): " + fibonacci(10)); // 55

        // Memoized fibonacci
        System.out.println("Fibonacci memoized(40): " + fibonacciMemo(40)); // 102334155
    }

    // Factorial: n! = n * (n-1)!
    static long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative not allowed");
        if (n <= 1) return 1; // Base case
        return n * factorial(n - 1); // Recursive case
    }

    // Iterative version (more efficient)
    static long factorialIterative(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }

    // Fibonacci: F(n) = F(n-1) + F(n-2)
    static long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    // Memoized Fibonacci: O(n) instead of O(2^n)
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

    // GCD using Euclidean algorithm
    static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    // Binary Search (recursive)
    static int binarySearch(int[] arr, int target, int left, int right) {
        if (left > right) return -1;
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
}