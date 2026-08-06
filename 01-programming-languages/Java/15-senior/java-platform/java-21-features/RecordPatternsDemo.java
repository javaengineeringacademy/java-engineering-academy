package academy.javaengineering.senior.java21;

import java.util.List;
import java.util.Arrays;

public class RecordPatternsDemo {

    record Point(int x, int y) {}
    record Line(Point start, Point end) {}
    record Rectangle(Point topLeft, Point bottomRight) {}
    record NamedRectangle(String name, Rectangle rect) {}

    sealed interface Shape permits Circle, Triangle, Composite {}
    record Circle(double radius) implements Shape {}
    record Triangle(Point a, Point b, Point c) implements Shape {}
    record Composite(List<Shape> shapes) implements Shape {}

    public static void main(String[] args) {
        deconstructionPatterns();
        nestedRecordPatterns();
        arrayPatterns();
        sealedTypePatterns();
    }

    // ==================== Deconstruction Patterns ====================

    static void deconstructionPatterns() {
        System.out.println("=== Deconstruction Patterns ===\n");

        Point p1 = new Point(3, 4);
        Point p2 = new Point(7, 10);

        if (p1 instanceof Point(int x, int y)) {
            System.out.println("Point at (" + x + ", " + y + ")");
        }

        if (p1 instanceof Point(int x, int y) && x == 3 && y == 4) {
            System.out.println("Confirmed: Point(3, 4)");
        }

        Line line = new Line(p1, p2);
        if (line instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
            System.out.println("Line from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")");
        }

        int dx = line instanceof Line(Point(int x1, _), Point(int x2, _)) ? x2 - x1 : 0;
        System.out.println("Horizontal distance: " + dx);
    }

    // ==================== Nested Record Patterns ====================

    static void nestedRecordPatterns() {
        System.out.println("\n=== Nested Record Patterns ===\n");

        Rectangle rect = new Rectangle(new Point(0, 0), new Point(100, 50));

        String desc = switch (rect) {
            case Rectangle(Point(int x1, int y1), Point(int x2, int y2))
                when x1 == 0 && y1 == 0 -> "Origin-anchored rect: " + (x2 * y2) + " sq units";
            case Rectangle(Point(int x1, _), Point(int x2, int y2))
                when x2 - x1 == y2 -> "Square of side " + (x2 - x1);
            default -> "Rectangle";
        };
        System.out.println(desc);

        NamedRectangle named = new NamedRectangle(
            "viewport",
            new Rectangle(new Point(10, 20), new Point(110, 70))
        );

        if (named instanceof NamedRectangle(String n, Rectangle(Point(int x1, int y1), Point(int x2, int y2)))) {
            System.out.printf("Named rect '%s': (%d,%d)-(%d,%d), area=%d%n",
                n, x1, y1, x2, y2, (x2 - x1) * (y2 - y1));
        }
    }

    // ==================== Array Patterns ====================

    static void arrayPatterns() {
        System.out.println("\n=== Array Patterns ===\n");

        Object[] array1 = {1, "hello", 3.14};
        Object[] array2 = {"first", "second"};
        Object[] array3 = {42};

        for (Object[] arr : List.of(array1, array2, array3)) {
            String result = switch (arr) {
                case Object[Integer i, String s, Double d] ->
                    "Three elements: int=" + i + ", str=" + s + ", dbl=" + d;
                case Object[String s1, String s2] ->
                    "Two strings: " + s1 + ", " + s2;
                case Object[Integer i] ->
                    "Single int: " + i;
                case Object[] o && o.length == 0 ->
                    "Empty array";
                default ->
                    "Array of " + arr.length + " elements";
            };
            System.out.println("  " + result);
        }

        int[][] matrix = {{1, 2}, {3, 4}, {5, 6}};
        for (int[] row : matrix) {
            String rowStr = switch (row) {
                case int[int a, int b] -> "[" + a + ", " + b + "]";
                case int[int a] -> "[" + a + "]";
                default -> "[]";
            };
            System.out.println("  Row: " + rowStr);
        }
    }

    // ==================== Sealed Type Patterns ====================

    static void sealedTypePatterns() {
        System.out.println("\n=== Sealed Type Patterns ===\n");

        Shape[] shapes = {
            new Circle(5.0),
            new Triangle(new Point(0, 0), new Point(10, 0), new Point(5, 8)),
            new Composite(List.of(new Circle(2.0), new Circle(3.0)))
        };

        for (Shape shape : shapes) {
            System.out.println("  " + describeShape(shape));
        }
    }

    static String describeShape(Shape shape) {
        return switch (shape) {
            case Circle(double r) -> "Circle with radius " + r;
            case Triangle(Point a, Point b, Point c) ->
                "Triangle: " + a + " -> " + b + " -> " + c;
            case Composite(List<Shape> shapes) && shapes.isEmpty() ->
                "Empty composite";
            case Composite(List<Shape> shapes) ->
                "Composite with " + shapes.size() + " shapes";
        };
    }
}
