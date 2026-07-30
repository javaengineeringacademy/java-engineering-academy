package com.javaacademy.sprint1.operators;

/**
 * BitwiseOperators - Demonstrates bitwise operators for bit manipulation.
 *
 * <p><b>Bitwise Operators (operate on integer types):</b>
 * <table border="1">
 * <tr><th>Operator</th><th>Name</th><th>Description</th><th>Example (5 & 3)</th></tr>
 * <tr><td>&</td><td>Bitwise AND</td><td>1 if both bits are 1</td><td>0101 & 0011 = 0001 (1)</td></tr>
 * <tr><td>|</td><td>Bitwise OR</td><td>1 if either bit is 1</td><td>0101 | 0011 = 0111 (7)</td></tr>
 * <tr><td>^</td><td>Bitwise XOR</td><td>1 if bits are different</td><td>0101 ^ 0011 = 0110 (6)</td></tr>
 * <tr><td>~</td><td>Bitwise NOT</td><td>Inverts all bits</td><td>~0101 = ...11111010 (-6)</td></tr>
 * <tr><td><<</td><td>Left Shift</td><td>Shift bits left, fill with 0</td><td>5 << 1 = 1010 (10)</td></tr>
 * <tr><td>>></td><td>Signed Right Shift</td><td>Shift bits right, keep sign</td><td>5 >> 1 = 0010 (2)</td></tr>
 * <tr><td>>>></td><td>Unsigned Right Shift</td><td>Shift bits right, fill with 0</td><td>-8 >>> 1 = 0x7FFFFFFD</td></tr>
 * </table>
 *
 * <p><b>Real-world uses:</b>
 * <ul>
 *   <li>Flags/bitmasks (permissions, settings)</li>
 *   <li>Fast multiply/divide by 2 (shifts)</li>
 *   <li>Data compression, encryption</li>
 *   <li>Graphics (color manipulation)</li>
 *   <li>Network protocols (IP addresses, masks)</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class BitwiseOperators {

    private BitwiseOperators() {
        throw new UnsupportedOperationException("Utility class");
    }

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

        // Practical: Power of 2 multiply/divide
        System.out.println("\n--- Fast Math (Powers of 2) ---");
        int value = 25;
        System.out.println("value * 4 = " + (value * 4) + " == value << 2 = " + (value << 2));
        System.out.println("value / 4 = " + (value / 4) + " == value >> 2 = " + (value >> 2));

        // Practical: Bit flags (permissions)
        System.out.println("\n--- Bit Flags (Permissions) ---");
        final int READ = 1 << 0;    // 0001 = 1
        final int WRITE = 1 << 1;   // 0010 = 2
        final int EXECUTE = 1 << 2; // 0100 = 4
        final int ADMIN = 1 << 3;   // 1000 = 8

        int userPerms = READ | WRITE;        // 0011 = 3
        int adminPerms = READ | WRITE | EXECUTE | ADMIN; // 1111 = 15

        System.out.println("User perms: " + userPerms + " (has write? " + ((userPerms & WRITE) != 0) + ")");
        System.out.println("Admin perms: " + adminPerms + " (has admin? " + ((adminPerms & ADMIN) != 0) + ")");

        // Compound assignments
        System.out.println("\n--- Compound Assignments ---");
        int flags = 0b0000; // 0
        flags |= 1 << 0; // Set bit 0
        flags |= 1 << 2; // Set bit 2
        System.out.println("After setting bits 0 and 2: " + flags + " (binary: " + Integer.toBinaryString(flags) + ")");

        boolean bit2Set = (flags & (1 << 2)) != 0;
        System.out.println("Is bit 2 set? " + bit2Set);

        flags &= ~(1 << 0); // Clear bit 0
        System.out.println("After clearing bit 0: " + flags + " (binary: " + Integer.toBinaryString(flags) + ")");

        flags ^= (1 << 1); // Toggle bit 1
        System.out.println("After toggling bit 1: " + flags + " (binary: " + Integer.toBinaryString(flags) + ")");

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