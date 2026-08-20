package academy.javaengineering.testing.performance.practices;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

/**
 * Exercise 1: Basic JMH Benchmark
 *
 * Tasks:
 * 1. Create a benchmark for string operations
 * 2. Compare String concatenation vs StringBuilder
 * 3. Configure appropriate iterations
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class Exercise1BasicBenchmark {

    private String baseString;

    @Setup
    public void setup() {
        baseString = "Hello World";
    }

    @Benchmark
    public String stringConcat() {
        // TODO: Benchmark string concatenation
        return baseString + " test";
    }

    @Benchmark
    public String stringBuilder() {
        // TODO: Benchmark StringBuilder
        return new StringBuilder(baseString).append(" test").toString();
    }
}
