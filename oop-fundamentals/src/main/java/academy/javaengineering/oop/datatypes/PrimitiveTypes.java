package academy.javaengineering.oop.datatypes;

/**
 * Demonstrates all 8 primitive data types in Java.
 */
public final class PrimitiveTypes {

    public static void main(String[] args) {
        // Integer types
        byte b = 100;
        short s = 30000;
        int i = 2_000_000_000;
        long l = 9_000_000_000L;

        // Floating-point types
        float f = 3.14f;
        double d = 3.14159;

        // Character type
        char c = 'A';

        // Boolean type
        boolean bool = true;

        System.out.println("=== Primitive Types Demo ===");
        System.out.printf("byte: %d (size: %d bits)%n", b, Byte.SIZE);
        System.out.printf("short: %d (size: %d bits)%n", s, Short.SIZE);
        System.out.printf("int: %d (size: %d bits)%n", i, Integer.SIZE);
        System.out.printf("long: %d (size: %d bits)%n", l, Long.SIZE);
        System.out.printf("float: %.2f (size: %d bits)%n", f, Float.SIZE);
        System.out.printf("double: %.5f (size: %d bits)%n", d, Double.SIZE);
        System.out.printf("char: '%c' (size: %d bits)%n", c, Character.SIZE);
        System.out.printf("boolean: %b%n", bool);

        // Default values
        DefaultsDemo defaults = new DefaultsDemo();
        System.out.println("\n--- Default Values ---");
        System.out.println("byte: " + defaults.byteField);
        System.out.println("short: " + defaults.shortField);
        System.out.println("int: " + defaults.intField);
        System.out.println("long: " + defaults.longField);
        System.out.println("float: " + defaults.floatField);
        System.out.println("double: " + defaults.doubleField);
        System.out.println("char: '" + defaults.charField + "' (code: " + (int) defaults.charField + ")");
        System.out.println("boolean: " + defaults.booleanField);
        System.out.println("String (reference): " + defaults.stringField);

        // Literal formats
        System.out.println("\n--- Literal Formats ---");
        int binary = 0b1010;
        int octal = 012;
        int hex = 0xFF;
        long longHex = 0xFFFF_FFFF_FFFFL;
        
        System.out.println("Binary 0b1010: " + binary);
        System.out.println("Octal 012: " + octal);
        System.out.println("Hex 0xFF: " + hex);
        System.out.println("Hex with underscores: " + longHex);
    }

    private static class DefaultsDemo {
        byte byteField;
        short shortField;
        int intField;
        long longField;
        float floatField;
        double doubleField;
        char charField;
        boolean booleanField;
        String stringField;
    }
}