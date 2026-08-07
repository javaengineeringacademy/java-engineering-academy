/**
 * SwitchExpressionsDemo.java
 *
 * Demonstrates switch expressions in Java 14+.
 * Switch expressions provide arrow syntax, yield, and exhaustiveness checks.
 *
 * Compile with: javac SwitchExpressionsDemo.java
 * Run with: java SwitchExpressionsDemo
 *
 * Expected Output:
 * === Switch Expressions (Java 14+) ===
 *
 * --- 1. Arrow Syntax (Java 14+) ---
 * Monday is a weekday
 * Saturday is a weekend
 * Sunday is a weekend
 *
 * --- 2. Switch Expression with Yield ---
 * Processing...
 * Result from block: 42
 *
 * --- 3. Exhaustive Switch (Java 17) ---
 * Value is ONE
 * Value is TWO
 * Value is THREE
 * Unknown value
 *
 * --- 4. Pattern Matching in Switch (Java 17) ---
 * String length: 5
 * Integer value: 42
 * Double value: 3.14
 * Boolean value: true
 * Unknown type
 *
 * --- 5. Switch with Enums ---
 * Spring: season of renewal
 * Summer: season of growth
 * Autumn: season of harvest
 * Winter: season of rest
 *
 * --- 6. Switch with Strings ---
 * apple: fruit
 * carrot: vegetable
 * beef: protein
 * Unknown: unknown food group
 *
 * --- 7. Switch with Records ---
 * Point at (0, 0): origin
 * Point at (5, 0): on x-axis
 * Point at (0, 5): on y-axis
 * Point at (3, 4): somewhere else
 *
 * --- 8. Switch with Sealed Classes ---
 * Shape is Circle with radius 5.0
 * Shape is Rectangle with width 4.0 and height 6.0
 * Shape is Triangle with base 3.0 and height 8.0
 */
public class SwitchExpressionsDemo {

    // =====================================================
    // 1. Arrow Syntax (Java 14+)
    // =====================================================
    // Arrow syntax provides concise switch expressions

    static void arrowSyntax() {
        System.out.println("--- 1. Arrow Syntax (Java 14+) ---");

        String[] days = {"Monday", "Saturday", "Sunday"};

        for (String day : days) {
            // Arrow syntax: no fall-through, no break needed
            String type = switch (day) {
                case "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" -> "weekday";
                case "Saturday", "Sunday" -> "weekend";
                default -> "unknown";
            };
            System.out.println(day + " is a " + type);
        }

        System.out.println();
    }

    // =====================================================
    // 2. Switch Expression with Yield
    // =====================================================
    // Yield allows you to return a value from a switch block

    static void switchWithYield() {
        System.out.println("--- 2. Switch Expression with Yield ---");

        int value = 42;

        // Switch expression with yield
        String result = switch (value) {
            case 0 -> "zero";
            case 1 -> "one";
            default -> {
                // Multi-line block with yield
                System.out.println("Processing...");
                yield "Result from block: " + value;
            }
        };

        System.out.println(result);
        System.out.println();
    }

    // =====================================================
    // 3. Exhaustive Switch (Java 17)
    // =====================================================
    // Java 17 ensures exhaustive switching on sealed types

    public enum Value {
        ONE, TWO, THREE
    }

    static void exhaustiveSwitch() {
        System.out.println("--- 3. Exhaustive Switch (Java 17) ---");

        Value[] values = {Value.ONE, Value.TWO, Value.THREE, null};

        for (Value v : values) {
            // Exhaustive switch - compiler ensures all cases are handled
            String result = switch (v) {
                case ONE -> "Value is ONE";
                case TWO -> "Value is TWO";
                case THREE -> "Value is THREE";
                case null -> "Unknown value";
            };
            System.out.println(result);
        }

        System.out.println();
    }

    // =====================================================
    // 4. Pattern Matching in Switch (Java 17)
    // =====================================================
    // Pattern matching allows type checking in switch

    static void patternMatchingInSwitch() {
        System.out.println("--- 4. Pattern Matching in Switch (Java 17) ---");

        Object[] objects = {"Hello", 42, 3.14, true, null};

        for (Object obj : objects) {
            // Pattern matching in switch
            String result = switch (obj) {
                case String s -> "String length: " + s.length();
                case Integer i -> "Integer value: " + i;
                case Double d -> "Double value: " + d;
                case Boolean b -> "Boolean value: " + b;
                case null -> "Unknown type";
                default -> "Other type: " + obj.getClass().getSimpleName();
            };
            System.out.println(result);
        }

        System.out.println();
    }

    // =====================================================
    // 5. Switch with Enums
    // =====================================================
    // Enums work naturally with switch expressions

    public enum Season {
        SPRING, SUMMER, AUTUMN, WINTER
    }

    static void switchWithEnums() {
        System.out.println("--- 5. Switch with Enums ---");

        Season[] seasons = {Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER};

        for (Season season : seasons) {
            // Switch with enum
            String description = switch (season) {
                case SPRING -> "season of renewal";
                case SUMMER -> "season of growth";
                case AUTUMN -> "season of harvest";
                case WINTER -> "season of rest";
            };
            System.out.println(season + " is a " + description);
        }

        System.out.println();
    }

    // =====================================================
    // 6. Switch with Strings
    // =====================================================
    // Strings work with switch expressions

    static void switchWithStrings() {
        System.out.println("--- 6. Switch with Strings ---");

        String[] foods = {"apple", "carrot", "beef", "unknown"};

        for (String food : foods) {
            // Switch with strings
            String group = switch (food) {
                case "apple", "banana", "orange" -> "fruit";
                case "carrot", "potato", "onion" -> "vegetable";
                case "beef", "chicken", "pork" -> "protein";
                default -> "unknown food group";
            };
            System.out.println(food + ": " + group);
        }

        System.out.println();
    }

    // =====================================================
    // 7. Switch with Records
    // =====================================================
    // Records can be used with pattern matching in switch

    public record Point(double x, double y) {
    }

    static void switchWithRecords() {
        System.out.println("--- 7. Switch with Records ---");

        Point[] points = {
                new Point(0, 0),
                new Point(5, 0),
                new Point(0, 5),
                new Point(3, 4)
        };

        for (Point point : points) {
            // Pattern matching with records in switch
            String location = switch (point) {
                case Point(0, 0) -> "origin";
                case Point(double x, 0) -> "on x-axis";
                case Point(0, double y) -> "on y-axis";
                default -> "somewhere else";
            };
            System.out.printf("Point at (%.0f, %.0f): %s%n", point.x(), point.y(), location);
        }

        System.out.println();
    }

    // =====================================================
    // 8. Switch with Sealed Classes
    // =====================================================
    // Sealed classes enable exhaustive pattern matching

    public sealed interface Shape permits Circle, Rectangle, Triangle {
        double area();
    }

    public record Circle(double radius) implements Shape {
        @Override
        public double area() {
            return Math.PI * radius * radius;
        }
    }

    public record Rectangle(double width, double height) implements Shape {
        @Override
        public double area() {
            return width * height;
        }
    }

    public record Triangle(double base, double height) implements Shape {
        @Override
        public double area() {
            return 0.5 * base * height;
        }
    }

    static void switchWithSealedClasses() {
        System.out.println("--- 8. Switch with Sealed Classes ---");

        Shape[] shapes = {
                new Circle(5.0),
                new Rectangle(4.0, 6.0),
                new Triangle(3.0, 8.0)
        };

        for (Shape shape : shapes) {
            // Exhaustive switch with sealed classes
            String description = switch (shape) {
                case Circle c -> String.format("Shape is Circle with radius %.1f", c.radius());
                case Rectangle r -> String.format("Shape is Rectangle with width %.1f and height %.1f",
                        r.width(), r.height());
                case Triangle t -> String.format("Shape is Triangle with base %.1f and height %.1f",
                        t.base(), t.height());
            };
            System.out.println(description);
        }

        System.out.println();
    }

    // =====================================================
    // Main Method
    // =====================================================

    public static void main(String[] args) {
        System.out.println("=== Switch Expressions (Java 14+) ===\n");

        arrowSyntax();
        switchWithYield();
        exhaustiveSwitch();
        patternMatchingInSwitch();
        switchWithEnums();
        switchWithStrings();
        switchWithRecords();
        switchWithSealedClasses();

        System.out.println("\n=== Complete ===");
    }
}
