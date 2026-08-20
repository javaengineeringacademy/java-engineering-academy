package academy.javaengineering.testing.examples;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Performance Testing Demo - JMH, Gatling
 */
class PerformanceTestingDemo {

    // ============================================
    // Performance Testing Concepts
    // ============================================

    /*
     * Performance testing evaluates:
     * - Throughput: Operations per second
     * - Latency: Time per operation
     * - Scalability: Performance under load
     * - Resource usage: CPU, memory, I/O
     * 
     * Types:
     * - Micro-benchmarks (JMH)
     * - Load testing (Gatling, JMeter)
     * - Stress testing
     * - Endurance testing
     */

    // ============================================
    // JMH Style Benchmark (Conceptual)
    // ============================================

    /*
     * JMH (Java Microbenchmark Harness) is the standard for
     * micro-benchmarking in Java.
     * 
     * Dependencies:
     * <dependency>
     *     <groupId>org.openjdk.jmh</groupId>
     *     <artifactId>jmh-core</artifactId>
     *     <version>1.37</version>
     * </dependency>
     * <dependency>
     *     <groupId>org.openjdk.jmh</groupId>
     *     <artifactId>jmh-generator-annprocess</artifactId>
     *     <version>1.37</version>
     *     <scope>provided</scope>
     * </dependency>
     */

    // ============================================
    // String Performance Comparison
    // ============================================

    static class StringPerformance {
        // StringBuilder vs String concatenation
        static String concatenateWithPlus(int iterations) {
            String result = "";
            for (int i = 0; i < iterations; i++) {
                result += "a";
            }
            return result;
        }

        static String concatenateWithBuilder(int iterations) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < iterations; i++) {
                sb.append("a");
            }
            return sb.toString();
        }

        static String concatenateWithBuffer(int iterations) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < iterations; i++) {
                sb.append("a");
            }
            return sb.toString();
        }
    }

    /*
     * JMH Benchmark:
     * 
     * @BenchmarkMode(Mode.AverageTime)
     * @OutputTimeUnit(TimeUnit.MILLISECONDS)
     * @State(Scope.Benchmark)
     * public class StringBenchmark {
     * 
     *     @Param({"100", "1000", "10000"})
     *     private int iterations;
     * 
     *     @Benchmark
     *     public String testStringConcatenation() {
     *         return StringPerformance.concatenateWithPlus(iterations);
     *     }
     * 
     *     @Benchmark
     *     public String testStringBuilder() {
     *         return StringPerformance.concatenateWithBuilder(iterations);
     *     }
     * 
     *     @Benchmark
     *     public String testStringBuffer() {
     *         return StringPerformance.concatenateWithBuffer(iterations);
     *     }
     * 
     *     public static void main(String[] args) throws Exception {
     *         Options opt = new OptionsBuilder()
     *             .include(StringBenchmark.class.getSimpleName())
     *             .forks(1)
     *             .build();
     *         new Runner(opt).run();
     *     }
     * }
     */

    // ============================================
    // Sorting Algorithm Performance
    // ============================================

    static class SortingPerformance {
        static void bubbleSort(int[] arr) {
            int n = arr.length;
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                    }
                }
            }
        }

        static void insertionSort(int[] arr) {
            int n = arr.length;
            for (int i = 1; i < n; i++) {
                int key = arr[i];
                int j = i - 1;
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j--;
                }
                arr[j + 1] = key;
            }
        }

        static int[] generateRandomArray(int size) {
            Random random = new Random(42);
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = random.nextInt(size * 10);
            }
            return arr;
        }
    }

    // ============================================
    // Simple Benchmark Runner
    // ============================================

    static class SimpleBenchmark {
        static long measureTime(Runnable task, int iterations) {
            // Warmup
            for (int i = 0; i < iterations / 10; i++) {
                task.run();
            }

            // Measure
            long start = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                task.run();
            }
            long end = System.nanoTime();

            return (end - start) / iterations; // Average nanoseconds
        }

        static void runStringBenchmark() {
            System.out.println("--- String Concatenation Benchmark ---");
            int iterations = 10000;

            long plusTime = measureTime(
                () -> StringPerformance.concatenateWithPlus(100),
                iterations
            );
            long builderTime = measureTime(
                () -> StringPerformance.concatenateWithBuilder(100),
                iterations
            );
            long bufferTime = measureTime(
                () -> StringPerformance.concatenateWithBuffer(100),
                iterations
            );

            System.out.printf("String +:      %,d ns/op%n", plusTime);
            System.out.printf("StringBuilder: %,d ns/op%n", builderTime);
            System.out.printf("StringBuffer:  %,d ns/op%n", bufferTime);
            System.out.printf("Speedup (Builder vs +): %.1fx%n", (double) plusTime / builderTime);
        }

        static void runSortingBenchmark() {
            System.out.println("\n--- Sorting Algorithm Benchmark ---");
            int size = 1000;
            int iterations = 100;

            long bubbleTime = measureTime(() -> {
                int[] arr = SortingPerformance.generateRandomArray(size);
                SortingPerformance.bubbleSort(arr);
            }, iterations);

            long insertionTime = measureTime(() -> {
                int[] arr = SortingPerformance.generateRandomArray(size);
                SortingPerformance.insertionSort(arr);
            }, iterations);

            long javaUtilTime = measureTime(() -> {
                int[] arr = SortingPerformance.generateRandomArray(size);
                Arrays.sort(arr);
            }, iterations);

            System.out.printf("Bubble Sort:    %,d ns/op%n", bubbleTime);
            System.out.printf("Insertion Sort: %,d ns/op%n", insertionTime);
            System.out.printf("Arrays.sort:    %,d ns/op%n", javaUtilTime);
        }
    }

    // ============================================
    // Concurrent Performance Test
    // ============================================

    static class ConcurrentPerformance {
        static AtomicLong counter = new AtomicLong(0);

        static void incrementCounter() {
            counter.incrementAndGet();
        }

        static void runConcurrentBenchmark() throws InterruptedException {
            System.out.println("\n--- Concurrent Performance Benchmark ---");
            int threadCount = 4;
            int incrementsPerThread = 1000000;

            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            long startTime = System.nanoTime();

            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < incrementsPerThread; j++) {
                        incrementCounter();
                    }
                    latch.countDown();
                });
            }

            latch.await();
            long endTime = System.nanoTime();

            executor.shutdown();

            long totalIncrements = (long) threadCount * incrementsPerThread;
            long durationMs = (endTime - startTime) / 1_000_000;
            long throughput = totalIncrements / (durationMs > 0 ? durationMs : 1);

            System.out.println("Threads: " + threadCount);
            System.out.println("Total increments: " + totalIncrements);
            System.out.println("Duration: " + durationMs + " ms");
            System.out.println("Throughput: " + throughput + " ops/ms");
            System.out.println("Final counter: " + counter.get());
        }
    }

    // ============================================
    // Gatling (Conceptual)
    // ============================================

    /*
     * Gatling is a load testing framework.
     * 
     * Dependencies:
     * <dependency>
     *     <groupId>io.gatling.highcharts</groupId>
     *     <artifactId>gatling-charts-highcharts</artifactId>
     *     <version>3.9.5</version>
     *     <scope>test</scope>
     * </dependency>
     * 
     * Simulation class:
     * 
     * public class UserSimulation extends Simulation {
     * 
     *     HttpProtocolBuilder httpProtocol = http
     *         .baseUrl("http://localhost:8080")
     *         .acceptHeader("application/json");
     * 
     *     ScenarioBuilder scn = scenario("Get Users")
     *         .exec(
     *             http("get_users")
     *                 .get("/api/users")
     *                 .check(status().is(200))
     *         );
     * 
     *     {
     *         setUp(
     *             scn.injectOpen(
     *                 atOnceUsers(10),
     *                 rampUsers(100).during(30)
     *             )
     *         ).protocols(httpProtocol);
     *     }
     * }
     * 
     * Run: mvn gatling:test
     */

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Performance Testing Demo ===\n");

        SimpleBenchmark.runStringBenchmark();
        SimpleBenchmark.runSortingBenchmark();
        ConcurrentPerformance.runConcurrentBenchmark();

        System.out.println("\n--- Performance Testing Tools ---");
        System.out.println("JMH:     Micro-benchmarking");
        System.out.println("Gatling: Load testing");
        System.out.println("JMeter:  Load testing (GUI)");
        System.out.println("wrk:     HTTP benchmarking");

        System.out.println("\n--- Best Practices ---");
        System.out.println("1. Always warmup before measuring");
        System.out.println("2. Run multiple iterations for accuracy");
        System.out.println("3. Profile before optimizing");
        System.out.println("4. Test under realistic conditions");
        System.out.println("5. Monitor resource usage (CPU, memory)");

        System.out.println("\n=== Performance Testing Demo Complete ===");
    }
}
