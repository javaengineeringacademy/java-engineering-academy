package academy.javaengineering.modern.pattern;

import java.util.List;

/**
 * Advanced pattern matching with complex types.
 */
public class AdvancedPatternExample {

    // Expression tree
    sealed interface Expr permits Num, Add, Mul, Neg {}
    record Num(double value) implements Expr {}
    record Add(Expr left, Expr right) implements Expr {}
    record Mul(Expr left, Expr right) implements Expr {}
    record Neg(Expr operand) implements Expr {}

    // JSON-like structure
    sealed interface Json permits JsonString, JsonNumber, JsonArray, JsonObject {}
    record JsonString(String value) implements Json {}
    record JsonNumber(double value) implements Json {}
    record JsonArray(List<Json> elements) implements Json {}
    record JsonObject(java.util.Map<String, Json> entries) implements Json {}

    public static void main(String[] args) {
        // Expression evaluation
        Expr expr = new Add(
            new Mul(new Num(2), new Num(3)),
            new Neg(new Num(4))
        );
        System.out.println("Expression: 2 * 3 + (-4)");
        System.out.println("Result: " + eval(expr));

        // Nested patterns
        Expr complex = new Mul(
            new Add(new Num(1), new Num(2)),
            new Add(new Num(3), new Num(4))
        );
        System.out.println("\nComplex: (1 + 2) * (3 + 4)");
        System.out.println("Result: " + eval(complex));

        // JSON processing
        Json json = new JsonObject(java.util.Map.of(
            "name", new JsonString("Alice"),
            "age", new JsonNumber(30),
            "scores", new JsonArray(List.of(
                new JsonNumber(85),
                new JsonNumber(92),
                new JsonNumber(78)
            ))
        ));
        System.out.println("\nJSON structure:");
        System.out.println("Type: " + getJsonType(json));
        System.out.println("Description: " + describeJson(json));

        // Pattern with conditions
        Object[] inputs = {"Hello", 42, 3.14, null, List.of(1, 2, 3)};
        for (Object input : inputs) {
            System.out.println("\nInput: " + input);
            System.out.println("Classification: " + classify(input));
        }
    }

    static double eval(Expr expr) {
        return switch (expr) {
            case Num n -> n.value();
            case Add a -> eval(a.left()) + eval(a.right());
            case Mul m -> eval(m.left()) * eval(m.right());
            case Neg n -> -eval(n.operand());
        };
    }

    static String getJsonType(Json json) {
        return switch (json) {
            case JsonString s -> "String";
            case JsonNumber n -> "Number";
            case JsonArray a -> "Array";
            case JsonObject o -> "Object";
        };
    }

    static String describeJson(Json json) {
        return switch (json) {
            case JsonString s -> "String value: " + s.value();
            case JsonNumber n -> "Number value: " + n.value();
            case JsonArray a -> "Array with " + a.elements().size() + " elements";
            case JsonObject o -> "Object with " + o.entries().size() + " entries";
        };
    }

    static String classify(Object obj) {
        return switch (obj) {
            case String s && s.length() > 5 -> "Long string (" + s.length() + " chars)";
            case String s -> "Short string (" + s.length() + " chars)";
            case Integer i && i > 0 -> "Positive integer: " + i;
            case Integer i && i < 0 -> "Negative integer: " + i;
            case Integer i -> "Zero";
            case Double d -> "Double: " + d;
            case null -> "Null value";
            case List<?> list && list.isEmpty() -> "Empty list";
            case List<?> list -> "List with " + list.size() + " elements";
            default -> "Unknown type: " + obj.getClass().getSimpleName();
        };
    }
}
