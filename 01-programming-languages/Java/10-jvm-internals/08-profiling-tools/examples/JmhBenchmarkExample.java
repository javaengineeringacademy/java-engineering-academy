package jvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * JmhBenchmarkExample - JMH (Java Microbenchmark Harness) benchmark example
 *
 * Demonstrates:
 * - JMH annotations and configuration
 * - Benchmark modes (Throughput, AverageTime, SampleTime)
 * - Warmup and measurement iterations
 * - State management (State, Setup, TearDown)
 * - Parameterized benchmarks
 * - Blackhole consumption to prevent dead code elimination
 * - Forking for JIT optimization stability
 *
 * To run real JMH benchmarks, add JMH dependency:
 *   org.openjdk.jmh:jmh-core:1.37
 *   org.openjdk.jmh:jmh-generator-annprocess:1.37
 *
 * Then generate: mvn clean package && java -jar target/benchmarks.jar
 */
public class JmhBenchmarkExample {

    public static void main(String[] args) {
        System.out.println("=== JMH Benchmark Concepts ===\n");

        System.out.println("JMH Annotations:");
        System.out.println("  @Benchmark          - Marks a benchmark method");
        System.out.println("  @BenchmarkMode      - Sets benchmark mode");
        System.out.println("  @OutputTimeUnit     - Controls output time unit");
        System.out.println("  @Warmup             - Configures warmup iterations");
        System.out.println("  @Measurement        - Configures measurement iterations");
        System.out.println("  @Fork               - JVM forks for isolation");
        System.out.println("  @State              - Holds benchmark state");
        System.out.println("  @Setup              - Pre-benchmark initialization");
        System.out.println("  @TearDown           - Post-benchmark cleanup");
        System.out.println("  @Param              - Parameterized benchmarks");
        System.out.println("  @Group              - Group related benchmarks");
        System.out.println();

        System.out.println("Benchmark Modes:");
        System.out.println("  Throughput    - Operations per time unit");
        System.out.println("  AverageTime   - Time per operation");
        System.out.println("  SampleTime    - Distribution of times");
        System.out.println("  SingleShotTime - Time for single invocation");
        System.out.println();

        demonstrateBenchmarkConcepts();
    }

    static void demonstrateBenchmarkConcepts() {
        System.out.println("=== Manual Benchmark Simulation ===\n");

        // Simulating what JMH does internally

        // 1. Throughput benchmark - how many ops per second
        int operations = 0;
        long durationNanos = TimeUnit.SECONDS.toNanos(1);
        long start = System.nanoTime();
        while (System.nanoTime() - start < durationNanos) {
            new HashMap<>();
            operations++;
        }
        System.out.printf("HashMap creation throughput: ~%d ops/sec%n", operations);

        // 2. AverageTime benchmark - how long per operation
        int iterations = 10_000;
        long totalNanos = 0;
        for (int i = 0; i < iterations; i++) {
            long opStart = System.nanoTime();
            List<Integer> list = IntStream.range(0, 100).boxed().collect(Collectors.toList());
            totalNanos += System.nanoTime() - opStart;
        }
        System.out.printf("List creation average time: %d ns/op (over %d iterations)%n",
                totalNanos / iterations, iterations);

        // 3. Parameterized benchmark - varying input sizes
        System.out.println("\nParameterized benchmark (varying input size):");
        for (int size : new int[]{10, 100, 1000, 10_000}) {
            long nanoStart = System.nanoTime();
            for (int iter = 0; iter < 1000; iter++) {
                List<Integer> data = IntStream.range(0, size).boxed().collect(Collectors.toList());
                data.stream().filter(x -> x % 2 == 0).count();
            }
            long elapsed = System.nanoTime() - nanoStart;
            System.out.printf("  size=%6d: avg=%d ns%n", size, elapsed / 1000);
        }

        // 4. Dead code elimination prevention
        System.out.println("\nBlackhole consumption prevents JIT from eliminating dead code:");
        System.out.println("  // JIT can remove unused results");
        System.out.println("  // Wrong: list.stream().filter(...).count();");
        System.out.println("  // Correct: blackhole.consume(list.stream().filter(...).count());");
        System.out.println("  // Or use @CompilerControl to prevent inlining");

        // 5. State management concepts
        System.out.println("\nState management:");
        System.out.println("  @State(Scope.Thread) - Per-thread state (default)");
        System.out.println("  @State(Scope.Benchmark) - Shared across threads in benchmark");
        System.out.println("  @State(Scope.Group) - Shared across group methods");
    }

    /*
     * JMH Benchmark Template (for use with JMH framework):
     *
     * @BenchmarkMode(Mode.AverageTime)
     * @OutputTimeUnit(TimeUnit.NANOSECONDS)
     * @State(Scope.Thread)
     * @Warmup(iterations = 5, time = 1)
     * @Measurement(iterations = 10, time = 1)
     * @Fork(value = 2, jvmArgsAppend = {"-Xms2g", "-Xmx2g"})
     * public class JmhBenchmarkTemplate {
     *
     *     private List<Integer> data;
     *
     *     @Setup(Level.Trial)
     *     public void setup() {
     *         data = IntStream.range(0, 1000).boxed().collect(Collectors.toList());
     *     }
     *
     *     @Benchmark
     *     public long streamFilter(Blackhole bh) {
     *         long count = data.stream().filter(x -> x % 2 == 0).count();
     *         bh.consume(count);
     *         return count;
     *     }
     *
     *     @Benchmark
     *     @Param({"10", "100", "1000"})
     *     public long parameterizedFilter(Blackhole bh) {
     *         long count = data.stream().filter(x -> x % 2 == 0).count();
     *         bh.consume(count);
     *         return count;
     *     }
     *
     *     @Benchmark
     *     @Group("collection")
     *     public List<Integer> createCollection() {
     *         return new ArrayList<>(data);
     *     }
     *
     *     @Benchmark
     *     @Group("collection")
     *     @GroupThreads(2)
     *     public void consumeCollection(List<Integer> list) {
     *         list.size();
     *     }
     * }
     */
}
