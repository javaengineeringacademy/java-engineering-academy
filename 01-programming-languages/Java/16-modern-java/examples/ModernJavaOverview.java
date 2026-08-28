package academy.javaengineering.modern;

import java.util.List;

/**
 * Root-level examples demonstrating modern Java features overview.
 */
public class ModernJavaOverview {

    /**
     * Demonstrates combining multiple modern features.
     */
    public static void main(String[] args) {
        System.out.println("=== Modern Java Features Overview ===\n");

        // Records (Java 16)
        record Point(int x, int y) {}
        var point = new Point(10, 20);
        System.out.println("Record: " + point);

        // var (Java 10)
        var message = "Hello, Modern Java!";
        var numbers = List.of(1, 2, 3, 4, 5);
        System.out.println("var inference: " + message);
        System.out.println("var with collections: " + numbers);

        // Text Blocks (Java 15)
        String json = """
                {
                    "name": "Java",
                    "version": 17,
                    "features": ["records", "sealed", "patterns"]
                }
                """;
        System.out.println("\nText Block:\n" + json);

        // Pattern Matching for switch (Java 21)
        Object obj = "Hello";
        String result = switch (obj) {
            case Integer i -> "Integer: " + i;
            case String s -> "String: " + s;
            case null -> "null";
            default -> "Unknown";
        };
        System.out.println("\nPattern switch: " + result);

        // Switch Expressions (Java 14)
        int dayOfWeek = 3;
        String dayName = switch (dayOfWeek) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6, 7 -> "Weekend";
            default -> throw new IllegalArgumentException("Invalid day: " + dayOfWeek);
        };
        System.out.println("Switch expression: " + dayName);

        // Sealed Classes (Java 17)
        sealed interface Shape permits Circle, Rectangle {}
        record Circle(double radius) implements Shape {}
        record Rectangle(double width, double height) implements Shape {}

        Shape shape = new Circle(5.0);
        String shapeInfo = switch (shape) {
            case Circle c -> "Circle with radius " + c.radius();
            case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
        };
        System.out.println("\nSealed + Pattern: " + shapeInfo);

        // Multi-catch (Java 7)
        try {
            riskyOperation();
        } catch (IllegalArgumentException | ArithmeticException e) {
            System.out.println("\nMulti-catch: " + e.getClass().getSimpleName());
        }

        // instanceof Pattern Matching (Java 16)
        if (shape instanceof Circle c && c.radius() > 3) {
            System.out.println("instanceof pattern: Large circle with radius " + c.radius());
        }

        System.out.println("\n=== All features demonstrated ===");
    }

    static void riskyOperation() {
        throw new ArithmeticException("Demo exception");
    }
}
