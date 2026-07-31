package academy.javaengineering.oop.operators;

/**
 * Demonstrates all arithmetic operators in Java.
 */
public final class ArithmeticOperators {

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
        //
        // --- Basic Operations ---
        // a + b = 13
        // a - b = 7
        // a * b = 30
        // a / b = 3
        // a % b = 1
        //
        // --- Integer Division Gotcha ---
        // 10 / 3 = 3
        // 10 / 3.0 = 3.3333333333333335
        // 10.0 / 3 = 3.3333333333333335
        // (double) 10 / 3 = 3.3333333333333335
        //
        // --- Modulus with Negatives ---
        // 10 % 3 = 1
        // -10 % 3 = -1
        // 10 % -3 = 1
        // -10 % -3 = -1
        //
        // --- Increment/Decrement ---
        // x = 5
        // ++x (pre-increment): 6
        // x++ (post-increment): 6
        // x after post: 7
        // --y (pre-decrement): 4
        // y-- (post-decrement): 4
        // y after post: 3
        //
        // --- Compound Assignment ---
        // z += 5 → 15
        // z -= 3 → 12
        // z *= 2 → 24
        // z /= 4 → 6
        // z %= 5 → 1
        //
        // --- Precedence Demo ---
        // 10 + 5 * 2 = 20
        // (10 + 5) * 2 = 30
        //
        // --- Integer Overflow ---
        // MAX_VALUE: 2147483647
        // MAX_VALUE + 1: -2147483648
        // MIN_VALUE - 1: 2147483647
    }
}