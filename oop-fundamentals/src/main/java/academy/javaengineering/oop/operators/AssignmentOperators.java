package academy.javaengineering.oop.operators;

/**
 * Demonstrates assignment and compound assignment operators.
 */
public final class AssignmentOperators {

    public static void main(String[] args) {
        System.out.println("=== Assignment Operators ===\n");

        // Simple assignment
        int x = 10;
        System.out.println("Simple assignment: x = 10 → x = " + x);

        // Assignment expression returns value
        int y = (x = 20); // y gets the value assigned to x (20)
        System.out.println("y = (x = 20) → x = " + x + ", y = " + y);

        // Chained assignment (right-to-left associativity)
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
    }
}