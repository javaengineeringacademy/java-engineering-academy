package parallel;

import java.util.*;
import java.util.stream.*;

/**
 * Tests for Parallel Operations
 */
public class ParallelTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Parallel Operations Tests ===\n");

        testParallelStreamIsParallel();
        testParallelFilter();
        testParallelCount();
        testParallelSum();
        testSequentialConversion();
        testParallelReduce();
        testParallelCollect();
        testIsParallelMethod();
        testParallelMap();
        testParallelForEach();

        System.out.println("\n=== Results ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total: " + (passed + failed));

        if (failed > 0) {
            System.exit(1);
        }
    }

    static void testParallelStreamIsParallel() {
        List<Integer> numbers = List.of(1, 2, 3);
        boolean result = numbers.parallelStream().isParallel();
        assertTrue("parallelStream is parallel", result);
    }

    static void testParallelFilter() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        List<Integer> result = numbers.parallelStream()
            .filter(n -> n % 2 == 0)
            .sorted()
            .collect(Collectors.toList());
        assertEqual("Parallel filter", List.of(2, 4, 6), result);
    }

    static void testParallelCount() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        long result = numbers.parallelStream()
            .filter(n -> n > 3)
            .count();
        assertEqual("Parallel count", 2L, result);
    }

    static void testParallelSum() {
        int result = IntStream.rangeClosed(1, 100)
            .parallel()
            .sum();
        assertEqual("Parallel sum", 5050, result);
    }

    static void testSequentialConversion() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        boolean result = numbers.parallelStream()
            .sequential()
            .isParallel();
        assertTrue("Sequential conversion", !result);
    }

    static void testParallelReduce() {
        int result = IntStream.rangeClosed(1, 10)
            .parallel()
            .reduce(0, Integer::sum, Integer::sum);
        assertEqual("Parallel reduce", 55, result);
    }

    static void testParallelCollect() {
        List<Integer> result = IntStream.rangeClosed(1, 10)
            .parallel()
            .boxed()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        Collections.sort(result);
        assertEqual("Parallel collect", List.of(2, 4, 6, 8, 10), result);
    }

    static void testIsParallelMethod() {
        List<Integer> numbers = List.of(1, 2, 3);
        boolean seq = numbers.stream().isParallel();
        boolean par = numbers.parallelStream().isParallel();
        assertTrue("Stream isParallel false", !seq);
        assertTrue("ParallelStream isParallel true", par);
    }

    static void testParallelMap() {
        List<Integer> result = IntStream.rangeClosed(1, 5)
            .parallel()
            .boxed()
            .map(n -> n * 2)
            .sorted()
            .collect(Collectors.toList());
        assertEqual("Parallel map", List.of(2, 4, 6, 8, 10), result);
    }

    static void testParallelForEach() {
        List<Integer> source = IntStream.rangeClosed(1, 10).boxed().toList();
        List<Integer> result = Collections.synchronizedList(new ArrayList<>());
        source.parallelStream().forEach(result::add);
        assertTrue("Parallel forEach size", result.size() == 10);
    }

    // --- Assertion Helpers ---

    static void assertEqual(String testName, Object expected, Object actual) {
        if (Objects.equals(expected, actual)) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            System.out.println("  Expected: " + expected);
            System.out.println("  Actual:   " + actual);
            failed++;
        }
    }

    static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            System.out.println("  Expected: true");
            System.out.println("  Actual:   false");
            failed++;
        }
    }
}
