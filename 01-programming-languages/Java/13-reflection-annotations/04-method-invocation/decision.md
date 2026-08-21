# Decision: Method Invocation

## When to Use Method Invocation

**Use when:**
- Building frameworks that call user-defined methods (JUnit, Spring)
- Implementing command patterns with dynamic dispatch
- Creating AOP proxies that intercept method calls
- Building serialization frameworks that invoke getters/setters
- Writing test utilities that call private methods

**Avoid when:**
- You know the method at compile time — use direct calls
- Performance is critical — MethodHandle is faster
- Simple delegation — interfaces are better

## Decision Matrix

| Need | Approach | Performance |
|------|----------|-------------|
| Call known method | Direct invocation | Fastest |
| Call by name at runtime | `Method.invoke()` | Slow (10-50x) |
| High-perf dynamic dispatch | `MethodHandle` | Near-direct |
| Intercept all calls | Dynamic Proxy | Slow but flexible |
| Call private method | `setAccessible(true)` + invoke | Slow |

## Cost-Benefit Analysis

```
Do you know the method at compile time?
├── YES → Use direct invocation
└── NO  → Is this a framework/plugin use case?
          ├── YES → Use Method.invoke() with caching
          └── NO  → Can you use MethodHandle?
                    ├── YES → Prefer MethodHandle
                    └── NO  → Use Method.invoke(), document why
```
