package academy.javaengineering.fundamentals;

/**
 * Demonstrates Java primitive types, wrapper classes, type casting,
 * the var keyword, and constants.
 *
 * <p>Java has 8 primitive types: byte, short, int, long, float, double,
 * char, and boolean. Each has a corresponding wrapper class for use
 * with generics and the Collections framework.</p>
 */
public class VariablesAndTypes {

    // Constants - compile-time constants must be static final
    public static final double PI = 3.141592653589793;
    public static final String GREETING = "Hello, Java!";
    public static final int MAX_VALUE = Integer.MAX_VALUE;

    // Instance fields
    private String name;
    private int age;

    public VariablesAndTypes(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static void main(String[] args) {
        System.out.println("=== Variables and Types Demo ===\n");

        demoPrimitiveTypes();
        demoWrapperClasses();
        demoTypeCasting();
        demoVarKeyword();
        demoConstants();
        demoScope();
    }

    // --- Primitive Types ---

    /**
     * Demonstrates all 8 Java primitive types with their sizes and ranges.
     */
    public static void demoPrimitiveTypes() {
        System.out.println("--- Primitive Types ---");

        // byte: 8-bit signed, range -128 to 127
        byte smallNumber = 100;
        System.out.println("byte:  " + smallNumber + " (8-bit, range: -128 to 127)");

        // short: 16-bit signed, range -32,768 to 32,767
        short mediumNumber = 30_000;
        System.out.println("short: " + mediumNumber + " (16-bit, range: -32,768 to 32,767)");

        // int: 32-bit signed (most common for whole numbers)
        int largeNumber = 2_000_000_000;
        System.out.println("int:   " + largeNumber + " (32-bit)");

        // long: 64-bit signed, use 'L' suffix
        long veryLargeNumber = 9_000_000_000L;
        System.out.println("long:  " + veryLargeNumber + " (64-bit, use L suffix)");

        // float: 32-bit IEEE 754, use 'f' suffix
        float decimal = 3.14f;
        System.out.println("float: " + decimal + " (32-bit, use f suffix)");

        // double: 64-bit IEEE 754 (default for decimals)
        double preciseDecimal = 3.141592653589793;
        System.out.println("double: " + preciseDecimal + " (64-bit, default for decimals)");

        // char: 16-bit Unicode character
        char letter = 'A';
        char emoji = '\u0041'; // Unicode for 'A'
        System.out.println("char:  " + letter + " (16-bit Unicode, value: " + (int) letter + ")");

        // boolean: true or false
        boolean isJavaFun = true;
        boolean isFishTasty = false;
        System.out.println("boolean: " + isJavaFun + " (true or false)");
        System.out.println();
    }

    // --- Wrapper Classes ---

    /**
     * Demonstrates wrapper classes and autoboxing/unboxing.
     */
    public static void demoWrapperClasses() {
        System.out.println("--- Wrapper Classes ---");

        // Wrapper class instantiation
        Byte byteObj = 127;
        Short shortObj = 32767;
        Integer intObj = 2147483647;
        Long longObj = 9_000_000_000L;
        Float floatObj = 3.14f;
        Double doubleObj = 2.71828;
        Character charObj = 'Z';
        Boolean boolObj = true;

        System.out.println("Byte:    " + byteObj + " (min: " + Byte.MIN_VALUE + ", max: " + Byte.MAX_VALUE + ")");
        System.out.println("Short:   " + shortObj + " (min: " + Short.MIN_VALUE + ", max: " + Short.MAX_VALUE + ")");
        System.out.println("Integer: " + intObj + " (min: " + Integer.MIN_VALUE + ", max: " + Integer.MAX_VALUE + ")");
        System.out.println("Long:    " + longObj + " (min: " + Long.MIN_VALUE + ", max: " + Long.MAX_VALUE + ")");
        System.out.println("Float:   " + floatObj);
        System.out.println("Double:  " + doubleObj);
        System.out.println("Character: " + charObj + " (isLetter: " + Character.isLetter(charObj) + ")");
        System.out.println("Boolean: " + boolObj);

        // Autoboxing (primitive -> wrapper)
        Integer autoBoxed = 42;
        System.out.println("\nAutoboxed int to Integer: " + autoBoxed);

        // Unboxing (wrapper -> primitive)
        int unboxed = autoBoxed;
        System.out.println("Unboxed Integer to int: " + unboxed);

        // Useful wrapper methods
        System.out.println("Integer.parseInt(\"123\"): " + Integer.parseInt("123"));
        System.out.println("Integer.toHexString(255): " + Integer.toHexString(255));
        System.out.println("Integer.bitCount(255): " + Integer.bitCount(255));
        System.out.println("Double.isNaN(Double.NaN): " + Double.isNaN(Double.NaN));
        System.out.println("Character.toLowerCase('A'): " + Character.toLowerCase('A'));
        System.out.println("Character.isDigit('5'): " + Character.isDigit('5'));
        System.out.println();
    }

    // --- Type Casting ---

    /**
     * Demonstrates widening (implicit) and narrowing (explicit) type casting.
     */
    public static void demoTypeCasting() {
        System.out.println("--- Type Casting ---");

        // Widening casting (automatic) - smaller to larger type
        byte byteVal = 10;
        int intFromByte = byteVal;        // byte -> int
        long longFromInt = intFromByte;   // int -> long
        float floatFromLong = longFromInt; // long -> float
        double doubleFromFloat = floatFromLong; // float -> double

        System.out.println("Widening (implicit):");
        System.out.println("  byte -> int:     " + byteVal + " -> " + intFromByte);
        System.out.println("  int -> long:     " + intFromByte + " -> " + longFromInt);
        System.out.println("  long -> float:   " + longFromInt + " -> " + floatFromLong);
        System.out.println("  float -> double: " + floatFromLong + " -> " + doubleFromFloat);

        // Narrowing casting (manual) - larger to smaller type
        double pi = 3.14159;
        int intFromDouble = (int) pi;        // loses precision
        byte byteFromInt = (byte) 256;       // overflows to 0

        System.out.println("\nNarrowing (explicit):");
        System.out.println("  double -> int:   " + pi + " -> " + intFromDouble + " (precision lost)");
        System.out.println("  int -> byte:     256 -> " + byteFromInt + " (overflow)");

        // Overflow example
        int maxInt = Integer.MAX_VALUE;
        int overflow = maxInt + 1;
        System.out.println("  Integer.MAX_VALUE + 1 = " + overflow + " (overflow wrap-around)");

        // Casting between char and int
        char ch = 'A';
        int ascii = ch;
        char fromAscii = (char) 97;
        System.out.println("\n  char 'A' -> int: " + ascii);
        System.out.println("  int 97 -> char: '" + fromAscii + "'");

        System.out.println();
    }

    // --- var Keyword (Java 10+) ---

    /**
     * Demonstrates the var keyword for local variable type inference.
     */
    public static void demoVarKeyword() {
        System.out.println("--- var Keyword (Java 10+) ---");

        // var infers the type from the initializer
        var number = 42;             // inferred as int
        var decimal = 3.14;          // inferred as double
        var text = "Hello";          // inferred as String
        var flag = true;             // inferred as boolean
        var list = java.util.List.of(1, 2, 3); // inferred as List<Integer>

        System.out.println("var number = 42 -> type: int");
        System.out.println("var decimal = 3.14 -> type: double");
        System.out.println("var text = \"Hello\" -> type: " + text.getClass().getSimpleName());
        System.out.println("var flag = true -> type: boolean");
        System.out.println("var list = List.of(1,2,3) -> type: " + list.getClass().getSimpleName());

        // var cannot be used for:
        // - Fields/instance variables
        // - Method parameters
        // - Method return types
        // - Uninitialized variables
        // - null type
        System.out.println("\nvar restrictions: no fields, no params, no return types, no uninitialized vars");

        // var with complex types improves readability
        var map = new java.util.HashMap<String, java.util.List<Integer>>();
        var entry = map.entrySet().iterator();

        System.out.println("var improves readability with complex generic types");
        System.out.println();
    }

    // --- Constants ---

    /**
     * Demonstrates constants using static final.
     */
    public static void demoConstants() {
        System.out.println("--- Constants ---");

        System.out.println("PI = " + PI);
        System.out.println("GREETING = " + GREETING);
        System.out.println("MAX_VALUE = " + MAX_VALUE);

        // Final variable (instance constant)
        final int instanceConstant = 100;
        System.out.println("instanceConstant = " + instanceConstant);

        // Naming convention: UPPER_SNAKE_CASE for constants
        System.out.println("\nNaming convention: static final fields use UPPER_SNAKE_CASE");

        // Blank final - must be initialized in constructor
        final String blankFinal;
        blankFinal = "Initialized later";
        System.out.println("blankFinal = " + blankFinal);
        System.out.println();
    }

    // --- Variable Scope ---

    /**
     * Demonstrates variable scope rules in Java.
     */
    public static void demoScope() {
        System.out.println("--- Variable Scope ---");

        int outerVar = 10; // method scope

        if (outerVar > 5) {
            int innerVar = 20; // block scope
            System.out.println("Outer: " + outerVar + ", Inner: " + innerVar);
        }
        // innerVar is not accessible here

        // Loop scope
        for (int i = 0; i < 3; i++) {
            int loopVar = i * 10;
            System.out.println("i=" + i + ", loopVar=" + loopVar);
        }
        // i and loopVar are not accessible here

        // Shadowing (legal but discouraged)
        int x = 10;
        {
            int x2 = 20; // Different name to avoid confusion
            System.out.println("Inner x2 shadows outer x: outer=" + x + ", inner=" + x2);
        }

        System.out.println("Variable scope: block-scoped with curly braces");
        System.out.println();
    }

    // --- Getters (instance methods) ---

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
