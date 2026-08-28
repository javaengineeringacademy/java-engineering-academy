package academy.javaengineering.modern.instanceofpattern;

import java.util.List;

/**
 * instanceof pattern matching with real-world patterns.
 */
public class InstanceofPatterns {

    public static void main(String[] args) {
        // Data validation
        System.out.println("=== Data Validation ===");
        Object[] inputs = {"hello@email.com", 42, "short", 1234567890L, null};
        for (Object input : inputs) {
            System.out.println(validateInput(input));
        }

        // API response handling
        System.out.println("\n=== API Response Handling ===");
        record ApiResponse(int status, String body) {}
        record ApiError(int status, String message) {}
        record ApiSuccess(int status, Object data) {}

        List<Object> responses = List.of(
            new ApiResponse(200, "OK"),
            new ApiError(404, "Not Found"),
            new ApiSuccess(200, List.of("item1", "item2")),
            "Invalid response"
        );

        for (Object response : responses) {
            if (response instanceof ApiResponse r && r.status() == 200) {
                System.out.println("Success: " + r.body());
            } else if (response instanceof ApiError e) {
                System.out.println("Error " + e.status() + ": " + e.message());
            } else if (response instanceof ApiSuccess s) {
                System.out.println("Success with data: " + s.data());
            } else {
                System.out.println("Invalid response format");
            }
        }

        // Configuration parsing
        System.out.println("\n=== Configuration Parsing ===");
        Object[] configs = {"true", "false", "42", "hello", null};
        for (Object config : configs) {
            if (config instanceof String s && s.equalsIgnoreCase("true")) {
                System.out.println("Boolean true");
            } else if (config instanceof String s && s.equalsIgnoreCase("false")) {
                System.out.println("Boolean false");
            } else if (config instanceof String s && s.matches("\\d+")) {
                System.out.println("Number: " + Integer.parseInt(s));
            } else if (config instanceof String s) {
                System.out.println("String: " + s);
            } else {
                System.out.println("Null or unknown");
            }
        }

        // Event handling
        System.out.println("\n=== Event Handling ===");
        record ClickEvent(int x, int y) {}
        record KeyEvent(String key, boolean ctrl) {}
        record ScrollEvent(int amount) {}

        List<Object> events = List.of(
            new ClickEvent(100, 200),
            new KeyEvent("A", true),
            new ScrollEvent(-3),
            "Unknown event"
        );

        for (Object event : events) {
            if (event instanceof ClickEvent c && c.x() > 100) {
                System.out.println("Right click at (" + c.x() + "," + c.y() + ")");
            } else if (event instanceof ClickEvent c) {
                System.out.println("Click at (" + c.x() + "," + c.y() + ")");
            } else if (event instanceof KeyEvent k && k.ctrl()) {
                System.out.println("Ctrl+" + k.key());
            } else if (event instanceof KeyEvent k) {
                System.out.println("Key: " + k.key());
            } else if (event instanceof ScrollEvent s) {
                System.out.println("Scroll: " + s.amount());
            } else {
                System.out.println("Unknown event");
            }
        }
    }

    static String validateInput(Object input) {
        if (input instanceof String s && s.contains("@") && s.contains(".")) {
            return "Valid email: " + s;
        } else if (input instanceof String s && s.length() < 5) {
            return "Too short: " + s;
        } else if (input instanceof String s) {
            return "Invalid string: " + s;
        } else if (input instanceof Integer i && i > 0 && i < 100) {
            return "Valid age: " + i;
        } else if (input instanceof Integer i) {
            return "Invalid age: " + i;
        } else if (input instanceof Long l) {
            return "Long value: " + l;
        } else if (input == null) {
            return "Null value";
        } else {
            return "Unknown type: " + input.getClass().getSimpleName();
        }
    }
}
