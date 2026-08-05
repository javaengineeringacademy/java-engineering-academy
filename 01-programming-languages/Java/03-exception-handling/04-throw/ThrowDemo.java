package academy.javaengineering.exceptionhandling;

public class ThrowDemo {

    public static void main(String[] args) {
        throwBasicDemo();
        throwWithMessage();
        throwChainedException();
    }

    public static void throwBasicDemo() {
        System.out.println("=== Throw Basic Demo ===");
        try {
            validateAge(15);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    public static void throwWithMessage() {
        System.out.println("\n=== Throw with Message ===");
        try {
            processInput("");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    public static void throwChainedException() {
        System.out.println("\n=== Throw Chained Exception ===");
        try {
            processData(null);
        } catch (RuntimeException e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        }
    }

    public static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative: " + age);
        }
        if (age < 18) {
            throw new IllegalArgumentException("Must be at least 18 years old");
        }
        System.out.println("Age valid: " + age);
    }

    public static void processInput(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        System.out.println("Processing: " + input);
    }

    public static void processData(String data) {
        try {
            if (data == null) {
                throw new NullPointerException("Data is null");
            }
            data.length();
        } catch (NullPointerException e) {
            throw new RuntimeException("Failed to process data", e);
        }
    }

    public static void validateRange(int value, int min, int max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                "Value " + value + " not in range [" + min + ", " + max + "]"
            );
        }
    }
}
