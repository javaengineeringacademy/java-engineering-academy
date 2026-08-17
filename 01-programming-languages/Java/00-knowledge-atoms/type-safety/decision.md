# Decision Guide: Type Safety

## When to Use
- Always — use generics to enforce type safety at compile time
- Use `instanceof` or pattern matching before casting
- Use sealed classes to restrict class hierarchies and enable exhaustive switches

## When to Worry
- Raw types bypass compile-time checks — avoid them
- Type erasure means generic types are not available at runtime
- Casting after type erasure can cause ClassCastException

## Trade-offs
| Approach | Safety | Performance | Flexibility |
|----------|--------|-------------|-------------|
| Generics | Compile-time checks | No overhead (erased) | Less flexibility |
| Raw types | No checks | No overhead | Maximum flexibility |
| instanceof | Runtime check | Slight overhead | Full flexibility |
| Sealed classes | Exhaustive switch | No overhead | Restricted hierarchy |

## Expert Recommendation
Always use generics. Avoid raw types. Use pattern matching (Java 16+) to simplify instanceof + cast. Use sealed classes (Java 17+) for restricted hierarchies.
