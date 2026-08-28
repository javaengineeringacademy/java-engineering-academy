package academy.javaengineering.modern.recordpatterns;

/**
 * Basic record pattern examples.
 */
public class BasicRecordPatternExample {

    record Point(int x, int y) {}
    record Line(Point start, Point end) {}
    record Rectangle(Point topLeft, Point bottomRight) {}
    record Circle(Point center, double radius) {}

    public static void main(String[] args) {
        // Basic record pattern
        System.out.println("=== Basic Record Pattern ===");
        Point point = new Point(10, 20);
        if (point instanceof Point(int x, int y)) {
            System.out.println("Point coordinates: (" + x + "," + y + ")");
        }

        // Record pattern with guards
        System.out.println("\n=== Record Pattern with Guards ===");
        Point[] points = {new Point(5, 5), new Point(-3, 4), new Point(0, 0), new Point(7, -2)};
        for (Point p : points) {
            if (p instanceof Point(int x, int y) && x > 0 && y > 0) {
                System.out.println("First quadrant: (" + x + "," + y + ")");
            } else if (p instanceof Point(int x, int y) && x > 0) {
                System.out.println("Positive x: (" + x + "," + y + ")");
            } else if (p instanceof Point(int x, int y)) {
                System.out.println("Other: (" + x + "," + y + ")");
            }
        }

        // Nested record patterns
        System.out.println("\n=== Nested Record Patterns ===");
        Line line = new Line(new Point(0, 0), new Point(10, 10));
        if (line instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
            System.out.println("Line from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
        }

        // Rectangle with nested points
        System.out.println("\n=== Rectangle with Nested Points ===");
        Rectangle rect = new Rectangle(new Point(0, 0), new Point(5, 3));
        if (rect instanceof Rectangle(Point(int x1, int y1), Point(int x2, int y2))) {
            int width = x2 - x1;
            int height = y2 - y1;
            System.out.println("Rectangle: " + width + "x" + height);
        }

        // Circle with nested point
        System.out.println("\n=== Circle with Nested Point ===");
        Circle circle = new Circle(new Point(5, 5), 3.0);
        if (circle instanceof Circle(Point(int cx, int cy), double r)) {
            System.out.println("Circle at (" + cx + "," + cy + ") with radius " + r);
        }

        // Record pattern in switch
        System.out.println("\n=== Record Pattern in Switch ===");
        Object[] shapes = {
            new Point(1, 2),
            new Line(new Point(0, 0), new Point(5, 5)),
            new Circle(new Point(3, 3), 2.0)
        };
        for (Object shape : shapes) {
            String description = switch (shape) {
                case Point(int x, int y) -> "Point at (" + x + "," + y + ")";
                case Line(Point(int x1, int y1), Point(int x2, int y2)) -> 
                    "Line from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")";
                case Circle(Point(int cx, int cy), double r) -> 
                    "Circle at (" + cx + "," + cy + ") radius " + r;
                default -> "Unknown shape";
            };
            System.out.println(description);
        }
    }
}
