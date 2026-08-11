package academy.javaengineering.exceptions.runtimeexception.examples;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Practical examples of RuntimeException subtypes in real scenarios.
 */
public class RuntimeExceptionExample {

    public static class UserValidator {

        public static void validateAge(int age) {
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Invalid age: " + age);
            }
        }

        public static void validateEmail(String email) {
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email must not be empty");
            }
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format: " + email);
            }
        }

        public static void validateNotNull(Object obj, String fieldName) {
            if (obj == null) {
                throw new IllegalArgumentException(fieldName + " must not be null");
            }
        }
    }

    public static class OrderProcessor {
        private boolean initialized = false;
        private boolean processing = false;
        private boolean completed = false;

        public void initialize() {
            if (initialized) {
                throw new IllegalStateException("Already initialized");
            }
            initialized = true;
            System.out.println("Order processor initialized");
        }

        public void process() {
            if (!initialized) {
                throw new IllegalStateException("Must initialize before processing");
            }
            if (processing) {
                throw new IllegalStateException("Already processing");
            }
            if (completed) {
                throw new IllegalStateException("Already completed");
            }
            processing = true;
            System.out.println("Processing order...");
            processing = false;
            completed = true;
            System.out.println("Order processed");
        }
    }

    public static class SafeListAccess {

        public static <T> T safeGet(List<T> list, int index, T defaultValue) {
            if (list == null) {
                throw new IllegalArgumentException("List must not be null");
            }
            try {
                return list.get(index);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Index " + index + " out of bounds, using default");
                return defaultValue;
            }
        }
    }

    public static void demonstrateConcurrentModification() {
        System.out.println("=== ConcurrentModificationException ===");
        List<String> items = new ArrayList<>();
        items.add("a");
        items.add("b");
        items.add("c");

        try {
            for (String item : items) {
                if ("b".equals(item)) {
                    items.remove(item);
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName());
        }
    }

    public static void demonstrateSafeRemoval() {
        System.out.println("=== Safe Removal with Iterator ===");
        List<String> items = new ArrayList<>();
        items.add("a");
        items.add("b");
        items.add("c");

        var iterator = items.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if ("b".equals(item)) {
                iterator.remove();
            }
        }
        System.out.println("Items after removal: " + items);
    }

    public static class NumberParser {

        public static int parseIntOrThrow(String value) {
            if (value == null || value.isBlank()) {
                throw new NumberFormatException("Input must not be null or blank");
            }
            return Integer.parseInt(value.trim());
        }

        public static int parseIntOrDefault(String value, int defaultValue) {
            if (value == null || value.isBlank()) {
                return defaultValue;
            }
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }

    public static <T> T safeCast(Object obj, Class<T> type) {
        if (obj == null) {
            return null;
        }
        if (type.isInstance(obj)) {
            return type.cast(obj);
        }
        throw new ClassCastException(
            "Cannot cast " + obj.getClass().getName() + " to " + type.getName());
    }

    public static void main(String[] args) {
        System.out.println("=== User Validation ===");
        try {
            UserValidator.validateAge(25);
            System.out.println("Age 25 is valid");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            UserValidator.validateAge(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Order Processor ===");
        OrderProcessor processor = new OrderProcessor();
        processor.initialize();
        try {
            processor.initialize();
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        processor.process();

        System.out.println("\n=== Safe List Access ===");
        List<String> list = List.of("a", "b", "c");
        String value = SafeListAccess.safeGet(list, 1, "default");
        System.out.println("Got: " + value);
        value = SafeListAccess.safeGet(list, 10, "default");
        System.out.println("Got: " + value);

        demonstrateConcurrentModification();
        demonstrateSafeRemoval();

        System.out.println("\n=== Number Parser ===");
        try {
            int num = NumberParser.parseIntOrThrow("42");
            System.out.println("Parsed: " + num);
        } catch (NumberFormatException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        int fallback = NumberParser.parseIntOrDefault("abc", 0);
        System.out.println("Fallback value: " + fallback);

        System.out.println("\n=== Safe Casting ===");
        String result = safeCast("Hello", String.class);
        System.out.println("Cast succeeded: " + result);

        try {
            Integer num = safeCast("Hello", Integer.class);
        } catch (ClassCastException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
