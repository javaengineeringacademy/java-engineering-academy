package com.javaacademy.sprint1.operators;

/**
 * RelationalOperators - Demonstrates comparison operators.
 * 
 * <p><b>Relational Operators (return boolean):</b>
 * <table border="1">
 * <tr><th>Operator</th><th>Name</th><th>Example</th><th>Result</th></tr>
 * <tr><td>==</td><td>Equal to</td><td>5 == 3</td><td>false</td></tr>
 * <tr><td>!=</td><td>Not equal to</td><td>5 != 3</td><td>true</td></tr>
 * <tr><td>></td><td>Greater than</td><td>5 > 3</td><td>true</td></tr>
 * <tr><td><</td><td>Less than</td><td>5 < 3</td><td>false</td></tr>
 * <tr><td>>=</td><td>Greater than or equal</td><td>5 >= 5</td><td>true</td></tr>
 * <tr><td><=</td><td>Less than or equal</td><td>5 <= 3</td><td>false</td></tr>
 * </table>
 * 
 * <p><b>Important:</b> These work on primitives. For objects, == compares references!
 * Use {@code .equals()} for content comparison.
 * 
 * <p><b>Real-world analogy:</b> Like asking yes/no questions about two things:
 * "Is A equal to B?" "Is A bigger than B?"
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class RelationalOperators {

    private RelationalOperators() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        double x = 3.14;
        double y = 2.71;

        System.out.println("=== Relational Operators ===");
        System.out.println("a = " + a + ", b = " + b);
        System.out.println("x = " + x + ", y = " + y);
        System.out.println();

        // Integer comparisons
        System.out.println("--- Integer Comparisons ---");
        System.out.println("a == b: " + (a == b)); // false
        System.out.println("a != b: " + (a != b)); // true
        System.out.println("a > b:  " + (a > b));  // false
        System.out.println("a < b:  " + (a < b));  // true
        System.out.println("a >= b: " + (a >= b)); // false
        System.out.println("a <= b: " + (a <= b)); // true

        // Double comparisons
        System.out.println("\n--- Double Comparisons ---");
        System.out.println("x > y: " + (x > y));   // true
        System.out.println("x < y: " + (x < y));   // false

        // Comparing different types (widening happens)
        System.out.println("\n--- Mixed Type Comparisons ---");
        System.out.println("a (int) == 10.0 (double): " + (a == 10.0)); // true (int promoted to double)

        // Char comparisons (based on Unicode value)
        System.out.println("\n--- Char Comparisons ---");
        char c1 = 'A'; // 65
        char c2 = 'B'; // 66
        System.out.println("'A' < 'B': " + (c1 < c2)); // true
        System.out.println("'a' > 'Z': " + ('a' > 'Z')); // true (97 > 90)

        // Boolean comparisons (only == and !=)
        System.out.println("\n--- Boolean Comparisons ---");
        boolean flag1 = true;
        boolean flag2 = false;
        System.out.println("true == false: " + (flag1 == flag2)); // false
        System.out.println("true != false: " + (flag1 != flag2)); // true
        // flag1 > flag2 // COMPILE ERROR: booleans not comparable with > < >= <=

        // Object reference comparison (== vs .equals())
        System.out.println("\n--- Object Reference vs Content ---");
        String s1 = "hello";
        String s2 = "hello";        // String pool - same object
        String s3 = new String("hello"); // New object
        
        System.out.println("s1 == s2: " + (s1 == s2)); // true (same pool reference)
        System.out.println("s1 == s3: " + (s1 == s3)); // false (different objects)
        System.out.println("s1.equals(s3): " + s1.equals(s3)); // true (same content)
        System.out.println("s1.equals(s2): " + s1.equals(s2)); // true

        // NaN special case
        System.out.println("\n--- NaN Special Case ---");
        double nan = Double.NaN;
        System.out.println("NaN == NaN: " + (nan == nan)); // false!
        System.out.println("Double.isNaN(nan): " + Double.isNaN(nan)); // true

        // Expected output shows all comparisons
    }
}