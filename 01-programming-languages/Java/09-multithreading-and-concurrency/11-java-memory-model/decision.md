# Java Memory Model Decision Guide

## When to Use Each Guarantee

| Guarantee | Mechanism |
|-----------|-----------|
| Visibility | `volatile`, `synchronized`, `final` fields |
| Ordering | `volatile`, `synchronized`, `happens-before` |
| Atomicity | `synchronized`, `Atomic*` classes |

## Common JMM Pitfalls

| Pitfall | Cause | Fix |
|---------|-------|-----|
| Stale reads | No synchronization | Use `volatile` or `synchronized` |
| Instruction reordering | Compiler/CPU optimization | Use happens-before guarantees |
| Non-atomic 64-bit reads | No atomicity for long/double | Use `volatile` or `AtomicLong` |
