package academy.javaengineering.modern.pattern;

/**
 * Basic pattern matching examples.
 */
public class BasicPatternExample {

    sealed interface Shape permits Circle, Rectangle, Triangle {}
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Triangle(double base, double height) implements Shape {}

    public static void main(String[] args) {
        // Basic type pattern
        Object obj = "Hello";
        String result = switch (obj) {
            case Integer i -> "Integer: " + i;
            case String s -> "String: " + s;
            case null -> "null";
            default -> "Unknown";
        };
        System.out.println("Basic pattern: " + result);

        // Pattern with sealed types
        Shape shape = new Circle(5);
        String description = switch (shape) {
            case Circle c -> "Circle with radius " + c.radius();
            case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
            case Triangle t -> "Triangle " + t.base() + "x" + t.height();
        };
        System.out.println("\nShape pattern: " + description);

        // Guarded pattern
        Object input = "Hello, World!";
        String lengthResult = switch (input) {
            case String s && s.length() > 10 -> "Long string: " + s.length();
            case String s -> "Short string: " + s.length();
            case Integer i -> "Integer: " + i;
            default -> "Other";
        };
        System.out.println("\nGuarded pattern: " + lengthResult);

        // Multiple patterns
        int number = 3;
        String category = switch (number) {
            case 1, 2, 3 -> "Small";
            case 4, 5, 6 -> "Medium";
            case 7, 8, 9 -> "Large";
            default -> "Unknown";
        };
        System.out.println("\nMultiple patterns: " + category);

        // Null handling
        Object nullObj = null;
        String nullResult = switch (nullObj) {
            case String s -> "String: " + s;
            case Integer i -> "Integer: " + i;
            case null -> "Null detected";
            default -> "Other";
        };
        System.out.println("\nNull pattern: " + nullResult);
    }
}
