package academy.javaengineering.fundamentals;

/**
 * Demonstrates all Java operator categories: arithmetic, relational,
 * logical, bitwise, ternary, assignment, and instanceof pattern matching.
 *
 * <p>Operators are symbols that perform operations on variables and values.
 * Java provides a rich set of operators organized into categories.</p>
 */
public class Operators {

    public static void main(String[] args) {
        System.out.println("=== Operators Demo ===\n");

        demoArithmeticOperators();
        demoRelationalOperators();
        demoLogicalOperators();
        demoBitwiseOperators();
        demoTernaryOperator();
        demoAssignmentOperators();
        demoInstanceofPatternMatching();
        demoOperatorPrecedence();
    }

    // --- Arithmetic Operators ---

    /**
     * Demonstrates arithmetic operators: +, -, *, /, %, ++, --.
     */
    public static void demoArithmeticOperators() {
        System.out.println("--- Arithmetic Operators ---");

        int a = 17, b = 5;

        System.out.println("a = " + a + ", b = " + b);
        System.out.println("a + b = " + (a + b));
        System.out.println("a - b = " + (a - b));
        System.out.println("a * b = " + (a * b));
        System.out.println("a / b = " + (a / b) + " (integer division)");
        System.out.println("a % b = " + (a % b) + " (modulo/remainder)");

        // Floating point division
        double da = 17.0, db = 5.0;
        System.out.println("da / db = " + (da / db) + " (floating point division)");

        // Increment and decrement
        int c = 10;
        System.out.println("\nc = " + c);
        System.out.println("c++ = " + (c++) + " (post-increment, returns old value then increments)");
        System.out.println("After c++: c = " + c);
        System.out.println("++c = " + (++c) + " (pre-increment, increments then returns new value)");
        System.out.println("After ++c: c = " + c);
        System.out.println("c-- = " + (c--) + " (post-decrement)");
        System.out.println("--c = " + (--c) + " (pre-decrement)");

        // Division by zero
        System.out.println("\nDivision by zero:");
        System.out.println("5.0 / 0 = " + (5.0 / 0) + " (floating point: infinity)");
        try {
            int divideByZero = 5 / 0;
            System.out.println("5 / 0 = " + divideByZero);
        } catch (ArithmeticException e) {
            System.out.println("5 / 0 throws: " + e.getMessage());
        }
        System.out.println();
    }

    // --- Relational Operators ---

    /**
     * Demonstrates relational operators: ==, !=, <, >, <=, >=.
     */
    public static void demoRelationalOperators() {
        System.out.println("--- Relational Operators ---");

        int x = 10, y = 20;

        System.out.println("x = " + x + ", y = " + y);
        System.out.println("x == y: " + (x == y));
        System.out.println("x != y: " + (x != y));
        System.out.println("x < y:  " + (x < y));
        System.out.println("x > y:  " + (x > y));
        System.out.println("x <= y: " + (x <= y));
        System.out.println("x >= y: " + (x >= y));

        // String comparison
        String s1 = "hello";
        String s2 = "hello";
        String s3 = new String("hello");

        System.out.println("\nString comparison:");
        System.out.println("s1 == s2:   " + (s1 == s2) + " (same reference in pool)");
        System.out.println("s1 == s3:   " + (s1 == s3) + " (different objects)");
        System.out.println("s1.equals(s3): " + s1.equals(s3) + " (value equality)");

        // Comparing primitives vs objects
        Integer i1 = 127;
        Integer i2 = 127;
        Integer i3 = 128;
        Integer i4 = 128;

        System.out.println("\nInteger caching (-128 to 127):");
        System.out.println("i1 == i2:   " + (i1 == i2) + " (cached, same object)");
        System.out.println("i3 == i4:   " + (i3 == i4) + " (not cached, different objects)");
        System.out.println("i3.equals(i4): " + i3.equals(i4) + " (value equality)");
        System.out.println();
    }

    // --- Logical Operators ---

    /**
     * Demonstrates logical operators: &&, ||, !.
     */
    public static void demoLogicalOperators() {
        System.out.println("--- Logical Operators ---");

        boolean a = true, b = false;

        System.out.println("a = " + a + ", b = " + b);
        System.out.println("a && b = " + (a && b) + " (AND)");
        System.out.println("a || b = " + (a || b) + " (OR)");
        System.out.println("!a = " + (!a) + " (NOT)");
        System.out.println("!b = " + (!b) + " (NOT)");

        // Short-circuit evaluation
        System.out.println("\nShort-circuit evaluation:");
        int x = 0;
        boolean result = (x != 0) && (10 / x > 1); // Safe: second operand not evaluated
        System.out.println("(x != 0) && (10 / x > 1): " + result + " (no ArithmeticException)");

        // Truth table
        System.out.println("\nTruth Table:");
        System.out.println("A     | B     | A && B | A || B | !A");
        System.out.println("------+-------+--------+--------+-----");
        boolean[][] truthTable = {{true, true}, {true, false}, {false, true}, {false, false}};
        for (boolean[] row : truthTable) {
            System.out.printf("%-5b | %-5b | %-6b | %-6b | %-5b%n",
                    row[0], row[1], row[0] && row[1], row[0] || row[1], !row[0]);
        }
        System.out.println();
    }

    // --- Bitwise Operators ---

    /**
     * Demonstrates bitwise operators: &, |, ^, ~, <<, >>, >>>.
     */
    public static void demoBitwiseOperators() {
        System.out.println("--- Bitwise Operators ---");

        int a = 0b1010; // 10 in binary
        int b = 0b1100; // 12 in binary

        System.out.println("a = " + Integer.toBinaryString(a) + " (" + a + ")");
        System.out.println("b = " + Integer.toBinaryString(b) + " (" + b + ")");
        System.out.println("a & b  (AND): " + Integer.toBinaryString(a & b) + " (" + (a & b) + ")");
        System.out.println("a | b  (OR):  " + Integer.toBinaryString(a | b) + " (" + (a | b) + ")");
        System.out.println("a ^ b  (XOR): " + Integer.toBinaryString(a ^ b) + " (" + (a ^ b) + ")");
        System.out.println("~a     (NOT): " + Integer.toBinaryString(~a) + " (" + (~a) + ")");

        // Shift operators
        System.out.println("\nShift Operators:");
        int val = 16;
        System.out.println("val = " + Integer.toBinaryString(val) + " (" + val + ")");
        System.out.println("val << 2 (left shift):  " + Integer.toBinaryString(val << 2) + " (" + (val << 2) + ")");
        System.out.println("val >> 2 (right shift): " + Integer.toBinaryString(val >> 2) + " (" + (val >> 2) + ")");
        System.out.println("val >>> 2 (unsigned right shift): " + (val >>> 2));

        // Bitwise tricks
        System.out.println("\nBitwise Tricks:");
        System.out.println("Check if even: (5 & 1) == 0 -> " + ((5 & 1) == 0));
        System.out.println("Check if odd:  (5 & 1) == 1 -> " + ((5 & 1) == 1));
        System.out.println("Swap without temp: a ^= b; b ^= a; a ^= b");

        // Practical use case: bit flags
        int READ = 1;     // 001
        int WRITE = 2;    // 010
        int EXECUTE = 4;  // 100

        int permissions = READ | WRITE; // Grant read and write
        System.out.println("\nPermissions: " + Integer.toBinaryString(permissions));
        System.out.println("Has READ:     " + ((permissions & READ) != 0));
        System.out.println("Has WRITE:    " + ((permissions & WRITE) != 0));
        System.out.println("Has EXECUTE:  " + ((permissions & EXECUTE) != 0));

        permissions |= EXECUTE; // Add execute
        System.out.println("After adding EXECUTE: " + Integer.toBinaryString(permissions));
        permissions &= ~WRITE;  // Remove write
        System.out.println("After removing WRITE: " + Integer.toBinaryString(permissions));
        System.out.println();
    }

    // --- Ternary Operator ---

    /**
     * Demonstrates the ternary (conditional) operator.
     */
    public static void demoTernaryOperator() {
        System.out.println("--- Ternary Operator ---");

        int age = 20;
        String status = (age >= 18) ? "Adult" : "Minor";
        System.out.println("Age " + age + " -> " + status);

        // Nested ternary (use sparingly)
        int score = 85;
        String grade = (score >= 90) ? "A" :
                        (score >= 80) ? "B" :
                        (score >= 70) ? "C" :
                        (score >= 60) ? "D" : "F";
        System.out.println("Score " + score + " -> Grade: " + grade);

        // Finding max/min
        int a = 10, b = 20;
        int max = (a > b) ? a : b;
        System.out.println("Max of " + a + " and " + b + ": " + max);

        // Null-safe usage
        String name = null;
        String displayName = (name != null) ? name : "Unknown";
        System.out.println("Display name: " + displayName);
        System.out.println();
    }

    // --- Assignment Operators ---

    /**
     * Demonstrates compound assignment operators.
     */
    public static void demoAssignmentOperators() {
        System.out.println("--- Assignment Operators ---");

        int x = 10;
        System.out.println("x = " + x);

        x += 5;   // x = x + 5
        System.out.println("x += 5 -> " + x);

        x -= 3;   // x = x - 3
        System.out.println("x -= 3 -> " + x);

        x *= 2;   // x = x * 2
        System.out.println("x *= 2 -> " + x);

        x /= 4;   // x = x / 4
        System.out.println("x /= 4 -> " + x);

        x %= 3;   // x = x % 3
        System.out.println("x %= 3 -> " + x);

        x &= 0b1111;  // x = x & 0b1111
        System.out.println("x &= 0b1111 -> " + x + " (" + Integer.toBinaryString(x) + ")");

        x |= 0b0010;  // x = x | 0b0010
        System.out.println("x |= 0b0010 -> " + x + " (" + Integer.toBinaryString(x) + ")");

        x ^= 0b0001;  // x = x ^ 0b0001
        System.out.println("x ^= 0b0001 -> " + x + " (" + Integer.toBinaryString(x) + ")");

        x <<= 2;  // x = x << 2
        System.out.println("x <<= 2 -> " + x + " (" + Integer.toBinaryString(x) + ")");

        x >>= 1;  // x = x >> 1
        System.out.println("x >>= 1 -> " + x + " (" + Integer.toBinaryString(x) + ")");
        System.out.println();
    }

    // --- instanceof Pattern Matching (Java 16+) ---

    /**
     * Demonstrates instanceof with pattern matching for type checking.
     */
    public static void demoInstanceofPatternMatching() {
        System.out.println("--- instanceof Pattern Matching (Java 16+) ---");

        Object obj = "Hello, Pattern Matching!";

        // Traditional instanceof
        if (obj instanceof String) {
            String s = (String) obj;
            System.out.println("Traditional: " + s.toUpperCase());
        }

        // Pattern matching instanceof (Java 16+)
        if (obj instanceof String s) {
            System.out.println("Pattern matching: " + s.toUpperCase());
        }

        // Pattern matching with conditions
        Object num = 42;
        if (num instanceof Integer i && i > 10) {
            System.out.println("Integer greater than 10: " + i);
        }

        // Demonstrating sealed class pattern matching (Java 17+)
        Shape circle = new Circle(5.0);
        Shape rectangle = new Rectangle(4.0, 6.0);

        System.out.println("\nShape area calculations:");
        printShapeInfo(circle);
        printShapeInfo(rectangle);

        // Null handling - pattern matching doesn't match null
        Object nullObj = null;
        if (nullObj instanceof String s) {
            System.out.println("This won't print: " + s);
        } else {
            System.out.println("null does not match any pattern");
        }
        System.out.println();
    }

    private static void printShapeInfo(Shape shape) {
        if (shape instanceof Circle c) {
            System.out.printf("Circle with radius %.1f, area = %.2f%n", c.radius(), c.radius() * c.radius() * Math.PI);
        } else if (shape instanceof Rectangle r) {
            System.out.printf("Rectangle %.1f x %.1f, area = %.2f%n", r.width(), r.height(), r.width() * r.height());
        }
    }

    // --- Operator Precedence ---

    /**
     * Demonstrates operator precedence (highest to lowest).
     */
    public static void demoOperatorPrecedence() {
        System.out.println("--- Operator Precedence ---");

        int result1 = 2 + 3 * 4;       // Multiplication first: 14
        int result2 = (2 + 3) * 4;     // Parentheses first: 20

        System.out.println("2 + 3 * 4 = " + result1 + " (multiplication before addition)");
        System.out.println("(2 + 3) * 4 = " + result2 + " (parentheses override precedence)");

        // Complex expression
        int a = 5, b = 3, c = 2;
        int result = a + b * c - a / b;
        System.out.println("\n5 + 3 * 2 - 5 / 3 = " + result);
        System.out.println("  Step 1: 3 * 2 = 6");
        System.out.println("  Step 2: 5 / 3 = 1 (integer division)");
        System.out.println("  Step 3: 5 + 6 - 1 = 10");
        System.out.println();
    }

    // --- Sealed class for instanceof demo ---

    public sealed interface Shape permits Circle, Rectangle {}

    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
}
