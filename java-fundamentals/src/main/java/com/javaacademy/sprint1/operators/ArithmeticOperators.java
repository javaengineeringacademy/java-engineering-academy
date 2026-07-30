package com.javaacademy.sprint1.operators;

/**
 * ArithmeticOperators - Demonstrates all arithmetic operators in Java.
 * 
 * <p><b>Arithmetic Operators:</b>
 * <table border="1">
 * <tr><th>Operator</th><th>Name</th><th>Example</th><th>Result</th></tr>
 * <tr><td>+</td><td>Addition</td><td>10 + 3</td><td>13</td></tr>
 * <tr><td>-</td><td>Subtraction</td><td>10 - 3</td><td>7</td></tr>
 * <tr><td>*</td><td>Multiplication</td><td>10 * 3</td><td>30</td></tr>
 * <tr><td>/</td><td>Division</td><td>10 / 3</td><td>3 (integer division!)</td></tr>
 * <tr><td>%</td><td>Modulus/Remainder</td><td>10 % 3</td><td>1</td></tr>
 * <tr><td>++</td><td>Increment</td><td>++x / x++</td><td>Pre/Post</td></tr>
 * <tr><td>--</td><td>Decrement</td><td>--x / x--</td><td>Pre/Post</td></tr>
 * <tr><td>+</td><td>Unary Plus</td><td>+5</td><td>5</td></tr>
 * <tr><td>-</td><td>Unary Minus</td><td>-5</td><td>-5</td></tr>
 * </table>
 * 
 * <p><b>Real-world analogy:</b> Like a calculator - but integer division truncates!
 * 10 / 3 = 3, not 3.333... (use double for decimals)
 * 
 * <p><b>Integer Division Gotcha:</b> Both operands int → result int (truncated).
 * At least one operand must be floating-point for decimal result.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class ArithmeticOperators {

    private ArithmeticOperators() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Demonstrates arithmetic operations.
     * 
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        int a = 10;
        int b = 3;

        System.out.println("=== Arithmetic Operators ===");
        System.out.println("a = " + a + ", b = " + b);
        System.out.println();

        // Basic operations
        System.out.println("--- Basic Operations ---");
        System.out.println("a + b = " + (a + b));  // 13
        System.out.println("a - b = " + (a - b));  // 7
        System.out.println("a * b = " + (a * b));  // 30
        System.out.println("a / b = " + (a / b));  // 3 (INTEGER DIVISION!)
        System.out.println("a % b = " + (a % b));  // 1 (remainder)

        // Integer division gotcha
        System.out.println("\n--- Integer Division Gotcha ---");
        System.out.println("10 / 3 = " + (10 / 3));        // 3, not 3.333...
        System.out.println("10 / 3.0 = " + (10 / 3.0));    // 3.333... (double)
        System.out.println("10.0 / 3 = " + (10.0 / 3));    // 3.333... (double)
        System.out.println("(double) 10 / 3 = " + ((double) 10 / 3)); // 3.333...

        // Modulus with negatives
        System.out.println("\n--- Modulus with Negatives ---");
        System.out.println("10 % 3 = " + (10 % 3));    // 1
        System.out.println("-10 % 3 = " + (-10 % 3));  // -1 (sign follows dividend)
        System.out.println("10 % -3 = " + (10 % -3));  // 1
        System.out.println("-10 % -3 = " + (-10 % -3)); // -1

        // Increment/Decrement
        System.out.println("\n--- Increment/Decrement ---");
        int x = 5;
        System.out.println("x = " + x);
        System.out.println("++x (pre-increment): " + (++x)); // 6, x is now 6
        System.out.println("x++ (post-increment): " + (x++)); // 6, x becomes 7
        System.out.println("x after post: " + x);             // 7
        
        int y = 5;
        System.out.println("--y (pre-decrement): " + (--y)); // 4
        System.out.println("y-- (post-decrement): " + (y--)); // 4, y becomes 3

        // Compound assignment
        System.out.println("\n--- Compound Assignment ---");
        int z = 10;
        z += 5;  // z = z + 5
        System.out.println("z += 5: " + z); // 15
        z -= 3;  // z = z - 3
        System.out.println("z -= 3: " + z); // 12
        z *= 2;  // z = z * 2
        System.out.println("z *= 2: " + z); // 24
        z /= 4;  // z = z / 4
        System.out.println("z /= 4: " + z); // 6
        z %= 5;  // z = z % 5
        System.out.println("z %= 5: " + z); // 1

        // Operator precedence (highest to lowest)
        System.out.println("\n--- Precedence Demo ---");
        int result = 10 + 5 * 2;        // * before + → 10 + 10 = 20
        System.out.println("10 + 5 * 2 = " + result); // 20
        result = (10 + 5) * 2;          // Parentheses first → 15 * 2 = 30
        System.out.println("(10 + 5) * 2 = " + result); // 30

        // Overflow (no exception, wraps around!)
        System.out.println("\n--- Integer Overflow ---");
        int max = Integer.MAX_VALUE;    // 2,147,483,647
        System.out.println("MAX_VALUE: " + max);
        System.out.println("MAX_VALUE + 1: " + (max + 1)); // -2,147,483,648 (wraps!)
        System.out.println("MIN_VALUE - 1: " + (Integer.MIN_VALUE - 1)); // 2,147,483,647

        // Expected output:
        // === Arithmetic Operators ===
        // a = 10, b = 3
        // ...
        // 10 / 3 = 3
        // 10 / 3.0 = 3.333...
        // ...
    }
}