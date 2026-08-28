package academy.javaengineering.modern.pattern;

import java.util.List;

/**
 * Solutions for Pattern Matching practice exercises.
 */
public class PatternMatchingSolutions {

    // Exercise 1: Shape Calculator
    public sealed interface Shape permits Circle, Rectangle, Triangle {}
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
    public record Triangle(double base, double height) implements Shape {}

    public static double area(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
            case Triangle t -> 0.5 * t.base() * t.height();
        };
    }

    public static double perimeter(Shape shape) {
        return switch (shape) {
            case Circle c -> 2 * Math.PI * c.radius();
            case Rectangle r -> 2 * (r.width() + r.height());
            case Triangle t -> t.base() + t.height() + Math.sqrt(t.base() * t.base() + t.height() * t.height());
        };
    }

    public static String describe(Shape shape) {
        return switch (shape) {
            case Circle c -> "Circle with radius " + c.radius();
            case Rectangle r -> "Rectangle " + r.width() + "x" + r.height();
            case Triangle t -> "Triangle " + t.base() + "x" + t.height();
        };
    }

    // Exercise 2: Object Classifier
    public static String classify(Object obj) {
        return switch (obj) {
            case String s && s.length() < 5 -> "Short string";
            case String s && s.length() < 10 -> "Medium string";
            case String s -> "Long string";
            case Integer i && i < 0 -> "Negative integer";
            case Integer i && i == 0 -> "Zero";
            case Integer i -> "Positive integer";
            case List<?> list && list.isEmpty() -> "Empty list";
            case List<?> list && list.size() == 1 -> "Single element list";
            case List<?> list -> "Multiple element list";
            case null -> "Null";
            default -> "Unknown";
        };
    }

    // Exercise 3: Expression Evaluator
    public sealed interface Expression permits Num, Add, Mul, Pow {}
    public record Num(double value) implements Expression {}
    public record Add(Expression left, Expression right) implements Expression {}
    public record Mul(Expression left, Expression right) implements Expression {}
    public record Pow(Expression base, Expression exponent) implements Expression {}

    public static double eval(Expression expr) {
        return switch (expr) {
            case Num n -> n.value();
            case Add a -> eval(a.left()) + eval(a.right());
            case Mul m -> eval(m.left()) * eval(m.right());
            case Pow p -> Math.pow(eval(p.base()), eval(p.exponent()));
        };
    }

    // Exercise 4: HTTP Status Handler
    public sealed interface HttpStatus permits Success, Redirect, ClientError, ServerError {}
    public record Success(int code, String body) implements HttpStatus {}
    public record Redirect(String url) implements HttpStatus {}
    public record ClientError(int code, String message) implements HttpStatus {}
    public record ServerError(int code, String message) implements HttpStatus {}

    public static String handleStatus(HttpStatus status) {
        return switch (status) {
            case Success s -> "Success [" + s.code() + "]: " + s.body();
            case Redirect r -> "Redirect to: " + r.url();
            case ClientError ce -> "Client Error [" + ce.code() + "]: " + ce.message();
            case ServerError se -> "Server Error [" + se.code() + "]: " + se.message();
        };
    }

    public static void main(String[] args) {
        // Test Exercise 1
        System.out.println("--- Exercise 1: Shape Calculator ---");
        Shape circle = new Circle(5);
        Shape rectangle = new Rectangle(4, 6);
        Shape triangle = new Triangle(3, 8);
        System.out.println(describe(circle) + " - Area: " + area(circle));
        System.out.println(describe(rectangle) + " - Area: " + area(rectangle));
        System.out.println(describe(triangle) + " - Area: " + area(triangle));

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: Object Classifier ---");
        Object[] inputs = {"Hi", "Hello, World!", 42, -5, 0, List.of(), List.of(1), List.of(1, 2), null};
        for (Object input : inputs) {
            System.out.println(input + " -> " + classify(input));
        }

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Expression Evaluator ---");
        Expression expr = new Add(
            new Mul(new Num(2), new Num(3)),
            new Pow(new Num(2), new Num(3))
        );
        System.out.println("2 * 3 + 2^3 = " + eval(expr));

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: HTTP Status Handler ---");
        List<HttpStatus> statuses = List.of(
            new Success(200, "OK"),
            new Redirect("/login"),
            new ClientError(404, "Not Found"),
            new ServerError(500, "Internal Server Error")
        );
        statuses.forEach(s -> System.out.println(handleStatus(s)));
    }
}
