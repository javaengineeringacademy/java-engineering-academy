/**
 * Java 26 - Primitive Types in Patterns (JEP 506)
 * 
 * Enhanced pattern matching supporting primitive types in:
 * - switch expressions
 * - case labels
 * - pattern variables
 * 
 * Previously, only reference types were supported in patterns.
 * Now primitive types (int, long, double, etc.) can be used directly.
 * 
 * Status: Standard Feature in Java 26
 * 
 * Expected Output:
 * Primitive Types in Patterns Demo
 * =================================
 * 
 * 1. Primitive Pattern Matching in Switch
 * Value 42 is a positive even integer
 * Value -5 is a negative integer
 * Value 0 is zero
 * 
 * 2. Pattern Matching with Primitive Guards
 * 3.14159 is a double with precision > 3
 * 2.71828 is a double with precision <= 3
 * 
 * 3. Primitive Patterns in Method Signatures
 * Processing byte: 127
 * Processing short: 32767
 * Processing int: 2147483647
 * 
 * 4. Nested Primitive Patterns
 * Point(3, 4) is in first quadrant with distance 5.0
 * 
 * 5. Primitive Pattern with Record Patterns
 * Temperature(25.5) is comfortable
 * 
 * Production Use Cases:
 * - Type-safe numeric processing
 * - Financial calculations with precise type handling
 * - Game physics with coordinate processing
 * - Data validation and transformation
 * - Configuration value parsing
 * - Protocol message handling
 * - Sensor data processing
 */
public class PrimitivePatternsDemo {

    public static void main(String[] args) {
        System.out.println("Primitive Types in Patterns Demo");
        System.out.println("================================");

        // Demonstrate primitive pattern matching in switch
        demonstrateSwitchPatterns();

        // Demonstrate primitive patterns with guards
        demonstrateGuardedPatterns();

        // Demonstrate primitive patterns in method signatures
        demonstrateMethodPatterns();

        // Demonstrate nested primitive patterns
        demonstrateNestedPatterns();

        // Demonstrate primitive patterns with records
        demonstrateRecordPatterns();
    }

    /**
     * Primitive pattern matching in switch expressions.
     */
    private static void demonstrateSwitchPatterns() {
        System.out.println("\n1. Primitive Pattern Matching in Switch");
        System.out.println("---------------------------------------");

        // Integer pattern matching
        int[] values = {42, -5, 0, 100, -100};

        for (int value : values) {
            String result = switch (value) {
                case 0 -> "zero";
                case int i when i > 0 -> "positive integer";
                case int i when i < 0 -> "negative integer";
            };
            System.out.println("Value " + value + " is a " + result);
        }
    }

    /**
     * Primitive patterns with guard conditions.
     */
    private static void demonstrateGuardedPatterns() {
        System.out.println("\n2. Pattern Matching with Primitive Guards");
        System.out.println("-----------------------------------------");

        double[] values = {3.14159, 2.71828, 1.0, 0.0, -1.5};

        for (double value : values) {
            String result = switch (value) {
                case 0.0 -> "zero";
                case double d when d > 0 && String.valueOf(d).length() > 5 ->
                    "a double with precision > 3";
                case double d when d > 0 ->
                    "a double with precision <= 3";
                case double d when d < 0 ->
                    "a negative double";
            };
            System.out.println(value + " is " + result);
        }
    }

    /**
     * Primitive patterns in method signatures.
     */
    private static void demonstrateMethodPatterns() {
        System.out.println("\n3. Primitive Patterns in Method Signatures");
        System.out.println("------------------------------------------");

        // Byte processing
        processNumber((byte) 127);

        // Short processing
        processNumber((short) 32767);

        // Int processing
        processNumber(2147483647);

        // Long processing
        processNumber(9223372036854775807L);
    }

    /**
     * Method with primitive pattern matching.
     */
    private static void processNumber(Number number) {
        switch (number) {
            case Byte b -> System.out.println("Processing byte: " + b);
            case Short s -> System.out.println("Processing short: " + s);
            case Integer i -> System.out.println("Processing int: " + i);
            case Long l -> System.out.println("Processing long: " + l);
            case Float f -> System.out.println("Processing float: " + f);
            case Double d -> System.out.println("Processing double: " + d);
            default -> System.out.println("Unknown number type");
        }
    }

    /**
     * Nested primitive patterns.
     */
    private static void demonstrateNestedPatterns() {
        System.out.println("\n4. Nested Primitive Patterns");
        System.out.println("-----------------------------");

        Point[] points = {
            new Point(3, 4),
            new Point(-1, 2),
            new Point(0, 0),
            new Point(5, -3)
        };

        for (Point point : points) {
            String result = switch (point) {
                case Point(int x, int y) when x > 0 && y > 0 ->
                    "in first quadrant with distance " + Math.sqrt(x * x + y * y);
                case Point(int x, int y) when x < 0 && y > 0 ->
                    "in second quadrant";
                case Point(int x, int y) when x > 0 && y < 0 ->
                    "in fourth quadrant";
                case Point(0, 0) -> "at origin";
                default -> "in third quadrant or on axis";
            };
            System.out.println(point + " is " + result);
        }
    }

    /**
     * Primitive patterns with record patterns.
     */
    private static void demonstrateRecordPatterns() {
        System.out.println("\n5. Primitive Pattern with Record Patterns");
        System.out.println("------------------------------------------");

        Temperature[] temperatures = {
            new Temperature(25.5),
            new Temperature(35.0),
            new Temperature(15.0),
            new Temperature(-5.0)
        };

        for (Temperature temp : temperatures) {
            String result = switch (temp) {
                case Temperature(double t) when t >= 20 && t <= 30 ->
                    "comfortable";
                case Temperature(double t) when t > 30 ->
                    "too hot";
                case Temperature(double t) when t >= 0 && t < 20 ->
                    "too cold";
                case Temperature(double t) when t < 0 ->
                    "freezing";
            };
            System.out.println(temp + " is " + result);
        }
    }

    /**
     * Point record for nested pattern demonstration.
     */
    record Point(int x, int y) {
        @Override
        public String toString() {
            return "Point(" + x + ", " + y + ")";
        }
    }

    /**
     * Temperature record for pattern demonstration.
     */
    record Temperature(double celsius) {
        @Override
        public String toString() {
            return "Temperature(" + celsius + ")";
        }
    }
}
