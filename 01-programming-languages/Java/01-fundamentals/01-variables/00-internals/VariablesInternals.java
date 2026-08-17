package academy.javaengineering.fundamentals.variables;

/**
 * Demonstrates how Java stores variables internally.
 */
public class VariablesInternals {

    public static void main(String[] args) {
        System.out.println("=== Variable Internals Demo ===\n");

        // 1. Primitive sizes
        System.out.println("--- Primitive Type Sizes ---");
        System.out.println("byte:   " + Byte.BYTES + " byte(s),  range: " + Byte.MIN_VALUE + " to " + Byte.MAX_VALUE);
        System.out.println("short:  " + Short.BYTES + " byte(s), range: " + Short.MIN_VALUE + " to " + Short.MAX_VALUE);
        System.out.println("int:    " + Integer.BYTES + " byte(s),  range: " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE);
        System.out.println("long:   " + Long.BYTES + " byte(s),  range: " + Long.MIN_VALUE + " to " + Long.MAX_VALUE);
        System.out.println("float:  " + Float.BYTES + " byte(s),  precision: ~6-7 decimal digits");
        System.out.println("double: " + Double.BYTES + " byte(s),  precision: ~15-16 decimal digits");
        System.out.println("boolean: conceptually 1 bit (JVM implementation varies)");
        System.out.println("char:    " + Character.BYTES + " byte(s),   range: 0 to 65535");

        // 2. Overflow demonstration
        System.out.println("\n--- Overflow Demonstration ---");
        int maxInt = Integer.MAX_VALUE;
        System.out.println("Integer.MAX_VALUE: " + maxInt);
        System.out.println("Integer.MAX_VALUE + 1: " + (maxInt + 1)); // Wraps to MIN_VALUE

        byte b = 127;
        System.out.println("\nbyte 127 + 1: " + (byte)(b + 1)); // Wraps to -128

        // 3. Floating point precision
        System.out.println("\n--- Floating Point Precision ---");
        double a = 0.1;
        double b2 = 0.2;
        double sum = a + b2;
        System.out.println("0.1 + 0.2 = " + sum);
        System.out.println("0.1 + 0.2 == 0.3? " + (sum == 0.3));
        System.out.println("Actual value of 0.1 + 0.2: " + String.format("%.20f", sum));

        // 4. String pool
        System.out.println("\n--- String Pool Internals ---");
        String s1 = "Hello";
        String s2 = "Hello";
        String s3 = new String("Hello");
        String s4 = s3.intern();

        System.out.println("s1 == s2 (pool reuse): " + (s1 == s2));
        System.out.println("s1 == s3 (heap vs pool): " + (s1 == s3));
        System.out.println("s1 == s4 (interned): " + (s1 == s4));
        System.out.println("s1.equals(s3): " + s1.equals(s3));

        // 5. Type casting internals
        System.out.println("\n--- Type Casting Internals ---");
        int intValue = 130;
        byte byteValue = (byte) intValue;
        System.out.println("int 130 cast to byte: " + byteValue + " (binary: " + Integer.toBinaryString(byteValue) + ")");

        double pi = 3.14159;
        int piInt = (int) pi;
        System.out.println("double 3.14159 cast to int: " + piInt + " (truncated)");

        // 6. Memory reference demonstration
        System.out.println("\n--- Reference vs Value ---");
        int x = 42;
        int y = x;
        System.out.println("x == y: " + (x == y) + " (primitive values compared)");

        Integer obj1 = 127;
        Integer obj2 = 127;
        Integer obj3 = new Integer(127);
        System.out.println("obj1 == obj2 (cached): " + (obj1 == obj2));
        System.out.println("obj1 == obj3 (new object): " + (obj1 == obj3));
        System.out.println("obj1.equals(obj3): " + obj1.equals(obj3));

        System.out.println("\n=== Internals Demo Complete ===");
    }
}
