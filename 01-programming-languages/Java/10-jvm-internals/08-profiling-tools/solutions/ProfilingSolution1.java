package academy.javaengineering.jvm.profiling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Solution 1: JMH Benchmark Exercise - ArrayList vs LinkedList
 */
public class ProfilingSolution1 {

    private static final int DATA_SIZE = 10_000;
    private static final int WARMUP_ITERATIONS = 100;
    private static final int MEASUREMENT_ITERATIONS = 500;

    public static void main(String[] args) {
        System.out.println("=== JMH-Style Benchmark Solution ===\n");
        System.out.printf("Data size: %d, Warmup: %d, Measurement: %d%n%n",
                DATA_SIZE, WARMUP_ITERATIONS, MEASUREMENT_ITERATIONS);

        benchmarkAddToBeginning();
        benchmarkAddToEnd();
        benchmarkGetByIndex();
        benchmarkContains();
    }

    static void benchmarkAddToBeginning() {
        System.out.println("--- Add to Beginning ---");

        // ArrayList
        warmup(() -> {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 100; i++) list.add(0, i);
        });

        long arrayListNanos = measureAverageNanos(() -> {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < DATA_SIZE; i++) list.add(0, i);
        });

        // LinkedList
        warmup(() -> {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < 100; i++) list.add(0, i);
        });

        long linkedListNanos = measureAverageNanos(() -> {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < DATA_SIZE; i++) list.add(0, i);
        });

        System.out.printf("  ArrayList:  %,d ns/op%n", arrayListNanos);
        System.out.printf("  LinkedList: %,d ns/op%n", linkedListNanos);
        System.out.printf("  Winner: %s (%.1fx faster)%n%n",
                linkedListNanos < arrayListNanos ? "LinkedList" : "ArrayList",
                (double) Math.max(arrayListNanos, linkedListNanos) /
                        Math.min(arrayListNanos, linkedListNanos));
    }

    static void benchmarkAddToEnd() {
        System.out.println("--- Add to End ---");

        warmup(() -> {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 100; i++) list.add(i);
        });

        long arrayListNanos = measureAverageNanos(() -> {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < DATA_SIZE; i++) list.add(i);
        });

        warmup(() -> {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < 100; i++) list.add(i);
        });

        long linkedListNanos = measureAverageNanos(() -> {
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < DATA_SIZE; i++) list.add(i);
        });

        System.out.printf("  ArrayList:  %,d ns/op%n", arrayListNanos);
        System.out.printf("  LinkedList: %,d ns/op%n", linkedListNanos);
        System.out.printf("  Winner: %s (%.1fx faster)%n%n",
                linkedListNanos < arrayListNanos ? "LinkedList" : "ArrayList",
                (double) Math.max(arrayListNanos, linkedListNanos) /
                        Math.min(arrayListNanos, linkedListNanos));
    }

    static void benchmarkGetByIndex() {
        System.out.println("--- Get By Index ---");

        List<Integer> arrayList = createArrayList(DATA_SIZE);
        List<Integer> linkedList = new LinkedList<>(arrayList);

        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            arrayList.get(DATA_SIZE / 2);
            linkedList.get(DATA_SIZE / 2);
        }

        long arrayListNanos = measureAverageNanos(() -> {
            for (int i = 0; i < DATA_SIZE; i++) {
                arrayList.get(i);
            }
        });

        long linkedListNanos = measureAverageNanos(() -> {
            for (int i = 0; i < DATA_SIZE; i++) {
                linkedList.get(i);
            }
        });

        System.out.printf("  ArrayList:  %,d ns/op (O(1) random access)%n", arrayListNanos);
        System.out.printf("  LinkedList: %,d ns/op (O(n) traversal)%n", linkedListNanos);
        System.out.printf("  Winner: ArrayList (%.1fx faster)%n%n",
                (double) linkedListNanos / arrayListNanos);
    }

    static void benchmarkContains() {
        System.out.println("--- Contains (search) ---");

        List<Integer> arrayList = createArrayList(DATA_SIZE);
        List<Integer> linkedList = new LinkedList<>(arrayList);
        List<Integer> searchTargets = IntStream.range(0, DATA_SIZE)
                .filter(i -> i % 100 == 0).boxed().collect(Collectors.toList());

        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS / 10; i++) {
            for (int target : searchTargets) {
                arrayList.contains(target);
                linkedList.contains(target);
            }
        }

        long arrayListNanos = measureAverageNanos(() -> {
            for (int target : searchTargets) {
                arrayList.contains(target);
            }
        });

        long linkedListNanos = measureAverageNanos(() -> {
            for (int target : searchTargets) {
                linkedList.contains(target);
            }
        });

        System.out.printf("  ArrayList:  %,d ns/op (better cache locality)%n", arrayListNanos);
        System.out.printf("  LinkedList: %,d ns/op (pointer chasing)%n", linkedListNanos);
        System.out.printf("  Winner: ArrayList (%.1fx faster)%n%n",
                (double) linkedListNanos / arrayListNanos);

        System.out.println("Summary:");
        System.out.println("  - ArrayList wins for random access and search (cache locality)");
        System.out.println("  - LinkedList wins for frequent insertions at the beginning");
        System.out.println("  - Both have similar performance for appending to end");
        System.out.println("  - In practice, ArrayList is preferred 99% of the time");
    }

    static List<Integer> createArrayList(int size) {
        return IntStream.range(0, size).boxed().collect(Collectors.toList());
    }

    static void warmup(Runnable benchmark) {
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            benchmark.run();
        }
    }

    static long measureAverageNanos(Runnable operation) {
        long totalNanos = 0;
        for (int i = 0; i < MEASUREMENT_ITERATIONS; i++) {
            long start = System.nanoTime();
            operation.run();
            totalNanos += System.nanoTime() - start;
        }
        return totalNanos / MEASUREMENT_ITERATIONS;
    }
}
