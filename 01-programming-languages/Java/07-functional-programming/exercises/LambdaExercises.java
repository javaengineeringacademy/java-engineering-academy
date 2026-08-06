package academy.javaengineering.exercises;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Exercises: Lambda Expressions
 *
 * Complete the TODO sections below.
 */
public class LambdaExercises {

    // TODO 1: Implement functional interfaces using lambdas
    // Create a lambda that takes a String and returns its uppercase version
    public Function<String, String> toUpperCaseLambda() {
        // TODO: implement this
        return null;
    }

    // Create a lambda that takes two integers and returns their sum
    public BiFunction<Integer, Integer, Integer> sumLambda() {
        // TODO: implement this
        return null;
    }

    // Create a lambda that checks if a number is positive
    public Predicate<Integer> isPositiveLambda() {
        // TODO: implement this
        return null;
    }

    // TODO 2: Use predicates for filtering
    // Filter a list of strings to keep only those longer than the given length
    public List<String> filterByLength(List<String> strings, int minLength) {
        // TODO: implement this using lambda
        return List.of();
    }

    // Filter a list of integers to keep only even numbers
    public List<Integer> filterEvens(List<Integer> numbers) {
        // TODO: implement this using lambda
        return List.of();
    }

    // TODO 3: Implement a custom functional interface
    @FunctionalInterface
    interface StringProcessor {
        String process(String input);
    }

    // Apply multiple string processors in sequence
    public String processString(String input, StringProcessor... processors) {
        // TODO: implement this
        return "";
    }

    // TODO 4: Use Function composition
    // Given: Function<A, B> and Function<B, C>, return Function<A, C>
    // This is essentially function chaining
    public <A, B, C> Function<A, C> compose(Function<A, B> f1, Function<B, C> f2) {
        // TODO: implement this
        return null;
    }

    // TODO 5: Implement curry-like behavior
    // Take a BiFunction and return a Function that takes the first argument
    // and returns another Function that takes the second argument
    public <A, B, R> Function<A, Function<B, R>> curry(BiFunction<A, B, R> biFunc) {
        // TODO: implement this
        return null;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        LambdaExercises exercises = new LambdaExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== LambdaExercises Tests ===\n");

        // Test 1
        total++;
        try {
            Function<String, String> upper = exercises.toUpperCaseLambda();
            BiFunction<Integer, Integer, Integer> sum = exercises.sumLambda();
            Predicate<Integer> positive = exercises.isPositiveLambda();
            if (upper != null && "HELLO".equals(upper.apply("hello"))
                && sum != null && sum.apply(3, 4) == 7
                && positive != null && positive.test(5) && !positive.test(-3)) {
                System.out.println("Test 1 PASSED: lambda basics");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: lambda basics");
            }
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            System.out.println("Test 1 FAILED: lambda basics - " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            List<String> words = List.of("hi", "hello", "hey", "hola", "howdy");
            List<String> longWords = exercises.filterByLength(words, 4);
            List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);
            List<Integer> evens = exercises.filterEvens(nums);
            if (longWords.size() == 3 && longWords.containsAll(List.of("hello", "hola", "howdy"))
                && evens.size() == 3 && evens.containsAll(List.of(2, 4, 6))) {
                System.out.println("Test 2 PASSED: filtering with lambdas");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: filtering with lambdas");
            }
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            System.out.println("Test 2 FAILED: filtering with lambdas - " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            StringProcessor upper = String::toUpperCase;
            StringProcessor trimmed = String::trim;
            StringProcessor noSpaces = s -> s.replace(" ", "");
            String result = exercises.processString("  Hello World  ", trimmed, upper, noSpaces);
            if ("HELLOWORLD".equals(result)) {
                System.out.println("Test 3 PASSED: custom functional interface");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: custom functional interface - got '" + result + "'");
            }
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            System.out.println("Test 3 FAILED: custom functional interface - " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            Function<Integer, Integer> doubleIt = x -> x * 2;
            Function<Integer, Integer> addTen = x -> x + 10;
            Function<Integer, Integer> composed = exercises.compose(doubleIt, addTen);
            // compose(doubleIt, addTen) should: first double, then add 10
            // So 5 -> 10 -> 20
            if (composed != null && composed.apply(5) == 20) {
                System.out.println("Test 4 PASSED: function composition");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: function composition - got " + (composed != null ? composed.apply(5) : "null"));
            }
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            System.out.println("Test 4 FAILED: function composition - " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;
            Function<Integer, Function<Integer, Integer>> curried = exercises.curry(multiply);
            Function<Integer, Integer> timesThree = curried.apply(3);
            if (curried != null && timesThree.apply(4) == 12 && timesThree.apply(10) == 30) {
                System.out.println("Test 5 PASSED: curry");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: curry");
            }
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            System.out.println("Test 5 FAILED: curry - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
