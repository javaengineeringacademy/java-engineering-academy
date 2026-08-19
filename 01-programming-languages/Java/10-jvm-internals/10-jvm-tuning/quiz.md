# 10. JVM Tuning - Quiz

## Questions

### Q1: Heap Sizing
What is the recommended practice for -Xms and -Xmx?
- A) Set -Xms much smaller than -Xmx
- B) Set -Xms equal to -Xmx to avoid resize pauses
- C) Never set -Xmx (let JVM decide)
- D) Set -Xms to 1GB for all applications

**Answer: B**
Explanation: Setting -Xms = -Xmx avoids heap resize operations during runtime, which can cause pauses. This is especially important for latency-sensitive applications.

### Q2: Container Memory
In a Docker container with 4GB memory limit, what is a safe -Xmx value?
- A) 4GB
- B) 3GB (75% of container limit)
- C) 2GB (50% of container limit)
- D) 1GB

**Answer: B**
Explanation: Leave ~25% of container memory for non-heap usage (Metaspace, Code Cache, native memory, thread stacks). Setting -Xmx too close to container limit causes OOM kills.

### Q3: GC Logging
What is the unified GC logging flag for Java 11+?
- A) -XX:+PrintGCDetails
- B) -Xlog:gc*:file=gc.log
- C) -verbose:gc
- D) -XX:+UseGCLogFileRotation

**Answer: B**
Explanation: Java 9+ uses the unified logging system. -Xlog:gc* enables all GC logging. Older flags are deprecated.

### Q4: MaxGCPauseMillis
What does -XX:MaxGCPauseMillis=200 target?
- A) Maximum heap size
- B) Maximum pause time for each GC cycle
- C) Maximum number of GC threads
- D) Maximum allocation rate

**Answer: B**
Explanation: MaxGCPauseMillis sets a soft target for GC pause times. The JVM adjusts GC parameters to try to meet this target (G1, ZGC, Shenandoah support this).

### Q5: GCTimeRatio
What does -XX:GCTimeRatio=12 mean?
- A) GC should use 12% of CPU time
- B) GC time should be at most 1/(1+12) = 7.7% of total time
- C) Run 12 GC cycles per minute
- D) 12 GC threads

**Answer: B**
Explanation: GCTimeRatio defines the ratio of GC time to application time. With ratio=12, GC should use at most 1/13 (7.7%) of total time. Higher values prioritize throughput.

### Q6: Metaspace
How do you prevent OutOfMemoryError: Metaspace?
- A) Increase -Xmx
- B) Increase -XX:MaxMetaspaceSize
- C) Decrease -XX:MetaspaceSize
- D) Use G1 GC

**Answer: B**
Explanation: Metaspace stores class metadata. Increase MaxMetaspaceSize to prevent OOM. Also check for classloader leaks that cause Metaspace to grow indefinitely.

### Q7: JIT Compilation
What flag enables tiered compilation (default in modern JVMs)?
- A) -XX:+TieredCompilation
- B) -XX:-TieredCompilation
- C) -XX:+UseC1Compiler
- D) -XX:+UseC2Compiler

**Answer: A**
Explanation: Tiered compilation (-XX:+TieredCompilation) is enabled by default. It progresses from interpreter to C1 (with profiling) to C2 (fully optimized).

### Q8: Benchmarking
What is the correct way to benchmark JVM changes?
- A) Run once and compare
- B) Warm up the JVM, then measure over sufficient iterations
- C) Compare interpreted vs compiled performance
- D) Only measure GC pause times

**Answer: B**
Explanation: Always warm up the JVM (run enough iterations for JIT compilation) before measuring. Use statistical analysis and run multiple iterations for reliable results.

### Q9: SoftMaxHeapSize
What does -XX:SoftMaxHeapSize=4g do?
- A) Sets maximum heap to 4GB
- B) Sets a soft limit that ZGC tries to stay under
- C) Sets minimum heap to 4GB
- D) Enables compressed oops for 4GB heaps

**Answer: B**
Explanation: SoftMaxHeapSize is a hint to ZGC (and G1) to try to keep heap usage below this value. The JVM may exceed it under memory pressure.

### Q10: Performance Goals
What is the typical order of JVM tuning priorities?
- A) Memory → Throughput → Latency
- B) Latency → Throughput → Memory
- C) Throughput → Latency → Memory
- D) It depends on application requirements

**Answer: D**
Explanation: Tuning priorities depend entirely on the application. Batch processing prioritizes throughput, web servers prioritize latency, and embedded systems prioritize memory.

## Score Guide
- **9-10 correct**: Tuning expert
- **7-8 correct**: Solid understanding, review specific scenarios
- **5-6 correct**: Good start, study tuning methodology
- **Below 5**: Review basics before proceeding
