package combining;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

/**
 * Combining Operations Examples
 * Demonstrates concat(), Stream.of(), iterate(), generate()
 */
public class CombiningExample {

    public static void main(String[] args) {
        System.out.println("=== Combining Operations ===\n");

        concatExample();
        streamOfExample();
        streamEmptyExample();
        iterateExample();
        generateExample();
        builderExample();
        practicalExamples();
    }

    // --- concat() Examples ---

    static void concatExample() {
        System.out.println("--- concat() Examples ---");

        Stream<Integer> s1 = Stream.of(1, 2, 3);
        Stream<Integer> s2 = Stream.of(4, 5, 6);

        List<Integer> result = Stream.concat(s1, s2)
            .collect(Collectors.toList());
        System.out.println("Concat: " + result);

        // Concat three streams
        Stream<Integer> s3 = Stream.of(7, 8, 9);
        List<Integer> result2 = Stream.concat(
            Stream.concat(Stream.of(1, 2), Stream.of(3, 4)),
            Stream.of(5, 6)
        ).collect(Collectors.toList());
        System.out.println("Triple concat: " + result2);

        System.out.println();
    }

    // --- Stream.of() Examples ---

    static void streamOfExample() {
        System.out.println("--- Stream.of() Examples ---");

        // From values
        Stream<String> stream = Stream.of("Alice", "Bob", "Charlie");
        List<String> result = stream.collect(Collectors.toList());
        System.out.println("From values: " + result);

        // From array
        Integer[] numbers = {1, 2, 3, 4, 5};
        List<Integer> fromArray = Stream.of(numbers)
            .collect(Collectors.toList());
        System.out.println("From array: " + fromArray);

        // Single value
        Stream<Integer> single = Stream.of(42);
        System.out.println("Single value count: " + single.count());

        System.out.println();
    }

    // --- Stream.empty() Examples ---

    static void streamEmptyExample() {
        System.out.println("--- Stream.empty() Examples ---");

        Stream<String> empty = Stream.empty();
        long count = empty.count();
        System.out.println("Empty stream count: " + count);

        // Conditional stream
        boolean condition = false;
        Stream<String> conditional = condition ? Stream.of("value") : Stream.empty();
        System.out.println("Conditional count: " + conditional.count());

        System.out.println();
    }

    // --- iterate() Examples ---

    static void iterateExample() {
        System.out.println("--- iterate() Examples ---");

        // Powers of 2
        List<Integer> powersOf2 = Stream.iterate(1, n -> n * 2)
            .limit(10)
            .collect(Collectors.toList());
        System.out.println("Powers of 2: " + powersOf2);

        // Fibonacci sequence
        List<Integer> fibonacci = Stream.iterate(
                new int[]{0, 1},
                fib -> new int[]{fib[1], fib[0] + fib[1]}
            )
            .limit(10)
            .map(fib -> fib[0])
            .collect(Collectors.toList());
        System.out.println("Fibonacci: " + fibonacci);

        // Counting
        List<Integer> counting = Stream.iterate(1, n -> n + 1)
            .limit(10)
            .collect(Collectors.toList());
        System.out.println("Counting: " + counting);

        System.out.println();
    }

    // --- generate() Examples ---

    static void generateExample() {
        System.out.println("--- generate() Examples ---");

        // Random numbers
        List<Double> randoms = Stream.generate(Math::random)
            .limit(5)
            .collect(Collectors.toList());
        System.out.println("Randoms: " + randoms);

        // Constant value
        List<String> zeros = Stream.generate(() -> "0")
            .limit(5)
            .collect(Collectors.toList());
        System.out.println("Zeros: " + zeros);

        // With state
        AtomicInteger counter = new AtomicInteger(0);
        List<Integer> counting = Stream.generate(counter::incrementAndGet)
            .limit(10)
            .collect(Collectors.toList());
        System.out.println("Counting: " + counting);

        System.out.println();
    }

    // --- Builder Examples ---

    static void builderExample() {
        System.out.println("--- Builder Examples ---");

        Stream<String> stream = Stream.<String>builder()
            .add("Alice")
            .add("Bob")
            .add("Charlie")
            .build();
        List<String> result = stream.collect(Collectors.toList());
        System.out.println("Builder: " + result);

        System.out.println();
    }

    // --- Practical Examples ---

    static void practicalExamples() {
        System.out.println("--- Practical Examples ---");

        // Example 1: Combine data sources
        Stream<String> source1 = Stream.of("Alice", "Bob");
        Stream<String> source2 = Stream.of("Charlie", "David");
        Stream<String> source3 = Stream.of("Eve");

        List<String> allNames = Stream.concat(
            Stream.concat(source1, source2),
            source3
        ).collect(Collectors.toList());
        System.out.println("All names: " + allNames);

        // Example 2: Generate sequence
        List<Integer> sequence = IntStream.rangeClosed(1, 20)
            .boxed()
            .collect(Collectors.toList());
        System.out.println("Sequence: " + sequence);

        // Example 3: Geometric series
        List<Double> geometric = Stream.iterate(1.0, n -> n / 2)
            .limit(8)
            .collect(Collectors.toList());
        System.out.println("Geometric: " + geometric);

        // Example 4: Repeat value
        List<String> repeated = Stream.generate(() -> "Hello")
            .limit(5)
            .collect(Collectors.toList());
        System.out.println("Repeated: " + repeated);

        System.out.println();
    }
}
