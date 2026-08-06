/**
 * WrapperClasses - Complete Guide to Java Wrapper Classes
 * 
 * Java provides 8 wrapper classes for primitive types:
 * - Integer, Long, Double, Float, Boolean, Byte, Short, Character
 * 
 * Wrapper classes provide:
 * - Object representation of primitives
 * - Useful methods (parsing, conversion, etc.)
 * - Ability to use primitives in collections
 * - Null safety (nullable types)
 */
public class WrapperClasses {

    /**
     * Demonstrates all 8 wrapper classes with their ranges.
     */
    public static void wrapperClassOverview() {
        System.out.println("=== All 8 Wrapper Classes ===");

        // Byte (-128 to 127)
        Byte byteObj = 127;
        System.out.println("Byte: " + byteObj + " (range: -128 to 127)");
        System.out.println("  Byte.MIN_VALUE: " + Byte.MIN_VALUE);
        System.out.println("  Byte.MAX_VALUE: " + Byte.MAX_VALUE);

        // Short (-32,768 to 32,767)
        Short shortObj = 32767;
        System.out.println("Short: " + shortObj + " (range: -32768 to 32767)");
        System.out.println("  Short.MIN_VALUE: " + Short.MIN_VALUE);
        System.out.println("  Short.MAX_VALUE: " + Short.MAX_VALUE);

        // Integer (-2,147,483,648 to 2,147,483,647)
        Integer intObj = 2147483647;
        System.out.println("Integer: " + intObj + " (range: -2^31 to 2^31-1)");
        System.out.println("  Integer.MIN_VALUE: " + Integer.MIN_VALUE);
        System.out.println("  Integer.MAX_VALUE: " + Integer.MAX_VALUE);

        // Long (-9,223,372,036,854,775,808 to 9,223,372,036,854,775,807)
        Long longObj = 9223372036854775807L;
        System.out.println("Long: " + longObj + " (range: -2^63 to 2^63-1)");
        System.out.println("  Long.MIN_VALUE: " + Long.MIN_VALUE);
        System.out.println("  Long.MAX_VALUE: " + Long.MAX_VALUE);

        // Float (IEEE 754 single precision)
        Float floatObj = 3.14f;
        System.out.println("Float: " + floatObj + " (~6-7 decimal digits precision)");
        System.out.println("  Float.MIN_VALUE: " + Float.MIN_VALUE);
        System.out.println("  Float.MAX_VALUE: " + Float.MAX_VALUE);

        // Double (IEEE 754 double precision)
        Double doubleObj = 3.141592653589793;
        System.out.println("Double: " + doubleObj + " (~15-16 decimal digits precision)");
        System.out.println("  Double.MIN_VALUE: " + Double.MIN_VALUE);
        System.out.println("  Double.MAX_VALUE: " + Double.MAX_VALUE);

        // Boolean (true or false)
        Boolean boolObj = true;
        System.out.println("Boolean: " + boolObj + " (true or false)");
        System.out.println("  Boolean.TRUE: " + Boolean.TRUE);
        System.out.println("  Boolean.FALSE: " + Boolean.FALSE);

        // Character (0 to 65,535)
        Character charObj = 'A';
        System.out.println("Character: " + charObj + " (range: 0 to 65,535)");
        System.out.println("  Character.MIN_VALUE: " + Character.MIN_VALUE);
        System.out.println("  Character.MAX_VALUE: " + (int) Character.MAX_VALUE);
    }

    /**
     * Demonstrates autoboxing (primitive → wrapper) and
     * unboxing (wrapper → primitive).
     */
    public static void boxingUnboxing() {
        System.out.println("\n=== Boxing and Unboxing ===");

        // Autoboxing (primitive → wrapper)
        Integer autoboxed = 42; // Implicit Integer.valueOf(42)
        Long longAutoboxed = 100L; // Implicit Long.valueOf(100L)
        Double doubleAutoboxed = 3.14; // Implicit Double.valueOf(3.14)

        System.out.println("Autoboxing:");
        System.out.println("  Integer autoboxed = 42 → " + autoboxed);
        System.out.println("  Long autoboxed = 100L → " + longAutoboxed);
        System.out.println("  Double autoboxed = 3.14 → " + doubleAutoboxed);

        // Unboxing (wrapper → primitive)
        Integer wrapper = 100;
        int unboxed = wrapper; // Implicit wrapper.intValue()
        System.out.println("\nUnboxing:");
        System.out.println("  Integer wrapper = 100; int unboxed = wrapper → " + unboxed);

        // Explicit boxing/unboxing
        Integer explicitBox = Integer.valueOf(42); // Explicit
        int explicitUnbox = explicitBox.intValue(); // Explicit
        System.out.println("\nExplicit:");
        System.out.println("  Integer.valueOf(42) → " + explicitBox);
        System.out.println("  explicitBox.intValue() → " + explicitUnbox);
    }

    /**
     * Demonstrates the Integer cache (-128 to 127).
     * Objects in this range are cached and reused.
     */
    public static void integerCache() {
        System.out.println("\n=== Integer Cache (-128 to 127) ===");

        // Cache hit (same object)
        Integer a = 127;
        Integer b = 127;
        System.out.println("Cache hit (127):");
        System.out.println("  Integer a = 127; Integer b = 127;");
        System.out.println("  a == b: " + (a == b)); // true (same object)
        System.out.println("  a.equals(b): " + a.equals(b)); // true

        // Cache miss (different objects)
        Integer c = 128;
        Integer d = 128;
        System.out.println("\nCache miss (128):");
        System.out.println("  Integer c = 128; Integer d = 128;");
        System.out.println("  c == d: " + (c == d)); // false (different objects)
        System.out.println("  c.equals(d): " + c.equals(d)); // true

        // ValueOf vs constructor
        Integer e1 = Integer.valueOf(100);
        Integer e2 = new Integer(100); // Deprecated in Java 9+
        System.out.println("\nValueOf vs Constructor:");
        System.out.println("  Integer.valueOf(100) vs new Integer(100):");
        System.out.println("  valueOf: " + e1.hashCode() + " (uses cache)");
        System.out.println("  constructor: " + e2.hashCode() + " (new object)");
    }

    /**
     * Demonstrates parsing methods and their differences.
     */
    public static void parsingMethods() {
        System.out.println("\n=== Parsing Methods ===");

        // parseInt vs valueOf
        String numberStr = "12345";

        // parseInt returns primitive
        int parsedInt = Integer.parseInt(numberStr);
        System.out.println("Integer.parseInt(\"" + numberStr + "\") → " + parsedInt);
        System.out.println("  Returns: int (primitive)");

        // valueOf returns wrapper
        Integer integerValue = Integer.valueOf(numberStr);
        System.out.println("Integer.valueOf(\"" + numberStr + "\") → " + integerValue);
        System.out.println("  Returns: Integer (wrapper, uses cache if possible)");

        // Parsing with radix
        String hexStr = "FF";
        int hexParsed = Integer.parseInt(hexStr, 16);
        System.out.println("\nParsing with radix:");
        System.out.println("  Integer.parseInt(\"FF\", 16) → " + hexParsed);

        String binaryStr = "1010";
        int binaryParsed = Integer.parseInt(binaryStr, 2);
        System.out.println("  Integer.parseInt(\"1010\", 2) → " + binaryParsed);

        // NumberFormatException
        System.out.println("\nNumberFormatException:");
        try {
            Integer.parseInt("not a number");
        } catch (NumberFormatException e) {
            System.out.println("  Integer.parseInt(\"not a number\") → " + e.getMessage());
        }
    }

    /**
     * Demonstrates common pitfalls with wrapper classes.
     */
    public static void commonPitfalls() {
        System.out.println("\n=== Common Pitfalls ===");

        // Pitfall 1: == comparison
        System.out.println("1. == comparison (reference equality):");
        Integer x = 200;
        Integer y = 200;
        System.out.println("   Integer x = 200; Integer y = 200;");
        System.out.println("   x == y: " + (x == y)); // false!
        System.out.println("   Always use .equals() for value comparison");

        // Pitfall 2: NullPointerException with unboxing
        System.out.println("\n2. NullPointerException with unboxing:");
        Integer nullInt = null;
        try {
            int value = nullInt; // NPE!
            System.out.println("   This won't print");
        } catch (NullPointerException e) {
            System.out.println("   Integer nullInt = null; int value = nullInt;");
            System.out.println("   → NullPointerException!");
        }

        // Pitfall 3: Unexpected null in arithmetic
        System.out.println("\n3. Unexpected null in arithmetic:");
        Integer a = null;
        Integer b = 10;
        try {
            int result = a + b; // NPE due to unboxing
        } catch (NullPointerException e) {
            System.out.println("   Integer a = null; Integer b = 10;");
            System.out.println("   int result = a + b; → NullPointerException!");
        }

        // Pitfall 4: Cache range confusion
        System.out.println("\n4. Cache range confusion:");
        Integer p = 128;
        Integer q = 128;
        System.out.println("   Integer p = 128; Integer q = 128;");
        System.out.println("   p == q: " + (p == q)); // false
        System.out.println("   But: Integer p = 127; Integer q = 127; → true");
    }

    /**
     * Demonstrates performance implications.
     */
    public static void performanceImplications() {
        System.out.println("\n=== Performance Implications ===");

        int iterations = 10_000_000;

        // Primitive performance
        long start = System.nanoTime();
        long primitiveSum = 0;
        for (int i = 0; i < iterations; i++) {
            primitiveSum += i;
        }
        long primitiveTime = System.nanoTime() - start;

        // Wrapper performance (with boxing/unboxing)
        start = System.nanoTime();
        Long wrapperSum = 0L;
        for (int i = 0; i < iterations; i++) {
            wrapperSum += i; // Autoboxing each iteration
        }
        long wrapperTime = System.nanoTime() - start;

        System.out.println("Performance comparison (" + iterations + " iterations):");
        System.out.println("  Primitive: " + primitiveTime / 1_000_000 + " ms");
        System.out.println("  Wrapper: " + wrapperTime / 1_000_000 + " ms");
        System.out.println("  Overhead: " + ((double) wrapperTime / primitiveTime) + "x");
        System.out.println("\n  Use primitives for performance-critical code");
        System.out.println("  Use wrappers for collections and null safety");
    }

    /**
     * Demonstrates useful wrapper class methods.
     */
    public static void usefulMethods() {
        System.out.println("\n=== Useful Wrapper Methods ===");

        // Integer methods
        System.out.println("Integer methods:");
        System.out.println("  Integer.toHexString(255) → " + Integer.toHexString(255));
        System.out.println("  Integer.toBinaryString(10) → " + Integer.toBinaryString(10));
        System.out.println("  Integer.compare(10, 20) → " + Integer.compare(10, 20));
        System.out.println("  Integer.max(10, 20) → " + Integer.max(10, 20));
        System.out.println("  Integer.min(10, 20) → " + Integer.min(10, 20));

        // Character methods
        System.out.println("\nCharacter methods:");
        System.out.println("  Character.isDigit('5') → " + Character.isDigit('5'));
        System.out.println("  Character.isLetter('A') → " + Character.isLetter('A'));
        System.out.println("  Character.toUpperCase('a') → " + Character.toUpperCase('a'));
        System.out.println("  Character.toLowerCase('A') → " + Character.toLowerCase('A'));
        System.out.println("  Character.isWhitespace(' ') → " + Character.isWhitespace(' '));

        // Double methods
        System.out.println("\nDouble methods:");
        System.out.println("  Double.isNaN(0.0/0.0) → " + Double.isNaN(0.0 / 0.0));
        System.out.println("  Double.isInfinite(1.0/0.0) → " + Double.isInfinite(1.0 / 0.0));
        System.out.println("  Double.parseDouble(\"3.14\") → " + Double.parseDouble("3.14"));
    }

    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        System.out.println("Java Wrapper Classes Complete Guide");
        System.out.println("===================================");

        wrapperClassOverview();
        boxingUnboxing();
        integerCache();
        parsingMethods();
        commonPitfalls();
        performanceImplications();
        usefulMethods();

        System.out.println("\n=== Key Takeaways ===");
        System.out.println("1. 8 wrapper classes for 8 primitive types");
        System.out.println("2. Autoboxing/unboxing converts between them");
        System.out.println("3. Integer cache (-128 to 127) reuses objects");
        System.out.println("4. Use .equals() not == for value comparison");
        System.out.println("5. Watch for NullPointerException with unboxing");
        System.out.println("6. Primitives are faster; use wrappers for collections");
        System.out.println("7. Use valueOf() not constructors for caching");
        System.out.println("8. parseInt() returns primitive, valueOf() returns wrapper");
    }
}
