package academy.javaengineering.oop.practices;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Practice: Lambda Expressions in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Writing lambda expressions with correct syntax
 * - Expression lambdas vs block lambdas
 * - Variable capture (effectively final)
 * - Type inference in lambda parameters
 * - Using lambdas with functional interfaces
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 02-lambda-expressions ===\n");

        // Test Exercise 1: Lambda as a Comparator
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
        names.sort((a, b) -> a.compareTo(b));
        System.out.println("Exercise 1 - Lambda sort: "
            + ("Alice".equals(names.get(0)) && "Bob".equals(names.get(1)) ? "PASS" : "FAIL"));

        // Test Exercise 2: Lambda with Predicate
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> evens = filterNumbers(numbers, n -> n % 2 == 0);
        System.out.println("Exercise 2 - Predicate filter: "
            + (evens.size() == 3 && evens.containsAll(Arrays.asList(2, 4, 6)) ? "PASS" : "FAIL"));

        // Test Exercise 3: Lambda with Function
        List<String> uppercased = transformList(Arrays.asList("hello", "world"), s -> s.toUpperCase());
        System.out.println("Exercise 3 - Function transform: "
            + ("HELLO".equals(uppercased.get(0)) && "WORLD".equals(uppercased.get(1)) ? "PASS" : "FAIL"));

        // Test Exercise 4: Variable capture
        String prefix = "ITEM-";
        List<String> items = Arrays.asList("A", "B", "C");
        List<String> prefixed = addPrefix(items, prefix);
        System.out.println("Exercise 4 - Variable capture: "
            + ("ITEM-A".equals(prefixed.get(0)) && "ITEM-C".equals(prefixed.get(2)) ? "PASS" : "FAIL"));

        // Test Exercise 5: Lambda composition
        UnaryOperator<String> trim = String::trim;
        UnaryOperator<String> upper = String::toUpperCase;
        UnaryOperator<String> composed = compose(trim, upper);
        System.out.println("Exercise 5 - Lambda composition: "
            + ("HELLO".equals(composed.apply("  hello  ")) ? "PASS" : "FAIL"));
    }

    // TODO 1: Filter a list of integers using a Predicate<Integer>
    // Return only elements that match the predicate
    static List<Integer> filterNumbers(List<Integer> numbers, Predicate<Integer> predicate) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 2: Transform a list using a Function<T, R>
    // Apply the function to each element and return a new list
    static <T, R> List<R> transformList(List<T> input, Function<T, R> function) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 3: Add a prefix to each string in a list using a lambda with variable capture
    // The prefix variable is captured from the enclosing scope
    static List<String> addPrefix(List<String> items, String prefix) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 4: Compose two UnaryOperator<T> functions: apply first, then second
    // compose(trim, upper) applied to "  hello  " should give "HELLO"
    static <T> UnaryOperator<T> compose(UnaryOperator<T> first, UnaryOperator<T> second) {
        // YOUR CODE HERE
        return null;
    }
}
