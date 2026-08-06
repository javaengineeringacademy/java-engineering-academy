# 07. JIT Compilation - Deep Dive

## Introduction

Just-In-Time (JIT) compilation is one of the most critical performance optimization techniques in the JVM. It transforms Java bytecode into native machine code at runtime, enabling Java applications to achieve performance comparable to natively compiled languages.

## JIT Compilation Pipeline

```
┌─────────────────────────────────────────────────────────────┐
│                    JIT Compilation Pipeline                   │
│                                                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │  Bytecode   │  │Interpreter  │  │  Profiling  │        │
│  │  Loading    │──│  Execution  │──│  Collection │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│                                                    │        │
│                                                    ▼        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │  Native     │  │  C2         │  │  C1         │        │
│  │  Execution  │◄─│  Compilation│◄─│ Compilation │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
```

## C1 vs C2 vs Graal

### C1 Compiler (Client Compiler)
- Fast compilation, basic optimizations
- Lower memory usage
- Good for startup performance
- Used in tiered compilation (levels 1-3)

### C2 Compiler (Server Compiler)
- Slower compilation, aggressive optimizations
- Higher memory usage
- Best for peak performance
- Used in tiered compilation (level 4)

### Graal JIT (Java 17+)
- Written in Java (self-hosting compiler)
- Advanced optimization techniques
- Enables AOT compilation with GraalVM Native Image
- Experimental in OpenJDK

| Feature | C1 | C2 | Graal |
|---------|----|----|-------|
| **Compilation Speed** | Fast | Slow | Slow |
| **Optimization Level** | Basic | Aggressive | Advanced |
| **Memory Usage** | Low | High | High |
| **Code Quality** | Good | Better | Best |
| **Startup Impact** | Low | Medium | Medium |

## Tiered Compilation Levels

| Level | Compiler | Profiling | Optimizations |
|-------|----------|-----------|---------------|
| 0 | Interpreter | No | None |
| 1 | C1 | No | Basic |
| 2 | C1 | Limited | Moderate |
| 3 | C1 | Full | Most |
| 4 | C2 | Full | All |

### Tiered Compilation Flow

```
1. Method called → Interpreter (L0)
2. Hot method detected → C1 compile (L3)
3. C1 collects profiling data
4. Method still hot → C2 compile (L4)
5. C2 applies aggressive optimizations
```

## Optimization Techniques

### 1. Inline Expansion (Method Inlining)
- Replaces method call with method body
- Eliminates method call overhead
- Enables further optimizations

### 2. Escape Analysis
- Determines if objects escape the method
- Non-escaping objects can be stack-allocated
- Reduces GC pressure

### 3. Lock Elimination
- Removes unnecessary synchronization
- Biased locking for single-threaded access
- Lock coarsening for adjacent synchronized blocks

### 4. Loop Optimizations
- **Loop unrolling**: Reduce branch overhead
- **Loop inversion**: Convert while to do-while
- **Bounds check elimination**: Remove redundant checks
- **Loop vectorization**: Use SIMD instructions

### 5. Dead Code Elimination
- Remove unreachable code
- Constant propagation: Replace variables with constants
- Branch elimination: Remove always-false branches

### 6. Constant Folding
- Evaluate constant expressions at compile time
- Replace expressions with pre-computed values
- Eliminates runtime computation

## JIT Thresholds and Tuning

### Compilation Thresholds

```bash
-XX:CompileThreshold=10000      # Invocations before compile
-XX:FreqInlineSize=325         # C1: max bytecode size for inlining
-XX:MaxInlineSize=35           # C2: max bytecode size for inlining
-XX:InlineSmallCode=2000       # C2: max native code size for inlining
```

### Code Cache Configuration

```bash
-XX:InitialCodeCacheSize=256k   # Initial size
-XX:ReservedCodeCacheSize=256m  # Max size (default: 240MB)
-XX:CodeCacheExpansionSize=64   # Expansion size in bytes
```

### Tiered Compilation Flags

```bash
-XX:+TieredCompilation              # Enable (default: true)
-XX:TieredStopAtLevel=4            # Max level (default: 4)
-XX:-TieredCompilation             # Disable for C2 only
```

## JIT Diagnostic Flags

```bash
# Print compilation events
-XX:+PrintCompilation

# Print inlining decisions
-XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining

# Print code cache usage
-XX:+UnlockDiagnosticVMOptions -XX:+PrintCodeCache

# Print escape analysis
-XX:+UnlockDiagnosticVMOptions -XX:+PrintEscapeAnalysis

# Print native assembly
-XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly
```

## Best Practices

1. **Warm Up Applications**: Run representative workloads before measuring
2. **Monitor Compilation**: Enable compilation logging in production
3. **Avoid Deoptimization**: Use final classes and methods
4. **Tune for Workload**: Adjust compilation thresholds
5. **Profile Before Optimizing**: Identify actual hot methods

## Common Mistakes

1. **Not Warming Up**: Measuring without JIT compilation
2. **Code Cache Too Small**: Default code cache for large application
3. **Triggering Deoptimization**: Polymorphic call sites

## Interview Questions

1. **What is JIT compilation?** - Compiling bytecode to native code at runtime
2. **What is the difference between C1 and C2?** - C1 is fast/basic, C2 is slow/aggressive
3. **What is tiered compilation?** - Multiple compilation levels from interpreter to C2
4. **What is method inlining?** - Replacing method call with method body
5. **What is escape analysis?** - Analyzing if objects escape for stack allocation

## References

- [JIT Compilation Guide](https://www.baeldung.com/jit-compiler)
- [HotSpot Internals](https://openjdk.org/groups/hotspot/)
- "Java Performance" by Scott Oaks
- "Optimizing Java" by Benjamin J. Evans

## Related Topics
- [GC](../05-garbage-collection/) — JIT and GC interact
- [Memory Model](../../00-knowledge-atoms/java-memory-model/) — JIT optimizations affect memory
- [Performance](../../15-senior/performance-engineering/) — JIT tuning
- [Safepoints](../../15-senior/jvm-deep-dive/safepoints/) — JIT triggers safepoints
