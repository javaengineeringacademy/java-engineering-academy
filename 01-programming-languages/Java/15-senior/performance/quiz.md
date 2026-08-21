# Performance Quiz

## Question 1 (Code Output)
What is the output of this JMH benchmark?

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class StringBenchmark {
    @Benchmark
    public String stringConcat() {
        String s = "";
        for (int i = 0; i < 100; i++) s += "a";
        return s;
    }
    
    @Benchmark
    public String stringBuilder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) sb.append("a");
        return sb.toString();
    }
}
```

A) stringConcat is faster due to JVM optimization
B) stringBuilder is significantly faster (10-100x)
C) They produce the same result
D) Compilation error — @BenchmarkMode misplaced

**Answer: B**
**Explanation:** String concatenation with `+=` creates a new StringBuilder (and potentially new String) per iteration, causing O(n²) allocation. `StringBuilder.append()` reuses a single buffer with O(1) amortized cost. JMH benchmarks consistently show StringBuilder is 10-100x faster for loops.

---

## Question 2 (Architecture)
Your JFR recording shows 95% of CPU time in `G1 Evacuation Pause`. What does this indicate?

A) Application is CPU-bound
B) Heap is too small — too many young generation collections
C) GC is broken and needs replacement
D) JVM version is outdated

**Answer: B**
**Explanation:** G1 Evacuation Pause is the young GC phase. If it consumes 95% of CPU, the JVM is spending almost all time collecting garbage. This means the young generation is too small for the allocation rate, or the old generation is full, triggering frequent mixed GC. Solutions: increase `-Xmx`, tune `-XX:MaxGCPauseMillis`, or reduce allocation rate.

---

## Question 3 (Code Output)
What is the output of this allocation tracking code?

```java
public class Main {
    static int[] heavyAllocation() {
        int[] result = new int[1000];
        for (int i = 0; i < 1000; i++) result[i] = i * i;
        return result;
    }
    
    public static void main(String[] args) {
        long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        int[] data = heavyAllocation();
        long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Allocated: " + (after - before) + " bytes");
    }
}
```

A) Allocated: 0 bytes (array on stack)
B) Allocated: ~4000 bytes (1000 ints × 4 bytes)
C) Allocated: ~16000 bytes (array object header + data)
D) Allocated: negative number

**Answer: C**
**Explanation:** An `int[1000]` array requires: ~16 bytes object header + 4 bytes length + 4000 bytes data = ~4020 bytes minimum. With JVM alignment (8-byte or 16-byte boundaries), this typically shows as ~4032-16000 bytes depending on alignment and GC overhead. The answer closest to reality is C (with object overhead and alignment).

---

## Question 4 (Performance Analysis)
Your application's P99 latency is 200ms but average is 5ms. JFR shows no GC pauses. Thread dumps show all threads are RUNNABLE. What is the most likely cause?

A) GC overhead
B) Lock contention between threads
C) JIT compilation spikes (on-stack replacement)
D) Network latency to database

**Answer: C**
**Explanation:** The large gap between average (5ms) and P99 (200ms) with no GC and all threads runnable suggests JIT compilation spikes. When the JIT compiler activates for a hot method, it temporarily steals CPU from application threads. This is OSR (On-Stack Replacement) compilation. Solutions: pre-warm the application, increase JIT compile threshold, or use AOT compilation.

---

## Question 5 (JMH)
What does `@CompilerControl(CompilerControl.Mode.DONT_INLINE)` do in JMH?

A) Prevents JIT from inlining the benchmark method
B) Prevents JIT from compiling the method at all
C) Forces the method to be interpreted
D) Disables JIT compilation for the entire JVM

**Answer: A**
**Explanation:** `@CompilerControl(DONT_INLINE)` tells the JIT compiler not to inline this specific method. This is useful for benchmarking to prevent the JIT from optimizing away the measurement by inlining the method into its caller. It does not prevent compilation — only prevents inlining.

---

## Question 6 (Profiling)
You attach async-profiler to a production JVM and find 40% of CPU in `java.util.HashMap.get()`. What is this likely?

A) HashMap is broken in Java
B) Poor hash distribution causing long chains — likely a key type with bad hashCode()
C) Too many HashMap instances
D) GC is compacting the HashMap

**Answer: B**
**Explanation:** High CPU in `HashMap.get()` with poor performance typically indicates hash collision hotspots. If a key type has a `hashCode()` that returns the same value for many keys, HashMap degrades to O(n) linked list traversal. Solutions: improve `hashCode()` distribution, use ConcurrentHashMap for concurrent access, or switch to a TreeMap for ordered keys.

---

## Question 7 (Code Output)
What is the memory impact of this code?

```java
public class Main {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            list.add(new byte[100]);
        }
        System.out.println("Size: " + list.size());
    }
}
```

A) ~100MB (1M × 100 bytes)
B) ~116MB (100MB data + 16MB ArrayList overhead)
C) ~200MB (100MB data + 100MB object headers)
D) ~116MB minimum (100MB data + ArrayList + byte[] headers)

**Answer: C**
**Explanation:** Each `byte[100]` has ~16 bytes object header + 100 bytes data = 116 bytes (aligned). 1M objects × ~116 bytes ≈ 116MB. Plus ArrayList's internal `Object[]` array (8MB for 1M references) plus ArrayList overhead. Total ≈ ~124MB, closest to C's estimate including headers for all objects.

---

## Question 8 (Performance)
Which is faster for numeric iteration in Java?

A) `for (int i = 0; i < n; i++)` — traditional for loop
B) `IntStream.range(0, n).forEach(...)` — parallel stream
C) `IntStream.range(0, n).parallel().forEach(...)` — parallel stream
D) They are identical in performance

**Answer: A**
**Explanation:** For simple numeric iteration, a traditional for loop is fastest. Stream API adds overhead from iterator/lambda machinery. Parallel streams add ForkJoinPool coordination overhead. For small N, parallel is slower due to thread coordination. For large N with CPU-bound work, parallel may help, but the question asks about simple iteration.

---

## Question 9 (Architecture)
Your application allocates 10GB/day of short-lived objects. Which GC tuning parameters would most reduce pause times?

A) `-XX:NewRatio=2` — larger old generation
B) `-XX:MaxGCPauseMillis=50` — target shorter pauses
C) `-XX:+UseZGC` — concurrent garbage collection
D) `-XX:ParallelGCThreads=16` — more GC threads

**Answer: C**
**Explanation:** With 10GB/day of short-lived objects, the young generation fills rapidly, causing frequent GC pauses. ZGC performs most collection concurrently (not stop-the-world), reducing pause times to sub-millisecond regardless of heap size. `-XX:MaxGCPauseMillis` targets G1GC pauses. `-XX:NewRatio` helps G1GC but doesn't eliminate pauses.

---

## Question 10 (Performance)
What is the purpose of JMH's `@State` annotation?

A) Marks the benchmark class as thread-safe
B) Defines the scope of state (PerBenchmark, PerThread, PerIteration)
C) Enables garbage collection between iterations
D) Controls the number of threads used

**Answer: B**
**Explanation:** `@State` defines the lifecycle scope of benchmark state objects. `@State(Scope.Benchmark)` creates one instance per benchmark. `@State(Scope.Thread)` creates one per thread (avoiding false sharing). `@State(Scope.Group)` allows thread group coordination. Without `@State`, JMH cannot properly manage benchmark state.
