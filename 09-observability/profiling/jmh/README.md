# JMH - Java Microbenchmark Harness

## Overview

JMH is a Java benchmarking framework for creating reliable microbenchmarks with proper measurement and analysis.

## Core Concepts

### Benchmark Modes
- **Throughput** - Operations per unit time
- **AverageTime** - Average time per operation
- **SampleTime** - Distribution of times
- **SingleShotTime** - Time for single batch

### Time Units
- **NANOSECONDS**
- **MICROSECONDS**
- **MILLISECONDS**
- **SECONDS**

## Basic Benchmark

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Thread)
public class StringBenchmark {
    
    private String string;
    
    @Setup
    public void setup() {
        string = "Hello, World!";
    }
    
    @Benchmark
    public String concatStrings() {
        return "Hello, " + "World!";
    }
    
    @Benchmark
    public String stringBuilder() {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello, ");
        sb.append("World!");
        return sb.toString();
    }
}
```

## Annotations

### Configuration
```java
@BenchmarkMode(Mode.Throughput)    // Measurement mode
@OutputTimeUnit(TimeUnit.SECONDS)  // Time unit
@Warmup(iterations = 3, time = 1)  // Warmup iterations
@Measurement(iterations = 5, time = 1) // Measurement iterations
@Fork(1)                          // JVM forks
@State(Scope.Thread)              // State scope
@Setup(Level.Trial)               // Setup level
@TearDown(Level.Trial)            // TearDown level
```

### State Scopes
```java
@State(Scope.Benchmark) // Shared across threads
@State(Scope.Group)     // Shared within group
@State(Scope.Thread)    // Per-thread
```

## Advanced Features

### Blackhole
```java
@Benchmark
public void measure(Blackhole blackhole) {
    Object result = expensiveOperation();
    blackhole.consume(result); // Prevent dead code elimination
}
```

### Parametric Benchmarks
```java
@Benchmark
@Param({"10", "100", "1000"})
public void benchmarkList(Blackhole blackhole) {
    List<Integer> list = new ArrayList<>(param);
    blackhole.consume(list);
}
```

### Grouping
```java
@Benchmark
@Group("stringConcat")
@GroupThreads(4)
public String producer() {
    return "data";
}

@Benchmark
@Group("stringConcat")
@GroupThreads(2)
public void consumer(String data) {
    // Process data
}
```

## Running Benchmarks

```bash
# Run all benchmarks
java -jar benchmarks.jar

# Run specific benchmark
java -jar benchmarks.jar StringBenchmark

# Run with specific mode
java -jar benchmarks.jar -bm thrpt

# Export results
java -jar benchmarks.jar -rf json -rff results.json
```

## Best Practices

1. Warm up JVM before measurement
2. Use multiple forks for stability
3. Prevent dead code elimination
4. Use Blackhole for outputs
5. Choose appropriate measurement mode
6. Run benchmarks in isolation
7. Use statistical analysis of results
8. Compare against baseline implementations
