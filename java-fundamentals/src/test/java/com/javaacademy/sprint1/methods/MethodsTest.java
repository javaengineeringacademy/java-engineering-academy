package com.javaacademy.sprint1.methods;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MethodBasicsTest {

    @Test
    void testStaticMethods() {
        assertEquals("Hello, Alice!", greet("Alice"));
        assertEquals(30, add(10, 20));
    }

    @Test
    void testPassByValuePrimitive() {
        int primitive = 10;
        modifyPrimitive(primitive);
        assertEquals(10, primitive);  // Unchanged!
    }

    @Test
    void testPassByValueReference() {
        StringBuilder ref = new StringBuilder("Original");
        modifyReference(ref);
        assertEquals("Original - Modified", ref.toString());  // Changed!
    }

    @Test
    void testVoidMethod() {
        assertDoesNotThrow(() -> printSeparator());
    }

    // Methods under test
    static String greet(String name) { return "Hello, " + name + "!"; }
    static int add(int a, int b) { return a + b; }
    static void modifyPrimitive(int value) { value = 20; }
    static void modifyReference(StringBuilder sb) { sb.append(" - Modified"); }
    static void printSeparator() { System.out.println("===="); }
}

class OverloadingTest {

    @Test
    void testOverloadedByCount() {
        Calculator calc = new Calculator();
        assertEquals(30, calc.add(10, 20));
        assertEquals(60, calc.add(10, 20, 30));
    }

    @Test
    void testOverloadedByType() {
        Calculator calc = new Calculator();
        assertEquals(30, calc.add(10, 20));
        assertEquals(30.8, calc.add(10.5, 20.3), 0.001);
        assertEquals(131, calc.add('A', 'B'));  // 65 + 66
    }

    @Test
    void testOverloadedByOrder() {
        Calculator calc = new Calculator();
        assertDoesNotThrow(() -> calc.print(10, "Hi"));
        assertDoesNotThrow(() -> calc.print("Hi", 10));
    }

    @Test
    void testVarargsOverloading() {
        Calculator calc = new Calculator();
        assertEquals(0, calc.sum());
        assertEquals(1, calc.sum(1));
        assertEquals(6, calc.sum(1, 2, 3));
        assertEquals(6, calc.sum(new int[]{1, 2, 3}));
    }

    @Test
    void testConstructorOverloading() {
        Person p1 = new Person();
        Person p2 = new Person("Alice");
        Person p3 = new Person("Bob", 30);
        
        assertEquals("Unknown", p1.name);
        assertEquals(0, p1.age);
        assertEquals("Alice", p2.name);
        assertEquals(0, p2.age);
        assertEquals("Bob", p3.name);
        assertEquals(30, p3.age);
    }
}

class VarargsTest {

    @Test
    void testVarargsBasic() {
        assertEquals("Numbers: 1 2 3 (count: 3)", captureOutput(() -> printNumbers(1, 2, 3)));
        assertEquals("Numbers: 10 (count: 1)", captureOutput(() -> printNumbers(10)));
        assertEquals("Numbers:  (count: 0)", captureOutput(() -> printNumbers()));
    }

    @Test
    void testVarargsArray() {
        int[] nums = {1, 2, 3};
        assertEquals("Numbers: 1 2 3 (count: 3)", captureOutput(() -> printNumbers(nums)));
    }

    @Test
    void testMixedParams() {
        assertEquals("Hello: Alice Bob Charlie ", captureOutput(() -> greet("Hello", "Alice", "Bob", "Charlie")));
        assertEquals("Hi:  ", captureOutput(() -> greet("Hi")));
    }

    @Test
    void testSumOverload() {
        assertEquals(0, sum());
        assertEquals(1, sum(1));
        assertEquals(3, sum(1, 2));
        assertEquals(6, sum(1, 2, 3));
    }

    @Test
    void testGenericVarargs() {
        assertDoesNotThrow(() -> printList("A", "B", "C"));
        assertDoesNotThrow(() -> printList(1, 2, 3));
    }

    static void printNumbers(int... numbers) {
        System.out.print("Numbers: ");
        for (int n : numbers) System.out.print(n + " ");
        System.out.println("(count: " + numbers.length + ")");
    }

    static void greet(String greeting, String... names) {
        System.out.print(greeting + ": ");
        for (String name : names) System.out.print(name + " ");
        System.out.println();
    }

    static int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) total += n;
        return total;
    }

    @SafeVarargs
    static <T> void printList(T... items) {
        System.out.print("List: ");
        for (T item : items) System.out.print(item + " ");
        System.out.println();
    }

    static String captureOutput(Runnable r) {
        var oldOut = System.out;
        var baos = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(baos));
        try {
            r.run();
            return baos.toString().trim();
        } finally {
            System.setOut(oldOut);
        }
    }
}

class RecursionTest {

    @Test
    void testFactorial() {
        assertEquals(1, factorial(0));
        assertEquals(1, factorial(1));
        assertEquals(120, factorial(5));
        assertEquals(120, factorialIterative(5));
    }

    @Test
    void testFibonacci() {
        assertEquals(0, fibonacci(0));
        assertEquals(1, fibonacci(1));
        assertEquals(1, fibonacci(2));
        assertEquals(55, fibonacci(10));
    }

    @Test
    void testFibonacciMemo() {
        assertEquals(55, fibonacciMemo(10));
        assertEquals(102334155, fibonacciMemo(40));
    }

    @Test
    void testPower() {
        assertEquals(1, power(2, 0));
        assertEquals(2, power(2, 1));
        assertEquals(1024, power(2, 10));
        assertEquals(81, power(3, 4));
    }

    @Test
    void testGcd() {
        assertEquals(6, gcd(48, 18));
        assertEquals(1, gcd(101, 10));
        assertEquals(5, gcd(15, 5));
    }

    @Test
    void testBinarySearch() {
        int[] sorted = {1, 3, 5, 7, 9, 11, 13, 15};
        assertEquals(3, binarySearch(sorted, 7, 0, sorted.length - 1));
        assertEquals(-1, binarySearch(sorted, 6, 0, sorted.length - 1));
    }

    @Test
    void testStringReverse() {
        assertEquals("olleh", reverse("hello"));
        assertEquals("a", reverse("a"));
        assertEquals("", reverse(""));
    }

    @Test
    void testHanoi() {
        assertDoesNotThrow(() -> hanoi(3, 'A', 'C', 'B'));
    }

    // Implementations
    static long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException();
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    static long factorialIterative(int n) {
        long r = 1;
        for (int i = 2; i <= n; i++) r *= i;
        return r;
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

    static long power(int base, int exp) {
        if (exp < 0) throw new IllegalArgumentException();
        if (exp == 0) return 1;
        return base * power(base, exp - 1);
    }

    static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    static int binarySearch(int[] arr, int target, int left, int right) {
        if (left > right) return -1;
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        if (arr[mid] > target) return binarySearch(arr, target, left, mid - 1);
        return binarySearch(arr, target, mid + 1, right);
    }

    static String reverse(String s) {
        if (s == null || s.length() <= 1) return s;
        return reverse(s.substring(1)) + s.charAt(0);
    }

    static void hanoi(int n, char from, char to, char aux) {
        if (n == 1) return;
        hanoi(n - 1, from, aux, to);
        hanoi(n - 1, aux, to, from);
    }
}

// Helper classes
class Calculator {
    int add(int a, int b) { return a + b; }
    int add(int a, int b, int c) { return a + b + c; }
    double add(double a, double b) { return a + b; }
    int add(char a, char b) { return a + b; }
    void print(int num, String text) {}
    void print(String text, int num) {}
    int sum(int... numbers) { int t = 0; for (int n : numbers) t += n; return t; }
}

class Person {
    String name; int age;
    Person() { this.name = "Unknown"; this.age = 0; }
    Person(String name) { this.name = name; this.age = 0; }
    Person(String name, int age) { this.name = name; this.age = age; }
}