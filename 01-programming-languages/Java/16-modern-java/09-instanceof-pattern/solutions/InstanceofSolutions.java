package academy.javaengineering.modern.instanceofpattern;

import java.util.List;

/**
 * Solutions for instanceof Pattern Matching practice exercises.
 */
public class InstanceofSolutions {

    // Exercise 1: Type Converter
    public static String convertToString(Object obj) {
        if (obj instanceof Integer i) {
            return "Integer: " + i;
        } else if (obj instanceof Double d) {
            return "Double: " + d;
        } else if (obj instanceof String s) {
            return "String: " + s;
        } else if (obj instanceof Boolean b) {
            return "Boolean: " + b;
        } else if (obj instanceof List<?> list) {
            return "List with " + list.size() + " elements";
        } else if (obj == null) {
            return "Null";
        } else {
            return "Unknown: " + obj.getClass().getSimpleName();
        }
    }

    // Exercise 2: Shape Analyzer
    sealed interface Shape permits Circle, Rectangle, Triangle {}
    record Circle(double radius) implements Shape {}
    record Rectangle(double width, double height) implements Shape {}
    record Triangle(double base, double height) implements Shape {}

    public static double calculateArea(Shape shape) {
        if (shape instanceof Circle c) {
            return Math.PI * c.radius() * c.radius();
        } else if (shape instanceof Rectangle r) {
            return r.width() * r.height();
        } else if (shape instanceof Triangle t) {
            return 0.5 * t.base() * t.height();
        } else {
            throw new IllegalArgumentException("Unknown shape");
        }
    }

    public static boolean isRegular(Shape shape) {
        if (shape instanceof Circle c) {
            return c.radius() > 0;
        } else if (shape instanceof Rectangle r) {
            return r.width() == r.height();
        } else if (shape instanceof Triangle t) {
            return t.base() == t.height();
        } else {
            return false;
        }
    }

    public static String classifySize(Shape shape) {
        double area = calculateArea(shape);
        if (area < 10) {
            return "Small";
        } else if (area < 100) {
            return "Medium";
        } else {
            return "Large";
        }
    }

    // Exercise 3: Data Validator
    public record ValidationResult(boolean valid, String message) {
        public static ValidationResult success(String message) {
            return new ValidationResult(true, message);
        }

        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message);
        }
    }

    public static ValidationResult validate(Object obj) {
        if (obj instanceof String s) {
            if (s == null || s.isEmpty()) {
                return ValidationResult.failure("String is empty");
            }
            if (s.matches(".*[^a-zA-Z0-9].*")) {
                return ValidationResult.failure("String contains special characters");
            }
            return ValidationResult.success("Valid string: " + s);
        } else if (obj instanceof Integer i) {
            if (i < 0 || i > 100) {
                return ValidationResult.failure("Integer out of range: " + i);
            }
            return ValidationResult.success("Valid integer: " + i);
        } else if (obj instanceof List<?> list) {
            if (list.isEmpty()) {
                return ValidationResult.failure("List is empty");
            }
            return ValidationResult.success("Valid list with " + list.size() + " elements");
        } else if (obj == null) {
            return ValidationResult.failure("Null value");
        } else {
            return ValidationResult.failure("Unknown type: " + obj.getClass().getSimpleName());
        }
    }

    // Exercise 4: Response Handler
    sealed interface ApiResponse permits Success, Error, Redirect {}
    record Success(int status, Object data) implements ApiResponse {}
    record Error(int status, String message) implements ApiResponse {}
    record Redirect(String url) implements ApiResponse {}

    public static String handleResponse(ApiResponse response) {
        if (response instanceof Success s && s.status() == 200) {
            return "Success: " + s.data();
        } else if (response instanceof Success s) {
            return "Success with status " + s.status();
        } else if (response instanceof Error e && e.status() >= 500) {
            return "Server error: " + e.message();
        } else if (response instanceof Error e) {
            return "Client error: " + e.message();
        } else if (response instanceof Redirect r) {
            return "Redirect to: " + r.url();
        } else {
            return "Unknown response";
        }
    }

    public static void main(String[] args) {
        // Test Exercise 1
        System.out.println("--- Exercise 1: Type Converter ---");
        Object[] inputs = {42, 3.14, "Hello", true, List.of(1, 2, 3), null};
        for (Object input : inputs) {
            System.out.println(convertToString(input));
        }

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: Shape Analyzer ---");
        Shape[] shapes = {new Circle(5), new Rectangle(4, 6), new Triangle(3, 8)};
        for (Shape shape : shapes) {
            System.out.println(shape + " - Area: " + calculateArea(shape) + 
                ", Regular: " + isRegular(shape) + 
                ", Size: " + classifySize(shape));
        }

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Data Validator ---");
        Object[] validators = {"hello", "hello world", 42, -5, List.of(1, 2), List.of(), null};
        for (Object obj : validators) {
            ValidationResult result = validate(obj);
            System.out.println(obj + " -> " + (result.valid() ? "Valid" : "Invalid") + 
                ": " + result.message());
        }

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: Response Handler ---");
        ApiResponse[] responses = {
            new Success(200, "OK"),
            new Error(404, "Not Found"),
            new Error(500, "Internal Server Error"),
            new Redirect("/login")
        };
        for (ApiResponse response : responses) {
            System.out.println(handleResponse(response));
        }
    }
}
