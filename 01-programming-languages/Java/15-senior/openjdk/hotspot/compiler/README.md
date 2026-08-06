# HotSpot JIT Compilers — C1, C2, and Graal

HotSpot uses JIT (Just-In-Time) compilation to translate hot bytecode into optimized native machine code. The compiler pipeline is tiered, progressively optimizing methods as they become hot.

## Compilation Tiers

HotSpot uses a four-tier compilation model:

```
Tier 0: Interpreter
  ↓ (method becomes hot)
Tier 1-3: C1 Compiler (with profiling)
  ↓ (method becomes very hot)
Tier 4: C2 Compiler (full optimization)
```

### Tier Breakdown

| Tier | Engine | What Happens |
|------|--------|--------------|
| 0 | Interpreter | Execute bytecode, collect profiling data |
| 1 | C1 | Compile with full profiling (no optimization beyond basic) |
| 2 | C1 | Compile with limited profiling |
| 3 | C1 | Compile with no profiling (just fast compilation) |
| 4 | C2 | Compile with full optimization |

The tier at which compilation stops is controlled by `-XX:TieredStopAtLevel`.

## C1 — The Client Compiler

C1 (also called the "client" or "fast" compiler) is designed for fast compilation with basic optimizations.

### What C1 Does

1. Parses bytecode into a graph (HIR — High-level Intermediate Representation)
2. Performs basic optimizations
3. Generates native code quickly

### C1 Optimizations

- Method inlining (small methods only)
- Constant folding
- Dead code elimination
- Null check elimination
- Simple escape analysis
- Intrinsic recognition (optimized implementations of `System.arraycopy`, etc.)

### When C1 Compiles

- During startup for warm methods
- As an intermediate step before C2
- For methods that won't benefit from aggressive optimization

### C1 Flags

```bash
# Force C1 only (no C2)
-XX:+TieredCompilation -XX:TieredStopAtLevel=1

# C1 compilation threshold
-XX:CompileThreshold=1000

# Print C1 compilation
-XX:+PrintCompilation
```

## C2 — The Server Compiler

C2 (also called the "server" or "optimizing" compiler) is designed for maximum peak performance. It compiles slower but produces highly optimized native code.

### What C2 Does

1. Parses bytecode into a graph (ideal graph — sea-of-nodes IR)
2. Performs aggressive optimizations
3. Performs register allocation and instruction scheduling
4. Generates highly optimized native code

### C2 Optimizations

- **Aggressive inlining**: Inlines virtual calls based on type profiling
- **Escape analysis**: Allocates objects on the stack if they don't escape
- **Loop optimizations**: Unrolling, vectorization (SIMD), peeling
- **Null check elimination**: Removes redundant null checks
- **Range check elimination**: Removes redundant array bounds checks
- **Lock coalescing**: Merges adjacent locked regions
- **Dead code elimination**: Removes provably unreachable code
- **Global Value Numbering**: Eliminates redundant computations
- **Loop Predication**: Hoists loop-invariant checks

### When C2 Compiles

- After C1 compilation has collected enough profiling
- For methods that are called very frequently
- For loop-intensive code that benefits from optimization

### C2 Flags

```bash
# Force C2 only
-XX:+TieredCompilation -XX:TieredStopAtLevel=4

# C2 specific thresholds
-XX:CompileThreshold=10000
-XX:+PrintCompilation

# Control inlining
-XX:MaxInlineSize=35      # Bytecode size for inlining
-XX:FreqInlineSize=325    # Size for frequent methods

# Control loop optimizations
-XX:LoopUnrollLimit=60
-XX:MaxVectorSize=64
```

## Graal — Experimental JIT Compiler

Graal is a modern JIT compiler written entirely in Java. It is available as an experimental feature in HotSpot and is the default in GraalVM.

### Graal Architecture

```
Bytecode → Graal IR → Optimization → LIR → Native Code
```

### Graal vs. C2

| Aspect | C2 | Graal |
|--------|-----|-------|
| Language | C++ | Java |
| IR | Sea-of-nodes | Sea-of-nodes |
| Compilation speed | Fast | Slower |
| Peak performance | High | Potentially higher |
| Maintenance | Hard (C++) | Easier (Java) |
| Maturity | Decades | Newer |

### Enabling Graal in HotSpot

```bash
# Experimental — requires separate module
-XX:+UnlockExperimentalVMOptions -XX:+UseJVMCICompiler
```

## Compilation Pipeline

```
Bytecode
  ↓
Bytecode Parser → Graph (IR)
  ↓
Optimization Passes (iterative)
  ↓
Chaitin-Briggs Register Allocation
  ↓
Instruction Selection
  ↓
Instruction Scheduling
  ↓
Native Code → Code Cache
```

### The Code Cache

Compiled native code is stored in the Code Cache, a fixed-size memory region:

```bash
# Code cache size
-XX:ReservedCodeCacheSize=240m   # Default
-XX:InitialCodeCacheSize=240m
```

The Code Cache is divided into:
- **Non-method**: Runtime stubs, adapters
- **Profiled**: C1-compiled code with profiling data
- **Non-profiled**: C2-compiled optimized code

When the Code Cache fills up, methods are deoptimized and fall back to the interpreter.

## Deoptimization

Deoptimization occurs when a compiled method's assumptions are violated:

- A class is loaded that wasn't seen during compilation
- A virtual call site sees a new type
- An inline cache misses

The JVM maps the compiled frame back to the interpreter frame and continues execution in the interpreter. The method may be recompiled later with updated assumptions.

```bash
# Monitor deoptimization
-XX:+PrintDeoptimization
-XX:+PrintCompilation
```

## Key Source Files

| File | Purpose |
|------|---------|
| `src/hotspot/share/opto/` | C2 compiler (ideal graph, optimizations) |
| `src/hotspot/cpu/*/c1_LIRAssembler.cpp` | C1 LIR code generation |
| `src/hotspot/share/compiler/` | Compiler infrastructure |
| `src/hotspot/share/code/codeCache.cpp` | Code cache management |
| `src/hotspot/share/opto/deoptimization.cpp` | Deoptimization logic |
