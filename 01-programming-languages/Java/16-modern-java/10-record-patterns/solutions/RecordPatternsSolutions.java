package academy.javaengineering.modern.recordpatterns;

import java.util.List;
import java.util.Map;

/**
 * Solutions for Record Patterns practice exercises.
 */
public class RecordPatternsSolutions {

    // Exercise 1: Geometry Calculator
    record Point(double x, double y) {}
    record Circle(Point center, double radius) {}
    record Rectangle(Point topLeft, Point bottomRight) {}
    record Triangle(Point a, Point b, Point c) {}

    public static double calculateArea(Object shape) {
        return switch (shape) {
            case Circle(Point var center, double r) -> Math.PI * r * r;
            case Rectangle(Point(double x1, double y1), Point(double x2, double y2)) -> 
                Math.abs(x2 - x1) * Math.abs(y2 - y1);
            case Triangle(Point(double x1, double y1), Point(double x2, double y2), Point(double x3, double y3)) ->
                Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2);
            default -> throw new IllegalArgumentException("Unknown shape");
        };
    }

    public static double calculatePerimeter(Object shape) {
        return switch (shape) {
            case Circle(Point var center, double r) -> 2 * Math.PI * r;
            case Rectangle(Point(double x1, double y1), Point(double x2, double y2)) ->
                2 * (Math.abs(x2 - x1) + Math.abs(y2 - y1));
            case Triangle(Point(double x1, double y1), Point(double x2, double y2), Point(double x3, double y3)) ->
                distance(x1, y1, x2, y2) + distance(x2, y2, x3, y3) + distance(x3, y3, x1, y1);
            default -> throw new IllegalArgumentException("Unknown shape");
        };
    }

    static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Exercise 2: JSON Parser
    sealed interface Json permits JsonString, JsonNumber, JsonArray, JsonObject {}
    record JsonString(String value) implements Json {}
    record JsonNumber(double value) implements Json {}
    record JsonArray(List<Json> elements) implements Json {}
    record JsonObject(Map<String, Json> entries) implements Json {}

    public static String prettyPrint(Json json) {
        return prettyPrint(json, 0);
    }

    static String prettyPrint(Json json, int indent) {
        String padding = "  ".repeat(indent);
        return switch (json) {
            case JsonString(String value) -> "\"" + value + "\"";
            case JsonNumber(double value) -> String.valueOf(value);
            case JsonArray(List<Json> elements) -> {
                if (elements.isEmpty()) {
                    yield "[]";
                }
                StringBuilder sb = new StringBuilder("[\n");
                for (int i = 0; i < elements.size(); i++) {
                    sb.append(padding).append("  ").append(prettyPrint(elements.get(i), indent + 1));
                    if (i < elements.size() - 1) sb.append(",");
                    sb.append("\n");
                }
                sb.append(padding).append("]");
                yield sb.toString();
            }
            case JsonObject(Map<String, Json> entries) -> {
                if (entries.isEmpty()) {
                    yield "{}";
                }
                StringBuilder sb = new StringBuilder("{\n");
                int i = 0;
                for (var entry : entries.entrySet()) {
                    sb.append(padding).append("  \"").append(entry.getKey()).append("\": ")
                      .append(prettyPrint(entry.getValue(), indent + 1));
                    if (i < entries.size() - 1) sb.append(",");
                    sb.append("\n");
                    i++;
                }
                sb.append(padding).append("}");
                yield sb.toString();
            }
        };
    }

    public static int calculateDepth(Json json) {
        return switch (json) {
            case JsonString(String value) -> 0;
            case JsonNumber(double value) -> 0;
            case JsonArray(List<Json> elements) -> elements.stream()
                .mapToInt(RecordPatternsSolutions::calculateDepth)
                .max()
                .orElse(0) + 1;
            case JsonObject(Map<String, Json> entries) -> entries.values().stream()
                .mapToInt(RecordPatternsSolutions::calculateDepth)
                .max()
                .orElse(0) + 1;
        };
    }

    // Exercise 3: Expression Evaluator
    sealed interface Expr permits Number, Add, Multiply, Power {}
    record Number(double value) implements Expr {}
    record Add(Expr left, Expr right) implements Expr {}
    record Multiply(Expr left, Expr right) implements Expr {}
    record Power(Expr base, Expr exponent) implements Expr {}

    public static double eval(Expr expr) {
        return switch (expr) {
            case Number(double value) -> value;
            case Add(Expr left, Expr right) -> eval(left) + eval(right);
            case Multiply(Expr left, Expr right) -> eval(left) * eval(right);
            case Power(Expr base, Expr exponent) -> Math.pow(eval(base), eval(exponent));
        };
    }

    public static String exprToString(Expr expr) {
        return switch (expr) {
            case Number(double value) -> String.valueOf(value);
            case Add(Expr left, Expr right) -> "(" + exprToString(left) + " + " + exprToString(right) + ")";
            case Multiply(Expr left, Expr right) -> "(" + exprToString(left) + " * " + exprToString(right) + ")";
            case Power(Expr base, Expr exponent) -> "(" + exprToString(base) + " ^ " + exprToString(exponent) + ")";
        };
    }

    // Exercise 4: AST Visitor
    sealed interface Expr2 permits Literal, Variable, BinaryOp {}
    record Literal(int value) implements Expr2 {}
    record Variable(String name) implements Expr2 {}
    record BinaryOp(String op, Expr2 left, Expr2 right) implements Expr2 {}

    public static int eval2(Expr2 expr) {
        return switch (expr) {
            case Literal(int value) -> value;
            case Variable(String name) -> 0; // Default value
            case BinaryOp(String op, Expr2 left, Expr2 right) -> {
                int l = eval2(left);
                int r = eval2(right);
                yield switch (op) {
                    case "+" -> l + r;
                    case "-" -> l - r;
                    case "*" -> l * r;
                    case "/" -> l / r;
                    default -> throw new IllegalArgumentException("Unknown op: " + op);
                };
            }
        };
    }

    public static String exprToString2(Expr2 expr) {
        return switch (expr) {
            case Literal(int value) -> String.valueOf(value);
            case Variable(String name) -> name;
            case BinaryOp(String op, Expr2 left, Expr2 right) -> 
                "(" + exprToString2(left) + " " + op + " " + exprToString2(right) + ")";
        };
    }

    public static int countNodes(Expr2 expr) {
        return switch (expr) {
            case Literal(int value) -> 1;
            case Variable(String name) -> 1;
            case BinaryOp(String op, Expr2 left, Expr2 right) -> 
                1 + countNodes(left) + countNodes(right);
        };
    }

    public static void main(String[] args) {
        // Test Exercise 1
        System.out.println("--- Exercise 1: Geometry Calculator ---");
        Circle circle = new Circle(new Point(0, 0), 5);
        Rectangle rect = new Rectangle(new Point(0, 0), new Point(4, 3));
        Triangle tri = new Triangle(new Point(0, 0), new Point(4, 0), new Point(0, 3));
        System.out.printf("Circle area: %.2f%n", calculateArea(circle));
        System.out.printf("Rectangle area: %.2f%n", calculateArea(rect));
        System.out.printf("Triangle area: %.2f%n", calculateArea(tri));

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: JSON Parser ---");
        Json json = new JsonObject(Map.of(
            "name", new JsonString("Alice"),
            "age", new JsonNumber(30),
            "scores", new JsonArray(List.of(new JsonNumber(85), new JsonNumber(92)))
        ));
        System.out.println(prettyPrint(json));
        System.out.println("Depth: " + calculateDepth(json));

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Expression Evaluator ---");
        Expr expr = new Add(
            new Multiply(new Number(2), new Number(3)),
            new Power(new Number(2), new Number(3))
        );
        System.out.println("Expression: " + exprToString(expr));
        System.out.println("Result: " + eval(expr));

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: AST Visitor ---");
        Expr2 ast = new BinaryOp("+", new Literal(5), new BinaryOp("*", new Literal(3), new Variable("x")));
        System.out.println("AST: " + exprToString2(ast));
        System.out.println("Eval: " + eval2(ast));
        System.out.println("Nodes: " + countNodes(ast));
    }
}
