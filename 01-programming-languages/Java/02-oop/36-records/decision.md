# Decision Guide: Records

## When to Use
- Immutable data carriers (DTOs, API responses, config holders)
- Tuple-like returns from methods
- Map entries and cache entries
- Pattern matching targets (Java 21+ sealed + records)
- Value objects with automatic equals/hashCode/toString

## When NOT to Use
- Entities with mutable state (use classes)
- JPA/Hibernate entities (need setters, no-arg constructors)
- When you need inheritance (records are implicitly final)
- When you need custom equals/hashCode (records don't allow it)
- When you need no-arg constructors for serialization frameworks

## Trade-offs

| Aspect | Record | Class with Lombok @Value | Manual Class |
|--------|--------|-------------------------|--------------|
| Boilerplate | None | Low | High |
| Immutability | Enforced | Enforced | Manual |
| Pattern matching | Built-in (21+) | No | No |
| Custom equals/hashCode | Not allowed | Allowed | Allowed |
| Inheritance | Not allowed | Not allowed | Allowed |
| No-arg constructor | Not allowed | Not allowed | Allowed |

## Expert Recommendation
Use records as the default for immutable data carriers. They eliminate boilerplate, enforce immutability, and integrate with pattern matching. Switch to classes only when you need mutability, inheritance, no-arg constructors, or custom equality logic.
