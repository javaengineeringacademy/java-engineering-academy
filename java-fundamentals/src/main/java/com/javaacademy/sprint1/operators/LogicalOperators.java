package com.javaacademy.sprint1.operators;

/**
 * LogicalOperators - Demonstrates logical operators (&&, ||, !).
 *
 * <p><b>Logical Operators (return boolean):</b>
 * <table border="1">
 * <tr><th>Operator</th><th>Name</th><th>Description</th><th>Short-Circuit?</th></tr>
 * <tr><td>&&</td><td>Logical AND</td><td>true if both operands true</td><td>Yes</td></tr>
 * <tr><td>||</td><td>Logical OR</td><td>true if either operand true</td><td>Yes</td></tr>
 * <tr><td>!</td><td>Logical NOT</td><td>Inverts boolean value</td><td>N/A</td></tr>
 * <tr><td>&</td><td>Bitwise AND</td><td>Evaluates both sides</td><td>No</td></tr>
 * <tr><td>|</td><td>Bitwise OR</td><td>Evaluates both sides</td><td>No</td></tr>
 * <tr><td>^</td><td>Bitwise XOR</td><td>true if operands differ</td><td>No</td></tr>
 * </table>
 *
 * <p><b>Short-Circuit Evaluation:</b>
 * <ul>
 *   <li>{@code &&}: If left is false, right is NOT evaluated</li>
 *   <li>{@code ||}: If left is true, right is NOT evaluated</li>
 *   <li>Use for performance and null safety: {@code if (str != null && str.length() > 0)}</li>
 * </ul>
 *
 * <p><b>Real-world analogy:</b>
 * - {@code &&} = "Both must agree" (veto power)
 * - {@code ||} = "Either can decide" (first yes wins)
 * - Short-circuit = "Stop asking once decision is made"
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class LogicalOperators {

    private LogicalOperators() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        boolean a = true;
        boolean b = false;

        System.out.println("=== Logical Operators ===");
        System.out.println("a = " + a + ", b = " + b);
        System.out.println();

        // Basic logical operations
        System.out.println("--- Basic Operations ---");
        System.out.println("a && b: " + (a && b)); // false
        System.out.println("a || b: " + (a || b)); // true
        System.out.println("!a: " + (!a));         // false
        System.out.println("!b: " + (!b));         // true

        // Truth tables
        System.out.println("\n--- Truth Table for && ---");
        System.out.println("true && true   = " + (true && true));
        System.out.println("true && false  = " + (true && false));
        System.out.println("false && true  = " + (false && true));
        System.out.println("false && false = " + (false && false));

        System.out.println("\n--- Truth Table for || ---");
        System.out.println("true || true   = " + (true || true));
        System.out.println("true || false  = " + (true || false));
        System.out.println("false || true  = " + (false || true));
        System.out.println("false || false = " + (false || false));

        // Short-circuit evaluation demo
        System.out.println("\n--- Short-Circuit Evaluation ---");
        boolean result;

        // && stops at first false
        result = false && expensiveOperation("&&");
        System.out.println("false && expensive(): " + result); // expensiveOperation NOT called

        // || stops at first true
        result = true || expensiveOperation("||");
        System.out.println("true || expensive(): " + result); // expensiveOperation NOT called

        // Practical null-safe check
        System.out.println("\n--- Null-Safe Pattern ---");
        String str = null;
        if (str != null && str.length() > 0) {
            System.out.println("String has content");
        } else {
            System.out.println("String is null or empty (no NPE!)");
        }

        // Non-short-circuit operators (&, |) - evaluate BOTH sides
        System.out.println("\n--- Non-Short-Circuit (&, |) ---");
        result = false & expensiveOperation("&");
        System.out.println("false & expensive(): " + result); // expensiveOperation CALLED!

        result = true | expensiveOperation("|");
        System.out.println("true | expensive(): " + result);  // expensiveOperation CALLED!

        // XOR (exclusive OR)
        System.out.println("\n--- XOR (^) ---");
        System.out.println("true ^ true   = " + (true ^ true));   // false
        System.out.println("true ^ false  = " + (true ^ false));  // true
        System.out.println("false ^ true  = " + (false ^ true));  // true
        System.out.println("false ^ false = " + (false ^ false)); // false

        // Precedence: ! > && > ||
        System.out.println("\n--- Precedence: ! > && > || ---");
        boolean x = true, y = false, z = true;
        result = !x && y || z; // (!x) && y || z → false && false || true → false || true → true
        System.out.println("!true && false || true = " + result); // true

        // Expected output shows short-circuit behavior
    }

    /**
     * Simulates an expensive operation with side effect (prints to console).
     *
     * @param operator the operator being tested
     * @return always true
     */
    private static boolean expensiveOperation(String operator) {
        System.out.println("  >>> expensiveOperation() called for " + operator + " <<<");
        return true;
    }
}