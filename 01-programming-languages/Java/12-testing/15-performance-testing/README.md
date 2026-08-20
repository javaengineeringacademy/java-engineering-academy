# 11.15 Performance Testing

## 1. Introduction

Performance testing evaluates system behavior under load. JMH (Java Microbenchmark Harness) is the standard tool for Java microbenchmarks, while Gatling provides load testing for applications.

## 2. Learning Objectives

- Understand performance testing concepts
- Write microbenchmarks with JMH
- Configure JMH benchmarks
- Analyze performance results
- Compare performance testing tools

## 3. Prerequisites

- Java knowledge
- Understanding of JVM internals
- Testing concepts

## 4. Why This Concept Exists

Performance testing addresses:
- Identifying bottlenecks
- Validating performance requirements
- Comparing implementation alternatives
- Detecting performance regressions
- Capacity planning

## 5. Problem Statement

How do we measure and validate the performance of Java code?

## 6. Theory

### Performance Metrics

| Metric | Description |
|--------|-------------|
| Throughput | Operations per second |
| Latency | Time per operation |
| Variance | Consistency of results |
| Memory | Allocation rate |

### JMH Annotations

| Annotation | Purpose |
|------------|---------|
| @Benchmark | Mark benchmark method |
| @BenchmarkMode | Mode (Throughput, AverageTime) |
| @OutputTimeUnit | Time unit for results |
| @Warmup | Warmup iterations |
| @Measurement | Measurement iterations |
| @Fork | JVM fork count |
| @State | Shared state across iterations |

### Benchmark Modes

| Mode | Description |
|------|-------------|
| Throughput | Ops/time unit |
| AverageTime | Time/op |
| SampleTime | Distribution of times |
| SingleShotTime | Single invocation time |

## 7. Internal Working

### JMH Execution Flow

1. Parse benchmark configuration
2. Generate benchmark code
3. Fork new JVM (if configured)
4. Warmup iterations
5. Measurement iterations
6. Collect and analyze results
7. Generate report

### JIT Optimization Handling

JMH handles JIT optimizations:
- Dead code elimination
- Constant folding
- Loop unrolling
- Inlining

## 8. JVM Perspective

- Benchmarks run in forked JVM
- Warmup ensures JIT compilation
- Black hole prevents dead code elimination
- State objects shared across iterations

## 9. Memory Representation

```
JMH Memory Model:
┌─────────────────────────────────────┐
│           Forked JVM                │
│  - Benchmark class instances        │
│  - State objects                    │
│  - JMH infrastructure               │
│  - JIT compiled code                │
├─────────────────────────────────────┤
│         JMH Runner                  │
│  - Benchmark configuration          │
│  - Result collector                 │
│  - Report generator                 │
└─────────────────────────────────────┘
```

## 10. Easy Example

```java
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class MyBenchmark {

    @Benchmark
    public int testMethod() {
        return 1 + 1;
    }
}
```

## 11. Medium Example

```java
import org.openjdk.jmh.annotations.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class CollectionBenchmark {

    private List<Integer> arrayList;
    private List<Integer> linkedList;
    private Set<Integer> hashSet;
    private Set<Integer> treeSet;

    @Setup
    public void setup() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        hashSet = new HashSet<>();
        treeSet = new TreeSet<>();
        for (int i = 0; i < 1000; i++) {
            arrayList.add(i);
            linkedList.add(i);
            hashSet.add(i);
            treeSet.add(i);
        }
    }

    @Benchmark
    public int arrayListGet() {
        return arrayList.get(500);
    }

    @Benchmark
    public boolean linkedListContains() {
        return linkedList.contains(500);
    }

    @Benchmark
    public boolean hashSetContains() {
        return hashSet.contains(500);
    }
}
```

## 12. Hard Example

```java
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 10, time = 1)
@Fork(2)
@State(Scope.Thread)
public class AdvancedBenchmark {

    private AtomicInteger atomicCounter;
    private int syncCounter;
    private volatile int volatileCounter;

    @Setup
    public void setup() {
        atomicCounter = new AtomicInteger(0);
        syncCounter = 0;
        volatileCounter = 0;
    }

    @Benchmark
    public int atomicIncrement() {
        return atomicCounter.incrementAndGet();
    }

    @Benchmark
    public synchronized int synchronizedIncrement() {
        return ++syncCounter;
    }

    @Benchmark
    public int volatileIncrement() {
        return ++volatileCounter;
    }

    @TearDown
    public void tearDown() {
        atomicCounter.set(0);
        syncCounter = 0;
        volatileCounter = 0;
    }
}
```

## Interview Questions

1. **What is JMH?**
   JMH is a Java microbenchmark harness for writing reliable performance benchmarks.

2. **Why use JMH over manual timing?**
   JMH handles JIT optimization, warmup, and statistical analysis automatically.

3. **What is a warmup iteration?**
   Warmup iterations allow JIT to compile and optimize code before measurement.

4. **What is @Fork?**
   @Fork specifies how many JVM forks to use for benchmark execution.

5. **How do you prevent dead code elimination?**
   Use Blackhole.consume() or return values to prevent JIT from removing unused code.
