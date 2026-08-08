import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Demonstrates Stream as iteration abstraction.
 */
public class StreamIterationDemo {

    public static void main(String[] args) {
        basicStream();
        filterMapCollect();
        lazyEvaluation();
        parallelStream();
        infiniteStream();
    }

    static void basicStream() {
        System.out.println("=== Basic Stream ===");
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana");

        names.stream()
            .forEach(name -> System.out.println("Name: " + name));
        System.out.println();
    }

    static void filterMapCollect() {
        System.out.println("=== Filter + Map + Collect ===");
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> evenSquares = numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * n)
            .collect(Collectors.toList());

        System.out.println("Even squares: " + evenSquares);
        System.out.println();
    }

    static void lazyEvaluation() {
        System.out.println("=== Lazy Evaluation ===");
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana", "Eve");

        System.out.println("Before terminal operation - nothing printed");

        long count = names.stream()
            .filter(name -> {
                System.out.println("Filtering: " + name);
                return name.length() > 3;
            })
            .count();

        System.out.println("Count: " + count);
        System.out.println();
    }

    static void parallelStream() {
        System.out.println("=== Parallel Stream ===");
        List<Integer> numbers = IntStream.rangeClosed(1, 10).boxed().toList();

        int sum = numbers.parallelStream()
            .reduce(0, Integer::sum);

        System.out.println("Sum: " + sum);

        // Parallel processing
        System.out.println("Parallel processing:");
        numbers.parallelStream()
            .filter(n -> n % 2 == 0)
            .forEach(n -> System.out.println("  Thread: " + Thread.currentThread().getName() + " - " + n));
        System.out.println();
    }

    static void infiniteStream() {
        System.out.println("=== Infinite Stream ===");
        // Generate first 5 even numbers
        List<Integer> evens = Stream.iterate(0, n -> n + 2)
            .limit(5)
            .collect(Collectors.toList());

        System.out.println("First 5 evens: " + evens);

        // Generate random numbers
        List<Double> randoms = Stream.generate(Math::random)
            .limit(3)
            .collect(Collectors.toList());

        System.out.println("3 randoms: " + randoms);
        System.out.println();
    }
}
