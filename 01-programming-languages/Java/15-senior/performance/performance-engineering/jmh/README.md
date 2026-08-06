# JMH (Java Microbenchmark Harness)

## What JMH Is

JMH is the official microbenchmark harness developed by the OpenJDK team. It is designed specifically for writing reliable, correct microbenchmarks for Java code. Microbenchmarks measure the performance of small, focused code snippets in isolation.

Unlike macrobenchmarks (which measure entire application throughput), microbenchmarks isolate specific operations to understand their performance characteristics at a fine-grained level.

## When to Use (and When NOT to Use)

**Use JMH when:**
- Comparing two implementations of a specific algorithm
- Measuring the overhead of language features (e.g., autoboxing, generics)
- Validating a performance optimization at the method level
- Measuring JIT compiler behavior and optimization effects

**Do NOT use JMH when:**
- Benchmarking complex, multi-threaded systems (use macrobenchmarks)
- Measuring I/O performance (use specialized I/O benchmarks)
- Comparing across different JVM versions for regression testing (use JCStress for concurrency, SPECjbb for throughput)
- Profiling (JMH shows *what* is fast, not *why* it is slow)

## Core Annotations

### @Benchmark

The `@Benchmark` annotation marks a method as a benchmark entry point:

```java
@Benchmark
public void myBenchmark(Blackhole blackhole) {
    // code under test
}
```

### @Warmup and @Measurement

Control the number of iterations for warmup and measurement:

```java
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Benchmark
public void benchmark() { ... }
```

- **@Warmup**: JVM needs time to reach peak optimization. Warmup allows the JIT compiler to optimize hot paths.
- **@Measurement**: Actual iterations used to gather performance data.

### @Fork

Controls how many JVM forks to run (each fork starts a fresh JVM):

```java
@Fork(value = 2, jvmArgsAppend = {"-Xmx256m", "-Xms256m"})
@Benchmark
public void benchmark() { ... }
```

Use forks to isolate benchmark results from JIT state of the host JVM.

## Blackhole

`Blackhole` prevents the JIT compiler from eliminating dead code (dead code elimination):

```java
@Benchmark
public long blackhole(Blackhole bh) {
    long result = expensiveComputation();
    bh.consume(result); // prevents DCE
    return result;
}
```

Without `Blackhole`, if the JIT determines `result` is never used, it may skip the computation entirely, producing misleading benchmarks.

## Benchmark Modes

### avgt (Average Time)
Measures the average time per operation. The most common mode:

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Benchmark
public void avgtBenchmark() { ... }
```

### sample (Sample Time)
Collects individual operation times and produces a histogram with percentiles:

```java
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Benchmark
public void sampleBenchmark() { ... }
```

### sspt (Single Shot Time)
Measures time for a single invocation (useful for batch operations):

```java
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Benchmark
public void ssptBenchmark() { ... }
```

## Real Examples

### String Concatenation

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Benchmark
public String stringConcatenation() {
    String result = "";
    for (int i = 0; i < 100; i++) {
        result += i;  // O(n^2) - creates new String each time
    }
    return result;
}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Benchmark
public String stringBuilder() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100; i++) {
        sb.append(i);  // O(n) - single allocation
    }
    return sb.toString();
}
```

### Autoboxing

```java
@Benchmark
public long autoboxedSum() {
    Long sum = 0L;  // autoboxing on every iteration
    for (long i = 0; i < 1000; i++) {
        sum += i;  // unbox, add, box
    }
    return sum;
}

@Benchmark
public long primitiveSum() {
    long sum = 0L;  // no boxing
    for (long i = 0; i < 1000; i++) {
        sum += i;
    }
    return sum;
}
```

### Stream vs Loop

```java
@Benchmark
public double streamSum() {
    return LongStream.rangeClosed(0, 1000)
        .average()
        .orElse(0.0);
}

@Benchmark
public double loopSum() {
    long sum = 0;
    for (long i = 0; i <= 1000; i++) {
        sum += i;
    }
    return (double) sum / 1000;
}
```

## Running Benchmarks

```bash
# Run all benchmarks
mvn clean install exec:java -Dexec.mainClass="org.openjdk.jmh.Main"

# Run specific benchmark
java -jar benchmarks.jar StringConcatBenchmark

# Run with profiling
java -jar benchmarks.jar -prof stack StringConcatBenchmark

# Run in GC profiling mode
java -jar benchmarks.jar -prof gc StringConcatBenchmark
```

## Common Pitfalls

1. **Not warming up enough**: JIT needs 10,000+ invocations to compile hot methods
2. **Using System.nanoTime()**: JMH has built-in time measurement
3. **Forgetting @Fork**: JIT state from host JVM contaminates results
4. **Benchmarking too much**: Keep benchmarks focused on a single operation
5. **Ignoring JIT optimizations**: Use Blackhole, @CompilerControl, or return values

## See Also
- [JFR (Java Flight Recorder)](../jfr/) — Production profiling companion to JMH
- [Profiling](../profiling/) — When and how to profile beyond microbenchmarks
- [Performance Patterns](../performance-patterns/) — Broader optimization strategies

## Interview Questions

1. **Why can't you use `System.nanoTime()` for benchmarking?** — It measures wall-clock time including GC pauses and OS scheduling. JMH handles warmup, dead code elimination, and statistical analysis.

2. **What is dead code elimination in benchmarking?** — If the JIT determines a computation's result is unused, it may skip it entirely, producing misleading fast results. Use `Blackhole.consume()` or return the value.

3. **Why is `@Fork` necessary?** — Each fork starts a fresh JVM, isolating benchmarks from JIT state of the host JVM and other benchmarks. Without forks, results are contaminated.

4. **What is the difference between `avgt` and `sample` modes?** — `avgt` reports mean time per operation. `sample` collects individual operation times and reports percentiles (p50, p99, etc.). Use `sample` for latency-sensitive code.

5. **How many warmup iterations are enough?** — At least 10,000 invocations for the JIT to compile hot methods. JMH defaults are conservative; increase for complex code paths.

6. **What is `@CompilerControl`?** — Forces or prevents JIT inlining/control-flow optimizations for specific methods. Useful for measuring the effect of a single optimization.

## Performance

JMH itself adds ~10-50ns overhead per benchmark iteration (annotations, mode checks, timing). The benchmark overhead is negligible compared to most real operations. Fork management adds ~1-5 seconds per fork (JVM startup). For reliable results, use at least 3 forks with 10+ warmup and 10+ measurement iterations.

## Examples

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(3)
@State(Scope.Benchmark)
public class StringBenchmark {
    
    @Benchmark
    public String stringConcat() {
        String result = "";
        for (int i = 0; i < 100; i++) {
            result += i;
        }
        return result;
    }
    
    @Benchmark
    public String stringBuilder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append(i);
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(StringBenchmark.class.getSimpleName())
            .build();
        new Runner(opt).run();
    }
}
```

## Internal Working

JMH generates benchmark code at compile time (annotation processor). It wraps the benchmark method with timing infrastructure, applies dead code elimination prevention (Blackhole or return values), and manages warmup/measurement iterations. The JVM's JIT compiler optimizes the benchmark during warmup, and JMH collects timing data using `System.nanoTime()` with high-precision counters.

## Why This Concept Exists

Microbenchmarking Java code is deceptively hard. The JIT compiler optimizes aggressively — it can eliminate dead code, inline methods, and vectorize loops. Without proper warmup and dead code prevention, benchmarks measure the wrong thing. JMH was created by the OpenJDK team to provide a correct, reliable benchmarking harness that accounts for JIT behavior.

## Pitfalls

1. **Not enough warmup**: JIT needs 10,000+ invocations to compile — premature measurement gives wrong results
2. **Dead code elimination**: If benchmark result is unused, JIT skips the computation — use Blackhole
3. **JVM state contamination**: Running benchmarks without `@Fork` inherits JIT state from previous runs
4. **Benchmarking too much**: Keep each benchmark focused on a single operation
5. **Ignoring GC**: GC pauses affect timing — use `-prof gc` to measure allocation rate

## References

- [JMH Official Samples](https://hg.openjdk.java.net/jdk/sandbox/file/tip/test/micro/org/openjdk/jmh/samples/)
- [JMH Visualizer](http://jmh.morethan.io/)
- [OpenJDK JMH Documentation](https://openjdk.org/projects/code-tools/jmh/)
