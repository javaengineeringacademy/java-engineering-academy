package academy.javaengineering.exercises;

import java.util.Arrays;

/**
 * Exercises: Methods (Parameters, Return Values, Overloading)
 *
 * Complete the TODO sections below.
 */
public class MethodExercises {

    // TODO 1: Implement method overloading
    // Create overloaded methods called 'add' that handle:
    // - add(int, int) returns int
    // - add(double, double) returns double
    // - add(int, int, int) returns int
    // - add(String, String) returns String (concatenation with space)
    public int add(int a, int b) {
        // TODO: implement this
        return 0;
    }

    public double add(double a, double b) {
        // TODO: implement this
        return 0.0;
    }

    public int add(int a, int b, int c) {
        // TODO: implement this
        return 0;
    }

    public String add(String a, String b) {
        // TODO: implement this
        return "";
    }

    // TODO 2: Reverse a string using recursion
    public String reverseString(String str) {
        // TODO: implement this recursively
        return "";
    }

    // TODO 3: Calculate factorial using recursion
    // For n <= 1, return 1
    public long factorial(int n) {
        // TODO: implement this recursively
        return 0;
    }

    // TODO 4: Implement a method that returns a variable number of arguments
    // Takes a separator and varargs of strings
    // Returns them joined by the separator
    // Example: join(", ", "a", "b", "c") returns "a, b, c"
    public String join(String separator, String... values) {
        // TODO: implement this
        return "";
    }

    // TODO 5: Implement a method that accepts a functional interface
    // Takes an array of ints and a transformation function
    // Returns a new array where each element has been transformed
    @FunctionalInterface
    interface IntTransformer {
        int transform(int value);
    }

    public int[] transformArray(int[] input, IntTransformer transformer) {
        // TODO: implement this
        return new int[0];
    }

    // TODO 6: Implement bubble sort as a static method
    // Sort the array in place and return it
    public static int[] bubbleSort(int[] arr) {
        // TODO: implement this
        return arr;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        MethodExercises exercises = new MethodExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== MethodExercises Tests ===\n");

        // Test 1 - Overloading
        total++;
        int intAdd = exercises.add(5, 3);
        double doubleAdd = exercises.add(2.5, 3.5);
        int tripleAdd = exercises.add(1, 2, 3);
        String strAdd = exercises.add("Hello", "World");
        if (intAdd == 8 && doubleAdd == 6.0 && tripleAdd == 6 && "Hello World".equals(strAdd)) {
            System.out.println("Test 1 PASSED: method overloading");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: method overloading");
        }

        // Test 2 - Reverse String
        total++;
        String r1 = exercises.reverseString("hello");
        String r2 = exercises.reverseString("a");
        String r3 = exercises.reverseString("");
        if ("olleh".equals(r1) && "a".equals(r2) && "".equals(r3)) {
            System.out.println("Test 2 PASSED: reverseString");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: reverseString - got '" + r1 + "'");
        }

        // Test 3 - Factorial
        total++;
        long f1 = exercises.factorial(5);
        long f2 = exercises.factorial(0);
        long f3 = exercises.factorial(1);
        if (f1 == 120 && f2 == 1 && f3 == 1) {
            System.out.println("Test 3 PASSED: factorial");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: factorial - expected [120, 1, 1], got [" + f1 + ", " + f2 + ", " + f3 + "]");
        }

        // Test 4 - Join
        total++;
        String j1 = exercises.join(", ", "a", "b", "c");
        String j2 = exercises.join(" - ", "one");
        String j3 = exercises.join("|");
        if ("a, b, c".equals(j1) && "one".equals(j2) && "".equals(j3)) {
            System.out.println("Test 4 PASSED: join");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: join - got '" + j1 + "'");
        }

        // Test 5 - Transform Array
        total++;
        int[] input = {1, 2, 3, 4, 5};
        int[] doubled = exercises.transformArray(input, x -> x * 2);
        int[] squared = exercises.transformArray(input, x -> x * x);
        if (Arrays.equals(doubled, new int[]{2, 4, 6, 8, 10})
            && Arrays.equals(squared, new int[]{1, 4, 9, 16, 25})) {
            System.out.println("Test 5 PASSED: transformArray");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: transformArray");
        }

        // Test 6 - Bubble Sort
        total++;
        int[] sorted1 = MethodExercises.bubbleSort(new int[]{5, 3, 8, 1, 2});
        int[] sorted2 = MethodExercises.bubbleSort(new int[]{1});
        int[] sorted3 = MethodExercises.bubbleSort(new int[]{3, 3, 3});
        if (Arrays.equals(sorted1, new int[]{1, 2, 3, 5, 8})
            && Arrays.equals(sorted2, new int[]{1})
            && Arrays.equals(sorted3, new int[]{3, 3, 3})) {
            System.out.println("Test 6 PASSED: bubbleSort");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: bubbleSort");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
