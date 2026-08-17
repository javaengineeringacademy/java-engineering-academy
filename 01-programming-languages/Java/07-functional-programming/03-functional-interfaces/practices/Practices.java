package academy.javaengineering.oop.practices;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Practice: Functional Interfaces in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Using Predicate<T> for filtering
 * - Using Function<T, R> for transformation
 * - Using Consumer<T> for side effects
 * - Using Supplier<T> for lazy creation
 * - Using BinaryOperator<T> for reduction
 * - Custom @FunctionalInterface definitions
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 03-functional-interfaces ===\n");

        // Test Exercise 1: Predicate - chain and/or
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        List<Integer> result = filterList(Arrays.asList(-3, 2, 5, 8, -1, 0), isPositive.and(isEven));
        System.out.println("Exercise 1 - Predicate chain: "
            + (result.size() == 2 && result.containsAll(Arrays.asList(2, 8)) ? "PASS" : "FAIL"));

        // Test Exercise 2: Function - compose and andThen
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;
        int result2 = applyComposition(doubleIt, addTen, 5); // 5 -> 10 -> 20
        System.out.println("Exercise 2 - Function compose: "
            + (result2 == 20 ? "PASS" : "FAIL (expected 20, got " + result2 + ")"));

        // Test Exercise 3: Consumer - perform side effect
        StringBuilder log = new StringBuilder();
        consumeAndLog(Arrays.asList("A", "B", "C"), s -> log.append(s));
        System.out.println("Exercise 3 - Consumer side effect: "
            + ("ABC".equals(log.toString()) ? "PASS" : "FAIL"));

        // Test Exercise 4: Supplier - lazy creation
        Supplier<List<String>> listFactory = () -> Arrays.asList("default");
        List<String> created = createIfAbsent(null, listFactory);
        System.out.println("Exercise 4 - Supplier lazy: "
            + (created != null && created.size() == 1 ? "PASS" : "FAIL"));

        // Test Exercise 5: BinaryOperator - reduce
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
        int sum = reduceList(nums, Integer::sum, 0);
        System.out.println("Exercise 5 - BinaryOperator reduce: "
            + (sum == 15 ? "PASS" : "FAIL"));
    }

    // TODO 1: Filter a list using a Predicate
    // Return elements that match the predicate
    static <T> List<T> filterList(List<T> list, Predicate<T> predicate) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 2: Apply a composition: first apply doubleIt, then addTen
    // compose(x -> x*2, x -> x+10) applied to 5 should return 20 (5*2=10, 10+10=20)
    static int applyComposition(Function<Integer, Integer> first,
                                 Function<Integer, Integer> second, int input) {
        // YOUR CODE HERE
        return 0;
    }

    // TODO 3: Consume each element using a Consumer and log it
    // Use consumer.accept() for each element
    static <T> void consumeAndLog(List<T> list, Consumer<T> consumer) {
        // YOUR CODE HERE
    }

    // TODO 4: If the input is null, create and return using the Supplier
    // Otherwise return the input
    static <T> T createIfAbsent(T input, Supplier<T> supplier) {
        // YOUR CODE HERE
        return null;
    }

    // TODO 5: Reduce a list to a single value using BinaryOperator
    // Start with the identity value and apply the operator
    static <T> T reduceList(List<T> list, BinaryOperator<T> operator, T identity) {
        // YOUR CODE HERE
        return identity;
    }
}
