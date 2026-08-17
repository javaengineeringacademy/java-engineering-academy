package academy.javaengineering.fundamentals.operators;

/**
 * Demonstrates how operators work internally in Java.
 */
public class OperatorsInternals {

    public static void main(String[] args) {
        System.out.println("=== Operators Internals Demo ===\n");

        // 1. Operator precedence
        System.out.println("--- Operator Precedence ---");
        int result1 = 2 + 3 * 4;
        int result2 = (2 + 3) * 4;
        System.out.println("2 + 3 * 4 = " + result1 + " (multiplication first)");
        System.out.println("(2 + 3) * 4 = " + result2 + " (parentheses first)");

        // 2. Short-circuit evaluation
        System.out.println("\n--- Short-Circuit Evaluation ---");
        int x = 0;
        boolean result3 = (x != 0) && (10 / x > 1);
        System.out.println("(x != 0) && (10 / x > 1) = " + result3 + " (right side not evaluated)");

        boolean result4 = (x == 0) || (10 / x > 1);
        System.out.println("(x == 0) || (10 / x > 1) = " + result4 + " (short-circuit after first true)");

        // 3. Integer overflow
        System.out.println("\n--- Integer Overflow ---");
        int maxInt = Integer.MAX_VALUE;
        System.out.println("Integer.MAX_VALUE: " + maxInt);
        System.out.println("Integer.MAX_VALUE + 1: " + (maxInt + 1));
        System.out.println("Binary: " + Integer.toBinaryString(maxInt) + " + 1 = " + Integer.toBinaryString(maxInt + 1));

        // 4. Floating-point precision
        System.out.println("\n--- Floating-Point Precision ---");
        double a = 0.1;
        double b = 0.2;
        System.out.println("0.1 + 0.2 = " + (a + b));
        System.out.println("0.1 + 0.2 == 0.3? " + ((a + b) == 0.3));
        System.out.println("Actual bits: " + Double.toHexString(a + b));

        // 5. Bitwise operations
        System.out.println("\n--- Bitwise Operations ---");
        int num = 42;
        System.out.println("Number: " + num + " (binary: " + Integer.toBinaryString(num) + ")");
        System.out.println("Set bit 3: " + (num | (1 << 3)) + " (binary: " + Integer.toBinaryString(num | (1 << 3)) + ")");
        System.out.println("Clear bit 3: " + (num & ~(1 << 3)) + " (binary: " + Integer.toBinaryString(num & ~(1 << 3)) + ")");
        System.out.println("Toggle bit 3: " + (num ^ (1 << 3)) + " (binary: " + Integer.toBinaryString(num ^ (1 << 3)) + ")");
        System.out.println("Is power of 2? " + ((num & (num - 1)) == 0));

        int powerOf2 = 16;
        System.out.println(powerOf2 + " is power of 2? " + ((powerOf2 & (powerOf2 - 1)) == 0));

        // 6. XOR swap
        System.out.println("\n--- XOR Swap (No Temp Variable) ---");
        int a2 = 5;
        int b2 = 3;
        System.out.println("Before: a=" + a2 + ", b=" + b2);
        a2 ^= b2;
        b2 ^= a2;
        a2 ^= b2;
        System.out.println("After:  a=" + a2 + ", b=" + b2);

        System.out.println("\n=== Internals Demo Complete ===");
    }
}
