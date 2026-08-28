package academy.javaengineering.modern.recordpatterns;

import java.util.List;

/**
 * Advanced record pattern usage.
 */
public class AdvancedRecordPatternExample {

    // Sealed hierarchy with records
    sealed interface Shape permits Circle, Rectangle, Triangle {}
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Triangle(double base, double height) implements Shape {}

    // Complex nested structures
    record Point(double x, double y) {}
    record Line(Point start, Point end) {}
    record Polygon(List<Point> vertices) {}

    // Result type
    sealed interface Result<T> permits Success, Failure {}
    record Success<T>(T value) implements Result<T> {}
    record Failure<T>(String error) implements Result<T> {}

    public static void main(String[] args) {
        // Pattern matching with sealed types
        System.out.println("=== Pattern Matching with Sealed Types ===");
        List<Shape> shapes = List.of(
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 8),
            new Circle(2)
        );

        for (Shape shape : shapes) {
            String description = switch (shape) {
                case Circle(double r) && r > 3 -> "Large circle: radius=" + r;
                case Circle(double r) -> "Small circle: radius=" + r;
                case Rectangle(double w, double h) && w == h -> "Square: side=" + w;
                case Rectangle(double w, double h) -> "Rectangle: " + w + "x" + h;
                case Triangle(double b, double h) -> "Triangle: " + b + "x" + h;
            };
            System.out.println(description);
        }

        // Nested record patterns
        System.out.println("\n=== Nested Record Patterns ===");
        Line line = new Line(new Point(0, 0), new Point(10, 10));
        if (line instanceof Line(Point(double x1, double y1), Point(double x2, double y2))) {
            double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
            System.out.printf("Line length: %.2f%n", length);
        }

        // Result type pattern matching
        System.out.println("\n=== Result Type Pattern Matching ===");
        List<Result<Integer>> results = List.of(
            new Success<>(42),
            new Failure<>("Error 1"),
            new Success<>(100),
            new Failure<>("Error 2")
        );

        for (Result<Integer> result : results) {
            if (result instanceof Success<Integer>(int value)) {
                System.out.println("Success: " + value);
            } else if (result instanceof Failure<Integer>(String error)) {
                System.out.println("Failure: " + error);
            }
        }

        // Complex nested structures
        System.out.println("\n=== Complex Nested Structures ===");
        Polygon polygon = new Polygon(List.of(
            new Point(0, 0),
            new Point(5, 0),
            new Point(5, 5),
            new Point(0, 5)
        ));

        if (polygon instanceof Polygon(List<Point> vertices) && vertices.size() == 4) {
            System.out.println("Quadrilateral with vertices:");
            for (Point vertex : vertices) {
                System.out.printf("  (%.1f, %.1f)%n", vertex.x(), vertex.y());
            }
        }
    }

    // Helper method to calculate area based on shape type
    static double calculateArea(Shape shape) {
        return switch (shape) {
            case Circle(double r) -> Math.PI * r * r;
            case Rectangle(double w, double h) -> w * h;
            case Triangle(double b, double h) -> 0.5 * b * h;
        };
    }
}
