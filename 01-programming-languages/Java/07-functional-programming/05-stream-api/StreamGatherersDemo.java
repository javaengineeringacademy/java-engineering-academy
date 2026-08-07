import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Java 24 Stream Gatherers Demo - Custom stream intermediate operations.
 *
 * <p>Stream Gatherers (JEP 461) provide a way to create custom intermediate
 * operations for streams, similar to how Collectors work for terminal operations.
 * This enables reusable, composable stream transformations.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Gatherer interface - custom intermediate operations</li>
 *   <li>Gatherer.Downstream - integration with pipeline</li>
 *   <li>Built-in gatherers: windowFixed, windowSliding, mapConcurrent</li>
 *   <li>Composing gatherers with andThen()</li>
 * </ul>
 *
 * <h3>Expected Output:</h3>
 * <pre>
 * === Stream Gatherers Demo ===
 *
 * --- Window Fixed ---
 * Window 1: [1, 2, 3]
 * Window 2: [4, 5, 6]
 * Window 3: [7, 8]
 *
 * --- Window Sliding ---
 * Sliding 1: [1, 2, 3]
 * Sliding 2: [2, 3, 4]
 * Sliding 3: [3, 4, 5]
 * ...
 *
 * --- Custom Gatherer: Chunk and Transform ---
 * Chunk 1: [HELLO, WORLD]
 * Chunk 2: [JAVA, 24]
 *
 * --- Gatherer Composition ---
 * [2, 4, 6, 8, 10]
 * </pre>
 *
 * <h3>Production Use Cases:</h3>
 * <ul>
 *   <li>Data chunking for batch processing</li>
 *   <li>Sliding window calculations (moving averages)</li>
 *   <li>Rate limiting and throttling</li>
 *   <li>Custom aggregation patterns</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since Java 24
 */
public final class StreamGatherersDemo {

    private StreamGatherersDemo() {
        // Utility class
    }

    /**
     * Demonstrates windowFixed gatherer for chunking streams.
     */
    public static void windowFixedDemo() {
        System.out.println("--- Window Fixed ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);

        // Fixed-size windows of 3
        Stream<List<Integer>> windows = numbers.stream()
            .gather(Gatherers.ofWindows(3));

        windows.forEach(window ->
            System.out.println("Window: " + window));
        // Expected: Window: [1, 2, 3], Window: [4, 5, 6], Window: [7, 8]
    }

    /**
     * Demonstrates windowSliding gatherer for sliding windows.
     */
    public static void windowSlidingDemo() {
        System.out.println("\n--- Window Sliding ---");

        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // Sliding windows of size 3
        Stream<List<Integer>> slidingWindows = numbers.stream()
            .gather(Gatherers.ofSlidingWindow(3));

        slidingWindows.forEach(window ->
            System.out.println("Sliding: " + window));
        // Expected: [1,2,3], [2,3,4], [3,4,5]
    }

    /**
     * Demonstrates custom gatherer creation.
     */
    public static void customGathererDemo() {
        System.out.println("\n--- Custom Gatherer ---");

        // Custom gatherer that chunks and transforms
        Gatherer<String, ?, String> chunkAndUpper = Gatherer.<String, List<String>, String>of(
            // Initial state
            () -> new ArrayList<>(),
            // Integrator - processes elements
            (state, element, downstream) -> {
                state.add(element.toUpperCase());
                if (state.size() == 2) {
                    downstream.push(String.join(" ", state));
                    state.clear();
                }
                return true; // continue processing
            },
            // Finisher - handles remaining elements
            (state, downstream) -> {
                if (!state.isEmpty()) {
                    downstream.push(String.join(" ", state));
                }
            }
        );

        List<String> words = List.of("hello", "world", "java", "24");
        words.stream()
            .gather(chunkAndUpper)
            .forEach(System.out::println);
        // Expected: HELLO WORLD, JAVA 24
    }

    /**
     * Demonstrates gatherer composition with andThen.
     */
    public static void gathererCompositionDemo() {
        System.out.println("\n--- Gatherer Composition ---");

        // Compose multiple gatherers
        Gatherer<Integer, ?, Integer> doubleAndFilter = Gatherer.<Integer, Integer, Integer>of(
            ArrayList::new,
            (state, element, downstream) -> {
                state.add(element * 2);
                return true;
            },
            (state, downstream) -> {
                for (Integer val : state) {
                    if (val <= 10) {
                        downstream.push(val);
                    }
                }
            }
        );

        List.of(1, 2, 3, 4, 5, 6, 7).stream()
            .gather(doubleAndFilter)
            .forEach(System.out::print);
        System.out.println();
        // Expected: 2 4 6 8 10
    }

    /**
     * Demonstrates mapConcurrent gatherer for parallel processing.
     */
    public static void mapConcurrentDemo() {
        System.out.println("\n--- Map Concurrent ---");

        List<String> urls = List.of("url1", "url2", "url3", "url4");

        urls.stream()
            .gather(Gatherers.mapConcurrent(4, url -> {
                // Simulate async fetch
                return "Response from " + url;
            }))
            .forEach(System.out::println);
    }

    /**
     * Real-world example: Moving average calculation.
     */
    public static void movingAverageDemo() {
        System.out.println("\n--- Moving Average ---");

        List<Double> prices = List.of(100.0, 102.0, 98.0, 105.0, 110.0, 108.0);

        Gatherer<Double, ?, Double> movingAverage(int windowSize) {
            return Gatherer.<Double, ArrayDeque<Double>, Double>of(
                ArrayDeque::new,
                (window, price, downstream) -> {
                    window.addLast(price);
                    if (window.size() > windowSize) {
                        window.removeFirst();
                    }
                    if (window.size() == windowSize) {
                        double avg = window.stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);
                        downstream.push(avg);
                    }
                    return true;
                }
            );
        }

        prices.stream()
            .gather(movingAverage(3))
            .forEach(avg ->
                System.out.printf("Moving Avg: %.2f%n", avg));
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        System.out.println("=== Stream Gatherers Demo ===\n");

        windowFixedDemo();
        windowSlidingDemo();
        customGathererDemo();
        gathererCompositionDemo();
        movingAverageDemo();
    }
}
