package academy.javaengineering.functional.references;

import java.util.*;
import java.util.function.*;

/**
 * Comprehensive examples of Method References in Java 21.
 *
 * <p>This class demonstrates all four types of method references and their
 * applications in functional programming. Each example is self-contained
 * and can be run independently.</p>
 *
 * <p>Topics covered:</p>
 * <ul>
 *   <li>Static method references</li>
 *   <li>Instance method references of particular objects</li>
 *   <li>Instance method references of arbitrary objects</li>
 *   <li>Constructor references</li>
 *   <li>Method reference composition</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since 1.0
 */
public final class MethodReferenceExamples {

    private MethodReferenceExamples() {
        // Utility class - no instantiation
    }

    /**
     * Demonstrates static method references.
     */
    public static void staticMethodReferences() {
        System.out.println("=== Static Method References ===\n");

        // Lambda
        Function<String, Integer> parseIntLambda = s -> Integer.parseInt(s);

        // Method reference
        Function<String, Integer> parseIntRef = Integer::parseInt;

        System.out.println("Parse '123' (lambda): " + parseIntLambda.apply("123"));
        System.out.println("Parse '123' (reference): " + parseIntRef.apply("123"));

        // Integer.sum
        BinaryOperator<Integer> addLambda = (a, b) -> Integer.sum(a, b);
        BinaryOperator<Integer> addRef = Integer::sum;

        System.out.println("3 + 4 (lambda): " + addLambda.apply(3, 4));
        System.out.println("3 + 4 (reference): " + addRef.apply(3, 4));

        // Math.abs
        Function<Integer, Integer> absLambda = n -> Math.abs(n);
        Function<Integer, Integer> absRef = Math::abs;

        System.out.println("abs(-5) (lambda): " + absLambda.apply(-5));
        System.out.println("abs(-5) (reference): " + absRef.apply(-5));
    }

    /**
     * Demonstrates instance method references of particular objects.
     */
    public static void particularObjectReferences() {
        System.out.println("\n=== Instance Method References (Particular Object) ===\n");

        // String concatenation with particular object
        String prefix = "Mr. ";
        Function<String, String> addPrefixLambda = s -> prefix.concat(s);
        Function<String, String> addPrefixRef = prefix::concat;

        System.out.println("Smith (lambda): " + addPrefixLambda.apply("Smith"));
        System.out.println("Smith (reference): " + addPrefixRef.apply("Smith"));

        // StringBuilder
        StringBuilder builder = new StringBuilder();
        Supplier<String> toStringLambda = () -> builder.toString();
        Supplier<String> toStringRef = builder::toString;

        builder.append("Hello");
        System.out.println("Builder (lambda): " + toStringLambda.get());
        builder.append(" World");
        System.out.println("Builder (reference): " + toStringRef.get());

        // List operations
        List<String> list = new ArrayList<>();
        Consumer<String> addLambda = s -> list.add(s);
        Consumer<String> addRef = list::add;

        addLambda.accept("Lambda");
        addRef.accept("Reference");
        System.out.println("List: " + list);
    }

    /**
     * Demonstrates instance method references of arbitrary objects.
     */
    public static void arbitraryObjectReferences() {
        System.out.println("\n=== Instance Method References (Arbitrary Object) ===\n");

        // String.length
        Function<String, Integer> lengthLambda = s -> s.length();
        Function<String, Integer> lengthRef = String::length;

        System.out.println("Length of 'hello' (lambda): " + lengthLambda.apply("hello"));
        System.out.println("Length of 'hello' (reference): " + lengthRef.apply("hello"));

        // String.isEmpty
        Predicate<String> isEmptyLambda = s -> s.isEmpty();
        Predicate<String> isEmptyRef = String::isEmpty;

        System.out.println("Is '' empty (lambda): " + isEmptyLambda.test(""));
        System.out.println("Is '' empty (reference): " + isEmptyRef.test(""));
        System.out.println("Is 'hello' empty (lambda): " + isEmptyLambda.test("hello"));
        System.out.println("Is 'hello' empty (reference): " + isEmptyRef.test("hello"));

        // String.toUpperCase
        UnaryOperator<String> toUpperLambda = s -> s.toUpperCase();
        UnaryOperator<String> toUpperRef = String::toUpperCase;

        System.out.println("hello (lambda): " + toUpperLambda.apply("hello"));
        System.out.println("hello (reference): " + toUpperRef.apply("hello"));
    }

    /**
     * Demonstrates constructor references.
     */
    public static void constructorReferences() {
        System.out.println("\n=== Constructor References ===\n");

        // StringBuilder constructor
        Supplier<StringBuilder> lambdaBuilder = () -> new StringBuilder();
        Supplier<StringBuilder> refBuilder = StringBuilder::new;

        StringBuilder sb1 = lambdaBuilder.get();
        StringBuilder sb2 = refBuilder.get();
        sb1.append("Lambda");
        sb2.append("Reference");

        System.out.println("Lambda: " + sb1);
        System.out.println("Reference: " + sb2);

        // ArrayList constructor
        Supplier<List<String>> listLambda = () -> new ArrayList<>();
        Supplier<List<String>> listRef = ArrayList::new;

        List<String> list1 = listLambda.get();
        List<String> list2 = listRef.get();
        list1.add("Lambda");
        list2.add("Reference");

        System.out.println("List (lambda): " + list1);
        System.out.println("List (reference): " + list2);

        // Array constructor
        Function<Integer, int[]> arrayLambda = n -> new int[n];
        Function<Integer, int[]> arrayRef = int[]::new;

        int[] arr1 = arrayLambda.apply(5);
        int[] arr2 = arrayRef.apply(5);

        System.out.println("Array (lambda) length: " + arr1.length);
        System.out.println("Array (reference) length: " + arr2.length);
    }

    /**
     * Demonstrates method references in stream operations.
     */
    public static void streamMethodReferences() {
        System.out.println("\n=== Method References in Streams ===\n");

        List<String> names = Arrays.asList("alice", "bob", "charlie", "diana", "eve");

        // Map with method reference
        List<String> upperNames = names.stream()
            .map(String::toUpperCase)
            .toList();
        System.out.println("Uppercase: " + upperNames);

        // Filter with method reference
        List<String> nonEmpty = names.stream()
            .filter(s -> !s.isEmpty())
            .toList();
        System.out.println("Non-empty: " + nonEmpty);

        // Sort with method reference
        List<String> sorted = names.stream()
            .sorted(String::compareToIgnoreCase)
            .toList();
        System.out.println("Sorted: " + sorted);

        // ForEach with method reference
        System.out.print("Print: ");
        names.stream()
            .limit(3)
            .forEach(System.out::println);
    }

    /**
     * Demonstrates method reference composition.
     */
    public static void composition() {
        System.out.println("\n=== Method Reference Composition ===\n");

        // Compose string operations
        Function<String, String> trim = String::trim;
        Function<String, String> toLower = String::toLowerCase;
        Function<String, Integer> length = String::length;

        Function<String, Integer> processAndMeasure = trim
            .andThen(toLower)
            .andThen(length);

        System.out.println("Process '  Hello  ': " + processAndMeasure.apply("  Hello  "));

        // Compose with predicates
        Predicate<String> isNotEmpty = s -> !s.isEmpty();
        Predicate<String> hasMinLength = s -> s.length() >= 3;

        Predicate<String> isValid = isNotEmpty.and(hasMinLength);

        System.out.println("Is 'ab' valid? " + isValid.test("ab"));
        System.out.println("Is 'abc' valid? " + isValid.test("abc"));

        // Compose with consumers
        Consumer<String> print = System.out::println;
        Consumer<String> log = s -> System.out.println("LOG: " + s);

        Consumer<String> printAndLog = print.andThen(log);
        printAndLog.accept("Hello");
    }

    /**
     * Main method to run all examples.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        staticMethodReferences();
        particularObjectReferences();
        arbitraryObjectReferences();
        constructorReferences();
        streamMethodReferences();
        composition();

        System.out.println("\n=== Summary ===");
        System.out.println("Key takeaways:");
        System.out.println("1. Static method: ClassName::staticMethod");
        System.out.println("2. Instance of particular object: object::instanceMethod");
        System.out.println("3. Instance of arbitrary object: ClassName::instanceMethod");
        System.out.println("4. Constructor: ClassName::new");
        System.out.println("5. Method references are more readable than equivalent lambdas");
    }
}
