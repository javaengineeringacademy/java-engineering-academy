package academy.javaengineering.fundamentals;

/**
 * Methods in Java
 *
 * This file covers:
 * - Method declaration and syntax
 * - Parameters and return types
 * - Method overloading
 * - Variable arguments (varargs)
 * - Static methods
 * - Recursive methods
 */
public class Methods {

    public static void main(String[] args) {

        // =========================================================
        // 1. CALLING METHODS
        // =========================================================
        System.out.println("=== Calling Methods ===");

        // Calling methods defined in this class
        greet("Alice");
        greet("Bob", "Good morning");

        int result = add(5, 3);
        System.out.println("5 + 3 = " + result);

        // =========================================================
        // 2. METHOD OVERLOADING
        // =========================================================
        System.out.println("\n=== Method Overloading ===");

        // Same method name, different parameters
        System.out.println("add(5, 3)           = " + add(5, 3));
        System.out.println("add(5.5, 3.3)       = " + add(5.5, 3.3));
        System.out.println("add(1, 2, 3)        = " + add(1, 2, 3));
        System.out.println("add(1,2,3,4,5)      = " + add(1, 2, 3, 4, 5));

        // =========================================================
        // 3. RETURN TYPES
        // =========================================================
        System.out.println("\n=== Return Types ===");

        // void method (no return value)
        printSeparator('-', 40);

        // Returning different types
        int maxVal = getMax(10, 20);
        System.out.println("Max of 10 and 20: " + maxVal);

        double avgVal = getAverage(10, 20, 30);
        System.out.println("Average of 10, 20, 30: " + avgVal);

        boolean isEven = checkEven(7);
        System.out.println("7 is even: " + isEven);

        // Returning arrays
        int[] range = getRange(1, 5);
        System.out.print("Range 1-5: ");
        for (int num : range) {
            System.out.print(num + " ");
        }
        System.out.println();

        // =========================================================
        // 4. STATIC METHODS
        // =========================================================
        System.out.println("\n=== Static Methods ===");

        // Static methods belong to the class, not instances
        // Can be called without creating an object
        System.out.println("Math.abs(-10)    = " + Math.abs(-10));
        System.out.println("Math.max(5, 10)  = " + Math.max(5, 10));
        System.out.println("Math.sqrt(144)   = " + Math.sqrt(144));
        System.out.println("Math.PI          = " + Math.PI);

        // Calling our static methods
        System.out.println("factorial(10)    = " + factorial(10));
        System.out.println("isPrime(17)      = " + isPrime(17));

        // =========================================================
        // 5. VARIABLE ARGUMENTS (VARARGS)
        // =========================================================
        System.out.println("\n=== Variable Arguments (Varargs) ===");

        // Varargs allow passing variable number of arguments
        System.out.println("Sum of 1,2,3:       " + sum(1, 2, 3));
        System.out.println("Sum of 1,2,3,4,5:   " + sum(1, 2, 3, 4, 5));
        System.out.println("Sum of no args:     " + sum());

        // Varargs with other parameters
        printItems("Fruits:", "Apple", "Banana", "Cherry");
        printItems("Numbers:", 1, 2, 3, 4, 5);

        // =========================================================
        // 6. RECURSIVE METHODS
        // =========================================================
        System.out.println("\n=== Recursive Methods ===");

        // Factorial using recursion
        System.out.println("factorial(10) recursive = " + factorialRecursive(10));

        // Fibonacci using recursion
        System.out.print("Fibonacci(10) recursive: ");
        for (int i = 0; i < 10; i++) {
            System.out.print(fibonacciRecursive(i) + " ");
        }
        System.out.println();

        // Power function
        System.out.println("power(2, 10) = " + power(2, 10));

        // Binary search (recursive)
        int[] sortedArray = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
        int searchIndex = binarySearch(sortedArray, 23, 0, sortedArray.length - 1);
        System.out.println("Found 23 at index: " + searchIndex);

        // String reversal
        System.out.println("Reverse 'Hello': " + reverseString("Hello"));

        // Sum of digits
        System.out.println("Sum of digits of 12345: " + sumOfDigits(12345));

        // =========================================================
        // 7. PASS BY VALUE
        // =========================================================
        System.out.println("\n=== Pass By Value ===");

        // Java is always pass-by-value
        // For primitives: copy of value is passed
        // For objects: copy of reference is passed (not the object itself)

        int x = 10;
        System.out.println("Before modifyPrimitive: x = " + x);
        modifyPrimitive(x);
        System.out.println("After modifyPrimitive:  x = " + x); // Still 10

        int[] arr = {1, 2, 3};
        System.out.println("\nBefore modifyArray: arr[0] = " + arr[0]);
        modifyArray(arr);
        System.out.println("After modifyArray:  arr[0] = " + arr[0]); // Changed to 99

        System.out.println("\n=== Methods Demo Complete ===");
    }

    // =========================================================
    // METHOD DECLARATIONS
    // =========================================================

    // Method 1: Simple void method with one parameter
    // Access modifier | return type | method name | parameters
    public static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }

    // Method 2: Overloaded version with two parameters
    public static void greet(String name, String greeting) {
        System.out.println(greeting + ", " + name + "!");
    }

    // Method 3: Method with return value
    public static int add(int a, int b) {
        return a + b;
    }

    // Method 4: Overloaded add with doubles
    public static double add(double a, double b) {
        return a + b;
    }

    // Method 5: Overloaded add with varargs
    public static int add(int a, int b, int c) {
        return a + b + c;
    }

    // Method 6: Varargs version
    public static int add(int... numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }

    // Method 7: Get maximum of two integers
    public static int getMax(int a, int b) {
        return (a > b) ? a : b;
    }

    // Method 8: Calculate average
    public static double getAverage(int... numbers) {
        if (numbers.length == 0) return 0;
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return (double) sum / numbers.length;
    }

    // Method 9: Check if number is even
    public static boolean checkEven(int number) {
        return number % 2 == 0;
    }

    // Method 10: Return an array
    public static int[] getRange(int start, int end) {
        int[] range = new int[end - start + 1];
        for (int i = 0; i < range.length; i++) {
            range[i] = start + i;
        }
        return range;
    }

    // Method 11: Print separator line
    public static void printSeparator(char ch, int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(ch);
        }
        System.out.println();
    }

    // Method 12: Varargs - sum of numbers
    public static int sum(int... numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }

    // Method 13: Varargs with other parameters
    public static void printItems(String label, String... items) {
        System.out.print(label + " ");
        for (String item : items) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    // Method 14: Varargs with other parameter types
    public static void printItems(String label, int... numbers) {
        System.out.print(label + " ");
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    // =========================================================
    // STATIC METHODS
    // =========================================================

    // Method 15: Factorial (iterative)
    public static long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be non-negative");
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // Method 16: Check prime number
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // =========================================================
    // RECURSIVE METHODS
    // =========================================================

    // Method 17: Factorial (recursive)
    public static long factorialRecursive(int n) {
        if (n < 0) throw new IllegalArgumentException("n must be non-negative");
        if (n <= 1) return 1;  // Base case
        return n * factorialRecursive(n - 1);  // Recursive case
    }

    // Method 18: Fibonacci (recursive)
    public static int fibonacciRecursive(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }

    // Method 19: Power function (recursive)
    public static long power(int base, int exponent) {
        if (exponent == 0) return 1;
        if (exponent < 0) return 1 / power(base, -exponent);
        return base * power(base, exponent - 1);
    }

    // Method 20: Binary search (recursive)
    public static int binarySearch(int[] arr, int target, int low, int high) {
        if (low > high) return -1;
        int mid = (low + high) / 2;
        if (arr[mid] == target) return mid;
        else if (arr[mid] < target) return binarySearch(arr, target, mid + 1, high);
        else return binarySearch(arr, target, low, mid - 1);
    }

    // Method 21: Reverse string (recursive)
    public static String reverseString(String str) {
        if (str.length() <= 1) return str;
        return reverseString(str.substring(1)) + str.charAt(0);
    }

    // Method 22: Sum of digits (recursive)
    public static int sumOfDigits(int n) {
        n = Math.abs(n);
        if (n < 10) return n;
        return (n % 10) + sumOfDigits(n / 10);
    }

    // =========================================================
    // PASS BY VALUE EXAMPLES
    // =========================================================

    // Method 23: Modifying primitive (doesn't affect original)
    public static void modifyPrimitive(int value) {
        value = 99;
        System.out.println("Inside method: value = " + value);
    }

    // Method 24: Modifying array (affects original)
    public static void modifyArray(int[] arr) {
        arr[0] = 99;
        System.out.println("Inside method: arr[0] = " + arr[0]);
    }
}
