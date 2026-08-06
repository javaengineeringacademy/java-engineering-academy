# HotSpot JVM Internals

## What is HotSpot?

HotSpot is the **default JVM implementation** used by OpenJDK and most Java distributions. It's the runtime that executes Java bytecode and provides the Java platform's performance characteristics.

**Key characteristics:**
- Just-In-Time (JIT) compilation
- Adaptive optimization
- Advanced garbage collection
- Multi-threaded execution
- Platform-specific optimizations

## JIT Compilation

### Compilation Tiers

HotSpot uses a **multi-tiered compilation** approach:

```
Bytecode → C1 (Client) → C2 (Server) → Optimized Code
```

### Tier 0-3: Interpreter
- **Tier 0**: Pure interpreter execution
- **Tier 1-3**: Mixed mode with varying C1 compilation levels

### C1 (Client Compiler)
- Fast compilation
- Basic optimizations
- Quick startup
- Lower peak performance
- Used for initial compilation

### C2 (Server Compiler)
- Slower compilation
- Advanced optimizations
- Higher peak performance
- Used for hot methods
- Aggressive inlining and loop optimizations

### Graal Compiler (Experimental)
- Modern JIT compiler written in Java
- Better optimization capabilities
- Still experimental in HotSpot
- Available via GraalVM

### Compilation Thresholds

```bash
# Default thresholds
-XX:CompileThreshold=10000  # Method invocation count
-XX:OnStackReplacePercentage=140  # OSR threshold
-XX:InterpreterInvocationPercentage=33
```

### Compilation Flags

```bash
# Force C1 compilation
-XX:+TieredStopAtLevel=1

# Force C2 compilation
-XX:+TieredStopAtLevel=4

# Disable tiered compilation
-XX:-TieredCompilation

# Print compilation log
-XX:+PrintCompilation
```

## Garbage Collectors

### Serial GC
- **Use case**: Single-threaded applications, small heaps
- **Algorithm**: Mark-Sweep-Compact
- **Flags**: `-XX:+UseSerialGC`
- **Best for**: Embedded systems, simple applications

### Parallel GC (Throughput)
- **Use case**: Throughput-focused applications
- **Algorithm**: Parallel Mark-Sweep-Compact
- **Flags**: `-XX:+UseParallelGC`
- **Best for**: Batch processing, scientific computing

### CMS (Concurrent Mark Sweep) — Deprecated
- **Use case**: Low-latency applications (pre-Java 9)
- **Algorithm**: Concurrent mark-sweep
- **Flags**: `-XX:+UseConcMarkSweepGC`
- **Status**: Deprecated in Java 9, removed in Java 14

### G1 (Garbage-First)
- **Use case**: General-purpose, balanced throughput/latency
- **Algorithm**: Region-based, incremental collection
- **Flags**: `-XX:+UseG1GC`
- **Best for**: Most applications (default since Java 9)

### ZGC
- **Use case**: Ultra-low latency (sub-millisecond pauses)
- **Algorithm**: Concurrent, region-based, colored pointers
- **Flags**: `-XX:+UseZGC`
- **Best for**: Latency-critical applications, large heaps

### Shenandoah
- **Use case**: Ultra-low latency (sub-millisecond pauses)
- **Algorithm**: Concurrent, region-based, Brooks pointers
- **Flags**: `-XX:+UseShenandoahGC`
- **Best for**: Latency-critical applications, large heaps

### Comparison

| GC | Pause Time | Throughput | Heap Size | Best For |
|----|------------|------------|-----------|----------|
| Serial | High | Low | Small | Simple apps |
| Parallel | Medium | High | Medium-Large | Batch processing |
| G1 | Low-Medium | High | Medium-Large | General purpose |
| ZGC | Ultra-low | Medium-High | Large | Latency-critical |
| Shenandoah | Ultra-low | Medium-High | Large | Latency-critical |

## Performance Optimizations

### Method Inlining
- Small methods are inlined at compile time
- Reduces method call overhead
- Enables further optimizations
- Controlled by `-XX:MaxInlineSize` and `-XX:FreqInlineSize`

### Escape Analysis
- Determines if objects escape the method
- Enables scalar replacement (stack allocation)
- Reduces GC pressure
- Controlled by `-XX:+DoEscapeAnalysis`

### Loop Optimizations
- **Loop unrolling**: Reduces loop overhead
- **Loop vectorization**: Uses SIMD instructions
- **Loop fusion/fission**: Optimizes loop structure

### Branch Prediction
- HotSpot uses profiling data to predict branches
- Optimizes code layout for predicted paths
- Reduces branch misprediction penalties

### Constant Folding
- Compile-time evaluation of constant expressions
- Eliminates redundant calculations
- Improves runtime performance

## JVM Flags Reference

### Memory Flags

```bash
# Heap size
-Xms512m          # Initial heap size
-Xmx4g            # Maximum heap size
-Xmn1g            # Young generation size

# Metaspace
-XX:MetaspaceSize=256m
-XX:MaxMetaspaceSize=512m

# Thread stack
-Xss1m            # Thread stack size
```

### GC Flags

```bash
# G1 specific
-XX:G1HeapRegionSize=16m
-XX:MaxGCPauseMillis=200
-XX:G1NewSizePercent=30
-XX:G1MaxNewSizePercent=60

# ZGC specific
-XX:SoftMaxHeapSize=4g
-XX:ConcGCThreads=2
-XX:ZCollectionInterval=5
```

### Performance Flags

```bash
# JIT compilation
-XX:CompileThreshold=10000
-XX:+TieredCompilation
-XX:+PrintCompilation

# GC logging
-Xlog:gc*:file=gc.log:time,uptime,level,tags

# Diagnostics
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/tmp/heapdump.hprof
```

### Diagnostic Flags

```bash
# Thread dumps
-XX:+UnlockDiagnosticVMOptions
-XX:+PrintConcurrentLocks
-XX:+PrintDeadlock

# Performance monitoring
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-XX:+PrintAdaptiveSizePolicy
```

## JVM Architecture

```
┌─────────────────────────────────────┐
│           Java Code                 │
└─────────────┬───────────────────────┘
              │
              ▼
┌─────────────────────────────────────┐
│         Class Loader                │
│  (Bootstrap, Extension, Application)│
└─────────────┬───────────────────────┘
              │
              ▼
┌─────────────────────────────────────┐
│      Runtime Data Areas             │
│  (Method Area, Heap, Stack, PC)     │
└─────────────┬───────────────────────┘
              │
              ▼
┌─────────────────────────────────────┐
│        Execution Engine             │
│  (Interpreter, JIT Compiler, GC)    │
└─────────────────────────────────────┘
```

## Monitoring and Profiling

### Built-in Tools

```bash
# JVisualVM (included with JDK)
jvisualvm

# JConsole
jconsole

# Java Flight Recorder
jcmd <pid> JFR.start
jcmd <pid> JFR.dump filename=recording.jfr
jcmd <pid> JFR.stop
```

### Command-Line Monitoring

```bash
# Thread dumps
jstack <pid>

# Heap analysis
jmap -heap <pid>
jmap -dump:format=b,file=heap.hprof <pid>

# Process information
jps -v
jinfo <pid>
```

## Best Practices

1. **Start with defaults**: HotSpot's defaults are well-tuned
2. **Profile first**: Don't optimize without data
3. **Use appropriate GC**: Match GC to your workload
4. **Monitor in production**: Use JFR and GC logs
5. **Test thoroughly**: Verify performance improvements
