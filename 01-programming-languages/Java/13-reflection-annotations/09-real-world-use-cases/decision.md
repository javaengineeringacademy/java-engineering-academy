# Decision: Real-World Use Cases

## When to Apply Reflection Knowledge

**Use when:**
- Debugging framework behavior (Spring, Hibernate, JUnit)
- Choosing between reflection and code generation
- Designing APIs that work with unknown types
- Building custom frameworks or libraries
- Understanding performance implications of framework choices

## Framework Selection Guide

| Framework | Reflection Use | Performance Impact | Alternative |
|-----------|---------------|-------------------|-------------|
| Spring | Heavy (DI, AOP) | Startup cost, runtime caching | Manual DI |
| Hibernate | Field access, proxies | Lazy loading overhead | Eager loading |
| Jackson | Field/getter mapping | Per-request reflection | Code generation |
| JUnit | Test discovery | Per-run | Manual test registration |
| Lombok | None (compile-time) | Zero runtime cost | None needed |
| Dagger 2 | None (compile-time) | Zero runtime cost | None needed |

## Architecture Decisions

```
Should you use a reflection-heavy framework?
├── YES → Can you tolerate the startup cost?
│         ├── YES → Use the framework (productivity wins)
│         └── NO  → Consider compile-time alternatives (Dagger 2, Lombok)
└── NO  → Do you need to work with unknown types?
          ├── YES → Use reflection with proper caching
          └── NO  → Use direct access for performance
```
