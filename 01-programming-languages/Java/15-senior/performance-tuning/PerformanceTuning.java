import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;

/**
 * Performance tuning examples demonstrating JMH benchmarks,
 * memory allocation patterns, and thread pool optimization.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class PerformanceTuning {

    private static final int SIZE = 1000;

    // ============================================================
    // JMH Benchmark: String Concatenation
    // ============================================================

    @Benchmark
    public String stringConcatenation() {
        String result = "";
        for (int i = 0; i < SIZE; i++) {
            result += "item" + i;
        }
        return result;
    }

    @Benchmark
    public String stringBuilderConcatenation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            sb.append("item").append(i);
        }
        return sb.toString();
    }

    @Benchmark
    public String stringJoinConcatenation() {
        String[] items = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
            items[i] = "item" + i;
        }
        return String.join("", items);
    }

    // ============================================================
    // Memory Allocation Demo
    // ============================================================

    /**
     * Demonstrates excessive object creation in tight loops.
     * This creates millions of temporary objects.
     */
    public long badAllocationPattern() {
        Long sum = 0L;
        for (int i = 0; i < 1_000_000; i++) {
            sum += i;  // Autoboxing on every iteration!
        }
        return sum;
    }

    /**
     * Demonstrates efficient allocation using primitives.
     * No object creation in the loop.
     */
    public long goodAllocationPattern() {
        long sum = 0L;  // Primitive, no boxing
        for (int i = 0; i < 1_000_000; i++) {
            sum += i;
        }
        return sum;
    }

    /**
     * Demonstrates object pooling for frequently created objects.
     */
    public static class ObjectPool<T> {
        private final BlockingQueue<T> pool;
        private final java.util.function.Supplier<T> factory;

        public ObjectPool(int size, java.util.function.Supplier<T> factory) {
            this.pool = new ArrayBlockingQueue<>(size);
            this.factory = factory;
            for (int i = 0; i < size; i++) {
                pool.offer(factory.get());
            }
        }

        public T borrow() {
            T obj = pool.poll();
            return obj != null ? obj : factory.get();
        }

        public void returnObject(T obj) {
            pool.offer(obj);
        }
    }

    @Benchmark
    public byte[] allocateByteArray() {
        return new byte[1024];
    }

    @Benchmark
    public StringBuilder allocateStringBuilder() {
        return new StringBuilder(1024);
    }

    // ============================================================
    // Thread Pool Tuning Examples
    // ============================================================

    /**
     * Thread pool for CPU-bound tasks.
     * Size = number of available processors.
     */
    public static ThreadPoolExecutor createCpuBoundPool() {
        int processors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
            processors,
            processors,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * Thread pool for I/O-bound tasks.
     * Size = 2 * number of available processors (rule of thumb).
     */
    public static ThreadPoolExecutor createIoBoundPool() {
        int processors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
            processors * 2,
            processors * 4,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10000),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    /**
     * Demonstrates monitoring thread pool metrics.
     */
    public static void monitorThreadPool(ThreadPoolExecutor executor, long intervalMs) {
        ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
        monitor.scheduleAtFixedRate(() -> {
            System.out.printf("Pool Size: %d, Active: %d, Completed: %d, Queue: %d%n",
                executor.getPoolSize(),
                executor.getActiveCount(),
                executor.getCompletedTaskCount(),
                executor.getQueue().size());
        }, 0, intervalMs, TimeUnit.MILLISECONDS);
    }

    // ============================================================
    // Benchmark Runner
    // ============================================================

    public static void main(String[] args) throws Exception {
        System.out.println("=== Memory Allocation Demo ===");
        
        PerformanceTuning demo = new PerformanceTuning();
        
        long start = System.nanoTime();
        long result1 = demo.badAllocationPattern();
        long badTime = System.nanoTime() - start;
        
        start = System.nanoTime();
        long result2 = demo.goodAllocationPattern();
        long goodTime = System.nanoTime() - start;
        
        System.out.printf("Bad pattern: %d ns, result: %d%n", badTime, result1);
        System.out.printf("Good pattern: %d ns, result: %d%n", goodTime, result2);
        System.out.printf("Speedup: %.2fx%n", (double) badTime / goodTime);
        
        System.out.println("\n=== Thread Pool Demo ===");
        
        ThreadPoolExecutor cpuPool = createCpuBoundPool();
        ThreadPoolExecutor ioPool = createIoBoundPool();
        
        monitorThreadPool(cpuPool, 1000);
        
        // Submit some tasks
        for (int i = 0; i < 20; i++) {
            cpuPool.submit(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        Thread.sleep(3000);
        
        cpuPool.shutdown();
        ioPool.shutdown();
    }
}
