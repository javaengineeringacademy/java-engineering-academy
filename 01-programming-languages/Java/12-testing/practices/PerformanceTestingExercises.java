package academy.javaengineering.testing.practices;

import java.util.concurrent.*;

/**
 * Performance Testing Exercises
 * Practice JMH and load testing concepts
 */
class PerformanceTestingExercises {

    // ============================================
    // Exercise 1: String Concatenation
    // ============================================

    static class StringConcat {
        static String withPlus(int iterations) {
            String result = "";
            for (int i = 0; i < iterations; i++) {
                result += "a";
            }
            return result;
        }

        static String withBuilder(int iterations) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < iterations; i++) {
                sb.append("a");
            }
            return sb.toString();
        }

        static String withBuffer(int iterations) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < iterations; i++) {
                sb.append("a");
            }
            return sb.toString();
        }
    }

    /*
     * TODO: Write JMH benchmark for string concatenation
     * 
     * @BenchmarkMode(Mode.AverageTime)
     * @OutputTimeUnit(TimeUnit.MILLISECONDS)
     * @State(Scope.Benchmark)
     * public class StringBenchmark {
     *     @Param({"100", "1000", "10000"})
     *     private int iterations;
     * 
     *     @Benchmark
     *     public String testStringPlus() {
     *         return StringConcat.withPlus(iterations);
     *     }
     * 
     *     @Benchmark
     *     public String testStringBuilder() {
     *         return StringConcat.withBuilder(iterations);
     *     }
     * }
     */

    // ============================================
    // Exercise 2: Sorting Algorithms
    // ============================================

    static class SortingAlgorithms {
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
            java.util.Random random = new java.util.Random(42);
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = random.nextInt(size * 10);
            }
            return arr;
        }
    }

    /*
     * TODO: Benchmark different sorting algorithms
     * Compare performance for different array sizes
     */

    // ============================================
    // Exercise 3: Collection Performance
    // ============================================

    static class CollectionPerformance {
        static void benchmarkArrayList() {
            java.util.List<Integer> list = new java.util.ArrayList<>();
            long start = System.nanoTime();
            for (int i = 0; i < 100000; i++) {
                list.add(i);
            }
            for (int i = 0; i < 100000; i++) {
                list.get(i);
            }
            long end = System.nanoTime();
            System.out.println("ArrayList: " + (end - start) / 1_000_000 + " ms");
        }

        static void benchmarkLinkedList() {
            java.util.List<Integer> list = new java.util.LinkedList<>();
            long start = System.nanoTime();
            for (int i = 0; i < 100000; i++) {
                list.add(i);
            }
            for (int i = 0; i < 100000; i++) {
                list.get(i);
            }
            long end = System.nanoTime();
            System.out.println("LinkedList: " + (end - start) / 1_000_000 + " ms");
        }
    }

    /*
     * TODO: Benchmark ArrayList vs LinkedList
     * Compare add, get, and remove operations
     */

    // ============================================
    // Exercise 4: Concurrent Performance
    // ============================================

    static class ConcurrentCounter {
        private int count = 0;
        private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.ReentrantLock();

        void increment() {
            count++;
        }

        synchronized void synchronizedIncrement() {
            count++;
        }

        void lockedIncrement() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        int getCount() { return count; }
    }

    /*
     * TODO: Benchmark concurrent increment strategies
     * Compare: unsynchronized, synchronized, locked
     */

    // ============================================
    // Exercise 5: Load Testing Simulation
    // ============================================

    static class LoadTestSimulator {
        static int simulateLoad(int concurrentUsers, int requestsPerUser) throws InterruptedException {
            ExecutorService executor = Executors.newFixedThreadPool(concurrentUsers);
            CountDownLatch latch = new CountDownLatch(concurrentUsers);
            java.util.concurrent.atomic.AtomicInteger totalRequests = new java.util.concurrent.atomic.AtomicInteger(0);

            long startTime = System.nanoTime();

            for (int i = 0; i < concurrentUsers; i++) {
                executor.submit(() -> {
                    for (int j = 0; j < requestsPerUser; j++) {
                        // Simulate processing
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        totalRequests.incrementAndGet();
                    }
                    latch.countDown();
                });
            }

            latch.await();
            long endTime = System.nanoTime();

            executor.shutdown();

            return totalRequests.get();
        }
    }

    /*
     * TODO: Simulate load testing
     * Test with different concurrent user counts
     * Measure throughput and response time
     */

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Performance Testing Exercises ===\n");

        System.out.println("--- String Concatenation Benchmark ---");
        int iterations = 10000;
        long start = System.nanoTime();
        StringConcat.withPlus(100);
        long plusTime = System.nanoTime() - start;

        start = System.nanoTime();
        StringConcat.withBuilder(100);
        long builderTime = System.nanoTime() - start;

        System.out.println("String +: " + plusTime + " ns");
        System.out.println("StringBuilder: " + builderTime + " ns");
        System.out.println("Speedup: " + (double) plusTime / builderTime + "x\n");

        System.out.println("--- Collection Performance ---");
        CollectionPerformance.benchmarkArrayList();
        CollectionPerformance.benchmarkLinkedList();

        System.out.println("\n--- Load Test Simulation ---");
        int total = LoadTestSimulator.simulateLoad(10, 100);
        System.out.println("Total requests completed: " + total);
    }
}
