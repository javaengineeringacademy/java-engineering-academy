package academy.javaengineering.modern.records;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Record patterns and advanced record usage.
 */
public class RecordPatterns {

    public sealed interface Shape permits Circle, Rectangle, Triangle {}
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
    public record Triangle(double base, double height) implements Shape {}

    public record Point(int x, int y) {}
    public record Line(Point start, Point end) {}

    public static void main(String[] args) {
        // Record with other records
        var line = new Line(new Point(0, 0), new Point(10, 10));
        System.out.println("Line: " + line);
        System.out.println("Start: " + line.start());
        System.out.println("End: " + line.end());

        // Sealed interface with records
        List<Shape> shapes = List.of(
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 8),
            new Circle(2)
        );

        // Pattern matching with records (Java 21)
        System.out.println("\n--- Shape Areas ---");
        for (Shape shape : shapes) {
            double area = calculateArea(shape);
            System.out.println(shape + " area: " + area);
        }

        // Record with streams
        var points = List.of(
            new Point(1, 2),
            new Point(3, 4),
            new Point(5, 6)
        );

        int sumX = points.stream()
            .mapToInt(Point::x)
            .sum();
        int sumY = points.stream()
            .mapToInt(Point::y)
            .sum();

        System.out.println("\nSum of x: " + sumX);
        System.out.println("Sum of y: " + sumY);

        // Record in map
        var pointMap = points.stream()
            .collect(Collectors.toMap(
                p -> "(" + p.x() + "," + p.y() + ")",
                p -> p.x() + p.y()
            ));
        System.out.println("\nPoint map: " + pointMap);
    }

    static double calculateArea(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
            case Triangle t -> 0.5 * t.base() * t.height();
        };
    }
}
