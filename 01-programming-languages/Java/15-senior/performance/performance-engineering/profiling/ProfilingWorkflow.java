package performance.profiling;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ProfilingWorkflow {

    private static final int DATA_SIZE = 100_000;
    private static final int ITERATIONS = 10;

    public static void cpuBoundExample() {
        System.out.println("=== CPU-Bound Profiling Example ===");
        System.out.println("Simulates CPU-intensive computation (matrix multiplication).");
        System.out.println("Use async-profiler with -e cpu to find hot methods.");
        System.out.println();

        double[][] matrixA = generateMatrix(500, 500);
        double[][] matrixB = generateMatrix(500, 500);

        long startTime = System.nanoTime();
        double[][] result = multiplyMatrices(matrixA, matrixB);
        long endTime = System.nanoTime();

        System.out.printf("Matrix multiplication: %d ms%n", (endTime - startTime) / 1_000_000);
        System.out.printf("Result[0][0] = %.2f%n%n", result[0][0]);
    }

    public static void memoryBoundExample() {
        System.out.println("=== Memory-Bound Profiling Example ===");
        System.out.println("Simulates excessive object allocation (memory pressure).");
        System.out.println("Use async-profiler with -e alloc to find allocation hotspots.");
        System.out.println();

        List<byte[]> memoryLeak = new ArrayList<>();
        long startTime = System.nanoTime();

        for (int i = 0; i < 10; i++) {
            List<String> tempData = new ArrayList<>();
            for (int j = 0; j < DATA_SIZE; j++) {
                String data = "Item-" + j + "-Iteration-" + i + "-Padding-" + "X".repeat(100);
                tempData.add(data);
            }

            byte[] serialized = tempData.toString().getBytes();
            memoryLeak.add(serialized);

            System.out.printf("Iteration %d: Allocated %d objects%n", i, tempData.size());
        }

        long endTime = System.nanoTime();
        System.out.printf("Total time: %d ms%n", (endTime - startTime) / 1_000_000);
        System.out.printf("Memory leak size: %d MB%n%n", memoryLeak.size() * 10 / 1024);
    }

    public static void threadContentionExample() throws InterruptedException {
        System.out.println("=== Thread Contention Profiling Example ===");
        System.out.println("Simulates thread contention on shared resources.");
        System.out.println("Use jstack or async-profiler with -e lock to find contended locks.");
        System.out.println();

        ReentrantLock sharedLock = new ReentrantLock();
        AtomicInteger counter = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(8);

        long startTime = System.nanoTime();

        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < 8; t++) {
            final int threadId = t;
            futures.add(executor.submit(() -> {
                for (int i = 0; i < 10_000; i++) {
                    sharedLock.lock();
                    try {
                        counter.incrementAndGet();
                        Thread.sleep(0, 100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        sharedLock.unlock();
                    }
                }
                System.out.printf("Thread %d completed%n", threadId);
            }));
        }

        for (Future<?> future : futures) {
            future.get();
        }

        long endTime = System.nanoTime();
        System.out.printf("Counter value: %d%n", counter.get());
        System.out.printf("Total time: %d ms%n", (endTime - startTime) / 1_000_000);
        System.out.printf("Lock contention overhead: %.1f%% of total time%n%n",
            calculateContentionOverhead(sharedLock));
    }

    public static void optimizedContentionExample() throws InterruptedException {
        System.out.println("=== Optimized (Reduced Contention) Example ===");

        AtomicInteger atomicCounter = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(8);

        long startTime = System.nanoTime();

        List<Future<?>> futures = new ArrayList<>();
        for (int t = 0; t < 8; t++) {
            final int threadId = t;
            futures.add(executor.submit(() -> {
                for (int i = 0; i < 10_000; i++) {
                    atomicCounter.incrementAndGet();
                }
                System.out.printf("Thread %d completed%n", threadId);
            }));
        }

        for (Future<?> future : futures) {
            future.get();
        }

        long endTime = System.nanoTime();
        System.out.printf("Counter value: %d%n", atomicCounter.get());
        System.out.printf("Total time: %d ms%n%n", (endTime - startTime) / 1_000_000);
    }

    private static double[][] generateMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        Random random = new Random(42);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextDouble();
            }
        }
        return matrix;
    }

    private static double[][] multiplyMatrices(double[][] a, double[][] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                double sum = 0;
                for (int k = 0; k < colsA; k++) {
                    sum += a[i][k] * b[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    private static double calculateContentionOverhead(ReentrantLock lock) {
        long totalWaitTime = lock.getQueueLength();
        return (totalWaitTime / 8.0) * 100;
    }

    public static void main(String[] args) throws InterruptedException {
        cpuBoundExample();
        memoryBoundExample();
        threadContentionExample();
        optimizedContentionExample();

        System.out.println("=== Profiling Workflow Summary ===");
        System.out.println("1. IDENTIFY: Use metrics/logs to find the bottleneck type");
        System.out.println("2. PROFILE: Run appropriate profiler (cpu/alloc/lock)");
        System.out.println("3. ANALYZE: Examine flame graphs, allocation sites, thread dumps");
        System.out.println("4. OPTIMIZE: Apply targeted fix");
        System.out.println("5. VERIFY: Confirm improvement with benchmarks");
    }
}
