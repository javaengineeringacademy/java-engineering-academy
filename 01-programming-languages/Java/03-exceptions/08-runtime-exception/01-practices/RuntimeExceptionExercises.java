package academy.javaengineering.exceptions.runtimeexception.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercises for RuntimeException subtypes.
 * Complete each method to throw or handle the correct exception.
 */
public class RuntimeExceptionExercises {

    /**
     * Exercise 1: Throw IllegalArgumentException if age is negative.
     */
    public static void validateAge(int age) {
        // TODO: throw IllegalArgumentException if age < 0
    }

    /**
     * Exercise 2: Throw NullPointerException if name is null, then
     * return the length of the name.
     */
    public static int getNameLength(String name) {
        // TODO: throw NullPointerException if name is null
        return 0;
    }

    /**
     * Exercise 3: Throw ArrayIndexOutOfBoundsException if index is
     * out of bounds for the given array.
     */
    public static int getElement(int[] array, int index) {
        // TODO: check bounds and throw ArrayIndexOutOfBoundsException
        return 0;
    }

    /**
     * Exercise 4: Throw IllegalStateException if the service is not started.
     */
    private static boolean started = false;

    public static void start() {
        started = true;
    }

    public static void doWork() {
        // TODO: throw IllegalStateException if started is false
    }

    /**
     * Exercise 5: Parse a string to int, throwing NumberFormatException
     * if parsing fails. Return the parsed value.
     */
    public static int parseInteger(String value) {
        // TODO: parse and let NumberFormatException propagate or throw custom
        return 0;
    }

    /**
     * Exercise 6: Throw IllegalArgumentException if the list is null or empty.
     * Return the first element.
     */
    public static <T> T getFirst(List<T> list) {
        // TODO: validate and return first element
        return null;
    }

    /**
     * Exercise 7: Throw UnsupportedOperationException if the operation
     * is not "read" or "write".
     */
    public static void performOperation(String operation) {
        // TODO: validate operation and throw if unsupported
    }

    /**
     * Exercise 8: Cast an Object to String, throwing ClassCastException
     * if the object is not a String. Use instanceof check.
     */
    public static String asString(Object obj) {
        // TODO: safe cast with instanceof check
        return null;
    }

    /**
     * Exercise 9: Divide two integers. Throw ArithmeticException if
     * the divisor is zero.
     */
    public static int divide(int dividend, int divisor) {
        // TODO: check for zero divisor
        return 0;
    }

    /**
     * Exercise 10: Add an item to a list only if it is not null.
     * Throw IllegalArgumentException if the item is null.
     */
    public static void addNonNull(List<String> list, String item) {
        // TODO: validate and add
    }

    public static void main(String[] args) {
        System.out.println("Run each exercise and verify the exception behavior.");
        System.out.println("Use the solutions file to check your answers.");
    }
}
