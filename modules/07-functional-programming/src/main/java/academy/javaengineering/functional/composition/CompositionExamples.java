package academy.javaengineering.functional.composition;

import java.util.function.*;

/**
 * Comprehensive examples of Function Composition in Java 21.
 *
 * <p>This class demonstrates all aspects of function composition including
 * andThen, compose, predicate composition, and pipeline building. Each
 * example is self-contained and can be run independently.</p>
 *
 * <p>Topics covered:</p>
 * <ul>
 *   <li>Function composition with andThen and compose</li>
 *   <li>Predicate composition with and, or, negate</li>
 *   <li>Consumer composition with andThen</li>
 *   <li>Pipeline building patterns</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class CompositionExamples {

    private CompositionExamples() {
        // Utility class - no instantiation
    }

    /**
     * Demonstrates basic function composition.
     */
    public static void basicComposition() {
        System.out.println("=== Basic Function Composition ===\n");

        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;

        // andThen: double first, then add 10
        Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
        System.out.println("5 double then add 10: " + doubleThenAdd.apply(5));

        // compose: add 10 first, then double
        Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
        System.out.println("5 add 10 then double: " + addThenDouble.apply(5));

        // Chain
        Function<Integer, Integer> pipeline = doubleIt
            .andThen(addTen)
            .andThen(x -> x * x);
        System.out.println("5 pipeline: " + pipeline.apply(5));
    }

    /**
     * Demonstrates predicate composition.
     */
    public static void predicateComposition() {
        System.out.println("\n=== Predicate Composition ===\n");

        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isLessThan100 = n -> n < 100;

        // and
        Predicate<Integer> isPositiveEven = isPositive.and(isEven);
        System.out.println("5 is positive even: " + isPositiveEven.test(5));
        System.out.println("6 is positive even: " + isPositiveEven.test(6));

        // or
        Predicate<Integer> isSmallOrEven = isLessThan100.or(isEven);
        System.out.println("-5 is small or even: " + isSmallOrEven.test(-5));

        // negate
        Predicate<Integer> isNotPositive = isPositive.negate();
        System.out.println("-1 is not positive: " + isNotPositive.test(-1));

        java.util.List<Integer> numbers = java.util.Arrays.asList(-5, 0, 3, 6, 100);
        System.out.println("Positive and even: " + numbers.stream()
            .filter(isPositiveEven)
            .toList());
    }

    /**
     * Demonstrates text processing pipeline.
     */
    public static void textPipeline() {
        System.out.println("\n=== Text Processing Pipeline ===\n");

        Function<String, String> pipeline = Function.<String>identity()
            .andThen(String::trim)
            .andThen(String::toLowerCase)
            .andThen(s -> s.replaceAll("[^a-z0-9\\s]", ""))
            .andThen(s -> s.replaceAll("\\s+", "_"))
            .andThen(s -> "processed: " + s);

        System.out.println(pipeline.apply("  Hello, World!  "));
        System.out.println(pipeline.apply("  Java Programming 101  "));
        System.out.println(pipeline.apply("  Lambda Expressions!!!  "));
    }

    /**
     * Demonstrates consumer composition.
     */
    public static void consumerComposition() {
        System.out.println("\n=== Consumer Composition ===\n");

        Consumer<String> print = System.out::println;
        Consumer<String> log = s -> System.out.println("LOG: " + s);
        Consumer<String> uppercase = s -> System.out.println("UPPER: " + s.toUpperCase());

        Consumer<String> printAndLog = print.andThen(log);
        Consumer<String> allActions = print.andThen(log).andThen(uppercase);

        System.out.println("printAndLog:");
        printAndLog.accept("Hello");

        System.out.println("\nallActions:");
        allActions.accept("World");
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        basicComposition();
        predicateComposition();
        textPipeline();
        consumerComposition();

        System.out.println("\n=== Summary ===");
        System.out.println("Key takeaways:");
        System.out.println("1. andThen: this → after");
        System.out.println("2. compose: before → this");
        System.out.println("3. Predicate: and, or, negate");
        System.out.println("4. Consumer: andThen");
        System.out.println("5. Cache composed functions for reuse");
    }
}
