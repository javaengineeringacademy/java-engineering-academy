package academy.javaengineering.testing.performance.solutions;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Thread)
public class Exercise1BasicBenchmarkSolution {

    private String baseString;

    @Setup
    public void setup() {
        baseString = "Hello World";
    }

    @Benchmark
    public String stringConcat() {
        return baseString + " test";
    }

    @Benchmark
    public String stringBuilder() {
        return new StringBuilder(baseString).append(" test").toString();
    }
}
