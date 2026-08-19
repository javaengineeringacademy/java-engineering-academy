# 07. JIT Compilation - Decision Guide

## When to Study This Topic

| Scenario | Priority |
|----------|----------|
| Optimizing application startup time | **Must** |
| Debugging deoptimization issues | **Should** |
| Understanding why measured performance differs from expected | **Should** |
| Tuning compilation thresholds for specific workloads | **Should** |
| Understanding tiered compilation behavior | **Should** |
| Simple applications with default settings | **Nice to have** |

## When This Knowledge is Essential

- **Performance tuning**: JIT compilation is the primary reason Java approaches native performance
- **Startup optimization**: Understanding C1 vs C2 compilation helps tune startup
- **Deoptimization debugging**: Polymorphic call sites can cause sudden performance drops
- **Warm-up measurement**: JIT effects mean benchmarks must warm up before measuring
- **Code Cache management**: Insufficient code cache degrades performance

## Key Decision Points

| Decision | JIT Knowledge Impact |
|----------|---------------------|
| Tiered compilation levels | Balances startup vs peak performance |
| Compilation thresholds | Controls when methods get compiled |
| Inlining decisions | Affects both performance and code cache usage |
| Code cache sizing | Prevents JIT from running out of space |
