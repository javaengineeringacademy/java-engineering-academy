import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;

/**
 * Comprehensive examples of Functional Interfaces in Java 21.
 *
 * <p>This class demonstrates all aspects of functional interfaces including
 * built-in interfaces, custom interfaces, composition patterns, and primitive
 * specialized variants. Each example is self-contained and can be run independently.</p>
 *
 * <p>Topics covered:</p>
 * <ul>
 *   <li>Built-in functional interfaces (Predicate, Function, Consumer, Supplier)</li>
 *   <li>Custom functional interface creation</li>
 *   <li>Composition with default methods</li>
 *   <li>Primitive specialized interfaces</li>
 *   <li>Enterprise patterns</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class FunctionalInterfaceExample {

    private FunctionalInterfaceExample() {
        // Utility class - no instantiation
    }

    /**
     * Demonstrates built-in functional interfaces.
     */
    public static void builtInInterfaces() {
        System.out.println("=== Built-in Functional Interfaces ===\n");

        // Predicate - tests a condition
        Predicate<String> isLong = s -> s.length() > 5;
        System.out.println("Is 'Hello' long? " + isLong.test("Hello"));
        System.out.println("Is 'Functional' long? " + isLong.test("Functional"));

        // Function - transforms a value
        Function<String, Integer> toLength = String::length;
        System.out.println("Length of 'Java': " + toLength.apply("Java"));

        // Consumer - performs an action
        Consumer<String> printer = System.out::println;
        printer.accept("Hello, World!");

        // Supplier - provides a value
        Supplier<Double> randomValue = Math::random;
        System.out.println("Random: " + randomValue.get());

        // UnaryOperator - transforms same type
        UnaryOperator<String> toUpper = String::toUpperCase;
        System.out.println("Uppercase: " + toUpper.apply("hello"));

        // BinaryOperator - combines two values
        BinaryOperator<Integer> add = Integer::sum;
        System.out.println("3 + 4 = " + add.apply(3, 4));
    }

    /**
     * Demonstrates predicate composition.
     */
    public static void predicateComposition() {
        System.out.println("\n=== Predicate Composition ===\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isLessThan10 = n -> n < 10;

        // AND composition
        Predicate<Integer> isPositiveEven = isPositive.and(isEven);
        System.out.println("Positive and even numbers:");
        numbers.stream()
            .filter(isPositiveEven)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // OR composition
        Predicate<Integer> isSmallOrEven = isLessThan10.or(isEven);
        System.out.println("\nLess than 10 or even numbers:");
        numbers.stream()
            .filter(isSmallOrEven)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // NEGATE
        Predicate<Integer> isNotPositive = isPositive.negate();
        System.out.println("\nNot positive numbers:");
        numbers.stream()
            .filter(isNotPositive)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();
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
     * Demonstrates custom functional interfaces.
     */
    public static void customInterfaces() {
        System.out.println("\n=== Custom Functional Interfaces ===\n");

        // Custom Transformer interface
        @FunctionalInterface
        interface Transformer<T, R> {
            R transform(T input);

            default <V> Transformer<T, V> andThen(Transformer<R, V> after) {
                return input -> after.transform(transform(input));
            }

            static <T> Transformer<T, T> identity() {
                return input -> input;
            }
        }

        Transformer<String, Integer> toLength = String::length;
        Transformer<Integer, String> toString = Object::toString;
        Transformer<String, String> pipeline = toLength.andThen(toString);

        System.out.println("Pipeline 'hello': " + pipeline.transform("hello"));

        // Custom Validator interface
        @FunctionalInterface
        interface Validator<T> {
            boolean validate(T value);

            default Validator<T> and(Validator<T> other) {
                return value -> this.validate(value) && other.validate(value);
            }

            default Validator<T> or(Validator<T> other) {
                return value -> this.validate(value) || other.validate(value);
            }

            default Validator<T> negate() {
                return value -> !this.validate(value);
            }
        }

        Validator<String> isNotEmpty = s -> !s.isEmpty();
        Validator<String> hasMinLength = s -> s.length() >= 3;
        Validator<String> isValid = isNotEmpty.and(hasMinLength);

        System.out.println("Is 'ab' valid? " + isValid.validate("ab"));
        System.out.println("Is 'abc' valid? " + isValid.validate("abc"));
    }

    /**
     * Demonstrates primitive specialized interfaces.
     */
    public static void primitiveInterfaces() {
        System.out.println("\n=== Primitive Specialized Interfaces ===\n");

        // IntPredicate (avoids Integer boxing)
        IntPredicate isEven = n -> n % 2 == 0;
        System.out.println("Is 4 even? " + isEven.test(4));
        System.out.println("Is 5 even? " + isEven.test(5));

        // IntUnaryOperator (avoids Integer boxing)
        IntUnaryOperator square = x -> x * x;
        System.out.println("Square of 5: " + square.applyAsInt(5));

        // IntConsumer (avoids Integer boxing)
        IntConsumer printSquare = x -> System.out.println("Square: " + (x * x));
        printSquare.accept(4);

        // IntSupplier (avoids Integer boxing)
        IntSupplier randomInt = () -> (int) (Math.random() * 100);
        System.out.println("Random int: " + randomInt.getAsInt());

        // Performance comparison
        int iterations = 10_000_000;

        // Generic UnaryOperator (boxing overhead)
        long start = System.nanoTime();
        UnaryOperator<Integer> genericSquare = x -> x * x;
        int sum1 = 0;
        for (int i = 0; i < iterations; i++) {
            sum1 += genericSquare.apply(i);
        }
        long genericTime = System.nanoTime() - start;

        // Primitive IntUnaryOperator (no boxing)
        start = System.nanoTime();
        IntUnaryOperator primitiveSquare = x -> x * x;
        int sum2 = 0;
        for (int i = 0; i < iterations; i++) {
            sum2 += primitiveSquare.applyAsInt(i);
        }
        long primitiveTime = System.nanoTime() - start;

        System.out.printf("Generic: %.2f ms, Primitive: %.2f ms, Speedup: %.2fx%n",
            genericTime / 1_000_000.0,
            primitiveTime / 1_000_000.0,
            (double) genericTime / primitiveTime);
    }

    /**
     * Demonstrates enterprise patterns.
     */
    public static void enterprisePatterns() {
        System.out.println("\n=== Enterprise Patterns ===\n");

        // Order record
        record Order(String id, String status, BigDecimal amount, LocalDateTime createdAt) {}

        // Functional interfaces for order processing
        @FunctionalInterface
        interface OrderValidator {
            boolean validate(Order order);

            default OrderValidator and(OrderValidator other) {
                return order -> this.validate(order) && other.validate(order);
            }
        }

        @FunctionalInterface
        interface OrderTransformer<T> {
            T transform(Order order);

            default <V> OrderTransformer<V> andThen(java.util.function.Function<? super T, ? extends V> after) {
                return order -> after.apply(this.transform(order));
            }
        }

        // Create test data
        List<Order> orders = List.of(
            new Order("ORD-001", "PENDING", new BigDecimal("99.99"), LocalDateTime.now().minusDays(2)),
            new Order("ORD-002", "SHIPPED", new BigDecimal("149.99"), LocalDateTime.now().minusDays(10)),
            new Order("ORD-003", "PENDING", new BigDecimal("0"), LocalDateTime.now().minusDays(1))
        );

        // Define validators
        OrderValidator hasValidAmount = order -> order.amount().compareTo(BigDecimal.ZERO) > 0;
        OrderValidator isRecent = order -> order.createdAt().isAfter(LocalDateTime.now().minusDays(7));
        OrderValidator isPending = order -> "PENDING".equals(order.status());

        OrderValidator processable = hasValidAmount.and(isRecent).and(isPending);

        // Define transformers
        OrderTransformer<String> toSummary = order -> 
            String.format("%s: %s - $%s", order.id(), order.status(), order.amount());

        // Process orders
        System.out.println("Processable orders:");
        orders.stream()
            .filter(processable::validate)
            .map(toSummary::transform)
            .forEach(s -> System.out.println("  " + s));
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        builtInInterfaces();
        predicateComposition();
        functionComposition();
        customInterfaces();
        primitiveInterfaces();
        enterprisePatterns();

        System.out.println("\n=== Summary ===");
        System.out.println("Key takeaways:");
        System.out.println("1. Functional interfaces have exactly one abstract method");
        System.out.println("2. Built-in interfaces: Predicate, Function, Consumer, Supplier");
        System.out.println("3. Composition methods: and(), or(), andThen(), compose()");
        System.out.println("4. Primitive specialized interfaces avoid boxing overhead");
        System.out.println("5. @FunctionalInterface annotation provides compile-time validation");
    }
}
