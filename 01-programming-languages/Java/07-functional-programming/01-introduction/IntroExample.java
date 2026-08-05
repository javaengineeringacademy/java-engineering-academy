import java.util.Arrays;
import java.util.List;
import java.util.function.*;

/**
 * Introduction to Functional Programming in Java 21.
 *
 * <p>This class demonstrates the core concepts of functional programming,
 * including lambda expressions, method references, and functional interfaces.
 * Each example is self-contained and can be run independently.</p>
 *
 * <p>Key concepts covered:</p>
 * <ul>
 *   <li>Anonymous classes vs lambda expressions</li>
 *   <li>Functional interfaces (Predicate, Function, Consumer, Supplier)</li>
 *   <li>Method references</li>
 *   <li>Immutability principles</li>
 *   <li>Pure functions</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class IntroExample {

    private IntroExample() {
        // Utility class - no instantiation
    }

    /**
     * Demonstrates lambda expressions as a replacement for anonymous classes.
     */
    public static void lambdaBasics() {
        System.out.println("=== Lambda Basics ===\n");

        // Anonymous class (pre-Java 8)
        Runnable oldWay = new Runnable() {
            @Override
            public void run() {
                System.out.println("Running from anonymous class");
            }
        };

        // Lambda expression (Java 8+)
        Runnable newWay = () -> System.out.println("Running from lambda");

        // Method reference (even shorter)
        Runnable shortest = System.out::println;

        System.out.print("Anonymous class: ");
        oldWay.run();

        System.out.print("Lambda: ");
        newWay.run();
    }

    /**
     * Demonstrates functional interfaces and their implementations.
     */
    public static void functionalInterfaces() {
        System.out.println("\n=== Functional Interfaces ===\n");

        // Predicate - takes T, returns boolean
        Predicate<String> isLong = s -> s.length() > 5;
        System.out.println("Is 'Hello' long? " + isLong.test("Hello"));
        System.out.println("Is 'Functional' long? " + isLong.test("Functional"));

        // Function - takes T, returns R
        Function<String, Integer> toLength = String::length;
        System.out.println("Length of 'Java': " + toLength.apply("Java"));

        // Consumer - takes T, returns void
        Consumer<String> printer = s -> System.out.println("  Printing: " + s);
        Arrays.asList("Alice", "Bob", "Charlie").forEach(printer);

        // Supplier - takes nothing, returns T
        Supplier<Double> randomValue = Math::random;
        System.out.println("Random value: " + randomValue.get());
    }

    /**
     * Demonstrates method reference variations.
     */
    public static void methodReferences() {
        System.out.println("\n=== Method References ===\n");

        List<String> names = Arrays.asList("charlie", "alice", "bob");

        // Reference to static method
        Function<String, String> upperCase = String::valueOf; // This is just identity
        // Better example:
        java.util.function.UnaryOperator<String> toUpper = String::toUpperCase;
        names.stream()
            .map(toUpper)
            .forEach(s -> System.out.println("  " + s));

        // Reference to instance method of particular object
        String prefix = "Mr./Ms. ";
        Function<String, String> addPrefix = prefix::concat;
        names.stream()
            .map(addPrefix)
            .forEach(s -> System.out.println("  " + s));

        // Reference to instance method of arbitrary object
        List<Integer> lengths = names.stream()
            .map(String::length)
            .toList();
        System.out.println("Lengths: " + lengths);

        // Reference to constructor
        Function<String, StringBuilder> toBuilder = StringBuilder::new;
        StringBuilder builder = toBuilder.apply("Hello");
        System.out.println("Builder: " + builder);
    }

    /**
     * Demonstrates predicate composition.
     */
    public static void predicateComposition() {
        System.out.println("\n=== Predicate Composition ===\n");

        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isLessThan100 = n -> n < 100;

        // AND composition
        Predicate<Integer> isPositiveEven = isPositive.and(isEven);
        System.out.println("5 is positive even? " + isPositiveEven.test(5));
        System.out.println("6 is positive even? " + isPositiveEven.test(6));

        // OR composition
        Predicate<Integer> isSmallOrNegative = isPositive.negate().or(isLessThan100);
        System.out.println("-5 is small or negative? " + isSmallOrNegative.test(-5));
        System.out.println("150 is small or negative? " + isSmallOrNegative.test(150));

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
     * Demonstrates immutability with records.
     */
    public static void immutability() {
        System.out.println("\n=== Immutability with Records ===\n");

        // Record - immutable by default
        record Point(double x, double y) {}

        Point p1 = new Point(3.0, 4.0);
        Point p2 = new Point(10.0, p1.y());

        // Custom with method
        record Person(String name, int age) {
            Person withName(String newName) {
                return new Person(newName, this.age);
            }
            Person withAge(int newAge) {
                return new Person(this.name, newAge);
            }
        }

        Person alice = new Person("Alice", 30);
        Person aliceOlder = alice.withAge(31);
        System.out.println("Original: " + alice);
        System.out.println("Modified: " + aliceOlder);
        System.out.println("Original unchanged: " + alice);
    }

    /**
     * Demonstrates pure functions.
     */
    public static void pureFunctions() {
        System.out.println("\n=== Pure Functions ===\n");

        // Pure function: same input always gives same output, no side effects
        java.util.function.BinaryOperator<Integer> add = Integer::sum;
        System.out.println("2 + 3 = " + add.apply(2, 3));
        System.out.println("2 + 3 = " + add.apply(2, 3)); // Same result

        // Impure function (simulated with mutable state)
        // NOT RECOMMENDED - just for demonstration
        int[] counter = {0};
        java.util.function.UnaryOperator<Integer> increment = x -> {
            counter[0]++; // Side effect!
            return x + 1;
        };

        System.out.println("Increment 5: " + increment.apply(5));
        System.out.println("Counter after first call: " + counter[0]);
        System.out.println("Increment 5 again: " + increment.apply(5));
        System.out.println("Counter after second call: " + counter[0]); // Changed!
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        lambdaBasics();
        functionalInterfaces();
        methodReferences();
        predicateComposition();
        functionComposition();
        immutability();
        pureFunctions();

        System.out.println("\n=== Summary ===");
        System.out.println("Functional programming in Java provides:");
        System.out.println("- Lambda expressions for concise function syntax");
        System.out.println("- Functional interfaces as lambda targets");
        System.out.println("- Method references for even shorter syntax");
        System.out.println("- Immutability for safer code");
        System.out.println("- Pure functions for predictability");
    }
}
