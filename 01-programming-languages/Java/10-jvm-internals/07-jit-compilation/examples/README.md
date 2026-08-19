# 07. JIT Compilation - Examples

## Example Files

| File | Description |
|------|-------------|
| `JitDemo.java` | Basic JIT compilation demonstration with profiling |
| `JitOptimizations.java` | Shows JIT optimization techniques (inlining, escape analysis) |
| `ExecutionEngine.java` | Interpreter vs JIT compiled performance comparison |

## Running Examples

```bash
# Basic JIT demo
java -XX:+PrintCompilation JitDemo

# Show inlining decisions
java -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining JitDemo

# Escape analysis
java -XX:+UnlockDiagnosticVMOptions -XX:+PrintEscapeAnalysis JitOptimizations

# Performance comparison
java -XX:+PrintCompilation ExecutionEngine
```

## Key Concepts Demonstrated

- JIT compilation events and tiers
- Method inlining decisions
- Escape analysis and stack allocation
- Tiered compilation flow
- Code cache behavior
