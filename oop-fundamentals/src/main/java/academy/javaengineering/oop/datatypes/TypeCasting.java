package academy.javaengineering.oop.datatypes;

/**
 * Demonstrates type conversion (widening and narrowing) in Java.
 */
public final class TypeCasting {

    public static void main(String[] args) {
        System.out.println("=== Widening Conversion (Implicit) ===");
        
        // byte → short → int → long → float → double
        byte b = 100;
        short s = b;      // byte to short
        int i = s;        // short to int
        long l = i;       // int to long
        float f = l;      // long to float
        double d = f;     // float to double
        
        System.out.println("byte(100) → short → int → long → float → double");
        System.out.println("Result: " + d); // 100.0

        // char to int (widening)
        char c = 'A';
        int charAsInt = c; // Automatic
        System.out.println("\nchar 'A' → int: " + charAsInt); // 65

        System.out.println("\n=== Narrowing Conversion (Explicit Cast) ===");
        
        // double → float → long → int → short → byte
        double bigDouble = 123.456;
        float narrowedFloat = (float) bigDouble;  // Cast required
        long narrowedLong = (long) narrowedFloat; // Cast required
        int narrowedInt = (int) narrowedLong;     // Cast required
        short narrowedShort = (short) narrowedInt; // Cast required
        byte narrowedByte = (byte) narrowedShort;  // Cast required
        
        System.out.println("double(123.456) → float → long → int → short → byte");
        System.out.println("Final byte value: " + narrowedByte); // 123 (truncated decimal)

        // Data loss examples
        System.out.println("\n=== Data Loss Examples ===");
        int largeInt = 130;
        byte overflowByte = (byte) largeInt; // 130 > 127, overflow!
        System.out.println("int(130) → byte: " + overflowByte); // -126 (two's complement)

        int veryLarge = 1_000_000_000;
        short overflowShort = (short) veryLarge;
        System.out.println("int(1_000_000_000) → short: " + overflowShort); // -27136

        // Floating-point to integer (truncation, not rounding)
        double pi = 3.14159;
        int truncated = (int) pi;
        System.out.println("double(3.14159) → int: " + truncated); // 3 (not 4!)

        // char narrowing
        int codePoint = 65;
        char fromInt = (char) codePoint;
        System.out.println("int(65) → char: '" + fromInt + "'"); // 'A'

        // Compound assignment operators DO implicit narrowing!
        System.out.println("\n=== Compound Assignment (Implicit Narrowing) ===");
        byte byteVal = 10;
        byteVal += 5;        // OK! Equivalent to: byteVal = (byte) (byteVal + 5);
        System.out.println("byteVal += 5: " + byteVal); // 15 (no cast needed!)
        
        // But this would need cast:
        // byteVal = byteVal + 5; // COMPILE ERROR! int + byte = int

        short s = 100;
        s *= 2; // s = (short) (s * 2);
        System.out.println("short s = 100; s *= 2: " + s); // 200

        System.out.println("\n=== Primitive Wrapper Conversions ===");
        // Autoboxing: primitive → wrapper
        Integer wrapped = 42; // Integer.valueOf(42)
        
        // Unboxing: wrapper → primitive
        int unwrapped = wrapped; // wrapped.intValue()
        
        System.out.println("Autoboxing: int(42) → Integer: " + wrapped);
        System.out.println("Unboxing: Integer → int: " + unwrapped);
        
        // Null danger with unboxing
        Integer nullable = null;
        // int crash = nullable; // NullPointerException at runtime!
        System.out.println("null Integer unboxing: throws NPE (commented out)");
    }
}