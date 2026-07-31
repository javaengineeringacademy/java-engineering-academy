package academy.javaengineering.oop.operators;

/**
 * Demonstrates bitwise operators for bit manipulation.
 */
public final class BitwiseOperators {

    public static void main(String[] args) {
        int a = 5;  // 0101 in binary
        int b = 3;  // 0011 in binary

        System.out.println("=== Bitwise Operators ===");
        System.out.println("a = " + a + " (binary: " + Integer.toBinaryString(a) + ")");
        System.out.println("b = " + b + " (binary: " + Integer.toBinaryString(b) + ")");
        System.out.println();

        // Basic bitwise operations
        System.out.println("--- Basic Operations ---");
        System.out.println("a & b = " + (a & b) + " (binary: " + Integer.toBinaryString(a & b) + ")"); // 1
        System.out.println("a | b = " + (a | b) + " (binary: " + Integer.toBinaryString(a | b) + ")"); // 7
        System.out.println("a ^ b = " + (a ^ b) + " (binary: " + Integer.toBinaryString(a ^ b) + ")"); // 6
        System.out.println("~a = " + (~a) + " (binary: " + Integer.toBinaryString(~a) + ")");         // -6

        // Shift operations
        System.out.println("\n--- Shift Operations ---");
        System.out.println("a << 1 = " + (a << 1) + " (binary: " + Integer.toBinaryString(a << 1) + ")"); // 10
        System.out.println("a << 2 = " + (a << 2) + " (binary: " + Integer.toBinaryString(a << 2) + ")"); // 20
        System.out.println("a >> 1 = " + (a >> 1) + " (binary: " + Integer.toBinaryString(a >> 1) + ")"); // 2

        // Right shift with negative (sign extension)
        int negative = -8; // 11111111111111111111111111111000
        System.out.println("\nNegative shifts:");
        System.out.println("-8 = " + Integer.toBinaryString(negative));
        System.out.println("-8 >> 1 = " + (negative >> 1) + " (sign preserved: " + Integer.toBinaryString(negative >> 1) + ")");
        System.out.println("-8 >>> 1 = " + (negative >>> 1) + " (zero-filled: " + Integer.toBinaryString(negative >>> 1) + ")");

        // Compound assignments
        System.out.println("\n--- Compound Assignments ---");
        int flags = 0b0000; // 0
        flags |= 0b0001; // Set bit 0
        flags |= 0b0100; // Set bit 2
        System.out.println("After setting bits 0 and 2: " + flags + " (binary: " + Integer.toBinaryString(flags) + ")");

        flags &= 0b0101; // Keep only bits 0 and 2
        System.out.println("flags &= 5: " + Integer.toBinaryString(flags)); // 101 (5)

        flags ^= 0b0010; // Toggle bit 1
        System.out.println("flags ^= 2: " + Integer.toBinaryString(flags)); // 111 (7)

        // Practical: Power of 2 multiply/divide
        System.out.println("\n--- Fast Math (Powers of 2) ---");
        int value = 25;
        System.out.println("value * 4 = " + (value * 4) + " == value << 2 = " + (value << 2));
        System.out.println("value / 4 = " + (value / 4) + " == value >> 2 = " + (value >> 2));

        // Practical: Bit flags (permissions)
        System.out.println("\n--- Practical: Permission Bitmask ---");
        final int READ = 0b001;    // 1
        final int WRITE = 0b010;   // 2
        final int EXECUTE = 0b100; // 4
        final int ADMIN = 0b1000;  // 8

        int userPerms = READ | WRITE;        // 011 = 3
        int adminPerms = READ | WRITE | EXECUTE | ADMIN; // 1111 = 15

        System.out.println("User perms: " + Integer.toBinaryString(userPerms) + " (" + userPerms + ")");
        System.out.println("Admin perms: " + Integer.toBinaryString(adminPerms) + " (" + adminPerms + ")");

        // Check permission
        boolean canWrite = (userPerms & WRITE) != 0;
        boolean canExecute = (userPerms & EXECUTE) != 0;
        System.out.println("User can write: " + canWrite);
        System.out.println("User can execute: " + canExecute);

        // Grant permission
        userPerms |= EXECUTE;
        System.out.println("After granting execute: " + Integer.toBinaryString(userPerms));

        // Revoke permission
        userPerms &= ~WRITE;
        System.out.println("After revoking write: " + Integer.toBinaryString(userPerms));

        // Practical: Swap without temp variable (XOR swap)
        System.out.println("\n--- XOR Swap Trick ---");
        int x = 10, y = 20;
        System.out.println("Before: x=" + x + ", y=" + y);
        x ^= y;
        y ^= x;
        x ^= y;
        System.out.println("After:  x=" + x + ", y=" + y);

        // Utility methods
        System.out.println("\n--- Integer Utility Methods ---");
        System.out.println("Integer.bitCount(7): " + Integer.bitCount(7));      // 3 (111)
        System.out.println("Integer.highestOneBit(10): " + Integer.highestOneBit(10)); // 8
        System.out.println("Integer.numberOfLeadingZeros(1): " + Integer.numberOfLeadingZeros(1));
        System.out.println("Integer.numberOfTrailingZeros(8): " + Integer.numberOfTrailingZeros(8)); // 3
        System.out.println("Integer.rotateLeft(0b1001, 2): " + Integer.toBinaryString(Integer.rotateLeft(0b1001, 2)));
        System.out.println("Integer.reverse(0b1001): " + Integer.toBinaryString(Integer.reverse(0b1001)));
    }
}