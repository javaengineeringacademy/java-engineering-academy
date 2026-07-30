package com.javaacademy.sprint1.strings;

/**
 * StringBuilderDemo - Demonstrates mutable string operations with StringBuilder and StringBuffer.
 *
 * <p><b>StringBuilder vs StringBuffer:</b>
 * <table border="1">
 * <tr><th>Feature</th><th>StringBuilder</th><th>StringBuffer</th></tr>
 * <tr><td>Thread-safe</td><td>No</td><td>Yes (synchronized)</td></tr>
 * <tr><td>Performance</td><td>Faster</td><td>Slower</td></tr>
 * <tr><td>Introduced</td><td>Java 5</td><td>Java 1</td></tr>
 * <tr><td>Use case</td><td>Single-threaded (most cases)</td><td>Multi-threaded (rare)</td></tr>
 * </table>
 *
 * <p><b>Real-world analogy:</b> StringBuilder = whiteboard (edit freely).
 * StringBuffer = whiteboard with lock (only one person edits at a time).
 *
 * <p><b>Capacity:</b> Grows automatically (doubles + 2).
 * Pre-size with constructor for known large strings: {@code new StringBuilder(10000)}
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class StringBuilderDemo {

    private StringBuilderDemo() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== StringBuilder Demo ===\n");

        // Creation
        System.out.println("--- Creation ---");
        StringBuilder sb1 = new StringBuilder();           // Capacity 16
        StringBuilder sb2 = new StringBuilder(100);        // Pre-sized
        StringBuilder sb3 = new StringBuilder("Hello");    // From String
        StringBuilder sb4 = new StringBuilder(sb3);        // Copy

        System.out.println("Default capacity: " + sb1.capacity());   // 16
        System.out.println("Pre-sized capacity: " + sb2.capacity()); // 100

        // Append (most common)
        System.out.println("\n--- Append ---");
        StringBuilder sb = new StringBuilder();
        sb.append("Java");
        sb.append(" ").append("Engineering").append(" ").append("Academy");
        System.out.println("Result: " + sb); // Java Engineering Academy

        // Chaining
        sb.append(" ").append(2024).append(" v").append(1.0);
        System.out.println("Chained: " + sb);

        // Insert
        System.out.println("\n--- Insert ---");
        sb = new StringBuilder("HelloWorld");
        sb.insert(5, " "); // Insert at index 5
        System.out.println("After insert: " + sb); // Hello World

        // Delete
        System.out.println("\n--- Delete ---");
        sb = new StringBuilder("Hello World");
        sb.delete(5, 6); // Delete char at 5 (space)
        System.out.println("Delete char: " + sb); // HelloWorld
        sb.delete(0, 5); // Delete "Hello"
        System.out.println("Delete range: " + sb); // World

        // Replace
        System.out.println("\n--- Replace ---");
        sb = new StringBuilder("Java");
        sb.replace(0, 4, "Python"); // Replace all
        System.out.println("Replace all: " + sb); // Python

        // Reverse
        System.out.println("\n--- Reverse ---");
        sb = new StringBuilder("racecar");
        sb.reverse();
        System.out.println("Reversed: " + sb); // racecar (palindrome)

        // Capacity management
        System.out.println("\n--- Capacity ---");
        sb = new StringBuilder("Hi");
        System.out.println("Length: " + sb.length() + ", Capacity: " + sb.capacity());
        sb.ensureCapacity(100);
        System.out.println("After ensureCapacity(100): " + sb.capacity());
        sb.trimToSize(); // Reduce capacity to length
        System.out.println("After trimToSize: " + sb.capacity());

        // Char array access
        System.out.println("\n--- Char Access ---");
        sb = new StringBuilder("Hello");
        char c = sb.charAt(1); // 'e'
        System.out.println("charAt(1): " + c);
        sb.setCharAt(1, 'a');
        System.out.println("setCharAt(1, 'a'): " + sb); // Hallo

        // Subsequence (CharSequence interface)
        System.out.println("\n--- Subsequence ---");
        sb = new StringBuilder("JavaEngineering");
        CharSequence sub = sb.subSequence(4, 15);
        System.out.println("subSequence(4, 15): " + sub); // Engineering

        // Convert to String
        String result = sb.toString();
        System.out.println("toString(): " + result);

        // Performance: StringBuilder vs String concatenation
        System.out.println("\n--- Performance Demo ---");
        // BAD: String s = ""; for(...) s += i; // Creates new String each iteration!
        // GOOD: StringBuilder sb = new StringBuilder(); for(...) sb.append(i);

        // Expected output shows all StringBuilder operations and performance difference
    }
}