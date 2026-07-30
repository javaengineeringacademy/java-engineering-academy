package com.javaacademy.sprint1.operators;

/**
 * AssignmentOperators - Demonstrates assignment and compound assignment operators.
 *
 * <p><b>Assignment Operators:</b>
 * <table border="1">
 * <tr><th>Operator</th><th>Name</th><th>Equivalent To</th></tr>
 * <tr><td>=</td><td>Simple Assignment</td><td>x = y</td></tr>
 * <tr><td>+=</td><td>Add and Assign</td><td>x = x + y</td></tr>
 * <tr><td>-=</td><td>Subtract and Assign</td><td>x = x - y</td></tr>
 * <tr><td>*=</td><td>Multiply and Assign</td><td>x = x * y</td></tr>
 * <tr><td>/=</td><td>Divide and Assign</td><td>x = x / y</td></tr>
 * <tr><td>%=</td><td>Modulus and Assign</td><td>x = x % y</td></tr>
 * <tr><td>&=</td><td>Bitwise AND and Assign</td><td>x = x & y</td></tr>
 * <tr><td>|=</td><td>Bitwise OR and Assign</td><td>x = x | y</td></tr>
 * <tr><td>^=</td><td>Bitwise XOR and Assign</td><td>x = x ^ y</td></tr>
 * <tr><td><<=</td><td>Left Shift and Assign</td><td>x = x << y</td></tr>
 * <tr><td>>>=</td><td>Right Shift and Assign</td><td>x = x >> y</td></tr>
 * <tr><td>>>=</td><td>Unsigned Right Shift and Assign</td><td>x = x >>> y</td></tr>
 * </table>
 *
 * <p><b>Key Points:</b>
 * <ul>
 *   <li>Assignment expression has a value (the assigned value)</li>
 *   <li>Right-to-left associativity: {@code a = b = c = 5} means {@code a = (b = (c = 5))}</li>
 *   <li>Compound assignments include implicit narrowing cast</li>
 * </ul>
 *
 * <p><b>Real-world analogy:</b> Like updating a scoreboard -
 * {@code score += 10} means "add 10 to current score and update it"
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class AssignmentOperators {

    private AssignmentOperators() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Assignment Operators ===\n");

        // Simple assignment
        int x = 10;
        System.out.println("Simple assignment: x = 10 → x = " + x);

        // Assignment expression returns value
        int y = (x = 20); // y gets the value assigned to x (20)
        System.out.println("y = (x = 20) → x = " + x + ", y = " + y);

        // Chained assignment (right-to-left)
        int a, b, c;
        a = b = c = 5; // c=5, b=5, a=5
        System.out.println("\nChained: a = b = c = 5 → a=" + a + ", b=" + b + ", c=" + c);

        // Compound assignments
        System.out.println("\n--- Compound Assignments ---");
        int num = 10;

        num += 5;  // num = num + 5
        System.out.println("num += 5 → " + num); // 15

        num -= 3;  // num = num - 3
        System.out.println("num -= 3 → " + num); // 12

        num *= 2;  // num = num * 2
        System.out.println("num *= 2 → " + num); // 24

        num /= 4;  // num = num / 4
        System.out.println("num /= 4 → " + num); // 6

        num %= 5;  // num = num % 5
        System.out.println("num %= 5 → " + num); // 1

        // Compound with bitwise
        System.out.println("\n--- Bitwise Compound Assignments ---");
        int flags = 0b0000; // 0
        flags |= 0b0001; // Set bit 0
        System.out.println("flags |= 1 → " + Integer.toBinaryString(flags)); // 1
        flags |= 0b0100; // Set bit 2
        System.out.println("flags |= 4 → " + Integer.toBinaryString(flags)); // 101 (5)
        flags &= 0b0101; // Keep only bits 0 and 2
        System.out.println("flags &= 5 → " + Integer.toBinaryString(flags)); // 101 (5)
        flags ^= 0b0010; // Toggle bit 1
        System.out.println("flags ^= 2 → " + Integer.toBinaryString(flags)); // 111 (7)

        // Compound assignment with implicit narrowing
        System.out.println("\n--- Implicit Narrowing in Compound Assignment ---");
        byte b1 = 10;
        // b1 = b1 + 5; // COMPILE ERROR: int + byte = int, cannot assign to byte
        b1 += 5;      // OK! Equivalent to: b1 = (byte) (b1 + 5)
        System.out.println("byte b1 = 10; b1 += 5 → " + b1); // 15

        short s = 100;
        s *= 2; // s = (short) (s * 2)
        System.out.println("short s = 100; s *= 2 → " + s); // 200

        // Expected output
    }
}