package com.javaacademy.sprint1.datatypes;

/**
 * PrimitiveTypes - Demonstrates Java's 8 primitive data types.
 * 
 * <p>Java has exactly 8 primitive types (not objects, stored directly in stack):
 * <table border="1" cellpadding="5" summary="Primitive types">
 *   <tr><th>Type</th><th>Size</th><th>Default</th><th>Range</th><th>Literal Suffix</th></tr>
 *   <tr><td><b>byte</b></td><td>8-bit</td><td>0</td><td>-128 to 127</td><td>(none)</td></tr>
 *   <tr><td><b>short</b></td><td>16-bit</td><td>0</td><td>-32,768 to 32,767</td><td>(none)</td></tr>
 *   <tr><td><b>int</b></td><td>32-bit</td><td>0</td><td>-2^31 to 2^31-1</td><td>(none)</td></tr>
 *   <tr><td><b>long</b></td><td>64-bit</td><td>0L</td><td>-2^63 to 2^63-1</td><td>L or l</td></tr>
 *   <tr><td><b>float</b></td><td>32-bit</td><td>0.0f</td><td>~±3.4e38 (6-7 digits)</td><td>F or f</td></tr>
 *   <tr><td><b>double</b></td><td>64-bit</td><td>0.0d</td><td>~±1.7e308 (15 digits)</td><td>D or d (optional)</td></tr>
 *   <tr><td><b>char</b></td><td>16-bit</td><td>'\u0000'</td><td>0 to 65,535 (Unicode)</td><td>single quotes</td></tr>
 *   <tr><td><b>boolean</b></td><td>1-bit (JVM)</td><td>false</td><td>true/false</td><td>(none)</td></tr>
 * </table>
 * 
 * <p><b>Real-world analogy:</b> Primitives are like raw materials (bricks, cement) - 
 * simple, efficient, no overhead. Objects are like pre-built furniture - 
 * feature-rich but heavier.
 * 
 * <p><b>Best practice:</b> Prefer primitives for performance. Use wrappers (Integer, Double, etc.)
 * only when you need null, generics, or utility methods.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class PrimitiveTypes {

    /** Private constructor - utility class. */
    private PrimitiveTypes() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Demonstrates all primitive types with literals.
     * 
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        System.out.println("=== Primitive Types Demo ===\n");

        // Integer types
        byte byteValue = 100;           // 8-bit signed
        short shortValue = 30000;       // 16-bit signed
        int intValue = 2_000_000_000;   // 32-bit signed (underscores for readability, Java 7+)
        long longValue = 9_000_000_000L; // 64-bit signed (L suffix required)

        System.out.println("--- Integer Types ---");
        System.out.printf("byte:   %d (size: %d bits, max: %d)%n", byteValue, Byte.SIZE, Byte.MAX_VALUE);
        System.out.printf("short:  %d (size: %d bits, max: %d)%n", shortValue, Short.SIZE, Short.MAX_VALUE);
        System.out.printf("int:    %d (size: %d bits, max: %d)%n", intValue, Integer.SIZE, Integer.MAX_VALUE);
        System.out.printf("long:   %d (size: %d bits, max: %d)%n", longValue, Long.SIZE, Long.MAX_VALUE);

        // Floating-point types
        float floatValue = 3.14159f;    // 32-bit IEEE 754 (f suffix required)
        double doubleValue = 3.141592653589793; // 64-bit IEEE 754 (default for decimals)

        System.out.println("\n--- Floating-Point Types ---");
        System.out.printf("float:  %.5f (size: %d bits, precision: ~7 digits)%n", floatValue, Float.SIZE);
        System.out.printf("double: %.15f (size: %d bits, precision: ~15 digits)%n", doubleValue, Double.SIZE);

        // Special floating-point values
        System.out.println("\n--- Special Float/Double Values ---");
        System.out.println("Float.POSITIVE_INFINITY: " + Float.POSITIVE_INFINITY);
        System.out.println("Float.NEGATIVE_INFINITY: " + Float.NEGATIVE_INFINITY);
        System.out.println("Float.NaN: " + Float.NaN);
        System.out.println("Double.NaN == Double.NaN: " + (Double.NaN == Double.NaN)); // false!
        System.out.println("Double.isNaN(Double.NaN): " + Double.isNaN(Double.NaN));   // true

        // Character type (16-bit Unicode)
        char charValue = 'A';
        char unicodeChar = '\u0041';    // Unicode escape for 'A'
        char emoji = '\uD83D\uDE00';    // 😀 (supplementary char = 2 chars in UTF-16)

        System.out.println("\n--- Character Type ---");
        System.out.println("char 'A': " + charValue + " (code: " + (int) charValue + ")");
        System.out.println("Unicode \\u0041: " + unicodeChar);
        System.out.println("Emoji (2 chars): length=" + String.valueOf(emoji).length());

        // Boolean type
        boolean boolTrue = true;
        boolean boolFalse = false;
        boolean result = (10 > 5); // Relational expression yields boolean

        System.out.println("\n--- Boolean Type ---");
        System.out.println("true: " + boolTrue);
        System.out.println("false: " + boolFalse);
        System.out.println("10 > 5: " + result);

        // Default values (for fields, not local variables)
        System.out.println("\n--- Default Values (fields) ---");
        DefaultsDemo defaults = new DefaultsDemo();
        System.out.println("byte: " + defaults.byteField);
        System.out.println("short: " + defaults.shortField);
        System.out.println("int: " + defaults.intField);
        System.out.println("long: " + defaults.longField);
        System.out.println("float: " + defaults.floatField);
        System.out.println("double: " + defaults.doubleField);
        System.out.println("char: '" + defaults.charField + "' (code: " + (int) defaults.charField + ")");
        System.out.println("boolean: " + defaults.booleanField);
        System.out.println("String (reference): " + defaults.stringField);

        // Literal formats (Java 7+)
        System.out.println("\n--- Literal Formats ---");
        int binary = 0b1010;      // Binary (Java 7+)
        int octal = 012;          // Octal (leading 0)
        int hex = 0xFF;           // Hexadecimal (leading 0x)
        long longHex = 0xFFFF_FFFF_FFFFL; // Underscores in literals (Java 7+)
        
        System.out.println("Binary 0b1010: " + binary);
        System.out.println("Octal 012: " + octal);
        System.out.println("Hex 0xFF: " + hex);
        System.out.println("Hex with underscores: " + longHex);

        // Expected output shows all primitive types with their characteristics
    }

    /** Helper class to show default field values. */
    private static class DefaultsDemo {
        byte byteField;
        short shortField;
        int intField;
        long longField;
        float floatField;
        double doubleField;
        char charField;
        boolean booleanField;
        String stringField; // reference type default is null
    }
}