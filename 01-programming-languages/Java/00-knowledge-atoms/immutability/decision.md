# Decision Guide: Immutability

## When to Use
- Value objects that are shared across threads (no synchronization needed)
- Cache keys and map keys (hashCode never changes)
- DTOs and configuration objects (simplifies reasoning)
- Security-sensitive objects (credentials, tokens)
- String-like objects where concatenation creates new instances

## When NOT to Use
- Large objects with frequent state changes (matrix operations, game state)
- Performance-critical paths where object creation overhead is unacceptable
- Objects with large mutable backing arrays that would need defensive copying

## Trade-offs
| Aspect | Immutable | Mutable |
|--------|-----------|---------|
| Thread Safety | Inherent | Requires synchronization |
| Caching | Safe | Risky |
| Debugging | Easier (consistent state) | Harder (state changes) |
| Object Creation | New instance per change | In-place modification |
| Memory | Higher (more objects) | Lower (reuse) |

## Expert Recommendation
Default to immutability for value objects. Use `record` types (Java 14+) for data carriers. For mutable performance-critical objects, document thread-safety guarantees explicitly.
