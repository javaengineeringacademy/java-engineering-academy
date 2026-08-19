# 07. JIT Compilation Internals Deep Dive

## JIT Compilation Pipeline

### Compilation Flow

```
1. Interpreter Execution
   ├── Bytecode executed line-by-line
   ├── Profiling data collected
   └── Hot method detection (invocation counter)

2. C1 Compilation (Client)
   ├── Fast compilation (< 100ms)
   ├── Basic optimizations:
   │   ├── Method inlining (small methods)
   │   ├── Constant folding
   │   ├── Dead code elimination
   │   └── Basic escape analysis
   └── Profiling data preserved for C2

3. C2 Compilation (Server)
   ├── Slower compilation (100ms-1s)
   ├── Aggressive optimizations:
   │   ├── Full escape analysis
   │   ├── Loop optimizations (unrolling, vectorization)
   │   ├── Lock coarsening/elimination
   │   ├── Null check elimination
   │   └── Range check elimination
   └── Native code emitted

4. Code Cache
   ├── Stores compiled native code
   ├── Divided into: Non-method, Profiled, Non-profiled
   └── Managed by JIT compiler
```

### Tiered Compilation Levels

```
Level 0: Interpreter
├── No compilation
├── Profiling data collected
└── Fast startup

Level 1: C1 (no profiling)
├── Basic C1 compilation
├── No profiling overhead
└── For methods known to be simple

Level 2: C1 (limited profiling)
├── C1 compilation with basic profiling
└── For methods with some polymorphism

Level 3: C1 (full profiling)
├── C1 compilation with full profiling
├── Monomorphic call site detection
└── Last level before C2

Level 4: C2 (fully optimized)
├── Aggressive C2 compilation
├── All optimizations applied
└── Peak performance
```

### Optimization Techniques

```
Method Inlining:
├── Replaces method call with method body
├── Eliminates call overhead (push/pop frames)
├── Enables further optimizations
├── Limited by method size (MaxInlineSize, FreqInlineSize)
└── Aggressive inlining for hot methods

Escape Analysis:
├── Determines if objects escape the method
├── Non-escaping objects can be:
│   ├── Allocated on the stack (no GC pressure)
│   ├── Eliminated entirely (scalar replacement)
│   └── Locks can be eliminated
└── Requires whole-method analysis

Loop Optimizations:
├── Loop unrolling: Reduce branch overhead
├── Loop inversion: Convert while to do-while
├── Loop vectorization: Use SIMD instructions
├── Bounds check elimination: Remove redundant checks
└── Loop peeling: Separate first iteration

Lock Optimizations:
├── Lock coarsening: Merge adjacent synchronized blocks
├── Lock elimination: Remove unnecessary locks
├── Biased locking: Avoid CAS for single-threaded access
└── Adaptive spinning: Busy-wait before blocking
```

### Deoptimimization

```
Deoptimization Triggers:
├── New class loaded (affects virtual call profiling)
├── Monomorphic call site becomes polymorphic
├── Full optimization assumption violated
├── Stack walking required (exception, GC)
└── Class redefinition (JVMTI)

Deoptimization Process:
├── Compiled code marked as invalid
├── Execution falls back to interpreter
├── Frame reconstructed from compiled code
├── Profiling data preserved for recompilation
└── Method may be recompiled later

Impact:
├── Brief pause during deoptimization
├── Performance drop until recompilation
└── Can be monitored with -XX:+PrintCompilation
```

### Code Cache Management

```
Code Cache Layout:
├── Non-method Code Cache (~5MB)
│   ├── Adapter handlers
│   ├── Buffer blobs
│   └── Stub routines
├── Profiled Code Cache (~122MB)
│   ├── C1-compiled methods
│   └── Profiling data
└── Non-profiled Code Cache (~122MB)
    ├── C2-compiled methods
    └── Fully optimized code

Code Cache Sizing:
├── -XX:ReservedCodeCacheSize=240MB (default)
├── -XX:InitialCodeCacheSize=256KB
├── Too small: JIT stops compiling (methods interpret)
└── Too large: Wasted memory
```
