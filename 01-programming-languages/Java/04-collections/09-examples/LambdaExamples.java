package academy.javaengineering.collections.examples;

import java.util.*;
import java.util.function.*;

public class LambdaExamples {
    public static void main(String[] args) {
        System.out.println("=== Lambda Examples ===\n");

        // Predicate - T -> boolean
        System.out.println("--- Predicate ---");
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;
        System.out.println("Is 4 even? " + isEven.test(4));
        System.out.println("Is -3 positive? " + isPositive.test(-3));

        // Combine predicates
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);
        System.out.println("Is 4 even and positive? " + isEvenAndPositive.test(4));

        List<Integer> numbers = Arrays.asList(-2, -1, 0, 1, 2, 3, 4, 5);
        System.out.println("Even and positive: " + filterList(numbers, isEvenAndPositive));

        // Function - T -> R
        System.out.println("\n--- Function ---");
        Function<String, Integer> toLength = String::length;
        Function<Integer, String> intToString = n -> "Number: " + n;
        System.out.println("Length of Java: " + toLength.apply("Java"));
        System.out.println("Int to string: " + intToString.apply(42));

        // Chain functions
        Function<String, String> upperThenConcat = toLength
            .andThen(n -> "Length " + n)
            .andThen(s -> s + " chars");
        System.out.println("Chain: " + upperThenConcat.apply("Hello"));

        // Consumer - T -> void
        System.out.println("\n--- Consumer ---");
        Consumer<String> print = System.out::println;
        Consumer<String> printUpper = s -> System.out.println(s.toUpperCase());
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        names.forEach(print);
        names.forEach(printUpper);

        // Supplier - () -> T
        System.out.println("\n--- Supplier ---");
        Supplier<List<String>> listFactory = ArrayList::new;
        Supplier<Double> randomDouble = Math::random;
        List<String> newList = listFactory.get();
        System.out.println("New list: " + newList);
        System.out.println("Random: " + randomDouble.get());

        // UnaryOperator - T -> T
        System.out.println("\n--- UnaryOperator ---");
        UnaryOperator<String> exclaim = s -> s + "!";
        UnaryOperator<String> question = s -> s + "?";
        System.out.println(exclaim.apply("Hello"));
        System.out.println(question.apply("Hello"));

        // BinaryOperator - (T, T) -> T
        System.out.println("\n--- BinaryOperator ---");
        BinaryOperator<Integer> sum = Integer::sum;
        BinaryOperator<Integer> max = BinaryOperator.maxBy(Integer::compareTo);
        System.out.println("Sum: " + sum.apply(3, 4));
        System.out.println("Max: " + max.apply(3, 4));
    }

    static <T> List<T> filterList(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
