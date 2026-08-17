# Decision Guide: Value Objects

## When to Use
- Representing domain concepts like Money, Email, Address, Coordinate
- DTOs and API request/response payloads
- Map keys and collection elements that need value-based equality
- Parameters where immutability prevents accidental mutation
- Thread-safe sharing without synchronization

## When NOT to Use
- Entities with lifecycle and identity (e.g., User, Order with DB id)
- Objects requiring mutable state after construction
- JPA/Hibernate entities (need setters and no-arg constructors)
- Objects with complex mutable graph relationships

## Trade-offs

| Aspect | Value Object (record) | Value Object (class) | Mutable Class |
|--------|----------------------|---------------------|---------------|
| Immutability | Enforced | Manual enforcement | No |
| Boilerplate | Minimal | High (equals, hashCode, toString) | Medium |
| Validation | Compact constructor | Constructor | Setters |
| Thread safety | Inherently safe | Safe if immutable | Requires sync |
| JPA support | No | No | Yes |

## Expert Recommendation
Use records for simple value objects (Point, Money, Email). Use final classes with manual equals/hashCode when you need complex validation or behavior. Never use mutable classes as value objects — they defeat the purpose and introduce subtle bugs in collections and concurrent code.
