package academy.javaengineering.modern.switchexpressions;

import java.util.List;

/**
 * Switch expressions with real-world patterns.
 */
public class SwitchWithPatterns {

    public record Point(int x, int y) {}
    public sealed interface Shape permits Circle, Rectangle {}
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}

    public static void main(String[] args) {
        // Point classification
        System.out.println("=== Point Classification ===");
        Point[] points = {new Point(0, 0), new Point(5, 0), new Point(0, 5), new Point(3, 3)};
        for (Point p : points) {
            System.out.println(p + " -> " + classifyPoint(p));
        }

        // Shape area
        System.out.println("\n=== Shape Area ===");
        List<Shape> shapes = List.of(new Circle(5), new Rectangle(4, 6));
        for (Shape shape : shapes) {
            System.out.println(shape + " -> Area: " + calculateArea(shape));
        }

        // HTTP status
        System.out.println("\n=== HTTP Status ===");
        int[] statusCodes = {200, 301, 404, 500};
        for (int code : statusCodes) {
            System.out.println("Code " + code + " -> " + getStatusDescription(code));
        }

        // Color parsing
        System.out.println("\n=== Color Parsing ===");
        String[] colors = {"red", "green", "blue", "unknown"};
        for (String color : colors) {
            System.out.println("Color: " + color + " -> " + parseColor(color));
        }

        // Calculator
        System.out.println("\n=== Calculator ===");
        System.out.println("10 + 5 = " + calculate(10, 5, '+'));
        System.out.println("10 - 5 = " + calculate(10, 5, '-'));
        System.out.println("10 * 5 = " + calculate(10, 5, '*'));
        System.out.println("10 / 5 = " + calculate(10, 5, '/'));
    }

    static String classifyPoint(Point p) {
        return switch (p) {
            case Point(int x, int y) && x == 0 && y == 0 -> "Origin";
            case Point(int x, int y) && x == 0 -> "On Y-axis";
            case Point(int x, int y) && y == 0 -> "On X-axis";
            case Point(int x, int y) && x == y -> "On diagonal";
            case Point(int x, int y) -> "Regular point";
        };
    }

    static double calculateArea(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
        };
    }

    static String getStatusDescription(int code) {
        return switch (code) {
            case 200 -> "OK";
            case 301 -> "Moved Permanently";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown status code";
        };
    }

    static String parseColor(String color) {
        return switch (color.toLowerCase()) {
            case "red", "r" -> "#FF0000";
            case "green", "g" -> "#00FF00";
            case "blue", "b" -> "#0000FF";
            case null -> "No color";
            default -> "Unknown color";
        };
    }

    static double calculate(double a, double b, char operator) {
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }
}
