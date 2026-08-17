package academy.javaengineering.oop.practices;

import java.util.Optional;

/**
 * Practice: Optional in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Creating Optional instances (empty, of, ofNullable)
 * - Accessing values (orElse, orElseGet, orElseThrow)
 * - Transforming values with map and flatMap
 * - Filtering with filter
 * - Using ifPresent and ifPresentOrElse
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 08-optional ===\n");

        // Test Exercise 1: safeDivide
        System.out.println("Exercise 1 - safeDivide: "
            + (safeDivide(10, 2).orElse(-1) == 5
            && safeDivide(10, 0).orElse(-1) == -1 ? "PASS" : "FAIL"));

        // Test Exercise 2: findFirstUpperCase
        System.out.println("Exercise 2 - findFirstUpperCase: "
            + ("B".equals(findFirstUpperCase(new String[]{"hello", "Bar", "baz"}))
            && findFirstUpperCase(new String[]{"hello", "world"}) == null ? "PASS" : "FAIL"));

        // Test Exercise 3: transformOptional
        Optional<String> result = transformOptional("hello", s -> s.toUpperCase());
        Optional<String> emptyResult = transformOptional(null, s -> s.toUpperCase());
        System.out.println("Exercise 3 - transformOptional: "
            + ("HELLO".equals(result.orElse("")) && emptyResult.isEmpty() ? "PASS" : "FAIL"));

        // Test Exercise 4: filterOptional
        Optional<Integer> filtered = filterOptional(15, n -> n > 10);
        Optional<Integer> notFiltered = filterOptional(5, n -> n > 10);
        System.out.println("Exercise 4 - filterOptional: "
            + (filtered.isPresent() && !notFiltered.isPresent() ? "PASS" : "FAIL"));

        // Test Exercise 5: unwrapOrThrow
        System.out.println("Exercise 5 - unwrapOrThrow: "
            + ("hello".equals(unwrapOrThrow(Optional.of("hello")))
            ? "PASS" : "FAIL"));
        try {
            unwrapOrThrow(Optional.empty());
            System.out.println("Exercise 5 - unwrapOrThrow (exception): FAIL (no exception)");
        } catch (RuntimeException e) {
            System.out.println("Exercise 5 - unwrapOrThrow (exception): PASS");
        }
    }

    // TODO 1: Divide two integers safely using Optional
    // If divisor is 0, return Optional.empty()
    // Otherwise return Optional.of(result)
    static Optional<Integer> safeDivide(int dividend, int divisor) {
        // YOUR CODE HERE
        return Optional.empty();
    }

    // TODO 2: Find the first uppercase word in an array
    // Return Optional.of(word) if found, Optional.empty() if none
    // A word has uppercase if the first character is uppercase: Character.isUpperCase(s.charAt(0))
    static String findFirstUpperCase(String[] words) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Transform an Optional value using a Function
    // If input is null, return Optional.empty()
    // Otherwise apply the function and wrap in Optional
    static <T, R> Optional<R> transformOptional(T input, java.util.function.Function<T, R> function) {
        // YOUR CODE HERE
        return Optional.empty();
    }

    // TODO 4: Filter an Optional value using a Predicate
    // If the value matches the predicate, return it; otherwise return Optional.empty()
    static Optional<Integer> filterOptional(Integer value, java.util.function.Predicate<Integer> predicate) {
        // YOUR CODE HERE
        return Optional.empty();
    }

    // TODO 5: Unwrap an Optional or throw RuntimeException with "Value is missing"
    // Use orElseThrow()
    static <T> T unwrapOrThrow(Optional<T> optional) {
        // YOUR CODE HERE
        return null;
    }
}
