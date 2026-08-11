package academy.javaengineering.exceptions.runtimeexception.solutions;

import java.util.List;

/**
 * Solutions for RuntimeException exercises.
 * Each method demonstrates the correct exception behavior.
 */
public class RuntimeExceptionSolutions {

    /**
     * Solution 1: Throw IllegalArgumentException if age is negative.
     */
    public static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age must not be negative, got: " + age);
        }
        System.out.println("Age " + age + " is valid");
    }

    /**
     * Solution 2: Throw NullPointerException if name is null, then
     * return the length of the name.
     */
    public static int getNameLength(String name) {
        if (name == null) {
            throw new NullPointerException("Name must not be null");
        }
        return name.length();
    }

    /**
     * Solution 3: Throw ArrayIndexOutOfBoundsException if index is
     * out of bounds for the given array.
     */
    public static int getElement(int[] array, int index) {
        if (array == null) {
            throw new NullPointerException("Array must not be null");
        }
        if (index < 0 || index >= array.length) {
            throw new ArrayIndexOutOfBoundsException(
                "Index " + index + " out of bounds for length " + array.length);
        }
        return array[index];
    }

    /**
     * Solution 4: Throw IllegalStateException if the service is not started.
     */
    private static boolean started = false;

    public static void start() {
        started = true;
    }

    public static void doWork() {
        if (!started) {
            throw new IllegalStateException("Service must be started before doing work");
        }
        System.out.println("Working...");
    }

    /**
     * Solution 5: Parse a string to int, letting NumberFormatException propagate.
     */
    public static int parseInteger(String value) {
        if (value == null || value.isBlank()) {
            throw new NumberFormatException("Input must not be null or blank");
        }
        return Integer.parseInt(value.trim());
    }

    /**
     * Solution 6: Throw IllegalArgumentException if the list is null or empty.
     * Return the first element.
     */
    public static <T> T getFirst(List<T> list) {
        if (list == null) {
            throw new IllegalArgumentException("List must not be null");
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List must not be empty");
        }
        return list.get(0);
    }

    /**
     * Solution 7: Throw UnsupportedOperationException if the operation
     * is not "read" or "write".
     */
    public static void performOperation(String operation) {
        if (!"read".equals(operation) && !"write".equals(operation)) {
            throw new UnsupportedOperationException(
                "Unsupported operation: " + operation + ". Supported: read, write");
        }
        System.out.println("Performing: " + operation);
    }

    /**
     * Solution 8: Cast an Object to String, throwing ClassCastException
     * if the object is not a String.
     */
    public static String asString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof String)) {
            throw new ClassCastException(
                "Cannot cast " + obj.getClass().getName() + " to String");
        }
        return (String) obj;
    }

    /**
     * Solution 9: Divide two integers. Throw ArithmeticException if
     * the divisor is zero.
     */
    public static int divide(int dividend, int divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return dividend / divisor;
    }

    /**
     * Solution 10: Add an item to a list only if it is not null.
     * Throw IllegalArgumentException if the item is null.
     */
    public static void addNonNull(List<String> list, String item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null");
        }
        list.add(item);
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: validateAge ===");
        try {
            validateAge(25);
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 2: getNameLength ===");
        try {
            System.out.println("Length: " + getNameLength("Hello"));
            getNameLength(null);
        } catch (NullPointerException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 3: getElement ===");
        int[] arr = {10, 20, 30};
        try {
            System.out.println("Element: " + getElement(arr, 1));
            getElement(arr, 5);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 4: doWork ===");
        try {
            doWork();
            start();
            doWork();
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 5: parseInteger ===");
        try {
            System.out.println("Parsed: " + parseInteger("42"));
            parseInteger("abc");
        } catch (NumberFormatException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 6: getFirst ===");
        try {
            List<String> list = List.of("a", "b");
            System.out.println("First: " + getFirst(list));
            getFirst(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 7: performOperation ===");
        try {
            performOperation("read");
            performOperation("delete");
        } catch (UnsupportedOperationException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 8: asString ===");
        try {
            System.out.println("String: " + asString("Hello"));
            asString(42);
        } catch (ClassCastException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 9: divide ===");
        try {
            System.out.println("Result: " + divide(10, 3));
            divide(10, 0);
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n=== Exercise 10: addNonNull ===");
        try {
            java.util.List<String> items = new java.util.ArrayList<>();
            addNonNull(items, "test");
            addNonNull(items, null);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
