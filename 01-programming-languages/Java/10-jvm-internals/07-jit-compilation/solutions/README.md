# 07. JIT Compilation - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | JIT compilation observation |
| `Solution2.java` | Warm-up benchmark |
| `Solution3.java` | Deoptimization detection |

## Running Solutions

```bash
java -XX:+PrintCompilation Solution1
java -XX:+PrintCompilation Solution2
java -XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining Solution3
```

## Common Mistakes to Avoid

1. **Benchmarking without warm-up**: First invocations are interpreted, not compiled
2. **Code cache too small**: Causes performance cliff when JIT stops compiling
3. **Polymorphic call sites**: Too many implementations cause deoptimization
4. **Ignoring -server flag**: Default may be -client on some platforms
