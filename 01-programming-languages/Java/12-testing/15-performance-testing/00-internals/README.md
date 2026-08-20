# Performance Testing - Internals

## JMH Execution Flow

```
Benchmark Class
    ↓
Code Generation (annotation processing)
    ↓
Fork JVM (if configured)
    ↓
Warmup Phase
    ↓
Measurement Phase
    ↓
Result Collection
    ↓
Statistical Analysis
    ↓
Report Generation
```

## JIT Optimization Prevention

JMH prevents common JIT optimizations:

1. **Dead code elimination**: Return or consume values
2. **Constant folding**: Use @State and variable inputs
3. **Loop unrolling**: Use @State for iteration-independent code
4. **Inlining**: Use @CompilerControl

## Statistical Analysis

JMH calculates:
- Mean execution time
- Standard deviation
- Confidence intervals
- Outlier detection

## Fork Isolation

Each fork creates a new JVM:
- Clean JIT state
- Isolated memory
- Reproducible results
