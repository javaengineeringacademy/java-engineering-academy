package academy.javaengineering.fundamentals;

/**
 * Variables and Types in Java
 *
 * This file covers:
 * - Primitive types (byte, short, int, long, float, double, boolean, char)
 * - Reference types (String, objects)
 * - Type casting (widening and narrowing)
 * - Naming conventions
 * - Constants using the final keyword
 */
public class VariablesAndTypes {

    // Class-level constants
    public static final double PI = 3.14159265358979;
    public static final String APP_NAME = "JavaDemo";

    public static void main(String[] args) {

        // =========================================================
        // 1. PRIMITIVE TYPES
        // =========================================================
        // Java has 8 primitive types. They store actual values, not references.

        // byte: 8-bit signed integer, range -128 to 127
        byte smallNumber = 100;
        byte minByte = -128;
        byte maxByte = 127;
        System.out.println("=== Primitive Types ===");
        System.out.println("byte  -> " + smallNumber + " (8 bits, range: -128 to 127)");

        // short: 16-bit signed integer, range -32,768 to 32,767
        short mediumNumber = 30000;
        System.out.println("short -> " + mediumNumber + " (16 bits, range: -32768 to 32767)");

        // int: 32-bit signed integer, range -2^31 to 2^31-1 (~2.1 billion)
        int largeNumber = 2000000000;
        System.out.println("int   -> " + largeNumber + " (32 bits, most commonly used integer type)");

        // long: 64-bit signed integer, use 'L' suffix for long literals
        long veryLargeNumber = 9_000_000_000L; // underscores improve readability
        System.out.println("long  -> " + veryLargeNumber + " (64 bits, use L suffix)");

        // float: 32-bit floating point, use 'f' suffix
        float decimalNumber = 3.14f;
        System.out.println("float -> " + decimalNumber + " (32 bits, use f suffix)");

        // double: 64-bit floating point, default for decimals
        double preciseDecimal = 3.141592653589793;
        System.out.println("double -> " + preciseDecimal + " (64 bits, default decimal type)");

        // boolean: true or false
        boolean isJavaFun = true;
        boolean isCodingHard = false;
        System.out.println("boolean -> " + isJavaFun + " (1 bit, only true or false)");

        // char: single 16-bit Unicode character, use single quotes
        char grade = 'A';
        char copyright = '\u00A9'; // Unicode for ©
        System.out.println("char  -> " + grade + " (16 bits, single quotes, Unicode support)");
        System.out.println("char (unicode) -> " + copyright);

        // =========================================================
        // 2. REFERENCE TYPES
        // =========================================================
        // Reference types point to objects in memory, not the actual data.

        // String is the most common reference type
        String name = "Java Developer";
        System.out.println("\n=== Reference Types ===");
        System.out.println("String -> " + name);

        // Arrays are reference types
        int[] numbers = {1, 2, 3, 4, 5};
        System.out.println("Array  -> first element: " + numbers[0]);

        // Objects (custom classes)
        Person person = new Person("Alice", 25);
        System.out.println("Object -> " + person);

        // null is a special literal that means "no object"
        String empty = null;
        System.out.println("null reference -> " + empty);

        // =========================================================
        // 3. TYPE CASTING
        // =========================================================
        // Widening (implicit): smaller type to larger type - no data loss
        System.out.println("\n=== Type Casting ===");

        int intVal = 100;
        long longVal = intVal;        // int -> long (widening)
        float floatVal = longVal;     // long -> float (widening)
        double doubleVal = floatVal;  // float -> double (widening)

        System.out.println("Widening (implicit):");
        System.out.println("  int -> long   : " + intVal + " -> " + longVal);
        System.out.println("  long -> float  : " + longVal + " -> " + floatVal);
        System.out.println("  float -> double: " + floatVal + " -> " + doubleVal);

        // Narrowing (explicit): larger type to smaller type - potential data loss
        double pi = 3.14159;
        int piInt = (int) pi;        // double -> int (truncates decimal)
        byte piByte = (byte) piInt;  // int -> byte (may overflow)

        System.out.println("\nNarrowing (explicit) - requires cast operator:");
        System.out.println("  double -> int  : " + pi + " -> " + piInt + " (decimal truncated)");
        System.out.println("  int -> byte    : " + piInt + " -> " + piByte + " (may overflow if > 127)");

        // Dangerous narrowing example
        int bigNumber = 130;
        byte overflow = (byte) bigNumber; // 130 overflows byte range (-128 to 127)
        System.out.println("  Overflow example: 130 cast to byte -> " + overflow + " (overflow!)");

        // =========================================================
        // 4. VARIABLE NAMING CONVENTIONS
        // =========================================================
        System.out.println("\n=== Naming Conventions ===");

        // Variables and methods: camelCase
        int studentCount = 30;
        String firstName = "John";
        boolean isGraduated = true;

        // Classes: PascalCase
        // (e.g., public class StudentManager { ... })

        // Constants: UPPER_SNAKE_CASE
        final int MAX_RETRY_COUNT = 3;
        final String DEFAULT_LOCALE = "en_US";

        // Package names: all lowercase
        // (e.g., academy.javaengineering.fundamentals)

        System.out.println("Variable names  : camelCase       -> studentCount, firstName");
        System.out.println("Class names     : PascalCase      -> StudentManager");
        System.out.println("Constants       : UPPER_SNAKE_CASE -> MAX_RETRY_COUNT");
        System.out.println("Package names   : lowercase        -> academy.javaengineering");

        // Rules for valid identifiers:
        // - Must start with a letter, underscore (_), or dollar sign ($)
        // - Can contain letters, digits, underscores, dollar signs
        // - Cannot be a reserved keyword (class, int, public, etc.)
        // - Case-sensitive (age != Age != AGE)

        // =========================================================
        // 5. CONSTANTS (final keyword)
        // =========================================================
        System.out.println("\n=== Constants (final keyword) ===");

        // final variable cannot be reassigned after initialization
        final int DAYS_IN_WEEK = 7;
        final String GREETING = "Hello, World!";
        final double TAX_RATE = 0.08;

        System.out.println("DAYS_IN_WEEK = " + DAYS_IN_WEEK);
        System.out.println("GREETING     = " + GREETING);
        System.out.println("TAX_RATE     = " + TAX_RATE);

        // This would cause a compile error:
        // DAYS_IN_WEEK = 8;  // Error: cannot assign a value to final variable

        // Static final (class constant)
        System.out.println("PI           = " + PI);
        System.out.println("APP_NAME     = " + APP_NAME);

        // =========================================================
        // 6. DEFAULT VALUES
        // =========================================================
        // Class fields (not local variables) get default values
        System.out.println("\n=== Default Values for Class Fields ===");
        System.out.println("byte    -> 0");
        System.out.println("short   -> 0");
        System.out.println("int     -> 0");
        System.out.println("long    -> 0L");
        System.out.println("float   -> 0.0f");
        System.out.println("double  -> 0.0d");
        System.out.println("boolean -> false");
        System.out.println("char    -> '\\u0000' (null character)");
        System.out.println("Object  -> null");

        // Local variables do NOT get default values - must initialize before use
        // int localVar; // This is declared but not initialized
        // System.out.println(localVar); // Compile error: variable might not have been initialized

        int localVar = 42; // Must initialize before use
        System.out.println("\nLocal variable (must be initialized): " + localVar);

        // =========================================================
        // 7. LITERAL SUFFIXES AND FORMATTING
        // =========================================================
        System.out.println("\n=== Literal Suffixes and Formatting ===");

        // Integer literals
        int decimal = 100;
        int hexadecimal = 0x64;    // 0x prefix for hex
        int octal = 0144;          // 0 prefix for octal
        int binary = 0b1100100;    // 0b prefix for binary

        System.out.println("Decimal    : " + decimal);
        System.out.println("Hexadecimal: " + hexadecimal + " (0x64)");
        System.out.println("Octal      : " + octal + " (0144)");
        System.out.println("Binary     : " + binary + " (0b1100100)");

        // Underscores for readability (Java 7+)
        long creditCard = 1234_5678_9012_3456L;
        long hexBytes = 0xFF_EC_DE_5E;
        double piFormatted = 3.141_592_653_589_79;

        System.out.println("\nUnderscores for readability:");
        System.out.println("Credit card : " + creditCard);
        System.out.println("Hex bytes   : " + hexBytes);
        System.out.println("Pi          : " + piFormatted);

        System.out.println("\n=== Variables and Types Demo Complete ===");
    }
}

/**
 * Simple Person class to demonstrate object reference types.
 */
class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
