package academy.javaengineering.functional.lambda;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

/**
 * Comprehensive examples of Lambda Expressions in Java 21.
 *
 * <p>This class demonstrates all aspects of lambda expressions including
 * syntax variations, variable capture, scoping rules, and performance
 * characteristics. Each example is self-contained and can be run independently.</p>
 *
 * <p>Topics covered:</p>
 * <ul>
 *   <li>Basic lambda syntax (expression and block bodies)</li>
 *   <li>Parameter type inference</li>
 *   <li>Variable capture and effectively final rules</li>
 *   <li>Lambda scoping rules</li>
 *   <li>Performance considerations</li>
 *   <li>Common pitfalls and how to avoid them</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class LambdaExamples {

    private LambdaExamples() {
        // Utility class - no instantiation
    }

    // Static field accessible by lambdas
    private static int staticCounter = 0;

    /**
     * Demonstrates basic lambda syntax variations.
     */
    public static void basicSyntax() {
        System.out.println("=== Basic Lambda Syntax ===\n");

        // No parameters
        Runnable noParams = () -> System.out.println("No parameters");
        noParams.run();

        // One parameter (parentheses optional)
        UnaryOperator<String> toUpper = s -> s.toUpperCase();
        System.out.println("Uppercase: " + toUpper.apply("hello"));

        // Multiple parameters
        BinaryOperator<Integer> add = (a, b) -> a + b;
        System.out.println("Add: " + add.apply(3, 4));

        // Expression body (implicit return)
        Function<Integer, Integer> doubleIt = x -> x * 2;
        System.out.println("Double: " + doubleIt.apply(5));

        // Block body (explicit return required)
        Function<String, String> reverse = s -> {
            StringBuilder sb = new StringBuilder(s);
            return sb.reverse().toString();
        };
        System.out.println("Reverse: " + reverse.apply("hello"));
    }

    /**
     * Demonstrates type inference in lambda parameters.
     */
    public static void typeInference() {
        System.out.println("\n=== Type Inference ===\n");

        // Types inferred from context
        Comparator<String> comp = (a, b) -> a.compareTo(b);
        System.out.println("Compare 'apple' to 'banana': " + comp.compare("apple", "banana"));

        Predicate<Integer> isPositive = n -> n > 0;
        System.out.println("Is 5 positive? " + isPositive.test(5));

        // Explicit types (when needed for clarity)
        Comparator<String> comp2 = (String a, String b) -> a.compareTo(b);
        System.out.println("Explicit types: " + comp2.compare("apple", "banana"));

        // Var parameters (Java 11+)
        Function<String, Integer> toLength = (var s) -> s.length();
        System.out.println("Length with var: " + toLength.apply("hello"));
    }

    /**
     * Demonstrates variable capture rules.
     */
    public static void variableCapture() {
        System.out.println("\n=== Variable Capture ===\n");

        // Effectively final variable
        String prefix = "ITEM-";
        List<String> items = Arrays.asList("Laptop", "Phone", "Tablet");

        // Lambda captures 'prefix' variable
        items.forEach(item -> System.out.println(prefix + item));

        // AtomicInteger for mutable state (workaround)
        AtomicInteger counter = new AtomicInteger(0);
        items.forEach(item -> {
            System.out.println("Processing #" + counter.incrementAndGet() + ": " + item);
        });

        System.out.println("Final counter: " + counter.get());
    }

    /**
     * Demonstrates scoping rules.
     */
    public static void scopingRules() {
        System.out.println("\n=== Scoping Rules ===\n");

        String outer = "outer";

        Runnable lambda = () -> {
            // Can access outer variable
            System.out.println("Outer variable: " + outer);

            // Cannot declare variable with same name
            // String outer = "inner"; // Compilation error!
        };

        lambda.run();

        // Instance field access
        LambdaExamples instance = new LambdaExamples();
        Runnable fieldAccess = () -> {
            staticCounter++;
            System.out.println("Static counter: " + staticCounter);
        };
        fieldAccess.run();
        fieldAccess.run();
    }

    /**
     * Demonstrates lambda with block body.
     */
    public static void blockBody() {
        System.out.println("\n=== Block Body Lambdas ===\n");

        // Multiple statements
        Function<String, String> processName = name -> {
            String trimmed = name.trim();
            String capitalized = trimmed.substring(0, 1).toUpperCase() + trimmed.substring(1).toLowerCase();
            return capitalized;
        };

        System.out.println("Processed name: " + processName.apply("  alice  "));
        System.out.println("Processed name: " + processName.apply("  BOB  "));

        // Exception handling in block body
        Function<String, Integer> safeParse = input -> {
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Failed to parse: " + input);
                return -1;
            }
        };

        System.out.println("Parse '123': " + safeParse.apply("123"));
        System.out.println("Parse 'abc': " + safeParse.apply("abc"));
    }

    /**
     * Demonstrates predicate composition.
     */
    public static void predicateComposition() {
        System.out.println("\n=== Predicate Composition ===\n");

        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isLessThan100 = n -> n < 100;

        // AND
        Predicate<Integer> isPositiveEven = isPositive.and(isEven);
        System.out.println("5 is positive even? " + isPositiveEven.test(5));
        System.out.println("6 is positive even? " + isPositiveEven.test(6));

        // OR
        Predicate<Integer> isSmallOrNegative = isPositive.negate().or(isLessThan100);
        System.out.println("-5 is small or negative? " + isSmallOrNegative.test(-5));

        // NEGATE
        Predicate<Integer> isNotPositive = isPositive.negate();
        System.out.println("-1 is not positive? " + isNotPositive.test(-1));
    }

    /**
     * Demonstrates function composition.
     */
    public static void functionComposition() {
        System.out.println("\n=== Function Composition ===\n");

        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;
        Function<Integer, Integer> square = x -> x * x;

        // andThen: applies this function first, then the argument
        Function<Integer, Integer> doubleThenAddTen = doubleIt.andThen(addTen);
        System.out.println("5 double then add 10: " + doubleThenAddTen.apply(5));

        // compose: applies the argument first, then this function
        Function<Integer, Integer> addTenThenSquare = square.compose(addTen);
        System.out.println("5 add 10 then square: " + addTenThenSquare.apply(5));

        // Chain multiple operations
        Function<Integer, Integer> pipeline = doubleIt
            .andThen(addTen)
            .andThen(square);
        System.out.println("5 double, add 10, square: " + pipeline.apply(5));
    }

    /**
     * Demonstrates lambda performance considerations.
     */
    public static void performanceConsiderations() {
        System.out.println("\n=== Performance Considerations ===\n");

        // Reuse lambda instances (preferred)
        final Function<String, Integer> TO_LENGTH = String::length;
        final IntBinaryOperator ADD = Integer::sum;

        // Benchmark: Reused vs new lambda
        int iterations = 10_000_000;

        // Warmup
        for (int i = 0; i < 1_000_000; i++) {
            TO_LENGTH.apply("test");
            ADD.applyAsInt(0, i);
        }

        // Benchmark: Reused lambda
        long start = System.nanoTime();
        int sum1 = 0;
        for (int i = 0; i < iterations; i++) {
            sum1 = ADD.applyAsInt(sum1, i);
        }
        long reusedTime = System.nanoTime() - start;

        // Benchmark: New lambda each time
        start = System.nanoTime();
        int sum2 = 0;
        for (int i = 0; i < iterations; i++) {
            IntBinaryOperator newLambda = (a, b) -> a + b;
            sum2 = newLambda.applyAsInt(sum2, i);
        }
        long newTime = System.nanoTime() - start;

        System.out.printf("Reused lambda: %.2f ms%n", reusedTime / 1_000_000.0);
        System.out.printf("New lambda: %.2f ms%n", newTime / 1_000_000.0);
        System.out.printf("Speedup: %.2fx%n", (double) newTime / reusedTime);
        System.out.println("Always prefer reusing lambda instances!");
    }

    /**
     * Demonstrates common lambda pitfalls.
     */
    public static void commonPitfalls() {
        System.out.println("\n=== Common Pitfalls ===\n");

        // Pitfall 1: Mutable variable capture (won't compile)
        // int counter = 0;
        // list.forEach(item -> counter++);  // Error!

        // Workaround: Use AtomicInteger
        AtomicInteger counter = new AtomicInteger(0);
        List<String> items = Arrays.asList("a", "b", "c");
        items.forEach(item -> counter.incrementAndGet());
        System.out.println("Counter (AtomicInteger): " + counter.get());

        // Pitfall 2: Incorrect return in block lambda
        // Function<String, Integer> length = s -> {
        //     s.length();  // Error! Missing return
        // };

        // Correct: Explicit return
        Function<String, Integer> length = s -> {
            return s.length();
        };
        System.out.println("Length: " + length.apply("hello"));

        // Pitfall 3: Shadowing outer variables
        String x = "outer";
        Runnable lambda = () -> {
            // String x = "inner";  // Error! Shadows outer variable
            System.out.println("x = " + x);
        };
        lambda.run();
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        basicSyntax();
        typeInference();
        variableCapture();
        scopingRules();
        blockBody();
        predicateComposition();
        functionComposition();
        performanceConsiderations();
        commonPitfalls();

        System.out.println("\n=== Summary ===");
        System.out.println("Key takeaways:");
        System.out.println("1. Lambda syntax: (params) -> expression or { statements; }");
        System.out.println("2. Type inference is automatic from context");
        System.out.println("3. Only effectively final variables can be captured");
        System.out.println("4. Lambdas have their own scope but access outer variables");
        System.out.println("5. Reuse lambda instances for better performance");
        System.out.println("6. Use block body for multiple statements");
    }
}
